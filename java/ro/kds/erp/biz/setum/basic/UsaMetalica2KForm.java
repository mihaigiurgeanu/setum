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
public class UsaMetalica2KForm implements Serializable {
        
    String code;
    String name;
    String description;
    String subclass;
    String version;
    Integer material;
    Integer k;
    Double lg;
    Double hg;
    Double le;
    Double he;
    Double lcorrection;
    Double hcorrection;
    Double lCurrent;
    Integer kType;
    Integer intFoil;
    Integer ieFoil;
    Integer extFoil;
    Integer intFoilSec;
    Integer ieFoilSec;
    Integer extFoilSec;
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
    java.math.BigDecimal entryPrice;
    java.math.BigDecimal sellPrice;
    Integer montareSistem;
    Integer decupareSistemId;
    Integer sistemSetumSauBeneficiar;
    Integer broascaId;
    Integer broascaBuc;
    Integer cilindruId;
    Integer cilindruBuc;
    Integer copiatCheieId;
    Integer copiatCheieBuc;
    Integer sildId;
    String sildTip;
    String sildCuloare;
    Integer sildBuc;
    Integer rozetaId;
    String rozetaTip;
    String rozetaCuloare;
    Integer rozetaBuc;
    Integer manerId;
    String manerTip;
    String manerCuloare;
    Integer manerBuc;
    Integer yalla1Id;
    Integer yalla1Buc;
    Integer yalla2Id;
    Integer yalla2Buc;
    Integer baraAntipanicaId;
    Integer baraAntipanicaBuc;
    Integer manerSemicilindruId;
    Integer manerSemicilindruBuc;
    Integer selectorOrdineId;
    Integer selectorOrdineBuc;
    Integer amortizorId;
    Integer amortizorBuc;
    Integer alteSisteme1Id;
    Integer alteSisteme1Buc;
    Integer alteSisteme2Id;
    Integer alteSisteme2Buc;
    String sistemeComment;

