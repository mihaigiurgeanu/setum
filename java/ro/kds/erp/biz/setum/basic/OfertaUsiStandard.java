package ro.kds.erp.biz.setum.basic;

import java.rmi.RemoteException;
import javax.ejb.EJBObject;
import javax.ejb.FinderException;
import ro.kds.erp.biz.ResponseBean;

/**
 * OfertaUsiStandard remote interface.
 *
 * Class was automaticaly generated from a template.
 *
 */
public interface OfertaUsiStandard extends EJBObject {
        
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
    public ResponseBean updatePrice(java.math.BigDecimal newPrice) throws RemoteException;
    public ResponseBean updateVatPrice(java.math.BigDecimal newVatPrice) throws RemoteException;
    public ResponseBean updateRelativeGain(Double newRelativeGain) throws RemoteException;
    public ResponseBean updateAbsoluteGain(java.math.BigDecimal newAbsoluteGain) throws RemoteException;
    public ResponseBean updateProductCategory(String newProductCategory) throws RemoteException;
    public ResponseBean updateProductCode(String newProductCode) throws RemoteException;
    public ResponseBean updateProductName(String newProductName) throws RemoteException;
    public ResponseBean updateUsa(String newUsa) throws RemoteException;
    public ResponseBean updateUsaCode(String newUsaCode) throws RemoteException;
    public ResponseBean updateUsaId(Integer newUsaId) throws RemoteException;
    public ResponseBean updateUsaDescription(String newUsaDescription) throws RemoteException;
    public ResponseBean updateBroasca(String newBroasca) throws RemoteException;
    public ResponseBean updateCilindru(String newCilindru) throws RemoteException;
    public ResponseBean updateSild(String newSild) throws RemoteException;
    public ResponseBean updateYalla(String newYalla) throws RemoteException;
    public ResponseBean updateVizor(String newVizor) throws RemoteException;
    public ResponseBean updateEntryPrice(java.math.BigDecimal newEntryPrice) throws RemoteException;
    public ResponseBean updateSellPrice(java.math.BigDecimal newSellPrice) throws RemoteException;

    public ResponseBean loadListing() throws RemoteException;
    public ResponseBean lineItemsListing() throws RemoteException;
    public ResponseBean loadSubForm(Integer loadId) throws RemoteException;
    public ResponseBean addProduct(Integer id) throws RemoteException, FinderException;
    public ResponseBean removeItem() throws RemoteException;
    public ResponseBean saveSubForm() throws RemoteException;
    public java.util.Collection lineItemsCollectionMap() throws RemoteException;
    public java.util.Map getOfferFieldsMap() throws RemoteException;
}
