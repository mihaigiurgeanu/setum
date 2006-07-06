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
import ro.kds.erp.data.CompositeProductLocal;
import javax.ejb.CreateException;
import javax.ejb.RemoveException;
import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.ejb.EJBException;
import javax.jms.Message;
import org.objectweb.jonas.common.Log;
import javax.jms.MessageListener;
import ro.kds.erp.scripting.Script;
import ro.kds.erp.scripting.ScriptErrorException;
import ro.kds.erp.biz.Products;

/**
 * Business logic for definition of UsaMetalica product. It obsoletes old implementation
 * for UsaMetalica2K and UsaMetalica1K. The form field <code>k</code> that may have values
 * <code>1</code> or <code>2</code> separates between the 2 types of UsaMetalica. This will
 * allow the design of only one form to handle both types.
 *
 * Created: Fri Nov 18 15:34:24 2005
 *
 * @author <a href="mailto:Mihai Giurgeanu@CRIMIRA"></a>
 * @version $Id: UsaMetalica2KBeanImplementation.java,v 1.16 2006/07/06 07:17:43 mihai Exp $
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
	    attribs.add(ah.create("decupareSistemId", form.getDecupareSistemId()));
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

	    attribs.add(ah.create("benefBroasca", form.getBenefBroasca()));
	    attribs.add(ah.create("benefBroascaBuc", form.getBenefBroascaBuc()));
	    attribs.add(ah.create("benefBroascaTip", form.getBenefBroascaTip()));
	    attribs.add(ah.create("benefCilindru", form.getBenefCilindru()));
	    attribs.add(ah.create("benefCilindruBuc", form.getBenefCilindruBuc()));
	    attribs.add(ah.create("benefCilindruTip", form.getBenefCilindruTip()));
	    attribs.add(ah.create("benefSild", form.getBenefSild()));
	    attribs.add(ah.create("benefSildBuc", form.getBenefSildBuc()));
	    attribs.add(ah.create("benefSildTip", form.getBenefSildTip()));
	    attribs.add(ah.create("benefYalla", form.getBenefYalla()));
	    attribs.add(ah.create("benefYallaBuc", form.getBenefYallaBuc()));
	    attribs.add(ah.create("benefYallaTip", form.getBenefYallaTip()));
	    attribs.add(ah.create("benefBaraAntipanica", form.getBenefBaraAntipanica()));
	    attribs.add(ah.create("benefBaraAntipanicaTip", form.getBenefBaraAntipanicaTip()));
	    attribs.add(ah.create("benefBaraAntipanicaBuc", form.getBenefBaraAntipanicaBuc()));
	    attribs.add(ah.create("benefManer", form.getBenefManer()));
	    attribs.add(ah.create("benefManerBuc", form.getBenefManerBuc()));
	    attribs.add(ah.create("benefSelectorOrdine", form.getBenefSelectorOrdine()));
	    attribs.add(ah.create("benefSelectorOrdineBuc", form.getBenefSelectorOrdineBuc()));
	    attribs.add(ah.create("benefAmortizor", form.getBenefAmortizor()));
	    attribs.add(ah.create("benefAmortizorBuc", form.getBenefAmortizorBuc()));
	    attribs.add(ah.create("benefAlteSisteme1", form.getBenefAlteSisteme1()));
	    attribs.add(ah.create("benefAlteSisteme1Buc", form.getBenefAlteSisteme1Buc()));
	    attribs.add(ah.create("benefAlteSisteme2", form.getBenefAlteSisteme2()));
	    attribs.add(ah.create("benefAlteSisteme2Buc", form.getBenefAlteSisteme2Buc()));

	    attribs.add(ah.create("intFinisajBlatId", form.getIntFinisajBlatId()));
	    attribs.add(ah.create("intFinisajTocId", form.getIntFinisajTocId()));
	    attribs.add(ah.create("intFinisajGrilajId", form.getIntFinisajGrilajId()));
	    attribs.add(ah.create("intFinisajFereastraId", form.getIntFinisajFereastraId()));
	    attribs.add(ah.create("intFinisajSupraluminaId", form.getIntFinisajSupraluminaId()));
	    attribs.add(ah.create("intFinisajPanouLateralId", form.getIntFinisajPanouLateralId()));

	    attribs.add(ah.create("extFinisajBlatId", form.getExtFinisajBlatId()));
	    attribs.add(ah.create("extFinisajTocId", form.getExtFinisajTocId()));
	    attribs.add(ah.create("extFinisajGrilajId", form.getExtFinisajGrilajId()));
	    attribs.add(ah.create("extFinisajFereastraId", form.getExtFinisajFereastraId()));
	    attribs.add(ah.create("extFinisajSupraluminaId", form.getExtFinisajSupraluminaId()));
	    attribs.add(ah.create("extFinisajPanouLateralId", form.getExtFinisajPanouLateralId()));

	    attribs.add(ah.create("finisajTocBlat", form.getFinisajTocBlat()));
	    attribs.add(ah.create("finisajGrilajBlat", form.getFinisajGrilajBlat()));
	    attribs.add(ah.create("finisajFereastraBlat", form.getFinisajFereastraBlat()));
	    attribs.add(ah.create("finisajSupraluminaBlat", form.getFinisajSupraluminaBlat()));
	    attribs.add(ah.create("finisajPanouLateralBlat", form.getFinisajPanouLateralBlat()));
	    attribs.add(ah.create("finisajBlatExtInt", form.getFinisajBlatExtInt()));
	    attribs.add(ah.create("finisajTocExtInt", form.getFinisajTocExtInt()));
	    attribs.add(ah.create("finisajGrilajExtInt", form.getFinisajGrilajExtInt()));
	    attribs.add(ah.create("finisajFereastraExtInt", form.getFinisajFereastraExtInt()));
	    attribs.add(ah.create("finisajSupraluminaExtInt", form.getFinisajSupraluminaExtInt()));
	    attribs.add(ah.create("finisajPanouLateralExtInt", form.getFinisajPanouLateralExtInt()));
    

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
	    form.readIeFoilSec(amap);
	    form.readIntFoilSec(amap);
	    form.readExtFoilSec(amap);
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

	    form.readBenefBroasca(amap);
	    form.readBenefBroascaBuc(amap);
	    form.readBenefBroascaTip(amap);
	    form.readBenefCilindru(amap);
	    form.readBenefCilindruBuc(amap);
	    form.readBenefCilindruTip(amap);
	    form.readBenefSild(amap);
	    form.readBenefSildBuc(amap);
	    form.readBenefSildTip(amap);
	    form.readBenefYalla(amap);
	    form.readBenefYallaBuc(amap);
	    form.readBenefYallaTip(amap);
	    form.readBenefBaraAntipanica(amap);
	    form.readBenefBaraAntipanicaTip(amap);
	    form.readBenefBaraAntipanicaBuc(amap);
	    form.readBenefManer(amap);
	    form.readBenefManerBuc(amap);
	    form.readBenefSelectorOrdine(amap);
	    form.readBenefSelectorOrdineBuc(amap);
	    form.readBenefAmortizor(amap);
	    form.readBenefAmortizorBuc(amap);
	    form.readBenefAlteSisteme1(amap);
	    form.readBenefAlteSisteme1Buc(amap);
	    form.readBenefAlteSisteme2(amap);
	    form.readBenefAlteSisteme2Buc(amap);

	    //form.readIntFinisajBlat(amap);
	    form.readIntFinisajBlatId(amap);
	    //form.readIntFinisajToc(amap);
	    form.readIntFinisajTocId(amap);
	    //form.readIntFinisajGrilaj(amap);
	    form.readIntFinisajGrilajId(amap);
	    //form.readIntFinisajFereastra(amap);
	    form.readIntFinisajFereastraId(amap);
	    //form.readIntFinisajSupralumina(amap);
	    form.readIntFinisajSupraluminaId(amap);
	    //form.readIntFinisajPanouLateral(amap);
	    form.readIntFinisajPanouLateralId(amap);

	    //form.readExtFinisajBlat(amap);
	    form.readExtFinisajBlatId(amap);
	    //form.readExtFinisajToc(amap);
	    form.readExtFinisajTocId(amap);
	    //form.readExtFinisajGrilaj(amap);
	    form.readExtFinisajGrilajId(amap);
	    //form.readExtFinisajFereastra(amap);
	    form.readExtFinisajFereastraId(amap);
	    //form.readExtFinisajSupralumina(amap);
	    form.readExtFinisajSupraluminaId(amap);
	    //form.readExtFinisajPanouLateral(amap);
	    form.readExtFinisajPanouLateralId(amap);

	    form.readFinisajTocBlat(amap);
	    form.readFinisajGrilajBlat(amap);
	    form.readFinisajFereastraBlat(amap);
	    form.readFinisajSupraluminaBlat(amap);
	    form.readFinisajPanouLateralBlat(amap);
	    form.readFinisajBlatExtInt(amap);
	    form.readFinisajTocExtInt(amap);
	    form.readFinisajGrilajExtInt(amap);
	    form.readFinisajFereastraExtInt(amap);
	    form.readFinisajSupraluminaExtInt(amap);
	    form.readFinisajPanouLateralExtInt(amap);

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
// 	form.setDescription("");
 	form.setSubclass("A");
 	form.setVersion("UF");
 	form.setMaterial(new Integer(1));
 	form.setK(new Integer(1));
// 	form.setLg(new Double(0));
// 	form.setHg(new Double(0));
// 	form.setLe(new Double(0));
// 	form.setHe(new Double(0));
// 	form.setLcorrection(new Double(0));
// 	form.setHcorrection(new Double(0));
// 	form.setLCurrent(new Double(0));
// 	form.setKType(new Integer(0));
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
// 	form.setLFrame(new Double(0));
// 	form.setBFrame(new Double(0));
// 	form.setCFrame(new Double(0));
 	form.setFoilPosition(new Integer(1));
 	form.setTresholdType(new Integer(1));
// 	form.setLTreshold(new Double(0));
// 	form.setHTreshold(new Double(0));
// 	form.setCTreshold(new Double(0));
 	form.setTresholdSpace(new Integer(1));
// 	form.setH1Treshold(new Double(0));
// 	form.setH2Treshold(new Double(0));
// 	form.setSellPrice(new BigDecimal(0));
// 	form.setEntryPrice(new BigDecimal(0));
// 	form.setMontareSistem(new Integer(1));
// 	form.setDecupareSistemId(new Integer(0));
 	form.setSistemSetumSauBeneficiar(new Integer(1));
// 	form.setBroascaId(new Integer(0));
// 	form.setBroascaBuc(new Integer(0));
// 	form.setCilindruId(new Integer(0));
// 	form.setCilindruBuc(new Integer(0));
// 	form.setCopiatCheieId(new Integer(0));
// 	form.setCopiatCheieBuc(new Integer(0));
// 	form.setSildId(new Integer(0));
// 	form.setSildTip("");
// 	form.setSildCuloare("");
// 	form.setSildBuc(new Integer(0));
// 	form.setRozetaId(new Integer(0));
// 	form.setRozetaTip("");
// 	form.setRozetaCuloare("");
// 	form.setRozetaBuc(new Integer(0));
// 	form.setManerId(new Integer(0));
// 	form.setManerTip("");
// 	form.setManerCuloare("");
// 	form.setManerBuc(new Integer(0));
// 	form.setYalla1Id(new Integer(0));
// 	form.setYalla1Buc(new Integer(0));
// 	form.setYalla2Buc(new Integer(0));
// 	form.setYalla2Id(new Integer(0));
// 	form.setBaraAntipanicaId(new Integer(0));
// 	form.setBaraAntipanicaBuc(new Integer(0));
// 	form.setManerSemicilindruId(new Integer(0));
// 	form.setManerSemicilindruBuc(new Integer(0));
// 	form.setAmortizorId(new Integer(0));
// 	form.setAmortizorBuc(new Integer(0));
// 	form.setAlteSisteme1Id(new Integer(0));
// 	form.setAlteSisteme2Buc(new Integer(0));
// 	form.setAlteSisteme2Id(new Integer(0));
// 	form.setSistemeComment("");


	Boolean trueObj = new Boolean(true);

	form.setFinisajTocBlat(trueObj);
	form.setFinisajGrilajBlat(trueObj);
	form.setFinisajFereastraBlat(trueObj);
	form.setFinisajSupraluminaBlat(trueObj);
	form.setFinisajPanouLateralBlat(trueObj);
	form.setFinisajTocExtInt(trueObj);
	form.setFinisajGrilajExtInt(trueObj);
	form.setFinisajFereastraExtInt(trueObj);
	form.setFinisajSupraluminaExtInt(trueObj);
	form.setFinisajPanouLateralExtInt(trueObj);
	    
    }


    /**
     * Add the value lists to the response. This method overrides the default
     * implementation that does nothing. It is called by different methods
     * that return data to be loaded in the form.
     */
    public void loadValueLists(ResponseBean r) {


	r.addValueList("subclass", ValueLists.makeStdValueList(11001));
	r.addValueList("version", ValueLists.makeStdValueList(11002));
	r.addValueList("material", ValueLists.makeStdValueList(11003));
	r.addValueList("intFoil", ValueLists.makeStdValueList(11006));
	r.addValueList("extFoil", ValueLists.makeStdValueList(11006));
	r.addValueList("intFoilSec", ValueLists.makeStdValueList(11006));
	r.addValueList("extFoilSec", ValueLists.makeStdValueList(11006));
	r.addValueList("isolation", ValueLists.makeStdValueList(11010));
	r.addValueList("openingDir", ValueLists.makeStdValueList(11004));
	r.addValueList("openingSide", ValueLists.makeStdValueList(11005));
	r.addValueList("foilPosition", ValueLists.makeStdValueList(11008));

	r.addValueList("benefBroascaTip", ValueLists.makeStdValueList(11020));
	r.addValueList("benefCilindruTip", ValueLists.makeStdValueList(11021));
	r.addValueList("benefSildTip", ValueLists.makeStdValueList(11023));
	r.addValueList("benefYallaTip", ValueLists.makeStdValueList(11022));
	r.addValueList("benefBaraAntipanicaTip", ValueLists.makeStdValueList(11024));

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
	r.addValueList("manerSemicilindruId",
		       ValueLists.makeVLForCategoryRef("manerSemicilindruId"));



	
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
	    
	    r = new ResponseBean();

	    if(id != null) {
		InitialContext ic = new InitialContext();
		Context env = (Context) ic.lookup("java:comp/env");
		ProductLocalHome ph = (ProductLocalHome)PortableRemoteObject.narrow
		    (env.lookup("ejb/ProductHome"), ProductLocalHome.class);
		
		ProductLocal usa = ph.findByPrimaryKey(id);
		CompositeProductLocal cp = usa.getCompositeProduct();
		if(cp == null) {
		    logger.log(BasicLevel.DEBUG, "No composite product found ....");
		} else {
		    for(Iterator i = cp.getComponents().iterator();
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
			// Attributes
			Map amap = p.getAttributesMap();
			AttributeLocal a = (AttributeLocal)amap.get("businessCategory");
			if(a != null)
			    r.addField("options.businessCategory", a.getStringValue());
			else
			    r.addField("options.businessCategory", "");
		    }
		}
	    }
		
	} catch (FinderException e) {
	    r = ResponseBean.ERR_NOTFOUND;
	    logger.log(BasicLevel.ERROR, "Can not load the listing options " +
		       e.getMessage());
	    logger.log(BasicLevel.DEBUG, e);
	} catch (NamingException e) {
	    r = ResponseBean.ERR_CONFIG_NAMING;
	    logger.log(BasicLevel.ERROR, "Can not load the listing options " +
		       e.getMessage());
	    logger.log(BasicLevel.DEBUG, e);
	} catch (Exception e) {
	    logger.log(BasicLevel.ERROR, "Can not load the listing options " +
		       e.getMessage());
	    logger.log(BasicLevel.DEBUG, e);
	    r = ResponseBean.ERR_UNEXPECTED;	    
	}
	return r;
    }

    /**
     * Add a new option.
     */
    public ResponseBean addOption(Integer optionId, String businessCategory) {
	ResponseBean r;
	try {

	    InitialContext ic = new InitialContext();
	    Context env = (Context) ic.lookup("java:comp/env");
	    ProductLocalHome ph = (ProductLocalHome)PortableRemoteObject.narrow
		(env.lookup("ejb/ProductHome"), ProductLocalHome.class);
	    
	    ProductLocal usa = ph.findByPrimaryKey(id);

	    CompositeProductLocal cp = usa.getCompositeProduct();

	    if (cp == null) {
		CompositeProductLocalHome cph = (CompositeProductLocalHome)PortableRemoteObject
		    .narrow(env.lookup("ejb/CompositeProductLocalHome"), CompositeProductLocalHome.class);
		cp = cph.create(usa.getId());
		usa.setCompositeProduct(cp);
	    }

	    cp.getComponents().add(ph.findByPrimaryKey(optionId));

	    r = ResponseBean.SUCCESS;
	} catch (CreateException e) {
	    r = ResponseBean.ERR_CREATE;
	    logger.log(BasicLevel.ERROR, "Can not create related composite product: " 
		       + e.getMessage());
	    logger.log(BasicLevel.DEBUG, e);
	} catch (FinderException e) {
	    r = ResponseBean.ERR_NOTFOUND;
	    logger.log(BasicLevel.ERROR, "Can not add the option " +
		       e.getMessage());
	    logger.log(BasicLevel.DEBUG, e);
	} catch (NamingException e) {
	    r = ResponseBean.ERR_CONFIG_NAMING;
	    logger.log(BasicLevel.ERROR, "Can not add the option " +
		       e.getMessage());
	    logger.log(BasicLevel.DEBUG, e);
	} catch (Exception e) {
	    logger.log(BasicLevel.ERROR, "Can not add the option " +
		       e.getMessage());
	    logger.log(BasicLevel.DEBUG, e);
	    r = ResponseBean.ERR_UNEXPECTED;	    
	}
	return r;
    }

    /**
     * Removes the given option
     */
    public ResponseBean removeOption (Integer optionId) {
	ResponseBean r;

	try {
	    
	    InitialContext ic = new InitialContext();
	    Context env = (Context) ic.lookup("java:comp/env");
	    ProductLocalHome ph = (ProductLocalHome)PortableRemoteObject.narrow
		(env.lookup("ejb/ProductHome"), ProductLocalHome.class);
	    
	    ProductLocal option = ph.findByPrimaryKey(optionId);
	    option.remove();

	    r = ResponseBean.SUCCESS;

	} catch (RemoveException e) {
	    r = ResponseBean.ERR_REMOVE;
	    logger.log(BasicLevel.ERROR, "Can not remove option " + optionId);
	    logger.log(BasicLevel.INFO, e);
	} catch (FinderException e) {
	    r = ResponseBean.ERR_NOTFOUND;
	    logger.log(BasicLevel.ERROR, "Can not add the option " +
		       e.getMessage());
	    logger.log(BasicLevel.DEBUG, e);
	} catch (NamingException e) {
	    r = ResponseBean.ERR_CONFIG_NAMING;
	    logger.log(BasicLevel.ERROR, "Can not add the option " +
		       e.getMessage());
	    logger.log(BasicLevel.DEBUG, e);
	} catch (Exception e) {
	    logger.log(BasicLevel.ERROR, "Can not add the option " +
		       e.getMessage());
	    logger.log(BasicLevel.DEBUG, e);
	    r = ResponseBean.ERR_UNEXPECTED;	    
	}

	return r;
    }

    public ResponseBean computeCalculatedFields(ResponseBean r) {
	r = super.computeCalculatedFields(r);

	load_finisaje(r);

	return r;
    }


    private void load_finisaje(ResponseBean r) {
	
	try {
	    InitialContext ic = new InitialContext();
	    Context env = (Context)ic.lookup("java:comp/env");
	    ProductLocalHome ph = (ProductLocalHome)PortableRemoteObject
		.narrow(env.lookup("ejb/ProductHome"), ProductLocalHome.class);
	    

	    if(form.getIntFinisajBlatId().intValue() != 0) {
		ProductLocal p;
		p = ph.findByPrimaryKey(form.getIntFinisajBlatId());
		if(r != null && form.getIntFinisajBlat().compareTo(p.getDescription()) != 0) {
		    r.addField("intFinisajBlat", p.getDescription());
		}		
		form.setIntFinisajBlat(p.getDescription());
	    }

	    if(form.getIntFinisajTocId().intValue() != 0) {
		ProductLocal p;
		p = ph.findByPrimaryKey(form.getIntFinisajTocId());
		if(r != null && form.getIntFinisajToc().compareTo(p.getDescription()) != 0) {
		    r.addField("intFinisajToc", p.getDescription());
		}
		form.setIntFinisajToc(p.getDescription());
	    }

	    if(form.getIntFinisajGrilajId().intValue() != 0) {
		ProductLocal p;
		p = ph.findByPrimaryKey(form.getIntFinisajGrilajId());
		if(r != null && form.getIntFinisajGrilaj().compareTo(p.getDescription()) != 0) {
		    r.addField("intFinisajGrilaj", p.getDescription());
		}
		form.setIntFinisajGrilaj(p.getDescription());
	    }

	    if(form.getIntFinisajFereastraId().intValue() != 0) {
		ProductLocal p;
		p = ph.findByPrimaryKey(form.getIntFinisajFereastraId());
		if(r != null && form.getIntFinisajFereastra().compareTo(p.getDescription()) != 0) {
		    r.addField("intFinisajFerestra", p.getDescription());
		}
		form.setIntFinisajFereastra(p.getDescription());
	    }

	    if(form.getIntFinisajSupraluminaId().intValue() != 0) {
		ProductLocal p;
		p = ph.findByPrimaryKey(form.getIntFinisajSupraluminaId());
		if(r != null && form.getIntFinisajSupralumina().compareTo(p.getDescription()) != 0) {
		    r.addField("intFinisajSupralumina", p.getDescription());
		}
		form.setIntFinisajSupralumina(p.getDescription());
	    }

	    if(form.getIntFinisajPanouLateralId().intValue() != 0) {
		ProductLocal p;
		p = ph.findByPrimaryKey(form.getIntFinisajPanouLateralId());
		if(r != null && form.getIntFinisajPanouLateral().compareTo(p.getDescription()) != 0) {
		    r.addField("intFinisajPanouLateral", p.getDescription());
		}
		form.setIntFinisajPanouLateral(p.getDescription());
	    }



	    if(form.getExtFinisajBlatId().intValue() != 0) {
		ProductLocal p;
		p = ph.findByPrimaryKey(form.getExtFinisajBlatId());
		if(r != null && form.getExtFinisajBlat().compareTo(p.getDescription()) != 0) {
		    r.addField("intFinisajBlat", p.getDescription());
		}		
		form.setExtFinisajBlat(p.getDescription());
	    }

	    if(form.getExtFinisajTocId().intValue() != 0) {
		ProductLocal p;
		p = ph.findByPrimaryKey(form.getExtFinisajTocId());
		if(r != null && form.getExtFinisajToc().compareTo(p.getDescription()) != 0) {
		    r.addField("intFinisajToc", p.getDescription());
		}
		form.setExtFinisajToc(p.getDescription());
	    }

	    if(form.getExtFinisajGrilajId().intValue() != 0) {
		ProductLocal p;
		p = ph.findByPrimaryKey(form.getExtFinisajGrilajId());
		if(r != null && form.getExtFinisajGrilaj().compareTo(p.getDescription()) != 0) {
		    r.addField("intFinisajGrilaj", p.getDescription());
		}
		form.setExtFinisajGrilaj(p.getDescription());
	    }

	    if(form.getExtFinisajFereastraId().intValue() != 0) {
		ProductLocal p;
		p = ph.findByPrimaryKey(form.getExtFinisajFereastraId());
		if(r != null && form.getExtFinisajFereastra().compareTo(p.getDescription()) != 0) {
		    r.addField("intFinisajFerestra", p.getDescription());
		}
		form.setExtFinisajFereastra(p.getDescription());
	    }

	    if(form.getExtFinisajSupraluminaId().intValue() != 0) {
		ProductLocal p;
		p = ph.findByPrimaryKey(form.getExtFinisajSupraluminaId());
		if(r != null && form.getExtFinisajSupralumina().compareTo(p.getDescription()) != 0) {
		    r.addField("intFinisajSupralumina", p.getDescription());
		}
		form.setExtFinisajSupralumina(p.getDescription());
	    }

	    if(form.getExtFinisajPanouLateralId().intValue() != 0) {
		ProductLocal p;
		p = ph.findByPrimaryKey(form.getExtFinisajPanouLateralId());
		if(r != null && form.getExtFinisajPanouLateral().compareTo(p.getDescription()) != 0) {
		    r.addField("intFinisajPanouLateral", p.getDescription());
		}
		form.setExtFinisajPanouLateral(p.getDescription());
	    }
	    
	} catch (Exception e) {
	    logger.log(BasicLevel.WARN, "Can not read finisaje", e);
	}

    } 

    /**
     * Add to the script suplimentary product references for allowing the script
     * to calculate the price.
     *
     * @param script a <code>Script</code> value
     */
    protected void addFieldsToScript(Script s) {
	super.addFieldsToScript(s);

	try {

	    CategoryLocalHome ch = Products.getCategoryHome();
	    ProductLocalHome ph = Products.getProductHome();
	    AttributeLocalHome ah = Products.getAttributeHome();

	    // subclass
	    try {
		logger.log(BasicLevel.DEBUG, "setting the subclassObj");
		CategoryLocal c = ch.findByPrimaryKey(new Integer(11001));
		ProductLocal p = c.getProductByCode(form.getSubclass());		
		s.setVar("subclassObj", p, ProductLocal.class);
	    } catch (ScriptErrorException e) {
		logger.log(BasicLevel.ERROR, "ScriptErrorException " + e.getMessage());
		logger.log(BasicLevel.DEBUG, e);
	    } catch (FinderException e) {
		logger.log(BasicLevel.ERROR, "Category 11001 not found");
		logger.log(BasicLevel.DEBUG, e);
	    }
	    // version
	    try {
		logger.log(BasicLevel.DEBUG, "setting the versionObj");
		CategoryLocal c = ch.findByPrimaryKey(new Integer(11002));
		ProductLocal p = c.getProductByCode(form.getVersion());		
		s.setVar("versionObj", p, ProductLocal.class);
	    } catch (ScriptErrorException e) {
		logger.log(BasicLevel.ERROR, "ScriptErrorException " + e.getMessage());
		logger.log(BasicLevel.DEBUG, e);
	    } catch (FinderException e) {
		logger.log(BasicLevel.ERROR, "Category 11002 not found");
		logger.log(BasicLevel.DEBUG, e);
	    }
	    // material
	    try {
		logger.log(BasicLevel.DEBUG, "setting the materialObj");
		CategoryLocal c = ch.findByPrimaryKey(new Integer(11003));
		ProductLocal p = c.getProductByCode(form.getMaterial());		
		s.setVar("materialObj", p, ProductLocal.class);
	    } catch (ScriptErrorException e) {
		logger.log(BasicLevel.ERROR, "ScriptErrorException " + e.getMessage());
		logger.log(BasicLevel.DEBUG, e);
	    } catch (FinderException e) {
		logger.log(BasicLevel.ERROR, "Category 11003 not found");
		logger.log(BasicLevel.DEBUG, e);
	    }
	    // intFoil
	    try {
		CategoryLocal c = ch.findByPrimaryKey(new Integer(11006));
		ProductLocal p = c.getProductByCode(form.getIntFoil());		
		s.setVar("intFoilObj", p, ProductLocal.class);
	    } catch (ScriptErrorException e) {
		logger.log(BasicLevel.ERROR, "ScriptErrorException " + e.getMessage());
		logger.log(BasicLevel.DEBUG, e);
	    } catch (FinderException e) {
		logger.log(BasicLevel.ERROR, "Category 11006 not found");
		logger.log(BasicLevel.DEBUG, e);
	    }
	    // extFoil
	    try {
		CategoryLocal c = ch.findByPrimaryKey(new Integer(11006));
		ProductLocal p = c.getProductByCode(form.getExtFoil());		
		s.setVar("extFoilObj", p, ProductLocal.class);
	    } catch (ScriptErrorException e) {
		logger.log(BasicLevel.ERROR, "ScriptErrorException " + e.getMessage());
		logger.log(BasicLevel.DEBUG, e);
	    } catch (FinderException e) {
		logger.log(BasicLevel.ERROR, "Category 11006 not found");
		logger.log(BasicLevel.DEBUG, e);
	    }
	    // intFoilSec
	    try {
		CategoryLocal c = ch.findByPrimaryKey(new Integer(11006));
		ProductLocal p = c.getProductByCode(form.getIntFoilSec());		
		s.setVar("intFoilSecObj", p, ProductLocal.class);
	    } catch (ScriptErrorException e) {
		logger.log(BasicLevel.ERROR, "ScriptErrorException " + e.getMessage());
		logger.log(BasicLevel.DEBUG, e);
	    } catch (FinderException e) {
		logger.log(BasicLevel.ERROR, "Category 11006 not found");
		logger.log(BasicLevel.DEBUG, e);
	    }
	    // intFoilSec
	    try {
		CategoryLocal c = ch.findByPrimaryKey(new Integer(11006));
		ProductLocal p = c.getProductByCode(form.getExtFoilSec());		
		s.setVar("extFoilSecObj", p, ProductLocal.class);
	    } catch (ScriptErrorException e) {
		logger.log(BasicLevel.ERROR, "ScriptErrorException " + e.getMessage());
		logger.log(BasicLevel.DEBUG, e);
	    } catch (FinderException e) {
		logger.log(BasicLevel.ERROR, "Category 11006 not found");
		logger.log(BasicLevel.DEBUG, e);
	    }
	    // isolation
	    try {
		CategoryLocal c = ch.findByPrimaryKey(new Integer(11010));
		ProductLocal p = c.getProductByCode(form.getIsolation());		
		s.setVar("isolationObj", p, ProductLocal.class);
	    } catch (ScriptErrorException e) {
		logger.log(BasicLevel.ERROR, "ScriptErrorException " + e.getMessage());
		logger.log(BasicLevel.DEBUG, e);
	    } catch (FinderException e) {
		logger.log(BasicLevel.ERROR, "Category 11010 not found");
		logger.log(BasicLevel.DEBUG, e);
	    }
	    // openingDir
	    try {
		CategoryLocal c = ch.findByPrimaryKey(new Integer(11004));
		ProductLocal p = c.getProductByCode(form.getOpeningDir());		
		s.setVar("openingDirObj", p, ProductLocal.class);
	    } catch (ScriptErrorException e) {
		logger.log(BasicLevel.ERROR, "ScriptErrorException " + e.getMessage());
		logger.log(BasicLevel.DEBUG, e);
	    } catch (FinderException e) {
		logger.log(BasicLevel.ERROR, "Category 11004 not found");
		logger.log(BasicLevel.DEBUG, e);
	    }
	    // openingSide
	    try {
		CategoryLocal c = ch.findByPrimaryKey(new Integer(11005));
		ProductLocal p = c.getProductByCode(form.getOpeningSide());		
		s.setVar("openingSideObj", p, ProductLocal.class);
	    } catch (ScriptErrorException e) {
		logger.log(BasicLevel.ERROR, "ScriptErrorException " + e.getMessage());
		logger.log(BasicLevel.DEBUG, e);
	    } catch (FinderException e) {
		logger.log(BasicLevel.ERROR, "Category 11005 not found");
		logger.log(BasicLevel.DEBUG, e);
	    }
	    // foilPosition
	    try {
		CategoryLocal c = ch.findByPrimaryKey(new Integer(11008));
		ProductLocal p = c.getProductByCode(form.getFoilPosition());		
		s.setVar("foilPositionObj", p, ProductLocal.class);
	    } catch (ScriptErrorException e) {
		logger.log(BasicLevel.ERROR, "ScriptErrorException " + e.getMessage());
		logger.log(BasicLevel.DEBUG, e);
	    } catch (FinderException e) {
		logger.log(BasicLevel.ERROR, "Category 11008 not found");
		logger.log(BasicLevel.DEBUG, e);
	    }
	    // benefBroascaTip
	    try {
		CategoryLocal c = ch.findByPrimaryKey(new Integer(11020));
		ProductLocal p = c.getProductByCode(form.getBenefBroascaTip());		
		s.setVar("benefBroascaTipObj", p, ProductLocal.class);
	    } catch (ScriptErrorException e) {
		logger.log(BasicLevel.ERROR, "ScriptErrorException " + e.getMessage());
		logger.log(BasicLevel.DEBUG, e);
	    } catch (FinderException e) {
		logger.log(BasicLevel.ERROR, "Category 11020 not found");
		logger.log(BasicLevel.DEBUG, e);
	    }
	    // benefCilindruTip
	    try {
		CategoryLocal c = ch.findByPrimaryKey(new Integer(11021));
		ProductLocal p = c.getProductByCode(form.getBenefCilindruTip());		
		s.setVar("benefCilindruTipObj", p, ProductLocal.class);
	    } catch (ScriptErrorException e) {
		logger.log(BasicLevel.ERROR, "ScriptErrorException " + e.getMessage());
		logger.log(BasicLevel.DEBUG, e);
	    } catch (FinderException e) {
		logger.log(BasicLevel.ERROR, "Category 11021 not found");
		logger.log(BasicLevel.DEBUG, e);
	    }
	    // benefSildTip
	    try {
		CategoryLocal c = ch.findByPrimaryKey(new Integer(11023));
		ProductLocal p = c.getProductByCode(form.getBenefSildTip());		
		s.setVar("benefSildTipObj", p, ProductLocal.class);
	    } catch (ScriptErrorException e) {
		logger.log(BasicLevel.ERROR, "ScriptErrorException " + e.getMessage());
		logger.log(BasicLevel.DEBUG, e);
	    } catch (FinderException e) {
		logger.log(BasicLevel.ERROR, "Category 11023 not found");
		logger.log(BasicLevel.DEBUG, e);
	    }
	    // benefYallaTip
	    try {
		CategoryLocal c = ch.findByPrimaryKey(new Integer(11022));
		ProductLocal p = c.getProductByCode(form.getBenefYallaTip());		
		s.setVar("benefYallaTipObj", p, ProductLocal.class);
	    } catch (ScriptErrorException e) {
		logger.log(BasicLevel.ERROR, "ScriptErrorException " + e.getMessage());
		logger.log(BasicLevel.DEBUG, e);
	    } catch (FinderException e) {
		logger.log(BasicLevel.ERROR, "Category 11022 not found");
		logger.log(BasicLevel.DEBUG, e);
	    }
	    // benefBaraAntipanicaTip
	    try {
		CategoryLocal c = ch.findByPrimaryKey(new Integer(11024));
		ProductLocal p = c.getProductByCode(form.getBenefBaraAntipanicaTip());		
		s.setVar("benefBaraAntipanicaTipObj", p, ProductLocal.class);
	    } catch (ScriptErrorException e) {
		logger.log(BasicLevel.ERROR, "ScriptErrorException " + e.getMessage());
		logger.log(BasicLevel.DEBUG, e);
	    } catch (FinderException e) {
		logger.log(BasicLevel.ERROR, "Category 11024 not found");
		logger.log(BasicLevel.DEBUG, e);
	    }

	    // broascaId
	    try {
		ProductLocal p = ph.findByPrimaryKey(form.getBroascaId());
		s.setVar("broasca", p, ProductLocal.class);
	    } catch (ScriptErrorException e) {
		logger.log(BasicLevel.ERROR, "ScriptErrorException " + e.getMessage());
		logger.log(BasicLevel.DEBUG, e);
	    } catch (FinderException e) {
		logger.log(BasicLevel.ERROR, "Product not found");
		logger.log(BasicLevel.DEBUG, e);
	    }
	    // cilindruId
	    try {
		ProductLocal p = ph.findByPrimaryKey(form.getCilindruId());
		s.setVar("cilindru", p, ProductLocal.class);
	    } catch (ScriptErrorException e) {
		logger.log(BasicLevel.ERROR, "ScriptErrorException " + e.getMessage());
		logger.log(BasicLevel.DEBUG, e);
	    } catch (FinderException e) {
		logger.log(BasicLevel.ERROR, "Product not found");
		logger.log(BasicLevel.DEBUG, e);
	    }
	    // copiatCheieId
	    try {
		ProductLocal p = ph.findByPrimaryKey(form.getCopiatCheieId());
		s.setVar("copiatCheie", p, ProductLocal.class);
	    } catch (ScriptErrorException e) {
		logger.log(BasicLevel.ERROR, "ScriptErrorException " + e.getMessage());
		logger.log(BasicLevel.DEBUG, e);
	    } catch (FinderException e) {
		logger.log(BasicLevel.ERROR, "Product not found");
		logger.log(BasicLevel.DEBUG, e);
	    }
	    // sildId
	    try {
		ProductLocal p = ph.findByPrimaryKey(form.getSildId());
		s.setVar("sild", p, ProductLocal.class);
	    } catch (ScriptErrorException e) {
		logger.log(BasicLevel.ERROR, "ScriptErrorException " + e.getMessage());
		logger.log(BasicLevel.DEBUG, e);
	    } catch (FinderException e) {
		logger.log(BasicLevel.ERROR, "Product not found");
		logger.log(BasicLevel.DEBUG, e);
	    }
	    // rozetaId
	    try {
		ProductLocal p = ph.findByPrimaryKey(form.getRozetaId());
		s.setVar("rozeta", p, ProductLocal.class);
	    } catch (ScriptErrorException e) {
		logger.log(BasicLevel.ERROR, "ScriptErrorException " + e.getMessage());
		logger.log(BasicLevel.DEBUG, e);
	    } catch (FinderException e) {
		logger.log(BasicLevel.ERROR, "Product not found");
		logger.log(BasicLevel.DEBUG, e);
	    }
	    // manerId
	    try {
		ProductLocal p = ph.findByPrimaryKey(form.getManerId());
		s.setVar("maner", p, ProductLocal.class);
	    } catch (ScriptErrorException e) {
		logger.log(BasicLevel.ERROR, "ScriptErrorException " + e.getMessage());
		logger.log(BasicLevel.DEBUG, e);
	    } catch (FinderException e) {
		logger.log(BasicLevel.ERROR, "Product not found");
		logger.log(BasicLevel.DEBUG, e);
	    }
	    // yalla1Id
	    try {
		ProductLocal p = ph.findByPrimaryKey(form.getYalla1Id());
		s.setVar("yalla1", p, ProductLocal.class);
	    } catch (ScriptErrorException e) {
		logger.log(BasicLevel.ERROR, "ScriptErrorException " + e.getMessage());
		logger.log(BasicLevel.DEBUG, e);
	    } catch (FinderException e) {
		logger.log(BasicLevel.ERROR, "Product not found");
		logger.log(BasicLevel.DEBUG, e);
	    }
	    // yalla2Id
	    try {
		ProductLocal p = ph.findByPrimaryKey(form.getYalla2Id());
		s.setVar("yalla2", p, ProductLocal.class);
	    } catch (ScriptErrorException e) {
		logger.log(BasicLevel.ERROR, "ScriptErrorException " + e.getMessage());
		logger.log(BasicLevel.DEBUG, e);
	    } catch (FinderException e) {
		logger.log(BasicLevel.ERROR, "Product not found");
		logger.log(BasicLevel.DEBUG, e);
	    }
	    // baraAntipanicaId
	    try {
		ProductLocal p = ph.findByPrimaryKey(form.getBaraAntipanicaId());
		s.setVar("baraAntipanica", p, ProductLocal.class);
	    } catch (ScriptErrorException e) {
		logger.log(BasicLevel.ERROR, "ScriptErrorException " + e.getMessage());
		logger.log(BasicLevel.DEBUG, e);
	    } catch (FinderException e) {
		logger.log(BasicLevel.ERROR, "Product not found");
		logger.log(BasicLevel.DEBUG, e);
	    }
	    // selectorOrdineId
	    try {
		ProductLocal p = ph.findByPrimaryKey(form.getSelectorOrdineId());
		s.setVar("selectorOrdine", p, ProductLocal.class);
	    } catch (ScriptErrorException e) {
		logger.log(BasicLevel.ERROR, "ScriptErrorException " + e.getMessage());
		logger.log(BasicLevel.DEBUG, e);
	    } catch (FinderException e) {
		logger.log(BasicLevel.ERROR, "Product not found");
		logger.log(BasicLevel.DEBUG, e);
	    }
	    // amortizorId
	    try {
		ProductLocal p = ph.findByPrimaryKey(form.getAmortizorId());
		s.setVar("amortizor", p, ProductLocal.class);
	    } catch (ScriptErrorException e) {
		logger.log(BasicLevel.ERROR, "ScriptErrorException " + e.getMessage());
		logger.log(BasicLevel.DEBUG, e);
	    } catch (FinderException e) {
		logger.log(BasicLevel.ERROR, "Product not found");
		logger.log(BasicLevel.DEBUG, e);
	    }
	    // decupareSistemId
	    try {
		ProductLocal p = ph.findByPrimaryKey(form.getDecupareSistemId());
		s.setVar("decupareSistem", p, ProductLocal.class);
	    } catch (ScriptErrorException e) {
		logger.log(BasicLevel.ERROR, "ScriptErrorException " + e.getMessage());
		logger.log(BasicLevel.DEBUG, e);
	    } catch (FinderException e) {
		logger.log(BasicLevel.ERROR, "Product not found");
		logger.log(BasicLevel.DEBUG, e);
	    }
	    // manerSemiclindruId
	    try {
		ProductLocal p = ph.findByPrimaryKey(form.getManerSemicilindruId());
		s.setVar("manerSemicilindru", p, ProductLocal.class);
	    } catch (ScriptErrorException e) {
		logger.log(BasicLevel.ERROR, "ScriptErrorException " + e.getMessage());
		logger.log(BasicLevel.DEBUG, e);
	    } catch (FinderException e) {
		logger.log(BasicLevel.ERROR, "Product not found");
		logger.log(BasicLevel.DEBUG, e);
	    }
	    // alteSisteme1Id
	    try {
		ProductLocal p = ph.findByPrimaryKey(form.getAlteSisteme1Id());
		s.setVar("alteSisteme1", p, ProductLocal.class);
	    } catch (ScriptErrorException e) {
		logger.log(BasicLevel.ERROR, "ScriptErrorException " + e.getMessage());
		logger.log(BasicLevel.DEBUG, e);
	    } catch (FinderException e) {
		logger.log(BasicLevel.ERROR, "Product not found");
		logger.log(BasicLevel.DEBUG, e);
	    }
	    // alteSisteme2Id
	    try {
		ProductLocal p = ph.findByPrimaryKey(form.getAlteSisteme2Id());
		s.setVar("alteSisteme2", p, ProductLocal.class);
	    } catch (ScriptErrorException e) {
		logger.log(BasicLevel.ERROR, "ScriptErrorException " + e.getMessage());
		logger.log(BasicLevel.DEBUG, e);
	    } catch (FinderException e) {
		logger.log(BasicLevel.ERROR, "Product not found");
		logger.log(BasicLevel.DEBUG, e);
	    }
	} catch (NamingException e) {
	    logger.log(BasicLevel.ERROR, e);
	}
    }

}
