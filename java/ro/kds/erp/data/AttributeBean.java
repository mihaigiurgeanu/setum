package ro.kds.erp.data;

import javax.ejb.EntityBean;
import javax.ejb.CreateException;
import javax.ejb.RemoveException;
import javax.ejb.EntityContext;
import org.objectweb.util.monolog.api.BasicLevel;
import org.objectweb.util.monolog.api.Logger;

import org.objectweb.util.monolog.api.BasicLevel;
import java.math.BigDecimal;
import org.objectweb.jonas.common.Log;

import org.objectweb.jonas.common.Log;



public abstract class AttributeBean implements EntityBean {

    
    static private Logger logger = null;
    javax.ejb.EntityContext ejbContext;

    public  Integer ejbCreate() throws CreateException {
        logger.log(BasicLevel.DEBUG, "");
	
        // Init here the bean fields

        return null;
    }

    public void ejbPostCreate() {
        logger.log(BasicLevel.DEBUG, "");
    }

    public  Integer ejbCreate(String name, String value) throws CreateException {
        logger.log(BasicLevel.DEBUG, "");
	setName(name);
	setStringValue(value);
        // Init here the bean fields

        return null;
    }

    public void ejbPostCreate(String name, String value) {
        logger.log(BasicLevel.DEBUG, "");
    }

    public  Integer ejbCreate(String name, Integer value) throws CreateException {
        logger.log(BasicLevel.DEBUG, "");
	
        // Init here the bean fields
	setName(name);
	setIntValue(value);

        return null;
    }

    public void ejbPostCreate(String name, Integer value) {
        logger.log(BasicLevel.DEBUG, "");
    }

    public  Integer ejbCreate(String name, BigDecimal value) throws CreateException {
        logger.log(BasicLevel.DEBUG, "");
	
        // Init here the bean fields
	setName(name);
	setDecimalValue(value);

        return null;
    }

    public void ejbPostCreate(String name, BigDecimal value) {
        logger.log(BasicLevel.DEBUG, "");
    }

    public  Integer ejbCreate(String name, Double value) throws CreateException {
        logger.log(BasicLevel.DEBUG, "");
	
        // Init here the bean fields
	setName(name);
	setDecimalValue(new BigDecimal(value.doubleValue()));

        return null;
    }

    public void ejbPostCreate(String name, Double value) {
        logger.log(BasicLevel.DEBUG, "");
    }


    public  Integer ejbCreate(String name, Boolean value) throws CreateException {
        logger.log(BasicLevel.DEBUG, "");
	
        // Init here the bean fields
	setName(name);
	setBoolValue(value);

        return null;
    }

    public void ejbPostCreate(String name, Boolean value) {
        logger.log(BasicLevel.DEBUG, "");
    }

    public  Integer ejbCreate(String name, ProductLocal value) 
	throws CreateException {
        logger.log(BasicLevel.DEBUG, "");
	
        // Init here the bean fields
	setName(name);

        return null;
    }

    public void ejbPostCreate(String name, ProductLocal value) {
        logger.log(BasicLevel.DEBUG, "");
	setProduct(value);
    }

    // ------------------------------------------------------------------
    // Persistent fields
    //
    // ------------------------------------------------------------------
    public abstract Integer getId();
    public abstract void setId(Integer id);

    public abstract String getName();
    public abstract void setName(String name);

    public abstract String getStringValue();
    public abstract void setStringValue(String attrValue);

    public abstract Integer getIntValue();
    public abstract void setIntValue(Integer attrValue);
    
    public abstract BigDecimal getDecimalValue();
    public abstract void setDecimalValue(BigDecimal attrValue);

    public abstract ProductLocal getProduct();
    public abstract void setProduct(ProductLocal p);

    public abstract Boolean getBoolValue();
    public abstract void setBoolValue(Boolean attrValue);

    public Double getDoubleValue() {
	return new Double(getDecimalValue().doubleValue());
    }
    
    public void setDoubleValue(Double attrValue) {
	setDecimalValue(new BigDecimal(attrValue.doubleValue()));
    }

    // ------------------------------------------------------------------
    // Standard call back methods
    // ------------------------------------------------------------------

    public  void ejbActivate() {
        logger.log(BasicLevel.DEBUG, "");
    }

    public  void ejbPassivate() {
        logger.log(BasicLevel.DEBUG, "");
    }

    public  void ejbLoad() {
        logger.log(BasicLevel.DEBUG, "");
    }

    public  void ejbStore() {
        logger.log(BasicLevel.DEBUG, "");
    }

    public  void ejbRemove() throws RemoveException {
        logger.log(BasicLevel.DEBUG, "");
    }

    public  void setEntityContext(final EntityContext ctx) {
        if (logger == null) {
            logger = Log.getLogger("ro.kds.erp.data.AttributeBean");
        }
        logger.log(BasicLevel.DEBUG, "");
        ejbContext = ctx;
    }

    public  void unsetEntityContext() {
        logger.log(BasicLevel.DEBUG, "");
        ejbContext = null;
    }

} // AttributeBean
