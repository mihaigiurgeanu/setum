// Product.java

package ro.kds.erp.data;

import javax.ejb.EJBLocalObject;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;

/**
 * Product local interface
 */
public interface ProductLocal extends EJBLocalObject {
    public Integer getId();

    public String getName();
    public void setName(String name);

    public String getCode();
    public void setCode(String code);

    public BigDecimal getEntryPrice();
    public void setEntryPrice(BigDecimal entryPrice);
    
    public BigDecimal getSellPrice();
    public void setSellPrice(BigDecimal sellPrice);
    
    public BigDecimal getPrice1();
    public void setPrice1(BigDecimal price1);

    public BigDecimal getPrice2();
    public void setPrice2(BigDecimal price1);

    public BigDecimal getPrice3();
    public void setPrice3(BigDecimal price1);

    public BigDecimal getPrice4();
    public void setPrice4(BigDecimal price1);

    public BigDecimal getPrice5();
    public void setPrice5(BigDecimal price1);

    public float getPriceCorrectionP();
    public void setPriceCorrectionP(float percent);

    public String getDescription();
    public void setDescription(String description);
    
    public String getDescription1();
    public void setDescription1(String description);

    public Collection getAttributes();
    public void setAttributes(Collection attribs);    

    public CompositeProductLocal getCompositeProduct();
    public void setCompositeProduct(CompositeProductLocal p);

    public CategoryLocal getCategory();
    public void setCategory(CategoryLocal category);


    /**
     * Convenience method. Gets the attributes collection of this product
     * and builds a map of attributes name-attribute.
     */
    public Map getAttributesMap();

}
