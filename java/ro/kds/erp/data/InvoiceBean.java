package ro.kds.erp.data;

import javax.ejb.EntityContext;
import javax.ejb.EntityBean;
import java.rmi.RemoteException;
import javax.ejb.EJBException;
import javax.ejb.RemoveException;
import javax.ejb.CreateException;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;
import org.objectweb.util.monolog.api.BasicLevel;
import javax.naming.NamingException;
import javax.ejb.FinderException;
import java.math.BigDecimal;
import javax.naming.Context;
import org.objectweb.util.monolog.api.Logger;
import org.objectweb.jonas.common.Log;
import java.util.Collection;

/**
 * Describe class InvoiceBean here.
 *
 *
 * Created: Fri Oct 27 23:01:01 2006
 *
 * @author <a href="mailto:Mihai Giurgeanu@MIHAIG">U-MIHAIG\Mihai Giurgeanu</a>
 * @version 1.0
 */
public abstract class InvoiceBean implements EntityBean {


    static private Logger logger = null;
    javax.ejb.EntityContext ejbContext;


    /**
     * Bean creation.
     */
    public Integer ejbCreate() throws CreateException, DataLayerException {
	try {
	    InitialContext ic = new InitialContext();
	    Context env = (Context)ic.lookup("java:comp/env");
	    
	    DocumentLocalHome dh = (DocumentLocalHome)PortableRemoteObject.
		narrow(env.lookup("ejb/DocumentHome"), DocumentLocalHome.class);
	    // create document here and give the same id to
	    // the invoice.
	    DocumentLocal d = dh.create();
	    logger.log(BasicLevel.DEBUG, "Associated document created.");
	    logger.log(BasicLevel.DEBUG, "the document's id is " + d.getId());
	    
	    setId(d.getId());
	} catch (NamingException e) {
	    logger.log(BasicLevel.DEBUG, "Naming exception caught: " + e.getMessage());
	    throw new DataLayerException(e.getMessage(), e);
	}

	return null;
    }

    /**
     * Records the relation between document and invoice.
     */
    public void ejbPostCreate() throws DataLayerException {
	InitialContext ic;
	Context env;

	try {
	    ic = new InitialContext();
	    env = (Context) ic.lookup("java:comp/env");
	} catch (Exception e) {
	    logger.log(BasicLevel.ERROR, "Can not initialize naming service");
	    logger.log(BasicLevel.DEBUG, e);
	    throw new DataLayerException(e);
	}

	try {
	    DocumentLocalHome dh = (DocumentLocalHome)PortableRemoteObject.
		narrow(env.lookup("ejb/DocumentHome"), DocumentLocalHome.class);
	    DocumentLocal d = dh.findByPrimaryKey(getId());

	    setDocument(d);
	} catch (NamingException e) {
	    logger.log(BasicLevel.DEBUG,e);
	    throw new DataLayerException(e.getMessage(), e);
	} catch (FinderException e) {
	    throw new DataLayerException("The document created with id: " +
					 getId() + " was not founde", e);
	}
    }



    // Data fields and relations


    public abstract Integer getId();
    public abstract void setId(Integer id);

    public abstract DocumentLocal getDocument();
    public abstract void setDocument(DocumentLocal doc);

    public abstract OrderLocal getOrder();
    public abstract void setOrder(OrderLocal o);

    public abstract String getRole();
    public abstract void setRole(String role);

    public abstract BigDecimal getAmount();
    public abstract void setAmount(BigDecimal amount);
    
    public abstract BigDecimal getTax();
    public abstract void setTax(BigDecimal tax);
    
    public abstract Collection getPayments();
    public abstract void setPayments(Collection payments);


    public abstract BigDecimal ejbSelectSumOfPayments(Integer id) throws FinderException;

    public BigDecimal getSumOfPayments() throws FinderException {
	BigDecimal amount = ejbSelectSumOfPayments(getId());
	if(amount == null) {
	    return new BigDecimal(0);
	} else {
	    return amount;
	}
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
	if(logger == null) {
	    logger = Log.getLogger("ro.kds.erp.data.InvoiceBean");
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
