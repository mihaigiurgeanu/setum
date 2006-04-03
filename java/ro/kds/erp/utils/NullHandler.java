package ro.kds.erp.utils;

/**
 * Utility class that helps dealing with null values. It checks if
 * the parameter is null and then returns a specific object.
 *
 *
 * Created: Mon Apr 03 17:57:51 2006
 *
 * @author <a href="mailto:Mihai Giurgeanu@CRIMIRA"></a>
 * @version 1.0
 */
public class NullHandler {

    private Object defaultValue;

    /**
     * Creates a new <code>NullHandler</code> instance.
     *
     * @param returnIfNull is a object to be returned by the 
     * <code>val</code> method if its param is <code>null<null>.
     */
    public NullHandler(Object returnIfNull) {
	this.defaultValue = returnIfNull;
    }

    /**
     * Checks the given param. If it is null, it returns a default value
     * else returns the given object.
     */
    public Object val(Object value) {
	return value==null?defaultValue:value;
    }

}
