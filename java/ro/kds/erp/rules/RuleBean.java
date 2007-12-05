package ro.kds.erp.rules;

import javax.ejb.EJBException;
import javax.ejb.EntityContext;
import javax.ejb.EntityBean;
import javax.ejb.RemoveException;
import java.rmi.RemoteException;
import javax.ejb.CreateException;
import org.objectweb.util.monolog.api.BasicLevel;
import org.objectweb.util.monolog.api.Logger;
import org.objectweb.jonas.common.Log;
import javax.ejb.FinderException;
import java.util.Collection;

/**
 * <code>Rule</code> bean implementation.
 *
 *
 * Created: Tue Nov  6 00:15:26 2007
 *
 * @author <a href="mailto:mihai@mihai-giurgeanus-computer.local">Mihai Giurgeanu</a>
 * @version 1.0
 */
public abstract class RuleBean implements EntityBean {

    static private Logger logger = null;
    javax.ejb.EntityContext ejbContext;

    public Integer ejbCreate() throws CreateException {
	logger.log(BasicLevel.DEBUG, "");
	return null;
    }

    public void ejbPostCreate() {
	logger.log(BasicLevel.DEBUG, "");
    }

    public abstract Integer getId();
    public abstract void setId(Integer id);
    
    public abstract String getName();
    public abstract void setName(String name);

    public abstract String getCondition();
    public abstract void setCondition(String condition);
    
    public abstract String getMessage();
    public abstract void setMessage(String msg);

    public abstract Boolean getErrorFlag();
    public abstract void setErrorFlag(Boolean error);

    abstract public Collection ejbFindByName(String name) throws FinderException;

    /**
     * Creates a new <code>RuleBean</code> instance.
     *
     */
    public RuleBean() {

    }

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
            logger = Log.getLogger("ro.kds.erp.rules.RuleBean");
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
    public void unsetEntityContext() throws EJBException, RemoteException {
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
    public void ejbRemove() throws RemoveException, EJBException, RemoteException {

	logger.log(BasicLevel.DEBUG, "");
    }

    /**
     * Describe <code>ejbActivate</code> method here.
     *
     * @exception EJBException if an error occurs
     * @exception RemoteException if an error occurs
     */
    public void ejbActivate() throws EJBException, RemoteException {

	logger.log(BasicLevel.DEBUG, "");
    }

    /**
     * Describe <code>ejbPassivate</code> method here.
     *
     * @exception EJBException if an error occurs
     * @exception RemoteException if an error occurs
     */
    public void ejbPassivate() throws EJBException, RemoteException {

	logger.log(BasicLevel.DEBUG, "");
    }

    /**
     * Describe <code>ejbLoad</code> method here.
     *
     * @exception EJBException if an error occurs
     * @exception RemoteException if an error occurs
     */
    public void ejbLoad() throws EJBException, RemoteException {

	logger.log(BasicLevel.DEBUG, "");
    }

    /**
     * Describe <code>ejbStore</code> method here.
     *
     * @exception EJBException if an error occurs
     * @exception RemoteException if an error occurs
     */
    public void ejbStore() throws EJBException, RemoteException {

	logger.log(BasicLevel.DEBUG, "");
    }

}
