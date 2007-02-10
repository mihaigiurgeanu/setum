package ro.kds.erp.reports;

import javax.servlet.FilterChain;
import javax.servlet.ServletResponse;
import javax.servlet.ServletRequest;
import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.Writer;
import org.apache.fop.apps.FopFactory;
import java.io.StringWriter;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Source;
import org.xml.sax.InputSource;
import java.io.StringReader;
import javax.xml.transform.Transformer;
import org.apache.fop.apps.Fop;
import javax.xml.transform.sax.SAXSource;
import org.apache.fop.apps.FOPException;
import org.objectweb.util.monolog.api.BasicLevel;
import org.objectweb.util.monolog.Monolog;
import org.objectweb.util.monolog.api.Logger;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.Result;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.naming.InitialContext;
import ro.kds.erp.biz.PreferencesHome;
import javax.rmi.PortableRemoteObject;
import javax.naming.NamingException;
import javax.naming.Context;
import java.io.File;
import ro.kds.erp.biz.Preferences;
import java.rmi.RemoteException;
import org.xml.sax.SAXException;
import javax.ejb.CreateException;

/**
 * Filter that intercepts the response of a servlet and procees it with
 * FOP processor. The intercepted output must be an XSLFO document.
 *
 * To use this filter you must set  the init parameter <code>mime</code>
 * to the desired output mime type (i.e. for PDF output, the <code>mime</code> init
 * parameter must be <code>application/pdf</code>. The mime type must
 * be one that is supported by the FOP processor.
 *
 * Created: Sat Jan 27 11:13:15 2007
 *
 * @author <a href="mailto:Mihai Giurgeanu@MIHAIG">U-MIHAIG\Mihai Giurgeanu</a>
 * @version 1.0
 */
public class FOPFilter implements Filter {


    /**
     * The name of the init parameter that contains the mime type
     * of the output requested by the FOP processor. It should
     * be one of the types supported by the FOP processor.
     *
     */
    private final String MIMETYPE_PARAM = "mime";

    /**
     * The environment key for the path of the FOP configuration file.
     */
    private final String FOP_CONFIG_FILE = "FOPConfigurationFile";

    /**
     * The configured mime type of the requested output.
     */
    private String cfgMimeType;


    /**
     * Logging object.
     */
    private Logger logger;

    /**
     * Caches a reference to a <code>FopFactory</code> object.
     */
    FopFactory factory;


    // Implementation of javax.servlet.Filter

    /**
     * Filter initialization.
     *
     * @param config a <code>FilterConfig</code> value
     * @exception ServletException if an error occurs
     */
    public void init(FilterConfig config) throws ServletException {
	logger = Monolog.getMonologFactory().getLogger("ro.kds.erp.reports.XSLRendererServlet");
	logger.log(BasicLevel.DEBUG, "");

	cfgMimeType = config.getInitParameter(MIMETYPE_PARAM);

	factory = FopFactory.newInstance();

	try {
	    InitialContext ic = new InitialContext();
	    Context env = (Context)ic.lookup("java:comp/env");
	    
	    PreferencesHome ph = (PreferencesHome)PortableRemoteObject.
		narrow(env.lookup("ejb/PreferencesHome"), PreferencesHome.class);
	    Preferences p = ph.create();

	    String envKeyFileName = (String)env.lookup(FOP_CONFIG_FILE);
	    logger.log(BasicLevel.DEBUG, "Reading the path of the FOP configuration file from the preferences key: " + envKeyFileName);

	    String configFileName = p.get(envKeyFileName, File.separator);
	    logger.log(BasicLevel.INFO, "Reading FOP configuration from: " + configFileName);

	    factory.setUserConfig(new File(configFileName));
	}
	catch(NamingException e) {
	    logger.log(BasicLevel.ERROR, "NamingException while getting the name of the FOP configuration file: " + e.getMessage());
	    logger.log(BasicLevel.DEBUG, e);
	}
	catch(RemoteException e) {
	    logger.log(BasicLevel.WARN, "RemoteException while getting the name of the FOP configuration file: " + e.getMessage());
	    logger.log(BasicLevel.DEBUG, e);
	}
	catch(SAXException e) {
	    logger.log(BasicLevel.ERROR, "Error reading the FOP configuration file: " + e.getMessage());
	    logger.log(BasicLevel.DEBUG, e);
	}
	catch(CreateException e) {
	    logger.log(BasicLevel.WARN, "CreateException while creating a PreferenceBean to read the path of the FOP configuraion file: "
		       + e.getMessage());
	    logger.log(BasicLevel.DEBUG, e);
	}
	catch(IOException e) {
	    logger.log(BasicLevel.ERROR, "Can not read FOP configuration file: " + e.getMessage());
	    logger.log(BasicLevel.DEBUG, e);
	}
    }

