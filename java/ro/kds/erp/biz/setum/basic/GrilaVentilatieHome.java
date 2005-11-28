package ro.kds.erp.biz.setum.basic;

import java.rmi.RemoteException;
import java.util.Collection;
import javax.ejb.CreateException;
import javax.ejb.EJBHome;

/**
 * GrilaVentilatie home interface.
 *
 * Class was automaticaly generated from a template.
 *
 */
public interface GrilaVentilatieHome extends EJBHome {        
    GrilaVentilatie create() throws CreateException, RemoteException;
}
