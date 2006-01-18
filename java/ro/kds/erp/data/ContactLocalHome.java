package ro.kds.erp.data;

import javax.ejb.EJBLocalHome;
import javax.ejb.CreateException;
import javax.ejb.FinderException;



/**
 * ContactLocalHome.java
 *
 *
 * Created: Tue Jan 17 04:39:09 2006
 *
 * @author <a href="mailto:mihai@cris.kds.ro">Mihai Giurgeanu</a>
 * @version 1.0
 */

public interface ContactLocalHome extends EJBLocalHome {
    public ContactLocal create() throws CreateException;
    public ContactLocal findByPrimaryKey(Integer id) throws FinderException;
}// ContactLocalHome
