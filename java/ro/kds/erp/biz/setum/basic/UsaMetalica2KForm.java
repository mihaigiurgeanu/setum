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
    Double lUtil;
    Double hUtil;
    Double lFoaie;
    Double hFoaie;
    Double lFoaieSec;
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
    String benefBroasca;
    Integer benefBroascaBuc;
    Integer benefBroascaTip;
    String benefCilindru;
    Integer benefCilindruBuc;
    Integer benefCilindruTip;
    String benefSild;
    Integer benefSildBuc;
    Integer benefSildTip;
    String benefYalla;
    Integer benefYallaBuc;
    Integer benefYallaTip;
    String benefBaraAntipanica;
    Integer benefBaraAntipanicaBuc;
    Integer benefBaraAntipanicaTip;
    String benefManer;
    Integer benefManerBuc;
    String benefSelectorOrdine;
    Integer benefSelectorOrdineBuc;
    String benefAmortizor;
    Integer benefAmortizorBuc;
    String benefAlteSisteme1;
    Integer benefAlteSisteme1Buc;
    String benefAlteSisteme2;
    Integer benefAlteSisteme2Buc;
    String sistemeComment;
    String intFinisajBlat;
    Integer intFinisajBlatId;
    String intFinisajToc;
    Integer intFinisajTocId;
    String intFinisajGrilaj;
    Integer intFinisajGrilajId;
    String intFinisajFereastra;
    Integer intFinisajFereastraId;
    String intFinisajSupralumina;
    Integer intFinisajSupraluminaId;
    String intFinisajPanouLateral;
    Integer intFinisajPanouLateralId;
    String extFinisajBlat;
    Integer extFinisajBlatId;
    String extFinisajToc;
    Integer extFinisajTocId;
    String extFinisajGrilaj;
    Integer extFinisajGrilajId;
    String extFinisajFereastra;
    Integer extFinisajFereastraId;
    String extFinisajSupralumina;
    Integer extFinisajSupraluminaId;
    String extFinisajPanouLateral;
    Integer extFinisajPanouLateralId;
    Boolean finisajTocBlat;
    Boolean finisajGrilajBlat;
    Boolean finisajFereastraBlat;
    Boolean finisajSupraluminaBlat;
    Boolean finisajPanouLateralBlat;
    Boolean finisajBlatExtInt;
    Boolean finisajTocExtInt;
    Boolean finisajGrilajExtInt;
    Boolean finisajFereastraExtInt;
    Boolean finisajSupraluminaExtInt;
    Boolean finisajPanouLateralExtInt;

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
   


       this.lUtil = new Double(0);
   


       this.hUtil = new Double(0);
   


       this.lFoaie = new Double(0);
   


       this.hFoaie = new Double(0);
   


       this.lFoaieSec = new Double(0);
   


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



       this.benefBroasca = "";



       this.benefBroascaBuc = new Integer(0);



       this.benefBroascaTip = new Integer(0);



       this.benefCilindru = "";



       this.benefCilindruBuc = new Integer(0);



       this.benefCilindruTip = new Integer(0);



       this.benefSild = "";



       this.benefSildBuc = new Integer(0);



       this.benefSildTip = new Integer(0);



       this.benefYalla = "";



       this.benefYallaBuc = new Integer(0);



       this.benefYallaTip = new Integer(0);



       this.benefBaraAntipanica = "";



       this.benefBaraAntipanicaBuc = new Integer(0);



       this.benefBaraAntipanicaTip = new Integer(0);



       this.benefManer = "";



       this.benefManerBuc = new Integer(0);



       this.benefSelectorOrdine = "";



       this.benefSelectorOrdineBuc = new Integer(0);



       this.benefAmortizor = "";



       this.benefAmortizorBuc = new Integer(0);



       this.benefAlteSisteme1 = "";



       this.benefAlteSisteme1Buc = new Integer(0);



       this.benefAlteSisteme2 = "";



       this.benefAlteSisteme2Buc = new Integer(0);



       this.sistemeComment = "";



       this.intFinisajBlat = "";



       this.intFinisajBlatId = new Integer(0);



       this.intFinisajToc = "";



       this.intFinisajTocId = new Integer(0);



       this.intFinisajGrilaj = "";



       this.intFinisajGrilajId = new Integer(0);



       this.intFinisajFereastra = "";



       this.intFinisajFereastraId = new Integer(0);



       this.intFinisajSupralumina = "";



       this.intFinisajSupraluminaId = new Integer(0);



       this.intFinisajPanouLateral = "";



       this.intFinisajPanouLateralId = new Integer(0);



       this.extFinisajBlat = "";



       this.extFinisajBlatId = new Integer(0);



       this.extFinisajToc = "";



       this.extFinisajTocId = new Integer(0);



       this.extFinisajGrilaj = "";



       this.extFinisajGrilajId = new Integer(0);



       this.extFinisajFereastra = "";



       this.extFinisajFereastraId = new Integer(0);



       this.extFinisajSupralumina = "";



       this.extFinisajSupraluminaId = new Integer(0);



       this.extFinisajPanouLateral = "";



       this.extFinisajPanouLateralId = new Integer(0);



       this.finisajTocBlat = new Boolean(false);



       this.finisajGrilajBlat = new Boolean(false);



       this.finisajFereastraBlat = new Boolean(false);



       this.finisajSupraluminaBlat = new Boolean(false);



       this.finisajPanouLateralBlat = new Boolean(false);



       this.finisajBlatExtInt = new Boolean(false);



       this.finisajTocExtInt = new Boolean(false);



       this.finisajGrilajExtInt = new Boolean(false);



       this.finisajFereastraExtInt = new Boolean(false);



       this.finisajSupraluminaExtInt = new Boolean(false);



       this.finisajPanouLateralExtInt = new Boolean(false);



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

    public void setLUtil(Double newLUtil) {
        this.lUtil = newLUtil;
    }

    public Double getLUtil() {
        return lUtil;
    }

    public void readLUtil(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("lUtil");
	if(a != null) {
	    this.setLUtil(a.getDoubleValue());
	}
    }

    public void setHUtil(Double newHUtil) {
        this.hUtil = newHUtil;
    }

    public Double getHUtil() {
        return hUtil;
    }

    public void readHUtil(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("hUtil");
	if(a != null) {
	    this.setHUtil(a.getDoubleValue());
	}
    }

    public void setLFoaie(Double newLFoaie) {
        this.lFoaie = newLFoaie;
    }

    public Double getLFoaie() {
        return lFoaie;
    }

    public void readLFoaie(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("lFoaie");
	if(a != null) {
	    this.setLFoaie(a.getDoubleValue());
	}
    }

    public void setHFoaie(Double newHFoaie) {
        this.hFoaie = newHFoaie;
    }

    public Double getHFoaie() {
        return hFoaie;
    }

    public void readHFoaie(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("hFoaie");
	if(a != null) {
	    this.setHFoaie(a.getDoubleValue());
	}
    }

    public void setLFoaieSec(Double newLFoaieSec) {
        this.lFoaieSec = newLFoaieSec;
    }

    public Double getLFoaieSec() {
        return lFoaieSec;
    }

    public void readLFoaieSec(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("lFoaieSec");
	if(a != null) {
	    this.setLFoaieSec(a.getDoubleValue());
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

    public void setBenefBroasca(String newBenefBroasca) {
        this.benefBroasca = newBenefBroasca;
    }

    public String getBenefBroasca() {
        return benefBroasca;
    }

    public void readBenefBroasca(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("benefBroasca");
	if(a != null) {
	    this.setBenefBroasca(a.getStringValue());
	}
    }

    public void setBenefBroascaBuc(Integer newBenefBroascaBuc) {
        this.benefBroascaBuc = newBenefBroascaBuc;
    }

    public Integer getBenefBroascaBuc() {
        return benefBroascaBuc;
    }

    public void readBenefBroascaBuc(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("benefBroascaBuc");
	if(a != null) {
	    this.setBenefBroascaBuc(a.getIntValue());
	}
    }

    public void setBenefBroascaTip(Integer newBenefBroascaTip) {
        this.benefBroascaTip = newBenefBroascaTip;
    }

    public Integer getBenefBroascaTip() {
        return benefBroascaTip;
    }

    public void readBenefBroascaTip(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("benefBroascaTip");
	if(a != null) {
	    this.setBenefBroascaTip(a.getIntValue());
	}
    }

    public void setBenefCilindru(String newBenefCilindru) {
        this.benefCilindru = newBenefCilindru;
    }

    public String getBenefCilindru() {
        return benefCilindru;
    }

    public void readBenefCilindru(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("benefCilindru");
	if(a != null) {
	    this.setBenefCilindru(a.getStringValue());
	}
    }

    public void setBenefCilindruBuc(Integer newBenefCilindruBuc) {
        this.benefCilindruBuc = newBenefCilindruBuc;
    }

    public Integer getBenefCilindruBuc() {
        return benefCilindruBuc;
    }

    public void readBenefCilindruBuc(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("benefCilindruBuc");
	if(a != null) {
	    this.setBenefCilindruBuc(a.getIntValue());
	}
    }

    public void setBenefCilindruTip(Integer newBenefCilindruTip) {
        this.benefCilindruTip = newBenefCilindruTip;
    }

    public Integer getBenefCilindruTip() {
        return benefCilindruTip;
    }

    public void readBenefCilindruTip(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("benefCilindruTip");
	if(a != null) {
	    this.setBenefCilindruTip(a.getIntValue());
	}
    }

    public void setBenefSild(String newBenefSild) {
        this.benefSild = newBenefSild;
    }

    public String getBenefSild() {
        return benefSild;
    }

    public void readBenefSild(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("benefSild");
	if(a != null) {
	    this.setBenefSild(a.getStringValue());
	}
    }

    public void setBenefSildBuc(Integer newBenefSildBuc) {
        this.benefSildBuc = newBenefSildBuc;
    }

    public Integer getBenefSildBuc() {
        return benefSildBuc;
    }

    public void readBenefSildBuc(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("benefSildBuc");
	if(a != null) {
	    this.setBenefSildBuc(a.getIntValue());
	}
    }

    public void setBenefSildTip(Integer newBenefSildTip) {
        this.benefSildTip = newBenefSildTip;
    }

    public Integer getBenefSildTip() {
        return benefSildTip;
    }

    public void readBenefSildTip(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("benefSildTip");
	if(a != null) {
	    this.setBenefSildTip(a.getIntValue());
	}
    }

    public void setBenefYalla(String newBenefYalla) {
        this.benefYalla = newBenefYalla;
    }

    public String getBenefYalla() {
        return benefYalla;
    }

    public void readBenefYalla(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("benefYalla");
	if(a != null) {
	    this.setBenefYalla(a.getStringValue());
	}
    }

    public void setBenefYallaBuc(Integer newBenefYallaBuc) {
        this.benefYallaBuc = newBenefYallaBuc;
    }

    public Integer getBenefYallaBuc() {
        return benefYallaBuc;
    }

    public void readBenefYallaBuc(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("benefYallaBuc");
	if(a != null) {
	    this.setBenefYallaBuc(a.getIntValue());
	}
    }

    public void setBenefYallaTip(Integer newBenefYallaTip) {
        this.benefYallaTip = newBenefYallaTip;
    }

    public Integer getBenefYallaTip() {
        return benefYallaTip;
    }

    public void readBenefYallaTip(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("benefYallaTip");
	if(a != null) {
	    this.setBenefYallaTip(a.getIntValue());
	}
    }

    public void setBenefBaraAntipanica(String newBenefBaraAntipanica) {
        this.benefBaraAntipanica = newBenefBaraAntipanica;
    }

    public String getBenefBaraAntipanica() {
        return benefBaraAntipanica;
    }

    public void readBenefBaraAntipanica(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("benefBaraAntipanica");
	if(a != null) {
	    this.setBenefBaraAntipanica(a.getStringValue());
	}
    }

    public void setBenefBaraAntipanicaBuc(Integer newBenefBaraAntipanicaBuc) {
        this.benefBaraAntipanicaBuc = newBenefBaraAntipanicaBuc;
    }

    public Integer getBenefBaraAntipanicaBuc() {
        return benefBaraAntipanicaBuc;
    }

    public void readBenefBaraAntipanicaBuc(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("benefBaraAntipanicaBuc");
	if(a != null) {
	    this.setBenefBaraAntipanicaBuc(a.getIntValue());
	}
    }

    public void setBenefBaraAntipanicaTip(Integer newBenefBaraAntipanicaTip) {
        this.benefBaraAntipanicaTip = newBenefBaraAntipanicaTip;
    }

    public Integer getBenefBaraAntipanicaTip() {
        return benefBaraAntipanicaTip;
    }

    public void readBenefBaraAntipanicaTip(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("benefBaraAntipanicaTip");
	if(a != null) {
	    this.setBenefBaraAntipanicaTip(a.getIntValue());
	}
    }

    public void setBenefManer(String newBenefManer) {
        this.benefManer = newBenefManer;
    }

    public String getBenefManer() {
        return benefManer;
    }

    public void readBenefManer(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("benefManer");
	if(a != null) {
	    this.setBenefManer(a.getStringValue());
	}
    }

    public void setBenefManerBuc(Integer newBenefManerBuc) {
        this.benefManerBuc = newBenefManerBuc;
    }

    public Integer getBenefManerBuc() {
        return benefManerBuc;
    }

    public void readBenefManerBuc(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("benefManerBuc");
	if(a != null) {
	    this.setBenefManerBuc(a.getIntValue());
	}
    }

    public void setBenefSelectorOrdine(String newBenefSelectorOrdine) {
        this.benefSelectorOrdine = newBenefSelectorOrdine;
    }

    public String getBenefSelectorOrdine() {
        return benefSelectorOrdine;
    }

    public void readBenefSelectorOrdine(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("benefSelectorOrdine");
	if(a != null) {
	    this.setBenefSelectorOrdine(a.getStringValue());
	}
    }

    public void setBenefSelectorOrdineBuc(Integer newBenefSelectorOrdineBuc) {
        this.benefSelectorOrdineBuc = newBenefSelectorOrdineBuc;
    }

    public Integer getBenefSelectorOrdineBuc() {
        return benefSelectorOrdineBuc;
    }

    public void readBenefSelectorOrdineBuc(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("benefSelectorOrdineBuc");
	if(a != null) {
	    this.setBenefSelectorOrdineBuc(a.getIntValue());
	}
    }

    public void setBenefAmortizor(String newBenefAmortizor) {
        this.benefAmortizor = newBenefAmortizor;
    }

    public String getBenefAmortizor() {
        return benefAmortizor;
    }

    public void readBenefAmortizor(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("benefAmortizor");
	if(a != null) {
	    this.setBenefAmortizor(a.getStringValue());
	}
    }

    public void setBenefAmortizorBuc(Integer newBenefAmortizorBuc) {
        this.benefAmortizorBuc = newBenefAmortizorBuc;
    }

    public Integer getBenefAmortizorBuc() {
        return benefAmortizorBuc;
    }

    public void readBenefAmortizorBuc(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("benefAmortizorBuc");
	if(a != null) {
	    this.setBenefAmortizorBuc(a.getIntValue());
	}
    }

    public void setBenefAlteSisteme1(String newBenefAlteSisteme1) {
        this.benefAlteSisteme1 = newBenefAlteSisteme1;
    }

    public String getBenefAlteSisteme1() {
        return benefAlteSisteme1;
    }

    public void readBenefAlteSisteme1(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("benefAlteSisteme1");
	if(a != null) {
	    this.setBenefAlteSisteme1(a.getStringValue());
	}
    }

    public void setBenefAlteSisteme1Buc(Integer newBenefAlteSisteme1Buc) {
        this.benefAlteSisteme1Buc = newBenefAlteSisteme1Buc;
    }

    public Integer getBenefAlteSisteme1Buc() {
        return benefAlteSisteme1Buc;
    }

    public void readBenefAlteSisteme1Buc(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("benefAlteSisteme1Buc");
	if(a != null) {
	    this.setBenefAlteSisteme1Buc(a.getIntValue());
	}
    }

    public void setBenefAlteSisteme2(String newBenefAlteSisteme2) {
        this.benefAlteSisteme2 = newBenefAlteSisteme2;
    }

    public String getBenefAlteSisteme2() {
        return benefAlteSisteme2;
    }

    public void readBenefAlteSisteme2(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("benefAlteSisteme2");
	if(a != null) {
	    this.setBenefAlteSisteme2(a.getStringValue());
	}
    }

    public void setBenefAlteSisteme2Buc(Integer newBenefAlteSisteme2Buc) {
        this.benefAlteSisteme2Buc = newBenefAlteSisteme2Buc;
    }

    public Integer getBenefAlteSisteme2Buc() {
        return benefAlteSisteme2Buc;
    }

    public void readBenefAlteSisteme2Buc(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("benefAlteSisteme2Buc");
	if(a != null) {
	    this.setBenefAlteSisteme2Buc(a.getIntValue());
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

    public void setIntFinisajBlat(String newIntFinisajBlat) {
        this.intFinisajBlat = newIntFinisajBlat;
    }

    public String getIntFinisajBlat() {
        return intFinisajBlat;
    }

    public void readIntFinisajBlat(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("intFinisajBlat");
	if(a != null) {
	    this.setIntFinisajBlat(a.getStringValue());
	}
    }

    public void setIntFinisajBlatId(Integer newIntFinisajBlatId) {
        this.intFinisajBlatId = newIntFinisajBlatId;
    }

    public Integer getIntFinisajBlatId() {
        return intFinisajBlatId;
    }

    public void readIntFinisajBlatId(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("intFinisajBlatId");
	if(a != null) {
	    this.setIntFinisajBlatId(a.getIntValue());
	}
    }

    public void setIntFinisajToc(String newIntFinisajToc) {
        this.intFinisajToc = newIntFinisajToc;
    }

    public String getIntFinisajToc() {
        return intFinisajToc;
    }

    public void readIntFinisajToc(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("intFinisajToc");
	if(a != null) {
	    this.setIntFinisajToc(a.getStringValue());
	}
    }

    public void setIntFinisajTocId(Integer newIntFinisajTocId) {
        this.intFinisajTocId = newIntFinisajTocId;
    }

    public Integer getIntFinisajTocId() {
        return intFinisajTocId;
    }

    public void readIntFinisajTocId(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("intFinisajTocId");
	if(a != null) {
	    this.setIntFinisajTocId(a.getIntValue());
	}
    }

    public void setIntFinisajGrilaj(String newIntFinisajGrilaj) {
        this.intFinisajGrilaj = newIntFinisajGrilaj;
    }

    public String getIntFinisajGrilaj() {
        return intFinisajGrilaj;
    }

    public void readIntFinisajGrilaj(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("intFinisajGrilaj");
	if(a != null) {
	    this.setIntFinisajGrilaj(a.getStringValue());
	}
    }

    public void setIntFinisajGrilajId(Integer newIntFinisajGrilajId) {
        this.intFinisajGrilajId = newIntFinisajGrilajId;
    }

    public Integer getIntFinisajGrilajId() {
        return intFinisajGrilajId;
    }

    public void readIntFinisajGrilajId(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("intFinisajGrilajId");
	if(a != null) {
	    this.setIntFinisajGrilajId(a.getIntValue());
	}
    }

    public void setIntFinisajFereastra(String newIntFinisajFereastra) {
        this.intFinisajFereastra = newIntFinisajFereastra;
    }

    public String getIntFinisajFereastra() {
        return intFinisajFereastra;
    }

    public void readIntFinisajFereastra(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("intFinisajFereastra");
	if(a != null) {
	    this.setIntFinisajFereastra(a.getStringValue());
	}
    }

    public void setIntFinisajFereastraId(Integer newIntFinisajFereastraId) {
        this.intFinisajFereastraId = newIntFinisajFereastraId;
    }

    public Integer getIntFinisajFereastraId() {
        return intFinisajFereastraId;
    }

    public void readIntFinisajFereastraId(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("intFinisajFereastraId");
	if(a != null) {
	    this.setIntFinisajFereastraId(a.getIntValue());
	}
    }

    public void setIntFinisajSupralumina(String newIntFinisajSupralumina) {
        this.intFinisajSupralumina = newIntFinisajSupralumina;
    }

    public String getIntFinisajSupralumina() {
        return intFinisajSupralumina;
    }

    public void readIntFinisajSupralumina(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("intFinisajSupralumina");
	if(a != null) {
	    this.setIntFinisajSupralumina(a.getStringValue());
	}
    }

    public void setIntFinisajSupraluminaId(Integer newIntFinisajSupraluminaId) {
        this.intFinisajSupraluminaId = newIntFinisajSupraluminaId;
    }

    public Integer getIntFinisajSupraluminaId() {
        return intFinisajSupraluminaId;
    }

    public void readIntFinisajSupraluminaId(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("intFinisajSupraluminaId");
	if(a != null) {
	    this.setIntFinisajSupraluminaId(a.getIntValue());
	}
    }

    public void setIntFinisajPanouLateral(String newIntFinisajPanouLateral) {
        this.intFinisajPanouLateral = newIntFinisajPanouLateral;
    }

    public String getIntFinisajPanouLateral() {
        return intFinisajPanouLateral;
    }

    public void readIntFinisajPanouLateral(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("intFinisajPanouLateral");
	if(a != null) {
	    this.setIntFinisajPanouLateral(a.getStringValue());
	}
    }

    public void setIntFinisajPanouLateralId(Integer newIntFinisajPanouLateralId) {
        this.intFinisajPanouLateralId = newIntFinisajPanouLateralId;
    }

    public Integer getIntFinisajPanouLateralId() {
        return intFinisajPanouLateralId;
    }

    public void readIntFinisajPanouLateralId(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("intFinisajPanouLateralId");
	if(a != null) {
	    this.setIntFinisajPanouLateralId(a.getIntValue());
	}
    }

    public void setExtFinisajBlat(String newExtFinisajBlat) {
        this.extFinisajBlat = newExtFinisajBlat;
    }

    public String getExtFinisajBlat() {
        return extFinisajBlat;
    }

    public void readExtFinisajBlat(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("extFinisajBlat");
	if(a != null) {
	    this.setExtFinisajBlat(a.getStringValue());
	}
    }

    public void setExtFinisajBlatId(Integer newExtFinisajBlatId) {
        this.extFinisajBlatId = newExtFinisajBlatId;
    }

    public Integer getExtFinisajBlatId() {
        return extFinisajBlatId;
    }

    public void readExtFinisajBlatId(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("extFinisajBlatId");
	if(a != null) {
	    this.setExtFinisajBlatId(a.getIntValue());
	}
    }

    public void setExtFinisajToc(String newExtFinisajToc) {
        this.extFinisajToc = newExtFinisajToc;
    }

    public String getExtFinisajToc() {
        return extFinisajToc;
    }

    public void readExtFinisajToc(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("extFinisajToc");
	if(a != null) {
	    this.setExtFinisajToc(a.getStringValue());
	}
    }

    public void setExtFinisajTocId(Integer newExtFinisajTocId) {
        this.extFinisajTocId = newExtFinisajTocId;
    }

    public Integer getExtFinisajTocId() {
        return extFinisajTocId;
    }

    public void readExtFinisajTocId(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("extFinisajTocId");
	if(a != null) {
	    this.setExtFinisajTocId(a.getIntValue());
	}
    }

    public void setExtFinisajGrilaj(String newExtFinisajGrilaj) {
        this.extFinisajGrilaj = newExtFinisajGrilaj;
    }

    public String getExtFinisajGrilaj() {
        return extFinisajGrilaj;
    }

    public void readExtFinisajGrilaj(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("extFinisajGrilaj");
	if(a != null) {
	    this.setExtFinisajGrilaj(a.getStringValue());
	}
    }

    public void setExtFinisajGrilajId(Integer newExtFinisajGrilajId) {
        this.extFinisajGrilajId = newExtFinisajGrilajId;
    }

    public Integer getExtFinisajGrilajId() {
        return extFinisajGrilajId;
    }

    public void readExtFinisajGrilajId(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("extFinisajGrilajId");
	if(a != null) {
	    this.setExtFinisajGrilajId(a.getIntValue());
	}
    }

    public void setExtFinisajFereastra(String newExtFinisajFereastra) {
        this.extFinisajFereastra = newExtFinisajFereastra;
    }

    public String getExtFinisajFereastra() {
        return extFinisajFereastra;
    }

    public void readExtFinisajFereastra(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("extFinisajFereastra");
	if(a != null) {
	    this.setExtFinisajFereastra(a.getStringValue());
	}
    }

    public void setExtFinisajFereastraId(Integer newExtFinisajFereastraId) {
        this.extFinisajFereastraId = newExtFinisajFereastraId;
    }

    public Integer getExtFinisajFereastraId() {
        return extFinisajFereastraId;
    }

    public void readExtFinisajFereastraId(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("extFinisajFereastraId");
	if(a != null) {
	    this.setExtFinisajFereastraId(a.getIntValue());
	}
    }

    public void setExtFinisajSupralumina(String newExtFinisajSupralumina) {
        this.extFinisajSupralumina = newExtFinisajSupralumina;
    }

    public String getExtFinisajSupralumina() {
        return extFinisajSupralumina;
    }

    public void readExtFinisajSupralumina(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("extFinisajSupralumina");
	if(a != null) {
	    this.setExtFinisajSupralumina(a.getStringValue());
	}
    }

    public void setExtFinisajSupraluminaId(Integer newExtFinisajSupraluminaId) {
        this.extFinisajSupraluminaId = newExtFinisajSupraluminaId;
    }

    public Integer getExtFinisajSupraluminaId() {
        return extFinisajSupraluminaId;
    }

    public void readExtFinisajSupraluminaId(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("extFinisajSupraluminaId");
	if(a != null) {
	    this.setExtFinisajSupraluminaId(a.getIntValue());
	}
    }

    public void setExtFinisajPanouLateral(String newExtFinisajPanouLateral) {
        this.extFinisajPanouLateral = newExtFinisajPanouLateral;
    }

    public String getExtFinisajPanouLateral() {
        return extFinisajPanouLateral;
    }

    public void readExtFinisajPanouLateral(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("extFinisajPanouLateral");
	if(a != null) {
	    this.setExtFinisajPanouLateral(a.getStringValue());
	}
    }

    public void setExtFinisajPanouLateralId(Integer newExtFinisajPanouLateralId) {
        this.extFinisajPanouLateralId = newExtFinisajPanouLateralId;
    }

    public Integer getExtFinisajPanouLateralId() {
        return extFinisajPanouLateralId;
    }

    public void readExtFinisajPanouLateralId(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("extFinisajPanouLateralId");
	if(a != null) {
	    this.setExtFinisajPanouLateralId(a.getIntValue());
	}
    }

    public void setFinisajTocBlat(Boolean newFinisajTocBlat) {
        this.finisajTocBlat = newFinisajTocBlat;
    }

    public Boolean getFinisajTocBlat() {
        return finisajTocBlat;
    }

    public void readFinisajTocBlat(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("finisajTocBlat");
	if(a != null) {
	    this.setFinisajTocBlat(a.getBoolValue());
	}
    }

    public void setFinisajGrilajBlat(Boolean newFinisajGrilajBlat) {
        this.finisajGrilajBlat = newFinisajGrilajBlat;
    }

    public Boolean getFinisajGrilajBlat() {
        return finisajGrilajBlat;
    }

    public void readFinisajGrilajBlat(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("finisajGrilajBlat");
	if(a != null) {
	    this.setFinisajGrilajBlat(a.getBoolValue());
	}
    }

    public void setFinisajFereastraBlat(Boolean newFinisajFereastraBlat) {
        this.finisajFereastraBlat = newFinisajFereastraBlat;
    }

    public Boolean getFinisajFereastraBlat() {
        return finisajFereastraBlat;
    }

    public void readFinisajFereastraBlat(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("finisajFereastraBlat");
	if(a != null) {
	    this.setFinisajFereastraBlat(a.getBoolValue());
	}
    }

    public void setFinisajSupraluminaBlat(Boolean newFinisajSupraluminaBlat) {
        this.finisajSupraluminaBlat = newFinisajSupraluminaBlat;
    }

    public Boolean getFinisajSupraluminaBlat() {
        return finisajSupraluminaBlat;
    }

    public void readFinisajSupraluminaBlat(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("finisajSupraluminaBlat");
	if(a != null) {
	    this.setFinisajSupraluminaBlat(a.getBoolValue());
	}
    }

    public void setFinisajPanouLateralBlat(Boolean newFinisajPanouLateralBlat) {
        this.finisajPanouLateralBlat = newFinisajPanouLateralBlat;
    }

    public Boolean getFinisajPanouLateralBlat() {
        return finisajPanouLateralBlat;
    }

    public void readFinisajPanouLateralBlat(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("finisajPanouLateralBlat");
	if(a != null) {
	    this.setFinisajPanouLateralBlat(a.getBoolValue());
	}
    }

    public void setFinisajBlatExtInt(Boolean newFinisajBlatExtInt) {
        this.finisajBlatExtInt = newFinisajBlatExtInt;
    }

    public Boolean getFinisajBlatExtInt() {
        return finisajBlatExtInt;
    }

    public void readFinisajBlatExtInt(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("finisajBlatExtInt");
	if(a != null) {
	    this.setFinisajBlatExtInt(a.getBoolValue());
	}
    }

    public void setFinisajTocExtInt(Boolean newFinisajTocExtInt) {
        this.finisajTocExtInt = newFinisajTocExtInt;
    }

    public Boolean getFinisajTocExtInt() {
        return finisajTocExtInt;
    }

    public void readFinisajTocExtInt(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("finisajTocExtInt");
	if(a != null) {
	    this.setFinisajTocExtInt(a.getBoolValue());
	}
    }

    public void setFinisajGrilajExtInt(Boolean newFinisajGrilajExtInt) {
        this.finisajGrilajExtInt = newFinisajGrilajExtInt;
    }

    public Boolean getFinisajGrilajExtInt() {
        return finisajGrilajExtInt;
    }

    public void readFinisajGrilajExtInt(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("finisajGrilajExtInt");
	if(a != null) {
	    this.setFinisajGrilajExtInt(a.getBoolValue());
	}
    }

    public void setFinisajFereastraExtInt(Boolean newFinisajFereastraExtInt) {
        this.finisajFereastraExtInt = newFinisajFereastraExtInt;
    }

    public Boolean getFinisajFereastraExtInt() {
        return finisajFereastraExtInt;
    }

    public void readFinisajFereastraExtInt(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("finisajFereastraExtInt");
	if(a != null) {
	    this.setFinisajFereastraExtInt(a.getBoolValue());
	}
    }

    public void setFinisajSupraluminaExtInt(Boolean newFinisajSupraluminaExtInt) {
        this.finisajSupraluminaExtInt = newFinisajSupraluminaExtInt;
    }

    public Boolean getFinisajSupraluminaExtInt() {
        return finisajSupraluminaExtInt;
    }

    public void readFinisajSupraluminaExtInt(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("finisajSupraluminaExtInt");
	if(a != null) {
	    this.setFinisajSupraluminaExtInt(a.getBoolValue());
	}
    }

    public void setFinisajPanouLateralExtInt(Boolean newFinisajPanouLateralExtInt) {
        this.finisajPanouLateralExtInt = newFinisajPanouLateralExtInt;
    }

    public Boolean getFinisajPanouLateralExtInt() {
        return finisajPanouLateralExtInt;
    }

    public void readFinisajPanouLateralExtInt(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("finisajPanouLateralExtInt");
	if(a != null) {
	    this.setFinisajPanouLateralExtInt(a.getBoolValue());
	}
    }

}
