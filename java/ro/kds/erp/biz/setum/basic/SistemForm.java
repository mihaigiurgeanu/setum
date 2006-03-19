package ro.kds.erp.biz.setum.basic;

import java.io.Serializable;
import java.util.Map;
import ro.kds.erp.data.AttributeLocal;

/**
 * A represantation of form data. While user is editing the data associated
 * with this object, the data is maintained temporary into this bean. At
 * the end, when <code>save</code> operation is called, the data is
 * saved in the persistance layer.
 *
 * Class was automaticaly generated from a template.
 *
 */
public class SistemForm implements Serializable {
        
    String name;
    String code;
    Integer categoryId;
    java.math.BigDecimal entryPrice;
    java.math.BigDecimal sellPrice;
    java.math.BigDecimal partPrice;
    java.math.BigDecimal laborPrice;
    Double relativeGainSP;
    java.math.BigDecimal absoluteGainSP;
    Double relativeGainPP;
    java.math.BigDecimal absoluteGainPP;

    public SistemForm() {


       this.name = "";



       this.code = "";



       this.categoryId = new Integer(0);



       this.entryPrice = new java.math.BigDecimal(0);



       this.sellPrice = new java.math.BigDecimal(0);



       this.partPrice = new java.math.BigDecimal(0);



       this.laborPrice = new java.math.BigDecimal(0);



       this.relativeGainSP = new Double(0);
   


       this.absoluteGainSP = new java.math.BigDecimal(0);



       this.relativeGainPP = new Double(0);
   


       this.absoluteGainPP = new java.math.BigDecimal(0);



    }

    public void setName(String newName) {
        this.name = newName;
    }

    public String getName() {
        return name;
    }

    public void readName(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("name");
	if(a != null) {
	    this.setName(a.getStringValue());
	}
    }

    public void setCode(String newCode) {
        this.code = newCode;
    }

    public String getCode() {
        return code;
    }

    public void readCode(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("code");
	if(a != null) {
	    this.setCode(a.getStringValue());
	}
    }

    public void setCategoryId(Integer newCategoryId) {
        this.categoryId = newCategoryId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void readCategoryId(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("categoryId");
	if(a != null) {
	    this.setCategoryId(a.getIntValue());
	}
    }

    public void setEntryPrice(java.math.BigDecimal newEntryPrice) {
        this.entryPrice = newEntryPrice;
    }

    public java.math.BigDecimal getEntryPrice() {
        return entryPrice;
    }

    public void readEntryPrice(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("entryPrice");
	if(a != null) {
	    this.setEntryPrice(a.getDecimalValue());
	}
    }

    public void setSellPrice(java.math.BigDecimal newSellPrice) {
        this.sellPrice = newSellPrice;
    }

    public java.math.BigDecimal getSellPrice() {
        return sellPrice;
    }

    public void readSellPrice(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("sellPrice");
	if(a != null) {
	    this.setSellPrice(a.getDecimalValue());
	}
    }

    public void setPartPrice(java.math.BigDecimal newPartPrice) {
        this.partPrice = newPartPrice;
    }

    public java.math.BigDecimal getPartPrice() {
        return partPrice;
    }

    public void readPartPrice(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("partPrice");
	if(a != null) {
	    this.setPartPrice(a.getDecimalValue());
	}
    }

    public void setLaborPrice(java.math.BigDecimal newLaborPrice) {
        this.laborPrice = newLaborPrice;
    }

    public java.math.BigDecimal getLaborPrice() {
        return laborPrice;
    }

    public void readLaborPrice(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("laborPrice");
	if(a != null) {
	    this.setLaborPrice(a.getDecimalValue());
	}
    }

    public void setRelativeGainSP(Double newRelativeGainSP) {
        this.relativeGainSP = newRelativeGainSP;
    }

    public Double getRelativeGainSP() {
        return relativeGainSP;
    }

    public void readRelativeGainSP(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("relativeGainSP");
	if(a != null) {
	    this.setRelativeGainSP(a.getDoubleValue());
	}
    }

    public void setAbsoluteGainSP(java.math.BigDecimal newAbsoluteGainSP) {
        this.absoluteGainSP = newAbsoluteGainSP;
    }

    public java.math.BigDecimal getAbsoluteGainSP() {
        return absoluteGainSP;
    }

    public void readAbsoluteGainSP(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("absoluteGainSP");
	if(a != null) {
	    this.setAbsoluteGainSP(a.getDecimalValue());
	}
    }

    public void setRelativeGainPP(Double newRelativeGainPP) {
        this.relativeGainPP = newRelativeGainPP;
    }

    public Double getRelativeGainPP() {
        return relativeGainPP;
    }

    public void readRelativeGainPP(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("relativeGainPP");
	if(a != null) {
	    this.setRelativeGainPP(a.getDoubleValue());
	}
    }

    public void setAbsoluteGainPP(java.math.BigDecimal newAbsoluteGainPP) {
        this.absoluteGainPP = newAbsoluteGainPP;
    }

    public java.math.BigDecimal getAbsoluteGainPP() {
        return absoluteGainPP;
    }

    public void readAbsoluteGainPP(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("absoluteGainPP");
	if(a != null) {
	    this.setAbsoluteGainPP(a.getDecimalValue());
	}
    }

}
