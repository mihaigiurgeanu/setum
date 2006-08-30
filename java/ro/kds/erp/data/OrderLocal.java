package ro.kds.erp.data;

import javax.ejb.EJBLocalObject;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

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
    public Integer getId();
    public void setId(Integer id);

    /**
     * An order is a document. The common properties of documents
     * are implemented by <code>DocumentEJB</code> bean.
     */
    public DocumentLocal getDocument();
    public void setDocument(DocumentLocal doc);

    /**
     * An order is made by a client
     */
    public ClientLocal getClient();
    public void setClient(ClientLocal client);
    
    /**
     * Each order has associated with it a special unique <code>Product</code>
     * that gathers all the details about how the order should be processed.
     */
    public ProductLocal getDeliveryConditions();
    public void setDeliveryConditions(ProductLocal p);

    public BigDecimal getDiscount();
    public void setDiscount(BigDecimal discount);

    public BigDecimal getAdvancePayment();
    public void setAdvancePayment(BigDecimal payment);

    /**
     * A text description of the document associated with 
     * the payment and the payment method used.
     */
    public String getAdvanceDocument();
    public setAdvanceDocument(String docDescription);

    public Date getDeliveryDate();
    public void setDeliveryDate(Date date);

    public Collection getLines();
    public void setLines(Collection lines);


}
