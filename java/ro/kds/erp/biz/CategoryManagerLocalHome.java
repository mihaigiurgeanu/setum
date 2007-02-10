package ro.kds.erp.biz;

import javax.ejb.EJBLocalHome;
import javax.ejb.CreateException;


/**
 * Describe interface CategoryManagerLocalHome here.
 *
 *
 * Created: Sun Jan 21 21:16:14 2007
 *
 * @author <a href="mailto:Mihai Giurgeanu@MIHAIG">U-MIHAIG\Mihai Giurgeanu</a>
 * @version 1.0
 */
public interface CategoryManagerLocalHome extends EJBLocalHome {
    public CategoryManagerLocal create() throws CreateException;
}
