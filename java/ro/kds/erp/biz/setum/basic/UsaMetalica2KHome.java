package ro.kds.erp.biz.setum.basic;

import java.rmi.RemoteException;
import java.util.Collection;
import javax.ejb.CreateException;
import javax.ejb.EJBHome;

/**
 * UsaMetalica2K home interface.
 *
 * Class was automaticaly generated from a template.
 *
 */
public interface UsaMetalica2KHome extends EJBHome {        
    UsaMetalica2K create() throws CreateException, RemoteException;
}
