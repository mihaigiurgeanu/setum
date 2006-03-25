package ro.kds.erp.data;

import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import java.rmi.RemoteException;
import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import javax.ejb.RemoveException;
import java.util.Collection;
import org.objectweb.util.monolog.api.BasicLevel;
import org.objectweb.jonas.common.Log;
import javax.ejb.CreateException;

import org.objectweb.util.monolog.api.Logger;
import javax.ejb.FinderException;

/**
 * Describe class CategoryBean here.
 *
 *
 * Created: Fri Sep 16 19:11:19 2005
 *
 * @author <a href="mailto:Mihai Giurgeanu@CRIMIRA"></a>
 * @version 1.0
 */
public abstract class CategoryBean implements EntityBean {

    static private Logger logger = null;
    javax.ejb.EntityContext ejbContext;


    public Integer ejbCreate(Integer id,
			     String categoryName) throws CreateException {
	setId(id);
	setName(categoryName);

	return null;
    }

    public void ejbPostCreate(Integer id, String categoryName) {
	
    }

    // ------------------------------------------------------------------
    // Persistent fields
    //
    // ------------------------------------------------------------------
    public abstract Integer getId();
    public abstract void setId(Integer newId);

    public abstract String getName();
    public abstract void setName(String name);

    public abstract Collection getSubCategories();
    public abstract void setSubCategories(Collection categories);

    public abstract Collection getProducts();
    public abstract void setProducts(Collection products);

    // ------------------------------------------------------------------
    // Business methods
    //
    // ------------------------------------------------------------------
    public Integer getProductsCount() {
	try {
	    return new Integer(ejbSelectProductsCount(getId()));
	} catch (FinderException e) {
	    logger.log(BasicLevel.WARN, e);
	    return new Integer(0);
	}
    }


    // ------------------------------------------------------------------
    // Select methods
    //
    // ------------------------------------------------------------------
    public abstract int ejbSelectProductsCount(Integer productId) throws FinderException;

    // ------------------------------------------------------------------
    // Standard call back methods
    // ------------------------------------------------------------------

    // Implementation of javax.ejb.EntityBean

    /**
     * Describe <code>setEntityContext</code> method here.
     *
     * @param entityContext an <code>EntityContext</code> value
     * @exception EJBException if an error occurs
     * @exception RemoteException if an error occurs
     */
    public void setEntityContext(EntityContext entityContext) throws EJBException, RemoteException {

        if (logger == null) {
            logger = Log.getLogger("ro.kds.erp.data.CategoryBean");
        }
        logger.log(BasicLevel.DEBUG, "");
        ejbContext = entityContext;

    }

    /**
     * Describe <code>unsetEntityContext</code> method here.
     *
     * @exception EJBException if an error occurs
     * @exception RemoteException if an error occurs
     */
    public void unsetEntityContext() 
	throws EJBException, RemoteException {

        logger.log(BasicLevel.DEBUG, "");
        ejbContext = null;
    }

    /**
     * Describe <code>ejbRemove</code> method here.
     *
     * @exception RemoveException if an error occurs
     * @exception EJBException if an error occurs
     * @exception RemoteException if an error occurs
     */
    public void ejbRemove() 
	throws RemoveException, EJBException, RemoteException {

	
    }

    /**
     * Describe <code>ejbActivate</code> method here.
     *
     * @exception EJBException if an error occurs
     * @exception RemoteException if an error occurs
     */
    public void ejbActivate() throws EJBException, RemoteException {

    }

    /**
     * Describe <code>ejbPassivate</code> method here.
     *
     * @exception EJBException if an error occurs
     * @exception RemoteException if an error occurs
     */
    public void ejbPassivate() throws EJBException, RemoteException {

    }

    /**
     * Describe <code>ejbLoad</code> method here.
     *
     * @exception EJBException if an error occurs
     * @exception RemoteException if an error occurs
     */
    public void ejbLoad() throws EJBException, RemoteException {

    }

    /**
     * Describe <code>ejbStore</code> method here.
     *
     * @exception EJBException if an error occurs
     * @exception RemoteException if an error occurs
     */
    public void ejbStore() throws EJBException, RemoteException {

    }

}
