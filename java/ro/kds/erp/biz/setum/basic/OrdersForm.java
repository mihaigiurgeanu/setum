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
public class OrdersForm implements Serializable {
        
    String number;
    java.util.Date date;
    Integer clientId;
    String clientName;
    Integer montaj;
    Integer localitate;
    String localitateAlta;
    java.math.BigDecimal distanta;
    String observatii;
    java.math.BigDecimal total;
    java.math.BigDecimal totalTva;
    java.math.BigDecimal discount;
    java.math.BigDecimal totalFinal;
    java.math.BigDecimal totalFinalTva;
    java.math.BigDecimal avans;
    String achitatCu;
    java.math.BigDecimal valoareAvans;
    java.math.BigDecimal diferenta;
    java.util.Date termenLivrare;
    java.util.Date termenLivrare1;
    String adresaMontaj;
    String adresaReper;
    String telefon;
    String contact;
    Integer offerItemId;
    String productName;
    String productCode;
    java.math.BigDecimal price;
    java.math.BigDecimal productPrice;
    Double priceRatio;
    java.math.BigDecimal quantity;
    java.math.BigDecimal value;
    java.math.BigDecimal tax;

    public OrdersForm() {


       this.number = "";




       // No rule to initialize this.date



       this.clientId = new Integer(0);



       this.clientName = "";



       this.montaj = new Integer(0);



       this.localitate = new Integer(0);



       this.localitateAlta = "";



       this.distanta = new java.math.BigDecimal(0);



       this.observatii = "";



       this.total = new java.math.BigDecimal(0);



       this.totalTva = new java.math.BigDecimal(0);



       this.discount = new java.math.BigDecimal(0);



       this.totalFinal = new java.math.BigDecimal(0);



       this.totalFinalTva = new java.math.BigDecimal(0);



       this.avans = new java.math.BigDecimal(0);



       this.achitatCu = "";



       this.valoareAvans = new java.math.BigDecimal(0);



       this.diferenta = new java.math.BigDecimal(0);




       // No rule to initialize this.termenLivrare




       // No rule to initialize this.termenLivrare1



       this.adresaMontaj = "";



       this.adresaReper = "";



       this.telefon = "";



       this.contact = "";



       this.offerItemId = new Integer(0);



       this.productName = "";



       this.productCode = "";



       this.price = new java.math.BigDecimal(0);



       this.productPrice = new java.math.BigDecimal(0);



       this.priceRatio = new Double(0);
   


       this.quantity = new java.math.BigDecimal(0);



       this.value = new java.math.BigDecimal(0);



       this.tax = new java.math.BigDecimal(0);



    }

    public void setNumber(String newNumber) {
        this.number = newNumber;
    }

    public String getNumber() {
        return number;
    }

    public void readNumber(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("number");
	if(a != null) {
	    this.setNumber(a.getStringValue());
	}
    }

    public void setDate(java.util.Date newDate) {
        this.date = newDate;
    }

    public java.util.Date getDate() {
        return date;
    }


