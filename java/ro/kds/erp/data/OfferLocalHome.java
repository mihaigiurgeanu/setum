package ro.kds.erp.data;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.EJBLocalHome;
import java.util.Collection;

/**
 * Methods for creating and locating OfferEJB entities.
 *
 *
 * Created: Sun Oct 23 20:07:43 2005
 *
 * @author <a href="mailto:Mihai Giurgeanu@CRIMIRA"></a>
 * @version 1.0
 */
public interface OfferLocalHome extends EJBLocalHome {
    public OfferLocal create() throws CreateException, DataLayerException;
    public OfferLocal findByPrimaryKey(Integer id) throws FinderException;

    /**
     * Find all offers that fall into a specific category.
     */
    public Collection findByCategory(Integer category) throws FinderException;
}
