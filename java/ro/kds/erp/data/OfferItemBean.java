package ro.kds.erp.data;

import javax.ejb.EntityContext;
import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import java.rmi.RemoteException;
import javax.ejb.RemoveException;
import org.objectweb.util.monolog.api.BasicLevel;
import java.math.BigDecimal;
import org.objectweb.util.monolog.api.Logger;
import org.objectweb.jonas.common.Log;
import javax.ejb.CreateException;

/**
 * The CMP 2.0 implementation of the OfferItemEJB.
 *
 *
 * Created: Sun Oct 23 18:37:09 2005
 *
 * @author <a href="mailto:Mihai Giurgeanu@CRIMIRA"></a>
 * @version 1.0
 */
public abstract class OfferItemBean implements EntityBean {

    static private Logger logger = null;
    javax.ejb.EntityContext ejbContext;

    public Integer ejbCreate() throws CreateException {
        logger.log(BasicLevel.DEBUG, "");
	
        // Init here the bean fields
	setBusinessCategory("http://www.kds.ro/erp/businessCategory/noCategory");
        return null;
    }

    public void ejbPostCreate() {
        logger.log(BasicLevel.DEBUG, "");
    }


    // Persistent fields



    public abstract Integer getId();
    public abstract void setId(Integer id);

    public abstract ProductLocal getProduct();
    public abstract void setProduct(ProductLocal p);

    public abstract BigDecimal getQuantity();
    public abstract void setQuantity(BigDecimal q);
    
    public abstract String getUnit();
    public abstract void setUnit(String unit);

    public abstract BigDecimal getPrice();
    public abstract void setPrice(BigDecimal p);

    public abstract BigDecimal getPrice1();
    public abstract void setPrice1(BigDecimal p);
    
    public abstract BigDecimal getPrice2();
    public abstract void setPrice2(BigDecimal p);

    public abstract BigDecimal getPrice3();
    public abstract void setPrice3(BigDecimal p);

    public abstract BigDecimal getPrice4();
    public abstract void setPrice4(BigDecimal p);

    public abstract BigDecimal getPrice5();
    public abstract void setPrice5(BigDecimal p);

    public abstract BigDecimal getDiscount();
    public abstract void setDiscount(BigDecimal d);

    public abstract String getBusinessCategory();
    public abstract void setBusinessCategory(String bc);

    public abstract String getComments();
    public abstract void setComments(String comments);

    // Implementation of javax.ejb.EntityBean

    public void setEntityContext(EntityContext entityContext) throws EJBException, RemoteException {


        if (logger == null) {
            logger = Log.getLogger("ro.kds.erp.data.OfferItemBean");
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
