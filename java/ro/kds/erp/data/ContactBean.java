package ro.kds.erp.data;

import javax.ejb.RemoveException;
import javax.ejb.EntityBean;
import javax.ejb.EJBException;
import javax.ejb.EntityContext;
import java.rmi.RemoteException;
import org.objectweb.util.monolog.api.Logger;
import org.objectweb.jonas.common.Log;
import org.objectweb.util.monolog.api.BasicLevel;
import javax.ejb.CreateException;



/**
 * ContactBean.java
 *
 *
 * Created: Tue Jan 17 04:57:35 2006
 *
 * @author <a href="mailto:mihai@cris.kds.ro">Mihai Giurgeanu</a>
 * @version 1.0
 */
public abstract class ContactBean implements EntityBean {
    static private Logger logger = null;
    javax.ejb.EntityContext ejbContext;

    public Integer ejbCreate() throws CreateException {
	return null;
    }
    public void ejbPostCreate() throws CreateException {
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
            logger = Log.getLogger("ro.kds.erp.data.ClientBean");
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

    // Persistent fields and relations
    public abstract Integer getId();
    public abstract void setId(Integer newId);

    public abstract String getFirstName();
    public abstract void setFirstName(String firstName);
    
    public abstract String getLastName();
    public abstract void setLastName(String lastName);

    public abstract String getPhone();
    public abstract void setPhone(String phone);

    public abstract String getMobile();
    public abstract void setMobile(String mobile);

    public abstract String getFax();
    public abstract void setFax(String fax);

    public abstract String getEmail();
    public abstract void setEmail(String email);

    public abstract String getDepartment();
    public abstract void setDepartment(String department);

    public abstract String getTitle();
    public abstract void setTitle(String title);

    public abstract String getComment();
    public abstract void setComment(String comment);

} // ContactBean
