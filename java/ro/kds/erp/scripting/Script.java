package ro.kds.erp.scripting;

/**
 * Describes the interface to an object that will be used for
 * loading and running a script. The methods declared in this interface
 * allows a script client to set different variables that will be
 * available into the script, to retrieve variables from the script
 * and to retrieve the returned result of the script.
 *
 *
 * Created: Thu Oct 20 13:35:28 2005
 *
 * @author <a href="mailto:Mihai Giurgeanu@CRIMIRA"></a>
 * @version 1.0
 */
public interface Script {


    /**
     * Verify if the script can be run. This method is useful for 
     * optional scripts. The invoking class might test if there is
     * a script to run by testing the result of this method.
     *
     * @returns <code>true</code> if the script exits and <code>false</code>
     * if there is no script to run.
     */
    public boolean loaded();


    /**
     * Set a variable that will be available to the script as
     * the string passed in <code>varName</code>.
     *
     * @param varName contains the name of the variable (how the script
     * will see it);
     * @param varValue is the value of the variable
     * @param varClass is the class of the varValue (the class that you intend
     * to expose)
     */
    public void setVar(String varName, Object varValue, Class varClass) 
	throws ScriptErrorException;
    /**
     * Set a variable that will be available to the script as
     * the string passed in <code>varName</code>.
     *
     * @param varName contains the name of the variable (how the script
     * will see it);
     * @param varValue is the value of the variable
     */
    public void setVar(String varName, Object varValue)
	throws ScriptErrorException;

    /**
     * Get the value of a script variable. The variable might of been
     * set earlier with setVar, or it might be a variable created into the
     * script.
     * 
     * @param varName is the name of the scripting variable.
     * @param varClass is the class of the returned value
     */
    public Object getVar(String varName, Class varClass)
	throws ScriptErrorException;
    /**
     * Tries to determine the most appropriate type
     * for the returned value of a scripting variable.
     * 
     * @param varName is the name of the scripting variable.
     * @returns the class of the scripting variable.
     */
    public Class getVarClass(String varName)
	throws ScriptErrorException;
    /**
     * Gets the result of script evaluation.
     * 
     * @param varClass si the class of the object to return.
     * @returns an object with the class given by varClass.
     */
    public Object getResult(Class varClass)
	throws ScriptErrorException;
    
    /**
     * Try to establish the most apropriate class for the result.
     *
     * @returns the class of the result object.
     */
    public Class getResultClass()
	throws ScriptErrorException;

    /**
     * Runs the script.
     *
     * @throws ScriptErrorException if the script ends in error.
     */
    public void run() throws ScriptErrorException;
}
