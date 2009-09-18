package ro.kds.erp.web;

/**
 * Describe class SessionBeanMissingException here.
 *
 *
 * Created: Mon Oct 27 04:10:02 2008
 *
 * @author <a href="mailto:mihai@mihai-giurgeanus-computer.local">Mihai Giurgeanu</a>
 * @version 1.0
 */
public class SessionBeanMissingException extends Exception {

    /**
     * Creates a new <code>SessionBeanMissingException</code> instance.
     *
     */
    public SessionBeanMissingException() {
	super();
    }

    /**
     * Creates a new <code>SessionBeanMissingException</code> instance.
     *
     */
    public SessionBeanMissingException(String message) {
	super(message);
    }

    /**
     * Creates a new <code>SessionBeanMissingException</code> instance.
     *
     */
    public SessionBeanMissingException(Throwable t) {
	super(t);
    }

    /**
     * Creates a new <code>SessionBeanMissingException</code> instance.
     *
     */
    public SessionBeanMissingException(String message, Throwable t) {
	super(message, t);
    }
}
