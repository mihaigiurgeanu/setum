package ro.kds.erp.biz.setum;

import ro.kds.erp.biz.setum.basic.ArbitraryOfferBean;
import ro.kds.erp.biz.ResponseBean;
import javax.ejb.FinderException;
import ro.kds.erp.biz.ResponseBean;
import ro.kds.erp.data.OfferItemLocal;
import ro.kds.erp.biz.PreferencesBean;
import org.objectweb.util.monolog.api.BasicLevel;
import java.util.Calendar;
import java.math.BigDecimal;
import javax.naming.InitialContext;
import ro.kds.erp.data.OfferLocalHome;
import javax.rmi.PortableRemoteObject;
import ro.kds.erp.data.OfferLocal;
import ro.kds.erp.data.DocumentLocal;
import javax.naming.NamingException;
import ro.kds.erp.data.DataLayerException;
import javax.ejb.CreateException;
import ro.kds.erp.data.ProductLocal;
import ro.kds.erp.data.OfferItemLocalHome;
import ro.kds.erp.data.ProductLocalHome;
import javax.ejb.RemoveException;
import javax.naming.Context;
import java.util.Collection;
import java.util.Iterator;
import java.util.Date;
import ro.kds.erp.biz.Preferences;
import ro.kds.erp.biz.SequenceHome;
import ro.kds.erp.biz.Sequence;
import ro.kds.erp.biz.ResponseBean;
import ro.kds.erp.data.ClientLocalHome;
import ro.kds.erp.data.ClientLocal;
import ro.kds.erp.data.CategoryLocal;
import ro.kds.erp.biz.CategoryManagerLocalHome;
import ro.kds.erp.biz.CategoryManagerLocal;
import ro.kds.erp.biz.CommonServicesLocal;
import ro.kds.erp.biz.CommonServicesLocalHome;
import ro.kds.erp.biz.ServiceNotAvailable;
import java.math.RoundingMode;
import ro.kds.erp.utils.NullHandler;
import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;
import org.apache.commons.lang.StringUtils;
import ro.kds.erp.data.OrderLineLocal;
import java.text.DateFormat;
import ro.kds.erp.data.ContactLocal;
import ro.kds.erp.data.ContactLocalHome;
import java.util.TreeMap;
import java.util.Map;

/**
 * Specific implemetation of business rules for ArbitraryOfferEJB.
 *
 *
 * Created: Thu Nov 17 08:54:52 2005
 *
 * @author <a href="mailto:Mihai Giurgeanu@CRIMIRA"></a>
 * @version 1.0
 */
public class ArbitraryOfferBizBean extends ArbitraryOfferBean {

    /**
     * The category for offers managed by this bean.
     */
    public static final Integer OFFERS_CATEGORY =
	new Integer(1002);



    /**
     * The name of the Offer entity.
     */
    public static final String ENTITY_OFFER = "Offer";
    /**
     * The name of the offer line entity.
     */
    public static final String ENTITY_OFFER_ITEM = "OfferItem";



    /**
     * The selected offer line in the subform.
     */
    OfferItemLocal offerItem;

