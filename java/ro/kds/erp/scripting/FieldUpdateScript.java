package ro.kds.erp.scripting;

import ro.kds.erp.biz.ResponseBean;
import ro.kds.erp.biz.PreferencesBean;
import java.io.File;
import ro.kds.erp.biz.Preferences;
import tcl.lang.TclException;
import tcl.lang.Interp;
import tcl.lang.ReflectObject;
import org.objectweb.util.monolog.api.BasicLevel;
import org.objectweb.util.monolog.api.Logger;
import org.objectweb.jonas.common.Log;
import ro.kds.erp.biz.FormLogic;

/**
 * Evaluates an optional script on updating a form field. The
 * script will be able to validate the new value, perform certain
 * calculations and to modify values of another fields in the
 * form. 
 *
 * Maybe this class should be transformed into a statefull Session Bean
 * to uniformely present its functionality as a service.
 *
 * Created: Sat Oct 15 12:48:07 2005
 *
 * @author <a href="mailto:Mihai Giurgeanu@CRIMIRA"></a>
 * @version 1.0
 */
public class FieldUpdateScript {
    private String scriptFileName;
    static private Logger logger = Log.getLogger("ro.kds.erp.scripting.FieldUpdateScript");

    /**
     * The name of the system configurations (accessed through 
     * <code>Preferences</code> bean) containd the path to the
     * scripts folder. It's value is scripting.folder
     */
    public static final String SCRIPTING_FOLDER_OPTION = "scripting.folder";

    /**
     * The old value of the field. This value
     * will be passed to the script as the variable <code>oldVal</code>.
     */
    private Object oldVal;

    /**
     * The class for the oldVal object (the class of the field);
     */
    private Class oldValClass;

    /**
     * The bean that contains the fields of this form. It will be passed
     * to the script as <code>form</code> variable.
     */
    private Object form;
    
    /**
     * The class of the form object.
     */
    private Class formClass;

    /**
     * The bean which will contain the response values. It will be passed
     * to the script as the <code>response</code> variable. The script
     * should add to the response a record with the values of all
     * the calculated fields.
     */
    private ResponseBean response;

    /**
     * The oject managing the business logic of the form. 
     */
    private FormLogic logic;

    /**
     * Creates a new <code>FieldUpdateScript</code> instance.
     *
     */
    public FieldUpdateScript(String scriptFileName) {
	this.scriptFileName = scriptFileName;
    }

    /**
     * Verify if the script can be run. To do this verifies if the file
     * <code>scriptFileName</code> exists and can be read.
     */
    public boolean loaded() {
	File script = new File(scriptFileName);
	if(script.canRead())
	    return true;
     
	logger.log(BasicLevel.DEBUG, "Script dose not exists for file " +
		   scriptFileName);
	return false;
    }

    /**
     * The old value of the field. The script is called after
     * a new value was assigned to the field.  The script can 
     * access this value as the <code>oldVal</code> variable.
     */
    public FieldUpdateScript setOldValue(Object oldVal) {
	this.oldVal = oldVal;
	return this;
    }

    /**
     * Set the class of the <code>oldVal</code> variable. It is the same
     * as the class for the field this script is called for.
     */
    public FieldUpdateScript setOldValueClass(Class oldValClass) {
	this.oldValClass = oldValClass;
	return this;
    }

    /**
     * Sets the form bean value. It is the bean that contains the values
     * of all form fields.
     */
    public FieldUpdateScript setForm(Object form) {
	this.form = form;
	return this;
    }

    /**
     * Set the class used for the form bean that will be passed to the
     * script.
     */
    public FieldUpdateScript setFormClass(Class newFormClass) {
	this.formClass = newFormClass;
	return this;
    }

    /**
     * Sets the ResponseBean that will be used by the script to pass
     * back any calculated fields or error information.
     *
     */
    public FieldUpdateScript setResponseBean(ResponseBean response) {
	this.response = response;
	return this;
    }

    /**
     * Set the pointer to the object implementing the business logic
     * for the form. It should be the pointer to the session bean 
     * implmentation that evaluates this script.
     * 
     * In the script, it will be accessible as variable <code>$logic</code>.
     * The intent use is for calls like:
     * 
     * <code>
     * $logic applyRules
     * </code>
     */
    public FieldUpdateScript setFormLogic(FormLogic logic) {
	this.logic = logic;
	return this;
    }

    /**
     * Runs the script.
     *
     * @throws ScriptErroException if the script ends in error.
     */
    public void run() throws ScriptErrorException {
	logger.log(BasicLevel.DEBUG, "runing script " + scriptFileName);

	try {
	    Interp interp = new Interp();
	    
	    interp.setVar("logic", 
			  ReflectObject.newInstance(interp, 
						    FormLogic.class, 
						    logic), 
			  0);

	    interp.setVar("oldVal", 
			  ReflectObject.newInstance(interp, 
						    oldValClass, 
						    oldVal), 
			  0);
	    interp.setVar("form", 
			  ReflectObject.newInstance(interp, 
						    formClass, 
						    form), 
			  0);
	    interp.setVar("response", 
			  ReflectObject.newInstance(interp, 
						    ResponseBean.class, 
						    response), 
			  0);
		
	    interp.setVar("logger", 
			  ReflectObject.newInstance(interp, 
						    Logger.class, 
						    logger),
			  0);
	    
	    if(loaded()) {
		try {
		    interp.evalFile(scriptFileName);
		    logger.log(BasicLevel.DEBUG, "Script executed for file " + 
			       scriptFileName);
		} catch (TclException e) {
		    throw new ScriptErrorException(interp.getResult().toString(), e);
		}
	    }
	    interp.dispose();
	} catch (TclException e) {
	    throw new ScriptErrorException(e);
	}
    }

    /**
     * Locates and prepares a script for execution. It receives the fully
     * qualified name of a class and transforms it into the fulll path
     * name of the file.
     */
    public static FieldUpdateScript loadScript(String className, 
					      String fieldName) {
	try {
	    logger.log(BasicLevel.DEBUG, "loading script for class: " + 
		       className + ", field: " + fieldName);

	    Preferences prefs = PreferencesBean.getPreferences();
	    String scriptsFolder = prefs.get(SCRIPTING_FOLDER_OPTION, 
					     File.separator);
	    String scriptName = scriptsFolder + File.separator +
		(className + "." + fieldName)
		.replace('.', File.separatorChar) + ".tcl";

	    logger.log(BasicLevel.DEBUG, "full path to script: " + scriptName);
	    return new FieldUpdateScript(scriptName);
	} catch (Exception e) {
	    logger.log(BasicLevel.WARN, "option " + SCRIPTING_FOLDER_OPTION
		       + " was not read. The following exception was caught: ",
		       e);
	    String scriptName = (className + "." + fieldName)
		.replace('.', File.separatorChar) + ".tcl";

	    logger.log(BasicLevel.DEBUG, "full path to script: " + scriptName);
	    return new FieldUpdateScript(scriptName);
	}
    }
}
