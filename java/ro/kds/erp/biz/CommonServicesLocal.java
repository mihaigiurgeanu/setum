package ro.kds.erp.biz;

import javax.ejb.EJBLocalObject;
import ro.kds.erp.data.ProductLocal;
import ro.kds.erp.data.AttributeLocal;
import java.util.Collection;

/**
 * Collection of common business logic services.
 *
 *
 * Created: Tue Feb  6 22:03:12 2007
 *
 * @author <a href="mailto:Mihai Giurgeanu@MIHAIG">U-MIHAIG\Mihai Giurgeanu</a>
 * @version 1.0
 */
public interface CommonServicesLocal extends EJBLocalObject {
    /**
     * Finds the <code>Product</code> entity by its code. Each code should
     * be unique within a certain category. If there are more then one
     * products with the same code, this method returns one of them.
     *
     * @param categoryId is the <code>id</code> of the category in which to
     * look for the code.
     * @param code is the searched code
     * @return the <code>Product</code> entity with the given code.
     * 
     * @exception ProductNotAvailable is thrown if the specified code could not be
     * found in the specified category, the category could not be found for the 
     * given id or if a configuration or system error occured.
     * 
     */
    ProductLocal findProductByCode(Integer categoryId, String code) throws ProductNotAvailable;

    /**
     * Looks up a product by its id.
     *
     * @param id is the primary key of the <code>Product</code> entity.
     * @exception ProductNotAvailable if there is no product with the specified id
     * or a configuration or system eror occurs.
     */
    ProductLocal findProductById(Integer id) throws ProductNotAvailable;


    /**
     * Retrieves a specific attribute of an product.
     */
    AttributeLocal getAttributeByProductId(Integer pid, String attrName) throws ProductNotAvailable;

    /**
     * Gets the list of the rules in a given rules set.
     *
     * @param rulesSet is the name of the rules set.
     * @returns the list of <code>RuleLocal</code> objects.
     */
    Collection getRules(String rulesSet);
}
