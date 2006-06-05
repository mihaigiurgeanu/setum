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
public class GauriAerisireForm implements Serializable {
        
    Double diametru;
    Double pas;
    Integer nrRanduri;
    String pozitionare1;
    String pozitionare2;
    String pozitionare3;
    java.math.BigDecimal sellPrice;
    java.math.BigDecimal entryPrice;
    java.math.BigDecimal price1;
    String businessCategory;
    Integer quantity;

    public GauriAerisireForm() {


       this.diametru = new Double(0);
   


       this.pas = new Double(0);
   


       this.nrRanduri = new Integer(0);



       this.pozitionare1 = "";



       this.pozitionare2 = "";



       this.pozitionare3 = "";



       this.sellPrice = new java.math.BigDecimal(0);



       this.entryPrice = new java.math.BigDecimal(0);



       this.price1 = new java.math.BigDecimal(0);



       this.businessCategory = "";



       this.quantity = new Integer(0);



    }

    public void setDiametru(Double newDiametru) {
        this.diametru = newDiametru;
    }

    public Double getDiametru() {
        return diametru;
    }

    public void readDiametru(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("diametru");
	if(a != null) {
	    this.setDiametru(a.getDoubleValue());
	}
    }

    public void setPas(Double newPas) {
        this.pas = newPas;
    }

    public Double getPas() {
        return pas;
    }

    public void readPas(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("pas");
	if(a != null) {
	    this.setPas(a.getDoubleValue());
	}
    }

    public void setNrRanduri(Integer newNrRanduri) {
        this.nrRanduri = newNrRanduri;
    }

    public Integer getNrRanduri() {
        return nrRanduri;
    }

    public void readNrRanduri(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("nrRanduri");
	if(a != null) {
	    this.setNrRanduri(a.getIntValue());
	}
    }

    public void setPozitionare1(String newPozitionare1) {
        this.pozitionare1 = newPozitionare1;
    }

    public String getPozitionare1() {
        return pozitionare1;
    }

    public void readPozitionare1(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("pozitionare1");
	if(a != null) {
	    this.setPozitionare1(a.getStringValue());
	}
    }

    public void setPozitionare2(String newPozitionare2) {
        this.pozitionare2 = newPozitionare2;
    }

    public String getPozitionare2() {
        return pozitionare2;
    }

    public void readPozitionare2(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("pozitionare2");
	if(a != null) {
	    this.setPozitionare2(a.getStringValue());
	}
    }

    public void setPozitionare3(String newPozitionare3) {
        this.pozitionare3 = newPozitionare3;
    }

    public String getPozitionare3() {
        return pozitionare3;
    }

    public void readPozitionare3(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("pozitionare3");
	if(a != null) {
	    this.setPozitionare3(a.getStringValue());
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

    public void setPrice1(java.math.BigDecimal newPrice1) {
        this.price1 = newPrice1;
    }

    public java.math.BigDecimal getPrice1() {
        return price1;
    }

    public void readPrice1(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("price1");
	if(a != null) {
	    this.setPrice1(a.getDecimalValue());
	}
    }

    public void setBusinessCategory(String newBusinessCategory) {
        this.businessCategory = newBusinessCategory;
    }

    public String getBusinessCategory() {
        return businessCategory;
    }

    public void readBusinessCategory(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("businessCategory");
	if(a != null) {
	    this.setBusinessCategory(a.getStringValue());
	}
    }

    public void setQuantity(Integer newQuantity) {
        this.quantity = newQuantity;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void readQuantity(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("quantity");
	if(a != null) {
	    this.setQuantity(a.getIntValue());
	}
    }

}
