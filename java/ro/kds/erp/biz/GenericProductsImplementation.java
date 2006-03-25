package ro.kds.erp.biz;

import ro.kds.erp.biz.basic.GenericProductsBean;
import javax.ejb.FinderException;
import org.objectweb.util.monolog.api.BasicLevel;
import ro.kds.erp.data.CategoryLocal;
import ro.kds.erp.data.ProductLocal;
import ro.kds.erp.data.AttributeLocal;
import ro.kds.erp.data.CategoryLocalHome;
import ro.kds.erp.data.ProductLocalHome;
import ro.kds.erp.data.AttributeLocalHome;
import javax.naming.NamingException;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;
import javax.naming.Context;
import java.util.Collection;
import java.util.Iterator;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import javax.ejb.CreateException;
import javax.ejb.RemoveException;
import java.util.Collections;
import java.util.List;
import java.util.Comparator;
import java.util.ArrayList;

/**
 * Describe class GenericProductsImplementation here.
 *
 *
 * Created: Tue Mar 14 18:58:14 2006
 *
 * @author <a href="mailto:Mihai Giurgeanu@CRIMIRA"></a>
 * @version $Id: GenericProductsImplementation.java,v 1.4 2006/03/25 22:05:20 mihai Exp $
 */
public class GenericProductsImplementation extends GenericProductsBean {

    /**
     * The name of the environment parameter that contains
     * the value of the root category id.
     */
    public static final String ROOT_CATEGORY_ID_PARAM="rootCategoryId";

    /**
     * Get the listing of all categories contained in the
     * preconfigured categrory id. The name of the preconfigured
     * category id is kept in the static constant 
     * <code>ROOT_CATEGORY_ID_PARAM</code>.
     *
     * @returns a <code>ResponseBean</code> containing as records all
     * the rows that should be listed in the categories listing. The
     * fields in the listing are:
     * <ul>
     * <li>categories.id</li>
     * <li>categories.name</li>
     * <li>categories.productsCount</li>
     * </ul>
     */
    public ResponseBean loadCategories() {
	ResponseBean r;

	try {
	    CategoryLocal rootCategory = getRootCategory();
	    List categories = new ArrayList(rootCategory.getSubCategories());

	    Collections.sort(categories,
			     new Comparator() {
				 public int compare(Object o1, Object o2) {
				     return ((CategoryLocal)o1).getId()
					 .compareTo(((CategoryLocal)o2).getId());
				 }
				 
				 public boolean equals(Object o) {
				     return this.equals(o);
				 }
			     });
	    

	    r = new ResponseBean();
	    for(Iterator i = categories.iterator(); i.hasNext(); ) {
		CategoryLocal c = (CategoryLocal)i.next();
		r.addRecord();
		r.addField("categories.id", c.getId());
		r.addField("categories.name", c.getName());
		r.addField("categories.productsCount", c.getProductsCount());
	    }

	} catch (NamingException e) {
	    r = ResponseBean.ERR_CONFIG_NAMING;
	    logger.log(BasicLevel.ERROR, e);
	} catch (FinderException e) {
	    r = ResponseBean.ERR_CONFIG_NOTFOUND;
	    logger.log(BasicLevel.ERROR, e);
	} catch (Exception e) {
	    r = ResponseBean.ERR_UNEXPECTED;
	    logger.log(BasicLevel.ERROR, e);
	}
	return r;
    }


