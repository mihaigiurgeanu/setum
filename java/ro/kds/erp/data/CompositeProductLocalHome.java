package ro.kds.erp.data;

import javax.ejb.EJBLocalHome;
import javax.ejb.CreateException;
import javax.ejb.FinderException;

/**
 * Describe interface CompositeProductLocalHome here.
 *
 *
 * Created: Sat Sep 24 12:03:36 2005
 *
 * @author <a href="mailto:Mihai Giurgeanu@CRIMIRA"></a>
 * @version 1.0
 */
public interface CompositeProductLocalHome extends EJBLocalHome {
    CompositeProductLocal create(Integer id) throws CreateException;
    CompositeProductLocal findByPrimaryKey(Integer id) throws FinderException;
}
