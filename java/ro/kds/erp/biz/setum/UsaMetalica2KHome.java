// UsaMetalica2KHome.java

package ro.kds.erp.biz.setum;

import java.rmi.RemoteException;
import java.util.Collection;
import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import javax.ejb.FinderException;

/**
 * Home interface for the bean UsaMetalica2K
 */
public interface UsaMetalica2KHome extends EJBHome {
    UsaMetalica2K create() throws CreateException, RemoteException;
}
