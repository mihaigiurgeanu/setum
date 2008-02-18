package ro.kds.erp.biz.setum.basic;

import java.rmi.RemoteException;
import javax.ejb.EJBObject;
import javax.ejb.FinderException;
import ro.kds.erp.biz.ResponseBean;

/**
 * GauriAerisire remote interface.
 *
 * Class was automaticaly generated from a template.
 *
 */
public interface GauriAerisire extends EJBObject {
        
    public ResponseBean newFormData() throws RemoteException;
    public ResponseBean saveFormData() throws RemoteException;
    public ResponseBean loadFormData(Integer id) throws RemoteException, FinderException;

    /**
     * Access to the form data.
     */
     public GauriAerisireForm getForm() throws RemoteException;


    public ResponseBean getCurrentFormData() throws RemoteException;
    public ResponseBean getLoadedPrimaryKey() throws RemoteException;

    public ResponseBean updateDiametru(Double newDiametru) throws RemoteException;
    public ResponseBean updatePas(Double newPas) throws RemoteException;
    public ResponseBean updateNrRanduri(Integer newNrRanduri) throws RemoteException;
    public ResponseBean updatePozitionare1(String newPozitionare1) throws RemoteException;
    public ResponseBean updatePozitionare2(String newPozitionare2) throws RemoteException;
    public ResponseBean updatePozitionare3(String newPozitionare3) throws RemoteException;
    public ResponseBean updateSellPrice(java.math.BigDecimal newSellPrice) throws RemoteException;
    public ResponseBean updateEntryPrice(java.math.BigDecimal newEntryPrice) throws RemoteException;
    public ResponseBean updatePrice1(java.math.BigDecimal newPrice1) throws RemoteException;
    public ResponseBean updateBusinessCategory(String newBusinessCategory) throws RemoteException;
    public ResponseBean updateQuantity(Integer newQuantity) throws RemoteException;
    public ResponseBean updateGroupingCode(String newGroupingCode) throws RemoteException;


}
