package ro.kds.erp.biz.setum.basic;

import java.rmi.RemoteException;
import javax.ejb.EJBObject;
import javax.ejb.FinderException;
import ro.kds.erp.biz.ResponseBean;

/**
 * UsaStdNeechipata remote interface.
 *
 * Class was automaticaly generated from a template.
 *
 */
public interface UsaStdNeechipata extends EJBObject {
        
    public ResponseBean newFormData() throws RemoteException;
    public ResponseBean saveFormData() throws RemoteException;
    public ResponseBean loadFormData(Integer id) throws RemoteException, FinderException;
    public ResponseBean getCurrentFormData() throws RemoteException;
    public ResponseBean getLoadedPrimaryKey() throws RemoteException;

    public ResponseBean updateName(String newName) throws RemoteException;
    public ResponseBean updateCode(String newCode) throws RemoteException;
    public ResponseBean updateDescription(String newDescription) throws RemoteException;
    public ResponseBean updatePrice(java.math.BigDecimal newPrice) throws RemoteException;

    public ResponseBean loadListing() throws RemoteException;
}