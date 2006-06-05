package ro.kds.erp.biz.setum;

import ro.kds.erp.biz.setum.basic.FereastraBean;
import ro.kds.erp.biz.ResponseBean;
import javax.ejb.FinderException;
import ro.kds.erp.biz.ResponseBean;
import ro.kds.erp.biz.ResponseBean;
import javax.naming.InitialContext;
import ro.kds.erp.data.ProductLocalHome;
import javax.rmi.PortableRemoteObject;
import ro.kds.erp.data.ProductLocal;
import ro.kds.erp.data.AttributeLocalHome;
import javax.naming.NamingException;
import org.objectweb.util.monolog.api.BasicLevel;
import ro.kds.erp.data.AttributeLocal;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import ro.kds.erp.data.CategoryLocalHome;
import javax.naming.Context;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.ArrayList;
import ro.kds.erp.data.CategoryLocal;

/**
 * Specific business rules implementation of the FerastraEJB session bean.
 *
 *
 * Created: Sun Nov 20 16:22:08 2005
 *
 * @author <a href="mailto:Mihai Giurgeanu@CRIMIRA"></a>
 * @version 1.0
 */
public class FereastraBizBean extends FereastraBean {

    /**
     * Saves the data gathered in the form bean into there
     * persistent layer. The current object is identified by
     * the <code>id</code> instance variable. If this is <code>null</code>
     * then a new object will be created.
     *
     * @return a <code>ResponseBean</code> value
     */
    public ResponseBean saveFormData() {
	ResponseBean r;

	try {
	    
	    InitialContext ic = new InitialContext();
	    Context env = (Context) ic.lookup("java:comp/env");
	    ProductLocalHome ph = (ProductLocalHome)PortableRemoteObject.
		narrow(env.lookup("ejb/ProductHome"), ProductLocalHome.class);
	    ProductLocal p;

	    if(id == null) {
		CategoryLocalHome ch = 
		    (CategoryLocalHome)PortableRemoteObject.narrow
		    (env.lookup("ejb/CategoryHome"), CategoryLocalHome.class);
		Integer categoryId = (Integer)env.lookup("categoryId");
		p = ph.create();
		id = p.getId();
		p.setCategory(ch.findByPrimaryKey(categoryId));
		p.setName("Fereastra " + id);
		p.setCode("F" + id);
	    } else {
		p = ph.findByPrimaryKey(id);
	    }

	    p.setDescription("Fereastra " + form.getLf() + "x" + form.getHf());

	    AttributeLocalHome ah = (AttributeLocalHome)PortableRemoteObject.
		narrow(env.lookup("ejb/AttributeHome"), 
		       AttributeLocalHome.class);
	    
	    ArrayList attributes = new ArrayList();

	    attributes.add(ah.create("canat", form.getCanat()));
	    attributes.add(ah.create("lf", form.getLf()));
	    attributes.add(ah.create("hf", form.getHf()));

	    p.setSellPrice(form.getSellPrice());
	    p.setEntryPrice(form.getEntryPrice());
	    p.setPrice1(form.getPrice1());

	    attributes.add(ah.create("pozitionare1", form.getPozitionare1()));
	    attributes.add(ah.create("pozitionare2", form.getPozitionare2()));
	    attributes.add(ah.create("pozitionare3", form.getPozitionare3()));
	    attributes.add(ah.create("deschidere", form.getDeschidere()));
	    attributes.add(ah.create("sensDeschidere", form.getSensDeschidere()));
	    attributes.add(ah.create("pozitionareBalamale",
				     form.getPozitionareBalamale()));
	    attributes.add(ah.create("componenta", form.getComponenta()));
	    attributes.add(ah.create("tipComponenta", form.getTipComponenta()));
	    attributes.add(ah.create("tipGeam", form.getTipGeam()));
	    attributes.add(ah.create("quantity", form.getQuantity()));


	    try {
		attributes.add(ah.create("geamSimplu", 
					 ph.findByPrimaryKey(form.getGeamSimpluId())));
	    } catch (FinderException ignore) {}
	    try {
		attributes.add(ah.create("geamTermopan",
					 ph.findByPrimaryKey(form.getGeamTermopanId())));
	    } catch (FinderException ignore) {}

	    attributes.add(ah.create("tipGrilaj", form.getTipGrilaj()));
	    try {
		attributes.add(ah.create("grilajStas", 
					 ph.findByPrimaryKey(form.getGrilajStasId())));
	    } catch (FinderException ignore) {}

	    attributes.add(ah.create("valoareGrilajAtipic", 
				     form.getValoareGrilajAtipic()));
// 	    attributes.add(ah.create("tipTabla", form.getTipTabla()));
// 	    try {
// 		attributes.add(ah.create("tabla", 
// 					 ph.findByPrimaryKey(form.getTablaId())));
// 	    } catch (FinderException ignore) {}


	    
	    attributes.add(ah.create("businessCategory", form.getBusinessCategory()));

	    p.setAttributes(attributes);
	    

	    r = validate();
	} catch (NamingException e) {
	    r = ResponseBean.ERR_CONFIG_NAMING;
	    logger.log(BasicLevel.ERROR, "Error saving object id "  + this.id, 
		       e);
	    ejbContext.setRollbackOnly();
	} catch (Exception e) {
	    r = ResponseBean.ERR_UNEXPECTED;
	    logger.log(BasicLevel.ERROR, "Error saving object id "  + this.id, 
		       e);
	    ejbContext.setRollbackOnly();
	}

	return r;
    }

