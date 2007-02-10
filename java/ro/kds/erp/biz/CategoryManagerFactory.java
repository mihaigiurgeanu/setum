package ro.kds.erp.biz;

import javax.rmi.PortableRemoteObject;
import javax.naming.NamingException;
import javax.ejb.CreateException;
import javax.naming.InitialContext;
import javax.naming.Context;

/**
 * Common methods for getting a reference to a <code>CategoryManager</code>.
 *
 *
 * Created: Mon Jan 22 21:10:39 2007
 *
 * @author <a href="mailto:Mihai Giurgeanu@MIHAIG">U-MIHAIG\Mihai Giurgeanu</a>
 * @version 1.0
 */
public class CategoryManagerFactory {

    /**
     * Get a reference to a <code>CategoryManagerLocal</code> object (a local reference).
     *
     * If a code is using this method, it must define the following EJB references:
     *
     * "ejb/CategoryManagerHome/default" - a default category manager
     * for each special category, a reference of the form: "ejb/CategoryManagerHome/<category-id>"
     *
     * @param categoyId is the primary key of a <code>CategoryEJB</code> entity.
     */
    public static CategoryManagerLocal getCategoryManager(Integer categoryId) 
	throws CategoryManagerException {

	try {
	    CategoryManagerLocalHome cmh;
	    InitialContext ic = new InitialContext();
	    Context env = (Context)ic.lookup("java:comp/env");
	    try {

		cmh = (CategoryManagerLocalHome)PortableRemoteObject.
		    narrow(env.lookup("ejb/CategoryManagerHome/" + categoryId), 
			   CategoryManagerLocalHome.class);
	    } catch (NamingException e) {
		// it seems that no category manager is registered for the
		// given id ... try to get the default category manager
		cmh = (CategoryManagerLocalHome)PortableRemoteObject.
		    narrow(env.lookup("ejb/CategoryManagerHome/default"),
			   CategoryManagerLocalHome.class);
	    }
	
	    return cmh.create();
	} catch (NamingException e) {
	    throw new CategoryManagerException(e.getMessage(), e);
	} catch (CreateException e) {
	    throw new CategoryManagerException(e.getMessage(), e);
	}
    }
}
