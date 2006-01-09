package ro.kds.erp.biz.setum;

import ro.kds.erp.biz.ResponseBean;
import javax.ejb.FinderException;
import ro.kds.erp.biz.ResponseBean;
import ro.kds.erp.biz.PreferencesBean;
import java.util.Calendar;
import javax.naming.InitialContext;
import ro.kds.erp.data.OfferLocalHome;
import javax.rmi.PortableRemoteObject;
import ro.kds.erp.data.OfferLocal;
import ro.kds.erp.data.DocumentLocal;
import javax.naming.NamingException;
import org.objectweb.util.monolog.api.BasicLevel;
import javax.ejb.CreateException;
import javax.naming.Context;
import java.util.Date;
import ro.kds.erp.biz.Preferences;
import ro.kds.erp.data.DataLayerException;
import java.util.Collection;
import java.util.Iterator;
import java.math.BigDecimal;
import ro.kds.erp.data.OfferItemLocal;
import ro.kds.erp.data.OfferItemLocalHome;
import ro.kds.erp.data.ProductLocal;
import ro.kds.erp.data.CategoryLocalHome;
import ro.kds.erp.data.CategoryLocal;
import java.util.LinkedHashMap;

import java.util.Map;
import javax.ejb.RemoveException;
import ro.kds.erp.data.ProductLocalHome;
import ro.kds.erp.biz.ResponseBean;
import ro.kds.erp.biz.ResponseBean;
import java.util.TreeMap;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import ro.kds.erp.biz.SequenceHome;
import ro.kds.erp.biz.Sequence;
import javax.jms.JMSException;

/**
 * Customization of generated class 
 * <code>ro.kds.erp.biz.setum.ResponseBean</code>. Provides code for
 * <code>createNewFormBean</code>, <code>loadFieldsData</code> and
 * <code>saveFormData</code>.
 *
 *
 * Created: Tue Oct 25 05:47:33 2005
 *
 * @author <a href="mailto:Mihai Giurgeanu@CRIMIRA"></a>
 * @version 1.0
 */
public class StandardOfferBean extends ro.kds.erp.biz.setum.basic.StandardOfferBean {

    /**
     * The category for offers managed by this bean.
     */
    public static final Integer STANDARD_OFFERS_CATEGORY =
	new Integer(1000);

    /**
     * The selected offer line in the subform.
     */
    OfferItemLocal offerItem;

    /**
     * Initialize the form fields.
     *
     */
    public final void createNewFormBean() {
	super.createNewFormBean(); // create the FormBean
	offerItem = null;

	// get the preferences
	int availability; // the number of days the offer should be available
	try {
	    Preferences prefs = PreferencesBean.getPreferences();
	    availability = prefs.getInteger("offer.availability", 
					    new Integer(30)).intValue();
	} catch (Exception e) {
	    logger.log(BasicLevel.ERROR, "Exception when geting preferences: ", e);
	    availability = 30;
	}

	Integer offerNo;
	try {
	    InitialContext ic = new InitialContext();
	    Context env = (Context) ic.lookup("java:comp/env");
	    
	    SequenceHome sh = (SequenceHome)PortableRemoteObject.narrow
		(env.lookup("ejb/SequenceHome"), SequenceHome.class);
	    Sequence s = sh.create();
	    offerNo = s.getNext("ro.setumsa.sequnces.offers");
	} catch (Exception e) {
	    offerNo = null;
	    logger.log(BasicLevel.WARN, "Can not get a number for offer", e);
	}

	form.setNo(String.valueOf(offerNo));
	form.setDocDate(new Date());

	form.setDateFrom(new Date());
	Calendar dateToCal = Calendar.getInstance();
	dateToCal.setTime(form.getDateFrom());
	dateToCal.add(Calendar.DATE, availability);
	form.setDateTo(dateToCal.getTime());
	form.setPeriod(new Integer(availability));
	form.setDiscontinued(new Boolean(false));

	form.setName("");
	form.setDescription("");
	form.setComment("");

	// subform fields
	form.setProductId(new Integer(0));
	form.setPrice(new BigDecimal(0));
	form.setRelativeGain(new Double(0));
	form.setAbsoluteGain(new BigDecimal(0));

	// read only fields on subform
	form.setProductCategory("");
	form.setProductCode("");
	form.setProductName("");
	form.setEntryPrice(new BigDecimal(0));
	form.setSellPrice(new BigDecimal(0));
    }

