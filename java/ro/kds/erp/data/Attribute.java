package ro.kds.erp.data;

import javax.ejb.EJBObject;
import java.math.BigDecimal;



public interface Attribute extends javax.ejb.EJBObject {

    public String getName();
    public void setName(String attrName);
    
    public String getStringValue();
    public void setStrigValue(String attrValue);

    public Integer getIntValue();
    public void setIntValue(Integer attrValue);
    
    public BigDecimal getDecimalValue();
    public void setDecimalValue(BigDecimal attrValue);

    public Double getDoubleValue();
    public void setDoubleValue(Double attrValue);

    public Boolean getBoolValue();
    public void setBoolValue(Boolean attrValue);

} // Attribute
