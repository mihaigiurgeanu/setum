package ro.kds.erp.biz;

import javax.ejb.SessionContext;
import java.rmi.RemoteException;
import javax.ejb.SessionBean;
import javax.ejb.EJBException;
import javax.ejb.CreateException;
import org.objectweb.util.monolog.api.BasicLevel;
import org.objectweb.util.monolog.Monolog;
import org.objectweb.util.monolog.api.Logger;
import javax.naming.InitialContext;
import ro.kds.erp.data.ProductLocalHome;
import javax.rmi.PortableRemoteObject;
import ro.kds.erp.data.AttributeLocal;
import javax.naming.NamingException;
import javax.ejb.FinderException;
import javax.naming.Context;
import java.util.Collection;
import java.util.Iterator;
import ro.kds.erp.data.ProductLocal;
import ro.kds.erp.data.CompositeProductLocal;

/**
 * Implementation of a <code>CategoryManager</code> giving a default product
 * processing. The processing is based on fields in the database with no
 * specific calculated fields or business logic.
 *
 *
 * Created: Sun Jan 21 23:06:27 2007
 *
 * @author <a href="mailto:Mihai Giurgeanu@MIHAIG">U-MIHAIG\Mihai Giurgeanu</a>
 * @version 1.0
 */
public class DefaultCategoryManagerBean implements SessionBean {
    static protected Logger logger = null;


    /**
     * Create a new session bean
     */
    public void ejbCreate() throws CreateException {
	logger.log(BasicLevel.DEBUG, "");
    }

    // Implementation of javax.ejb.SessionBean

    public void setSessionContext(SessionContext sessionContext) throws EJBException, RemoteException {

        if (logger == null) {
            logger = Monolog.getMonologFactory().getLogger("ro.kds.erp.biz.DefaultCategoryManagerBean");
        }
        logger.log(BasicLevel.DEBUG, "");
    }

    public void ejbRemove() throws EJBException, RemoteException {

	logger.log(BasicLevel.DEBUG, "");
    }

    public void ejbPassivate() throws EJBException, RemoteException {

	logger.log(BasicLevel.DEBUG, "");
    }

    public void ejbActivate() throws EJBException, RemoteException {

	logger.log(BasicLevel.DEBUG, "");
    }





    // Business methods


    /**
     * Builds a standard product report, adding the fields of the
     * <code>ProductBean</code> as well as all its attributes to the 
     * <code>ResponseBean</code>
     *
     * @return a <code>ResponseBean</code> with one record containing
     * the fields of the product and its attributes. It does not add
     * specific calculated fields.
     */
    public ResponseBean getProductReport(Integer productId) {
	ResponseBean r;

	try {

	    InitialContext ic = new InitialContext();
	    Context env = (Context) ic.lookup("java:comp/env");

	    ProductLocalHome ph = (ProductLocalHome)PortableRemoteObject.
		narrow(env.lookup("ejb/ProductHome"), ProductLocalHome.class);
	    ProductLocal p = ph.findByPrimaryKey(productId);


	    r = new ResponseBean();
	    r.addRecord();

	    r.addField("name", p.getName());
	    r.addField("code", p.getCode());
	    r.addField("entryPrice", p.getEntryPrice());
	    r.addField("sellPrice", p.getSellPrice());
	    r.addField("price1", p.getPrice1());
	    r.addField("price2", p.getPrice2());
	    r.addField("price3", p.getPrice3());
	    r.addField("price4", p.getPrice4());
	    r.addField("price5", p.getPrice5());
	    r.addField("priceCorrectionP", p.getPriceCorrectionP());
	    r.addField("description", p.getDescription());
	    r.addField("description1", p.getDescription1());
	    r.addField("discontinued", p.getDiscontinued());
	    r.addField("category.name", p.getCategory().getName());
	    r.addField("category.id", p.getCategory().getId());

	    Collection attribs = p.getAttributes();
	    for(Iterator i = attribs.iterator(); i.hasNext();) {
		AttributeLocal a = (AttributeLocal)i.next();

		// select all non null values
		if( a.getStringValue() != null ) {
		    r.addField(a.getName(), a.getStringValue());
		}
		if( a.getIntValue() != null ) {
		    r.addField(a.getName(), a.getIntValue());
		}
		if( a.getDecimalValue() != null ) {
		    r.addField(a.getName(), a.getDecimalValue());
		}
		if( a.getBoolValue() != null ) {
		    r.addField(a.getName(), a.getBoolValue());
		}
		if( a.getProduct() != null ) {

		    try {
			r.addField(a.getName(), CategoryManagerFactory.getCategoryManager(a.getProduct().getCategory().getId())
				   .getProductReport(a.getProduct().getId()));
		    } catch (CategoryManagerException e) {
			logger.log(BasicLevel.WARN, "Category manager could not be created: " + e.getMessage());
			logger.log(BasicLevel.DEBUG, e);
		    }
		}
	    }


	    CompositeProductLocal composite = p.getCompositeProduct();
	    if(composite != null) {
		Collection parts = composite.getComponents();
		ResponseBean partsListing = new ResponseBean();
		for(Iterator i = parts.iterator(); i.hasNext(); ) {
		    ProductLocal part = (ProductLocal)i.next();
		    partsListing.addRecord();
		    try {
			partsListing.addField("part", CategoryManagerFactory.getCategoryManager(part.getCategory().getId())
					      .getProductReport(part.getId()));
		    } catch (CategoryManagerException e) {
			logger.log(BasicLevel.WARN, "CategoryManager could not be created while trying to get description of product with id " + part.getId());
		    }
		    
		}
		r.addField("parts", partsListing);
	    }



	} catch (NamingException e) {
	    logger.log(BasicLevel.ERROR, "NamingException while instantiationg the product id " + productId + ": " + e.getMessage());
	    logger.log(BasicLevel.DEBUG, e);
	    r = ResponseBean.getErrConfigNaming(e.getMessage());
	} catch (FinderException e) {
	    logger.log(BasicLevel.WARN, "Product could not be found for id " + productId);
	    logger.log(BasicLevel.DEBUG, e);
	    r = ResponseBean.getErrNotFound("productId=" + productId);
	}

	return r;
    }
}