    /**
     * Saves data from the <code>form</code> bean into persistent layer.
     *
     * @return a <code>ResponseBean</code> value
     */
    public final ResponseBean saveFormData() {
	ResponseBean r = new ResponseBean();
	try {
	    InitialContext ic = new InitialContext();
	    Context env = (Context)ic.lookup("java:comp/env");
	    
	    OfferLocalHome oh = (OfferLocalHome)PortableRemoteObject.
		narrow(env.lookup("ejb/OfferHome"), OfferLocalHome.class);
	    OfferLocal offer;
	    if(id == null) {
		// the form bean contains data for a new form
		offer = oh.create();
		offer.setCategory(STANDARD_OFFERS_CATEGORY);
		id = offer.getId();
	    } else {
		offer = oh.findByPrimaryKey(id);
	    }
	    DocumentLocal docData = offer.getDocument();
	    docData.setNumber(form.getNo());
	    docData.setDate(form.getDocDate());
	    offer.setDateFrom(form.getDateFrom());
	    offer.setDateTo(form.getDateTo());
	    offer.setDiscontinued(form.getDiscontinued());
	    offer.setName(form.getName());
	    offer.setDescription(form.getDescription());
	    offer.setComment(form.getComment());
	} catch (NamingException e) {
	    logger.log(BasicLevel.ERROR, "Naming exception was thrown. Is ejb/OfferHome defined?", e);
	    r.setCode(1);
	    r.setMessage("Eroare de configurare. Datele nu au fost salvate");
	} catch (DataLayerException e) {
	    logger.log(BasicLevel.ERROR, "DataLayerException caught. Is the name ejb/DocumentHome defined in the database?", e);
	    r.setCode(1);
	    r.setMessage("Eroare de configurare. Datele nu au fost salvate");
	} catch (CreateException e) {
	    logger.log(BasicLevel.ERROR, "Can not create the new offer object", e);
	    r.setCode(3);
	    r.setMessage("Eroare in baza de date la creerea unei noi inregistrari. Datele nu au fost salvate!");
	} catch (FinderException e) {
	    logger.log(BasicLevel.ERROR, "No such object for offer id = " + id, e);
	    r.setCode(3);
	    r.setMessage("Aceasta oferta nu a fost gasita in baza de date. Datele nu au putut fi salvate!");
	}
	return r;
    }

    /**
     * Get the data from the database and intialize the form bean.
     *
     * @return a <code>ResponseBean</code> value
     * @exception FinderException if an error occurs
     */
    public final ResponseBean loadFields() throws FinderException {
	ResponseBean r = new ResponseBean();
	
	try {
	    InitialContext ic = new InitialContext();
	    Context env = (Context)ic.lookup("java:comp/env");
	    
	    OfferLocalHome oh = (OfferLocalHome)PortableRemoteObject.
		narrow(env.lookup("ejb/OfferHome"), OfferLocalHome.class);

	    OfferLocal offer = oh.findByPrimaryKey(id);
	    DocumentLocal doc = offer.getDocument();
	    
	    form.setNo(doc.getNumber());
	    form.setDocDate(doc.getDate());
	    form.setDateFrom(offer.getDateFrom());
	    form.setDateTo(offer.getDateTo());

	    int period = (int)(form.getDateFrom().getTime() -
			       form.getDateTo().getTime()) / (1000*3600*24);
	    form.setPeriod(new Integer(period));
	    
	    form.setName(offer.getName());
	    form.setDescription(offer.getDescription());
	    form.setComment(offer.getComment());
	} catch (NamingException e) {
	    logger.log(BasicLevel.ERROR, "Can not get the jndi name for ejb/OfferHome", e);
	    r.setCode(3);
	    r.setMessage("Eroare de configurare. Nu am putut incarca datele!");
	}
	
	return r;
    }

