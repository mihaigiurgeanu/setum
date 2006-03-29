package ro.kds.erp.biz.setum;

import ro.kds.erp.data.CategoryLocalHome;
import ro.kds.erp.data.ProductLocalHome;
import ro.kds.erp.data.AttributeLocalHome;
import javax.naming.NamingException;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;
import javax.naming.Context;

/**
 * Utility static functions realted to products and categories.
 *
 *
 * Created: Wed Mar 29 17:07:41 2006
 *
 * @author <a href="mailto:Mihai Giurgeanu@CRIMIRA"></a>
 * @version 1.0
 */
public class Products {

    /**
     * Cache home reference
     */
    private static CategoryLocalHome cHome;
    /**
     * Cache home reference
     */
    private static ProductLocalHome pHome;
    /**
     * Cache home reference
     */
    private static AttributeLocalHome aHome;



    /**
     * Gets home reference.
     */
    public static CategoryLocalHome getCategoryHome() throws NamingException {
	if(cHome != null) return cHome;
	
	InitialContext ic = new InitialContext();
	Context env = (Context)ic.lookup("java:comp/env");
	cHome = (CategoryLocalHome) PortableRemoteObject.
	    narrow(env.lookup("ejb/CategoryHome"), CategoryLocalHome.class);
	return cHome;
    }

    /**
     * Gets home reference.
     */
    public static ProductLocalHome getProductHome() throws NamingException {
	if(pHome != null) return pHome;

	InitialContext ic = new InitialContext();
	Context env = (Context)ic.lookup("java:comp/env");
	pHome = (ProductLocalHome)PortableRemoteObject.
	    narrow(env.lookup("ejb/ProductHome"), ProductLocalHome.class);
	return pHome;
    }

    /**
     * Gets home reference.
     */
    public static AttributeLocalHome getAttributeHome() throws NamingException {
	if(aHome != null) return aHome;

	InitialContext ic = new InitialContext();
	Context env = (Context)ic.lookup("java:comp/env");
	aHome = (AttributeLocalHome)PortableRemoteObject.
	    narrow(env.lookup("ejb/AttributeHome"), AttributeLocalHome.class);
	return aHome;
    }
}

