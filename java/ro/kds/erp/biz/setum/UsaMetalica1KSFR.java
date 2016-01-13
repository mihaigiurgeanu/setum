// UsaMetalica1KSFR.java
// Stateful Session Bean

package ro.kds.erp.biz.setum;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.RemoveException;
import javax.ejb.EJBObject;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.SessionSynchronization;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.transaction.NotSupportedException;
import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.objectweb.jonas.common.Log;
import org.objectweb.util.monolog.api.Logger;
import org.objectweb.util.monolog.api.BasicLevel;
import ro.kds.erp.biz.ResponseBean;
import javax.ejb.FinderException;
import java.math.BigDecimal;
import ro.kds.erp.data.ProductLocal;
import ro.kds.erp.data.ProductLocalHome;
import javax.rmi.PortableRemoteObject;
import ro.kds.erp.data.AttributeLocalHome;
import java.util.Collection;
import ro.kds.erp.data.CategoryLocalHome;
import ro.kds.erp.data.CategoryLocal;
import ro.kds.erp.data.AttributeLocal;
import java.util.Iterator;

/**
 *
 */
public class UsaMetalica1KSFR implements SessionBean, SessionSynchronization {

    static private Logger logger = null;
    SessionContext ejbContext;

    Integer id;

    // ------------------------------------------------------------------
    // SessionSynchronization implementation
    // ------------------------------------------------------------------


    public void afterBegin() {
        logger.log(BasicLevel.DEBUG, "");
    }



    public void beforeCompletion() {
        logger.log(BasicLevel.DEBUG, "");
    }


    public void afterCompletion(boolean committed) {
        logger.log(BasicLevel.DEBUG, "");
    }


    // ------------------------------------------------------------------
    // SessionBean implementation
    // ------------------------------------------------------------------


    public void setSessionContext(SessionContext ctx) {
        if (logger == null) {
            logger = Log.getLogger("ro.kds.erp.biz.setum.UsamMetalica1KSFR");
        }
        logger.log(BasicLevel.DEBUG, "");
        ejbContext = ctx;
    }


    public void ejbRemove() {
        logger.log(BasicLevel.DEBUG, "");
    }


    public void ejbCreate() throws CreateException {
	id = null;
	um = null;
        logger.log(BasicLevel.DEBUG, "");
    }

    public void ejbPassivate() {
        logger.log(BasicLevel.DEBUG, "");
    }

    public void ejbActivate() {
        logger.log(BasicLevel.DEBUG, "");
    }

    // ------------------------------------------------------------------
    // UsaMetalica1K implementation
    // ------------------------------------------------------------------

    UsaMetalicaBean um;

    /**
     * Initialization of a new product. On calling save method, the
     * product will be added to the database.
     */
    public ResponseBean newProduct() {
	um = new UsaMetalicaBean();
	ResponseBean r = new ResponseBean();
	r.addRecord();
	id = null; // a new product will be added
	
	// set values of the calculated fields
	ResponseBean validation = applyRules();
	
	// build response
	r.addField("validationCode", validation.getCode());
	copyFieldsToResponse(r);
	return r;
    }

