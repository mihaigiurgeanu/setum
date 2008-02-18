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
public class FereastraForm implements Serializable {

    /**
     * Builds an RDF URI for the field. This RDF is used by the UI to display
     * a localized and friendly name for the field in validation messages.
     *
     * @param fieldName - the name of the field
     * @return an RDF URI by concatenating the string <code>http://www.kds.ro/readybeans/rdf/forms/Fereastra#</code>
     * with the name of the field.
     */
     public static String uri(String fieldName) {
	return "http://www.kds.ro/readybeans/rdf/forms/Fereastra#" + fieldName;
     }

    Integer standard;
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
    java.math.BigDecimal sellPrice;
    java.math.BigDecimal entryPrice;
    java.math.BigDecimal price1;
    String businessCategory;
    Integer quantity;
    String groupingCode;

    public FereastraForm() {


       this.standard = new Integer(0);



       this.canat = new Integer(0);



       this.lf = new Double(0);
   


       this.hf = new Double(0);
   


       this.pozitionare1 = "";



       this.pozitionare2 = "";



       this.pozitionare3 = "";



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



       this.sellPrice = new java.math.BigDecimal(0);



       this.entryPrice = new java.math.BigDecimal(0);



       this.price1 = new java.math.BigDecimal(0);



       this.businessCategory = "";



       this.quantity = new Integer(0);



       this.groupingCode = "";



    }

    public void setStandard(Integer newStandard) {
        this.standard = newStandard;
    }

    public Integer getStandard() {
        return standard;
    }

    public void readStandard(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("standard");
	if(a != null) {
	    this.setStandard(a.getIntValue());
	}
    }

    public void setCanat(Integer newCanat) {
        this.canat = newCanat;
    }

    public Integer getCanat() {
        return canat;
    }

    public void readCanat(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("canat");
	if(a != null) {
	    this.setCanat(a.getIntValue());
	}
    }

    public void setLf(Double newLf) {
        this.lf = newLf;
    }

    public Double getLf() {
        return lf;
    }

    public void readLf(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("lf");
	if(a != null) {
	    this.setLf(a.getDoubleValue());
	}
    }

    public void setHf(Double newHf) {
        this.hf = newHf;
    }

    public Double getHf() {
        return hf;
    }

    public void readHf(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("hf");
	if(a != null) {
	    this.setHf(a.getDoubleValue());
	}
    }

    public void setPozitionare1(String newPozitionare1) {
        this.pozitionare1 = newPozitionare1;
    }

    public String getPozitionare1() {
        return pozitionare1;
    }

    public void readPozitionare1(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("pozitionare1");
	if(a != null) {
	    this.setPozitionare1(a.getStringValue());
	}
    }

    public void setPozitionare2(String newPozitionare2) {
        this.pozitionare2 = newPozitionare2;
    }

    public String getPozitionare2() {
        return pozitionare2;
    }

    public void readPozitionare2(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("pozitionare2");
	if(a != null) {
	    this.setPozitionare2(a.getStringValue());
	}
    }

    public void setPozitionare3(String newPozitionare3) {
        this.pozitionare3 = newPozitionare3;
    }

    public String getPozitionare3() {
        return pozitionare3;
    }

    public void readPozitionare3(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("pozitionare3");
	if(a != null) {
	    this.setPozitionare3(a.getStringValue());
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

    public void setGroupingCode(String newGroupingCode) {
        this.groupingCode = newGroupingCode;
    }

    public String getGroupingCode() {
        return groupingCode;
    }

    public void readGroupingCode(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("groupingCode");
	if(a != null) {
	    this.setGroupingCode(a.getStringValue());
	}
    }

}
