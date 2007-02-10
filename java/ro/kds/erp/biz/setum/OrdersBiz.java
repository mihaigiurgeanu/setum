package ro.kds.erp.biz.setum;

import ro.kds.erp.biz.setum.basic.OrdersBean;
import javax.ejb.SessionContext;
import ro.kds.erp.biz.ResponseBean;
import javax.ejb.FinderException;
import ro.kds.erp.data.OrderLocalHome;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;
import javax.naming.NamingException;
import org.objectweb.util.monolog.api.BasicLevel;
import javax.naming.Context;
import ro.kds.erp.data.ProductLocal;
import ro.kds.erp.data.OrderLocal;
import javax.ejb.CreateException;
import ro.kds.erp.data.ClientLocal;
import ro.kds.erp.data.DataLayerException;
import java.math.BigDecimal;
import ro.kds.erp.data.OrderLineLocal;
import ro.kds.erp.data.OrderLineLocalHome;
import ro.kds.erp.data.ClientLocalHome;
import ro.kds.erp.data.OfferItemLocalHome;
import ro.kds.erp.data.OfferItemLocal;
import java.util.Collection;
import java.util.Map;
import java.util.Date;
import javax.ejb.RemoveException;
import java.util.Iterator;

import java.util.ArrayList;
import ro.kds.erp.biz.ResponseBean;
import ro.kds.erp.biz.ResponseBean;
import ro.kds.erp.biz.PreferencesBean;
import ro.kds.erp.biz.SequenceHome;
import java.util.Calendar;
import ro.kds.erp.biz.Preferences;
import ro.kds.erp.biz.Sequence;
import ro.kds.erp.biz.ResponseBean;
import javax.ejb.FinderException;
import ro.kds.erp.biz.ResponseBean;
import javax.ejb.FinderException;
import ro.kds.erp.biz.ResponseBean;
import ro.kds.erp.biz.ResponseBean;
import ro.kds.erp.data.InvoiceLocal;
import ro.kds.erp.data.InvoiceLocalHome;
import ro.kds.erp.data.PaymentLocal;
import ro.kds.erp.data.PaymentLocalHome;

/**
 * Describe class OrdersBiz here.
 *
 *
 * Created: Mon Sep 18 04:39:17 2006
 *
 * @author <a href="mailto:Mihai Giurgeanu@MIHAIG">U-MIHAIG\Mihai Giurgeanu</a>
 * @version 1.0
 */
public class OrdersBiz extends OrdersBean {



    /**
     * The name of the Order entity.
     */
    public final String ENTITY_ORDER = "Order";
    /**
     * The name of the OrderLine entity.
     */
    public final String ENTITY_ORDER_LINE = "OrderLine";
    /**
     * The name of the Invoice entity.
     */
    public final String ENTITY_INVOICE = "Invoice";
    /**
     * The name of the Payment entity.
     */
    public final String ENTITY_PAYMENT = "Payment";
    
    /**
     * The maximum number of records to be returned in one request
     * for getting the listing of orders or lines.
     */
    static final int LISTING_ROWS_PER_REQUEST = 30;

    /**
     * Initialization of fields. The default initialization is
     * made by the parrent class.
     *
     */
    public void createNewFormBean() {
	super.createNewFormBean();
	


	// find out what is the default delivery interval in days
	int defaultDelivery; 
	
	try {
	    Preferences prefs = PreferencesBean.getPreferences();
	    defaultDelivery = prefs.getInteger("orders.delivery.interval", 
					    new Integer(30)).intValue();

	    form.setTvaPercent(prefs.getDouble("invoices.tax.percent",
					       new Double(19.0)));
	} catch (Exception e) {
	    logger.log(BasicLevel.ERROR, "Exception when geting preferences: ", e);
	    defaultDelivery = 30;
	    form.setTvaPercent(new Double(19.0));
	}

	// get a new order no
	Integer orderNo;
	try {
	    InitialContext ic = new InitialContext();
	    Context env = (Context) ic.lookup("java:comp/env");
	    
	    SequenceHome sh = (SequenceHome)PortableRemoteObject.narrow
		(env.lookup("ejb/SequenceHome"), SequenceHome.class);
	    Sequence s = sh.create();
	    orderNo = s.getNext("ro.setumsa.sequnces.orders");
	} catch (Exception e) {
	    orderNo = null;
	    logger.log(BasicLevel.WARN, "Can not get a number for order", e);
	}


	form.setNumber(orderNo.toString());
	form.setDate(new Date());

	Calendar dateDelivCal = Calendar.getInstance();
	dateDelivCal.setTime(form.getDate());
	dateDelivCal.add(Calendar.DATE, defaultDelivery);
	form.setTermenLivrare(dateDelivCal.getTime());
	form.setTermenLivrare1(dateDelivCal.getTime());


    }



    /**
     * Load the fields values from the database. The <code>id</code> variable
     * holds the primary key of the currently selected record.
     * @throws FinderException if the id contains a value that can not be
     * located in the database (through the use of 
     * <code>OrderLocalHome.findByPrimaryKey</code>).
     */
    public ResponseBean loadFields() throws FinderException {
	ResponseBean r;

	try {
	    OrderLocal o = getCurrentOrder();

	    form.setNumber(o.getDocument().getNumber());
	    form.setDate(o.getDocument().getDate());
	    
	    ClientLocal client = o.getClient();
	    if(client != null) {
		form.setClientId(client.getId());
		form.setClientName(client.getName());
	    } else {
		form.setClientId(new Integer(0));
		form.setClientName("");
	    }

	    form.setMontaj(o.getInstallation());
	    form.setLocalitate(o.getDeliveryLocation());
	    form.setLocalitateAlta(o.getDeliveryLocationOther());
	    form.setDistanta(o.getDeliveryDistance());
	    form.setObservatii(o.getDeliveryComments());
	    
	    form.setDiscount((BigDecimal)o.getDiscount());
	    form.setAvans((BigDecimal)o.getAdvancePayment());
	    form.setAchitatCu((String)o.getAdvanceDocument());

	    form.setTermenLivrare(o.getDeliveryTerm());
	    form.setTermenLivrare1(o.getDeliveryTerm1());
	    form.setAdresaMontaj(o.getDeliveryAddress());
	    form.setAdresaReper(o.getDeliveryAddressHint());
	    form.setTelefon(o.getDeliveryPhone());
	    form.setContact(o.getDeliveryContact());	    

	    form.setTotal(getOrderedAmount());
	    form.setPayedAmount(o.getPayedAmount());
	    form.setInvoicedAmount(o.getInvoicedAmount());

	    r = new ResponseBean();
	} catch (NamingException e) {
	    r = ResponseBean.getErrConfigNaming(e.getMessage());

	    logger.log(BasicLevel.ERROR, "Order data could not be loaded from database");
	    logger.log(BasicLevel.DEBUG, e);
	} catch (FinderException e) {
	    logger.log(BasicLevel.ERROR, "Finder exceptin occured");
	    logger.log(BasicLevel.DEBUG, e);
	    throw e;
	} catch (Exception e) {
	    r = ResponseBean.getErrUnexpected(e);

	    logger.log(BasicLevel.ERROR, "Unexpected exception occured. Data for order id " + id + 
		       " could not be loaded from the database: " + e);
	    logger.log(BasicLevel.DEBUG, e);
	}

	return r;
    }


