package ro.kds.erp.biz;

import ro.kds.erp.biz.basic.RulesServerBean;
import javax.naming.NamingException;
import ro.kds.erp.rules.RulesSetLocalHome;
import ro.kds.erp.rules.RulesSetLocal;
import org.objectweb.util.monolog.api.BasicLevel;
import javax.ejb.FinderException;
import ro.kds.erp.rules.RuleLocal;
import ro.kds.erp.rules.RuleLocalHome;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;
import javax.naming.Context;
import java.util.Iterator;
import java.util.Collection;
import javax.ejb.CreateException;
import javax.ejb.RemoveException;

/**
 * Describe class RulesServerImplementation here.
 *
 *
 * Created: Tue Dec  4 09:57:36 2007
 *
 * @author <a href="mailto:mihai@cris.kds.ro">Mihai Giurgeanu</a>
 * @version 1.0
 */
public class RulesServerImplementation extends RulesServerBean {


    /**
     * Retrieves the sets listings from the database.
     */
    public ResponseBean loadSets() {
	ResponseBean r;
	try {
	    RulesSetLocalHome ch = getRulesSetHome();
	    Collection sets = ch.findAll();

	    r = new ResponseBean();
	    for (Iterator i = sets.iterator(); i.hasNext(); ) {
		RulesSetLocal set = (RulesSetLocal)i.next();
		r.addRecord();
		r.addField("set.id", set.getId());
		r.addField("set.name", set.getName());
	    }
	} catch (NamingException e) {
	    r = ResponseBean.getErrConfigNaming(e.getMessage());
	    logger.log(BasicLevel.ERROR, e);
	} catch (FinderException e) {
	    r = ResponseBean.getErrConfigNotFound(e.getMessage());
	    logger.log(BasicLevel.ERROR, e);
	} catch (Exception e) {
	    r = ResponseBean.getErrUnexpected(e);
	    logger.log(BasicLevel.ERROR, e);
	}
	return r;
    }

    /**
     * Retrive the rules listings within the current
     * set.
     */
    public ResponseBean loadRules() {
	ResponseBean r;
	try {
	    RulesSetLocal set = getCurrentSet();
	    if (set == null) {
		r = ResponseBean.SUCCESS; // we deal with a new set (no rules yet)
	    } else {
		Collection rules = set.getRules();
		r = new ResponseBean();
		for(Iterator i=rules.iterator(); i.hasNext(); ) {
		    RuleLocal rule = (RuleLocal)i.next();
		    r.addRecord();
		    r.addField("rule.id", rule.getId());
		    r.addField("rule.name", rule.getName());
		    r.addField("rule.condition", rule.getCondition());
		    r.addField("rule.message", rule.getMessage());
		    r.addField("rule.errorFlag", rule.getErrorFlag());
		}
	    }
	} catch (FinderException e) {
	    r = ResponseBean.getErrNotFound(e.getMessage());
	    logger.log(BasicLevel.DEBUG, e);
	} catch (Exception e) {
	    r = ResponseBean.getErrUnexpected(e);
	    logger.log(BasicLevel.ERROR, e);
	}
	return r;
    }



    /**
     * Retrieves the fields values from the database. It is used
     * by UI module to display the form data. The method fills
     * the associated <code>form</code> object with the data.
     *
     * It is called by the <code>loadFormData</code> of the 
     * superclass that does the common processing.
     *
     * @return a new <code>ResponseBean</code> object that will
     * be filled by <code>loadFormData</code> method.
     */
    public ResponseBean loadFields() throws FinderException {
	ResponseBean r;
	try {
	    RulesSetLocal set = getCurrentSet();
	    if (set != null) {
		form.setSetName(set.getName());
		r = new ResponseBean();
	    } else {
		logger.log(BasicLevel.DEBUG, "No set selected, can not load data form!");
		r = ResponseBean.getErrNotCurrent("RulesSet");
	    }
	} catch (NamingException e) {
	    r = ResponseBean.getErrConfigNaming(e.getMessage());
	    logger.log(BasicLevel.ERROR, "Naming configuration error: " + e.getMessage());
	    logger.log(BasicLevel.DEBUG, e);
	} catch (FinderException e) {
	    r = ResponseBean.getErrNotFound(e.getMessage());
	    logger.log(BasicLevel.ERROR, "Can not find selected rules set in the database: " +
		       e.getMessage());
	    logger.log(BasicLevel.DEBUG, e);
	}
	return r;
    }