    /**
     * Get all the products in the current selected category.
     *
     * @returns a <code>ResponseBean</code> containing the product records.
     * For each record, there are the following fields:
     * <ul>
     * <li>products.id</li>
     * <li>products.code</li>
     * <li>products.name</li>
     * <li>products.entryPrice</li>
     * <li>products.sellPrice</li>
     * </ul>
     */
    public ResponseBean loadProducts() {
	ResponseBean r;

	try {
	    CategoryLocal c = getCurrentCategory();
	    if(c == null) {
		r = ResponseBean.SUCCESS; // it is a new category, it has no products
	    } else {
		List products = new ArrayList(c.getProducts());
		Collections.sort(products,
				 new Comparator() {
				     public int compare(Object o1, Object o2) {
					 return ((ProductLocal)o1).getCode()
					     .compareToIgnoreCase(((ProductLocal)o2).getCode());
				     }

				     public boolean equals(Object o) {
					 return this.equals(o);
				     }
				 });

		r = new ResponseBean();
		for(Iterator i = products.iterator(); i.hasNext(); ) {
		    ProductLocal p = (ProductLocal)i.next();
		    r.addRecord();
		    r.addField("products.id", p.getId());
		    r.addField("products.code", p.getCode());
		    r.addField("products.name", p.getName());
		    r.addField("products.entryPrice", p.getEntryPrice());
		    r.addField("products.sellPrice", p.getSellPrice());
		}
	    }
	} catch (FinderException e) {
	    r = ResponseBean.ERR_NOTFOUND;
	} catch (Exception e) {
	    r = ResponseBean.ERR_UNEXPECTED;
	    logger.log(BasicLevel.ERROR, e);
	}
	return r;
    }

    /**
     * Get all the attributes of the current product.
     *
     * @returns a <code>ResponseBean</code> containing the attribute records.
     * The fields of each record are:
     * <ul>
     * <li>attr.id</li>
     * <li>attr.name</li>
     * <li>attr.string</li>
     * <li>attr.int</li>
     * <li>attr.decimal</li>
     * <li>attr.double</li>
     * </ul>
     */
    public ResponseBean loadAttributes() {
	ResponseBean r;

	try {
	    ProductLocal p = getCurrentProduct();
	    if(p == null) {
		r = ResponseBean.SUCCESS; // it is a new product, it has no attributes
	    } else {
		Collection attribs = p.getAttributes();
		r = new ResponseBean();
		for(Iterator i = attribs.iterator(); i.hasNext(); ) {
		    AttributeLocal a = (AttributeLocal)i.next();
		    r.addRecord();
		    r.addField("attr.id", a.getId());
		    r.addField("attr.name", a.getName());
		    r.addField("attr.string", a.getStringValue());
		    r.addField("attr.int", a.getIntValue());
		    r.addField("attr.decimal", a.getDecimalValue());
		    r.addField("attr.double", a.getDoubleValue());
		}
	    }
	} catch (FinderException e) {
	    r = ResponseBean.ERR_NOTFOUND;
	} catch (Exception e) {
	    r = ResponseBean.ERR_UNEXPECTED;
	    logger.log(BasicLevel.ERROR, e);
	}
	return r;
    }



    /**
     * Loads the main form field values from the database. This method it is
     * not directly exposed in the remote interface. It is called
     * by the <code>loadFormData</code> bean method.
     *
     * @returns a new <code>ResponseBean</code> with no record containing the
     * error code (0 for success, and 4 if no product is current). The returned value
     * should not be a standard error object, since the <code>loadFormData</code> 
     * methode will add fields to it.
     * @throws FinderException if the <code>id</code> is not null and the 
     * category can not be found in the database.
     */
    public ResponseBean loadFields() throws FinderException {
	ResponseBean r;
	CategoryLocal c = getCurrentCategory();
	
	if(c != null) {
	    form.setCategoryId(c.getId());
	    form.setCategoryName(c.getName());
	    r = new ResponseBean();
	} else {
	    r = new ResponseBean();
	    r.setCode(4);
	    r.setMessage("Nici o categorie nu este selectata");
	}
	
	return r;
    }

    /**
     * Loads the product subform field values from the database. This method it is
     * not directly exposed in the remote interface. It is called
     * by the <code>loadProductData</code> bean method.
     *
     * @returns a new <code>ResponseBean</code> with no record containing the
     * error code (0 for success, and 4 if no product is current). The returned object
     * should not be a standard error object, since the <code>loadFormData</code> 
     * methode will add fields to it.
     * @throws FinderException if the <code>productId</code> is not null and the 
     * product can not be found in the database.
     */
    public ResponseBean loadProductFields() throws FinderException {
	ResponseBean r;

	ProductLocal p = getCurrentProduct();
	if(p != null) {
	    form.setProductId(p.getId());
	    form.setProductName(p.getName());
	    form.setProductCode(p.getCode());
	    form.setProductDescription(p.getDescription());
	    form.setProductEntryPrice(p.getEntryPrice());
	    form.setProductSellPrice(p.getSellPrice());
	    form.setProductPrice1(p.getPrice1());
	    form.setProductPrice2(p.getPrice2());
	    form.setProductPrice3(p.getPrice3());
	    form.setProductPrice4(p.getPrice4());
	    form.setProductPrice5(p.getPrice5());
	    r = new ResponseBean();
	} else {
	    r = new ResponseBean();
	    r.setCode(4);
	    r.setMessage("Nici un produs nu este selectat");
	}

	return r;
    }
    
