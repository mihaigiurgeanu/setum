// UsaMetalica1K.java

package ro.kds.erp.biz.setum;

import java.rmi.RemoteException;
import javax.ejb.EJBObject;
import javax.ejb.FinderException;
import ro.kds.erp.biz.ResponseBean;

/**
 * UsaMetalica1K remote interface
 */
public interface UsaMetalica1K extends EJBObject {
    public ResponseBean newProduct() throws RemoteException;

    public ResponseBean updateSubclass(String s) throws RemoteException;
    public ResponseBean updateVersion(String s) throws RemoteException;
    public ResponseBean updateMaterial(Integer matCode) throws RemoteException;
    public ResponseBean updateLg(Double lg) throws RemoteException;
    public ResponseBean updateHg(Double hg) throws RemoteException;
    public ResponseBean updateLcorrection(Double lcorrection) throws RemoteException;
    public ResponseBean updateHcorrection(Double hcorrection) throws RemoteException;
    public ResponseBean updateIntFoil(Integer intFoil) throws RemoteException;
    public ResponseBean updateIeFoil(Integer ieFoil) throws RemoteException;
    public ResponseBean updateExtFoil(Integer extFoil) throws RemoteException;
    public ResponseBean updateIsolation(Integer isolation) throws RemoteException;
    public ResponseBean updateOpeningDir(Integer od) throws RemoteException;
    public ResponseBean updateOpeningSide(Integer os) throws RemoteException;
    public ResponseBean updateFrameType(Integer ft) throws RemoteException;
    public ResponseBean updateLFrame(Double lFrame) throws RemoteException;
    public ResponseBean updateBFrame(Double bFrame) throws RemoteException;
    public ResponseBean updateCFrame(Double cFrame) throws RemoteException;
    public ResponseBean updateFoilPosition(Integer fp) throws RemoteException;
    public ResponseBean updateTresholdType(Integer tt) throws RemoteException;
    public ResponseBean updateLTreshold(Double lt) throws RemoteException;
    public ResponseBean updateHTreshold(Double ht) throws RemoteException;
    public ResponseBean updateCTreshold(Double ct) throws RemoteException;
    public ResponseBean updateTresholdSpace(Integer ts) throws RemoteException;
    public ResponseBean updateH1Treshold(Double h1) throws RemoteException;
    public ResponseBean updateH2Treshold(Double h2) throws RemoteException;

    public ResponseBean saveProduct() throws RemoteException;
    public ResponseBean loadProduct(Integer id) throws RemoteException, FinderException;
}