    public ResponseBean offersListing() {
	try {
	    InitialContext ic = new InitialContext();
	    Context env = (Context)ic.lookup("java:comp/env");
	    OfferLocalHome oh = (OfferLocalHome)
		PortableRemoteObject.narrow
		(env.lookup("ejb/OfferHome"), OfferLocalHome.class);

	    Collection offers = oh.findByCategory(STANDARD_OFFERS_CATEGORY);
	    ArrayList sortedOffers = new ArrayList(offers);
	    Collections.sort(sortedOffers, new Comparator() {
		    public int compare(Object o1, Object o2) {
			OfferLocal offer1 = (OfferLocal) o1;
			OfferLocal offer2 = (OfferLocal) o2;
			return - offer1.getDateFrom().
			    compareTo(offer2.getDateFrom());
		    }
		});

	    ResponseBean r = new ResponseBean();
	    for(Iterator i = offers.iterator(); i.hasNext(); ) {
		OfferLocal offer = (OfferLocal) i.next();
		r.addRecord();
		r.addField("id", offer.getId());
		r.addField("offersListing.no", offer.getDocument().getNumber());
		r.addField("offersListing.name", offer.getName());
		r.addField("offersListing.docDate", offer.getDocument().getDate());
		r.addField("offersListing.dateFrom", offer.getDateFrom());
		r.addField("offersListing.dateTo", offer.getDateTo());
		r.addField("offersListing.status", offer.getDocument().getIsDraft().booleanValue()?"draft":(offer.getDiscontinued().booleanValue()?"anulata":"lansata"));
	    }
	    return r;
	} catch (Exception e) {
	    ResponseBean r = new ResponseBean();
	    r.setCode(1);
	    r.setMessage("Eroare sistem. Nu pot incarca lista de oferte!");
	    logger.log(BasicLevel.ERROR, "Can not make the offers list", e);
	    return r;
	}
    }


    public ResponseBean lineItemsListing() {
	ResponseBean r;

	try {
	    InitialContext ic = new InitialContext();
	    Context env = (Context)ic.lookup("java:comp/env");
	    
	    OfferLocalHome oh = (OfferLocalHome)PortableRemoteObject.
		narrow(env.lookup("ejb/OfferHome"), OfferLocalHome.class);

	    OfferLocal offer = oh.findByPrimaryKey(id);

	    Collection offerItems = offer.getItems();
	    r = new ResponseBean();
	    for(Iterator i = offerItems.iterator(); i.hasNext();) {
		OfferItemLocal item = (OfferItemLocal)i.next();
		r.addRecord();
		r.addField("id", item.getId());
		ProductLocal p = item.getProduct();
		if(p != null) {
		    r.addField("offerLines.category", 
			       p.getCategory().getName());
		    r.addField("offerLines.name", p.getName());
		}

		r.addField("offerLines.price", item.getPrice());
		
	    }

	} catch (Exception e) {
	    logger.log(BasicLevel.WARN, "Current offer can not be selected", 
		       e);
	    r = new ResponseBean();
	    r.setCode(4);
	    r.setMessage("Nu se poate incarca lista de oferte. Ati selectat o oferta?");
	}
	return r;
    }

    /**
     * Builds a <code>Collection</code> of <code>Map</code> objects to be
     * used in generating the offer report. For each item of the current
     * offer, puts a <code>Map</code> object into returned 
     * <code>Collection</code>.
     *
     * @return a <code>Collection</code> of <code>Map</code> objects each
     * beeing a <code>Map</code> representation of current offer's items.
     */
    public Collection lineItemsCollectionMap() {
	Collection reportData;
	
	try {
	    InitialContext ic = new InitialContext();
	    Context env = (Context)ic.lookup("java:comp/env");
	    
	    OfferLocalHome oh = (OfferLocalHome)PortableRemoteObject.
		narrow(env.lookup("ejb/OfferHome"), OfferLocalHome.class);
	    OfferLocal offer = oh.findByPrimaryKey(id);

	    Collection offerItems = offer.getItems();
	    TreeMap sortedData = new TreeMap();
	    for(Iterator i = offerItems.iterator(); i.hasNext();) {
		OfferItemLocal item = (OfferItemLocal)i.next();
		HashMap dataRow = new HashMap();
		ProductLocal p = item.getProduct();
		CategoryLocal cat = p.getCategory();
		dataRow.put("name", cat.getName() + 
			    " - " + p.getName());
		dataRow.put("code", p.getCode());
		dataRow.put("sellPrice", item.getPrice());
		sortedData.put(cat.getName() + " - " + p.getName(), dataRow);
	    }
	    reportData = sortedData.values();
	} catch (Exception e) {
	    logger.log(BasicLevel.ERROR,
		       "Can not build the report data. Exception caught.", e);
	    reportData = new ArrayList();
	}
	return new ArrayList(reportData);
    }

