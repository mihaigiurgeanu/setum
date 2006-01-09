package ro.kds.erp.biz;

import javax.ejb.EJBObject;
import java.rmi.RemoteException;



/**
 * Sequence.java
 *
 * Stateless session bean used for generating numbers in sequence. The
 * numbers are used for creating codes for products or for assigning 
 * numbers to offers or orders.
 *
 * Created: Thu Dec  8 04:54:48 2005
 *
 * @author <a href="mailto:mihai@cris.kds.ro">Mihai Giurgeanu</a>
 * @version 1.0
 */

public interface Sequence extends EJBObject {
    /**
     * Returns the next number in sequence. The current sequence number
     * is incremented.
     */
    public Integer getNext(String sequenceName) throws RemoteException;

    /**
     * Returns the current number in sequence. There is no side effect.
     */
    public Integer getCur(String sequenceName) throws RemoteException;

    /**
     * Sets the current number in sequence.
     */
    public void setCur(String sequenceName, Integer value) throws RemoteException;
}// Sequence
