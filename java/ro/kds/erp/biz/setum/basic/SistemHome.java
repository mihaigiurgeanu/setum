package ro.kds.erp.biz.setum.basic;

import java.rmi.RemoteException;
import java.util.Collection;
import javax.ejb.CreateException;
import javax.ejb.EJBHome;

/**
 * Sistem home interface.
 *
 * Class was automaticaly generated from a template.
 *
 */
public interface SistemHome extends EJBHome {        
    Sistem create() throws CreateException, RemoteException;
}