    /**
     * Load the value of the form bean fields from the persistence layer.
     *
     * @return a <code>ResponseBean</code> value
     * @exception FinderException if an error occurs
     */
    public ResponseBean loadFields() throws FinderException {
	ResponseBean r;

	logger.log(BasicLevel.DEBUG, "Loading product id " + this.id);
	try {
	    InitialContext ic = new InitialContext();
	    Context env = (Context) ic.lookup("java:comp/env");
	
	    ProductLocalHome ph = (ProductLocalHome)PortableRemoteObject.narrow
		(env.lookup("ejb/ProductHome"), ProductLocalHome.class);
	    ProductLocal p = ph.findByPrimaryKey(this.id);
	    form.setSellPrice(p.getSellPrice());
	    form.setEntryPrice(p.getEntryPrice());
	    form.setPrice1(p.getPrice1());
	    
	    Map attributes = p.getAttributesMap();
	    AttributeLocal a;
	    if((a = (AttributeLocal)attributes.get("canat")) != null)
		form.setCanat(a.getIntValue());
	    
	    if((a = (AttributeLocal)attributes.get("lf")) != null)
		form.setLf(a.getDoubleValue());

	    if((a = (AttributeLocal)attributes.get("hf")) != null)
		form.setHf(a.getDoubleValue());

	    if((a = (AttributeLocal)attributes.get("pozitionare1")) != null)
		form.setPozitionare1(a.getStringValue());

	    if((a = (AttributeLocal)attributes.get("pozitionare2")) != null)
		form.setPozitionare2(a.getStringValue());

	    if((a = (AttributeLocal)attributes.get("pozitionare3")) != null)
		form.setPozitionare3(a.getStringValue());
	    
	    if((a = (AttributeLocal)attributes.get("deschidere")) != null)
		form.setDeschidere(a.getIntValue());
	    
	    if((a = (AttributeLocal)attributes.get("sensDeschidere")) != null)
		form.setSensDeschidere(a.getIntValue());
	    
	    if((a = (AttributeLocal)attributes.get("pozitionareBalamale")) != null)
		form.setPozitionareBalamale(a.getIntValue());

	    if((a = (AttributeLocal)attributes.get("componenta")) != null)
		form.setComponenta(a.getIntValue());

	    if((a = (AttributeLocal)attributes.get("tipComponenta")) != null)
		form.setTipComponenta(a.getIntValue());

	    if((a = (AttributeLocal)attributes.get("tipGeam")) != null)
		form.setTipGeam(a.getIntValue());
	    
	    if((a = (AttributeLocal)attributes.get("geamSimplu")) != null)
		form.setGeamSimpluId(a.getProduct().getId());
	    
	    if((a = (AttributeLocal)attributes.get("geamTermopan")) != null)
		form.setGeamTermopanId(a.getProduct().getId());

	    if((a = (AttributeLocal)attributes.get("tipGrilaj")) != null)
		form.setTipGrilaj(a.getIntValue());

	    if((a = (AttributeLocal)attributes.get("grilajStas")) != null)
		form.setGrilajStasId(a.getProduct().getId());

	    if((a = (AttributeLocal)attributes.get("valoareGrilajAtipic")) != null)
		form.setValoareGrilajAtipic(a.getDecimalValue());

// 	    if((a = (AttributeLocal)attributes.get("tipTabla")) != null)
// 		form.setTipTabla(a.getIntValue());

// 	    if((a = (AttributeLocal)attributes.get("tabla")) != null)
// 		form.setTablaId(a.getProduct().getId());

	    if((a = (AttributeLocal)attributes.get("quantity")) != null)
		form.setQuantity(a.getIntValue());

	    if((a = (AttributeLocal)attributes.get("businessCategory")) != null)
		form.setBusinessCategory(a.getStringValue());

	    r = new ResponseBean();

	} catch (NamingException e) {
	    logger.log(BasicLevel.ERROR, "Error loading the product " + id, e);
	    r = ResponseBean.ERR_CONFIG_NAMING;
	} catch (FinderException e) {
	    logger.log(BasicLevel.ERROR, "Error loading the product " + id, e);
	    throw(e);
	} catch (Exception e) {
	    logger.log(BasicLevel.ERROR, "Error loading the product " + id, e);
	    r = ResponseBean.ERR_UNEXPECTED;
	}

	return r;
    }

