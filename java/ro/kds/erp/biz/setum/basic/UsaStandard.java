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
    public ResponseBean updateBroascaId(Integer newBroascaId) throws RemoteException;
    public ResponseBean updateCilindruId(Integer newCilindruId) throws RemoteException;
    public ResponseBean updateSildId(Integer newSildId) throws RemoteException;
    public ResponseBean updateYallaId(Integer newYallaId) throws RemoteException;
    public ResponseBean updateVizorId(Integer newVizorId) throws RemoteException;

    public ResponseBean loadListing(Integer startRow) throws RemoteException;
    public ResponseBean getListingLength() throws RemoteException;
    public void recalculatePrices() throws RemoteException;
    public ResponseBean removeProductDefinition() throws RemoteException;

}
