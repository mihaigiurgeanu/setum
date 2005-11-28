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
public class UsaMetalica1KForm implements Serializable {
        
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

    public void setSubclass(String newSubclass) {
        this.subclass = newSubclass;
    }

    public String getSubclass() {
        return subclass;
    }

    public void setVersion(String newVersion) {
        this.version = newVersion;
    }

    public String getVersion() {
        return version;
    }

    public void setMaterial(Integer newMaterial) {
        this.material = newMaterial;
    }

    public Integer getMaterial() {
        return material;
    }

    public void setLg(Double newLg) {
        this.lg = newLg;
    }

    public Double getLg() {
        return lg;
    }

    public void setHg(Double newHg) {
        this.hg = newHg;
    }

    public Double getHg() {
        return hg;
    }

    public void setLe(Double newLe) {
        this.le = newLe;
    }

    public Double getLe() {
        return le;
    }

    public void setHe(Double newHe) {
        this.he = newHe;
    }

    public Double getHe() {
        return he;
    }

    public void setLcorrection(Double newLcorrection) {
        this.lcorrection = newLcorrection;
    }

    public Double getLcorrection() {
        return lcorrection;
    }

    public void setHcorrection(Double newHcorrection) {
        this.hcorrection = newHcorrection;
    }

    public Double getHcorrection() {
        return hcorrection;
    }

    public void setIntFoil(Integer newIntFoil) {
        this.intFoil = newIntFoil;
    }

    public Integer getIntFoil() {
        return intFoil;
    }

    public void setIeFoil(Integer newIeFoil) {
        this.ieFoil = newIeFoil;
    }

    public Integer getIeFoil() {
        return ieFoil;
    }

    public void setExtFoil(Integer newExtFoil) {
        this.extFoil = newExtFoil;
    }

    public Integer getExtFoil() {
        return extFoil;
    }

    public void setIsolation(Integer newIsolation) {
        this.isolation = newIsolation;
    }

    public Integer getIsolation() {
        return isolation;
    }

    public void setOpeningDir(Integer newOpeningDir) {
        this.openingDir = newOpeningDir;
    }

    public Integer getOpeningDir() {
        return openingDir;
    }

    public void setOpeningSide(Integer newOpeningSide) {
        this.openingSide = newOpeningSide;
    }

    public Integer getOpeningSide() {
        return openingSide;
    }

    public void setFrameType(Integer newFrameType) {
        this.frameType = newFrameType;
    }

    public Integer getFrameType() {
        return frameType;
    }

    public void setLFrame(Double newLFrame) {
        this.lFrame = newLFrame;
    }

    public Double getLFrame() {
        return lFrame;
    }

    public void setBFrame(Double newBFrame) {
        this.bFrame = newBFrame;
    }

    public Double getBFrame() {
        return bFrame;
    }

    public void setCFrame(Double newCFrame) {
        this.cFrame = newCFrame;
    }

    public Double getCFrame() {
        return cFrame;
    }

    public void setFoilPosition(Integer newFoilPosition) {
        this.foilPosition = newFoilPosition;
    }

    public Integer getFoilPosition() {
        return foilPosition;
    }

    public void setTresholdType(Integer newTresholdType) {
        this.tresholdType = newTresholdType;
    }

    public Integer getTresholdType() {
        return tresholdType;
    }

    public void setLTreshold(Double newLTreshold) {
        this.lTreshold = newLTreshold;
    }

    public Double getLTreshold() {
        return lTreshold;
    }

    public void setHTreshold(Double newHTreshold) {
        this.hTreshold = newHTreshold;
    }

    public Double getHTreshold() {
        return hTreshold;
    }

    public void setCTreshold(Double newCTreshold) {
        this.cTreshold = newCTreshold;
    }

    public Double getCTreshold() {
        return cTreshold;
    }

    public void setTresholdSpace(Integer newTresholdSpace) {
        this.tresholdSpace = newTresholdSpace;
    }

    public Integer getTresholdSpace() {
        return tresholdSpace;
    }

    public void setH1Treshold(Double newH1Treshold) {
        this.h1Treshold = newH1Treshold;
    }

    public Double getH1Treshold() {
        return h1Treshold;
    }

    public void setH2Treshold(Double newH2Treshold) {
        this.h2Treshold = newH2Treshold;
    }

    public Double getH2Treshold() {
        return h2Treshold;
    }

    public void setFereastraId(Integer newFereastraId) {
        this.fereastraId = newFereastraId;
    }

    public Integer getFereastraId() {
        return fereastraId;
    }

    public void setFereastra(String newFereastra) {
        this.fereastra = newFereastra;
    }

    public String getFereastra() {
        return fereastra;
    }

}
