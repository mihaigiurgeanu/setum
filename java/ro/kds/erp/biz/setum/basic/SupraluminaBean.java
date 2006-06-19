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
 * Standard implementation of the Supralumina session bean.
 * You will have to extend this class to proivde code for the abstract
 * methods loadFormData, newForm and saveSaveFormData.
 */
public abstract class SupraluminaBean 
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
            logger = Monolog.getMonologFactory().getLogger("ro.kds.erp.biz.setum.basic.Supralumina");
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
    // Supralumina implementation
    // ------------------------------------------------------------------
    protected SupraluminaForm form;
    
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
	logger.log(BasicLevel.DEBUG, "Loading Supralumina with id = " + loadId);
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
     * Create a new SupraluminaForm and initialize the 
     * <code>this.form</code> instance variable. Overwrite this method
     * if you want to provide other code for initializing the form bean.
     */
    protected void createNewFormBean() {
	form = new SupraluminaForm();
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
     * <code>ro.kds.erp.biz.setum.basic.Supralumina_calculatedFields</code>
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
			      SupraluminaForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);

		addFieldsToScript(script);
		script.run();

		getFieldsFromScript(script, r);

	    } catch (ScriptErrorException e) {
		logger.log(BasicLevel.ERROR, "Error executing rules script for Supralumina", e);
	    }
	}
	return r;
    }
	

    /**
     * Evaluates the validation script. The script loader loades the script
     * file corresponding to:
     *
     * <code>ro.kds.erp.biz.setum.basic.Supralumina_validation</code>
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
			      SupraluminaForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		addFieldsToScript(script);
		script.run();

		// I don't add modified fields back to the response bean,
		// so any fields modifications made in the script will
		// be ignored.
	    } catch (ScriptErrorException e) {
		logger.log(BasicLevel.ERROR, "Error executing rules script for Supralumina", e);
	    }
	}
	return r;
    }

    public ResponseBean updateTip(Integer tip) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getTip();
	form.setTip(tip);
	r.addRecord();
	r.addField("tip", tip); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".tip");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, Integer.class);
		script.setVar(FORM_VARNAME, form, SupraluminaForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the tip", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateLs(Double ls) {
        ResponseBean r = new ResponseBean();
	Double oldVal = form.getLs();
	form.setLs(ls);
	r.addRecord();
	r.addField("ls", ls); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".ls");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, Double.class);
		script.setVar(FORM_VARNAME, form, SupraluminaForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the ls", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateHs(Double hs) {
        ResponseBean r = new ResponseBean();
	Double oldVal = form.getHs();
	form.setHs(hs);
	r.addRecord();
	r.addField("hs", hs); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".hs");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, Double.class);
		script.setVar(FORM_VARNAME, form, SupraluminaForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the hs", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateCells(Integer cells) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getCells();
	form.setCells(cells);
	r.addRecord();
	r.addField("cells", cells); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".cells");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, Integer.class);
		script.setVar(FORM_VARNAME, form, SupraluminaForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the cells", e);
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
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".deschidere");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, Integer.class);
		script.setVar(FORM_VARNAME, form, SupraluminaForm.class);
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
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".sensDeschidere");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, Integer.class);
		script.setVar(FORM_VARNAME, form, SupraluminaForm.class);
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
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".pozitionareBalamale");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, Integer.class);
		script.setVar(FORM_VARNAME, form, SupraluminaForm.class);
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
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".componenta");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, Integer.class);
		script.setVar(FORM_VARNAME, form, SupraluminaForm.class);
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
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".tipComponenta");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, Integer.class);
		script.setVar(FORM_VARNAME, form, SupraluminaForm.class);
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
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".tipGeam");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, Integer.class);
		script.setVar(FORM_VARNAME, form, SupraluminaForm.class);
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
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".geamSimpluId");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, Integer.class);
		script.setVar(FORM_VARNAME, form, SupraluminaForm.class);
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
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".geamTermopanId");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, Integer.class);
		script.setVar(FORM_VARNAME, form, SupraluminaForm.class);
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
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".tipGrilaj");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, Integer.class);
		script.setVar(FORM_VARNAME, form, SupraluminaForm.class);
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
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".grilajStasId");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, Integer.class);
		script.setVar(FORM_VARNAME, form, SupraluminaForm.class);
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
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".valoareGrilajAtipic");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, java.math.BigDecimal.class);
		script.setVar(FORM_VARNAME, form, SupraluminaForm.class);
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
    public ResponseBean updateTipTabla(Integer tipTabla) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getTipTabla();
	form.setTipTabla(tipTabla);
	r.addRecord();
	r.addField("tipTabla", tipTabla); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".tipTabla");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, Integer.class);
		script.setVar(FORM_VARNAME, form, SupraluminaForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the tipTabla", e);
           }
        }
	computeCalculatedFields(r);
	return r;
    }
    public ResponseBean updateTablaId(Integer tablaId) {
        ResponseBean r = new ResponseBean();
	Integer oldVal = form.getTablaId();
	form.setTablaId(tablaId);
	r.addRecord();
	r.addField("tablaId", tablaId); // for number format
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".tablaId");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, Integer.class);
		script.setVar(FORM_VARNAME, form, SupraluminaForm.class);
		script.setVar(RESPONSE_VARNAME, r, ResponseBean.class);
		addFieldsToScript(script);
		script.run();
		getFieldsFromScript(script, r); // add all the changed
						// fields to the response also
	   } catch (ScriptErrorException e) {
	       logger.log(BasicLevel.ERROR, "Can not run the script for updating the tablaId", e);
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
		script.setVar(FORM_VARNAME, form, SupraluminaForm.class);
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
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".entryPrice");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, java.math.BigDecimal.class);
		script.setVar(FORM_VARNAME, form, SupraluminaForm.class);
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
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".price1");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, java.math.BigDecimal.class);
		script.setVar(FORM_VARNAME, form, SupraluminaForm.class);
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
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".businessCategory");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, String.class);
		script.setVar(FORM_VARNAME, form, SupraluminaForm.class);
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
	Script script = TclFileScript.loadScript(getScriptPrefix() + ".quantity");
	if(script.loaded()) {
	   try {
		script.setVar(LOGIC_VARNAME, this);
		script.setVar(OLDVAL_VARNAME, oldVal, Integer.class);
		script.setVar(FORM_VARNAME, form, SupraluminaForm.class);
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
	r.addField("tip", form.getTip());
	r.addField("ls", form.getLs());
	r.addField("hs", form.getHs());
	r.addField("cells", form.getCells());
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
	r.addField("tipTabla", form.getTipTabla());
	r.addField("tablaId", form.getTablaId());
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
	    s.setVar("tip", form.getTip(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: tip from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("ls", form.getLs(), Double.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: ls from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("hs", form.getHs(), Double.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: hs from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("cells", form.getCells(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: cells from the script");
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
	    s.setVar("tipTabla", form.getTipTabla(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: tipTabla from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    s.setVar("tablaId", form.getTablaId(), Integer.class);
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not set the value of field: tablaId from the script");
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
	    field = s.getVar("tip", Integer.class);
	    if(!field.equals(form.getTip())) {
	        logger.log(BasicLevel.DEBUG, "Field tip modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setTip((Integer)field);
	        r.addField("tip", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: tip from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("ls", Double.class);
	    if(!field.equals(form.getLs())) {
	        logger.log(BasicLevel.DEBUG, "Field ls modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setLs((Double)field);
	        r.addField("ls", (Double)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: ls from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("hs", Double.class);
	    if(!field.equals(form.getHs())) {
	        logger.log(BasicLevel.DEBUG, "Field hs modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setHs((Double)field);
	        r.addField("hs", (Double)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: hs from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("cells", Integer.class);
	    if(!field.equals(form.getCells())) {
	        logger.log(BasicLevel.DEBUG, "Field cells modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setCells((Integer)field);
	        r.addField("cells", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: cells from the script");
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
	    field = s.getVar("tipTabla", Integer.class);
	    if(!field.equals(form.getTipTabla())) {
	        logger.log(BasicLevel.DEBUG, "Field tipTabla modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setTipTabla((Integer)field);
	        r.addField("tipTabla", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: tipTabla from the script");
            logger.log(BasicLevel.DEBUG, e);
        }
	try {
	    field = s.getVar("tablaId", Integer.class);
	    if(!field.equals(form.getTablaId())) {
	        logger.log(BasicLevel.DEBUG, "Field tablaId modified by script. Its new value is <<" + (field==null?"null":field.toString()) + ">>");
	        form.setTablaId((Integer)field);
	        r.addField("tablaId", (Integer)field);
	    }
	} catch (ScriptErrorException e) {
	    logger.log(BasicLevel.WARN, "Can not get the value of field: tablaId from the script");
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
             return "ro.kds.erp.biz.setum.basic.Supralumina";
         }
         
     }
}

