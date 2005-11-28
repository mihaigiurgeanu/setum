package ro.kds.erp.biz.setum;

import java.math.BigDecimal;
import ro.kds.erp.biz.ResponseBean;
import javax.ejb.FinderException;
import javax.naming.InitialContext;
import ro.kds.erp.data.ProductLocalHome;
import javax.rmi.PortableRemoteObject;
import ro.kds.erp.data.ProductLocal;
import javax.naming.NamingException;
import org.objectweb.util.monolog.api.BasicLevel;
import ro.kds.erp.data.CategoryLocalHome;
import ro.kds.erp.data.CategoryLocal;
import javax.naming.Context;
import java.util.Collection;
import java.util.Iterator;

/**
 * Definirea de usi standard neechipate.
 *
 *
 * Created: Wed Nov 02 10:37:07 2005
 *
 * @author <a href="mailto:Mihai Giurgeanu@CRIMIRA"></a>
 * @version 1.0
 */
public class UsaStdNeechipataBean 
    extends ro.kds.erp.biz.setum.basic.UsaStdNeechipataBean {


    /**
     * Form data initialization.
     */
    public void createNewFormBean() {
	super.createNewFormBean();
	
	form.setName("");
	form.setCode("");
	form.setDescription("");
	form.setPrice(new BigDecimal(0));
    }

    /**
     * Load the form field values from the persistence layer.
     */
    public ResponseBean loadFields() throws FinderException {
	ResponseBean r;
	try {
	    InitialContext ic = new InitialContext();
	    Context env = (Context) ic.lookup("java:comp/env");
	    ProductLocalHome ph = (ProductLocalHome)PortableRemoteObject.narrow
		(env.lookup("ejb/ProductHome"), ProductLocalHome.class);
	    
	    // the instance variable id should contain the primary key
	    // for the currently selected product
	    ProductLocal p = ph.findByPrimaryKey(id);
	    
	    form.setName(p.getName());
	    form.setCode(p.getCode());
	    form.setDescription(p.getDescription());
	    form.setPrice(p.getSellPrice());

	    r = new ResponseBean();

	    // the fields will be added to the respons by
	    // the caller (super.loadFormData());

	} catch (NamingException e) {
	    r = new ResponseBean();
	    r.setCode(1);
	    r.setMessage("Eroare de configurare a serverului");
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
     */
    public ResponseBean saveFormData() {
	ResponseBean r;
	try {
	    InitialContext ic = new InitialContext();
	    Context env = (Context) ic.lookup("java:comp/env");
	    ProductLocalHome ph = (ProductLocalHome)PortableRemoteObject.narrow
		(env.lookup("ejb/ProductHome"), ProductLocalHome.class);

	    ProductLocal p;
	    if(id == null) {
		p = ph.create();
		CategoryLocalHome ch = (CategoryLocalHome)
		    PortableRemoteObject.narrow
		    (env.lookup("ejb/CategoryHome"), CategoryLocalHome.class);

		Integer catId = (Integer)env.lookup("categoryId");
		p.setCategory(ch.findByPrimaryKey(catId));
	    } else {
		p = ph.findByPrimaryKey(id);
	    }

	    p.setName(form.getName());
	    p.setCode(form.getCode());
	    p.setDescription(form.getDescription());
	    p.setSellPrice(form.getPrice());
	    p.setEntryPrice(form.getPrice());
	    p.setPrice1(form.getPrice());

	    r = new ResponseBean();
	} catch (NamingException e) {
	    r = new ResponseBean();
	    r.setCode(1);
	    r.setMessage("Eroare de configurare a aplicatiei");
	    logger.log(BasicLevel.ERROR, e.getMessage(), e);
	} catch (Exception e) {
	    r = new ResponseBean();
	    r.setCode(3);
	    r.setMessage("Eroare la salvarea campurilor in baza de date");
	    logger.log(BasicLevel.ERROR, e.getMessage(), e);
	}
	
	return r;
    }

    /**
     * Build the listing of products.
     */
    public ResponseBean loadListing() {
	ResponseBean r;
	
	try {
	    InitialContext ic = new InitialContext();
	    Context env = (Context)ic.lookup("java:comp/env");
	    Integer catId = (Integer)env.lookup("categoryId");
	    CategoryLocalHome ch = 
		(CategoryLocalHome)PortableRemoteObject.narrow
		(env.lookup("ejb/CategoryHome"), CategoryLocalHome.class);

	    r = new ResponseBean();
	    
	    CategoryLocal theCategory = ch.findByPrimaryKey(catId);
	    Collection products = theCategory.getProducts();

	    for(Iterator i = products.iterator(); i.hasNext(); ) {
		ProductLocal p = (ProductLocal)i.next();
		
		r.addRecord();
		r.addField("id", p.getId());
		r.addField("col-code", p.getCode());
		r.addField("col-usa", p.getName());
		r.addField("col-description", p.getDescription());
		r.addField("col-price", p.getSellPrice());
	    }
	} catch (NamingException e) {
	    r = new ResponseBean();
	    r.setCode(1);
	    r.setMessage("Eroare de configurare. Lista nu a putut fi incarcata");
	    logger.log(BasicLevel.ERROR, e.getMessage(), e);
	} catch (Exception e) {
	    r = new ResponseBean();
	    r.setCode(3);
	    r.setMessage("Eroare aplicatie. Lista nu a putut fi incarcata");
	    logger.log(BasicLevel.ERROR, e.getMessage(), e);
	}

	return r;
    }
}
