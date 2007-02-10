package ro.kds.erp.biz;

import javax.ejb.EJBLocalObject;
import javax.ejb.EJBObject;

/**
 * <code>ServiceFactory</code> is a helper object that manages the different
 * services on the system. It is a central service definition mechanism. Each
 * sesion EJB that should be accessed as a service it is registered with this
 * locator in the ejb-jar.xml, then the service may be obtained by name. The name
 * of the service is the the name registered in the ejb-ref repository.
 *
 * There are 2 kinds of services that can be registered: remote services and local services,
 * the difference between them is that one kind of service is implemented through a remote 
 * ejb interface and the other through a local ejb interface.
 *
 *
 * Created: Mon Feb  5 19:12:36 2007
 *
 * @author <a href="mailto:Mihai Giurgeanu@MIHAIG">U-MIHAIG\Mihai Giurgeanu</a>
 * @version 1.0
 */
public interface ServiceFactoryLocal extends EJBLocalObject {
    /**
     * Returns a pointer to the remote interface of an EJB identified
     * by the given <code>serviceName</code> with the given home class. The service
     * must be decalred in the jar descriptors for the <code>Servicefactory</code> ejb with
     * the name used to idetify it, but it is no need to define it in the ejb descriptors
     * of the clients of <code>ServiceFactory</code> ejb.
     *
     * @param serviceName is the name of the service to locate
     * @param remote is the name of the home interface to use to create the service
     *
     * @exception ServiceNotAvailable if the service interface can not be instantiated
     * due to a <code>NamingException</code>, a <code>CreateException</code> or a 
     * business logic specific exception.
     */
    public EJBObject remote(String serviceName, Class remote) throws ServiceNotAvailable;

    /**
     * Returns a pointer to the local interface of an EJB identified
     * by the given <code>serviceName</code> with the given home class. The service
     * must be decalred in the jar descriptors for the <code>ServiceFactory</code> ejb with
     * the name used to idetify it, but it is no need to define it in the ejb descriptors
     * of the clients of <code>ServiceFactory</code> ejb.
     *
     * @param serviceName is the name of the service to locate
     * @param local is the name of the home interface to use to create the service
     *
     */
    public EJBLocalObject local(String serviceName, Class local) throws ServiceNotAvailable;
}