    /**
     * Initialize the fields of the form bean.
     *
     */
    public void createNewFormBean() {
	super.createNewFormBean();

	form.setDeschidere(new Integer(1));
	form.setTipComponenta(new Integer(1));
	form.setTipGeam(new Integer(1));
	form.setTipGrilaj(new Integer(1));
    }

    /**
     * Adds to the given response bean all the value lists
     * to be used on this form.
     *
     * @param r a <code>ResponseBean</code> value
     */
    public void loadValueLists(ResponseBean r) {
	Map vl;
	
	r.addValueList("canat", ValueLists.makeStdValueList(11050));
	r.addValueList("sensDeschidere", ValueLists.makeStdValueList(11060));
	r.addValueList("pozitionareBalamale", ValueLists.makeStdValueList(11065));
	r.addValueList("componenta", ValueLists.makeStdValueList(11070));
	r.addValueList("tipGrilaj", ValueLists.makeStdValueList(11075));
// 	r.addValueList("tipTabla", ValueLists.makeStdValueList(11080));

	// geamSimpluId
	vl = new LinkedHashMap();
	try {
	    InitialContext ic = new InitialContext();
	    Context env = (Context) ic.lookup("java:comp/env");
	    Integer categoryId = (Integer)env.lookup("geamuriCategoryId");
	    CategoryLocalHome ch = 
		(CategoryLocalHome)PortableRemoteObject.narrow
		(env.lookup("ejb/CategoryHome"), CategoryLocalHome.class);
	    CategoryLocal categ = ch.findByPrimaryKey(categoryId);
	    Collection products = categ.getProducts();
	    for(Iterator i = products.iterator(); i.hasNext(); ) {
		ProductLocal p = (ProductLocal)i.next();
		vl.put(p.getName(), p.getId());
	    }
	} catch (Exception e) {
	    logger.log(BasicLevel.ERROR, 
		       "Can not build vl for geamSimpluId", e);
	}
	r.addValueList("geamSimpluId", vl);

	// geamTermopanId
	vl = new LinkedHashMap();
	try {
	    InitialContext ic = new InitialContext();
	    Context env = (Context) ic.lookup("java:comp/env");
	    Integer categoryId = (Integer)env.lookup("termopaneCategoryId");
	    CategoryLocalHome ch = 
		(CategoryLocalHome)PortableRemoteObject.narrow
		(env.lookup("ejb/CategoryHome"), CategoryLocalHome.class);
	    CategoryLocal categ = ch.findByPrimaryKey(categoryId);
	    Collection products = categ.getProducts();
	    for(Iterator i = products.iterator(); i.hasNext(); ) {
		ProductLocal p = (ProductLocal)i.next();
		vl.put(p.getName(), p.getId());
	    }
	} catch (Exception e) {
	    logger.log(BasicLevel.ERROR, 
		       "Can not build vl for geamTermopanId", e);
	}
	r.addValueList("geamTermopanId", vl);


	// grilajStasId
	vl = new LinkedHashMap();
	try {
	    InitialContext ic = new InitialContext();
	    Context env = (Context) ic.lookup("java:comp/env");
	    Integer categoryId = (Integer)env.lookup("grilajeCategoryId");
	    CategoryLocalHome ch = 
		(CategoryLocalHome)PortableRemoteObject.narrow
		(env.lookup("ejb/CategoryHome"), CategoryLocalHome.class);
	    CategoryLocal categ = ch.findByPrimaryKey(categoryId);
	    Collection products = categ.getProducts();
	    for(Iterator i = products.iterator(); i.hasNext(); ) {
		ProductLocal p = (ProductLocal)i.next();
		vl.put(p.getName(), p.getId());
	    }
	} catch (Exception e) {
	    logger.log(BasicLevel.ERROR, 
		       "Can not build vl for geamTermopanId", e);
	}
	r.addValueList("grilajStasId", vl);
	
	
// 	// tablaId
// 	vl = new LinkedHashMap();
// 	try {
// 	    InitialContext ic = new InitialContext();
// 	    Context env = (Context) ic.lookup("java:comp/env");
// 	    Integer categoryId = (Integer)env.lookup("tablaCategoryId");
// 	    CategoryLocalHome ch = 
// 		(CategoryLocalHome)PortableRemoteObject.narrow
// 		(env.lookup("ejb/CategoryHome"), CategoryLocalHome.class);
// 	    CategoryLocal categ = ch.findByPrimaryKey(categoryId);
// 	    Collection products = categ.getProducts();
// 	    for(Iterator i = products.iterator(); i.hasNext(); ) {
// 		ProductLocal p = (ProductLocal)i.next();
// 		vl.put(p.getName(), p.getId());
// 	    }
// 	} catch (Exception e) {
// 	    logger.log(BasicLevel.ERROR, 
// 		       "Can not build vl for geamTermopanId", e);
// 	}
// 	r.addValueList("tablaId", vl);
	
    }

}
