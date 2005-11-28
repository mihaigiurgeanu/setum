package ro.kds.erp.data;

import javax.ejb.EJBLocalObject;
import java.math.BigDecimal;



public interface AttributeLocal extends javax.ejb.EJBLocalObject {

    public Integer getId();

    public String getName();
    public void setName(String attrName);
    
    public String getStringValue();
    public void setStringValue(String attrValue);

    public Integer getIntValue();
    public void setIntValue(Integer attrValue);
    
    public BigDecimal getDecimalValue();
    public void setDecimalValue(BigDecimal attrValue);
    
    public Double getDoubleValue();
    public void setDoubleValue(Double attrValue);

    public ProductLocal getProduct();
    public void setProduct(ProductLocal p);
} // AttributeLocal