    /**
     * Save the form fields value into the database. This method will save only data
     * of <code>OrderLocal</code> object, the data for <code>OrderLineLocal</code> objects
     * will be saved by the <code>saveOfferItemData</code> method.
     */
    public ResponseBean saveFormData() {
	ResponseBean r;

	try {
	    OrderLocal o;
	    if(id == null) {
		OrderLocalHome oh = getOrderHome();
		o = oh.create();
		id = o.getId(); // currently loaded order
	    } else {
		o = getCurrentOrder();
	    }

	    o.getDocument().setNumber(form.getNumber());
	    o.getDocument().setDate(form.getDate());
	    o.setClient(getFormClient());

	    o.setInstallation(form.getMontaj());
	    o.setDeliveryLocation(form.getLocalitate());
	    o.setDeliveryLocationOther(form.getLocalitateAlta());
	    o.setDeliveryDistance(form.getDistanta());
	    o.setDeliveryComments(form.getObservatii());
	    
	    o.setDiscount(form.getDiscount().setScale(2, BigDecimal.ROUND_HALF_UP));
	    o.setAdvancePayment(form.getAvans().setScale(2, BigDecimal.ROUND_HALF_UP));
	    o.setAdvanceDocument(form.getAchitatCu());

	    o.setDeliveryTerm(form.getTermenLivrare());
	    o.setDeliveryTerm1(form.getTermenLivrare1());
	    o.setDeliveryAddress(form.getAdresaMontaj());
	    o.setDeliveryAddressHint(form.getAdresaReper());
	    o.setDeliveryPhone(form.getTelefon());
	    o.setDeliveryContact(form.getContact());

	    r = new ResponseBean();
	} catch (NamingException e) {
	    r = ResponseBean.getErrConfigNaming(e.getMessage());
	    logger.log(BasicLevel.ERROR, "Order id " + id + " not saved; " + e);
	    logger.log(BasicLevel.DEBUG, e);
	} catch (FinderException e) {
	    r = ResponseBean.getErrNotFound(e.getMessage());
	    logger.log(BasicLevel.ERROR, "Order id " + id + " not saved; " + e);
	    logger.log(BasicLevel.DEBUG, e);
	} catch (CreateException e) {
	    r = ResponseBean.getErrCreate(e.getMessage());
	    logger.log(BasicLevel.ERROR, "Order id " + id + " not saved; " + e);
	    logger.log(BasicLevel.DEBUG, e);
	} catch (DataLayerException e) {
	    r = ResponseBean.getErrConfigNaming(e.getMessage());
	    logger.log(BasicLevel.ERROR, "Order id " + id + " not saved; " + e);
	    logger.log(BasicLevel.DEBUG, e);
	} catch (Exception e) {
	    r = ResponseBean.getErrUnexpected(e);
	    logger.log(BasicLevel.ERROR, "Order id " + id + " not saved; " + e);
	    logger.log(BasicLevel.DEBUG, e);
	}

	return r;
    }



    /**
     * Removes the order and its order lines.
     */
    public ResponseBean removeOrder() {
	ResponseBean r;

	if(! isSelectedOrder() ) {
	    r = ResponseBean.getErrNotCurrent("Order");
	} else {


	    try {
		OrderLocal o = getCurrentOrder();
		o.remove();
		r = new ResponseBean();

	    } catch (RemoveException e) {
		r = ResponseBean.getErrRemove("id = " + id);
		logger.log(BasicLevel.INFO, "Remove failed due to reomve exception " + e.getMessage());
		logger.log(BasicLevel.DEBUG, e);
	    } catch (NamingException e) {
		r = ResponseBean.getErrConfigNaming(e.getMessage());
		logger.log(BasicLevel.INFO, "Remove failed due to naming exception " + e.getMessage());
		logger.log(BasicLevel.DEBUG, e);
	    } catch (FinderException e) {
		r = ResponseBean.getErrNotFound("id = " + id);
		logger.log(BasicLevel.INFO, "Remove failed due to finder exception " + e.getMessage());
		logger.log(BasicLevel.DEBUG, e);
	    } catch (Exception e) {
		r = ResponseBean.getErrUnexpected(e);
		logger.log(BasicLevel.INFO, "Unexpected exception " + e);
		logger.log(BasicLevel.DEBUG, e);
	    }


	}
	return r;
    }



    /**
     * Initializes the order line fields in the <code>form</code> object.
     * It is called when creating a new order line in the subform object.
     */
    public void initOrderLineFields() {
	form.setOfferItemId(new Integer(0));
	form.setProductName("");
	form.setProductCode("");
	form.setPrice(new BigDecimal(0));
	form.setProductPrice(new BigDecimal(0));
	form.setPriceRatio(new Double(0));
	form.setQuantity(new BigDecimal(0));
	form.setValue(new BigDecimal(0));
	form.setTax(new BigDecimal(0));	
    }



    /**
     * Load the values of order line record fields from the database.
     */
    public ResponseBean loadOrderLineFields() {
	ResponseBean r;

	if(isSelectedOrderLine()) {




	    // OrderLine selected
	    try {
		
		OrderLineLocal ol = getCurrentLine();


		form.setOfferItemId(ol.getOfferItem().getId());
		form.setProductName(ol.getOfferItem().getProduct().getName());
		form.setProductCode(ol.getOfferItem().getProduct().getCode());
		form.setPrice(ol.getPrice());
		form.setProductPrice(ol.getOfferItem().getPrice());
		form.setQuantity(ol.getQuantity());
		

		r = new ResponseBean();
		logger.log(BasicLevel.DEBUG, "Order line " + orderLineId + " has been loaded.");
	    } catch (NamingException e) {
		r = ResponseBean.getErrConfigNaming(e.getMessage());
		logger.log(BasicLevel.ERROR, "Order line " + 
			   orderLineId + 
			   " can not be loaded due to a naming error");
		logger.log(BasicLevel.DEBUG, e);
	    } catch (FinderException e) {
		r = ResponseBean.getErrNotFound(e.getMessage());
		logger.log(BasicLevel.ERROR, "Selected order line, " + orderLineId + " can not be found.");
		logger.log(BasicLevel.DEBUG, e);
	    } catch (Exception e) {
		r = ResponseBean.getErrUnexpected(e);
		logger.log(BasicLevel.ERROR, "Order line " + orderLineId + 
			   " can not be loaded due to an unexpected exception " + e);
		logger.log(BasicLevel.DEBUG, e);
	    }


	    
	} else {
	    r = ResponseBean.getErrNotCurrent(ENTITY_ORDER_LINE);
	    logger.log(BasicLevel.DEBUG, "No order line selected");
	}

	return r;
    }

