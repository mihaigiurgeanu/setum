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
public class ArbitraryOfferForm implements Serializable {

    /**
     * Builds an RDF URI for the field. This RDF is used by the UI to display
     * a localized and friendly name for the field in validation messages.
     *
     * @param fieldName - the name of the field
     * @return an RDF URI by concatenating the string <code>http://www.kds.ro/readybeans/rdf/forms/ArbitraryOffer#</code>
     * with the name of the field.
     */
     public static String uri(String fieldName) {
	return "http://www.kds.ro/readybeans/rdf/forms/ArbitraryOffer#" + fieldName;
     }

    String no;
    java.util.Date docDate;
    java.util.Date dateFrom;
    java.util.Date dateTo;
    Boolean discontinued;
    Integer period;
    Integer clientId;
    String clientName;
    String name;
    String description;
    String comment;
    String contract;
    String anexa;
    String terms;
    String attribute1;
    String attribute2;
    String attribute3;
    String attribute4;
    String attribute5;
    String attribute6;
    String attribute7;
    String attribute8;
    String attribute9;
    String attribute10;
    Integer productId;
    java.math.BigDecimal price;
    java.math.BigDecimal quantity;
    java.math.BigDecimal value;
    java.math.BigDecimal vatPrice;
    java.math.BigDecimal vatValue;
    Double relativeGain;
    java.math.BigDecimal absoluteGain;
    String productCategory;
    String productCode;
    String productName;
    java.math.BigDecimal entryPrice;
    java.math.BigDecimal sellPrice;
    String businessCategory;
    Integer montajId;
    Double montajProcent;
    Boolean montajSeparat;
    Integer locationId;
    String otherLocation;
    java.math.BigDecimal distance;
    Integer deliveries;
    java.math.BigDecimal valMontaj;
    java.math.BigDecimal valTransport;
    Integer clientContactId;
    String contact;
    String clientContactName;
    String clientContactPhone;
    String clientContactFax;
    String clientContactMobile;
    String clientContactEmail;

    public ArbitraryOfferForm() {


       this.no = "";




       // No rule to initialize this.docDate




       // No rule to initialize this.dateFrom




       // No rule to initialize this.dateTo



       this.discontinued = new Boolean(false);



       this.period = new Integer(0);



       this.clientId = new Integer(0);



       this.clientName = "";



       this.name = "";



       this.description = "";



       this.comment = "";



       this.contract = "";



       this.anexa = "";



       this.terms = "";



       this.attribute1 = "";



       this.attribute2 = "";



       this.attribute3 = "";



       this.attribute4 = "";



       this.attribute5 = "";



       this.attribute6 = "";



       this.attribute7 = "";



       this.attribute8 = "";



       this.attribute9 = "";



       this.attribute10 = "";



       this.productId = new Integer(0);



       this.price = new java.math.BigDecimal(0);



       this.quantity = new java.math.BigDecimal(0);



       this.value = new java.math.BigDecimal(0);



       this.vatPrice = new java.math.BigDecimal(0);



       this.vatValue = new java.math.BigDecimal(0);



       this.relativeGain = new Double(0);
   


       this.absoluteGain = new java.math.BigDecimal(0);



       this.productCategory = "";



       this.productCode = "";



       this.productName = "";



       this.entryPrice = new java.math.BigDecimal(0);



       this.sellPrice = new java.math.BigDecimal(0);



       this.businessCategory = "";



       this.montajId = new Integer(0);



       this.montajProcent = new Double(0);
   


       this.montajSeparat = new Boolean(false);



       this.locationId = new Integer(0);



       this.otherLocation = "";



       this.distance = new java.math.BigDecimal(0);



       this.deliveries = new Integer(0);



       this.valMontaj = new java.math.BigDecimal(0);



       this.valTransport = new java.math.BigDecimal(0);



       this.clientContactId = new Integer(0);



       this.contact = "";



       this.clientContactName = "";



       this.clientContactPhone = "";



       this.clientContactFax = "";



       this.clientContactMobile = "";



       this.clientContactEmail = "";



    }

    public void setNo(String newNo) {
        this.no = newNo;
    }

    public String getNo() {
        return no;
    }

    public void readNo(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("no");
	if(a != null) {
	    this.setNo(a.getStringValue());
	}
    }

    public void setDocDate(java.util.Date newDocDate) {
        this.docDate = newDocDate;
    }

    public java.util.Date getDocDate() {
        return docDate;
    }


    public void setDateFrom(java.util.Date newDateFrom) {
        this.dateFrom = newDateFrom;
    }

    public java.util.Date getDateFrom() {
        return dateFrom;
    }


