package ro.kds.erp.biz.setum;

import javax.ejb.SessionContext;
import ro.kds.erp.biz.ResponseBean;
import javax.naming.InitialContext;
import ro.kds.erp.data.ProductLocalHome;
import javax.rmi.PortableRemoteObject;
import ro.kds.erp.data.CompositeProductLocalHome;
import ro.kds.erp.data.ProductLocal;
import org.objectweb.util.monolog.api.BasicLevel;
import ro.kds.erp.data.CompositeProductLocal;
import ro.kds.erp.data.CategoryLocalHome;
import javax.naming.NamingException;
import ro.kds.erp.data.CategoryLocal;
import java.util.TreeMap;
import javax.naming.Context;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import javax.ejb.FinderException;
import java.math.BigDecimal;
import ro.kds.erp.scripting.Script;
import java.rmi.RemoteException;

/**
 * Gestiunea usilor standard. Usile standard sunt produse compuse obtinute
 * din echiparea usilor standard neechipate cu broasca, cilindru, sild, 
 * yalla, vizor.
 *
 *
 * $Id: UsaStandardBean.java,v 1.2 2006/01/09 10:15:49 mihai Exp $
 *
 * @author <a href="mailto:Mihai Giurgeanu@CRIMIRA"></a>
 * @version 1.0
 */
