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
 * Standard implementation of the GenericProducts session bean.
 * You will have to extend this class to proivde code for the abstract
 * methods loadFormData, newForm and saveSaveFormData.
 */
public abstract class GenericProductsBean 
	implements SessionBean {

    static protected Logger logger = null;
    protected SessionContext ejbContext;

    /**
     * Stores the reference to the <code>ServiceFactory</code>
     * to be passed to script execution.
     */
    protected ServiceFactoryLocal factory;

    protected Integer id;
    protected Integer productId;
    protected Integer attributeId;
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
            logger = Monolog.getMonologFactory().getLogger("ro.kds.erp.biz.basic.GenericProducts");
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
    // GenericProducts implementation
    // ------------------------------------------------------------------
    protected GenericProductsForm form;

    /**
     * Access to the form data.
     */
     public GenericProductsForm getForm() {
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
        productId = null;
        attributeId = null;
	
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
	logger.log(BasicLevel.DEBUG, "Loading GenericProducts with id = " + loadId);
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
     * Create a new GenericProductsForm and initialize the 
     * <code>this.form</code> instance variable. Overwrite this method
     * if you want to provide other code for initializing the form bean.
     */
    protected void createNewFormBean() {
	form = new GenericProductsForm();
    }

    /**
     * Save the current record into the database.
     */
    public abstract ResponseBean saveFormData();

    public ResponseBean newProductData() {
        initProductFields();
        ResponseBean r = new ResponseBean();
        productId = null;
        computeCalculatedFields(null);

        r.addRecord();
        copyFieldsToResponse(r);
        return r;
    }

    protected abstract void initProductFields();

    /**
     * Load the data in the subform product
     */
    public ResponseBean loadProductData(Integer loadId) throws FinderException {

	logger.log(BasicLevel.DEBUG, "Loading subform Product for id = " + loadId);
	initProductFields();
	productId = loadId;

	ResponseBean r = loadProductFields();
	computeCalculatedFields(null);
	r.addRecord();
	copyFieldsToResponse(r);
	return r;
    }

    /**
     * Loads the fields corresponding to the subform product
     * from the database.
     */
    protected abstract ResponseBean loadProductFields() throws FinderException;

    /**
     * Save the current subform record into the database.
     */
    public abstract ResponseBean saveProductData();

    public ResponseBean newAttributeData() {
        initAttributeFields();
        ResponseBean r = new ResponseBean();
        attributeId = null;
        computeCalculatedFields(null);

        r.addRecord();
        copyFieldsToResponse(r);
        return r;
    }

    protected abstract void initAttributeFields();

    /**
     * Load the data in the subform attribute
     */
    public ResponseBean loadAttributeData(Integer loadId) throws FinderException {

	logger.log(BasicLevel.DEBUG, "Loading subform Attribute for id = " + loadId);
	initAttributeFields();
	attributeId = loadId;

	ResponseBean r = loadAttributeFields();
	computeCalculatedFields(null);
	r.addRecord();
	copyFieldsToResponse(r);
	return r;
    }

    /**
     * Loads the fields corresponding to the subform attribute
     * from the database.
     */
    protected abstract ResponseBean loadAttributeFields() throws FinderException;

    /**
     * Save the current subform record into the database.
     */
    public abstract ResponseBean saveAttributeData();



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
     * <code>ro.kds.erp.biz.basic.GenericProducts_calculatedFields</code>
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
			      GenericProductsForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);

		addFieldsToScript(script);
		script.run();

		getFieldsFromScript(script, r);

	    } catch (ScriptErrorException e) {
		logger.log(BasicLevel.ERROR, "Error executing rules script for GenericProducts", e);
	    }
	}
	return r;
    }
	

    /**
     * Evaluates the validation script. The script loader loades the script
     * file corresponding to:
     *
     * <code>ro.kds.erp.biz.basic.GenericProducts_validation</code>
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
			      GenericProductsForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();

		// I don't add modified fields back to the response bean,
		// so any fields modifications made in the script will
		// be ignored.
	    } catch (ScriptErrorException e) {
		logger.log(BasicLevel.ERROR, "Error executing rules script for GenericProducts", e);
	    }
	}
	return r;
    }

    public ResponseBean updateCategoryId(Integer categoryId) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getCategoryId();
	form.setCategoryId(categoryId);
	r.addRecord();
	r.addField("categoryId", categoryId); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".categoryId");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, Integer.class);
		script.setVar(FORM_VARNAME, form, GenericProductsForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the categoryId", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateCategoryName(String categoryName) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getCategoryName();
	form.setCategoryName(categoryName);
	r.addRecord();
	r.addField("categoryName", categoryName); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".categoryName");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, GenericProductsForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the categoryName", e);
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
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, Integer.class);
		script.setVar(FORM_VARNAME, form, GenericProductsForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
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
		script.setVar(FORM_VARNAME, form, GenericProductsForm.class);
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
    public ResponseBean updateProductDescription(String productDescription) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getProductDescription();
	form.setProductDescription(productDescription);
	r.addRecord();
	r.addField("productDescription", productDescription); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".productDescription");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, GenericProductsForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the productDescription", e);
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
		script.setVar(FORM_VARNAME, form, GenericProductsForm.class);
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
    public ResponseBean updateProductEntryPrice(java.math.BigDecimal productEntryPrice) {
        ResponseBean r = new ResponseBean();
	java.math.BigDecimal oldVal = form.getProductEntryPrice();
	form.setProductEntryPrice(productEntryPrice);
	r.addRecord();
	r.addField("productEntryPrice", productEntryPrice); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".productEntryPrice");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, java.math.BigDecimal.class);
		script.setVar(FORM_VARNAME, form, GenericProductsForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the productEntryPrice", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateProductSellPrice(java.math.BigDecimal productSellPrice) {
        ResponseBean r = new ResponseBean();
	java.math.BigDecimal oldVal = form.getProductSellPrice();
	form.setProductSellPrice(productSellPrice);
	r.addRecord();
	r.addField("productSellPrice", productSellPrice); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".productSellPrice");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, java.math.BigDecimal.class);
		script.setVar(FORM_VARNAME, form, GenericProductsForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the productSellPrice", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateProductPrice1(java.math.BigDecimal productPrice1) {
        ResponseBean r = new ResponseBean();
	java.math.BigDecimal oldVal = form.getProductPrice1();
	form.setProductPrice1(productPrice1);
	r.addRecord();
	r.addField("productPrice1", productPrice1); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".productPrice1");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, java.math.BigDecimal.class);
		script.setVar(FORM_VARNAME, form, GenericProductsForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the productPrice1", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateProductPrice2(java.math.BigDecimal productPrice2) {
        ResponseBean r = new ResponseBean();
	java.math.BigDecimal oldVal = form.getProductPrice2();
	form.setProductPrice2(productPrice2);
	r.addRecord();
	r.addField("productPrice2", productPrice2); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".productPrice2");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, java.math.BigDecimal.class);
		script.setVar(FORM_VARNAME, form, GenericProductsForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the productPrice2", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateProductPrice3(java.math.BigDecimal productPrice3) {
        ResponseBean r = new ResponseBean();
	java.math.BigDecimal oldVal = form.getProductPrice3();
	form.setProductPrice3(productPrice3);
	r.addRecord();
	r.addField("productPrice3", productPrice3); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".productPrice3");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, java.math.BigDecimal.class);
		script.setVar(FORM_VARNAME, form, GenericProductsForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the productPrice3", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateProductPrice4(java.math.BigDecimal productPrice4) {
        ResponseBean r = new ResponseBean();
	java.math.BigDecimal oldVal = form.getProductPrice4();
	form.setProductPrice4(productPrice4);
	r.addRecord();
	r.addField("productPrice4", productPrice4); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".productPrice4");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, java.math.BigDecimal.class);
		script.setVar(FORM_VARNAME, form, GenericProductsForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the productPrice4", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateProductPrice5(java.math.BigDecimal productPrice5) {
        ResponseBean r = new ResponseBean();
	java.math.BigDecimal oldVal = form.getProductPrice5();
	form.setProductPrice5(productPrice5);
	r.addRecord();
	r.addField("productPrice5", productPrice5); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".productPrice5");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, java.math.BigDecimal.class);
		script.setVar(FORM_VARNAME, form, GenericProductsForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the productPrice5", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateAttrId(Integer attrId) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getAttrId();
	form.setAttrId(attrId);
	r.addRecord();
	r.addField("attrId", attrId); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".attrId");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, Integer.class);
		script.setVar(FORM_VARNAME, form, GenericProductsForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the attrId", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateAttrName(String attrName) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getAttrName();
	form.setAttrName(attrName);
	r.addRecord();
	r.addField("attrName", attrName); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".attrName");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, GenericProductsForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the attrName", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateAttrString(String attrString) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getAttrString();
	form.setAttrString(attrString);
	r.addRecord();
	r.addField("attrString", attrString); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".attrString");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, GenericProductsForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the attrString", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateAttrInt(Integer attrInt) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getAttrInt();
	form.setAttrInt(attrInt);
	r.addRecord();
	r.addField("attrInt", attrInt); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".attrInt");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, Integer.class);
		script.setVar(FORM_VARNAME, form, GenericProductsForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the attrInt", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateAttrDecimal(java.math.BigDecimal attrDecimal) {
        ResponseBean r = new ResponseBean();
	java.math.BigDecimal oldVal = form.getAttrDecimal();
	form.setAttrDecimal(attrDecimal);
	r.addRecord();
	r.addField("attrDecimal", attrDecimal); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".attrDecimal");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, java.math.BigDecimal.class);
		script.setVar(FORM_VARNAME, form, GenericProductsForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the attrDecimal", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateAttrDouble(Double attrDouble) {
        ResponseBean r = new ResponseBean();
	Double oldVal = form.getAttrDouble();
	form.setAttrDouble(attrDouble);
	r.addRecord();
	r.addField("attrDouble", attrDouble); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".attrDouble");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, Double.class);
		script.setVar(FORM_VARNAME, form, GenericProductsForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the attrDouble", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }


    /**
     * Get the fields stored internaly and adds them to the response.
     */
    protected void copyFieldsToResponse(ResponseBean r) {
	r.addField("categoryId", form.getCategoryId());
	r.addField("categoryName", form.getCategoryName());
	r.addField("productId", form.getProductId());
	r.addField("productName", form.getProductName());
	r.addField("productDescription", form.getProductDescription());
	r.addField("productCode", form.getProductCode());
	r.addField("productEntryPrice", form.getProductEntryPrice());
	r.addField("productSellPrice", form.getProductSellPrice());
	r.addField("productPrice1", form.getProductPrice1());
	r.addField("productPrice2", form.getProductPrice2());
	r.addField("productPrice3", form.getProductPrice3());
	r.addField("productPrice4", form.getProductPrice4());
	r.addField("productPrice5", form.getProductPrice5());
	r.addField("attrId", form.getAttrId());
	r.addField("attrName", form.getAttrName());
	r.addField("attrString", form.getAttrString());
	r.addField("attrInt", form.getAttrInt());
	r.addField("attrDecimal", form.getAttrDecimal());
	r.addField("attrDouble", form.getAttrDouble());
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
	    s.setVar("categoryId", form.getCategoryId(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: categoryId from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("categoryName", form.getCategoryName(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: categoryName from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("productId", form.getProductId(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: productId from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("productName", form.getProductName(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: productName from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("productDescription", form.getProductDescription(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: productDescription from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("productCode", form.getProductCode(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: productCode from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("productEntryPrice", form.getProductEntryPrice(), java.math.BigDecimal.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: productEntryPrice from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("productSellPrice", form.getProductSellPrice(), java.math.BigDecimal.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: productSellPrice from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("productPrice1", form.getProductPrice1(), java.math.BigDecimal.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: productPrice1 from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("productPrice2", form.getProductPrice2(), java.math.BigDecimal.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: productPrice2 from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("productPrice3", form.getProductPrice3(), java.math.BigDecimal.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: productPrice3 from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("productPrice4", form.getProductPrice4(), java.math.BigDecimal.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: productPrice4 from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("productPrice5", form.getProductPrice5(), java.math.BigDecimal.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: productPrice5 from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("attrId", form.getAttrId(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: attrId from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("attrName", form.getAttrName(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: attrName from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("attrString", form.getAttrString(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: attrString from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("attrInt", form.getAttrInt(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: attrInt from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("attrDecimal", form.getAttrDecimal(), java.math.BigDecimal.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: attrDecimal from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("attrDouble", form.getAttrDouble(), Double.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: attrDouble from the script");
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
	    field = s.getVar("categoryId", Integer.class);
	    if(!field.equals(form.getCategoryId())) {
	        logger.log(BasicLevel.DEBUG, "Field categoryId modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setCategoryId((Integer)field);
	        r.addField("categoryId", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: categoryId from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("categoryName", String.class);
	    if(!field.equals(form.getCategoryName())) {
	        logger.log(BasicLevel.DEBUG, "Field categoryName modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setCategoryName((String)field);
	        r.addField("categoryName", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: categoryName from the script");
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
	    field = s.getVar("productDescription", String.class);
	    if(!field.equals(form.getProductDescription())) {
	        logger.log(BasicLevel.DEBUG, "Field productDescription modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setProductDescription((String)field);
	        r.addField("productDescription", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: productDescription from the script");
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
	    field = s.getVar("productEntryPrice", java.math.BigDecimal.class);
	    if(!field.equals(form.getProductEntryPrice())) {
	        logger.log(BasicLevel.DEBUG, "Field productEntryPrice modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setProductEntryPrice((java.math.BigDecimal)field);
	        r.addField("productEntryPrice", (java.math.BigDecimal)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: productEntryPrice from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("productSellPrice", java.math.BigDecimal.class);
	    if(!field.equals(form.getProductSellPrice())) {
	        logger.log(BasicLevel.DEBUG, "Field productSellPrice modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setProductSellPrice((java.math.BigDecimal)field);
	        r.addField("productSellPrice", (java.math.BigDecimal)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: productSellPrice from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("productPrice1", java.math.BigDecimal.class);
	    if(!field.equals(form.getProductPrice1())) {
	        logger.log(BasicLevel.DEBUG, "Field productPrice1 modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setProductPrice1((java.math.BigDecimal)field);
	        r.addField("productPrice1", (java.math.BigDecimal)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: productPrice1 from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("productPrice2", java.math.BigDecimal.class);
	    if(!field.equals(form.getProductPrice2())) {
	        logger.log(BasicLevel.DEBUG, "Field productPrice2 modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setProductPrice2((java.math.BigDecimal)field);
	        r.addField("productPrice2", (java.math.BigDecimal)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: productPrice2 from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("productPrice3", java.math.BigDecimal.class);
	    if(!field.equals(form.getProductPrice3())) {
	        logger.log(BasicLevel.DEBUG, "Field productPrice3 modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setProductPrice3((java.math.BigDecimal)field);
	        r.addField("productPrice3", (java.math.BigDecimal)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: productPrice3 from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("productPrice4", java.math.BigDecimal.class);
	    if(!field.equals(form.getProductPrice4())) {
	        logger.log(BasicLevel.DEBUG, "Field productPrice4 modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setProductPrice4((java.math.BigDecimal)field);
	        r.addField("productPrice4", (java.math.BigDecimal)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: productPrice4 from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("productPrice5", java.math.BigDecimal.class);
	    if(!field.equals(form.getProductPrice5())) {
	        logger.log(BasicLevel.DEBUG, "Field productPrice5 modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setProductPrice5((java.math.BigDecimal)field);
	        r.addField("productPrice5", (java.math.BigDecimal)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: productPrice5 from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("attrId", Integer.class);
	    if(!field.equals(form.getAttrId())) {
	        logger.log(BasicLevel.DEBUG, "Field attrId modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setAttrId((Integer)field);
	        r.addField("attrId", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: attrId from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("attrName", String.class);
	    if(!field.equals(form.getAttrName())) {
	        logger.log(BasicLevel.DEBUG, "Field attrName modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setAttrName((String)field);
	        r.addField("attrName", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: attrName from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("attrString", String.class);
	    if(!field.equals(form.getAttrString())) {
	        logger.log(BasicLevel.DEBUG, "Field attrString modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setAttrString((String)field);
	        r.addField("attrString", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: attrString from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("attrInt", Integer.class);
	    if(!field.equals(form.getAttrInt())) {
	        logger.log(BasicLevel.DEBUG, "Field attrInt modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setAttrInt((Integer)field);
	        r.addField("attrInt", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: attrInt from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("attrDecimal", java.math.BigDecimal.class);
	    if(!field.equals(form.getAttrDecimal())) {
	        logger.log(BasicLevel.DEBUG, "Field attrDecimal modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setAttrDecimal((java.math.BigDecimal)field);
	        r.addField("attrDecimal", (java.math.BigDecimal)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: attrDecimal from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("attrDouble", Double.class);
	    if(!field.equals(form.getAttrDouble())) {
	        logger.log(BasicLevel.DEBUG, "Field attrDouble modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setAttrDouble((Double)field);
	        r.addField("attrDouble", (Double)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: attrDouble from the script");
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
             return "ro.kds.erp.biz.basic.GenericProducts";
         }
         
     }

}

