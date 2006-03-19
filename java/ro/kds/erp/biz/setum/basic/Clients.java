package ro.kds.erp.biz.setum.basic;

import java.rmi.RemoteException;
import javax.ejb.EJBObject;
import javax.ejb.FinderException;
import ro.kds.erp.biz.ResponseBean;

/**
 * Clients remote interface.
 *
 * Class was automaticaly generated from a template.
 *
 */
public interface Clients extends EJBObject {
        
    public ResponseBean newFormData() throws RemoteException;
    public ResponseBean saveFormData() throws RemoteException;
    public ResponseBean loadFormData(Integer id) throws RemoteException, FinderException;


    public ResponseBean getCurrentFormData() throws RemoteException;
    public ResponseBean getLoadedPrimaryKey() throws RemoteException;

    public ResponseBean updateIsCompany(Integer newIsCompany) throws RemoteException;
    public ResponseBean updateFirstName(String newFirstName) throws RemoteException;
    public ResponseBean updateLastName(String newLastName) throws RemoteException;
    public ResponseBean updateCompanyName(String newCompanyName) throws RemoteException;
    public ResponseBean updateAddress(String newAddress) throws RemoteException;
    public ResponseBean updatePostalCode(String newPostalCode) throws RemoteException;
    public ResponseBean updateCity(String newCity) throws RemoteException;
    public ResponseBean updateCountryCode(String newCountryCode) throws RemoteException;
    public ResponseBean updateCompanyCode(String newCompanyCode) throws RemoteException;
    public ResponseBean updatePhone(String newPhone) throws RemoteException;
    public ResponseBean updateIban(String newIban) throws RemoteException;
    public ResponseBean updateBank(String newBank) throws RemoteException;
    public ResponseBean updateComment(String newComment) throws RemoteException;
    public ResponseBean updateContactFirstName(String newContactFirstName) throws RemoteException;
    public ResponseBean updateContactLastName(String newContactLastName) throws RemoteException;
    public ResponseBean updateContactDepartment(String newContactDepartment) throws RemoteException;
    public ResponseBean updateContactPhone(String newContactPhone) throws RemoteException;
    public ResponseBean updateContactMobile(String newContactMobile) throws RemoteException;
    public ResponseBean updateContactFax(String newContactFax) throws RemoteException;
    public ResponseBean updateContactEmail(String newContactEmail) throws RemoteException;
    public ResponseBean updateContactTitle(String newContactTitle) throws RemoteException;
    public ResponseBean updateContactComment(String newContactComment) throws RemoteException;

    public ResponseBean loadListing(Integer isCompany) throws RemoteException;
    public ResponseBean contactsListing() throws RemoteException;
    public ResponseBean loadSubForm(Integer loadId) throws RemoteException;
    public ResponseBean addNewContact() throws RemoteException;
    public ResponseBean removeContact() throws RemoteException;
    public ResponseBean saveSubForm() throws RemoteException;
    public java.util.Collection clientsCollectionMap() throws RemoteException;
}
