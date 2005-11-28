package ro.kds.erp.biz.setum.basic;

import java.rmi.RemoteException;
import javax.ejb.EJBObject;
import javax.ejb.FinderException;
import ro.kds.erp.biz.ResponseBean;

/**
 * UsaMetalica1K remote interface.
 *
 * Class was automaticaly generated from a template.
 *
 */
public interface UsaMetalica1K extends EJBObject {
        
    public ResponseBean newFormData() throws RemoteException;
    public ResponseBean saveFormData() throws RemoteException;
    public ResponseBean loadFormData(Integer id) throws RemoteException, FinderException;
    public ResponseBean getCurrentFormData() throws RemoteException;
    public ResponseBean getLoadedPrimaryKey() throws RemoteException;

    public ResponseBean updateSubclass(String newSubclass) throws RemoteException;
    public ResponseBean updateVersion(String newVersion) throws RemoteException;
    public ResponseBean updateMaterial(Integer newMaterial) throws RemoteException;
    public ResponseBean updateLg(Double newLg) throws RemoteException;
    public ResponseBean updateHg(Double newHg) throws RemoteException;
    public ResponseBean updateLe(Double newLe) throws RemoteException;
    public ResponseBean updateHe(Double newHe) throws RemoteException;
    public ResponseBean updateLcorrection(Double newLcorrection) throws RemoteException;
    public ResponseBean updateHcorrection(Double newHcorrection) throws RemoteException;
    public ResponseBean updateIntFoil(Integer newIntFoil) throws RemoteException;
    public ResponseBean updateIeFoil(Integer newIeFoil) throws RemoteException;
    public ResponseBean updateExtFoil(Integer newExtFoil) throws RemoteException;
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

}
