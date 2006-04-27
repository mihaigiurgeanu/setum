package ro.kds.erp.biz.setum;

import ro.kds.erp.biz.setum.basic.OneLevelSelectionsBean;
import ro.kds.erp.biz.ResponseBean;
import ro.kds.erp.data.ProductsSelectionLocal;
import javax.naming.NamingException;
import javax.ejb.FinderException;
import javax.naming.InitialContext;
import ro.kds.erp.data.ProductsSelectionLocalHome;
import javax.rmi.PortableRemoteObject;
import javax.naming.Context;
import java.util.Collection;
import ro.kds.erp.biz.ResponseBean;
import ro.kds.erp.biz.ResponseBean;
import javax.ejb.FinderException;

/**
 * Extends auto generated <code>OneLevelSelectionsBean</code> class and give
 * specific business logic implementation.
 *
 * The business logic implemented refers at defining selections of products
 * that contain only products (i.e. do not contain other selections) and
 * that are grouped in a given products selection.
 *
 * The code of the parent selection is given in environment value with the
 * name <code>parent</code>.
 *
 *
 * Created: Thu Apr 27 03:19:21 2006
 *
 * @author <a href="mailto:mihai@cris.kds.ro">Mihai Giurgeanu</a>
 * @version 1.0
 */
public class OneLevelSelectionsBizBean extends OneLevelSelectionsBean {

    /**
     * The name of the environment value that has the code of the
     * parent selection.
     */
    public static final String PARENT_PARAM = "parent";

    /**
     * Caches a reference to the parent selection.
     */
    private ProductsSelectionLocal parentSelection;

    /**
     * Cache of a reference to the <code>ProductsSelectionLocalHome</code>
     */
    private ProductsSelectionLocalHome selectionHome;

    /**
     * Build the listing of selections contained in the parent category.
     */
    public ResponseBean getListing() {
	ResponseBean r;

	try {
	    ProductsSelectionLocal parent = getParentSelection();
	    Collection selections = parent.getSelections();

	    r = new ResponseBean();
	} catch (NamingException e) {
	    r = ResponseBean.ERR_CONFIG_NAMING;
	} catch (FinderException e) {
	    r = ResponseBean.ERR_CONFIG_NOTFOUND;
	}

	return r;
    }

    /**
     * Describe <code>saveFormData</code> method here.
     *
     * @return a <code>ResponseBean</code> value
     */
    public final ResponseBean saveFormData() {
	return null;
    }

    /**
     * Describe <code>loadFields</code> method here.
     *
     * @return a <code>ResponseBean</code> value
     * @exception FinderException if an error occurs
     */
    public final ResponseBean loadFields() throws FinderException {
	return null;
    }
     

    /**
     * Locates a reference to the parent selection
     */
    private ProductsSelectionLocal getParentSelection() 
	throws NamingException, FinderException {

	if (parentSelection == null ) {
	    Context ic = new InitialContext();
	    Context env = (Context) ic.lookup("java:comp/env");
	    String parentCode = (String) env.lookup(PARENT_PARAM);

	    ProductsSelectionLocalHome ph = getSelectionHome();
	    parentSelection = ph.findByCode(parentCode);
	}

	return parentSelection;
    }

    /**
     * Get a reference to a ProductsSelectionLocalHome object.
     */
    private ProductsSelectionLocalHome getSelectionHome() 
	throws NamingException {
	if (selectionHome == null) {
	    Context ic = new InitialContext();
	    Context env = (Context)ic.lookup("java:comp/env");
	    selectionHome = (ProductsSelectionLocalHome)PortableRemoteObject
		.narrow(env.lookup("ejb/ProductsSelectionHome"), ProductsSelectionLocalHome.class);
	}
	return selectionHome;
    }
}

