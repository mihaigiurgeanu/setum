package ro.kds.erp.biz.setum.basic;

import java.rmi.RemoteException;
import javax.ejb.EJBObject;
import javax.ejb.FinderException;
import ro.kds.erp.biz.ResponseBean;

/**
 * UsaStandard remote interface.
 *
 * Class was automaticaly generated from a template.
 *
 */
public interface UsaStandard extends EJBObject {
        
    public ResponseBean newFormData() throws RemoteException;
    public ResponseBean saveFormData() throws RemoteException;
    public ResponseBean loadFormData(Integer id) throws RemoteException, FinderException;
    public ResponseBean getCurrentFormData() throws RemoteException;
    public ResponseBean getLoadedPrimaryKey() throws RemoteException;

    public ResponseBean updateName(String newName) throws RemoteException;
    public ResponseBean updateCode(String newCode) throws RemoteException;
    public ResponseBean updateUsaId(Integer newUsaId) throws RemoteException;
    public ResponseBean updateBroascaId(String newBroascaId) throws RemoteException;
    public ResponseBean updateCilindruId(String newCilindruId) throws RemoteException;
    public ResponseBean updateSildId(String newSildId) throws RemoteException;
    public ResponseBean updateYallaId(String newYallaId) throws RemoteException;
    public ResponseBean updateVizorId(String newVizorId) throws RemoteException;

    public ResponseBean loadListing() throws RemoteException;
}
