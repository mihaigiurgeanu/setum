package ro.kds.erp.biz.setum.basic;

import java.rmi.RemoteException;
import java.util.Collection;
import javax.ejb.CreateException;
import javax.ejb.EJBHome;

/**
 * Finisaje home interface.
 *
 * Class was automaticaly generated from a template.
 *
 */
public interface FinisajeHome extends EJBHome {        
    Finisaje create() throws CreateException, RemoteException;
}
