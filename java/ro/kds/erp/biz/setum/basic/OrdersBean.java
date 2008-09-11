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
        Integer itemId
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

		script.setVar("param_itemId", itemId, Integer.class);

		
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
	r.addField("diferenta", form.getDiferenta());
	r.addField("termenLivrare", form.getTermenLivrare());
	r.addField("termenLivrare1", form.getTermenLivrare1());
	r.addField("adresaMontaj", form.getAdresaMontaj());
	r.addField("adresaReper", form.getAdresaReper());
	r.addField("telefon", form.getTelefon());
	r.addField("contact", form.getContact());
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
	r.addField("invoiceNumber", form.getInvoiceNumber());
	r.addField("invoiceDate", form.getInvoiceDate());
	r.addField("invoiceRole", form.getInvoiceRole());
	r.addField("invoiceAmount", form.getInvoiceAmount());
	r.addField("invoiceTax", form.getInvoiceTax());
	r.addField("invoiceTotal", form.getInvoiceTotal());
	r.addField("invoicePayed", form.getInvoicePayed());
	r.addField("invoiceUnpayed", form.getInvoiceUnpayed());
	r.addField("paymentNumber", form.getPaymentNumber());
	r.addField("paymentDate", form.getPaymentDate());
	r.addField("paymentAmount", form.getPaymentAmount());
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
	    s.setVar("diferenta", form.getDiferenta(), java.math.BigDecimal.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: diferenta from the script");
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

