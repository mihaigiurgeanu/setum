package ro.kds.erp.biz.setum;

import java.io.Serializable;

/**
 * A represantation of form data. While user is editing the data associated
 * with this object, the data is maintained temporary into this bean. At
 * the end, when <code>save</code> operation is called, the data are
 * saved in the persistance layer.
 *
 *
 * Created: Tue Sep 27 21:53:01 2005
 *
 * @author <a href="mailto:Mihai Giurgeanu@CRIMIRA"></a>
 * @version 1.0
 */
public class UsaMetalicaBean implements Serializable {

    /**
     * Describe subclass here.
     */
    private String subclass;

    /**
     * Describe version here.
     */
    private String version;

    /**
     * Describe material here.
     */
    private int material;

    /**
     * Describe lg here.
     */
    private double lg;

    /**
     * Describe hg here.
     */
    private double hg;

    /**
     * Describe le here.
     */
    private double le;

    /**
     * Describe he here.
     */
    private double he;

    /**
     * Describe lcorrection here.
     */
    private double lcorrection;

    /**
     * Describe hcorrection here.
     */
    private double hcorrection;

    /**
     * Describe intFoil here.
     */
    private int intFoil;

    /**
     * Describe extFoil here.
     */
    private int extFoil;

    /**
     * Describe ieFoil here.
     */
    private int ieFoil;

    /**
     * Describe isolation here.
     */
    private int isolation;

    /**
     * Describe openingDir here.
     */
    private int openingDir;

    /**
     * Describe openingSide here.
     */
    private int openingSide;

    /**
     * Describe frameType here.
     */
    private int frameType;

    /**
     * Describe lFrame here.
     */
    private double lFrame;

    /**
     * Describe bFrame here.
     */
    private double bFrame;

    /**
     * Describe cFrame here.
     */
    private double cFrame;

    /**
     * Describe foilPosition here.
     */
    private int foilPosition;

    /**
     * Describe tresholdType here.
     */
    private int tresholdType;

    /**
     * Describe lTreshold here.
     */
    private double lTreshold;

    /**
     * Describe hTreshold here.
     */
    private double hTreshold;

    /**
     * Describe cTreshold here.
     */
    private double cTreshold;

    /**
     * Describe tresholdSpace here.
     */
    private int tresholdSpace;

    /**
     * Describe h1Treshold here.
     */
    private double h1Treshold;

    /**
     * Describe h2Treshold here.
     */
    private double h2Treshold;

    /**
     * Creates a new <code>UsaMetalicaBean</code> instance.
     *
     */
    public UsaMetalicaBean() {
	// set default values
	setSubclass("A");
	setVersion("UF");
	setMaterial(1);
	setLg(0);
	setHg(0);
	setLcorrection(0);
	setHcorrection(0);
	setIeFoil(1);
	setIntFoil(1);
	setExtFoil(1);
	setIsolation(1);
	setOpeningDir(1);
	setOpeningSide(1);
	setFrameType(1);
	setLFrame(0);
	setBFrame(0);
	setCFrame(0);
	setFoilPosition(1);
	setTresholdType(1);
	setLTreshold(0);
	setHTreshold(0);
	setCTreshold(0);
	setTresholdSpace(1);
	setH1Treshold(0);
	setH2Treshold(0);
    }

    /**
     * Get the <code>Subclass</code> value.
     *
     * @return a <code>String</code> value
     */
    public final String getSubclass() {
	return subclass;
    }

    /**
     * Set the <code>Subclass</code> value.
     *
     * @param newSubclass The new Subclass value.
     */
    public final void setSubclass(final String newSubclass) {
	this.subclass = newSubclass;
    }

    /**
     * Get the <code>Version</code> value.
     *
     * @return a <code>String</code> value
     */
    public final String getVersion() {
	return version;
    }

    /**
     * Set the <code>Version</code> value.
     *
     * @param newVersion The new Version value.
     */
    public final void setVersion(final String newVersion) {
	this.version = newVersion;
    }

    /**
     * Get the <code>Material</code> value.
     *
     * @return an <code>int</code> value
     */
    public final int getMaterial() {
	return material;
    }

    /**
     * Set the <code>Material</code> value.
     *
     * @param newMaterial The new Material value.
     */
    public final void setMaterial(final int newMaterial) {
	this.material = newMaterial;
    }

    /**
     * Get the <code>Lg</code> value.
     *
     * @return a <code>double</code> value
     */
    public final double getLg() {
	return lg;
    }

