package ro.kds.erp.biz.setum.basic;

import javax.ejb.SessionBean;
import javax.ejb.SessionSynchronization;
import javax.ejb.SessionContext;
import org.objectweb.util.monolog.api.BasicLevel;
import javax.ejb.CreateException;
import ro.kds.erp.biz.ResponseBean;
import javax.naming.InitialContext;
import javax.ejb.FinderException;
import org.objectweb.util.monolog.api.Logger;
import org.objectweb.jonas.common.Log;
import java.util.Collection;
import java.util.Iterator;
import ro.kds.erp.scripting.Script;
import ro.kds.erp.scripting.TclFileScript;
import ro.kds.erp.scripting.ScriptErrorException;

/**
 * Standard implementation of the UsaMetalica2K session bean.
 * You will have to extend this class to proivde code for the abstract
 * methods loadFormData, newForm and saveSaveFormData.
 */
public abstract class UsaMetalica2KBean 
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
            logger = Log.getLogger("ro.kds.erp.biz.setum.basic.UsaMetalica2K");
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
    // UsaMetalica2K implementation
    // ------------------------------------------------------------------
    protected UsaMetalica2KForm form;
    
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
     * Updating or inserting the form into the database.
     */
    public abstract ResponseBean saveFormData();

    /**
     * Load the form from the database.
     */
     public ResponseBean loadFormData(Integer loadId) throws FinderException {
	logger.log(BasicLevel.DEBUG, "Loading UsaMetalica2K with id = " + loadId);
	createNewFormBean();
	id = loadId;

	ResponseBean r = loadFields();
	computeCalculatedFields(null);
	r.addRecord();
	copyFieldsToResponse(r);
	return r;
     }

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
     * Locates the data by the primary key and loads the fields into the
     * form. The <code>form</code> instance variable already contains
     * a new initialized form data. The <code>id</code> local variable contains
     * the primary key for the record that should be located and loaded into
     * the form bean.
     */
     public abstract ResponseBean loadFields() throws FinderException;

    /**
     * Create a new UsaMetalica2KForm and initialize the 
     * <code>this.form</code> instance variable. Overwrite this method
     * if you want to provide other code for initializing the form bean.
     */
    protected void createNewFormBean() {
	form = new UsaMetalica2KForm();
    }

    /**
     * Evaluates the script for computing the values of the calculated
     * fields. The script is located by the script loader using the key:
     * 
     * <code>ro.kds.erp.biz.setum.basic.UsaMetalica2K_calculatedFields</code>
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
		.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K_calculatedFields");
	if(script.loaded()) {
	    try {
		script.setVar(FORM_VARNAME, form, 
			      UsaMetalica2KForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);

		addFieldsToScript(script);
		script.run();

		getFieldsFromScript(script, r);

	    } catch (ScriptErrorException e) {
		logger.log(BasicLevel.ERROR, "Error executing rules script for UsaMetalica2K", e);
	    }
	}
	return r;
    }
	

    /**
     * Evaluates the validation script. The script loader loades the script
     * file corresponding to:
     *
     * <code>ro.kds.erp.biz.setum.basic.UsaMetalica2K_validation</code>
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
		.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K_validation");
	if(script.loaded()) {
	    try {
		script.setVar(FORM_VARNAME, form, 
			      UsaMetalica2KForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		addFieldsToScript(script);
		script.run();

		// I don't add modified fields back to the response bean,
		// so any fields modifications made in the script will
		// be ignored.
	    } catch (ScriptErrorException e) {
		logger.log(BasicLevel.ERROR, "Error executing rules script for UsaMetalica2K", e);
	    }
	}
	return r;
    }

    public ResponseBean updateSubclass(String subclass) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getSubclass();
	form.setSubclass(subclass);
	r.addRecord();
	r.addField("subclass", subclass); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.subclass");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, UsaMetalica2KForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the subclass", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateVersion(String version) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getVersion();
	form.setVersion(version);
	r.addRecord();
	r.addField("version", version); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.version");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, UsaMetalica2KForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the version", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateMaterial(Integer material) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getMaterial();
	form.setMaterial(material);
	r.addRecord();
	r.addField("material", material); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.material");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, Integer.class);
		script.setVar(FORM_VARNAME, form, UsaMetalica2KForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the material", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateLg(Double lg) {
        ResponseBean r = new ResponseBean();
	Double oldVal = form.getLg();
	form.setLg(lg);
	r.addRecord();
	r.addField("lg", lg); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.lg");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, Double.class);
		script.setVar(FORM_VARNAME, form, UsaMetalica2KForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the lg", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateHg(Double hg) {
        ResponseBean r = new ResponseBean();
	Double oldVal = form.getHg();
	form.setHg(hg);
	r.addRecord();
	r.addField("hg", hg); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.hg");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, Double.class);
		script.setVar(FORM_VARNAME, form, UsaMetalica2KForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the hg", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateLe(Double le) {
        ResponseBean r = new ResponseBean();
	Double oldVal = form.getLe();
	form.setLe(le);
	r.addRecord();
	r.addField("le", le); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.le");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, Double.class);
		script.setVar(FORM_VARNAME, form, UsaMetalica2KForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the le", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateHe(Double he) {
        ResponseBean r = new ResponseBean();
	Double oldVal = form.getHe();
	form.setHe(he);
	r.addRecord();
	r.addField("he", he); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.he");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, Double.class);
		script.setVar(FORM_VARNAME, form, UsaMetalica2KForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the he", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateLcorrection(Double lcorrection) {
        ResponseBean r = new ResponseBean();
	Double oldVal = form.getLcorrection();
	form.setLcorrection(lcorrection);
	r.addRecord();
	r.addField("lcorrection", lcorrection); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.lcorrection");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, Double.class);
		script.setVar(FORM_VARNAME, form, UsaMetalica2KForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the lcorrection", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateHcorrection(Double hcorrection) {
        ResponseBean r = new ResponseBean();
	Double oldVal = form.getHcorrection();
	form.setHcorrection(hcorrection);
	r.addRecord();
	r.addField("hcorrection", hcorrection); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.hcorrection");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, Double.class);
		script.setVar(FORM_VARNAME, form, UsaMetalica2KForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the hcorrection", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateLCurrent(Double lCurrent) {
        ResponseBean r = new ResponseBean();
	Double oldVal = form.getLCurrent();
	form.setLCurrent(lCurrent);
	r.addRecord();
	r.addField("lCurrent", lCurrent); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.lCurrent");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, Double.class);
		script.setVar(FORM_VARNAME, form, UsaMetalica2KForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the lCurrent", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateKType(Integer kType) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getKType();
	form.setKType(kType);
	r.addRecord();
	r.addField("kType", kType); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.kType");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, Integer.class);
		script.setVar(FORM_VARNAME, form, UsaMetalica2KForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the kType", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateIntFoil(Integer intFoil) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getIntFoil();
	form.setIntFoil(intFoil);
	r.addRecord();
	r.addField("intFoil", intFoil); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.intFoil");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, Integer.class);
		script.setVar(FORM_VARNAME, form, UsaMetalica2KForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the intFoil", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateIeFoil(Integer ieFoil) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getIeFoil();
	form.setIeFoil(ieFoil);
	r.addRecord();
	r.addField("ieFoil", ieFoil); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.ieFoil");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, Integer.class);
		script.setVar(FORM_VARNAME, form, UsaMetalica2KForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the ieFoil", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateExtFoil(Integer extFoil) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getExtFoil();
	form.setExtFoil(extFoil);
	r.addRecord();
	r.addField("extFoil", extFoil); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.extFoil");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, Integer.class);
		script.setVar(FORM_VARNAME, form, UsaMetalica2KForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the extFoil", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateIntFoilSec(Integer intFoilSec) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getIntFoilSec();
	form.setIntFoilSec(intFoilSec);
	r.addRecord();
	r.addField("intFoilSec", intFoilSec); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.intFoilSec");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, Integer.class);
		script.setVar(FORM_VARNAME, form, UsaMetalica2KForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the intFoilSec", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateIeFoilSec(Integer ieFoilSec) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getIeFoilSec();
	form.setIeFoilSec(ieFoilSec);
	r.addRecord();
	r.addField("ieFoilSec", ieFoilSec); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.ieFoilSec");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, Integer.class);
		script.setVar(FORM_VARNAME, form, UsaMetalica2KForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the ieFoilSec", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateExtFoilSec(Integer extFoilSec) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getExtFoilSec();
	form.setExtFoilSec(extFoilSec);
	r.addRecord();
	r.addField("extFoilSec", extFoilSec); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.extFoilSec");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, Integer.class);
		script.setVar(FORM_VARNAME, form, UsaMetalica2KForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the extFoilSec", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateIsolation(Integer isolation) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getIsolation();
	form.setIsolation(isolation);
	r.addRecord();
	r.addField("isolation", isolation); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.isolation");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, Integer.class);
		script.setVar(FORM_VARNAME, form, UsaMetalica2KForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the isolation", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateOpeningDir(Integer openingDir) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getOpeningDir();
	form.setOpeningDir(openingDir);
	r.addRecord();
	r.addField("openingDir", openingDir); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.openingDir");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, Integer.class);
		script.setVar(FORM_VARNAME, form, UsaMetalica2KForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the openingDir", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateOpeningSide(Integer openingSide) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getOpeningSide();
	form.setOpeningSide(openingSide);
	r.addRecord();
	r.addField("openingSide", openingSide); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.openingSide");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, Integer.class);
		script.setVar(FORM_VARNAME, form, UsaMetalica2KForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the openingSide", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateFrameType(Integer frameType) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getFrameType();
	form.setFrameType(frameType);
	r.addRecord();
	r.addField("frameType", frameType); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.frameType");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, Integer.class);
		script.setVar(FORM_VARNAME, form, UsaMetalica2KForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the frameType", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateLFrame(Double lFrame) {
        ResponseBean r = new ResponseBean();
	Double oldVal = form.getLFrame();
	form.setLFrame(lFrame);
	r.addRecord();
	r.addField("lFrame", lFrame); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.lFrame");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, Double.class);
		script.setVar(FORM_VARNAME, form, UsaMetalica2KForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the lFrame", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateBFrame(Double bFrame) {
        ResponseBean r = new ResponseBean();
	Double oldVal = form.getBFrame();
	form.setBFrame(bFrame);
	r.addRecord();
	r.addField("bFrame", bFrame); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.bFrame");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, Double.class);
		script.setVar(FORM_VARNAME, form, UsaMetalica2KForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the bFrame", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateCFrame(Double cFrame) {
        ResponseBean r = new ResponseBean();
	Double oldVal = form.getCFrame();
	form.setCFrame(cFrame);
	r.addRecord();
	r.addField("cFrame", cFrame); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.cFrame");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, Double.class);
		script.setVar(FORM_VARNAME, form, UsaMetalica2KForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the cFrame", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateFoilPosition(Integer foilPosition) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getFoilPosition();
	form.setFoilPosition(foilPosition);
	r.addRecord();
	r.addField("foilPosition", foilPosition); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.foilPosition");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, Integer.class);
		script.setVar(FORM_VARNAME, form, UsaMetalica2KForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the foilPosition", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateTresholdType(Integer tresholdType) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getTresholdType();
	form.setTresholdType(tresholdType);
	r.addRecord();
	r.addField("tresholdType", tresholdType); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.tresholdType");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, Integer.class);
		script.setVar(FORM_VARNAME, form, UsaMetalica2KForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the tresholdType", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateLTreshold(Double lTreshold) {
        ResponseBean r = new ResponseBean();
	Double oldVal = form.getLTreshold();
	form.setLTreshold(lTreshold);
	r.addRecord();
	r.addField("lTreshold", lTreshold); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.lTreshold");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, Double.class);
		script.setVar(FORM_VARNAME, form, UsaMetalica2KForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the lTreshold", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateHTreshold(Double hTreshold) {
        ResponseBean r = new ResponseBean();
	Double oldVal = form.getHTreshold();
	form.setHTreshold(hTreshold);
	r.addRecord();
	r.addField("hTreshold", hTreshold); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.hTreshold");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, Double.class);
		script.setVar(FORM_VARNAME, form, UsaMetalica2KForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the hTreshold", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateCTreshold(Double cTreshold) {
        ResponseBean r = new ResponseBean();
	Double oldVal = form.getCTreshold();
	form.setCTreshold(cTreshold);
	r.addRecord();
	r.addField("cTreshold", cTreshold); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.cTreshold");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, Double.class);
		script.setVar(FORM_VARNAME, form, UsaMetalica2KForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the cTreshold", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateTresholdSpace(Integer tresholdSpace) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getTresholdSpace();
	form.setTresholdSpace(tresholdSpace);
	r.addRecord();
	r.addField("tresholdSpace", tresholdSpace); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.tresholdSpace");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, Integer.class);
		script.setVar(FORM_VARNAME, form, UsaMetalica2KForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the tresholdSpace", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateH1Treshold(Double h1Treshold) {
        ResponseBean r = new ResponseBean();
	Double oldVal = form.getH1Treshold();
	form.setH1Treshold(h1Treshold);
	r.addRecord();
	r.addField("h1Treshold", h1Treshold); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.h1Treshold");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, Double.class);
		script.setVar(FORM_VARNAME, form, UsaMetalica2KForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the h1Treshold", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateH2Treshold(Double h2Treshold) {
        ResponseBean r = new ResponseBean();
	Double oldVal = form.getH2Treshold();
	form.setH2Treshold(h2Treshold);
	r.addRecord();
	r.addField("h2Treshold", h2Treshold); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.h2Treshold");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, Double.class);
		script.setVar(FORM_VARNAME, form, UsaMetalica2KForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the h2Treshold", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateFereastraId(Integer fereastraId) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getFereastraId();
	form.setFereastraId(fereastraId);
	r.addRecord();
	r.addField("fereastraId", fereastraId); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.fereastraId");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, Integer.class);
		script.setVar(FORM_VARNAME, form, UsaMetalica2KForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the fereastraId", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateFereastra(String fereastra) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getFereastra();
	form.setFereastra(fereastra);
	r.addRecord();
	r.addField("fereastra", fereastra); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.fereastra");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, UsaMetalica2KForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the fereastra", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateGrilaVentilatieId(Integer grilaVentilatieId) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getGrilaVentilatieId();
	form.setGrilaVentilatieId(grilaVentilatieId);
	r.addRecord();
	r.addField("grilaVentilatieId", grilaVentilatieId); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.grilaVentilatieId");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, Integer.class);
		script.setVar(FORM_VARNAME, form, UsaMetalica2KForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the grilaVentilatieId", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateGrilaVentilatie(String grilaVentilatie) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getGrilaVentilatie();
	form.setGrilaVentilatie(grilaVentilatie);
	r.addRecord();
	r.addField("grilaVentilatie", grilaVentilatie); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.grilaVentilatie");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, UsaMetalica2KForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the grilaVentilatie", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateGauriAerisireId(Integer gauriAerisireId) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getGauriAerisireId();
	form.setGauriAerisireId(gauriAerisireId);
	r.addRecord();
	r.addField("gauriAerisireId", gauriAerisireId); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.gauriAerisireId");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, Integer.class);
		script.setVar(FORM_VARNAME, form, UsaMetalica2KForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the gauriAerisireId", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateGauriAerisire(String gauriAerisire) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getGauriAerisire();
	form.setGauriAerisire(gauriAerisire);
	r.addRecord();
	r.addField("gauriAerisire", gauriAerisire); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.gauriAerisire");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, UsaMetalica2KForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the gauriAerisire", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }

    /**
     * Get the fields stored internaly and adds them to the response.
     */
    protected void copyFieldsToResponse(ResponseBean r) {
	r.addField("subclass", form.getSubclass());
	r.addField("version", form.getVersion());
	r.addField("material", form.getMaterial());
	r.addField("lg", form.getLg());
	r.addField("hg", form.getHg());
	r.addField("le", form.getLe());
	r.addField("he", form.getHe());
	r.addField("lcorrection", form.getLcorrection());
	r.addField("hcorrection", form.getHcorrection());
	r.addField("lCurrent", form.getLCurrent());
	r.addField("kType", form.getKType());
	r.addField("intFoil", form.getIntFoil());
	r.addField("ieFoil", form.getIeFoil());
	r.addField("extFoil", form.getExtFoil());
	r.addField("intFoilSec", form.getIntFoilSec());
	r.addField("ieFoilSec", form.getIeFoilSec());
	r.addField("extFoilSec", form.getExtFoilSec());
	r.addField("isolation", form.getIsolation());
	r.addField("openingDir", form.getOpeningDir());
	r.addField("openingSide", form.getOpeningSide());
	r.addField("frameType", form.getFrameType());
	r.addField("lFrame", form.getLFrame());
	r.addField("bFrame", form.getBFrame());
	r.addField("cFrame", form.getCFrame());
	r.addField("foilPosition", form.getFoilPosition());
	r.addField("tresholdType", form.getTresholdType());
	r.addField("lTreshold", form.getLTreshold());
	r.addField("hTreshold", form.getHTreshold());
	r.addField("cTreshold", form.getCTreshold());
	r.addField("tresholdSpace", form.getTresholdSpace());
	r.addField("h1Treshold", form.getH1Treshold());
	r.addField("h2Treshold", form.getH2Treshold());
	r.addField("fereastraId", form.getFereastraId());
	r.addField("fereastra", form.getFereastra());
	r.addField("grilaVentilatieId", form.getGrilaVentilatieId());
	r.addField("grilaVentilatie", form.getGrilaVentilatie());
	r.addField("gauriAerisireId", form.getGauriAerisireId());
	r.addField("gauriAerisire", form.getGauriAerisire());
	loadValueLists(r);
    }

    /**
     * Add all the fields of the form as variables for the script
     */
    protected void addFieldsToScript(Script s) {
	try {
	    s.setVar("subclass", form.getSubclass(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: subclass from the script", e);
        }
	try {
	    s.setVar("version", form.getVersion(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: version from the script", e);
        }
	try {
	    s.setVar("material", form.getMaterial(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: material from the script", e);
        }
	try {
	    s.setVar("lg", form.getLg(), Double.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: lg from the script", e);
        }
	try {
	    s.setVar("hg", form.getHg(), Double.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: hg from the script", e);
        }
	try {
	    s.setVar("le", form.getLe(), Double.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: le from the script", e);
        }
	try {
	    s.setVar("he", form.getHe(), Double.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: he from the script", e);
        }
	try {
	    s.setVar("lcorrection", form.getLcorrection(), Double.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: lcorrection from the script", e);
        }
	try {
	    s.setVar("hcorrection", form.getHcorrection(), Double.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: hcorrection from the script", e);
        }
	try {
	    s.setVar("lCurrent", form.getLCurrent(), Double.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: lCurrent from the script", e);
        }
	try {
	    s.setVar("kType", form.getKType(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: kType from the script", e);
        }
	try {
	    s.setVar("intFoil", form.getIntFoil(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: intFoil from the script", e);
        }
	try {
	    s.setVar("ieFoil", form.getIeFoil(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: ieFoil from the script", e);
        }
	try {
	    s.setVar("extFoil", form.getExtFoil(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: extFoil from the script", e);
        }
	try {
	    s.setVar("intFoilSec", form.getIntFoilSec(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: intFoilSec from the script", e);
        }
	try {
	    s.setVar("ieFoilSec", form.getIeFoilSec(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: ieFoilSec from the script", e);
        }
	try {
	    s.setVar("extFoilSec", form.getExtFoilSec(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: extFoilSec from the script", e);
        }
	try {
	    s.setVar("isolation", form.getIsolation(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: isolation from the script", e);
        }
	try {
	    s.setVar("openingDir", form.getOpeningDir(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: openingDir from the script", e);
        }
	try {
	    s.setVar("openingSide", form.getOpeningSide(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: openingSide from the script", e);
        }
	try {
	    s.setVar("frameType", form.getFrameType(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: frameType from the script", e);
        }
	try {
	    s.setVar("lFrame", form.getLFrame(), Double.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: lFrame from the script", e);
        }
	try {
	    s.setVar("bFrame", form.getBFrame(), Double.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: bFrame from the script", e);
        }
	try {
	    s.setVar("cFrame", form.getCFrame(), Double.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: cFrame from the script", e);
        }
	try {
	    s.setVar("foilPosition", form.getFoilPosition(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: foilPosition from the script", e);
        }
	try {
	    s.setVar("tresholdType", form.getTresholdType(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: tresholdType from the script", e);
        }
	try {
	    s.setVar("lTreshold", form.getLTreshold(), Double.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: lTreshold from the script", e);
        }
	try {
	    s.setVar("hTreshold", form.getHTreshold(), Double.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: hTreshold from the script", e);
        }
	try {
	    s.setVar("cTreshold", form.getCTreshold(), Double.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: cTreshold from the script", e);
        }
	try {
	    s.setVar("tresholdSpace", form.getTresholdSpace(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: tresholdSpace from the script", e);
        }
	try {
	    s.setVar("h1Treshold", form.getH1Treshold(), Double.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: h1Treshold from the script", e);
        }
	try {
	    s.setVar("h2Treshold", form.getH2Treshold(), Double.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: h2Treshold from the script", e);
        }
	try {
	    s.setVar("fereastraId", form.getFereastraId(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: fereastraId from the script", e);
        }
	try {
	    s.setVar("fereastra", form.getFereastra(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: fereastra from the script", e);
        }
	try {
	    s.setVar("grilaVentilatieId", form.getGrilaVentilatieId(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: grilaVentilatieId from the script", e);
        }
	try {
	    s.setVar("grilaVentilatie", form.getGrilaVentilatie(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: grilaVentilatie from the script", e);
        }
	try {
	    s.setVar("gauriAerisireId", form.getGauriAerisireId(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: gauriAerisireId from the script", e);
        }
	try {
	    s.setVar("gauriAerisire", form.getGauriAerisire(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: gauriAerisire from the script", e);
        }
    }

    /**
     * Get the values of the fields from script variables. Writes in the
     * response bean any field that was modified.
     */
    protected void getFieldsFromScript(Script s, ResponseBean r) {
	Object field;
	try {
	    field = s.getVar("subclass", String.class);
	    if(!field.equals(form.getSubclass())) {
	        logger.log(BasicLevel.DEBUG, "Field subclass modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setSubclass((String)field);
	        r.addField("subclass", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: subclass from the script", e);
        }
	try {
	    field = s.getVar("version", String.class);
	    if(!field.equals(form.getVersion())) {
	        logger.log(BasicLevel.DEBUG, "Field version modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setVersion((String)field);
	        r.addField("version", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: version from the script", e);
        }
	try {
	    field = s.getVar("material", Integer.class);
	    if(!field.equals(form.getMaterial())) {
	        logger.log(BasicLevel.DEBUG, "Field material modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setMaterial((Integer)field);
	        r.addField("material", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: material from the script", e);
        }
	try {
	    field = s.getVar("lg", Double.class);
	    if(!field.equals(form.getLg())) {
	        logger.log(BasicLevel.DEBUG, "Field lg modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setLg((Double)field);
	        r.addField("lg", (Double)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: lg from the script", e);
        }
	try {
	    field = s.getVar("hg", Double.class);
	    if(!field.equals(form.getHg())) {
	        logger.log(BasicLevel.DEBUG, "Field hg modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setHg((Double)field);
	        r.addField("hg", (Double)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: hg from the script", e);
        }
	try {
	    field = s.getVar("le", Double.class);
	    if(!field.equals(form.getLe())) {
	        logger.log(BasicLevel.DEBUG, "Field le modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setLe((Double)field);
	        r.addField("le", (Double)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: le from the script", e);
        }
	try {
	    field = s.getVar("he", Double.class);
	    if(!field.equals(form.getHe())) {
	        logger.log(BasicLevel.DEBUG, "Field he modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setHe((Double)field);
	        r.addField("he", (Double)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: he from the script", e);
        }
	try {
	    field = s.getVar("lcorrection", Double.class);
	    if(!field.equals(form.getLcorrection())) {
	        logger.log(BasicLevel.DEBUG, "Field lcorrection modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setLcorrection((Double)field);
	        r.addField("lcorrection", (Double)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: lcorrection from the script", e);
        }
	try {
	    field = s.getVar("hcorrection", Double.class);
	    if(!field.equals(form.getHcorrection())) {
	        logger.log(BasicLevel.DEBUG, "Field hcorrection modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setHcorrection((Double)field);
	        r.addField("hcorrection", (Double)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: hcorrection from the script", e);
        }
	try {
	    field = s.getVar("lCurrent", Double.class);
	    if(!field.equals(form.getLCurrent())) {
	        logger.log(BasicLevel.DEBUG, "Field lCurrent modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setLCurrent((Double)field);
	        r.addField("lCurrent", (Double)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: lCurrent from the script", e);
        }
	try {
	    field = s.getVar("kType", Integer.class);
	    if(!field.equals(form.getKType())) {
	        logger.log(BasicLevel.DEBUG, "Field kType modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setKType((Integer)field);
	        r.addField("kType", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: kType from the script", e);
        }
	try {
	    field = s.getVar("intFoil", Integer.class);
	    if(!field.equals(form.getIntFoil())) {
	        logger.log(BasicLevel.DEBUG, "Field intFoil modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setIntFoil((Integer)field);
	        r.addField("intFoil", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: intFoil from the script", e);
        }
	try {
	    field = s.getVar("ieFoil", Integer.class);
	    if(!field.equals(form.getIeFoil())) {
	        logger.log(BasicLevel.DEBUG, "Field ieFoil modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setIeFoil((Integer)field);
	        r.addField("ieFoil", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: ieFoil from the script", e);
        }
	try {
	    field = s.getVar("extFoil", Integer.class);
	    if(!field.equals(form.getExtFoil())) {
	        logger.log(BasicLevel.DEBUG, "Field extFoil modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setExtFoil((Integer)field);
	        r.addField("extFoil", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: extFoil from the script", e);
        }
	try {
	    field = s.getVar("intFoilSec", Integer.class);
	    if(!field.equals(form.getIntFoilSec())) {
	        logger.log(BasicLevel.DEBUG, "Field intFoilSec modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setIntFoilSec((Integer)field);
	        r.addField("intFoilSec", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: intFoilSec from the script", e);
        }
	try {
	    field = s.getVar("ieFoilSec", Integer.class);
	    if(!field.equals(form.getIeFoilSec())) {
	        logger.log(BasicLevel.DEBUG, "Field ieFoilSec modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setIeFoilSec((Integer)field);
	        r.addField("ieFoilSec", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: ieFoilSec from the script", e);
        }
	try {
	    field = s.getVar("extFoilSec", Integer.class);
	    if(!field.equals(form.getExtFoilSec())) {
	        logger.log(BasicLevel.DEBUG, "Field extFoilSec modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setExtFoilSec((Integer)field);
	        r.addField("extFoilSec", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: extFoilSec from the script", e);
        }
	try {
	    field = s.getVar("isolation", Integer.class);
	    if(!field.equals(form.getIsolation())) {
	        logger.log(BasicLevel.DEBUG, "Field isolation modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setIsolation((Integer)field);
	        r.addField("isolation", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: isolation from the script", e);
        }
	try {
	    field = s.getVar("openingDir", Integer.class);
	    if(!field.equals(form.getOpeningDir())) {
	        logger.log(BasicLevel.DEBUG, "Field openingDir modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setOpeningDir((Integer)field);
	        r.addField("openingDir", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: openingDir from the script", e);
        }
	try {
	    field = s.getVar("openingSide", Integer.class);
	    if(!field.equals(form.getOpeningSide())) {
	        logger.log(BasicLevel.DEBUG, "Field openingSide modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setOpeningSide((Integer)field);
	        r.addField("openingSide", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: openingSide from the script", e);
        }
	try {
	    field = s.getVar("frameType", Integer.class);
	    if(!field.equals(form.getFrameType())) {
	        logger.log(BasicLevel.DEBUG, "Field frameType modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setFrameType((Integer)field);
	        r.addField("frameType", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: frameType from the script", e);
        }
	try {
	    field = s.getVar("lFrame", Double.class);
	    if(!field.equals(form.getLFrame())) {
	        logger.log(BasicLevel.DEBUG, "Field lFrame modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setLFrame((Double)field);
	        r.addField("lFrame", (Double)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: lFrame from the script", e);
        }
	try {
	    field = s.getVar("bFrame", Double.class);
	    if(!field.equals(form.getBFrame())) {
	        logger.log(BasicLevel.DEBUG, "Field bFrame modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setBFrame((Double)field);
	        r.addField("bFrame", (Double)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: bFrame from the script", e);
        }
	try {
	    field = s.getVar("cFrame", Double.class);
	    if(!field.equals(form.getCFrame())) {
	        logger.log(BasicLevel.DEBUG, "Field cFrame modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setCFrame((Double)field);
	        r.addField("cFrame", (Double)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: cFrame from the script", e);
        }
	try {
	    field = s.getVar("foilPosition", Integer.class);
	    if(!field.equals(form.getFoilPosition())) {
	        logger.log(BasicLevel.DEBUG, "Field foilPosition modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setFoilPosition((Integer)field);
	        r.addField("foilPosition", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: foilPosition from the script", e);
        }
	try {
	    field = s.getVar("tresholdType", Integer.class);
	    if(!field.equals(form.getTresholdType())) {
	        logger.log(BasicLevel.DEBUG, "Field tresholdType modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setTresholdType((Integer)field);
	        r.addField("tresholdType", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: tresholdType from the script", e);
        }
	try {
	    field = s.getVar("lTreshold", Double.class);
	    if(!field.equals(form.getLTreshold())) {
	        logger.log(BasicLevel.DEBUG, "Field lTreshold modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setLTreshold((Double)field);
	        r.addField("lTreshold", (Double)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: lTreshold from the script", e);
        }
	try {
	    field = s.getVar("hTreshold", Double.class);
	    if(!field.equals(form.getHTreshold())) {
	        logger.log(BasicLevel.DEBUG, "Field hTreshold modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setHTreshold((Double)field);
	        r.addField("hTreshold", (Double)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: hTreshold from the script", e);
        }
	try {
	    field = s.getVar("cTreshold", Double.class);
	    if(!field.equals(form.getCTreshold())) {
	        logger.log(BasicLevel.DEBUG, "Field cTreshold modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setCTreshold((Double)field);
	        r.addField("cTreshold", (Double)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: cTreshold from the script", e);
        }
	try {
	    field = s.getVar("tresholdSpace", Integer.class);
	    if(!field.equals(form.getTresholdSpace())) {
	        logger.log(BasicLevel.DEBUG, "Field tresholdSpace modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setTresholdSpace((Integer)field);
	        r.addField("tresholdSpace", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: tresholdSpace from the script", e);
        }
	try {
	    field = s.getVar("h1Treshold", Double.class);
	    if(!field.equals(form.getH1Treshold())) {
	        logger.log(BasicLevel.DEBUG, "Field h1Treshold modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setH1Treshold((Double)field);
	        r.addField("h1Treshold", (Double)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: h1Treshold from the script", e);
        }
	try {
	    field = s.getVar("h2Treshold", Double.class);
	    if(!field.equals(form.getH2Treshold())) {
	        logger.log(BasicLevel.DEBUG, "Field h2Treshold modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setH2Treshold((Double)field);
	        r.addField("h2Treshold", (Double)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: h2Treshold from the script", e);
        }
	try {
	    field = s.getVar("fereastraId", Integer.class);
	    if(!field.equals(form.getFereastraId())) {
	        logger.log(BasicLevel.DEBUG, "Field fereastraId modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setFereastraId((Integer)field);
	        r.addField("fereastraId", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: fereastraId from the script", e);
        }
	try {
	    field = s.getVar("fereastra", String.class);
	    if(!field.equals(form.getFereastra())) {
	        logger.log(BasicLevel.DEBUG, "Field fereastra modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setFereastra((String)field);
	        r.addField("fereastra", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: fereastra from the script", e);
        }
	try {
	    field = s.getVar("grilaVentilatieId", Integer.class);
	    if(!field.equals(form.getGrilaVentilatieId())) {
	        logger.log(BasicLevel.DEBUG, "Field grilaVentilatieId modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setGrilaVentilatieId((Integer)field);
	        r.addField("grilaVentilatieId", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: grilaVentilatieId from the script", e);
        }
	try {
	    field = s.getVar("grilaVentilatie", String.class);
	    if(!field.equals(form.getGrilaVentilatie())) {
	        logger.log(BasicLevel.DEBUG, "Field grilaVentilatie modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setGrilaVentilatie((String)field);
	        r.addField("grilaVentilatie", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: grilaVentilatie from the script", e);
        }
	try {
	    field = s.getVar("gauriAerisireId", Integer.class);
	    if(!field.equals(form.getGauriAerisireId())) {
	        logger.log(BasicLevel.DEBUG, "Field gauriAerisireId modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setGauriAerisireId((Integer)field);
	        r.addField("gauriAerisireId", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: gauriAerisireId from the script", e);
        }
	try {
	    field = s.getVar("gauriAerisire", String.class);
	    if(!field.equals(form.getGauriAerisire())) {
	        logger.log(BasicLevel.DEBUG, "Field gauriAerisire modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setGauriAerisire((String)field);
	        r.addField("gauriAerisire", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: gauriAerisire from the script", e);
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
