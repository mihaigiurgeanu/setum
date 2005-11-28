package ro.kds.erp.biz.setum.basic;

import java.io.Serializable;

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
    java.math.BigDecimal price;
    Double relativeGain;
    java.math.BigDecimal absoluteGain;
    String usa;
    String broasca;
    String cilindru;
    String sild;
    String yalla;
    String vizor;
    java.math.BigDecimal referencePrice;

    public void setNo(String newNo) {
        this.no = newNo;
    }

    public String getNo() {
        return no;
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

    public void setPeriod(Integer newPeriod) {
        this.period = newPeriod;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setName(String newName) {
        this.name = newName;
    }

    public String getName() {
        return name;
    }

    public void setDescription(String newDescription) {
        this.description = newDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setComment(String newComment) {
        this.comment = newComment;
    }

    public String getComment() {
        return comment;
    }

    public void setPrice(java.math.BigDecimal newPrice) {
        this.price = newPrice;
    }

    public java.math.BigDecimal getPrice() {
        return price;
    }

    public void setRelativeGain(Double newRelativeGain) {
        this.relativeGain = newRelativeGain;
    }

    public Double getRelativeGain() {
        return relativeGain;
    }

    public void setAbsoluteGain(java.math.BigDecimal newAbsoluteGain) {
        this.absoluteGain = newAbsoluteGain;
    }

    public java.math.BigDecimal getAbsoluteGain() {
        return absoluteGain;
    }

    public void setUsa(String newUsa) {
        this.usa = newUsa;
    }

    public String getUsa() {
        return usa;
    }

    public void setBroasca(String newBroasca) {
        this.broasca = newBroasca;
    }

    public String getBroasca() {
        return broasca;
    }

    public void setCilindru(String newCilindru) {
        this.cilindru = newCilindru;
    }

    public String getCilindru() {
        return cilindru;
    }

    public void setSild(String newSild) {
        this.sild = newSild;
    }

    public String getSild() {
        return sild;
    }

    public void setYalla(String newYalla) {
        this.yalla = newYalla;
    }

    public String getYalla() {
        return yalla;
    }

    public void setVizor(String newVizor) {
        this.vizor = newVizor;
    }

    public String getVizor() {
        return vizor;
    }

    public void setReferencePrice(java.math.BigDecimal newReferencePrice) {
        this.referencePrice = newReferencePrice;
    }

    public java.math.BigDecimal getReferencePrice() {
        return referencePrice;
    }

}
