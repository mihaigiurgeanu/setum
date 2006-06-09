package ro.kds.erp.biz.setum.basic;

import java.rmi.RemoteException;
import javax.ejb.EJBObject;
import javax.ejb.FinderException;
import ro.kds.erp.biz.ResponseBean;

/**
 * Sistem remote interface.
 *
 * Class was automaticaly generated from a template.
 *
 */
public interface Sistem extends EJBObject {
        
    public ResponseBean newFormData() throws RemoteException;
    public ResponseBean saveFormData() throws RemoteException;
    public ResponseBean loadFormData(Integer id) throws RemoteException, FinderException;


    public ResponseBean getCurrentFormData() throws RemoteException;
    public ResponseBean getLoadedPrimaryKey() throws RemoteException;

    public ResponseBean updateName(String newName) throws RemoteException;
    public ResponseBean updateCode(String newCode) throws RemoteException;
    public ResponseBean updateCategoryId(Integer newCategoryId) throws RemoteException;
    public ResponseBean updateEntryPrice(java.math.BigDecimal newEntryPrice) throws RemoteException;
    public ResponseBean updateSellPrice(java.math.BigDecimal newSellPrice) throws RemoteException;
    public ResponseBean updatePartPrice(java.math.BigDecimal newPartPrice) throws RemoteException;
    public ResponseBean updateLaborPrice(java.math.BigDecimal newLaborPrice) throws RemoteException;
    public ResponseBean updateRelativeGainSP(Double newRelativeGainSP) throws RemoteException;
    public ResponseBean updateAbsoluteGainSP(java.math.BigDecimal newAbsoluteGainSP) throws RemoteException;
    public ResponseBean updateRelativeGainPP(Double newRelativeGainPP) throws RemoteException;
    public ResponseBean updateAbsoluteGainPP(java.math.BigDecimal newAbsoluteGainPP) throws RemoteException;

    public ResponseBean loadListing() throws RemoteException;

}