    /**
     * Loads the attribute subform field values from the database. This method it is
     * not directly exposed in the remote interface. It is called
     * by the <code>loadAttributeData</code> bean method.
     *
     * @returns a new <code>ResponseBean</code> with no record containing the
     * error code (0 for success, and 4 if no product is current). The returned object
     * should not be a standard error object, since the <code>loadFormData</code> 
     * methode will add fields to it.
     * @throws FinderException if the <code>attributeId</code> is not null and the 
     * attribute can not be found in the database.
     */
    public ResponseBean loadAttributeFields() throws FinderException {
	ResponseBean r;

	AttributeLocal a = getCurrentAttribute();
	if(a != null) {
	    form.setAttrId(a.getId());
	    form.setAttrName(a.getName());
	    form.setAttrString(a.getStringValue());
	    form.setAttrInt(a.getIntValue());
	    form.setAttrDecimal(a.getDecimalValue());
	    form.setAttrDouble(a.getDoubleValue());
	    r = new ResponseBean();
	} else {
	    r = new ResponseBean();
	    r.setCode(4);
	    r.setMessage("Nici un atribut nu este selectat");
	}
	return r;
    }

    /**
     * Initializing the values of the product subform.
     */
    protected void initProductFields() {
	form.setProductId(new Integer(0));
	form.setProductName("");
	form.setProductCode("");
	form.setProductDescription("");
	form.setProductEntryPrice(new BigDecimal(0));
	form.setProductSellPrice(new BigDecimal(0));
	form.setProductPrice1(new BigDecimal(0));
	form.setProductPrice2(new BigDecimal(0));
	form.setProductPrice3(new BigDecimal(0));
	form.setProductPrice4(new BigDecimal(0));
	form.setProductPrice5(new BigDecimal(0));	
    }

    /**
     * Initializing the fields of the attribute subform.
     */
    protected void initAttributeFields() {
	form.setAttrId(new Integer(0));
	form.setAttrName("");
	form.setAttrString("");
	form.setAttrInt(new Integer(0));
	form.setAttrDecimal(new BigDecimal(0));
	form.setAttrDouble(new Double(0));
    }

    /**
     * Saves the data of the main form into the persistent layer.
     * The method is directly exposed through the remote interface,
     * no modifications of its returned data will be made.
     *
     * @returns an <code>ResponseBean</code> object containing the
     * error code (0 for success) and a record with the value of
     * categoryId field.
     */
    public ResponseBean saveFormData() {
	ResponseBean r;

	try {
	    CategoryLocal c = getCurrentCategory();
	    if(c == null) {
		c = makeNewCategory();
	    } else {
		c.setName(form.getCategoryName());
	    }
	    r = new ResponseBean();
	    r.addRecord();
	    r.addField("categoryId", c.getId());
	} catch (FinderException e) {
	    // the current category can not be found, although 
	    // a categoryId exists
	    r = ResponseBean.ERR_NOTFOUND;
	    logger.log(BasicLevel.ERROR, e);
	} catch (NamingException e) {
	    r = ResponseBean.ERR_CONFIG_NAMING;
	    logger.log(BasicLevel.ERROR, e);
	} catch (CreateException e) {
	    r = ResponseBean.ERR_CREATE;
	    logger.log(BasicLevel.ERROR, e);
	} catch (RemoteException e) {
	    // can be generated when trying to obtain an automatic
	    // new category id
	    r = ResponseBean.ERR_REMOTE;
	    logger.log(BasicLevel.ERROR, e);
	}
	
	return r;
    }

