package ro.kds.erp.biz.setum;

import ro.kds.erp.biz.setum.basic.GauriAerisireBean;
import ro.kds.erp.biz.ResponseBean;
import javax.naming.InitialContext;
import ro.kds.erp.data.ProductLocalHome;
import javax.rmi.PortableRemoteObject;
import ro.kds.erp.data.ProductLocal;
import ro.kds.erp.data.CategoryLocalHome;
import ro.kds.erp.data.AttributeLocalHome;
import javax.naming.NamingException;
import org.objectweb.util.monolog.api.BasicLevel;
import javax.ejb.FinderException;
import ro.kds.erp.data.AttributeLocal;
import java.math.BigDecimal;
import javax.naming.Context;
import java.util.Map;
import java.util.ArrayList;

/**
 * Describe class GauriAerisireBizBean here.
 *
 *
 * Created: Tue Nov 22 04:31:25 2005
 *
 * @author <a href="mailto:Mihai Giurgeanu@CRIMIRA"></a>
 * @version 1.0
 */
public class GauriAerisireBizBean extends GauriAerisireBean {

    /**
     * Save the form bean's fields into the persistent layer.
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
		p.setName("Gauri aerisire " + id);
		p.setCode("GA" + id.toString());
	    } else {
		p = ph.findByPrimaryKey(id);
	    }

	    p.setDescription(String.format("Gauri aerisire %.0f/%.0f/%d", form.getDiametru(), form.getPas(), form.getNrRanduri()));
	    p.setSellPrice(form.getSellPrice());
	    p.setEntryPrice(form.getEntryPrice());
	    p.setPrice1(form.getPrice1());

	    AttributeLocalHome ah = (AttributeLocalHome)PortableRemoteObject.
		narrow(env.lookup("ejb/AttributeHome"), 
		       AttributeLocalHome.class);
	    
	    ArrayList attributes = new ArrayList();

	    attributes.add(ah.create("diametru", form.getDiametru()));
	    attributes.add(ah.create("pas", form.getPas()));
	    attributes.add(ah.create("nrRanduri", form.getNrRanduri()));
	    attributes.add(ah.create("pozitionare1", form.getPozitionare1()));
	    attributes.add(ah.create("pozitionare2", form.getPozitionare2()));
	    attributes.add(ah.create("pozitionare3", form.getPozitionare3()));
	    attributes.add(ah.create("businessCategory", form.getBusinessCategory()));
	    attributes.add(ah.create("quantity", form.getQuantity()));

	    p.setAttributes(attributes);
	    r = validate();
	} catch (NamingException e) {
	    r = ResponseBean.ERR_CONFIG_NAMING;
	    logger.log(BasicLevel.ERROR, "Error saving product with id " + id,
		       e);
	} catch (Exception e) {
	    r = ResponseBean.ERR_UNEXPECTED;
	    logger.log(BasicLevel.ERROR, "Error saving product with id " + id,
		       e);
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
	    
	    if((a = (AttributeLocal)as.get("diametru")) != null)
		form.setDiametru(a.getDoubleValue());

	    if((a = (AttributeLocal)as.get("pas")) != null)
		form.setPas(a.getDoubleValue());
	    
	    if((a = (AttributeLocal)as.get("nrRanduri")) != null)
		form.setNrRanduri(a.getIntValue());

	    if((a = (AttributeLocal)as.get("pozitionare1")) != null)
		form.setPozitionare1(a.getStringValue());
	    
	    if((a = (AttributeLocal)as.get("pozitionare2")) != null)
		form.setPozitionare2(a.getStringValue());

	    if((a = (AttributeLocal)as.get("pozitionare3")) != null)
		form.setPozitionare3(a.getStringValue());

	    if((a = (AttributeLocal)as.get("businessCategory")) != null)
		form.setBusinessCategory(a.getStringValue());

	    if((a = (AttributeLocal)as.get("quantity")) != null)
		form.setQuantity(a.getIntValue());

	    r = new ResponseBean();
	} catch (NamingException e) {
	    r = ResponseBean.ERR_CONFIG_NAMING;
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
	gcode.add("GA");
	gcode.add(form.getDiametru())
             .add(form.getPas())
             .add(form.getNrRanduri());
	

	String gcodestr = gcode.toString();
	if(gcodestr.compareTo(form.getGroupingCode()) != 0) {
	    form.setGroupingCode(gcodestr);
	    r.addField("groupingCode", gcodestr);
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