    /**
     * Save the data of the order line subform.
     */
    public ResponseBean saveOrderLineData() {
	ResponseBean r;

	logger.log(BasicLevel.DEBUG, "Saving OrderLine subform");

	// Current record should be saved, because it might be a new one
	r = saveFormData();
	if (r.getCode() != ResponseBean.CODE_SUCCESS)
	    return r;

	// Preconditions
	if(! isSelectedOrder()) {
	    logger.log(BasicLevel.DEBUG, "No order is currently selected");
	    return ResponseBean.ERR_NOTCURRENT;
	}

	if(form.getOfferItemId() == null) {
	    logger.log(BasicLevel.WARN, "The order line does not contain an offer item so will not be saved");
	    return ResponseBean.getErrDataMissing("offerItem");
	}



	try {
	    OrderLocal o = getCurrentOrder();
	    OrderLineLocal ol;
	    
	    if(!isSelectedOrderLine()) {
		// no order line selected
		// the data of the subform will be saved to a new order line
		logger.log(BasicLevel.DEBUG, "New order line will be created");
		OrderLineLocalHome olh = getOrderLineHome();
		ol = olh.create();
		o.getLines().add(ol);
	    } else {
		ol = getCurrentLine();
	    }

	    ol.setOfferItem(getOfferItem());
	    ol.setPrice(form.getPrice().setScale(2, BigDecimal.ROUND_HALF_UP));
	    ol.setQuantity(form.getQuantity().setScale(2, BigDecimal.ROUND_HALF_UP));
	    
	    try {
		form.setTotal(getOrderedAmount());
	    } catch (Exception e) {
		logger.log(BasicLevel.ERROR, "Can not get the ordered amount: " + e.getMessage());
		logger.log(BasicLevel.DEBUG, e);
		form.setTotal(new BigDecimal(0));
	    }
	    r = computeCalculatedFields(null);
	    r.addField("total", form.getTotal());

	} catch (NamingException e) {
	    logger.log(BasicLevel.ERROR, "Naming service exception: " + e.getMessage());
	    logger.log(BasicLevel.DEBUG, e);
	    r = ResponseBean.getErrConfigNaming(e.getMessage());
	} catch (FinderException e) {
	    logger.log(BasicLevel.ERROR, "Entity can not be found: " + e.getMessage());
	    logger.log(BasicLevel.DEBUG, e);
	    r = ResponseBean.getErrNotFound(e.getMessage());
	} catch (CreateException e) {
	    logger.log(BasicLevel.ERROR, "OrderLine entity can not be created: " + e.getMessage());
	    logger.log(BasicLevel.DEBUG, e);
	    r = ResponseBean.getErrCreate(ENTITY_ORDER_LINE);
	} catch (Exception e) {
	    logger.log(BasicLevel.ERROR, "Unexpected exception: " + e);
	    r = ResponseBean.getErrUnexpected(e);
	}

	return r;
    }



    /**
     * Add a new line to the order. The line should point to an OfferItem entity
     * that is trasformed in order.
     */
    public ResponseBean addItem(Integer offerItemId) {
        initOrderLineFields();
        orderLineId = null;
	form.setOfferItemId(offerItemId);

	ResponseBean r;
	try {
	    try {
		OfferItemLocal oi = getOfferItem();
		form.setQuantity(oi.getQuantity());
		form.setPrice(oi.getPrice());
		form.setValue(new BigDecimal(form.getPrice().doubleValue() *
					     form.getQuantity().doubleValue()));
		form.setTax(new BigDecimal(form.getValue().doubleValue() *
					   form.getTvaPercent().doubleValue()));

		logger.log(BasicLevel.DEBUG, "default quantity: " + oi.getQuantity());
		logger.log(BasicLevel.DEBUG, "default price: " + oi.getPrice());

		if(form.getQuantity() == null) form.setQuantity(new BigDecimal(0));
		if(form.getPrice() == null) form.setPrice(new BigDecimal(0));

	    } catch (NamingException e) {
		logger.log(BasicLevel.WARN, "Naming exception when getting the offer item. Continue execution of business logic.");
		logger.log(BasicLevel.DEBUG, e);
	    }	    
	    computeCalculatedFields(null);
	    r = saveOrderLineData();
	} catch (FinderException e) {
	    logger.log(BasicLevel.ERROR, "Finder exception " + e);
	    logger.log(BasicLevel.DEBUG, e);
	    r = ResponseBean.getErrNotFound(e.getMessage());
	}
        return r;
    }


    /**
     * Remove the currently selected order line.
     */
    public ResponseBean removeItem() {
	if(! isSelectedOrderLine() ) {
	    return ResponseBean.getErrNotCurrent(ENTITY_ORDER_LINE);
	}
	
	ResponseBean r;
	try {
	    OrderLineLocal ol = getCurrentLine();
	    ol.remove();

	    r = new ResponseBean();
	    try {
		OrderLocal o = getCurrentOrder();
		form.setTotal(getOrderedAmount());
		r = computeCalculatedFields(null);
		r.addField("total", form.getTotal());
	    } catch (Exception e) {
		logger.log(BasicLevel.ERROR, "Can not get the ordered amount: " + e.getMessage());
		logger.log(BasicLevel.DEBUG, e);
		form.setTotal(new BigDecimal(0));
	    }
	    
	} catch (FinderException e) {
	    r = ResponseBean.getErrNotFound(e.getMessage());
	    logger.log(BasicLevel.ERROR, "Finder exception when trying to delete an order line");
	    logger.log(BasicLevel.DEBUG, e);
	} catch (RemoveException e) {
	    r = ResponseBean.getErrRemove(ENTITY_ORDER_LINE);
	    logger.log(BasicLevel.ERROR, "Order line with id " + orderLineId + 
		       " can not be remove because " + e);
	    logger.log(BasicLevel.DEBUG, e);
	} catch (NamingException e) {
	    logger.log(BasicLevel.ERROR, "Configuration error: name not found: " + e.getMessage());
	    logger.log(BasicLevel.DEBUG, e);
	    r = ResponseBean.getErrConfigNaming(e.getMessage());
	}

	return r;
    }


