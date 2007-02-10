package ro.kds.erp.biz;

/**
 * Exception <code>ServiceNotAvailable</code>.
 *
 * Created: Mon Feb  5 19:26:14 2007
 *
 * @author <a href="mailto:Mihai Giurgeanu@MIHAIG">U-MIHAIG\Mihai Giurgeanu</a>
 * @version 1.0
 */
public class ServiceNotAvailable extends Exception {

    /**
     * Constructs a new <code>ServiceNotAvailable</code> with
     * <code>null</code> as its detail message.
     */
    public ServiceNotAvailable() {
    }

    /**
     * Constructs a new <code>ServiceNotAvailable</code> with
     * the specified detail message.
     *
     * @param message the detail message string.
     */
    public ServiceNotAvailable(final String message) {
	super(message);
    }

    /**
     * Constructs a new <code>ServiceNotAvailable</code> with
     * the specified cause and a detail message of
     * <code>(cause == null ? null : cause.toString())</code>
     * (which typically contains the class and detail message of cause).
     *
     * @param cause the causing throwable. A null value is permitted
     *     and indicates that the cause is nonexistent or unknown.
     */
    public ServiceNotAvailable(final Throwable cause) {
	super(cause == null ? (String) null : cause.toString());
	initCause(cause);
    }

    /**
     * Constructs a new <code>ServiceNotAvailable</code> with
     * the specified cause and message.
     *
     * @param message the detail message string.
     * @param cause the causing throwable. A null value is permitted
     *     and indicates that the cause is nonexistent or unknown.
     */
    public ServiceNotAvailable(final String message,final Throwable cause) {
	super(message);
	initCause(cause);
    }
}

