package ro.kds.erp.web;

import javax.servlet.ServletException;
import org.objectweb.util.monolog.api.BasicLevel;
import org.objectweb.util.monolog.api.Logger;
import org.objectweb.util.monolog.Monolog;
import java.util.HashMap;
import org.apache.commons.beanutils.ConvertUtils;
import ro.kds.erp.utils.DateConverter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import javax.ejb.EJBObject;
import ro.kds.erp.biz.ResponseBean;
import java.io.PrintWriter;
import javax.naming.NamingException;
import javax.ejb.CreateException;
import java.rmi.RemoteException;
import java.lang.reflect.InvocationTargetException;
import javax.naming.InitialContext;
import javax.ejb.EJBHome;
import javax.rmi.PortableRemoteObject;
import java.util.Map.Entry;
import javax.naming.Context;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;
import java.util.Date;
import javax.servlet.http.HttpServlet;

/**
 * This is a trimed down version of the <code>CallDispatcher</code> that will
 * be used for a less verbose api of client server communications. The only
 * commands recognized by this dispatcher are:
 *
 * <dl>
 * <dt>change</dt>
 * <dd>The <code>field</code> http parameter of the request contains the
 * name of the field that would be updated and the <code>value</code>
 * parameter contains the <code>String</code> representation of the new value
 * to be put on that field. The servlet will look in the current
 * session bean for a method called 
 * <code>updateField</code>, and invokes the method by converting the
 * <code>value</code> parameter to the type of the method's parameter.
 * </dd>
 *
 * <dt>Reflection commands</dt>
 * <dd>The current session bean is retrieved. It is searched for a method
 * called as the <code>command</code> parameter. For each of its parameters
 * the values are looked in the current request and converted to the 
 * parameter types found in the method. The method is then invoked with these
 * parameters.
 * </dd>
 *
 * </dl>
 *
 * Parameter <code>operation</code> contains other additional operation commands related
 * mostly with the context of command execution. The operations that may be invoked are:
 * <dl>
 * <dt>new-context
 * <dd>When a <code>new-context</code> operation is received, a new session bean is created
 * prior to executing the command.
 * 
 * <dt>close-context
 * <dd>When a <code>close-context</code> operation is received, the session bean is removed
 * after executing the command.
 * </dl>
 *
 * Created: Tue Mar 14 17:09:40 2006
 *
 * @author <a href="mailto:Mihai Giurgeanu@CRIMIRA"></a>
 * @version $Id: SimpleCallDispatcherServlet.java,v 1.4 2006/09/27 14:43:12 mihai Exp $
 */
public class SimpleCallDispatcherServlet extends HttpServlet {


    private Logger logger = null;

    /**
     * The HttpRequest session attribute containing the session bean. It is
     * read from servlet's init parameter with the name "sessionName".
     */
    private String SESSION_ATTR;

    /**
     * The JNDI location of the session bean's home. It is read from the
     * servlet's init parameter "sessionBean".
     */
    private String SESSION_JNDI;

    /**
     * A value of <code>true</code> will inhibit the automatic creation of bean
     * whenever the bean is empty. The value of this variable is read from the
     * servlet's init parameter with the name "explicitBeanCreation".
     */
    private boolean EXPLICIT_BEAN_CREATION;

    /**
     * The map of the methods in the session bean's class.
     */
    private Map methods;

    /**
     * The home class for instantiating the session bean.
     */
    private Class homeInterface;

