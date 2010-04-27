package ro.kds.erp.data;

import javax.ejb.RemoveException;
import javax.ejb.EntityBean;
import javax.ejb.EJBException;
import javax.ejb.EntityContext;
import java.rmi.RemoteException;
import org.objectweb.util.monolog.api.BasicLevel;
import org.objectweb.jonas.common.Log;
import org.objectweb.util.monolog.api.Logger;
import javax.ejb.CreateException;
import java.util.Collection;



/**
 * ClientBean.java
 *
 *
 * Created: Tue Jan 17 04:48:33 2006
 *
 * @author <a href="mailto:mihai@cris.kds.ro">Mihai Giurgeanu</a>
 * @version 1.0
 */
public abstract class ClientBean implements EntityBean {
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

    // Persistent fields and relationships
    public abstract Integer getId();
    public abstract void setId(Integer newId);

    public abstract boolean getIsCompany();
    public abstract void setIsCompany(boolean isCompany);
    
    public abstract String getFirstName();
    public abstract void setFirstName(String firstName);
    
    public abstract String getLastName();
    public abstract void setLastName(String lastName);

    public abstract String getCompanyName();
    public abstract void setCompanyName(String compayName);

    public abstract String getAddress();
    public abstract void setAddress(String address);

    public abstract String getPostalCode();
    public abstract void setPostalCode(String postalCode);

    public abstract String getCity();
    public abstract void setCity(String city);

    public abstract String getCountryCode();
    public abstract void setCountryCode(String countryCode);

    public abstract String getCompanyCode();
    public abstract void setCompanyCode(String companyCode);

    public abstract String getPhone();
    public abstract void setPhone(String phone);

    public abstract String getIban();
    public abstract void setIban(String iban);
    
    public abstract String getBank();
    public abstract void setBank(String bank);

    public abstract String getComment();
    public abstract void setComment(String comment);

    public abstract Collection getContacts();
    public abstract void setContacts(Collection contacts);

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

    /**
     * Convenience method to build the name of the client
     */
    public String getName() {
	if(getIsCompany()) {
	    return getCompanyName();
	} else {
	    return getLastName() + " " + getFirstName();
	}
    }

} // ClientBean
