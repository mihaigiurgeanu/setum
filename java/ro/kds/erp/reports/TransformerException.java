package ro.kds.erp.reports;


/**
 * 
 *
 *
 * Created: Wed Jan 24 20:03:43 2007
 *
 * @author <a href="mailto:Mihai Giurgeanu@MIHAIG">U-MIHAIG\Mihai Giurgeanu</a>
 * @version 1.0
 */
public class TransformerException extends Exception {

    /**
     * Creates a new <code>TransformerException</code> instance.
     *
     */
    public TransformerException() {
	super();
    }

    /**
     * Creates a new <code>TransformerException</code> instance.
     *
     */
    public TransformerException(String message) {
	super(message);
    }

    /**
     * Creates a new <code>TransformerException</code> instance.
     *
     */
    public TransformerException(Throwable t) {
	super(t);
    }

    /**
     * Creates a new <code>TransformerException</code> instance.
     *
     */
    public TransformerException(String message, Throwable t) {
	super(message, t);
    }

}
