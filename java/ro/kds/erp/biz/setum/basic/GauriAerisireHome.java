package ro.kds.erp.biz.setum.basic;

import java.rmi.RemoteException;
import java.util.Collection;
import javax.ejb.CreateException;
import javax.ejb.EJBHome;

/**
 * GauriAerisire home interface.
 *
 * Class was automaticaly generated from a template.
 *
 */
public interface GauriAerisireHome extends EJBHome {        
    GauriAerisire create() throws CreateException, RemoteException;
}
