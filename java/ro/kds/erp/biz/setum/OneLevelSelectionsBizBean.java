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
import ro.kds.erp.data.ProductLocalHome;
import java.util.HashMap;
import ro.kds.erp.data.ProductLocal;
import java.util.Iterator;

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
     * Indicates the current product in this selection. <code>null</code>
     * value indicates that no product is selected.
     */
    private Integer selectedProductId;

    /**
     * Cache of a reference to the <code>ProductsSelectionLocalHome</code>
     */
    private ProductsSelectionLocalHome selectionHome;

    /**
     * Cache of the home interface. Accessed through <code>getProductHome</code>
     * method.
     */
    private ProductLocalHome productHomeCache;

    /**
     * A cache of the items in the main listing (selections listing)
     * used to optimize the comunication between client UI and server
     * business logic.
     */
    private ArrayList selectionsListingCache;

    /**
     * Hash map that contains the products in the current selection.
     * The values of <code>products</code> are of type
     * <code>ProductLocal</code> and the keys are <code>Integer</code>,
     * represeneting the primary key of the product object.
     * We use a <code>HashMap</code> to ensure that there are no duplicates
     * in the selections.
     */
    private HashMap products;

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
		r.addField("selection.id", s.getId());
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
	ResponseBean r;

	try {
	    r = new ResponseBean();
	    r.addRecord();
	    ProductsSelectionLocal s = getCurrent();
	    if(s == null) {
		ProductsSelectionLocalHome sh = getSelectionHome();
		s = sh.create();
		
		ProductsSelectionLocal parent = getParentSelection();
		parent.getSelections().add(s);
		
		this.id = s.getId();
		r.addField("id", this.id);
	    }

	    s.setName(form.getName());
	    s.setCode(form.getCode());
	    s.setDescription(form.getDescription());
	    s.setProducts(products.values());
	} catch (FinderException e) {
	    logger.log(BasicLevel.ERROR, "Can not find the product id " + this.id);
	    logger.log(BasicLevel.DEBUG, e);
	    r = ResponseBean.ERR_NOTFOUND;
	} catch (NamingException e) {
	    logger.log(BasicLevel.ERROR, e.getMessage());
	    logger.log(BasicLevel.DEBUG, e);
	    r = ResponseBean.ERR_CONFIG_NAMING;
	} catch (CreateException e) {
	    logger.log(BasicLevel.ERROR, "Can not create the selection entity");
	    logger.log(BasicLevel.DEBUG, e);
	    r = ResponseBean.ERR_CREATE;
	}
	return r;
    }


    /**
     * The user selected a product in the current selection.
     */
    public ResponseBean selectProduct(Integer productId) {
	this.selectedProductId = productId;
	return ResponseBean.SUCCESS;
    }

    /**
     * Together with the initialization of the form bean, it will initialize
     * the state variables added by this implementation.
     */
    protected void createNewFormBean() {
	super.createNewFormBean();
	products = new HashMap();
	selectedProductId = null;
    }

    /**
     * Describe <code>loadFields</code> method here.
     *
     * @return a <code>ResponseBean</code> value
     * @exception FinderException if an error occurs
     */
    public ResponseBean loadFields() throws FinderException {
	ResponseBean r;
	try {
	    ProductsSelectionLocal s = getCurrent();
	    if(s == null) {
		r = ResponseBean.ERR_NOTCURRENT;
	    } else {
		form.setId(s.getId());
		form.setCode(s.getCode());
		form.setName(s.getName());
		form.setDescription(s.getDescription());
		products = new HashMap();
		for(Iterator i = s.getProducts().iterator(); i.hasNext(); ) {
		    ProductLocal p = (ProductLocal)i.next();
		    products.put(p.getPrimaryKey(), p);
		}
		r = new ResponseBean();
		
	    }
	} catch (NamingException e) {
	    logger.log(BasicLevel.ERROR, "Naming exception occured");
	    logger.log(BasicLevel.DEBUG, e);
	    r = ResponseBean.ERR_CONFIG_NAMING;
	}
	return r;
    }

    /**
     * Add a product to the selection.
     */
    public ResponseBean addProduct(Integer productId) {
	ResponseBean r;
	if(products.containsKey(productId)) {
	    logger.log(BasicLevel.DEBUG, "Product with id " + productId +
		       " already added");
	    r = ResponseBean.SUCCESS;
	} else {
	    try {
		ProductLocalHome ph = getProductHome();
		products.put(productId, ph.findByPrimaryKey(productId));
		logger.log(BasicLevel.DEBUG, "Product " + productId +
			   " added to the current selection -- id " + this.id);
		r = ResponseBean.SUCCESS;
	    } catch (NamingException e) {
		logger.log(BasicLevel.ERROR, "Naming exception while getting the product home interface");
		logger.log(BasicLevel.DEBUG, e);
		r = ResponseBean.ERR_CONFIG_NAMING;
	    } catch (FinderException e) {
		logger.log(BasicLevel.ERROR, "Product with id " + productId +
			   " not found");
		logger.log(BasicLevel.DEBUG, e);
		r = ResponseBean.ERR_NOTFOUND;
	    }
	}
	return r;
    }

    /**
     * Remove a product from the selection.
     */
    public ResponseBean removeProduct() {
	ResponseBean r;
	if(this.selectedProductId != null) {
	    products.remove(this.selectedProductId);
	    logger.log(BasicLevel.DEBUG, "Remove product id: " + this.selectedProductId);
	    r = ResponseBean.SUCCESS;
	} else {
	    r = ResponseBean.ERR_NOTCURRENT;
	}
	return r;
    }

    /**
     * Get the products in the current selection.
     */
    public ResponseBean productsListing() {
	ResponseBean r = new ResponseBean();

	for(Iterator i = products.values().iterator(); i.hasNext();) {
	    ProductLocal p = (ProductLocal)i.next();
	    
	    r.addRecord();
	    r.addField("product.id", p.getId());
	    r.addField("product.code", p.getCode());
	    r.addField("product.name", p.getName());
	    r.addField("product.description", p.getDescription());
	    r.addField("product.categoru", p.getCategory().getName());
	    r.addField("product.price1", p.getPrice1());
	    r.addField("product.price2", p.getPrice2());
	    r.addField("product.price3", p.getPrice3());
	    r.addField("product.price4", p.getPrice4());
	    r.addField("product.price5", p.getPrice5());
	}

	return r;
    }

    /**
     * Current entity for the main form.
     * @returns an entity bean that has the primary key <code>this.id</code>
     * or null if there is no current id selected -- that is <code>this.id == null</code>.
     *
     * @throws FinderException if there is no entity for the given primary key
     *
     */
    private ProductsSelectionLocal getCurrent() throws FinderException, NamingException {
	if (this.id == null) {
	    logger.log(BasicLevel.DEBUG, "No current selection selected");
	    return null;
	}

	ProductsSelectionLocalHome sh = getSelectionHome();

	return sh.findByPrimaryKey(this.id);
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

    /**
     * Get a reference to the ProductLocalHome object. It caches the result.
     */
    private ProductLocalHome getProductHome() 
	throws NamingException {
	
	if(productHomeCache == null) {
	    Context ic = new InitialContext();
	    Context env = (Context)ic.lookup("java:comp/env");
	    productHomeCache = (ProductLocalHome)PortableRemoteObject
		.narrow(env.lookup("ejb/ProductHome"), ProductLocalHome.class);
	}
	return productHomeCache;
    }
}

