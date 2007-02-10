package ro.kds.erp.biz;

import javax.ejb.EJBLocalHome;
import javax.ejb.CreateException;

/**
 * Local home interface for <code>CommonServices</code> ejb.
 *
 *
 * Created: Tue Feb  6 22:18:46 2007
 *
 * @author <a href="mailto:Mihai Giurgeanu@MIHAIG">U-MIHAIG\Mihai Giurgeanu</a>
 * @version 1.0
 */
public interface CommonServicesLocalHome extends EJBLocalHome {

    CommonServicesLocal create() throws CreateException;
}
