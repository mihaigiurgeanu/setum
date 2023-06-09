package ro.kds.erp.data;

import javax.ejb.EJBLocalHome;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import java.util.Collection;



/**
 * ClientLocalHome.java
 *
 *
 * Created: Tue Jan 17 04:36:37 2006
 *
 * @author <a href="mailto:mihai@cris.kds.ro">Mihai Giurgeanu</a>
 * @version $Id: ClientLocalHome.java,v 1.2 2006/08/23 03:50:06 mihai Exp $
 */

public interface ClientLocalHome extends EJBLocalHome {
    public ClientLocal create() throws CreateException;
    public ClientLocal findByPrimaryKey(Integer id) throws FinderException;
    public Collection findByCompanyFlag(boolean isCompany)
	throws FinderException;
    public Collection findAll() throws FinderException;
}// ClientLocalHome
