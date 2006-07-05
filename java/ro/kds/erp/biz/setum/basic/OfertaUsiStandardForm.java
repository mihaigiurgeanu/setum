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
public class OfertaUsiStandardForm implements Serializable {
        
    String no;
    java.util.Date docDate;
    java.util.Date dateFrom;
    java.util.Date dateTo;
    Boolean discontinued;
    Integer period;
    String name;
    String description;
    String comment;
    java.math.BigDecimal vat;
    java.math.BigDecimal price;
    java.math.BigDecimal vatPrice;
    Double relativeGain;
    java.math.BigDecimal absoluteGain;
    String productCategory;
    String productCode;
    String productName;
    String usa;
    String usaCode;
    Integer usaId;
    String usaDescription;
    String broasca;
    String cilindru;
    String sild;
    String yalla;
    String vizor;
    java.math.BigDecimal entryPrice;
    java.math.BigDecimal sellPrice;
    String selectionCode;
    String FilterUsa;
    String FilterBroasca;
    String FilterCilindru;
    String FilterSild;
    String FilterYalla;
    String FilterVizor;

    public OfertaUsiStandardForm() {


       this.no = "";




       // No rule to initialize this.docDate




       // No rule to initialize this.dateFrom




       // No rule to initialize this.dateTo



       this.discontinued = new Boolean(false);



       this.period = new Integer(0);



       this.name = "";



       this.description = "";



       this.comment = "";



       this.vat = new java.math.BigDecimal(0);



       this.price = new java.math.BigDecimal(0);



       this.vatPrice = new java.math.BigDecimal(0);



       this.relativeGain = new Double(0);
   


       this.absoluteGain = new java.math.BigDecimal(0);



       this.productCategory = "";



       this.productCode = "";



       this.productName = "";



       this.usa = "";



       this.usaCode = "";



       this.usaId = new Integer(0);



       this.usaDescription = "";



       this.broasca = "";



       this.cilindru = "";



       this.sild = "";



       this.yalla = "";



       this.vizor = "";



       this.entryPrice = new java.math.BigDecimal(0);



       this.sellPrice = new java.math.BigDecimal(0);



       this.selectionCode = "";



       this.FilterUsa = "";



       this.FilterBroasca = "";



       this.FilterCilindru = "";



       this.FilterSild = "";



       this.FilterYalla = "";



       this.FilterVizor = "";



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

    public void setVat(java.math.BigDecimal newVat) {
        this.vat = newVat;
    }

    public java.math.BigDecimal getVat() {
        return vat;
    }

    public void readVat(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("vat");
	if(a != null) {
	    this.setVat(a.getDecimalValue());
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

    public void setUsa(String newUsa) {
        this.usa = newUsa;
    }

    public String getUsa() {
        return usa;
    }

    public void readUsa(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("usa");
	if(a != null) {
	    this.setUsa(a.getStringValue());
	}
    }

    public void setUsaCode(String newUsaCode) {
        this.usaCode = newUsaCode;
    }

    public String getUsaCode() {
        return usaCode;
    }

    public void readUsaCode(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("usaCode");
	if(a != null) {
	    this.setUsaCode(a.getStringValue());
	}
    }

    public void setUsaId(Integer newUsaId) {
        this.usaId = newUsaId;
    }

    public Integer getUsaId() {
        return usaId;
    }

    public void readUsaId(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("usaId");
	if(a != null) {
	    this.setUsaId(a.getIntValue());
	}
    }

    public void setUsaDescription(String newUsaDescription) {
        this.usaDescription = newUsaDescription;
    }

    public String getUsaDescription() {
        return usaDescription;
    }

    public void readUsaDescription(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("usaDescription");
	if(a != null) {
	    this.setUsaDescription(a.getStringValue());
	}
    }

    public void setBroasca(String newBroasca) {
        this.broasca = newBroasca;
    }

    public String getBroasca() {
        return broasca;
    }

    public void readBroasca(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("broasca");
	if(a != null) {
	    this.setBroasca(a.getStringValue());
	}
    }

    public void setCilindru(String newCilindru) {
        this.cilindru = newCilindru;
    }

    public String getCilindru() {
        return cilindru;
    }

    public void readCilindru(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("cilindru");
	if(a != null) {
	    this.setCilindru(a.getStringValue());
	}
    }

    public void setSild(String newSild) {
        this.sild = newSild;
    }

    public String getSild() {
        return sild;
    }

    public void readSild(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("sild");
	if(a != null) {
	    this.setSild(a.getStringValue());
	}
    }

    public void setYalla(String newYalla) {
        this.yalla = newYalla;
    }

    public String getYalla() {
        return yalla;
    }

    public void readYalla(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("yalla");
	if(a != null) {
	    this.setYalla(a.getStringValue());
	}
    }

    public void setVizor(String newVizor) {
        this.vizor = newVizor;
    }

    public String getVizor() {
        return vizor;
    }

    public void readVizor(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("vizor");
	if(a != null) {
	    this.setVizor(a.getStringValue());
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

    public void setSelectionCode(String newSelectionCode) {
        this.selectionCode = newSelectionCode;
    }

    public String getSelectionCode() {
        return selectionCode;
    }

    public void readSelectionCode(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("selectionCode");
	if(a != null) {
	    this.setSelectionCode(a.getStringValue());
	}
    }

    public void setFilterUsa(String newFilterUsa) {
        this.FilterUsa = newFilterUsa;
    }

    public String getFilterUsa() {
        return FilterUsa;
    }

    public void readFilterUsa(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("FilterUsa");
	if(a != null) {
	    this.setFilterUsa(a.getStringValue());
	}
    }

    public void setFilterBroasca(String newFilterBroasca) {
        this.FilterBroasca = newFilterBroasca;
    }

    public String getFilterBroasca() {
        return FilterBroasca;
    }

    public void readFilterBroasca(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("FilterBroasca");
	if(a != null) {
	    this.setFilterBroasca(a.getStringValue());
	}
    }

    public void setFilterCilindru(String newFilterCilindru) {
        this.FilterCilindru = newFilterCilindru;
    }

    public String getFilterCilindru() {
        return FilterCilindru;
    }

    public void readFilterCilindru(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("FilterCilindru");
	if(a != null) {
	    this.setFilterCilindru(a.getStringValue());
	}
    }

    public void setFilterSild(String newFilterSild) {
        this.FilterSild = newFilterSild;
    }

    public String getFilterSild() {
        return FilterSild;
    }

    public void readFilterSild(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("FilterSild");
	if(a != null) {
	    this.setFilterSild(a.getStringValue());
	}
    }

    public void setFilterYalla(String newFilterYalla) {
        this.FilterYalla = newFilterYalla;
    }

    public String getFilterYalla() {
        return FilterYalla;
    }

    public void readFilterYalla(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("FilterYalla");
	if(a != null) {
	    this.setFilterYalla(a.getStringValue());
	}
    }

    public void setFilterVizor(String newFilterVizor) {
        this.FilterVizor = newFilterVizor;
    }

    public String getFilterVizor() {
        return FilterVizor;
    }

    public void readFilterVizor(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("FilterVizor");
	if(a != null) {
	    this.setFilterVizor(a.getStringValue());
	}
    }

}
