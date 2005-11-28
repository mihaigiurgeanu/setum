package ro.kds.erp.data;

import javax.ejb.EJBLocalHome;
import javax.ejb.CreateException;
import javax.ejb.FinderException;

/**
 * Create and locate OfferEJB objects.
 *
 *
 * Created: Sun Oct 23 18:13:44 2005
 *
 * @author <a href="mailto:Mihai Giurgeanu@CRIMIRA"></a>
 * @version 1.0
 */
public interface OfferItemLocalHome extends EJBLocalHome {
    public OfferItemLocal create() throws CreateException;
    public OfferItemLocal findByPrimaryKey(Integer id) throws FinderException;
}
