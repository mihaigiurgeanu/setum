package ro.kds.erp.biz.setum.basic;

import java.rmi.RemoteException;
import java.util.Collection;
import javax.ejb.CreateException;
import javax.ejb.EJBHome;

/**
 * UsaStandard home interface.
 *
 * Class was automaticaly generated from a template.
 *
 */
public interface UsaStandardHome extends EJBHome {        
    UsaStandard create() throws CreateException, RemoteException;
}