    /**
     * Describe <code>destroy</code> method here.
     *
     */
    public void destroy() {

    }

    /**
     * Transforms the XSLFO response of the servlet through the FOP
     * processing. It does that by wraping the <code>response</code>
     * object into a customized <code>ServletResponseWrapper</code> and
     * intercepting the response.
     *
     * @param request a <code>ServletRequest</code> value
     * @param response a <code>ServletResponse</code> value
     * @param chain a <code>FilterChain</code> value
     * @exception IOException if an error occurs
     * @exception ServletException if an error occurs
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

	logger.log(BasicLevel.DEBUG, "FOPFilter called for mime " + cfgMimeType);
	
	try {

	    Fop fop = factory.newFop(cfgMimeType, response.getOutputStream());
	    TransformerFactory transformerFactory = TransformerFactory.newInstance();
	    Transformer transformer = transformerFactory.newTransformer(); // identity transformer
	    

	    StringWriter xslWriter = new StringWriter();
	    chain.doFilter(request, new XSLServletResponse((HttpServletResponse)response, xslWriter));
	    Source src = new SAXSource(new InputSource(new StringReader(xslWriter.toString())));

	    response.setContentType(cfgMimeType);
	    Result res = new SAXResult(fop.getDefaultHandler());
	    transformer.transform(src, res);
	} catch (FOPException e) {
	    logger.log(BasicLevel.WARN, "FOP transformation failed: " + e.getMessage());
	    logger.log(BasicLevel.DEBUG, e);
	    throw new ServletException("FOPException: " + e.getMessage(), e);
	} catch (TransformerConfigurationException e) {
	    logger.log(BasicLevel.ERROR, "XSLT identity transformer could not be instantiated due to configuration error: " + e.getMessage());
	    logger.log(BasicLevel.DEBUG, e);
	    throw new ServletException("TransformerConfigurationException: " + e.getMessage());
	} catch (javax.xml.transform.TransformerException e) {
	    logger.log(BasicLevel.WARN, "The transformation of xsl to fop result failed: " + e.getMessage());
	    logger.log(BasicLevel.DEBUG, e);
	    throw new ServletException("TransformerException: " + e.getMessage());
	}

    }

}



/**
 * A <code>ServletWrapper</code> to intercept the response content of the
 * servlet. When instantiating a new object of this class, the constructor
 * receives a <code>Writer</code> object. This object will be returned
 * wher calling <code>getWriter</code> method of the response wrapper.
 */
class XSLServletResponse extends HttpServletResponseWrapper {
    private PrintWriter writer;

    /**
     * @param response is the original <code>HttpServletReponse</code> to be wrapped.
     * @param writer is a <code>java.io.Writer</code> object that will be used
     * to intercept the output of the servlet.
     */
    public XSLServletResponse(HttpServletResponse response, Writer writer) {
	super(response);
	this.writer = new PrintWriter(writer);
    }

    /**
     * Overwrites the <code>getWriter</code> method to substitute the original
     * <code>PrintWriter</code> object used to send the response to the client
     * with a custom one that will be used to intercept the response from the
     * servlet.
     *
     * @return a <code>PrintWriter</code> value
     * @exception IOException if an error occurs
     */
    public PrintWriter getWriter() throws IOException {
	return this.writer;
    }
  
}
