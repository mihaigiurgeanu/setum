package ro.kds.erp.biz.setum.basic;

import java.rmi.RemoteException;
import java.util.Collection;
import javax.ejb.CreateException;
import javax.ejb.EJBHome;

/**
 * Orders home interface.
 *
 * Class was automaticaly generated from a template.
 *
 */
public interface OrdersHome extends EJBHome {        
    Orders create() throws CreateException, RemoteException;
}
