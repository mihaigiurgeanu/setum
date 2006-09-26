package ro.kds.erp.data;

import javax.ejb.EJBLocalObject;
import java.math.BigDecimal;

/**
 * Describe interface OrderLineLocal here.
 *
 *
 * Created: Sun Sep 17 14:24:25 2006
 *
 * @author <a href="mailto:Mihai Giurgeanu@MIHAIG">U-MIHAIG\Mihai Giurgeanu</a>
 * @version 1.0
 */
public interface OrderLineLocal extends EJBLocalObject {
    
    /**
     * Primary key field.
     */
    public Integer getId();
    /**
     * Primary key field
     */
    public void setId(Integer id);
    
    /**
     * The offer item related to this order. The information about the
     * ordered product is contained in this offer item. The quantity ordered
     * may also be restricted by the business logic by the quantity offered
     * in the offer.
     */
    public OfferItemLocal getOfferItem();
    /**
     * The offer item related to this order. The information about the
     * ordered product is contained in this offer item. The quantity ordered
     * may also be restricted by the business logic by the quantity offered
     * in the offer.
     */
    public void setOfferItem(OfferItemLocal oi);

    /**
     * The price of the ordered product. It might be different from the offered
     * price.
     */
    public BigDecimal getPrice();
    /** 
     * The price of the ordered product. It might be different from the offered
     * price.
     */
    public void setPrice(BigDecimal price);

    /**
     * The ordered quantity.
     */
    public BigDecimal getQuantity();
    /**
     * The ordered quantity.
     */
    public void setQuantity(BigDecimal q);

    
}