    public ResponseBean loadSubForm(Integer loadId) {
	ResponseBean r;

	try {


	    InitialContext ic = new InitialContext();
	    Context env = (Context)ic.lookup("java:comp/env");
	    
	    OfferItemLocalHome oh = (OfferItemLocalHome)PortableRemoteObject.
		narrow(env.lookup("ejb/OfferItemHome"), 
		       OfferItemLocalHome.class);
	    offerItem = oh.findByPrimaryKey(loadId);
	    ProductLocal p = offerItem.getProduct();
	    
	    if(p != null) {
		form.setProductId(p.getId());
		form.setEntryPrice(p.getEntryPrice());
		form.setSellPrice(p.getSellPrice());
		form.setProductCategory(p.getCategory().getName());
		form.setProductCode(p.getCode());
		form.setProductName(p.getName());
	    }
	    else {
		form.setProductId(new Integer(0));
		form.setEntryPrice(new BigDecimal(0));
		form.setSellPrice(new BigDecimal(0));
		form.setProductCategory("");
		form.setProductCode("");
		form.setProductName("");
	    }

	    form.setPrice(offerItem.getPrice());
	    computeCalculatedFields(null);
	    
	    r = new ResponseBean();
	    r.addRecord();
	    copyFieldsToResponse(r);

	    // add the list values used for this form
	    r.addValueList("productId", getProductsVL());
	} catch (NamingException e) {
	    r = new ResponseBean();
	    r.setCode(1);
	    r.setMessage("Eroare de configurare in sistem. Detaliile acestei linii de pe oferta nu pot fi incarcate.");
	    logger.log(BasicLevel.ERROR, "Naming exception. Is ejb/OfferItemHome defined?", e);
	} catch (FinderException e) {	    
	    r = new ResponseBean();
	    r.setCode(3);
	    r.setMessage("Inregistrarea nu exista in baza de date.");
	    logger.log(BasicLevel.ERROR, "OfferItemLocal not found for id " + loadId, e);
	}

	return r;
    }
    
    /**
     * Adding a new item on an offer.
     */
    public ResponseBean addNewItem() {
	offerItem = null;
	form.setProductId(new Integer(0));
	form.setEntryPrice(new BigDecimal(0));
	form.setSellPrice(new BigDecimal(0));
	form.setProductCategory("");
	form.setProductCode("");
	form.setProductName("");
	form.setPrice(new BigDecimal(0));
	
	
	computeCalculatedFields(null);
	ResponseBean r = new ResponseBean();
	r.addRecord();
	copyFieldsToResponse(r);
	
	// add the list values used for this form
	r.addValueList("productId", getProductsVL());

	return r;
    }

    public ResponseBean removeItem() {
	ResponseBean r;
	if(offerItem == null) {
	    r = new ResponseBean();
	    r.setCode(4);
	    r.setMessage("Nu este selectat nici un rand pe oferta");
	} else {
	    try {
		offerItem.remove();
		offerItem = null;
		r = new ResponseBean();
	    } catch (RemoveException e) {
		r = new ResponseBean();
		r.setCode(3);
		r.setMessage("Eroare la stergerea liniei: " + e.getMessage());
		logger.log(BasicLevel.ERROR, "Exception when removing item", e);
	    }
	}
	return r;
    }


