package ro.kds.erp.data;

import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import java.rmi.RemoteException;
import javax.ejb.RemoveException;
import org.objectweb.util.monolog.api.BasicLevel;
import org.objectweb.util.monolog.api.Logger;
import org.objectweb.jonas.common.Log;
import java.util.Date;
import javax.ejb.CreateException;

/**
 * CMP 2.0 implementation of the DocumentEJB entity bean. It encapsulates
 * the minimal data persistence logic of a document. Examples of
 * documents: offer, order, invoice.
 *
 *
 * Created: Sun Oct 23 17:54:21 2005
 *
 * @author <a href="mailto:Mihai Giurgeanu@CRIMIRA"></a>
 * @version 1.0
 */
public abstract class DocumentBean implements EntityBean {

    static private Logger logger = null;
    javax.ejb.EntityContext ejbContext;

    public Integer ejbCreate() throws CreateException {
        logger.log(BasicLevel.DEBUG, "");
	
        // Init here the bean fields
	setDate(new Date());
	setIsDraft(new Boolean(true));
        return null;
    }

    public void ejbPostCreate() {
        logger.log(BasicLevel.DEBUG, "");
    }

    // Persistent status


    public abstract Integer getId();
    public abstract void setId(Integer id);
    
    public abstract String getNumber();
    public abstract void setNumber(String number);
    
    public abstract Date getDate();
    public abstract void setDate(Date d);

    public abstract Boolean getIsDraft();
    public abstract void setIsDraft(Boolean isDraft);
    


    // Implementation of javax.ejb.EntityBean

    public void setEntityContext(EntityContext entityContext) throws EJBException, RemoteException {

        if (logger == null) {
            logger = Log.getLogger("ro.kds.erp.data.DocumentBean");
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
