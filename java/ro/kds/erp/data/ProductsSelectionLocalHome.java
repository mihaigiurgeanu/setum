package ro.kds.erp.data;

import javax.ejb.EJBLocalHome;
import javax.ejb.CreateException;
import javax.ejb.FinderException;

/**
 * Describe interface ProductsSelectionLocalHome here.
 *
 *
 * Created: Wed Apr 26 17:48:38 2006
 *
 * @author <a href="mailto:Mihai Giurgeanu@CRIMIRA"></a>
 * @version 1.0
 */
public interface ProductsSelectionLocalHome extends EJBLocalHome {

    ProductsSelectionLocal create() throws CreateException;
    ProductsSelectionLocal findByPrimaryKey(java.lang.Integer pk) throws FinderException;
    ProductsSelectionLocal findByCode(String code) throws FinderException;

}
