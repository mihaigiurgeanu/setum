package ro.kds.erp.data;

import javax.ejb.EJBLocalObject;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import javax.ejb.FinderException;

/**
 * The local interface for OrderBean.
 *
 *
 * Created: Wed Aug 30 03:39:55 2006
 *
 * @author <a href="mailto:mihai@cris.kds.ro">Mihai Giurgeanu</a>
 * @version 1.0
 */
public interface OrderLocal extends EJBLocalObject {

    /**
     * Primary key.
     */
    public Integer getId();
    /**
     * Primary key.
     */
    public void setId(Integer id);

    /**
     * An order is a document. The common properties of documents
     * are implemented by <code>DocumentEJB</code> bean.
     */
    public DocumentLocal getDocument();
    /**
     * An order is a document. The common properties of documents
     * are implemented by <code>DocumentEJB</code> bean.
     */
    public void setDocument(DocumentLocal doc);

    /**
     * The client that issued the order.
     */
    public ClientLocal getClient();
    /**
     * The client that issued the order.
     */
    public void setClient(ClientLocal client);

    /**
     * The code of installation procedure to be performed. Code
     * 0 is for no installation procedure.
     */
    public Integer getInstallation();
    /**
     * The code of installation procedure to be performed. Code
     * 0 is for no installation procedure.
     */
    public void setInstallation(Integer code);
   
    /**
     * The code of location where the order items should be delivered.
     * Code 0 is for no location.
     */
    public Integer getDeliveryLocation();
    /**
     * The code of location where the order items should be delivered.
     * Code 0 is for no location.
     */
    public void setDeliveryLocation(Integer location);

    /**
     * The name of the delivery location if a code is not available
     */
    public String getDeliveryLocationOther();
    /**
     * The name of the delivery location if a code is not available
     */
    public void setDeliveryLocationOther(String location);

    /**
     * The distance to the delivery point. It is used for 
     * delivery cost computation.
     */
    public BigDecimal getDeliveryDistance();
    /**
     * The distance to the delivery point. It is used for
     * delivery cost computation.
     */
    public void setDeliveryDistance(BigDecimal distance);

    /**
     * Comments about the delivery.
     */
    public String getDeliveryComments();
    /**
     * Comments about the delivery.
     */
    public void setDeliveryComments(String comments);

    /**
     * Discount applied to the whole order.
     */
    public BigDecimal getDiscount();
    /**
     * Discount applied to the whole order.
     */
    public void setDiscount(BigDecimal discount);

    /**
     * Amount payed in advance by the client.
     */
    public BigDecimal getAdvancePayment();
    /**
     * Amount payed in advance by the client.
     */
    public void setAdvancePayment(BigDecimal payment);

    /**
     * The textual identification of the document atesting
     * the advance payment.
     */
    public String getAdvanceDocument();
    /**
     * The textual identification of the document atesting
     * the advance payment.
     */
    public void setAdvanceDocument(String docDescription);

    /**
     * The date when the delivery should be made.
     */
    public Date getDeliveryTerm();
    /**
     * The date when the delivery should be made.
     */
    public void setDeliveryTerm(Date term);

    /**
     * Suplimentary delivery term.
     */
    public Date getDeliveryTerm1();
    /**
     * Suplimentary delivery term.
     */
    public void setDeliveryTerm1(Date term);

    /**
     * The address for the delivery.
     */
    public String getDeliveryAddress();
    /**
     * The address for the delivery.
     */
    public void setDeliveryAddress(String addr);

    /**
     * Hint for locating the delivery address.
     */
    public String getDeliveryAddressHint();
    /**
     * Hint for locationg the delivery address.
     */
    public void setDeliveryAddressHint(String hint);

    /**
     * The phone number for the delivery location.
     */
    public String getDeliveryPhone();
    /**
     * The phone number for the delivery location.
     */
    public void setDeliveryPhone(String phone);

    /**
     * The name of the person at the delivery location.
     */
    public String getDeliveryContact();
    /**
     * The name of the person at the delivery location.
     */
    public void setDeliveryContact(String name);

    /**
     * The date when the order was delivered.
     */
    public Date getDeliveryDate();
    /**
     * The date when the order was delivered.
     */
    public void setDeliveryDate(Date date);

    /**
     * A short text representing the scheduled delivery hour.
     */
    public String getDeliveryHour();

    /**
     * A short text representing the scheduled delivery hour.
     */
    public void setDeliveryHour(String hour);

    /**
     * Cod tip demontare
     */
    public String getTipDemontare();
    public void setTipDemontare(String tipDemontare);

    public String getAttribute1();
    public void setAttribute1(String attr);

    public String getAttribute2();
    public void setAttribute2(String attr);

    public String getAttribute3();
    public void setAttribute3(String attr);

    public String getAttribute4();
    public void setAttribute4(String attr);

    public String getAttribute5();
    public void setAttribute5(String attr);


    /**
     * The order lines.
     *
     * @return a <code>java.util.Collection</code> of 
     * <code>OrderLineLocal</code> objects.
     */
    public Collection getLines();
    /**
     * The order lines.
     *
     * @param lines must be a <code>java.util.Collection</code> of
     * <code>OrderLineLocal</code> objects.
     */
    public void setLines(Collection lines);

    /**
     * The proformas issued for this order.
     */
    public Collection getProformas();
    
    /**
     * Sets the list of proformas issued for this order.
     */
    public void setProformas(Collection proformas);

    /**
     * The invoices issued for this invoice.
     */
    public Collection getInvoices();
    /**
     * The invoices issued for this invoice.
     */
    public void setInvoices(Collection invoices);


    /**
     * Computes the payed amount, adding up all payments made on
     * all invoices issued for this order.
     */
    public BigDecimal getPayedAmount() throws FinderException;

    /**
     * Computes the invoiced amount, adding up all invoices issued
     * on this order.
     */
    public BigDecimal getInvoicedAmount() throws FinderException;

    /**
     * The sum of payed amounts divided by exchangeRate.
     */
    public BigDecimal getCurrencyPayedAmount() throws FinderException;

    /**
     * The sum of invoiced amounts that were divided by exchangeRate.
     */
    public BigDecimal getCurrencyInvoicedAmount() throws FinderException;
}
