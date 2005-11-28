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
public class GauriAerisireForm implements Serializable {
        
    Double diametru;
    Double pas;
    Integer nrRanduri;
    String pozitionare1;
    String pozitionare2;
    String pozitionare3;
    java.math.BigDecimal sellPrice;
    java.math.BigDecimal entryPrice;
    java.math.BigDecimal price1;

    public void setDiametru(Double newDiametru) {
        this.diametru = newDiametru;
    }

    public Double getDiametru() {
        return diametru;
    }

    public void setPas(Double newPas) {
        this.pas = newPas;
    }

    public Double getPas() {
        return pas;
    }

    public void setNrRanduri(Integer newNrRanduri) {
        this.nrRanduri = newNrRanduri;
    }

    public Integer getNrRanduri() {
        return nrRanduri;
    }

    public void setPozitionare1(String newPozitionare1) {
        this.pozitionare1 = newPozitionare1;
    }

    public String getPozitionare1() {
        return pozitionare1;
    }

    public void setPozitionare2(String newPozitionare2) {
        this.pozitionare2 = newPozitionare2;
    }

    public String getPozitionare2() {
        return pozitionare2;
    }

    public void setPozitionare3(String newPozitionare3) {
        this.pozitionare3 = newPozitionare3;
    }

    public String getPozitionare3() {
        return pozitionare3;
    }

    public void setSellPrice(java.math.BigDecimal newSellPrice) {
        this.sellPrice = newSellPrice;
    }

    public java.math.BigDecimal getSellPrice() {
        return sellPrice;
    }

    public void setEntryPrice(java.math.BigDecimal newEntryPrice) {
        this.entryPrice = newEntryPrice;
    }

    public java.math.BigDecimal getEntryPrice() {
        return entryPrice;
    }

    public void setPrice1(java.math.BigDecimal newPrice1) {
        this.price1 = newPrice1;
    }

    public java.math.BigDecimal getPrice1() {
        return price1;
    }

}
