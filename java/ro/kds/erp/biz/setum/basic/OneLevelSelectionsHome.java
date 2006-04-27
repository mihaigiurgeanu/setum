package ro.kds.erp.biz.setum.basic;

import java.rmi.RemoteException;
import java.util.Collection;
import javax.ejb.CreateException;
import javax.ejb.EJBHome;

/**
 * OneLevelSelections home interface.
 *
 * Class was automaticaly generated from a template.
 *
 */
public interface OneLevelSelectionsHome extends EJBHome {        
    OneLevelSelections create() throws CreateException, RemoteException;
}
