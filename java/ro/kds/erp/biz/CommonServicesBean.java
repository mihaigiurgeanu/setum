package ro.kds.erp.biz;

import javax.ejb.EJBException;
import java.rmi.RemoteException;
import javax.ejb.SessionContext;
import javax.ejb.SessionBean;
import javax.ejb.CreateException;
import org.objectweb.util.monolog.api.BasicLevel;
import ro.kds.erp.data.ProductLocal;
import ro.kds.erp.data.CategoryLocal;
import javax.naming.InitialContext;
import ro.kds.erp.data.CategoryLocalHome;
import javax.rmi.PortableRemoteObject;
import javax.ejb.FinderException;
import javax.naming.NamingException;
import org.objectweb.util.monolog.Monolog;
import org.objectweb.util.monolog.api.Logger;
import ro.kds.erp.data.ProductLocalHome;
import javax.naming.Context;
import ro.kds.erp.data.AttributeLocal;
import java.util.Map;
import ro.kds.erp.rules.RulesSetLocalHome;
import java.util.Collection;

import java.util.Collection;

import java.util.Collection;
import ro.kds.erp.rules.RulesSetLocal;
import java.util.ArrayList;

/**
 * Implementation for common services offered by the business logic.
 *
 *
 * Created: Tue Feb  6 22:21:21 2007
 *
 * @author <a href="mailto:Mihai Giurgeanu@MIHAIG">U-MIHAIG\Mihai Giurgeanu</a>
 * @version 1.0
 */
public class CommonServicesBean implements SessionBean {


    static protected Logger logger = null;

    /**
     * Creation of a new bean.
     */
    public void ejbCreate() throws CreateException {
	logger.log(BasicLevel.DEBUG, "");
    }



    /**
     * Looks up a category by its id and within this category looks up
     * for a product by its code. If there are more products with the
     * same code in the same category, one of them will be returned.
     */
    public ProductLocal findProductByCode(Integer categoryId, String code) throws ProductNotAvailable {
	logger.log(BasicLevel.DEBUG, "");
	
	CategoryLocal category;
	try {	    
	    InitialContext ic = new InitialContext();
	    CategoryLocalHome ch = (CategoryLocalHome)PortableRemoteObject.
		narrow(ic.lookup("java:comp/env/ejb/CategoryHome"),
		       CategoryLocalHome.class);
	    category = ch.findByPrimaryKey(categoryId);
	} catch (FinderException e) {
	    logger.log(BasicLevel.WARN, "Category id can not be found: " + categoryId);
	    logger.log(BasicLevel.DEBUG, e);
	    throw new ProductNotAvailable(categoryId + "/"  + code, e);
	} catch (NamingException e) {
	    logger.log(BasicLevel.ERROR, 
		       "Naming service error when trying to locate category home: "
		       + e.getMessage());
	    logger.log(BasicLevel.DEBUG, e);
	    throw new ProductNotAvailable(categoryId + "/"  + code, e);
	}

	try {
	    ProductLocal p = category.getProductByCode(code);
	    return p;
	} catch (FinderException e) {
	    logger.log(BasicLevel.WARN, "Searched product code does not exist: " +
		       code + ", for category id " + categoryId);
	    logger.log(BasicLevel.DEBUG, e);
	    throw new ProductNotAvailable(categoryId + "/"  + code, e);
	}

    }

    /**
     * Looks up a product by its id.
     */
    public ProductLocal findProductById(Integer id) throws ProductNotAvailable {
	try {
	    InitialContext ic = new InitialContext();
	    Context env = (Context)ic.lookup("java:comp/env");
	    ProductLocalHome ph = (ProductLocalHome)PortableRemoteObject.
		narrow(env.lookup("ejb/ProductHome"), ProductLocalHome.class);
	    ProductLocal p = ph.findByPrimaryKey(id);

	    return p;
	} catch (Exception e) {
	    logger.log(BasicLevel.WARN, "Exception while finding a product by its id (" 
		       + id + "): " + e);
	    logger.log(BasicLevel.ERROR, e);
	    throw new ProductNotAvailable(id.toString(), e);
	}
    }



    public AttributeLocal getAttributeByProductId(Integer pid, String attrName) throws ProductNotAvailable {

	ProductLocal p = findProductById(pid);
	Map attribs = p.getAttributesMap();
	return (AttributeLocal)attribs.get(attrName);
    }


    /**
     * Gets the list of rules in a given rules set
     */
    public Collection getRules(String rulesSet) {
	Collection rules;
	try {
	    InitialContext ic = new InitialContext();
	    Context env = (Context)ic.lookup("java:comp/env");
	    RulesSetLocalHome home = (RulesSetLocalHome)PortableRemoteObject.
              narrow(env.lookup("ejb/RulesSetHome"), RulesSetLocalHome.class);
	    Collection sets = home.findByName(rulesSet);
	    if(sets.size() == 0) {
		logger.log(BasicLevel.WARN, "No rules set defined with name " + rulesSet);
		rules = new ArrayList();
	    } else {
		RulesSetLocal set = (RulesSetLocal)sets.iterator().next();
		if(sets.size() > 1) {
		    logger.log(BasicLevel.WARN, "There are more rules set with name " + rulesSet 
			       + ". It will be selected the one with id " + set.getId() + ".");
		}
		rules = set.getRules();
	    }
	} catch (Exception e) {
	    logger.log(BasicLevel.ERROR, "The rules set with name " + rulesSet +
		       " can not be retrieved due to an exception " + e);
	    logger.log(BasicLevel.DEBUG, e);
	    rules = new ArrayList();
	}
	return rules;
    }

    // Implementation of javax.ejb.SessionBean

    public void setSessionContext(SessionContext sessionContext) throws EJBException, RemoteException {

        if (logger == null) {
            logger = Monolog.getMonologFactory().getLogger("ro.kds.erp.biz.CommonServicesBean");
        }
	logger.log(BasicLevel.DEBUG, "");
    }

    public void ejbRemove() throws EJBException, RemoteException {

	logger.log(BasicLevel.DEBUG, "");
    }

    public void ejbActivate() throws EJBException, RemoteException {

	logger.log(BasicLevel.DEBUG, "");
    }

    public void ejbPassivate() throws EJBException, RemoteException {

	logger.log(BasicLevel.DEBUG, "");
    }

}
