package ro.kds.erp.data;

import javax.ejb.EntityBean;
import java.rmi.RemoteException;
import javax.ejb.RemoveException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.EJBException;
import javax.ejb.CreateException;
import java.util.Collection;
import org.objectweb.util.monolog.Monolog;
import org.objectweb.util.monolog.api.BasicLevel;
import org.objectweb.util.monolog.api.Logger;

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

    static private Logger logger;
    private EntityContext ejbContext;


    public Integer ejbCreate() throws javax.ejb.CreateException{
        logger.log(BasicLevel.DEBUG, "");

        // Init here the bean fields

        return null;
    }

    public void ejbPostCreate() {
        logger.log(BasicLevel.DEBUG, "");
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
	    logger = Monolog.getMonologFactory().getLogger("ro.kds.erp.data.ProductsSelectionBean");
	}
	logger.log(BasicLevel.DEBUG, "");
	this.ejbContext = entityContext;
    }

    public void unsetEntityContext() throws EJBException, RemoteException {
	logger.log(BasicLevel.DEBUG, "");
	ejbContext = null;
    }

    public void ejbRemove() throws RemoveException, EJBException, RemoteException {
	logger.log(BasicLevel.DEBUG, "");
    }

    public void ejbActivate() throws EJBException, RemoteException {
	logger.log(BasicLevel.DEBUG, "");
    }

    public void ejbPassivate() throws EJBException, RemoteException {
	logger.log(BasicLevel.DEBUG, "");
    }

    public void ejbLoad() throws EJBException, RemoteException {
	logger.log(BasicLevel.DEBUG, "");
    }

    public void ejbStore() throws EJBException, RemoteException {
	logger.log(BasicLevel.DEBUG, "");
    }

}