    /**
     * Updating or inserting a product into the database.
     */
    public ResponseBean saveProduct()  {
	ResponseBean r = new ResponseBean();
	logger.log(BasicLevel.DEBUG, "Saving product with id = " + id);

	// validation
	try {
	    InitialContext it = new InitialContext();
	    Context env = (Context)it.lookup("java:comp/env");

	    ProductLocal p;
	    ProductLocalHome ph = (ProductLocalHome)
		PortableRemoteObject
		.narrow(env.lookup("ejb/ProductLocalHome"), 
			ProductLocalHome.class);
	    AttributeLocalHome ah = (AttributeLocalHome)
		PortableRemoteObject
		.narrow(env.lookup("ejb/AttributeLocalHome"),
			AttributeLocalHome.class);

	    if(id == null) {
		// add a new product
		logger.log(BasicLevel.INFO, "Adding new product");
		p = ph.create();
		Integer catId = (Integer)env.lookup("categoryId");
		CategoryLocalHome ch = (CategoryLocalHome)
		    PortableRemoteObject
		    .narrow(env.lookup("ejb/CategoryLocalHome"),
			    CategoryLocalHome.class);
		CategoryLocal c = ch.findByPrimaryKey(catId);
		p.setCategory(c);
		id = p.getId();
		logger.log(BasicLevel.INFO, "New product id = " + id);
	    } else {
		p = ph.findByPrimaryKey(id);
	    }
	    
	    p.setName(um.getVersion() + "-" + um.getSubclass());
	    Collection attribs = p.getAttributes();
	    attribs.clear();
	    attribs.add(ah.create("subclass", um.getSubclass()));
	    attribs.add(ah.create("version", um.getVersion()));
	    attribs.add(ah.create("material", new Integer(um.getMaterial())));
	    attribs.add(ah.create("lg", new BigDecimal(um.getLg())));
	    attribs.add(ah.create("hg", new BigDecimal(um.getHg())));
	    attribs.add(ah.create("lcorrection", new BigDecimal(um.getLcorrection())));
	    attribs.add(ah.create("hcorrection", new BigDecimal(um.getHcorrection())));
	    attribs.add(ah.create("intFoil", new Integer(um.getIntFoil())));
	    attribs.add(ah.create("ieFoil", new Integer(um.getIeFoil())));
	    attribs.add(ah.create("extFoil", new Integer(um.getExtFoil())));
	    attribs.add(ah.create("isolation", new Integer(um.getIsolation())));
	    attribs.add(ah.create("openeningDir", new Integer(um.getOpeningDir())));
	    attribs.add(ah.create("openingSide", new Integer(um.getOpeningSide())));
	    attribs.add(ah.create("frameType", new Integer(um.getFrameType())));
	    attribs.add(ah.create("lFrame", new BigDecimal(um.getLFrame())));
	    attribs.add(ah.create("bFrame", new BigDecimal(um.getBFrame())));
	    attribs.add(ah.create("cFrame", new BigDecimal(um.getCFrame())));
	    attribs.add(ah.create("foilPosition", new Integer(um.getFoilPosition())));
	    attribs.add(ah.create("tresholdType", new Integer(um.getTresholdType())));
	    attribs.add(ah.create("lTreshold", new BigDecimal(um.getLTreshold())));
	    attribs.add(ah.create("hTreshold", new BigDecimal(um.getHTreshold())));
	    attribs.add(ah.create("cTreshold", new BigDecimal(um.getCTreshold())));
	    attribs.add(ah.create("tresholdSpace", new Integer(um.getTresholdSpace())));
	    attribs.add(ah.create("h1Treshold", new BigDecimal(um.getH1Treshold())));
	    attribs.add(ah.create("h2Treshold", new BigDecimal(um.getH2Treshold())));
	    r.setCode(0);
	} catch (Exception e) {
	    r.setCode(1);
	    r.setMessage(e.getMessage());
	    r.setMessage("Save operation failed: " + e);
	    e.printStackTrace();
	}

	return r;
    }

