package ro.kds.erp.data;


/**
 * Describe class DataLayerException here.
 *
 *
 * Created: Sun Oct 23 21:21:47 2005
 *
 * @author <a href="mailto:Mihai Giurgeanu@CRIMIRA"></a>
 * @version 1.0
 */
public class DataLayerException extends Exception {

    /**
     * Creates a new <code>DataLayerException</code> instance.
     *
     */
    public DataLayerException() {
	super();
    }

    /**
     * Creates a new <code>DataLayerException</code> instance.
     *
     */
    public DataLayerException(String message) {
	super(message);
    }

    /**
     * Creates a new <code>DataLayerException</code> instance.
     *
     */
    public DataLayerException(Throwable t) {
	super(t);
    }

    /**
     * Creates a new <code>DataLayerException</code> instance.
     *
     */
    public DataLayerException(String message, Throwable t) {
	super(message, t);
    }

}