    public ResponseBean saveSubForm() {
	ResponseBean r;
	OfferLocal offer = getCurrentOffer();
	if(offer == null) {
	    r = new ResponseBean();
	    r.setCode(4);
	    r.setMessage("Nu este selectata nici o oferta");
	} else {
	    try {
		InitialContext ic = new InitialContext();
		Context env = (Context) ic.lookup("java:comp/env");
		if(offerItem == null) {
		    OfferItemLocalHome oh = (OfferItemLocalHome)
			PortableRemoteObject.narrow
			(env.lookup("ejb/OfferItemHome"),
			 OfferItemLocalHome.class);
		    offerItem = oh.create();
		    offer.getItems().add(offerItem);
		}
		ProductLocalHome ph = 
		    (ProductLocalHome)PortableRemoteObject.narrow
		    (env.lookup("ejb/ProductHome"), ProductLocalHome.class);
		offerItem.setProduct(ph.findByPrimaryKey(form.getProductId()));
		offerItem.setPrice(form.getPrice());
		r = new ResponseBean();
	    } catch (Exception e) {
		r = new ResponseBean();
		r.setCode(1);
		r.setMessage("Eroare la operatia de salvare. Datele nu pot fi salvate");
		logger.log(BasicLevel.ERROR, e);
	    }
	}
	return r;
    }

    /**
     * Builds the value list for productId control. It contains the
     * products allowed for this kind of offer.
     *
     * First, this function looks at the category id in the environment
     * entry <code>rootCategoryId</code>. It takes all of its subcategories
     * and, for each of them, it adds all thte products to the list.
     *
     * @returns a <code>Map</code> containing the product name as key and
     * the product id as value.
     */
    protected Map getProductsVL() {
	LinkedHashMap productsVL = new LinkedHashMap();
	try {
	    InitialContext ic = new InitialContext();
	    Context env = (Context) ic.lookup("java:comp/env");
	    
	    Integer rootCategoryId = (Integer)env.lookup("rootCategoryId");
	    CategoryLocalHome ch = 
		(CategoryLocalHome)PortableRemoteObject.narrow
		(env.lookup("ejb/CategoryHome"), CategoryLocalHome.class);
	    CategoryLocal rootCategory = ch.findByPrimaryKey(rootCategoryId);
	    Collection categs = rootCategory.getSubCategories();
	    
	    for(Iterator i=categs.iterator(); i.hasNext(); ) {
		CategoryLocal c = (CategoryLocal)i.next();
		Collection products = c.getProducts();
		for(Iterator j = products.iterator(); j.hasNext(); ) {
		    ProductLocal p = (ProductLocal)j.next();
		    productsVL.put(p.getName(), p.getId());
		}
	    }
	} catch (Exception e) {
	    logger.log(BasicLevel.ERROR, 
		       "Can not make products value list", e);
	}
	return productsVL;
    }

    /**
     * Retrieves the offer object corresponding to the id instance variable.
     *
     * @return the current OfferLocal object or null if id is null.
     */
    protected OfferLocal getCurrentOffer() {
	if(id == null)
	    return null;

	try {
	    InitialContext ic = new InitialContext();
	    Context env = (Context)ic.lookup("java:comp/env");
	    
	    OfferLocalHome oh = (OfferLocalHome)PortableRemoteObject.
		narrow(env.lookup("ejb/OfferHome"), OfferLocalHome.class);
	    return oh.findByPrimaryKey(id);

	} catch (Exception e) {
	    logger.log(BasicLevel.ERROR, "Current offer can not be retrieved");
	    return null;
	}

    }


