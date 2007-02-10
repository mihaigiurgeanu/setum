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
