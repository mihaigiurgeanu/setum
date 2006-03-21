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
import ro.kds.erp.biz.SequenceHome;
import ro.kds.erp.biz.Sequence;
import java.math.BigDecimal;
import java.util.Map;
import ro.kds.erp.data.CompositeProductLocalHome;

/**
 * Business logic for definition of UsaMetalica product. It obsoletes old implementation
 * for UsaMetalica2K and UsaMetalica1K. The form field <code>k</code> that may have values
 * <code>1</code> or <code>2</code> separates between the 2 types of UsaMetalica. This will
 * allow the design of only one form to handle both types.
 *
 * Created: Fri Nov 18 15:34:24 2005
 *
 * @author <a href="mailto:Mihai Giurgeanu@CRIMIRA"></a>
 * @version $Id: UsaMetalica2KBeanImplementation.java,v 1.6 2006/03/21 00:40:38 mihai Exp $
 */
public class UsaMetalica2KBeanImplementation 
    extends ro.kds.erp.biz.setum.basic.UsaMetalica2KBean {

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
	    
	    p.setName(form.getName());
	    p.setCode(form.getCode());
	    p.setDescription(form.getDescription());
	    p.setEntryPrice(form.getEntryPrice());
	    p.setSellPrice(form.getSellPrice());

	    Collection attribs = p.getAttributes();
	    attribs.clear();
	    attribs.add(ah.create("subclass", form.getSubclass()));
	    attribs.add(ah.create("version", form.getVersion()));
	    attribs.add(ah.create("material", form.getMaterial()));
	    attribs.add(ah.create("k", form.getK()));
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
	    attribs.add(ah.create("montareSistem", form.getMontareSistem()));
	    attribs.add(ah.create("decupareSistmeId", form.getDecupareSistemId()));
	    attribs.add(ah.create("sistemeSetumSauBeneficiar", 
				  form.getSistemSetumSauBeneficiar()));
	    attribs.add(ah.create("broascaId", form.getBroascaId()));
	    attribs.add(ah.create("broascaBuc", form.getBroascaBuc()));
	    attribs.add(ah.create("cilindruId", form.getCilindruId()));
	    attribs.add(ah.create("cilindruBuc", form.getCilindruBuc()));
	    attribs.add(ah.create("copiatCheieId", form.getCopiatCheieId()));
	    attribs.add(ah.create("copiatCheieBuc", form.getCopiatCheieBuc()));
	    attribs.add(ah.create("sildId", form.getSildId()));
	    attribs.add(ah.create("sildTip", form.getSildTip()));
	    attribs.add(ah.create("sildCuloare", form.getSildCuloare()));
	    attribs.add(ah.create("sildBuc", form.getSildBuc()));
	    attribs.add(ah.create("rozetaId", form.getRozetaId()));
	    attribs.add(ah.create("rozetaTip", form.getRozetaTip()));
	    attribs.add(ah.create("rozetaCuloare", form.getRozetaCuloare()));
	    attribs.add(ah.create("rozetaBuc", form.getRozetaBuc()));
	    attribs.add(ah.create("manerId", form.getManerId()));
	    attribs.add(ah.create("manerTip", form.getManerTip()));
	    attribs.add(ah.create("manerCuloare", form.getManerCuloare()));
	    attribs.add(ah.create("manerBuc", form.getManerBuc()));
	    attribs.add(ah.create("yalla1Id", form.getYalla1Id()));
	    attribs.add(ah.create("yalla1Buc", form.getYalla1Buc()));
	    attribs.add(ah.create("yalla2Id", form.getYalla2Id()));
	    attribs.add(ah.create("yalla2Buc", form.getYalla2Buc()));
	    attribs.add(ah.create("baraAntipanicaId", form.getBaraAntipanicaId()));
	    attribs.add(ah.create("baraAntipanicaBuc", form.getBaraAntipanicaBuc()));
	    attribs.add(ah.create("manerSemicilindruId", form.getManerSemicilindruId()));
	    attribs.add(ah.create("manerSemicilindruBuc", form.getManerSemicilindruBuc()));
	    attribs.add(ah.create("selectorOrdineId", form.getSelectorOrdineId()));
	    attribs.add(ah.create("selectorOrdineBuc", form.getSelectorOrdineBuc()));
	    attribs.add(ah.create("amortizorId", form.getAmortizorId()));
	    attribs.add(ah.create("amortizorBuc", form.getAmortizorBuc()));
	    attribs.add(ah.create("alteSisteme1Id", form.getAlteSisteme1Id()));
	    attribs.add(ah.create("alteSisteme1Buc", form.getAlteSisteme1Buc()));
	    attribs.add(ah.create("alteSisteme2Id", form.getAlteSisteme2Id()));
	    attribs.add(ah.create("alteSisteme2Buc", form.getAlteSisteme2Buc()));
	    attribs.add(ah.create("sistemeComment", form.getSistemeComment()));

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
	    form.setCode(p.getCode());
	    form.setName(p.getName());
	    form.setDescription(p.getDescription());
	    form.setEntryPrice(p.getEntryPrice());
	    form.setSellPrice(p.getSellPrice());

	    // Attributes
	    Map amap = p.getAttributesMap();
	    form.readSubclass(amap);
	    form.readVersion(amap);
	    form.readMaterial(amap);
	    form.readK(amap);
	    form.readLg(amap);
	    form.readHg(amap);
	    form.readLcorrection(amap);
	    form.readHcorrection(amap);
	    form.readLCurrent(amap);
	    form.readKType(amap);
	    form.readIntFoil(amap);
	    form.readIeFoil(amap);
	    form.readExtFoil(amap);
	    form.readIsolation(amap);
	    form.readOpeningDir(amap);
	    form.readOpeningSide(amap);
	    form.readFrameType(amap);
	    form.readLFrame(amap);
	    form.readBFrame(amap);
	    form.readCFrame(amap);
	    form.readFoilPosition(amap);
	    form.readTresholdType(amap);
	    form.readLTreshold(amap);
	    form.readHTreshold(amap);
	    form.readCTreshold(amap);
	    form.readTresholdSpace(amap);
	    form.readH1Treshold(amap);
	    form.readH2Treshold(amap);

	    form.readMontareSistem(amap);
	    form.readDecupareSistemId(amap);
	    form.readSistemSetumSauBeneficiar(amap);
	    form.readBroascaId(amap);
	    form.readBroascaBuc(amap);
	    form.readCilindruId(amap);
	    form.readCilindruBuc(amap);
	    form.readCopiatCheieId(amap);
	    form.readCopiatCheieBuc(amap);
	    form.readSildId(amap);
	    form.readSildTip(amap);
	    form.readSildCuloare(amap);
	    form.readSildBuc(amap);
	    form.readRozetaId(amap);
	    form.readRozetaTip(amap);
	    form.readRozetaCuloare(amap);
	    form.readRozetaBuc(amap);
	    form.readManerId(amap);
	    form.readManerTip(amap);
	    form.readManerCuloare(amap);
	    form.readManerBuc(amap);
	    form.readYalla1Id(amap);
	    form.readYalla1Buc(amap);
	    form.readYalla2Id(amap);
	    form.readYalla2Buc(amap);
	    form.readBaraAntipanicaId(amap);
	    form.readBaraAntipanicaBuc(amap);
	    form.readManerSemicilindruId(amap);
	    form.readManerSemicilindruBuc(amap);
	    form.readSelectorOrdineId(amap);
	    form.readSelectorOrdineBuc(amap);
	    form.readAmortizorId(amap);
	    form.readAmortizorBuc(amap);
	    form.readAlteSisteme1Id(amap);
	    form.readAlteSisteme1Buc(amap);
	    form.readAlteSisteme2Id(amap);
	    form.readAlteSisteme2Buc(amap);
	    form.readSistemeComment(amap);

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

	// automatically generate a code
	String newCode;
	try {
	    InitialContext ic = new InitialContext();
	    Context env = (Context)ic.lookup("java:comp/env");
	    SequenceHome sh = (SequenceHome)PortableRemoteObject.narrow
		(env.lookup("ejb/SequenceHome"), SequenceHome.class);
	    Sequence s = sh.create();
	    newCode = s.getNext("ro.setumsa.sequences.products").toString();
	} catch (Exception e) {
	    newCode = "";
	}

	// set default values
	form.setCode(newCode);
	form.setName("Usa metalica " + newCode);
	form.setDescription("");
	form.setSubclass("A");
	form.setVersion("UF");
	form.setMaterial(new Integer(1));
	form.setK(new Integer(1));
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
	form.setSellPrice(new BigDecimal(0));
	form.setEntryPrice(new BigDecimal(0));
	form.setMontareSistem(new Integer(1));
	form.setDecupareSistemId(new Integer(0));
	form.setSistemSetumSauBeneficiar(new Integer(1));
	form.setBroascaId(new Integer(0));
	form.setBroascaBuc(new Integer(0));
	form.setCilindruId(new Integer(0));
	form.setCilindruBuc(new Integer(0));
	form.setCopiatCheieId(new Integer(0));
	form.setCopiatCheieBuc(new Integer(0));
	form.setSildId(new Integer(0));
	form.setSildTip("");
	form.setSildCuloare("");
	form.setSildBuc(new Integer(0));
	form.setRozetaId(new Integer(0));
	form.setRozetaTip("");
	form.setRozetaCuloare("");
	form.setRozetaBuc(new Integer(0));
	form.setManerId(new Integer(0));
	form.setManerTip("");
	form.setManerCuloare("");
	form.setManerBuc(new Integer(0));
	form.setYalla1Id(new Integer(0));
	form.setYalla1Buc(new Integer(0));
	form.setYalla2Buc(new Integer(0));
	form.setYalla2Id(new Integer(0));
	form.setBaraAntipanicaId(new Integer(0));
	form.setBaraAntipanicaBuc(new Integer(0));
	form.setManerSemicilindruId(new Integer(0));
	form.setManerSemicilindruBuc(new Integer(0));
	form.setAmortizorId(new Integer(0));
	form.setAmortizorBuc(new Integer(0));
	form.setAlteSisteme1Id(new Integer(0));
	form.setAlteSisteme2Buc(new Integer(0));
	form.setAlteSisteme2Id(new Integer(0));
	form.setSistemeComment("");
    }


    /**
     * Add the value lists to the response. This method overrides the default
     * implementation tha does nothing. It is called by different methods
     * that return data to be loaded in the form.
     */
    public void loadValueLists(ResponseBean r) {
	r.addValueList("broascaId", 
		       ValueLists.makeVLForCategoryRef("broascaId"));
	r.addValueList("cilindruId", 
		       ValueLists.makeVLForCategoryRef("cilindruId"));
	r.addValueList("copiatCheieId",
		       ValueLists.makeVLForCategoryRef("copiatCheieId"));
	r.addValueList("sildId",
		       ValueLists.makeVLForCategoryRef("sildId"));
	r.addValueList("rozetaId",
		       ValueLists.makeVLForCategoryRef("rozetaId"));
	r.addValueList("manerId",
		       ValueLists.makeVLForCategoryRef("manerId"));
	Map yallaVL = ValueLists.makeVLForCategoryRef("yallaId");
	r.addValueList("yalla1Id", yallaVL);
	r.addValueList("yalla2Id", yallaVL);
	r.addValueList("baraAntipanicaId",
		       ValueLists.makeVLForCategoryRef("baraAntipanicaId"));
	r.addValueList("selectorOrdineId",
		       ValueLists.makeVLForCategoryRef("selectorOrdineId"));
	r.addValueList("amortizorId",
		       ValueLists.makeVLForCategoryRef("amortizorId"));
	r.addValueList("decupareSistemId",
		       ValueLists.makeVLForCategoryRef("decupareSistemId"));

	try {
	    InitialContext ic = new InitialContext();
	    Map sistemeVL = ValueLists.makeVLForSubcategories
		((Integer)ic.lookup("java:comp/env/sistemeId"));
	    r.addValueList("alteSisteme1Id", sistemeVL);
	    r.addValueList("alteSisteme2Id", sistemeVL);
	} catch (Exception e) {
	    logger.log(BasicLevel.ERROR, "Nu pot face lista de sisteme", e);
	}
    }


    /**
     * Retrieves the listing of options. The options are different
     * kind of products that can be added to a door.
     */
    public ResponseBean getOptionsListing() {
	ResponseBean r;
	try {
	    InitialContext ic = new InitialContext();
	    Context env = (Context) ic.lookup("java:comp/env");
	    ProductLocalHome ph = (ProductLocalHome)PortableRemoteObject.narrow
		(env.lookup("ejb/ProductHome"), ProductLocalHome.class);
	    
	    ProductLocal usa = ph.findByPrimaryKey(id);
	    r = new ResponseBean();
	    for(Iterator i = 
		    usa.getCompositeProduct().getComponents().iterator();
		i.hasNext(); ) {
		ProductLocal p = (ProductLocal)i.next();
		r.addRecord();
		r.addField("options.id", p.getId());
		r.addField("options.code", p.getCode());
		r.addField("options.name", p.getName());
		r.addField("options.description", p.getDescription());
		r.addField("options.entryPrice", p.getEntryPrice());
		r.addField("options.sellPrice", p.getSellPrice());
		r.addField("options.categoryId", p.getCategory().getId());
		r.addField("options.categoryName", p.getCategory().getName());
	    }
	} catch (Exception e) {
	}
	return null;
    }

    /**
     * Price calculation.
     */
    protected void computePrice() {
	double se = (form.getLe().doubleValue()/1000)
	    * (form.getHe().doubleValue()/1000);
	double price = se * 200;
	double entryPrice = se * 125;

	form.setEntryPrice(new BigDecimal(entryPrice));
	form.setSellPrice(new BigDecimal(price));
    }

    public ResponseBean computeCalculatedFields(ResponseBean r) {
	computePrice();
	return super.computeCalculatedFields(r);
    }

}