    /**
     * Retrieves the fields values from the database. It is used
     * by UI module to display the form data. The method fills
     * the associated <code>form</code> object with the data.
     *
     * It is called by the <code>loadRuleData</code> of the 
     * superclass that does the common processing.
     *
     * @return a new <code>ResponseBean</code> object that will
     * be filled by <code>loadRuleData</code> method.
     */
    public ResponseBean loadRuleFields() throws FinderException {
	ResponseBean r;
	try {
	    RuleLocal rule = getCurrentRule();
	    if(rule != null) {
		form.setRuleName(rule.getName());
		form.setCondition(rule.getCondition());
		form.setMessage(rule.getMessage());
		form.setErrorFlag(rule.getErrorFlag());
		r = new ResponseBean();
	    } else {
		logger.log(BasicLevel.DEBUG, "No rule currently selected. The form data can not be loaded!");
		r = ResponseBean.getErrNotCurrent("Rule");
	    }
	} catch (NamingException e) {
	    r = ResponseBean.getErrConfigNaming(e.getMessage());
	    logger.log(BasicLevel.ERROR, "Naming configuration error: " + e.getMessage());
	    logger.log(BasicLevel.DEBUG, e);
	} catch (FinderException e) {
	    r = ResponseBean.getErrNotFound(e.getMessage());
	    logger.log(BasicLevel.ERROR, "Can not locate selected rule in the database: " + e.getMessage());
	    logger.log(BasicLevel.DEBUG, e);
	}
	return r;
    }



    /**
     * Save the set data.
     */
    public ResponseBean saveFormData() {
	ResponseBean r;

	try {
	    RulesSetLocal set = getCurrentSet();
	    if (set == null) {
		RulesSetLocalHome setHome = getRulesSetHome();
		set = setHome.create();
		this.id = set.getId();
		logger.log(BasicLevel.INFO, "New rules set created: " + id);
	    }
	    set.setName(form.getSetName());
	    logger.log(BasicLevel.INFO, "Rules set saved " + form.getSetName());
	    r = new ResponseBean();
	} catch (NamingException e) {
	    r = ResponseBean.getErrConfigNaming(e.getMessage());
	    logger.log(BasicLevel.ERROR, "Naming configuration error: " + e.getMessage());
	    logger.log(BasicLevel.DEBUG, e);
	} catch (FinderException e) {
	    r = ResponseBean.getErrNotFound(e.getMessage());
	    logger.log(BasicLevel.ERROR, "Selected rules set not found in the database: " +
                       e.getMessage());
	    logger.log(BasicLevel.DEBUG, e);
	} catch (CreateException e) {
	    r = ResponseBean.getErrCreate("RulesSet");
	    logger.log(BasicLevel.ERROR, "Can not create a new rules set. Rules set not saved: " +
		       form.getSetName());
	    logger.log(BasicLevel.DEBUG, e);
	}
	return r;
    }


    /**
     * Stores the rule's data into the database.
     */
    public ResponseBean saveRuleData() {
	ResponseBean r;
	
	try {
	    r = saveFormData();
	    RulesSetLocal set = getCurrentSet();

	    if(set != null) {
		RuleLocal rule = getCurrentRule();
		if(rule == null) {
		    RuleLocalHome ruleHome = getRuleHome();
		    rule = ruleHome.create();
		    this.ruleId = rule.getId();
		    logger.log(BasicLevel.INFO, "New rule created: " + ruleId);
		}
		rule.setName(form.getRuleName());
		rule.setCondition(form.getCondition());
		rule.setMessage(form.getMessage());
		rule.setErrorFlag(form.getErrorFlag());

		set.getRules().add(rule);
		logger.log(BasicLevel.INFO, "Rule data saved: " + form.getRuleName());

		r = new ResponseBean();
	    } else {
		logger.log(BasicLevel.WARN, "Could not save the current rules set. The current rule can not be saved either.");
	    }
	} catch (NamingException e) {
	    r = ResponseBean.getErrConfigNaming(e.getMessage());
	    logger.log(BasicLevel.ERROR, "Naming config error: " + e.getMessage());
            logger.log(BasicLevel.DEBUG, e);
	} catch (FinderException e) {
	    r = ResponseBean.getErrNotFound(e.getMessage());
	    logger.log(BasicLevel.ERROR, "Object not found in database: " + e.getMessage());
	    logger.log(BasicLevel.DEBUG, e);
	} catch (CreateException e) {
	    r = ResponseBean.getErrCreate("Rule");
	    logger.log(BasicLevel.ERROR, "Can not create the rule object into the database: " + 
		       form.getRuleName());
	    logger.log(BasicLevel.DEBUG, e);
	}
	return r;
    }