    /**
     * Servlet initialization.
     *
     * @exception ServletException if an error occurs
     */
    public final void init() throws ServletException {
	logger = Monolog.getMonologFactory().getLogger("ro.kds.erp.web.SimpleCallDispatcherServlet");
        logger.log(BasicLevel.DEBUG, "");

	try {
	    SESSION_ATTR = getServletConfig().getInitParameter("sessionName");
	    SESSION_JNDI = getServletConfig().getInitParameter("sessionBean");
	    
	    String explicitBeanCreation = getServletConfig().getInitParameter("explicitBeanCreation");
	    if(explicitBeanCreation != null && explicitBeanCreation.equals("true")) {
		EXPLICIT_BEAN_CREATION = true;
	    } else {
		EXPLICIT_BEAN_CREATION = false;
	    }

	    Class beanClass = Class.forName(getServletConfig()
					    .getInitParameter("sessionClass"));
	    methods = new HashMap();
	    
	    Method[] businessMethods = beanClass.getMethods();
	    for(int i=0; i<businessMethods.length; i++) {
		methods.put(businessMethods[i].getName(), 
			    businessMethods[i]);
	    }

	    homeInterface = Class.forName(getServletConfig().
					  getInitParameter("sessionHome"));

	    // Converter initialization
	    ConvertUtils.register(new ro.kds.erp.utils.DateConverter(),
				  java.util.Date.class);
	    
	} catch (Exception e) {
	    logger.log(BasicLevel.ERROR, "Servlet can not be initialized", e);
	    throw new ServletException(e);
	}
    }



    /**
     * Servlet's entry point.
     *
     * @param request a <code>HttpServletRequest</code> value
     * @param response a <code>HttpServletResponse</code> value
     * @exception ServletException if an error occurs
     * @exception IOException if an error occurs
     */
    public void doPost(HttpServletRequest request, 
		       HttpServletResponse response) 
	throws ServletException, IOException {

	logger.log(BasicLevel.DEBUG, "Component's path: " + 
		   request.getRequestURI());
	ResponseBean r;
	String operation = request.getParameter("operation");

	try {
	    EJBObject bean;

	    if(operation != null && operation.equals("new-context")) {
		removeSessionBean(request);
		bean = newSessionBean(request);
	    } else {
		bean = getSessionBean(request);
	    }
	
	    String command = request.getParameter("command");

	    logger.log(BasicLevel.DEBUG, "Command: " + command);
	    {
		String params = "";
		for(Iterator i = request.getParameterMap().entrySet().iterator(); 
		    i.hasNext();) {
		
		    Map.Entry entry = (Map.Entry)i.next();
		    params += "<<" + entry.getKey() + ">> = ";
		    Object[] value = (Object []) entry.getValue();
		    for(int j=0; j<value.length; j++)
			params += "<<" + value[j] + ">> ";
		    params += "\n";
		}

		logger.log(BasicLevel.DEBUG, "Parameters: \n" + params);
	    }

	    if(command.equals("change")) {
		String field = request.getParameter("field");
		String value = request.getParameter("value");
		logger.log(BasicLevel.DEBUG, "Operation change for field <<" +
			   field + ">> with value <<" + value + ">>");
		if(bean == null) {
		    r = new ResponseBean();
		    r.setCode(4);
		    r.setMessage("Trebuie sa selectati mai intai o inregistrare. Modificarea nu a fost salvata!");
		    logger.log(BasicLevel.INFO, "Change operation called but no itemwas selected");
		} else {
		    try {
			String methodName = "update" + 
			    new Character(Character.toUpperCase(field.charAt(0)))
			    .toString() + field.substring(1);
			logger.log(BasicLevel.DEBUG, "Searching for method name "
				   + methodName);
		    
			Method m = (Method)methods.get(methodName);
			if(m == null) {
			    r = new ResponseBean(); // don't do anything
			    // just log an warning
			    logger.log(BasicLevel.WARN, "Method " + methodName
				       + " not found when trying to update field "
				       + field);
			} else {
			    Class[] paramsTypes = m.getParameterTypes();
			    logger.log(BasicLevel.DEBUG,
				       "Method "  + methodName + " has " +
				       paramsTypes.length + " parameters");

			    logger.log(BasicLevel.DEBUG, "Converting " + value +
				       " to type " + paramsTypes[0].getName());

			    Object params[] = new Object[1];
			    params[0] = ConvertUtils.convert(value, 
							     paramsTypes[0]);

			    logger.log(BasicLevel.DEBUG,
				       "Invoking " + methodName + 
				       " with one parameter: " + value
				       + "(" + params[0] + " - "
				       + params[0].getClass().getName()
				       + ")");
			    r = (ResponseBean)m.invoke(bean, params);
			}
		    } catch (Exception e) {
			r = new ResponseBean();
			r.setCode(1);
			r.setMessage("Valoarea <<" + value +
				     ">> nu a putut fi salvata.");
			logger.log(BasicLevel.ERROR, "Value <<" + value + 
				   ">> can not be saved for field: " + field, e);
		    }
		}
	    
	    } else {
		// other commands
		Method m = (Method)methods.get(command);
		if(m == null) {
		    r = new ResponseBean();
		    logger.log(BasicLevel.WARN, "Command " + command + 
			       " unknown.");
		} else {
		    logger.log(BasicLevel.DEBUG, "Method " + command + " found.");
		    Class paramTypes[] = m.getParameterTypes();
		    logger.log(BasicLevel.DEBUG, "Method " + command + 
			       " requires " + paramTypes.length + " parameters.");
		    Object params[] = new Object[paramTypes.length];
		    for(int i=0; i<paramTypes.length; i++) {
			// i expect the parameters with the names
			// param1, param2, ... .
			String param = request.getParameter("param" + i);
			if(param == null) {
			    params[i] = null;
			    logger.log(BasicLevel.WARN, "param" + i + " does not exist. Setting it to null when calling method " + command);
			} else {
			    params[i] = ConvertUtils.convert(param, paramTypes[i]);
			    logger.log(BasicLevel.DEBUG, "param" + i + 
				       " has value <<" + param +
				       ">>  when calling method " + command);
			}
		    }
		    try {
			r = (ResponseBean)m.invoke(bean, params);
		    } catch (Exception e) {
			r = new ResponseBean();
			r.setCode(1);
			r.setMessage("Eroare, operatia nu a putut fi efectuata");
			logger.log(BasicLevel.ERROR, "Error executing command " 
				   + command, e);
		    }
		}
	    }
	} catch (Exception e) {
	    logger.log(BasicLevel.ERROR, "Bean can not be initialized");
	    logger.log(BasicLevel.DEBUG, e);
	    r = ResponseBean.getErrUnexpected(e);
	}
	response.setContentType("text/xml");
	PrintWriter out = response.getWriter();
	String xml = r.toXML();
	logger.log(BasicLevel.DEBUG, "Sending the response: \n" + xml);
	out.print(xml);

	if(operation != null && operation.equals("close-context")) {
	    removeSessionBean(request);
	}
    }


