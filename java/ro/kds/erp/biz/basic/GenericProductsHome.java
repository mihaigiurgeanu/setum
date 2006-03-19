package ro.kds.erp.biz.basic;

import java.rmi.RemoteException;
import java.util.Collection;
import javax.ejb.CreateException;
import javax.ejb.EJBHome;

/**
 * GenericProducts home interface.
 *
 * Class was automaticaly generated from a template.
 *
 */
public interface GenericProductsHome extends EJBHome {        
    GenericProducts create() throws CreateException, RemoteException;
}
