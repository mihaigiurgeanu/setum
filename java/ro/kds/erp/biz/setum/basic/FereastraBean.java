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
 * Standard implementation of the Fereastra session bean.
 * You will have to extend this class to proivde code for the abstract
 * methods loadFormData, newForm and saveSaveFormData.
 */
public abstract class FereastraBean 
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
            logger = Monolog.getMonologFactory().getLogger("ro.kds.erp.biz.setum.basic.Fereastra");
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
    // Fereastra implementation
    // ------------------------------------------------------------------
    protected FereastraForm form;
    
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
	logger.log(BasicLevel.DEBUG, "Loading Fereastra with id = " + loadId);
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
     * Create a new FereastraForm and initialize the 
     * <code>this.form</code> instance variable. Overwrite this method
     * if you want to provide other code for initializing the form bean.
     */
    protected void createNewFormBean() {
	form = new FereastraForm();
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
     * <code>ro.kds.erp.biz.setum.basic.Fereastra_calculatedFields</code>
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
		.loadScript("ro.kds.erp.biz.setum.basic.Fereastra_calculatedFields");
	if(script.loaded()) {
	    try {
		script.setVar(FORM_VARNAME, form, 
			      FereastraForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);

		addFieldsToScript(script);
		script.run();

		getFieldsFromScript(script, r);

	    } catch (ScriptErrorException e) {
		logger.log(BasicLevel.ERROR, "Error executing rules script for Fereastra", e);
	    }
	}
	return r;
    }
	

    /**
     * Evaluates the validation script. The script loader loades the script
     * file corresponding to:
     *
     * <code>ro.kds.erp.biz.setum.basic.Fereastra_validation</code>
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
		.loadScript("ro.kds.erp.biz.setum.basic.Fereastra_validation");
	if(script.loaded()) {
	    try {
		script.setVar(FORM_VARNAME, form, 
			      FereastraForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		addFieldsToScript(script);
		script.run();

		// I don't add modified fields back to the response bean,
		// so any fields modifications made in the script will
		// be ignored.
	    } catch (ScriptErrorException e) {
		logger.log(BasicLevel.ERROR, "Error executing rules script for Fereastra", e);
	    }
	}
	return r;
    }

    public ResponseBean updateStandard(Integer standard) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getStandard();
	form.setStandard(standard);
	r.addRecord();
	r.addField("standard", standard); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.Fereastra.standard");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, Integer.class);
		script.setVar(FORM_VARNAME, form, FereastraForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the standard", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateCanat(Integer canat) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getCanat();
	form.setCanat(canat);
	r.addRecord();
	r.addField("canat", canat); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.Fereastra.canat");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, Integer.class);
		script.setVar(FORM_VARNAME, form, FereastraForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the canat", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateLf(Double lf) {
        ResponseBean r = new ResponseBean();
	Double oldVal = form.getLf();
	form.setLf(lf);
	r.addRecord();
	r.addField("lf", lf); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.Fereastra.lf");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, Double.class);
		script.setVar(FORM_VARNAME, form, FereastraForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the lf", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateHf(Double hf) {
        ResponseBean r = new ResponseBean();
	Double oldVal = form.getHf();
	form.setHf(hf);
	r.addRecord();
	r.addField("hf", hf); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.Fereastra.hf");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, Double.class);
		script.setVar(FORM_VARNAME, form, FereastraForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the hf", e);
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
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.Fereastra.pozitionare1");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, FereastraForm.class);
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
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.Fereastra.pozitionare2");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, FereastraForm.class);
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
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.Fereastra.pozitionare3");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, FereastraForm.class);
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
    public ResponseBean updateDeschidere(Integer deschidere) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getDeschidere();
	form.setDeschidere(deschidere);
	r.addRecord();
	r.addField("deschidere", deschidere); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.Fereastra.deschidere");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, Integer.class);
		script.setVar(FORM_VARNAME, form, FereastraForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the deschidere", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateSensDeschidere(Integer sensDeschidere) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getSensDeschidere();
	form.setSensDeschidere(sensDeschidere);
	r.addRecord();
	r.addField("sensDeschidere", sensDeschidere); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.Fereastra.sensDeschidere");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, Integer.class);
		script.setVar(FORM_VARNAME, form, FereastraForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the sensDeschidere", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updatePozitionareBalamale(Integer pozitionareBalamale) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getPozitionareBalamale();
	form.setPozitionareBalamale(pozitionareBalamale);
	r.addRecord();
	r.addField("pozitionareBalamale", pozitionareBalamale); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.Fereastra.pozitionareBalamale");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, Integer.class);
		script.setVar(FORM_VARNAME, form, FereastraForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the pozitionareBalamale", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateComponenta(Integer componenta) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getComponenta();
	form.setComponenta(componenta);
	r.addRecord();
	r.addField("componenta", componenta); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.Fereastra.componenta");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, Integer.class);
		script.setVar(FORM_VARNAME, form, FereastraForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the componenta", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateTipComponenta(Integer tipComponenta) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getTipComponenta();
	form.setTipComponenta(tipComponenta);
	r.addRecord();
	r.addField("tipComponenta", tipComponenta); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.Fereastra.tipComponenta");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, Integer.class);
		script.setVar(FORM_VARNAME, form, FereastraForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the tipComponenta", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateTipGeam(Integer tipGeam) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getTipGeam();
	form.setTipGeam(tipGeam);
	r.addRecord();
	r.addField("tipGeam", tipGeam); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.Fereastra.tipGeam");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, Integer.class);
		script.setVar(FORM_VARNAME, form, FereastraForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the tipGeam", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateGeamSimpluId(Integer geamSimpluId) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getGeamSimpluId();
	form.setGeamSimpluId(geamSimpluId);
	r.addRecord();
	r.addField("geamSimpluId", geamSimpluId); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.Fereastra.geamSimpluId");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, Integer.class);
		script.setVar(FORM_VARNAME, form, FereastraForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the geamSimpluId", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateGeamTermopanId(Integer geamTermopanId) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getGeamTermopanId();
	form.setGeamTermopanId(geamTermopanId);
	r.addRecord();
	r.addField("geamTermopanId", geamTermopanId); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.Fereastra.geamTermopanId");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, Integer.class);
		script.setVar(FORM_VARNAME, form, FereastraForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the geamTermopanId", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateTipGrilaj(Integer tipGrilaj) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getTipGrilaj();
	form.setTipGrilaj(tipGrilaj);
	r.addRecord();
	r.addField("tipGrilaj", tipGrilaj); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.Fereastra.tipGrilaj");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, Integer.class);
		script.setVar(FORM_VARNAME, form, FereastraForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the tipGrilaj", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateGrilajStasId(Integer grilajStasId) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getGrilajStasId();
	form.setGrilajStasId(grilajStasId);
	r.addRecord();
	r.addField("grilajStasId", grilajStasId); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.Fereastra.grilajStasId");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, Integer.class);
		script.setVar(FORM_VARNAME, form, FereastraForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the grilajStasId", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateValoareGrilajAtipic(java.math.BigDecimal valoareGrilajAtipic) {
        ResponseBean r = new ResponseBean();
	java.math.BigDecimal oldVal = form.getValoareGrilajAtipic();
	form.setValoareGrilajAtipic(valoareGrilajAtipic);
	r.addRecord();
	r.addField("valoareGrilajAtipic", valoareGrilajAtipic); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.Fereastra.valoareGrilajAtipic");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, java.math.BigDecimal.class);
		script.setVar(FORM_VARNAME, form, FereastraForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the valoareGrilajAtipic", e);
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
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.Fereastra.sellPrice");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, java.math.BigDecimal.class);
		script.setVar(FORM_VARNAME, form, FereastraForm.class);
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
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.Fereastra.entryPrice");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, java.math.BigDecimal.class);
		script.setVar(FORM_VARNAME, form, FereastraForm.class);
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
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.Fereastra.price1");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, java.math.BigDecimal.class);
		script.setVar(FORM_VARNAME, form, FereastraForm.class);
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
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.Fereastra.businessCategory");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, FereastraForm.class);
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
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.Fereastra.quantity");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, Integer.class);
		script.setVar(FORM_VARNAME, form, FereastraForm.class);
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
	r.addField("standard", form.getStandard());
	r.addField("canat", form.getCanat());
	r.addField("lf", form.getLf());
	r.addField("hf", form.getHf());
	r.addField("pozitionare1", form.getPozitionare1());
	r.addField("pozitionare2", form.getPozitionare2());
	r.addField("pozitionare3", form.getPozitionare3());
	r.addField("deschidere", form.getDeschidere());
	r.addField("sensDeschidere", form.getSensDeschidere());
	r.addField("pozitionareBalamale", form.getPozitionareBalamale());
	r.addField("componenta", form.getComponenta());
	r.addField("tipComponenta", form.getTipComponenta());
	r.addField("tipGeam", form.getTipGeam());
	r.addField("geamSimpluId", form.getGeamSimpluId());
	r.addField("geamTermopanId", form.getGeamTermopanId());
	r.addField("tipGrilaj", form.getTipGrilaj());
	r.addField("grilajStasId", form.getGrilajStasId());
	r.addField("valoareGrilajAtipic", form.getValoareGrilajAtipic());
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
	    s.setVar("standard", form.getStandard(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: standard from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("canat", form.getCanat(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: canat from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("lf", form.getLf(), Double.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: lf from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("hf", form.getHf(), Double.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: hf from the script");
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
	    s.setVar("deschidere", form.getDeschidere(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: deschidere from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("sensDeschidere", form.getSensDeschidere(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: sensDeschidere from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("pozitionareBalamale", form.getPozitionareBalamale(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: pozitionareBalamale from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("componenta", form.getComponenta(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: componenta from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("tipComponenta", form.getTipComponenta(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: tipComponenta from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("tipGeam", form.getTipGeam(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: tipGeam from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("geamSimpluId", form.getGeamSimpluId(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: geamSimpluId from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("geamTermopanId", form.getGeamTermopanId(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: geamTermopanId from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("tipGrilaj", form.getTipGrilaj(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: tipGrilaj from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("grilajStasId", form.getGrilajStasId(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: grilajStasId from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("valoareGrilajAtipic", form.getValoareGrilajAtipic(), java.math.BigDecimal.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: valoareGrilajAtipic from the script");
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
	    field = s.getVar("standard", Integer.class);
	    if(!field.equals(form.getStandard())) {
	        logger.log(BasicLevel.DEBUG, "Field standard modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setStandard((Integer)field);
	        r.addField("standard", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: standard from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("canat", Integer.class);
	    if(!field.equals(form.getCanat())) {
	        logger.log(BasicLevel.DEBUG, "Field canat modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setCanat((Integer)field);
	        r.addField("canat", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: canat from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("lf", Double.class);
	    if(!field.equals(form.getLf())) {
	        logger.log(BasicLevel.DEBUG, "Field lf modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setLf((Double)field);
	        r.addField("lf", (Double)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: lf from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("hf", Double.class);
	    if(!field.equals(form.getHf())) {
	        logger.log(BasicLevel.DEBUG, "Field hf modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setHf((Double)field);
	        r.addField("hf", (Double)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: hf from the script");
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
	    field = s.getVar("deschidere", Integer.class);
	    if(!field.equals(form.getDeschidere())) {
	        logger.log(BasicLevel.DEBUG, "Field deschidere modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setDeschidere((Integer)field);
	        r.addField("deschidere", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: deschidere from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("sensDeschidere", Integer.class);
	    if(!field.equals(form.getSensDeschidere())) {
	        logger.log(BasicLevel.DEBUG, "Field sensDeschidere modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setSensDeschidere((Integer)field);
	        r.addField("sensDeschidere", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: sensDeschidere from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("pozitionareBalamale", Integer.class);
	    if(!field.equals(form.getPozitionareBalamale())) {
	        logger.log(BasicLevel.DEBUG, "Field pozitionareBalamale modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setPozitionareBalamale((Integer)field);
	        r.addField("pozitionareBalamale", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: pozitionareBalamale from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("componenta", Integer.class);
	    if(!field.equals(form.getComponenta())) {
	        logger.log(BasicLevel.DEBUG, "Field componenta modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setComponenta((Integer)field);
	        r.addField("componenta", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: componenta from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("tipComponenta", Integer.class);
	    if(!field.equals(form.getTipComponenta())) {
	        logger.log(BasicLevel.DEBUG, "Field tipComponenta modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setTipComponenta((Integer)field);
	        r.addField("tipComponenta", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: tipComponenta from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("tipGeam", Integer.class);
	    if(!field.equals(form.getTipGeam())) {
	        logger.log(BasicLevel.DEBUG, "Field tipGeam modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setTipGeam((Integer)field);
	        r.addField("tipGeam", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: tipGeam from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("geamSimpluId", Integer.class);
	    if(!field.equals(form.getGeamSimpluId())) {
	        logger.log(BasicLevel.DEBUG, "Field geamSimpluId modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setGeamSimpluId((Integer)field);
	        r.addField("geamSimpluId", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: geamSimpluId from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("geamTermopanId", Integer.class);
	    if(!field.equals(form.getGeamTermopanId())) {
	        logger.log(BasicLevel.DEBUG, "Field geamTermopanId modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setGeamTermopanId((Integer)field);
	        r.addField("geamTermopanId", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: geamTermopanId from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("tipGrilaj", Integer.class);
	    if(!field.equals(form.getTipGrilaj())) {
	        logger.log(BasicLevel.DEBUG, "Field tipGrilaj modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setTipGrilaj((Integer)field);
	        r.addField("tipGrilaj", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: tipGrilaj from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("grilajStasId", Integer.class);
	    if(!field.equals(form.getGrilajStasId())) {
	        logger.log(BasicLevel.DEBUG, "Field grilajStasId modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setGrilajStasId((Integer)field);
	        r.addField("grilajStasId", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: grilajStasId from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("valoareGrilajAtipic", java.math.BigDecimal.class);
	    if(!field.equals(form.getValoareGrilajAtipic())) {
	        logger.log(BasicLevel.DEBUG, "Field valoareGrilajAtipic modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setValoareGrilajAtipic((java.math.BigDecimal)field);
	        r.addField("valoareGrilajAtipic", (java.math.BigDecimal)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: valoareGrilajAtipic from the script");
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

