package ro.kds.erp.rules;

import javax.ejb.EJBLocalObject;
import java.util.Collection;

/**
 * A RulesSet contains rules to be applied for validation purposes
 * by a validation script. The purpose of the RulesSet and Rule 
 * entities is primary to give the user an interface to edit the
 * rues in a user interface.
 *
 *
 *
 * Created: Thu Oct 25 08:22:32 2007
 *
 * @author <a href="mailto:mihai@mihai-giurgeanus-computer.local">Mihai Giurgeanu</a>
 * @version 1.0
 */
public interface RulesSetLocal extends EJBLocalObject {

    public Integer getId();
    public void setId(Integer id);

    public String getName();
    public void setName(String name);

    /**
     * Access the rules stored in this set.
     * @return a <code>Collection</code> of <code>RuleLocal</code>
     * objects.
     */
    public Collection getRules();

    /**
     * Replace the list of the rules contained by this set with a new list.
     *
     * @param rules is a <code>Collection</code> of <code>RuleLocal</code>
     * objects.
     */
    public void setRules(Collection rules);
}
