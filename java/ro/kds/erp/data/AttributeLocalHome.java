package ro.kds.erp.data;

import javax.ejb.EJBLocalHome;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import java.util.Collection;
import java.math.BigDecimal;



public interface AttributeLocalHome extends javax.ejb.EJBLocalHome {
    AttributeLocal create() throws CreateException;
    AttributeLocal create(String name, String value) throws CreateException;
    AttributeLocal create(String name, Integer value) throws CreateException;
    AttributeLocal create(String name, BigDecimal value) throws CreateException;
    AttributeLocal create(String name, Double value) throws CreateException;
    AttributeLocal create(String name, ProductLocal value) throws CreateException;
    AttributeLocal findByPrimaryKey(java.lang.Integer pk) throws FinderException;
} // AttributeLocalHome
