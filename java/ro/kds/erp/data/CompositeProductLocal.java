package ro.kds.erp.data;

import javax.ejb.EJBLocalObject;
import java.util.Collection;

/**
 * Describe interface CompositeProductLocal here.
 *
 *
 * Created: Sat Sep 24 12:00:00 2005
 *
 * @author <a href="mailto:Mihai Giurgeanu@CRIMIRA"></a>
 * @version 1.0
 */
public interface CompositeProductLocal extends EJBLocalObject {
    public Integer getId();
    
    public Collection getComponents();
    public void setComponents(Collection components);

    public ProductLocal getProduct();
    public void setProduct(ProductLocal p);
}
