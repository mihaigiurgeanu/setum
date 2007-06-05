package ro.kds.erp.scripting;

import tcl.lang.Interp;
import tcl.lang.TclException;
import org.objectweb.util.monolog.api.BasicLevel;
import tcl.lang.TclObject;
import tcl.lang.TclInteger;
import tcl.lang.TclBoolean;
import tcl.lang.TclDouble;
import java.math.BigDecimal;
import tcl.lang.TclString;
import tcl.lang.ReflectObject;
import tcl.lang.InternalRep;
import ro.kds.erp.biz.PreferencesBean;
import org.objectweb.util.monolog.api.Logger;
import org.objectweb.jonas.common.Log;
import java.io.File;
import ro.kds.erp.biz.Preferences;
import tcl.lang.TclObject;
import tcl.lang.TclException;
import tcl.lang.Interp;
import tcl.lang.Command;
import java.util.Date;


/**
 * Use this class to execute a tcl script from a file. For loading a script
 * use the <code>loadScript</code> static method.
 *
 *
 * Created: Thu Oct 20 13:56:38 2005
 *
 * @author <a href="mailto:Mihai Giurgeanu@CRIMIRA"></a>
 * @version 1.0
 */
public class TclFileScript implements Script {

    private String scriptFileName;
    static private Logger logger = Log.getLogger("ro.kds.erp.scripting.TclFileScript");
    private Interp interp = null;

    /**
     * The name of the system configurations (accessed through 
     * <code>Preferences</code> bean) containd the path to the
     * scripts folder. It's value is scripting.folder
     */
    public static final String SCRIPTING_FOLDER_OPTION = "scripting.folder";

    /**
     * Creates a new <code>TclFileScript</code> instance.
     *
     */
    public TclFileScript(String fileName) {
	this.scriptFileName = fileName;
	interp = new Interp();
    }

    // Implementation of ro.kds.erp.scripting.Script

    /**
     * Verify if the script can be run. To do this verifies if the file
     * <code>scriptFileName</code> exists and can be read.
     */
    public boolean loaded() {
	if(interp == null) {
	    logger.log(BasicLevel.DEBUG, "The interpreter is not instantiated" 
		       + " for the file " + scriptFileName);
	    return false;
	}
	File script = new File(scriptFileName);
	if(script.canRead())
	    return true;
     
	logger.log(BasicLevel.DEBUG, "Script dose not exists for file " +
		   scriptFileName);
	return false;
    }

    /**
     * Set a scripting variable.
     *
     */
    public void setVar(String varName, Object varValue) 
	throws ScriptErrorException {
	if(varValue instanceof Integer) {
	    setVar(varName, varValue, Integer.class);
	} else if (varValue instanceof Boolean) {
	    setVar(varName, varValue, Boolean.class);
	} else if (varValue instanceof Number) {
	    setVar(varName, varValue, Number.class);
	} else if (varValue instanceof String) {
	    setVar(varName, varValue, String.class);
	} else if (varValue instanceof Date) {
	    setVar(varName, varValue, Date.class);
	} else {
	    setVar(varName, varValue, varValue.getClass());
	}
    }

    /**
     * Set a scripting variable.
     *
     * @exception ScriptErrorException if an error occurs
     */
    public void setVar(String varName, Object varValue, Class varClass) 
	throws ScriptErrorException {
	TclObject tobj;

	if (varValue == null) {
	    logger.log(BasicLevel.WARN, "The variable " + varName + 
		       " is null. It will not be passed to script!");
	} else {
	    try {
		if(varClass.equals(Integer.class)) {
		    tobj = TclInteger.newInstance(((Integer)varValue)
						  .intValue());
		} else if (varClass.equals(Boolean.class)) {
		    tobj = TclBoolean.newInstance(((Boolean)varValue)
						  .booleanValue());
		} else if (varClass.equals(Double.class)) {
		    tobj = TclDouble.newInstance(((Number)varValue)
						 .doubleValue());
		} else if (varClass.equals(BigDecimal.class)) {
		    tobj = TclDouble.newInstance(((Number)varValue)
						 .doubleValue());
		} else if (varClass.equals(String.class)) {
		    tobj = TclString.newInstance((String)varValue);
		} else if (varClass.equals(Date.class)) {
		    // pass the number of seconds to tcl script instead
		    // of number of miliseconds; the tcl script can use
		    // the command clock to do operations on date.
		    tobj = TclInteger.newInstance((int)(((Date)varValue).getTime()/1000));
		} else {
		    // a generic object
		    tobj = ReflectObject.newInstance(interp, varClass, 
						     varValue);
		}
		interp.setVar(varName, tobj, 0);
	    } catch (Exception e) {
		throw new ScriptErrorException("Can not set the variable " + 
					       varName, e);
	    }
	}
    }

    /**
     * Get the value of a scripting variable.
     *
     */
    public Object getVar(String varName, Class varClass) 
	throws ScriptErrorException {
	
	try {
	    TclObject tobj = interp.getVar(varName, 0);
	    if(varClass.equals(Integer.class)) {
		return new Integer(TclInteger.get(interp, tobj));
	    } else if (varClass.equals(Boolean.class)) {
		return new Boolean(TclBoolean.get(interp, tobj));
	    } else if (varClass.equals(Double.class)) {
		return new Double(TclDouble.get(interp, tobj));
	    } else if (varClass.equals(BigDecimal.class)) {
		return new BigDecimal(TclDouble.get(interp, tobj));
	    } else if (varClass.equals(String.class)) {
		return tobj.toString();
	    } else if (varClass.equals(Date.class)) {
		return new Date((long)TclInteger.get(interp, tobj) * 1000);
	    } else {
		// a generic object
		Object value = ReflectObject.get(interp, tobj);
		if(varClass.isInstance(value)) return value;

		throw new ScriptErrorException("The value of the variable " +
					       varName + 
					       " is of class " +
					       value.getClass().getName() +
					       " which is not of instance of "+
					       varClass.getName()
					       );
	    }
	} catch (TclException e) {
	    throw new ScriptErrorException("Can not get the variable " + 
					   varName, e);
	}
	
    }

