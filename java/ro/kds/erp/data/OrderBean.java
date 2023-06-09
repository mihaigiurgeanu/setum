package ro.kds.erp.data;

import java.rmi.RemoteException;
import javax.ejb.EJBException;
import javax.ejb.EntityContext;
import javax.ejb.EntityBean;
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
import java.util.Date;
import java.util.Iterator;

/**
 * Describe class OrderBean here.
 *
 *
 * Created: Sun Sep 17 12:31:29 2006
 *
 * @author <a href="mailto:Mihai Giurgeanu@MIHAIG">U-MIHAIG\Mihai Giurgeanu</a>
 * @version 1.0
 */
public abstract class OrderBean implements EntityBean {

    static private Logger logger = null;
    javax.ejb.EntityContext ejbContext;



    /**
     * Bean creation, no params required.
     */
    public Integer ejbCreate() throws CreateException, DataLayerException {
	try {
	    InitialContext it = new InitialContext();
	    Context env = (Context)it.lookup("java:comp/env");

	    DocumentLocalHome dh = (DocumentLocalHome)PortableRemoteObject.
		narrow(env.lookup("ejb/DocumentHome"), DocumentLocalHome.class);
	    // create the document here, to give to the order the
	    // same id as the document
	    DocumentLocal d = dh.create();
	    logger.log(BasicLevel.DEBUG, "Associated document created");
	    logger.log(BasicLevel.DEBUG, "The document's id is " + d.getId());

	    setId(d.getId());
	} catch (NamingException e) {
	    logger.log(BasicLevel.DEBUG, "Naming exception caught. Possible ejb/DocumentHome is not defined");
	    throw new DataLayerException(e.getMessage(), e);
	}

        return null;
    }