    /**
     * Get the list of orders. It only returns a part of the cached list of
     * orders starting with <code>startRow</code>, so the list can be loaded
     * bit by bit as the user scrolls into the list control.
     *
     * The cacned list of orders is initialized by calling <code>getOrdersCount</code>
     * method that should be called before calling <code>loadListing</code>.
     *
     * @param startRow is the row number for which the ui service is requesting
     * data.
     *
     * @return a ResponseBean cotaining Order records with the following fields:
     * <ul>
     * <li>orders.id</li>
     * <li>orders.no</li>
     * <li>orders.date</li>
     * <li>orders.client</li>
     * <li>orders.localitate</li>
     * <li>orders.distanta</li>
     * <li>orders.avans</li>
     * <li>orders.termenLivrare</li>
     * </ul>
     */
    public ResponseBean loadListing(Integer startRow) {
	ResponseBean r;

	r = new ResponseBean();
	int endRow = startRow.intValue() + LISTING_ROWS_PER_REQUEST;
	for(int i = startRow.intValue(); 
	    (i < endRow) && (i < listingCache.size()); i++) {

	    OrderLocal o = (OrderLocal)listingCache.get(i);
	    r.addRecord();

	    r.addField("orders.id",		o.getId());
	    r.addField("orders.no",		o.getDocument().getNumber());
	    r.addField("orders.date",		o.getDocument().getDate());
	    if(o.getClient() != null)
		r.addField("orders.client",	o.getClient().getName());
	    else
		r.addField("orders.client",	"");
	    try {
		r.addField("orders.localitate",	
			   ValueLists.getValueByCode(12005, o.getDeliveryLocation().toString()).getName());
	    } catch (Exception e) {
		r.addField("orders.localitate", "-");
		logger.log(BasicLevel.ERROR, "Can not read the value of orders.localitate for code " 
			   + o.getDeliveryLocation());
	    }
	    r.addField("orders.distanta",	o.getDeliveryDistance());
	    r.addField("orders.avans",		o.getAdvancePayment());
	    r.addField("orders.termenLivrare",	o.getDeliveryTerm());
	} 

	return r;
    }


    /**
     * Cache for the orders list
     */
    ArrayList listingCache;

    /**
     * Loads the list of the orders into a cache variable and returns the
     * number of orders. After calling this method you can call <code>loadListing</code>
     * to get the parts of the cached list.
     *
     * @return a <code>ResponseBean</code> with one record that has the field
     * <code>records-count</code> set to the number of orders.
     */
    public ResponseBean getOrdersCount() {
	ResponseBean r;

	try {
	    OrderLocalHome oh = getOrderHome();
	    listingCache = new ArrayList(oh.findAll());

	    r = new ResponseBean();
	    r.addRecord();
	    r.addField("records-count", listingCache.size());


	} catch (NamingException e) {
	    logger.log(BasicLevel.ERROR, "Naming exception when trying to read the orders listing from database: "
		       + e);
	    logger.log(BasicLevel.DEBUG, e);
	    r = ResponseBean.getErrConfigNaming(e.getMessage());
	} catch (FinderException e) {
	    logger.log(BasicLevel.ERROR, "Could not get the list of orders from database: " + e);
	    logger.log(BasicLevel.DEBUG, e);
	    r = ResponseBean.getErrNotFound(ENTITY_ORDER);
	} catch (Exception e) {
	    logger.log(BasicLevel.ERROR, "Unexpected exception occured: " + e);
	    logger.log(BasicLevel.DEBUG, e);
	    r = ResponseBean.getErrUnexpected(e);
	}

	return r;
    }

    /**
     * Retrieves the list of order lines for the currently selected order.
     * 
     * @return a <code>ResponseBean</code> with multiple records, each record
     * containing fields:
     * <ul>
     * <li>orderItems.id
     * <li>orderItems.offerNo
     * <li>orderItems.offerDate
     * <li>orderItems.productName
     * <li>orderItems.productCode
     * <li>orderItems.quantity
     * <li>orderItems.price
     * </ul>
     */
    public ResponseBean loadLines() {
	ResponseBean r;

	try {

	    OrderLocal o = getCurrentOrder();
	    Collection lines = o.getLines();

	    r = new ResponseBean();
	    for(Iterator i = lines.iterator(); i.hasNext();) {
		OrderLineLocal ol = (OrderLineLocal)i.next();
		
		r.addRecord();
		r.addField("orderItems.id", ol.getId());
		r.addField("orderItems.offerNo", ol.getOfferItem().getOffer().getDocument().getNumber());
		r.addField("orderItems.offerDate", ol.getOfferItem().getOffer().getDocument().getDate());
		r.addField("orderItems.productName", ol.getOfferItem().getProduct().getName());
		r.addField("orderItems.productCode", ol.getOfferItem().getProduct().getCode());

		r.addField("orderItems.quantity", ol.getQuantity());
		r.addField("orderItems.price", ol.getPrice());
	    }

	} catch (NamingException e) {
	    r = ResponseBean.getErrConfigNaming(e.getMessage());
	    logger.log(BasicLevel.ERROR, 
		       "The order lines could not be read because configuration error: " + e);
	    logger.log(BasicLevel.DEBUG, e);
	} catch (FinderException e) {
	    r = ResponseBean.getErrNotFound(e.getMessage());
	    logger.log(BasicLevel.ERROR, 
		       "Finder exception when trying to get the list of order lines: " + e);
	    logger.log(BasicLevel.DEBUG, e);
	} catch (Exception e) {
	    r = ResponseBean.getErrUnexpected(e);
	    logger.log(BasicLevel.ERROR, "Unexpected exception " + e);
	    logger.log(BasicLevel.DEBUG, e);
	}

	return r;
    }

    /**
     * Overriden method to get the client's name and other attribute.
     *
     * @param clientid is the id of the client owning the order.
     * @return a <code>ResponseBean</code> with the name of the new client.
     */
    public ResponseBean updateClientId(final Integer clientid) {
	ResponseBean r = super.updateClientId(clientid);
	
	try {
	    ClientLocal client = getFormClient();
	    r.addField("clientName", client.getName());
	} catch (NamingException e) {
	    logger.log(BasicLevel.ERROR, "Can not get the client's name due to a naming configuration problem: " + e.getMessage());
	    logger.log(BasicLevel.DEBUG, e);
	    r = ResponseBean.getErrConfigNaming(e.getMessage());
	} catch (FinderException e) {
	    logger.log(BasicLevel.ERROR, "FinderException while serching for the client with id: " + id);
	    logger.log(BasicLevel.DEBUG, e);
	    r = ResponseBean.getErrNotFound(e.getMessage());
	} catch (Exception e) {
	    logger.log(BasicLevel.ERROR, "Unexpected exception " + e);
	    logger.log(BasicLevel.DEBUG, e);
	    r = ResponseBean.getErrUnexpected(e);
	}

	return r;
    }
  

    /**
     * Overwrites the default method to not allow the client's name to be
     * updated inconsistently with the client's id. The default method and
     * script will still be ran, but the client's name will be overwritten
     * with the name corresponding to the client's id.
     *
     * @param clientName is the new name for the client's name field.
     *
     * @return a <code>ResponseBean</code> with the real client name.
     */
    public ResponseBean updateClientName(String clientName) {
	ResponseBean r = super.updateClientName(clientName);
	try {
	    ClientLocal client = getFormClient();
	    r.addField("clientName", client.getName());
	} catch (NamingException e) {
	    logger.log(BasicLevel.ERROR, "Can not get the client's name due to a naming configuration problem: " + e.getMessage());
	    logger.log(BasicLevel.DEBUG, e);
	    r = ResponseBean.getErrConfigNaming(e.getMessage());
	} catch (FinderException e) {
	    logger.log(BasicLevel.ERROR, "FinderException while serching for the client with id: " + form.getClientId());
	    logger.log(BasicLevel.DEBUG, e);
	    r = ResponseBean.getErrNotFound(e.getMessage());
	} catch (Exception e) {
	    logger.log(BasicLevel.ERROR, "Unexpected exception " + e);
	    logger.log(BasicLevel.DEBUG, e);
	    r = ResponseBean.getErrUnexpected(e);
	}

	return r;
    }