    protected CategoryLocal makeNewCategory() 
	throws FinderException, CreateException, 
	       NamingException, RemoteException {
	
	Integer cId = form.getCategoryId();
	if(cId.intValue() == 0) {
	    cId = getNextCategoryId();
	    logger.log(BasicLevel.DEBUG, "Create new category: " +
		       cId + " " + form.getCategoryName());
	}
	CategoryLocalHome ch = getCategoryHome();
	CategoryLocal c = ch.create(cId, form.getCategoryName());
	CategoryLocal root = getRootCategory();
	root.getSubCategories().add(c);

	form.setCategoryId(cId);
	id = cId; // the currently selected category

	return c;
    }

    /**
     * Saves the data of the product subform into the persistent layer.
     * The method is directly exposed through the remote interface,
     * no modifications of its returned data will be made.
     *
     * @returns an <code>ResponseBean</code> object containing the
     * error code (0 for success) and a record with the value of
     * productId field.
     */
    public ResponseBean saveProductData() {
	ResponseBean r;

	try {
	    ProductLocal p = getCurrentProduct();
	    if(p == null) {
		p = makeNewProduct();
	    }
	    p.setName(form.getProductName());
	    p.setCode(form.getProductCode());
	    p.setDescription(form.getProductDescription());
	    p.setEntryPrice(form.getProductEntryPrice());
	    p.setSellPrice(form.getProductSellPrice());
	    p.setPrice1(form.getProductPrice1());
	    p.setPrice2(form.getProductPrice2());
	    p.setPrice3(form.getProductPrice3());
	    p.setPrice4(form.getProductPrice4());
	    p.setPrice5(form.getProductPrice5());

	    r = new ResponseBean();
	    r.addRecord();
	    r.addField("productId", p.getId());
	} catch (FinderException e) {
	    // the currently selected product id is not in the 
	    // database
	    r = ResponseBean.ERR_NOTFOUND;
	    logger.log(BasicLevel.ERROR, e);
	} catch (NamingException e) {
	    // naming exception thrown by getProductHome()
	    r = ResponseBean.ERR_CONFIG_NAMING;
	    logger.log(BasicLevel.ERROR, e);
	} catch (CreateException e) {
	    // can not create the new product
	    r = ResponseBean.ERR_CREATE;
	    logger.log(BasicLevel.ERROR, e);
	} catch (RemoteException e) {
	    r = ResponseBean.ERR_REMOTE;
	    logger.log(BasicLevel.ERROR, e);
	}

	return r;
    }

    protected ProductLocal makeNewProduct() 
	throws NamingException, CreateException, 
	       FinderException, RemoteException {

	ProductLocalHome ph = getProductHome();
	ProductLocal p = ph.create();
	productId = p.getId(); // set the currently selected product
	form.setProductId(productId);


	CategoryLocal c = getCurrentCategory();
	if(c == null) c = makeNewCategory();
	c.getProducts().add(p);

	return p;
    }

    public ResponseBean saveAttributeData() {
	ResponseBean r;

	try {
	    AttributeLocal a = getCurrentAttribute();
	    if (a == null) {
		AttributeLocalHome ah = getAttributeHome();
		a = ah.create();
		attributeId = a.getId(); // the new attribute is the current one
		ProductLocal p = getCurrentProduct();
		if(p == null) p = makeNewProduct();
		p.getAttributes().add(a);

	    }
	    a.setName(form.getAttrName());
	    a.setStringValue(form.getAttrString());
	    a.setIntValue(form.getAttrInt());
	    a.setDecimalValue(form.getAttrDecimal());
	    a.setDoubleValue(form.getAttrDouble());

	    form.setAttrId(attributeId);
	    r = new ResponseBean();
	    r.addRecord();
	    r.addField("attrId", attributeId);
	} catch (FinderException e) {
	    r = ResponseBean.ERR_NOTFOUND;
	    logger.log(BasicLevel.ERROR, e);
	} catch (CreateException e) {
	    r = ResponseBean.ERR_CREATE;
	    logger.log(BasicLevel.ERROR, e);
	} catch (NamingException e) {
	    r = ResponseBean.ERR_CONFIG_NAMING;
	    logger.log(BasicLevel.ERROR, e);
	} catch (RemoteException e) {
	    r = ResponseBean.ERR_REMOTE;
	    logger.log(BasicLevel.ERROR, e);
	}

	return r;
    }

