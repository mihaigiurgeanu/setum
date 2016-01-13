// ProductHome.java

package ro.kds.erp.data;

import java.util.Collection;
import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

/**
 * Local Home interface for the bean Product
 */
public interface ProductLocalHome extends EJBLocalHome {

    ProductLocal create() throws CreateException;
    ProductLocal findByPrimaryKey(java.lang.Integer pk) throws FinderException;
    Collection findAll() throws FinderException;
}