    public void setDateTo(java.util.Date newDateTo) {
        this.dateTo = newDateTo;
    }

    public java.util.Date getDateTo() {
        return dateTo;
    }


    public void setDiscontinued(Boolean newDiscontinued) {
        this.discontinued = newDiscontinued;
    }

    public Boolean getDiscontinued() {
        return discontinued;
    }

    public void readDiscontinued(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("discontinued");
	if(a != null) {
	    this.setDiscontinued(a.getBoolValue());
	}
    }

    public void setPeriod(Integer newPeriod) {
        this.period = newPeriod;
    }

    public Integer getPeriod() {
        return period;
    }

    public void readPeriod(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("period");
	if(a != null) {
	    this.setPeriod(a.getIntValue());
	}
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

    public void setDescription(String newDescription) {
        this.description = newDescription;
    }

    public String getDescription() {
        return description;
    }

    public void readDescription(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("description");
	if(a != null) {
	    this.setDescription(a.getStringValue());
	}
    }

    public void setComment(String newComment) {
        this.comment = newComment;
    }

    public String getComment() {
        return comment;
    }

    public void readComment(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("comment");
	if(a != null) {
	    this.setComment(a.getStringValue());
	}
    }

    public void setContract(String newContract) {
        this.contract = newContract;
    }

    public String getContract() {
        return contract;
    }

    public void readContract(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("contract");
	if(a != null) {
	    this.setContract(a.getStringValue());
	}
    }

    public void setAnexa(String newAnexa) {
        this.anexa = newAnexa;
    }

    public String getAnexa() {
        return anexa;
    }

    public void readAnexa(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("anexa");
	if(a != null) {
	    this.setAnexa(a.getStringValue());
	}
    }

    public void setTerms(String newTerms) {
        this.terms = newTerms;
    }

    public String getTerms() {
        return terms;
    }

    public void readTerms(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("terms");
	if(a != null) {
	    this.setTerms(a.getStringValue());
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

    public void setAttribute6(String newAttribute6) {
        this.attribute6 = newAttribute6;
    }

    public String getAttribute6() {
        return attribute6;
    }

    public void readAttribute6(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("attribute6");
	if(a != null) {
	    this.setAttribute6(a.getStringValue());
	}
    }

    public void setAttribute7(String newAttribute7) {
        this.attribute7 = newAttribute7;
    }

    public String getAttribute7() {
        return attribute7;
    }

    public void readAttribute7(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("attribute7");
	if(a != null) {
	    this.setAttribute7(a.getStringValue());
	}
    }

    public void setAttribute8(String newAttribute8) {
        this.attribute8 = newAttribute8;
    }

    public String getAttribute8() {
        return attribute8;
    }

    public void readAttribute8(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("attribute8");
	if(a != null) {
	    this.setAttribute8(a.getStringValue());
	}
    }

    public void setAttribute9(String newAttribute9) {
        this.attribute9 = newAttribute9;
    }

    public String getAttribute9() {
        return attribute9;
    }

    public void readAttribute9(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("attribute9");
	if(a != null) {
	    this.setAttribute9(a.getStringValue());
	}
    }

    public void setAttribute10(String newAttribute10) {
        this.attribute10 = newAttribute10;
    }

    public String getAttribute10() {
        return attribute10;
    }

    public void readAttribute10(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("attribute10");
	if(a != null) {
	    this.setAttribute10(a.getStringValue());
	}
    }

    public void setProductId(Integer newProductId) {
        this.productId = newProductId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void readProductId(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("productId");
	if(a != null) {
	    this.setProductId(a.getIntValue());
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

    public void setVatPrice(java.math.BigDecimal newVatPrice) {
        this.vatPrice = newVatPrice;
    }

    public java.math.BigDecimal getVatPrice() {
        return vatPrice;
    }

    public void readVatPrice(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("vatPrice");
	if(a != null) {
	    this.setVatPrice(a.getDecimalValue());
	}
    }

    public void setVatValue(java.math.BigDecimal newVatValue) {
        this.vatValue = newVatValue;
    }

    public java.math.BigDecimal getVatValue() {
        return vatValue;
    }

    public void readVatValue(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("vatValue");
	if(a != null) {
	    this.setVatValue(a.getDecimalValue());
	}
    }

    public void setRelativeGain(Double newRelativeGain) {
        this.relativeGain = newRelativeGain;
    }

    public Double getRelativeGain() {
        return relativeGain;
    }

    public void readRelativeGain(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("relativeGain");
	if(a != null) {
	    this.setRelativeGain(a.getDoubleValue());
	}
    }

    public void setAbsoluteGain(java.math.BigDecimal newAbsoluteGain) {
        this.absoluteGain = newAbsoluteGain;
    }

    public java.math.BigDecimal getAbsoluteGain() {
        return absoluteGain;
    }

    public void readAbsoluteGain(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("absoluteGain");
	if(a != null) {
	    this.setAbsoluteGain(a.getDecimalValue());
	}
    }

    public void setProductCategory(String newProductCategory) {
        this.productCategory = newProductCategory;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void readProductCategory(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("productCategory");
	if(a != null) {
	    this.setProductCategory(a.getStringValue());
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

    public void setMontajId(Integer newMontajId) {
        this.montajId = newMontajId;
    }

    public Integer getMontajId() {
        return montajId;
    }

    public void readMontajId(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("montajId");
	if(a != null) {
	    this.setMontajId(a.getIntValue());
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

    public void setLocationId(Integer newLocationId) {
        this.locationId = newLocationId;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void readLocationId(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("locationId");
	if(a != null) {
	    this.setLocationId(a.getIntValue());
	}
    }

    public void setOtherLocation(String newOtherLocation) {
        this.otherLocation = newOtherLocation;
    }

    public String getOtherLocation() {
        return otherLocation;
    }

    public void readOtherLocation(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("otherLocation");
	if(a != null) {
	    this.setOtherLocation(a.getStringValue());
	}
    }

    public void setDistance(java.math.BigDecimal newDistance) {
        this.distance = newDistance;
    }

    public java.math.BigDecimal getDistance() {
        return distance;
    }

    public void readDistance(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("distance");
	if(a != null) {
	    this.setDistance(a.getDecimalValue());
	}
    }

    public void setDeliveries(Integer newDeliveries) {
        this.deliveries = newDeliveries;
    }

    public Integer getDeliveries() {
        return deliveries;
    }

    public void readDeliveries(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("deliveries");
	if(a != null) {
	    this.setDeliveries(a.getIntValue());
	}
    }

    public void setValMontaj(java.math.BigDecimal newValMontaj) {
        this.valMontaj = newValMontaj;
    }

    public java.math.BigDecimal getValMontaj() {
        return valMontaj;
    }

    public void readValMontaj(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("valMontaj");
	if(a != null) {
	    this.setValMontaj(a.getDecimalValue());
	}
    }

    public void setValTransport(java.math.BigDecimal newValTransport) {
        this.valTransport = newValTransport;
    }

    public java.math.BigDecimal getValTransport() {
        return valTransport;
    }

    public void readValTransport(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("valTransport");
	if(a != null) {
	    this.setValTransport(a.getDecimalValue());
	}
    }

    public void setClientContactId(Integer newClientContactId) {
        this.clientContactId = newClientContactId;
    }

    public Integer getClientContactId() {
        return clientContactId;
    }

    public void readClientContactId(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("clientContactId");
	if(a != null) {
	    this.setClientContactId(a.getIntValue());
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

    public void setClientContactName(String newClientContactName) {
        this.clientContactName = newClientContactName;
    }

    public String getClientContactName() {
        return clientContactName;
    }

    public void readClientContactName(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("clientContactName");
	if(a != null) {
	    this.setClientContactName(a.getStringValue());
	}
    }

    public void setClientContactPhone(String newClientContactPhone) {
        this.clientContactPhone = newClientContactPhone;
    }

    public String getClientContactPhone() {
        return clientContactPhone;
    }

    public void readClientContactPhone(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("clientContactPhone");
	if(a != null) {
	    this.setClientContactPhone(a.getStringValue());
	}
    }

    public void setClientContactFax(String newClientContactFax) {
        this.clientContactFax = newClientContactFax;
    }

    public String getClientContactFax() {
        return clientContactFax;
    }

    public void readClientContactFax(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("clientContactFax");
	if(a != null) {
	    this.setClientContactFax(a.getStringValue());
	}
    }

    public void setClientContactMobile(String newClientContactMobile) {
        this.clientContactMobile = newClientContactMobile;
    }

    public String getClientContactMobile() {
        return clientContactMobile;
    }

    public void readClientContactMobile(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("clientContactMobile");
	if(a != null) {
	    this.setClientContactMobile(a.getStringValue());
	}
    }

    public void setClientContactEmail(String newClientContactEmail) {
        this.clientContactEmail = newClientContactEmail;
    }

    public String getClientContactEmail() {
        return clientContactEmail;
    }

    public void readClientContactEmail(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("clientContactEmail");
	if(a != null) {
	    this.setClientContactEmail(a.getStringValue());
	}
    }

}
