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
		}
	    } else {
		r = ResponseBean.SUCCESS;
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

	    form.setPrice(offerItem.getPrice());
	    form.setBusinessCategory(offerItem.getBusinessCategory());
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
	    form.setPrice(p.getSellPrice());

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
	if(clientId != null && clientId.intValue() != 0) {
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

}
