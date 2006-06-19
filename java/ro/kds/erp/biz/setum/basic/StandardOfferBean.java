package ro.kds.erp.biz.setum.basic;

import javax.ejb.SessionBean;
import javax.ejb.SessionSynchronization;
import javax.ejb.SessionContext;
import org.objectweb.util.monolog.Monolog;
import org.objectweb.util.monolog.api.BasicLevel;
import org.objectweb.util.monolog.api.Logger;
//import org.objectweb.jonas.common.Log;
import javax.ejb.CreateException;
import ro.kds.erp.biz.ResponseBean;
import javax.naming.InitialContext;
import javax.ejb.FinderException;
import java.util.Collection;
import java.util.Iterator;
import ro.kds.erp.scripting.Script;
import ro.kds.erp.scripting.TclFileScript;
import ro.kds.erp.scripting.ScriptErrorException;
import javax.naming.*;

/**
 * Standard implementation of the StandardOffer session bean.
 * You will have to extend this class to proivde code for the abstract
 * methods loadFormData, newForm and saveSaveFormData.
 */
public abstract class StandardOfferBean 
	implements SessionBean {

    static protected Logger logger = null;
    protected SessionContext ejbContext;

    protected Integer id;
    final static String FORM_VARNAME = "form";
    final static String RESPONSE_VARNAME = "response";
    final static String LOGIC_VARNAME = "logic";
    final static String OLDVAL_VARNAME = "oldVal";

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
            logger = Monolog.getMonologFactory().getLogger("ro.kds.erp.biz.setum.basic.StandardOffer");
        }
        logger.log(BasicLevel.DEBUG, "");
        ejbContext = ctx;
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
    // StandardOffer implementation
    // ------------------------------------------------------------------
    protected StandardOfferForm form;
    
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
	logger.log(BasicLevel.DEBUG, "Loading StandardOffer with id = " + loadId);
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
     * Create a new StandardOfferForm and initialize the 
     * <code>this.form</code> instance variable. Overwrite this method
     * if you want to provide other code for initializing the form bean.
     */
    protected void createNewFormBean() {
	form = new StandardOfferForm();
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
     * <code>ro.kds.erp.biz.setum.basic.StandardOffer_calculatedFields</code>
     * 
     * The values of the modified fields are copied back into the form and
     * also in the ResponseBean passed to this method as parameter.
     *
     * @param r is the ResponseBean that will be returned with the values
     * of modified fields added to it. Before pass it to this method,
     * make sure that r.addRecord() has been called. This parameter can
     * be null in wich case a new ResponseBean will be created.
     *
     * @returns a ResponseBean containing the modified field values.
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
			      StandardOfferForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);

		addFieldsToScript(script);
		script.run();

		getFieldsFromScript(script, r);

	    } catch (ScriptErrorException e) {
		logger.log(BasicLevel.ERROR, "Error executing rules script for StandardOffer", e);
	    }
	}
	return r;
    }
	

    /**
     * Evaluates the validation script. The script loader loades the script
     * file corresponding to:
     *
     * <code>ro.kds.erp.biz.setum.basic.StandardOffer_validation</code>
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
			      StandardOfferForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		addFieldsToScript(script);
		script.run();

		// I don't add modified fields back to the response bean,
		// so any fields modifications made in the script will
		// be ignored.
	    } catch (ScriptErrorException e) {
		logger.log(BasicLevel.ERROR, "Error executing rules script for StandardOffer", e);
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
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, StandardOfferForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
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
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, java.util.Date.class);
		script.setVar(FORM_VARNAME, form, StandardOfferForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
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
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, java.util.Date.class);
		script.setVar(FORM_VARNAME, form, StandardOfferForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
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
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, java.util.Date.class);
		script.setVar(FORM_VARNAME, form, StandardOfferForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
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
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, Boolean.class);
		script.setVar(FORM_VARNAME, form, StandardOfferForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
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
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, Integer.class);
		script.setVar(FORM_VARNAME, form, StandardOfferForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
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
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, StandardOfferForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
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
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, StandardOfferForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
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
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, StandardOfferForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
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
    public ResponseBean updateProductId(Integer productId) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getProductId();
	form.setProductId(productId);
	r.addRecord();
	r.addField("productId", productId); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".productId");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, Integer.class);
		script.setVar(FORM_VARNAME, form, StandardOfferForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the productId", e);
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
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, java.math.BigDecimal.class);
		script.setVar(FORM_VARNAME, form, StandardOfferForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
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
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, java.math.BigDecimal.class);
		script.setVar(FORM_VARNAME, form, StandardOfferForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
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
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, Double.class);
		script.setVar(FORM_VARNAME, form, StandardOfferForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
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
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, java.math.BigDecimal.class);
		script.setVar(FORM_VARNAME, form, StandardOfferForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
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
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, StandardOfferForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
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
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, StandardOfferForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
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
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, StandardOfferForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
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
    public ResponseBean updateEntryPrice(java.math.BigDecimal entryPrice) {
        ResponseBean r = new ResponseBean();
	java.math.BigDecimal oldVal = form.getEntryPrice();
	form.setEntryPrice(entryPrice);
	r.addRecord();
	r.addField("entryPrice", entryPrice); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".entryPrice");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, java.math.BigDecimal.class);
		script.setVar(FORM_VARNAME, form, StandardOfferForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
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
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, java.math.BigDecimal.class);
		script.setVar(FORM_VARNAME, form, StandardOfferForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
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
	r.addField("productId", form.getProductId());
	r.addField("price", form.getPrice());
	r.addField("vatPrice", form.getVatPrice());
	r.addField("relativeGain", form.getRelativeGain());
	r.addField("absoluteGain", form.getAbsoluteGain());
	r.addField("productCategory", form.getProductCategory());
	r.addField("productCode", form.getProductCode());
	r.addField("productName", form.getProductName());
	r.addField("entryPrice", form.getEntryPrice());
	r.addField("sellPrice", form.getSellPrice());
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
	    s.setVar("productId", form.getProductId(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: productId from the script");
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
	    field = s.getVar("productId", Integer.class);
	    if(!field.equals(form.getProductId())) {
	        logger.log(BasicLevel.DEBUG, "Field productId modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setProductId((Integer)field);
	        r.addField("productId", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: productId from the script");
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
             logger.log(BasicLevel.WARN, "The value for script prefix can not be read from environment");
             logger.log(BasicLevel.DEBUG, e);
             return "ro.kds.erp.biz.setum.basic.StandardOffer";
         }
         
     }
}

