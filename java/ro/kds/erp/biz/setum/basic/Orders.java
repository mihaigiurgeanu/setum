package ro.kds.erp.biz.setum.basic;

import java.rmi.RemoteException;
import javax.ejb.EJBObject;
import javax.ejb.FinderException;
import ro.kds.erp.biz.ResponseBean;

/**
 * Orders remote interface.
 *
 * Class was automaticaly generated from a template.
 *
 */
public interface Orders extends EJBObject {
        
    public ResponseBean newFormData() throws RemoteException;
    public ResponseBean saveFormData() throws RemoteException;
    public ResponseBean loadFormData(Integer id) throws RemoteException, FinderException;

    /**
     * Access to the form data.
     */
     public OrdersForm getForm() throws RemoteException;

    public ResponseBean newOrderLineData() throws RemoteException;
    public ResponseBean saveOrderLineData() throws RemoteException;
    public ResponseBean loadOrderLineData(Integer id) throws RemoteException, FinderException;
    public ResponseBean newInvoiceData() throws RemoteException;
    public ResponseBean saveInvoiceData() throws RemoteException;
    public ResponseBean loadInvoiceData(Integer id) throws RemoteException, FinderException;
    public ResponseBean newPaymentData() throws RemoteException;
    public ResponseBean savePaymentData() throws RemoteException;
    public ResponseBean loadPaymentData(Integer id) throws RemoteException, FinderException;

    public ResponseBean getCurrentFormData() throws RemoteException;
    public ResponseBean getLoadedPrimaryKey() throws RemoteException;