    /**
     * Set the <code>Lg</code> value.
     *
     * @param newLg The new Lg value.
     */
    public final void setLg(final double newLg) {
	this.lg = newLg;
    }

    /**
     * Get the <code>Hg</code> value.
     *
     * @return a <code>double</code> value
     */
    public final double getHg() {
	return hg;
    }

    /**
     * Set the <code>Hg</code> value.
     *
     * @param newHg The new Hg value.
     */
    public final void setHg(final double newHg) {
	this.hg = newHg;
    }

    /**
     * Get the <code>Le</code> value.
     *
     * @return a <code>double</code> value
     */
    public final double getLe() {
	return le;
    }

    /**
     * Set the <code>Le</code> value.
     *
     * @param newLg The new Le value.
     */
    public final void setLe(final double newLe) {
	this.le = newLe;
    }

    /**
     * Get the <code>He</code> value.
     *
     * @return a <code>double</code> value
     */
    public final double getHe() {
	return he;
    }

    /**
     * Set the <code>He</code> value.
     *
     * @param newHg The new He value.
     */
    public final void setHe(final double newHe) {
	this.he = newHe;
    }

    /**
     * Get the <code>Lcorrection</code> value.
     *
     * @return a <code>double</code> value
     */
    public final double getLcorrection() {
	return lcorrection;
    }

    /**
     * Set the <code>Lcorrection</code> value.
     *
     * @param newLcorrection The new Lcorrection value.
     */
    public final void setLcorrection(final double newLcorrection) {
	this.lcorrection = newLcorrection;
    }

    /**
     * Get the <code>Hcorrection</code> value.
     *
     * @return a <code>double</code> value
     */
    public final double getHcorrection() {
	return hcorrection;
    }

    /**
     * Set the <code>Hcorrection</code> value.
     *
     * @param newHcorrection The new Hcorrection value.
     */
    public final void setHcorrection(final double newHcorrection) {
	this.hcorrection = newHcorrection;
    }

    /**
     * Get the <code>IntFoil</code> value.
     *
     * @return an <code>int</code> value
     */
    public final int getIntFoil() {
	return intFoil;
    }

    /**
     * Set the <code>IntFoil</code> value.
     *
     * @param newIntFoil The new IntFoil value.
     */
    public final void setIntFoil(final int newIntFoil) {
	this.intFoil = newIntFoil;
    }

    /**
     * Get the <code>ExtFoil</code> value.
     *
     * @return an <code>int</code> value
     */
    public final int getExtFoil() {
	return extFoil;
    }

    /**
     * Set the <code>ExtFoil</code> value.
     *
     * @param newExtFoil The new ExtFoil value.
     */
    public final void setExtFoil(final int newExtFoil) {
	this.extFoil = newExtFoil;
    }

    /**
     * Get the <code>IeFoil</code> value.
     *
     * @return an <code>int</code> value
     */
    public final int getIeFoil() {
	return ieFoil;
    }

    /**
     * Set the <code>IeFoil</code> value.
     *
     * @param newIeFoil The new IeFoil value.
     */
    public final void setIeFoil(final int newIeFoil) {
	this.ieFoil = newIeFoil;
    }

    /**
     * Get the <code>Isolation</code> value.
     *
     * @return an <code>int</code> value
     */
    public final int getIsolation() {
	return isolation;
    }

    /**
     * Set the <code>Isolation</code> value.
     *
     * @param newIsolation The new Isolation value.
     */
    public final void setIsolation(final int newIsolation) {
	this.isolation = newIsolation;
    }

    /**
     * Get the <code>OpeningDir</code> value.
     *
     * @return an <code>int</code> value
     */
    public final int getOpeningDir() {
	return openingDir;
    }

    /**
     * Set the <code>OpeningDir</code> value.
     *
     * @param newOpeningDir The new OpeningDir value.
     */
    public final void setOpeningDir(final int newOpeningDir) {
	this.openingDir = newOpeningDir;
    }

    /**
     * Get the <code>OpeningSide</code> value.
     *
     * @return an <code>int</code> value
     */
    public final int getOpeningSide() {
	return openingSide;
    }

    /**
     * Set the <code>OpeningSide</code> value.
     *
     * @param newOpeningSide The new OpeningSide value.
     */
    public final void setOpeningSide(final int newOpeningSide) {
	this.openingSide = newOpeningSide;
    }

    /**
     * Get the <code>FrameType</code> value.
     *
     * @return an <code>int</code> value
     */
    public final int getFrameType() {
	return frameType;
    }

    /**
     * Set the <code>FrameType</code> value.
     *
     * @param newFrameType The new FrameType value.
     */
    public final void setFrameType(final int newFrameType) {
	this.frameType = newFrameType;
    }

