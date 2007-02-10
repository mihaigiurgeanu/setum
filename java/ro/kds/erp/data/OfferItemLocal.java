package ro.kds.erp.data;

import javax.ejb.EJBLocalObject;
import java.math.BigDecimal;

/**
 * Methods for accessing an item line on an offer.
 *
 *
 * Created: Sun Oct 23 18:28:05 2005
 *
 * @author <a href="mailto:Mihai Giurgeanu@CRIMIRA"></a>
 * @version 1.0
 */
public interface OfferItemLocal extends EJBLocalObject {
    
    /**
     * Primary key field
     */
    public Integer getId();
    
    /**
     * Primary key field
     */
    public void setId(Integer id);

    /**
     * Get a reference to the product represented on this offer item.
     */
    public ProductLocal getProduct();

    /**
     * Set a reference to the product on this line item.
     */
    public void setProduct(ProductLocal p);

    /**
     * Get the offered quantity.
     */
    public BigDecimal getQuantity();
    /**
     * Set the offered quantity.
     */
    public void setQuantity(BigDecimal q);
    
    /**
     * Get the measure unit.
     */
    public String getUnit();
    
    /**
     * Set the unit of measure.
     */
    public void setUnit(String unit);

    /**
     * Get the unit price.
     */
    public BigDecimal getPrice();

    /**
     * Set the unit price.
     */
    public void setPrice(BigDecimal p);

    /**
     * Get the special unit price 1.
     */
    public BigDecimal getPrice1();

    /**
     * Set the special unit price 1.
     */
    public void setPrice1(BigDecimal p);
    
    /**
     * Get the special unit price 2.
     */
    public BigDecimal getPrice2();

    /**
     * Set the special unit price 2.
     */
    public void setPrice2(BigDecimal p);

    /**
     * Get the special unit price 3.
     */
    public BigDecimal getPrice3();

    /**
     * Set the special unit price 3.
     */
    public void setPrice3(BigDecimal p);

    /**
     * Get the special unit price 4.
     */
    public BigDecimal getPrice4();

    /**
     * Set the special unit price 4.
     */
    public void setPrice4(BigDecimal p);

    /**
     * Get the special unit price 5.
     */
    public BigDecimal getPrice5();

    /**
     * Set the special unit price 5.
     */
    public void setPrice5(BigDecimal p);

    /**
     * Get discount.
     */
    public BigDecimal getDiscount();

    /**
     * Set discount.
     */
    public void setDiscount(BigDecimal d);

    /**
     * Business category is a RDF resource identifier used by the
     * user interface to know how to edit the corresponding product.
     */
    public String getBusinessCategory();
    

    /**
     * Business category is a RDF resource identifier used by the
     * user interface to know how to edit the corresponding product.
     */
    public void setBusinessCategory(String bc);

    /**
     * Comments refering to this specific line item
     */
    public String getComments();

    /**
     * Comments refering to this specific line item
     */
    public void setComments(String comments);

    /**
     * The offer containing this offer item.
     */
    public OfferLocal getOffer();
}
