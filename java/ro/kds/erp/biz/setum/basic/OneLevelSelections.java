package ro.kds.erp.biz.setum.basic;

import java.rmi.RemoteException;
import javax.ejb.EJBObject;
import javax.ejb.FinderException;
import ro.kds.erp.biz.ResponseBean;

/**
 * OneLevelSelections remote interface.
 *
 * Class was automaticaly generated from a template.
 *
 */
public interface OneLevelSelections extends EJBObject {
        
    public ResponseBean newFormData() throws RemoteException;
    public ResponseBean saveFormData() throws RemoteException;
    public ResponseBean loadFormData(Integer id) throws RemoteException, FinderException;

    /**
     * Access to the form data.
     */
     public OneLevelSelectionsForm getForm() throws RemoteException;


    public ResponseBean getCurrentFormData() throws RemoteException;
    public ResponseBean getLoadedPrimaryKey() throws RemoteException;

    public ResponseBean updateId(Integer newId) throws RemoteException;
    public ResponseBean updateCode(String newCode) throws RemoteException;
    public ResponseBean updateName(String newName) throws RemoteException;
    public ResponseBean updateDescription(String newDescription) throws RemoteException;

    public ResponseBean loadListing(Integer startRow) throws RemoteException;
    public ResponseBean getListingLength() throws RemoteException;
    public ResponseBean productsListing() throws RemoteException;
    public ResponseBean selectProduct(Integer productId) throws RemoteException;
    public ResponseBean addProduct(Integer productId) throws RemoteException;
    public ResponseBean removeProduct() throws RemoteException;

}
