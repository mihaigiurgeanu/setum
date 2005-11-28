package ro.kds.erp.data;

import javax.ejb.EJBLocalObject;
import java.util.Collection;

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

    public Collection getSubCategories();
    public void setSubCategories(Collection categories);
}