    public ResponseBean removeCategory() {
	ResponseBean r;
	try {
	    CategoryLocal c = getCurrentCategory();
	    if(c == null) {
		r = ResponseBean.ERR_NOTCURRENT;
	    } else {
		c.remove();
		r = ResponseBean.SUCCESS;
	    }
	} catch (FinderException e) {
	    r = ResponseBean.ERR_NOTFOUND;
	    logger.log(BasicLevel.ERROR, e);
	} catch (RemoveException e) {
	    r = ResponseBean.ERR_REMOVE;
	    logger.log(BasicLevel.ERROR, e);
	}

	return r;
    }

    public ResponseBean removeProduct() {
	ResponseBean r;
	try {
	    ProductLocal p = getCurrentProduct();
	    if(p == null) {
		r = ResponseBean.ERR_NOTCURRENT;
	    } else {
		p.remove();
		r = ResponseBean.SUCCESS;
	    }
	} catch (FinderException e) {
	    r = ResponseBean.ERR_NOTFOUND;
	    logger.log(BasicLevel.ERROR, e);
	} catch (RemoveException e) {
	    r = ResponseBean.ERR_REMOVE;
	    logger.log(BasicLevel.ERROR, e);
	}

	return r;
    }

    public ResponseBean removeAttribute() {
	ResponseBean r;
	try {
	    AttributeLocal a = getCurrentAttribute();
	    if(a == null) {
		r = ResponseBean.ERR_NOTCURRENT;
	    } else {
		a.remove();
		r = ResponseBean.SUCCESS;
	    }
	} catch (FinderException e) {
	    r = ResponseBean.ERR_NOTFOUND;
	    logger.log(BasicLevel.ERROR, e);
	} catch (RemoveException e) {
	    r = ResponseBean.ERR_REMOVE;
	    logger.log(BasicLevel.ERROR, e);
	}

	return r;
    }

    /**
     * Convinience call to retrieve the category that is currently selected.
     *
     * @returns a <code>CategoryLocal</code> object if a valid id was given, or
     * null if no category is currently selected (the <code>id</code> field is
     * null).
     * @throws FinderException if the current <code>id</code> can not be found
     */
    protected CategoryLocal getCurrentCategory() throws FinderException {

	logger.log(BasicLevel.DEBUG, "Loading category for id " + id);
	if(id == null) return null;


	CategoryLocal c;
	try {
	    logger.log(BasicLevel.DEBUG, "Reading the category from database");
	    CategoryLocalHome home = getCategoryHome();
	    c = home.findByPrimaryKey(id);
	} catch (FinderException e) {
	    logger.log(BasicLevel.WARN, "Can not find category with id " + id);
	    logger.log(BasicLevel.DEBUG, e);
	    throw e;
	} catch (Exception e) {
	    logger.log(BasicLevel.ERROR, "Exception occured while searching for the category with id " 
		       + id + ": " + e.getMessage());
	    logger.log(BasicLevel.INFO, e);
	    c = null;
	}
	return c;
    }

    /**
     * Convinience call to retrieve the product that is currently selected.
     *
     * @returns a <code>ProductLocal</code> object if a valid productId was given, or
     * null if no category is product selected (the <code>productId</code> field is
     * null).
     * @throws FinderException if the current <code>productId</code> can not be found
     */
    protected ProductLocal getCurrentProduct() throws FinderException {
	if(productId == null) return null;

	ProductLocal p;
	try {
	    ProductLocalHome home = getProductHome();
	    p = home.findByPrimaryKey(productId);
	} catch (FinderException e) {
	    logger.log(BasicLevel.WARN, "Can not find the product with id " + productId);
	    logger.log(BasicLevel.DEBUG, e);
	    throw e;
	} catch (Exception e) {
	    logger.log(BasicLevel.ERROR, "Exception occured while searching for the product with id " 
		       + productId + ": " + e.getMessage());
	    logger.log(BasicLevel.INFO, e);
	    p = null;
	}

	return p;

    }


