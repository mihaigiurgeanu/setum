package ro.kds.erp.data;

import javax.ejb.EJBLocalObject;
import java.util.Collection;

/**
 * Describe interface ProductsSelectionLocal here.
 *
 *
 * Created: Wed Apr 26 17:43:54 2006
 *
 * @author <a href="mailto:Mihai Giurgeanu@CRIMIRA"></a>
 * @version 1.0
 */
public interface ProductsSelectionLocal extends EJBLocalObject {


    public Integer getId();

    public String getName();
    public void setName(String name);

    public String getCode();
    public void setCode(String code);

    public String getDescription();
    public void setDescription(String description);


    /**
     * @returns a <code>java.util.Collection</code> of products.
     */
    public Collection getProducts();

    /**
     * @param products is a <code>java.util.Collection</code> of products.
     */
    public void setProducts(Collection products);

    /**
     * Children selections.
     * @returns a <code>Collection</code> of <code>ProductsSelectionLocal</code> objects.
     */
    public Collection getSelections();
    /**
     * Children selections.
     * @param selections is a <code>Collection</code> of <code>ProductsSelectionLocal</code> objects.
     */
    public void setSelections(Collection selections);


}
