package ro.kds.erp.data;

import javax.ejb.EntityBean;
import javax.ejb.RemoveException;
import java.rmi.RemoteException;
import javax.ejb.EJBException;
import javax.ejb.EntityContext;
import javax.ejb.EntityBean;
import javax.ejb.CreateException;
import org.objectweb.util.monolog.api.BasicLevel;
import java.math.BigDecimal;
import org.objectweb.util.monolog.api.Logger;
import org.objectweb.jonas.common.Log;
import java.util.Date;
import javax.ejb.FinderException;
import java.util.Collection;


/**
 * Describe class DailySummaryBean here.
 *
 *
 * Created: Sat Feb 27 01:21:55 2010
 *
 * @author <a href="mailto:mihai@mihaigiurgeanu.local">Mihai Giurgeanu</a>
 * @version 1.0
 */
public abstract class DailySummaryBean implements EntityBean {

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
            logger = Log.getLogger("ro.kds.erp.data.DailySummaryBean");
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

    }

    /**
     * Describe <code>ejbRemove</code> method here.
     *
     * @exception RemoveException if an error occurs
     * @exception EJBException if an error occurs
     * @exception RemoteException if an error occurs
     */
    public void ejbRemove() throws RemoveException, EJBException, RemoteException {

    }

    /**
     * Describe <code>ejbActivate</code> method here.
     *
     * @exception EJBException if an error occurs
     * @exception RemoteException if an error occurs
     */
    public void ejbActivate() throws EJBException, RemoteException {

    }

    /**
     * Describe <code>ejbPassivate</code> method here.
     *
     * @exception EJBException if an error occurs
     * @exception RemoteException if an error occurs
     */
    public void ejbPassivate() throws EJBException, RemoteException {

    }

    /**
     * Describe <code>ejbLoad</code> method here.
     *
     * @exception EJBException if an error occurs
     * @exception RemoteException if an error occurs
     */
    public void ejbLoad() throws EJBException, RemoteException {

    }

    /**
     * Describe <code>ejbStore</code> method here.
     *
     * @exception EJBException if an error occurs
     * @exception RemoteException if an error occurs
     */
    public void ejbStore() throws EJBException, RemoteException {

    }

    //Persistent fields and relations.

    public abstract Integer getId();
    public abstract void setId(Integer id);

    public abstract Date getDate();
    public abstract void setDate(Date day);

    public abstract String getTypeURI();
    public abstract void setTypeURI(String type);

    public abstract BigDecimal getValue1();
    public abstract void setValue1(BigDecimal value);
    
    public abstract BigDecimal getValue2();
    public abstract void setValue2(BigDecimal value);
    
    public abstract BigDecimal getValue3();
    public abstract void setValue3(BigDecimal value);
    
    public abstract BigDecimal getValue4();
    public abstract void setValue4(BigDecimal value);
    
    public abstract BigDecimal getValue5();
    public abstract void setValue5(BigDecimal value);
    
    public abstract BigDecimal getValue6();
    public abstract void setValue6(BigDecimal value);
    
    public abstract BigDecimal getValue7();
    public abstract void setValue7(BigDecimal value);
    
    public abstract BigDecimal getValue8();
    public abstract void setValue8(BigDecimal value);
    
    public abstract BigDecimal getValue9();
    public abstract void setValue9(BigDecimal value);
    
    public abstract BigDecimal getValue10();
    public abstract void setValue10(BigDecimal value);
 
    public abstract Collection ejbFindByDate(String typeURI, Date day) throws FinderException;
}
