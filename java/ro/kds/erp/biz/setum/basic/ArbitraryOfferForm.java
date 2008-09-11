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
    java.math.BigDecimal distance;
    Integer deliveries;
    java.math.BigDecimal valMontaj;
    java.math.BigDecimal valTransport;

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



       this.distance = new java.math.BigDecimal(0);



       this.deliveries = new Integer(0);



       this.valMontaj = new java.math.BigDecimal(0);



       this.valTransport = new java.math.BigDecimal(0);



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

}
