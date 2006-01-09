package ro.kds.erp.biz.setum;

import java.util.TreeMap;
import ro.kds.erp.data.CategoryLocalHome;
import javax.rmi.PortableRemoteObject;
import javax.naming.InitialContext;
import ro.kds.erp.data.CategoryLocal;
import java.util.Iterator;
import java.util.Collection;
import ro.kds.erp.data.ProductLocal;
import org.objectweb.util.monolog.api.Logger;
import org.objectweb.util.monolog.api.BasicLevel;
import org.objectweb.jonas.common.Log;
import java.util.Map;

/**
 * Collection of static methods for building value lists used in the
 * XUL forms. A value list is a <code>java.util.Map</code> of pairs
 * (name, id) with the name beeing the key in the map. The name is
 * the part visible in the XUL menu and the id is the key of the object
 * the name represents.
 *
 *
 * Created: Wed Dec 14 00:37:30 2005
 *
 * @author <a href="mailto:mihai@cris.kds.ro">Mihai Giurgeanu</a>
 * @version 1.0
 */
public class ValueLists {

    static private Logger logger = Log.getLogger("ro.kds.erp.biz.setum.ValueLists");

    /**
     * Gets all the products in the given category and makes a value
     * list with pairs (name, id). The value list is ordered on name.
     * It accesses the <code>CategoryEJB</code> through its local interfaces.
     *
     * It searches for <code>java:comp/env/ejb/CategoryHome</code>.
     */
    public static Map makeVLForCategoryId(Integer categoryId) {
	TreeMap vl = new TreeMap();

	try {
	    InitialContext ic = new InitialContext();
	    CategoryLocalHome ch = (CategoryLocalHome)PortableRemoteObject.
		narrow(ic.lookup("java:comp/env/ejb/CategoryHome"),
		       CategoryLocalHome.class);
	    CategoryLocal c = ch.findByPrimaryKey(categoryId);
	    Collection products = c.getProducts();
	    for(Iterator i = products.iterator(); i.hasNext();) {
		ProductLocal p = (ProductLocal)i.next();
		vl.put(p.getName(), p.getId());
	    }
	} catch(Exception e) {
	    logger.log(BasicLevel.ERROR,
		       "Can not make value list for products category " +
		       categoryId, e);
	}

	return vl;
    }


    /**
     * Helper method for creating a value list from the products of a category
     * that is to be read from the jndi environment. The entries in value
     * list will be ordered by name.
     *
     * @param categoryName is the name to be looked up in the jndi directory
     * relative to <code>java:comp/env</code>
     */
    public static Map makeVLForCategoryRef(String categoryName) {
	Map vl;

	try {
	    InitialContext ic = new InitialContext();
	    Integer categoryId = (Integer)ic.lookup
		("java:comp/env/" + categoryName);
	    vl = makeVLForCategoryId(categoryId);
	} catch (Exception e) {
	    logger.log(BasicLevel.ERROR,
		       "Can not get the value list for category referenced " +
		       "by java:comp/env/" + categoryName + " in the jndi.",
		       e);
	    vl = new TreeMap();
	}
	
	return vl;
    }

    /**
     * Builds a value list with the products of subcategories of the given
     * category.
     *
     * @param categoryId is the primary key of a category which should have
     * subcategories. The method will get all its subcategories' products and
     * build a value lis.
     */
    public static Map makeVLForSubcategories(Integer categoryId) {
	TreeMap vl = new TreeMap();

	try {
	    InitialContext ic = new InitialContext();
	    CategoryLocalHome ch = (CategoryLocalHome)PortableRemoteObject.
		narrow(ic.lookup("java:comp/env/ejb/CategoryHome"),
		       CategoryLocalHome.class);
	    CategoryLocal c = ch.findByPrimaryKey(categoryId);
	    Collection categs = c.getSubCategories();
	    for(Iterator j = categs.iterator(); j.hasNext();) {
		CategoryLocal subCat = (CategoryLocal)j.next();
		Collection products = subCat.getProducts();
		for(Iterator i = products.iterator(); i.hasNext();) {
		    ProductLocal p = (ProductLocal)i.next();
		    vl.put(p.getName(), p.getId());
		}
	    }
	} catch(Exception e) {
	    logger.log(BasicLevel.ERROR,
		       "Can not make value list for subcategories in category "
		       + categoryId, e);
	}

	return vl;
    }
} // ValueLists
