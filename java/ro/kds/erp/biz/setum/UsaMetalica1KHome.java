// UsaMetalica1KHome.java

package ro.kds.erp.biz.setum;

import java.rmi.RemoteException;
import java.util.Collection;
import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import javax.ejb.FinderException;

/**
 * Home interface for the bean UsaMetalica1K
 */
public interface UsaMetalica1KHome extends EJBHome {
    UsaMetalica1K create() throws CreateException, RemoteException;
}
