package ro.kds.erp.biz.setum.basic;

import java.rmi.RemoteException;
import java.util.Collection;
import javax.ejb.CreateException;
import javax.ejb.EJBHome;

/**
 * UsaMetalica1K home interface.
 *
 * Class was automaticaly generated from a template.
 *
 */
public interface UsaMetalica1KHome extends EJBHome {        
    UsaMetalica1K create() throws CreateException, RemoteException;
}
