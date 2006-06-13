package ro.kds.erp.biz.setum.basic;

import java.rmi.RemoteException;
import java.util.Collection;
import javax.ejb.CreateException;
import javax.ejb.EJBHome;

/**
 * Supralumina home interface.
 *
 * Class was automaticaly generated from a template.
 *
 */
public interface SupraluminaHome extends EJBHome {        
    Supralumina create() throws CreateException, RemoteException;
}
