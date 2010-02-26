package ro.kds.erp.data;

import javax.ejb.EJBLocalHome;
import javax.ejb.CreateException;
import javax.ejb.FinderException;



public interface ProformaLocalHome extends javax.ejb.EJBLocalHome {
    public ProformaLocal create() throws CreateException, DataLayerException;
    public ProformaLocal findByPrimaryKey(Integer id) throws FinderException;
} // ProformaLocalHome
