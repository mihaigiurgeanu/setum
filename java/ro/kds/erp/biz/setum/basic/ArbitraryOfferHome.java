package ro.kds.erp.biz.setum.basic;

import java.rmi.RemoteException;
import java.util.Collection;
import javax.ejb.CreateException;
import javax.ejb.EJBHome;

/**
 * ArbitraryOffer home interface.
 *
 * Class was automaticaly generated from a template.
 *
 */
public interface ArbitraryOfferHome extends EJBHome {        
    ArbitraryOffer create() throws CreateException, RemoteException;
}