    /**
     * Add details about product to the response, by setting
     * the values of fields productCategory, productCode, productName,
     * entryPrice and sellPrice.
     *
     */
    public ResponseBean computeCalculatedFields(ResponseBean responseBean) {
	try {
	    InitialContext ic = new InitialContext();
	    Context env = (Context)ic.lookup("java:comp/env");
	    ProductLocalHome ph = (ProductLocalHome)PortableRemoteObject.narrow
		(env.lookup("ejb/ProductHome"), ProductLocalHome.class);
	    ProductLocal p = ph.findByPrimaryKey(form.getProductId());
	    
	    form.setProductCategory(p.getCategory().getName());
	    form.setProductCode(p.getCode());
	    form.setProductName(p.getName());
	    form.setEntryPrice(p.getEntryPrice());
	    form.setSellPrice(p.getSellPrice());
	} catch (FinderException e) {
	    // The product might not of been chosen on the form,
	    // so it is quite normal to get a FinderException.
	    logger.log(BasicLevel.DEBUG, "Product not found for id " + 
		       form.getProductId());
	    initItemCalculatedFields();

	} catch (Exception e) {
	    logger.log(BasicLevel.ERROR, "Could not retrieve the product's details", e);
	    initItemCalculatedFields();
	    
	}
	
	ResponseBean r = super.computeCalculatedFields(responseBean);

	r.addField("productCategory", form.getProductCategory());
	r.addField("productCode", form.getProductCode());
	r.addField("productName", form.getProductName());
	r.addField("entryPrice", form.getEntryPrice());
	r.addField("sellPrice", form.getSellPrice());

	return r;
    }
    
    /**
     * Initialize the calculated fields of the subform
     */
    protected void initItemCalculatedFields() {
	form.setProductCategory("-");
	form.setProductCode("-");
	form.setProductName("-");
	form.setEntryPrice(new BigDecimal(0));
	form.setSellPrice(new BigDecimal(0));
    }

    /**
     * Making an offer the current offer will have the effect that all the 
     * products contained in this offer will have a default salePrice given
     * by this offer. The products that are not in the current offer are
     * not affected. To discontinue older products, just discontinue the
     * old offers that are containing these products.
     */
    public ResponseBean makeCurrent() {
	OfferLocal offer = getCurrentOffer(); // get the offer object
	if(offer == null) {
	    ResponseBean r = new ResponseBean();
	    r.setCode(4);
	    r.setMessage("Selectati o oferta mai intai");
	    return r;
	}

	offer.setDiscontinued(new Boolean(false));
	offer.getDocument().setIsDraft(new Boolean(false));

	Collection items = offer.getItems();
	for(Iterator i = items.iterator(); i.hasNext();) {
	    OfferItemLocal item = (OfferItemLocal)i.next();
	    item.getProduct().setSellPrice(item.getPrice());
	}

	try {
	    PricesUpdater.updatePrices(); // composite products prices
	} catch (Exception e) {
	    logger.log(BasicLevel.ERROR, "Failed notifying the message bean about price updates. Rolling back this transaction.");
	    ResponseBean r = new ResponseBean();
	    r.setCode(1);
	    r.setMessage("Eroare aplicatie. Preturile nu au putut fi actualizate. Oferta aceasta NU a fost facuta curenta.");
	    ejbContext.setRollbackOnly();
	}
		
	return new ResponseBean();
    }

    /**
     * When an offer is discontinued it will discontinue all products
     * in this offer by setting there sellPrice to 0.
     */
    public ResponseBean discontinue() {
	OfferLocal offer = getCurrentOffer();
	if(offer == null) {
	    ResponseBean r = new ResponseBean();
	    r.setCode(4);
	    r.setMessage("Selectati o oferta mai intai");
	    return r;
	}

	offer.setDiscontinued(new Boolean(true));
	offer.getDocument().setIsDraft(new Boolean(false));

	Collection items = offer.getItems();
	for(Iterator i = items.iterator(); i.hasNext();) {
	    OfferItemLocal item = (OfferItemLocal)i.next();
	    item.getProduct().setSellPrice(new BigDecimal(0));
	}
	
	try {
	    PricesUpdater.updatePrices(); // composite products prices
	} catch (Exception e) {
	    logger.log(BasicLevel.ERROR, "Failed notifying the message bean about price updates. Rolling back this transaction.");
	    ResponseBean r = new ResponseBean();
	    r.setCode(1);
	    r.setMessage("Eroare aplicatie. Preturile nu au putut fi actualizate. Oferta aceasta NU a fost retrasa.");
	    ejbContext.setRollbackOnly();
	}

	return new ResponseBean();
    }

    /**
     * Call the offersListing method. Makes the bean usable by
     * CallDispatcherServlet
     */
    public ResponseBean loadListing() {
	return offersListing();
    }
}
