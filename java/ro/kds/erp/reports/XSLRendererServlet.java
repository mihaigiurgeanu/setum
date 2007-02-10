package ro.kds.erp.reports;

import javax.servlet.http.HttpServlet;
import javax.servlet.ServletException;
import org.objectweb.util.monolog.Monolog;
import org.objectweb.util.monolog.api.BasicLevel;
import org.objectweb.util.monolog.api.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletException;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import ro.kds.erp.biz.ResponseBean;
import java.lang.reflect.Method;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;
import javax.naming.NamingException;
import javax.ejb.CreateException;
import java.lang.reflect.InvocationTargetException;
import javax.naming.Context;
import java.io.PrintWriter;

/**
 * It renders a call to business logic form into XSLFO representation. This means that
 * it gets a reference to an object from the session and the name of a method with no
 * parameters and invokes that method on the object. The method should return a <code>ResponseBean</code>
 * object. Then this servlet uses <code>Transformer</code> ejb object to transform the
 * <code>ResponseBean</code> object into a string representation that would be the output
 * of the servlet.
 *
 * Note that the actual content of the output is not controlled by this servlet, but by the template
 * passed to the transformer, so the output of the serverlet is not guaranteed to be XSLFO.
 *
 * The servlet's parameters are: the key in the session that holds the object and the name of the method to
 * be invoked on the object, the reference to the <code>Transformer</code> that will do the processing
 * and the name of the template or stylesheet to be passed to the <code>Transformer</code>. The <code>Transformer</code>
 * will simply apply the template to the <code>ResponseBean</code> returned by the method invocation on the
 * object and will return a <code>String</code> that is the result of the application.
 *
 * The following init parameters should be provided:
 * <dl>
 * <dt>sessionName</dt>
 * <dd>The key in the http session associated with the object to execute the call that returns 
 * the <code>ResponseBean</code>.</dd>
 *
 * <dt>method</dt>
 * <dd>The name of the method to be called on the object.</dd>
 * 
 * <dt>transformer</dt>
 * <dd>The jndi name of the <code>Transformer</code> ejb to be used for processing the data.</dd>
 *
 * <dt>stylesheet</dt>
 * <dd>The name of the stylesheet (or template for freemarker) to be applied to data by the
 * <code>Transformer</code>.</dd>
 *
 * </dl>
 *
 * Created: Wed Jan 24 23:23:54 2007
 *
 * @author <a href="mailto:Mihai Giurgeanu@MIHAIG">U-MIHAIG\Mihai Giurgeanu</a>
 * @version 1.0
 */
public class XSLRendererServlet extends HttpServlet {

    /**
     * Name of init parameter
     */
    private final String SESSION_NAME="sessionName";
    /**
     * Name of init parameter
     */
    private final String METHOD_NAME="method";
    /**
     * Name of init parameter
     */
    private final String TRANSFORMER_REFERENCE="transformer";
    /**
     * Name of init parameter
     */
    private final String STYLESHEET_NAME="stylesheet";


    /**
     * The name of the session attribute holding the object. It
     * is read from servlet's init parameters.
     */
    private String sessionName;
    /**
     * The method to be called on the object. It
     * is read from servlet's init parameters.
     */
    private String method;
    /**
     * The transformer reference. It
     * is read from servlet's init parameters.
     */
    private String transformer;
    /**
     * The stylesheet name to be used by transformer. It
     * is read from servlet's init parameters.
     */
    private String stylesheet;


    /**
     * Transformer reference cache.
     */
    private TransformerLocal transformerBean;


    /**
     * The logger.
     */
    private Logger logger = null;

    /**
     * Servlet initialization routines. It reads the init parameters
     * and obtains a Transformer object.
     *
     * @exception ServletException if the servlet can not be
     * initialized.
     */
    public void init() throws ServletException {
	logger = Monolog.getMonologFactory().getLogger("ro.kds.erp.reports.XSLRendererServlet");
	logger.log(BasicLevel.DEBUG, "");

	sessionName = getServletConfig().getInitParameter(SESSION_NAME);
	method = getServletConfig().getInitParameter(METHOD_NAME);
	transformer = getServletConfig().getInitParameter(TRANSFORMER_REFERENCE);
	stylesheet = getServletConfig().getInitParameter(STYLESHEET_NAME);

	try {
	    InitialContext ic = new InitialContext();
	    Context env = (Context)ic.lookup("java:comp/env");
	    TransformerLocalHome th = (TransformerLocalHome)PortableRemoteObject
		.narrow(env.lookup(transformer), TransformerLocalHome.class);
	    transformerBean = th.create();
	} catch (NamingException e) {
	    logger.log(BasicLevel.ERROR, "NamingException while getting the home of the trasformer bean: " + e.getMessage());
	    logger.log(BasicLevel.DEBUG, e);
	    throw new ServletException(e);
	} catch (CreateException e) {
	    logger.log(BasicLevel.ERROR, "CreateException while creating a transformer bean: " + e.getMessage());
	    logger.log(BasicLevel.DEBUG, e);
	    throw new ServletException(e);
	}
    }

    /**
     * Executes the GET HTTP request. It performs the method call
     * on the configured object and uses the <code>Transformer</code>
     * to apply the stylesheet to the data. It returns to the client
     * the result of the transformation.
     *
     * @param request a <code>HttpServletRequest</code> value
     * @param response a <code>HttpServletResponse</code> value
     * @exception ServletException if an error occurs
     * @exception IOException if an error occurs
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	ResponseBean r;
	Object theBean;
	Method m;
	try {
	    theBean = request.getSession().getAttribute(sessionName);
	    m = theBean.getClass().getMethod(method, null);
	}
	catch (NoSuchMethodException e) {
	    logger.log(BasicLevel.ERROR, "The configured method does not exists for the bean: " + method);
	    logger.log(BasicLevel.DEBUG, e);
	    throw new ServletException(e);
	}
	catch (SecurityException e) {
	    logger.log(BasicLevel.ERROR, "SecurityException while invoking the method " + method);
	    logger.log(BasicLevel.DEBUG, e);
	    throw new ServletException(e);
	}

	try {
	    r = (ResponseBean)m.invoke(theBean, null);
	}
	catch(IllegalAccessException e) {
	    logger.log(BasicLevel.ERROR, "IllegalAccessException when calling method " + method);
	    logger.log(BasicLevel.DEBUG, e);
	    throw new ServletException(e);
	}
	catch(IllegalArgumentException e) {
	    logger.log(BasicLevel.ERROR, "IllegalArgumentException while calling method with no arguments: " + method);
	    logger.log(BasicLevel.DEBUG, e);
	    throw new ServletException(e);
	}
	catch(InvocationTargetException e) {
	    logger.log(BasicLevel.WARN, "InvocationTargetException while calling method: " + method);
	    logger.log(BasicLevel.DEBUG, e);
	    throw new ServletException(e);
	}
	catch(ExceptionInInitializerError e) {
	    logger.log(BasicLevel.WARN, "ExceptionInInitializerError while calling method " + method);
	    logger.log(BasicLevel.DEBUG, e);
	    throw new ServletException(e);
	}

	String xsl;
	try {
	    xsl = transformerBean.transform(r, stylesheet);
	} catch(TransformerException e) {
	    logger.log(BasicLevel.WARN, "The transformation of the ResponseBean failed within the Transformer.");
	    logger.log(BasicLevel.DEBUG, e);
	    throw new ServletException(e);
	}

	response.setContentType("application/xml");
	PrintWriter out = response.getWriter();
	logger.log(BasicLevel.DEBUG, "Sending the response: \n" + xsl);
	out.print(xsl);

    }


}
