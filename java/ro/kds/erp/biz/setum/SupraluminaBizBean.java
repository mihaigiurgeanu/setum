package ro.kds.erp.biz.setum;

import ro.kds.erp.biz.setum.basic.SupraluminaBean;
import ro.kds.erp.biz.ResponseBean;
import javax.naming.InitialContext;
import ro.kds.erp.data.ProductLocalHome;
import javax.rmi.PortableRemoteObject;
import ro.kds.erp.data.ProductLocal;
import ro.kds.erp.data.CategoryLocalHome;
import ro.kds.erp.data.AttributeLocalHome;
import javax.ejb.FinderException;
import javax.naming.NamingException;
import org.objectweb.util.monolog.api.BasicLevel;
import ro.kds.erp.data.AttributeLocal;
import java.util.LinkedHashMap;
import ro.kds.erp.data.CategoryLocal;
import javax.naming.Context;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.ArrayList;

/**
 * Describe class SupraluminaBizBean here.
 *
 *
 * Created: Tue Jun 13 02:44:07 2006
 *
 * @author <a href="mailto:mihai@cris.kds.ro">Mihai Giurgeanu</a>
 * @version 1.0
 */
public class SupraluminaBizBean extends SupraluminaBean {

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
		p.setName("Supralumina " + id);
		p.setCode("S" + id);
	    } else {
		p = ph.findByPrimaryKey(id);
	    }

	    p.setDescription("Supralumina " + form.getLs() + "x" + form.getHs());

	    AttributeLocalHome ah = (AttributeLocalHome)PortableRemoteObject.
		narrow(env.lookup("ejb/AttributeHome"), 
		       AttributeLocalHome.class);
	    
	    ArrayList attributes = new ArrayList();

	    attributes.add(ah.create("tip", form.getTip()));
	    attributes.add(ah.create("ls", form.getLs()));
	    attributes.add(ah.create("hs", form.getHs()));
	    attributes.add(ah.create("cells", form.getCells()));

	    p.setSellPrice(form.getSellPrice());
	    p.setEntryPrice(form.getEntryPrice());
	    p.setPrice1(form.getPrice1());

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
 	    attributes.add(ah.create("tipTabla", form.getTipTabla()));
 	    try {
 		attributes.add(ah.create("tabla", 
 					 ph.findByPrimaryKey(form.getTablaId())));
 	    } catch (FinderException ignore) {}


	    
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

	    if((a = (AttributeLocal)attributes.get("tip")) != null)
		form.setTip(a.getIntValue());

	    if((a = (AttributeLocal)attributes.get("ls")) != null)
		form.setLs(a.getDoubleValue());

	    if((a = (AttributeLocal)attributes.get("hs")) != null)
		form.setHs(a.getDoubleValue());

	    if((a = (AttributeLocal)attributes.get("cells")) != null)
		form.setCells(a.getIntValue());
	    
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

 	    if((a = (AttributeLocal)attributes.get("tipTabla")) != null)
 		form.setTipTabla(a.getIntValue());

 	    if((a = (AttributeLocal)attributes.get("tabla")) != null)
 		form.setTablaId(a.getProduct().getId());

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
	form.setComponenta(new Integer(1));
	form.setTipGeam(new Integer(1));
	form.setTipGrilaj(new Integer(1));
	form.setTip(new Integer(1));
	form.setCells(new Integer(1));
	form.setQuantity(new Integer(1));
    }

    /**
     * Adds to the given response bean all the value lists
     * to be used on this form.
     *
     * @param r a <code>ResponseBean</code> value
     */
    public void loadValueLists(ResponseBean r) {
	Map vl;
	
	r.addValueList("sensDeschidere", ValueLists.makeStdValueList(11060));
	r.addValueList("pozitionareBalamale", ValueLists.makeStdValueList(11065));
	r.addValueList("componenta", ValueLists.makeStdValueList(11070));
	r.addValueList("tipGrilaj", ValueLists.makeStdValueList(11075));
 	r.addValueList("tipTabla", ValueLists.makeStdValueList(11080));
	r.addValueList("standard", ValueLists.makeStdValueList(11090));
	r.addValueList("tip", ValueLists.makeStdValueList(11095));

	r.addValueList("geamSimpluId", ValueLists.makeVLForCategoryId(new Integer(11086)));
	r.addValueList("geamTermopanId", ValueLists.makeVLForCategoryId(new Integer(11087)));
	r.addValueList("grilajStasId", ValueLists.makeVLForCategoryId(new Integer(11077)));
	r.addValueList("tablaId", ValueLists.makeVLForCategoryId(new Integer(11081)));

	
    }

}
