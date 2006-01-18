package ro.kds.erp.biz.setum.basic;

import java.rmi.RemoteException;
import java.util.Collection;
import javax.ejb.CreateException;
import javax.ejb.EJBHome;

/**
 * Clients home interface.
 *
 * Class was automaticaly generated from a template.
 *
 */
public interface ClientsHome extends EJBHome {        
    Clients create() throws CreateException, RemoteException;
}
