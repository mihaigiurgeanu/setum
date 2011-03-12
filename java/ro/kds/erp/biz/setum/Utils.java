package ro.kds.erp.biz.setum;

import java.util.Map;
import ro.kds.erp.data.ProductLocal;
import org.objectweb.util.monolog.Monolog;
import ro.kds.erp.biz.ServiceFactoryLocal;
import ro.kds.erp.biz.CommonServicesLocal;
import ro.kds.erp.biz.CommonServicesLocalHome;
import ro.kds.erp.biz.ServiceNotAvailable;
import org.objectweb.util.monolog.api.BasicLevel;
import ro.kds.erp.biz.ProductNotAvailable;
import org.objectweb.util.monolog.api.Logger;




/**
 * Static methods for supporting the business logic.
 *
 *
 * Created: Wed Dec 14 00:21:47 2005
 *
 * @author <a href="mailto:mihai@cris.kds.ro">Mihai Giurgeanu</a>
 * @version 1.0
 */
public class Utils {

    static private ProductLocal agrementTehnic_cache = null;
    static protected Logger logger = Monolog.getMonologFactory().getLogger("ro.kds.erp.biz.ServiceFactoryBean");

    /**
     * Add an attribute value to a grouping code if the attribute
     * exists in the given attributes map.
     */
    public static void addAttrToGC(String attr, Map amap, GroupingCode gc) {
	if(amap.containsKey(attr)) {
	    gc.add(amap.get(attr));
	}
    }

    /**
     * Obtine valoarea agrementului tehnic din setari.
     */
    public static String getAgrementTehnic(ServiceFactoryLocal factory) {
	try {
	    if (agrementTehnic_cache == null) {
		CommonServicesLocal srv = (CommonServicesLocal)
		    factory.local("ejb/CommonServices", CommonServicesLocalHome.class);
		agrementTehnic_cache = srv.findProductByCode(15000, "Agrement tehnic");
	    }
	    return agrementTehnic_cache.getName();
	}
	catch (ServiceNotAvailable e) {
	    logger.log(BasicLevel.ERROR, "Eroare configurare ServiceFactory: serviciul ejb/CommonServices nu este disponibil. Nu se poate citi valoarea agrementului tehnic.", e);
	}
	catch (ProductNotAvailable e) {
	    logger.log(BasicLevel.WARN, "Agrementul tehnic nu este setat", e);
	}
	return "N/A";
    }

    /**
     * Helper method that matches a value against a filter. The filter
     * will match if the value contains the filter. The comparison is
     * made in case insensitive mode.
     */
    public static boolean stringFilter(String value, String filter) {
	if(filter == null || filter.length() == 0)
	    return true;

	if(value.toUpperCase().indexOf(filter.toUpperCase()) > -1) {
	    return true;
	}

	return false;
    }
} // Utils
