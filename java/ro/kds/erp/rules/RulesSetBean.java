package ro.kds.erp.rules;

import javax.ejb.RemoveException;
import javax.ejb.EntityBean;
import javax.ejb.EJBException;
import javax.ejb.EntityContext;
import java.rmi.RemoteException;
import javax.ejb.CreateException;
import org.objectweb.util.monolog.api.BasicLevel;
import javax.ejb.FinderException;
import org.objectweb.util.monolog.api.Logger;
import java.util.Collection;
import org.objectweb.jonas.common.Log;

/**
 * RulesSet implementation.
 *
 *
 * Created: Thu Oct 25 08:37:50 2007
 *
 * @author <a href="mailto:mihai@mihai-giurgeanus-computer.local">Mihai Giurgeanu</a>
 * @version 1.0
 */
public abstract class RulesSetBean implements EntityBean {


    static private Logger logger = null;
    javax.ejb.EntityContext ejbContext;


    public Integer ejbCreate() throws CreateException {
	logger.log(BasicLevel.DEBUG, "");
	return null;
    }

    public void ejbPostCreate() {
	logger.log(BasicLevel.DEBUG, "");
    }



    abstract public Integer getId();
    abstract public void setId(Integer id);

    abstract public String getName();
    abstract public void setName(String name);

    abstract public Collection getRules();
    abstract public void setRules(Collection rules);

    abstract public Collection ejbFindByName(String name) throws FinderException;
    abstract public Collection ejbFindAll() throws FinderException;

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
            logger = Log.getLogger("ro.kds.erp.rules.RulesSetBean");
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

