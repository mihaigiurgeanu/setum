// ProductEC2L.java
package ro.kds.erp.data;

import org.objectweb.jonas.common.Log;
import org.objectweb.util.monolog.api.Logger;
import org.objectweb.util.monolog.api.BasicLevel;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.CreateException;
import java.math.BigDecimal;
import javax.ejb.RemoveException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.ejb.FinderException;

/**
 *
 */
public  abstract class ProductEC2L implements javax.ejb.EntityBean {

    static private Logger logger = null;
    javax.ejb.EntityContext ejbContext;


    public Integer ejbCreate() throws javax.ejb.CreateException{
        logger.log(BasicLevel.DEBUG, "");

        // Init here the bean fields
	setDiscontinued(new Boolean(false));


        return null;
    }

    public void ejbPostCreate() {
        logger.log(BasicLevel.DEBUG, "");
    }

    // ------------------------------------------------------------------
    // Persistent fields
    //
    // ------------------------------------------------------------------
    public abstract Integer getId();
    public abstract void setId(Integer id);

    public abstract String getName();
    public abstract void setName(String name);

    public abstract String getCode();
    public abstract void setCode(String code);

    public abstract BigDecimal getEntryPrice();
    public abstract void setEntryPrice(BigDecimal entryPrice);
    
    public abstract BigDecimal getSellPrice();
    public abstract void setSellPrice(BigDecimal sellPrice);
    
    public abstract BigDecimal getPrice1();
    public abstract void setPrice1(BigDecimal price1);

    public abstract BigDecimal getPrice2();
    public abstract void setPrice2(BigDecimal price1);

    public abstract BigDecimal getPrice3();
    public abstract void setPrice3(BigDecimal price1);

    public abstract BigDecimal getPrice4();
    public abstract void setPrice4(BigDecimal price1);

    public abstract BigDecimal getPrice5();
    public abstract void setPrice5(BigDecimal price1);

    public abstract float getPriceCorrectionP();
    public abstract void setPriceCorrectionP(float percent);

    public abstract String getDescription();
    public abstract void setDescription(String description);
    
    public abstract String getDescription1();
    public abstract void setDescription1(String description);

    public abstract Boolean getDiscontinued();
    public abstract void setDiscontinued(Boolean discontinued);

    // ------------------------------------------------------------------
    // CMR fields
    //
    // ------------------------------------------------------------------
    public abstract Collection getAttributes();
    public abstract void setAttributes(Collection attribs);

    public abstract CompositeProductLocal getCompositeProduct();
    public abstract void setCompositeProduct(CompositeProductLocal p);

    public abstract CategoryLocal getCategory();
    public abstract void setCategory(CategoryLocal category);

    public abstract Collection getParents();
    public abstract void setParents(Collection composites);



    // ------------------------------------------------------------------
    // Business methods
    //
    // ------------------------------------------------------------------
    /**
     * Convenience method. Gets the attributes collection of this product
     * and builds a map of attributes name-attribute.
     */
    public Map getAttributesMap() {
	HashMap attribsMap = new HashMap();
	Collection attribs = getAttributes();

	for(Iterator i = attribs.iterator(); i.hasNext(); ) {
	    AttributeLocal a = (AttributeLocal)i.next();
	    attribsMap.put(a.getName(), a);
	}

	return attribsMap;
    }


    // ------------------------------------------------------------------
    // Select methods
    // ------------------------------------------------------------------
    


    // ------------------------------------------------------------------
    // Standard call back methods
    // ------------------------------------------------------------------


    public void setEntityContext(javax.ejb.EntityContext ctx) {
        if (logger == null) {
            logger = Log.getLogger("ro.kds.erp.data.ProductEC2L");
        }
        logger.log(BasicLevel.DEBUG, "");
        ejbContext = ctx;
    }


    public void unsetEntityContext() {
        logger.log(BasicLevel.DEBUG, "");
        ejbContext = null;
    }

    public void ejbRemove() throws javax.ejb.RemoveException {
        logger.log(BasicLevel.DEBUG, "");
    }


    public void ejbLoad() {
        logger.log(BasicLevel.DEBUG, "");
    }


    public void ejbStore() {
        logger.log(BasicLevel.DEBUG, "");
    }


    public void ejbPassivate() {
        logger.log(BasicLevel.DEBUG, "");
    }


    public void ejbActivate() {
        logger.log(BasicLevel.DEBUG, "");
    }

}