    /**
     * Initialization of the invoice form fields to the default values.
     *
     */
    public void initInvoiceFields() {
	logger.log(BasicLevel.DEBUG, ">");
	
	form.setInvoiceNumber("");
	form.setInvoiceDate(new Date());
	form.setInvoiceRole("");
	form.setInvoiceAmount(new BigDecimal(0));
	form.setInvoiceTax(new BigDecimal(0));
	form.setInvoiceTotal(new BigDecimal(0));
	form.setInvoicePayed(new BigDecimal(0));
	form.setInvoiceUnpayed(new BigDecimal(0));


	if(isSelectedOrder()) {
	    form.setInvoiceAmount(new BigDecimal(form.getTotalFinal().doubleValue() - 
						 form.getInvoicedAmount().doubleValue())
				  .setScale(2, BigDecimal.ROUND_HALF_UP));
	    form.setInvoiceTax(new BigDecimal(form.getInvoiceAmount().doubleValue() *
					      form.getTvaPercent().doubleValue() / 100)
			       .setScale(2, BigDecimal.ROUND_HALF_UP));
	}


	logger.log(BasicLevel.DEBUG, "<");
    }

    /**
     * Copy the values of currently selected invoice entity into
     * the form fields.
     *
     * @return a <code>ResponseBean</code> with the result code and,
     * if an error occurs, the error info.
     * @exception FinderException if no entity could be found for the
     * id indicating the currently selected invoice.
     */
    public ResponseBean loadInvoiceFields() throws FinderException {
	ResponseBean r;

	logger.log(BasicLevel.DEBUG, ">");

	try {
	    InvoiceLocal inv = getCurrentInvoice();

	    form.setInvoiceNumber(inv.getDocument().getNumber());
	    form.setInvoiceDate(inv.getDocument().getDate());
	    form.setInvoiceRole(inv.getRole());
	    form.setInvoiceAmount(inv.getAmount());
	    form.setInvoiceTax(inv.getTax());
	    form.setInvoicePayed(inv.getSumOfPayments());
	    
	    r = new ResponseBean();
	} catch (NamingException e) {
	    logger.log(BasicLevel.INFO, "The invoice could not be loaded " +
		       "due to a configuration error of the naming service: " +
		       e.getMessage());
	    logger.log(BasicLevel.DEBUG, e);
	    r = ResponseBean.getErrConfigNaming(e.getMessage());
	}


	logger.log(BasicLevel.DEBUG, "<");
	return r;
    }
  

    /**
     * Save the form fields into the persistent layer. If there is
     * no currently selected invoice, a new invoice entity will be
     * created for this order.
     */
    public ResponseBean saveInvoiceData() {

	ResponseBean r;

	// Current record should be saved, because it might be a new one
	r = saveFormData();
	if (r.getCode() != ResponseBean.CODE_SUCCESS)
	    return r;

	if(! isSelectedOrder()) {
	    logger.log(BasicLevel.WARN, "saveInvoiceData called but no order is selected");
	    return ResponseBean.getErrNotCurrent(ENTITY_ORDER);
	}


	logger.log(BasicLevel.DEBUG, ">");

	try {
	    InvoiceLocal inv;

	    if(isSelectedInvoice()) {
		inv = getCurrentInvoice();
	    } else {
		InvoiceLocalHome invh = getInvoiceHome();
		inv = invh.create();
		getCurrentOrder().getInvoices().add(inv);
	    }

	    inv.getDocument().setNumber(form.getInvoiceNumber());
	    inv.getDocument().setDate(form.getInvoiceDate());
	    inv.setRole(form.getInvoiceRole());
	    inv.setAmount(form.getInvoiceAmount().setScale(2, BigDecimal.ROUND_HALF_UP));
	    inv.setTax(form.getInvoiceTax().setScale(2, BigDecimal.ROUND_HALF_UP));

	    r = new ResponseBean();
	} catch (CreateException e) {
	    logger.log(BasicLevel.ERROR, "Invoice entity could not be created: " + e.getMessage());
	    logger.log(BasicLevel.INFO, "The order id at invoice create exception is: " + id);
	    logger.log(BasicLevel.DEBUG, e);
	    
	    r = ResponseBean.getErrCreate(ENTITY_INVOICE);
	} catch (DataLayerException e) {
	    logger.log(BasicLevel.ERROR, "Invoice entity could not be created: " + e.getMessage());
	    logger.log(BasicLevel.INFO, "The order id at invoice create exception is: " + id);
	    logger.log(BasicLevel.DEBUG, e);
	    
	    r = ResponseBean.getErrCreate(ENTITY_INVOICE);
	} catch (FinderException e) {
	    logger.log(BasicLevel.ERROR, "Finder exception: " + e.getMessage());
	    logger.log(BasicLevel.INFO, "The order id at finder exception is: " + id);
	    logger.log(BasicLevel.INFO, "The invoice id at finder exception is: " + id);
	    
	    r = ResponseBean.getErrNotFound(e.getMessage());
	} catch (NamingException e) {
	    logger.log(BasicLevel.ERROR, "Naming service error: " + e.getMessage());
	    logger.log(BasicLevel.DEBUG, e);
	    
	    r = ResponseBean.getErrConfigNaming(e.getMessage());
	} catch (Exception e) {
	    logger.log(BasicLevel.ERROR, "Unexpected exception" , e);
	    r = ResponseBean.getErrUnexpected(e);
	}


	logger.log(BasicLevel.DEBUG, "<");
	return r;
    }


    /**
     * Remove the currently selected invoice from the database.
     */
    public ResponseBean removeInvoice() {
	if(! isSelectedInvoice()) {
	    logger.log(BasicLevel.WARN, "removeInvoice called but no invoice is selected");
	    return ResponseBean.getErrNotCurrent(ENTITY_INVOICE);
	}


	logger.log(BasicLevel.DEBUG, ">");
	ResponseBean r;

	
	try {
	    InvoiceLocal inv = getCurrentInvoice();
	    inv.remove();
	    
	    r = new ResponseBean();
	} catch (NamingException e) {
	    logger.log(BasicLevel.ERROR, "Naming service error: " + e.getMessage());
	    logger.log(BasicLevel.DEBUG, e);
	    
	    r = ResponseBean.getErrConfigNaming(e.getMessage());
	} catch (FinderException e) {
	    logger.log(BasicLevel.ERROR, "Finder exception: " + e.getMessage());
	    logger.log(BasicLevel.INFO, "When finder exception occured, the invoice id was " + invoiceId);
	    logger.log(BasicLevel.DEBUG, e);

	    r = ResponseBean.getErrNotFound(e.getMessage());
	} catch (RemoveException e) {
	    logger.log(BasicLevel.ERROR, "Remove exception: " + e.getMessage());
	    logger.log(BasicLevel.INFO, "Failed to remove invoice with id " + invoiceId);
	    logger.log(BasicLevel.DEBUG, e);

	    r = ResponseBean.getErrRemove(ENTITY_INVOICE);
	} catch (Exception e) {
	    logger.log(BasicLevel.ERROR, "Unexpected eception", e);
	    r = ResponseBean.getErrUnexpected(e);
	}

	return r;
	    
    }