    /**
     * Get the <code>LFrame</code> value.
     *
     * @return a <code>double</code> value
     */
    public final double getLFrame() {
	return lFrame;
    }

    /**
     * Set the <code>LFrame</code> value.
     *
     * @param newLFrame The new LFrame value.
     */
    public final void setLFrame(final double newLFrame) {
	this.lFrame = newLFrame;
    }

    /**
     * Get the <code>BFrame</code> value.
     *
     * @return a <code>double</code> value
     */
    public final double getBFrame() {
	return bFrame;
    }

    /**
     * Set the <code>BFrame</code> value.
     *
     * @param newBFrame The new BFrame value.
     */
    public final void setBFrame(final double newBFrame) {
	this.bFrame = newBFrame;
    }

    /**
     * Get the <code>CFrame</code> value.
     *
     * @return a <code>double</code> value
     */
    public final double getCFrame() {
	return cFrame;
    }

    /**
     * Set the <code>CFrame</code> value.
     *
     * @param newCFrame The new CFrame value.
     */
    public final void setCFrame(final double newCFrame) {
	this.cFrame = newCFrame;
    }

    /**
     * Get the <code>FoilPosition</code> value.
     *
     * @return an <code>int</code> value
     */
    public final int getFoilPosition() {
	return foilPosition;
    }

    /**
     * Set the <code>FoilPosition</code> value.
     *
     * @param newFoilPosition The new FoilPosition value.
     */
    public final void setFoilPosition(final int newFoilPosition) {
	this.foilPosition = newFoilPosition;
    }

    /**
     * Get the <code>TresholdType</code> value.
     *
     * @return an <code>int</code> value
     */
    public final int getTresholdType() {
	return tresholdType;
    }

    /**
     * Set the <code>TresholdType</code> value.
     *
     * @param newTresholdType The new TresholdType value.
     */
    public final void setTresholdType(final int newTresholdType) {
	this.tresholdType = newTresholdType;
    }

    /**
     * Get the <code>LTreshold</code> value.
     *
     * @return a <code>double</code> value
     */
    public final double getLTreshold() {
	return lTreshold;
    }

    /**
     * Set the <code>LTreshold</code> value.
     *
     * @param newLTreshold The new LTreshold value.
     */
    public final void setLTreshold(final double newLTreshold) {
	this.lTreshold = newLTreshold;
    }

    /**
     * Get the <code>HTreshold</code> value.
     *
     * @return a <code>double</code> value
     */
    public final double getHTreshold() {
	return hTreshold;
    }

    /**
     * Set the <code>HTreshold</code> value.
     *
     * @param newHTreshold The new HTreshold value.
     */
    public final void setHTreshold(final double newHTreshold) {
	this.hTreshold = newHTreshold;
    }

    /**
     * Get the <code>CTreshold</code> value.
     *
     * @return a <code>double</code> value
     */
    public final double getCTreshold() {
	return cTreshold;
    }

    /**
     * Set the <code>CTreshold</code> value.
     *
     * @param newCTreshold The new CTreshold value.
     */
    public final void setCTreshold(final double newCTreshold) {
	this.cTreshold = newCTreshold;
    }

    /**
     * Get the <code>TresholdSpace</code> value.
     *
     * @return an <code>int</code> value
     */
    public final int getTresholdSpace() {
	return tresholdSpace;
    }

    /**
     * Set the <code>TresholdSpace</code> value.
     *
     * @param newTresholdSpace The new TresholdSpace value.
     */
    public final void setTresholdSpace(final int newTresholdSpace) {
	this.tresholdSpace = newTresholdSpace;
    }

    /**
     * Get the <code>H1Treshold</code> value.
     *
     * @return a <code>double</code> value
     */
    public final double getH1Treshold() {
	return h1Treshold;
    }

    /**
     * Set the <code>H1Treshold</code> value.
     *
     * @param newH1Treshold The new H1Treshold value.
     */
    public final void setH1Treshold(final double newH1Treshold) {
	this.h1Treshold = newH1Treshold;
    }

    /**
     * Get the <code>H2Treshold</code> value.
     *
     * @return a <code>double</code> value
     */
    public final double getH2Treshold() {
	return h2Treshold;
    }

    /**
     * Set the <code>H2Treshold</code> value.
     *
     * @param newH2Treshold The new H2Treshold value.
     */
    public final void setH2Treshold(final double newH2Treshold) {
	this.h2Treshold = newH2Treshold;
    }
}
