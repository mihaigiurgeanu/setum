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
     * The id of the category containing value lists.
     */
    static public final int VL_CATEGORY_ID = 9973;

    /**
     * Create a standard value list for the given products category. It makes
     * a value list with pairs (name, code) -- the code is the productCode.
     *
     * Uses the ejb ref to <code>java:comp/env/ejb/CategoryHome</code>.
     *
     * @param categoryId is the id of the category that contains the products
     * used to build the value list.
     *
     * @returns a <code>Map</code> with (name, code> pairs, where name is
     * the product name and code is the product code for each product in
     * the given category.
     */
    public static Map makeStdValueList(int categoryId) {
	TreeMap vl = new TreeMap();

	try {
	    InitialContext ic = new InitialContext();
	    CategoryLocalHome ch = (CategoryLocalHome)PortableRemoteObject.
		narrow(ic.lookup("java:comp/env/ejb/CategoryHome"),
		       CategoryLocalHome.class);
	    CategoryLocal c = ch.findByPrimaryKey(new Integer(categoryId));
	    Collection products = c.getProducts();
	    for(Iterator i = products.iterator(); i.hasNext();) {
		ProductLocal p = (ProductLocal)i.next();
		vl.put(p.getName(), p.getCode());
	    }
	} catch(Exception e) {
	    logger.log(BasicLevel.ERROR,
		       "Can not make value list for products category " +
		       categoryId, e);
	}

	return vl;
    }

    /**
     * Get all the products in the given category and makes a value
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

    /**
     * Builds a value list containing all the countries and country codes.
     */
    public static Map makeCountriesVL() {

	return makeStdValueList(12001);

//	TreeMap vl = new TreeMap();

//  	vl.put("Andorra", "ad");
//  	vl.put("United Arab Emirates", "ae");
//  	vl.put("Afghanistan", "af");
//  	vl.put("Antigua and Barbuda", "ag");
//  	vl.put("Anguilla", "ai");
//  	vl.put("Albania", "al");
//  	vl.put("Armenia", "am");
//  	vl.put("Netherlands Antilles", "an");
//  	vl.put("Angola", "ao");
//  	vl.put("Antarctica", "aq");
//  	vl.put("Argentina", "ar");
// 	//vl.put("Old style Arpanet", "arpa");
//  	vl.put("American Samoa", "as");
//  	vl.put("Austria", "at");
//  	vl.put("Australia", "au");
//  	vl.put("Aruba", "aw");
//  	vl.put("Azerbaidjan", "az");
//  	vl.put("Bosnia-Herzegovina", "ba");
//  	vl.put("Barbados", "bb");
//  	vl.put("Bangladesh", "bd");
//  	vl.put("Belgium", "be");
//  	vl.put("Burkina Faso", "bf");
//  	vl.put("Bulgaria", "bg");
//  	vl.put("Bahrain", "bh");
//  	vl.put("Burundi", "bi");
//  	vl.put("Benin", "bj");
//  	vl.put("Bermuda", "bm");
//  	vl.put("Brunei Darussalam", "bn");
//  	vl.put("Bolivia", "bo");
//  	vl.put("Brazil", "br");
//  	vl.put("Bahamas", "bs");
//  	vl.put("Bhutan", "bt");
//  	vl.put("Bouvet Island", "bv");
//  	vl.put("Botswana", "bw");
//  	vl.put("Belarus", "by");
//  	vl.put("Belize", "bz");
//  	vl.put("Canada", "ca");
//  	vl.put("Cocos (Keeling) Islands", "cc");
//  	vl.put("Central African Republic", "cf");
//  	vl.put("Congo", "cd");
//  	vl.put("Congo", "cg");
//  	vl.put("Switzerland", "ch");
//  	vl.put("Ivory Coast (Cote D'Ivoire)", "ci");
//  	vl.put("Cook Islands", "ck");
//  	vl.put("Chile", "cl");
//  	vl.put("Cameroon", "cm");
//  	vl.put("China", "cn");
//  	vl.put("Colombia", "co");
//  	vl.put("Commercial", "com");
//  	vl.put("Costa Rica", "cr");
//  	vl.put("Former Czechoslovakia", "cs");
//  	vl.put("Cuba", "cu");
//  	vl.put("Cape Verde", "cv");
//  	vl.put("Christmas Island", "cx");
//  	vl.put("Cyprus", "cy");
//  	vl.put("Czech Republic", "cz");
//  	vl.put("Germany", "de");
//  	vl.put("Djibouti", "dj");
//  	vl.put("Denmark", "dk");
//  	vl.put("Dominica", "dm");
//  	vl.put("Dominican Republic", "do");
//  	vl.put("Algeria", "dz");
//  	vl.put("Ecuador", "ec");
//  	vl.put("Educational", "edu");
//  	vl.put("Estonia", "ee");
//  	vl.put("Egypt", "eg");
//  	vl.put("Western Sahara", "eh");
//  	vl.put("Eritrea", "er");
//  	vl.put("Spain", "es");
//  	vl.put("Ethiopia", "et");
//  	vl.put("Finland", "fi");
//  	vl.put("Fiji", "fj");
//  	vl.put("Falkland Islands", "fk");
//  	vl.put("Micronesia", "fm");
//  	vl.put("Faroe Islands", "fo");
//  	vl.put("France", "fr");
//  	vl.put("France (European Territory)", "fx");
//  	vl.put("Gabon", "ga");
//  	vl.put("Great Britain", "gb");
//  	vl.put("Grenada", "gd");
//  	vl.put("Georgia", "ge");
//  	vl.put("French Guyana", "gf");
//  	vl.put("Ghana", "gh");
//  	vl.put("Gibraltar", "gi");
//  	vl.put("Greenland", "gl");
//  	vl.put("Gambia", "gm");
//  	vl.put("Guinea", "gn");
//  	vl.put("USA Government", "gov");
//  	vl.put("Guadeloupe (French)", "gp");
//  	vl.put("Equatorial Guinea", "gq");
//  	vl.put("Greece", "gr");
//  	vl.put("S. Georgia &amp; S. Sandwich Isls.", "gs");
//  	vl.put("Guatemala", "gt");
//  	vl.put("Guam (USA)", "gu");
//  	vl.put("Guinea Bissau", "gw");
//  	vl.put("Guyana", "gy");
//  	vl.put("Hong Kong", "hk");
//  	vl.put("Heard and McDonald Islands", "hm");
//  	vl.put("Honduras", "hn");
//  	vl.put("Croatia", "hr");
//  	vl.put("Haiti", "ht");
//  	vl.put("Hungary", "hu");
//  	vl.put("Indonesia", "id");
//  	vl.put("Ireland", "ie");
//  	vl.put("Israel", "il");
//  	vl.put("India", "in");
//  	vl.put("International", "int");
//  	vl.put("British Indian Ocean Territory", "io");
//  	vl.put("Iraq", "iq");
//  	vl.put("Iran", "ir");
//  	vl.put("Iceland", "is");
//  	vl.put("Italy", "it");
//  	vl.put("Jamaica", "jm");
//  	vl.put("Jordan", "jo");
//  	vl.put("Japan", "jp");
//  	vl.put("Kenya", "ke");
//  	vl.put("Kyrgyz Republic (Kyrgyzstan)", "kg");
//  	vl.put("Cambodia, Kingdom of", "kh");
//  	vl.put("Kiribati", "ki");
//  	vl.put("Comoros", "km");
//  	vl.put("Saint Kitts &amp; Nevis Anguilla", "kn");
//  	vl.put("North Korea", "kp");
//  	vl.put("South Korea", "kr");
//  	vl.put("Kuwait", "kw");
//  	vl.put("Cayman Islands", "ky");
//  	vl.put("Kazakhstan", "kz");
//  	vl.put("Laos", "la");
//  	vl.put("Lebanon", "lb");
//  	vl.put("Saint Lucia", "lc");
//  	vl.put("Liechtenstein", "li");
//  	vl.put("Sri Lanka", "lk");
//  	vl.put("Liberia", "lr");
//  	vl.put("Lesotho", "ls");
//  	vl.put("Lithuania", "lt");
//  	vl.put("Luxembourg", "lu");
//  	vl.put("Latvia", "lv");
//  	vl.put("Libya", "ly");
//  	vl.put("Morocco", "ma");
//  	vl.put("Monaco", "mc");
//  	vl.put("Moldavia", "md");
//  	vl.put("Madagascar", "mg");
//  	vl.put("Marshall Islands", "mh");
//  	vl.put("USA Military", "mil");
//  	vl.put("Macedonia", "mk");
//  	vl.put("Mali", "ml");
//  	vl.put("Myanmar", "mm");
//  	vl.put("Mongolia", "mn");
//  	vl.put("Macau", "mo");
//  	vl.put("Northern Mariana Islands", "mp");
//  	vl.put("Martinique (French)", "mq");
//  	vl.put("Mauritania", "mr");
//  	vl.put("Montserrat", "ms");
//  	vl.put("Malta", "mt");
//  	vl.put("Mauritius", "mu");
//  	vl.put("Maldives", "mv");
//  	vl.put("Malawi", "mw");
//  	vl.put("Mexico", "mx");
//  	vl.put("Malaysia", "my");
//  	vl.put("Mozambique", "mz");
//  	vl.put("Namibia", "na");
//  	//vl.put("NATO (this was purged in 1996 - see hq.nato.int)", "nato");
//  	vl.put("New Caledonia (French)", "nc");
//  	vl.put("Niger", "ne");
//  	vl.put("Network", "net");
//  	vl.put("Norfolk Island", "nf");
//  	vl.put("Nigeria", "ng");
//  	vl.put("Nicaragua", "ni");
//  	vl.put("Netherlands", "nl");
//  	vl.put("Norway", "no");
//  	vl.put("Nepal", "np");
//  	vl.put("Nauru", "nr");
//  	vl.put("Neutral Zone", "nt");
//  	vl.put("Niue", "nu");
//  	vl.put("New Zealand", "nz");
//  	vl.put("Oman", "om");
//  	//vl.put("Non-Profit Making Organisations (sic)", "org");
//  	vl.put("Panama", "pa");
//  	vl.put("Peru", "pe");
//  	vl.put("Polynesia (French)", "pf");
//  	vl.put("Papua New Guinea", "pg");
//  	vl.put("Philippines", "ph");
// 	vl.put("Pakistan", "pk");
// 	vl.put("Poland", "pl");
// 	vl.put("Saint Pierre and Miquelon", "pm");
// 	vl.put("Pitcairn Island", "pn");
// 	vl.put("Puerto Rico", "pr");
// 	vl.put("Portugal", "pt");
// 	vl.put("Palau", "pw");
// 	vl.put("Paraguay", "py");
// 	vl.put("Qatar", "qa");
// 	vl.put("Reunion (French)", "re");
// 	vl.put("Romania", "ro");
// 	vl.put("Russian Federation", "ru");
// 	vl.put("Rwanda", "rw");
// 	vl.put("Saudi Arabia", "sa");
// 	vl.put("Solomon Islands", "sb");
// 	vl.put("Seychelles", "sc");
// 	vl.put("Sudan", "sd");
// 	vl.put("Sweden", "se");
//  	vl.put("Singapore", "sg");
//  	vl.put("Saint Helena", "sh");
//  	vl.put("Slovenia", "si");
//  	vl.put("Svalbard and Jan Mayen Islands", "sj");
//  	vl.put("Slovak Republic", "sk");
//  	vl.put("Sierra Leone", "sl");
//  	vl.put("San Marino", "sm");
//  	vl.put("Senegal", "sn");
//  	vl.put("Somalia", "so");
//  	vl.put("Suriname", "sr");
//  	vl.put("Saint Tome (Sao Tome) and Principe", "st");
//  	vl.put("Former USSR", "su");
//  	vl.put("El Salvador", "sv");
//  	vl.put("Syria", "sy");
//  	vl.put("Swaziland", "sz");
//  	vl.put("Turks and Caicos Islands", "tc");
//  	vl.put("Chad", "td");
//  	vl.put("French Southern Territories", "tf");
//  	vl.put("Togo", "tg");
//  	vl.put("Thailand", "th");
//  	vl.put("Tadjikistan", "tj");
//  	vl.put("Tokelau", "tk");
//  	vl.put("Turkmenistan", "tm");
//  	vl.put("Tunisia", "tn");
//  	vl.put("Tonga", "to");
//  	vl.put("East Timor", "tp");
//  	vl.put("Turkey", "tr");
//  	vl.put("Trinidad and Tobago", "tt");
//  	vl.put("Tuvalu", "tv");
//  	vl.put("Taiwan", "tw");
//  	vl.put("Tanzania", "tz");
//  	vl.put("Ukraine", "ua");
//  	vl.put("Uganda", "ug");
//  	vl.put("United Kingdom", "uk");
//  	vl.put("USA Minor Outlying Islands", "um");
//  	vl.put("United States", "us");
//  	vl.put("Uruguay", "uy");
//  	vl.put("Uzbekistan", "uz");
//  	vl.put("Holy See (Vatican City State)", "va");
//  	vl.put("Saint Vincent &amp; Grenadines", "vc");
//  	vl.put("Venezuela", "ve");
//  	vl.put("Virgin Islands (British)", "vg");
//  	vl.put("Virgin Islands (USA)", "vi");
//  	vl.put("Vietnam", "vn");
//  	vl.put("Vanuatu", "vu");
//  	vl.put("Wallis and Futuna Islands", "wf");
//  	vl.put("Samoa", "ws");
//  	vl.put("Yemen", "ye");
//  	vl.put("Mayotte", "yt");
//  	vl.put("Yugoslavia", "yu");
//  	vl.put("South Africa", "za");
//  	vl.put("Zambia", "zm");
//  	vl.put("Zaire", "zr");
//  	vl.put("Zimbabwe", "zw");

// 	return vl;
    }
} // ValueLists
