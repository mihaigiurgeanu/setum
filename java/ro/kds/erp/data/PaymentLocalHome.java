package ro.kds.erp.data;

import javax.ejb.EJBLocalHome;
import javax.ejb.CreateException;
import javax.ejb.FinderException;

/**
 * Describe interface PaymentLocalHome here.
 *
 *
 * Created: Sat Oct 28 00:22:33 2006
 *
 * @author <a href="mailto:Mihai Giurgeanu@MIHAIG">U-MIHAIG\Mihai Giurgeanu</a>
 * @version 1.0
 */
public interface PaymentLocalHome extends EJBLocalHome {
    public PaymentLocal create() throws CreateException, DataLayerException;
    public PaymentLocal findByPrimaryKey(Integer id) throws FinderException;
}
