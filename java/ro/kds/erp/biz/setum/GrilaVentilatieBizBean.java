package ro.kds.erp.biz.setum;

import ro.kds.erp.biz.setum.basic.GrilaVentilatieBean;
import ro.kds.erp.biz.ResponseBean;
import javax.ejb.FinderException;
import ro.kds.erp.biz.ResponseBean;
import javax.naming.InitialContext;
import ro.kds.erp.data.ProductLocalHome;
import javax.rmi.PortableRemoteObject;
import ro.kds.erp.data.ProductLocal;
import ro.kds.erp.data.CategoryLocalHome;
import ro.kds.erp.data.AttributeLocalHome;
import javax.naming.NamingException;
import org.objectweb.util.monolog.api.BasicLevel;
import ro.kds.erp.data.AttributeLocal;
import java.math.BigDecimal;
import javax.naming.Context;
import java.util.Map;
import java.util.ArrayList;
import ro.kds.erp.data.CategoryLocal;
import java.util.Collection;
import java.util.Iterator;

/**
 * Describe class GrilaVentilatieBizBean here.
 *
 *
 * Created: Tue Nov 22 03:59:35 2005
 *
 * @author <a href="mailto:Mihai Giurgeanu@CRIMIRA"></a>
 * @version 1.0
 */
public class GrilaVentilatieBizBean extends GrilaVentilatieBean {

    /**
     * Stores the values in the form bean into the persistence layer.
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

	    CategoryLocalHome ch = 
		(CategoryLocalHome)PortableRemoteObject.narrow
		(env.lookup("ejb/CategoryHome"), CategoryLocalHome.class);

	    if(id == null) {
		Integer categoryId = (Integer)env.lookup("categoryId");
		p = ph.create();
		id = p.getId();
		p.setCategory(ch.findByPrimaryKey(categoryId));
		p.setName("Grila ventilatie " + id);
		p.setCode("GV" + id.toString());
	    } else {
		p = ph.findByPrimaryKey(id);
	    }

	    p.setDescription(String.format("Grila ventilatie %.0f x %.0f", form.getLgv(), form.getHgv()));
	    p.setEntryPrice(form.getEntryPrice());
	    p.setSellPrice(form.getSellPrice());
	    p.setPrice1(form.getPrice1());

	    AttributeLocalHome ah = (AttributeLocalHome)PortableRemoteObject.
		narrow(env.lookup("ejb/AttributeHome"), 
		       AttributeLocalHome.class);
	    
	    ArrayList attributes = new ArrayList();

	    attributes.add(ah.create("lgv", form.getLgv()));
	    attributes.add(ah.create("hgv", form.getHgv()));
	    attributes.add(ah.create("pozitionare1", form.getPozitionare1()));
	    attributes.add(ah.create("pozitionare2", form.getPozitionare2()));
	    attributes.add(ah.create("pozitionare3", form.getPozitionare3()));
	    attributes.add(ah.create("quantity", form.getQuantity()));
	    attributes.add(ah.create("businessCategory", form.getBusinessCategory()));

	    p.setAttributes(attributes);
	    r = validate();
	} catch (NamingException e) {
	    r = ResponseBean.ERR_CONFIG_NAMING;
	    logger.log(BasicLevel.ERROR, "Error saving product with id " + id,
		       e);
	    ejbContext.setRollbackOnly();
	} catch (Exception e) {
	    r = ResponseBean.ERR_UNEXPECTED;
	    logger.log(BasicLevel.ERROR, "Error saving product with id " + id,
		       e);
	    ejbContext.setRollbackOnly();
	}
	return r;
    }

    /**
     * Loads the fields of the form bean from the persistence layer.
     *
     * @return a <code>ResponseBean</code> value
     * @exception FinderException if an error occurs
     */
    public ResponseBean loadFields() throws FinderException {
	ResponseBean r;
	try {
	    InitialContext ic = new InitialContext();
	    Context env = (Context) ic.lookup("java:comp/env");
	
	    ProductLocalHome ph = (ProductLocalHome)PortableRemoteObject.narrow
		(env.lookup("ejb/ProductHome"), ProductLocalHome.class);
	    ProductLocal p = ph.findByPrimaryKey(this.id);
	    form.setSellPrice(p.getSellPrice());
	    form.setSellPrice(p.getSellPrice());
	    form.setPrice1(p.getPrice1());
	    
	    Map as = p.getAttributesMap();
	    AttributeLocal a;
	    
	    if((a = (AttributeLocal)as.get("lgv")) != null)
		form.setLgv(a.getDoubleValue());

	    if((a = (AttributeLocal)as.get("hgv")) != null)
		form.setHgv(a.getDoubleValue());
	    
	    if((a = (AttributeLocal)as.get("pozitionare1")) != null)
		form.setPozitionare1(a.getStringValue());
	    
	    if((a = (AttributeLocal)as.get("pozitionare2")) != null)
		form.setPozitionare2(a.getStringValue());

	    if((a = (AttributeLocal)as.get("pozitionare3")) != null)
		form.setPozitionare3(a.getStringValue());

	    if((a = (AttributeLocal)as.get("quantity")) != null)
		form.setQuantity(a.getIntValue());

	    if((a = (AttributeLocal)as.get("businessCategory")) != null)
		form.setBusinessCategory(a.getStringValue());

	    r = new ResponseBean();
	} catch (NamingException e) {
	    r = new ResponseBean();
	    r.setCode(1);
	    r.setMessage("Eroare de configurare a serverului. " +
			 "Datele nu au fost salvate");
	    logger.log(BasicLevel.ERROR, "Error saving product with id " + id,
		       e);
	}

	return r;
    }

