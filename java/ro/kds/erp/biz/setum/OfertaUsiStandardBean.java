package ro.kds.erp.biz.setum;

import ro.kds.erp.data.OfferItemLocal;
import ro.kds.erp.biz.PreferencesBean;
import org.objectweb.util.monolog.api.BasicLevel;
import java.util.Calendar;
import java.math.BigDecimal;
import ro.kds.erp.biz.ResponseBean;
import javax.naming.InitialContext;
import ro.kds.erp.data.OfferLocalHome;
import javax.rmi.PortableRemoteObject;
import ro.kds.erp.data.OfferLocal;
import ro.kds.erp.data.DocumentLocal;
import javax.naming.NamingException;
import ro.kds.erp.data.DataLayerException;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import ro.kds.erp.data.ProductLocal;
import ro.kds.erp.data.CompositeProductLocal;
import javax.naming.Context;
import java.util.Collection;
import java.util.Iterator;
import java.util.Date;
import ro.kds.erp.biz.Preferences;
import ro.kds.erp.data.OfferItemLocalHome;
import ro.kds.erp.data.ProductLocalHome;
import javax.ejb.SessionContext;
import java.util.HashMap;
import ro.kds.erp.data.CategoryLocal;
import java.util.ArrayList;
import java.util.Map;

/**
 * Business logic pentru form-ul de vizualizare/modificare oferte
 * pentru usi standard.
 *
 *
 * Created: Thu Nov 03 07:19:01 2005
 *
 * @author <a href="mailto:Mihai Giurgeanu@CRIMIRA"></a>
 * @version 1.0
 */
