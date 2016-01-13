package ro.kds.erp.utils;

import org.apache.commons.beanutils.Converter;
import java.text.DateFormat;
import org.apache.commons.beanutils.ConversionException;
import java.text.SimpleDateFormat;

/**
 * Implements a DateConverter to be used with beanutils ConvertUtils class.
 * Parses a string into a java.util.Date object.
 *
 * The class provided by beanutils (DateLocaleConverter) does not work
 * for empty constructor, so, instead of providing patterns to the
 * beanutils implementation, we wrote this simple date converter.
 *
 *
 * Created: Mon Jan 16 16:51:05 2006
 *
 * @author <a href="mailto:Mihai Giurgeanu@CRIMIRA"></a>
 * @version 1.0
 */
public class DateConverter implements Converter {

    /**
     * Creates a new <code>DateConverter</code> instance. This
     * converter will use the default locale to parse date string.
     *
     */
    public DateConverter() {

    }

    // Implementation of org.apache.commons.beanutils.Converter

    /**
     * Describe <code>convert</code> method here.
     *
     * @param type is the class to convert to.
     * @param object an <code>Object</code> value to convert.
     * @return an <code>Object</code> value.
     *
     * @throws ConversionException if the type is not assignable from
     * java.util.Date, or if a conversion error occurs.
     */
    public Object convert(final Class type, final Object object) 
	throws ConversionException {
	if(! type.isAssignableFrom(java.util.Date.class)) {
	    throw new ConversionException("Can not convert to " + type.getName());
	}
	try {
	    //DateFormat format = DateFormat.getDateInstance();
	    DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	    return format.parse((String)object);
	} catch (Exception e) {
	    throw new ConversionException(e);
	}
    }
  
}
