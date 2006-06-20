package ro.kds.erp.biz.setum;

import javax.ejb.FinderException;
import ro.kds.erp.biz.ResponseBean;
import ro.kds.erp.biz.ResponseBean;
import ro.kds.erp.biz.ResponseBean;
import ro.kds.erp.data.CategoryLocal;
import java.math.BigDecimal;
import javax.naming.InitialContext;
import ro.kds.erp.data.ProductLocalHome;
import javax.rmi.PortableRemoteObject;
import ro.kds.erp.data.ProductLocal;
import javax.naming.NamingException;
import org.objectweb.util.monolog.api.BasicLevel;
import ro.kds.erp.data.CategoryLocalHome;
import java.util.LinkedHashMap;
import javax.naming.Context;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.Collections;
import ro.kds.erp.data.AttributeLocal;
import java.util.List;
import java.util.Comparator;
import java.util.ArrayList;

import java.util.List;

import java.util.Comparator;

import java.util.ArrayList;
import ro.kds.erp.data.AttributeLocalHome;

/**
 * Bussines logic for recording and managing products of type "sisteme".
 *
 * It extends the generated class ro.kds.erp.biz.setum.basic.SistemBean,
 * that allows scripting possibilities to computing calculated fields and
 * changeing fields values.
 *
 *
 * Created: Sat Oct 29 21:05:29 2005
 *
 * @author <a href="mailto:Mihai Giurgeanu@CRIMIRA"></a>
 * @version 1.0
 */
public class SistemBean extends ro.kds.erp.biz.setum.basic.SistemBean {

    /**
     * Initializes the fields of the <code>form</code> instance variable.
     *
     */
    public final void createNewFormBean() {
	super.createNewFormBean();

	form.setCategoryId(new Integer(0));
	form.setCode("");
	form.setName("");
	form.setEntryPrice(new BigDecimal(0));
	form.setSellPrice(new BigDecimal(0));
	form.setPartPrice(new BigDecimal(0));
	form.setLaborPrice(new BigDecimal(0));
	form.setRelativeGainSP(new Double(0));
	form.setAbsoluteGainSP(new BigDecimal(0));
	form.setRelativeGainPP(new Double(0));
	form.setAbsoluteGainPP(new BigDecimal(0));
    }

    /**
     * Load the form field values from the persistent layer. It also sets
     * the values in the value lists.
     */
    public ResponseBean loadFields() throws FinderException {
	ResponseBean r;
	try {
	    InitialContext ic = new InitialContext();
	    Context env = (Context)ic.lookup("java:comp/env");
	    ProductLocalHome ph = (ProductLocalHome)PortableRemoteObject.narrow
		(env.lookup("ejb/ProductHome"), ProductLocalHome.class);
	    
	    ProductLocal p = ph.findByPrimaryKey(id);
	    
	    form.setCategoryId(p.getCategory().getId());
	    form.setCode(p.getCode());
	    form.setName(p.getName());
	    form.setEntryPrice(p.getEntryPrice());
	    form.setSellPrice(p.getSellPrice());
	    form.setPartPrice(p.getPrice1());
	    form.setLaborPrice(p.getPrice2());

	    Map attributes = p.getAttributesMap();
	    AttributeLocal a;
	    if((a = (AttributeLocal)attributes.get("L")) != null)
		form.setL(a.getDoubleValue());
	    
	    if((a = (AttributeLocal)attributes.get("H")) != null)
		form.setH(a.getDoubleValue());
	    
	    r = new ResponseBean();

	    // the fields will be added to the response by the 
	    // super's calling method (loadFormData)

	    
	    
	} catch (NamingException e) {
	    r = new ResponseBean();
	    r.setCode(1);
	    r.setMessage("Eroare de configurare a server-ului de business logic");
	    logger.log(BasicLevel.ERROR, e.getMessage(), e);
	}

	return r;
    }

    /**
     * Saves the values that are stored in the fields of the
     * <code>form</code> instance variable into the persistent layer,
     * by updating the entity identified by the value in the <code>id</code>
     * or, if the <code>id</code> is <code>null</code>, creates a new entity.
     *
     * @return a <code>ResponseBean</code> value
     */
    public ResponseBean saveFormData() {
	ResponseBean r;
	try {
	    InitialContext ic = new InitialContext();
	    Context env = (Context) ic.lookup("java:comp/env");
	    ProductLocalHome ph = (ProductLocalHome)PortableRemoteObject.narrow
		(env.lookup("ejb/ProductHome"), ProductLocalHome.class);
	    CategoryLocalHome ch = 
		(CategoryLocalHome)PortableRemoteObject.narrow
		(env.lookup("ejb/CategoryHome"), CategoryLocalHome.class);

	    ProductLocal p = (id == null)?ph.create():ph.findByPrimaryKey(id);
	    
	    p.setCategory(ch.findByPrimaryKey(form.getCategoryId()));
	    p.setCode(form.getCode());
	    p.setName(form.getName());
	    p.setEntryPrice(form.getEntryPrice());
	    p.setSellPrice(form.getSellPrice());
	    p.setPrice1(form.getPartPrice());
	    p.setPrice2(form.getLaborPrice());

	    AttributeLocalHome ah = (AttributeLocalHome)PortableRemoteObject.
		narrow(env.lookup("ejb/AttributeHome"), AttributeLocalHome.class);
	    ArrayList attributes = new ArrayList();

	    attributes.add(ah.create("L", form.getL()));
	    attributes.add(ah.create("H", form.getH()));
	    
	    p.setAttributes(attributes);
	       
	    r = new ResponseBean();
	} catch (NamingException e) {
	    r = new ResponseBean();
	    r.setCode(1);
	    r.setMessage("Eroare de configurare a server-ului de business logic.");
	    logger.log(BasicLevel.ERROR, e.getMessage(), e);
	} catch (Exception e) {
	    r = new ResponseBean();
	    r.setCode(3);
	    r.setMessage("Eroare la salvarea valorilor in baza de date.");
	    logger.log(BasicLevel.ERROR, e.getMessage(), e);
	}
	
	return r;
    }

