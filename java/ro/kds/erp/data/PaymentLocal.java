package ro.kds.erp.data;

import javax.ejb.EJBLocalObject;
import java.math.BigDecimal;

/**
 * Local interface for the <code>PaymentEJB</code> entity.
 *
 *
 * Created: Sat Oct 28 00:15:30 2006
 *
 * @author <a href="mailto:Mihai Giurgeanu@MIHAIG">U-MIHAIG\Mihai Giurgeanu</a>
 * @version 1.0
 */
public interface PaymentLocal extends EJBLocalObject {


    public Integer getId();
    public void setId(Integer id);
    
    public BigDecimal getAmount();
    public void setAmount(BigDecimal amount);

    public Double getExchangeRate();
    public void setExchangeRate(Double rate);

    public DocumentLocal getDocument();
    public void setDocument(DocumentLocal d);

    public InvoiceLocal getInvoice();
    public void setInvoice(InvoiceLocal i);
}
