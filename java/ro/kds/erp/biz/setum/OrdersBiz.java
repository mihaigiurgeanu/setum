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
     * The maximum number of records to be returned in one request
     * for getting the listing of orders or lines.
     */
    static final int LISTING_ROWS_PER_REQUEST = 30;


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

	    r = new ResponseBean();
	} catch (NamingException e) {
	    r = ResponseBean.getErrConfigNaming(e.getMessage());

	    logger.log(BasicLevel.ERROR, "Order data could not be loaded from database");
	    logger.log(BasicLevel.DEBUG, e);
	} catch (FinderException e) {
	    logger.log(BasicLevel.DEBUG, "Finder exceptin occured");
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
	    
	    o.setDiscount(form.getDiscount());
	    o.setAdvancePayment(form.getAvans());
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
	    ol.setPrice(form.getPrice());
	    ol.setQuantity(form.getQuantity());
	    
	    r = new ResponseBean();
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
	    r.addField("orders.client",		o.getClient().getName());
	    r.addField("orders.localitate",	o.getDeliveryLocation());
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



    //////////////////////////////////////////////////////////////////////////
    // Value lists

    /**
     * Add the value lists needed in the form to the response.
     *
     * @param response is the <code>ResponseBean</code> to which the value lists
     * should be added.
     */
    public final void loadValueLists(final ResponseBean response) {
	response.addValueList("montaj", ValueLists.makeStdValueList(11200));
	response.addValueList("localitate", ValueLists.makeStdValueList(12005));
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
    }

}
