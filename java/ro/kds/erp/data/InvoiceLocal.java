package ro.kds.erp.data;

import javax.ejb.EJBLocalObject;
import java.math.BigDecimal;
import java.util.Collection;
import javax.ejb.FinderException;

/**
 * Describe interface InvoiceLocal here.
 *
 *
 * Created: Fri Oct 27 22:48:26 2006
 *
 * @author <a href="mailto:Mihai Giurgeanu@MIHAIG">U-MIHAIG\Mihai Giurgeanu</a>
 * @version 1.0
 */
public interface InvoiceLocal extends EJBLocalObject {
    public Integer getId();
    public void setId(Integer id);

    public DocumentLocal getDocument();
    public void setDocument(DocumentLocal d);

    public OrderLocal getOrder();
    public void setOrder(OrderLocal o);

    public String getRole();
    public void setRole(String role);

    public BigDecimal getAmount();
    public void setAmount(BigDecimal amount);

    public BigDecimal getTax();
    public void setTax(BigDecimal tax);

    public Collection getPayments();
    public void setPayments(Collection payments);

    public BigDecimal getSumOfPayments() throws FinderException;
}
