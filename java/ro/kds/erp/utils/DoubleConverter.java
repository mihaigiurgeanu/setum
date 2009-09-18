package ro.kds.erp.utils;

import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.ConversionException;
import java.text.NumberFormat;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Locale;

/**
 * Describe class DoubleConverter here.
 *
 *
 * Created: Thu Nov 30 18:45:43 2006
 *
 * @author <a href="mailto:Mihai Giurgeanu@MIHAIG">U-MIHAIG\Mihai Giurgeanu</a>
 * @version 1.0
 */
public class DoubleConverter implements Converter {

    /**
     * Creates a new <code>DoubleConverter</code> instance.
     *
     */
    public DoubleConverter() {

    }

    // Implementation of org.apache.commons.beanutils.Converter

    /**
     * Describe <code>convert</code> method here.
     *
     * @param type is the type to convert to.
     * @param object is the value to be converted.
     *
     * @return an <code>Object</code> value
     *
     * @throws ConversionException if a conversion error occurs.
     *
     */
    public final Object convert(final Class type, final Object object) 
	throws ConversionException {

	try {
	    NumberFormat nf = NumberFormat.getInstance(Locale.US);
	    Number val = nf.parse((String)object);
	    if( type.isAssignableFrom(BigDecimal.class) ) {
		return new BigDecimal(val.doubleValue());
	    } else if (type.isAssignableFrom(Double.class)) {
		return new Double(val.doubleValue());
	    } else if (type.isAssignableFrom(Number.class)) {
		return val;
	    } else {
		throw  new ConversionException("Can not convert to " + type.getName());
	    }
	} catch (ParseException e) {
	    throw new ConversionException(e);
	}
    }
  
}
