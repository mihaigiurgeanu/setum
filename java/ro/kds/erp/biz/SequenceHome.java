package ro.kds.erp.biz;

import javax.ejb.EJBHome;
import javax.ejb.CreateException;
import java.rmi.RemoteException;



/**
 * SequenceHome.java
 *
 *
 * Created: Thu Dec  8 05:03:37 2005
 *
 * @author <a href="mailto:mihai@cris.kds.ro">Mihai Giurgeanu</a>
 * @version $Id: SequenceHome.java,v 1.1 2006/01/09 09:44:13 mihai Exp $
 */

public interface SequenceHome extends EJBHome {
    public Sequence create() throws CreateException, RemoteException;
}// SequenceHome
