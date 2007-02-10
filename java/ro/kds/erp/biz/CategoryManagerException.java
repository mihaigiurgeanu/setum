package ro.kds.erp.biz;


/**
 * Describe class CategoryManagerException here.
 *
 *
 * Created: Mon Jan 22 22:27:48 2007
 *
 * @author <a href="mailto:Mihai Giurgeanu@MIHAIG">U-MIHAIG\Mihai Giurgeanu</a>
 * @version 1.0
 */
public class CategoryManagerException extends Exception {

    /**
     * Creates a new <code>CategoryManagerException</code> instance.
     *
     */
    public CategoryManagerException() {
	super();
    }

    /**
     * Creates a new <code>CategoryManagerException</code> instance.
     *
     */
    public CategoryManagerException(String message) {
	super(message);
    }

    /**
     * Creates a new <code>CategoryManagerException</code> instance.
     *
     */
    public CategoryManagerException(Throwable t) {
	super(t);
    }

    /**
     * Creates a new <code>CategoryManagerException</code> instance.
     *
     */
    public CategoryManagerException(String message, Throwable t) {
	super(message, t);
    }


}
