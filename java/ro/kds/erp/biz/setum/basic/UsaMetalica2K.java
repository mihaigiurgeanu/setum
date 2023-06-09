package ro.kds.erp.biz.setum.basic;

import java.rmi.RemoteException;
import javax.ejb.EJBObject;
import javax.ejb.FinderException;
import ro.kds.erp.biz.ResponseBean;

/**
 * UsaMetalica2K remote interface.
 *
 * Class was automaticaly generated from a template.
 *
 */
public interface UsaMetalica2K extends EJBObject {
        
    public ResponseBean newFormData() throws RemoteException;
    public ResponseBean saveFormData() throws RemoteException;
    public ResponseBean loadFormData(Integer id) throws RemoteException, FinderException;

    /**
     * Access to the form data.
     */
     public UsaMetalica2KForm getForm() throws RemoteException;


    public ResponseBean getCurrentFormData() throws RemoteException;
    public ResponseBean getLoadedPrimaryKey() throws RemoteException;

    public ResponseBean updateCode(String newCode) throws RemoteException;
    public ResponseBean updateName(String newName) throws RemoteException;
    public ResponseBean updateDescription(String newDescription) throws RemoteException;
    public ResponseBean updateSubclass(String newSubclass) throws RemoteException;
    public ResponseBean updateVersion(String newVersion) throws RemoteException;
    public ResponseBean updateMaterial(Integer newMaterial) throws RemoteException;
    public ResponseBean updateK(Integer newK) throws RemoteException;
    public ResponseBean updateLg(Double newLg) throws RemoteException;
    public ResponseBean updateHg(Double newHg) throws RemoteException;
    public ResponseBean updateLe(Double newLe) throws RemoteException;
    public ResponseBean updateHe(Double newHe) throws RemoteException;
    public ResponseBean updateSe(Double newSe) throws RemoteException;
    public ResponseBean updateLcorrection(Double newLcorrection) throws RemoteException;
    public ResponseBean updateHcorrection(Double newHcorrection) throws RemoteException;
    public ResponseBean updateLCurrent(Double newLCurrent) throws RemoteException;
    public ResponseBean updateLUtil(Double newLUtil) throws RemoteException;
    public ResponseBean updateHUtil(Double newHUtil) throws RemoteException;
    public ResponseBean updateLFoaie(Double newLFoaie) throws RemoteException;
    public ResponseBean updateHFoaie(Double newHFoaie) throws RemoteException;
    public ResponseBean updateLFoaieSec(Double newLFoaieSec) throws RemoteException;
    public ResponseBean updateKType(Integer newKType) throws RemoteException;
    public ResponseBean updateIntFoil(Integer newIntFoil) throws RemoteException;
    public ResponseBean updateIeFoil(Integer newIeFoil) throws RemoteException;
    public ResponseBean updateExtFoil(Integer newExtFoil) throws RemoteException;
    public ResponseBean updateIntFoilSec(Integer newIntFoilSec) throws RemoteException;
    public ResponseBean updateIeFoilSec(Integer newIeFoilSec) throws RemoteException;
    public ResponseBean updateExtFoilSec(Integer newExtFoilSec) throws RemoteException;
    public ResponseBean updateIsolation(Integer newIsolation) throws RemoteException;
    public ResponseBean updateOpeningDir(Integer newOpeningDir) throws RemoteException;
    public ResponseBean updateOpeningSide(Integer newOpeningSide) throws RemoteException;
    public ResponseBean updateFrameType(Integer newFrameType) throws RemoteException;
    public ResponseBean updateLFrame(Double newLFrame) throws RemoteException;
    public ResponseBean updateBFrame(Double newBFrame) throws RemoteException;
    public ResponseBean updateCFrame(Double newCFrame) throws RemoteException;
    public ResponseBean updateLdesfToc(Double newLdesfToc) throws RemoteException;
    public ResponseBean updateFoilPosition(Integer newFoilPosition) throws RemoteException;
    public ResponseBean updateTresholdType(Integer newTresholdType) throws RemoteException;
    public ResponseBean updateLTreshold(Double newLTreshold) throws RemoteException;
    public ResponseBean updateHTreshold(Double newHTreshold) throws RemoteException;
    public ResponseBean updateCTreshold(Double newCTreshold) throws RemoteException;
    public ResponseBean updateTresholdSpace(Integer newTresholdSpace) throws RemoteException;
    public ResponseBean updateH1Treshold(Double newH1Treshold) throws RemoteException;
    public ResponseBean updateH2Treshold(Double newH2Treshold) throws RemoteException;
    public ResponseBean updateLdesfPrag(Double newLdesfPrag) throws RemoteException;
    public ResponseBean updateMasca(Integer newMasca) throws RemoteException;
    public ResponseBean updateLacrimar(Integer newLacrimar) throws RemoteException;
    public ResponseBean updateBolturi(Integer newBolturi) throws RemoteException;
    public ResponseBean updatePlatbanda(Integer newPlatbanda) throws RemoteException;
    public ResponseBean updateEntryPrice(java.math.BigDecimal newEntryPrice) throws RemoteException;
    public ResponseBean updateSellPrice(java.math.BigDecimal newSellPrice) throws RemoteException;
    public ResponseBean updateMontareSistem(Integer newMontareSistem) throws RemoteException;
    public ResponseBean updateDecupareSistemId(Integer newDecupareSistemId) throws RemoteException;
    public ResponseBean updateSistemSetumSauBeneficiar(Integer newSistemSetumSauBeneficiar) throws RemoteException;
    public ResponseBean updateBroascaId(Integer newBroascaId) throws RemoteException;
    public ResponseBean updateBroascaBuc(Integer newBroascaBuc) throws RemoteException;
    public ResponseBean updateCilindruId(Integer newCilindruId) throws RemoteException;
    public ResponseBean updateCilindruBuc(Integer newCilindruBuc) throws RemoteException;
    public ResponseBean updateCopiatCheieId(Integer newCopiatCheieId) throws RemoteException;
    public ResponseBean updateCopiatCheieBuc(Integer newCopiatCheieBuc) throws RemoteException;
    public ResponseBean updateVizorId(Integer newVizorId) throws RemoteException;
    public ResponseBean updateVizorBuc(Integer newVizorBuc) throws RemoteException;
    public ResponseBean updateSildId(Integer newSildId) throws RemoteException;
    public ResponseBean updateSildTip(String newSildTip) throws RemoteException;
    public ResponseBean updateSildCuloare(String newSildCuloare) throws RemoteException;
    public ResponseBean updateSildBuc(Integer newSildBuc) throws RemoteException;
    public ResponseBean updateRozetaId(Integer newRozetaId) throws RemoteException;
    public ResponseBean updateRozetaTip(String newRozetaTip) throws RemoteException;
    public ResponseBean updateRozetaCuloare(String newRozetaCuloare) throws RemoteException;
    public ResponseBean updateRozetaBuc(Integer newRozetaBuc) throws RemoteException;
    public ResponseBean updateManerId(Integer newManerId) throws RemoteException;
    public ResponseBean updateManerTip(String newManerTip) throws RemoteException;
    public ResponseBean updateManerCuloare(String newManerCuloare) throws RemoteException;
    public ResponseBean updateManerBuc(Integer newManerBuc) throws RemoteException;
    public ResponseBean updateYalla1Id(Integer newYalla1Id) throws RemoteException;
    public ResponseBean updateYalla1Buc(Integer newYalla1Buc) throws RemoteException;
    public ResponseBean updateYalla2Id(Integer newYalla2Id) throws RemoteException;
    public ResponseBean updateYalla2Buc(Integer newYalla2Buc) throws RemoteException;
    public ResponseBean updateBaraAntipanicaId(Integer newBaraAntipanicaId) throws RemoteException;
    public ResponseBean updateBaraAntipanicaBuc(Integer newBaraAntipanicaBuc) throws RemoteException;
    public ResponseBean updateManerSemicilindruId(Integer newManerSemicilindruId) throws RemoteException;
    public ResponseBean updateManerSemicilindruBuc(Integer newManerSemicilindruBuc) throws RemoteException;
    public ResponseBean updateSelectorOrdineId(Integer newSelectorOrdineId) throws RemoteException;
    public ResponseBean updateSelectorOrdineBuc(Integer newSelectorOrdineBuc) throws RemoteException;
    public ResponseBean updateAmortizorId(Integer newAmortizorId) throws RemoteException;
    public ResponseBean updateAmortizorBuc(Integer newAmortizorBuc) throws RemoteException;
    public ResponseBean updateAlteSisteme1Id(Integer newAlteSisteme1Id) throws RemoteException;
    public ResponseBean updateAlteSisteme1Buc(Integer newAlteSisteme1Buc) throws RemoteException;
    public ResponseBean updateAlteSisteme2Id(Integer newAlteSisteme2Id) throws RemoteException;
    public ResponseBean updateAlteSisteme2Buc(Integer newAlteSisteme2Buc) throws RemoteException;
    public ResponseBean updateBenefBroasca(String newBenefBroasca) throws RemoteException;
    public ResponseBean updateBenefBroascaBuc(Integer newBenefBroascaBuc) throws RemoteException;
    public ResponseBean updateBenefBroascaTip(Integer newBenefBroascaTip) throws RemoteException;
    public ResponseBean updateBenefCilindru(String newBenefCilindru) throws RemoteException;
    public ResponseBean updateBenefCilindruBuc(Integer newBenefCilindruBuc) throws RemoteException;
    public ResponseBean updateBenefCilindruTip(Integer newBenefCilindruTip) throws RemoteException;
    public ResponseBean updateBenefSild(String newBenefSild) throws RemoteException;
    public ResponseBean updateBenefSildBuc(Integer newBenefSildBuc) throws RemoteException;
    public ResponseBean updateBenefSildTip(Integer newBenefSildTip) throws RemoteException;
    public ResponseBean updateBenefYalla(String newBenefYalla) throws RemoteException;
    public ResponseBean updateBenefYallaBuc(Integer newBenefYallaBuc) throws RemoteException;
    public ResponseBean updateBenefYallaTip(Integer newBenefYallaTip) throws RemoteException;
    public ResponseBean updateBenefVizor(String newBenefVizor) throws RemoteException;
    public ResponseBean updateBenefVizorBuc(Integer newBenefVizorBuc) throws RemoteException;
    public ResponseBean updateBenefVizorTip(Integer newBenefVizorTip) throws RemoteException;
    public ResponseBean updateBenefBaraAntipanica(String newBenefBaraAntipanica) throws RemoteException;
    public ResponseBean updateBenefBaraAntipanicaBuc(Integer newBenefBaraAntipanicaBuc) throws RemoteException;
    public ResponseBean updateBenefBaraAntipanicaTip(Integer newBenefBaraAntipanicaTip) throws RemoteException;
    public ResponseBean updateBenefManer(String newBenefManer) throws RemoteException;
    public ResponseBean updateBenefManerBuc(Integer newBenefManerBuc) throws RemoteException;
    public ResponseBean updateBenefManerTip(Integer newBenefManerTip) throws RemoteException;
    public ResponseBean updateBenefSelectorOrdine(String newBenefSelectorOrdine) throws RemoteException;
    public ResponseBean updateBenefSelectorOrdineBuc(Integer newBenefSelectorOrdineBuc) throws RemoteException;
    public ResponseBean updateBenefSelectorOrdineTip(Integer newBenefSelectorOrdineTip) throws RemoteException;
    public ResponseBean updateBenefAmortizor(String newBenefAmortizor) throws RemoteException;
    public ResponseBean updateBenefAmortizorBuc(Integer newBenefAmortizorBuc) throws RemoteException;
    public ResponseBean updateBenefAmortizorTip(Integer newBenefAmortizorTip) throws RemoteException;
    public ResponseBean updateBenefAlteSisteme1(String newBenefAlteSisteme1) throws RemoteException;
    public ResponseBean updateBenefAlteSisteme1Buc(Integer newBenefAlteSisteme1Buc) throws RemoteException;
    public ResponseBean updateBenefAlteSisteme1Tip(Integer newBenefAlteSisteme1Tip) throws RemoteException;
    public ResponseBean updateBenefAlteSisteme2(String newBenefAlteSisteme2) throws RemoteException;
    public ResponseBean updateBenefAlteSisteme2Buc(Integer newBenefAlteSisteme2Buc) throws RemoteException;
    public ResponseBean updateBenefAlteSisteme2Tip(Integer newBenefAlteSisteme2Tip) throws RemoteException;
    public ResponseBean updateSistemeComment(String newSistemeComment) throws RemoteException;
    public ResponseBean updateIntFinisajBlat(String newIntFinisajBlat) throws RemoteException;
    public ResponseBean updateIntFinisajBlatId(Integer newIntFinisajBlatId) throws RemoteException;
    public ResponseBean updateIntFinisajToc(String newIntFinisajToc) throws RemoteException;
    public ResponseBean updateIntFinisajTocId(Integer newIntFinisajTocId) throws RemoteException;
    public ResponseBean updateIntFinisajGrilaj(String newIntFinisajGrilaj) throws RemoteException;
    public ResponseBean updateIntFinisajGrilajId(Integer newIntFinisajGrilajId) throws RemoteException;
    public ResponseBean updateIntFinisajFereastra(String newIntFinisajFereastra) throws RemoteException;
    public ResponseBean updateIntFinisajFereastraId(Integer newIntFinisajFereastraId) throws RemoteException;
    public ResponseBean updateIntFinisajSupralumina(String newIntFinisajSupralumina) throws RemoteException;
    public ResponseBean updateIntFinisajSupraluminaId(Integer newIntFinisajSupraluminaId) throws RemoteException;
    public ResponseBean updateIntFinisajPanouLateral(String newIntFinisajPanouLateral) throws RemoteException;
    public ResponseBean updateIntFinisajPanouLateralId(Integer newIntFinisajPanouLateralId) throws RemoteException;
    public ResponseBean updateExtFinisajBlat(String newExtFinisajBlat) throws RemoteException;
    public ResponseBean updateExtFinisajBlatId(Integer newExtFinisajBlatId) throws RemoteException;
    public ResponseBean updateExtFinisajToc(String newExtFinisajToc) throws RemoteException;
    public ResponseBean updateExtFinisajTocId(Integer newExtFinisajTocId) throws RemoteException;
    public ResponseBean updateExtFinisajGrilaj(String newExtFinisajGrilaj) throws RemoteException;
    public ResponseBean updateExtFinisajGrilajId(Integer newExtFinisajGrilajId) throws RemoteException;
    public ResponseBean updateExtFinisajFereastra(String newExtFinisajFereastra) throws RemoteException;
    public ResponseBean updateExtFinisajFereastraId(Integer newExtFinisajFereastraId) throws RemoteException;
    public ResponseBean updateExtFinisajSupralumina(String newExtFinisajSupralumina) throws RemoteException;
    public ResponseBean updateExtFinisajSupraluminaId(Integer newExtFinisajSupraluminaId) throws RemoteException;
    public ResponseBean updateExtFinisajPanouLateral(String newExtFinisajPanouLateral) throws RemoteException;
    public ResponseBean updateExtFinisajPanouLateralId(Integer newExtFinisajPanouLateralId) throws RemoteException;
    public ResponseBean updateFinisajTocBlat(Boolean newFinisajTocBlat) throws RemoteException;
    public ResponseBean updateFinisajGrilajBlat(Boolean newFinisajGrilajBlat) throws RemoteException;
    public ResponseBean updateFinisajFereastraBlat(Boolean newFinisajFereastraBlat) throws RemoteException;
    public ResponseBean updateFinisajSupraluminaBlat(Boolean newFinisajSupraluminaBlat) throws RemoteException;
    public ResponseBean updateFinisajPanouLateralBlat(Boolean newFinisajPanouLateralBlat) throws RemoteException;
    public ResponseBean updateFinisajBlatExtInt(Boolean newFinisajBlatExtInt) throws RemoteException;
    public ResponseBean updateFinisajTocExtInt(Boolean newFinisajTocExtInt) throws RemoteException;
    public ResponseBean updateFinisajGrilajExtInt(Boolean newFinisajGrilajExtInt) throws RemoteException;
    public ResponseBean updateFinisajFereastraExtInt(Boolean newFinisajFereastraExtInt) throws RemoteException;
    public ResponseBean updateFinisajSupraluminaExtInt(Boolean newFinisajSupraluminaExtInt) throws RemoteException;
    public ResponseBean updateFinisajPanouLateralExtInt(Boolean newFinisajPanouLateralExtInt) throws RemoteException;
    public ResponseBean updateSuprafataBlat(Double newSuprafataBlat) throws RemoteException;
    public ResponseBean updateSuprafataToc(Double newSuprafataToc) throws RemoteException;
    public ResponseBean updateSuprafataGrilaj(Double newSuprafataGrilaj) throws RemoteException;
    public ResponseBean updateSuprafataFereastra(Double newSuprafataFereastra) throws RemoteException;
    public ResponseBean updateSuprafataSupralumina(Double newSuprafataSupralumina) throws RemoteException;
    public ResponseBean updateSuprafataPanouLateral(Double newSuprafataPanouLateral) throws RemoteException;
    public ResponseBean updateSuprafataGrilaVentilatie(Double newSuprafataGrilaVentilatie) throws RemoteException;
    public ResponseBean updateLexec(Double newLexec) throws RemoteException;
    public ResponseBean updateHexec(Double newHexec) throws RemoteException;
    public ResponseBean updateGroupingCode(String newGroupingCode) throws RemoteException;


    public ResponseBean addOption (
        Integer optionId,
        String businessCategory
    ) throws RemoteException;
    public ResponseBean getOptionsListing (
    ) throws RemoteException;
    public ResponseBean removeOption (
        Integer optionId
    ) throws RemoteException;
    public ResponseBean computePrice (
    ) throws RemoteException;
}