    /**
     * Loads a product from the database.
     */
    public ResponseBean loadProduct(Integer loadId) throws FinderException {
	um = new UsaMetalicaBean();
	ResponseBean r;
	logger.log(BasicLevel.DEBUG, "Loading product with id = " + loadId);
	id = loadId;
	try {
	    r = new ResponseBean();
	    r.addRecord();

	    InitialContext it = new InitialContext();
	    Context env = (Context)it.lookup("java:comp/env");

	    ProductLocalHome ph = (ProductLocalHome)
		PortableRemoteObject
		.narrow(env.lookup("ejb/ProductLocalHome"), 
			ProductLocalHome.class);
	    ProductLocal p = ph.findByPrimaryKey(id);
	    Collection attribs = p.getAttributes();
	    for(Iterator i=attribs.iterator(); i.hasNext(); ) {
		AttributeLocal a = (AttributeLocal) i.next();
		if(a.getName().compareTo("subclass") == 0) {
		    um.setSubclass(a.getStringValue());
		} else if(a.getName().compareTo("version") == 0) {
		    um.setVersion(a.getStringValue());
		} else if(a.getName().compareTo("material") == 0) {
		    um.setMaterial(a.getIntValue().intValue());
		} else if(a.getName().compareTo("lg") == 0) {
		    um.setLg(a.getDecimalValue().doubleValue());
		} else if(a.getName().compareTo("hg") == 0) {
		    um.setHg(a.getDecimalValue().doubleValue());
		} else if(a.getName().compareTo("lcorrection") == 0) {
		    um.setLcorrection(a.getDecimalValue().doubleValue());
		} else if(a.getName().compareTo("hcorrection") == 0) {
		    um.setHcorrection(a.getDecimalValue().doubleValue());
		} else if(a.getName().compareTo("intFoil") == 0) {
		    um.setIntFoil(a.getIntValue().intValue());
		} else if(a.getName().compareTo("ieFoil") == 0) {
		    um.setIeFoil(a.getIntValue().intValue());
		} else if(a.getName().compareTo("extFoil") == 0) {
		    um.setExtFoil(a.getIntValue().intValue());
		} else if(a.getName().compareTo("isolation") == 0) {
		    um.setIsolation(a.getIntValue().intValue());
		} else if(a.getName().compareTo("openingDir") == 0) {
		    um.setOpeningDir(a.getIntValue().intValue());
		} else if(a.getName().compareTo("openingSide") == 0) {
		    um.setOpeningSide(a.getIntValue().intValue());
		} else if(a.getName().compareTo("frameType") == 0) {
		    um.setFrameType(a.getIntValue().intValue());
		} else if(a.getName().compareTo("lFrame") == 0) {
		    um.setLFrame(a.getDecimalValue().doubleValue());
		} else if(a.getName().compareTo("bFrame") == 0) {
		    um.setBFrame(a.getDecimalValue().doubleValue());
		} else if(a.getName().compareTo("cFrame") == 0) {
		    um.setCFrame(a.getDecimalValue().doubleValue());
		} else if(a.getName().compareTo("foilPosition") == 0) {
		    um.setFoilPosition(a.getIntValue().intValue());
		} else if(a.getName().compareTo("tresholdType") == 0) {
		    um.setTresholdType(a.getIntValue().intValue());
		} else if(a.getName().compareTo("lTreshold") == 0) {
		    um.setLTreshold(a.getDecimalValue().doubleValue());
		} else if(a.getName().compareTo("hTreshold") == 0) {
		    um.setHTreshold(a.getDecimalValue().doubleValue());
		} else if(a.getName().compareTo("cTreshold") == 0) {
		    um.setCTreshold(a.getDecimalValue().doubleValue());
		} else if(a.getName().compareTo("tresholdSpace") == 0) {
		    um.setTresholdSpace(a.getIntValue().intValue());
		} else if(a.getName().compareTo("h1Treshold") == 0) {
		    um.setH1Treshold(a.getDecimalValue().doubleValue());
		} else if(a.getName().compareTo("h2Treshold") == 0) {
		    um.setH2Treshold(a.getDecimalValue().doubleValue());
		}
		// set the values of calculated fields
		ResponseBean validation = applyRules();

		// build the response
		r.addField("validationCode", validation.getCode());
		copyFieldsToResponse(r);
	    }

	} catch (NamingException e) {
	    logger.log(BasicLevel.ERROR, e);
	    r = new ResponseBean();
	    r.setCode(1);
	    r.setMessage("ERROR: Configuration problem (jndi name not found)");
	}
	
	return r;
    }

    public ResponseBean updateSubclass(String s)  {
	ResponseBean r = new ResponseBean();
	um.setSubclass(s);
	return r;
    }

