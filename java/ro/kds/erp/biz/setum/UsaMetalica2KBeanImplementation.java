package ro.kds.erp.biz.setum;

import ro.kds.erp.biz.ResponseBean;
import org.objectweb.util.monolog.api.BasicLevel;
import javax.naming.InitialContext;
import ro.kds.erp.data.ProductLocal;
import ro.kds.erp.data.ProductLocalHome;
import javax.rmi.PortableRemoteObject;
import ro.kds.erp.data.AttributeLocalHome;
import ro.kds.erp.data.CategoryLocalHome;
import ro.kds.erp.data.CategoryLocal;
import javax.ejb.FinderException;
import ro.kds.erp.data.AttributeLocal;
import javax.naming.NamingException;
import javax.naming.Context;
import java.util.Collection;
import java.util.Iterator;

/**
 * Business logic implementation of the genrated UsaMetalica2K EJB. It
 * implements the business logic associated with data entry of a certain
 * kind of product.
 *
 *
 * Created: Fri Nov 18 15:34:24 2005
 *
 * @author <a href="mailto:Mihai Giurgeanu@CRIMIRA"></a>
 * @version 1.0
 */
public class UsaMetalica2KBeanImplementation 
    extends ro.kds.erp.biz.setum.basic.UsaMetalica2KBean {

    protected ProductLocal fereastra;
    protected ProductLocal grilaVentilatie;
    protected ProductLocal gauriAerisire;

    /**
     * Save data to the database.
     *
     * @return a <code>ResponseBean</code> value
     */
    public ResponseBean saveFormData() {
	ResponseBean r;
	logger.log(BasicLevel.DEBUG, "Saving product with id = " + id);

	try {
	    InitialContext it = new InitialContext();
	    Context env = (Context)it.lookup("java:comp/env");

	    ProductLocal p;
	    ProductLocalHome ph = (ProductLocalHome)PortableRemoteObject.
		narrow(env.lookup("ejb/ProductLocalHome"), 
		       ProductLocalHome.class);
	    AttributeLocalHome ah = (AttributeLocalHome)PortableRemoteObject.
		narrow(env.lookup("ejb/AttributeLocalHome"),
		       AttributeLocalHome.class);

	    if(id == null) {
		// add a new product
		logger.log(BasicLevel.INFO, "Adding new product");
		p = ph.create();
		Integer catId = (Integer)env.lookup("categoryId");
		CategoryLocalHome ch = (CategoryLocalHome)PortableRemoteObject.
		    narrow(env.lookup("ejb/CategoryLocalHome"),
			   CategoryLocalHome.class);
		CategoryLocal c = ch.findByPrimaryKey(catId);
		p.setCategory(c);
		id = p.getId();
		logger.log(BasicLevel.INFO, "New product id = " + id);
	    } else {
		p = ph.findByPrimaryKey(id);
	    }
	    
	    p.setName(form.getVersion() + "-" + form.getSubclass());
	    Collection attribs = p.getAttributes();
	    attribs.clear();
	    attribs.add(ah.create("subclass", form.getSubclass()));
	    attribs.add(ah.create("version", form.getVersion()));
	    attribs.add(ah.create("material", form.getMaterial()));
	    attribs.add(ah.create("lg", form.getLg()));
	    attribs.add(ah.create("hg", form.getHg()));
	    attribs.add(ah.create("lcorrection", form.getLcorrection()));
	    attribs.add(ah.create("hcorrection", form.getHcorrection()));
	    attribs.add(ah.create("lCurrent", form.getLCurrent()));
	    attribs.add(ah.create("kType", form.getKType()));
	    attribs.add(ah.create("intFoil", form.getIntFoil()));
	    attribs.add(ah.create("ieFoil", form.getIeFoil()));
	    attribs.add(ah.create("extFoil", form.getExtFoil()));
	    attribs.add(ah.create("intFoilSec", form.getIntFoilSec()));
	    attribs.add(ah.create("ieFoilSec", form.getIeFoilSec()));
	    attribs.add(ah.create("extFoilSec", form.getExtFoilSec()));
	    attribs.add(ah.create("isolation", form.getIsolation()));
	    attribs.add(ah.create("openeningDir", form.getOpeningDir()));
	    attribs.add(ah.create("openingSide", form.getOpeningSide()));
	    attribs.add(ah.create("frameType", form.getFrameType()));
	    attribs.add(ah.create("lFrame", form.getLFrame()));
	    attribs.add(ah.create("bFrame", form.getBFrame()));
	    attribs.add(ah.create("cFrame", form.getCFrame()));
	    attribs.add(ah.create("foilPosition", form.getFoilPosition()));
	    attribs.add(ah.create("tresholdType", form.getTresholdType()));
	    attribs.add(ah.create("lTreshold", form.getLTreshold()));
	    attribs.add(ah.create("hTreshold", form.getHTreshold()));
	    attribs.add(ah.create("cTreshold", form.getCTreshold()));
	    attribs.add(ah.create("tresholdSpace", form.getTresholdSpace()));
	    attribs.add(ah.create("h1Treshold", form.getH1Treshold()));
	    attribs.add(ah.create("h2Treshold", form.getH2Treshold()));
	    if(fereastra != null) {
		attribs.add(ah.create("fereastra", fereastra));		
	    }
	    if(grilaVentilatie != null) {
		attribs.add(ah.create("grilaVentilatie", grilaVentilatie));
	    }
	    if(gauriAerisire != null) {
		attribs.add(ah.create("gauriAerisire", gauriAerisire));
	    }

	    r = validate();
	} catch (Exception e) {
	    r = new ResponseBean();
	    r.setCode(1);
	    r.setMessage("Save operation failed: " + e);
	    e.printStackTrace();
	}

	return r;
    }


    /**
     * Loads the attributes of the product into the form bean.
     *
     * @return a <code>ResponseBean</code> value
     * @throws FinderException if the record is not find in the database for
     * primary key represented by <code>id</code> instance variable.
     */
    public ResponseBean loadFields() throws FinderException {
	ResponseBean r;
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
		    form.setSubclass(a.getStringValue());
		} else if(a.getName().compareTo("version") == 0) {
		    form.setVersion(a.getStringValue());
		} else if(a.getName().compareTo("material") == 0) {
		    form.setMaterial(a.getIntValue());
		} else if(a.getName().compareTo("lg") == 0) {
		    form.setLg(a.getDoubleValue());
		} else if(a.getName().compareTo("hg") == 0) {
		    form.setHg(a.getDoubleValue());
		} else if(a.getName().compareTo("lcorrection") == 0) {
		    form.setLcorrection(a.getDoubleValue());
		} else if(a.getName().compareTo("hcorrection") == 0) {
		    form.setHcorrection(a.getDoubleValue());
		} else if(a.getName().equals("lCurrent")) {
		    form.setLCurrent(a.getDoubleValue());
		} else if(a.getName().equals("kType")) {
		    form.setKType(a.getIntValue());
		} else if(a.getName().compareTo("intFoil") == 0) {
		    form.setIntFoil(a.getIntValue());
		} else if(a.getName().compareTo("ieFoil") == 0) {
		    form.setIeFoil(a.getIntValue());
		} else if(a.getName().compareTo("extFoil") == 0) {
		    form.setExtFoil(a.getIntValue());
		} else if(a.getName().compareTo("intFoilSec") == 0) {
		    form.setIntFoilSec(a.getIntValue());
		} else if(a.getName().compareTo("ieFoilSec") == 0) {
		    form.setIeFoilSec(a.getIntValue());
		} else if(a.getName().compareTo("extFoilSec") == 0) {
		    form.setExtFoilSec(a.getIntValue());
		} else if(a.getName().compareTo("isolation") == 0) {
		    form.setIsolation(a.getIntValue());
		} else if(a.getName().compareTo("openingDir") == 0) {
		    form.setOpeningDir(a.getIntValue());
		} else if(a.getName().compareTo("openingSide") == 0) {
		    form.setOpeningSide(a.getIntValue());
		} else if(a.getName().compareTo("frameType") == 0) {
		    form.setFrameType(a.getIntValue());
		} else if(a.getName().compareTo("lFrame") == 0) {
		    form.setLFrame(a.getDoubleValue());
		} else if(a.getName().compareTo("bFrame") == 0) {
		    form.setBFrame(a.getDoubleValue());
		} else if(a.getName().compareTo("cFrame") == 0) {
		    form.setCFrame(a.getDoubleValue());
		} else if(a.getName().compareTo("foilPosition") == 0) {
		    form.setFoilPosition(a.getIntValue());
		} else if(a.getName().compareTo("tresholdType") == 0) {
		    form.setTresholdType(a.getIntValue());
		} else if(a.getName().compareTo("lTreshold") == 0) {
		    form.setLTreshold(a.getDoubleValue());
		} else if(a.getName().compareTo("hTreshold") == 0) {
		    form.setHTreshold(a.getDoubleValue());
		} else if(a.getName().compareTo("cTreshold") == 0) {
		    form.setCTreshold(a.getDoubleValue());
		} else if(a.getName().compareTo("tresholdSpace") == 0) {
		    form.setTresholdSpace(a.getIntValue());
		} else if(a.getName().compareTo("h1Treshold") == 0) {
		    form.setH1Treshold(a.getDoubleValue());
		} else if(a.getName().compareTo("h2Treshold") == 0) {
		    form.setH2Treshold(a.getDoubleValue());
		} else if(a.getName().equals("fereastra")) {
		    fereastra = a.getProduct();
		    form.setFereastraId(fereastra.getId());
		    form.setFereastra(fereastra.getDescription());
		} else if(a.getName().equals("grilaVentilatie")) {
		    grilaVentilatie = a.getProduct();
		    form.setGrilaVentilatieId(grilaVentilatie.getId());
		    form.setGrilaVentilatie(grilaVentilatie.getDescription());
		} else if(a.getName().equals("gauriAerisire")) {
		    gauriAerisire = a.getProduct();
		    form.setGauriAerisireId(gauriAerisire.getId());
		    form.setGauriAerisire(gauriAerisire.getDescription());
		}
	    }

	} catch (NamingException e) {
	    logger.log(BasicLevel.ERROR, e);
	    r = new ResponseBean();
	    r.setCode(1);
	    r.setMessage("ERROR: Configuration problem (jndi name not found)");
	}
	
	return r;
    }


    /**
     * Describe <code>createNewFormBean</code> method here.
     *
     */
    public final void createNewFormBean() {
	super.createNewFormBean();
	

	// set default values
	form.setSubclass("A");
	form.setVersion("UF");
	form.setMaterial(new Integer(1));
	form.setLg(new Double(0));
	form.setHg(new Double(0));
	form.setLe(new Double(0));
	form.setHe(new Double(0));
	form.setLcorrection(new Double(0));
	form.setHcorrection(new Double(0));
	form.setLCurrent(new Double(0));
	form.setKType(new Integer(0));
	form.setIeFoil(new Integer(1));
	form.setIntFoil(new Integer(1));
	form.setExtFoil(new Integer(1));
	form.setIeFoilSec(new Integer(1));
	form.setIntFoilSec(new Integer(1));
	form.setExtFoilSec(new Integer(1));
	form.setIsolation(new Integer(1));
	form.setOpeningDir(new Integer(1));
	form.setOpeningSide(new Integer(1));
	form.setFrameType(new Integer(1));
	form.setLFrame(new Double(0));
	form.setBFrame(new Double(0));
	form.setCFrame(new Double(0));
	form.setFoilPosition(new Integer(1));
	form.setTresholdType(new Integer(1));
	form.setLTreshold(new Double(0));
	form.setHTreshold(new Double(0));
	form.setCTreshold(new Double(0));
	form.setTresholdSpace(new Integer(1));
	form.setH1Treshold(new Double(0));
	form.setH2Treshold(new Double(0));
	fereastra = null;
	form.setFereastraId(new Integer(0));
	form.setFereastra("Fara fereastra");
	grilaVentilatie = null;
	form.setGrilaVentilatieId(new Integer(0));
	form.setGrilaVentilatie("Fara grila ventilatie");
	gauriAerisire = null;
	form.setGauriAerisireId(new Integer(0));
	form.setGauriAerisire("Fara gauri aerisire");

    }

    /**
     * Overwrite default method. Load the corresponding object from the 
     * persistence layer.
     */
    public ResponseBean updateFereastraId(Integer fereastraId) {
	ResponseBean r;
	try {
	    if(fereastra == null || 
	       !(fereastra.getId().equals(fereastraId))) {
		InitialContext ic = new InitialContext();
		Context env = (Context) ic.lookup("java:comp/env");
		ProductLocalHome ph = 
		    (ProductLocalHome)PortableRemoteObject.narrow
		    (env.lookup("ejb/ProductLocalHome"), ProductLocalHome.class);
		fereastra = ph.findByPrimaryKey(fereastraId);
	    }

	    form.setFereastraId(fereastraId);
	    form.setFereastra(fereastra.getDescription());
	    
	    r = new ResponseBean();
	    r.addRecord();
	    r.addField("fereastraId", fereastraId);
	    r.addField("fereastra", fereastra.getDescription());
	} catch (Exception e) {
	    r = new ResponseBean();
	    r.setCode(1);
	    r.setMessage("Datele despre fereastra nu au putut fi incarcate");
	    logger.log(BasicLevel.ERROR, "Error updating fereastraId " +
		       fereastraId, e);
	}
	return r;
    }

    /**
     * Overwrite default method. Load the corresponding object from the 
     * persistence layer.
     */
    public ResponseBean updateGrilaVentilatieId(Integer grilaVentilatieId) {
	ResponseBean r;
	try {
	    if(grilaVentilatie == null || 
	       !(grilaVentilatie.getId().equals(grilaVentilatieId))) {
		InitialContext ic = new InitialContext();
		Context env = (Context) ic.lookup("java:comp/env");
		ProductLocalHome ph = 
		    (ProductLocalHome)PortableRemoteObject.narrow
		    (env.lookup("ejb/ProductLocalHome"), ProductLocalHome.class);
		grilaVentilatie = ph.findByPrimaryKey(grilaVentilatieId);
	    }

	    form.setGrilaVentilatieId(grilaVentilatieId);
	    form.setGrilaVentilatie(grilaVentilatie.getDescription());
	    
	    r = new ResponseBean();
	    r.addRecord();
	    r.addField("grilaVentilatieId", grilaVentilatieId);
	    r.addField("grilaVentilatie", grilaVentilatie.getDescription());
	} catch (Exception e) {
	    r = new ResponseBean();
	    r.setCode(1);
	    r.setMessage("Datele despre grila de ventilatie nu au putut fi incarcate");
	    logger.log(BasicLevel.ERROR, "Error updating grilaVentilatieId " +
		       grilaVentilatieId, e);
	}
	return r;
    }


    /**
     * Overwrite default method. Load the corresponding object from the 
     * persistence layer.
     */
    public ResponseBean updateGauriAerisireId(Integer gaId) {
	ResponseBean r;
	try {
	    if(gauriAerisire == null || 
	       !(gauriAerisire.getId().equals(gaId))) {
		InitialContext ic = new InitialContext();
		Context env = (Context) ic.lookup("java:comp/env");
		ProductLocalHome ph = 
		    (ProductLocalHome)PortableRemoteObject.narrow
		    (env.lookup("ejb/ProductLocalHome"), ProductLocalHome.class);
		gauriAerisire = ph.findByPrimaryKey(gaId);
	    }

	    form.setGauriAerisireId(gaId);
	    form.setGauriAerisire(gauriAerisire.getDescription());
	    
	    r = new ResponseBean();
	    r.addRecord();
	    r.addField("gauriAerisireId", gaId);
	    r.addField("gauriAerisire", gauriAerisire.getDescription());
	} catch (Exception e) {
	    r = new ResponseBean();
	    r.setCode(1);
	    r.setMessage("Datele despre gaurile de aerisire nu au putut fi incarcate");
	    logger.log(BasicLevel.ERROR, "Error updating gauriAerisireId " +
		       gaId, e);
	}
	return r;
    }
}
