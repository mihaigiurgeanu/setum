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
public class FereastraForm implements Serializable {
        
    Integer canat;
    Double lf;
    Double hf;
    String pozitionare1;
    String pozitionare2;
    String pozitionare3;
    Integer deschidere;
    Integer sensDeschidere;
    Integer pozitionareBalamale;
    Integer componenta;
    Integer tipComponenta;
    Integer tipGeam;
    Integer geamSimpluId;
    Integer geamTermopanId;
    Integer tipGrilaj;
    Integer grilajStasId;
    java.math.BigDecimal valoareGrilajAtipic;
    Integer tipTabla;
    Integer tablaId;
    java.math.BigDecimal sellPrice;
    java.math.BigDecimal entryPrice;
    java.math.BigDecimal price1;

    public void setCanat(Integer newCanat) {
        this.canat = newCanat;
    }

    public Integer getCanat() {
        return canat;
    }

    public void setLf(Double newLf) {
        this.lf = newLf;
    }

    public Double getLf() {
        return lf;
    }

    public void setHf(Double newHf) {
        this.hf = newHf;
    }

    public Double getHf() {
        return hf;
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

    public void setDeschidere(Integer newDeschidere) {
        this.deschidere = newDeschidere;
    }

    public Integer getDeschidere() {
        return deschidere;
    }

    public void setSensDeschidere(Integer newSensDeschidere) {
        this.sensDeschidere = newSensDeschidere;
    }

    public Integer getSensDeschidere() {
        return sensDeschidere;
    }

    public void setPozitionareBalamale(Integer newPozitionareBalamale) {
        this.pozitionareBalamale = newPozitionareBalamale;
    }

    public Integer getPozitionareBalamale() {
        return pozitionareBalamale;
    }

    public void setComponenta(Integer newComponenta) {
        this.componenta = newComponenta;
    }

    public Integer getComponenta() {
        return componenta;
    }

    public void setTipComponenta(Integer newTipComponenta) {
        this.tipComponenta = newTipComponenta;
    }

    public Integer getTipComponenta() {
        return tipComponenta;
    }

    public void setTipGeam(Integer newTipGeam) {
        this.tipGeam = newTipGeam;
    }

    public Integer getTipGeam() {
        return tipGeam;
    }

    public void setGeamSimpluId(Integer newGeamSimpluId) {
        this.geamSimpluId = newGeamSimpluId;
    }

    public Integer getGeamSimpluId() {
        return geamSimpluId;
    }

    public void setGeamTermopanId(Integer newGeamTermopanId) {
        this.geamTermopanId = newGeamTermopanId;
    }

    public Integer getGeamTermopanId() {
        return geamTermopanId;
    }

    public void setTipGrilaj(Integer newTipGrilaj) {
        this.tipGrilaj = newTipGrilaj;
    }

    public Integer getTipGrilaj() {
        return tipGrilaj;
    }

    public void setGrilajStasId(Integer newGrilajStasId) {
        this.grilajStasId = newGrilajStasId;
    }

    public Integer getGrilajStasId() {
        return grilajStasId;
    }

    public void setValoareGrilajAtipic(java.math.BigDecimal newValoareGrilajAtipic) {
        this.valoareGrilajAtipic = newValoareGrilajAtipic;
    }

    public java.math.BigDecimal getValoareGrilajAtipic() {
        return valoareGrilajAtipic;
    }

    public void setTipTabla(Integer newTipTabla) {
        this.tipTabla = newTipTabla;
    }

    public Integer getTipTabla() {
        return tipTabla;
    }

    public void setTablaId(Integer newTablaId) {
        this.tablaId = newTablaId;
    }

    public Integer getTablaId() {
        return tablaId;
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
