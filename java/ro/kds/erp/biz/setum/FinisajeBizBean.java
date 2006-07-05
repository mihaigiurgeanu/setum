package ro.kds.erp.biz.setum;

import ro.kds.erp.biz.setum.basic.FinisajeBean;
import ro.kds.erp.data.ProductLocal;
import ro.kds.erp.biz.ResponseBean;
import javax.ejb.FinderException;
import ro.kds.erp.data.ProductLocalHome;
import javax.naming.InitialContext;
import ro.kds.erp.data.CategoryLocalHome;
import ro.kds.erp.data.CategoryLocal;
import ro.kds.erp.data.AttributeLocalHome;
import org.objectweb.util.monolog.api.BasicLevel;
import javax.naming.NamingException;
import javax.ejb.CreateException;
import java.rmi.RemoteException;
import javax.rmi.PortableRemoteObject;
import javax.naming.Context;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

import java.util.Collection;

/**
 * Describe class FinisajeBizBean here.
 *
 *
 * Created: Mon Mar 27 00:09:04 2006
 *
 * @author <a href="mailto:mihai@cris.kds.ro">Mihai Giurgeanu</a>
 * @version 1.0
 */
public class FinisajeBizBean extends FinisajeBean {

    public ResponseBean loadFields() throws FinderException {
	ResponseBean r;
	
	ProductLocal p = getCurrentProduct();
	

	if (p != null) {

	    Map amap = p.getAttributesMap();
	    form.readZincare(amap);
	    form.readCapitonare(amap);
	    form.readPlacare(amap);
	    form.readGrundId(amap);
	    form.readVopsireTip(amap);
	    form.readRalStasId(amap);
	    form.readRalOrder(amap);
	    form.readRalOrderValue(amap);

	    form.setCode(p.getCode());
	    form.setName(p.getName());
	    form.setDescription(p.getDescription());
	    form.setSellPrice(p.getSellPrice());
	    form.setEntryPrice(p.getEntryPrice());
	    form.setPrice1(p.getPrice1());

	    r = new ResponseBean();
	} else {
	    r = new ResponseBean();
	    r.setCode(4);
	    r.setMessage("Nici un set de finisaje nu este incarcat");	    
	}
	return r;
    }



    public ResponseBean saveFormData() {
	ResponseBean r;

	try {
	    ProductLocal p = getCurrentProduct();
	    if(p == null) {
		ProductLocalHome ph = getProductHome();
		ph.create();
		id = p.getId();
		p.setCode(id.toString());

		InitialContext ic = new InitialContext();
		Context env = (Context)ic.lookup("java:comp/env");
		Integer categoryId = (Integer)env.lookup("categoryId");

		CategoryLocalHome ch = getCategoryHome();
		CategoryLocal c = ch.findByPrimaryKey(categoryId);

		p.setCategory(c);
	    }

	    p.setName(form.getName());
	    p.setCode(form.getCode());
	    p.setDescription(form.getDescription());
	    p.setSellPrice(form.getSellPrice());
	    p.setEntryPrice(form.getEntryPrice());
	    p.setPrice1(form.getPrice1());

	    AttributeLocalHome ah = getAttributeHome();
	    Collection attribs = new ArrayList();
	    attribs.add(ah.create("zincare", form.getZincare()));
	    attribs.add(ah.create("capitonare", form.getCapitonare()));
	    attribs.add(ah.create("placare", form.getPlacare()));
	    attribs.add(ah.create("grundId", form.getGrundId()));
	    attribs.add(ah.create("vopsireTip", form.getVopsireTip()));
	    attribs.add(ah.create("ralStasId", form.getRalStasId()));
	    attribs.add(ah.create("ralOrder", form.getRalOrder()));
	    attribs.add(ah.create("ralOrderValue", form.getRalOrderValue()));

	    p.setAttributes(attribs);

	    r = validate();
	} catch (FinderException e) {
	    r = ResponseBean.ERR_NOTFOUND;
	    logger.log(BasicLevel.ERROR, e);
	} catch (NamingException e) {
	    r = ResponseBean.ERR_CONFIG_NAMING;
	    logger.log(BasicLevel.ERROR, e);
	} catch (CreateException e) {
	    r = ResponseBean.ERR_CREATE;
	    logger.log(BasicLevel.ERROR, e);
	}	
	return r;
    }


    /**
     * Makes a new copy of this product.
     * Before calling this method, load the product you want to duplicate.
     * After calling this method, the loaded product becomes the new copy of the
     * previously loaded product.
     * @returns the id of the new product
     */
    public Integer duplicate() {
	id = null;
	saveFormData();
	return id;
    }


    private ProductLocal getCurrentProduct() throws FinderException {
	
	if(id == null) return null;

	ProductLocal p;
	try {
	    ProductLocalHome home = getProductHome();
	    p = home.findByPrimaryKey(id);
	} catch (FinderException e) {
	    logger.log(BasicLevel.WARN, "Can not find the product with id " + id);
	    logger.log(BasicLevel.DEBUG, e);
	    throw e;
	} catch (Exception e) {
	    logger.log(BasicLevel.ERROR, "Exception occured while searching for the product with id " 
		       + id + ": " + e.getMessage());
	    logger.log(BasicLevel.INFO, e);
	    p = null;
	}

	return p;
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


    // Bean life cycle methods

    public void ejbPassivate() {
	super.ejbPassivate();

	pHome = null;
	cHome = null;
	aHome = null;
    }

    public void ejbActivate() {
	super.ejbActivate();

	pHome = null;
	cHome = null;
	aHome = null;
    }


    // Variables to cache the local homes of data layer beans
    ProductLocalHome pHome;
    CategoryLocalHome cHome;
    AttributeLocalHome aHome;
}
