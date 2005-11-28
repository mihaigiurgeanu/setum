package ro.kds.erp.biz;

import javax.ejb.EJBHome;
import java.rmi.RemoteException;
import javax.ejb.CreateException;

/**
 * Home interface for the PreferencesEJB bean.
 *
 *
 * Created: Tue Oct 11 12:20:21 2005
 *
 * @author <a href="mailto:Mihai Giurgeanu@CRIMIRA"></a>
 * @version 1.0
 */
public interface PreferencesHome extends EJBHome {
    public Preferences create() throws RemoteException, CreateException;
}
