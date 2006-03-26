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

    public ResponseBean updateCode(String code) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getCode();
	form.setCode(code);
	r.addRecord();
	r.addField("code", code); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.code");
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
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the code", e);
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
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.name");
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
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.description");
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
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the description", e);
           }
        }
	computeCalculatedFields(r);
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
    public ResponseBean updateK(Integer k) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getK();
	form.setK(k);
	r.addRecord();
	r.addField("k", k); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.k");
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
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the k", e);
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
    public ResponseBean updateEntryPrice(java.math.BigDecimal entryPrice) {
        ResponseBean r = new ResponseBean();
	java.math.BigDecimal oldVal = form.getEntryPrice();
	form.setEntryPrice(entryPrice);
	r.addRecord();
	r.addField("entryPrice", entryPrice); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.entryPrice");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, java.math.BigDecimal.class);
		script.setVar(FORM_VARNAME, form, UsaMetalica2KForm.class);
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
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.sellPrice");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, java.math.BigDecimal.class);
		script.setVar(FORM_VARNAME, form, UsaMetalica2KForm.class);
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
    public ResponseBean updateMontareSistem(Integer montareSistem) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getMontareSistem();
	form.setMontareSistem(montareSistem);
	r.addRecord();
	r.addField("montareSistem", montareSistem); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.montareSistem");
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
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the montareSistem", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateDecupareSistemId(Integer decupareSistemId) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getDecupareSistemId();
	form.setDecupareSistemId(decupareSistemId);
	r.addRecord();
	r.addField("decupareSistemId", decupareSistemId); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.decupareSistemId");
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
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the decupareSistemId", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateSistemSetumSauBeneficiar(Integer sistemSetumSauBeneficiar) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getSistemSetumSauBeneficiar();
	form.setSistemSetumSauBeneficiar(sistemSetumSauBeneficiar);
	r.addRecord();
	r.addField("sistemSetumSauBeneficiar", sistemSetumSauBeneficiar); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.sistemSetumSauBeneficiar");
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
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the sistemSetumSauBeneficiar", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateBroascaId(Integer broascaId) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getBroascaId();
	form.setBroascaId(broascaId);
	r.addRecord();
	r.addField("broascaId", broascaId); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.broascaId");
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
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the broascaId", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateBroascaBuc(Integer broascaBuc) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getBroascaBuc();
	form.setBroascaBuc(broascaBuc);
	r.addRecord();
	r.addField("broascaBuc", broascaBuc); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.broascaBuc");
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
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the broascaBuc", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateCilindruId(Integer cilindruId) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getCilindruId();
	form.setCilindruId(cilindruId);
	r.addRecord();
	r.addField("cilindruId", cilindruId); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.cilindruId");
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
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the cilindruId", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateCilindruBuc(Integer cilindruBuc) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getCilindruBuc();
	form.setCilindruBuc(cilindruBuc);
	r.addRecord();
	r.addField("cilindruBuc", cilindruBuc); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.cilindruBuc");
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
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the cilindruBuc", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateCopiatCheieId(Integer copiatCheieId) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getCopiatCheieId();
	form.setCopiatCheieId(copiatCheieId);
	r.addRecord();
	r.addField("copiatCheieId", copiatCheieId); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.copiatCheieId");
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
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the copiatCheieId", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateCopiatCheieBuc(Integer copiatCheieBuc) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getCopiatCheieBuc();
	form.setCopiatCheieBuc(copiatCheieBuc);
	r.addRecord();
	r.addField("copiatCheieBuc", copiatCheieBuc); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.copiatCheieBuc");
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
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the copiatCheieBuc", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateSildId(Integer sildId) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getSildId();
	form.setSildId(sildId);
	r.addRecord();
	r.addField("sildId", sildId); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.sildId");
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
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the sildId", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateSildTip(String sildTip) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getSildTip();
	form.setSildTip(sildTip);
	r.addRecord();
	r.addField("sildTip", sildTip); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.sildTip");
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
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the sildTip", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateSildCuloare(String sildCuloare) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getSildCuloare();
	form.setSildCuloare(sildCuloare);
	r.addRecord();
	r.addField("sildCuloare", sildCuloare); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.sildCuloare");
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
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the sildCuloare", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateSildBuc(Integer sildBuc) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getSildBuc();
	form.setSildBuc(sildBuc);
	r.addRecord();
	r.addField("sildBuc", sildBuc); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.sildBuc");
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
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the sildBuc", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateRozetaId(Integer rozetaId) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getRozetaId();
	form.setRozetaId(rozetaId);
	r.addRecord();
	r.addField("rozetaId", rozetaId); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.rozetaId");
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
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the rozetaId", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateRozetaTip(String rozetaTip) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getRozetaTip();
	form.setRozetaTip(rozetaTip);
	r.addRecord();
	r.addField("rozetaTip", rozetaTip); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.rozetaTip");
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
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the rozetaTip", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateRozetaCuloare(String rozetaCuloare) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getRozetaCuloare();
	form.setRozetaCuloare(rozetaCuloare);
	r.addRecord();
	r.addField("rozetaCuloare", rozetaCuloare); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.rozetaCuloare");
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
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the rozetaCuloare", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateRozetaBuc(Integer rozetaBuc) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getRozetaBuc();
	form.setRozetaBuc(rozetaBuc);
	r.addRecord();
	r.addField("rozetaBuc", rozetaBuc); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.rozetaBuc");
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
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the rozetaBuc", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateManerId(Integer manerId) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getManerId();
	form.setManerId(manerId);
	r.addRecord();
	r.addField("manerId", manerId); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.manerId");
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
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the manerId", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateManerTip(String manerTip) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getManerTip();
	form.setManerTip(manerTip);
	r.addRecord();
	r.addField("manerTip", manerTip); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.manerTip");
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
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the manerTip", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateManerCuloare(String manerCuloare) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getManerCuloare();
	form.setManerCuloare(manerCuloare);
	r.addRecord();
	r.addField("manerCuloare", manerCuloare); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.manerCuloare");
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
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the manerCuloare", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateManerBuc(Integer manerBuc) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getManerBuc();
	form.setManerBuc(manerBuc);
	r.addRecord();
	r.addField("manerBuc", manerBuc); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.manerBuc");
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
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the manerBuc", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateYalla1Id(Integer yalla1Id) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getYalla1Id();
	form.setYalla1Id(yalla1Id);
	r.addRecord();
	r.addField("yalla1Id", yalla1Id); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.yalla1Id");
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
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the yalla1Id", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateYalla1Buc(Integer yalla1Buc) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getYalla1Buc();
	form.setYalla1Buc(yalla1Buc);
	r.addRecord();
	r.addField("yalla1Buc", yalla1Buc); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.yalla1Buc");
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
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the yalla1Buc", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateYalla2Id(Integer yalla2Id) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getYalla2Id();
	form.setYalla2Id(yalla2Id);
	r.addRecord();
	r.addField("yalla2Id", yalla2Id); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.yalla2Id");
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
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the yalla2Id", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateYalla2Buc(Integer yalla2Buc) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getYalla2Buc();
	form.setYalla2Buc(yalla2Buc);
	r.addRecord();
	r.addField("yalla2Buc", yalla2Buc); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.yalla2Buc");
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
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the yalla2Buc", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateBaraAntipanicaId(Integer baraAntipanicaId) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getBaraAntipanicaId();
	form.setBaraAntipanicaId(baraAntipanicaId);
	r.addRecord();
	r.addField("baraAntipanicaId", baraAntipanicaId); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.baraAntipanicaId");
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
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the baraAntipanicaId", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateBaraAntipanicaBuc(Integer baraAntipanicaBuc) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getBaraAntipanicaBuc();
	form.setBaraAntipanicaBuc(baraAntipanicaBuc);
	r.addRecord();
	r.addField("baraAntipanicaBuc", baraAntipanicaBuc); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.baraAntipanicaBuc");
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
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the baraAntipanicaBuc", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateManerSemicilindruId(Integer manerSemicilindruId) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getManerSemicilindruId();
	form.setManerSemicilindruId(manerSemicilindruId);
	r.addRecord();
	r.addField("manerSemicilindruId", manerSemicilindruId); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.manerSemicilindruId");
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
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the manerSemicilindruId", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateManerSemicilindruBuc(Integer manerSemicilindruBuc) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getManerSemicilindruBuc();
	form.setManerSemicilindruBuc(manerSemicilindruBuc);
	r.addRecord();
	r.addField("manerSemicilindruBuc", manerSemicilindruBuc); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.manerSemicilindruBuc");
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
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the manerSemicilindruBuc", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateSelectorOrdineId(Integer selectorOrdineId) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getSelectorOrdineId();
	form.setSelectorOrdineId(selectorOrdineId);
	r.addRecord();
	r.addField("selectorOrdineId", selectorOrdineId); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.selectorOrdineId");
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
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the selectorOrdineId", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateSelectorOrdineBuc(Integer selectorOrdineBuc) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getSelectorOrdineBuc();
	form.setSelectorOrdineBuc(selectorOrdineBuc);
	r.addRecord();
	r.addField("selectorOrdineBuc", selectorOrdineBuc); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.selectorOrdineBuc");
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
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the selectorOrdineBuc", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateAmortizorId(Integer amortizorId) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getAmortizorId();
	form.setAmortizorId(amortizorId);
	r.addRecord();
	r.addField("amortizorId", amortizorId); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.amortizorId");
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
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the amortizorId", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateAmortizorBuc(Integer amortizorBuc) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getAmortizorBuc();
	form.setAmortizorBuc(amortizorBuc);
	r.addRecord();
	r.addField("amortizorBuc", amortizorBuc); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.amortizorBuc");
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
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the amortizorBuc", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateAlteSisteme1Id(Integer alteSisteme1Id) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getAlteSisteme1Id();
	form.setAlteSisteme1Id(alteSisteme1Id);
	r.addRecord();
	r.addField("alteSisteme1Id", alteSisteme1Id); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.alteSisteme1Id");
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
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the alteSisteme1Id", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateAlteSisteme1Buc(Integer alteSisteme1Buc) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getAlteSisteme1Buc();
	form.setAlteSisteme1Buc(alteSisteme1Buc);
	r.addRecord();
	r.addField("alteSisteme1Buc", alteSisteme1Buc); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.alteSisteme1Buc");
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
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the alteSisteme1Buc", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateAlteSisteme2Id(Integer alteSisteme2Id) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getAlteSisteme2Id();
	form.setAlteSisteme2Id(alteSisteme2Id);
	r.addRecord();
	r.addField("alteSisteme2Id", alteSisteme2Id); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.alteSisteme2Id");
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
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the alteSisteme2Id", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateAlteSisteme2Buc(Integer alteSisteme2Buc) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getAlteSisteme2Buc();
	form.setAlteSisteme2Buc(alteSisteme2Buc);
	r.addRecord();
	r.addField("alteSisteme2Buc", alteSisteme2Buc); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.alteSisteme2Buc");
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
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the alteSisteme2Buc", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateBenefBroasca(String benefBroasca) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getBenefBroasca();
	form.setBenefBroasca(benefBroasca);
	r.addRecord();
	r.addField("benefBroasca", benefBroasca); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.benefBroasca");
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
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the benefBroasca", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateBenefBroascaBuc(Integer benefBroascaBuc) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getBenefBroascaBuc();
	form.setBenefBroascaBuc(benefBroascaBuc);
	r.addRecord();
	r.addField("benefBroascaBuc", benefBroascaBuc); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.benefBroascaBuc");
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
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the benefBroascaBuc", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateBenefBroascaTip(Integer benefBroascaTip) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getBenefBroascaTip();
	form.setBenefBroascaTip(benefBroascaTip);
	r.addRecord();
	r.addField("benefBroascaTip", benefBroascaTip); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.benefBroascaTip");
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
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the benefBroascaTip", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateBenefCilindru(String benefCilindru) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getBenefCilindru();
	form.setBenefCilindru(benefCilindru);
	r.addRecord();
	r.addField("benefCilindru", benefCilindru); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.benefCilindru");
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
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the benefCilindru", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateBenefCilindruBuc(Integer benefCilindruBuc) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getBenefCilindruBuc();
	form.setBenefCilindruBuc(benefCilindruBuc);
	r.addRecord();
	r.addField("benefCilindruBuc", benefCilindruBuc); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.benefCilindruBuc");
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
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the benefCilindruBuc", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateBenefCilindruTip(Integer benefCilindruTip) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getBenefCilindruTip();
	form.setBenefCilindruTip(benefCilindruTip);
	r.addRecord();
	r.addField("benefCilindruTip", benefCilindruTip); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.benefCilindruTip");
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
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the benefCilindruTip", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateBenefSild(String benefSild) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getBenefSild();
	form.setBenefSild(benefSild);
	r.addRecord();
	r.addField("benefSild", benefSild); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.benefSild");
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
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the benefSild", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateBenefSildBuc(Integer benefSildBuc) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getBenefSildBuc();
	form.setBenefSildBuc(benefSildBuc);
	r.addRecord();
	r.addField("benefSildBuc", benefSildBuc); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.benefSildBuc");
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
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the benefSildBuc", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateBenefSildTip(Integer benefSildTip) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getBenefSildTip();
	form.setBenefSildTip(benefSildTip);
	r.addRecord();
	r.addField("benefSildTip", benefSildTip); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.benefSildTip");
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
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the benefSildTip", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateBenefYalla(String benefYalla) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getBenefYalla();
	form.setBenefYalla(benefYalla);
	r.addRecord();
	r.addField("benefYalla", benefYalla); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.benefYalla");
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
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the benefYalla", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateBenefYallaBuc(Integer benefYallaBuc) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getBenefYallaBuc();
	form.setBenefYallaBuc(benefYallaBuc);
	r.addRecord();
	r.addField("benefYallaBuc", benefYallaBuc); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.benefYallaBuc");
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
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the benefYallaBuc", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateBenefYallaTip(Integer benefYallaTip) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getBenefYallaTip();
	form.setBenefYallaTip(benefYallaTip);
	r.addRecord();
	r.addField("benefYallaTip", benefYallaTip); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.benefYallaTip");
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
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the benefYallaTip", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateBenefBaraAntipanica(String benefBaraAntipanica) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getBenefBaraAntipanica();
	form.setBenefBaraAntipanica(benefBaraAntipanica);
	r.addRecord();
	r.addField("benefBaraAntipanica", benefBaraAntipanica); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.benefBaraAntipanica");
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
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the benefBaraAntipanica", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateBenefBaraAntipanicaBuc(Integer benefBaraAntipanicaBuc) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getBenefBaraAntipanicaBuc();
	form.setBenefBaraAntipanicaBuc(benefBaraAntipanicaBuc);
	r.addRecord();
	r.addField("benefBaraAntipanicaBuc", benefBaraAntipanicaBuc); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.benefBaraAntipanicaBuc");
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
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the benefBaraAntipanicaBuc", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateBenefBaraAntipanicaTip(Integer benefBaraAntipanicaTip) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getBenefBaraAntipanicaTip();
	form.setBenefBaraAntipanicaTip(benefBaraAntipanicaTip);
	r.addRecord();
	r.addField("benefBaraAntipanicaTip", benefBaraAntipanicaTip); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.benefBaraAntipanicaTip");
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
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the benefBaraAntipanicaTip", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateBenefManer(String benefManer) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getBenefManer();
	form.setBenefManer(benefManer);
	r.addRecord();
	r.addField("benefManer", benefManer); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.benefManer");
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
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the benefManer", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateBenefManerBuc(Integer benefManerBuc) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getBenefManerBuc();
	form.setBenefManerBuc(benefManerBuc);
	r.addRecord();
	r.addField("benefManerBuc", benefManerBuc); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.benefManerBuc");
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
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the benefManerBuc", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateBenefSelectorOrdine(String benefSelectorOrdine) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getBenefSelectorOrdine();
	form.setBenefSelectorOrdine(benefSelectorOrdine);
	r.addRecord();
	r.addField("benefSelectorOrdine", benefSelectorOrdine); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.benefSelectorOrdine");
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
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the benefSelectorOrdine", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateBenefSelectorOrdineBuc(Integer benefSelectorOrdineBuc) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getBenefSelectorOrdineBuc();
	form.setBenefSelectorOrdineBuc(benefSelectorOrdineBuc);
	r.addRecord();
	r.addField("benefSelectorOrdineBuc", benefSelectorOrdineBuc); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.benefSelectorOrdineBuc");
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
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the benefSelectorOrdineBuc", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateBenefAmortizor(String benefAmortizor) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getBenefAmortizor();
	form.setBenefAmortizor(benefAmortizor);
	r.addRecord();
	r.addField("benefAmortizor", benefAmortizor); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.benefAmortizor");
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
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the benefAmortizor", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateBenefAmortizorBuc(Integer benefAmortizorBuc) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getBenefAmortizorBuc();
	form.setBenefAmortizorBuc(benefAmortizorBuc);
	r.addRecord();
	r.addField("benefAmortizorBuc", benefAmortizorBuc); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.benefAmortizorBuc");
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
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the benefAmortizorBuc", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateBenefAlteSisteme1(String benefAlteSisteme1) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getBenefAlteSisteme1();
	form.setBenefAlteSisteme1(benefAlteSisteme1);
	r.addRecord();
	r.addField("benefAlteSisteme1", benefAlteSisteme1); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.benefAlteSisteme1");
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
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the benefAlteSisteme1", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateBenefAlteSisteme1Buc(Integer benefAlteSisteme1Buc) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getBenefAlteSisteme1Buc();
	form.setBenefAlteSisteme1Buc(benefAlteSisteme1Buc);
	r.addRecord();
	r.addField("benefAlteSisteme1Buc", benefAlteSisteme1Buc); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.benefAlteSisteme1Buc");
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
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the benefAlteSisteme1Buc", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateBenefAlteSisteme2(String benefAlteSisteme2) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getBenefAlteSisteme2();
	form.setBenefAlteSisteme2(benefAlteSisteme2);
	r.addRecord();
	r.addField("benefAlteSisteme2", benefAlteSisteme2); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.benefAlteSisteme2");
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
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the benefAlteSisteme2", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateBenefAlteSisteme2Buc(Integer benefAlteSisteme2Buc) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getBenefAlteSisteme2Buc();
	form.setBenefAlteSisteme2Buc(benefAlteSisteme2Buc);
	r.addRecord();
	r.addField("benefAlteSisteme2Buc", benefAlteSisteme2Buc); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.benefAlteSisteme2Buc");
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
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the benefAlteSisteme2Buc", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateSistemeComment(String sistemeComment) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getSistemeComment();
	form.setSistemeComment(sistemeComment);
	r.addRecord();
	r.addField("sistemeComment", sistemeComment); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.sistemeComment");
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
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the sistemeComment", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateIntFinisajBlat(String intFinisajBlat) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getIntFinisajBlat();
	form.setIntFinisajBlat(intFinisajBlat);
	r.addRecord();
	r.addField("intFinisajBlat", intFinisajBlat); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.intFinisajBlat");
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
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the intFinisajBlat", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateIntFinisajBlatId(Integer intFinisajBlatId) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getIntFinisajBlatId();
	form.setIntFinisajBlatId(intFinisajBlatId);
	r.addRecord();
	r.addField("intFinisajBlatId", intFinisajBlatId); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.intFinisajBlatId");
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
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the intFinisajBlatId", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateIntFinisajToc(String intFinisajToc) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getIntFinisajToc();
	form.setIntFinisajToc(intFinisajToc);
	r.addRecord();
	r.addField("intFinisajToc", intFinisajToc); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.intFinisajToc");
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
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the intFinisajToc", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateIntFinisajTocId(Integer intFinisajTocId) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getIntFinisajTocId();
	form.setIntFinisajTocId(intFinisajTocId);
	r.addRecord();
	r.addField("intFinisajTocId", intFinisajTocId); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.intFinisajTocId");
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
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the intFinisajTocId", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateIntFinisajGrilaj(Integer intFinisajGrilaj) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getIntFinisajGrilaj();
	form.setIntFinisajGrilaj(intFinisajGrilaj);
	r.addRecord();
	r.addField("intFinisajGrilaj", intFinisajGrilaj); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.intFinisajGrilaj");
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
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the intFinisajGrilaj", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateIntFinisajGrilajId(Integer intFinisajGrilajId) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getIntFinisajGrilajId();
	form.setIntFinisajGrilajId(intFinisajGrilajId);
	r.addRecord();
	r.addField("intFinisajGrilajId", intFinisajGrilajId); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.intFinisajGrilajId");
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
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the intFinisajGrilajId", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateIntFinisajFereastra(String intFinisajFereastra) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getIntFinisajFereastra();
	form.setIntFinisajFereastra(intFinisajFereastra);
	r.addRecord();
	r.addField("intFinisajFereastra", intFinisajFereastra); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.intFinisajFereastra");
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
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the intFinisajFereastra", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateIntFinisajFereastraId(Integer intFinisajFereastraId) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getIntFinisajFereastraId();
	form.setIntFinisajFereastraId(intFinisajFereastraId);
	r.addRecord();
	r.addField("intFinisajFereastraId", intFinisajFereastraId); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.intFinisajFereastraId");
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
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the intFinisajFereastraId", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateIntFinisajSupralumina(String intFinisajSupralumina) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getIntFinisajSupralumina();
	form.setIntFinisajSupralumina(intFinisajSupralumina);
	r.addRecord();
	r.addField("intFinisajSupralumina", intFinisajSupralumina); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.intFinisajSupralumina");
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
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the intFinisajSupralumina", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateIntFinisajSupraluminaId(Integer intFinisajSupraluminaId) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getIntFinisajSupraluminaId();
	form.setIntFinisajSupraluminaId(intFinisajSupraluminaId);
	r.addRecord();
	r.addField("intFinisajSupraluminaId", intFinisajSupraluminaId); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.intFinisajSupraluminaId");
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
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the intFinisajSupraluminaId", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateIntFinisajPanouLateral(String intFinisajPanouLateral) {
        ResponseBean r = new ResponseBean();
	String oldVal = form.getIntFinisajPanouLateral();
	form.setIntFinisajPanouLateral(intFinisajPanouLateral);
	r.addRecord();
	r.addField("intFinisajPanouLateral", intFinisajPanouLateral); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.intFinisajPanouLateral");
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
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the intFinisajPanouLateral", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateIntFinisajPanouLateralId(Integer intFinisajPanouLateralId) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getIntFinisajPanouLateralId();
	form.setIntFinisajPanouLateralId(intFinisajPanouLateralId);
	r.addRecord();
	r.addField("intFinisajPanouLateralId", intFinisajPanouLateralId); // for number format
	Script script = TclFileScript.loadScript("ro.kds.erp.biz.setum.basic.UsaMetalica2K.intFinisajPanouLateralId");
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
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the intFinisajPanouLateralId", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }

    /**
     * Get the fields stored internaly and adds them to the response.
     */
    protected void copyFieldsToResponse(ResponseBean r) {
	r.addField("code", form.getCode());
	r.addField("name", form.getName());
	r.addField("description", form.getDescription());
	r.addField("subclass", form.getSubclass());
	r.addField("version", form.getVersion());
	r.addField("material", form.getMaterial());
	r.addField("k", form.getK());
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
	r.addField("entryPrice", form.getEntryPrice());
	r.addField("sellPrice", form.getSellPrice());
	r.addField("montareSistem", form.getMontareSistem());
	r.addField("decupareSistemId", form.getDecupareSistemId());
	r.addField("sistemSetumSauBeneficiar", form.getSistemSetumSauBeneficiar());
	r.addField("broascaId", form.getBroascaId());
	r.addField("broascaBuc", form.getBroascaBuc());
	r.addField("cilindruId", form.getCilindruId());
	r.addField("cilindruBuc", form.getCilindruBuc());
	r.addField("copiatCheieId", form.getCopiatCheieId());
	r.addField("copiatCheieBuc", form.getCopiatCheieBuc());
	r.addField("sildId", form.getSildId());
	r.addField("sildTip", form.getSildTip());
	r.addField("sildCuloare", form.getSildCuloare());
	r.addField("sildBuc", form.getSildBuc());
	r.addField("rozetaId", form.getRozetaId());
	r.addField("rozetaTip", form.getRozetaTip());
	r.addField("rozetaCuloare", form.getRozetaCuloare());
	r.addField("rozetaBuc", form.getRozetaBuc());
	r.addField("manerId", form.getManerId());
	r.addField("manerTip", form.getManerTip());
	r.addField("manerCuloare", form.getManerCuloare());
	r.addField("manerBuc", form.getManerBuc());
	r.addField("yalla1Id", form.getYalla1Id());
	r.addField("yalla1Buc", form.getYalla1Buc());
	r.addField("yalla2Id", form.getYalla2Id());
	r.addField("yalla2Buc", form.getYalla2Buc());
	r.addField("baraAntipanicaId", form.getBaraAntipanicaId());
	r.addField("baraAntipanicaBuc", form.getBaraAntipanicaBuc());
	r.addField("manerSemicilindruId", form.getManerSemicilindruId());
	r.addField("manerSemicilindruBuc", form.getManerSemicilindruBuc());
	r.addField("selectorOrdineId", form.getSelectorOrdineId());
	r.addField("selectorOrdineBuc", form.getSelectorOrdineBuc());
	r.addField("amortizorId", form.getAmortizorId());
	r.addField("amortizorBuc", form.getAmortizorBuc());
	r.addField("alteSisteme1Id", form.getAlteSisteme1Id());
	r.addField("alteSisteme1Buc", form.getAlteSisteme1Buc());
	r.addField("alteSisteme2Id", form.getAlteSisteme2Id());
	r.addField("alteSisteme2Buc", form.getAlteSisteme2Buc());
	r.addField("benefBroasca", form.getBenefBroasca());
	r.addField("benefBroascaBuc", form.getBenefBroascaBuc());
	r.addField("benefBroascaTip", form.getBenefBroascaTip());
	r.addField("benefCilindru", form.getBenefCilindru());
	r.addField("benefCilindruBuc", form.getBenefCilindruBuc());
	r.addField("benefCilindruTip", form.getBenefCilindruTip());
	r.addField("benefSild", form.getBenefSild());
	r.addField("benefSildBuc", form.getBenefSildBuc());
	r.addField("benefSildTip", form.getBenefSildTip());
	r.addField("benefYalla", form.getBenefYalla());
	r.addField("benefYallaBuc", form.getBenefYallaBuc());
	r.addField("benefYallaTip", form.getBenefYallaTip());
	r.addField("benefBaraAntipanica", form.getBenefBaraAntipanica());
	r.addField("benefBaraAntipanicaBuc", form.getBenefBaraAntipanicaBuc());
	r.addField("benefBaraAntipanicaTip", form.getBenefBaraAntipanicaTip());
	r.addField("benefManer", form.getBenefManer());
	r.addField("benefManerBuc", form.getBenefManerBuc());
	r.addField("benefSelectorOrdine", form.getBenefSelectorOrdine());
	r.addField("benefSelectorOrdineBuc", form.getBenefSelectorOrdineBuc());
	r.addField("benefAmortizor", form.getBenefAmortizor());
	r.addField("benefAmortizorBuc", form.getBenefAmortizorBuc());
	r.addField("benefAlteSisteme1", form.getBenefAlteSisteme1());
	r.addField("benefAlteSisteme1Buc", form.getBenefAlteSisteme1Buc());
	r.addField("benefAlteSisteme2", form.getBenefAlteSisteme2());
	r.addField("benefAlteSisteme2Buc", form.getBenefAlteSisteme2Buc());
	r.addField("sistemeComment", form.getSistemeComment());
	r.addField("intFinisajBlat", form.getIntFinisajBlat());
	r.addField("intFinisajBlatId", form.getIntFinisajBlatId());
	r.addField("intFinisajToc", form.getIntFinisajToc());
	r.addField("intFinisajTocId", form.getIntFinisajTocId());
	r.addField("intFinisajGrilaj", form.getIntFinisajGrilaj());
	r.addField("intFinisajGrilajId", form.getIntFinisajGrilajId());
	r.addField("intFinisajFereastra", form.getIntFinisajFereastra());
	r.addField("intFinisajFereastraId", form.getIntFinisajFereastraId());
	r.addField("intFinisajSupralumina", form.getIntFinisajSupralumina());
	r.addField("intFinisajSupraluminaId", form.getIntFinisajSupraluminaId());
	r.addField("intFinisajPanouLateral", form.getIntFinisajPanouLateral());
	r.addField("intFinisajPanouLateralId", form.getIntFinisajPanouLateralId());
	loadValueLists(r);
    }

    /**
     * Add all the fields of the form as variables for the script
     */
    protected void addFieldsToScript(Script s) {
	try {
	    s.setVar("code", form.getCode(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: code from the script", e);
        }
	try {
	    s.setVar("name", form.getName(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: name from the script", e);
        }
	try {
	    s.setVar("description", form.getDescription(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: description from the script", e);
        }
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
	    s.setVar("k", form.getK(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: k from the script", e);
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
	    s.setVar("entryPrice", form.getEntryPrice(), java.math.BigDecimal.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: entryPrice from the script", e);
        }
	try {
	    s.setVar("sellPrice", form.getSellPrice(), java.math.BigDecimal.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: sellPrice from the script", e);
        }
	try {
	    s.setVar("montareSistem", form.getMontareSistem(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: montareSistem from the script", e);
        }
	try {
	    s.setVar("decupareSistemId", form.getDecupareSistemId(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: decupareSistemId from the script", e);
        }
	try {
	    s.setVar("sistemSetumSauBeneficiar", form.getSistemSetumSauBeneficiar(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: sistemSetumSauBeneficiar from the script", e);
        }
	try {
	    s.setVar("broascaId", form.getBroascaId(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: broascaId from the script", e);
        }
	try {
	    s.setVar("broascaBuc", form.getBroascaBuc(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: broascaBuc from the script", e);
        }
	try {
	    s.setVar("cilindruId", form.getCilindruId(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: cilindruId from the script", e);
        }
	try {
	    s.setVar("cilindruBuc", form.getCilindruBuc(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: cilindruBuc from the script", e);
        }
	try {
	    s.setVar("copiatCheieId", form.getCopiatCheieId(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: copiatCheieId from the script", e);
        }
	try {
	    s.setVar("copiatCheieBuc", form.getCopiatCheieBuc(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: copiatCheieBuc from the script", e);
        }
	try {
	    s.setVar("sildId", form.getSildId(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: sildId from the script", e);
        }
	try {
	    s.setVar("sildTip", form.getSildTip(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: sildTip from the script", e);
        }
	try {
	    s.setVar("sildCuloare", form.getSildCuloare(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: sildCuloare from the script", e);
        }
	try {
	    s.setVar("sildBuc", form.getSildBuc(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: sildBuc from the script", e);
        }
	try {
	    s.setVar("rozetaId", form.getRozetaId(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: rozetaId from the script", e);
        }
	try {
	    s.setVar("rozetaTip", form.getRozetaTip(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: rozetaTip from the script", e);
        }
	try {
	    s.setVar("rozetaCuloare", form.getRozetaCuloare(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: rozetaCuloare from the script", e);
        }
	try {
	    s.setVar("rozetaBuc", form.getRozetaBuc(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: rozetaBuc from the script", e);
        }
	try {
	    s.setVar("manerId", form.getManerId(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: manerId from the script", e);
        }
	try {
	    s.setVar("manerTip", form.getManerTip(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: manerTip from the script", e);
        }
	try {
	    s.setVar("manerCuloare", form.getManerCuloare(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: manerCuloare from the script", e);
        }
	try {
	    s.setVar("manerBuc", form.getManerBuc(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: manerBuc from the script", e);
        }
	try {
	    s.setVar("yalla1Id", form.getYalla1Id(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: yalla1Id from the script", e);
        }
	try {
	    s.setVar("yalla1Buc", form.getYalla1Buc(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: yalla1Buc from the script", e);
        }
	try {
	    s.setVar("yalla2Id", form.getYalla2Id(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: yalla2Id from the script", e);
        }
	try {
	    s.setVar("yalla2Buc", form.getYalla2Buc(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: yalla2Buc from the script", e);
        }
	try {
	    s.setVar("baraAntipanicaId", form.getBaraAntipanicaId(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: baraAntipanicaId from the script", e);
        }
	try {
	    s.setVar("baraAntipanicaBuc", form.getBaraAntipanicaBuc(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: baraAntipanicaBuc from the script", e);
        }
	try {
	    s.setVar("manerSemicilindruId", form.getManerSemicilindruId(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: manerSemicilindruId from the script", e);
        }
	try {
	    s.setVar("manerSemicilindruBuc", form.getManerSemicilindruBuc(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: manerSemicilindruBuc from the script", e);
        }
	try {
	    s.setVar("selectorOrdineId", form.getSelectorOrdineId(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: selectorOrdineId from the script", e);
        }
	try {
	    s.setVar("selectorOrdineBuc", form.getSelectorOrdineBuc(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: selectorOrdineBuc from the script", e);
        }
	try {
	    s.setVar("amortizorId", form.getAmortizorId(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: amortizorId from the script", e);
        }
	try {
	    s.setVar("amortizorBuc", form.getAmortizorBuc(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: amortizorBuc from the script", e);
        }
	try {
	    s.setVar("alteSisteme1Id", form.getAlteSisteme1Id(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: alteSisteme1Id from the script", e);
        }
	try {
	    s.setVar("alteSisteme1Buc", form.getAlteSisteme1Buc(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: alteSisteme1Buc from the script", e);
        }
	try {
	    s.setVar("alteSisteme2Id", form.getAlteSisteme2Id(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: alteSisteme2Id from the script", e);
        }
	try {
	    s.setVar("alteSisteme2Buc", form.getAlteSisteme2Buc(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: alteSisteme2Buc from the script", e);
        }
	try {
	    s.setVar("benefBroasca", form.getBenefBroasca(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: benefBroasca from the script", e);
        }
	try {
	    s.setVar("benefBroascaBuc", form.getBenefBroascaBuc(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: benefBroascaBuc from the script", e);
        }
	try {
	    s.setVar("benefBroascaTip", form.getBenefBroascaTip(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: benefBroascaTip from the script", e);
        }
	try {
	    s.setVar("benefCilindru", form.getBenefCilindru(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: benefCilindru from the script", e);
        }
	try {
	    s.setVar("benefCilindruBuc", form.getBenefCilindruBuc(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: benefCilindruBuc from the script", e);
        }
	try {
	    s.setVar("benefCilindruTip", form.getBenefCilindruTip(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: benefCilindruTip from the script", e);
        }
	try {
	    s.setVar("benefSild", form.getBenefSild(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: benefSild from the script", e);
        }
	try {
	    s.setVar("benefSildBuc", form.getBenefSildBuc(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: benefSildBuc from the script", e);
        }
	try {
	    s.setVar("benefSildTip", form.getBenefSildTip(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: benefSildTip from the script", e);
        }
	try {
	    s.setVar("benefYalla", form.getBenefYalla(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: benefYalla from the script", e);
        }
	try {
	    s.setVar("benefYallaBuc", form.getBenefYallaBuc(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: benefYallaBuc from the script", e);
        }
	try {
	    s.setVar("benefYallaTip", form.getBenefYallaTip(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: benefYallaTip from the script", e);
        }
	try {
	    s.setVar("benefBaraAntipanica", form.getBenefBaraAntipanica(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: benefBaraAntipanica from the script", e);
        }
	try {
	    s.setVar("benefBaraAntipanicaBuc", form.getBenefBaraAntipanicaBuc(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: benefBaraAntipanicaBuc from the script", e);
        }
	try {
	    s.setVar("benefBaraAntipanicaTip", form.getBenefBaraAntipanicaTip(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: benefBaraAntipanicaTip from the script", e);
        }
	try {
	    s.setVar("benefManer", form.getBenefManer(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: benefManer from the script", e);
        }
	try {
	    s.setVar("benefManerBuc", form.getBenefManerBuc(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: benefManerBuc from the script", e);
        }
	try {
	    s.setVar("benefSelectorOrdine", form.getBenefSelectorOrdine(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: benefSelectorOrdine from the script", e);
        }
	try {
	    s.setVar("benefSelectorOrdineBuc", form.getBenefSelectorOrdineBuc(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: benefSelectorOrdineBuc from the script", e);
        }
	try {
	    s.setVar("benefAmortizor", form.getBenefAmortizor(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: benefAmortizor from the script", e);
        }
	try {
	    s.setVar("benefAmortizorBuc", form.getBenefAmortizorBuc(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: benefAmortizorBuc from the script", e);
        }
	try {
	    s.setVar("benefAlteSisteme1", form.getBenefAlteSisteme1(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: benefAlteSisteme1 from the script", e);
        }
	try {
	    s.setVar("benefAlteSisteme1Buc", form.getBenefAlteSisteme1Buc(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: benefAlteSisteme1Buc from the script", e);
        }
	try {
	    s.setVar("benefAlteSisteme2", form.getBenefAlteSisteme2(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: benefAlteSisteme2 from the script", e);
        }
	try {
	    s.setVar("benefAlteSisteme2Buc", form.getBenefAlteSisteme2Buc(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: benefAlteSisteme2Buc from the script", e);
        }
	try {
	    s.setVar("sistemeComment", form.getSistemeComment(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: sistemeComment from the script", e);
        }
	try {
	    s.setVar("intFinisajBlat", form.getIntFinisajBlat(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: intFinisajBlat from the script", e);
        }
	try {
	    s.setVar("intFinisajBlatId", form.getIntFinisajBlatId(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: intFinisajBlatId from the script", e);
        }
	try {
	    s.setVar("intFinisajToc", form.getIntFinisajToc(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: intFinisajToc from the script", e);
        }
	try {
	    s.setVar("intFinisajTocId", form.getIntFinisajTocId(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: intFinisajTocId from the script", e);
        }
	try {
	    s.setVar("intFinisajGrilaj", form.getIntFinisajGrilaj(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: intFinisajGrilaj from the script", e);
        }
	try {
	    s.setVar("intFinisajGrilajId", form.getIntFinisajGrilajId(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: intFinisajGrilajId from the script", e);
        }
	try {
	    s.setVar("intFinisajFereastra", form.getIntFinisajFereastra(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: intFinisajFereastra from the script", e);
        }
	try {
	    s.setVar("intFinisajFereastraId", form.getIntFinisajFereastraId(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: intFinisajFereastraId from the script", e);
        }
	try {
	    s.setVar("intFinisajSupralumina", form.getIntFinisajSupralumina(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: intFinisajSupralumina from the script", e);
        }
	try {
	    s.setVar("intFinisajSupraluminaId", form.getIntFinisajSupraluminaId(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: intFinisajSupraluminaId from the script", e);
        }
	try {
	    s.setVar("intFinisajPanouLateral", form.getIntFinisajPanouLateral(), String.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: intFinisajPanouLateral from the script", e);
        }
	try {
	    s.setVar("intFinisajPanouLateralId", form.getIntFinisajPanouLateralId(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: intFinisajPanouLateralId from the script", e);
        }
    }

    /**
     * Get the values of the fields from script variables. Writes in the
     * response bean any field that was modified.
     */
    protected void getFieldsFromScript(Script s, ResponseBean r) {
	Object field;
	try {
	    field = s.getVar("code", String.class);
	    if(!field.equals(form.getCode())) {
	        logger.log(BasicLevel.DEBUG, "Field code modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setCode((String)field);
	        r.addField("code", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: code from the script", e);
        }
	try {
	    field = s.getVar("name", String.class);
	    if(!field.equals(form.getName())) {
	        logger.log(BasicLevel.DEBUG, "Field name modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setName((String)field);
	        r.addField("name", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: name from the script", e);
        }
	try {
	    field = s.getVar("description", String.class);
	    if(!field.equals(form.getDescription())) {
	        logger.log(BasicLevel.DEBUG, "Field description modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setDescription((String)field);
	        r.addField("description", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: description from the script", e);
        }
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
	    field = s.getVar("k", Integer.class);
	    if(!field.equals(form.getK())) {
	        logger.log(BasicLevel.DEBUG, "Field k modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setK((Integer)field);
	        r.addField("k", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: k from the script", e);
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
	    field = s.getVar("entryPrice", java.math.BigDecimal.class);
	    if(!field.equals(form.getEntryPrice())) {
	        logger.log(BasicLevel.DEBUG, "Field entryPrice modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setEntryPrice((java.math.BigDecimal)field);
	        r.addField("entryPrice", (java.math.BigDecimal)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: entryPrice from the script", e);
        }
	try {
	    field = s.getVar("sellPrice", java.math.BigDecimal.class);
	    if(!field.equals(form.getSellPrice())) {
	        logger.log(BasicLevel.DEBUG, "Field sellPrice modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setSellPrice((java.math.BigDecimal)field);
	        r.addField("sellPrice", (java.math.BigDecimal)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: sellPrice from the script", e);
        }
	try {
	    field = s.getVar("montareSistem", Integer.class);
	    if(!field.equals(form.getMontareSistem())) {
	        logger.log(BasicLevel.DEBUG, "Field montareSistem modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setMontareSistem((Integer)field);
	        r.addField("montareSistem", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: montareSistem from the script", e);
        }
	try {
	    field = s.getVar("decupareSistemId", Integer.class);
	    if(!field.equals(form.getDecupareSistemId())) {
	        logger.log(BasicLevel.DEBUG, "Field decupareSistemId modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setDecupareSistemId((Integer)field);
	        r.addField("decupareSistemId", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: decupareSistemId from the script", e);
        }
	try {
	    field = s.getVar("sistemSetumSauBeneficiar", Integer.class);
	    if(!field.equals(form.getSistemSetumSauBeneficiar())) {
	        logger.log(BasicLevel.DEBUG, "Field sistemSetumSauBeneficiar modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setSistemSetumSauBeneficiar((Integer)field);
	        r.addField("sistemSetumSauBeneficiar", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: sistemSetumSauBeneficiar from the script", e);
        }
	try {
	    field = s.getVar("broascaId", Integer.class);
	    if(!field.equals(form.getBroascaId())) {
	        logger.log(BasicLevel.DEBUG, "Field broascaId modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setBroascaId((Integer)field);
	        r.addField("broascaId", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: broascaId from the script", e);
        }
	try {
	    field = s.getVar("broascaBuc", Integer.class);
	    if(!field.equals(form.getBroascaBuc())) {
	        logger.log(BasicLevel.DEBUG, "Field broascaBuc modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setBroascaBuc((Integer)field);
	        r.addField("broascaBuc", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: broascaBuc from the script", e);
        }
	try {
	    field = s.getVar("cilindruId", Integer.class);
	    if(!field.equals(form.getCilindruId())) {
	        logger.log(BasicLevel.DEBUG, "Field cilindruId modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setCilindruId((Integer)field);
	        r.addField("cilindruId", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: cilindruId from the script", e);
        }
	try {
	    field = s.getVar("cilindruBuc", Integer.class);
	    if(!field.equals(form.getCilindruBuc())) {
	        logger.log(BasicLevel.DEBUG, "Field cilindruBuc modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setCilindruBuc((Integer)field);
	        r.addField("cilindruBuc", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: cilindruBuc from the script", e);
        }
	try {
	    field = s.getVar("copiatCheieId", Integer.class);
	    if(!field.equals(form.getCopiatCheieId())) {
	        logger.log(BasicLevel.DEBUG, "Field copiatCheieId modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setCopiatCheieId((Integer)field);
	        r.addField("copiatCheieId", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: copiatCheieId from the script", e);
        }
	try {
	    field = s.getVar("copiatCheieBuc", Integer.class);
	    if(!field.equals(form.getCopiatCheieBuc())) {
	        logger.log(BasicLevel.DEBUG, "Field copiatCheieBuc modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setCopiatCheieBuc((Integer)field);
	        r.addField("copiatCheieBuc", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: copiatCheieBuc from the script", e);
        }
	try {
	    field = s.getVar("sildId", Integer.class);
	    if(!field.equals(form.getSildId())) {
	        logger.log(BasicLevel.DEBUG, "Field sildId modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setSildId((Integer)field);
	        r.addField("sildId", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: sildId from the script", e);
        }
	try {
	    field = s.getVar("sildTip", String.class);
	    if(!field.equals(form.getSildTip())) {
	        logger.log(BasicLevel.DEBUG, "Field sildTip modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setSildTip((String)field);
	        r.addField("sildTip", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: sildTip from the script", e);
        }
	try {
	    field = s.getVar("sildCuloare", String.class);
	    if(!field.equals(form.getSildCuloare())) {
	        logger.log(BasicLevel.DEBUG, "Field sildCuloare modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setSildCuloare((String)field);
	        r.addField("sildCuloare", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: sildCuloare from the script", e);
        }
	try {
	    field = s.getVar("sildBuc", Integer.class);
	    if(!field.equals(form.getSildBuc())) {
	        logger.log(BasicLevel.DEBUG, "Field sildBuc modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setSildBuc((Integer)field);
	        r.addField("sildBuc", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: sildBuc from the script", e);
        }
	try {
	    field = s.getVar("rozetaId", Integer.class);
	    if(!field.equals(form.getRozetaId())) {
	        logger.log(BasicLevel.DEBUG, "Field rozetaId modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setRozetaId((Integer)field);
	        r.addField("rozetaId", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: rozetaId from the script", e);
        }
	try {
	    field = s.getVar("rozetaTip", String.class);
	    if(!field.equals(form.getRozetaTip())) {
	        logger.log(BasicLevel.DEBUG, "Field rozetaTip modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setRozetaTip((String)field);
	        r.addField("rozetaTip", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: rozetaTip from the script", e);
        }
	try {
	    field = s.getVar("rozetaCuloare", String.class);
	    if(!field.equals(form.getRozetaCuloare())) {
	        logger.log(BasicLevel.DEBUG, "Field rozetaCuloare modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setRozetaCuloare((String)field);
	        r.addField("rozetaCuloare", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: rozetaCuloare from the script", e);
        }
	try {
	    field = s.getVar("rozetaBuc", Integer.class);
	    if(!field.equals(form.getRozetaBuc())) {
	        logger.log(BasicLevel.DEBUG, "Field rozetaBuc modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setRozetaBuc((Integer)field);
	        r.addField("rozetaBuc", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: rozetaBuc from the script", e);
        }
	try {
	    field = s.getVar("manerId", Integer.class);
	    if(!field.equals(form.getManerId())) {
	        logger.log(BasicLevel.DEBUG, "Field manerId modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setManerId((Integer)field);
	        r.addField("manerId", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: manerId from the script", e);
        }
	try {
	    field = s.getVar("manerTip", String.class);
	    if(!field.equals(form.getManerTip())) {
	        logger.log(BasicLevel.DEBUG, "Field manerTip modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setManerTip((String)field);
	        r.addField("manerTip", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: manerTip from the script", e);
        }
	try {
	    field = s.getVar("manerCuloare", String.class);
	    if(!field.equals(form.getManerCuloare())) {
	        logger.log(BasicLevel.DEBUG, "Field manerCuloare modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setManerCuloare((String)field);
	        r.addField("manerCuloare", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: manerCuloare from the script", e);
        }
	try {
	    field = s.getVar("manerBuc", Integer.class);
	    if(!field.equals(form.getManerBuc())) {
	        logger.log(BasicLevel.DEBUG, "Field manerBuc modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setManerBuc((Integer)field);
	        r.addField("manerBuc", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: manerBuc from the script", e);
        }
	try {
	    field = s.getVar("yalla1Id", Integer.class);
	    if(!field.equals(form.getYalla1Id())) {
	        logger.log(BasicLevel.DEBUG, "Field yalla1Id modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setYalla1Id((Integer)field);
	        r.addField("yalla1Id", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: yalla1Id from the script", e);
        }
	try {
	    field = s.getVar("yalla1Buc", Integer.class);
	    if(!field.equals(form.getYalla1Buc())) {
	        logger.log(BasicLevel.DEBUG, "Field yalla1Buc modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setYalla1Buc((Integer)field);
	        r.addField("yalla1Buc", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: yalla1Buc from the script", e);
        }
	try {
	    field = s.getVar("yalla2Id", Integer.class);
	    if(!field.equals(form.getYalla2Id())) {
	        logger.log(BasicLevel.DEBUG, "Field yalla2Id modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setYalla2Id((Integer)field);
	        r.addField("yalla2Id", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: yalla2Id from the script", e);
        }
	try {
	    field = s.getVar("yalla2Buc", Integer.class);
	    if(!field.equals(form.getYalla2Buc())) {
	        logger.log(BasicLevel.DEBUG, "Field yalla2Buc modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setYalla2Buc((Integer)field);
	        r.addField("yalla2Buc", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: yalla2Buc from the script", e);
        }
	try {
	    field = s.getVar("baraAntipanicaId", Integer.class);
	    if(!field.equals(form.getBaraAntipanicaId())) {
	        logger.log(BasicLevel.DEBUG, "Field baraAntipanicaId modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setBaraAntipanicaId((Integer)field);
	        r.addField("baraAntipanicaId", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: baraAntipanicaId from the script", e);
        }
	try {
	    field = s.getVar("baraAntipanicaBuc", Integer.class);
	    if(!field.equals(form.getBaraAntipanicaBuc())) {
	        logger.log(BasicLevel.DEBUG, "Field baraAntipanicaBuc modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setBaraAntipanicaBuc((Integer)field);
	        r.addField("baraAntipanicaBuc", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: baraAntipanicaBuc from the script", e);
        }
	try {
	    field = s.getVar("manerSemicilindruId", Integer.class);
	    if(!field.equals(form.getManerSemicilindruId())) {
	        logger.log(BasicLevel.DEBUG, "Field manerSemicilindruId modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setManerSemicilindruId((Integer)field);
	        r.addField("manerSemicilindruId", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: manerSemicilindruId from the script", e);
        }
	try {
	    field = s.getVar("manerSemicilindruBuc", Integer.class);
	    if(!field.equals(form.getManerSemicilindruBuc())) {
	        logger.log(BasicLevel.DEBUG, "Field manerSemicilindruBuc modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setManerSemicilindruBuc((Integer)field);
	        r.addField("manerSemicilindruBuc", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: manerSemicilindruBuc from the script", e);
        }
	try {
	    field = s.getVar("selectorOrdineId", Integer.class);
	    if(!field.equals(form.getSelectorOrdineId())) {
	        logger.log(BasicLevel.DEBUG, "Field selectorOrdineId modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setSelectorOrdineId((Integer)field);
	        r.addField("selectorOrdineId", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: selectorOrdineId from the script", e);
        }
	try {
	    field = s.getVar("selectorOrdineBuc", Integer.class);
	    if(!field.equals(form.getSelectorOrdineBuc())) {
	        logger.log(BasicLevel.DEBUG, "Field selectorOrdineBuc modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setSelectorOrdineBuc((Integer)field);
	        r.addField("selectorOrdineBuc", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: selectorOrdineBuc from the script", e);
        }
	try {
	    field = s.getVar("amortizorId", Integer.class);
	    if(!field.equals(form.getAmortizorId())) {
	        logger.log(BasicLevel.DEBUG, "Field amortizorId modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setAmortizorId((Integer)field);
	        r.addField("amortizorId", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: amortizorId from the script", e);
        }
	try {
	    field = s.getVar("amortizorBuc", Integer.class);
	    if(!field.equals(form.getAmortizorBuc())) {
	        logger.log(BasicLevel.DEBUG, "Field amortizorBuc modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setAmortizorBuc((Integer)field);
	        r.addField("amortizorBuc", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: amortizorBuc from the script", e);
        }
	try {
	    field = s.getVar("alteSisteme1Id", Integer.class);
	    if(!field.equals(form.getAlteSisteme1Id())) {
	        logger.log(BasicLevel.DEBUG, "Field alteSisteme1Id modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setAlteSisteme1Id((Integer)field);
	        r.addField("alteSisteme1Id", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: alteSisteme1Id from the script", e);
        }
	try {
	    field = s.getVar("alteSisteme1Buc", Integer.class);
	    if(!field.equals(form.getAlteSisteme1Buc())) {
	        logger.log(BasicLevel.DEBUG, "Field alteSisteme1Buc modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setAlteSisteme1Buc((Integer)field);
	        r.addField("alteSisteme1Buc", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: alteSisteme1Buc from the script", e);
        }
	try {
	    field = s.getVar("alteSisteme2Id", Integer.class);
	    if(!field.equals(form.getAlteSisteme2Id())) {
	        logger.log(BasicLevel.DEBUG, "Field alteSisteme2Id modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setAlteSisteme2Id((Integer)field);
	        r.addField("alteSisteme2Id", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: alteSisteme2Id from the script", e);
        }
	try {
	    field = s.getVar("alteSisteme2Buc", Integer.class);
	    if(!field.equals(form.getAlteSisteme2Buc())) {
	        logger.log(BasicLevel.DEBUG, "Field alteSisteme2Buc modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setAlteSisteme2Buc((Integer)field);
	        r.addField("alteSisteme2Buc", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: alteSisteme2Buc from the script", e);
        }
	try {
	    field = s.getVar("benefBroasca", String.class);
	    if(!field.equals(form.getBenefBroasca())) {
	        logger.log(BasicLevel.DEBUG, "Field benefBroasca modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setBenefBroasca((String)field);
	        r.addField("benefBroasca", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: benefBroasca from the script", e);
        }
	try {
	    field = s.getVar("benefBroascaBuc", Integer.class);
	    if(!field.equals(form.getBenefBroascaBuc())) {
	        logger.log(BasicLevel.DEBUG, "Field benefBroascaBuc modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setBenefBroascaBuc((Integer)field);
	        r.addField("benefBroascaBuc", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: benefBroascaBuc from the script", e);
        }
	try {
	    field = s.getVar("benefBroascaTip", Integer.class);
	    if(!field.equals(form.getBenefBroascaTip())) {
	        logger.log(BasicLevel.DEBUG, "Field benefBroascaTip modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setBenefBroascaTip((Integer)field);
	        r.addField("benefBroascaTip", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: benefBroascaTip from the script", e);
        }
	try {
	    field = s.getVar("benefCilindru", String.class);
	    if(!field.equals(form.getBenefCilindru())) {
	        logger.log(BasicLevel.DEBUG, "Field benefCilindru modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setBenefCilindru((String)field);
	        r.addField("benefCilindru", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: benefCilindru from the script", e);
        }
	try {
	    field = s.getVar("benefCilindruBuc", Integer.class);
	    if(!field.equals(form.getBenefCilindruBuc())) {
	        logger.log(BasicLevel.DEBUG, "Field benefCilindruBuc modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setBenefCilindruBuc((Integer)field);
	        r.addField("benefCilindruBuc", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: benefCilindruBuc from the script", e);
        }
	try {
	    field = s.getVar("benefCilindruTip", Integer.class);
	    if(!field.equals(form.getBenefCilindruTip())) {
	        logger.log(BasicLevel.DEBUG, "Field benefCilindruTip modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setBenefCilindruTip((Integer)field);
	        r.addField("benefCilindruTip", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: benefCilindruTip from the script", e);
        }
	try {
	    field = s.getVar("benefSild", String.class);
	    if(!field.equals(form.getBenefSild())) {
	        logger.log(BasicLevel.DEBUG, "Field benefSild modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setBenefSild((String)field);
	        r.addField("benefSild", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: benefSild from the script", e);
        }
	try {
	    field = s.getVar("benefSildBuc", Integer.class);
	    if(!field.equals(form.getBenefSildBuc())) {
	        logger.log(BasicLevel.DEBUG, "Field benefSildBuc modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setBenefSildBuc((Integer)field);
	        r.addField("benefSildBuc", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: benefSildBuc from the script", e);
        }
	try {
	    field = s.getVar("benefSildTip", Integer.class);
	    if(!field.equals(form.getBenefSildTip())) {
	        logger.log(BasicLevel.DEBUG, "Field benefSildTip modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setBenefSildTip((Integer)field);
	        r.addField("benefSildTip", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: benefSildTip from the script", e);
        }
	try {
	    field = s.getVar("benefYalla", String.class);
	    if(!field.equals(form.getBenefYalla())) {
	        logger.log(BasicLevel.DEBUG, "Field benefYalla modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setBenefYalla((String)field);
	        r.addField("benefYalla", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: benefYalla from the script", e);
        }
	try {
	    field = s.getVar("benefYallaBuc", Integer.class);
	    if(!field.equals(form.getBenefYallaBuc())) {
	        logger.log(BasicLevel.DEBUG, "Field benefYallaBuc modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setBenefYallaBuc((Integer)field);
	        r.addField("benefYallaBuc", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: benefYallaBuc from the script", e);
        }
	try {
	    field = s.getVar("benefYallaTip", Integer.class);
	    if(!field.equals(form.getBenefYallaTip())) {
	        logger.log(BasicLevel.DEBUG, "Field benefYallaTip modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setBenefYallaTip((Integer)field);
	        r.addField("benefYallaTip", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: benefYallaTip from the script", e);
        }
	try {
	    field = s.getVar("benefBaraAntipanica", String.class);
	    if(!field.equals(form.getBenefBaraAntipanica())) {
	        logger.log(BasicLevel.DEBUG, "Field benefBaraAntipanica modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setBenefBaraAntipanica((String)field);
	        r.addField("benefBaraAntipanica", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: benefBaraAntipanica from the script", e);
        }
	try {
	    field = s.getVar("benefBaraAntipanicaBuc", Integer.class);
	    if(!field.equals(form.getBenefBaraAntipanicaBuc())) {
	        logger.log(BasicLevel.DEBUG, "Field benefBaraAntipanicaBuc modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setBenefBaraAntipanicaBuc((Integer)field);
	        r.addField("benefBaraAntipanicaBuc", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: benefBaraAntipanicaBuc from the script", e);
        }
	try {
	    field = s.getVar("benefBaraAntipanicaTip", Integer.class);
	    if(!field.equals(form.getBenefBaraAntipanicaTip())) {
	        logger.log(BasicLevel.DEBUG, "Field benefBaraAntipanicaTip modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setBenefBaraAntipanicaTip((Integer)field);
	        r.addField("benefBaraAntipanicaTip", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: benefBaraAntipanicaTip from the script", e);
        }
	try {
	    field = s.getVar("benefManer", String.class);
	    if(!field.equals(form.getBenefManer())) {
	        logger.log(BasicLevel.DEBUG, "Field benefManer modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setBenefManer((String)field);
	        r.addField("benefManer", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: benefManer from the script", e);
        }
	try {
	    field = s.getVar("benefManerBuc", Integer.class);
	    if(!field.equals(form.getBenefManerBuc())) {
	        logger.log(BasicLevel.DEBUG, "Field benefManerBuc modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setBenefManerBuc((Integer)field);
	        r.addField("benefManerBuc", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: benefManerBuc from the script", e);
        }
	try {
	    field = s.getVar("benefSelectorOrdine", String.class);
	    if(!field.equals(form.getBenefSelectorOrdine())) {
	        logger.log(BasicLevel.DEBUG, "Field benefSelectorOrdine modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setBenefSelectorOrdine((String)field);
	        r.addField("benefSelectorOrdine", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: benefSelectorOrdine from the script", e);
        }
	try {
	    field = s.getVar("benefSelectorOrdineBuc", Integer.class);
	    if(!field.equals(form.getBenefSelectorOrdineBuc())) {
	        logger.log(BasicLevel.DEBUG, "Field benefSelectorOrdineBuc modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setBenefSelectorOrdineBuc((Integer)field);
	        r.addField("benefSelectorOrdineBuc", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: benefSelectorOrdineBuc from the script", e);
        }
	try {
	    field = s.getVar("benefAmortizor", String.class);
	    if(!field.equals(form.getBenefAmortizor())) {
	        logger.log(BasicLevel.DEBUG, "Field benefAmortizor modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setBenefAmortizor((String)field);
	        r.addField("benefAmortizor", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: benefAmortizor from the script", e);
        }
	try {
	    field = s.getVar("benefAmortizorBuc", Integer.class);
	    if(!field.equals(form.getBenefAmortizorBuc())) {
	        logger.log(BasicLevel.DEBUG, "Field benefAmortizorBuc modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setBenefAmortizorBuc((Integer)field);
	        r.addField("benefAmortizorBuc", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: benefAmortizorBuc from the script", e);
        }
	try {
	    field = s.getVar("benefAlteSisteme1", String.class);
	    if(!field.equals(form.getBenefAlteSisteme1())) {
	        logger.log(BasicLevel.DEBUG, "Field benefAlteSisteme1 modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setBenefAlteSisteme1((String)field);
	        r.addField("benefAlteSisteme1", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: benefAlteSisteme1 from the script", e);
        }
	try {
	    field = s.getVar("benefAlteSisteme1Buc", Integer.class);
	    if(!field.equals(form.getBenefAlteSisteme1Buc())) {
	        logger.log(BasicLevel.DEBUG, "Field benefAlteSisteme1Buc modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setBenefAlteSisteme1Buc((Integer)field);
	        r.addField("benefAlteSisteme1Buc", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: benefAlteSisteme1Buc from the script", e);
        }
	try {
	    field = s.getVar("benefAlteSisteme2", String.class);
	    if(!field.equals(form.getBenefAlteSisteme2())) {
	        logger.log(BasicLevel.DEBUG, "Field benefAlteSisteme2 modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setBenefAlteSisteme2((String)field);
	        r.addField("benefAlteSisteme2", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: benefAlteSisteme2 from the script", e);
        }
	try {
	    field = s.getVar("benefAlteSisteme2Buc", Integer.class);
	    if(!field.equals(form.getBenefAlteSisteme2Buc())) {
	        logger.log(BasicLevel.DEBUG, "Field benefAlteSisteme2Buc modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setBenefAlteSisteme2Buc((Integer)field);
	        r.addField("benefAlteSisteme2Buc", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: benefAlteSisteme2Buc from the script", e);
        }
	try {
	    field = s.getVar("sistemeComment", String.class);
	    if(!field.equals(form.getSistemeComment())) {
	        logger.log(BasicLevel.DEBUG, "Field sistemeComment modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setSistemeComment((String)field);
	        r.addField("sistemeComment", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: sistemeComment from the script", e);
        }
	try {
	    field = s.getVar("intFinisajBlat", String.class);
	    if(!field.equals(form.getIntFinisajBlat())) {
	        logger.log(BasicLevel.DEBUG, "Field intFinisajBlat modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setIntFinisajBlat((String)field);
	        r.addField("intFinisajBlat", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: intFinisajBlat from the script", e);
        }
	try {
	    field = s.getVar("intFinisajBlatId", Integer.class);
	    if(!field.equals(form.getIntFinisajBlatId())) {
	        logger.log(BasicLevel.DEBUG, "Field intFinisajBlatId modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setIntFinisajBlatId((Integer)field);
	        r.addField("intFinisajBlatId", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: intFinisajBlatId from the script", e);
        }
	try {
	    field = s.getVar("intFinisajToc", String.class);
	    if(!field.equals(form.getIntFinisajToc())) {
	        logger.log(BasicLevel.DEBUG, "Field intFinisajToc modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setIntFinisajToc((String)field);
	        r.addField("intFinisajToc", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: intFinisajToc from the script", e);
        }
	try {
	    field = s.getVar("intFinisajTocId", Integer.class);
	    if(!field.equals(form.getIntFinisajTocId())) {
	        logger.log(BasicLevel.DEBUG, "Field intFinisajTocId modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setIntFinisajTocId((Integer)field);
	        r.addField("intFinisajTocId", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: intFinisajTocId from the script", e);
        }
	try {
	    field = s.getVar("intFinisajGrilaj", Integer.class);
	    if(!field.equals(form.getIntFinisajGrilaj())) {
	        logger.log(BasicLevel.DEBUG, "Field intFinisajGrilaj modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setIntFinisajGrilaj((Integer)field);
	        r.addField("intFinisajGrilaj", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: intFinisajGrilaj from the script", e);
        }
	try {
	    field = s.getVar("intFinisajGrilajId", Integer.class);
	    if(!field.equals(form.getIntFinisajGrilajId())) {
	        logger.log(BasicLevel.DEBUG, "Field intFinisajGrilajId modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setIntFinisajGrilajId((Integer)field);
	        r.addField("intFinisajGrilajId", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: intFinisajGrilajId from the script", e);
        }
	try {
	    field = s.getVar("intFinisajFereastra", String.class);
	    if(!field.equals(form.getIntFinisajFereastra())) {
	        logger.log(BasicLevel.DEBUG, "Field intFinisajFereastra modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setIntFinisajFereastra((String)field);
	        r.addField("intFinisajFereastra", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: intFinisajFereastra from the script", e);
        }
	try {
	    field = s.getVar("intFinisajFereastraId", Integer.class);
	    if(!field.equals(form.getIntFinisajFereastraId())) {
	        logger.log(BasicLevel.DEBUG, "Field intFinisajFereastraId modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setIntFinisajFereastraId((Integer)field);
	        r.addField("intFinisajFereastraId", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: intFinisajFereastraId from the script", e);
        }
	try {
	    field = s.getVar("intFinisajSupralumina", String.class);
	    if(!field.equals(form.getIntFinisajSupralumina())) {
	        logger.log(BasicLevel.DEBUG, "Field intFinisajSupralumina modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setIntFinisajSupralumina((String)field);
	        r.addField("intFinisajSupralumina", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: intFinisajSupralumina from the script", e);
        }
	try {
	    field = s.getVar("intFinisajSupraluminaId", Integer.class);
	    if(!field.equals(form.getIntFinisajSupraluminaId())) {
	        logger.log(BasicLevel.DEBUG, "Field intFinisajSupraluminaId modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setIntFinisajSupraluminaId((Integer)field);
	        r.addField("intFinisajSupraluminaId", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: intFinisajSupraluminaId from the script", e);
        }
	try {
	    field = s.getVar("intFinisajPanouLateral", String.class);
	    if(!field.equals(form.getIntFinisajPanouLateral())) {
	        logger.log(BasicLevel.DEBUG, "Field intFinisajPanouLateral modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setIntFinisajPanouLateral((String)field);
	        r.addField("intFinisajPanouLateral", (String)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: intFinisajPanouLateral from the script", e);
        }
	try {
	    field = s.getVar("intFinisajPanouLateralId", Integer.class);
	    if(!field.equals(form.getIntFinisajPanouLateralId())) {
	        logger.log(BasicLevel.DEBUG, "Field intFinisajPanouLateralId modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setIntFinisajPanouLateralId((Integer)field);
	        r.addField("intFinisajPanouLateralId", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: intFinisajPanouLateralId from the script", e);
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