    /**
     * Removes the selected entity.
     */
    public ResponseBean removeSet() {
	ResponseBean r;
	try {
	    RulesSetLocal set = getCurrentSet();
	    if (set != null) {
		logger.log(BasicLevel.INFO, "Deleting rules set " + set.getName());
		set.remove();
		id = null;
		r = new ResponseBean();
	    } else {
		r = ResponseBean.getErrNotCurrent("RulesSet");
		logger.log(BasicLevel.DEBUG, "removeSet called, but no rules set selected!");
	    }
	} catch (NamingException e) {
	    r = ResponseBean.getErrConfigNaming(e.getMessage());
	    logger.log(BasicLevel.ERROR, "Naming exception: " + e.getMessage());
	    logger.log(BasicLevel.DEBUG, e);
	} catch (FinderException e) {
	    r = ResponseBean.getErrNotFound(e.getMessage());
	    logger.log(BasicLevel.ERROR, "Current rules set not found in db: " + id);
	    logger.log(BasicLevel.DEBUG, e);
	} catch (RemoveException e) {
	    r = ResponseBean.getErrRemove(e.getMessage());
	    logger.log(BasicLevel.ERROR, "Error removing rules set from db: " + id);
	    logger.log(BasicLevel.DEBUG, e);
	}
	return r;
    }


    /**
     * Removes the selected entity.
     */
    public ResponseBean removeRule() {
	ResponseBean r;
	try {
	    RuleLocal rule = getCurrentRule();
	    if (rule != null) {
		logger.log(BasicLevel.INFO, "Deleting rule " + rule.getName());
		rule.remove();
		this.ruleId = null;
		r = new ResponseBean();
	    } else {
		r = ResponseBean.getErrNotCurrent("Rules");
		logger.log(BasicLevel.DEBUG, "removeRule called, but no rule selected!");
	    }
	} catch (NamingException e) {
	    r = ResponseBean.getErrConfigNaming(e.getMessage());
	    logger.log(BasicLevel.ERROR, "Naming exception: " + e.getMessage());
	    logger.log(BasicLevel.DEBUG, e);
	} catch (FinderException e) {
	    r = ResponseBean.getErrNotFound(e.getMessage());
	    logger.log(BasicLevel.ERROR, "Current rule not found in db: " + ruleId);
	    logger.log(BasicLevel.DEBUG, e);
	} catch (RemoveException e) {
	    r = ResponseBean.getErrRemove(e.getMessage());
	    logger.log(BasicLevel.ERROR, "Error removing rule from db: " + ruleId);
	    logger.log(BasicLevel.DEBUG, e);
	}
	return r;
    }


    /**
     * Subform fields initialization.
     */
    public void initRuleFields() {
	form.setRuleName("");
	form.setCondition("");
	form.setMessage("");
	form.setErrorFlag(new Boolean(false));
    }



    /**
     * Helper method to retrieve the current selected set.
     *
     * @return a <code>RulesSetLocal</code> object if there is
     * a current set selected or <code>null</code> otherwise.
     *
     * @throws FinderException if there is a current selected
     * set but it can not be found in the database. This should
     * never happen and it is an error.
     *
     * @throws NamingException if the naming service is not properly
     * configured.
     */
    protected RulesSetLocal getCurrentSet() throws FinderException, NamingException {
	logger.log(BasicLevel.DEBUG, "Looking up rules set for id: " + id);
	if(id == null) return null;

	RulesSetLocal set;
	RulesSetLocalHome home = getRulesSetHome();
	set = home.findByPrimaryKey(id);

	return set;

    }

    /**
     * Helper method to get a reference to the currently
     * selected rule object.
     *
     * @returns a <code>RuleLocal</code> if there is a currently selected
     * rule or <code>null</code> otherwise.
     *
     * @throws FinderException if the currently selected rule can not
     * be found in the database. This is a system error.
     *
     * @throws NamingException if the naming service is not properly
     * configured.
     */
    protected RuleLocal getCurrentRule() throws FinderException, NamingException {
	logger.log(BasicLevel.DEBUG, "Looking up rule for id: " + ruleId);
	if(ruleId == null) return null;

	return getRuleHome().findByPrimaryKey(ruleId);
    }

    /* Caching variables for EJBs homes */

    private RulesSetLocalHome cache_rulesSetHome = null;
    private RuleLocalHome cache_ruleHome = null;


    /**
     * Helper method to get the home reference.
     */
    protected RulesSetLocalHome getRulesSetHome() throws NamingException {
	if(cache_rulesSetHome != null) return cache_rulesSetHome;




	InitialContext ic = new InitialContext();
	Context env = (Context)ic.lookup("java:comp/env");
	cache_rulesSetHome = (RulesSetLocalHome) PortableRemoteObject.
              narrow(env.lookup("ejb/RulesSetHome"), RulesSetLocalHome.class);
	return cache_rulesSetHome;
    }

    /**
     * Helper method to get home reference.
     */
    protected RuleLocalHome getRuleHome() throws NamingException {
	if(cache_ruleHome != null) return cache_ruleHome;
	
	InitialContext ic = new InitialContext();
	Context env = (Context)ic.lookup("java:comp/env");
	cache_ruleHome = (RuleLocalHome) PortableRemoteObject.
            narrow(env.lookup("ejb/RuleHome"), RuleLocalHome.class);
	return cache_ruleHome;
    }

}
