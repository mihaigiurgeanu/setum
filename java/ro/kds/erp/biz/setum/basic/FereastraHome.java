package ro.kds.erp.biz.setum.basic;

import java.rmi.RemoteException;
import java.util.Collection;
import javax.ejb.CreateException;
import javax.ejb.EJBHome;

/**
 * Fereastra home interface.
 *
 * Class was automaticaly generated from a template.
 *
 */
public interface FereastraHome extends EJBHome {        
    Fereastra create() throws CreateException, RemoteException;
}
