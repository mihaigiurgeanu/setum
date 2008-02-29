package ro.kds.erp.biz.basic;

import javax.ejb.SessionBean;
import javax.ejb.SessionSynchronization;
import javax.ejb.SessionContext;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import java.util.Collection;
import java.util.Iterator;
import javax.naming.*;
import javax.rmi.PortableRemoteObject;

import org.objectweb.util.monolog.Monolog;
import org.objectweb.util.monolog.api.BasicLevel;
import org.objectweb.util.monolog.api.Logger;

import ro.kds.erp.scripting.Script;
import ro.kds.erp.scripting.TclFileScript;
import ro.kds.erp.scripting.ScriptErrorException;
import ro.kds.erp.biz.*;

/**
 * Standard implementation of the RulesServer session bean.
 * You will have to extend this class to proivde code for the abstract
 * methods loadFormData, newForm and saveSaveFormData.
 */
public abstract class RulesServerBean 
	implements SessionBean {

    static protected Logger logger = null;
    protected SessionContext ejbContext;

    /**
     * Stores the reference to the <code>ServiceFactory</code>
     * to be passed to script execution.
     */
    protected ServiceFactoryLocal factory;

    protected Integer id;
    protected Integer ruleId;
    final static String FORM_VARNAME = "form";
    final static String RESPONSE_VARNAME = "response";
    final static String LOGIC_VARNAME = "logic";
    final static String OLDVAL_VARNAME = "oldVal";
    final static String SERVICE_FACTORY_VARNAME = "factory";
    final static String LOGGER_VARNAME = "logger";

    /**
     * The name of the env parameter containing the script prefix.
     * The script prefix should be composed by words separated by the dot
     * in the same way as a fully qualified java class name would look like.
     * The scripts will be located by different script aware methods using
     * this prefix.
     */
    final static String SCRIPT_PREFIX_NAME = "script.prefix";

    /**
     * Cache for the script prefix read from environment variables.
     */
     private String scriptPrefix;

    // ------------------------------------------------------------------
    // SessionBean implementation
    // ------------------------------------------------------------------


    public void setSessionContext(SessionContext ctx) {
        if (logger == null) {
            logger = Monolog.getMonologFactory().getLogger("ro.kds.erp.biz.basic.RulesServer");
        }
        logger.log(BasicLevel.DEBUG, "");
        ejbContext = ctx;

	try {
            InitialContext ic = new InitialContext();
	    Context env = (Context)ic.lookup("java:comp/env");
	    ServiceFactoryLocalHome fh = (ServiceFactoryLocalHome)PortableRemoteObject.
		narrow(env.lookup("ejb/ServiceFactory"), ServiceFactoryLocalHome.class);
            factory = fh.create();
	} catch (Exception e) {
	    logger.log(BasicLevel.ERROR, "ServiceFactory was not instantiated", e);
	}
    }


    public void ejbRemove() {
        logger.log(BasicLevel.DEBUG, "");
    }


    public void ejbCreate() throws CreateException {
	id = null;
	form = null;
        logger.log(BasicLevel.DEBUG, "");
    }

    public void ejbPassivate() {
        logger.log(BasicLevel.DEBUG, "");
    }

    public void ejbActivate() {
        logger.log(BasicLevel.DEBUG, "");
    }

    // ------------------------------------------------------------------
    // RulesServer implementation
    // ------------------------------------------------------------------
    protected RulesServerForm form;

    /**
     * Access to the form data.
     */
     public RulesServerForm getForm() {
	return form;
     }

    
    /**
     * Initialization of a new object. On calling saveFormData method, the
     * object will be added to the database.
     */
    public ResponseBean newFormData() {
	createNewFormBean();

	ResponseBean r = new ResponseBean();
	r.addRecord();
	id = null; // a new product will be added
        ruleId = null;
	
	// set values of the calculated fields
	computeCalculatedFields(null);
	
	// build response
	copyFieldsToResponse(r);
	return r;
    }

    /**
     * Load the form from the database.
     */
     public ResponseBean loadFormData(Integer loadId) throws FinderException {
	logger.log(BasicLevel.DEBUG, "Loading RulesServer with id = " + loadId);
	createNewFormBean();
	id = loadId;

	ResponseBean r = loadFields();
	computeCalculatedFields(null);
	r.addRecord();
	copyFieldsToResponse(r);
	return r;
     }

    /**
     * Locates the data by the primary key and loads the fields into the
     * form. The <code>form</code> instance variable already contains
     * a new initialized form data. The <code>id</code> local variable contains
     * the primary key for the record that should be located and loaded into
     * the form bean.
     */
     public abstract ResponseBean loadFields() throws FinderException;

    /**
     * Create a new RulesServerForm and initialize the 
     * <code>this.form</code> instance variable. Overwrite this method
     * if you want to provide other code for initializing the form bean.
     */
    protected void createNewFormBean() {
	form = new RulesServerForm();
    }

    /**
     * Save the current record into the database.
     */
    public abstract ResponseBean saveFormData();

    public ResponseBean newRuleData() {
        initRuleFields();
        ResponseBean r = new ResponseBean();
        ruleId = null;
        computeCalculatedFields(null);

        r.addRecord();
        copyFieldsToResponse(r);
        return r;
    }

    protected abstract void initRuleFields();

    /**
     * Load the data in the subform rule
     */
    public ResponseBean loadRuleData(Integer loadId) throws FinderException {

	logger.log(BasicLevel.DEBUG, "Loading subform Rule for id = " + loadId);
	initRuleFields();
	ruleId = loadId;

	ResponseBean r = loadRuleFields();
	computeCalculatedFields(null);
	r.addRecord();
	copyFieldsToResponse(r);
	return r;
    }

    /**
     * Loads the fields corresponding to the subform rule
     * from the database.
     */
    protected abstract ResponseBean loadRuleFields() throws FinderException;

    /**
     * Save the current subform record into the database.
     */
    public abstract ResponseBean saveRuleData();



    /**
     * Retrieves the currently loaded data, without changing the current
     * state.
     *
     * @return a <code>ResponseBean</code> containing the current values
     * of the form fields.
     */
     public ResponseBean getCurrentFormData() {
	ResponseBean r = new ResponseBean();
	r.addRecord();
	copyFieldsToResponse(r);
	return r;
     }


    /**
     * Access to the object's primary key.
     *
     * @return a <code>ResponseBean</code> the id field
     */
     public ResponseBean getLoadedPrimaryKey() {
	ResponseBean r = new ResponseBean();
	r.addRecord();
	r.addField("id", id);
	return r;
     }

    /**
     * Evaluates the script for computing the values of the calculated
     * fields. The script is located by the script loader using the key:
     * 
     * <code>ro.kds.erp.biz.basic.RulesServer_calculatedFields</code>
     * 
     * The values of the modified fields are copied back into the form and
     * also in the ResponseBean passed to this method as parameter.
     *
     * @param r is the ResponseBean that will be returned with the values
     * of modified fields added to it. Before pass it to this method,
     * make sure that r.addRecord() has been called. This parameter can
     * be null in wich case a new ResponseBean will be created.
     *
     * @return a ResponseBean containing the modified field values.
     */
     public ResponseBean computeCalculatedFields(ResponseBean r) {
	if(r == null) {
	   r = new ResponseBean();
	   r.addRecord();
        }
	Script script = TclFileScript
		.loadScript(getScriptPrefix() + "_calculatedFields");
	if(script.loaded()) {
	    try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(FORM_VARNAME, form, 
			      RulesServerForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);

		addFieldsToScript(script);
		script.run();

		getFieldsFromScript(script, r);

	    } catch (ScriptErrorException e) {
		logger.log(BasicLevel.ERROR, "Error executing rules script for RulesServer", e);
	    }
	}
	return r;
    }
	

    /**
     * Evaluates the validation script. The script loader loades the script
     * file corresponding to:
     *
     * <code>ro.kds.erp.biz.basic.RulesServer_validation</code>
     *
     * The return is a ResponseBean that has records representing the
     * number validation errors. A record in the response contains the fields:
     * - message - is the error message
     * - fieldId - is the name of the field that caused the error
     *
     * The code of the response will represent the number of rules that
     * were broken.
     *
     * The modified fields will not be puted back into the form, so any
     * modifications of the fields values made during the script will 
     * be lost.
     */
    public ResponseBean validate() {
      	ResponseBean r = new ResponseBean();

	Script script = TclFileScript
		.loadScript(getScriptPrefix() + "_validation");
	if(script.loaded()) {
	    try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(FORM_VARNAME, form, 
			      RulesServerForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();

		// I don't add modified fields back to the response bean,
		// so any fields modifications made in the script will
		// be ignored.
	    } catch (ScriptErrorException e) {
		logger.log(BasicLevel.ERROR, "Error executing rules script for RulesServer", e);
	    }
	}
	return r;
    }

    public ResponseBean updateSetName(String setName) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getSetName();
	form.setSetName(setName);
	r.addRecord();
	r.addField("setName", setName); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".setName");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, RulesServerForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the setName", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateRuleName(String ruleName) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getRuleName();
	form.setRuleName(ruleName);
	r.addRecord();
	r.addField("ruleName", ruleName); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".ruleName");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, RulesServerForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the ruleName", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateCondition(String condition) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getCondition();
	form.setCondition(condition);
	r.addRecord();
	r.addField("condition", condition); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".condition");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, RulesServerForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the condition", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateMessage(String message) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getMessage();
	form.setMessage(message);
	r.addRecord();
	r.addField("message", message); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".message");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, RulesServerForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the message", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateMessageParam(String messageParam) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getMessageParam();
	form.setMessageParam(messageParam);
	r.addRecord();
	r.addField("messageParam", messageParam); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".messageParam");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, RulesServerForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the messageParam", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateErrorFlag(Boolean errorFlag) {
        ResponseBean r = new ResponseBean();
	Boolean oldVal = form.getErrorFlag();
	form.setErrorFlag(errorFlag);
	r.addRecord();
	r.addField("errorFlag", errorFlag); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".errorFlag");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, Boolean.class);
		script.setVar(FORM_VARNAME, form, RulesServerForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the errorFlag", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }

    /**
     * Generated implementation of the loadSets service. It will call
     * the script ro.kds.erp.biz.basic.RulesServer.loadSets
     * to execute the request.
     * 
     * The <code>ResponseBean</code> to be returned will be automatically populated with the
     * fields modified by the script and the changes will be automatically added to the
     * form bean.
     *
     * @return a <code>ResponseBean</code> containing the field values that were changed by
     * the script and any of the fields added to it by the script.
     * 
     */
    public ResponseBean loadSets (
    ) {


        ResponseBean r = new ResponseBean();
        r.addRecord();
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".loadSets");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(FORM_VARNAME, form, RulesServerForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);


		
		addFieldsToScript(script);
		script.run();
                getFieldsFromScript(script, r);
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for service loadSets", e);
           }
        }
	return r;
    }
    /**
     * Generated implementation of the loadRules service. It will call
     * the script ro.kds.erp.biz.basic.RulesServer.loadRules
     * to execute the request.
     * 
     * The <code>ResponseBean</code> to be returned will be automatically populated with the
     * fields modified by the script and the changes will be automatically added to the
     * form bean.
     *
     * @return a <code>ResponseBean</code> containing the field values that were changed by
     * the script and any of the fields added to it by the script.
     * 
     */
    public ResponseBean loadRules (
    ) {


        ResponseBean r = new ResponseBean();
        r.addRecord();
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".loadRules");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(FORM_VARNAME, form, RulesServerForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);


		
		addFieldsToScript(script);
		script.run();
                getFieldsFromScript(script, r);
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for service loadRules", e);
           }
        }
	return r;
    }
    /**
     * Generated implementation of the removeSet service. It will call
     * the script ro.kds.erp.biz.basic.RulesServer.removeSet
     * to execute the request.
     * 
     * The <code>ResponseBean</code> to be returned will be automatically populated with the
     * fields modified by the script and the changes will be automatically added to the
     * form bean.
     *
     * @return a <code>ResponseBean</code> containing the field values that were changed by
     * the script and any of the fields added to it by the script.
     * 
     */
    public ResponseBean removeSet (
    ) {


        ResponseBean r = new ResponseBean();
        r.addRecord();
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".removeSet");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(FORM_VARNAME, form, RulesServerForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);


		
		addFieldsToScript(script);
		script.run();
                getFieldsFromScript(script, r);
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for service removeSet", e);
           }
        }
	return r;
    }
    /**
     * Generated implementation of the removeRule service. It will call
     * the script ro.kds.erp.biz.basic.RulesServer.removeRule
     * to execute the request.
     * 
     * The <code>ResponseBean</code> to be returned will be automatically populated with the
     * fields modified by the script and the changes will be automatically added to the
     * form bean.
     *
     * @return a <code>ResponseBean</code> containing the field values that were changed by
     * the script and any of the fields added to it by the script.
     * 
     */
    public ResponseBean removeRule (
    ) {


        ResponseBean r = new ResponseBean();
        r.addRecord();
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".removeRule");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(FORM_VARNAME, form, RulesServerForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);


		
		addFieldsToScript(script);
		script.run();
                getFieldsFromScript(script, r);
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for service removeRule", e);
           }
        }
	return r;
    }

    /**
     * Get the fields stored internaly and adds them to the response.
     */
    protected void copyFieldsToResponse(ResponseBean r) {
	r.addField("setName", form.getSetName());
	r.addField("ruleName", form.getRuleName());
	r.addField("condition", form.getCondition());
	r.addField("message", form.getMessage());
	r.addField("messageParam", form.getMessageParam());
	r.addField("errorFlag", form.getErrorFlag());
	loadValueLists(r);
    }

    /**
     * Add all the fields of the form as variables for the script
     */
    protected void addFieldsToScript(Script s) {
        logger.log(BasicLevel.DEBUG, "start");
	try {
            s.setVar("logger", logger, Logger.class);
        } catch (ScriptErrorException e) {
            logger.log(BasicLevel.ERROR, "Can not set the logger variable in the script" +
                       e.getMessage());
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("setName", form.getSetName(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: setName from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("ruleName", form.getRuleName(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: ruleName from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("condition", form.getCondition(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: condition from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("message", form.getMessage(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: message from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("messageParam", form.getMessageParam(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: messageParam from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("errorFlag", form.getErrorFlag(), Boolean.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: errorFlag from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
        logger.log(BasicLevel.DEBUG, "end");
    }

    /**
     * Get the values of the fields from script variables. Writes in the
     * response bean any field that was modified.
     */
    protected void getFieldsFromScript(Script s, ResponseBean r) {
	Object field;
	try {
	    field = s.getVar("setName", String.class);
	    if(!field.equals(form.getSetName())) {
	        logger.log(BasicLevel.DEBUG, "Field setName modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setSetName((String)field);
	        r.addField("setName", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: setName from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("ruleName", String.class);
	    if(!field.equals(form.getRuleName())) {
	        logger.log(BasicLevel.DEBUG, "Field ruleName modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setRuleName((String)field);
	        r.addField("ruleName", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: ruleName from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("condition", String.class);
	    if(!field.equals(form.getCondition())) {
	        logger.log(BasicLevel.DEBUG, "Field condition modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setCondition((String)field);
	        r.addField("condition", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: condition from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("message", String.class);
	    if(!field.equals(form.getMessage())) {
	        logger.log(BasicLevel.DEBUG, "Field message modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setMessage((String)field);
	        r.addField("message", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: message from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("messageParam", String.class);
	    if(!field.equals(form.getMessageParam())) {
	        logger.log(BasicLevel.DEBUG, "Field messageParam modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setMessageParam((String)field);
	        r.addField("messageParam", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: messageParam from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("errorFlag", Boolean.class);
	    if(!field.equals(form.getErrorFlag())) {
	        logger.log(BasicLevel.DEBUG, "Field errorFlag modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setErrorFlag((Boolean)field);
	        r.addField("errorFlag", (Boolean)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: errorFlag from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
    }

    /**
     * Adds default value lists to the response bean. This method is 
     * called by the <code>copyFieldsToResponse(...)</code> method. Default
     * implementation does nothing. You have to overwrite this method
     * to add your value lists to the response bean, when a form data is
     * loading or when a new object is to be created.
     */
     protected void loadValueLists(ResponseBean r) {}


    /**
     * Convinience method to get the script prefix value from environment vars.
     * It caches the value, so only one call would search the jndi directory.
     */
     protected String getScriptPrefix() {
         if(scriptPrefix != null)
             return scriptPrefix;

         try {
             InitialContext ic = new InitialContext();
             Context env = (Context)ic.lookup("java:comp/env");
             scriptPrefix = (String)env.lookup(SCRIPT_PREFIX_NAME);
             return scriptPrefix;

         } catch (NamingException e) {
             logger.log(BasicLevel.WARN, "The value for 'script.prefix' can not be read from environment. Use prefsadmin utility to set this value to the root directory where the script files are located.");
             logger.log(BasicLevel.DEBUG, e);
             return "ro.kds.erp.biz.basic.RulesServer";
         }
         
     }

}