    /**
     * Initialize the relations for <code>document</code> and 
     * <code>deliveryConditions</code>.
     */
    public void ejbPostCreate() throws DataLayerException {
	// Retrieve the document and make the association with it.
	// It is done in the post create, because the application server
	// needs both beans to exist when creating a relationship.
	InitialContext ic;
	Context env;

	try {
	    ic = new InitialContext();
	    env = (Context)ic.lookup("java:comp/env");
	} catch (Exception e) {
	    logger.log(BasicLevel.ERROR, "Can not initialize naming service");
	    throw new DataLayerException(e);
	}

	try {
	    logger.log(BasicLevel.DEBUG, "Create document association");
	    DocumentLocalHome dh = (DocumentLocalHome)PortableRemoteObject.
		narrow(env.lookup("ejb/DocumentHome"), DocumentLocalHome.class);
	    DocumentLocal d = dh.findByPrimaryKey(getId()); /* the order bean
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



    // Data fields and relations
    
    
    public abstract Integer getId();
    public abstract void setId(Integer id);

    public abstract DocumentLocal getDocument();
    public abstract void setDocument(DocumentLocal doc);

    public abstract ClientLocal getClient();
    public abstract void setClient(ClientLocal client);

    public abstract Integer getInstallation();
    public abstract void setInstallation(Integer code);

    public abstract Integer getDeliveryLocation();
    public abstract void setDeliveryLocation(Integer code);

    public abstract String getDeliveryLocationOther();
    public abstract void setDeliveryLocationOther(String location);

    public abstract BigDecimal getDeliveryDistance();
    public abstract void setDeliveryDistance(BigDecimal distance);

    public abstract String getDeliveryComments();
    public abstract void setDeliveryComments(String comments);

    public abstract BigDecimal getDiscount();
    public abstract void setDiscount(BigDecimal disount);

    public abstract BigDecimal getAdvancePayment();
    public abstract void setAdvancePayment(BigDecimal amount);

    public abstract String getAdvanceDocument();
    public abstract void setAdvanceDocument(String document);

    public abstract Date getDeliveryTerm();
    public abstract void setDeliveryTerm(Date term);

    public abstract Date getDeliveryTerm1();
    public abstract void setDeliveryTerm1(Date term);

    public abstract String getDeliveryAddress();
    public abstract void setDeliveryAddress(String address);
    
    public abstract String getDeliveryAddressHint();
    public abstract void setDeliveryAddressHint(String hint);

    public abstract String getDeliveryPhone();
    public abstract void setDeliveryPhone(String phone);

    public abstract String getDeliveryContact();
    public abstract void setDeliveryContact(String contact);

    public abstract Date getDeliveryDate();
    public abstract void setDeliveryDate(Date dd);

    public abstract String getDeliveryHour();
    public abstract void setDeliveryHour(String hour);

    public abstract String getTipDemontare();
    public abstract void setTipDemontare(String tipDemontare);

    public abstract String getAttribute1();
    public abstract void setAttribute1(String attr);

    public abstract String getAttribute2();
    public abstract void setAttribute2(String attr);

    public abstract String getAttribute3();
    public abstract void setAttribute3(String attr);

    public abstract String getAttribute4();
    public abstract void setAttribute4(String attr);

    public abstract String getAttribute5();
    public abstract void setAttribute5(String attr);


    public abstract Collection getLines();
    public abstract void setLines(Collection lines);

    public abstract Collection getProformas();
    public abstract void setProformas(Collection proformas);

    public abstract Collection getInvoices();
    public abstract void setInvoices(Collection invoices);


    public abstract BigDecimal ejbSelectSumOfPayments(Integer id) throws FinderException;
    public abstract BigDecimal ejbSelectInvoicedAmount(Integer id) throws FinderException;
    public abstract Collection ejbSelectPayments(Integer id) throws FinderException;
    public abstract Collection ejbFindLivrari(Date start, Date end) throws FinderException;
    public abstract Collection ejbFindLivrariCuMontaj(Date start, Date end) throws FinderException;
    public abstract Collection ejbFindLivrariFaraMontaj(Date start, Date end) throws FinderException;

    public BigDecimal getInvoicedAmount() throws FinderException {
	BigDecimal amount = ejbSelectInvoicedAmount(getId());
	if(amount == null) {
	    return new BigDecimal(0);
	} else {
	    return amount;
	}
    }
    
    public BigDecimal getPayedAmount() throws FinderException {
	BigDecimal amount = ejbSelectSumOfPayments(getId());
	if(amount == null) {
	    return new BigDecimal(0);
	} else {
	    return amount;
	}
    }

    public BigDecimal getCurrencyInvoicedAmount() throws FinderException {
	BigDecimal amount = new BigDecimal(0);
	Collection invoices = getInvoices();
	for(Iterator i = invoices.iterator(); i.hasNext();) {
	    InvoiceLocal inv = (InvoiceLocal)i.next();
	    amount = amount.add(new BigDecimal(inv.getAmount().doubleValue()/inv.getExchangeRate().doubleValue()));
	}
	return amount.setScale(2,  BigDecimal.ROUND_HALF_UP);
    }
    
    public BigDecimal getCurrencyPayedAmount() throws FinderException {
	BigDecimal amount = new BigDecimal(0);
	/*
	OrderLocalHome orderHome;
	try {
	    InitialContext ic = new InitialContext();
	    Context env = (Context) ic.lookup("java:comp/env");
	    orderHome = (OrderLocalHome) PortableRemoteObject.
		narrow(env.lookup("ejb/OrderHome"), OrderLocalHome.class);
	} catch (NamingException e) {
	    logger.log(BasicLevel.WARN, "Can not get home for the name ejb/OrderHome: " + e.getMessage());
	    logger.log(BasicLevel.DEBUG, e);
	    throw new FinderException("NamingException occured: " + e.getMessage());
	}
	*/
	Collection payments = ejbSelectPayments(getId());
	for(Iterator i = payments.iterator(); i.hasNext();) {
	    PaymentLocal payment = (PaymentLocal)i.next();
	    amount = amount.add(new BigDecimal(payment.getAmount().doubleValue()/payment.getExchangeRate().doubleValue()));
	}
	return amount.setScale(2,  BigDecimal.ROUND_HALF_UP);
    }

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
