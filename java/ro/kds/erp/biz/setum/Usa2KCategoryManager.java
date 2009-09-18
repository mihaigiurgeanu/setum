package ro.kds.erp.biz.setum;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import java.rmi.RemoteException;
import javax.ejb.EJBException;
import org.objectweb.util.monolog.Monolog;
import org.objectweb.util.monolog.api.BasicLevel;
import ro.kds.erp.biz.ResponseBean;
import javax.naming.InitialContext;
import ro.kds.erp.data.ProductLocalHome;
import javax.rmi.PortableRemoteObject;
import ro.kds.erp.data.ProductLocal;
import ro.kds.erp.data.CompositeProductLocal;
import ro.kds.erp.biz.CategoryManagerFactory;
import ro.kds.erp.biz.CategoryManagerException;
import javax.naming.NamingException;
import javax.ejb.FinderException;
import javax.naming.Context;
import java.util.Collection;
import java.util.Iterator;
import javax.ejb.CreateException;
import org.objectweb.util.monolog.api.Logger;
import ro.kds.erp.biz.setum.basic.UsaMetalica2KHome;
import ro.kds.erp.biz.setum.basic.UsaMetalica2K;


/**
 * Implementare a unui <code>CategoryManager</code> specific usilor metalice
 * customizate.
 *
 *
 * Created: Tue Jan 23 22:00:22 2007
 *
 * @author <a href="mailto:Mihai Giurgeanu@MIHAIG">U-MIHAIG\Mihai Giurgeanu</a>
 * @version 1.0
 */
public class Usa2KCategoryManager implements SessionBean {
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
            logger = Monolog.getMonologFactory().getLogger("ro.kds.erp.biz.setum.Usa2KCategoryManagerBean");
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
     * Construieste un <code>ResponseBean</code> cu toate campurile produsului
     * si ale produselor componente. Produsul trebuie sa fie o usa metalica
     * customizata (<code>UsaMetalica2K</code>).
     *
     * @param productId este id-ul unui produs care trebuie sa fie
     * din categoria "Usa metalica" (creata cu bean-ul <code>UsaMetalica2K</code>)
     */
    public ResponseBean getProductReport(Integer productId) {
	ResponseBean r;

	try {

	    InitialContext ic = new InitialContext();
	    Context env = (Context) ic.lookup("java:comp/env");

	    ProductLocalHome ph = (ProductLocalHome)PortableRemoteObject.
		narrow(env.lookup("ejb/ProductHome"), ProductLocalHome.class);
	    ProductLocal p = ph.findByPrimaryKey(productId);


	    UsaMetalica2KHome usaH = (UsaMetalica2KHome)PortableRemoteObject.
		narrow(env.lookup("ejb/UsaMetalica2KHome"), UsaMetalica2KHome.class);
	    UsaMetalica2K usa = usaH.create();

	    r = usa.loadFormData(productId);


	    r.addField("category.name", p.getCategory().getName());
	    r.addField("category.id", p.getCategory().getId());

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
			logger.log(BasicLevel.WARN, "CategoryManager could not be created while trying to get description of product with id " 
				   + part.getId());
		    }
		    
		}
		r.addField("parts", partsListing);
	    }


	} catch (NamingException e) {
	    logger.log(BasicLevel.ERROR, "NamingException when preparing to get the report for the product id " + productId + ": " + e.getMessage());
	    logger.log(BasicLevel.DEBUG, e);
	    r = ResponseBean.getErrConfigNaming(e.getMessage());
	} catch (FinderException e) {
	    logger.log(BasicLevel.WARN, "Product could not be found for id " + productId);
	    logger.log(BasicLevel.DEBUG, e);
	    r = ResponseBean.getErrNotFound("productId=" + productId);
	} catch (CreateException e) {
	    logger.log(BasicLevel.ERROR, "CreateException while getting the report for product id "
		       + productId + ": " + e.getMessage());
	    logger.log(BasicLevel.DEBUG, e);
	    r = ResponseBean.getErrCreate(e.getMessage());
	} catch (RemoteException e) {
	    logger.log(BasicLevel.ERROR, "RemoteException while getting data for UsaMetalica2K with id " + productId);
	    logger.log(BasicLevel.DEBUG, e);
	    r = ResponseBean.getErrRemote("UsaMetalica2K");
	}

	return r;
    }
}
