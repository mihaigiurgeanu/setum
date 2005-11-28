package ro.kds.erp.data;

import javax.ejb.EntityBean;
import java.rmi.RemoteException;
import javax.ejb.RemoveException;
import javax.ejb.EntityContext;
import javax.ejb.EJBException;
import javax.ejb.CreateException;
import org.objectweb.util.monolog.api.BasicLevel;
import org.objectweb.util.monolog.api.Logger;
import org.objectweb.jonas.common.Log;
import java.util.Collection;

/**
 * Describe class CompositeProductBean here.
 *
 *
 * Created: Sat Sep 24 11:48:22 2005
 *
 * @author <a href="mailto:Mihai Giurgeanu@CRIMIRA"></a>
 * @version 1.0
 */
public abstract class CompositeProductBean  implements EntityBean {

    static private Logger logger = null;
    javax.ejb.EntityContext ejbContext;


    public Integer ejbCreate(Integer id) throws javax.ejb.CreateException{
        logger.log(BasicLevel.DEBUG, "");

        // Init here the bean fields
	setId(id);
        return id;
    }

    public void ejbPostCreate(Integer id) {
        logger.log(BasicLevel.DEBUG, "");
    }

    // CMP fields
    public abstract Integer getId();
    public abstract void setId(Integer id);

    // CMR fields
    public abstract Collection getComponents();
    public abstract void setComponents(Collection products);

    public abstract ProductLocal getProduct();
    public abstract void setProduct(ProductLocal p);

    // Implementation of javax.ejb.EntityBean

    /**
     * Describe <code>setEntityContext</code> method here.
     *
     * @param entityContext an <code>EntityContext</code> value
     * @exception EJBException if an error occurs
     * @exception RemoteException if an error occurs
     */
    public void setEntityContext( EntityContext ctx) throws EJBException, RemoteException {
        if (logger == null) {
            logger = Log.getLogger("ro.kds.erp.data.CompositeProductBean");
        }
        logger.log(BasicLevel.DEBUG, "");
        ejbContext = ctx;

    }

    /**
     * Describe <code>unsetEntityContext</code> method here.
     *
     * @exception EJBException if an error occurs
     * @exception RemoteException if an error occurs
     */
    public  void unsetEntityContext() throws EJBException, RemoteException {

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
    public  void ejbRemove() throws RemoveException, EJBException, RemoteException {

    }

    /**
     * Describe <code>ejbActivate</code> method here.
     *
     * @exception EJBException if an error occurs
     * @exception RemoteException if an error occurs
     */
    public  void ejbActivate() throws EJBException, RemoteException {

    }

    /**
     * Describe <code>ejbPassivate</code> method here.
     *
     * @exception EJBException if an error occurs
     * @exception RemoteException if an error occurs
     */
    public  void ejbPassivate() throws EJBException, RemoteException {

    }

    /**
     * Describe <code>ejbLoad</code> method here.
     *
     * @exception EJBException if an error occurs
     * @exception RemoteException if an error occurs
     */
    public  void ejbLoad() throws EJBException, RemoteException {

    }

    /**
     * Describe <code>ejbStore</code> method here.
     *
     * @exception EJBException if an error occurs
     * @exception RemoteException if an error occurs
     */
    public  void ejbStore() throws EJBException, RemoteException {

    }


}
