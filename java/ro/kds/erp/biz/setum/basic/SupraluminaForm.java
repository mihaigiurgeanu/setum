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
public class SupraluminaForm implements Serializable {
        
    Integer tip;
    Double ls;
    Double hs;
    Integer cells;
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
    String businessCategory;
    Integer quantity;

    public SupraluminaForm() {


       this.tip = new Integer(0);



       this.ls = new Double(0);
   


       this.hs = new Double(0);
   


       this.cells = new Integer(0);



       this.deschidere = new Integer(0);



       this.sensDeschidere = new Integer(0);



       this.pozitionareBalamale = new Integer(0);



       this.componenta = new Integer(0);



       this.tipComponenta = new Integer(0);



       this.tipGeam = new Integer(0);



       this.geamSimpluId = new Integer(0);



       this.geamTermopanId = new Integer(0);



       this.tipGrilaj = new Integer(0);



       this.grilajStasId = new Integer(0);



       this.valoareGrilajAtipic = new java.math.BigDecimal(0);



       this.tipTabla = new Integer(0);



       this.tablaId = new Integer(0);



       this.sellPrice = new java.math.BigDecimal(0);



       this.entryPrice = new java.math.BigDecimal(0);



       this.price1 = new java.math.BigDecimal(0);



       this.businessCategory = "";



       this.quantity = new Integer(0);



    }

    public void setTip(Integer newTip) {
        this.tip = newTip;
    }

    public Integer getTip() {
        return tip;
    }

    public void readTip(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("tip");
	if(a != null) {
	    this.setTip(a.getIntValue());
	}
    }

    public void setLs(Double newLs) {
        this.ls = newLs;
    }

    public Double getLs() {
        return ls;
    }

    public void readLs(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("ls");
	if(a != null) {
	    this.setLs(a.getDoubleValue());
	}
    }

    public void setHs(Double newHs) {
        this.hs = newHs;
    }

    public Double getHs() {
        return hs;
    }

    public void readHs(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("hs");
	if(a != null) {
	    this.setHs(a.getDoubleValue());
	}
    }

    public void setCells(Integer newCells) {
        this.cells = newCells;
    }

    public Integer getCells() {
        return cells;
    }

    public void readCells(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("cells");
	if(a != null) {
	    this.setCells(a.getIntValue());
	}
    }

    public void setDeschidere(Integer newDeschidere) {
        this.deschidere = newDeschidere;
    }

    public Integer getDeschidere() {
        return deschidere;
    }

    public void readDeschidere(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("deschidere");
	if(a != null) {
	    this.setDeschidere(a.getIntValue());
	}
    }

    public void setSensDeschidere(Integer newSensDeschidere) {
        this.sensDeschidere = newSensDeschidere;
    }

    public Integer getSensDeschidere() {
        return sensDeschidere;
    }

    public void readSensDeschidere(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("sensDeschidere");
	if(a != null) {
	    this.setSensDeschidere(a.getIntValue());
	}
    }

    public void setPozitionareBalamale(Integer newPozitionareBalamale) {
        this.pozitionareBalamale = newPozitionareBalamale;
    }

    public Integer getPozitionareBalamale() {
        return pozitionareBalamale;
    }

    public void readPozitionareBalamale(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("pozitionareBalamale");
	if(a != null) {
	    this.setPozitionareBalamale(a.getIntValue());
	}
    }

    public void setComponenta(Integer newComponenta) {
        this.componenta = newComponenta;
    }

    public Integer getComponenta() {
        return componenta;
    }

    public void readComponenta(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("componenta");
	if(a != null) {
	    this.setComponenta(a.getIntValue());
	}
    }

    public void setTipComponenta(Integer newTipComponenta) {
        this.tipComponenta = newTipComponenta;
    }

    public Integer getTipComponenta() {
        return tipComponenta;
    }

    public void readTipComponenta(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("tipComponenta");
	if(a != null) {
	    this.setTipComponenta(a.getIntValue());
	}
    }

    public void setTipGeam(Integer newTipGeam) {
        this.tipGeam = newTipGeam;
    }

    public Integer getTipGeam() {
        return tipGeam;
    }

    public void readTipGeam(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("tipGeam");
	if(a != null) {
	    this.setTipGeam(a.getIntValue());
	}
    }

    public void setGeamSimpluId(Integer newGeamSimpluId) {
        this.geamSimpluId = newGeamSimpluId;
    }

    public Integer getGeamSimpluId() {
        return geamSimpluId;
    }

    public void readGeamSimpluId(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("geamSimpluId");
	if(a != null) {
	    this.setGeamSimpluId(a.getIntValue());
	}
    }

    public void setGeamTermopanId(Integer newGeamTermopanId) {
        this.geamTermopanId = newGeamTermopanId;
    }

    public Integer getGeamTermopanId() {
        return geamTermopanId;
    }

    public void readGeamTermopanId(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("geamTermopanId");
	if(a != null) {
	    this.setGeamTermopanId(a.getIntValue());
	}
    }

    public void setTipGrilaj(Integer newTipGrilaj) {
        this.tipGrilaj = newTipGrilaj;
    }

    public Integer getTipGrilaj() {
        return tipGrilaj;
    }

    public void readTipGrilaj(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("tipGrilaj");
	if(a != null) {
	    this.setTipGrilaj(a.getIntValue());
	}
    }

    public void setGrilajStasId(Integer newGrilajStasId) {
        this.grilajStasId = newGrilajStasId;
    }

    public Integer getGrilajStasId() {
        return grilajStasId;
    }

    public void readGrilajStasId(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("grilajStasId");
	if(a != null) {
	    this.setGrilajStasId(a.getIntValue());
	}
    }

    public void setValoareGrilajAtipic(java.math.BigDecimal newValoareGrilajAtipic) {
        this.valoareGrilajAtipic = newValoareGrilajAtipic;
    }

    public java.math.BigDecimal getValoareGrilajAtipic() {
        return valoareGrilajAtipic;
    }

    public void readValoareGrilajAtipic(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("valoareGrilajAtipic");
	if(a != null) {
	    this.setValoareGrilajAtipic(a.getDecimalValue());
	}
    }

    public void setTipTabla(Integer newTipTabla) {
        this.tipTabla = newTipTabla;
    }

    public Integer getTipTabla() {
        return tipTabla;
    }

    public void readTipTabla(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("tipTabla");
	if(a != null) {
	    this.setTipTabla(a.getIntValue());
	}
    }

    public void setTablaId(Integer newTablaId) {
        this.tablaId = newTablaId;
    }

    public Integer getTablaId() {
        return tablaId;
    }

    public void readTablaId(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("tablaId");
	if(a != null) {
	    this.setTablaId(a.getIntValue());
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

    public void setQuantity(Integer newQuantity) {
        this.quantity = newQuantity;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void readQuantity(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("quantity");
	if(a != null) {
	    this.setQuantity(a.getIntValue());
	}
    }

}
