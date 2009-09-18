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

    /**
     * Builds an RDF URI for the field. This RDF is used by the UI to display
     * a localized and friendly name for the field in validation messages.
     *
     * @param fieldName - the name of the field
     * @return an RDF URI by concatenating the string <code>http://www.kds.ro/readybeans/rdf/forms/Orders#</code>
     * with the name of the field.
     */
     public static String uri(String fieldName) {
	return "http://www.kds.ro/readybeans/rdf/forms/Orders#" + fieldName;
     }

    String number;
    java.util.Date date;
    Integer clientId;
    String clientName;
    Integer montaj;
    Integer localitate;
    String localitateAlta;
    java.math.BigDecimal distanta;
    String observatii;
    java.math.BigDecimal valoareMontaj;
    java.math.BigDecimal valoareTransport;
    java.math.BigDecimal valoareProduse;
    java.math.BigDecimal total;
    Double tvaPercent;
    java.math.BigDecimal totalTva;
    java.math.BigDecimal discount;
    java.math.BigDecimal totalFinal;
    java.math.BigDecimal totalFinalTva;
    java.math.BigDecimal avans;
    String achitatCu;
    java.math.BigDecimal valoareAvans;
    java.math.BigDecimal payedAmount;
    java.math.BigDecimal invoicedAmount;
    java.math.BigDecimal diferenta;
    String currencyCode;
    java.math.BigDecimal exchangeRate;
    java.util.Date termenLivrare;
    java.util.Date termenLivrare1;
    String adresaMontaj;
    String adresaReper;
    String telefon;
    String contact;
    String deliveryHour;
    String tipDemontare;
    String attribute1;
    String attribute2;
    String attribute3;
    String attribute4;
    String attribute5;
    java.util.Date livrariRStart;
    java.util.Date livrariREnd;
    String livrariCuMontaj;
    Integer offerItemId;
    String productName;
    String productCode;
    java.math.BigDecimal price;
    java.math.BigDecimal productPrice;
    Double priceRatio;
    java.math.BigDecimal quantity;
    java.math.BigDecimal value;
    java.math.BigDecimal tax;
    Integer codMontaj;
    Double montajProcent;
    Boolean montajSeparat;
    String invoiceNumber;
    java.util.Date invoiceDate;
    String invoiceRole;
    java.math.BigDecimal invoiceAmount;
    java.math.BigDecimal invoiceTax;
    java.math.BigDecimal invoiceTotal;
    java.math.BigDecimal invoicePayed;
    java.math.BigDecimal invoiceUnpayed;
    String paymentNumber;
    java.util.Date paymentDate;
    java.math.BigDecimal paymentAmount;

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



       this.valoareMontaj = new java.math.BigDecimal(0);



       this.valoareTransport = new java.math.BigDecimal(0);



       this.valoareProduse = new java.math.BigDecimal(0);



       this.total = new java.math.BigDecimal(0);



       this.tvaPercent = new Double(0);
   


       this.totalTva = new java.math.BigDecimal(0);



       this.discount = new java.math.BigDecimal(0);



       this.totalFinal = new java.math.BigDecimal(0);



       this.totalFinalTva = new java.math.BigDecimal(0);



       this.avans = new java.math.BigDecimal(0);



       this.achitatCu = "";



       this.valoareAvans = new java.math.BigDecimal(0);



       this.payedAmount = new java.math.BigDecimal(0);



       this.invoicedAmount = new java.math.BigDecimal(0);



       this.diferenta = new java.math.BigDecimal(0);



       this.currencyCode = "";



       this.exchangeRate = new java.math.BigDecimal(0);




       // No rule to initialize this.termenLivrare




       // No rule to initialize this.termenLivrare1



       this.adresaMontaj = "";



       this.adresaReper = "";



       this.telefon = "";



       this.contact = "";



       this.deliveryHour = "";



       this.tipDemontare = "";



       this.attribute1 = "";



       this.attribute2 = "";



       this.attribute3 = "";



       this.attribute4 = "";



       this.attribute5 = "";




       // No rule to initialize this.livrariRStart




       // No rule to initialize this.livrariREnd



       this.livrariCuMontaj = "";



       this.offerItemId = new Integer(0);



       this.productName = "";



       this.productCode = "";



       this.price = new java.math.BigDecimal(0);



       this.productPrice = new java.math.BigDecimal(0);



       this.priceRatio = new Double(0);
   


       this.quantity = new java.math.BigDecimal(0);



       this.value = new java.math.BigDecimal(0);



       this.tax = new java.math.BigDecimal(0);



       this.codMontaj = new Integer(0);



       this.montajProcent = new Double(0);
   


       this.montajSeparat = new Boolean(false);



       this.invoiceNumber = "";




       // No rule to initialize this.invoiceDate



       this.invoiceRole = "";



       this.invoiceAmount = new java.math.BigDecimal(0);



       this.invoiceTax = new java.math.BigDecimal(0);



       this.invoiceTotal = new java.math.BigDecimal(0);



       this.invoicePayed = new java.math.BigDecimal(0);



       this.invoiceUnpayed = new java.math.BigDecimal(0);



       this.paymentNumber = "";




       // No rule to initialize this.paymentDate



       this.paymentAmount = new java.math.BigDecimal(0);



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

    public void setValoareMontaj(java.math.BigDecimal newValoareMontaj) {
        this.valoareMontaj = newValoareMontaj;
    }

    public java.math.BigDecimal getValoareMontaj() {
        return valoareMontaj;
    }

    public void readValoareMontaj(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("valoareMontaj");
	if(a != null) {
	    this.setValoareMontaj(a.getDecimalValue());
	}
    }

    public void setValoareTransport(java.math.BigDecimal newValoareTransport) {
        this.valoareTransport = newValoareTransport;
    }

    public java.math.BigDecimal getValoareTransport() {
        return valoareTransport;
    }

    public void readValoareTransport(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("valoareTransport");
	if(a != null) {
	    this.setValoareTransport(a.getDecimalValue());
	}
    }

    public void setValoareProduse(java.math.BigDecimal newValoareProduse) {
        this.valoareProduse = newValoareProduse;
    }

    public java.math.BigDecimal getValoareProduse() {
        return valoareProduse;
    }

    public void readValoareProduse(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("valoareProduse");
	if(a != null) {
	    this.setValoareProduse(a.getDecimalValue());
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

    public void setTvaPercent(Double newTvaPercent) {
        this.tvaPercent = newTvaPercent;
    }

    public Double getTvaPercent() {
        return tvaPercent;
    }

    public void readTvaPercent(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("tvaPercent");
	if(a != null) {
	    this.setTvaPercent(a.getDoubleValue());
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

    public void setPayedAmount(java.math.BigDecimal newPayedAmount) {
        this.payedAmount = newPayedAmount;
    }

    public java.math.BigDecimal getPayedAmount() {
        return payedAmount;
    }

    public void readPayedAmount(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("payedAmount");
	if(a != null) {
	    this.setPayedAmount(a.getDecimalValue());
	}
    }

    public void setInvoicedAmount(java.math.BigDecimal newInvoicedAmount) {
        this.invoicedAmount = newInvoicedAmount;
    }

    public java.math.BigDecimal getInvoicedAmount() {
        return invoicedAmount;
    }

    public void readInvoicedAmount(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("invoicedAmount");
	if(a != null) {
	    this.setInvoicedAmount(a.getDecimalValue());
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

    public void setCurrencyCode(String newCurrencyCode) {
        this.currencyCode = newCurrencyCode;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void readCurrencyCode(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("currencyCode");
	if(a != null) {
	    this.setCurrencyCode(a.getStringValue());
	}
    }

    public void setExchangeRate(java.math.BigDecimal newExchangeRate) {
        this.exchangeRate = newExchangeRate;
    }

    public java.math.BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void readExchangeRate(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("exchangeRate");
	if(a != null) {
	    this.setExchangeRate(a.getDecimalValue());
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

    public void setDeliveryHour(String newDeliveryHour) {
        this.deliveryHour = newDeliveryHour;
    }

    public String getDeliveryHour() {
        return deliveryHour;
    }

    public void readDeliveryHour(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("deliveryHour");
	if(a != null) {
	    this.setDeliveryHour(a.getStringValue());
	}
    }

    public void setTipDemontare(String newTipDemontare) {
        this.tipDemontare = newTipDemontare;
    }

    public String getTipDemontare() {
        return tipDemontare;
    }

    public void readTipDemontare(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("tipDemontare");
	if(a != null) {
	    this.setTipDemontare(a.getStringValue());
	}
    }

    public void setAttribute1(String newAttribute1) {
        this.attribute1 = newAttribute1;
    }

    public String getAttribute1() {
        return attribute1;
    }

    public void readAttribute1(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("attribute1");
	if(a != null) {
	    this.setAttribute1(a.getStringValue());
	}
    }

    public void setAttribute2(String newAttribute2) {
        this.attribute2 = newAttribute2;
    }

    public String getAttribute2() {
        return attribute2;
    }

    public void readAttribute2(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("attribute2");
	if(a != null) {
	    this.setAttribute2(a.getStringValue());
	}
    }

    public void setAttribute3(String newAttribute3) {
        this.attribute3 = newAttribute3;
    }

    public String getAttribute3() {
        return attribute3;
    }

    public void readAttribute3(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("attribute3");
	if(a != null) {
	    this.setAttribute3(a.getStringValue());
	}
    }

    public void setAttribute4(String newAttribute4) {
        this.attribute4 = newAttribute4;
    }

    public String getAttribute4() {
        return attribute4;
    }

    public void readAttribute4(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("attribute4");
	if(a != null) {
	    this.setAttribute4(a.getStringValue());
	}
    }

    public void setAttribute5(String newAttribute5) {
        this.attribute5 = newAttribute5;
    }

    public String getAttribute5() {
        return attribute5;
    }

    public void readAttribute5(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("attribute5");
	if(a != null) {
	    this.setAttribute5(a.getStringValue());
	}
    }

    public void setLivrariRStart(java.util.Date newLivrariRStart) {
        this.livrariRStart = newLivrariRStart;
    }

    public java.util.Date getLivrariRStart() {
        return livrariRStart;
    }


    public void setLivrariREnd(java.util.Date newLivrariREnd) {
        this.livrariREnd = newLivrariREnd;
    }

    public java.util.Date getLivrariREnd() {
        return livrariREnd;
    }


    public void setLivrariCuMontaj(String newLivrariCuMontaj) {
        this.livrariCuMontaj = newLivrariCuMontaj;
    }

    public String getLivrariCuMontaj() {
        return livrariCuMontaj;
    }

    public void readLivrariCuMontaj(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("livrariCuMontaj");
	if(a != null) {
	    this.setLivrariCuMontaj(a.getStringValue());
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

    public void setCodMontaj(Integer newCodMontaj) {
        this.codMontaj = newCodMontaj;
    }

    public Integer getCodMontaj() {
        return codMontaj;
    }

    public void readCodMontaj(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("codMontaj");
	if(a != null) {
	    this.setCodMontaj(a.getIntValue());
	}
    }

    public void setMontajProcent(Double newMontajProcent) {
        this.montajProcent = newMontajProcent;
    }

    public Double getMontajProcent() {
        return montajProcent;
    }

    public void readMontajProcent(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("montajProcent");
	if(a != null) {
	    this.setMontajProcent(a.getDoubleValue());
	}
    }

    public void setMontajSeparat(Boolean newMontajSeparat) {
        this.montajSeparat = newMontajSeparat;
    }

    public Boolean getMontajSeparat() {
        return montajSeparat;
    }

    public void readMontajSeparat(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("montajSeparat");
	if(a != null) {
	    this.setMontajSeparat(a.getBoolValue());
	}
    }

    public void setInvoiceNumber(String newInvoiceNumber) {
        this.invoiceNumber = newInvoiceNumber;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void readInvoiceNumber(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("invoiceNumber");
	if(a != null) {
	    this.setInvoiceNumber(a.getStringValue());
	}
    }

    public void setInvoiceDate(java.util.Date newInvoiceDate) {
        this.invoiceDate = newInvoiceDate;
    }

    public java.util.Date getInvoiceDate() {
        return invoiceDate;
    }


    public void setInvoiceRole(String newInvoiceRole) {
        this.invoiceRole = newInvoiceRole;
    }

    public String getInvoiceRole() {
        return invoiceRole;
    }

    public void readInvoiceRole(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("invoiceRole");
	if(a != null) {
	    this.setInvoiceRole(a.getStringValue());
	}
    }

    public void setInvoiceAmount(java.math.BigDecimal newInvoiceAmount) {
        this.invoiceAmount = newInvoiceAmount;
    }

    public java.math.BigDecimal getInvoiceAmount() {
        return invoiceAmount;
    }

    public void readInvoiceAmount(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("invoiceAmount");
	if(a != null) {
	    this.setInvoiceAmount(a.getDecimalValue());
	}
    }

    public void setInvoiceTax(java.math.BigDecimal newInvoiceTax) {
        this.invoiceTax = newInvoiceTax;
    }

    public java.math.BigDecimal getInvoiceTax() {
        return invoiceTax;
    }

    public void readInvoiceTax(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("invoiceTax");
	if(a != null) {
	    this.setInvoiceTax(a.getDecimalValue());
	}
    }

    public void setInvoiceTotal(java.math.BigDecimal newInvoiceTotal) {
        this.invoiceTotal = newInvoiceTotal;
    }

    public java.math.BigDecimal getInvoiceTotal() {
        return invoiceTotal;
    }

    public void readInvoiceTotal(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("invoiceTotal");
	if(a != null) {
	    this.setInvoiceTotal(a.getDecimalValue());
	}
    }

    public void setInvoicePayed(java.math.BigDecimal newInvoicePayed) {
        this.invoicePayed = newInvoicePayed;
    }

    public java.math.BigDecimal getInvoicePayed() {
        return invoicePayed;
    }

    public void readInvoicePayed(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("invoicePayed");
	if(a != null) {
	    this.setInvoicePayed(a.getDecimalValue());
	}
    }

    public void setInvoiceUnpayed(java.math.BigDecimal newInvoiceUnpayed) {
        this.invoiceUnpayed = newInvoiceUnpayed;
    }

    public java.math.BigDecimal getInvoiceUnpayed() {
        return invoiceUnpayed;
    }

    public void readInvoiceUnpayed(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("invoiceUnpayed");
	if(a != null) {
	    this.setInvoiceUnpayed(a.getDecimalValue());
	}
    }

    public void setPaymentNumber(String newPaymentNumber) {
        this.paymentNumber = newPaymentNumber;
    }

    public String getPaymentNumber() {
        return paymentNumber;
    }

    public void readPaymentNumber(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("paymentNumber");
	if(a != null) {
	    this.setPaymentNumber(a.getStringValue());
	}
    }

    public void setPaymentDate(java.util.Date newPaymentDate) {
        this.paymentDate = newPaymentDate;
    }

    public java.util.Date getPaymentDate() {
        return paymentDate;
    }


    public void setPaymentAmount(java.math.BigDecimal newPaymentAmount) {
        this.paymentAmount = newPaymentAmount;
    }

    public java.math.BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void readPaymentAmount(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("paymentAmount");
	if(a != null) {
	    this.setPaymentAmount(a.getDecimalValue());
	}
    }

}
