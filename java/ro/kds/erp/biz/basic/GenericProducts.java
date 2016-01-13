package ro.kds.erp.biz.basic;

import java.rmi.RemoteException;
import javax.ejb.EJBObject;
import javax.ejb.FinderException;
import ro.kds.erp.biz.ResponseBean;

/**
 * GenericProducts remote interface.
 *
 * Class was automaticaly generated from a template.
 *
 */
public interface GenericProducts extends EJBObject {
        
    public ResponseBean newFormData() throws RemoteException;
    public ResponseBean saveFormData() throws RemoteException;
    public ResponseBean loadFormData(Integer id) throws RemoteException, FinderException;

    /**
     * Access to the form data.
     */
     public GenericProductsForm getForm() throws RemoteException;

    public ResponseBean newProductData() throws RemoteException;
    public ResponseBean saveProductData() throws RemoteException;
    public ResponseBean loadProductData(Integer id) throws RemoteException, FinderException;
    public ResponseBean newAttributeData() throws RemoteException;
    public ResponseBean saveAttributeData() throws RemoteException;
    public ResponseBean loadAttributeData(Integer id) throws RemoteException, FinderException;

    public ResponseBean getCurrentFormData() throws RemoteException;
    public ResponseBean getLoadedPrimaryKey() throws RemoteException;

    public ResponseBean updateCategoryId(Integer newCategoryId) throws RemoteException;
    public ResponseBean updateCategoryName(String newCategoryName) throws RemoteException;
    public ResponseBean updateProductId(Integer newProductId) throws RemoteException;
    public ResponseBean updateProductName(String newProductName) throws RemoteException;
    public ResponseBean updateProductDescription(String newProductDescription) throws RemoteException;
    public ResponseBean updateProductCode(String newProductCode) throws RemoteException;
    public ResponseBean updateProductEntryPrice(java.math.BigDecimal newProductEntryPrice) throws RemoteException;
    public ResponseBean updateProductSellPrice(java.math.BigDecimal newProductSellPrice) throws RemoteException;
    public ResponseBean updateProductPrice1(java.math.BigDecimal newProductPrice1) throws RemoteException;
    public ResponseBean updateProductPrice2(java.math.BigDecimal newProductPrice2) throws RemoteException;
    public ResponseBean updateProductPrice3(java.math.BigDecimal newProductPrice3) throws RemoteException;
    public ResponseBean updateProductPrice4(java.math.BigDecimal newProductPrice4) throws RemoteException;
    public ResponseBean updateProductPrice5(java.math.BigDecimal newProductPrice5) throws RemoteException;
    public ResponseBean updateAttrId(Integer newAttrId) throws RemoteException;
    public ResponseBean updateAttrName(String newAttrName) throws RemoteException;
    public ResponseBean updateAttrString(String newAttrString) throws RemoteException;
    public ResponseBean updateAttrInt(Integer newAttrInt) throws RemoteException;
    public ResponseBean updateAttrDecimal(java.math.BigDecimal newAttrDecimal) throws RemoteException;
    public ResponseBean updateAttrDouble(Double newAttrDouble) throws RemoteException;

    public ResponseBean loadCategories() throws RemoteException;
    public ResponseBean loadProducts() throws RemoteException;
    public ResponseBean loadAttributes() throws RemoteException;
    public ResponseBean removeCategory() throws RemoteException;
    public ResponseBean removeProduct() throws RemoteException;
    public ResponseBean removeAttribute() throws RemoteException;

}
