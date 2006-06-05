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

/**
 * Standard implementation of the GauriAerisire session bean.
 * You will have to extend this class to proivde code for the abstract
 * methods loadFormData, newForm and saveSaveFormData.
 */
public abstract class GauriAerisireBean 
	implements SessionBean {

    static protected Logger logger = null;
    protected SessionContext ejbContext;

    protected Integer id;
    final static String FORM_VARNAME = "form";
    final static String RESPONSE_VARNAME = "response";
    final static String LOGIC_VARNAME = "logic";
    final static String OLDVAL_VARNAME = "oldVal";


    // ------------------------------------------------------------------
    // SessionBean implementation
    // ------------------------------------------------------------------


    public void setSessionContext(SessionContext ctx) {
        if (logger == null) {
            logger = Monolog.getMonologFactory().getLogger("ro.kds.erp.biz.setum.basic.GauriAerisire");
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
    // GauriAerisire implementation
    // ------------------------------------------------------------------
    protected GauriAerisireForm form;
    
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
	logger.log(BasicLevel.DEBUG, "Loading GauriAerisire with id = " + loadId);
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
     * Create a new GauriAerisireForm and initialize the 
     * <code>this.form</code> instance variable. Overwrite this method
     * if you want to provide other code for initializing the form bean.
     */
    protected void createNewFormBean() {
	form = new GauriAerisireForm();
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
     * <code>ro.kds.erp.biz.setum.basic.GauriAerisire_calculatedFields</code>
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
		.loadScript("ro.kds.erp.biz.setum.basic.GauriAerisire_calculatedFields");
	if(script.loaded()) {
	    try {
		script.setVar(FORM_VARNAME, form, 
			      GauriAerisireForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);

		addFieldsToScript(script);
		script.run();

		getFieldsFromScript(script, r);

	    } catch (ScriptErrorException e) {
		logger.log(BasicLevel.ERROR, "Error executing rules script for GauriAerisire", e);
	    }
	}
	return r;
    }
	

    /**
     * Evaluates the validation script. The script loader loades the script
     * file corresponding to:
     *
     * <code>ro.kds.erp.biz.setum.basic.GauriAerisire_validation</code>
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
		.loadScript("ro.kds.erp.biz.setum.basic.GauriAerisire_validation");
	if(script.loaded()) {
	    try {
		script.setVar(FORM_VARNAME, form, 
			      GauriAerisireForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		addFieldsToScript(script);
		script.run();

		// I don't add modified fields back to the response bean,
		// so any fields modifications made in the script will
		// be ignored.
	    } catch (ScriptErrorException e) {
		logger.log(BasicLevel.ERROR, "Error executing rules script for GauriAerisire", e);
	    }
	}
	return r;
    }

    public ResponseBean updateDiametru(Double diametru) {
        ResponseBean r = new ResponseBean();
	Double oldVal = form.getDiametru();
	form.setDiametru(diametru);
	r.addRecord();
	r.addField("diametru", diametru); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.GauriAerisire.diametru");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, Double.class);
		script.setVar(FORM_VARNAME, form, GauriAerisireForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the diametru", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updatePas(Double pas) {
        ResponseBean r = new ResponseBean();
	Double oldVal = form.getPas();
	form.setPas(pas);
	r.addRecord();
	r.addField("pas", pas); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.GauriAerisire.pas");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, Double.class);
		script.setVar(FORM_VARNAME, form, GauriAerisireForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the pas", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateNrRanduri(Integer nrRanduri) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getNrRanduri();
	form.setNrRanduri(nrRanduri);
	r.addRecord();
	r.addField("nrRanduri", nrRanduri); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.GauriAerisire.nrRanduri");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, Integer.class);
		script.setVar(FORM_VARNAME, form, GauriAerisireForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the nrRanduri", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updatePozitionare1(String pozitionare1) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getPozitionare1();
	form.setPozitionare1(pozitionare1);
	r.addRecord();
	r.addField("pozitionare1", pozitionare1); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.GauriAerisire.pozitionare1");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, GauriAerisireForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the pozitionare1", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updatePozitionare2(String pozitionare2) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getPozitionare2();
	form.setPozitionare2(pozitionare2);
	r.addRecord();
	r.addField("pozitionare2", pozitionare2); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.GauriAerisire.pozitionare2");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, GauriAerisireForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the pozitionare2", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updatePozitionare3(String pozitionare3) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getPozitionare3();
	form.setPozitionare3(pozitionare3);
	r.addRecord();
	r.addField("pozitionare3", pozitionare3); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.GauriAerisire.pozitionare3");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, GauriAerisireForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the pozitionare3", e);
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
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.GauriAerisire.sellPrice");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, java.math.BigDecimal.class);
		script.setVar(FORM_VARNAME, form, GauriAerisireForm.class);
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
    public ResponseBean updateEntryPrice(java.math.BigDecimal entryPrice) {
        ResponseBean r = new ResponseBean();
	java.math.BigDecimal oldVal = form.getEntryPrice();
	form.setEntryPrice(entryPrice);
	r.addRecord();
	r.addField("entryPrice", entryPrice); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.GauriAerisire.entryPrice");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, java.math.BigDecimal.class);
		script.setVar(FORM_VARNAME, form, GauriAerisireForm.class);
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
    public ResponseBean updatePrice1(java.math.BigDecimal price1) {
        ResponseBean r = new ResponseBean();
	java.math.BigDecimal oldVal = form.getPrice1();
	form.setPrice1(price1);
	r.addRecord();
	r.addField("price1", price1); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.GauriAerisire.price1");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, java.math.BigDecimal.class);
		script.setVar(FORM_VARNAME, form, GauriAerisireForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the price1", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateBusinessCategory(String businessCategory) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getBusinessCategory();
	form.setBusinessCategory(businessCategory);
	r.addRecord();
	r.addField("businessCategory", businessCategory); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.GauriAerisire.businessCategory");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, GauriAerisireForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the businessCategory", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateQuantity(Integer quantity) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getQuantity();
	form.setQuantity(quantity);
	r.addRecord();
	r.addField("quantity", quantity); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.GauriAerisire.quantity");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, Integer.class);
		script.setVar(FORM_VARNAME, form, GauriAerisireForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
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

    /**
     * Get the fields stored internaly and adds them to the response.
     */
    protected void copyFieldsToResponse(ResponseBean r) {
	r.addField("diametru", form.getDiametru());
	r.addField("pas", form.getPas());
	r.addField("nrRanduri", form.getNrRanduri());
	r.addField("pozitionare1", form.getPozitionare1());
	r.addField("pozitionare2", form.getPozitionare2());
	r.addField("pozitionare3", form.getPozitionare3());
	r.addField("sellPrice", form.getSellPrice());
	r.addField("entryPrice", form.getEntryPrice());
	r.addField("price1", form.getPrice1());
	r.addField("businessCategory", form.getBusinessCategory());
	r.addField("quantity", form.getQuantity());
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
	    s.setVar("diametru", form.getDiametru(), Double.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: diametru from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("pas", form.getPas(), Double.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: pas from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("nrRanduri", form.getNrRanduri(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: nrRanduri from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("pozitionare1", form.getPozitionare1(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: pozitionare1 from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("pozitionare2", form.getPozitionare2(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: pozitionare2 from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("pozitionare3", form.getPozitionare3(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: pozitionare3 from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("sellPrice", form.getSellPrice(), java.math.BigDecimal.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: sellPrice from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("entryPrice", form.getEntryPrice(), java.math.BigDecimal.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: entryPrice from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("price1", form.getPrice1(), java.math.BigDecimal.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: price1 from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("businessCategory", form.getBusinessCategory(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: businessCategory from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("quantity", form.getQuantity(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: quantity from the script");
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
	    field = s.getVar("diametru", Double.class);
	    if(!field.equals(form.getDiametru())) {
	        logger.log(BasicLevel.DEBUG, "Field diametru modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setDiametru((Double)field);
	        r.addField("diametru", (Double)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: diametru from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("pas", Double.class);
	    if(!field.equals(form.getPas())) {
	        logger.log(BasicLevel.DEBUG, "Field pas modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setPas((Double)field);
	        r.addField("pas", (Double)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: pas from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("nrRanduri", Integer.class);
	    if(!field.equals(form.getNrRanduri())) {
	        logger.log(BasicLevel.DEBUG, "Field nrRanduri modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setNrRanduri((Integer)field);
	        r.addField("nrRanduri", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: nrRanduri from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("pozitionare1", String.class);
	    if(!field.equals(form.getPozitionare1())) {
	        logger.log(BasicLevel.DEBUG, "Field pozitionare1 modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setPozitionare1((String)field);
	        r.addField("pozitionare1", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: pozitionare1 from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("pozitionare2", String.class);
	    if(!field.equals(form.getPozitionare2())) {
	        logger.log(BasicLevel.DEBUG, "Field pozitionare2 modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setPozitionare2((String)field);
	        r.addField("pozitionare2", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: pozitionare2 from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("pozitionare3", String.class);
	    if(!field.equals(form.getPozitionare3())) {
	        logger.log(BasicLevel.DEBUG, "Field pozitionare3 modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setPozitionare3((String)field);
	        r.addField("pozitionare3", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: pozitionare3 from the script");
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
	    field = s.getVar("price1", java.math.BigDecimal.class);
	    if(!field.equals(form.getPrice1())) {
	        logger.log(BasicLevel.DEBUG, "Field price1 modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setPrice1((java.math.BigDecimal)field);
	        r.addField("price1", (java.math.BigDecimal)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: price1 from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("businessCategory", String.class);
	    if(!field.equals(form.getBusinessCategory())) {
	        logger.log(BasicLevel.DEBUG, "Field businessCategory modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setBusinessCategory((String)field);
	        r.addField("businessCategory", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: businessCategory from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("quantity", Integer.class);
	    if(!field.equals(form.getQuantity())) {
	        logger.log(BasicLevel.DEBUG, "Field quantity modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setQuantity((Integer)field);
	        r.addField("quantity", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: quantity from the script");
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
}

