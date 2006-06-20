package ro.kds.erp.biz.setum.basic;

import java.rmi.RemoteException;
import javax.ejb.EJBObject;
import javax.ejb.FinderException;
import ro.kds.erp.biz.ResponseBean;

/**
 * Supralumina remote interface.
 *
 * Class was automaticaly generated from a template.
 *
 */
public interface Supralumina extends EJBObject {
        
    public ResponseBean newFormData() throws RemoteException;
    public ResponseBean saveFormData() throws RemoteException;
    public ResponseBean loadFormData(Integer id) throws RemoteException, FinderException;


    public ResponseBean getCurrentFormData() throws RemoteException;
    public ResponseBean getLoadedPrimaryKey() throws RemoteException;

    public ResponseBean updateTip(Integer newTip) throws RemoteException;
    public ResponseBean updateLs(Double newLs) throws RemoteException;
    public ResponseBean updateHs(Double newHs) throws RemoteException;
    public ResponseBean updateCells(Integer newCells) throws RemoteException;
    public ResponseBean updateDeschidere(Integer newDeschidere) throws RemoteException;
    public ResponseBean updateSensDeschidere(Integer newSensDeschidere) throws RemoteException;
    public ResponseBean updatePozitionareBalamale(Integer newPozitionareBalamale) throws RemoteException;
    public ResponseBean updateComponenta(Integer newComponenta) throws RemoteException;
    public ResponseBean updateTipComponenta(Integer newTipComponenta) throws RemoteException;
    public ResponseBean updateTipGeam(Integer newTipGeam) throws RemoteException;
    public ResponseBean updateGeamSimpluId(Integer newGeamSimpluId) throws RemoteException;
    public ResponseBean updateGeamTermopanId(Integer newGeamTermopanId) throws RemoteException;
    public ResponseBean updateTipGrilaj(Integer newTipGrilaj) throws RemoteException;
    public ResponseBean updateGrilajStasId(Integer newGrilajStasId) throws RemoteException;
    public ResponseBean updateValoareGrilajAtipic(java.math.BigDecimal newValoareGrilajAtipic) throws RemoteException;
    public ResponseBean updateTipTabla(Integer newTipTabla) throws RemoteException;
    public ResponseBean updateTablaId(Integer newTablaId) throws RemoteException;
    public ResponseBean updateSellPrice(java.math.BigDecimal newSellPrice) throws RemoteException;
    public ResponseBean updateEntryPrice(java.math.BigDecimal newEntryPrice) throws RemoteException;
    public ResponseBean updatePrice1(java.math.BigDecimal newPrice1) throws RemoteException;
    public ResponseBean updateBusinessCategory(String newBusinessCategory) throws RemoteException;
    public ResponseBean updateQuantity(Integer newQuantity) throws RemoteException;


    public ResponseBean faraGeam (
    ) throws RemoteException;
    public ResponseBean faraGrilaj (
    ) throws RemoteException;
    public ResponseBean faraTabla (
    ) throws RemoteException;
}