    public UsaMetalica2KForm() {


       this.code = "";



       this.name = "";



       this.description = "";



       this.subclass = "";



       this.version = "";



       this.material = new Integer(0);



       this.k = new Integer(0);



       this.lg = new Double(0);
   


       this.hg = new Double(0);
   


       this.le = new Double(0);
   


       this.he = new Double(0);
   


       this.lcorrection = new Double(0);
   


       this.hcorrection = new Double(0);
   


       this.lCurrent = new Double(0);
   


       this.kType = new Integer(0);



       this.intFoil = new Integer(0);



       this.ieFoil = new Integer(0);



       this.extFoil = new Integer(0);



       this.intFoilSec = new Integer(0);



       this.ieFoilSec = new Integer(0);



       this.extFoilSec = new Integer(0);



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
   


       this.entryPrice = new java.math.BigDecimal(0);



       this.sellPrice = new java.math.BigDecimal(0);



       this.montareSistem = new Integer(0);



       this.decupareSistemId = new Integer(0);



       this.sistemSetumSauBeneficiar = new Integer(0);



       this.broascaId = new Integer(0);



       this.broascaBuc = new Integer(0);



       this.cilindruId = new Integer(0);



       this.cilindruBuc = new Integer(0);



       this.copiatCheieId = new Integer(0);



       this.copiatCheieBuc = new Integer(0);



       this.sildId = new Integer(0);



       this.sildTip = "";



       this.sildCuloare = "";



       this.sildBuc = new Integer(0);



       this.rozetaId = new Integer(0);



       this.rozetaTip = "";



       this.rozetaCuloare = "";



       this.rozetaBuc = new Integer(0);



       this.manerId = new Integer(0);



       this.manerTip = "";



       this.manerCuloare = "";



       this.manerBuc = new Integer(0);



       this.yalla1Id = new Integer(0);



       this.yalla1Buc = new Integer(0);



       this.yalla2Id = new Integer(0);



       this.yalla2Buc = new Integer(0);



       this.baraAntipanicaId = new Integer(0);



       this.baraAntipanicaBuc = new Integer(0);



       this.manerSemicilindruId = new Integer(0);



       this.manerSemicilindruBuc = new Integer(0);



       this.selectorOrdineId = new Integer(0);



       this.selectorOrdineBuc = new Integer(0);



       this.amortizorId = new Integer(0);



       this.amortizorBuc = new Integer(0);



       this.alteSisteme1Id = new Integer(0);



       this.alteSisteme1Buc = new Integer(0);



       this.alteSisteme2Id = new Integer(0);



       this.alteSisteme2Buc = new Integer(0);



       this.sistemeComment = "";



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

    public void setK(Integer newK) {
        this.k = newK;
    }

    public Integer getK() {
        return k;
    }

    public void readK(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("k");
	if(a != null) {
	    this.setK(a.getIntValue());
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

    public void setLCurrent(Double newLCurrent) {
        this.lCurrent = newLCurrent;
    }

    public Double getLCurrent() {
        return lCurrent;
    }

    public void readLCurrent(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("lCurrent");
	if(a != null) {
	    this.setLCurrent(a.getDoubleValue());
	}
    }

    public void setKType(Integer newKType) {
        this.kType = newKType;
    }

    public Integer getKType() {
        return kType;
    }

    public void readKType(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("kType");
	if(a != null) {
	    this.setKType(a.getIntValue());
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

    public void setIntFoilSec(Integer newIntFoilSec) {
        this.intFoilSec = newIntFoilSec;
    }

    public Integer getIntFoilSec() {
        return intFoilSec;
    }

    public void readIntFoilSec(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("intFoilSec");
	if(a != null) {
	    this.setIntFoilSec(a.getIntValue());
	}
    }

    public void setIeFoilSec(Integer newIeFoilSec) {
        this.ieFoilSec = newIeFoilSec;
    }

    public Integer getIeFoilSec() {
        return ieFoilSec;
    }

    public void readIeFoilSec(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("ieFoilSec");
	if(a != null) {
	    this.setIeFoilSec(a.getIntValue());
	}
    }

    public void setExtFoilSec(Integer newExtFoilSec) {
        this.extFoilSec = newExtFoilSec;
    }

    public Integer getExtFoilSec() {
        return extFoilSec;
    }

    public void readExtFoilSec(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("extFoilSec");
	if(a != null) {
	    this.setExtFoilSec(a.getIntValue());
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

    public void setMontareSistem(Integer newMontareSistem) {
        this.montareSistem = newMontareSistem;
    }

    public Integer getMontareSistem() {
        return montareSistem;
    }

    public void readMontareSistem(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("montareSistem");
	if(a != null) {
	    this.setMontareSistem(a.getIntValue());
	}
    }

    public void setDecupareSistemId(Integer newDecupareSistemId) {
        this.decupareSistemId = newDecupareSistemId;
    }

    public Integer getDecupareSistemId() {
        return decupareSistemId;
    }

    public void readDecupareSistemId(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("decupareSistemId");
	if(a != null) {
	    this.setDecupareSistemId(a.getIntValue());
	}
    }

    public void setSistemSetumSauBeneficiar(Integer newSistemSetumSauBeneficiar) {
        this.sistemSetumSauBeneficiar = newSistemSetumSauBeneficiar;
    }

    public Integer getSistemSetumSauBeneficiar() {
        return sistemSetumSauBeneficiar;
    }

    public void readSistemSetumSauBeneficiar(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("sistemSetumSauBeneficiar");
	if(a != null) {
	    this.setSistemSetumSauBeneficiar(a.getIntValue());
	}
    }

    public void setBroascaId(Integer newBroascaId) {
        this.broascaId = newBroascaId;
    }

    public Integer getBroascaId() {
        return broascaId;
    }

    public void readBroascaId(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("broascaId");
	if(a != null) {
	    this.setBroascaId(a.getIntValue());
	}
    }

    public void setBroascaBuc(Integer newBroascaBuc) {
        this.broascaBuc = newBroascaBuc;
    }

    public Integer getBroascaBuc() {
        return broascaBuc;
    }

    public void readBroascaBuc(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("broascaBuc");
	if(a != null) {
	    this.setBroascaBuc(a.getIntValue());
	}
    }

    public void setCilindruId(Integer newCilindruId) {
        this.cilindruId = newCilindruId;
    }

    public Integer getCilindruId() {
        return cilindruId;
    }

    public void readCilindruId(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("cilindruId");
	if(a != null) {
	    this.setCilindruId(a.getIntValue());
	}
    }

    public void setCilindruBuc(Integer newCilindruBuc) {
        this.cilindruBuc = newCilindruBuc;
    }

    public Integer getCilindruBuc() {
        return cilindruBuc;
    }

    public void readCilindruBuc(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("cilindruBuc");
	if(a != null) {
	    this.setCilindruBuc(a.getIntValue());
	}
    }

    public void setCopiatCheieId(Integer newCopiatCheieId) {
        this.copiatCheieId = newCopiatCheieId;
    }

    public Integer getCopiatCheieId() {
        return copiatCheieId;
    }

    public void readCopiatCheieId(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("copiatCheieId");
	if(a != null) {
	    this.setCopiatCheieId(a.getIntValue());
	}
    }

    public void setCopiatCheieBuc(Integer newCopiatCheieBuc) {
        this.copiatCheieBuc = newCopiatCheieBuc;
    }

    public Integer getCopiatCheieBuc() {
        return copiatCheieBuc;
    }

    public void readCopiatCheieBuc(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("copiatCheieBuc");
	if(a != null) {
	    this.setCopiatCheieBuc(a.getIntValue());
	}
    }

    public void setSildId(Integer newSildId) {
        this.sildId = newSildId;
    }

    public Integer getSildId() {
        return sildId;
    }

    public void readSildId(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("sildId");
	if(a != null) {
	    this.setSildId(a.getIntValue());
	}
    }

    public void setSildTip(String newSildTip) {
        this.sildTip = newSildTip;
    }

    public String getSildTip() {
        return sildTip;
    }

    public void readSildTip(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("sildTip");
	if(a != null) {
	    this.setSildTip(a.getStringValue());
	}
    }

    public void setSildCuloare(String newSildCuloare) {
        this.sildCuloare = newSildCuloare;
    }

    public String getSildCuloare() {
        return sildCuloare;
    }

    public void readSildCuloare(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("sildCuloare");
	if(a != null) {
	    this.setSildCuloare(a.getStringValue());
	}
    }

    public void setSildBuc(Integer newSildBuc) {
        this.sildBuc = newSildBuc;
    }

    public Integer getSildBuc() {
        return sildBuc;
    }

    public void readSildBuc(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("sildBuc");
	if(a != null) {
	    this.setSildBuc(a.getIntValue());
	}
    }

    public void setRozetaId(Integer newRozetaId) {
        this.rozetaId = newRozetaId;
    }

    public Integer getRozetaId() {
        return rozetaId;
    }

    public void readRozetaId(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("rozetaId");
	if(a != null) {
	    this.setRozetaId(a.getIntValue());
	}
    }

    public void setRozetaTip(String newRozetaTip) {
        this.rozetaTip = newRozetaTip;
    }

    public String getRozetaTip() {
        return rozetaTip;
    }

    public void readRozetaTip(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("rozetaTip");
	if(a != null) {
	    this.setRozetaTip(a.getStringValue());
	}
    }

    public void setRozetaCuloare(String newRozetaCuloare) {
        this.rozetaCuloare = newRozetaCuloare;
    }

    public String getRozetaCuloare() {
        return rozetaCuloare;
    }

    public void readRozetaCuloare(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("rozetaCuloare");
	if(a != null) {
	    this.setRozetaCuloare(a.getStringValue());
	}
    }

    public void setRozetaBuc(Integer newRozetaBuc) {
        this.rozetaBuc = newRozetaBuc;
    }

    public Integer getRozetaBuc() {
        return rozetaBuc;
    }

    public void readRozetaBuc(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("rozetaBuc");
	if(a != null) {
	    this.setRozetaBuc(a.getIntValue());
	}
    }

    public void setManerId(Integer newManerId) {
        this.manerId = newManerId;
    }

    public Integer getManerId() {
        return manerId;
    }

    public void readManerId(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("manerId");
	if(a != null) {
	    this.setManerId(a.getIntValue());
	}
    }

    public void setManerTip(String newManerTip) {
        this.manerTip = newManerTip;
    }

    public String getManerTip() {
        return manerTip;
    }

    public void readManerTip(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("manerTip");
	if(a != null) {
	    this.setManerTip(a.getStringValue());
	}
    }

    public void setManerCuloare(String newManerCuloare) {
        this.manerCuloare = newManerCuloare;
    }

    public String getManerCuloare() {
        return manerCuloare;
    }

    public void readManerCuloare(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("manerCuloare");
	if(a != null) {
	    this.setManerCuloare(a.getStringValue());
	}
    }

    public void setManerBuc(Integer newManerBuc) {
        this.manerBuc = newManerBuc;
    }

    public Integer getManerBuc() {
        return manerBuc;
    }

    public void readManerBuc(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("manerBuc");
	if(a != null) {
	    this.setManerBuc(a.getIntValue());
	}
    }

    public void setYalla1Id(Integer newYalla1Id) {
        this.yalla1Id = newYalla1Id;
    }

    public Integer getYalla1Id() {
        return yalla1Id;
    }

    public void readYalla1Id(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("yalla1Id");
	if(a != null) {
	    this.setYalla1Id(a.getIntValue());
	}
    }

    public void setYalla1Buc(Integer newYalla1Buc) {
        this.yalla1Buc = newYalla1Buc;
    }

    public Integer getYalla1Buc() {
        return yalla1Buc;
    }

    public void readYalla1Buc(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("yalla1Buc");
	if(a != null) {
	    this.setYalla1Buc(a.getIntValue());
	}
    }

    public void setYalla2Id(Integer newYalla2Id) {
        this.yalla2Id = newYalla2Id;
    }

    public Integer getYalla2Id() {
        return yalla2Id;
    }

    public void readYalla2Id(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("yalla2Id");
	if(a != null) {
	    this.setYalla2Id(a.getIntValue());
	}
    }

    public void setYalla2Buc(Integer newYalla2Buc) {
        this.yalla2Buc = newYalla2Buc;
    }

    public Integer getYalla2Buc() {
        return yalla2Buc;
    }

    public void readYalla2Buc(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("yalla2Buc");
	if(a != null) {
	    this.setYalla2Buc(a.getIntValue());
	}
    }

    public void setBaraAntipanicaId(Integer newBaraAntipanicaId) {
        this.baraAntipanicaId = newBaraAntipanicaId;
    }

    public Integer getBaraAntipanicaId() {
        return baraAntipanicaId;
    }

    public void readBaraAntipanicaId(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("baraAntipanicaId");
	if(a != null) {
	    this.setBaraAntipanicaId(a.getIntValue());
	}
    }

    public void setBaraAntipanicaBuc(Integer newBaraAntipanicaBuc) {
        this.baraAntipanicaBuc = newBaraAntipanicaBuc;
    }

    public Integer getBaraAntipanicaBuc() {
        return baraAntipanicaBuc;
    }

    public void readBaraAntipanicaBuc(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("baraAntipanicaBuc");
	if(a != null) {
	    this.setBaraAntipanicaBuc(a.getIntValue());
	}
    }

    public void setManerSemicilindruId(Integer newManerSemicilindruId) {
        this.manerSemicilindruId = newManerSemicilindruId;
    }

    public Integer getManerSemicilindruId() {
        return manerSemicilindruId;
    }

    public void readManerSemicilindruId(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("manerSemicilindruId");
	if(a != null) {
	    this.setManerSemicilindruId(a.getIntValue());
	}
    }

    public void setManerSemicilindruBuc(Integer newManerSemicilindruBuc) {
        this.manerSemicilindruBuc = newManerSemicilindruBuc;
    }

    public Integer getManerSemicilindruBuc() {
        return manerSemicilindruBuc;
    }

    public void readManerSemicilindruBuc(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("manerSemicilindruBuc");
	if(a != null) {
	    this.setManerSemicilindruBuc(a.getIntValue());
	}
    }

    public void setSelectorOrdineId(Integer newSelectorOrdineId) {
        this.selectorOrdineId = newSelectorOrdineId;
    }

    public Integer getSelectorOrdineId() {
        return selectorOrdineId;
    }

    public void readSelectorOrdineId(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("selectorOrdineId");
	if(a != null) {
	    this.setSelectorOrdineId(a.getIntValue());
	}
    }

    public void setSelectorOrdineBuc(Integer newSelectorOrdineBuc) {
        this.selectorOrdineBuc = newSelectorOrdineBuc;
    }

    public Integer getSelectorOrdineBuc() {
        return selectorOrdineBuc;
    }

    public void readSelectorOrdineBuc(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("selectorOrdineBuc");
	if(a != null) {
	    this.setSelectorOrdineBuc(a.getIntValue());
	}
    }

    public void setAmortizorId(Integer newAmortizorId) {
        this.amortizorId = newAmortizorId;
    }

    public Integer getAmortizorId() {
        return amortizorId;
    }

    public void readAmortizorId(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("amortizorId");
	if(a != null) {
	    this.setAmortizorId(a.getIntValue());
	}
    }

    public void setAmortizorBuc(Integer newAmortizorBuc) {
        this.amortizorBuc = newAmortizorBuc;
    }

    public Integer getAmortizorBuc() {
        return amortizorBuc;
    }

    public void readAmortizorBuc(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("amortizorBuc");
	if(a != null) {
	    this.setAmortizorBuc(a.getIntValue());
	}
    }

    public void setAlteSisteme1Id(Integer newAlteSisteme1Id) {
        this.alteSisteme1Id = newAlteSisteme1Id;
    }

    public Integer getAlteSisteme1Id() {
        return alteSisteme1Id;
    }

    public void readAlteSisteme1Id(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("alteSisteme1Id");
	if(a != null) {
	    this.setAlteSisteme1Id(a.getIntValue());
	}
    }

    public void setAlteSisteme1Buc(Integer newAlteSisteme1Buc) {
        this.alteSisteme1Buc = newAlteSisteme1Buc;
    }

    public Integer getAlteSisteme1Buc() {
        return alteSisteme1Buc;
    }

    public void readAlteSisteme1Buc(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("alteSisteme1Buc");
	if(a != null) {
	    this.setAlteSisteme1Buc(a.getIntValue());
	}
    }

    public void setAlteSisteme2Id(Integer newAlteSisteme2Id) {
        this.alteSisteme2Id = newAlteSisteme2Id;
    }

    public Integer getAlteSisteme2Id() {
        return alteSisteme2Id;
    }

    public void readAlteSisteme2Id(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("alteSisteme2Id");
	if(a != null) {
	    this.setAlteSisteme2Id(a.getIntValue());
	}
    }

    public void setAlteSisteme2Buc(Integer newAlteSisteme2Buc) {
        this.alteSisteme2Buc = newAlteSisteme2Buc;
    }

    public Integer getAlteSisteme2Buc() {
        return alteSisteme2Buc;
    }

    public void readAlteSisteme2Buc(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("alteSisteme2Buc");
	if(a != null) {
	    this.setAlteSisteme2Buc(a.getIntValue());
	}
    }

    public void setSistemeComment(String newSistemeComment) {
        this.sistemeComment = newSistemeComment;
    }

    public String getSistemeComment() {
        return sistemeComment;
    }

    public void readSistemeComment(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("sistemeComment");
	if(a != null) {
	    this.setSistemeComment(a.getStringValue());
	}
    }

}
