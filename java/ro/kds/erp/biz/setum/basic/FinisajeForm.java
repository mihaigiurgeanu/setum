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
public class FinisajeForm implements Serializable {

    /**
     * Builds an RDF URI for the field. This RDF is used by the UI to display
     * a localized and friendly name for the field in validation messages.
     *
     * @param fieldName - the name of the field
     * @return an RDF URI by concatenating the string <code>http://www.kds.ro/readybeans/rdf/forms/Finisaje#</code>
     * with the name of the field.
     */
     public static String uri(String fieldName) {
	return "http://www.kds.ro/readybeans/rdf/forms/Finisaje#" + fieldName;
     }

    Integer zincare;
    Integer furnir;
    Integer placare;
    Integer grundId;
    Integer vopsireTip;
    Integer ralStasId;
    String ralOrder;
    java.math.BigDecimal ralOrderValue;
    String code;
    String name;
    String description;
    java.math.BigDecimal sellPrice;
    java.math.BigDecimal entryPrice;
    java.math.BigDecimal price1;
    String groupingCode;

    public FinisajeForm() {


       this.zincare = new Integer(0);



       this.furnir = new Integer(0);



       this.placare = new Integer(0);



       this.grundId = new Integer(0);



       this.vopsireTip = new Integer(0);



       this.ralStasId = new Integer(0);



       this.ralOrder = "";



       this.ralOrderValue = new java.math.BigDecimal(0);



       this.code = "";



       this.name = "";



       this.description = "";



       this.sellPrice = new java.math.BigDecimal(0);



       this.entryPrice = new java.math.BigDecimal(0);



       this.price1 = new java.math.BigDecimal(0);



       this.groupingCode = "";



    }

    public void setZincare(Integer newZincare) {
        this.zincare = newZincare;
    }

    public Integer getZincare() {
        return zincare;
    }

    public void readZincare(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("zincare");
	if(a != null) {
	    this.setZincare(a.getIntValue());
	}
    }

    public void setFurnir(Integer newFurnir) {
        this.furnir = newFurnir;
    }

    public Integer getFurnir() {
        return furnir;
    }

    public void readFurnir(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("furnir");
	if(a != null) {
	    this.setFurnir(a.getIntValue());
	}
    }

    public void setPlacare(Integer newPlacare) {
        this.placare = newPlacare;
    }

    public Integer getPlacare() {
        return placare;
    }

    public void readPlacare(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("placare");
	if(a != null) {
	    this.setPlacare(a.getIntValue());
	}
    }

    public void setGrundId(Integer newGrundId) {
        this.grundId = newGrundId;
    }

    public Integer getGrundId() {
        return grundId;
    }

    public void readGrundId(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("grundId");
	if(a != null) {
	    this.setGrundId(a.getIntValue());
	}
    }

    public void setVopsireTip(Integer newVopsireTip) {
        this.vopsireTip = newVopsireTip;
    }

    public Integer getVopsireTip() {
        return vopsireTip;
    }

    public void readVopsireTip(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("vopsireTip");
	if(a != null) {
	    this.setVopsireTip(a.getIntValue());
	}
    }

    public void setRalStasId(Integer newRalStasId) {
        this.ralStasId = newRalStasId;
    }

    public Integer getRalStasId() {
        return ralStasId;
    }

    public void readRalStasId(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("ralStasId");
	if(a != null) {
	    this.setRalStasId(a.getIntValue());
	}
    }

    public void setRalOrder(String newRalOrder) {
        this.ralOrder = newRalOrder;
    }

    public String getRalOrder() {
        return ralOrder;
    }

    public void readRalOrder(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("ralOrder");
	if(a != null) {
	    this.setRalOrder(a.getStringValue());
	}
    }

    public void setRalOrderValue(java.math.BigDecimal newRalOrderValue) {
        this.ralOrderValue = newRalOrderValue;
    }

    public java.math.BigDecimal getRalOrderValue() {
        return ralOrderValue;
    }

    public void readRalOrderValue(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("ralOrderValue");
	if(a != null) {
	    this.setRalOrderValue(a.getDecimalValue());
	}
    }

    public void setCode(String newCode) {
        this.code = newCode;
    }

    public String getCode() {
        return code;
    }

    public void readCode(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("code");
	if(a != null) {
	    this.setCode(a.getStringValue());
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
