package ro.kds.erp.biz.setum.basic;

import java.rmi.RemoteException;
import java.util.Collection;
import javax.ejb.CreateException;
import javax.ejb.EJBHome;

/**
 * UsaStdNeechipata home interface.
 *
 * Class was automaticaly generated from a template.
 *
 */
public interface UsaStdNeechipataHome extends EJBHome {        
    UsaStdNeechipata create() throws CreateException, RemoteException;
}
