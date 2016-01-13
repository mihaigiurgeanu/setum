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
public class StandardOfferForm implements Serializable {

    /**
     * Builds an RDF URI for the field. This RDF is used by the UI to display
     * a localized and friendly name for the field in validation messages.
     *
     * @param fieldName - the name of the field
     * @return an RDF URI by concatenating the string <code>http://www.kds.ro/readybeans/rdf/forms/StandardOffer#</code>
     * with the name of the field.
     */
     public static String uri(String fieldName) {
	return "http://www.kds.ro/readybeans/rdf/forms/StandardOffer#" + fieldName;
     }

    String no;
    java.util.Date docDate;
    java.util.Date dateFrom;
    java.util.Date dateTo;
    Boolean discontinued;
    Integer period;
    String name;
    String description;
    String comment;
    Integer productId;
    java.math.BigDecimal price;
    java.math.BigDecimal vatPrice;
    java.math.BigDecimal price1;
    java.math.BigDecimal price2;
    Double relativeGain;
    java.math.BigDecimal absoluteGain;
    String productCategory;
    String productCode;
    String productName;
    java.math.BigDecimal entryPrice;
    java.math.BigDecimal sellPrice;
    String lineComments;

    public StandardOfferForm() {


       this.no = "";




       // No rule to initialize this.docDate




       // No rule to initialize this.dateFrom




       // No rule to initialize this.dateTo



       this.discontinued = new Boolean(false);



       this.period = new Integer(0);



       this.name = "";



       this.description = "";



       this.comment = "";



       this.productId = new Integer(0);



       this.price = new java.math.BigDecimal(0);



       this.vatPrice = new java.math.BigDecimal(0);



       this.price1 = new java.math.BigDecimal(0);



       this.price2 = new java.math.BigDecimal(0);



       this.relativeGain = new Double(0);
   


       this.absoluteGain = new java.math.BigDecimal(0);



       this.productCategory = "";



       this.productCode = "";



       this.productName = "";



       this.entryPrice = new java.math.BigDecimal(0);



       this.sellPrice = new java.math.BigDecimal(0);



       this.lineComments = "";



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

    public void setPrice2(java.math.BigDecimal newPrice2) {
        this.price2 = newPrice2;
    }

    public java.math.BigDecimal getPrice2() {
        return price2;
    }

    public void readPrice2(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("price2");
	if(a != null) {
	    this.setPrice2(a.getDecimalValue());
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

    public void setLineComments(String newLineComments) {
        this.lineComments = newLineComments;
    }

    public String getLineComments() {
        return lineComments;
    }

    public void readLineComments(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("lineComments");
	if(a != null) {
	    this.setLineComments(a.getStringValue());
	}
    }

}