    public void setClientId(Integer newClientId) {
        this.clientId = newClientId;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void readClientId(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("clientId");
	if(a != null) {
	    this.setClientId(a.getIntValue());
	}
    }

    public void setClientName(String newClientName) {
        this.clientName = newClientName;
    }

    public String getClientName() {
        return clientName;
    }

    public void readClientName(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("clientName");
	if(a != null) {
	    this.setClientName(a.getStringValue());
	}
    }

    public void setMontaj(Integer newMontaj) {
        this.montaj = newMontaj;
    }

    public Integer getMontaj() {
        return montaj;
    }

    public void readMontaj(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("montaj");
	if(a != null) {
	    this.setMontaj(a.getIntValue());
	}
    }

    public void setLocalitate(Integer newLocalitate) {
        this.localitate = newLocalitate;
    }

    public Integer getLocalitate() {
        return localitate;
    }

    public void readLocalitate(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("localitate");
	if(a != null) {
	    this.setLocalitate(a.getIntValue());
	}
    }

    public void setLocalitateAlta(String newLocalitateAlta) {
        this.localitateAlta = newLocalitateAlta;
    }

    public String getLocalitateAlta() {
        return localitateAlta;
    }

    public void readLocalitateAlta(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("localitateAlta");
	if(a != null) {
	    this.setLocalitateAlta(a.getStringValue());
	}
    }

    public void setDistanta(java.math.BigDecimal newDistanta) {
        this.distanta = newDistanta;
    }

    public java.math.BigDecimal getDistanta() {
        return distanta;
    }

    public void readDistanta(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("distanta");
	if(a != null) {
	    this.setDistanta(a.getDecimalValue());
	}
    }

    public void setObservatii(String newObservatii) {
        this.observatii = newObservatii;
    }

    public String getObservatii() {
        return observatii;
    }

    public void readObservatii(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("observatii");
	if(a != null) {
	    this.setObservatii(a.getStringValue());
	}
    }

    public void setTotal(java.math.BigDecimal newTotal) {
        this.total = newTotal;
    }

    public java.math.BigDecimal getTotal() {
        return total;
    }

    public void readTotal(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("total");
	if(a != null) {
	    this.setTotal(a.getDecimalValue());
	}
    }

    public void setTotalTva(java.math.BigDecimal newTotalTva) {
        this.totalTva = newTotalTva;
    }

    public java.math.BigDecimal getTotalTva() {
        return totalTva;
    }

    public void readTotalTva(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("totalTva");
	if(a != null) {
	    this.setTotalTva(a.getDecimalValue());
	}
    }

    public void setDiscount(java.math.BigDecimal newDiscount) {
        this.discount = newDiscount;
    }

    public java.math.BigDecimal getDiscount() {
        return discount;
    }

    public void readDiscount(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("discount");
	if(a != null) {
	    this.setDiscount(a.getDecimalValue());
	}
    }

    public void setTotalFinal(java.math.BigDecimal newTotalFinal) {
        this.totalFinal = newTotalFinal;
    }

    public java.math.BigDecimal getTotalFinal() {
        return totalFinal;
    }

    public void readTotalFinal(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("totalFinal");
	if(a != null) {
	    this.setTotalFinal(a.getDecimalValue());
	}
    }

    public void setTotalFinalTva(java.math.BigDecimal newTotalFinalTva) {
        this.totalFinalTva = newTotalFinalTva;
    }

    public java.math.BigDecimal getTotalFinalTva() {
        return totalFinalTva;
    }

    public void readTotalFinalTva(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("totalFinalTva");
	if(a != null) {
	    this.setTotalFinalTva(a.getDecimalValue());
	}
    }

    public void setAvans(java.math.BigDecimal newAvans) {
        this.avans = newAvans;
    }

    public java.math.BigDecimal getAvans() {
        return avans;
    }

    public void readAvans(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("avans");
	if(a != null) {
	    this.setAvans(a.getDecimalValue());
	}
    }

    public void setAchitatCu(String newAchitatCu) {
        this.achitatCu = newAchitatCu;
    }

    public String getAchitatCu() {
        return achitatCu;
    }

    public void readAchitatCu(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("achitatCu");
	if(a != null) {
	    this.setAchitatCu(a.getStringValue());
	}
    }

    public void setValoareAvans(java.math.BigDecimal newValoareAvans) {
        this.valoareAvans = newValoareAvans;
    }

    public java.math.BigDecimal getValoareAvans() {
        return valoareAvans;
    }

    public void readValoareAvans(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("valoareAvans");
	if(a != null) {
	    this.setValoareAvans(a.getDecimalValue());
	}
    }

    public void setDiferenta(java.math.BigDecimal newDiferenta) {
        this.diferenta = newDiferenta;
    }

    public java.math.BigDecimal getDiferenta() {
        return diferenta;
    }

    public void readDiferenta(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("diferenta");
	if(a != null) {
	    this.setDiferenta(a.getDecimalValue());
	}
    }

    public void setTermenLivrare(java.util.Date newTermenLivrare) {
        this.termenLivrare = newTermenLivrare;
    }

    public java.util.Date getTermenLivrare() {
        return termenLivrare;
    }


    public void setTermenLivrare1(java.util.Date newTermenLivrare1) {
        this.termenLivrare1 = newTermenLivrare1;
    }

    public java.util.Date getTermenLivrare1() {
        return termenLivrare1;
    }


    public void setAdresaMontaj(String newAdresaMontaj) {
        this.adresaMontaj = newAdresaMontaj;
    }

    public String getAdresaMontaj() {
        return adresaMontaj;
    }

    public void readAdresaMontaj(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("adresaMontaj");
	if(a != null) {
	    this.setAdresaMontaj(a.getStringValue());
	}
    }

    public void setAdresaReper(String newAdresaReper) {
        this.adresaReper = newAdresaReper;
    }

    public String getAdresaReper() {
        return adresaReper;
    }

    public void readAdresaReper(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("adresaReper");
	if(a != null) {
	    this.setAdresaReper(a.getStringValue());
	}
    }

    public void setTelefon(String newTelefon) {
        this.telefon = newTelefon;
    }

    public String getTelefon() {
        return telefon;
    }

    public void readTelefon(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("telefon");
	if(a != null) {
	    this.setTelefon(a.getStringValue());
	}
    }

    public void setContact(String newContact) {
        this.contact = newContact;
    }

    public String getContact() {
        return contact;
    }

    public void readContact(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("contact");
	if(a != null) {
	    this.setContact(a.getStringValue());
	}
    }

    public void setOfferItemId(Integer newOfferItemId) {
        this.offerItemId = newOfferItemId;
    }

    public Integer getOfferItemId() {
        return offerItemId;
    }

    public void readOfferItemId(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("offerItemId");
	if(a != null) {
	    this.setOfferItemId(a.getIntValue());
	}
    }

    public void setProductName(String newProductName) {
        this.productName = newProductName;
    }

    public String getProductName() {
        return productName;
    }

    public void readProductName(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("productName");
	if(a != null) {
	    this.setProductName(a.getStringValue());
	}
    }

    public void setProductCode(String newProductCode) {
        this.productCode = newProductCode;
    }

    public String getProductCode() {
        return productCode;
    }

    public void readProductCode(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("productCode");
	if(a != null) {
	    this.setProductCode(a.getStringValue());
	}
    }

    public void setPrice(java.math.BigDecimal newPrice) {
        this.price = newPrice;
    }

    public java.math.BigDecimal getPrice() {
        return price;
    }

    public void readPrice(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("price");
	if(a != null) {
	    this.setPrice(a.getDecimalValue());
	}
    }

    public void setProductPrice(java.math.BigDecimal newProductPrice) {
        this.productPrice = newProductPrice;
    }

    public java.math.BigDecimal getProductPrice() {
        return productPrice;
    }

    public void readProductPrice(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("productPrice");
	if(a != null) {
	    this.setProductPrice(a.getDecimalValue());
	}
    }

    public void setPriceRatio(Double newPriceRatio) {
        this.priceRatio = newPriceRatio;
    }

    public Double getPriceRatio() {
        return priceRatio;
    }

    public void readPriceRatio(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("priceRatio");
	if(a != null) {
	    this.setPriceRatio(a.getDoubleValue());
	}
    }

    public void setQuantity(java.math.BigDecimal newQuantity) {
        this.quantity = newQuantity;
    }

    public java.math.BigDecimal getQuantity() {
        return quantity;
    }

    public void readQuantity(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("quantity");
	if(a != null) {
	    this.setQuantity(a.getDecimalValue());
	}
    }

    public void setValue(java.math.BigDecimal newValue) {
        this.value = newValue;
    }

    public java.math.BigDecimal getValue() {
        return value;
    }

    public void readValue(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("value");
	if(a != null) {
	    this.setValue(a.getDecimalValue());
	}
    }

    public void setTax(java.math.BigDecimal newTax) {
        this.tax = newTax;
    }

    public java.math.BigDecimal getTax() {
        return tax;
    }

    public void readTax(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("tax");
	if(a != null) {
	    this.setTax(a.getDecimalValue());
	}
    }

}
