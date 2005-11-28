package ro.kds.erp.data;

import javax.ejb.EJBLocalHome;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import java.util.Collection;

/**
 * Describe interface CategoryLocalHome here.
 *
 *
 * Created: Fri Sep 16 19:06:18 2005
 *
 * @author <a href="mailto:Mihai Giurgeanu@CRIMIRA"></a>
 * @version 1.0
 */
public interface CategoryLocalHome extends EJBLocalHome {
    public CategoryLocal create(Integer id, 
				String categoryName) throws CreateException;
    public CategoryLocal findByPrimaryKey(Integer pk) throws FinderException;
}