    /**
     * Get the listing of invoices for the current order.
     * The fields in the returned <code>ResponseBean</code> for
     * each invoice are:
     * <ul>
     * <li>invoices.id
     * <li>invoices.number
     * <li>invoices.date
     * <li>invoices.role
     * <li>invoices.amount
     * </ul>
     *
     * @return an <code>ResponseBean</code> object holding a list of records,
     * one for each invoice for the current order.
     */
    public ResponseBean loadInvoices() {
	if(! isSelectedOrder()) {
	    logger.log(BasicLevel.WARN, "loadInvoices called but no order is selected.");
	    return ResponseBean.getErrNotCurrent(ENTITY_ORDER);
	}

	logger.log(BasicLevel.DEBUG, ">");
	ResponseBean r;
	
	try {
	    Collection invoices = getCurrentOrder().getInvoices();

	    r = new ResponseBean();
	    for(Iterator i = invoices.iterator(); i.hasNext();) {
		InvoiceLocal inv = (InvoiceLocal)i.next();
		
		r.addRecord();
		r.addField("invoices.id", inv.getId());
		r.addField("invoices.number", inv.getDocument().getNumber());
		r.addField("invoices.date", inv.getDocument().getDate());
		r.addField("invoices.role", inv.getRole());
		r.addField("invoices.amount", inv.getAmount());
	    }
	} catch (NamingException e) {
	    logger.log(BasicLevel.ERROR, "Naming exception " + e.getMessage());
	    logger.log(BasicLevel.DEBUG, e);
	    r = ResponseBean.getErrConfigNaming(e.getMessage());
	} catch (FinderException e) {
	    logger.log(BasicLevel.ERROR, "FinderException: " + e.getMessage());
	    logger.log(BasicLevel.INFO, "Order id searched when FinderException occured is: " + id);
	    logger.log(BasicLevel.DEBUG, e);
	    r = ResponseBean.getErrNotFound(e.getMessage());
	} catch (Exception e) {
	    logger.log(BasicLevel.ERROR, "Unexpected exception", e);
	    r = ResponseBean.getErrUnexpected(e);
	}	    

	logger.log(BasicLevel.DEBUG, "<");
	return r;
    }

    /**
     * Initialization of form fields with default values. After the
     * initialization, the <code>computeCalculatedFields</code> will
     * be called, so no business logic should be done here, only strict
     * initialization with empty values -- 0 for numbers, empty strings and
     * current dates.
     *
     */
    public  void initPaymentFields() {

	form.setPaymentNumber("");
	form.setPaymentDate(new Date());
	form.setPaymentAmount(new BigDecimal(0));

	if(isSelectedInvoice()) {
	    form.setPaymentAmount(form.getInvoiceTotal().subtract(form.getInvoicePayed()));
	}

    }

    /**
     * Copy the values from payment entity into the form fields. This method
     * is called internally after a selection of a new payment entity was
     * performed.
     *
     * @return a <code>ResponseBean</code> with the result code.
     * @exception FinderException if the payment for the selected id
     * could not be found in the database.
     */
    public ResponseBean loadPaymentFields() throws FinderException {
	logger.log(BasicLevel.DEBUG, ">");

	if(! isSelectedPayment()) {
	    logger.log(BasicLevel.WARN, "< loadPaymentFields called but there is no current payment");
	    return ResponseBean.getErrNotCurrent(ENTITY_PAYMENT);
	}

	ResponseBean r;

	try {
	    PaymentLocal payment = getCurrentPayment();
	    
	    form.setPaymentNumber(payment.getDocument().getNumber());
	    form.setPaymentDate(payment.getDocument().getDate());
	    form.setPaymentAmount(payment.getAmount());

	    r = new ResponseBean();
	} catch (NamingException e) {
	    logger.log(BasicLevel.ERROR, "Naming service error: " + e.getMessage());
	    logger.log(BasicLevel.DEBUG, e);
	    r = ResponseBean.getErrConfigNaming(e.getMessage());
	}

	logger.log(BasicLevel.DEBUG, "<");
	return r;
    }

    /**
     * Save the values of the form fields into the current payment entity. If
     * no payment entity is selected, a new payment entity is added to the
     * currently selected invoice.
     *
     * @return a <code>ResponseBean</code> with the return code.
     */
    public ResponseBean savePaymentData() {
	ResponseBean r;

	// Current record should be saved, because it might be a new one
	r = saveInvoiceData();
	if (r.getCode() != ResponseBean.CODE_SUCCESS)
	    return r;

	if(! isSelectedInvoice()) {
	    logger.log(BasicLevel.WARN, "savePaymentData called, but not invoice is currently selected.");
	    return ResponseBean.getErrNotCurrent(ENTITY_INVOICE);
	}
	
	logger.log(BasicLevel.DEBUG, ">");

	try {
	    PaymentLocal payment;
	    if(isSelectedPayment()) {
		payment = getCurrentPayment();
	    } else {
		PaymentLocalHome ph = getPaymentHome();
		payment = ph.create();
		payment.setInvoice(getCurrentInvoice());
	    }

	    payment.getDocument().setNumber(form.getPaymentNumber());
	    payment.getDocument().setDate(form.getPaymentDate());
	    payment.setAmount(form.getPaymentAmount().setScale(2, BigDecimal.ROUND_HALF_UP));

	    r = new ResponseBean();
	} catch(NamingException e) {
	    logger.log(BasicLevel.ERROR, "Naming service exception: " + e.getMessage());
	    logger.log(BasicLevel.DEBUG, e);
	    r = ResponseBean.getErrConfigNaming(e.getMessage());
	} catch(FinderException e) {
	    logger.log(BasicLevel.ERROR, "Finder exception: " + e.getMessage());
	    logger.log(BasicLevel.INFO, "The current payment id when finder exception occured is: " + paymentId);
	    logger.log(BasicLevel.INFO, "The current invoice id when finder exception occured is: " + invoiceId);
	    logger.log(BasicLevel.DEBUG, e);

	    r = ResponseBean.getErrNotFound(e.getMessage());
	} catch (CreateException e) {
	    logger.log(BasicLevel.ERROR, "A new payment could not be created: " + e.getMessage());
	    logger.log(BasicLevel.INFO, "The invoice id when create exception occured is: " + invoiceId);
	    logger.log(BasicLevel.DEBUG, e);

	    r = ResponseBean.getErrCreate(ENTITY_PAYMENT);
	} catch (DataLayerException e) {
	    logger.log(BasicLevel.ERROR, "A new payment could not be created: " + e.getMessage());
	    logger.log(BasicLevel.INFO, "The invoice id when create exception occured is: " + invoiceId);
	    logger.log(BasicLevel.DEBUG, e);

	    r = ResponseBean.getErrCreate(ENTITY_PAYMENT);
	}


	logger.log(BasicLevel.DEBUG, "<");
	return r;
    }