public class OfertaUsiStandardBean 
    extends ro.kds.erp.biz.setum.basic.OfertaUsiStandardBean {



    /**
     * The category for offers managed by this bean.
     */
    public static final Integer OFFERS_CATEGORY =
	new Integer(1001);

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

	form.setNo("");
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
	form.setPrice(new BigDecimal(0));
	form.setRelativeGain(new Double(0));
	form.setAbsoluteGain(new BigDecimal(0));

	// read only fields on subform
	form.setUsa("");
	form.setBroasca("");
	form.setCilindru("");
	form.setSild("");
	form.setYalla("");
	form.setVizor("");
	form.setReferencePrice(new BigDecimal(0));
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
		offer.setCategory(OFFERS_CATEGORY);
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


    /**
     * Builds the list of all offers in this category.
     */
    public ResponseBean loadListing() {
	try {
	    InitialContext ic = new InitialContext();
	    Context env = (Context)ic.lookup("java:comp/env");
	    OfferLocalHome oh = (OfferLocalHome)
		PortableRemoteObject.narrow
		(env.lookup("ejb/OfferHome"), OfferLocalHome.class);

	    Collection offers = oh.findByCategory(OFFERS_CATEGORY);
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

    /**
     * Builds the list of line items for the selected offer. The instance
     * variable <code>id</code> must contain the value of the primary key
     * of the currently selected offer.
     */
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

		CompositeProductLocal cp = p.getCompositeProduct();
		Collection parts = cp.getComponents();
		for(Iterator j=parts.iterator(); j.hasNext();) {
		    ProductLocal part = (ProductLocal)j.next();
		    Integer partCatId = part.getCategory().getId();
		    if(partCatId.equals(USA_SIMPLA_ID)) {
			r.addField("col-usa", part.getName());
		    } else if (partCatId.equals(BROASCA_ID)) {
			r.addField("col-broasca", part.getName());
		    } else if (partCatId.equals(CILINDRU_ID)) {
			r.addField("col-cilindru", part.getName());
		    } else if (partCatId.equals(SILD_ID)) {
			r.addField("col-sild", part.getName());
		    } else if (partCatId.equals(YALLA_ID)) {
			r.addField("col-yalla", part.getName());
		    } else if (partCatId.equals(VIZOR_ID)) {
			r.addField("col-vizor", part.getName());
		    } else {
			logger.log(BasicLevel.WARN, "Productd " + p.getId() + 
				   " has an unknown component type " 
				   + part.getCategory().getName());
		    }
		}

		r.addField("col-price", item.getPrice());
		
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
     * The data for a given subform is loaded from the database into
     * the <code>form</code> instance variable.
     */
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
		CompositeProductLocal cp = p.getCompositeProduct();
		Collection parts = cp.getComponents();
		BigDecimal referencePrice = new BigDecimal(0);
		for(Iterator i = parts.iterator(); i.hasNext();) {
		    ProductLocal part = (ProductLocal)i.next();
		    Integer catid = part.getCategory().getId();
		    if(catid.equals(USA_SIMPLA_ID)) {
			form.setUsa(part.getName());
		    } else if(catid.equals(BROASCA_ID)) {
			form.setBroasca(part.getName());
		    } else if(catid.equals(CILINDRU_ID)) {
			form.setCilindru(part.getName());
		    } else if(catid.equals(SILD_ID)) {
			form.setSild(part.getName());
		    } else if(catid.equals(YALLA_ID)) {
			form.setYalla(part.getName());
		    } else if(catid.equals(VIZOR_ID)) {
			form.setVizor(part.getName());
		    } else {
			logger.log(BasicLevel.WARN, "Productd " + p.getId() + 
				   " has an unknown component type " 
				   + part.getCategory().getName());
		    }
		    referencePrice = referencePrice.add(part.getSellPrice());
		}
		form.setReferencePrice(referencePrice);
	    }
	    else {
		logger.log(BasicLevel.WARN, "No product associated with this offer " + id);
		form.setUsa("");
		form.setBroasca("");
		form.setCilindru("");
		form.setSild("");
		form.setYalla("");
		form.setVizor("");
		form.setReferencePrice(new BigDecimal(0));
	    }

	    form.setPrice(offerItem.getPrice());
	    computeCalculatedFields(null);
	    
	    r = new ResponseBean();
	    r.addRecord();
	    copyFieldsToResponse(r);

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
     * Adds a product to the offer. Makes a default offer item entry pointing
     * to the given product.
     *
     * @param productId is the primary key of the composite product to be
     * added to this offer.
     */
    public ResponseBean addProduct(Integer productId) throws FinderException {
	ResponseBean r;
	logger.log(BasicLevel.DEBUG, "addProduct id " + productId);
	try {
	    InitialContext ic = new InitialContext();
	    Context env = (Context) ic.lookup("java:comp/env");
	    ProductLocalHome ph = (ProductLocalHome)PortableRemoteObject.narrow
		(env.lookup("ejb/ProductHome"), ProductLocalHome.class);
	    ProductLocal p = ph.findByPrimaryKey(productId);
	    CompositeProductLocal cp = p.getCompositeProduct();
	    if(cp == null) {
		throw new FinderException("There is no CompositeProduct associated with this product, for productId " + productId);
	    }
	    
	    BigDecimal price = new BigDecimal(0);
	    Collection parts = cp.getComponents();
	    for(Iterator i = parts.iterator(); i.hasNext();) {
		ProductLocal part = (ProductLocal)i.next();
		price = price.add(part.getSellPrice());
	    }

	    OfferItemLocalHome oih = 
		(OfferItemLocalHome)PortableRemoteObject.narrow
		(env.lookup("ejb/OfferItemHome"), OfferItemLocalHome.class);
	    OfferLocalHome oh = (OfferLocalHome)PortableRemoteObject.narrow
		(env.lookup("ejb/OfferHome"), OfferLocalHome.class);

	    // id is the instance var containing the id of current offer
	    OfferLocal o = oh.findByPrimaryKey(id);
	    
	    // OfferItem is the instance var containg the current item
	    offerItem = oih.create();
	    offerItem.setProduct(p);
	    offerItem.setPrice(price);
	    
	    o.getItems().add(offerItem);
	    r = new ResponseBean();
	} catch (NamingException e) {
	    
	    r = new ResponseBean();
	    r.setCode(1);
	    r.setMessage("Eroare de configurare a aplicatiei. Produsul nu poate fi adaugat la oferta");

	    logger.log(BasicLevel.ERROR, "Error while adding product id " + 
		       productId + 
		       " to the current offer", e);
	} catch (CreateException e) {
	    r = new ResponseBean();
	    r.setCode(3);
	    r.setMessage("Eroare in baza de date. Produsul nu poate fi adaugat la oferta");

	    logger.log(BasicLevel.ERROR, "Error while adding product id " + 
		       productId + 
		       " to the current offer", e);
	}

	return r;
    }

    /**
     * Saves the data for the current line item into the persistent layer.
     */
    public ResponseBean saveSubForm() {
	ResponseBean r;
	
	offerItem.setPrice(form.getPrice());
	
	r = new ResponseBean();

	return r;
    }

    /**
     * Removes the current line item
     */
    public ResponseBean removeItem() {
	ResponseBean r;
	try {
	    offerItem.remove();
	    r = new ResponseBean();
	} catch (Exception e) {
	    logger.log(BasicLevel.ERROR, "The current item can not be removed",
		       e);
	    r = new ResponseBean();
	    r.setCode(3);
	    r.setMessage("Nu se poate sterge linia din oferta!");
	}
	return r;
    }

    /**
     * CategoryId
     */
    private Integer USA_STD_ID;
    /**
     * CategoryId
     */
    private Integer USA_SIMPLA_ID;
    /**
     * CategoryId
     */
    private Integer BROASCA_ID;
    /**
     * CategoryId
     */
    private Integer CILINDRU_ID;
    /**
     * CategoryId
     */
    private Integer SILD_ID;
    /**
     * CategoryId
     */
    private Integer YALLA_ID;
    /**
     * CategoryId
     */
    private Integer VIZOR_ID;

    /**
     * Reads the id's of product categories to be assembled into this composite
     * product.
     */
    private void initCategoryIds() {
	try {
	    Context it = new InitialContext();
	
	    USA_STD_ID = (Integer)it
		.lookup("java:comp/env/setum/category/usaStdId");
	    USA_SIMPLA_ID = (Integer)it
		.lookup("java:comp/env/setum/category/usaSimplaId");
	    BROASCA_ID = (Integer)it
		.lookup("java:comp/env/setum/category/broascaId");
	    CILINDRU_ID = (Integer)it
		.lookup("java:comp/env/setum/category/cilindruId");
	    SILD_ID = (Integer)it
		.lookup("java:comp/env/setum/category/sildId");
	    YALLA_ID = (Integer)it
		.lookup("java:comp/env/setum/category/yallaId");
	    VIZOR_ID = (Integer)it
		.lookup("java:comp/env/setum/category/vizorId");
	} catch (NamingException e) {
	    logger.log(BasicLevel.ERROR,
		       "Category ids were not loaded", e);
	}
    }

    /**
     * Some initialization code. It calls 
     * <code>super.setSessionContext()</code>.
     *
     * @param sessionContext a <code>SessionContext</code> value
     */
    public final void setSessionContext(final SessionContext sessionContext) {
	super.setSessionContext(sessionContext);
	initCategoryIds();
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
	    reportData = new ArrayList();
	    for(Iterator i = offerItems.iterator(); i.hasNext();) {
		OfferItemLocal item = (OfferItemLocal)i.next();
		HashMap dataRow = new HashMap();
		ProductLocal p = item.getProduct();
		CategoryLocal cat = p.getCategory();
		dataRow.put("name", p.getName());
		dataRow.put("code", p.getCode());
		dataRow.put("sellPrice", item.getPrice());
		
		dataRow.put("broasca_name", "-");
		dataRow.put("cilindru_name", "-");
		dataRow.put("sild_name", "-");
		dataRow.put("yalla_name", "-");
		dataRow.put("vizor_name", "-");

		for(Iterator j = p.getCompositeProduct().getComponents()
			.iterator(); j.hasNext(); ) {
		    ProductLocal part = (ProductLocal)j.next();

		    if(part.getCategory().getId().intValue() == 
		       USA_SIMPLA_ID.intValue()) {
			
			dataRow.put("usa_id", part.getId());
			dataRow.put("usa_name", part.getName());
			dataRow.put("usa_description", part.getDescription());
			dataRow.put("usa_code", part.getCode());
		    }
		    if(part.getCategory().getId().intValue() == 
		       BROASCA_ID.intValue()) {
			dataRow.put("broasca_name", part.getName());
		
		    }
		    if(part.getCategory().getId().intValue() == 
		       CILINDRU_ID.intValue()) {
			dataRow.put("cilindru_name", part.getName());
			
		    }
		    if(part.getCategory().getId().intValue() == 
		       SILD_ID.intValue()) {
			dataRow.put("sild_name", part.getName());
			
		    }
		    if(part.getCategory().getId().intValue() == 
		       YALLA_ID.intValue()) {
			dataRow.put("yalla_name", part.getName());
			
		    }
		    if(part.getCategory().getId().intValue() == 
		       VIZOR_ID.intValue()) {
			
			dataRow.put("vizor_name", part.getName());
		    }
		    
		}

		reportData.add(dataRow);
	    }
	} catch (Exception e) {
	    logger.log(BasicLevel.ERROR,
		       "Can not build the report data. Exception caught.", e);
	    reportData = new ArrayList();
	}
	return reportData;
    }

    /**
     * Creates a map of pairs field name -- field value to be used as
     * a parameters map for a report displaying the list of items.
     */
    public Map fieldsMap() {
	Map theMap = new HashMap();
	theMap.put("offerNo", form.getNo());
	theMap.put("docDate", form.getDocDate());
	theMap.put("offerDateFrom", form.getDateFrom());
	theMap.put("offerDateTo", form.getDateTo());
	theMap.put("offerPeriod", form.getPeriod());
	theMap.put("offerName", form.getName());
	theMap.put("offerDescription", form.getDescription());
	theMap.put("offerComment", form.getComment());

	return theMap;
    }

}

