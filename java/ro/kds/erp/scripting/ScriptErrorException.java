package ro.kds.erp.scripting;

/**
 * Exception generated when a script ends in error.
 *
 *
 * Created: Wed Oct 19 14:30:47 2005
 *
 * @author <a href="mailto:Mihai Giurgeanu@CRIMIRA"></a>
 * @version 1.0
 */
public class ScriptErrorException extends Exception {

    /**
     * Creates a new <code>ScriptErrorException</code> instance.
     *
     */
    public ScriptErrorException() {
	super();
    }

    /**
     * Creates a new <code>ScriptErrorException</code> instance.
     *
     */
    public ScriptErrorException(String message) {
	super(message);
    }

    /**
     * Creates a new <code>ScriptErrorException</code> instance.
     *
     */
    public ScriptErrorException(Throwable t) {
	super(t);
    }

    /**
     * Creates a new <code>ScriptErrorException</code> instance.
     *
     */
    public ScriptErrorException(String message, Throwable t) {
	super(message, t);
    }

}