    public ResponseBean updateVersion(String s)  {
	ResponseBean r = new ResponseBean();
	um.setVersion(s);
	return r;
    }
    public ResponseBean updateMaterial(Integer matCode)  {
	ResponseBean r = new ResponseBean();
	um.setMaterial(matCode.intValue());
	return r;
    }
    public ResponseBean updateLg(Double lg)  {
	ResponseBean r = new ResponseBean();
	um.setLg(lg.doubleValue());
	applyRules();
	r.addRecord();
	r.addField("le", um.getLe());
	return r;
    }
    public ResponseBean updateHg(Double hg)  {
	ResponseBean r = new ResponseBean();
	um.setHg(hg.doubleValue());
	applyRules();
	r.addRecord();
	r.addField("he", um.getHe());
	return r;
    }
    public ResponseBean updateLcorrection(Double lcorrection)  {
	ResponseBean r = new ResponseBean();
	um.setLcorrection(lcorrection.doubleValue());
	applyRules();
	r.addRecord();
	r.addField("le", um.getLe());
	return r;
    }
    public ResponseBean updateHcorrection(Double hcorrection)  {
	ResponseBean r = new ResponseBean();
	um.setHcorrection(hcorrection.doubleValue());
	applyRules();
	r.addRecord();
	r.addField("he", um.getHe());
	return r;
    }
    public ResponseBean updateIntFoil(Integer intFoil)  {
	ResponseBean r = new ResponseBean();
	um.setIntFoil(intFoil.intValue());
	return r;
    }
    public ResponseBean updateIeFoil(Integer ieFoil)  {
	ResponseBean r = new ResponseBean();
	um.setIeFoil(ieFoil.intValue());
	return r;
    }
    public ResponseBean updateExtFoil(Integer extFoil)  {
	ResponseBean r = new ResponseBean();
	um.setExtFoil(extFoil.intValue());
	return r;
    }
    public ResponseBean updateIsolation(Integer isolation)  {
	ResponseBean r = new ResponseBean();
	um.setIsolation(isolation.intValue());
	return r;
    }
    public ResponseBean updateOpeningDir(Integer od)  {
	ResponseBean r = new ResponseBean();
	um.setOpeningDir(od.intValue());
	return r;
    }
    public ResponseBean updateOpeningSide(Integer os)  {
	ResponseBean r = new ResponseBean();
	um.setOpeningSide(os.intValue());
	return r;
    }
    public ResponseBean updateFrameType(Integer ft)  {
	ResponseBean r = new ResponseBean();
	um.setFrameType(ft.intValue());
	return r;
    }
    public ResponseBean updateLFrame(Double lFrame)  {
	ResponseBean r = new ResponseBean();
	um.setLFrame(lFrame.doubleValue());
	return r;
    }
    public ResponseBean updateBFrame(Double bFrame)  {
	ResponseBean r = new ResponseBean();
	um.setBFrame(bFrame.doubleValue());
	return r;
    }
    public ResponseBean updateCFrame(Double cFrame)  {
	ResponseBean r = new ResponseBean();
	um.setCFrame(cFrame.doubleValue());
	return r;
    }
    public ResponseBean updateFoilPosition(Integer fp)  {
	ResponseBean r = new ResponseBean();
	um.setFoilPosition(fp.intValue());
	return r;
    }
    public ResponseBean updateTresholdType(Integer tt)  {
	ResponseBean r = new ResponseBean();
	um.setTresholdType(tt.intValue());
	return r;
    }
    public ResponseBean updateLTreshold(Double lt)  {
	ResponseBean r = new ResponseBean();
	um.setLTreshold(lt.doubleValue());
	return r;
    }
    public ResponseBean updateHTreshold(Double ht)  {
	ResponseBean r = new ResponseBean();
	um.setHTreshold(ht.doubleValue());
	return r;
    }
    public ResponseBean updateCTreshold(Double ct)  {
	ResponseBean r = new ResponseBean();
	um.setCTreshold(ct.doubleValue());
	return r;
    }
    public ResponseBean updateTresholdSpace(Integer ts)  {
	ResponseBean r = new ResponseBean();
	um.setTresholdSpace(ts.intValue());
	return r;
    }
    public ResponseBean updateH1Treshold(Double h1)  {
	ResponseBean r = new ResponseBean();
	um.setH1Treshold(h1.doubleValue());
	return r;
    }
    public ResponseBean updateH2Treshold(Double h2)  {
	ResponseBean r = new ResponseBean();
	um.setH2Treshold(h2.doubleValue());
	return r;
    }