    /**
     * Describe <code>createNewFormBean</code> method here.
     *
     */
    public void createNewFormBean() {
	super.createNewFormBean();

	form.setQuantity(new Integer(1));
    }


    /**
     * Overwrite the base class implementation to add <code>groupingCode</code>
     * field.
     *
     * @param responseBean a <code>ResponseBean</code> value
     * @return a <code>ResponseBean</code> value
     */
    public ResponseBean computeCalculatedFields(ResponseBean r) {
	r = super.computeCalculatedFields(r);

	GroupingCode gcode = new GroupingCode();
	gcode.add("GV");
	gcode.add(form.getLgv())
             .add(form.getHgv());
	

	String gcodestr = gcode.toString();
	if(gcodestr.compareTo(form.getGroupingCode()) != 0) {
	    form.setGroupingCode(gcodestr);
	    r.addField("groupingCode", gcodestr);
	}


	try {
		InitialContext ic = new InitialContext();
		Context env = (Context)ic.lookup("java:comp/env");
		CategoryLocalHome ch = (CategoryLocalHome)PortableRemoteObject.narrow
		    (env.lookup("ejb/CategoryHome"), CategoryLocalHome.class);
		CategoryLocal c = ch.findByPrimaryKey(new Integer(11099));
		Collection grileStd = c.getProducts();
		for(Iterator i = grileStd.iterator(); i.hasNext();) {
		    ProductLocal p = (ProductLocal)i.next();
		    Map attributes = p.getAttributesMap();
		    
		    AttributeLocal l = (AttributeLocal)attributes.get("L");
		    AttributeLocal h = (AttributeLocal)attributes.get("H");
		    if(l != null
		       && l.getDoubleValue().equals(form.getLgv())
		       && h!= null
		       && h.getDoubleValue().equals(form.getHgv())
		       ) {
			form.setPrice1(new BigDecimal(p.getPrice1().doubleValue() * form.getQuantity().intValue()));
			form.setSellPrice(form.getPrice1());
			form.setEntryPrice(new BigDecimal(p.getEntryPrice().doubleValue() * form.getQuantity().intValue()));
		    }
		}
	    } catch (Exception e) {
		logger.log(BasicLevel.WARN, 
			   "Error while updating lf and hf fields based on standard field",
			   e);
	    }

	return r;
    }

    public ResponseBean getProductReport(Integer productId) {
	ResponseBean r;
	try {
	    r = loadFormData(productId);
	    InitialContext ic = new InitialContext();
	    Context env = (Context)ic.lookup("java:comp/env");
	    ProductLocalHome ph = (ProductLocalHome)PortableRemoteObject.
		narrow(env.lookup("ejb/ProductHome"), ProductLocalHome.class);
	    ProductLocal p = ph.findByPrimaryKey(productId);

	    r.addField("category.name", p.getCategory().getName());
	    r.addField("category.id", p.getCategory().getId());
	} catch (FinderException e) {
	    logger.log(BasicLevel.ERROR, "FinderException in FereastraBizBean.getProductReport for productId " + productId);
	    logger.log(BasicLevel.DEBUG, e);
	    r = ResponseBean.getErrNotFound(e.getMessage());
	}
	catch (NamingException e) {
	    logger.log(BasicLevel.ERROR, "NamingException when preparing to get the report for the product id " + productId + ": " + e.getMessage());
	    logger.log(BasicLevel.DEBUG, e);
	    r = ResponseBean.getErrConfigNaming(e.getMessage());
	}
	return r;
    }

}
