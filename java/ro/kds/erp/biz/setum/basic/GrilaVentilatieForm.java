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
public class GrilaVentilatieForm implements Serializable {

    /**
     * Builds an RDF URI for the field. This RDF is used by the UI to display
     * a localized and friendly name for the field in validation messages.
     *
     * @param fieldName - the name of the field
     * @return an RDF URI by concatenating the string <code>http://www.kds.ro/readybeans/rdf/forms/GrilaVentilatie#</code>
     * with the name of the field.
     */
     public static String uri(String fieldName) {
	return "http://www.kds.ro/readybeans/rdf/forms/GrilaVentilatie#" + fieldName;
     }

    Double lgv;
    Double hgv;
    String pozitionare1;
    String pozitionare2;
    String pozitionare3;
    java.math.BigDecimal sellPrice;
    java.math.BigDecimal entryPrice;
    java.math.BigDecimal price1;
    String businessCategory;
    Integer quantity;

    public GrilaVentilatieForm() {


       this.lgv = new Double(0);
   


       this.hgv = new Double(0);
   


       this.pozitionare1 = "";



       this.pozitionare2 = "";



       this.pozitionare3 = "";



       this.sellPrice = new java.math.BigDecimal(0);



       this.entryPrice = new java.math.BigDecimal(0);



       this.price1 = new java.math.BigDecimal(0);



       this.businessCategory = "";



       this.quantity = new Integer(0);



    }

    public void setLgv(Double newLgv) {
        this.lgv = newLgv;
    }

    public Double getLgv() {
        return lgv;
    }

    public void readLgv(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("lgv");
	if(a != null) {
	    this.setLgv(a.getDoubleValue());
	}
    }

    public void setHgv(Double newHgv) {
        this.hgv = newHgv;
    }

    public Double getHgv() {
        return hgv;
    }

    public void readHgv(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("hgv");
	if(a != null) {
	    this.setHgv(a.getDoubleValue());
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