    /**
     * Get the fields stored internaly and adds them to the response.
     */
    void copyFieldsToResponse(ResponseBean r) {
	r.addField("subclass", um.getSubclass());
	r.addField("version", um.getVersion());
	r.addField("material", um.getMaterial());
	r.addField("lg", um.getLg());
	r.addField("hg", um.getHg());
	r.addField("le", um.getLe());
	r.addField("he", um.getHe());
	r.addField("lcorrection", um.getLcorrection());
	r.addField("hcorrection", um.getHcorrection());
	r.addField("intFoil", um.getIntFoil());
	r.addField("extFoil", um.getExtFoil());
	r.addField("ieFoil", um.getIeFoil());
	r.addField("isolation", um.getIsolation());
	r.addField("openingDir", um.getOpeningDir());
	r.addField("openingSide", um.getOpeningSide());
	r.addField("frameType", um.getFrameType());
	r.addField("lFrame", um.getLFrame());
	r.addField("bFrame", um.getBFrame());
	r.addField("cFrame", um.getCFrame());
	r.addField("foilPosition", um.getFoilPosition());
	r.addField("tresholdType", um.getTresholdType());
	r.addField("lTreshold", um.getLTreshold());
	r.addField("hTreshold", um.getHTreshold());
	r.addField("cTreshold", um.getCTreshold());
	r.addField("tresholdSpace", um.getTresholdSpace());
	r.addField("h1Treshold", um.getH1Treshold());
	r.addField("h2Treshold", um.getH2Treshold());
    }

    /**
     * Applies all the rules to the current form values. The rules
     * are validation rules and rules for calculated fields. After
     * the execution of these method, all calculated fields of the
     * form will have values.
     *
     * @returns a <code>ResponseBean</code> having code 0 if no error
     * has been encountered and no records. 
     * If a validation rule is broken, the response
     * code is the id of the last validation rule broken, and the records
     * collection will contain records with ruleid and errmsg fields for
     * each broken rule.
     */
    ResponseBean applyRules() {
	ResponseBean r = new ResponseBean();

	// calculated fields
	um.setLe(um.getLg() + um.getLcorrection() - 20);
	um.setHe(um.getHg() + um.getHcorrection() - 10);
	
	// validation rules
	if(um.getIntFoil() == 1 && um.getLg() < 500) {
	    r.setCode(1);
	    r.setMessage("Pentru foaie Lisa Lminim = 500");
	    r.addRecord();
	    r.addField("ruleid", 1);
	    r.addField("errmsg", "Pentru foaie Lisa Lminim = 500");
	}

	if(um.getIntFoil() == 1 && um.getLg() > 1080) {
	    r.setCode(2);
	    r.setMessage("Pentru foaie Lisa Lmaxim = 1080");
	    r.addRecord();
	    r.addField("ruleid", 2);
	    r.addField("errmsg", "Pentru foaie Lisa Lmaxim = 1080");
	}

	if(um.getIntFoil() == 1 && um.getHg() < 500) {
	    r.setCode(3);
	    r.setMessage("Pentru foaie Lisa Hminim = 500");
	    r.addRecord();
	    r.addField("ruleid", 3);
	    r.addField("errmsg", "Pentru foaie Lisa Hminim = 500");
	}

	if(um.getIntFoil() == 1 && um.getHg() > 2400) {
	    r.setCode(4);
	    r.setMessage("Pentru foaie Lisa Hmaxim = 2400");
	    r.addRecord();
	    r.addField("ruleid", 4);
	    r.addField("errmsg", "Pentru foaie Lisa Hmaxim = 2400");
	}

	if(um.getIntFoil() == 2 && um.getLg() < 780) {
	    r.setCode(5);
	    r.setMessage("Pentru foaie amprentata Lminim = 780");
	    r.addRecord();
	    r.addField("ruleid", 5);
	    r.addField("errmsg", "Pentru foaie amprentata Lminim = 780");
	}

	if(um.getIntFoil() == 2 && um.getLg() > 1740) {
	    r.setCode(6);
	    r.setMessage("Pentru foaie amprentata Lmaxim = 1740");
	    r.addRecord();
	    r.addField("ruleid", 6);
	    r.addField("errmsg", "Pentru foaie amprentata Lmaxim = 1740");
	}

	if(um.getIntFoil() == 2 && um.getHg() < 1080) {
	    r.setCode(7);
	    r.setMessage("Pentru foaie amprentata Hminim = 1080");
	    r.addRecord();
	    r.addField("ruleid", 7);
	    r.addField("errmsg", "Pentru foaie amprentata Hminim = 1080");
	}

	if(um.getIntFoil() == 2 && um.getHg() > 2150) {
	    r.setCode(8);
	    r.setMessage("Pentru foaie amprentata Hmaxim = 2150");
	    r.addRecord();
	    r.addField("ruleid", 8);
	    r.addField("errmsg", "Pentru foaie amprentata Hmaxim = 2150");
	}
	return r;
    }
}

