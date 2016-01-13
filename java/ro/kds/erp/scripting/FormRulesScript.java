package ro.kds.erp.scripting;

import ro.kds.erp.biz.ResponseBean;
import org.objectweb.util.monolog.api.BasicLevel;
import tcl.lang.Interp;
import tcl.lang.ReflectObject;
import tcl.lang.TclException;
import ro.kds.erp.biz.PreferencesBean;
import org.objectweb.util.monolog.api.Logger;
import org.objectweb.jonas.common.Log;
import java.io.File;
import ro.kds.erp.biz.Preferences;

/**
 * Evaluates an optional script that should compute the values of the
 * calculated fields and check validation rules for data form.
 *
 * The script will be run having the following variables defined:
 * - form is the object containing all form fields
 * - response is the response bean that will be sent back to the client
 *
 * The script should first compute the values of the calculated fields. After
 * that it should evaluate all the validation rules. When a rule is broken
 * the script should add a new record to the response bean. The record
 * will contain an error message and the field name that invalidates the
 * rule. The field name would be used by the UI client to set the focus
 * to that field. The response code of the response bean should be incremented.
 *
 * Here is a sample of a script doing these:
 *
 * <code>
 * $response addRecord
 * $response addField code [expr [$response getCode] + 1]
 * $response addField message "ERROR MESSAGE"
 * $response addField fieldId myField
 * </code>
 *
 *
 * Created: Wed Oct 19 21:14:16 2005
 *
 * @author <a href="mailto:Mihai Giurgeanu@CRIMIRA"></a>
 * @version 1.0
 */
public class FormRulesScript {


    private String scriptFileName;
    static private Logger logger = Log.getLogger("ro.kds.erp.scripting.FormRulesScript");

    /**
     * The name of the system configurations (accessed through 
     * <code>Preferences</code> bean) containd the path to the
     * scripts folder. It's value is scripting.folder
     */
    public static final String SCRIPTING_FOLDER_OPTION = "scripting.folder";

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
     * Creates a new <code>FormRulesScritp</code> instance.
     *
     */
    public FormRulesScript(String scriptFileName) {
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
     * Sets the form bean value. It is the bean that contains the values
     * of all form fields.
     */
    public FormRulesScript setForm(Object form) {
	this.form = form;
	return this;
    }

    /**
     * Set the class used for the form bean that will be passed to the
     * script.
     */
    public FormRulesScript setFormClass(Class newFormClass) {
	this.formClass = newFormClass;
	return this;
    }

    /**
     * Sets the ResponseBean that will be used by the script to pass
     * back any calculated fields or error information.
     *
     */
    public FormRulesScript setResponseBean(ResponseBean response) {
	this.response = response;
	return this;
    }

    /**
     * Runs the script.
     *
     * @throws ScriptErrorException if the script ends in error.
     */
    public void run() throws ScriptErrorException {
	logger.log(BasicLevel.DEBUG, "runing script " + scriptFileName);

	try {
	    Interp interp = new Interp();
	    
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
    public static FormRulesScript loadScript(String className) {
	try {
	    logger.log(BasicLevel.DEBUG, "loading script for class: " + 
		       className);

	    Preferences prefs = PreferencesBean.getPreferences();
	    String scriptsFolder = prefs.get(SCRIPTING_FOLDER_OPTION, 
					     File.separator);
	    String scriptName = scriptsFolder + File.separator +
		className.replace('.', File.separatorChar) + ".tcl";

	    logger.log(BasicLevel.DEBUG, "full path to script: " + scriptName);
	    return new FormRulesScript(scriptName);
	} catch (Exception e) {
	    logger.log(BasicLevel.WARN, "option " + SCRIPTING_FOLDER_OPTION
		       + " was not read. The following exception was caught: ",
		       e);
	    String scriptName = className
		.replace('.', File.separatorChar) + ".tcl";

	    logger.log(BasicLevel.DEBUG, "full path to script: " + scriptName);
	    return new FormRulesScript(scriptName);
	}
    }



}
