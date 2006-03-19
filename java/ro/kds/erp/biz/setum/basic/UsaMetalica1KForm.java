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
public class UsaMetalica1KForm implements Serializable {
        
    String code;
    String name;
    String description;
    String subclass;
    String version;
    Integer material;
    Double lg;
    Double hg;
    Double le;
    Double he;
    Double lcorrection;
    Double hcorrection;
    Integer intFoil;
    Integer ieFoil;
    Integer extFoil;
    Integer isolation;
    Integer openingDir;
    Integer openingSide;
    Integer frameType;
    Double lFrame;
    Double bFrame;
    Double cFrame;
    Integer foilPosition;
    Integer tresholdType;
    Double lTreshold;
    Double hTreshold;
    Double cTreshold;
    Integer tresholdSpace;
    Double h1Treshold;
    Double h2Treshold;
    Integer fereastraId;
    String fereastra;
    java.math.BigDecimal entryPrice;
    java.math.BigDecimal sellPrice;

    public UsaMetalica1KForm() {


       this.code = "";



       this.name = "";



       this.description = "";



       this.subclass = "";



       this.version = "";



       this.material = new Integer(0);



       this.lg = new Double(0);
   


       this.hg = new Double(0);
   


       this.le = new Double(0);
   


       this.he = new Double(0);
   


       this.lcorrection = new Double(0);
   


       this.hcorrection = new Double(0);
   


       this.intFoil = new Integer(0);



       this.ieFoil = new Integer(0);



       this.extFoil = new Integer(0);



       this.isolation = new Integer(0);



       this.openingDir = new Integer(0);



       this.openingSide = new Integer(0);



       this.frameType = new Integer(0);



       this.lFrame = new Double(0);
   


       this.bFrame = new Double(0);
   


       this.cFrame = new Double(0);
   


       this.foilPosition = new Integer(0);



       this.tresholdType = new Integer(0);



       this.lTreshold = new Double(0);
   


       this.hTreshold = new Double(0);
   


       this.cTreshold = new Double(0);
   


       this.tresholdSpace = new Integer(0);



       this.h1Treshold = new Double(0);
   


       this.h2Treshold = new Double(0);
   


       this.fereastraId = new Integer(0);



       this.fereastra = "";



       this.entryPrice = new java.math.BigDecimal(0);



       this.sellPrice = new java.math.BigDecimal(0);



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

    public void setSubclass(String newSubclass) {
        this.subclass = newSubclass;
    }

    public String getSubclass() {
        return subclass;
    }

    public void readSubclass(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("subclass");
	if(a != null) {
	    this.setSubclass(a.getStringValue());
	}
    }

    public void setVersion(String newVersion) {
        this.version = newVersion;
    }

    public String getVersion() {
        return version;
    }

    public void readVersion(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("version");
	if(a != null) {
	    this.setVersion(a.getStringValue());
	}
    }

    public void setMaterial(Integer newMaterial) {
        this.material = newMaterial;
    }

    public Integer getMaterial() {
        return material;
    }

    public void readMaterial(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("material");
	if(a != null) {
	    this.setMaterial(a.getIntValue());
	}
    }

    public void setLg(Double newLg) {
        this.lg = newLg;
    }

    public Double getLg() {
        return lg;
    }

    public void readLg(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("lg");
	if(a != null) {
	    this.setLg(a.getDoubleValue());
	}
    }

    public void setHg(Double newHg) {
        this.hg = newHg;
    }

    public Double getHg() {
        return hg;
    }

    public void readHg(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("hg");
	if(a != null) {
	    this.setHg(a.getDoubleValue());
	}
    }

    public void setLe(Double newLe) {
        this.le = newLe;
    }

    public Double getLe() {
        return le;
    }

    public void readLe(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("le");
	if(a != null) {
	    this.setLe(a.getDoubleValue());
	}
    }

    public void setHe(Double newHe) {
        this.he = newHe;
    }

    public Double getHe() {
        return he;
    }

    public void readHe(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("he");
	if(a != null) {
	    this.setHe(a.getDoubleValue());
	}
    }

    public void setLcorrection(Double newLcorrection) {
        this.lcorrection = newLcorrection;
    }

    public Double getLcorrection() {
        return lcorrection;
    }

    public void readLcorrection(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("lcorrection");
	if(a != null) {
	    this.setLcorrection(a.getDoubleValue());
	}
    }

    public void setHcorrection(Double newHcorrection) {
        this.hcorrection = newHcorrection;
    }

    public Double getHcorrection() {
        return hcorrection;
    }

    public void readHcorrection(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("hcorrection");
	if(a != null) {
	    this.setHcorrection(a.getDoubleValue());
	}
    }

    public void setIntFoil(Integer newIntFoil) {
        this.intFoil = newIntFoil;
    }

    public Integer getIntFoil() {
        return intFoil;
    }

    public void readIntFoil(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("intFoil");
	if(a != null) {
	    this.setIntFoil(a.getIntValue());
	}
    }

    public void setIeFoil(Integer newIeFoil) {
        this.ieFoil = newIeFoil;
    }

    public Integer getIeFoil() {
        return ieFoil;
    }

    public void readIeFoil(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("ieFoil");
	if(a != null) {
	    this.setIeFoil(a.getIntValue());
	}
    }

    public void setExtFoil(Integer newExtFoil) {
        this.extFoil = newExtFoil;
    }

    public Integer getExtFoil() {
        return extFoil;
    }

    public void readExtFoil(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("extFoil");
	if(a != null) {
	    this.setExtFoil(a.getIntValue());
	}
    }

    public void setIsolation(Integer newIsolation) {
        this.isolation = newIsolation;
    }

    public Integer getIsolation() {
        return isolation;
    }

    public void readIsolation(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("isolation");
	if(a != null) {
	    this.setIsolation(a.getIntValue());
	}
    }

    public void setOpeningDir(Integer newOpeningDir) {
        this.openingDir = newOpeningDir;
    }

    public Integer getOpeningDir() {
        return openingDir;
    }

    public void readOpeningDir(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("openingDir");
	if(a != null) {
	    this.setOpeningDir(a.getIntValue());
	}
    }

    public void setOpeningSide(Integer newOpeningSide) {
        this.openingSide = newOpeningSide;
    }

    public Integer getOpeningSide() {
        return openingSide;
    }

    public void readOpeningSide(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("openingSide");
	if(a != null) {
	    this.setOpeningSide(a.getIntValue());
	}
    }

    public void setFrameType(Integer newFrameType) {
        this.frameType = newFrameType;
    }

    public Integer getFrameType() {
        return frameType;
    }

    public void readFrameType(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("frameType");
	if(a != null) {
	    this.setFrameType(a.getIntValue());
	}
    }

    public void setLFrame(Double newLFrame) {
        this.lFrame = newLFrame;
    }

    public Double getLFrame() {
        return lFrame;
    }

    public void readLFrame(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("lFrame");
	if(a != null) {
	    this.setLFrame(a.getDoubleValue());
	}
    }

    public void setBFrame(Double newBFrame) {
        this.bFrame = newBFrame;
    }

    public Double getBFrame() {
        return bFrame;
    }

    public void readBFrame(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("bFrame");
	if(a != null) {
	    this.setBFrame(a.getDoubleValue());
	}
    }

    public void setCFrame(Double newCFrame) {
        this.cFrame = newCFrame;
    }

    public Double getCFrame() {
        return cFrame;
    }

    public void readCFrame(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("cFrame");
	if(a != null) {
	    this.setCFrame(a.getDoubleValue());
	}
    }

    public void setFoilPosition(Integer newFoilPosition) {
        this.foilPosition = newFoilPosition;
    }

    public Integer getFoilPosition() {
        return foilPosition;
    }

    public void readFoilPosition(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("foilPosition");
	if(a != null) {
	    this.setFoilPosition(a.getIntValue());
	}
    }

    public void setTresholdType(Integer newTresholdType) {
        this.tresholdType = newTresholdType;
    }

    public Integer getTresholdType() {
        return tresholdType;
    }

    public void readTresholdType(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("tresholdType");
	if(a != null) {
	    this.setTresholdType(a.getIntValue());
	}
    }

    public void setLTreshold(Double newLTreshold) {
        this.lTreshold = newLTreshold;
    }

    public Double getLTreshold() {
        return lTreshold;
    }

    public void readLTreshold(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("lTreshold");
	if(a != null) {
	    this.setLTreshold(a.getDoubleValue());
	}
    }

    public void setHTreshold(Double newHTreshold) {
        this.hTreshold = newHTreshold;
    }

    public Double getHTreshold() {
        return hTreshold;
    }

    public void readHTreshold(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("hTreshold");
	if(a != null) {
	    this.setHTreshold(a.getDoubleValue());
	}
    }

    public void setCTreshold(Double newCTreshold) {
        this.cTreshold = newCTreshold;
    }

    public Double getCTreshold() {
        return cTreshold;
    }

    public void readCTreshold(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("cTreshold");
	if(a != null) {
	    this.setCTreshold(a.getDoubleValue());
	}
    }

    public void setTresholdSpace(Integer newTresholdSpace) {
        this.tresholdSpace = newTresholdSpace;
    }

    public Integer getTresholdSpace() {
        return tresholdSpace;
    }

    public void readTresholdSpace(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("tresholdSpace");
	if(a != null) {
	    this.setTresholdSpace(a.getIntValue());
	}
    }

    public void setH1Treshold(Double newH1Treshold) {
        this.h1Treshold = newH1Treshold;
    }

    public Double getH1Treshold() {
        return h1Treshold;
    }

    public void readH1Treshold(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("h1Treshold");
	if(a != null) {
	    this.setH1Treshold(a.getDoubleValue());
	}
    }

    public void setH2Treshold(Double newH2Treshold) {
        this.h2Treshold = newH2Treshold;
    }

    public Double getH2Treshold() {
        return h2Treshold;
    }

    public void readH2Treshold(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("h2Treshold");
	if(a != null) {
	    this.setH2Treshold(a.getDoubleValue());
	}
    }

    public void setFereastraId(Integer newFereastraId) {
        this.fereastraId = newFereastraId;
    }

    public Integer getFereastraId() {
        return fereastraId;
    }

    public void readFereastraId(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("fereastraId");
	if(a != null) {
	    this.setFereastraId(a.getIntValue());
	}
    }

    public void setFereastra(String newFereastra) {
        this.fereastra = newFereastra;
    }

    public String getFereastra() {
        return fereastra;
    }

    public void readFereastra(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("fereastra");
	if(a != null) {
	    this.setFereastra(a.getStringValue());
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

}
