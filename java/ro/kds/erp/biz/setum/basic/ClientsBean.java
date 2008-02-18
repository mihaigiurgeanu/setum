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
 * Standard implementation of the Clients session bean.
 * You will have to extend this class to proivde code for the abstract
 * methods loadFormData, newForm and saveSaveFormData.
 */
public abstract class ClientsBean 
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
            logger = Monolog.getMonologFactory().getLogger("ro.kds.erp.biz.setum.basic.Clients");
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
    // Clients implementation
    // ------------------------------------------------------------------
    protected ClientsForm form;

    /**
     * Access to the form data.
     */
     public ClientsForm getForm() {
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
	logger.log(BasicLevel.DEBUG, "Loading Clients with id = " + loadId);
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
     * Create a new ClientsForm and initialize the 
     * <code>this.form</code> instance variable. Overwrite this method
     * if you want to provide other code for initializing the form bean.
     */
    protected void createNewFormBean() {
	form = new ClientsForm();
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
     * <code>ro.kds.erp.biz.setum.basic.Clients_calculatedFields</code>
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
			      ClientsForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);

		addFieldsToScript(script);
		script.run();

		getFieldsFromScript(script, r);

	    } catch (ScriptErrorException e) {
		logger.log(BasicLevel.ERROR, "Error executing rules script for Clients", e);
	    }
	}
	return r;
    }
	

    /**
     * Evaluates the validation script. The script loader loades the script
     * file corresponding to:
     *
     * <code>ro.kds.erp.biz.setum.basic.Clients_validation</code>
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
			      ClientsForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();

		// I don't add modified fields back to the response bean,
		// so any fields modifications made in the script will
		// be ignored.
	    } catch (ScriptErrorException e) {
		logger.log(BasicLevel.ERROR, "Error executing rules script for Clients", e);
	    }
	}
	return r;
    }

    public ResponseBean updateIsCompany(Integer isCompany) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getIsCompany();
	form.setIsCompany(isCompany);
	r.addRecord();
	r.addField("isCompany", isCompany); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".isCompany");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, Integer.class);
		script.setVar(FORM_VARNAME, form, ClientsForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the isCompany", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateFirstName(String firstName) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getFirstName();
	form.setFirstName(firstName);
	r.addRecord();
	r.addField("firstName", firstName); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".firstName");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, ClientsForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the firstName", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateLastName(String lastName) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getLastName();
	form.setLastName(lastName);
	r.addRecord();
	r.addField("lastName", lastName); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".lastName");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, ClientsForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the lastName", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateCompanyName(String companyName) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getCompanyName();
	form.setCompanyName(companyName);
	r.addRecord();
	r.addField("companyName", companyName); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".companyName");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, ClientsForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the companyName", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateAddress(String address) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getAddress();
	form.setAddress(address);
	r.addRecord();
	r.addField("address", address); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".address");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, ClientsForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the address", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updatePostalCode(String postalCode) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getPostalCode();
	form.setPostalCode(postalCode);
	r.addRecord();
	r.addField("postalCode", postalCode); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".postalCode");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, ClientsForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the postalCode", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateCity(String city) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getCity();
	form.setCity(city);
	r.addRecord();
	r.addField("city", city); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".city");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, ClientsForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the city", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateCountryCode(String countryCode) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getCountryCode();
	form.setCountryCode(countryCode);
	r.addRecord();
	r.addField("countryCode", countryCode); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".countryCode");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, ClientsForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the countryCode", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateCompanyCode(String companyCode) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getCompanyCode();
	form.setCompanyCode(companyCode);
	r.addRecord();
	r.addField("companyCode", companyCode); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".companyCode");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, ClientsForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the companyCode", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updatePhone(String phone) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getPhone();
	form.setPhone(phone);
	r.addRecord();
	r.addField("phone", phone); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".phone");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, ClientsForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the phone", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateIban(String iban) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getIban();
	form.setIban(iban);
	r.addRecord();
	r.addField("iban", iban); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".iban");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, ClientsForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the iban", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateBank(String bank) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getBank();
	form.setBank(bank);
	r.addRecord();
	r.addField("bank", bank); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".bank");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, ClientsForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the bank", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateComment(String comment) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getComment();
	form.setComment(comment);
	r.addRecord();
	r.addField("comment", comment); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".comment");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, ClientsForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the comment", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateContactFirstName(String contactFirstName) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getContactFirstName();
	form.setContactFirstName(contactFirstName);
	r.addRecord();
	r.addField("contactFirstName", contactFirstName); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".contactFirstName");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, ClientsForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the contactFirstName", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateContactLastName(String contactLastName) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getContactLastName();
	form.setContactLastName(contactLastName);
	r.addRecord();
	r.addField("contactLastName", contactLastName); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".contactLastName");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, ClientsForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the contactLastName", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateContactDepartment(String contactDepartment) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getContactDepartment();
	form.setContactDepartment(contactDepartment);
	r.addRecord();
	r.addField("contactDepartment", contactDepartment); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".contactDepartment");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, ClientsForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the contactDepartment", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateContactPhone(String contactPhone) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getContactPhone();
	form.setContactPhone(contactPhone);
	r.addRecord();
	r.addField("contactPhone", contactPhone); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".contactPhone");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, ClientsForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the contactPhone", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateContactMobile(String contactMobile) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getContactMobile();
	form.setContactMobile(contactMobile);
	r.addRecord();
	r.addField("contactMobile", contactMobile); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".contactMobile");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, ClientsForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the contactMobile", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateContactFax(String contactFax) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getContactFax();
	form.setContactFax(contactFax);
	r.addRecord();
	r.addField("contactFax", contactFax); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".contactFax");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, ClientsForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the contactFax", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateContactEmail(String contactEmail) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getContactEmail();
	form.setContactEmail(contactEmail);
	r.addRecord();
	r.addField("contactEmail", contactEmail); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".contactEmail");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, ClientsForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the contactEmail", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateContactTitle(String contactTitle) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getContactTitle();
	form.setContactTitle(contactTitle);
	r.addRecord();
	r.addField("contactTitle", contactTitle); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".contactTitle");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, ClientsForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the contactTitle", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateContactComment(String contactComment) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getContactComment();
	form.setContactComment(contactComment);
	r.addRecord();
	r.addField("contactComment", contactComment); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".contactComment");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, ClientsForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the contactComment", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }


    /**
     * Get the fields stored internaly and adds them to the response.
     */
    protected void copyFieldsToResponse(ResponseBean r) {
	r.addField("isCompany", form.getIsCompany());
	r.addField("firstName", form.getFirstName());
	r.addField("lastName", form.getLastName());
	r.addField("companyName", form.getCompanyName());
	r.addField("address", form.getAddress());
	r.addField("postalCode", form.getPostalCode());
	r.addField("city", form.getCity());
	r.addField("countryCode", form.getCountryCode());
	r.addField("companyCode", form.getCompanyCode());
	r.addField("phone", form.getPhone());
	r.addField("iban", form.getIban());
	r.addField("bank", form.getBank());
	r.addField("comment", form.getComment());
	r.addField("contactFirstName", form.getContactFirstName());
	r.addField("contactLastName", form.getContactLastName());
	r.addField("contactDepartment", form.getContactDepartment());
	r.addField("contactPhone", form.getContactPhone());
	r.addField("contactMobile", form.getContactMobile());
	r.addField("contactFax", form.getContactFax());
	r.addField("contactEmail", form.getContactEmail());
	r.addField("contactTitle", form.getContactTitle());
	r.addField("contactComment", form.getContactComment());
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
	try {
	    s.setVar("isCompany", form.getIsCompany(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: isCompany from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("firstName", form.getFirstName(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: firstName from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("lastName", form.getLastName(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: lastName from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("companyName", form.getCompanyName(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: companyName from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("address", form.getAddress(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: address from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("postalCode", form.getPostalCode(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: postalCode from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("city", form.getCity(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: city from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("countryCode", form.getCountryCode(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: countryCode from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("companyCode", form.getCompanyCode(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: companyCode from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("phone", form.getPhone(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: phone from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("iban", form.getIban(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: iban from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("bank", form.getBank(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: bank from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("comment", form.getComment(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: comment from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("contactFirstName", form.getContactFirstName(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: contactFirstName from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("contactLastName", form.getContactLastName(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: contactLastName from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("contactDepartment", form.getContactDepartment(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: contactDepartment from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("contactPhone", form.getContactPhone(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: contactPhone from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("contactMobile", form.getContactMobile(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: contactMobile from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("contactFax", form.getContactFax(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: contactFax from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("contactEmail", form.getContactEmail(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: contactEmail from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("contactTitle", form.getContactTitle(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: contactTitle from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("contactComment", form.getContactComment(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: contactComment from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
    }

    /**
     * Get the values of the fields from script variables. Writes in the
     * response bean any field that was modified.
     */
    protected void getFieldsFromScript(Script s, ResponseBean r) {
	Object field;
	try {
	    field = s.getVar("isCompany", Integer.class);
	    if(!field.equals(form.getIsCompany())) {
	        logger.log(BasicLevel.DEBUG, "Field isCompany modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setIsCompany((Integer)field);
	        r.addField("isCompany", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: isCompany from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("firstName", String.class);
	    if(!field.equals(form.getFirstName())) {
	        logger.log(BasicLevel.DEBUG, "Field firstName modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setFirstName((String)field);
	        r.addField("firstName", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: firstName from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("lastName", String.class);
	    if(!field.equals(form.getLastName())) {
	        logger.log(BasicLevel.DEBUG, "Field lastName modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setLastName((String)field);
	        r.addField("lastName", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: lastName from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("companyName", String.class);
	    if(!field.equals(form.getCompanyName())) {
	        logger.log(BasicLevel.DEBUG, "Field companyName modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setCompanyName((String)field);
	        r.addField("companyName", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: companyName from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("address", String.class);
	    if(!field.equals(form.getAddress())) {
	        logger.log(BasicLevel.DEBUG, "Field address modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setAddress((String)field);
	        r.addField("address", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: address from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("postalCode", String.class);
	    if(!field.equals(form.getPostalCode())) {
	        logger.log(BasicLevel.DEBUG, "Field postalCode modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setPostalCode((String)field);
	        r.addField("postalCode", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: postalCode from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("city", String.class);
	    if(!field.equals(form.getCity())) {
	        logger.log(BasicLevel.DEBUG, "Field city modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setCity((String)field);
	        r.addField("city", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: city from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("countryCode", String.class);
	    if(!field.equals(form.getCountryCode())) {
	        logger.log(BasicLevel.DEBUG, "Field countryCode modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setCountryCode((String)field);
	        r.addField("countryCode", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: countryCode from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("companyCode", String.class);
	    if(!field.equals(form.getCompanyCode())) {
	        logger.log(BasicLevel.DEBUG, "Field companyCode modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setCompanyCode((String)field);
	        r.addField("companyCode", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: companyCode from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("phone", String.class);
	    if(!field.equals(form.getPhone())) {
	        logger.log(BasicLevel.DEBUG, "Field phone modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setPhone((String)field);
	        r.addField("phone", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: phone from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("iban", String.class);
	    if(!field.equals(form.getIban())) {
	        logger.log(BasicLevel.DEBUG, "Field iban modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setIban((String)field);
	        r.addField("iban", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: iban from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("bank", String.class);
	    if(!field.equals(form.getBank())) {
	        logger.log(BasicLevel.DEBUG, "Field bank modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setBank((String)field);
	        r.addField("bank", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: bank from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("comment", String.class);
	    if(!field.equals(form.getComment())) {
	        logger.log(BasicLevel.DEBUG, "Field comment modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setComment((String)field);
	        r.addField("comment", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: comment from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("contactFirstName", String.class);
	    if(!field.equals(form.getContactFirstName())) {
	        logger.log(BasicLevel.DEBUG, "Field contactFirstName modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setContactFirstName((String)field);
	        r.addField("contactFirstName", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: contactFirstName from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("contactLastName", String.class);
	    if(!field.equals(form.getContactLastName())) {
	        logger.log(BasicLevel.DEBUG, "Field contactLastName modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setContactLastName((String)field);
	        r.addField("contactLastName", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: contactLastName from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("contactDepartment", String.class);
	    if(!field.equals(form.getContactDepartment())) {
	        logger.log(BasicLevel.DEBUG, "Field contactDepartment modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setContactDepartment((String)field);
	        r.addField("contactDepartment", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: contactDepartment from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("contactPhone", String.class);
	    if(!field.equals(form.getContactPhone())) {
	        logger.log(BasicLevel.DEBUG, "Field contactPhone modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setContactPhone((String)field);
	        r.addField("contactPhone", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: contactPhone from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("contactMobile", String.class);
	    if(!field.equals(form.getContactMobile())) {
	        logger.log(BasicLevel.DEBUG, "Field contactMobile modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setContactMobile((String)field);
	        r.addField("contactMobile", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: contactMobile from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("contactFax", String.class);
	    if(!field.equals(form.getContactFax())) {
	        logger.log(BasicLevel.DEBUG, "Field contactFax modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setContactFax((String)field);
	        r.addField("contactFax", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: contactFax from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("contactEmail", String.class);
	    if(!field.equals(form.getContactEmail())) {
	        logger.log(BasicLevel.DEBUG, "Field contactEmail modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setContactEmail((String)field);
	        r.addField("contactEmail", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: contactEmail from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("contactTitle", String.class);
	    if(!field.equals(form.getContactTitle())) {
	        logger.log(BasicLevel.DEBUG, "Field contactTitle modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setContactTitle((String)field);
	        r.addField("contactTitle", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: contactTitle from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("contactComment", String.class);
	    if(!field.equals(form.getContactComment())) {
	        logger.log(BasicLevel.DEBUG, "Field contactComment modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setContactComment((String)field);
	        r.addField("contactComment", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: contactComment from the script");
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
             return "ro.kds.erp.biz.setum.basic.Clients";
         }
         
     }

}

