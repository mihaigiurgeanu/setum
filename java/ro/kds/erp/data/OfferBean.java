package ro.kds.erp.data;

import javax.ejb.EntityContext;
import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import java.rmi.RemoteException;
import javax.ejb.RemoveException;
import org.objectweb.util.monolog.api.BasicLevel;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;
import javax.naming.Context;
import org.objectweb.util.monolog.api.Logger;
import org.objectweb.jonas.common.Log;
import java.util.Collection;
import java.util.Date;
import javax.naming.NamingException;
import javax.ejb.CreateException;
import javax.ejb.FinderException;

/**
 * CMP 2.0 implementation for OfferEJB entity bean.
 *
 *
 * Created: Sun Oct 23 20:11:15 2005
 *
 * @author <a href="mailto:Mihai Giurgeanu@CRIMIRA"></a>
 * @version 1.0
 */
public abstract class OfferBean implements EntityBean {

    static private Logger logger = null;
    javax.ejb.EntityContext ejbContext;

    /**
     * Create a new Offer entity. It also creates the associated Document.
     *
     * @throws DataLayerException if the document can not be created due to 
     * a <code>NamingException</code>.
     * @throws CreateException if the implementation provided by the
     * application server could not create the entity into the database.
     */
    public Integer ejbCreate() throws CreateException, DataLayerException {
        logger.log(BasicLevel.DEBUG, "");
	
        // Init here the bean fields
	try {
	    InitialContext it = new InitialContext();
	    Context env = (Context)it.lookup("java:comp/env");

	    DocumentLocalHome dh = (DocumentLocalHome)PortableRemoteObject.
		narrow(env.lookup("ejb/DocumentHome"), DocumentLocalHome.class);
	    // create the document here, to give to the offer the
	    // same id as the document
	    DocumentLocal d = dh.create();
	    logger.log(BasicLevel.DEBUG, "Associated document created");
	    logger.log(BasicLevel.DEBUG, "The document's id is " + d.getId());

	    setId(d.getId());
	} catch (NamingException e) {
	    throw new DataLayerException("Naming exception caught. Maybe the name ejb/DocumentHome is not defined.", e);
	}

        return null;
    }

    public void ejbPostCreate() throws DataLayerException {
	// Retrieve the document and make the association with it.
	// It is done in the post create, because the application server
	// needs both beans to exist when creating a relationship.
	try {
	    logger.log(BasicLevel.DEBUG, "Create document association");
	    InitialContext it = new InitialContext();
	    Context env = (Context)it.lookup("java:comp/env");

	    DocumentLocalHome dh = (DocumentLocalHome)PortableRemoteObject.
		narrow(env.lookup("ejb/DocumentHome"), DocumentLocalHome.class);
	    DocumentLocal d = dh.findByPrimaryKey(getId()); /* the offer bean
							       was created with
							       the same id as
							       the document
							    */
	    setDocument(d);
	} catch (NamingException e) {
	    throw new DataLayerException("Naming exception caught. Maybe the name ejb/DocumentHome is not defined.", e);
	} catch (FinderException e) {
	    throw new DataLayerException("The document created with id: " 
					 + getId() + " was not found", e);
	}
    }


    // Persistent fields and relations.

    public abstract Integer getId();
    public abstract void setId(Integer id);

    public abstract String getName();
    public abstract void setName(String offerName);

    public abstract String getDescription();
    public abstract void setDescription(String offerDescription);

    public abstract Date getDateFrom();
    public abstract void setDateFrom(Date dateFrom);

    public abstract Date getDateTo();
    public abstract void setDateTo(Date dateTo);

    public abstract Boolean getDiscontinued();
    public abstract void setDiscontinued(Boolean discontinue);

    public abstract String getComment();
    public abstract void setComment(String comment);

    public abstract Collection getItems();
    public abstract void setItems(Collection items);

    public abstract DocumentLocal getDocument();
    public abstract void setDocument(DocumentLocal d);

    public abstract Integer getCategory();
    public abstract void setCategory(Integer cat);

    public abstract ClientLocal getClient();
    public abstract void setClient(ClientLocal client);

    // Implementation of javax.ejb.EntityBean

    public void setEntityContext(EntityContext entityContext) throws EJBException, RemoteException {

        if (logger == null) {
            logger = Log.getLogger("ro.kds.erp.data.OfferBean");
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
