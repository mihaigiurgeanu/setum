package ro.kds.erp.reports;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import java.rmi.RemoteException;
import javax.ejb.EJBException;
import ro.kds.erp.biz.ResponseBean;
import javax.ejb.CreateException;
import org.objectweb.util.monolog.api.BasicLevel;
import org.objectweb.util.monolog.Monolog;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.HashMap;
import org.xml.sax.InputSource;
import java.io.StringReader;
import java.io.StringWriter;
import javax.naming.Context;
import org.objectweb.util.monolog.api.Logger;
import java.io.Writer;
import java.util.Map;
import java.io.File;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.Configuration;
import freemarker.ext.dom.NodeModel;
import java.io.IOException;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import freemarker.template.TemplateException;
import ro.kds.erp.biz.PreferencesHome;
import ro.kds.erp.biz.Preferences;
import javax.rmi.PortableRemoteObject;


/**
 * Describe class FTLTransformerBean

n here.
 *
 *
 * Created: Wed Jan 24 18:13:18 2007
 *
 * @author <a href="mailto:Mihai Giurgeanu@MIHAIG">U-MIHAIG\Mihai Giurgeanu</a>
 * @version 1.0
 */
public class FTLTransformerBean implements SessionBean {
    static protected Logger logger = null;

    /**
     * Holds the freemarker global configuration. It is initialized 
     * by the <code>setSessionContext</code> call.
     */
    private Configuration cfg;


    /**
     * The name of the environment key under which is stored the path to folder where templates are located.
     */
    protected final String TEMPLATES_DIRECTORY_KEY = "TemplatesLocation";

    /**
     * Create a new session bean
     */
    public void ejbCreate() throws CreateException {
	logger.log(BasicLevel.DEBUG, "");
    }



    // Implementation of javax.ejb.SessionBean

    public void setSessionContext(SessionContext sessionContext) throws EJBException, RemoteException {

        if (logger == null) {
            logger = Monolog.getMonologFactory().getLogger("ro.kds.erp.reports.FTLTransformerBean");
        }
        logger.log(BasicLevel.DEBUG, "");


	try {
	    Context ic = new InitialContext();
	    Context env = (Context)ic.lookup("java:comp/env");

	    PreferencesHome ph = (PreferencesHome)PortableRemoteObject
		.narrow(env.lookup("ejb/PreferencesHome"), PreferencesHome.class);
	    Preferences prefs = ph.create();

	    String dirName = prefs.get((String)env.lookup(TEMPLATES_DIRECTORY_KEY), File.separator);
	    logger.log(BasicLevel.INFO, "Freemarker config: DirectoryForTemplateLoading=" + dirName);

	    this.cfg = new Configuration();
	    this.cfg.setDirectoryForTemplateLoading(new File(dirName));
	    this.cfg.setObjectWrapper(new DefaultObjectWrapper());

	} catch (RemoteException e) {
	    logger.log(BasicLevel.ERROR, "RemoteException while configuring the freemarker engine: " + e.getMessage());
	    logger.log(BasicLevel.DEBUG, e);
	    throw new EJBException(e);
	} catch (NamingException e) {
	    logger.log(BasicLevel.ERROR, "NamingException while setting configuration for freemarker engine: " + e.getMessage());
	    logger.log(BasicLevel.DEBUG, e);
	    throw new EJBException(e);
	} catch (IOException e) {
	    logger.log(BasicLevel.ERROR, "IOException while configuring templates location: " + e.getMessage());
	    logger.log(BasicLevel.DEBUG, e);
	    throw new EJBException(e);
	} catch (CreateException e) {
	    logger.log(BasicLevel.ERROR, "CreateException while trying to configure the freemarker enging: " + e.getMessage());
	    logger.log(BasicLevel.DEBUG, e);
	    throw new EJBException(e);
	}


    }

    public void ejbRemove() throws EJBException, RemoteException {

        logger.log(BasicLevel.DEBUG, "");
    }

    public void ejbPassivate() throws EJBException, RemoteException {

        logger.log(BasicLevel.DEBUG, "");
    }

    public void ejbActivate() throws EJBException, RemoteException {

        logger.log(BasicLevel.DEBUG, "");
    }


    // Business methods


    /**
     * Executes a freemarker template and transforms the xml represantation of the 
     * <code>ResponseBean</code> into
     * a string represantation according to the freemarker template. The template will
     * receive the <code>ResponseBean</code>.
     *
     * @param r is the <code>ResponseBean</code> to be processed by the template.
     * @param templateName is the name of the template file. The folder to look for the
     * template file is given in the environment variable <code>TemplatesLocation</code>.
     */
    public String transform(ResponseBean r, String templateName) throws TransformerException {

	try {
	    Template template = this.cfg.getTemplate(templateName);
	    Map root = new HashMap();
	    root.put("doc",NodeModel.parse(new InputSource(new StringReader(r.toXML()))));
	    
	    Writer out = new StringWriter();
	    template.process(root, out);
	    out.flush();
	    return out.toString();
	}
	catch (IOException e) {
	    logger.log(BasicLevel.ERROR, "IOException while processing the template " + templateName
		       + ": " + e.getMessage());
	    logger.log(BasicLevel.DEBUG, e);
	    throw new TransformerException("IOException:" + e.getMessage(), e);
	}
	catch (SAXException e) {
	    logger.log(BasicLevel.WARN, "SAXException while parsing the ResponseBean's resulting xml: "
		       + e.getMessage());
	    logger.log(BasicLevel.DEBUG, e);
	    throw new TransformerException("SAXException:" + e.getMessage(), e);
	}
	catch (ParserConfigurationException e) {
	    logger.log(BasicLevel.ERROR, "ParserConfigurationException while parsing the ResponseBean's resulting xml: "
		       + e.getMessage());
	    logger.log(BasicLevel.DEBUG, e);
	    throw new TransformerException("ParserConfigurationException:" + e.getMessage(), e);
	}
	catch (TemplateException e) {
	    logger.log(BasicLevel.WARN, "TemplateException while processing the template " 
		       + templateName + ": " + e.getMessage());
	    logger.log(BasicLevel.DEBUG, e);
	    throw new TransformerException("TemplateException:" + e.getMessage(), e);
	}
    }

}
