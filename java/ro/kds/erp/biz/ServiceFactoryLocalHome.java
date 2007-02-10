package ro.kds.erp.biz;

import javax.ejb.EJBLocalHome;
import javax.ejb.CreateException;

/**
 * The local home of a <code>ServiceFactory</code> ejb.
 *
 *
 * Created: Mon Feb  5 19:33:12 2007
 *
 * @author <a href="mailto:Mihai Giurgeanu@MIHAIG">U-MIHAIG\Mihai Giurgeanu</a>
 * @version 1.0
 */
public interface ServiceFactoryLocalHome extends EJBLocalHome {

    public ServiceFactoryLocal create() throws CreateException;

}
