package ro.kds.erp.biz;

import java.rmi.RemoteException;
import javax.ejb.EJBObject;

/**
 * Describe interface ListMakerBean here.
 *
 *
 * Created: Tue Oct 04 20:55:24 2005
 *
 * @author <a href="mailto:Mihai Giurgeanu@CRIMIRA"></a>
 * @version 1.0
 */
public interface ListMakerBean extends EJBObject {
    ResponseBean makeList() throws RemoteException;
}
