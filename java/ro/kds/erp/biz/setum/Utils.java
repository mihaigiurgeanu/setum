package ro.kds.erp.biz.setum;

import java.util.Map;




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



    /**
     * Add an attribute value to a grouping code if the attribute
     * exists in the given attributes map.
     */
    public static void addAttrToGC(String attr, Map amap, GroupingCode gc) {
	if(amap.containsKey(attr)) {
	    gc.add(amap.get(attr));
	}
    }
    
} // Utils