    public ResponseBean updateNumber(String newNumber) throws RemoteException;
    public ResponseBean updateDate(java.util.Date newDate) throws RemoteException;
    public ResponseBean updateClientId(Integer newClientId) throws RemoteException;
    public ResponseBean updateClientName(String newClientName) throws RemoteException;
    public ResponseBean updateMontaj(Integer newMontaj) throws RemoteException;
    public ResponseBean updateLocalitate(Integer newLocalitate) throws RemoteException;
    public ResponseBean updateLocalitateAlta(String newLocalitateAlta) throws RemoteException;
    public ResponseBean updateDistanta(java.math.BigDecimal newDistanta) throws RemoteException;
    public ResponseBean updateObservatii(String newObservatii) throws RemoteException;
    public ResponseBean updateValoareMontaj(java.math.BigDecimal newValoareMontaj) throws RemoteException;
    public ResponseBean updateValoareTransport(java.math.BigDecimal newValoareTransport) throws RemoteException;
    public ResponseBean updateValoareProduse(java.math.BigDecimal newValoareProduse) throws RemoteException;
    public ResponseBean updateTotal(java.math.BigDecimal newTotal) throws RemoteException;
    public ResponseBean updateTvaPercent(Double newTvaPercent) throws RemoteException;
    public ResponseBean updateTotalTva(java.math.BigDecimal newTotalTva) throws RemoteException;
    public ResponseBean updateDiscount(java.math.BigDecimal newDiscount) throws RemoteException;
    public ResponseBean updateTotalFinal(java.math.BigDecimal newTotalFinal) throws RemoteException;
    public ResponseBean updateTotalFinalTva(java.math.BigDecimal newTotalFinalTva) throws RemoteException;
    public ResponseBean updateAvans(java.math.BigDecimal newAvans) throws RemoteException;
    public ResponseBean updateAchitatCu(String newAchitatCu) throws RemoteException;
    public ResponseBean updateValoareAvans(java.math.BigDecimal newValoareAvans) throws RemoteException;
    public ResponseBean updatePayedAmount(java.math.BigDecimal newPayedAmount) throws RemoteException;
    public ResponseBean updateInvoicedAmount(java.math.BigDecimal newInvoicedAmount) throws RemoteException;
    public ResponseBean updateDiferenta(java.math.BigDecimal newDiferenta) throws RemoteException;
    public ResponseBean updateCurrencyCode(String newCurrencyCode) throws RemoteException;
    public ResponseBean updateExchangeRate(java.math.BigDecimal newExchangeRate) throws RemoteException;
    public ResponseBean updateTermenLivrare(java.util.Date newTermenLivrare) throws RemoteException;
    public ResponseBean updateTermenLivrare1(java.util.Date newTermenLivrare1) throws RemoteException;
    public ResponseBean updateAdresaMontaj(String newAdresaMontaj) throws RemoteException;
    public ResponseBean updateAdresaReper(String newAdresaReper) throws RemoteException;
    public ResponseBean updateTelefon(String newTelefon) throws RemoteException;
    public ResponseBean updateContact(String newContact) throws RemoteException;
    public ResponseBean updateDeliveryHour(String newDeliveryHour) throws RemoteException;
    public ResponseBean updateTipDemontare(String newTipDemontare) throws RemoteException;
    public ResponseBean updateAttribute1(String newAttribute1) throws RemoteException;
    public ResponseBean updateAttribute2(String newAttribute2) throws RemoteException;
    public ResponseBean updateAttribute3(String newAttribute3) throws RemoteException;
    public ResponseBean updateAttribute4(String newAttribute4) throws RemoteException;
    public ResponseBean updateAttribute5(String newAttribute5) throws RemoteException;
    public ResponseBean updateLivrariRStart(java.util.Date newLivrariRStart) throws RemoteException;
    public ResponseBean updateLivrariREnd(java.util.Date newLivrariREnd) throws RemoteException;
    public ResponseBean updateLivrariCuMontaj(String newLivrariCuMontaj) throws RemoteException;
    public ResponseBean updateOfferItemId(Integer newOfferItemId) throws RemoteException;
    public ResponseBean updateProductName(String newProductName) throws RemoteException;
    public ResponseBean updateProductCode(String newProductCode) throws RemoteException;
    public ResponseBean updatePrice(java.math.BigDecimal newPrice) throws RemoteException;
    public ResponseBean updateProductPrice(java.math.BigDecimal newProductPrice) throws RemoteException;
    public ResponseBean updatePriceRatio(Double newPriceRatio) throws RemoteException;
    public ResponseBean updateQuantity(java.math.BigDecimal newQuantity) throws RemoteException;
    public ResponseBean updateValue(java.math.BigDecimal newValue) throws RemoteException;
    public ResponseBean updateTax(java.math.BigDecimal newTax) throws RemoteException;
    public ResponseBean updateCodMontaj(Integer newCodMontaj) throws RemoteException;
    public ResponseBean updateMontajProcent(Double newMontajProcent) throws RemoteException;
    public ResponseBean updateMontajSeparat(Boolean newMontajSeparat) throws RemoteException;
    public ResponseBean updateInvoiceNumber(String newInvoiceNumber) throws RemoteException;
    public ResponseBean updateInvoiceDate(java.util.Date newInvoiceDate) throws RemoteException;
    public ResponseBean updateInvoiceRole(String newInvoiceRole) throws RemoteException;
    public ResponseBean updateInvoiceAmount(java.math.BigDecimal newInvoiceAmount) throws RemoteException;
    public ResponseBean updateInvoiceTax(java.math.BigDecimal newInvoiceTax) throws RemoteException;
    public ResponseBean updateInvoiceTotal(java.math.BigDecimal newInvoiceTotal) throws RemoteException;
    public ResponseBean updateInvoicePayed(java.math.BigDecimal newInvoicePayed) throws RemoteException;
    public ResponseBean updateInvoiceUnpayed(java.math.BigDecimal newInvoiceUnpayed) throws RemoteException;
    public ResponseBean updatePaymentNumber(String newPaymentNumber) throws RemoteException;
    public ResponseBean updatePaymentDate(java.util.Date newPaymentDate) throws RemoteException;
    public ResponseBean updatePaymentAmount(java.math.BigDecimal newPaymentAmount) throws RemoteException;

    public ResponseBean getOrdersCount() throws RemoteException;
    public ResponseBean loadListing(Integer startRow) throws RemoteException;
    public ResponseBean loadFullListing() throws RemoteException;
    public ResponseBean removeOrder() throws RemoteException;
    public ResponseBean loadLines() throws RemoteException;
    public ResponseBean removeInvoice() throws RemoteException;
    public ResponseBean loadInvoices() throws RemoteException;
    public ResponseBean removePayment() throws RemoteException;
    public ResponseBean loadPayments() throws RemoteException;
    public ResponseBean orderReport() throws RemoteException;
    public ResponseBean initLivrariReport(String cuMontaj) throws RemoteException;
    public ResponseBean livrariReport() throws RemoteException;
    public ResponseBean livrariCuMontajReport() throws RemoteException;
    public ResponseBean livrariFaraMontajReport() throws RemoteException;

    public ResponseBean addItem (
        Integer offerIntemId
    ) throws RemoteException;
    public ResponseBean removeItem (
    ) throws RemoteException;
}
