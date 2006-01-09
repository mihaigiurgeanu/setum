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
    public ResponseBean getCurrentFormData() throws RemoteException;
    public ResponseBean getLoadedPrimaryKey() throws RemoteException;

    public ResponseBean updateCode(String newCode) throws RemoteException;
    public ResponseBean updateName(String newName) throws RemoteException;
    public ResponseBean updateDescription(String newDescription) throws RemoteException;
    public ResponseBean updateSubclass(String newSubclass) throws RemoteException;
    public ResponseBean updateVersion(String newVersion) throws RemoteException;
    public ResponseBean updateMaterial(Integer newMaterial) throws RemoteException;
    public ResponseBean updateLg(Double newLg) throws RemoteException;
    public ResponseBean updateHg(Double newHg) throws RemoteException;
    public ResponseBean updateLe(Double newLe) throws RemoteException;
    public ResponseBean updateHe(Double newHe) throws RemoteException;
    public ResponseBean updateLcorrection(Double newLcorrection) throws RemoteException;
    public ResponseBean updateHcorrection(Double newHcorrection) throws RemoteException;
    public ResponseBean updateLCurrent(Double newLCurrent) throws RemoteException;
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
    public ResponseBean updateFoilPosition(Integer newFoilPosition) throws RemoteException;
    public ResponseBean updateTresholdType(Integer newTresholdType) throws RemoteException;
    public ResponseBean updateLTreshold(Double newLTreshold) throws RemoteException;
    public ResponseBean updateHTreshold(Double newHTreshold) throws RemoteException;
    public ResponseBean updateCTreshold(Double newCTreshold) throws RemoteException;
    public ResponseBean updateTresholdSpace(Integer newTresholdSpace) throws RemoteException;
    public ResponseBean updateH1Treshold(Double newH1Treshold) throws RemoteException;
    public ResponseBean updateH2Treshold(Double newH2Treshold) throws RemoteException;
    public ResponseBean updateFereastraId(Integer newFereastraId) throws RemoteException;
    public ResponseBean updateFereastra(String newFereastra) throws RemoteException;
    public ResponseBean updateGrilaVentilatieId(Integer newGrilaVentilatieId) throws RemoteException;
    public ResponseBean updateGrilaVentilatie(String newGrilaVentilatie) throws RemoteException;
    public ResponseBean updateGauriAerisireId(Integer newGauriAerisireId) throws RemoteException;
    public ResponseBean updateGauriAerisire(String newGauriAerisire) throws RemoteException;
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
    public ResponseBean updateSistemeComment(String newSistemeComment) throws RemoteException;

}
