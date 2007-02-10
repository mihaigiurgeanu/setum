package ro.kds.erp.data;

import javax.ejb.EJBLocalHome;
import javax.ejb.CreateException;
import javax.ejb.FinderException;

/**
 * Describe interface InvoiceLocalHome here.
 *
 *
 * Created: Fri Oct 27 22:55:24 2006
 *
 * @author <a href="mailto:Mihai Giurgeanu@MIHAIG">U-MIHAIG\Mihai Giurgeanu</a>
 * @version 1.0
 */
public interface InvoiceLocalHome extends EJBLocalHome {
    public InvoiceLocal create() throws CreateException, DataLayerException;
    public InvoiceLocal findByPrimaryKey(Integer id) throws FinderException;
}
