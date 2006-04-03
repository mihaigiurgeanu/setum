package ro.kds.erp.biz.setum.basic;

import java.rmi.RemoteException;
import javax.ejb.EJBObject;
import javax.ejb.FinderException;
import ro.kds.erp.biz.ResponseBean;

/**
 * StandardOffer remote interface.
 *
 * Class was automaticaly generated from a template.
 *
 */
public interface StandardOffer extends EJBObject {
        
    public ResponseBean newFormData() throws RemoteException;
    public ResponseBean saveFormData() throws RemoteException;
    public ResponseBean loadFormData(Integer id) throws RemoteException, FinderException;


    public ResponseBean getCurrentFormData() throws RemoteException;
    public ResponseBean getLoadedPrimaryKey() throws RemoteException;

    public ResponseBean updateNo(String newNo) throws RemoteException;
    public ResponseBean updateDocDate(java.util.Date newDocDate) throws RemoteException;
    public ResponseBean updateDateFrom(java.util.Date newDateFrom) throws RemoteException;
    public ResponseBean updateDateTo(java.util.Date newDateTo) throws RemoteException;
    public ResponseBean updateDiscontinued(Boolean newDiscontinued) throws RemoteException;
    public ResponseBean updatePeriod(Integer newPeriod) throws RemoteException;
    public ResponseBean updateName(String newName) throws RemoteException;
    public ResponseBean updateDescription(String newDescription) throws RemoteException;
    public ResponseBean updateComment(String newComment) throws RemoteException;
    public ResponseBean updateProductId(Integer newProductId) throws RemoteException;
    public ResponseBean updatePrice(java.math.BigDecimal newPrice) throws RemoteException;
    public ResponseBean updateRelativeGain(Double newRelativeGain) throws RemoteException;
    public ResponseBean updateAbsoluteGain(java.math.BigDecimal newAbsoluteGain) throws RemoteException;
    public ResponseBean updateProductCategory(String newProductCategory) throws RemoteException;
    public ResponseBean updateProductCode(String newProductCode) throws RemoteException;
    public ResponseBean updateProductName(String newProductName) throws RemoteException;
    public ResponseBean updateEntryPrice(java.math.BigDecimal newEntryPrice) throws RemoteException;
    public ResponseBean updateSellPrice(java.math.BigDecimal newSellPrice) throws RemoteException;

    public ResponseBean offersListing() throws RemoteException;
    public ResponseBean loadListing() throws RemoteException;
    public ResponseBean lineItemsListing() throws RemoteException;
    public ResponseBean loadSubForm(Integer loadId) throws RemoteException;
    public ResponseBean addNewItem() throws RemoteException;
    public ResponseBean addProduct(Integer productId) throws RemoteException;
    public ResponseBean removeItem() throws RemoteException;
    public ResponseBean saveSubForm() throws RemoteException;
    public java.util.Collection lineItemsCollectionMap() throws RemoteException;
    public ResponseBean makeCurrent() throws RemoteException;
    public ResponseBean discontinue() throws RemoteException;
    public java.util.Map getOfferFieldsMap() throws RemoteException;
}
