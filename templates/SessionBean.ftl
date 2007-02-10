[#ftl]
package ${.node.class.package};

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
 * Standard implementation of the ${.node.class.name} session bean.
 * You will have to extend this class to proivde code for the abstract
 * methods loadFormData, newForm and saveSaveFormData.
 */
public abstract class ${.node.class.name}Bean 
	implements SessionBean {

    static protected Logger logger = null;
    protected SessionContext ejbContext;

    /**
     * Stores the reference to the <code>ServiceFactory</code>
     * to be passed to script execution.
     */
    protected ServiceFactoryLocal factory;

    protected Integer id;
    [#list .node.class.subforms.subform as subform]
    protected Integer ${subform.@name}Id;
    [/#list]
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
            logger = Monolog.getMonologFactory().getLogger("${.node.class.package}.${.node.class.name}");
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
    // ${.node.class.name} implementation
    // ------------------------------------------------------------------
    protected ${.node.class.name}Form form;
    
    /**
     * Initialization of a new object. On calling saveFormData method, the
     * object will be added to the database.
     */
    public ResponseBean newFormData() {
	createNewFormBean();

	ResponseBean r = new ResponseBean();
	r.addRecord();
	id = null; // a new product will be added
        [#list .node.class.subforms.subform as subform]
        ${subform.@name}Id = null;
        [/#list]
	
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
	logger.log(BasicLevel.DEBUG, "Loading ${.node.class.name} with id = " + loadId);
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
     * Create a new ${.node.class.name}Form and initialize the 
     * <code>this.form</code> instance variable. Overwrite this method
     * if you want to provide other code for initializing the form bean.
     */
    protected void createNewFormBean() {
	form = new ${.node.class.name}Form();
    }

    /**
     * Save the current record into the database.
     */
    public abstract ResponseBean saveFormData();

    [#list .node.class.subforms.subform as subform]
    public ResponseBean new${subform.@name?cap_first}Data() {
        init${subform.@name?cap_first}Fields();
        ResponseBean r = new ResponseBean();
        ${subform.@name}Id = null;
        computeCalculatedFields(null);

        r.addRecord();
        copyFieldsToResponse(r);
        return r;
    }

    protected abstract void init${subform.@name?cap_first}Fields();

    /**
     * Load the data in the subform ${subform.@name}
     */
    public ResponseBean load${subform.@name?cap_first}Data(Integer loadId) throws FinderException {

	logger.log(BasicLevel.DEBUG, "Loading subform ${subform.@name?cap_first} for id = " + loadId);
	init${subform.@name?cap_first}Fields();
	${subform.@name}Id = loadId;

	ResponseBean r = load${subform.@name?cap_first}Fields();
	computeCalculatedFields(null);
	r.addRecord();
	copyFieldsToResponse(r);
	return r;
    }

    /**
     * Loads the fields corresponding to the subform ${subform.@name}
     * from the database.
     */
    protected abstract ResponseBean load${subform.@name?cap_first}Fields() throws FinderException;

    /**
     * Save the current subform record into the database.
     */
    public abstract ResponseBean save${subform.@name?cap_first}Data();

    [/#list]


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
     * <code>${.node.class.package}.${.node.class.name}_calculatedFields</code>
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
		script.setVar(FORM_VARNAME, form, 
			      ${.node.class.name}Form.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);

		addFieldsToScript(script);
		script.run();

		getFieldsFromScript(script, r);

	    } catch (ScriptErrorException e) {
		logger.log(BasicLevel.ERROR, "Error executing rules script for ${.node.class.name}", e);
	    }
	}
	return r;
    }
	

    /**
     * Evaluates the validation script. The script loader loades the script
     * file corresponding to:
     *
     * <code>${.node.class.package}.${.node.class.name}_validation</code>
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
		script.setVar(FORM_VARNAME, form, 
			      ${.node.class.name}Form.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();

		// I don't add modified fields back to the response bean,
		// so any fields modifications made in the script will
		// be ignored.
	    } catch (ScriptErrorException e) {
		logger.log(BasicLevel.ERROR, "Error executing rules script for ${.node.class.name}", e);
	    }
	}
	return r;
    }

    [#list .node.class.field as field]
    public ResponseBean update${field.name?cap_first}(${field.type} ${field.name}) {
        ResponseBean r = new ResponseBean();
	${field.type} oldVal = form.get${field.name?cap_first}();
	form.set${field.name?cap_first}(${field.name});
	r.addRecord();
	r.addField("${field.name}", ${field.name}); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".${field.name}");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, ${field.type}.class);
		script.setVar(FORM_VARNAME, form, ${.node.class.name}Form.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the ${field.name}", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    [/#list]

    [#list .node.class.services.method as method]
    /**
     * Generated implementation of the ${method.name} service. It will call
     * the script ${.node.class.package}.${.node.class.name}.${method.name}
     * to execute the request.
     * [#if method.@returnType = "standard"]
     * The <code>ResponseBean</code> to be returned will be automatically populated with the
     * fields modified by the script and the changes will be automatically added to the
     * form bean.
     *
     * @return a <code>ResponseBean</code> containing the field values that were changed by
     * the script and any of the fields added to it by the script.
     * [#else]
     * The method does not automatically add the changed values to the <code>ResponseBean</code>
     * and does not add the values of changed variables to the form. The script is responsible
     * to build the <code>ResponseBean</code> by using the <code>response</code> variable in the
     * script and to make the changes in the form bean by using <code>form</code> variable in
     * the script.
     *
     * @return the <code>ResponseBean</code> that the script can access through the <code>response</code>
     * variable.
     * [/#if]
     */
    public ResponseBean ${method.name} (
        [#list method.params.param as param]
        ${param.type} ${param.name}[#if param_has_next],[/#if]
        [/#list]
    ) [#if method.throws?has_content]throws [#list method.throws.throw as throw]${throw}[#if throw_has_next],[/#if] [/#list][/#if]{


        ResponseBean r = new ResponseBean();
        [#if method.@returnType = "standard"]
        r.addRecord();
        [/#if]
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".${method.name}");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(FORM_VARNAME, form, ${.node.class.name}Form.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
                [#if method.@returnType = "standard"]
                getFieldsFromScript(script, r);
                [/#if]
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for service ${method.name}", e);
           }
        }
	return r;
    }
    [/#list]

    /**
     * Get the fields stored internaly and adds them to the response.
     */
    protected void copyFieldsToResponse(ResponseBean r) {
	[#list .node.class.field as field]
	r.addField("${field.name}", form.get${field.name?cap_first}());
	[/#list]
	loadValueLists(r);
    }

    /**
     * Add all the fields of the form as variables for the script
     */
    protected void addFieldsToScript(Script s) {
	try {
            s.setVar("logger", logger, Logger.class);
        } catch (ScriptErrorException e) {
            logger.log(BasicLevel.ERROR, "Can not set the logger variable in the script" +
                       e.getMessage());
            logger.log(BasicLevel.DEBUG, e);
        }
	[#list .node.class.field as field]
	try {
	    s.setVar("${field.name}", form.get${field.name?cap_first}(), ${field.type}.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: ${field.name} from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	[/#list]
    }

    /**
     * Get the values of the fields from script variables. Writes in the
     * response bean any field that was modified.
     */
    protected void getFieldsFromScript(Script s, ResponseBean r) {
	Object field;
	[#list .node.class.field as field]
	try {
	    field = s.getVar("${field.name}", ${field.type}.class);
	    if(!field.equals(form.get${field.name?cap_first}())) {
	        logger.log(BasicLevel.DEBUG, "Field ${field.name} modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.set${field.name?cap_first}((${field.type})field);
	        r.addField("${field.name}", (${field.type})field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: ${field.name} from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	[/#list]
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
             return "${.node.class.package}.${.node.class.name}";
         }
         
     }
}