    /**
     * Convinience call to retrieve the attribute that is currently selected.
     *
     * @returns a <code>AttributeLocal</code> object if a valid attributeId was given, or
     * null if no category is product selected (the <code>attributeId</code> field is
     * null).
     * @throws FinderException if the current <code>attributeId</code> can not be found
     */
    protected AttributeLocal getCurrentAttribute() throws FinderException {
	if(attributeId == null) return null;

	AttributeLocal a;
	try {
	    AttributeLocalHome home = getAttributeHome();
	    a = home.findByPrimaryKey(attributeId);
	} catch (FinderException e) {
	    logger.log(BasicLevel.WARN, "Can not find attribute with id " + attributeId);
	    logger.log(BasicLevel.INFO, e);
	    throw e;
	} catch (Exception e) {
	    logger.log(BasicLevel.ERROR, "Exception thrown when searching for attribute " +
		       attributeId + ": " + e.getMessage());
	    logger.log(BasicLevel.INFO, e);
	    a = null;
	}
	
	return a;
    } 

    /**
     * Get the root category. The root category is the category that contains
     * all the categories in edited by this business logic.
     */
    protected CategoryLocal getRootCategory() throws NamingException, FinderException {
	InitialContext ic = new InitialContext();
	Context env = (Context)ic.lookup("java:comp/env");
	Integer rootCategoryId = (Integer)env.lookup(ROOT_CATEGORY_ID_PARAM);
	
	CategoryLocalHome ch = getCategoryHome();
	CategoryLocal rootCategory = ch.findByPrimaryKey(rootCategoryId);
	return rootCategory;
    }

    /**
     * Utility method to get the home interface for category bean.
     */
    protected CategoryLocalHome getCategoryHome() throws NamingException {
	if(cHome != null) return cHome;
	
	InitialContext ic = new InitialContext();
	Context env = (Context)ic.lookup("java:comp/env");
	cHome = (CategoryLocalHome) PortableRemoteObject.
	    narrow(env.lookup("ejb/CategoryHome"), CategoryLocalHome.class);
	return cHome;
    }

    /**
     * Utility method to get the home interface for product bean.
     */
    protected ProductLocalHome getProductHome() throws NamingException {
	if(pHome != null) return pHome;

	InitialContext ic = new InitialContext();
	Context env = (Context)ic.lookup("java:comp/env");
	pHome = (ProductLocalHome)PortableRemoteObject.
	    narrow(env.lookup("ejb/ProductHome"), ProductLocalHome.class);
	return pHome;
    }

    /**
     * Utility method to get the home interface for attribute bean.
     */
    protected AttributeLocalHome getAttributeHome() throws NamingException {
	if(aHome != null) return aHome;

	InitialContext ic = new InitialContext();
	Context env = (Context)ic.lookup("java:comp/env");
	aHome = (AttributeLocalHome)PortableRemoteObject.
	    narrow(env.lookup("ejb/AttributeHome"), AttributeLocalHome.class);
	return aHome;
    }

    /**
     * Makes a new category id.
     */
    private Integer getNextCategoryId() throws CreateException, NamingException, RemoteException {
	InitialContext ic = new InitialContext();
	Context env = (Context) ic.lookup("java:comp/env");
	    
	SequenceHome sh = (SequenceHome)PortableRemoteObject.narrow
	    (env.lookup("ejb/SequenceHome"), SequenceHome.class);
	Sequence s = sh.create();
	return s.getNext("ro.setumsa.sequences.categories");
    }

    // Bean life cycle methods

    public void ejbPassivate() {
	super.ejbPassivate();

	cHome = null;
	pHome = null;
	aHome = null;
    }

    public void ejbActivate() {
	super.ejbActivate();

	cHome = null;
	pHome = null;
	aHome = null;
    }


    // Variables to cache the local homes of data layer beans
    CategoryLocalHome cHome;
    ProductLocalHome pHome;
    AttributeLocalHome aHome;
}
