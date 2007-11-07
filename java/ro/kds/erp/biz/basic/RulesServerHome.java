package ro.kds.erp.biz.basic;

import java.rmi.RemoteException;
import java.util.Collection;
import javax.ejb.CreateException;
import javax.ejb.EJBHome;

/**
 * RulesServer home interface.
 *
 * Class was automaticaly generated from a template.
 *
 */
public interface RulesServerHome extends EJBHome {        
    RulesServer create() throws CreateException, RemoteException;
}
