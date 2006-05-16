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
import org.objectweb.util.monolog.api.BasicLevel;
import java.util.ArrayList;
import javax.ejb.CreateException;

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
     * A cache of the items in the main listing (selections listing)
     * used to optimize the comunication between client UI and server
     * business logic.
     */
    private ArrayList selectionsListingCache;

    /**
     * Build the listing of selections contained in the parent category.
     */
    public ResponseBean loadListing(Integer startRow) {
	ResponseBean r;

	if(selectionsListingCache == null) {
	    r = ResponseBean.ERR_OUT_OF_ORDER_OPERATION;
	    logger.log(BasicLevel.WARN, "selectionsListingCache not initialized!");
	} else {
	    r = new ResponseBean();
	    for(int i = startRow.intValue(); 
		i<selectionsListingCache.size() && i<startRow.intValue() + 30;
		i ++) {

		ProductsSelectionLocal s = 
		    (ProductsSelectionLocal) selectionsListingCache.get(i);


		r.addRecord();
		r.addField("selction.id", s.getId());
		r.addField("selection.code", s.getCode());
		r.addField("selection.name", s.getName());
		//r.addField("selection.description", s.getDescription());
	    }
	}

	return r;
    }

    /**
     * Counts the number of records in the listing and also initializes
     * a listing operation. This method must be called before <code>loadListing
     * </code> method.
     *
     * The method has a side efect of initializing a listing cache, so the
     * records returned by the <code>loadListing</code> method will not be
     * affected by other operations made by other users. The refresh of the
     * listing will be done by another call to <code>getListingLength</code>
     * method, and thus, the user interface will handle the listing in a 
     * natural way.
     *
     * @returns a <code>ResponseBean</code> with a single record containing
     * the field <code>records-count</code>.
     */
    public ResponseBean getListingLength() { 
	ResponseBean r;
	try {
	    ProductsSelectionLocal parent = getParentSelection();
	    Collection selections = parent.getSelections();

	    selectionsListingCache = new ArrayList(selections);
	    r = new ResponseBean();
	    r.addRecord();
	    r.addField("records-count", selectionsListingCache.size());

	} catch (NamingException e) {
	    r = ResponseBean.ERR_CONFIG_NAMING;
	} catch (CreateException e) {
	    r = ResponseBean.ERR_CONFIG_NOTFOUND;
	}
	return r;
   }

    /**
     * Describe <code>saveFormData</code> method here.
     *
     * @return a <code>ResponseBean</code> value
     */
    public ResponseBean saveFormData() {
	return null;
    }

    /**
     * Describe <code>loadFields</code> method here.
     *
     * @return a <code>ResponseBean</code> value
     * @exception FinderException if an error occurs
     */
    public ResponseBean loadFields() throws FinderException {
	return null;
    }

    /**
     * Add a product to the selection.
     */
    public ResponseBean addProduct(Integer productId) {
	return null;
    }

    /**
     * Remove a product from the selection.
     */
    public ResponseBean removeProduct(Integer productId) {
	return null;
    }

    /**
     * Get the products in the current selection.
     */
    public ResponseBean productsListing() {
	return null;
    }

    /**
     * Locates a reference to the parent selection. If the parent
     * selection does not exist yet, it is created.
     */
    private ProductsSelectionLocal getParentSelection() 
	throws NamingException, CreateException {

	if (parentSelection == null ) {
	    Context ic = new InitialContext();
	    Context env = (Context) ic.lookup("java:comp/env");
	    String parentCode = (String) env.lookup(PARENT_PARAM);

	    ProductsSelectionLocalHome ph = getSelectionHome();
	    try {
		parentSelection = ph.findByCode(parentCode);
	    } catch (FinderException e) {
		parentSelection = ph.create();
		parentSelection.setCode(parentCode);
		logger.log(BasicLevel.INFO, "Parent selection created");
	    }
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

