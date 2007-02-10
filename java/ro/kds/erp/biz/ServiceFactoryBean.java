package ro.kds.erp.biz;

import javax.ejb.EJBException;
import javax.ejb.SessionContext;
import javax.ejb.SessionBean;
import java.rmi.RemoteException;
import javax.ejb.CreateException;
import org.objectweb.util.monolog.api.BasicLevel;
import javax.ejb.EJBObject;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;
import javax.naming.NamingException;
import org.objectweb.util.monolog.Monolog;
import javax.naming.Context;
import org.objectweb.util.monolog.api.Logger;
import java.lang.reflect.Method;
import javax.ejb.EJBLocalObject;


/**
 * Implementation for <code>ServiceFactory</code> bean. The <code>ServiceFactory</code>
 * bean provides a convinient way to gain access to services implemented as EJB beans.
 *
 *
 * Created: Mon Feb  5 21:35:55 2007
 *
 * @author <a href="mailto:Mihai Giurgeanu@MIHAIG">U-MIHAIG\Mihai Giurgeanu</a>
 * @version 1.0
 */
public class ServiceFactoryBean implements SessionBean {
    static protected Logger logger = null;


    /**
     * Creation of a new <code>ServiceFactory</code>.
     */
    public void ejbCreate() throws CreateException {
	logger.log(BasicLevel.DEBUG, "");
    }



    /**
     * Instantiates a remote interface to the ejb referred through the <code>serviceName</code>.
     *
     * @param serviceName is the name of the service. It is used to get the ejb remote home from
     * the <code>ejb-ref</code> elements configured for the <code>ServiceFactory</code> bean.
     *
     * @param remoteHome is the home class to be used to create a remote reference to the bean.
     *
     * @exception ServiceNotAvailable is thrown on any problem encountered in creating the service.
     *
     */
    public EJBObject remote(String serviceName, Class remoteHome) throws ServiceNotAvailable {
	try {
	    InitialContext ic = new InitialContext();
	    Context env = (Context)ic.lookup("java:comp/env");
	    Object home = PortableRemoteObject.narrow(env.lookup(serviceName), remoteHome);
	    Method m = remoteHome.getMethod("create", null);

	    EJBObject object = (EJBObject)m.invoke(home, null);

	    return object;
	} catch (Exception e) {
	    throw new ServiceNotAvailable(serviceName + "::" + remoteHome.getName(), e);
	}
    }



    /**
     * Instantiates a local interface to the ejb referred through the <code>serviceName</code>.
     *
     * @param serviceName is the name of the service. It is used to get the ejb remote home from
     * the <code>ejb-local-ref</code> elements configured for the <code>ServiceFactory</code> bean.
     *
     * @param localHome is the local home class to be used to create a local reference to the bean.
     *
     * @exception ServiceNotAvailable is thrown on any problem encountered in creating the service.
     *
     */
    public EJBLocalObject local(String serviceName, Class localHome) throws ServiceNotAvailable {
	try {
	    InitialContext ic = new InitialContext();
	    Context env = (Context)ic.lookup("java:comp/env");
	    Object home = PortableRemoteObject.narrow(env.lookup(serviceName), localHome);
	    Method m = localHome.getMethod("create", null);

	    EJBLocalObject object = (EJBLocalObject)m.invoke(home, null);

	    return object;
	} catch (Exception e) {
	    throw new ServiceNotAvailable("serviceName" + "::" + localHome.getName(), e);
	}
    }


    // Implementation of javax.ejb.SessionBean

    public void setSessionContext(SessionContext sessionContext) throws EJBException, RemoteException {
        if (logger == null) {
            logger = Monolog.getMonologFactory().getLogger("ro.kds.erp.biz.ServiceFactoryBean");
        }
        logger.log(BasicLevel.DEBUG, "");
    }
    public void ejbRemove() throws EJBException, RemoteException {

        logger.log(BasicLevel.DEBUG, "");
    }
    public void ejbActivate() throws EJBException, RemoteException {

        logger.log(BasicLevel.DEBUG, "");
    }
    public void ejbPassivate() throws EJBException, RemoteException {

        logger.log(BasicLevel.DEBUG, "");
    }

}
