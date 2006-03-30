package ro.kds.erp.data;

import javax.ejb.EJBLocalObject;
import java.util.Collection;
import javax.ejb.FinderException;

/**
 * Describe interface CategoryLocal here.
 *
 *
 * Created: Fri Sep 16 19:01:44 2005
 *
 * @author <a href="mailto:Mihai Giurgeanu@CRIMIRA"></a>
 * @version 1.0
 */
public interface CategoryLocal extends EJBLocalObject {
    public Integer getId();
    public void setId(Integer newId);
    
    public String getName();
    public void setName(String categoryName);

    public Collection getProducts();
    public void setProducts(Collection products);

    public Integer getProductsCount();

    public Collection getSubCategories();
    public void setSubCategories(Collection categories);


    /**
     * Retrieves the child product that has the given code. If there are
     * more children with the same code, only one will be returned. It is
     * not defined which of the child products with the given code will be
     * returned, so it is a logical error to have more child products with
     * the same code. This is not enforced in any way, and there will be
     * no exception thrown if there are more children products with the
     * same code.
     *
     * @returns a <code>ProductLocal</code> object that is in this category and
     * has the specified code.
     * @throws FinderException if no products could be found.
     */
    public ProductLocal getProductByCode(String code) throws FinderException;

    /**
     * Calls the <code>getProductByCode(String code)</code> converting
     * the given <code>Integer code</code> to <code>String</code>.
     *
     * @param code is the code that will be converted to <code>String</code>
     * @returns the <code>ProductLocal</code> object that has the code equal
     * to <code>String</code> represantation of <code>code</code> parameter.
     * @throws FinderException if no product is found.
     */
    public ProductLocal getProductByCode(Integer code) throws FinderException;
}
