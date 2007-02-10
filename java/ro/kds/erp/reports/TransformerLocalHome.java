package ro.kds.erp.reports;

import javax.ejb.EJBLocalHome;
import javax.ejb.CreateException;

/**
 * The <code>home</code> interface for <code>Transformer</code> beans.
 *
 *
 * Created: Wed Jan 24 13:35:59 2007
 *
 * @author <a href="mailto:Mihai Giurgeanu@MIHAIG">U-MIHAIG\Mihai Giurgeanu</a>
 * @version 1.0
 */
public interface TransformerLocalHome extends EJBLocalHome {
    public TransformerLocal create() throws CreateException;
}
