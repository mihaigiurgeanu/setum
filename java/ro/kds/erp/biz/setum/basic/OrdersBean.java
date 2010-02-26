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
 * Standard implementation of the Orders session bean.
 * You will have to extend this class to proivde code for the abstract
 * methods loadFormData, newForm and saveSaveFormData.
 */
public abstract class OrdersBean 
	implements SessionBean {

    static protected Logger logger = null;
    protected SessionContext ejbContext;

    /**
     * Stores the reference to the <code>ServiceFactory</code>
     * to be passed to script execution.
     */
    protected ServiceFactoryLocal factory;

    protected Integer id;
    protected Integer orderLineId;
    protected Integer proformaId;
    protected Integer invoiceId;
    protected Integer paymentId;
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
            logger = Monolog.getMonologFactory().getLogger("ro.kds.erp.biz.setum.basic.Orders");
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
    // Orders implementation
    // ------------------------------------------------------------------
    protected OrdersForm form;

    /**
     * Access to the form data.
     */
     public OrdersForm getForm() {
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
        orderLineId = null;
        proformaId = null;
        invoiceId = null;
        paymentId = null;
	
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
	logger.log(BasicLevel.DEBUG, "Loading Orders with id = " + loadId);
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
     * Create a new OrdersForm and initialize the 
     * <code>this.form</code> instance variable. Overwrite this method
     * if you want to provide other code for initializing the form bean.
     */
    protected void createNewFormBean() {
	form = new OrdersForm();
    }

    /**
     * Save the current record into the database.
     */
    public abstract ResponseBean saveFormData();

    public ResponseBean newOrderLineData() {
        initOrderLineFields();
        ResponseBean r = new ResponseBean();
        orderLineId = null;
        computeCalculatedFields(null);

        r.addRecord();
        copyFieldsToResponse(r);
        return r;
    }

    protected abstract void initOrderLineFields();

    /**
     * Load the data in the subform orderLine
     */
    public ResponseBean loadOrderLineData(Integer loadId) throws FinderException {

	logger.log(BasicLevel.DEBUG, "Loading subform OrderLine for id = " + loadId);
	initOrderLineFields();
	orderLineId = loadId;

	ResponseBean r = loadOrderLineFields();
	computeCalculatedFields(null);
	r.addRecord();
	copyFieldsToResponse(r);
	return r;
    }

    /**
     * Loads the fields corresponding to the subform orderLine
     * from the database.
     */
    protected abstract ResponseBean loadOrderLineFields() throws FinderException;

    /**
     * Save the current subform record into the database.
     */
    public abstract ResponseBean saveOrderLineData();

    public ResponseBean newProformaData() {
        initProformaFields();
        ResponseBean r = new ResponseBean();
        proformaId = null;
        computeCalculatedFields(null);

        r.addRecord();
        copyFieldsToResponse(r);
        return r;
    }

    protected abstract void initProformaFields();

    /**
     * Load the data in the subform proforma
     */
    public ResponseBean loadProformaData(Integer loadId) throws FinderException {

	logger.log(BasicLevel.DEBUG, "Loading subform Proforma for id = " + loadId);
	initProformaFields();
	proformaId = loadId;

	ResponseBean r = loadProformaFields();
	computeCalculatedFields(null);
	r.addRecord();
	copyFieldsToResponse(r);
	return r;
    }

    /**
     * Loads the fields corresponding to the subform proforma
     * from the database.
     */
    protected abstract ResponseBean loadProformaFields() throws FinderException;

    /**
     * Save the current subform record into the database.
     */
    public abstract ResponseBean saveProformaData();

    public ResponseBean newInvoiceData() {
        initInvoiceFields();
        ResponseBean r = new ResponseBean();
        invoiceId = null;
        computeCalculatedFields(null);

        r.addRecord();
        copyFieldsToResponse(r);
        return r;
    }

    protected abstract void initInvoiceFields();

    /**
     * Load the data in the subform invoice
     */
    public ResponseBean loadInvoiceData(Integer loadId) throws FinderException {

	logger.log(BasicLevel.DEBUG, "Loading subform Invoice for id = " + loadId);
	initInvoiceFields();
	invoiceId = loadId;

	ResponseBean r = loadInvoiceFields();
	computeCalculatedFields(null);
	r.addRecord();
	copyFieldsToResponse(r);
	return r;
    }

    /**
     * Loads the fields corresponding to the subform invoice
     * from the database.
     */
    protected abstract ResponseBean loadInvoiceFields() throws FinderException;

    /**
     * Save the current subform record into the database.
     */
    public abstract ResponseBean saveInvoiceData();

    public ResponseBean newPaymentData() {
        initPaymentFields();
        ResponseBean r = new ResponseBean();
        paymentId = null;
        computeCalculatedFields(null);

        r.addRecord();
        copyFieldsToResponse(r);
        return r;
    }

    protected abstract void initPaymentFields();

    /**
     * Load the data in the subform payment
     */
    public ResponseBean loadPaymentData(Integer loadId) throws FinderException {

	logger.log(BasicLevel.DEBUG, "Loading subform Payment for id = " + loadId);
	initPaymentFields();
	paymentId = loadId;

	ResponseBean r = loadPaymentFields();
	computeCalculatedFields(null);
	r.addRecord();
	copyFieldsToResponse(r);
	return r;
    }

    /**
     * Loads the fields corresponding to the subform payment
     * from the database.
     */
    protected abstract ResponseBean loadPaymentFields() throws FinderException;

    /**
     * Save the current subform record into the database.
     */
    public abstract ResponseBean savePaymentData();



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
     * <code>ro.kds.erp.biz.setum.basic.Orders_calculatedFields</code>
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
			      OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);

		addFieldsToScript(script);
		script.run();

		getFieldsFromScript(script, r);

	    } catch (ScriptErrorException e) {
		logger.log(BasicLevel.ERROR, "Error executing rules script for Orders", e);
	    }
	}
	return r;
    }
	

    /**
     * Evaluates the validation script. The script loader loades the script
     * file corresponding to:
     *
     * <code>ro.kds.erp.biz.setum.basic.Orders_validation</code>
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
			      OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();

		// I don't add modified fields back to the response bean,
		// so any fields modifications made in the script will
		// be ignored.
	    } catch (ScriptErrorException e) {
		logger.log(BasicLevel.ERROR, "Error executing rules script for Orders", e);
	    }
	}
	return r;
    }

    public ResponseBean updateNumber(String number) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getNumber();
	form.setNumber(number);
	r.addRecord();
	r.addField("number", number); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".number");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the number", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateDate(java.util.Date date) {
        ResponseBean r = new ResponseBean();
	java.util.Date oldVal = form.getDate();
	form.setDate(date);
	r.addRecord();
	r.addField("date", date); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".date");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, java.util.Date.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the date", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateClientId(Integer clientId) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getClientId();
	form.setClientId(clientId);
	r.addRecord();
	r.addField("clientId", clientId); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".clientId");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, Integer.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the clientId", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateClientName(String clientName) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getClientName();
	form.setClientName(clientName);
	r.addRecord();
	r.addField("clientName", clientName); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".clientName");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the clientName", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateMontaj(Integer montaj) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getMontaj();
	form.setMontaj(montaj);
	r.addRecord();
	r.addField("montaj", montaj); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".montaj");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, Integer.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the montaj", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateLocalitate(Integer localitate) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getLocalitate();
	form.setLocalitate(localitate);
	r.addRecord();
	r.addField("localitate", localitate); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".localitate");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, Integer.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the localitate", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateLocalitateAlta(String localitateAlta) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getLocalitateAlta();
	form.setLocalitateAlta(localitateAlta);
	r.addRecord();
	r.addField("localitateAlta", localitateAlta); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".localitateAlta");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the localitateAlta", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateDistanta(java.math.BigDecimal distanta) {
        ResponseBean r = new ResponseBean();
	java.math.BigDecimal oldVal = form.getDistanta();
	form.setDistanta(distanta);
	r.addRecord();
	r.addField("distanta", distanta); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".distanta");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, java.math.BigDecimal.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the distanta", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateObservatii(String observatii) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getObservatii();
	form.setObservatii(observatii);
	r.addRecord();
	r.addField("observatii", observatii); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".observatii");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the observatii", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateValoareMontaj(java.math.BigDecimal valoareMontaj) {
        ResponseBean r = new ResponseBean();
	java.math.BigDecimal oldVal = form.getValoareMontaj();
	form.setValoareMontaj(valoareMontaj);
	r.addRecord();
	r.addField("valoareMontaj", valoareMontaj); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".valoareMontaj");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, java.math.BigDecimal.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the valoareMontaj", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateValoareTransport(java.math.BigDecimal valoareTransport) {
        ResponseBean r = new ResponseBean();
	java.math.BigDecimal oldVal = form.getValoareTransport();
	form.setValoareTransport(valoareTransport);
	r.addRecord();
	r.addField("valoareTransport", valoareTransport); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".valoareTransport");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, java.math.BigDecimal.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the valoareTransport", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateValoareProduse(java.math.BigDecimal valoareProduse) {
        ResponseBean r = new ResponseBean();
	java.math.BigDecimal oldVal = form.getValoareProduse();
	form.setValoareProduse(valoareProduse);
	r.addRecord();
	r.addField("valoareProduse", valoareProduse); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".valoareProduse");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, java.math.BigDecimal.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the valoareProduse", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateTotal(java.math.BigDecimal total) {
        ResponseBean r = new ResponseBean();
	java.math.BigDecimal oldVal = form.getTotal();
	form.setTotal(total);
	r.addRecord();
	r.addField("total", total); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".total");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, java.math.BigDecimal.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the total", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateTvaPercent(Double tvaPercent) {
        ResponseBean r = new ResponseBean();
	Double oldVal = form.getTvaPercent();
	form.setTvaPercent(tvaPercent);
	r.addRecord();
	r.addField("tvaPercent", tvaPercent); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".tvaPercent");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, Double.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the tvaPercent", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateTotalTva(java.math.BigDecimal totalTva) {
        ResponseBean r = new ResponseBean();
	java.math.BigDecimal oldVal = form.getTotalTva();
	form.setTotalTva(totalTva);
	r.addRecord();
	r.addField("totalTva", totalTva); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".totalTva");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, java.math.BigDecimal.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the totalTva", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateDiscount(java.math.BigDecimal discount) {
        ResponseBean r = new ResponseBean();
	java.math.BigDecimal oldVal = form.getDiscount();
	form.setDiscount(discount);
	r.addRecord();
	r.addField("discount", discount); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".discount");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, java.math.BigDecimal.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the discount", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateTotalFinal(java.math.BigDecimal totalFinal) {
        ResponseBean r = new ResponseBean();
	java.math.BigDecimal oldVal = form.getTotalFinal();
	form.setTotalFinal(totalFinal);
	r.addRecord();
	r.addField("totalFinal", totalFinal); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".totalFinal");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, java.math.BigDecimal.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the totalFinal", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateTotalFinalTva(java.math.BigDecimal totalFinalTva) {
        ResponseBean r = new ResponseBean();
	java.math.BigDecimal oldVal = form.getTotalFinalTva();
	form.setTotalFinalTva(totalFinalTva);
	r.addRecord();
	r.addField("totalFinalTva", totalFinalTva); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".totalFinalTva");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, java.math.BigDecimal.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the totalFinalTva", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateAvans(java.math.BigDecimal avans) {
        ResponseBean r = new ResponseBean();
	java.math.BigDecimal oldVal = form.getAvans();
	form.setAvans(avans);
	r.addRecord();
	r.addField("avans", avans); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".avans");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, java.math.BigDecimal.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the avans", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateAchitatCu(String achitatCu) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getAchitatCu();
	form.setAchitatCu(achitatCu);
	r.addRecord();
	r.addField("achitatCu", achitatCu); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".achitatCu");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the achitatCu", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateValoareAvans(java.math.BigDecimal valoareAvans) {
        ResponseBean r = new ResponseBean();
	java.math.BigDecimal oldVal = form.getValoareAvans();
	form.setValoareAvans(valoareAvans);
	r.addRecord();
	r.addField("valoareAvans", valoareAvans); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".valoareAvans");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, java.math.BigDecimal.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the valoareAvans", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updatePayedAmount(java.math.BigDecimal payedAmount) {
        ResponseBean r = new ResponseBean();
	java.math.BigDecimal oldVal = form.getPayedAmount();
	form.setPayedAmount(payedAmount);
	r.addRecord();
	r.addField("payedAmount", payedAmount); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".payedAmount");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, java.math.BigDecimal.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the payedAmount", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateInvoicedAmount(java.math.BigDecimal invoicedAmount) {
        ResponseBean r = new ResponseBean();
	java.math.BigDecimal oldVal = form.getInvoicedAmount();
	form.setInvoicedAmount(invoicedAmount);
	r.addRecord();
	r.addField("invoicedAmount", invoicedAmount); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".invoicedAmount");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, java.math.BigDecimal.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the invoicedAmount", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateTotalCurrency(java.math.BigDecimal totalCurrency) {
        ResponseBean r = new ResponseBean();
	java.math.BigDecimal oldVal = form.getTotalCurrency();
	form.setTotalCurrency(totalCurrency);
	r.addRecord();
	r.addField("totalCurrency", totalCurrency); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".totalCurrency");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, java.math.BigDecimal.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the totalCurrency", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateTotalTvaCurrency(java.math.BigDecimal totalTvaCurrency) {
        ResponseBean r = new ResponseBean();
	java.math.BigDecimal oldVal = form.getTotalTvaCurrency();
	form.setTotalTvaCurrency(totalTvaCurrency);
	r.addRecord();
	r.addField("totalTvaCurrency", totalTvaCurrency); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".totalTvaCurrency");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, java.math.BigDecimal.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the totalTvaCurrency", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateTotalFinalCurrency(java.math.BigDecimal totalFinalCurrency) {
        ResponseBean r = new ResponseBean();
	java.math.BigDecimal oldVal = form.getTotalFinalCurrency();
	form.setTotalFinalCurrency(totalFinalCurrency);
	r.addRecord();
	r.addField("totalFinalCurrency", totalFinalCurrency); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".totalFinalCurrency");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, java.math.BigDecimal.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the totalFinalCurrency", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateTotalFinalTvaCurrency(java.math.BigDecimal totalFinalTvaCurrency) {
        ResponseBean r = new ResponseBean();
	java.math.BigDecimal oldVal = form.getTotalFinalTvaCurrency();
	form.setTotalFinalTvaCurrency(totalFinalTvaCurrency);
	r.addRecord();
	r.addField("totalFinalTvaCurrency", totalFinalTvaCurrency); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".totalFinalTvaCurrency");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, java.math.BigDecimal.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the totalFinalTvaCurrency", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateAvansCurrency(java.math.BigDecimal avansCurrency) {
        ResponseBean r = new ResponseBean();
	java.math.BigDecimal oldVal = form.getAvansCurrency();
	form.setAvansCurrency(avansCurrency);
	r.addRecord();
	r.addField("avansCurrency", avansCurrency); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".avansCurrency");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, java.math.BigDecimal.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the avansCurrency", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updatePayedAmountCurrency(java.math.BigDecimal payedAmountCurrency) {
        ResponseBean r = new ResponseBean();
	java.math.BigDecimal oldVal = form.getPayedAmountCurrency();
	form.setPayedAmountCurrency(payedAmountCurrency);
	r.addRecord();
	r.addField("payedAmountCurrency", payedAmountCurrency); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".payedAmountCurrency");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, java.math.BigDecimal.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the payedAmountCurrency", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateCurrencyPayedAmount(java.math.BigDecimal currencyPayedAmount) {
        ResponseBean r = new ResponseBean();
	java.math.BigDecimal oldVal = form.getCurrencyPayedAmount();
	form.setCurrencyPayedAmount(currencyPayedAmount);
	r.addRecord();
	r.addField("currencyPayedAmount", currencyPayedAmount); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".currencyPayedAmount");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, java.math.BigDecimal.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the currencyPayedAmount", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateCurrencyInvoicedAmount(java.math.BigDecimal currencyInvoicedAmount) {
        ResponseBean r = new ResponseBean();
	java.math.BigDecimal oldVal = form.getCurrencyInvoicedAmount();
	form.setCurrencyInvoicedAmount(currencyInvoicedAmount);
	r.addRecord();
	r.addField("currencyInvoicedAmount", currencyInvoicedAmount); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".currencyInvoicedAmount");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, java.math.BigDecimal.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the currencyInvoicedAmount", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateDiferenta(java.math.BigDecimal diferenta) {
        ResponseBean r = new ResponseBean();
	java.math.BigDecimal oldVal = form.getDiferenta();
	form.setDiferenta(diferenta);
	r.addRecord();
	r.addField("diferenta", diferenta); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".diferenta");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, java.math.BigDecimal.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the diferenta", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateCurrencyDiferenta(java.math.BigDecimal currencyDiferenta) {
        ResponseBean r = new ResponseBean();
	java.math.BigDecimal oldVal = form.getCurrencyDiferenta();
	form.setCurrencyDiferenta(currencyDiferenta);
	r.addRecord();
	r.addField("currencyDiferenta", currencyDiferenta); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".currencyDiferenta");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, java.math.BigDecimal.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the currencyDiferenta", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateCurrencyCode(String currencyCode) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getCurrencyCode();
	form.setCurrencyCode(currencyCode);
	r.addRecord();
	r.addField("currencyCode", currencyCode); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".currencyCode");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the currencyCode", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateExchangeRate(java.math.BigDecimal exchangeRate) {
        ResponseBean r = new ResponseBean();
	java.math.BigDecimal oldVal = form.getExchangeRate();
	form.setExchangeRate(exchangeRate);
	r.addRecord();
	r.addField("exchangeRate", exchangeRate); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".exchangeRate");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, java.math.BigDecimal.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the exchangeRate", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateTermenLivrare(java.util.Date termenLivrare) {
        ResponseBean r = new ResponseBean();
	java.util.Date oldVal = form.getTermenLivrare();
	form.setTermenLivrare(termenLivrare);
	r.addRecord();
	r.addField("termenLivrare", termenLivrare); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".termenLivrare");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, java.util.Date.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the termenLivrare", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateTermenLivrare1(java.util.Date termenLivrare1) {
        ResponseBean r = new ResponseBean();
	java.util.Date oldVal = form.getTermenLivrare1();
	form.setTermenLivrare1(termenLivrare1);
	r.addRecord();
	r.addField("termenLivrare1", termenLivrare1); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".termenLivrare1");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, java.util.Date.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the termenLivrare1", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateAdresaMontaj(String adresaMontaj) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getAdresaMontaj();
	form.setAdresaMontaj(adresaMontaj);
	r.addRecord();
	r.addField("adresaMontaj", adresaMontaj); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".adresaMontaj");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the adresaMontaj", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateAdresaReper(String adresaReper) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getAdresaReper();
	form.setAdresaReper(adresaReper);
	r.addRecord();
	r.addField("adresaReper", adresaReper); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".adresaReper");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the adresaReper", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateTelefon(String telefon) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getTelefon();
	form.setTelefon(telefon);
	r.addRecord();
	r.addField("telefon", telefon); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".telefon");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the telefon", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateContact(String contact) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getContact();
	form.setContact(contact);
	r.addRecord();
	r.addField("contact", contact); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".contact");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the contact", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateDeliveryHour(String deliveryHour) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getDeliveryHour();
	form.setDeliveryHour(deliveryHour);
	r.addRecord();
	r.addField("deliveryHour", deliveryHour); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".deliveryHour");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the deliveryHour", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateTipDemontare(String tipDemontare) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getTipDemontare();
	form.setTipDemontare(tipDemontare);
	r.addRecord();
	r.addField("tipDemontare", tipDemontare); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".tipDemontare");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the tipDemontare", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateAttribute1(String attribute1) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getAttribute1();
	form.setAttribute1(attribute1);
	r.addRecord();
	r.addField("attribute1", attribute1); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".attribute1");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the attribute1", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateAttribute2(String attribute2) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getAttribute2();
	form.setAttribute2(attribute2);
	r.addRecord();
	r.addField("attribute2", attribute2); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".attribute2");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the attribute2", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateAttribute3(String attribute3) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getAttribute3();
	form.setAttribute3(attribute3);
	r.addRecord();
	r.addField("attribute3", attribute3); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".attribute3");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the attribute3", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateAttribute4(String attribute4) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getAttribute4();
	form.setAttribute4(attribute4);
	r.addRecord();
	r.addField("attribute4", attribute4); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".attribute4");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the attribute4", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateAttribute5(String attribute5) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getAttribute5();
	form.setAttribute5(attribute5);
	r.addRecord();
	r.addField("attribute5", attribute5); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".attribute5");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the attribute5", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateLivrariRStart(java.util.Date livrariRStart) {
        ResponseBean r = new ResponseBean();
	java.util.Date oldVal = form.getLivrariRStart();
	form.setLivrariRStart(livrariRStart);
	r.addRecord();
	r.addField("livrariRStart", livrariRStart); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".livrariRStart");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, java.util.Date.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the livrariRStart", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateLivrariREnd(java.util.Date livrariREnd) {
        ResponseBean r = new ResponseBean();
	java.util.Date oldVal = form.getLivrariREnd();
	form.setLivrariREnd(livrariREnd);
	r.addRecord();
	r.addField("livrariREnd", livrariREnd); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".livrariREnd");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, java.util.Date.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the livrariREnd", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateLivrariCuMontaj(String livrariCuMontaj) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getLivrariCuMontaj();
	form.setLivrariCuMontaj(livrariCuMontaj);
	r.addRecord();
	r.addField("livrariCuMontaj", livrariCuMontaj); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".livrariCuMontaj");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the livrariCuMontaj", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateIncasariFromDate(java.util.Date incasariFromDate) {
        ResponseBean r = new ResponseBean();
	java.util.Date oldVal = form.getIncasariFromDate();
	form.setIncasariFromDate(incasariFromDate);
	r.addRecord();
	r.addField("incasariFromDate", incasariFromDate); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".incasariFromDate");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, java.util.Date.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the incasariFromDate", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateIncasariToDate(java.util.Date incasariToDate) {
        ResponseBean r = new ResponseBean();
	java.util.Date oldVal = form.getIncasariToDate();
	form.setIncasariToDate(incasariToDate);
	r.addRecord();
	r.addField("incasariToDate", incasariToDate); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".incasariToDate");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, java.util.Date.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the incasariToDate", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateOfferItemId(Integer offerItemId) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getOfferItemId();
	form.setOfferItemId(offerItemId);
	r.addRecord();
	r.addField("offerItemId", offerItemId); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".offerItemId");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, Integer.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the offerItemId", e);
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
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
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
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
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
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
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
    public ResponseBean updateProductPrice(java.math.BigDecimal productPrice) {
        ResponseBean r = new ResponseBean();
	java.math.BigDecimal oldVal = form.getProductPrice();
	form.setProductPrice(productPrice);
	r.addRecord();
	r.addField("productPrice", productPrice); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".productPrice");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, java.math.BigDecimal.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the productPrice", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updatePriceRatio(Double priceRatio) {
        ResponseBean r = new ResponseBean();
	Double oldVal = form.getPriceRatio();
	form.setPriceRatio(priceRatio);
	r.addRecord();
	r.addField("priceRatio", priceRatio); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".priceRatio");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, Double.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the priceRatio", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateQuantity(java.math.BigDecimal quantity) {
        ResponseBean r = new ResponseBean();
	java.math.BigDecimal oldVal = form.getQuantity();
	form.setQuantity(quantity);
	r.addRecord();
	r.addField("quantity", quantity); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".quantity");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, java.math.BigDecimal.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the quantity", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateValue(java.math.BigDecimal value) {
        ResponseBean r = new ResponseBean();
	java.math.BigDecimal oldVal = form.getValue();
	form.setValue(value);
	r.addRecord();
	r.addField("value", value); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".value");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, java.math.BigDecimal.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the value", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateTax(java.math.BigDecimal tax) {
        ResponseBean r = new ResponseBean();
	java.math.BigDecimal oldVal = form.getTax();
	form.setTax(tax);
	r.addRecord();
	r.addField("tax", tax); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".tax");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, java.math.BigDecimal.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the tax", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateCodMontaj(Integer codMontaj) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getCodMontaj();
	form.setCodMontaj(codMontaj);
	r.addRecord();
	r.addField("codMontaj", codMontaj); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".codMontaj");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, Integer.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the codMontaj", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateMontajProcent(Double montajProcent) {
        ResponseBean r = new ResponseBean();
	Double oldVal = form.getMontajProcent();
	form.setMontajProcent(montajProcent);
	r.addRecord();
	r.addField("montajProcent", montajProcent); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".montajProcent");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, Double.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the montajProcent", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateMontajSeparat(Boolean montajSeparat) {
        ResponseBean r = new ResponseBean();
	Boolean oldVal = form.getMontajSeparat();
	form.setMontajSeparat(montajSeparat);
	r.addRecord();
	r.addField("montajSeparat", montajSeparat); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".montajSeparat");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, Boolean.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the montajSeparat", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateProformaNumber(String proformaNumber) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getProformaNumber();
	form.setProformaNumber(proformaNumber);
	r.addRecord();
	r.addField("proformaNumber", proformaNumber); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".proformaNumber");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the proformaNumber", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateProformaDate(java.util.Date proformaDate) {
        ResponseBean r = new ResponseBean();
	java.util.Date oldVal = form.getProformaDate();
	form.setProformaDate(proformaDate);
	r.addRecord();
	r.addField("proformaDate", proformaDate); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".proformaDate");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, java.util.Date.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the proformaDate", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateProformaRole(String proformaRole) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getProformaRole();
	form.setProformaRole(proformaRole);
	r.addRecord();
	r.addField("proformaRole", proformaRole); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".proformaRole");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the proformaRole", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateProformaAmount(java.math.BigDecimal proformaAmount) {
        ResponseBean r = new ResponseBean();
	java.math.BigDecimal oldVal = form.getProformaAmount();
	form.setProformaAmount(proformaAmount);
	r.addRecord();
	r.addField("proformaAmount", proformaAmount); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".proformaAmount");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, java.math.BigDecimal.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the proformaAmount", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateProformaTax(java.math.BigDecimal proformaTax) {
        ResponseBean r = new ResponseBean();
	java.math.BigDecimal oldVal = form.getProformaTax();
	form.setProformaTax(proformaTax);
	r.addRecord();
	r.addField("proformaTax", proformaTax); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".proformaTax");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, java.math.BigDecimal.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the proformaTax", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateProformaExchangeRate(Double proformaExchangeRate) {
        ResponseBean r = new ResponseBean();
	Double oldVal = form.getProformaExchangeRate();
	form.setProformaExchangeRate(proformaExchangeRate);
	r.addRecord();
	r.addField("proformaExchangeRate", proformaExchangeRate); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".proformaExchangeRate");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, Double.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the proformaExchangeRate", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateProformaTotal(java.math.BigDecimal proformaTotal) {
        ResponseBean r = new ResponseBean();
	java.math.BigDecimal oldVal = form.getProformaTotal();
	form.setProformaTotal(proformaTotal);
	r.addRecord();
	r.addField("proformaTotal", proformaTotal); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".proformaTotal");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, java.math.BigDecimal.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the proformaTotal", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateProformaPercent(Double proformaPercent) {
        ResponseBean r = new ResponseBean();
	Double oldVal = form.getProformaPercent();
	form.setProformaPercent(proformaPercent);
	r.addRecord();
	r.addField("proformaPercent", proformaPercent); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".proformaPercent");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, Double.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the proformaPercent", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateProformaUsePercent(Boolean proformaUsePercent) {
        ResponseBean r = new ResponseBean();
	Boolean oldVal = form.getProformaUsePercent();
	form.setProformaUsePercent(proformaUsePercent);
	r.addRecord();
	r.addField("proformaUsePercent", proformaUsePercent); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".proformaUsePercent");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, Boolean.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the proformaUsePercent", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateProformaComment(String proformaComment) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getProformaComment();
	form.setProformaComment(proformaComment);
	r.addRecord();
	r.addField("proformaComment", proformaComment); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".proformaComment");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the proformaComment", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateProformaContract(String proformaContract) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getProformaContract();
	form.setProformaContract(proformaContract);
	r.addRecord();
	r.addField("proformaContract", proformaContract); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".proformaContract");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the proformaContract", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateProformaObiectiv(String proformaObiectiv) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getProformaObiectiv();
	form.setProformaObiectiv(proformaObiectiv);
	r.addRecord();
	r.addField("proformaObiectiv", proformaObiectiv); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".proformaObiectiv");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the proformaObiectiv", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateProformaCurrency(String proformaCurrency) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getProformaCurrency();
	form.setProformaCurrency(proformaCurrency);
	r.addRecord();
	r.addField("proformaCurrency", proformaCurrency); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".proformaCurrency");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the proformaCurrency", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateProformaAmountCurrency(java.math.BigDecimal proformaAmountCurrency) {
        ResponseBean r = new ResponseBean();
	java.math.BigDecimal oldVal = form.getProformaAmountCurrency();
	form.setProformaAmountCurrency(proformaAmountCurrency);
	r.addRecord();
	r.addField("proformaAmountCurrency", proformaAmountCurrency); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".proformaAmountCurrency");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, java.math.BigDecimal.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the proformaAmountCurrency", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateProformaTaxCurrency(java.math.BigDecimal proformaTaxCurrency) {
        ResponseBean r = new ResponseBean();
	java.math.BigDecimal oldVal = form.getProformaTaxCurrency();
	form.setProformaTaxCurrency(proformaTaxCurrency);
	r.addRecord();
	r.addField("proformaTaxCurrency", proformaTaxCurrency); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".proformaTaxCurrency");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, java.math.BigDecimal.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the proformaTaxCurrency", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateProformaTotalCurrency(java.math.BigDecimal proformaTotalCurrency) {
        ResponseBean r = new ResponseBean();
	java.math.BigDecimal oldVal = form.getProformaTotalCurrency();
	form.setProformaTotalCurrency(proformaTotalCurrency);
	r.addRecord();
	r.addField("proformaTotalCurrency", proformaTotalCurrency); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".proformaTotalCurrency");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, java.math.BigDecimal.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the proformaTotalCurrency", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateProformaAttribute1(String proformaAttribute1) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getProformaAttribute1();
	form.setProformaAttribute1(proformaAttribute1);
	r.addRecord();
	r.addField("proformaAttribute1", proformaAttribute1); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".proformaAttribute1");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the proformaAttribute1", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateProformaAttribute2(String proformaAttribute2) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getProformaAttribute2();
	form.setProformaAttribute2(proformaAttribute2);
	r.addRecord();
	r.addField("proformaAttribute2", proformaAttribute2); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".proformaAttribute2");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the proformaAttribute2", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateProformaAttribute3(String proformaAttribute3) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getProformaAttribute3();
	form.setProformaAttribute3(proformaAttribute3);
	r.addRecord();
	r.addField("proformaAttribute3", proformaAttribute3); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".proformaAttribute3");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the proformaAttribute3", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateProformaAttribute4(String proformaAttribute4) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getProformaAttribute4();
	form.setProformaAttribute4(proformaAttribute4);
	r.addRecord();
	r.addField("proformaAttribute4", proformaAttribute4); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".proformaAttribute4");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the proformaAttribute4", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateProformaAttribute5(String proformaAttribute5) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getProformaAttribute5();
	form.setProformaAttribute5(proformaAttribute5);
	r.addRecord();
	r.addField("proformaAttribute5", proformaAttribute5); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".proformaAttribute5");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the proformaAttribute5", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateProformaAttribute6(String proformaAttribute6) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getProformaAttribute6();
	form.setProformaAttribute6(proformaAttribute6);
	r.addRecord();
	r.addField("proformaAttribute6", proformaAttribute6); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".proformaAttribute6");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the proformaAttribute6", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateProformaAttribute7(String proformaAttribute7) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getProformaAttribute7();
	form.setProformaAttribute7(proformaAttribute7);
	r.addRecord();
	r.addField("proformaAttribute7", proformaAttribute7); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".proformaAttribute7");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the proformaAttribute7", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateProformaAttribute8(String proformaAttribute8) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getProformaAttribute8();
	form.setProformaAttribute8(proformaAttribute8);
	r.addRecord();
	r.addField("proformaAttribute8", proformaAttribute8); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".proformaAttribute8");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the proformaAttribute8", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateProformaAttribute9(String proformaAttribute9) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getProformaAttribute9();
	form.setProformaAttribute9(proformaAttribute9);
	r.addRecord();
	r.addField("proformaAttribute9", proformaAttribute9); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".proformaAttribute9");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the proformaAttribute9", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateProformaAttribute10(String proformaAttribute10) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getProformaAttribute10();
	form.setProformaAttribute10(proformaAttribute10);
	r.addRecord();
	r.addField("proformaAttribute10", proformaAttribute10); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".proformaAttribute10");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the proformaAttribute10", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateProformaAttribute11(String proformaAttribute11) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getProformaAttribute11();
	form.setProformaAttribute11(proformaAttribute11);
	r.addRecord();
	r.addField("proformaAttribute11", proformaAttribute11); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".proformaAttribute11");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the proformaAttribute11", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateProformaAttribute12(String proformaAttribute12) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getProformaAttribute12();
	form.setProformaAttribute12(proformaAttribute12);
	r.addRecord();
	r.addField("proformaAttribute12", proformaAttribute12); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".proformaAttribute12");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the proformaAttribute12", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateProformaAttribute13(String proformaAttribute13) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getProformaAttribute13();
	form.setProformaAttribute13(proformaAttribute13);
	r.addRecord();
	r.addField("proformaAttribute13", proformaAttribute13); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".proformaAttribute13");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the proformaAttribute13", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateProformaAttribute14(String proformaAttribute14) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getProformaAttribute14();
	form.setProformaAttribute14(proformaAttribute14);
	r.addRecord();
	r.addField("proformaAttribute14", proformaAttribute14); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".proformaAttribute14");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the proformaAttribute14", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateProformaAttribute15(String proformaAttribute15) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getProformaAttribute15();
	form.setProformaAttribute15(proformaAttribute15);
	r.addRecord();
	r.addField("proformaAttribute15", proformaAttribute15); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".proformaAttribute15");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the proformaAttribute15", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateInvoiceNumber(String invoiceNumber) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getInvoiceNumber();
	form.setInvoiceNumber(invoiceNumber);
	r.addRecord();
	r.addField("invoiceNumber", invoiceNumber); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".invoiceNumber");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the invoiceNumber", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateInvoiceDate(java.util.Date invoiceDate) {
        ResponseBean r = new ResponseBean();
	java.util.Date oldVal = form.getInvoiceDate();
	form.setInvoiceDate(invoiceDate);
	r.addRecord();
	r.addField("invoiceDate", invoiceDate); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".invoiceDate");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, java.util.Date.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the invoiceDate", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateInvoiceRole(String invoiceRole) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getInvoiceRole();
	form.setInvoiceRole(invoiceRole);
	r.addRecord();
	r.addField("invoiceRole", invoiceRole); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".invoiceRole");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the invoiceRole", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateInvoiceAmount(java.math.BigDecimal invoiceAmount) {
        ResponseBean r = new ResponseBean();
	java.math.BigDecimal oldVal = form.getInvoiceAmount();
	form.setInvoiceAmount(invoiceAmount);
	r.addRecord();
	r.addField("invoiceAmount", invoiceAmount); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".invoiceAmount");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, java.math.BigDecimal.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the invoiceAmount", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateInvoiceTax(java.math.BigDecimal invoiceTax) {
        ResponseBean r = new ResponseBean();
	java.math.BigDecimal oldVal = form.getInvoiceTax();
	form.setInvoiceTax(invoiceTax);
	r.addRecord();
	r.addField("invoiceTax", invoiceTax); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".invoiceTax");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, java.math.BigDecimal.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the invoiceTax", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateInvoiceExchangeRate(Double invoiceExchangeRate) {
        ResponseBean r = new ResponseBean();
	Double oldVal = form.getInvoiceExchangeRate();
	form.setInvoiceExchangeRate(invoiceExchangeRate);
	r.addRecord();
	r.addField("invoiceExchangeRate", invoiceExchangeRate); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".invoiceExchangeRate");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, Double.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the invoiceExchangeRate", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateInvoiceTotal(java.math.BigDecimal invoiceTotal) {
        ResponseBean r = new ResponseBean();
	java.math.BigDecimal oldVal = form.getInvoiceTotal();
	form.setInvoiceTotal(invoiceTotal);
	r.addRecord();
	r.addField("invoiceTotal", invoiceTotal); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".invoiceTotal");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, java.math.BigDecimal.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the invoiceTotal", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateInvoicePayed(java.math.BigDecimal invoicePayed) {
        ResponseBean r = new ResponseBean();
	java.math.BigDecimal oldVal = form.getInvoicePayed();
	form.setInvoicePayed(invoicePayed);
	r.addRecord();
	r.addField("invoicePayed", invoicePayed); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".invoicePayed");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, java.math.BigDecimal.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the invoicePayed", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateInvoiceUnpayed(java.math.BigDecimal invoiceUnpayed) {
        ResponseBean r = new ResponseBean();
	java.math.BigDecimal oldVal = form.getInvoiceUnpayed();
	form.setInvoiceUnpayed(invoiceUnpayed);
	r.addRecord();
	r.addField("invoiceUnpayed", invoiceUnpayed); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".invoiceUnpayed");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, java.math.BigDecimal.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the invoiceUnpayed", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updatePaymentNumber(String paymentNumber) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getPaymentNumber();
	form.setPaymentNumber(paymentNumber);
	r.addRecord();
	r.addField("paymentNumber", paymentNumber); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".paymentNumber");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the paymentNumber", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updatePaymentDate(java.util.Date paymentDate) {
        ResponseBean r = new ResponseBean();
	java.util.Date oldVal = form.getPaymentDate();
	form.setPaymentDate(paymentDate);
	r.addRecord();
	r.addField("paymentDate", paymentDate); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".paymentDate");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, java.util.Date.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the paymentDate", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updatePaymentAmount(java.math.BigDecimal paymentAmount) {
        ResponseBean r = new ResponseBean();
	java.math.BigDecimal oldVal = form.getPaymentAmount();
	form.setPaymentAmount(paymentAmount);
	r.addRecord();
	r.addField("paymentAmount", paymentAmount); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".paymentAmount");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, java.math.BigDecimal.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the paymentAmount", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updatePaymentExchangeRate(Double paymentExchangeRate) {
        ResponseBean r = new ResponseBean();
	Double oldVal = form.getPaymentExchangeRate();
	form.setPaymentExchangeRate(paymentExchangeRate);
	r.addRecord();
	r.addField("paymentExchangeRate", paymentExchangeRate); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".paymentExchangeRate");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(OLDVAL_VARNAME, oldVal, Double.class);
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the paymentExchangeRate", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }

    /**
     * Generated implementation of the addItem service. It will call
     * the script ro.kds.erp.biz.setum.basic.Orders.addItem
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
    public ResponseBean addItem (
        Integer offerIntemId
    ) {


        ResponseBean r = new ResponseBean();
        r.addRecord();
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".addItem");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);

		script.setVar("param_offerIntemId", offerIntemId, Integer.class);

		
		addFieldsToScript(script);
		script.run();
                getFieldsFromScript(script, r);
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for service addItem", e);
           }
        }
	return r;
    }
    /**
     * Generated implementation of the removeItem service. It will call
     * the script ro.kds.erp.biz.setum.basic.Orders.removeItem
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
    public ResponseBean removeItem (
    ) {


        ResponseBean r = new ResponseBean();
        r.addRecord();
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".removeItem");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this, this.getClass());
		script.setVar(FORM_VARNAME, form, OrdersForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		script.setVar(SERVICE_FACTORY_VARNAME, factory, ServiceFactoryLocal.class);
		script.setVar(LOGGER_VARNAME, logger, Logger.class);


		
		addFieldsToScript(script);
		script.run();
                getFieldsFromScript(script, r);
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for service removeItem", e);
           }
        }
	return r;
    }

    /**
     * Get the fields stored internaly and adds them to the response.
     */
    protected void copyFieldsToResponse(ResponseBean r) {
	r.addField("number", form.getNumber());
	r.addField("date", form.getDate());
	r.addField("clientId", form.getClientId());
	r.addField("clientName", form.getClientName());
	r.addField("montaj", form.getMontaj());
	r.addField("localitate", form.getLocalitate());
	r.addField("localitateAlta", form.getLocalitateAlta());
	r.addField("distanta", form.getDistanta());
	r.addField("observatii", form.getObservatii());
	r.addField("valoareMontaj", form.getValoareMontaj());
	r.addField("valoareTransport", form.getValoareTransport());
	r.addField("valoareProduse", form.getValoareProduse());
	r.addField("total", form.getTotal());
	r.addField("tvaPercent", form.getTvaPercent());
	r.addField("totalTva", form.getTotalTva());
	r.addField("discount", form.getDiscount());
	r.addField("totalFinal", form.getTotalFinal());
	r.addField("totalFinalTva", form.getTotalFinalTva());
	r.addField("avans", form.getAvans());
	r.addField("achitatCu", form.getAchitatCu());
	r.addField("valoareAvans", form.getValoareAvans());
	r.addField("payedAmount", form.getPayedAmount());
	r.addField("invoicedAmount", form.getInvoicedAmount());
	r.addField("totalCurrency", form.getTotalCurrency());
	r.addField("totalTvaCurrency", form.getTotalTvaCurrency());
	r.addField("totalFinalCurrency", form.getTotalFinalCurrency());
	r.addField("totalFinalTvaCurrency", form.getTotalFinalTvaCurrency());
	r.addField("avansCurrency", form.getAvansCurrency());
	r.addField("payedAmountCurrency", form.getPayedAmountCurrency());
	r.addField("currencyPayedAmount", form.getCurrencyPayedAmount());
	r.addField("currencyInvoicedAmount", form.getCurrencyInvoicedAmount());
	r.addField("diferenta", form.getDiferenta());
	r.addField("currencyDiferenta", form.getCurrencyDiferenta());
	r.addField("currencyCode", form.getCurrencyCode());
	r.addField("exchangeRate", form.getExchangeRate());
	r.addField("termenLivrare", form.getTermenLivrare());
	r.addField("termenLivrare1", form.getTermenLivrare1());
	r.addField("adresaMontaj", form.getAdresaMontaj());
	r.addField("adresaReper", form.getAdresaReper());
	r.addField("telefon", form.getTelefon());
	r.addField("contact", form.getContact());
	r.addField("deliveryHour", form.getDeliveryHour());
	r.addField("tipDemontare", form.getTipDemontare());
	r.addField("attribute1", form.getAttribute1());
	r.addField("attribute2", form.getAttribute2());
	r.addField("attribute3", form.getAttribute3());
	r.addField("attribute4", form.getAttribute4());
	r.addField("attribute5", form.getAttribute5());
	r.addField("livrariRStart", form.getLivrariRStart());
	r.addField("livrariREnd", form.getLivrariREnd());
	r.addField("livrariCuMontaj", form.getLivrariCuMontaj());
	r.addField("incasariFromDate", form.getIncasariFromDate());
	r.addField("incasariToDate", form.getIncasariToDate());
	r.addField("offerItemId", form.getOfferItemId());
	r.addField("productName", form.getProductName());
	r.addField("productCode", form.getProductCode());
	r.addField("price", form.getPrice());
	r.addField("productPrice", form.getProductPrice());
	r.addField("priceRatio", form.getPriceRatio());
	r.addField("quantity", form.getQuantity());
	r.addField("value", form.getValue());
	r.addField("tax", form.getTax());
	r.addField("codMontaj", form.getCodMontaj());
	r.addField("montajProcent", form.getMontajProcent());
	r.addField("montajSeparat", form.getMontajSeparat());
	r.addField("proformaNumber", form.getProformaNumber());
	r.addField("proformaDate", form.getProformaDate());
	r.addField("proformaRole", form.getProformaRole());
	r.addField("proformaAmount", form.getProformaAmount());
	r.addField("proformaTax", form.getProformaTax());
	r.addField("proformaExchangeRate", form.getProformaExchangeRate());
	r.addField("proformaTotal", form.getProformaTotal());
	r.addField("proformaPercent", form.getProformaPercent());
	r.addField("proformaUsePercent", form.getProformaUsePercent());
	r.addField("proformaComment", form.getProformaComment());
	r.addField("proformaContract", form.getProformaContract());
	r.addField("proformaObiectiv", form.getProformaObiectiv());
	r.addField("proformaCurrency", form.getProformaCurrency());
	r.addField("proformaAmountCurrency", form.getProformaAmountCurrency());
	r.addField("proformaTaxCurrency", form.getProformaTaxCurrency());
	r.addField("proformaTotalCurrency", form.getProformaTotalCurrency());
	r.addField("proformaAttribute1", form.getProformaAttribute1());
	r.addField("proformaAttribute2", form.getProformaAttribute2());
	r.addField("proformaAttribute3", form.getProformaAttribute3());
	r.addField("proformaAttribute4", form.getProformaAttribute4());
	r.addField("proformaAttribute5", form.getProformaAttribute5());
	r.addField("proformaAttribute6", form.getProformaAttribute6());
	r.addField("proformaAttribute7", form.getProformaAttribute7());
	r.addField("proformaAttribute8", form.getProformaAttribute8());
	r.addField("proformaAttribute9", form.getProformaAttribute9());
	r.addField("proformaAttribute10", form.getProformaAttribute10());
	r.addField("proformaAttribute11", form.getProformaAttribute11());
	r.addField("proformaAttribute12", form.getProformaAttribute12());
	r.addField("proformaAttribute13", form.getProformaAttribute13());
	r.addField("proformaAttribute14", form.getProformaAttribute14());
	r.addField("proformaAttribute15", form.getProformaAttribute15());
	r.addField("invoiceNumber", form.getInvoiceNumber());
	r.addField("invoiceDate", form.getInvoiceDate());
	r.addField("invoiceRole", form.getInvoiceRole());
	r.addField("invoiceAmount", form.getInvoiceAmount());
	r.addField("invoiceTax", form.getInvoiceTax());
	r.addField("invoiceExchangeRate", form.getInvoiceExchangeRate());
	r.addField("invoiceTotal", form.getInvoiceTotal());
	r.addField("invoicePayed", form.getInvoicePayed());
	r.addField("invoiceUnpayed", form.getInvoiceUnpayed());
	r.addField("paymentNumber", form.getPaymentNumber());
	r.addField("paymentDate", form.getPaymentDate());
	r.addField("paymentAmount", form.getPaymentAmount());
	r.addField("paymentExchangeRate", form.getPaymentExchangeRate());
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
	    s.setVar("number", form.getNumber(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: number from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("date", form.getDate(), java.util.Date.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: date from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("clientId", form.getClientId(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: clientId from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("clientName", form.getClientName(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: clientName from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("montaj", form.getMontaj(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: montaj from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("localitate", form.getLocalitate(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: localitate from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("localitateAlta", form.getLocalitateAlta(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: localitateAlta from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("distanta", form.getDistanta(), java.math.BigDecimal.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: distanta from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("observatii", form.getObservatii(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: observatii from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("valoareMontaj", form.getValoareMontaj(), java.math.BigDecimal.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: valoareMontaj from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("valoareTransport", form.getValoareTransport(), java.math.BigDecimal.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: valoareTransport from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("valoareProduse", form.getValoareProduse(), java.math.BigDecimal.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: valoareProduse from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("total", form.getTotal(), java.math.BigDecimal.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: total from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("tvaPercent", form.getTvaPercent(), Double.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: tvaPercent from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("totalTva", form.getTotalTva(), java.math.BigDecimal.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: totalTva from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("discount", form.getDiscount(), java.math.BigDecimal.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: discount from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("totalFinal", form.getTotalFinal(), java.math.BigDecimal.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: totalFinal from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("totalFinalTva", form.getTotalFinalTva(), java.math.BigDecimal.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: totalFinalTva from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("avans", form.getAvans(), java.math.BigDecimal.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: avans from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("achitatCu", form.getAchitatCu(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: achitatCu from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("valoareAvans", form.getValoareAvans(), java.math.BigDecimal.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: valoareAvans from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("payedAmount", form.getPayedAmount(), java.math.BigDecimal.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: payedAmount from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("invoicedAmount", form.getInvoicedAmount(), java.math.BigDecimal.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: invoicedAmount from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("totalCurrency", form.getTotalCurrency(), java.math.BigDecimal.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: totalCurrency from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("totalTvaCurrency", form.getTotalTvaCurrency(), java.math.BigDecimal.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: totalTvaCurrency from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("totalFinalCurrency", form.getTotalFinalCurrency(), java.math.BigDecimal.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: totalFinalCurrency from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("totalFinalTvaCurrency", form.getTotalFinalTvaCurrency(), java.math.BigDecimal.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: totalFinalTvaCurrency from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("avansCurrency", form.getAvansCurrency(), java.math.BigDecimal.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: avansCurrency from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("payedAmountCurrency", form.getPayedAmountCurrency(), java.math.BigDecimal.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: payedAmountCurrency from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("currencyPayedAmount", form.getCurrencyPayedAmount(), java.math.BigDecimal.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: currencyPayedAmount from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("currencyInvoicedAmount", form.getCurrencyInvoicedAmount(), java.math.BigDecimal.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: currencyInvoicedAmount from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("diferenta", form.getDiferenta(), java.math.BigDecimal.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: diferenta from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("currencyDiferenta", form.getCurrencyDiferenta(), java.math.BigDecimal.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: currencyDiferenta from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("currencyCode", form.getCurrencyCode(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: currencyCode from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("exchangeRate", form.getExchangeRate(), java.math.BigDecimal.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: exchangeRate from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("termenLivrare", form.getTermenLivrare(), java.util.Date.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: termenLivrare from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("termenLivrare1", form.getTermenLivrare1(), java.util.Date.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: termenLivrare1 from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("adresaMontaj", form.getAdresaMontaj(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: adresaMontaj from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("adresaReper", form.getAdresaReper(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: adresaReper from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("telefon", form.getTelefon(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: telefon from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("contact", form.getContact(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: contact from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("deliveryHour", form.getDeliveryHour(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: deliveryHour from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("tipDemontare", form.getTipDemontare(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: tipDemontare from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("attribute1", form.getAttribute1(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: attribute1 from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("attribute2", form.getAttribute2(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: attribute2 from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("attribute3", form.getAttribute3(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: attribute3 from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("attribute4", form.getAttribute4(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: attribute4 from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("attribute5", form.getAttribute5(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: attribute5 from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("livrariRStart", form.getLivrariRStart(), java.util.Date.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: livrariRStart from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("livrariREnd", form.getLivrariREnd(), java.util.Date.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: livrariREnd from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("livrariCuMontaj", form.getLivrariCuMontaj(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: livrariCuMontaj from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("incasariFromDate", form.getIncasariFromDate(), java.util.Date.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: incasariFromDate from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("incasariToDate", form.getIncasariToDate(), java.util.Date.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: incasariToDate from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("offerItemId", form.getOfferItemId(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: offerItemId from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("productName", form.getProductName(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: productName from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("productCode", form.getProductCode(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: productCode from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("price", form.getPrice(), java.math.BigDecimal.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: price from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("productPrice", form.getProductPrice(), java.math.BigDecimal.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: productPrice from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("priceRatio", form.getPriceRatio(), Double.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: priceRatio from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("quantity", form.getQuantity(), java.math.BigDecimal.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: quantity from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("value", form.getValue(), java.math.BigDecimal.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: value from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("tax", form.getTax(), java.math.BigDecimal.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: tax from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("codMontaj", form.getCodMontaj(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: codMontaj from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("montajProcent", form.getMontajProcent(), Double.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: montajProcent from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("montajSeparat", form.getMontajSeparat(), Boolean.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: montajSeparat from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("proformaNumber", form.getProformaNumber(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: proformaNumber from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("proformaDate", form.getProformaDate(), java.util.Date.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: proformaDate from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("proformaRole", form.getProformaRole(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: proformaRole from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("proformaAmount", form.getProformaAmount(), java.math.BigDecimal.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: proformaAmount from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("proformaTax", form.getProformaTax(), java.math.BigDecimal.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: proformaTax from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("proformaExchangeRate", form.getProformaExchangeRate(), Double.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: proformaExchangeRate from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("proformaTotal", form.getProformaTotal(), java.math.BigDecimal.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: proformaTotal from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("proformaPercent", form.getProformaPercent(), Double.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: proformaPercent from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("proformaUsePercent", form.getProformaUsePercent(), Boolean.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: proformaUsePercent from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("proformaComment", form.getProformaComment(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: proformaComment from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("proformaContract", form.getProformaContract(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: proformaContract from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("proformaObiectiv", form.getProformaObiectiv(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: proformaObiectiv from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("proformaCurrency", form.getProformaCurrency(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: proformaCurrency from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("proformaAmountCurrency", form.getProformaAmountCurrency(), java.math.BigDecimal.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: proformaAmountCurrency from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("proformaTaxCurrency", form.getProformaTaxCurrency(), java.math.BigDecimal.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: proformaTaxCurrency from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("proformaTotalCurrency", form.getProformaTotalCurrency(), java.math.BigDecimal.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: proformaTotalCurrency from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("proformaAttribute1", form.getProformaAttribute1(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: proformaAttribute1 from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("proformaAttribute2", form.getProformaAttribute2(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: proformaAttribute2 from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("proformaAttribute3", form.getProformaAttribute3(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: proformaAttribute3 from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("proformaAttribute4", form.getProformaAttribute4(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: proformaAttribute4 from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("proformaAttribute5", form.getProformaAttribute5(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: proformaAttribute5 from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("proformaAttribute6", form.getProformaAttribute6(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: proformaAttribute6 from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("proformaAttribute7", form.getProformaAttribute7(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: proformaAttribute7 from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("proformaAttribute8", form.getProformaAttribute8(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: proformaAttribute8 from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("proformaAttribute9", form.getProformaAttribute9(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: proformaAttribute9 from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("proformaAttribute10", form.getProformaAttribute10(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: proformaAttribute10 from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("proformaAttribute11", form.getProformaAttribute11(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: proformaAttribute11 from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("proformaAttribute12", form.getProformaAttribute12(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: proformaAttribute12 from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("proformaAttribute13", form.getProformaAttribute13(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: proformaAttribute13 from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("proformaAttribute14", form.getProformaAttribute14(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: proformaAttribute14 from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("proformaAttribute15", form.getProformaAttribute15(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: proformaAttribute15 from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("invoiceNumber", form.getInvoiceNumber(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: invoiceNumber from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("invoiceDate", form.getInvoiceDate(), java.util.Date.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: invoiceDate from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("invoiceRole", form.getInvoiceRole(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: invoiceRole from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("invoiceAmount", form.getInvoiceAmount(), java.math.BigDecimal.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: invoiceAmount from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("invoiceTax", form.getInvoiceTax(), java.math.BigDecimal.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: invoiceTax from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("invoiceExchangeRate", form.getInvoiceExchangeRate(), Double.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: invoiceExchangeRate from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("invoiceTotal", form.getInvoiceTotal(), java.math.BigDecimal.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: invoiceTotal from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("invoicePayed", form.getInvoicePayed(), java.math.BigDecimal.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: invoicePayed from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("invoiceUnpayed", form.getInvoiceUnpayed(), java.math.BigDecimal.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: invoiceUnpayed from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("paymentNumber", form.getPaymentNumber(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: paymentNumber from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("paymentDate", form.getPaymentDate(), java.util.Date.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: paymentDate from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("paymentAmount", form.getPaymentAmount(), java.math.BigDecimal.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: paymentAmount from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("paymentExchangeRate", form.getPaymentExchangeRate(), Double.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: paymentExchangeRate from the script");
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
	    field = s.getVar("number", String.class);
	    if(!field.equals(form.getNumber())) {
	        logger.log(BasicLevel.DEBUG, "Field number modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setNumber((String)field);
	        r.addField("number", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: number from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("date", java.util.Date.class);
	    if(!field.equals(form.getDate())) {
	        logger.log(BasicLevel.DEBUG, "Field date modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setDate((java.util.Date)field);
	        r.addField("date", (java.util.Date)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: date from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("clientId", Integer.class);
	    if(!field.equals(form.getClientId())) {
	        logger.log(BasicLevel.DEBUG, "Field clientId modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setClientId((Integer)field);
	        r.addField("clientId", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: clientId from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("clientName", String.class);
	    if(!field.equals(form.getClientName())) {
	        logger.log(BasicLevel.DEBUG, "Field clientName modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setClientName((String)field);
	        r.addField("clientName", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: clientName from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("montaj", Integer.class);
	    if(!field.equals(form.getMontaj())) {
	        logger.log(BasicLevel.DEBUG, "Field montaj modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setMontaj((Integer)field);
	        r.addField("montaj", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: montaj from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("localitate", Integer.class);
	    if(!field.equals(form.getLocalitate())) {
	        logger.log(BasicLevel.DEBUG, "Field localitate modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setLocalitate((Integer)field);
	        r.addField("localitate", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: localitate from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("localitateAlta", String.class);
	    if(!field.equals(form.getLocalitateAlta())) {
	        logger.log(BasicLevel.DEBUG, "Field localitateAlta modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setLocalitateAlta((String)field);
	        r.addField("localitateAlta", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: localitateAlta from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("distanta", java.math.BigDecimal.class);
	    if(!field.equals(form.getDistanta())) {
	        logger.log(BasicLevel.DEBUG, "Field distanta modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setDistanta((java.math.BigDecimal)field);
	        r.addField("distanta", (java.math.BigDecimal)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: distanta from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("observatii", String.class);
	    if(!field.equals(form.getObservatii())) {
	        logger.log(BasicLevel.DEBUG, "Field observatii modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setObservatii((String)field);
	        r.addField("observatii", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: observatii from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("valoareMontaj", java.math.BigDecimal.class);
	    if(!field.equals(form.getValoareMontaj())) {
	        logger.log(BasicLevel.DEBUG, "Field valoareMontaj modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setValoareMontaj((java.math.BigDecimal)field);
	        r.addField("valoareMontaj", (java.math.BigDecimal)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: valoareMontaj from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("valoareTransport", java.math.BigDecimal.class);
	    if(!field.equals(form.getValoareTransport())) {
	        logger.log(BasicLevel.DEBUG, "Field valoareTransport modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setValoareTransport((java.math.BigDecimal)field);
	        r.addField("valoareTransport", (java.math.BigDecimal)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: valoareTransport from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("valoareProduse", java.math.BigDecimal.class);
	    if(!field.equals(form.getValoareProduse())) {
	        logger.log(BasicLevel.DEBUG, "Field valoareProduse modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setValoareProduse((java.math.BigDecimal)field);
	        r.addField("valoareProduse", (java.math.BigDecimal)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: valoareProduse from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("total", java.math.BigDecimal.class);
	    if(!field.equals(form.getTotal())) {
	        logger.log(BasicLevel.DEBUG, "Field total modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setTotal((java.math.BigDecimal)field);
	        r.addField("total", (java.math.BigDecimal)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: total from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("tvaPercent", Double.class);
	    if(!field.equals(form.getTvaPercent())) {
	        logger.log(BasicLevel.DEBUG, "Field tvaPercent modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setTvaPercent((Double)field);
	        r.addField("tvaPercent", (Double)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: tvaPercent from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("totalTva", java.math.BigDecimal.class);
	    if(!field.equals(form.getTotalTva())) {
	        logger.log(BasicLevel.DEBUG, "Field totalTva modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setTotalTva((java.math.BigDecimal)field);
	        r.addField("totalTva", (java.math.BigDecimal)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: totalTva from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("discount", java.math.BigDecimal.class);
	    if(!field.equals(form.getDiscount())) {
	        logger.log(BasicLevel.DEBUG, "Field discount modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setDiscount((java.math.BigDecimal)field);
	        r.addField("discount", (java.math.BigDecimal)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: discount from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("totalFinal", java.math.BigDecimal.class);
	    if(!field.equals(form.getTotalFinal())) {
	        logger.log(BasicLevel.DEBUG, "Field totalFinal modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setTotalFinal((java.math.BigDecimal)field);
	        r.addField("totalFinal", (java.math.BigDecimal)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: totalFinal from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("totalFinalTva", java.math.BigDecimal.class);
	    if(!field.equals(form.getTotalFinalTva())) {
	        logger.log(BasicLevel.DEBUG, "Field totalFinalTva modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setTotalFinalTva((java.math.BigDecimal)field);
	        r.addField("totalFinalTva", (java.math.BigDecimal)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: totalFinalTva from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("avans", java.math.BigDecimal.class);
	    if(!field.equals(form.getAvans())) {
	        logger.log(BasicLevel.DEBUG, "Field avans modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setAvans((java.math.BigDecimal)field);
	        r.addField("avans", (java.math.BigDecimal)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: avans from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("achitatCu", String.class);
	    if(!field.equals(form.getAchitatCu())) {
	        logger.log(BasicLevel.DEBUG, "Field achitatCu modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setAchitatCu((String)field);
	        r.addField("achitatCu", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: achitatCu from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("valoareAvans", java.math.BigDecimal.class);
	    if(!field.equals(form.getValoareAvans())) {
	        logger.log(BasicLevel.DEBUG, "Field valoareAvans modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setValoareAvans((java.math.BigDecimal)field);
	        r.addField("valoareAvans", (java.math.BigDecimal)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: valoareAvans from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("payedAmount", java.math.BigDecimal.class);
	    if(!field.equals(form.getPayedAmount())) {
	        logger.log(BasicLevel.DEBUG, "Field payedAmount modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setPayedAmount((java.math.BigDecimal)field);
	        r.addField("payedAmount", (java.math.BigDecimal)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: payedAmount from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("invoicedAmount", java.math.BigDecimal.class);
	    if(!field.equals(form.getInvoicedAmount())) {
	        logger.log(BasicLevel.DEBUG, "Field invoicedAmount modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setInvoicedAmount((java.math.BigDecimal)field);
	        r.addField("invoicedAmount", (java.math.BigDecimal)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: invoicedAmount from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("totalCurrency", java.math.BigDecimal.class);
	    if(!field.equals(form.getTotalCurrency())) {
	        logger.log(BasicLevel.DEBUG, "Field totalCurrency modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setTotalCurrency((java.math.BigDecimal)field);
	        r.addField("totalCurrency", (java.math.BigDecimal)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: totalCurrency from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("totalTvaCurrency", java.math.BigDecimal.class);
	    if(!field.equals(form.getTotalTvaCurrency())) {
	        logger.log(BasicLevel.DEBUG, "Field totalTvaCurrency modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setTotalTvaCurrency((java.math.BigDecimal)field);
	        r.addField("totalTvaCurrency", (java.math.BigDecimal)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: totalTvaCurrency from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("totalFinalCurrency", java.math.BigDecimal.class);
	    if(!field.equals(form.getTotalFinalCurrency())) {
	        logger.log(BasicLevel.DEBUG, "Field totalFinalCurrency modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setTotalFinalCurrency((java.math.BigDecimal)field);
	        r.addField("totalFinalCurrency", (java.math.BigDecimal)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: totalFinalCurrency from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("totalFinalTvaCurrency", java.math.BigDecimal.class);
	    if(!field.equals(form.getTotalFinalTvaCurrency())) {
	        logger.log(BasicLevel.DEBUG, "Field totalFinalTvaCurrency modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setTotalFinalTvaCurrency((java.math.BigDecimal)field);
	        r.addField("totalFinalTvaCurrency", (java.math.BigDecimal)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: totalFinalTvaCurrency from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("avansCurrency", java.math.BigDecimal.class);
	    if(!field.equals(form.getAvansCurrency())) {
	        logger.log(BasicLevel.DEBUG, "Field avansCurrency modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setAvansCurrency((java.math.BigDecimal)field);
	        r.addField("avansCurrency", (java.math.BigDecimal)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: avansCurrency from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("payedAmountCurrency", java.math.BigDecimal.class);
	    if(!field.equals(form.getPayedAmountCurrency())) {
	        logger.log(BasicLevel.DEBUG, "Field payedAmountCurrency modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setPayedAmountCurrency((java.math.BigDecimal)field);
	        r.addField("payedAmountCurrency", (java.math.BigDecimal)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: payedAmountCurrency from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("currencyPayedAmount", java.math.BigDecimal.class);
	    if(!field.equals(form.getCurrencyPayedAmount())) {
	        logger.log(BasicLevel.DEBUG, "Field currencyPayedAmount modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setCurrencyPayedAmount((java.math.BigDecimal)field);
	        r.addField("currencyPayedAmount", (java.math.BigDecimal)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: currencyPayedAmount from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("currencyInvoicedAmount", java.math.BigDecimal.class);
	    if(!field.equals(form.getCurrencyInvoicedAmount())) {
	        logger.log(BasicLevel.DEBUG, "Field currencyInvoicedAmount modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setCurrencyInvoicedAmount((java.math.BigDecimal)field);
	        r.addField("currencyInvoicedAmount", (java.math.BigDecimal)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: currencyInvoicedAmount from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("diferenta", java.math.BigDecimal.class);
	    if(!field.equals(form.getDiferenta())) {
	        logger.log(BasicLevel.DEBUG, "Field diferenta modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setDiferenta((java.math.BigDecimal)field);
	        r.addField("diferenta", (java.math.BigDecimal)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: diferenta from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("currencyDiferenta", java.math.BigDecimal.class);
	    if(!field.equals(form.getCurrencyDiferenta())) {
	        logger.log(BasicLevel.DEBUG, "Field currencyDiferenta modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setCurrencyDiferenta((java.math.BigDecimal)field);
	        r.addField("currencyDiferenta", (java.math.BigDecimal)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: currencyDiferenta from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("currencyCode", String.class);
	    if(!field.equals(form.getCurrencyCode())) {
	        logger.log(BasicLevel.DEBUG, "Field currencyCode modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setCurrencyCode((String)field);
	        r.addField("currencyCode", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: currencyCode from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("exchangeRate", java.math.BigDecimal.class);
	    if(!field.equals(form.getExchangeRate())) {
	        logger.log(BasicLevel.DEBUG, "Field exchangeRate modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setExchangeRate((java.math.BigDecimal)field);
	        r.addField("exchangeRate", (java.math.BigDecimal)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: exchangeRate from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("termenLivrare", java.util.Date.class);
	    if(!field.equals(form.getTermenLivrare())) {
	        logger.log(BasicLevel.DEBUG, "Field termenLivrare modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setTermenLivrare((java.util.Date)field);
	        r.addField("termenLivrare", (java.util.Date)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: termenLivrare from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("termenLivrare1", java.util.Date.class);
	    if(!field.equals(form.getTermenLivrare1())) {
	        logger.log(BasicLevel.DEBUG, "Field termenLivrare1 modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setTermenLivrare1((java.util.Date)field);
	        r.addField("termenLivrare1", (java.util.Date)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: termenLivrare1 from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("adresaMontaj", String.class);
	    if(!field.equals(form.getAdresaMontaj())) {
	        logger.log(BasicLevel.DEBUG, "Field adresaMontaj modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setAdresaMontaj((String)field);
	        r.addField("adresaMontaj", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: adresaMontaj from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("adresaReper", String.class);
	    if(!field.equals(form.getAdresaReper())) {
	        logger.log(BasicLevel.DEBUG, "Field adresaReper modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setAdresaReper((String)field);
	        r.addField("adresaReper", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: adresaReper from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("telefon", String.class);
	    if(!field.equals(form.getTelefon())) {
	        logger.log(BasicLevel.DEBUG, "Field telefon modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setTelefon((String)field);
	        r.addField("telefon", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: telefon from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("contact", String.class);
	    if(!field.equals(form.getContact())) {
	        logger.log(BasicLevel.DEBUG, "Field contact modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setContact((String)field);
	        r.addField("contact", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: contact from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("deliveryHour", String.class);
	    if(!field.equals(form.getDeliveryHour())) {
	        logger.log(BasicLevel.DEBUG, "Field deliveryHour modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setDeliveryHour((String)field);
	        r.addField("deliveryHour", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: deliveryHour from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("tipDemontare", String.class);
	    if(!field.equals(form.getTipDemontare())) {
	        logger.log(BasicLevel.DEBUG, "Field tipDemontare modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setTipDemontare((String)field);
	        r.addField("tipDemontare", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: tipDemontare from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("attribute1", String.class);
	    if(!field.equals(form.getAttribute1())) {
	        logger.log(BasicLevel.DEBUG, "Field attribute1 modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setAttribute1((String)field);
	        r.addField("attribute1", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: attribute1 from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("attribute2", String.class);
	    if(!field.equals(form.getAttribute2())) {
	        logger.log(BasicLevel.DEBUG, "Field attribute2 modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setAttribute2((String)field);
	        r.addField("attribute2", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: attribute2 from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("attribute3", String.class);
	    if(!field.equals(form.getAttribute3())) {
	        logger.log(BasicLevel.DEBUG, "Field attribute3 modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setAttribute3((String)field);
	        r.addField("attribute3", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: attribute3 from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("attribute4", String.class);
	    if(!field.equals(form.getAttribute4())) {
	        logger.log(BasicLevel.DEBUG, "Field attribute4 modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setAttribute4((String)field);
	        r.addField("attribute4", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: attribute4 from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("attribute5", String.class);
	    if(!field.equals(form.getAttribute5())) {
	        logger.log(BasicLevel.DEBUG, "Field attribute5 modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setAttribute5((String)field);
	        r.addField("attribute5", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: attribute5 from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("livrariRStart", java.util.Date.class);
	    if(!field.equals(form.getLivrariRStart())) {
	        logger.log(BasicLevel.DEBUG, "Field livrariRStart modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setLivrariRStart((java.util.Date)field);
	        r.addField("livrariRStart", (java.util.Date)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: livrariRStart from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("livrariREnd", java.util.Date.class);
	    if(!field.equals(form.getLivrariREnd())) {
	        logger.log(BasicLevel.DEBUG, "Field livrariREnd modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setLivrariREnd((java.util.Date)field);
	        r.addField("livrariREnd", (java.util.Date)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: livrariREnd from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("livrariCuMontaj", String.class);
	    if(!field.equals(form.getLivrariCuMontaj())) {
	        logger.log(BasicLevel.DEBUG, "Field livrariCuMontaj modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setLivrariCuMontaj((String)field);
	        r.addField("livrariCuMontaj", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: livrariCuMontaj from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("incasariFromDate", java.util.Date.class);
	    if(!field.equals(form.getIncasariFromDate())) {
	        logger.log(BasicLevel.DEBUG, "Field incasariFromDate modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setIncasariFromDate((java.util.Date)field);
	        r.addField("incasariFromDate", (java.util.Date)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: incasariFromDate from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("incasariToDate", java.util.Date.class);
	    if(!field.equals(form.getIncasariToDate())) {
	        logger.log(BasicLevel.DEBUG, "Field incasariToDate modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setIncasariToDate((java.util.Date)field);
	        r.addField("incasariToDate", (java.util.Date)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: incasariToDate from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("offerItemId", Integer.class);
	    if(!field.equals(form.getOfferItemId())) {
	        logger.log(BasicLevel.DEBUG, "Field offerItemId modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setOfferItemId((Integer)field);
	        r.addField("offerItemId", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: offerItemId from the script");
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
	    field = s.getVar("productPrice", java.math.BigDecimal.class);
	    if(!field.equals(form.getProductPrice())) {
	        logger.log(BasicLevel.DEBUG, "Field productPrice modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setProductPrice((java.math.BigDecimal)field);
	        r.addField("productPrice", (java.math.BigDecimal)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: productPrice from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("priceRatio", Double.class);
	    if(!field.equals(form.getPriceRatio())) {
	        logger.log(BasicLevel.DEBUG, "Field priceRatio modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setPriceRatio((Double)field);
	        r.addField("priceRatio", (Double)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: priceRatio from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("quantity", java.math.BigDecimal.class);
	    if(!field.equals(form.getQuantity())) {
	        logger.log(BasicLevel.DEBUG, "Field quantity modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setQuantity((java.math.BigDecimal)field);
	        r.addField("quantity", (java.math.BigDecimal)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: quantity from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("value", java.math.BigDecimal.class);
	    if(!field.equals(form.getValue())) {
	        logger.log(BasicLevel.DEBUG, "Field value modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setValue((java.math.BigDecimal)field);
	        r.addField("value", (java.math.BigDecimal)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: value from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("tax", java.math.BigDecimal.class);
	    if(!field.equals(form.getTax())) {
	        logger.log(BasicLevel.DEBUG, "Field tax modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setTax((java.math.BigDecimal)field);
	        r.addField("tax", (java.math.BigDecimal)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: tax from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("codMontaj", Integer.class);
	    if(!field.equals(form.getCodMontaj())) {
	        logger.log(BasicLevel.DEBUG, "Field codMontaj modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setCodMontaj((Integer)field);
	        r.addField("codMontaj", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: codMontaj from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("montajProcent", Double.class);
	    if(!field.equals(form.getMontajProcent())) {
	        logger.log(BasicLevel.DEBUG, "Field montajProcent modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setMontajProcent((Double)field);
	        r.addField("montajProcent", (Double)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: montajProcent from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("montajSeparat", Boolean.class);
	    if(!field.equals(form.getMontajSeparat())) {
	        logger.log(BasicLevel.DEBUG, "Field montajSeparat modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setMontajSeparat((Boolean)field);
	        r.addField("montajSeparat", (Boolean)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: montajSeparat from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("proformaNumber", String.class);
	    if(!field.equals(form.getProformaNumber())) {
	        logger.log(BasicLevel.DEBUG, "Field proformaNumber modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setProformaNumber((String)field);
	        r.addField("proformaNumber", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: proformaNumber from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("proformaDate", java.util.Date.class);
	    if(!field.equals(form.getProformaDate())) {
	        logger.log(BasicLevel.DEBUG, "Field proformaDate modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setProformaDate((java.util.Date)field);
	        r.addField("proformaDate", (java.util.Date)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: proformaDate from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("proformaRole", String.class);
	    if(!field.equals(form.getProformaRole())) {
	        logger.log(BasicLevel.DEBUG, "Field proformaRole modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setProformaRole((String)field);
	        r.addField("proformaRole", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: proformaRole from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("proformaAmount", java.math.BigDecimal.class);
	    if(!field.equals(form.getProformaAmount())) {
	        logger.log(BasicLevel.DEBUG, "Field proformaAmount modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setProformaAmount((java.math.BigDecimal)field);
	        r.addField("proformaAmount", (java.math.BigDecimal)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: proformaAmount from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("proformaTax", java.math.BigDecimal.class);
	    if(!field.equals(form.getProformaTax())) {
	        logger.log(BasicLevel.DEBUG, "Field proformaTax modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setProformaTax((java.math.BigDecimal)field);
	        r.addField("proformaTax", (java.math.BigDecimal)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: proformaTax from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("proformaExchangeRate", Double.class);
	    if(!field.equals(form.getProformaExchangeRate())) {
	        logger.log(BasicLevel.DEBUG, "Field proformaExchangeRate modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setProformaExchangeRate((Double)field);
	        r.addField("proformaExchangeRate", (Double)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: proformaExchangeRate from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("proformaTotal", java.math.BigDecimal.class);
	    if(!field.equals(form.getProformaTotal())) {
	        logger.log(BasicLevel.DEBUG, "Field proformaTotal modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setProformaTotal((java.math.BigDecimal)field);
	        r.addField("proformaTotal", (java.math.BigDecimal)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: proformaTotal from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("proformaPercent", Double.class);
	    if(!field.equals(form.getProformaPercent())) {
	        logger.log(BasicLevel.DEBUG, "Field proformaPercent modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setProformaPercent((Double)field);
	        r.addField("proformaPercent", (Double)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: proformaPercent from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("proformaUsePercent", Boolean.class);
	    if(!field.equals(form.getProformaUsePercent())) {
	        logger.log(BasicLevel.DEBUG, "Field proformaUsePercent modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setProformaUsePercent((Boolean)field);
	        r.addField("proformaUsePercent", (Boolean)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: proformaUsePercent from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("proformaComment", String.class);
	    if(!field.equals(form.getProformaComment())) {
	        logger.log(BasicLevel.DEBUG, "Field proformaComment modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setProformaComment((String)field);
	        r.addField("proformaComment", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: proformaComment from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("proformaContract", String.class);
	    if(!field.equals(form.getProformaContract())) {
	        logger.log(BasicLevel.DEBUG, "Field proformaContract modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setProformaContract((String)field);
	        r.addField("proformaContract", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: proformaContract from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("proformaObiectiv", String.class);
	    if(!field.equals(form.getProformaObiectiv())) {
	        logger.log(BasicLevel.DEBUG, "Field proformaObiectiv modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setProformaObiectiv((String)field);
	        r.addField("proformaObiectiv", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: proformaObiectiv from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("proformaCurrency", String.class);
	    if(!field.equals(form.getProformaCurrency())) {
	        logger.log(BasicLevel.DEBUG, "Field proformaCurrency modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setProformaCurrency((String)field);
	        r.addField("proformaCurrency", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: proformaCurrency from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("proformaAmountCurrency", java.math.BigDecimal.class);
	    if(!field.equals(form.getProformaAmountCurrency())) {
	        logger.log(BasicLevel.DEBUG, "Field proformaAmountCurrency modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setProformaAmountCurrency((java.math.BigDecimal)field);
	        r.addField("proformaAmountCurrency", (java.math.BigDecimal)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: proformaAmountCurrency from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("proformaTaxCurrency", java.math.BigDecimal.class);
	    if(!field.equals(form.getProformaTaxCurrency())) {
	        logger.log(BasicLevel.DEBUG, "Field proformaTaxCurrency modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setProformaTaxCurrency((java.math.BigDecimal)field);
	        r.addField("proformaTaxCurrency", (java.math.BigDecimal)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: proformaTaxCurrency from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("proformaTotalCurrency", java.math.BigDecimal.class);
	    if(!field.equals(form.getProformaTotalCurrency())) {
	        logger.log(BasicLevel.DEBUG, "Field proformaTotalCurrency modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setProformaTotalCurrency((java.math.BigDecimal)field);
	        r.addField("proformaTotalCurrency", (java.math.BigDecimal)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: proformaTotalCurrency from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("proformaAttribute1", String.class);
	    if(!field.equals(form.getProformaAttribute1())) {
	        logger.log(BasicLevel.DEBUG, "Field proformaAttribute1 modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setProformaAttribute1((String)field);
	        r.addField("proformaAttribute1", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: proformaAttribute1 from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("proformaAttribute2", String.class);
	    if(!field.equals(form.getProformaAttribute2())) {
	        logger.log(BasicLevel.DEBUG, "Field proformaAttribute2 modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setProformaAttribute2((String)field);
	        r.addField("proformaAttribute2", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: proformaAttribute2 from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("proformaAttribute3", String.class);
	    if(!field.equals(form.getProformaAttribute3())) {
	        logger.log(BasicLevel.DEBUG, "Field proformaAttribute3 modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setProformaAttribute3((String)field);
	        r.addField("proformaAttribute3", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: proformaAttribute3 from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("proformaAttribute4", String.class);
	    if(!field.equals(form.getProformaAttribute4())) {
	        logger.log(BasicLevel.DEBUG, "Field proformaAttribute4 modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setProformaAttribute4((String)field);
	        r.addField("proformaAttribute4", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: proformaAttribute4 from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("proformaAttribute5", String.class);
	    if(!field.equals(form.getProformaAttribute5())) {
	        logger.log(BasicLevel.DEBUG, "Field proformaAttribute5 modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setProformaAttribute5((String)field);
	        r.addField("proformaAttribute5", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: proformaAttribute5 from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("proformaAttribute6", String.class);
	    if(!field.equals(form.getProformaAttribute6())) {
	        logger.log(BasicLevel.DEBUG, "Field proformaAttribute6 modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setProformaAttribute6((String)field);
	        r.addField("proformaAttribute6", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: proformaAttribute6 from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("proformaAttribute7", String.class);
	    if(!field.equals(form.getProformaAttribute7())) {
	        logger.log(BasicLevel.DEBUG, "Field proformaAttribute7 modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setProformaAttribute7((String)field);
	        r.addField("proformaAttribute7", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: proformaAttribute7 from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("proformaAttribute8", String.class);
	    if(!field.equals(form.getProformaAttribute8())) {
	        logger.log(BasicLevel.DEBUG, "Field proformaAttribute8 modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setProformaAttribute8((String)field);
	        r.addField("proformaAttribute8", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: proformaAttribute8 from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("proformaAttribute9", String.class);
	    if(!field.equals(form.getProformaAttribute9())) {
	        logger.log(BasicLevel.DEBUG, "Field proformaAttribute9 modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setProformaAttribute9((String)field);
	        r.addField("proformaAttribute9", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: proformaAttribute9 from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("proformaAttribute10", String.class);
	    if(!field.equals(form.getProformaAttribute10())) {
	        logger.log(BasicLevel.DEBUG, "Field proformaAttribute10 modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setProformaAttribute10((String)field);
	        r.addField("proformaAttribute10", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: proformaAttribute10 from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("proformaAttribute11", String.class);
	    if(!field.equals(form.getProformaAttribute11())) {
	        logger.log(BasicLevel.DEBUG, "Field proformaAttribute11 modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setProformaAttribute11((String)field);
	        r.addField("proformaAttribute11", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: proformaAttribute11 from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("proformaAttribute12", String.class);
	    if(!field.equals(form.getProformaAttribute12())) {
	        logger.log(BasicLevel.DEBUG, "Field proformaAttribute12 modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setProformaAttribute12((String)field);
	        r.addField("proformaAttribute12", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: proformaAttribute12 from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("proformaAttribute13", String.class);
	    if(!field.equals(form.getProformaAttribute13())) {
	        logger.log(BasicLevel.DEBUG, "Field proformaAttribute13 modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setProformaAttribute13((String)field);
	        r.addField("proformaAttribute13", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: proformaAttribute13 from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("proformaAttribute14", String.class);
	    if(!field.equals(form.getProformaAttribute14())) {
	        logger.log(BasicLevel.DEBUG, "Field proformaAttribute14 modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setProformaAttribute14((String)field);
	        r.addField("proformaAttribute14", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: proformaAttribute14 from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("proformaAttribute15", String.class);
	    if(!field.equals(form.getProformaAttribute15())) {
	        logger.log(BasicLevel.DEBUG, "Field proformaAttribute15 modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setProformaAttribute15((String)field);
	        r.addField("proformaAttribute15", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: proformaAttribute15 from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("invoiceNumber", String.class);
	    if(!field.equals(form.getInvoiceNumber())) {
	        logger.log(BasicLevel.DEBUG, "Field invoiceNumber modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setInvoiceNumber((String)field);
	        r.addField("invoiceNumber", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: invoiceNumber from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("invoiceDate", java.util.Date.class);
	    if(!field.equals(form.getInvoiceDate())) {
	        logger.log(BasicLevel.DEBUG, "Field invoiceDate modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setInvoiceDate((java.util.Date)field);
	        r.addField("invoiceDate", (java.util.Date)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: invoiceDate from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("invoiceRole", String.class);
	    if(!field.equals(form.getInvoiceRole())) {
	        logger.log(BasicLevel.DEBUG, "Field invoiceRole modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setInvoiceRole((String)field);
	        r.addField("invoiceRole", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: invoiceRole from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("invoiceAmount", java.math.BigDecimal.class);
	    if(!field.equals(form.getInvoiceAmount())) {
	        logger.log(BasicLevel.DEBUG, "Field invoiceAmount modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setInvoiceAmount((java.math.BigDecimal)field);
	        r.addField("invoiceAmount", (java.math.BigDecimal)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: invoiceAmount from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("invoiceTax", java.math.BigDecimal.class);
	    if(!field.equals(form.getInvoiceTax())) {
	        logger.log(BasicLevel.DEBUG, "Field invoiceTax modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setInvoiceTax((java.math.BigDecimal)field);
	        r.addField("invoiceTax", (java.math.BigDecimal)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: invoiceTax from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("invoiceExchangeRate", Double.class);
	    if(!field.equals(form.getInvoiceExchangeRate())) {
	        logger.log(BasicLevel.DEBUG, "Field invoiceExchangeRate modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setInvoiceExchangeRate((Double)field);
	        r.addField("invoiceExchangeRate", (Double)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: invoiceExchangeRate from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("invoiceTotal", java.math.BigDecimal.class);
	    if(!field.equals(form.getInvoiceTotal())) {
	        logger.log(BasicLevel.DEBUG, "Field invoiceTotal modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setInvoiceTotal((java.math.BigDecimal)field);
	        r.addField("invoiceTotal", (java.math.BigDecimal)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: invoiceTotal from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("invoicePayed", java.math.BigDecimal.class);
	    if(!field.equals(form.getInvoicePayed())) {
	        logger.log(BasicLevel.DEBUG, "Field invoicePayed modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setInvoicePayed((java.math.BigDecimal)field);
	        r.addField("invoicePayed", (java.math.BigDecimal)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: invoicePayed from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("invoiceUnpayed", java.math.BigDecimal.class);
	    if(!field.equals(form.getInvoiceUnpayed())) {
	        logger.log(BasicLevel.DEBUG, "Field invoiceUnpayed modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setInvoiceUnpayed((java.math.BigDecimal)field);
	        r.addField("invoiceUnpayed", (java.math.BigDecimal)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: invoiceUnpayed from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("paymentNumber", String.class);
	    if(!field.equals(form.getPaymentNumber())) {
	        logger.log(BasicLevel.DEBUG, "Field paymentNumber modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setPaymentNumber((String)field);
	        r.addField("paymentNumber", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: paymentNumber from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("paymentDate", java.util.Date.class);
	    if(!field.equals(form.getPaymentDate())) {
	        logger.log(BasicLevel.DEBUG, "Field paymentDate modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setPaymentDate((java.util.Date)field);
	        r.addField("paymentDate", (java.util.Date)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: paymentDate from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("paymentAmount", java.math.BigDecimal.class);
	    if(!field.equals(form.getPaymentAmount())) {
	        logger.log(BasicLevel.DEBUG, "Field paymentAmount modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setPaymentAmount((java.math.BigDecimal)field);
	        r.addField("paymentAmount", (java.math.BigDecimal)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: paymentAmount from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("paymentExchangeRate", Double.class);
	    if(!field.equals(form.getPaymentExchangeRate())) {
	        logger.log(BasicLevel.DEBUG, "Field paymentExchangeRate modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setPaymentExchangeRate((Double)field);
	        r.addField("paymentExchangeRate", (Double)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: paymentExchangeRate from the script");
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
             return "ro.kds.erp.biz.setum.basic.Orders";
         }
         
     }

}

