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
public class SistemForm implements Serializable {
        
    String name;
    String code;
    Integer categoryId;
    java.math.BigDecimal entryPrice;
    java.math.BigDecimal sellPrice;
    java.math.BigDecimal partPrice;
    java.math.BigDecimal laborPrice;
    Double relativeGainSP;
    java.math.BigDecimal absoluteGainSP;
    Double relativeGainPP;
    java.math.BigDecimal absoluteGainPP;

    public void setName(String newName) {
        this.name = newName;
    }

    public String getName() {
        return name;
    }

    public void setCode(String newCode) {
        this.code = newCode;
    }

    public String getCode() {
        return code;
    }

    public void setCategoryId(Integer newCategoryId) {
        this.categoryId = newCategoryId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setEntryPrice(java.math.BigDecimal newEntryPrice) {
        this.entryPrice = newEntryPrice;
    }

    public java.math.BigDecimal getEntryPrice() {
        return entryPrice;
    }

    public void setSellPrice(java.math.BigDecimal newSellPrice) {
        this.sellPrice = newSellPrice;
    }

    public java.math.BigDecimal getSellPrice() {
        return sellPrice;
    }

    public void setPartPrice(java.math.BigDecimal newPartPrice) {
        this.partPrice = newPartPrice;
    }

    public java.math.BigDecimal getPartPrice() {
        return partPrice;
    }

    public void setLaborPrice(java.math.BigDecimal newLaborPrice) {
        this.laborPrice = newLaborPrice;
    }

    public java.math.BigDecimal getLaborPrice() {
        return laborPrice;
    }

    public void setRelativeGainSP(Double newRelativeGainSP) {
        this.relativeGainSP = newRelativeGainSP;
    }

    public Double getRelativeGainSP() {
        return relativeGainSP;
    }

    public void setAbsoluteGainSP(java.math.BigDecimal newAbsoluteGainSP) {
        this.absoluteGainSP = newAbsoluteGainSP;
    }

    public java.math.BigDecimal getAbsoluteGainSP() {
        return absoluteGainSP;
    }

    public void setRelativeGainPP(Double newRelativeGainPP) {
        this.relativeGainPP = newRelativeGainPP;
    }

    public Double getRelativeGainPP() {
        return relativeGainPP;
    }

    public void setAbsoluteGainPP(java.math.BigDecimal newAbsoluteGainPP) {
        this.absoluteGainPP = newAbsoluteGainPP;
    }

    public java.math.BigDecimal getAbsoluteGainPP() {
        return absoluteGainPP;
    }

}
