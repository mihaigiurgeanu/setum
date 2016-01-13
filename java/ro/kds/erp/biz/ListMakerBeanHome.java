package ro.kds.erp.biz;

import java.rmi.RemoteException;
import javax.ejb.CreateException;
import javax.ejb.EJBHome;


/**
 * Describe interface ListMakerBeanHome here.
 *
 *
 * Created: Tue Oct 04 20:54:38 2005
 *
 * @author <a href="mailto:Mihai Giurgeanu@CRIMIRA"></a>
 * @version 1.0
 */
public interface ListMakerBeanHome extends EJBHome {
    ListMakerBean create() throws RemoteException, CreateException;
}
