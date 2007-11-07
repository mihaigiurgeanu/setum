package ro.kds.erp.rules;

import javax.ejb.EJBLocalHome;
import javax.ejb.CreateException;
import javax.ejb.FinderException;

/**
 * Describe interface RuleLocalHome here.
 *
 *
 * Created: Tue Oct 30 18:51:32 2007
 *
 * @author <a href="mailto:mihai@mihai-giurgeanus-computer.local">Mihai Giurgeanu</a>
 * @version 1.0
 */
public interface RuleLocalHome extends EJBLocalHome {
    public RuleLocal create() throws CreateException;
    public RuleLocal findByPrimaryKey(Integer id) throws FinderException;
    public RuleLocal findByName(String name) throws FinderException;
}