    /**
     * Creates a new session bean and registers it into the current session.
     *
     * @param request is the current HttpServletRequest.
     */
    EJBObject newSessionBean(HttpServletRequest request) 
	throws NamingException, CreateException, RemoteException,
	       NoSuchMethodException, IllegalAccessException,
	       InvocationTargetException {

	InitialContext ic = new InitialContext();
	Context env = (Context)ic.lookup("java:comp/env");

	EJBHome beanHome = (EJBHome)PortableRemoteObject.narrow
	    (env.lookup(SESSION_JNDI), homeInterface);
	EJBObject bean = (EJBObject)homeInterface.getMethod("create", null)
	    .invoke(beanHome, null);
	request.getSession().setAttribute(SESSION_ATTR, bean);
	
	return bean;
    }

    /**
     * Removes the session bean object.
     *
     * @param request is the current HttpServletRequest.
     */
    private void removeSessionBean(HttpServletRequest request) {
	logger.log(BasicLevel.DEBUG, "Removing session bean " 
		   + SESSION_ATTR);
	try {
	    EJBObject bean = (EJBObject)request.getSession()
		.getAttribute(SESSION_ATTR);
	    if(bean != null) {
		bean.remove();
	    }
	} catch(Exception e) {
	    logger.log(BasicLevel.WARN, "Error removing the session bean", e);
	}
	request.getSession().removeAttribute(SESSION_ATTR);
    }

    /**
     * Get the current session bean from the servlet's HTTP session.
     */
    private EJBObject getSessionBean(HttpServletRequest request)
	throws NamingException, CreateException, RemoteException,
	       NoSuchMethodException, IllegalAccessException,
	       InvocationTargetException {

	EJBObject bean = (EJBObject)request.getSession().getAttribute(SESSION_ATTR);
	if(bean == null && !EXPLICIT_BEAN_CREATION) {
	    bean = newSessionBean(request);
	}
	return bean;
    }

}
