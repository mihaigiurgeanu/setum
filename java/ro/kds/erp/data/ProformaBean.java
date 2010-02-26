package ro.kds.erp.data;

import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.CreateException;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;
import org.objectweb.util.monolog.api.BasicLevel;
import javax.naming.NamingException;
import javax.ejb.FinderException;
import java.math.BigDecimal;
import javax.ejb.RemoveException;
import javax.naming.Context;
import org.objectweb.util.monolog.api.Logger;
import org.objectweb.jonas.common.Log;



public abstract class ProformaBean implements EntityBean {

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

    public abstract Double getExchangeRate();
    public abstract void setExchangeRate(Double rate);

    public abstract Boolean getUsePercent();
    public abstract void setUsePercent(Boolean usePercent);

    public abstract String getComment();
    public abstract void setComment(String comment);

    public abstract String getContract();
    public abstract void setContract(String contract);
    
    public abstract String getObiectiv();
    public abstract void setObiectiv(String obiectiv);

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

    public abstract String getAttribute6();
    public abstract void setAttribute6(String attr);

    public abstract String getAttribute7();
    public abstract void setAttribute7(String attr);

    public abstract String getAttribute8();
    public abstract void setAttribute8(String attr);

    public abstract String getAttribute9();
    public abstract void setAttribute9(String attr);

    public abstract String getAttribute10();
    public abstract void setAttribute10(String attr);

    public abstract String getAttribute11();
    public abstract void setAttribute11(String attr);

    public abstract String getAttribute12();
    public abstract void setAttribute12(String attr);

    public abstract String getAttribute13();
    public abstract void setAttribute13(String attr);

    public abstract String getAttribute14();
    public abstract void setAttribute14(String attr);

    public abstract String getAttribute15();
    public abstract void setAttribute15(String attr);


    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public void ejbLoad() {
    }

    public void ejbStore() {
    }

    public void ejbRemove() throws RemoveException {
	logger.log(BasicLevel.DEBUG, "");
    }

    public void setEntityContext(EntityContext ctx) {
	if(logger == null) {
	    logger = Log.getLogger("ro.kds.erp.data.InvoiceBean");
	}
	
	logger.log(BasicLevel.DEBUG, "");
	ejbContext = ctx;
    }

    public void unsetEntityContext() {
	logger.log(BasicLevel.DEBUG, "");
	ejbContext = null;
    }

} // ProformaBean
