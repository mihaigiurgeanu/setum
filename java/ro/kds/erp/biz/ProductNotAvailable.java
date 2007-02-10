package ro.kds.erp.biz;

/**
 * Exception <code>ProductNotAvailable</code>.
 *
 * Created: Tue Feb  6 22:10:12 2007
 *
 * @author <a href="mailto:Mihai Giurgeanu@MIHAIG">U-MIHAIG\Mihai Giurgeanu</a>
 * @version 1.0
 */
public class ProductNotAvailable extends Exception {

    /**
     * Constructs a new <code>ProductNotAvailable</code> with
     * <code>null</code> as its detail message.
     */
    public ProductNotAvailable() {
    }

    /**
     * Constructs a new <code>ProductNotAvailable</code> with
     * the specified detail message.
     *
     * @param message the detail message string.
     */
    public ProductNotAvailable(final String message) {
	super(message);
    }

    /**
     * Constructs a new <code>ProductNotAvailable</code> with
     * the specified cause and a detail message of
     * <code>(cause == null ? null : cause.toString())</code>
     * (which typically contains the class and detail message of cause).
     *
     * @param cause the causing throwable. A null value is permitted
     *     and indicates that the cause is nonexistent or unknown.
     */
    public ProductNotAvailable(final Throwable cause) {
	super(cause == null ? (String) null : cause.toString());
	initCause(cause);
    }

    /**
     * Constructs a new <code>ProductNotAvailable</code> with
     * the specified cause and message.
     *
     * @param message the detail message string.
     * @param cause the causing throwable. A null value is permitted
     *     and indicates that the cause is nonexistent or unknown.
     */
    public ProductNotAvailable(final String message,final Throwable cause) {
	super(message);
	initCause(cause);
    }
}

