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
import ro.kds.erp.biz.SequenceHome;
import ro.kds.erp.biz.Sequence;
import java.util.Collections;

import java.util.Comparator;
import ro.kds.erp.data.ProductsSelectionLocalHome;
import ro.kds.erp.data.ProductsSelectionLocal;
import java.util.TreeSet;

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
     * Constant VAT value
     */
    private static final BigDecimal VAT = new BigDecimal(0.19);

    /**
     * Cache the line items listing. The cache is initialized
     * by the call to <code>lineItemsCount</code> method.
     */
    private ArrayList lineItemsListingCache;

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
     * The products selection that will be printed on the offer report.
     */
    private ProductsSelectionLocal currentSelection;

    /**
     * Initialize the form fields.
     *
     */
    public final void createNewFormBean() {
	super.createNewFormBean(); // create the FormBean
	offerItem = null;

	currentSelection = null;

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


	form.setVat(VAT); // constant for this form


	// subform fields
	form.setPrice(new BigDecimal(0));
	form.setVatPrice(new BigDecimal(0));
	form.setRelativeGain(new Double(0));
	form.setAbsoluteGain(new BigDecimal(0));

	// read only fields on subform
	form.setUsaId(new Integer(0));
	form.setUsaCode("");
	form.setUsa("");
	form.setUsaDescription("");
	form.setBroasca("");
	form.setCilindru("");
	form.setSild("");
	form.setYalla("");
	form.setVizor("");
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

	    r = validate();
	} catch (NamingException e) {
	    logger.log(BasicLevel.ERROR, "Naming exception was thrown. Is ejb/OfferHome defined?", e);
	    r = ResponseBean.ERR_CONFIG_NAMING;
	} catch (DataLayerException e) {
	    logger.log(BasicLevel.ERROR, "DataLayerException caught. Is the name ejb/DocumentHome defined in the database?", e);
	    r = ResponseBean.ERR_UNEXPECTED;
	} catch (CreateException e) {
	    logger.log(BasicLevel.ERROR, "Can not create the new offer object", e);
	    r = ResponseBean.ERR_CREATE;
	} catch (FinderException e) {
	    logger.log(BasicLevel.ERROR, "No such object for offer id = " + id, e);
	    r = ResponseBean.ERR_NOTFOUND;
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
     * Builds the list of line items for the selected offer. Returns only
     * 30 rows starting with start row from the cached listing in the
     * <code>lineItemsListingCache</code> instance variable. The cache
     * is initialized by a call to <code>lineItemsCount</code> method.
     */
    public ResponseBean lineItemsListing(Integer startRow) {
	ResponseBean r;

	r = new ResponseBean();
	for(int i = startRow.intValue(); 
	    i < lineItemsListingCache.size() && i < startRow.intValue() + 30; i++) {
	    OfferItemLocal item = (OfferItemLocal)lineItemsListingCache.get(i);

	    r.addRecord();
	    r.addField("id", item.getId());
	    ProductLocal p = item.getProduct();
	    if(p == null) {
		logger.log(BasicLevel.WARN, "There is no product associated with current item "
			   + item.getId() + ". Probably it was deleted from master table!");


		r.addField("col-usa", "XXX");
		r.addField("col-broasca", "XXX");
		r.addField("col-cilindru", "XXX");
		r.addField("col-sild", "XXX");
		r.addField("col-yalla", "XXX");
		r.addField("col-vizor", "XXX");

	    } else {
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
	    }
	    r.addField("col-price", item.getPrice());
		
	}

	return r;
    }


    /**
     * Returns the number of lineitems on this offer. This would tipically be used
     * by the user interface to optimally display the listing, without loading it
     * all.
     * 
     * This method should be called before loadLineItemsListing method.
     *
     * @returns a <code>ResponseBean</code> containing a single record
     * that has the field <code>records-count</code> set to the number
     * of line items.
     */
    public ResponseBean lineItemsCount() {
	ResponseBean r;

	try {
	    lineItemsListingCache = getFilteredItems();
	    r = new ResponseBean();
	    r.addRecord();
	    r.addField("records-count", lineItemsListingCache.size());


	} catch (NamingException e) {
	    logger.log(BasicLevel.ERROR, e.getMessage(), e);
	    r = ResponseBean.ERR_CONFIG_NAMING;
	} catch (FinderException e) {
	    logger.log(BasicLevel.ERROR, e.getMessage(), e);
	    r = ResponseBean.ERR_CONFIG_NOTFOUND;
	} catch (Exception e) {
	    logger.log(BasicLevel.ERROR, e.getMessage(), e);
	    r = ResponseBean.ERR_UNEXPECTED;
	}
	return r;

    }


    /**
     * Retruns the list of items for this offer filtered by
     * the values of the current filter.
     */
    private ArrayList getFilteredItems() throws NamingException, FinderException {
	ArrayList items = new ArrayList();

	if(id != null) { // the current offer is not a new one

	    InitialContext ic = new InitialContext();
	    Context env = (Context)ic.lookup("java:comp/env");
	    
	    OfferLocalHome oh = (OfferLocalHome)PortableRemoteObject.
		narrow(env.lookup("ejb/OfferHome"), OfferLocalHome.class);
	
	    OfferLocal offer = oh.findByPrimaryKey(id);

	    logger.log(BasicLevel.DEBUG, "Count of line items: " +
		       offer.getItems().size());

	    for(Iterator i = offer.getItems().iterator();
		i.hasNext(); ) {
	    
		OfferItemLocal oi = (OfferItemLocal)i.next();
		ProductLocal p = oi.getProduct();

		if(p!=null) {
		    CompositeProductLocal cp = p.getCompositeProduct();
		    Collection parts = cp.getComponents();
		    String usa = "";
		    String broasca = "";
		    String cilindru = "";
		    String sild = "";
		    String yalla = "";
		    String vizor = "";
		    for(Iterator j = parts.iterator(); j.hasNext(); ) {
			ProductLocal part = (ProductLocal)j.next();
			Integer catid = part.getCategory().getId();
			if(catid.equals(USA_SIMPLA_ID)) {
			    usa = part.getName();
			} else if(catid.equals(BROASCA_ID)) {
			    broasca = part.getName();
			} else if(catid.equals(CILINDRU_ID)) {
			    cilindru = part.getName();
			} else if(catid.equals(SILD_ID)) {
			    sild = part.getName();
			} else if(catid.equals(YALLA_ID)) {
			    yalla = part.getName();
			} else if(catid.equals(VIZOR_ID)) {
			    vizor = part.getName();
			} else {
			    logger.log(BasicLevel.WARN, "Productd " + p.getId() + 
				       " has an unknown component type " 
				       + part.getCategory().getName());
			}

		    }
		    if(stringFilter(usa, form.getFilterUsa()) &&
		       stringFilter(broasca, form.getFilterBroasca()) &&
		       stringFilter(sild, form.getFilterSild()) &&
		       stringFilter(cilindru, form.getFilterCilindru()) &&
		       stringFilter(yalla, form.getFilterYalla()) &&
		       stringFilter(vizor, form.getFilterVizor())) {

			items.add(oi);
		    } else {
			logger.log(BasicLevel.DEBUG, "Product " + p.getId() + " was filtered out.");
		    }
		}
	    }
	}
	return items;
    }

    /**
     * Helper method that matches a value against a filter. The filter
     * will match if the value contains the filter. The comparison is
     * made in case insensitive mode.
     */
    private boolean stringFilter(String value, String filter) {
	if(filter == null || filter.length() == 0)
	    return true;

	if(value.toUpperCase().indexOf(filter.toUpperCase()) > -1) {
	    return true;
	}

	return false;
    }

    /*
     * Delete the current filter, so all the items will be displayd.
     */
    public ResponseBean clearFilter() {
	form.setFilterUsa("");
	form.setFilterBroasca("");
	form.setFilterCilindru("");
	form.setFilterCilindru("");
	form.setFilterYalla("");
	form.setFilterVizor("");
	
	return ResponseBean.SUCCESS;
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
	    form.setUsa("");
	    form.setUsaId(new Integer(0));
	    form.setUsaCode("");
	    form.setUsaDescription("");
	    form.setBroasca("");
	    form.setCilindru("");
	    form.setSild("");
	    form.setYalla("");
	    form.setVizor("");
	    form.setEntryPrice(new BigDecimal(0));
	    form.setSellPrice(new BigDecimal(0));
	    form.setProductCategory("");
	    form.setProductCode("");
	    form.setProductName("");
	    if(p != null) {
		CompositeProductLocal cp = p.getCompositeProduct();
		Collection parts = cp.getComponents();
		for(Iterator i = parts.iterator(); i.hasNext();) {
		    ProductLocal part = (ProductLocal)i.next();
		    Integer catid = part.getCategory().getId();
		    if(catid.equals(USA_SIMPLA_ID)) {
			form.setUsaId(part.getId());
			form.setUsaCode(part.getCode());
			form.setUsa(part.getName());
			form.setUsaDescription(part.getDescription());
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
		}
		form.setEntryPrice(p.getEntryPrice());
		form.setSellPrice(p.getSellPrice());
		form.setProductCategory(p.getCategory().getName());
		form.setProductCode(p.getCode());
		form.setProductName(p.getName());
	    }
	    else {
		logger.log(BasicLevel.WARN, "No product associated with this offer item " + loadId);
	    }

	    form.setVatPrice(offerItem.getPrice());
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

	    OfferItemLocalHome oih = 
		(OfferItemLocalHome)PortableRemoteObject.narrow
		(env.lookup("ejb/OfferItemHome"), OfferItemLocalHome.class);
	    OfferLocalHome oh = (OfferLocalHome)PortableRemoteObject.narrow
		(env.lookup("ejb/OfferHome"), OfferLocalHome.class);

	    // OfferItem is the instance var containg the current item
	    offerItem = oih.create();
	    offerItem.setProduct(p);
	    offerItem.setPrice(round(computeVATPrice(p.getSellPrice())));

	    r = null; // to avoid compile time error

	    // id is the instance var containing the id of current offer
	    if(id == null) {
		r = saveFormData();
	    }
	    if(id != null) {
		OfferLocal o = oh.findByPrimaryKey(id);
		o.getItems().add(offerItem);
		r = ResponseBean.SUCCESS;
	    }
	} catch (NamingException e) {
	    
	    r = ResponseBean.ERR_CONFIG_NAMING;
	    logger.log(BasicLevel.ERROR, "Error while adding product id " + 
		       productId + 
		       " to the current offer", e);
	} catch (CreateException e) {
	    r = ResponseBean.ERR_CREATE;
	    logger.log(BasicLevel.ERROR, "Error while adding product id " + 
		       productId + 
		       " to the current offer", e);
	} catch (FinderException e) {
	    r = ResponseBean.ERR_NOTFOUND;
	    logger.log(BasicLevel.ERROR, "The offer with id " + id 
		       + " could not be found");
	    logger.log(BasicLevel.INFO, e);
	}
	return r;
    }

    /**
     * Saves the data for the current line item into the persistent layer.
     */
    public ResponseBean saveSubForm() {
	ResponseBean r;
	
	offerItem.setPrice(form.getVatPrice());
	
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
     * Overwrites the default method to store the current selection into an
     * internal variable. This is useful because there are multiple ways to
     * select the current selection, updateSelectionCode beeing just one of
     * them.
     */
    public ResponseBean updateSelectionCode(String code) {
	ResponseBean r;
	if(code.length() == 0) {
	    currentSelection = null;
	    r = super.updateSelectionCode("");
	} else {
	    try {
		InitialContext ic = new InitialContext();
		Context env = (Context)ic.lookup("java:comp/env");
		
		ProductsSelectionLocalHome sh = (ProductsSelectionLocalHome)PortableRemoteObject.
		    narrow(env.lookup("ejb/ProductsSelectionHome"), ProductsSelectionLocalHome.class);
		ProductsSelectionLocal s = sh.findByCode(code);
		
		currentSelection = s;
		r = super.updateSelectionCode(code);
	
	    } catch (FinderException e) {
		r = ResponseBean.ERR_NOTFOUND;
		logger.log(BasicLevel.ERROR, e);
	    } catch (NamingException e) {
		r = ResponseBean.ERR_CONFIG_NAMING;
		logger.log(BasicLevel.ERROR, e);
	    }
	}

	return r;
    }


    /**
     * Called when a new selection is activated for the report. It returns
     * details about the given selection.
     */
    public ResponseBean selectSelection(Integer selectionId) {
	ResponseBean r;

	try {
	    InitialContext ic = new InitialContext();
	    Context env = (Context)ic.lookup("java:comp/env");
	    
	    ProductsSelectionLocalHome sh = (ProductsSelectionLocalHome)PortableRemoteObject.
		narrow(env.lookup("ejb/ProductsSelectionHome"), ProductsSelectionLocalHome.class);
	    ProductsSelectionLocal s = sh.findByPrimaryKey(selectionId);

	    r = new ResponseBean();
	    r.addRecord();
	    r.addField("selectionCode", s.getCode());
	    r.addField("selectionName", s.getName());
	    r.addField("selectionDescription", s.getDescription());

	    currentSelection = s;

	} catch (FinderException e) {
	    r = ResponseBean.ERR_NOTFOUND;
	    logger.log(BasicLevel.ERROR, e);
	} catch (NamingException e) {
	    r = ResponseBean.ERR_CONFIG_NAMING;
	    logger.log(BasicLevel.ERROR, e);
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
	ArrayList reportData;
	
	try {
	    InitialContext ic = new InitialContext();
	    Context env = (Context)ic.lookup("java:comp/env");
	    
	    OfferLocalHome oh = (OfferLocalHome)PortableRemoteObject.
		narrow(env.lookup("ejb/OfferHome"), OfferLocalHome.class);
	    OfferLocal offer = oh.findByPrimaryKey(id);

	    Collection offerItems = offer.getItems();
	    reportData = new ArrayList();

	    TreeSet selectionSet;

	    if(currentSelection != null) {
		selectionSet  = new TreeSet();
		Collection selprods = currentSelection.getProducts();
		for(Iterator i = selprods.iterator(); i.hasNext();) {
		    ProductLocal p = (ProductLocal)i.next();
		    selectionSet.add(p.getId());
		}
	    } else {
		selectionSet = null;
	    }
	    for(Iterator i = offerItems.iterator(); i.hasNext();) {
		OfferItemLocal item = (OfferItemLocal)i.next();

		if(selectionSet == null || selectionSet.contains(item.getProduct().getId())) {
		    HashMap dataRow = new HashMap();
		    loadSubForm(item.getId());
		    dataRow.put("name", form.getProductName());
		    dataRow.put("code", form.getProductCode());
		    dataRow.put("sellPrice", form.getVatPrice());
		    
		    dataRow.put("usa_code", form.getUsaCode());
		    dataRow.put("usa_name", form.getUsa());
		    dataRow.put("usa_id", form.getUsaId());
		    dataRow.put("usa_description", form.getUsaDescription());
		    dataRow.put("broasca_name", form.getBroasca());
		    dataRow.put("cilindru_name", form.getCilindru());
		    dataRow.put("sild_name", form.getSild());
		    dataRow.put("yalla_name", form.getYalla());
		    dataRow.put("vizor_name", form.getVizor());
		    reportData.add(dataRow);
		}

	    }
	} catch (Exception e) {
	    logger.log(BasicLevel.ERROR,
		       "Can not build the report data. Exception caught.", e);
	    reportData = new ArrayList();
	}
	Collections.sort(reportData, new Comparator() {
		public int compare(Object o1, Object o2) {
		    HashMap row1 = (HashMap) o1;
		    HashMap row2 = (HashMap) o2;
		    int comp;
		    if((comp = ((String)row1.get("usa_code")).compareTo((String)row2.get("usa_code"))) == 0) {
			if((comp = ((String)row1.get("broasca_name")).compareTo((String)row2.get("broasca_name"))) == 0) {
			    if((comp = ((String)row1.get("cilindru_name")).compareTo((String)row2.get("cilindru_name"))) == 0) {
				if((comp = ((String)row1.get("sild_name")).compareTo((String)row2.get("sild_name"))) == 0) {
				    if((comp = ((String)row1.get("yalla_name")).compareTo((String)row2.get("yalla_name"))) == 0) {
					if((comp = ((String)row1.get("vizor_name")).compareTo((String)row2.get("vizor_name"))) == 0) {
					    // the two are equal
					}
				    }
				}
			    }
			}
		    }

		    return comp;
		    
		}
	    });
	return reportData;
    }

    /**
     * Creates a map of pairs field name -- field value to be used as
     * a parameters map for a report displaying the list of items.
     * The fields are values of the main record.
     */
    public Map getOfferFieldsMap() {
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


    /**
     * The method that will get the rounded value of price. The argument
     * is the price of the product to be rounded.
     */
    static public BigDecimal round(BigDecimal price) {
	long lPrice = price.longValue();
	long digit = lPrice % 10;
	lPrice /= 10;
	if(digit > 0)
	    lPrice += 1;
	return new BigDecimal(lPrice * 10);
    }

    private BigDecimal computeVATPrice(BigDecimal price) {
	return price.multiply(form.getVat().add(new BigDecimal(1)));
    }
}

