package ro.kds.erp.data;

import javax.ejb.EntityBean;
import java.rmi.RemoteException;
import javax.ejb.EJBException;
import javax.ejb.EntityContext;
import javax.ejb.EntityBean;
import javax.ejb.RemoveException;
import javax.ejb.CreateException;
import java.math.BigDecimal;
import org.objectweb.util.monolog.api.BasicLevel;
import org.objectweb.util.monolog.api.Logger;
import org.objectweb.jonas.common.Log;

/**
 * Describe class OrderLineBean here.
 *
 *
 * Created: Sun Sep 17 14:39:50 2006
 *
 * @author <a href="mailto:Mihai Giurgeanu@MIHAIG">U-MIHAIG\Mihai Giurgeanu</a>
 * @version 1.0
 */
public abstract class OrderLineBean implements EntityBean {


    static private Logger logger = null;
    EntityContext ejbContext;



    // Create methods

    /**
     * Implementation of local home create method.
     */
    public Integer ejbCreate() throws CreateException {
	return null;
    }


    /**
     * Implementation of local home create method.
     */
    public void ejbPostCreate() throws CreateException {
    }



    // CMP fields and relations

    public abstract Integer getId();
    public abstract void setId(Integer id);

    public abstract OfferItemLocal getOfferItem();
    public abstract void setOfferItem(OfferItemLocal oi);

    public abstract BigDecimal getPrice();
    public abstract void setPrice(BigDecimal price);

    public abstract BigDecimal getQuantity();
    public abstract void setQuantity(BigDecimal q);


    // Implementation of javax.ejb.EntityBean

    public void setEntityContext(EntityContext entityContext) throws EJBException, RemoteException {

        if (logger == null) {
            logger = Log.getLogger("ro.kds.erp.data.OrderBean");
        }
        logger.log(BasicLevel.DEBUG, "");
        ejbContext = entityContext;
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