    /**
     * Returns a response bean containing the list of products. It lists all
     * the products in the subcategories of the category identified by
     * the environment entry <code>Integer rootCategoryId</code>.
     */
    public ResponseBean loadListing() {
	ResponseBean r;
	
	try {
	    InitialContext ic = new InitialContext();
	    Context env = (Context)ic.lookup("java:comp/env");
	    Integer rootCategoryId = (Integer)env.lookup("rootCategoryId");
	    CategoryLocalHome ch = 
		(CategoryLocalHome)PortableRemoteObject.narrow
		(env.lookup("ejb/CategoryHome"), CategoryLocalHome.class);

	    r = new ResponseBean();

	    CategoryLocal rootCategory = ch.findByPrimaryKey(rootCategoryId);
	    List categs = new ArrayList(rootCategory.getSubCategories());
	    Collections.sort(categs,
			     new Comparator() {
				 public int compare(Object o1, Object o2) {
				     return ((CategoryLocal)o1).getName()
					 .compareToIgnoreCase(((CategoryLocal)o2).getName());
				 }

				 public boolean equals(Object obj) {
				     return this.equals(obj);
				 }
			     });
				 
	    for(Iterator i = categs.iterator(); i.hasNext(); ) {
		CategoryLocal cat = (CategoryLocal)i.next();
		List products = new ArrayList(cat.getProducts());

		Collections.sort(products,
				 new Comparator() {
				     public int compare(Object o1, Object o2) {
					 return ((ProductLocal)o1).getName()
					     .compareToIgnoreCase(((ProductLocal)o2).getName());
				     }
				     public boolean equals(Object obj) {
					 return this.equals(obj);
				     }
				 });

		for(Iterator j = products.iterator(); j.hasNext();) {
		    ProductLocal p = (ProductLocal)j.next();
		    
		    r.addRecord();
		    r.addField("id", p.getId());
		    r.addField("col-code", p.getCode());
		    r.addField("col-category", cat.getName());
		    r.addField("col-name", p.getName());
		    r.addField("col-entryPrice", p.getEntryPrice());
		    r.addField("col-sellPrice", p.getSellPrice());
		    r.addField("col-partPrice", p.getPrice1());

		}

	    }
	} catch (NamingException e) {
	    r = new ResponseBean();
	    r.setCode(1);
	    r.setMessage("Eoare de configurare. Lista de produse nu poate fi incarcata.");
	    logger.log(BasicLevel.ERROR, 
		       "Error in loading the list of products", e);
	} catch (Exception e) {
	    r = new ResponseBean();
	    r.setCode(3);
	    r.setMessage("Eroare sistem. Lista de produse nu poate fi incarcata.");
	    logger.log(BasicLevel.ERROR, 
		       "Error in loading the list of products", e);
	}

	return r;
    }

    /**
     * Builds the value list containing the allowed categories. The
     * method will get all the subcategories of the category identified
     * by environment entry <code>Integer</code> <code>rootCategoryId</code>.
     *
     * @return a <code>Map</code> with the value-list's labels as keys
     * and the value-list's values as values of the <code>Map</code>.
     */
    protected Map categoryIdValueList() {
	TreeMap categoryIdVL = new TreeMap(); // using TreeMap, the list will be sorted
	try {
	    InitialContext ic = new InitialContext();
	    Context env = (Context)ic.lookup("java:comp/env");
	    
	    Integer rootCategoryId = (Integer)env.lookup("rootCategoryId");
	    CategoryLocalHome ch = 
		(CategoryLocalHome)PortableRemoteObject.narrow
		(env.lookup("ejb/CategoryHome"), CategoryLocalHome.class);
	    CategoryLocal rootCategory = ch.findByPrimaryKey(rootCategoryId);
	    Collection categs = rootCategory.getSubCategories();
	    for (Iterator i=categs.iterator(); i.hasNext();) {
		CategoryLocal cat = (CategoryLocal)i.next();
		categoryIdVL.put(cat.getName(), cat.getId());
	    }
	} catch (Exception e) {
	    logger.log(BasicLevel.ERROR,
		       "Can not build the value list for categoryId", e);
	}
	return categoryIdVL;
    }

    /**
     * Add the value lists to the response bean. This method is called
     * by the <code>super.copyFieldsToResponse(...)</code> method.
     */
    protected void loadValueLists(ResponseBean r) {
	r.addValueList("categoryId", categoryIdValueList());
    }
}