    /**
     * Remove the currently selected invoice from the database.
     */
    public ResponseBean removePayment() {
	if(! isSelectedPayment()) {
	    logger.log(BasicLevel.WARN, "removePayment called but no payment is selected");
	    return ResponseBean.getErrNotCurrent(ENTITY_PAYMENT);
	}


	logger.log(BasicLevel.DEBUG, ">");
	ResponseBean r;

	
	try {
	    PaymentLocal pay = getCurrentPayment();
	    pay.remove();
	    
	    r = new ResponseBean();
	} catch (NamingException e) {
	    logger.log(BasicLevel.ERROR, "Naming service error: " + e.getMessage());
	    logger.log(BasicLevel.DEBUG, e);
	    
	    r = ResponseBean.getErrConfigNaming(e.getMessage());
	} catch (FinderException e) {
	    logger.log(BasicLevel.ERROR, "Finder exception: " + e.getMessage());
	    logger.log(BasicLevel.INFO, "When finder exception occured, the payment id was " + paymentId);
	    logger.log(BasicLevel.DEBUG, e);

	    r = ResponseBean.getErrNotFound(e.getMessage());
	} catch (RemoveException e) {
	    logger.log(BasicLevel.ERROR, "Remove exception: " + e.getMessage());
	    logger.log(BasicLevel.INFO, "Failed to remove payment with id " + paymentId);
	    logger.log(BasicLevel.DEBUG, e);

	    r = ResponseBean.getErrRemove(ENTITY_INVOICE);
	} catch (Exception e) {
	    logger.log(BasicLevel.ERROR, "Unexpected eception", e);
	    r = ResponseBean.getErrUnexpected(e);
	}

	return r;
	    
    }

    /**
     * Computes the list of payments for the currently selected invoice. The
     * fields in the listing for each payment are:
     *
     * <ul>
     * <li>payments.id
     * <li>payments.number
     * <li>payments.date
     * <li>payments.amount
     * </ul>
     *
     * @return a <code>ResponseBean</code> containing the list of payment records
     * for the currently selected invoice.
     */
    public ResponseBean loadPayments() {
	if(! isSelectedInvoice()) {
	    logger.log(BasicLevel.WARN, "loadPayemnts called but no invoice is currentlye selected.");
	    return ResponseBean.getErrNotCurrent(ENTITY_INVOICE);
	}

	ResponseBean r;
	logger.log(BasicLevel.DEBUG, ">");
	
	try {
	    Collection payments = getCurrentInvoice().getPayments();

	    r = new ResponseBean();
	    for(Iterator i = payments.iterator(); i.hasNext();) {
		PaymentLocal payment = (PaymentLocal)i.next();

		r.addRecord();
		r.addField("payments.id", payment.getId());
		r.addField("payments.number", payment.getDocument().getNumber());
		r.addField("payments.date", payment.getDocument().getDate());
		r.addField("payments.amount", payment.getAmount());

	    }
	} catch (NamingException e) {
	    logger.log(BasicLevel.ERROR, "Naming service error: " + e.getMessage());
	    logger.log(BasicLevel.DEBUG, e);
	    
	    r = ResponseBean.getErrConfigNaming(e.getMessage());
	} catch (FinderException e) {
	    logger.log(BasicLevel.ERROR, "Finder exception " + e.getMessage());
	    logger.log(BasicLevel.INFO, "Invoice id at finder exception time was: " + invoiceId);
	    logger.log(BasicLevel.DEBUG, e);

	    r = ResponseBean.getErrNotFound(e.getMessage());
	} catch (Exception e) {
	    logger.log(BasicLevel.ERROR, "Unexpected exception", e);

	    r = ResponseBean.getErrUnexpected(e);
	}

	logger.log(BasicLevel.DEBUG, "<");
	return r;
    }



    /**
     * Computes the ordered value by summing up the values of order lines.
     */
    public BigDecimal getOrderedAmount() throws NamingException, 
						FinderException {
	OrderLocal o = getCurrentOrder();
	Collection lines = o.getLines();

	BigDecimal amount = new BigDecimal(0);
	for(Iterator i = lines.iterator(); i.hasNext();) {
	    OrderLineLocal ol = (OrderLineLocal)i.next();
	    
	    amount = amount.add(ol.getQuantity().multiply(ol.getPrice()));
	}

	return amount;
    }

    //////////////////////////////////////////////////////////////////////////
    // Value lists

    /**
     * Add the value lists needed in the form to the response.
     *
     * @param response is the <code>ResponseBean</code> to which the value lists
     * should be added.
     */
    public void loadValueLists(final ResponseBean response) {
	response.addValueList("montaj", ValueLists.makeStdValueList(11200));
	response.addValueList("localitate", ValueLists.makeStdValueList(12005));
	response.addValueList("invoiceRole", ValueLists.makeStdValueList(11250));
    }




    //////////////////////////////////////////////////////////////////////////
    // Support methods



    /**
     * Cache for a reference to <code>OrdersLocalHome</code>.
     */
    OrderLocalHome cache_oh;
    /**
     * Cache for a reference to <code>ClientLocalHome</code>
     */
    ClientLocalHome cache_ch;
    /**
     * Cache for a reference to <code>OrderLineLocalHome</code>.
     */
    OrderLineLocalHome cache_ollh;
    /**
     * Cache for a reference to <code>OfferItemLocalHome</code>.
     */
    OfferItemLocalHome cache_oih;
    /**
     * Cache for a reference to <code>InvoiceLocalHome</code>
     */
    InvoiceLocalHome cache_invh;
    /**
     * Cache for a reference to <code>PaymentLocalHome</code>
     */
    PaymentLocalHome cache_payh;
    
    /**
     * Utility method to get a <code>OrderLocalHome</code> reference.
     *
     * @throws NamingException if the naming server performed an error.
     */
    protected OrderLocalHome getOrderHome() throws NamingException {
	if (cache_oh == null) {
	    try {
		InitialContext ic = new InitialContext();
		Context env = (Context) ic.lookup("java:comp/env");
		cache_oh = (OrderLocalHome) PortableRemoteObject.
		    narrow(env.lookup("ejb/OrderHome"), OrderLocalHome.class);
	    } catch (NamingException e) {
		logger.log(BasicLevel.WARN, "Can not get home for the name ejb/OrderHome: " + e.getMessage());
		logger.log(BasicLevel.DEBUG, e);
		cache_oh = null;
		throw e;
	    }
	}

	return cache_oh;
    }

