package ro.kds.erp.biz.setum.basic;

import java.rmi.RemoteException;
import javax.ejb.EJBObject;
import javax.ejb.FinderException;
import ro.kds.erp.biz.ResponseBean;

/**
 * ArbitraryOffer remote interface.
 *
 * Class was automaticaly generated from a template.
 *
 */
public interface ArbitraryOffer extends EJBObject {
        
    public ResponseBean newFormData() throws RemoteException;
    public ResponseBean saveFormData() throws RemoteException;
    public ResponseBean loadFormData(Integer id) throws RemoteException, FinderException;

    /**
     * Access to the form data.
     */
     public ArbitraryOfferForm getForm() throws RemoteException;


    public ResponseBean getCurrentFormData() throws RemoteException;
    public ResponseBean getLoadedPrimaryKey() throws RemoteException;

    public ResponseBean updateNo(String newNo) throws RemoteException;
    public ResponseBean updateDocDate(java.util.Date newDocDate) throws RemoteException;
    public ResponseBean updateDateFrom(java.util.Date newDateFrom) throws RemoteException;
    public ResponseBean updateDateTo(java.util.Date newDateTo) throws RemoteException;
    public ResponseBean updateDiscontinued(Boolean newDiscontinued) throws RemoteException;
    public ResponseBean updatePeriod(Integer newPeriod) throws RemoteException;
    public ResponseBean updateClientId(Integer newClientId) throws RemoteException;
    public ResponseBean updateClientName(String newClientName) throws RemoteException;
    public ResponseBean updateName(String newName) throws RemoteException;
    public ResponseBean updateDescription(String newDescription) throws RemoteException;
    public ResponseBean updateComment(String newComment) throws RemoteException;
    public ResponseBean updateContract(String newContract) throws RemoteException;
    public ResponseBean updateAnexa(String newAnexa) throws RemoteException;
    public ResponseBean updateTerms(String newTerms) throws RemoteException;
    public ResponseBean updateAttribute1(String newAttribute1) throws RemoteException;
    public ResponseBean updateAttribute2(String newAttribute2) throws RemoteException;
    public ResponseBean updateAttribute3(String newAttribute3) throws RemoteException;
    public ResponseBean updateAttribute4(String newAttribute4) throws RemoteException;
    public ResponseBean updateAttribute5(String newAttribute5) throws RemoteException;
    public ResponseBean updateAttribute6(String newAttribute6) throws RemoteException;
    public ResponseBean updateAttribute7(String newAttribute7) throws RemoteException;
    public ResponseBean updateAttribute8(String newAttribute8) throws RemoteException;
    public ResponseBean updateAttribute9(String newAttribute9) throws RemoteException;
    public ResponseBean updateAttribute10(String newAttribute10) throws RemoteException;
    public ResponseBean updateProductId(Integer newProductId) throws RemoteException;
    public ResponseBean updatePrice(java.math.BigDecimal newPrice) throws RemoteException;
    public ResponseBean updateQuantity(java.math.BigDecimal newQuantity) throws RemoteException;
    public ResponseBean updateValue(java.math.BigDecimal newValue) throws RemoteException;
    public ResponseBean updateVatPrice(java.math.BigDecimal newVatPrice) throws RemoteException;
    public ResponseBean updateVatValue(java.math.BigDecimal newVatValue) throws RemoteException;
    public ResponseBean updateRelativeGain(Double newRelativeGain) throws RemoteException;
    public ResponseBean updateAbsoluteGain(java.math.BigDecimal newAbsoluteGain) throws RemoteException;
    public ResponseBean updateProductCategory(String newProductCategory) throws RemoteException;
    public ResponseBean updateProductCode(String newProductCode) throws RemoteException;
    public ResponseBean updateProductName(String newProductName) throws RemoteException;
    public ResponseBean updateEntryPrice(java.math.BigDecimal newEntryPrice) throws RemoteException;
    public ResponseBean updateSellPrice(java.math.BigDecimal newSellPrice) throws RemoteException;
    public ResponseBean updateBusinessCategory(String newBusinessCategory) throws RemoteException;
    public ResponseBean updateMontajId(Integer newMontajId) throws RemoteException;
    public ResponseBean updateMontajProcent(Double newMontajProcent) throws RemoteException;
    public ResponseBean updateMontajSeparat(Boolean newMontajSeparat) throws RemoteException;
    public ResponseBean updateLocationId(Integer newLocationId) throws RemoteException;
    public ResponseBean updateOtherLocation(String newOtherLocation) throws RemoteException;
    public ResponseBean updateDistance(java.math.BigDecimal newDistance) throws RemoteException;
    public ResponseBean updateDeliveries(Integer newDeliveries) throws RemoteException;
    public ResponseBean updateValMontaj(java.math.BigDecimal newValMontaj) throws RemoteException;
    public ResponseBean updateValTransport(java.math.BigDecimal newValTransport) throws RemoteException;
    public ResponseBean updateClientContactId(Integer newClientContactId) throws RemoteException;
    public ResponseBean updateContact(String newContact) throws RemoteException;
    public ResponseBean updateClientContactName(String newClientContactName) throws RemoteException;
    public ResponseBean updateClientContactPhone(String newClientContactPhone) throws RemoteException;
    public ResponseBean updateClientContactFax(String newClientContactFax) throws RemoteException;
    public ResponseBean updateClientContactMobile(String newClientContactMobile) throws RemoteException;
    public ResponseBean updateClientContactEmail(String newClientContactEmail) throws RemoteException;

    public ResponseBean loadListing() throws RemoteException;
    public ResponseBean getOffersCount() throws RemoteException;
    public ResponseBean loadPartialListing(Integer startRow) throws RemoteException;
    public ResponseBean lineItemsListing() throws RemoteException;
    public ResponseBean loadSubForm(Integer loadId) throws RemoteException;
    public ResponseBean addItem(Integer productId, String businessCategory) throws RemoteException;
    public ResponseBean removeItem() throws RemoteException;
    public ResponseBean saveSubForm() throws RemoteException;
    public ResponseBean loadClientItems(Integer clientId) throws RemoteException;
    public ResponseBean offerReport() throws RemoteException;

}
