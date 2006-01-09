package ro.kds.erp.biz;

import java.rmi.RemoteException;
import javax.ejb.SessionContext;
import javax.ejb.SessionBean;
import javax.ejb.EJBException;
import javax.ejb.CreateException;



/**
 * Implementation of Sequence bean logic.
 * It stores the current values of the sequence in the system preferences.
 *
 *
 * Created: Thu Dec  8 05:08:39 2005
 *
 * @author <a href="mailto:mihai@cris.kds.ro">Mihai Giurgeanu</a>
 * @version $Id: SequenceBean.java,v 1.1 2006/01/09 09:44:13 mihai Exp $
 */
public class SequenceBean implements SessionBean {
    
    // Implementation of javax.ejb.SessionBean

    public void setSessionContext(SessionContext sessionContext) throws EJBException, RemoteException {
	
    }

    public void ejbRemove() throws EJBException, RemoteException {
	
    }

    public void ejbPassivate() throws EJBException, RemoteException {
	
    }

    public void ejbActivate() throws EJBException, RemoteException {
	
    }

    public void ejbCreate() throws CreateException {
	
    }
    
    public Integer getNext(String seqName) {
	try {
	    synchronized(SequenceBean.class) {
		Preferences prefs = PreferencesBean.getPreferences();
		Integer cur = prefs.getInteger(seqName, new Integer(1000));
		Integer next = new Integer(cur.intValue() + 1);
		prefs.put(seqName, next);
		return next;
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	    return new Integer(0);
	}
    }
    
    public Integer getCur(String seqName) {
	try {
	    Preferences prefs = PreferencesBean.getPreferences();
	    Integer cur = prefs.getInteger(seqName, new Integer(1000));
	    return cur;
	} catch (Exception e) {
	    e.printStackTrace();
	    return new Integer(0);
	}
    }

    public void setCur(String seqName, Integer cur) {
	try {
	    synchronized(SequenceBean.class) {
		Preferences prefs = PreferencesBean.getPreferences();
		prefs.put(seqName, cur);
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

} // SequenceBean
