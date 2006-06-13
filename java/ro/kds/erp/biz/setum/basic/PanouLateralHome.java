package ro.kds.erp.biz.setum.basic;

import java.rmi.RemoteException;
import java.util.Collection;
import javax.ejb.CreateException;
import javax.ejb.EJBHome;

/**
 * PanouLateral home interface.
 *
 * Class was automaticaly generated from a template.
 *
 */
public interface PanouLateralHome extends EJBHome {        
    PanouLateral create() throws CreateException, RemoteException;
}