    /**
     * Initialization of the form values.
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

	form.setAnexa("1");

	form.setTerms(
		      "Termen de livrare:___zile lucratoare\n" +
		      "Garantie: 12 luni de la data livarii produsului.\n" +
		      "Termeni contractuali: avans 50% la data semnarii contractului, diferenta de 50% la data livrarii produsului, in lei la cursul BNR din ziua facturarii."
		      );

	// subform fields
	form.setProductId(new Integer(0));
	form.setPrice(new BigDecimal(0));
	form.setRelativeGain(new Double(0));
	form.setAbsoluteGain(new BigDecimal(0));
	form.setQuantity(new BigDecimal(1));

	// read only fields on subform
	form.setProductCategory("");
	form.setProductCode("");
	form.setProductName("");
	form.setEntryPrice(new BigDecimal(0));
	form.setSellPrice(new BigDecimal(0));

	form.setBusinessCategory("");
    }

    /**
     * Load the form's fields values from the persistent storage.
     * It only loads the offer's fields not the fields of the offer items.
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

	    if(offer.getClient() != null) {
		form.setClientId(offer.getClient().getId());
		form.setClientName(offer.getClient().getName());
	    }

	    if(offer.getClientContact() != null) {
		ContactLocal contact = offer.getClientContact();
		form.setClientContactId(contact.getId());
		form.setClientContactName(contact.getTitle() + " " 
					  + contact.getFirstName() + " " 
					  + contact.getLastName());
		form.setClientContactPhone(contact.getPhone());
		form.setClientContactFax(contact.getFax());
		form.setClientContactEmail(contact.getEmail());
		form.setClientContactMobile(contact.getMobile());

		logger.log(BasicLevel.DEBUG, "loadFields: Contact loadded: " + 
			   form.getClientContactName() + ", id: " + 
			   form.getClientContactId());
	    } else {
		
		logger.log(BasicLevel.DEBUG, "loadFields: offer has no contacts");
	    }

	    form.setName(offer.getName());
	    form.setDescription(offer.getDescription());
	    form.setComment(offer.getComment());
	    form.setContact(offer.getContact());

	    form.setContract(offer.getContract());
	    form.setAnexa(offer.getAnexa());
	    form.setTerms(offer.getTerms());

	    form.setAttribute1(offer.getAttribute1());
	    form.setAttribute2(offer.getAttribute2());
	    form.setAttribute3(offer.getAttribute3());
	    form.setAttribute4(offer.getAttribute4());
	    form.setAttribute5(offer.getAttribute5());
	    form.setAttribute6(offer.getAttribute6());
	    form.setAttribute7(offer.getAttribute7());
	    form.setAttribute8(offer.getAttribute8());
	    form.setAttribute9(offer.getAttribute9());
	    form.setAttribute10(offer.getAttribute10());

	} catch (NamingException e) {
	    logger.log(BasicLevel.ERROR, "Can not get the jndi name for ejb/OfferHome", e);
	    r.setCode(3);
	    r.setMessage("Eroare de configurare. Nu am putut incarca datele!");
	}
	
	return r;
    }

    /**
     * Writes the data stored in the form bean into the persistent
     * storage. It only saves data related to the offer's header not
     * the data of the offer's line items.
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

		Integer offerNo;
		try {
		    SequenceHome sh = (SequenceHome)PortableRemoteObject.narrow
			(env.lookup("ejb/SequenceHome"), SequenceHome.class);
		    Sequence s = sh.create();
		    offerNo = s.getNext("ro.setumsa.sequnces.offers");
		} catch (Exception e) {
		    offerNo = new Integer(0);
		    logger.log(BasicLevel.WARN, "Can not get a number for offer", e);
		}

		form.setNo(String.valueOf(offerNo));
	    } else {
		offer = oh.findByPrimaryKey(id);
	    }
	    DocumentLocal docData = offer.getDocument();
	    docData.setNumber(form.getNo());
	    docData.setDate(form.getDocDate());
	    offer.setDateFrom(form.getDateFrom());
	    offer.setDateTo(form.getDateTo());
	    offer.setDiscontinued(form.getDiscontinued());

	    if(form.getClientId() != null && form.getClientId().intValue() != 0) {
		try {
		    ClientLocalHome ch = (ClientLocalHome)PortableRemoteObject.
			narrow(env.lookup("ejb/ClientHome"), ClientLocalHome.class);
		    ClientLocal client = ch.findByPrimaryKey(form.getClientId());
		    offer.setClient(client);
		} catch (Exception e) {
		    logger.log(BasicLevel.WARN, "Relation not set with client id " + form.getClientId());
		    logger.log(BasicLevel.DEBUG, e);
		}
	    } else {
		offer.setClient(null);
	    }
	    if(form.getClientContactId() != null && form.getClientContactId().intValue() != 0) {
		try {
		    ContactLocalHome contacth = (ContactLocalHome)PortableRemoteObject.
			narrow(env.lookup("ejb/ContactHome"), ContactLocalHome.class);
		    ContactLocal contact = contacth.findByPrimaryKey(form.getClientContactId());
		    offer.setClientContact(contact);
		    logger.log(BasicLevel.DEBUG, "Contact saved to database: " + form.getClientContactId());
		} catch (Exception e) {
		    logger.log(BasicLevel.ERROR, "Relation could not be set to contact id " 
			       + form.getClientContactId()
			       +": " + e.getMessage());
		    logger.log(BasicLevel.DEBUG, e);
		}
	    } else {
		offer.setClientContact(null);
		logger.log(BasicLevel.DEBUG, "Offer has no contact");
	    }

	    offer.setName(form.getName());
	    offer.setDescription(form.getDescription());
	    offer.setComment(form.getComment());
	    offer.setContact(form.getContact());

	    offer.setContract(form.getContract());
	    offer.setAnexa(form.getAnexa());
	    offer.setTerms(form.getTerms());

	    offer.setAttribute1(form.getAttribute1());
	    offer.setAttribute2(form.getAttribute2());
	    offer.setAttribute3(form.getAttribute3());
	    offer.setAttribute4(form.getAttribute4());
	    offer.setAttribute5(form.getAttribute5());
	    offer.setAttribute6(form.getAttribute6());
	    offer.setAttribute7(form.getAttribute7());
	    offer.setAttribute8(form.getAttribute8());
	    offer.setAttribute9(form.getAttribute9());
	    offer.setAttribute10(form.getAttribute10());

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
	    r.setMessage("Eroare in baza de date la crearea unei noi inregistrari. Datele nu au fost salvate!");
	} catch (FinderException e) {
	    logger.log(BasicLevel.ERROR, "No such object for offer id = " + id, e);
	    r.setCode(3);
	    r.setMessage("Aceasta oferta nu a fost gasita in baza de date. Datele nu au putut fi salvate!");
	}
	return r;
    }

    /**
     * Builds the list of offers from the database. Only the offer with
     * <code>OFFERS_CATEGORY</code> will be returned.
     *
     * @return a <code>ResponseBean</code> with multiple records.
     */
    public ResponseBean loadListing() {
	try {
	    InitialContext ic = new InitialContext();
	    Context env = (Context)ic.lookup("java:comp/env");
	    OfferLocalHome oh = (OfferLocalHome)
		PortableRemoteObject.narrow
		(env.lookup("ejb/OfferHome"), OfferLocalHome.class);

	    ArrayList offers = new ArrayList(oh.findByCategory(OFFERS_CATEGORY));
	    Collections.sort(offers, new Comparator() {
		    public int compare(Object o1, Object o2) {
			OfferLocal offer1 = (OfferLocal)o1;
			OfferLocal offer2 = (OfferLocal)o2;
			String n1 = StringUtils.leftPad(offer1.getDocument().getNumber(), 6, '0');
			String n2 = StringUtils.leftPad(offer2.getDocument().getNumber(), 6, '0');

			return -(n1.compareTo(n2));
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
		
		ClientLocal client = offer.getClient();
		if (client != null) {
		String clientName;
		if(client.getIsCompany()) {
		    clientName = client.getCompanyName();
		} else {
		    clientName = client.getLastName() + ", " + client.getFirstName();
		}
		r.addField("offersListing.client", clientName);
		} else {
		    r.addField("offersListing.client", "-");
		}
		
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
     * Cache the offers list.
     */
    ArrayList listingCache;

    /**
     * The count of lines each request to loadPartialListing will return;
     */
    static final int LISTING_ROWS_PER_REQUEST = 30;
    
    /**
     * Initialization of the listing cache.
     *
     * @return the number of offers in the listing.
     */
    public ResponseBean getOffersCount() {
	logger.log(BasicLevel.DEBUG, ">");

	ResponseBean r;

	try {
	    InitialContext ic = new InitialContext();
	    Context env = (Context)ic.lookup("java:comp/env");
	    OfferLocalHome oh = (OfferLocalHome)
		PortableRemoteObject.narrow
		(env.lookup("ejb/OfferHome"), OfferLocalHome.class);

	    listingCache = new ArrayList(oh.findByCategory(OFFERS_CATEGORY));
	    Collections.sort(listingCache, new Comparator() {
		    public int compare(Object o1, Object o2) {
			OfferLocal offer1 = (OfferLocal)o1;
			OfferLocal offer2 = (OfferLocal)o2;
			String n1 = StringUtils.leftPad(offer1.getDocument().getNumber(), 6, '0');
			String n2 = StringUtils.leftPad(offer2.getDocument().getNumber(), 6, '0');

			return -(n1.compareTo(n2));
		    }
		});

	  
	    r = new ResponseBean();
	    r.addRecord();
	    r.addField("records-count", listingCache.size());
	} catch (NamingException e) {
	    logger.log(BasicLevel.ERROR, "Naming exception when trying to read the offers listing from database: "
		       + e);
	    logger.log(BasicLevel.DEBUG, e);
	    r = ResponseBean.getErrConfigNaming(e.getMessage());
	} catch (FinderException e) {
	    logger.log(BasicLevel.ERROR, "Could not get the list of offers from database: " + e);
	    logger.log(BasicLevel.DEBUG, e);
	    r = ResponseBean.getErrNotFound(ENTITY_OFFER);
	} catch (Exception e) {
	    logger.log(BasicLevel.ERROR, "Unexpected exception occured: " + e);
	    logger.log(BasicLevel.DEBUG, e);
	    r = ResponseBean.getErrUnexpected(e);
	}
	logger.log(BasicLevel.DEBUG, "<");
	return r;
    }

    /**
     * Loads a part of the offer listing, starting with <code>startRow</code>.
     *
     * @param startRow is the first row from the <code>listingCache</code> that will
     * be returned in the listing.
     */
    public ResponseBean loadPartialListing(Integer startRow) {
	ResponseBean r;

	r = new ResponseBean();
	if(listingCache == null) {
	    r = getOffersCount();
	    if(listingCache == null) {
		logger.log(BasicLevel.ERROR, "Could not load the offers in memory");
		return r;
	    }
	}

	int endRow = startRow.intValue() + LISTING_ROWS_PER_REQUEST;
	for(int i = startRow.intValue(); 
	    (i < endRow) && (i < listingCache.size()); i++) {
	    
	    OfferLocal offer = (OfferLocal) listingCache.get(i);
	    r.addRecord();
	    r.addField("id", offer.getId());
	    r.addField("offersListing.no", offer.getDocument().getNumber());
	    r.addField("offersListing.name", offer.getName());
	    r.addField("offersListing.docDate", offer.getDocument().getDate());
	    r.addField("offersListing.dateFrom", offer.getDateFrom());
	    r.addField("offersListing.dateTo", offer.getDateTo());
	    
	    ClientLocal client = offer.getClient();
	    if (client != null) {
		String clientName;
		if(client.getIsCompany()) {
		    clientName = client.getCompanyName();
		} else {
		    clientName = client.getLastName() + ", " + client.getFirstName();
		}
		r.addField("offersListing.client", clientName);
	    } else {
		r.addField("offersListing.client", "-");
	    }
	    
	}
	return r;
    }

    /**
     * Makes the list of the lineItems of the current selected offer.
     */
    public ResponseBean lineItemsListing() {
	ResponseBean r;

	try {
	    if(id != null) {
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
			r.addField("productId", p.getId());
		    }
		    
		    r.addField("offerLines.price", item.getPrice());
		    r.addField("businessCategory", item.getBusinessCategory());
		    r.addField("offerLines.quantity", item.getQuantity());
		}
	    } else {
		r = new ResponseBean();
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
     * Makes the list of the lineItems ordered by a specific client.
     */
    public ResponseBean loadClientItems(Integer clientId) {
	ResponseBean r;

	logger.log(BasicLevel.DEBUG, "Loading the list of offerItems for client: " + clientId);
	try {
	    InitialContext ic = new InitialContext();
	    Context env = (Context)ic.lookup("java:comp/env");
		
	    OfferLocalHome oh = (OfferLocalHome)PortableRemoteObject.
		narrow(env.lookup("ejb/OfferHome"), OfferLocalHome.class);
		
	    Collection offers = oh.findByClientId(clientId);
	    r = new ResponseBean();
	    for(Iterator j = offers.iterator(); j.hasNext();) {
		OfferLocal offer = (OfferLocal)j.next();
		
		logger.log(BasicLevel.DEBUG, "Loading items from offer: " + offer.getName());

		Collection offerItems = offer.getItems();
		for(Iterator i = offerItems.iterator(); i.hasNext();) {
		    OfferItemLocal item = (OfferItemLocal)i.next();
		    r.addRecord();
		    r.addField("offerLines.id", item.getId());
		    r.addField("offerLines.offer", offer.getDocument().getNumber());
		    r.addField("offerLines.date", offer.getDocument().getDate());
		    ProductLocal p = item.getProduct();
		    if(p != null) {
			r.addField("offerLines.category", p.getCategory().getName());
			r.addField("offerLines.name", p.getName());
			r.addField("productId", p.getId());
		    }
		    
		    r.addField("offerLines.price", item.getPrice());
		    r.addField("businessCategory", item.getBusinessCategory());
		}
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
     * Load data for the form fields bean corresponding to the line
     * item fields.
     */
    public ResponseBean loadSubForm(Integer loadId) {
	ResponseBean r;

	try {


	    InitialContext ic = new InitialContext();
	    Context env = (Context)ic.lookup("java:comp/env");
	    
	    OfferItemLocalHome oh = (OfferItemLocalHome)PortableRemoteObject.
		narrow(env.lookup("ejb/OfferItemHome"), 
		       OfferItemLocalHome.class);
	    this.offerItem = oh.findByPrimaryKey(loadId);
	    ProductLocal p = offerItem.getProduct();
	    
	    if(p != null) {
		form.setProductId(p.getId());
		form.setProductCategory(p.getCategory().getName());
		form.setProductCode(p.getCode());
		form.setProductName(p.getName());

		form.setSellPrice(p.getSellPrice());
		form.setEntryPrice(p.getEntryPrice());
	    }
	    else {
		form.setProductId(new Integer(0));
		form.setProductCategory("");
		form.setProductCode("");
		form.setProductName("");
		form.setSellPrice(new BigDecimal(0));
		form.setEntryPrice(new BigDecimal(0));
	    }

	    NullHandler zi = new NullHandler(new Integer(0));
	    NullHandler zbd = new NullHandler(new BigDecimal(0));
	    NullHandler zs = new NullHandler("");
	    NullHandler zd = new NullHandler(new Double(0));
	    NullHandler zb = new NullHandler(new Boolean(false));

	    form.setPrice(zbd.val(offerItem.getPrice()));
	    form.setBusinessCategory(zs.val(offerItem.getBusinessCategory()));
	    form.setMontajId(zi.val(offerItem.getMontajId()));
	    form.setMontajProcent(zd.val(offerItem.getMontajProcent()));
	    form.setMontajSeparat(zb.val(offerItem.getMontajSeparat()));
	    form.setLocationId(zi.val(offerItem.getLocationId()));
	    form.setOtherLocation(zs.val(offerItem.getOtherLocation()));
	    form.setDistance(zbd.val(offerItem.getDistance()));
	    form.setDeliveries(zi.val(offerItem.getDeliveries()));
	    form.setQuantity(zbd.val(offerItem.getQuantity()));

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
     * Update the fields related with the product when the product id
     * is modifief.
     */
    public ResponseBean updateProductId(Integer productId) {
	ResponseBean r;
	try {
	    InitialContext ic = new InitialContext();
	    Context env = (Context)ic.lookup("java:comp/env");
	    ProductLocalHome ph = (ProductLocalHome)PortableRemoteObject.narrow
		(env.lookup("ejb/ProductHome"), ProductLocalHome.class);
	    ProductLocal p = ph.findByPrimaryKey(productId);
	    
	    form.setProductCategory(p.getCategory().getName());
	    form.setProductCode(p.getCode());
	    form.setProductName(p.getName());
	    form.setSellPrice(p.getSellPrice());
	    form.setEntryPrice(p.getEntryPrice());
	    //form.setPrice(p.getSellPrice().add(form.getAbsoluteGain()));

	    r = super.updateProductId(productId);
	    if(r.getCode() == 0) {
		logger.log(BasicLevel.DEBUG, "Add to the response bean " +
			   "returned by std. updateProductId the values " +
			   "for the new product.");
		r.addField("productCategory", form.getProductCategory());
		r.addField("productCode", form.getProductCode());
		r.addField("productName", form.getProductName());
		r.addField("entryPrice", form.getEntryPrice());
		r.addField("sellPrice", form.getSellPrice());
		r.addField("price", form.getPrice());
	    }
	} catch (FinderException e) {
	    logger.log(BasicLevel.ERROR, "Product not found for id " + 
		       productId, e);
	    r = new ResponseBean();
	    r.setCode(3);
	    r.setMessage("Produsul ales nu exista in baza de date");
	} catch (Exception e) {
	    logger.log(BasicLevel.ERROR, "Could not retrieve the product's " +
		       "details for productId" + productId, e);
	    r = new ResponseBean();
	    r.setCode(1);
	    r.setMessage("Eroare aplicatie! " +
			 "Nu se poate adauga produsul selectat.");
	}
	
	return r;
    }

    public ResponseBean updateClientId(Integer clientId) {
	ResponseBean r;
	if(clientId != null && clientId.intValue() != 0 && clientId.intValue() != form.getClientId().intValue()) {
	    try {
		InitialContext ic = new InitialContext();
		Context env = (Context)ic.lookup("java:comp/env");
		ClientLocalHome ch = (ClientLocalHome)PortableRemoteObject.
		    narrow(env.lookup("ejb/ClientHome"), ClientLocalHome.class);
		ClientLocal client = ch.findByPrimaryKey(clientId);
		form.setClientId(clientId);
		form.setClientName(client.getName());
		r = new ResponseBean();
		r.addRecord();
		r.addField("clientName", client.getName());

		form.setClientContactId(null);
		form.setClientContactName("");
		form.setClientContactPhone("");
		form.setClientContactFax("");
		form.setClientContactMobile("");
		form.setClientContactEmail("");
		
		addClientContactVL(r);
	    } catch (Exception e) {
		logger.log(BasicLevel.ERROR, 
			   "Can not get the client info for client id: " + clientId);
		logger.log(BasicLevel.DEBUG, e);
		r = ResponseBean.ERR_UNEXPECTED;
	    }
	} else {
	    form.setClientId(new Integer(0));
	    form.setClientName("");
	    r = new ResponseBean();
	    r.addRecord();
	    r.addField("clientName", "");
	}
	return r;
    }

    /**
     * Adding a new item on an offer.
     */
    public ResponseBean addItem(Integer id, String businessCategory) {
	ResponseBean r;

	try {
	    InitialContext ic = new InitialContext();
	    Context env = (Context) ic.lookup("java:comp/env");
	    
	    ProductLocalHome home = 
		(ProductLocalHome)PortableRemoteObject.narrow
		(env.lookup("ejb/ProductHome"), ProductLocalHome.class);
	    ProductLocal p = home.findByPrimaryKey(id);

	    OfferItemLocalHome oih = 
		(OfferItemLocalHome)PortableRemoteObject.narrow
		(env.lookup("ejb/OfferItemHome"), OfferItemLocalHome.class);

	    OfferItemLocal oi;
	    oi = oih.create();
	    oi.setProduct(p);
	    oi.setPrice(p.getSellPrice()); // change it with the reference price
	    oi.setQuantity(new BigDecimal(1));
	    form.setMontajSeparat(new Boolean(true));

	    oi.setBusinessCategory(businessCategory);

	    getCurrentOffer().getItems().add(oi);

	    r = new ResponseBean();
	} catch (Exception e) {
	    r = new ResponseBean();
	    r.setCode(1);
	    r.setMessage("Nu se poate adauga produsul la oferta din cauza unei erori interne.");
	    logger.log(BasicLevel.ERROR, "The product with id " + id +
		       " can not be loaded:", e);
	} 

	return r;
    }
    

    /**
     * Remove an offer item
     */
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


    /**
     * Save data associated with the current offer line item.
     */
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
		offerItem.setBusinessCategory(form.getBusinessCategory());
		offerItem.setMontajId(form.getMontajId());
		offerItem.setMontajProcent(form.getMontajProcent());
		offerItem.setMontajSeparat(form.getMontajSeparat());
		offerItem.setLocationId(form.getLocationId());
		offerItem.setOtherLocation(form.getOtherLocation());
		offerItem.setDistance(form.getDistance());
		offerItem.setDeliveries(form.getDeliveries());
		offerItem.setQuantity(form.getQuantity());

		updateOrderPriceOfAttachedOrderLines();

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
     * Actualizeaza pretul pentru toate liniile de comanda care sunt conectate
     * la aceasta linie de oferta.
     */
    private void updateOrderPriceOfAttachedOrderLines() {
	if(offerItem != null) {
	    Collection orderLines = offerItem.getOrderLines();
	    for(Iterator i = orderLines.iterator(); i.hasNext(); ) {
		OrderLineLocal oll = (OrderLineLocal) i.next();
		oll.setPrice(offerItem.getPrice());
	    }
	}
    }



    /**
     * Builds a <code>ResponseBean</code> containing the data for the offer report. It will contain a 
     * single record having describing the offer (roughly the fields of <code>OfferEJB</code>). The record
     * will have the field <code>lines</code> that will be a <code>ResponseBean</code> with a record for
     * each item. Each of the records of the <code>offer_items</code> fields will contain a <code>product</code>
     * of type <code>ResponseBean</code> describing the product. Products may contain as well other 
     * <code>ResponseBean</code> objects and so on.
     *
     * An offer should be selected before calling this method.
     *
     * @return a rather complex <code>ResponseBean</code> describing the offer, the items of the offer and the products.
     * If an error occurs, the returned <code>ResponseBean</code> will contain the error code and description.
     */
    public ResponseBean offerReport() {

	ResponseBean r;

	if (id == null) {
	    logger.log(BasicLevel.DEBUG, "offerReport: No current offer");
	    r = ResponseBean.ERR_NOTCURRENT;
	}
	else {
	    r = new ResponseBean();
	    r.addRecord();
	    
	    DateFormat dfmt = DateFormat.getDateInstance();

	    r.addField("no", form.getNo());
	    r.addField("docDate", dfmt.format(form.getDocDate()));
	    r.addField("dateFrom", dfmt.format(form.getDateFrom()));
	    r.addField("dateTo", dfmt.format(form.getDateTo()));
	    r.addField("discontinued", form.getDiscontinued());
	    r.addField("period", form.getPeriod());
	    r.addField("clientId", form.getClientId());
	    r.addField("clientName", form.getClientName());
	    r.addField("name", form.getName());
	    r.addField("description", form.getDescription());
	    r.addField("comment", form.getComment());
	    r.addField("contact", form.getContact());
	    r.addField("clientContactName", form.getClientContactName());
	    r.addField("clientContactPhone", form.getClientContactPhone());
	    r.addField("clientContactFax", form.getClientContactFax());
	    r.addField("clientContactEmail", form.getClientContactEmail());
	    r.addField("agrementTehnic", Utils.getAgrementTehnic(factory)); 
	    r.addField("contract", form.getContract());
	    r.addField("anexa", form.getAnexa());
	    r.addField("terms", form.getTerms());
	    r.addField("attribute1", form.getAttribute1());
	    r.addField("attribute2", form.getAttribute2());
	    r.addField("attribute3", form.getAttribute3());
	    r.addField("attribute4", form.getAttribute4());
	    r.addField("attribute5", form.getAttribute5());
	    r.addField("attribute6", form.getAttribute6());
	    r.addField("attribute7", form.getAttribute7());
	    r.addField("attribute8", form.getAttribute8());
	    r.addField("attribute9", form.getAttribute9());
	    r.addField("attribute10", form.getAttribute10());
	    

	    ClientUtils clientUtils;
	    OfferLocal offer = getCurrentOffer();
	    if (offer == null) {
		logger.log(BasicLevel.WARN, "getCurrentOffer returned null in offerReport call! Client data will not be available.");
		clientUtils = new ClientUtils(null);
	    } else {
		ClientLocal client = offer.getClient();
		clientUtils = new ClientUtils(client);
	    }
	    clientUtils.populateResponse(r);

	    r.addField("lines", linesReport());
	    r.addField("contacts", clientUtils.getContacts());
	}

	return r;
    }


    /**
     * Builds a <code>ResponseBean</code> that has all the
     * line items of the current offer and for each line item
     * the description of the product as <code>ResponseBean</code>
     * field.
     */
    private ResponseBean linesReport() {

	ResponseBean r;
	OfferLocal offer = getCurrentOffer();

	if(offer != null) {
		
	    Collection offerItems = offer.getItems();
	    r = new ResponseBean();
	    for(Iterator i = offerItems.iterator(); i.hasNext();) {
		OfferItemLocal item = (OfferItemLocal)i.next();

		loadSubForm(item.getId());
		r.addRecord();
		r.addField("productId", form.getProductId());
		r.addField("price", form.getPrice().setScale(2, RoundingMode.HALF_UP));
		r.addField("priceCuMontaj", 
			   form.getPrice()
			   .add((form.getValMontaj().add(form.getValTransport()))
				.divide(form.getQuantity()))
			   .setScale(2, RoundingMode.HALF_UP));
		r.addField("vatPrice", form.getVatPrice().setScale(2, RoundingMode.HALF_UP));
		r.addField("relativeGain", form.getRelativeGain());
		r.addField("absoluteGain", form.getAbsoluteGain());
		r.addField("productCategory", form.getProductCategory());
		r.addField("productCode", form.getProductCode());
		r.addField("productName", form.getProductName());
		r.addField("entryPrice", form.getEntryPrice().setScale(2, RoundingMode.HALF_UP));
		r.addField("sellPrice", form.getSellPrice().setScale(2, RoundingMode.HALF_UP));
		r.addField("businessCategory", form.getBusinessCategory());
		r.addField("montajId", form.getMontajId());
		r.addField("montajProcent", form.getMontajProcent());
		r.addField("montajSeparat", form.getMontajSeparat());
		r.addField("locationId", form.getLocationId());
		r.addField("location", form.getOtherLocation());
		r.addField("distance", form.getDistance());
		r.addField("deliveries", form.getDeliveries());
		r.addField("valMontaj", form.getValMontaj().setScale(2, RoundingMode.HALF_UP));
		r.addField("valTransport", form.getValTransport().setScale(2, RoundingMode.HALF_UP));
		r.addField("quantity", form.getQuantity());
		r.addField("lineSellValue", 
			   form.getQuantity()
			   .multiply(form.getSellPrice())
			   .setScale(2, RoundingMode.HALF_UP));
		r.addField("lineValue", 
			   form.getQuantity()
			   .multiply(form.getPrice())
			   .setScale(2, RoundingMode.HALF_UP));
		r.addField("lineValueCuMontaj", 
			   form.getQuantity()
			   .multiply(form.getPrice()) 
			   .add(form.getValMontaj())
			   .add(form.getValTransport())
			   .setScale(2, RoundingMode.HALF_UP));
		r.addField("lineEntryValue", 
			   form.getQuantity()
			   .multiply(form.getEntryPrice()));
		r.addField("lineVatValue", 
			   form.getQuantity()
			   .multiply(form.getVatPrice()));

		try {
		    CommonServicesLocal srv = (CommonServicesLocal)factory.local("ejb/CommonServices", CommonServicesLocalHome.class);

		    try {
			ProductLocal p = srv.findProductById(form.getMontajId());
			r.addField("tipMontaj", p.getName());
		    } catch (Exception e) {
			logger.log(BasicLevel.WARN, "Numele pentru tipul de montaj nu a putut fi gasit");
			logger.log(BasicLevel.DEBUG, e);
			r.addField("tipMontaj", "-");
		    }
		    try {
			ProductLocal p = srv.findProductById(form.getLocationId());
			r.addField("locationName", p.getName());
		    } catch (Exception e) {
			logger.log(BasicLevel.WARN, "Numele pentru localitatea de transport nu a putut fi gasit");
			logger.log(BasicLevel.DEBUG, e);
			r.addField("locationName", "-");
		    }

		} catch (ServiceNotAvailable e) {
		    logger.log(BasicLevel.ERROR, "Service ejb/CommonServices not available. Application setup error. Please check if the ejb/CommonServices local bean has been configured with ServiceFactory bean");
		    logger.log(BasicLevel.DEBUG, e);
		}

		r.addField("product", productReport());
	    }
	} else {
	    r = ResponseBean.ERR_NOTCURRENT;
	}

	return r;


    }


    /**
     * Build a <code>ResponseBean</code> with the fields of the product in the current offer line. 
     * The response
     * may contain other <code>ResponseBean</code> objects for other products that are
     * contained by the main product.
     */
    private ResponseBean productReport() {
	ResponseBean r;

	try {
	    InitialContext ic = new InitialContext();
	    Context env = (Context)ic.lookup("java:comp/env");
	    ProductLocalHome ph = (ProductLocalHome)PortableRemoteObject.
		narrow(env.lookup("ejb/ProductHome"), ProductLocalHome.class);

	    ProductLocal p = ph.findByPrimaryKey(form.getProductId());	    
	    CategoryLocal c = p.getCategory();
	    

	    CategoryManagerLocalHome cmh;
	    try {
		cmh = (CategoryManagerLocalHome)PortableRemoteObject.
		    narrow(env.lookup("ejb/CategoryManagerHome/" + c.getId()), 
			   CategoryManagerLocalHome.class);
	    } catch (NamingException e) {
		// it seems that no category manager is registered for the
		// given id ... try to get the default category manager
		cmh = (CategoryManagerLocalHome)PortableRemoteObject.
		    narrow(env.lookup("ejb/CategoryManagerHome/default"),
			   CategoryManagerLocalHome.class);
	    }
	    CategoryManagerLocal cm = cmh.create();

	    r = cm.getProductReport(form.getProductId());
	} catch (NamingException e) {
	    r = ResponseBean.getErrConfigNaming(e.getMessage());
	    logger.log(BasicLevel.ERROR, "Name configuration error: " + e.getMessage()
		       + " for product id " + form.getProductId());
	    logger.log(BasicLevel.DEBUG, e);
	} catch (FinderException e) {
	    r = ResponseBean.getErrNotFound(e.getMessage());
	    logger.log(BasicLevel.WARN, 
		       "FinderException (" + e.getMessage() +") while processing product with id "
		       + form.getProductId());
	    logger.log(BasicLevel.DEBUG, e);
	} catch (CreateException e) {
	    r = ResponseBean.getErrCreate(e.getMessage());
	    logger.log(BasicLevel.WARN,
		       "CreateException: CategoryManager could not be instantiated: " + e.getMessage()
		       + " for product id " + form.getProductId());
	    logger.log(BasicLevel.DEBUG, e);
	}

	return r;
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
     * Default value lists
     */
    public void loadValueLists(ResponseBean r) {
	r.addValueList("montajId", ValueLists.makeVLForCategoryId(11200));
	r.addValueList("locationId", ValueLists.makeVLForCategoryId(12005));
	addClientContactVL(r);
    }

    /**
     * Lista de contacte. Contactele sunt doar dintre contactele unui anumit client.
     */
    protected void addClientContactVL(ResponseBean r) {
	Map vl = new TreeMap();
	if(form.getClientId() != null && form.getClientId().intValue() != 0) {
	    try {
		InitialContext ic = new InitialContext();
		Context env = (Context)ic.lookup("java:comp/env");
		ClientLocalHome ch = (ClientLocalHome)PortableRemoteObject.
		    narrow(env.lookup("ejb/ClientHome"), ClientLocalHome.class);
		ClientLocal client = ch.findByPrimaryKey(form.getClientId());
		
		Collection contacts = client.getContacts();
		for(Iterator i = contacts.iterator(); i.hasNext();) {
		    ContactLocal c = (ContactLocal)i.next();
		    vl.put(c.getLastName() + ", " + c.getFirstName(), c.getId());
		}
	    } catch (Exception e) {
		logger.log(BasicLevel.WARN, "Nu am putut construi lista de contacte pentru client id "
			   + form.getClientId());
		logger.log(BasicLevel.DEBUG, e);
	    }
	}
	r.addValueList("clientContactId", vl);
    }


}