    /**
     * Retrieves the class of an object variable.
     *
     */
    public Class getVarClass(String varName) throws ScriptErrorException {
	InternalRep rep;
	TclObject tobj;
	try {
	    tobj = interp.getVar(varName, 0);
	    rep = tobj.getInternalRep();
	} catch (TclException e) {
	    throw new ScriptErrorException("Can not get the variable " 
					   + varName, e);
	}

	if(rep instanceof TclInteger)
	    return Integer.class;
	if(rep instanceof TclDouble)
	    return Double.class;
	if(rep instanceof TclBoolean)
	    return Boolean.class;
	try {
	    if(rep instanceof ReflectObject)
		return ReflectObject.getClass(interp, tobj);
	} catch (TclException e) {
	    throw new ScriptErrorException("Can not get the class of var " +
					   varName, e);
	}
	
	throw new ScriptErrorException("Can not process this kind of internal represantion: " + rep.getClass().getName());
    }

    /**
     * Get the result of script evaluation.
     *
     */
    public Object getResult(Class varClass) throws ScriptErrorException {
	try {
	    TclObject tobj = interp.getResult();
	    if(varClass.equals(Integer.class)) {
		return new Integer(TclInteger.get(interp, tobj));
	    } else if (varClass.equals(Boolean.class)) {
		return new Boolean(TclBoolean.get(interp, tobj));
	    } else if (varClass.equals(Double.class)) {
		return new Double(TclDouble.get(interp, tobj));
	    } else if (varClass.equals(BigDecimal.class)) {
		return new BigDecimal(TclDouble.get(interp, tobj));
	    } else if (varClass.equals(String.class)) {
		return tobj.toString();
	    } else {
		// a generic object
		Object value = ReflectObject.get(interp, tobj);
		if(varClass.isInstance(value)) return value;

		throw new ScriptErrorException("The value of the result " +
					       " is of class " +
					       value.getClass().getName() +
					       " which is not an instance of "+
					       varClass.getName()
					       );
	    }
	} catch (TclException e) {
	    throw new ScriptErrorException("Can not get the result", e);
	}
	
    }

    /**
     * Returns the class of the result of the script evaluation.
     *
     * @return a <code>Class</code> value
     * @exception ScriptErrorException if an error occurs
     */
    public Class getResultClass() throws ScriptErrorException {
	TclObject tobj = interp.getResult();
	InternalRep rep = tobj.getInternalRep();

	if(rep instanceof TclInteger)
	    return Integer.class;
	if(rep instanceof TclDouble)
	    return Double.class;
	if(rep instanceof TclBoolean)
	    return Boolean.class;
	try {
	    if(rep instanceof ReflectObject)
		return ReflectObject.getClass(interp, tobj);
	} catch (TclException e) {
	    throw new ScriptErrorException("Can not get the class of result", e);
	}
	
	throw new ScriptErrorException("Can not process this kind of internal represantion: " + rep.getClass().getName());
    }


    /**
     * Locates and prepares a script for execution. It receives the fully
     * qualified name of a class and transforms it into the fulll path
     * name of the file.
     *
     * The transformation is done by turning all dots in the class name into
     * system depend folders separator and appending the name to a folder
     * configured in the system preferences with the name "scripting.folder".
     *
     * @param className a string looking like a full qualified className.
     *
     */
    public static Script loadScript(String className) {
	try {
	    logger.log(BasicLevel.DEBUG, "loading script for class: " + 
		       className);

	    Preferences prefs = PreferencesBean.getPreferences();
	    String scriptsFolder = prefs.get(SCRIPTING_FOLDER_OPTION, 
					     File.separator);
	    logger.log(BasicLevel.DEBUG, "read " + SCRIPTING_FOLDER_OPTION +
		       " option from preferences: " + scriptsFolder);

	    String scriptName = scriptsFolder + File.separator +
		className.replace('.', File.separatorChar) + ".tcl";
	    logger.log(BasicLevel.DEBUG, "full path to script: " + scriptName);

	    return new TclFileScript(scriptName);
	} catch (Exception e) {
	    logger.log(BasicLevel.WARN, "option " + SCRIPTING_FOLDER_OPTION
		       + " was not read. The following exception was caught: ",
		       e);
	    String scriptName = className
		.replace('.', File.separatorChar) + ".tcl";

	    logger.log(BasicLevel.DEBUG, "full path to script: " + scriptName);
	    return new TclFileScript(scriptName);
	}
    }

    /**
     * Runs the script.
     *
     * @throws ScriptErrorException if the script ends in error.
     */
    public void run() throws ScriptErrorException {
	logger.log(BasicLevel.DEBUG, "runing script " + scriptFileName);

	if(loaded()) {
	    try {
		interp.evalFile(scriptFileName);
		logger.log(BasicLevel.DEBUG, "Script executed for file " + 
			   scriptFileName);
	    } catch (TclException e) {
		throw new ScriptErrorException(interp.getResult().toString(), 
					       e);
	    }
	}
    }

}
