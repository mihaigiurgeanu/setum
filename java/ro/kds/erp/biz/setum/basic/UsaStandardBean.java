package ro.kds.erp.biz.setum.basic;

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
 * Standard implementation of the UsaStandard session bean.
 * You will have to extend this class to proivde code for the abstract
 * methods loadFormData, newForm and saveSaveFormData.
 */
public abstract class UsaStandardBean 
	implements SessionBean {

    static protected Logger logger = null;
    protected SessionContext ejbContext;

    /**
     * Stores the reference to the <code>ServiceFactory</code>
     * to be passed to script execution.
     */
    protected ServiceFactoryLocal factory;

    protected Integer id;
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
            logger = Monolog.getMonologFactory().getLogger("ro.kds.erp.biz.setum.basic.UsaStandard");
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
    // UsaStandard implementation
    // ------------------------------------------------------------------
    protected UsaStandardForm form;

    /**
     * Access to the form data.
     */
     public UsaStandardForm getForm() {
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
	logger.log(BasicLevel.DEBUG, "Loading UsaStandard with id = " + loadId);
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
     * Create a new UsaStandardForm and initialize the 
     * <code>this.form</code> instance variable. Overwrite this method
     * if you want to provide other code for initializing the form bean.
     */
    protected void createNewFormBean() {
	form = new UsaStandardForm();
    }

    /**
     * Save the current record into the database.
     */
    public abstract ResponseBean saveFormData();



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
     * <code>ro.kds.erp.biz.setum.basic.UsaStandard_calculatedFields</code>
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
			      UsaStandardForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);

		addFieldsToScript(script);
		script.run();

		getFieldsFromScript(script, r);

	    } catch (ScriptErrorException e) {
		logger.log(BasicLevel.ERROR, "Error executing rules script for UsaStandard", e);
	    }
	}
	return r;
    }
	

    /**
     * Evaluates the validation script. The script loader loades the script
     * file corresponding to:
     *
     * <code>ro.kds.erp.biz.setum.basic.UsaStandard_validation</code>
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
			      UsaStandardForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();

		// I don't add modified fields back to the response bean,
		// so any fields modifications made in the script will
		// be ignored.
	    } catch (ScriptErrorException e) {
		logger.log(BasicLevel.ERROR, "Error executing rules script for UsaStandard", e);
	    }
	}
	return r;
    }

    public ResponseBean updateName(String name) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getName();
	form.setName(name);
	r.addRecord();
	r.addField("name", name); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".name");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, UsaStandardForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the name", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateCode(String code) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getCode();
	form.setCode(code);
	r.addRecord();
	r.addField("code", code); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".code");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, UsaStandardForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the code", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateDiscontinued(Integer discontinued) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getDiscontinued();
	form.setDiscontinued(discontinued);
	r.addRecord();
	r.addField("discontinued", discontinued); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".discontinued");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, Integer.class);
		script.setVar(FORM_VARNAME, form, UsaStandardForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the discontinued", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateUsaId(Integer usaId) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getUsaId();
	form.setUsaId(usaId);
	r.addRecord();
	r.addField("usaId", usaId); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".usaId");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, Integer.class);
		script.setVar(FORM_VARNAME, form, UsaStandardForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the usaId", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateBroascaId(Integer broascaId) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getBroascaId();
	form.setBroascaId(broascaId);
	r.addRecord();
	r.addField("broascaId", broascaId); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".broascaId");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, Integer.class);
		script.setVar(FORM_VARNAME, form, UsaStandardForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the broascaId", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateCilindruId(Integer cilindruId) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getCilindruId();
	form.setCilindruId(cilindruId);
	r.addRecord();
	r.addField("cilindruId", cilindruId); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".cilindruId");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, Integer.class);
		script.setVar(FORM_VARNAME, form, UsaStandardForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the cilindruId", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateSildId(Integer sildId) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getSildId();
	form.setSildId(sildId);
	r.addRecord();
	r.addField("sildId", sildId); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".sildId");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, Integer.class);
		script.setVar(FORM_VARNAME, form, UsaStandardForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the sildId", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateYallaId(Integer yallaId) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getYallaId();
	form.setYallaId(yallaId);
	r.addRecord();
	r.addField("yallaId", yallaId); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".yallaId");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, Integer.class);
		script.setVar(FORM_VARNAME, form, UsaStandardForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the yallaId", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateVizorId(Integer vizorId) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getVizorId();
	form.setVizorId(vizorId);
	r.addRecord();
	r.addField("vizorId", vizorId); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".vizorId");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, Integer.class);
		script.setVar(FORM_VARNAME, form, UsaStandardForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the vizorId", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateFilterUsa(String filterUsa) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getFilterUsa();
	form.setFilterUsa(filterUsa);
	r.addRecord();
	r.addField("filterUsa", filterUsa); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".filterUsa");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, UsaStandardForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the filterUsa", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateFilterBroasca(String filterBroasca) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getFilterBroasca();
	form.setFilterBroasca(filterBroasca);
	r.addRecord();
	r.addField("filterBroasca", filterBroasca); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".filterBroasca");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, UsaStandardForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the filterBroasca", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateFilterCilindru(String filterCilindru) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getFilterCilindru();
	form.setFilterCilindru(filterCilindru);
	r.addRecord();
	r.addField("filterCilindru", filterCilindru); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".filterCilindru");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, UsaStandardForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the filterCilindru", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateFilterSild(String filterSild) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getFilterSild();
	form.setFilterSild(filterSild);
	r.addRecord();
	r.addField("filterSild", filterSild); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".filterSild");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, UsaStandardForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the filterSild", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateFilterYalla(String filterYalla) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getFilterYalla();
	form.setFilterYalla(filterYalla);
	r.addRecord();
	r.addField("filterYalla", filterYalla); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".filterYalla");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, UsaStandardForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the filterYalla", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateFilterVizor(String filterVizor) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getFilterVizor();
	form.setFilterVizor(filterVizor);
	r.addRecord();
	r.addField("filterVizor", filterVizor); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".filterVizor");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, UsaStandardForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the filterVizor", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }


    /**
     * Get the fields stored internaly and adds them to the response.
     */
    protected void copyFieldsToResponse(ResponseBean r) {
	r.addField("name", form.getName());
	r.addField("code", form.getCode());
	r.addField("discontinued", form.getDiscontinued());
	r.addField("usaId", form.getUsaId());
	r.addField("broascaId", form.getBroascaId());
	r.addField("cilindruId", form.getCilindruId());
	r.addField("sildId", form.getSildId());
	r.addField("yallaId", form.getYallaId());
	r.addField("vizorId", form.getVizorId());
	r.addField("filterUsa", form.getFilterUsa());
	r.addField("filterBroasca", form.getFilterBroasca());
	r.addField("filterCilindru", form.getFilterCilindru());
	r.addField("filterSild", form.getFilterSild());
	r.addField("filterYalla", form.getFilterYalla());
	r.addField("filterVizor", form.getFilterVizor());
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
	    s.setVar("name", form.getName(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: name from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("code", form.getCode(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: code from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("discontinued", form.getDiscontinued(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: discontinued from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("usaId", form.getUsaId(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: usaId from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("broascaId", form.getBroascaId(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: broascaId from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("cilindruId", form.getCilindruId(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: cilindruId from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("sildId", form.getSildId(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: sildId from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("yallaId", form.getYallaId(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: yallaId from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("vizorId", form.getVizorId(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: vizorId from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("filterUsa", form.getFilterUsa(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: filterUsa from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("filterBroasca", form.getFilterBroasca(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: filterBroasca from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("filterCilindru", form.getFilterCilindru(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: filterCilindru from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("filterSild", form.getFilterSild(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: filterSild from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("filterYalla", form.getFilterYalla(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: filterYalla from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("filterVizor", form.getFilterVizor(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: filterVizor from the script");
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
	    field = s.getVar("name", String.class);
	    if(!field.equals(form.getName())) {
	        logger.log(BasicLevel.DEBUG, "Field name modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setName((String)field);
	        r.addField("name", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: name from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("code", String.class);
	    if(!field.equals(form.getCode())) {
	        logger.log(BasicLevel.DEBUG, "Field code modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setCode((String)field);
	        r.addField("code", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: code from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("discontinued", Integer.class);
	    if(!field.equals(form.getDiscontinued())) {
	        logger.log(BasicLevel.DEBUG, "Field discontinued modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setDiscontinued((Integer)field);
	        r.addField("discontinued", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: discontinued from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("usaId", Integer.class);
	    if(!field.equals(form.getUsaId())) {
	        logger.log(BasicLevel.DEBUG, "Field usaId modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setUsaId((Integer)field);
	        r.addField("usaId", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: usaId from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("broascaId", Integer.class);
	    if(!field.equals(form.getBroascaId())) {
	        logger.log(BasicLevel.DEBUG, "Field broascaId modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setBroascaId((Integer)field);
	        r.addField("broascaId", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: broascaId from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("cilindruId", Integer.class);
	    if(!field.equals(form.getCilindruId())) {
	        logger.log(BasicLevel.DEBUG, "Field cilindruId modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setCilindruId((Integer)field);
	        r.addField("cilindruId", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: cilindruId from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("sildId", Integer.class);
	    if(!field.equals(form.getSildId())) {
	        logger.log(BasicLevel.DEBUG, "Field sildId modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setSildId((Integer)field);
	        r.addField("sildId", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: sildId from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("yallaId", Integer.class);
	    if(!field.equals(form.getYallaId())) {
	        logger.log(BasicLevel.DEBUG, "Field yallaId modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setYallaId((Integer)field);
	        r.addField("yallaId", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: yallaId from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("vizorId", Integer.class);
	    if(!field.equals(form.getVizorId())) {
	        logger.log(BasicLevel.DEBUG, "Field vizorId modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setVizorId((Integer)field);
	        r.addField("vizorId", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: vizorId from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("filterUsa", String.class);
	    if(!field.equals(form.getFilterUsa())) {
	        logger.log(BasicLevel.DEBUG, "Field filterUsa modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setFilterUsa((String)field);
	        r.addField("filterUsa", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: filterUsa from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("filterBroasca", String.class);
	    if(!field.equals(form.getFilterBroasca())) {
	        logger.log(BasicLevel.DEBUG, "Field filterBroasca modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setFilterBroasca((String)field);
	        r.addField("filterBroasca", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: filterBroasca from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("filterCilindru", String.class);
	    if(!field.equals(form.getFilterCilindru())) {
	        logger.log(BasicLevel.DEBUG, "Field filterCilindru modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setFilterCilindru((String)field);
	        r.addField("filterCilindru", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: filterCilindru from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("filterSild", String.class);
	    if(!field.equals(form.getFilterSild())) {
	        logger.log(BasicLevel.DEBUG, "Field filterSild modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setFilterSild((String)field);
	        r.addField("filterSild", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: filterSild from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("filterYalla", String.class);
	    if(!field.equals(form.getFilterYalla())) {
	        logger.log(BasicLevel.DEBUG, "Field filterYalla modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setFilterYalla((String)field);
	        r.addField("filterYalla", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: filterYalla from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("filterVizor", String.class);
	    if(!field.equals(form.getFilterVizor())) {
	        logger.log(BasicLevel.DEBUG, "Field filterVizor modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setFilterVizor((String)field);
	        r.addField("filterVizor", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: filterVizor from the script");
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
             return "ro.kds.erp.biz.setum.basic.UsaStandard";
         }
         
     }

}