    /**
     * Utility method to get the pointer to the current <code>OrderLocal</code>
     * object. It looks up the database by using the current id value.
     *
     * @throws FinderException if the value could not be found.
     * @throws NamingException if can not get a reference to the <code>OrderLocalHome</code>
     * due to naming service failure.
     */
    protected OrderLocal getCurrentOrder() throws FinderException, NamingException {
	OrderLocalHome oh = getOrderHome();
	try {
	    OrderLocal o = oh.findByPrimaryKey(id);
	    return o;
	} catch (FinderException e) {
	    logger.log(BasicLevel.DEBUG, "Order id " + id + " not found");
	    FinderException finderE = new FinderException(ENTITY_ORDER);
	    finderE.initCause(e);
	    throw finderE;
	}
    }

    /**
     * Utility method to get the client with the id stored in the form fields.
     */
    protected ClientLocal getFormClient() throws NamingException, FinderException {
	ClientLocalHome ch = getClientHome();
	try {
	    return ch.findByPrimaryKey(form.getClientId());
	} catch (FinderException e) {
	    logger.log(BasicLevel.DEBUG, "Can not find client with id " + form.getClientId());
	    FinderException finderE = new FinderException(ClientsBeanImplementation.ENTITY_CLIENT);
	    finderE.initCause(e);
	    throw finderE;
	}
    }


    /**
     * Utility method to get a reference to the client local home.
     */
    protected ClientLocalHome getClientHome() throws NamingException {
	if(cache_ch == null) {
	    InitialContext ic = new InitialContext();
	    Context env = (Context)ic.lookup("java:comp/env");
	    cache_ch = (ClientLocalHome) PortableRemoteObject.
		narrow(env.lookup("ejb/ClientHome"), ClientLocalHome.class);
	}

	return cache_ch;
    }


    /**
     * Utility method to check if an order is currently selected
     */
    protected boolean isSelectedOrder() {
	return id != null;
    }

    /**
     * Utility method to check if an order line is currently selected.
     */
    protected boolean isSelectedOrderLine() {
	return orderLineId != null;
    }

    /**
     * Utility method to load the current orderline.
     */
    protected OrderLineLocal getCurrentLine() throws NamingException, FinderException {
	OrderLineLocalHome ollh = getOrderLineHome();
	try {
	    OrderLineLocal oll = ollh.findByPrimaryKey(orderLineId);
	    return oll;
	} catch (FinderException e) {
	    logger.log(BasicLevel.DEBUG, "Order line not found for OrderLineId = " + orderLineId);
	    throw new FinderException(ENTITY_ORDER_LINE);
	}
    }

    /**
     * Utility method to get the order line local home from the database.
     */
    protected OrderLineLocalHome getOrderLineHome() throws NamingException {
	if(cache_ollh == null) {
	    InitialContext ic = new InitialContext();
	    Context env = (Context)ic.lookup("java:comp/env");
	    cache_ollh = (OrderLineLocalHome) PortableRemoteObject.
		narrow(env.lookup("ejb/OrderLineHome"), OrderLineLocalHome.class);
	}

	return cache_ollh;
    }

    /**
     * Utility function to retrieve a reference to <code>OfferItemLocalHome</code>.
     */
    public OfferItemLocalHome getOfferItemHome() throws NamingException {
	if(cache_oih == null) {
	    InitialContext ic = new InitialContext();
	    Context env = (Context)ic.lookup("java:comp/env");
	    cache_oih = (OfferItemLocalHome)PortableRemoteObject.narrow
		(env.lookup("ejb/OfferItemHome"), OfferItemLocalHome.class);
	}

	return cache_oih;
    }

    /**
     * Retrieves an offer item from the persistence layer.
     */
    protected OfferItemLocal getOfferItem() 
	throws NamingException, FinderException {
       	
	try {
	    return getOfferItemHome().findByPrimaryKey(form.getOfferItemId());
	} catch (FinderException e) {
	    logger.log(BasicLevel.DEBUG, "Can not find form's OfferItem with id " + form.getOfferItemId());
	    FinderException newe = new FinderException(ArbitraryOfferBizBean.ENTITY_OFFER_ITEM);
	    newe.initCause(e);
	    throw newe;
	}
    }


    /**
     * Utility method to get a reference to the invoice bean home interface.
     */
    protected InvoiceLocalHome getInvoiceHome() throws NamingException {
	if(cache_invh == null) {
	    InitialContext ic = new InitialContext();
	    Context env = (Context)ic.lookup("java:comp/env");
	    cache_invh = (InvoiceLocalHome)PortableRemoteObject.narrow
		(env.lookup("ejb/InvoiceHome"), InvoiceLocalHome.class);
	}
	return cache_invh;
    }

    /**
     * Checks if there is a currently selected invoice.
     */
    protected boolean isSelectedInvoice() {
	return invoiceId != null;
    }

    /**
     * Load the currently selected invoice.
     *
     * @throws FinderException if no invoice is currently selected.
     */
    protected InvoiceLocal getCurrentInvoice() throws NamingException, FinderException {
	InvoiceLocalHome invh = getInvoiceHome();
	try {
	    InvoiceLocal invl = invh.findByPrimaryKey(invoiceId);
	    return invl;
	} catch (FinderException e) {
	    logger.log(BasicLevel.DEBUG, "Invoice not found for id = " + 
		       invoiceId + ". ", e);
	    throw new FinderException(ENTITY_INVOICE);
	}
    }


    /**
     * Utility method to get a reference to the PaymentLocalHome.
     */
    protected PaymentLocalHome getPaymentHome() throws NamingException {
	if(cache_payh == null) {
	    InitialContext ic = new InitialContext();
	    Context env = (Context)ic.lookup("java:comp/env");
	    cache_payh = (PaymentLocalHome)PortableRemoteObject.narrow
		(env.lookup("ejb/PaymentHome"), PaymentLocalHome.class);
	}

	return cache_payh;
    }

    /**
     * Convenience method that checks if there is a currentlu selected payment.
     */
    protected boolean isSelectedPayment() {
	return paymentId != null;
    }

    /**
     * Utility method to get the currently selected payment.
     * 
     * @throws FinderException if there is no currently selected payment.
     */
    protected PaymentLocal getCurrentPayment() throws NamingException, FinderException {
	PaymentLocalHome ph = getPaymentHome();
	try {
	    PaymentLocal p = ph.findByPrimaryKey(paymentId);
	    return p;
	} catch (FinderException e) {
	    logger.log(BasicLevel.DEBUG, "Can not find Payment id " + id +
		       ". ", e);
	    FinderException finderE = new FinderException(ENTITY_PAYMENT);
	    finderE.initCause(e);
	    throw finderE;
	}
    }




    /**
     * Initialize the cache variables here.
     *
     * @param sessionContext a <code>SessionContext</code> value
     */
    public void setSessionContext(final SessionContext sessionContext) {
	super.setSessionContext(sessionContext);
	
	cache_oh = null;
	cache_ch = null;
	cache_ollh = null;
	cache_oih = null;
	cache_invh = null;
	cache_payh = null;
    }


}