public class UsaStandardBean 
    extends ro.kds.erp.biz.setum.basic.UsaStandardBean {

    
    /**
     * Form data initialization
     */
    public void createNewFormBean() {
	super.createNewFormBean();

	form.setName("");
	form.setCode("");
	
	form.setUsaId(new Integer(0));
	form.setBroascaId(new Integer(0));
	form.setCilindruId(new Integer(0));
	form.setSildId(new Integer(0));
	form.setYallaId(new Integer(0));
	form.setVizorId(new Integer(0));
    }

    /**
     * Load the form field values from the persistance layer.
     * When thid method is called, the <code>id</code> instance
     * variable must hold the value of the primary key of the product
     * that is to be loaded.
     *
     * @throws FinderException if product with the primary key 
     * from <code>id</code> instance variable does not exits.
     */
    public ResponseBean loadFields() throws FinderException {
	ResponseBean r;

	try {
	    InitialContext ic = new InitialContext();
	    Context env = (Context) ic.lookup("java:comp/env");
	    ProductLocalHome ph = (ProductLocalHome)PortableRemoteObject.narrow
		(env.lookup("ejb/ProductHome"), ProductLocalHome.class);
	    CompositeProductLocalHome cph =
		(CompositeProductLocalHome)PortableRemoteObject.narrow
		(env.lookup("ejb/CompositeProductHome"), 
		 CompositeProductLocalHome.class);

	    ProductLocal usa = ph.findByPrimaryKey(id);
	    form.setCode(usa.getCode());
	    form.setName(usa.getName());
	    for(Iterator i = 
		    usa.getCompositeProduct().getComponents().iterator();
		i.hasNext(); ) {
		
		ProductLocal p = (ProductLocal)i.next();
		if(p.getCategory().getId().equals(USA_SIMPLA_ID)) {
		    form.setUsaId(p.getId());
		} else if (p.getCategory().getId().equals(BROASCA_ID)) {
		    form.setBroascaId(p.getId());
		} else if (p.getCategory().getId().equals(CILINDRU_ID)) {
		    form.setCilindruId(p.getId());
		} else if (p.getCategory().getId().equals(SILD_ID)) {
		    form.setSildId(p.getId());
		} else if (p.getCategory().getId().equals(YALLA_ID)) {
		    form.setYallaId(p.getId());
		} else if (p.getCategory().getId().equals(VIZOR_ID)) {
		    form.setVizorId(p.getId());
		} else {
		    logger.log(BasicLevel.WARN, "Product in category: " 
			       + p.getCategory().getName() +
			       " can not be part of this composite product.");
		}
	    }
	    r = new ResponseBean();
	    
	} catch (Exception e) {
	    r = new ResponseBean();
	    r.setCode(1);
	    r.setMessage("Eroare sistem. Nu se poate incarca lista de produse");
	    logger.log(BasicLevel.ERROR, e.getMessage(), e);
	}
	return r;
    }


    /**
     * Save values from the <code>form</code> instance variable into
     * the persistent layer. The <code>id</code> instance variable holds
     * the value of the primary key for the product that should be
     * updated. Ig <code>id</code> is <code>null</code> then a new entity
     * will be created.
     * 
     */
    public ResponseBean saveFormData() {
	ResponseBean r;


	try {


	    InitialContext ic = new InitialContext();
	    Context env = (Context)ic.lookup("java:comp/env");
	    ProductLocalHome ph = (ProductLocalHome)PortableRemoteObject.narrow
		(env.lookup("ejb/ProductHome"), ProductLocalHome.class);
	    CompositeProductLocalHome cph = 
		(CompositeProductLocalHome)PortableRemoteObject.narrow
		(env.lookup("ejb/CompositeProductHome"),
		 CompositeProductLocalHome.class);

	    ProductLocal product;
	    CompositeProductLocal p;
	    if(id == null) {
		// add a new composite product
		CategoryLocalHome ch =
		    (CategoryLocalHome)PortableRemoteObject.narrow
		    (env.lookup("ejb/CategoryHome"), CategoryLocalHome.class);

		product = ph.create();
		product.setCategory(ch.findByPrimaryKey(USA_STD_ID));
		p = cph.create(product.getId());
		product.setCompositeProduct(p);
	    } else {
		// locate existing composite product
		p = cph.findByPrimaryKey(id);
		product = p.getProduct();
		p.getComponents().clear();
	    }

	    product.setName(form.getName());
	    product.setCode(form.getCode());

	    Collection parts = p.getComponents();
	    ProductLocal usa = ph.findByPrimaryKey(form.getUsaId());
	    parts.add(usa);
	    BigDecimal sellPrice = new BigDecimal(0);
	    BigDecimal entryPrice = new BigDecimal(0);
	    BigDecimal price1 = new BigDecimal(0);

	    sellPrice = sellPrice.add(usa.getSellPrice());
	    entryPrice = entryPrice.add(usa.getEntryPrice());
	    price1 = price1.add(usa.getPrice1());

	    if(! form.getBroascaId().equals(new Integer(0))) {
		ProductLocal part = ph.findByPrimaryKey(form.getBroascaId()); 
		parts.add(part);
		sellPrice = sellPrice.add(part.getSellPrice());
		entryPrice = entryPrice.add(part.getEntryPrice());
		price1 = price1.add(part.getPrice1());
	    }
	    if(! form.getCilindruId().equals(new Integer(0))) {
		ProductLocal part = ph.findByPrimaryKey(form.getCilindruId());
		parts.add(part);
		sellPrice = sellPrice.add(part.getSellPrice());
		entryPrice = entryPrice.add(part.getEntryPrice());
		price1 = price1.add(part.getPrice1());
	    }
	    if(! form.getSildId().equals(new Integer(0))) {
		ProductLocal part = ph.findByPrimaryKey(form.getSildId()); 
		parts.add(part);
		sellPrice = sellPrice.add(part.getSellPrice());
		entryPrice = entryPrice.add(part.getEntryPrice());
		price1 = price1.add(part.getPrice1());
	    }
	    if(! form.getYallaId().equals(new Integer(0))) {
		ProductLocal part = ph.findByPrimaryKey(form.getYallaId());
		parts.add(part);
		sellPrice = sellPrice.add(part.getSellPrice());
		entryPrice = entryPrice.add(part.getEntryPrice());
		price1 = price1.add(part.getPrice1());
	    }
	    if(! form.getVizorId().equals(new Integer(0))) {
		ProductLocal part = ph.findByPrimaryKey(form.getVizorId());
		parts.add(part);
		sellPrice = sellPrice.add(part.getSellPrice());
		entryPrice = entryPrice.add(part.getEntryPrice());
		price1 = price1.add(part.getPrice1());
	    }

	    product.setSellPrice(sellPrice);
	    product.setEntryPrice(entryPrice);
	    product.setPrice1(price1);

	    r = new ResponseBean();
	    
	} catch (NamingException e) {
	    r = new ResponseBean();
	    r.setCode(1);
	    r.setMessage("Eroare de configurare a aplicatiei");
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
     * Loads the listing of composite products in this category.
     */
    public ResponseBean loadListing() {
	ResponseBean r;
	 
	try {
	    InitialContext ic = new InitialContext();
	    Context env = (Context)ic.lookup("java:comp/env");
	    CategoryLocalHome ch =
		(CategoryLocalHome)PortableRemoteObject.narrow
		(env.lookup("ejb/CategoryHome"), CategoryLocalHome.class);
	    CategoryLocal c = ch.findByPrimaryKey(USA_STD_ID);
	    
	    Collection products = c.getProducts();
	    r = new ResponseBean();
	    for(Iterator i = products.iterator(); i.hasNext(); ) {
		ProductLocal p = (ProductLocal)i.next();
		CompositeProductLocal cp = p.getCompositeProduct();
		Collection parts = cp.getComponents();
		r.addRecord();
		r.addField("id", cp.getId());
		for(Iterator j=parts.iterator(); j.hasNext();) {
		    ProductLocal part = (ProductLocal)j.next();
		    Integer partCatId = part.getCategory().getId();
		    if(partCatId.equals(USA_SIMPLA_ID)) {
			r.addField("col-usa", part.getName());
		    } else if (partCatId.equals(BROASCA_ID)) {
			r.addField("col-broasca", part.getName());
		    } else if (partCatId.equals(CILINDRU_ID)) {
			r.addField("col-cilindru", part.getName());
		    } else if (partCatId.equals(SILD_ID)) {
			r.addField("col-sild", part.getName());
		    } else if (partCatId.equals(YALLA_ID)) {
			r.addField("col-yalla", part.getName());
		    } else if (partCatId.equals(VIZOR_ID)) {
			r.addField("col-vizor", part.getName());
		    }
		}
	    }
	} catch (NamingException e) {
	    r = new ResponseBean();
	    r.setCode(1);
	    r.setMessage("Eroare la configurarea aplicatiei. Lista de produse nu poate fi incarcata.");
	    logger.log(BasicLevel.ERROR, e.getMessage(), e);
	} catch (Exception e) {
	    r = new ResponseBean();
	    r.setCode(3);
	    r.setMessage("Eroare la incarcarea listei. Lista de produse nu poate fi incarcata.");
	    logger.log(BasicLevel.ERROR, e.getMessage(), e);
	}

	return r;
    }


    /**
     * Recalculate the price for products in this category.
     */
    public void recalculatePrices() {

	// Get the list of all products in this category, loads them
	// one by one and save them back after price calculation.

	try {
	    InitialContext ic = new InitialContext();
	    Context env = (Context)ic.lookup("java:comp/env");
	    CategoryLocalHome ch =
		(CategoryLocalHome)PortableRemoteObject.narrow
		(env.lookup("ejb/CategoryHome"), CategoryLocalHome.class);
	    CategoryLocal c = ch.findByPrimaryKey(USA_STD_ID);
	    
	    Collection products = c.getProducts();

	    for(Iterator i = products.iterator(); i.hasNext(); ) {
		ProductLocal p = (ProductLocal)i.next();
		logger.log(BasicLevel.DEBUG, "Recalculating for product id: "
			   + p.getId());
		this.loadFormData(p.getId());
		this.saveFormData(); // makes the price calculation also
	    }
	} catch (NamingException e) {
	    logger.log(BasicLevel.ERROR, e.getMessage(), e);
	} catch (Exception e) {
	    logger.log(BasicLevel.ERROR, e.getMessage(), e);
	}

    }

    /**
     * CategoryId
     */
    private Integer USA_STD_ID;
    /**
     * CategoryId
     */
    private Integer USA_SIMPLA_ID;
    /**
     * CategoryId
     */
    private Integer BROASCA_ID;
    /**
     * CategoryId
     */
    private Integer CILINDRU_ID;
    /**
     * CategoryId
     */
    private Integer SILD_ID;
    /**
     * CategoryId
     */
    private Integer YALLA_ID;
    /**
     * CategoryId
     */
    private Integer VIZOR_ID;

    /**
     * Reads the id's of product categories to be assembled into this composite
     * product.
     */
    private void initCategoryIds() {
	try {
	    Context it = new InitialContext();
	
	    USA_STD_ID = (Integer)it
		.lookup("java:comp/env/setum/category/usaStdId");
	    USA_SIMPLA_ID = (Integer)it
		.lookup("java:comp/env/setum/category/usaSimplaId");
	    BROASCA_ID = (Integer)it
		.lookup("java:comp/env/setum/category/broascaId");
	    CILINDRU_ID = (Integer)it
		.lookup("java:comp/env/setum/category/cilindruId");
	    SILD_ID = (Integer)it
		.lookup("java:comp/env/setum/category/sildId");
	    YALLA_ID = (Integer)it
		.lookup("java:comp/env/setum/category/yallaId");
	    VIZOR_ID = (Integer)it
		.lookup("java:comp/env/setum/category/vizorId");
	} catch (NamingException e) {
	    logger.log(BasicLevel.ERROR,
		       "Category ids were not loaded", e);
	}
    }

    /**
     * Add bean initialization code.
     *
     * @param sessionContext a <code>SessionContext</code> value
     */
    public void setSessionContext(SessionContext sessionContext) {
	super.setSessionContext(sessionContext);
	
	initCategoryIds();
    }

    /**
     * Builds a value list using all products in a category.
     */
    private Map makeProductsValueList(Integer catId) {
	TreeMap vl = new TreeMap();
	vl.put("-", "0");

	try {
	    InitialContext ic = new InitialContext();
	    Context env = (Context) ic.lookup("java:comp/env");
	    CategoryLocalHome ch = 
		(CategoryLocalHome)PortableRemoteObject.narrow
		(env.lookup("ejb/CategoryHome"), CategoryLocalHome.class);
	    CategoryLocal c = ch.findByPrimaryKey(catId);
	    for(Iterator i = c.getProducts().iterator(); i.hasNext(); ) {
		ProductLocal p = (ProductLocal)i.next();
		vl.put(p.getName(), p.getId());
	    }
	} catch (Exception e) {
	    logger.log(BasicLevel.ERROR, "Can not get value list for categrory id " + catId, e);
	}
	
	return vl;
    }



    /**
     * Add the value lists to the response bean. This method is called
     * by the <code>super.copyFieldsToResponse(...)</code> method.
     */
    protected void loadValueLists(ResponseBean r) {
	r.addValueList("usaId", makeProductsValueList(USA_SIMPLA_ID));
	r.addValueList("broascaId", makeProductsValueList(BROASCA_ID));
	r.addValueList("cilindruId", makeProductsValueList(CILINDRU_ID));
	r.addValueList("sildId", makeProductsValueList(SILD_ID));
	r.addValueList("yallaId", makeProductsValueList(YALLA_ID));
	r.addValueList("vizorId", makeProductsValueList(VIZOR_ID));
    }


    /**
     * Provide to the script the codes and the names of the selected
     * componewnts. The script is responsible to derive the code and the
     * name of the new product.
     */
    protected void addFieldsToScript(Script s) {

	super.addFieldsToScript(s);
	try {
	    InitialContext ic = new InitialContext();
	    Context env = (Context)ic.lookup("java:comp/env");
	    ProductLocalHome ph = (ProductLocalHome)PortableRemoteObject.narrow
		(env.lookup("ejb/ProductHome"), ProductLocalHome.class);

	    try {
		ProductLocal p = ph.findByPrimaryKey(form.getUsaId());
		s.setVar("usaName", p.getName());
		s.setVar("usaCode", p.getCode());
	    } catch (FinderException e) {
		logger.log(BasicLevel.DEBUG,
			   "product does not exists; id " + form.getUsaId());
	    } catch (Exception e) {
		logger.log(BasicLevel.WARN,
			   "Can not add usaName and/or usaCode to the script",
			   e);
	    }
	    try {
		ProductLocal p = ph.findByPrimaryKey(form.getBroascaId());
		s.setVar("broascaName", p.getName());
		s.setVar("broascaCode", p.getCode());
	    } catch (FinderException e) {
		logger.log(BasicLevel.DEBUG,
			   "product does not exists; id " + form.getBroascaId());
	    } catch (Exception e) {
		logger.log(BasicLevel.WARN,
			   "Can not add broascaName and/or broascaCode to the script", e);
	    }
	    try {
		ProductLocal p = ph.findByPrimaryKey(form.getSildId());
		s.setVar("sildName", p.getName());
		s.setVar("sildCode", p.getCode());
	    } catch (FinderException e) {
		logger.log(BasicLevel.DEBUG,
			   "product does not exists; id " + form.getSildId());
	    } catch (Exception e) {
		logger.log(BasicLevel.WARN,
			   "Can not add sildName and/or sildCode to the script",
			   e);
	    }
	    try {
		ProductLocal p = ph.findByPrimaryKey(form.getYallaId());
		s.setVar("yallaName", p.getName());
		s.setVar("yallaCode", p.getCode());
	    } catch (FinderException e) {
		logger.log(BasicLevel.DEBUG,
			   "product does not exists; id " + form.getYallaId());
	    } catch (Exception e) {
		logger.log(BasicLevel.WARN,
			   "Can not add yallaName and/or yallaCod to the script",e);
	    }
	    try {
		ProductLocal p = ph.findByPrimaryKey(form.getCilindruId());
		s.setVar("cilindruName", p.getName());
		s.setVar("cilindruCode", p.getCode());
	    } catch (FinderException e) {
		logger.log(BasicLevel.DEBUG,
			   "product does not exists; id " + form.getCilindruId());
	    } catch (Exception e) {
		logger.log(BasicLevel.WARN,
			   "Can not add cilindruName and/or cilindruCod to the script", e);
	    }
	    try {
		ProductLocal p = ph.findByPrimaryKey(form.getVizorId());
		s.setVar("vizorName", p.getName());
		s.setVar("vizorCode", p.getCode());
	    } catch (FinderException e) {
		logger.log(BasicLevel.DEBUG,
			   "product does not exists; id " + form.getVizorId());
	    } catch (Exception e) {
		logger.log(BasicLevel.WARN,
			   "Can not add vizorName and/or vizorCode to the script", e);
	    }
	} catch (NamingException e) {
	    logger.log(BasicLevel.WARN, e);
	}
    }
}


