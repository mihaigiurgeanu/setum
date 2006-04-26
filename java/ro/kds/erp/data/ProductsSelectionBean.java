package ro.kds.erp.data;

import javax.ejb.EntityBean;
import java.rmi.RemoteException;
import javax.ejb.RemoveException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.EJBException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.ejb.CreateException;
import java.util.Collection;

/**
 * Describe class ProductsSelectionBean here.
 *
 *
 * Created: Wed Apr 26 14:44:00 2006
 *
 * @author <a href="mailto:Mihai Giurgeanu@CRIMIRA"></a>
 * @version 1.0
 */
public abstract class ProductsSelectionBean implements EntityBean {

    static private Log logger;
    private EntityContext ejbContext;


    public Integer ejbCreate() throws javax.ejb.CreateException{
        logger.trace("");

        // Init here the bean fields

        return null;
    }

    public void ejbPostCreate() {
        logger.trace("");
    }

    // ------------------------------------------------------------------
    // Persistent fields
    //
    // ------------------------------------------------------------------
    public abstract Integer getId();
    public abstract void setId(Integer id);

    public abstract String getName();
    public abstract void setName(String name);

    public abstract String getCode();
    public abstract void setCode(String code);

    public abstract String getDescription();
    public abstract void setDescription(String description);
    
    // ------------------------------------------------------------------
    // CMR fields
    // ------------------------------------------------------------------
    public abstract Collection getProducts();
    public abstract void setProducts(Collection products);

    public abstract Collection getSelections();
    public abstract void setSelections(Collection selections);


    // ------------------------------------------------------------------
    // Implementation of javax.ejb.EntityBean
    //
    // ------------------------------------------------------------------

    public void setEntityContext(EntityContext entityContext) throws EJBException, RemoteException {
	if(logger == null) {
	    logger = LogFactory.getLog(ProductsSelectionBean.class);
	}
	logger.trace("");
	this.ejbContext = entityContext;
    }

    public void unsetEntityContext() throws EJBException, RemoteException {
	logger.trace("");
	ejbContext = null;
    }

    public void ejbRemove() throws RemoveException, EJBException, RemoteException {
	logger.trace("");
    }

    public void ejbActivate() throws EJBException, RemoteException {
	logger.trace("");
    }

    public void ejbPassivate() throws EJBException, RemoteException {
	logger.trace("");
    }

    public void ejbLoad() throws EJBException, RemoteException {
	logger.trace("");
    }

    public void ejbStore() throws EJBException, RemoteException {
	logger.trace("");
    }

}
