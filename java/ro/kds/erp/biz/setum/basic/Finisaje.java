package ro.kds.erp.biz.setum.basic;

import java.rmi.RemoteException;
import javax.ejb.EJBObject;
import javax.ejb.FinderException;
import ro.kds.erp.biz.ResponseBean;

/**
 * Finisaje remote interface.
 *
 * Class was automaticaly generated from a template.
 *
 */
public interface Finisaje extends EJBObject {
        
    public ResponseBean newFormData() throws RemoteException;
    public ResponseBean saveFormData() throws RemoteException;
    public ResponseBean loadFormData(Integer id) throws RemoteException, FinderException;


    public ResponseBean getCurrentFormData() throws RemoteException;
    public ResponseBean getLoadedPrimaryKey() throws RemoteException;

    public ResponseBean updateZincare(Integer newZincare) throws RemoteException;
    public ResponseBean updateCapitonare(Integer newCapitonare) throws RemoteException;
    public ResponseBean updatePlacare(Integer newPlacare) throws RemoteException;
    public ResponseBean updateGrundId(Integer newGrundId) throws RemoteException;
    public ResponseBean updateVopsireTip(Integer newVopsireTip) throws RemoteException;
    public ResponseBean updateRalStasId(Integer newRalStasId) throws RemoteException;
    public ResponseBean updateRalOrder(String newRalOrder) throws RemoteException;
    public ResponseBean updateRalOrderValue(java.math.BigDecimal newRalOrderValue) throws RemoteException;
    public ResponseBean updateCode(String newCode) throws RemoteException;
    public ResponseBean updateName(String newName) throws RemoteException;
    public ResponseBean updateDescription(String newDescription) throws RemoteException;
    public ResponseBean updateSellPrice(java.math.BigDecimal newSellPrice) throws RemoteException;
    public ResponseBean updateEntryPrice(java.math.BigDecimal newEntryPrice) throws RemoteException;
    public ResponseBean updatePrice1(java.math.BigDecimal newPrice1) throws RemoteException;


}
