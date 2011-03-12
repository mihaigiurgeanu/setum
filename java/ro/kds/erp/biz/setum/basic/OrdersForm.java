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

    String searchText;
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
    java.math.BigDecimal totalCurrency;
    java.math.BigDecimal totalTvaCurrency;
    java.math.BigDecimal totalFinalCurrency;
    java.math.BigDecimal totalFinalTvaCurrency;
    java.math.BigDecimal avansCurrency;
    java.math.BigDecimal payedAmountCurrency;
    java.math.BigDecimal currencyPayedAmount;
    java.math.BigDecimal currencyInvoicedAmount;
    java.math.BigDecimal diferenta;
    java.math.BigDecimal currencyDiferenta;
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
    java.util.Date incasariFromDate;
    java.util.Date incasariToDate;
    java.math.BigDecimal incasariValoare;
    Integer incasariBucIncasate;
    Integer incasariBucNeincasate;
    Integer incasariBucRate;
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
    String proformaNumber;
    java.util.Date proformaDate;
    String proformaRole;
    java.math.BigDecimal proformaAmount;
    java.math.BigDecimal proformaTax;
    Double proformaExchangeRate;
    java.math.BigDecimal proformaTotal;
    Double proformaPercent;
    Boolean proformaUsePercent;
    String proformaComment;
    String proformaContract;
    String proformaObiectiv;
    String proformaCurrency;
    java.math.BigDecimal proformaAmountCurrency;
    java.math.BigDecimal proformaTaxCurrency;
    java.math.BigDecimal proformaTotalCurrency;
    String proformaAttribute1;
    String proformaAttribute2;
    String proformaAttribute3;
    String proformaAttribute4;
    String proformaAttribute5;
    String proformaAttribute6;
    String proformaAttribute7;
    String proformaAttribute8;
    String proformaAttribute9;
    String proformaAttribute10;
    String proformaAttribute11;
    String proformaAttribute12;
    String proformaAttribute13;
    String proformaAttribute14;
    String proformaAttribute15;
    String invoiceNumber;
    java.util.Date invoiceDate;
    String invoiceRole;
    java.math.BigDecimal invoiceAmount;
    java.math.BigDecimal invoiceTax;
    Double invoiceExchangeRate;
    java.math.BigDecimal invoiceTotal;
    java.math.BigDecimal invoicePayed;
    java.math.BigDecimal invoiceUnpayed;
    String paymentNumber;
    java.util.Date paymentDate;
    java.math.BigDecimal paymentAmount;
    Double paymentExchangeRate;

    public OrdersForm() {


       this.searchText = "";



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



       this.totalCurrency = new java.math.BigDecimal(0);



       this.totalTvaCurrency = new java.math.BigDecimal(0);



       this.totalFinalCurrency = new java.math.BigDecimal(0);



       this.totalFinalTvaCurrency = new java.math.BigDecimal(0);



       this.avansCurrency = new java.math.BigDecimal(0);



       this.payedAmountCurrency = new java.math.BigDecimal(0);



       this.currencyPayedAmount = new java.math.BigDecimal(0);



       this.currencyInvoicedAmount = new java.math.BigDecimal(0);



       this.diferenta = new java.math.BigDecimal(0);



       this.currencyDiferenta = new java.math.BigDecimal(0);



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




       // No rule to initialize this.incasariFromDate




       // No rule to initialize this.incasariToDate



       this.incasariValoare = new java.math.BigDecimal(0);



       this.incasariBucIncasate = new Integer(0);



       this.incasariBucNeincasate = new Integer(0);



       this.incasariBucRate = new Integer(0);



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



       this.proformaNumber = "";




       // No rule to initialize this.proformaDate



       this.proformaRole = "";



       this.proformaAmount = new java.math.BigDecimal(0);



       this.proformaTax = new java.math.BigDecimal(0);



       this.proformaExchangeRate = new Double(0);
   


       this.proformaTotal = new java.math.BigDecimal(0);



       this.proformaPercent = new Double(0);
   


       this.proformaUsePercent = new Boolean(false);



       this.proformaComment = "";



       this.proformaContract = "";



       this.proformaObiectiv = "";



       this.proformaCurrency = "";



       this.proformaAmountCurrency = new java.math.BigDecimal(0);



       this.proformaTaxCurrency = new java.math.BigDecimal(0);



       this.proformaTotalCurrency = new java.math.BigDecimal(0);



       this.proformaAttribute1 = "";



       this.proformaAttribute2 = "";



       this.proformaAttribute3 = "";



       this.proformaAttribute4 = "";



       this.proformaAttribute5 = "";



       this.proformaAttribute6 = "";



       this.proformaAttribute7 = "";



       this.proformaAttribute8 = "";



       this.proformaAttribute9 = "";



       this.proformaAttribute10 = "";



       this.proformaAttribute11 = "";



       this.proformaAttribute12 = "";



       this.proformaAttribute13 = "";



       this.proformaAttribute14 = "";



       this.proformaAttribute15 = "";



       this.invoiceNumber = "";




       // No rule to initialize this.invoiceDate



       this.invoiceRole = "";



       this.invoiceAmount = new java.math.BigDecimal(0);



       this.invoiceTax = new java.math.BigDecimal(0);



       this.invoiceExchangeRate = new Double(0);
   


       this.invoiceTotal = new java.math.BigDecimal(0);



       this.invoicePayed = new java.math.BigDecimal(0);



       this.invoiceUnpayed = new java.math.BigDecimal(0);



       this.paymentNumber = "";




       // No rule to initialize this.paymentDate



       this.paymentAmount = new java.math.BigDecimal(0);



       this.paymentExchangeRate = new Double(0);
   


    }

    public void setSearchText(String newSearchText) {
        this.searchText = newSearchText;
    }

    public String getSearchText() {
        return searchText;
    }

    public void readSearchText(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("searchText");
	if(a != null) {
	    this.setSearchText(a.getStringValue());
	}
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

    public void setTotalCurrency(java.math.BigDecimal newTotalCurrency) {
        this.totalCurrency = newTotalCurrency;
    }

    public java.math.BigDecimal getTotalCurrency() {
        return totalCurrency;
    }

    public void readTotalCurrency(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("totalCurrency");
	if(a != null) {
	    this.setTotalCurrency(a.getDecimalValue());
	}
    }

    public void setTotalTvaCurrency(java.math.BigDecimal newTotalTvaCurrency) {
        this.totalTvaCurrency = newTotalTvaCurrency;
    }

    public java.math.BigDecimal getTotalTvaCurrency() {
        return totalTvaCurrency;
    }

    public void readTotalTvaCurrency(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("totalTvaCurrency");
	if(a != null) {
	    this.setTotalTvaCurrency(a.getDecimalValue());
	}
    }

    public void setTotalFinalCurrency(java.math.BigDecimal newTotalFinalCurrency) {
        this.totalFinalCurrency = newTotalFinalCurrency;
    }

    public java.math.BigDecimal getTotalFinalCurrency() {
        return totalFinalCurrency;
    }

    public void readTotalFinalCurrency(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("totalFinalCurrency");
	if(a != null) {
	    this.setTotalFinalCurrency(a.getDecimalValue());
	}
    }

    public void setTotalFinalTvaCurrency(java.math.BigDecimal newTotalFinalTvaCurrency) {
        this.totalFinalTvaCurrency = newTotalFinalTvaCurrency;
    }

    public java.math.BigDecimal getTotalFinalTvaCurrency() {
        return totalFinalTvaCurrency;
    }

    public void readTotalFinalTvaCurrency(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("totalFinalTvaCurrency");
	if(a != null) {
	    this.setTotalFinalTvaCurrency(a.getDecimalValue());
	}
    }

    public void setAvansCurrency(java.math.BigDecimal newAvansCurrency) {
        this.avansCurrency = newAvansCurrency;
    }

    public java.math.BigDecimal getAvansCurrency() {
        return avansCurrency;
    }

    public void readAvansCurrency(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("avansCurrency");
	if(a != null) {
	    this.setAvansCurrency(a.getDecimalValue());
	}
    }

    public void setPayedAmountCurrency(java.math.BigDecimal newPayedAmountCurrency) {
        this.payedAmountCurrency = newPayedAmountCurrency;
    }

    public java.math.BigDecimal getPayedAmountCurrency() {
        return payedAmountCurrency;
    }

    public void readPayedAmountCurrency(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("payedAmountCurrency");
	if(a != null) {
	    this.setPayedAmountCurrency(a.getDecimalValue());
	}
    }

    public void setCurrencyPayedAmount(java.math.BigDecimal newCurrencyPayedAmount) {
        this.currencyPayedAmount = newCurrencyPayedAmount;
    }

    public java.math.BigDecimal getCurrencyPayedAmount() {
        return currencyPayedAmount;
    }

    public void readCurrencyPayedAmount(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("currencyPayedAmount");
	if(a != null) {
	    this.setCurrencyPayedAmount(a.getDecimalValue());
	}
    }

    public void setCurrencyInvoicedAmount(java.math.BigDecimal newCurrencyInvoicedAmount) {
        this.currencyInvoicedAmount = newCurrencyInvoicedAmount;
    }

    public java.math.BigDecimal getCurrencyInvoicedAmount() {
        return currencyInvoicedAmount;
    }

    public void readCurrencyInvoicedAmount(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("currencyInvoicedAmount");
	if(a != null) {
	    this.setCurrencyInvoicedAmount(a.getDecimalValue());
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

    public void setCurrencyDiferenta(java.math.BigDecimal newCurrencyDiferenta) {
        this.currencyDiferenta = newCurrencyDiferenta;
    }

    public java.math.BigDecimal getCurrencyDiferenta() {
        return currencyDiferenta;
    }

    public void readCurrencyDiferenta(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("currencyDiferenta");
	if(a != null) {
	    this.setCurrencyDiferenta(a.getDecimalValue());
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

    public void setIncasariFromDate(java.util.Date newIncasariFromDate) {
        this.incasariFromDate = newIncasariFromDate;
    }

    public java.util.Date getIncasariFromDate() {
        return incasariFromDate;
    }


    public void setIncasariToDate(java.util.Date newIncasariToDate) {
        this.incasariToDate = newIncasariToDate;
    }

    public java.util.Date getIncasariToDate() {
        return incasariToDate;
    }


    public void setIncasariValoare(java.math.BigDecimal newIncasariValoare) {
        this.incasariValoare = newIncasariValoare;
    }

    public java.math.BigDecimal getIncasariValoare() {
        return incasariValoare;
    }

    public void readIncasariValoare(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("incasariValoare");
	if(a != null) {
	    this.setIncasariValoare(a.getDecimalValue());
	}
    }

    public void setIncasariBucIncasate(Integer newIncasariBucIncasate) {
        this.incasariBucIncasate = newIncasariBucIncasate;
    }

    public Integer getIncasariBucIncasate() {
        return incasariBucIncasate;
    }

    public void readIncasariBucIncasate(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("incasariBucIncasate");
	if(a != null) {
	    this.setIncasariBucIncasate(a.getIntValue());
	}
    }

    public void setIncasariBucNeincasate(Integer newIncasariBucNeincasate) {
        this.incasariBucNeincasate = newIncasariBucNeincasate;
    }

    public Integer getIncasariBucNeincasate() {
        return incasariBucNeincasate;
    }

    public void readIncasariBucNeincasate(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("incasariBucNeincasate");
	if(a != null) {
	    this.setIncasariBucNeincasate(a.getIntValue());
	}
    }

    public void setIncasariBucRate(Integer newIncasariBucRate) {
        this.incasariBucRate = newIncasariBucRate;
    }

    public Integer getIncasariBucRate() {
        return incasariBucRate;
    }

    public void readIncasariBucRate(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("incasariBucRate");
	if(a != null) {
	    this.setIncasariBucRate(a.getIntValue());
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

    public void setProformaNumber(String newProformaNumber) {
        this.proformaNumber = newProformaNumber;
    }

    public String getProformaNumber() {
        return proformaNumber;
    }

    public void readProformaNumber(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("proformaNumber");
	if(a != null) {
	    this.setProformaNumber(a.getStringValue());
	}
    }

    public void setProformaDate(java.util.Date newProformaDate) {
        this.proformaDate = newProformaDate;
    }

    public java.util.Date getProformaDate() {
        return proformaDate;
    }


    public void setProformaRole(String newProformaRole) {
        this.proformaRole = newProformaRole;
    }

    public String getProformaRole() {
        return proformaRole;
    }

    public void readProformaRole(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("proformaRole");
	if(a != null) {
	    this.setProformaRole(a.getStringValue());
	}
    }

    public void setProformaAmount(java.math.BigDecimal newProformaAmount) {
        this.proformaAmount = newProformaAmount;
    }

    public java.math.BigDecimal getProformaAmount() {
        return proformaAmount;
    }

    public void readProformaAmount(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("proformaAmount");
	if(a != null) {
	    this.setProformaAmount(a.getDecimalValue());
	}
    }

    public void setProformaTax(java.math.BigDecimal newProformaTax) {
        this.proformaTax = newProformaTax;
    }

    public java.math.BigDecimal getProformaTax() {
        return proformaTax;
    }

    public void readProformaTax(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("proformaTax");
	if(a != null) {
	    this.setProformaTax(a.getDecimalValue());
	}
    }

    public void setProformaExchangeRate(Double newProformaExchangeRate) {
        this.proformaExchangeRate = newProformaExchangeRate;
    }

    public Double getProformaExchangeRate() {
        return proformaExchangeRate;
    }

    public void readProformaExchangeRate(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("proformaExchangeRate");
	if(a != null) {
	    this.setProformaExchangeRate(a.getDoubleValue());
	}
    }

    public void setProformaTotal(java.math.BigDecimal newProformaTotal) {
        this.proformaTotal = newProformaTotal;
    }

    public java.math.BigDecimal getProformaTotal() {
        return proformaTotal;
    }

    public void readProformaTotal(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("proformaTotal");
	if(a != null) {
	    this.setProformaTotal(a.getDecimalValue());
	}
    }

    public void setProformaPercent(Double newProformaPercent) {
        this.proformaPercent = newProformaPercent;
    }

    public Double getProformaPercent() {
        return proformaPercent;
    }

    public void readProformaPercent(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("proformaPercent");
	if(a != null) {
	    this.setProformaPercent(a.getDoubleValue());
	}
    }

    public void setProformaUsePercent(Boolean newProformaUsePercent) {
        this.proformaUsePercent = newProformaUsePercent;
    }

    public Boolean getProformaUsePercent() {
        return proformaUsePercent;
    }

    public void readProformaUsePercent(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("proformaUsePercent");
	if(a != null) {
	    this.setProformaUsePercent(a.getBoolValue());
	}
    }

    public void setProformaComment(String newProformaComment) {
        this.proformaComment = newProformaComment;
    }

    public String getProformaComment() {
        return proformaComment;
    }

    public void readProformaComment(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("proformaComment");
	if(a != null) {
	    this.setProformaComment(a.getStringValue());
	}
    }

    public void setProformaContract(String newProformaContract) {
        this.proformaContract = newProformaContract;
    }

    public String getProformaContract() {
        return proformaContract;
    }

    public void readProformaContract(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("proformaContract");
	if(a != null) {
	    this.setProformaContract(a.getStringValue());
	}
    }

    public void setProformaObiectiv(String newProformaObiectiv) {
        this.proformaObiectiv = newProformaObiectiv;
    }

    public String getProformaObiectiv() {
        return proformaObiectiv;
    }

    public void readProformaObiectiv(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("proformaObiectiv");
	if(a != null) {
	    this.setProformaObiectiv(a.getStringValue());
	}
    }

    public void setProformaCurrency(String newProformaCurrency) {
        this.proformaCurrency = newProformaCurrency;
    }

    public String getProformaCurrency() {
        return proformaCurrency;
    }

    public void readProformaCurrency(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("proformaCurrency");
	if(a != null) {
	    this.setProformaCurrency(a.getStringValue());
	}
    }

    public void setProformaAmountCurrency(java.math.BigDecimal newProformaAmountCurrency) {
        this.proformaAmountCurrency = newProformaAmountCurrency;
    }

    public java.math.BigDecimal getProformaAmountCurrency() {
        return proformaAmountCurrency;
    }

    public void readProformaAmountCurrency(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("proformaAmountCurrency");
	if(a != null) {
	    this.setProformaAmountCurrency(a.getDecimalValue());
	}
    }

    public void setProformaTaxCurrency(java.math.BigDecimal newProformaTaxCurrency) {
        this.proformaTaxCurrency = newProformaTaxCurrency;
    }

    public java.math.BigDecimal getProformaTaxCurrency() {
        return proformaTaxCurrency;
    }

    public void readProformaTaxCurrency(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("proformaTaxCurrency");
	if(a != null) {
	    this.setProformaTaxCurrency(a.getDecimalValue());
	}
    }

    public void setProformaTotalCurrency(java.math.BigDecimal newProformaTotalCurrency) {
        this.proformaTotalCurrency = newProformaTotalCurrency;
    }

    public java.math.BigDecimal getProformaTotalCurrency() {
        return proformaTotalCurrency;
    }

    public void readProformaTotalCurrency(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("proformaTotalCurrency");
	if(a != null) {
	    this.setProformaTotalCurrency(a.getDecimalValue());
	}
    }

    public void setProformaAttribute1(String newProformaAttribute1) {
        this.proformaAttribute1 = newProformaAttribute1;
    }

    public String getProformaAttribute1() {
        return proformaAttribute1;
    }

    public void readProformaAttribute1(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("proformaAttribute1");
	if(a != null) {
	    this.setProformaAttribute1(a.getStringValue());
	}
    }

    public void setProformaAttribute2(String newProformaAttribute2) {
        this.proformaAttribute2 = newProformaAttribute2;
    }

    public String getProformaAttribute2() {
        return proformaAttribute2;
    }

    public void readProformaAttribute2(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("proformaAttribute2");
	if(a != null) {
	    this.setProformaAttribute2(a.getStringValue());
	}
    }

    public void setProformaAttribute3(String newProformaAttribute3) {
        this.proformaAttribute3 = newProformaAttribute3;
    }

    public String getProformaAttribute3() {
        return proformaAttribute3;
    }

    public void readProformaAttribute3(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("proformaAttribute3");
	if(a != null) {
	    this.setProformaAttribute3(a.getStringValue());
	}
    }

    public void setProformaAttribute4(String newProformaAttribute4) {
        this.proformaAttribute4 = newProformaAttribute4;
    }

    public String getProformaAttribute4() {
        return proformaAttribute4;
    }

    public void readProformaAttribute4(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("proformaAttribute4");
	if(a != null) {
	    this.setProformaAttribute4(a.getStringValue());
	}
    }

    public void setProformaAttribute5(String newProformaAttribute5) {
        this.proformaAttribute5 = newProformaAttribute5;
    }

    public String getProformaAttribute5() {
        return proformaAttribute5;
    }

    public void readProformaAttribute5(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("proformaAttribute5");
	if(a != null) {
	    this.setProformaAttribute5(a.getStringValue());
	}
    }

    public void setProformaAttribute6(String newProformaAttribute6) {
        this.proformaAttribute6 = newProformaAttribute6;
    }

    public String getProformaAttribute6() {
        return proformaAttribute6;
    }

    public void readProformaAttribute6(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("proformaAttribute6");
	if(a != null) {
	    this.setProformaAttribute6(a.getStringValue());
	}
    }

    public void setProformaAttribute7(String newProformaAttribute7) {
        this.proformaAttribute7 = newProformaAttribute7;
    }

    public String getProformaAttribute7() {
        return proformaAttribute7;
    }

    public void readProformaAttribute7(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("proformaAttribute7");
	if(a != null) {
	    this.setProformaAttribute7(a.getStringValue());
	}
    }

    public void setProformaAttribute8(String newProformaAttribute8) {
        this.proformaAttribute8 = newProformaAttribute8;
    }

    public String getProformaAttribute8() {
        return proformaAttribute8;
    }

    public void readProformaAttribute8(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("proformaAttribute8");
	if(a != null) {
	    this.setProformaAttribute8(a.getStringValue());
	}
    }

    public void setProformaAttribute9(String newProformaAttribute9) {
        this.proformaAttribute9 = newProformaAttribute9;
    }

    public String getProformaAttribute9() {
        return proformaAttribute9;
    }

    public void readProformaAttribute9(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("proformaAttribute9");
	if(a != null) {
	    this.setProformaAttribute9(a.getStringValue());
	}
    }

    public void setProformaAttribute10(String newProformaAttribute10) {
        this.proformaAttribute10 = newProformaAttribute10;
    }

    public String getProformaAttribute10() {
        return proformaAttribute10;
    }

    public void readProformaAttribute10(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("proformaAttribute10");
	if(a != null) {
	    this.setProformaAttribute10(a.getStringValue());
	}
    }

    public void setProformaAttribute11(String newProformaAttribute11) {
        this.proformaAttribute11 = newProformaAttribute11;
    }

    public String getProformaAttribute11() {
        return proformaAttribute11;
    }

    public void readProformaAttribute11(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("proformaAttribute11");
	if(a != null) {
	    this.setProformaAttribute11(a.getStringValue());
	}
    }

    public void setProformaAttribute12(String newProformaAttribute12) {
        this.proformaAttribute12 = newProformaAttribute12;
    }

    public String getProformaAttribute12() {
        return proformaAttribute12;
    }

    public void readProformaAttribute12(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("proformaAttribute12");
	if(a != null) {
	    this.setProformaAttribute12(a.getStringValue());
	}
    }

    public void setProformaAttribute13(String newProformaAttribute13) {
        this.proformaAttribute13 = newProformaAttribute13;
    }

    public String getProformaAttribute13() {
        return proformaAttribute13;
    }

    public void readProformaAttribute13(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("proformaAttribute13");
	if(a != null) {
	    this.setProformaAttribute13(a.getStringValue());
	}
    }

    public void setProformaAttribute14(String newProformaAttribute14) {
        this.proformaAttribute14 = newProformaAttribute14;
    }

    public String getProformaAttribute14() {
        return proformaAttribute14;
    }

    public void readProformaAttribute14(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("proformaAttribute14");
	if(a != null) {
	    this.setProformaAttribute14(a.getStringValue());
	}
    }

    public void setProformaAttribute15(String newProformaAttribute15) {
        this.proformaAttribute15 = newProformaAttribute15;
    }

    public String getProformaAttribute15() {
        return proformaAttribute15;
    }

    public void readProformaAttribute15(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("proformaAttribute15");
	if(a != null) {
	    this.setProformaAttribute15(a.getStringValue());
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

    public void setInvoiceExchangeRate(Double newInvoiceExchangeRate) {
        this.invoiceExchangeRate = newInvoiceExchangeRate;
    }

    public Double getInvoiceExchangeRate() {
        return invoiceExchangeRate;
    }

    public void readInvoiceExchangeRate(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("invoiceExchangeRate");
	if(a != null) {
	    this.setInvoiceExchangeRate(a.getDoubleValue());
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

    public void setPaymentExchangeRate(Double newPaymentExchangeRate) {
        this.paymentExchangeRate = newPaymentExchangeRate;
    }

    public Double getPaymentExchangeRate() {
        return paymentExchangeRate;
    }

    public void readPaymentExchangeRate(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("paymentExchangeRate");
	if(a != null) {
	    this.setPaymentExchangeRate(a.getDoubleValue());
	}
    }

}
