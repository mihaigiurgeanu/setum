package ro.kds.erp.rules;

import javax.ejb.EJBLocalHome;
import javax.ejb.CreateException;
import javax.ejb.FinderException;

/**
 * The <code>LocalHome</code> interface of the <code>RulesSet</code>
 * bea.
 *
 *
 * Created: Thu Oct 25 08:32:39 2007
 *
 * @author <a href="mailto:mihai@mihai-giurgeanus-computer.local">Mihai Giurgeanu</a>
 * @version 1.0
 */
public interface RulesSetLocalHome extends EJBLocalHome {
    public RulesSetLocal create() throws CreateException;
    public RulesSetLocal findByPrimaryKey(Integer id) throws FinderException;
    public RulesSetLocal findByName(String name) throws FinderException;
}
