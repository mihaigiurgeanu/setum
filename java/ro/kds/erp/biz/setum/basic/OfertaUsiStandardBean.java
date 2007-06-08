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
 * Standard implementation of the OfertaUsiStandard session bean.
 * You will have to extend this class to proivde code for the abstract
 * methods loadFormData, newForm and saveSaveFormData.
 */
public abstract class OfertaUsiStandardBean 
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
            logger = Monolog.getMonologFactory().getLogger("ro.kds.erp.biz.setum.basic.OfertaUsiStandard");
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
    // OfertaUsiStandard implementation
    // ------------------------------------------------------------------
    protected OfertaUsiStandardForm form;

    /**
     * Access to the form data.
     */
     public OfertaUsiStandardForm getForm() {
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
	logger.log(BasicLevel.DEBUG, "Loading OfertaUsiStandard with id = " + loadId);
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
     * Create a new OfertaUsiStandardForm and initialize the 
     * <code>this.form</code> instance variable. Overwrite this method
     * if you want to provide other code for initializing the form bean.
     */
    protected void createNewFormBean() {
	form = new OfertaUsiStandardForm();
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
     * <code>ro.kds.erp.biz.setum.basic.OfertaUsiStandard_calculatedFields</code>
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
			      OfertaUsiStandardForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);

		addFieldsToScript(script);
		script.run();

		getFieldsFromScript(script, r);

	    } catch (ScriptErrorException e) {
		logger.log(BasicLevel.ERROR, "Error executing rules script for OfertaUsiStandard", e);
	    }
	}
	return r;
    }
	

    /**
     * Evaluates the validation script. The script loader loades the script
     * file corresponding to:
     *
     * <code>ro.kds.erp.biz.setum.basic.OfertaUsiStandard_validation</code>
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
			      OfertaUsiStandardForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();

		// I don't add modified fields back to the response bean,
		// so any fields modifications made in the script will
		// be ignored.
	    } catch (ScriptErrorException e) {
		logger.log(BasicLevel.ERROR, "Error executing rules script for OfertaUsiStandard", e);
	    }
	}
	return r;
    }

    public ResponseBean updateNo(String no) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getNo();
	form.setNo(no);
	r.addRecord();
	r.addField("no", no); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".no");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, OfertaUsiStandardForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the no", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateDocDate(java.util.Date docDate) {
        ResponseBean r = new ResponseBean();
	java.util.Date oldVal = form.getDocDate();
	form.setDocDate(docDate);
	r.addRecord();
	r.addField("docDate", docDate); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".docDate");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, java.util.Date.class);
		script.setVar(FORM_VARNAME, form, OfertaUsiStandardForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the docDate", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateDateFrom(java.util.Date dateFrom) {
        ResponseBean r = new ResponseBean();
	java.util.Date oldVal = form.getDateFrom();
	form.setDateFrom(dateFrom);
	r.addRecord();
	r.addField("dateFrom", dateFrom); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".dateFrom");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, java.util.Date.class);
		script.setVar(FORM_VARNAME, form, OfertaUsiStandardForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the dateFrom", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateDateTo(java.util.Date dateTo) {
        ResponseBean r = new ResponseBean();
	java.util.Date oldVal = form.getDateTo();
	form.setDateTo(dateTo);
	r.addRecord();
	r.addField("dateTo", dateTo); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".dateTo");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, java.util.Date.class);
		script.setVar(FORM_VARNAME, form, OfertaUsiStandardForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the dateTo", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateDiscontinued(Boolean discontinued) {
        ResponseBean r = new ResponseBean();
	Boolean oldVal = form.getDiscontinued();
	form.setDiscontinued(discontinued);
	r.addRecord();
	r.addField("discontinued", discontinued); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".discontinued");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, Boolean.class);
		script.setVar(FORM_VARNAME, form, OfertaUsiStandardForm.class);
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
    public ResponseBean updatePeriod(Integer period) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getPeriod();
	form.setPeriod(period);
	r.addRecord();
	r.addField("period", period); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".period");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, Integer.class);
		script.setVar(FORM_VARNAME, form, OfertaUsiStandardForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the period", e);
           }
        }
	computeCalculatedFields(r);
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
		script.setVar(FORM_VARNAME, form, OfertaUsiStandardForm.class);
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
    public ResponseBean updateDescription(String description) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getDescription();
	form.setDescription(description);
	r.addRecord();
	r.addField("description", description); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".description");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, OfertaUsiStandardForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the description", e);
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
		script.setVar(FORM_VARNAME, form, OfertaUsiStandardForm.class);
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
    public ResponseBean updateVat(java.math.BigDecimal vat) {
        ResponseBean r = new ResponseBean();
	java.math.BigDecimal oldVal = form.getVat();
	form.setVat(vat);
	r.addRecord();
	r.addField("vat", vat); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".vat");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, java.math.BigDecimal.class);
		script.setVar(FORM_VARNAME, form, OfertaUsiStandardForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the vat", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updatePrice(java.math.BigDecimal price) {
        ResponseBean r = new ResponseBean();
	java.math.BigDecimal oldVal = form.getPrice();
	form.setPrice(price);
	r.addRecord();
	r.addField("price", price); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".price");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, java.math.BigDecimal.class);
		script.setVar(FORM_VARNAME, form, OfertaUsiStandardForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the price", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateVatPrice(java.math.BigDecimal vatPrice) {
        ResponseBean r = new ResponseBean();
	java.math.BigDecimal oldVal = form.getVatPrice();
	form.setVatPrice(vatPrice);
	r.addRecord();
	r.addField("vatPrice", vatPrice); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".vatPrice");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, java.math.BigDecimal.class);
		script.setVar(FORM_VARNAME, form, OfertaUsiStandardForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the vatPrice", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateRelativeGain(Double relativeGain) {
        ResponseBean r = new ResponseBean();
	Double oldVal = form.getRelativeGain();
	form.setRelativeGain(relativeGain);
	r.addRecord();
	r.addField("relativeGain", relativeGain); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".relativeGain");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, Double.class);
		script.setVar(FORM_VARNAME, form, OfertaUsiStandardForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the relativeGain", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateAbsoluteGain(java.math.BigDecimal absoluteGain) {
        ResponseBean r = new ResponseBean();
	java.math.BigDecimal oldVal = form.getAbsoluteGain();
	form.setAbsoluteGain(absoluteGain);
	r.addRecord();
	r.addField("absoluteGain", absoluteGain); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".absoluteGain");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, java.math.BigDecimal.class);
		script.setVar(FORM_VARNAME, form, OfertaUsiStandardForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the absoluteGain", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateProductCategory(String productCategory) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getProductCategory();
	form.setProductCategory(productCategory);
	r.addRecord();
	r.addField("productCategory", productCategory); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".productCategory");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, OfertaUsiStandardForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the productCategory", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateProductCode(String productCode) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getProductCode();
	form.setProductCode(productCode);
	r.addRecord();
	r.addField("productCode", productCode); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".productCode");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, OfertaUsiStandardForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the productCode", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateProductName(String productName) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getProductName();
	form.setProductName(productName);
	r.addRecord();
	r.addField("productName", productName); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".productName");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, OfertaUsiStandardForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the productName", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateUsa(String usa) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getUsa();
	form.setUsa(usa);
	r.addRecord();
	r.addField("usa", usa); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".usa");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, OfertaUsiStandardForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the usa", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateUsaCode(String usaCode) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getUsaCode();
	form.setUsaCode(usaCode);
	r.addRecord();
	r.addField("usaCode", usaCode); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".usaCode");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, OfertaUsiStandardForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the usaCode", e);
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
		script.setVar(FORM_VARNAME, form, OfertaUsiStandardForm.class);
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
    public ResponseBean updateUsaDescription(String usaDescription) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getUsaDescription();
	form.setUsaDescription(usaDescription);
	r.addRecord();
	r.addField("usaDescription", usaDescription); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".usaDescription");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, OfertaUsiStandardForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the usaDescription", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateBroasca(String broasca) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getBroasca();
	form.setBroasca(broasca);
	r.addRecord();
	r.addField("broasca", broasca); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".broasca");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, OfertaUsiStandardForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the broasca", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateCilindru(String cilindru) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getCilindru();
	form.setCilindru(cilindru);
	r.addRecord();
	r.addField("cilindru", cilindru); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".cilindru");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, OfertaUsiStandardForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the cilindru", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateSild(String sild) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getSild();
	form.setSild(sild);
	r.addRecord();
	r.addField("sild", sild); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".sild");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, OfertaUsiStandardForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the sild", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateYalla(String yalla) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getYalla();
	form.setYalla(yalla);
	r.addRecord();
	r.addField("yalla", yalla); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".yalla");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, OfertaUsiStandardForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the yalla", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateVizor(String vizor) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getVizor();
	form.setVizor(vizor);
	r.addRecord();
	r.addField("vizor", vizor); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".vizor");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, OfertaUsiStandardForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the vizor", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateEntryPrice(java.math.BigDecimal entryPrice) {
        ResponseBean r = new ResponseBean();
	java.math.BigDecimal oldVal = form.getEntryPrice();
	form.setEntryPrice(entryPrice);
	r.addRecord();
	r.addField("entryPrice", entryPrice); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".entryPrice");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, java.math.BigDecimal.class);
		script.setVar(FORM_VARNAME, form, OfertaUsiStandardForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the entryPrice", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateSellPrice(java.math.BigDecimal sellPrice) {
        ResponseBean r = new ResponseBean();
	java.math.BigDecimal oldVal = form.getSellPrice();
	form.setSellPrice(sellPrice);
	r.addRecord();
	r.addField("sellPrice", sellPrice); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".sellPrice");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, java.math.BigDecimal.class);
		script.setVar(FORM_VARNAME, form, OfertaUsiStandardForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the sellPrice", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateSelectionCode(String selectionCode) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getSelectionCode();
	form.setSelectionCode(selectionCode);
	r.addRecord();
	r.addField("selectionCode", selectionCode); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".selectionCode");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, OfertaUsiStandardForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the selectionCode", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateFilterUsa(String FilterUsa) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getFilterUsa();
	form.setFilterUsa(FilterUsa);
	r.addRecord();
	r.addField("FilterUsa", FilterUsa); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".FilterUsa");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, OfertaUsiStandardForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the FilterUsa", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateFilterBroasca(String FilterBroasca) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getFilterBroasca();
	form.setFilterBroasca(FilterBroasca);
	r.addRecord();
	r.addField("FilterBroasca", FilterBroasca); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".FilterBroasca");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, OfertaUsiStandardForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the FilterBroasca", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateFilterCilindru(String FilterCilindru) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getFilterCilindru();
	form.setFilterCilindru(FilterCilindru);
	r.addRecord();
	r.addField("FilterCilindru", FilterCilindru); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".FilterCilindru");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, OfertaUsiStandardForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the FilterCilindru", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateFilterSild(String FilterSild) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getFilterSild();
	form.setFilterSild(FilterSild);
	r.addRecord();
	r.addField("FilterSild", FilterSild); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".FilterSild");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, OfertaUsiStandardForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the FilterSild", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateFilterYalla(String FilterYalla) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getFilterYalla();
	form.setFilterYalla(FilterYalla);
	r.addRecord();
	r.addField("FilterYalla", FilterYalla); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".FilterYalla");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, OfertaUsiStandardForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the FilterYalla", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateFilterVizor(String FilterVizor) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getFilterVizor();
	form.setFilterVizor(FilterVizor);
	r.addRecord();
	r.addField("FilterVizor", FilterVizor); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".FilterVizor");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, OfertaUsiStandardForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the FilterVizor", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }


    /**
     * Get the fields stored internaly and adds them to the response.
     */
    protected void copyFieldsToResponse(ResponseBean r) {
	r.addField("no", form.getNo());
	r.addField("docDate", form.getDocDate());
	r.addField("dateFrom", form.getDateFrom());
	r.addField("dateTo", form.getDateTo());
	r.addField("discontinued", form.getDiscontinued());
	r.addField("period", form.getPeriod());
	r.addField("name", form.getName());
	r.addField("description", form.getDescription());
	r.addField("comment", form.getComment());
	r.addField("vat", form.getVat());
	r.addField("price", form.getPrice());
	r.addField("vatPrice", form.getVatPrice());
	r.addField("relativeGain", form.getRelativeGain());
	r.addField("absoluteGain", form.getAbsoluteGain());
	r.addField("productCategory", form.getProductCategory());
	r.addField("productCode", form.getProductCode());
	r.addField("productName", form.getProductName());
	r.addField("usa", form.getUsa());
	r.addField("usaCode", form.getUsaCode());
	r.addField("usaId", form.getUsaId());
	r.addField("usaDescription", form.getUsaDescription());
	r.addField("broasca", form.getBroasca());
	r.addField("cilindru", form.getCilindru());
	r.addField("sild", form.getSild());
	r.addField("yalla", form.getYalla());
	r.addField("vizor", form.getVizor());
	r.addField("entryPrice", form.getEntryPrice());
	r.addField("sellPrice", form.getSellPrice());
	r.addField("selectionCode", form.getSelectionCode());
	r.addField("FilterUsa", form.getFilterUsa());
	r.addField("FilterBroasca", form.getFilterBroasca());
	r.addField("FilterCilindru", form.getFilterCilindru());
	r.addField("FilterSild", form.getFilterSild());
	r.addField("FilterYalla", form.getFilterYalla());
	r.addField("FilterVizor", form.getFilterVizor());
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
	    s.setVar("no", form.getNo(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: no from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("docDate", form.getDocDate(), java.util.Date.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: docDate from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("dateFrom", form.getDateFrom(), java.util.Date.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: dateFrom from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("dateTo", form.getDateTo(), java.util.Date.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: dateTo from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("discontinued", form.getDiscontinued(), Boolean.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: discontinued from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("period", form.getPeriod(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: period from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("name", form.getName(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: name from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("description", form.getDescription(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: description from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("comment", form.getComment(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: comment from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("vat", form.getVat(), java.math.BigDecimal.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: vat from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("price", form.getPrice(), java.math.BigDecimal.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: price from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("vatPrice", form.getVatPrice(), java.math.BigDecimal.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: vatPrice from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("relativeGain", form.getRelativeGain(), Double.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: relativeGain from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("absoluteGain", form.getAbsoluteGain(), java.math.BigDecimal.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: absoluteGain from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("productCategory", form.getProductCategory(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: productCategory from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("productCode", form.getProductCode(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: productCode from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("productName", form.getProductName(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: productName from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("usa", form.getUsa(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: usa from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("usaCode", form.getUsaCode(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: usaCode from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("usaId", form.getUsaId(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: usaId from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("usaDescription", form.getUsaDescription(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: usaDescription from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("broasca", form.getBroasca(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: broasca from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("cilindru", form.getCilindru(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: cilindru from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("sild", form.getSild(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: sild from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("yalla", form.getYalla(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: yalla from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("vizor", form.getVizor(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: vizor from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("entryPrice", form.getEntryPrice(), java.math.BigDecimal.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: entryPrice from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("sellPrice", form.getSellPrice(), java.math.BigDecimal.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: sellPrice from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("selectionCode", form.getSelectionCode(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: selectionCode from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("FilterUsa", form.getFilterUsa(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: FilterUsa from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("FilterBroasca", form.getFilterBroasca(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: FilterBroasca from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("FilterCilindru", form.getFilterCilindru(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: FilterCilindru from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("FilterSild", form.getFilterSild(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: FilterSild from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("FilterYalla", form.getFilterYalla(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: FilterYalla from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("FilterVizor", form.getFilterVizor(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: FilterVizor from the script");
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
	    field = s.getVar("no", String.class);
	    if(!field.equals(form.getNo())) {
	        logger.log(BasicLevel.DEBUG, "Field no modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setNo((String)field);
	        r.addField("no", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: no from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("docDate", java.util.Date.class);
	    if(!field.equals(form.getDocDate())) {
	        logger.log(BasicLevel.DEBUG, "Field docDate modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setDocDate((java.util.Date)field);
	        r.addField("docDate", (java.util.Date)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: docDate from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("dateFrom", java.util.Date.class);
	    if(!field.equals(form.getDateFrom())) {
	        logger.log(BasicLevel.DEBUG, "Field dateFrom modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setDateFrom((java.util.Date)field);
	        r.addField("dateFrom", (java.util.Date)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: dateFrom from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("dateTo", java.util.Date.class);
	    if(!field.equals(form.getDateTo())) {
	        logger.log(BasicLevel.DEBUG, "Field dateTo modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setDateTo((java.util.Date)field);
	        r.addField("dateTo", (java.util.Date)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: dateTo from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("discontinued", Boolean.class);
	    if(!field.equals(form.getDiscontinued())) {
	        logger.log(BasicLevel.DEBUG, "Field discontinued modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setDiscontinued((Boolean)field);
	        r.addField("discontinued", (Boolean)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: discontinued from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("period", Integer.class);
	    if(!field.equals(form.getPeriod())) {
	        logger.log(BasicLevel.DEBUG, "Field period modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setPeriod((Integer)field);
	        r.addField("period", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: period from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
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
	    field = s.getVar("description", String.class);
	    if(!field.equals(form.getDescription())) {
	        logger.log(BasicLevel.DEBUG, "Field description modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setDescription((String)field);
	        r.addField("description", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: description from the script");
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
	    field = s.getVar("vat", java.math.BigDecimal.class);
	    if(!field.equals(form.getVat())) {
	        logger.log(BasicLevel.DEBUG, "Field vat modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setVat((java.math.BigDecimal)field);
	        r.addField("vat", (java.math.BigDecimal)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: vat from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("price", java.math.BigDecimal.class);
	    if(!field.equals(form.getPrice())) {
	        logger.log(BasicLevel.DEBUG, "Field price modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setPrice((java.math.BigDecimal)field);
	        r.addField("price", (java.math.BigDecimal)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: price from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("vatPrice", java.math.BigDecimal.class);
	    if(!field.equals(form.getVatPrice())) {
	        logger.log(BasicLevel.DEBUG, "Field vatPrice modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setVatPrice((java.math.BigDecimal)field);
	        r.addField("vatPrice", (java.math.BigDecimal)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: vatPrice from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("relativeGain", Double.class);
	    if(!field.equals(form.getRelativeGain())) {
	        logger.log(BasicLevel.DEBUG, "Field relativeGain modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setRelativeGain((Double)field);
	        r.addField("relativeGain", (Double)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: relativeGain from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("absoluteGain", java.math.BigDecimal.class);
	    if(!field.equals(form.getAbsoluteGain())) {
	        logger.log(BasicLevel.DEBUG, "Field absoluteGain modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setAbsoluteGain((java.math.BigDecimal)field);
	        r.addField("absoluteGain", (java.math.BigDecimal)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: absoluteGain from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("productCategory", String.class);
	    if(!field.equals(form.getProductCategory())) {
	        logger.log(BasicLevel.DEBUG, "Field productCategory modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setProductCategory((String)field);
	        r.addField("productCategory", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: productCategory from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("productCode", String.class);
	    if(!field.equals(form.getProductCode())) {
	        logger.log(BasicLevel.DEBUG, "Field productCode modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setProductCode((String)field);
	        r.addField("productCode", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: productCode from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("productName", String.class);
	    if(!field.equals(form.getProductName())) {
	        logger.log(BasicLevel.DEBUG, "Field productName modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setProductName((String)field);
	        r.addField("productName", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: productName from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("usa", String.class);
	    if(!field.equals(form.getUsa())) {
	        logger.log(BasicLevel.DEBUG, "Field usa modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setUsa((String)field);
	        r.addField("usa", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: usa from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("usaCode", String.class);
	    if(!field.equals(form.getUsaCode())) {
	        logger.log(BasicLevel.DEBUG, "Field usaCode modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setUsaCode((String)field);
	        r.addField("usaCode", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: usaCode from the script");
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
	    field = s.getVar("usaDescription", String.class);
	    if(!field.equals(form.getUsaDescription())) {
	        logger.log(BasicLevel.DEBUG, "Field usaDescription modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setUsaDescription((String)field);
	        r.addField("usaDescription", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: usaDescription from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("broasca", String.class);
	    if(!field.equals(form.getBroasca())) {
	        logger.log(BasicLevel.DEBUG, "Field broasca modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setBroasca((String)field);
	        r.addField("broasca", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: broasca from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("cilindru", String.class);
	    if(!field.equals(form.getCilindru())) {
	        logger.log(BasicLevel.DEBUG, "Field cilindru modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setCilindru((String)field);
	        r.addField("cilindru", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: cilindru from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("sild", String.class);
	    if(!field.equals(form.getSild())) {
	        logger.log(BasicLevel.DEBUG, "Field sild modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setSild((String)field);
	        r.addField("sild", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: sild from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("yalla", String.class);
	    if(!field.equals(form.getYalla())) {
	        logger.log(BasicLevel.DEBUG, "Field yalla modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setYalla((String)field);
	        r.addField("yalla", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: yalla from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("vizor", String.class);
	    if(!field.equals(form.getVizor())) {
	        logger.log(BasicLevel.DEBUG, "Field vizor modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setVizor((String)field);
	        r.addField("vizor", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: vizor from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("entryPrice", java.math.BigDecimal.class);
	    if(!field.equals(form.getEntryPrice())) {
	        logger.log(BasicLevel.DEBUG, "Field entryPrice modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setEntryPrice((java.math.BigDecimal)field);
	        r.addField("entryPrice", (java.math.BigDecimal)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: entryPrice from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("sellPrice", java.math.BigDecimal.class);
	    if(!field.equals(form.getSellPrice())) {
	        logger.log(BasicLevel.DEBUG, "Field sellPrice modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setSellPrice((java.math.BigDecimal)field);
	        r.addField("sellPrice", (java.math.BigDecimal)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: sellPrice from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("selectionCode", String.class);
	    if(!field.equals(form.getSelectionCode())) {
	        logger.log(BasicLevel.DEBUG, "Field selectionCode modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setSelectionCode((String)field);
	        r.addField("selectionCode", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: selectionCode from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("FilterUsa", String.class);
	    if(!field.equals(form.getFilterUsa())) {
	        logger.log(BasicLevel.DEBUG, "Field FilterUsa modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setFilterUsa((String)field);
	        r.addField("FilterUsa", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: FilterUsa from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("FilterBroasca", String.class);
	    if(!field.equals(form.getFilterBroasca())) {
	        logger.log(BasicLevel.DEBUG, "Field FilterBroasca modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setFilterBroasca((String)field);
	        r.addField("FilterBroasca", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: FilterBroasca from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("FilterCilindru", String.class);
	    if(!field.equals(form.getFilterCilindru())) {
	        logger.log(BasicLevel.DEBUG, "Field FilterCilindru modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setFilterCilindru((String)field);
	        r.addField("FilterCilindru", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: FilterCilindru from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("FilterSild", String.class);
	    if(!field.equals(form.getFilterSild())) {
	        logger.log(BasicLevel.DEBUG, "Field FilterSild modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setFilterSild((String)field);
	        r.addField("FilterSild", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: FilterSild from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("FilterYalla", String.class);
	    if(!field.equals(form.getFilterYalla())) {
	        logger.log(BasicLevel.DEBUG, "Field FilterYalla modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setFilterYalla((String)field);
	        r.addField("FilterYalla", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: FilterYalla from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("FilterVizor", String.class);
	    if(!field.equals(form.getFilterVizor())) {
	        logger.log(BasicLevel.DEBUG, "Field FilterVizor modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setFilterVizor((String)field);
	        r.addField("FilterVizor", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: FilterVizor from the script");
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
             return "ro.kds.erp.biz.setum.basic.OfertaUsiStandard";
         }
         
     }
}

