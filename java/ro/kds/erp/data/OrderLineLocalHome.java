package ro.kds.erp.data;

import javax.ejb.EJBLocalHome;
import javax.ejb.CreateException;
import javax.ejb.FinderException;

/**
 * Describe interface OrderLineLocalHome here.
 *
 *
 * Created: Sun Sep 17 14:36:24 2006
 *
 * @author <a href="mailto:Mihai Giurgeanu@MIHAIG">U-MIHAIG\Mihai Giurgeanu</a>
 * @version 1.0
 */
public interface OrderLineLocalHome extends EJBLocalHome {

    /**
     * Create an empty OrderLine object.
     */
    public OrderLineLocal create() throws CreateException;

    /**
     * Standard find method.
     */
    public OrderLineLocal findByPrimaryKey(Integer id) throws FinderException;

}
