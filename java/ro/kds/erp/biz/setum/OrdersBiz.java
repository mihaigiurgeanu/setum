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
import ro.kds.erp.data.AttributeLocal;
import ro.kds.erp.data.OfferLocal;
import ro.kds.erp.biz.CommonServicesLocal;
import ro.kds.erp.biz.CommonServicesLocalHome;
import ro.kds.erp.biz.ServiceNotAvailable;
import ro.kds.erp.data.ProductLocalHome;
import ro.kds.erp.data.CategoryLocal;
import ro.kds.erp.biz.CategoryManagerLocalHome;
import ro.kds.erp.biz.CategoryManagerLocal;
import ro.kds.erp.biz.ProductNotAvailable;
import ro.kds.erp.biz.setum.basic.OrdersHome;
import ro.kds.erp.biz.setum.basic.Orders;
import java.rmi.RemoteException;
import org.apache.commons.lang.time.DateUtils;
import java.util.Collections;
import org.apache.commons.lang.StringUtils;
import java.util.Comparator;
import java.text.DateFormat;
import java.math.RoundingMode;
import ro.kds.erp.biz.Products;
import ro.kds.erp.data.ProformaLocalHome;
import ro.kds.erp.data.ProformaLocal;
import java.util.Date;
import ro.kds.erp.biz.ResponseBean;
import ro.kds.erp.data.DailySummaryLocalHome;
import ro.kds.erp.data.DailySummaryLocal;

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
     * The name of the Proforma entity.
     */
    public final String ENTITY_PROFORMA = "Proforma";
    
    /**
     * The maximum number of records to be returned in one request
     * for getting the listing of orders or lines.
     */
    static final int LISTING_ROWS_PER_REQUEST = 30;

    /**
     * The category id that holdes the configuration product km (defining price per km).
     */
    public final Integer CATEGORY_ID_SHIPPING = new Integer(12006);

    /**
     * The product for km (configuration product - price per km).
     */
    public final String PRODUCT_CODE_KM = "10";

    /**
     * The default proforma comment.
     */
    public final String defaultProformaComment = "NOTA: Acest document nu este o factura fiscala";

    /**
     * Numele DailySummary pentru raportul zilnic de incasari.
     */
    public final String RAPORT_INCASARI_SUMMARY = "http://www.kds.ro/setum/DailySummary/incasari";

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
	    form.setCurrencyCode(prefs.get("default.currecy", "RON"));

	} catch (Exception e) {
	    logger.log(BasicLevel.ERROR, "Exception when geting preferences: ", e);
	    defaultDelivery = 30;
	    form.setTvaPercent(new Double(19.0));
	    form.setCurrencyCode("RON");
	}
	
	form.setExchangeRate(new BigDecimal(1));

	form.setDate(new Date());

	Date delivDate = DateUtils.addDays(DateUtils.truncate(form.getDate(), Calendar.DAY_OF_MONTH), 
					   defaultDelivery);
	form.setTermenLivrare(delivDate);
	form.setTermenLivrare1(delivDate);
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
	    form.setDeliveryHour(o.getDeliveryHour());
	    form.setTipDemontare(o.getTipDemontare());
	    form.setAttribute1(o.getAttribute1());
	    form.setAttribute2(o.getAttribute2());
	    form.setAttribute3(o.getAttribute3());
	    form.setAttribute4(o.getAttribute4());
	    form.setAttribute5(o.getAttribute5());

	    //form.setTotal(getOrderedAmount());
	    computeOrderAmounts();

	    form.setPayedAmount(o.getPayedAmount());
	    form.setInvoicedAmount(o.getInvoicedAmount());
	    form.setCurrencyPayedAmount(o.getCurrencyPayedAmount());
	    form.setCurrencyInvoicedAmount(o.getCurrencyInvoicedAmount());

	    try {
		Preferences prefs = PreferencesBean.getPreferences();
		form.setCurrencyCode(prefs.get("default.currecy", "RON"));
	    } catch (Exception e) {
		logger.log(BasicLevel.ERROR, "Exception when geting preferences: ", e);
		form.setCurrencyCode("RON");
	    }

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
		    orderNo = new Integer(0);
		    logger.log(BasicLevel.WARN, "Can not get a number for order", e);
		}


		form.setNumber(orderNo.toString());
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
	    o.setDeliveryHour(form.getDeliveryHour());
	    o.setTipDemontare(form.getTipDemontare());
	    o.setAttribute1(form.getAttribute1());
	    o.setAttribute2(form.getAttribute2());
	    o.setAttribute3(form.getAttribute3());
	    o.setAttribute4(form.getAttribute4());
	    o.setAttribute5(form.getAttribute5());


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
	    ol.setPrice(form.getPrice());
	    ol.setQuantity(form.getQuantity());
	    
	    try {
		//form.setTotal(getOrderedAmount());
		computeOrderAmounts();
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

		// avoiding bug when quantity was not set in the offer
		if(oi.getQuantity() == null) {
		    oi.setQuantity(new BigDecimal(1));
		}
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
	logger.log(BasicLevel.DEBUG, "Enter removeItem");
	if(! isSelectedOrderLine() ) {
	    return ResponseBean.getErrNotCurrent(ENTITY_ORDER_LINE);
	}
	
	ResponseBean r;
	try {
	    OrderLineLocal ol = getCurrentLine();
	    logger.log(BasicLevel.DEBUG, "Call ol.remove");
	    ol.remove();
	    logger.log(BasicLevel.DEBUG, "Line removed");
	    r = new ResponseBean();
	    try {
		OrderLocal o = getCurrentOrder();
		//form.setTotal(getOrderedAmount());
		computeOrderAmounts();
		r = computeCalculatedFields(null);
		r.addField("total", form.getTotal());
	    } catch (Exception e) {
		logger.log(BasicLevel.ERROR, "Can not get the ordered amount: " + e.getMessage());
		logger.log(BasicLevel.DEBUG, e);
		form.setTotal(new BigDecimal(0));
	    }
	    
	    logger.log(BasicLevel.DEBUG, "removeItem completed.");
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

	logger.log(BasicLevel.DEBUG, "Exit removeItem");
	return r;
    }


    /**
     * Get the list of orders. It only returns a part of the cached list of
     * orders starting with <code>startRow</code>, so the list can be loaded
     * bit by bit as the user scrolls into the list control.
     *
     * The cached list of orders is initialized by calling <code>getOrdersCount</code>
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

    public ResponseBean loadFullListing() {
	ResponseBean r;
	r = new ResponseBean();

	try {
	    OrderLocalHome oh = getOrderHome();
	    Collection rows = oh.findAll();

	    for(Iterator i = rows.iterator(); i.hasNext();) {

		OrderLocal o = (OrderLocal)i.next();
		r.addRecord();

		r.addField("orders.id",		o.getId());
		r.addField("orders.no",		o.getDocument().getNumber());
		r.addField("orders.date",	o.getDocument().getDate());
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
		r.addField("orders.distanta",		o.getDeliveryDistance());
		r.addField("orders.avans",		o.getAdvancePayment());
		r.addField("orders.termenLivrare",	o.getDeliveryTerm());
	    } 
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
	logger.log(BasicLevel.DEBUG, ">");

	ResponseBean r;

	try {
	    OrderLocalHome oh = getOrderHome();
	    logger.log(BasicLevel.DEBUG, "Storing records in cache");
	    listingCache = new ArrayList(oh.findAll());
	    
	    logger.log(BasicLevel.DEBUG, "Sorting orders in cache");
	    Collections.sort(listingCache, new Comparator() {
		    public int compare(Object o1, Object o2) {
			logger.log(BasicLevel.DEBUG, "Comparing 2 orders: " + o1 + " and " + o2);
			try {
			    OrderLocal order1 = (OrderLocal)o1;
			    OrderLocal order2 = (OrderLocal)o2;
			    logger.log(BasicLevel.DEBUG, "Transform order numbers to strings");
			    String s1 = StringUtils.leftPad(order1.getDocument().getNumber(), 6, '0');
			    logger.log(BasicLevel.DEBUG, "O1 number: " + s1);
			    String s2 = StringUtils.leftPad(order2.getDocument().getNumber(), 6, '0');
			    logger.log(BasicLevel.DEBUG, "O2 number: " + s2);

			    return -(s1.compareTo(s2));
			} catch (Exception e) {
			    logger.log(BasicLevel.WARN, "Eroare la sortare comenzi: " + e);
			    logger.log(BasicLevel.DEBUG, "Detalii eroare la soratarea comenzii", e);
			    return -1;
			}
		    }
		});

	    logger.log(BasicLevel.DEBUG, "Creating the response");
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

	logger.log(BasicLevel.DEBUG, "<");
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
     * <li>orderItems.valoareMontaj
     * <li>orderItems.quantity
     * <li>orderItems.price
     * </ul>
     */
    public ResponseBean loadLines() {
	ResponseBean r;

	if(isSelectedOrder()) {
	    try {

		OrderLocal o = getCurrentOrder();
		Collection lines = o.getLines();

		double pretKm = getPretKm();

		r = new ResponseBean();
		Iterator firstLine = lines.iterator();
		int lineNo = 0;
		for(Iterator i = lines.iterator(); i.hasNext();) {
		    OrderLineLocal ol = (OrderLineLocal)i.next();
		    lineNo += 1;
		    try {
			OfferItemLocal oi = ol.getOfferItem();
			r.addRecord();
			r.addField("orderItems.id", ol.getId());
			r.addField("orderItems.offerNo", oi.getOffer().getDocument().getNumber());
			r.addField("orderItems.offerDate", ol.getOfferItem().getOffer().getDocument().getDate());
			r.addField("orderItems.productName", ol.getOfferItem().getProduct().getName() + "/" + lineNo);
			r.addField("orderItems.productCode", ol.getOfferItem().getProduct().getCode() + "/" + lineNo);
			r.addField("orderItems.valoareMontaj", 
				   (computePretMontaj(oi.getMontajId()) +
				    oi.getMontajProcent().doubleValue() *
				    ol.getPrice().doubleValue()/100) *
				   ol.getQuantity().doubleValue());
			 r.addField("orderItems.valoareTransport",
				    pretKm *
				    oi.getDistance().doubleValue() *
				    oi.getDeliveries().doubleValue());

			// avoiding bug when quantity was not set (offer did not have the quantity field)
			if(ol.getQuantity() == null) {
			    ol.setQuantity(new BigDecimal(1));
			}
			r.addField("orderItems.quantity", ol.getQuantity());
			r.addField("orderItems.price", ol.getPrice());
		    } catch (Exception e) {
			logger.log(BasicLevel.WARN, "Unexpected exception when loading order line data: " + e);
			logger.log(BasicLevel.DEBUG, e);
		    }
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
	}
	else {
	    // no order selected (maybe new order)
	    r = new ResponseBean();
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
	form.setInvoiceExchangeRate(new Double(0)); // las script-ul tcl (calculatedFields) sa calculeze default

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
	    form.setInvoiceExchangeRate(inv.getExchangeRate());
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
	    inv.setExchangeRate(form.getInvoiceExchangeRate());

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
		r.addField("invoices.exchangeRate", inv.getExchangeRate());
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
	form.setPaymentExchangeRate(new Double(0)); // scriptul calculatedFields va seta o valoare default

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
	    form.setPaymentExchangeRate(payment.getExchangeRate());

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
	    payment.setExchangeRate(form.getPaymentExchangeRate());

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
		r.addField("payments.exchangeRate", payment.getExchangeRate());

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

    /**
     * Sum product values, transport amounts and installation amounts for the
     * order items. Stores the results in form fields.
     */
    public void computeOrderAmounts() throws NamingException, 
					     FinderException {
	OrderLocal o = getCurrentOrder();
	Collection lines = o.getLines();

	double amount = 0;
	double iamount = 0;
	double tamount = 0;

	double pretKm; //pretul pe km
	pretKm = getPretKm();

	logger.log(BasicLevel.DEBUG, "Pret km=" + pretKm);

	for(Iterator i = lines.iterator(); i.hasNext();) {
	    OrderLineLocal ol = (OrderLineLocal)i.next();
	    double valProduse = ol.getQuantity().doubleValue() * ol.getPrice().doubleValue();
	    logger.log(BasicLevel.DEBUG, "Valoare produse din linie=" + valProduse);
	    amount += valProduse;
	    
	    logger.log(BasicLevel.DEBUG, "Get offer line.");
	    OfferItemLocal offerLine = ol.getOfferItem();
	    if(offerLine == null) {
		logger.log(BasicLevel.WARN, "OrderLine has a null offer item attached!");
	    } else {
		// montaj
		logger.log(BasicLevel.DEBUG, "Get montajId");
		Integer montajId = offerLine.getMontajId();
		if(montajId == null) {
		    logger.log(BasicLevel.DEBUG, "MontajId is null -> set to 0");
		    montajId = new Integer(0);
		}

		logger.log(BasicLevel.DEBUG, "srv is not null");
		double pretMontaj;
		if(montajId.intValue() != 0) {
		    logger.log(BasicLevel.DEBUG, "try to get value of montaj (montajId = " + montajId.intValue() + ")");
		    pretMontaj = computePretMontaj(montajId);
		} else {
		    logger.log(BasicLevel.DEBUG, "montajId is 0. set pretMontaj to 0");
		    pretMontaj = 0;
		}
		logger.log(BasicLevel.DEBUG, "Pret montaj=" + pretMontaj);
		double valoareMontaj;
		if(offerLine.getMontajProcent() != null) {
		    logger.log(BasicLevel.DEBUG, "MontajProcent is not null");
		    valoareMontaj = pretMontaj * ol.getQuantity().doubleValue() + offerLine.getMontajProcent().doubleValue() * valProduse/100;
		} else {
		    logger.log(BasicLevel.DEBUG, "MontajProcent is null");
		    valoareMontaj = pretMontaj * form.getQuantity().doubleValue();
		}
		logger.log(BasicLevel.DEBUG, "Valoare montaj=" + valoareMontaj);
		iamount += valoareMontaj;
		
		logger.log(BasicLevel.DEBUG, "Caluclez transport");
		// transport
		double distance = 0;
		int deliveries = 0;
		logger.log(BasicLevel.DEBUG, "getDistance");
		if(offerLine.getDistance() != null) {
		    logger.log(BasicLevel.DEBUG, "offerLine.getDistance is null");
		    distance = offerLine.getDistance().doubleValue();
		}
		logger.log(BasicLevel.DEBUG, "getDeliveries");
		if(offerLine.getDeliveries() != null) {
		    logger.log(BasicLevel.DEBUG, "getDeliveries is null");
		    deliveries = offerLine.getDeliveries().intValue();
		}
		double valoareTransport = distance * deliveries * pretKm;
		logger.log(BasicLevel.DEBUG, "Valoare transport=" + valoareTransport);
		tamount += valoareTransport;
	    }
	}
	logger.log(BasicLevel.DEBUG, "Cumulated order amount: " + amount);
	logger.log(BasicLevel.DEBUG, "Cumulated installation amount: " + iamount);
	logger.log(BasicLevel.DEBUG, "Cumulated shipping amount: " + tamount);

	form.setValoareProduse(new BigDecimal(amount));
	form.setValoareTransport(new BigDecimal(tamount));
	form.setValoareMontaj(new BigDecimal(iamount));
	form.setTotal(new BigDecimal(amount + tamount + iamount));

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
	response.addValueList("proformaRole", ValueLists.makeStdValueList(11250));
	response.addValueList("tipDemontare", ValueLists.makeStdValueList(11205));
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
     * Cache for a reference to <code>ProformaLocalHome</code>
     */
    ProformaLocalHome cache_prfh;
    /**
     * Cache for a reference to <code>InvoiceLocalHome</code>
     */
    InvoiceLocalHome cache_invh;
    /**
     * Cache for a reference to <code>PaymentLocalHome</code>
     */
    PaymentLocalHome cache_payh;
    /**
     * Cache for a reference to <code>DailySummaryLocalHome</code>
     */
    DailySummaryLocalHome cache_dsh;

    
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
     * Retrieves the offer item corresponding to the current order line
     * from the persistence layer.
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
     * Gets the offer entity of the current order line.
     */
    protected OfferLocal getOffer() throws NamingException, FinderException {
	return getOfferItem().getOffer();
    }

    /**
     * Utility method to get a reference to the proforma bean home interface.
     */
    protected ProformaLocalHome getProformaHome() throws NamingException {
	if(cache_prfh == null) {
	    InitialContext ic = new InitialContext();
	    Context env = (Context)ic.lookup("java:comp/env");
	    cache_prfh = (ProformaLocalHome)PortableRemoteObject.narrow
		(env.lookup("ejb/ProformaHome"), ProformaLocalHome.class);
	}
	return cache_prfh;
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
     * Checks if there is a currently selected proforma.
     */
    protected boolean isSelectedProforma() {
	return proformaId != null;
    }

    /**
     * Checks if there is a currently selected invoice.
     */
    protected boolean isSelectedInvoice() {
	return invoiceId != null;
    }

    /**
     * Load the currently selected proforma.
     *
     * @throws FinderException if no proforma is currently selected.
     */
    protected ProformaLocal getCurrentProforma() throws NamingException, FinderException {
	ProformaLocalHome prfh = getProformaHome();
	try {
	    ProformaLocal prfl = prfh.findByPrimaryKey(proformaId);
	    return prfl;
	} catch (FinderException e) {
	    logger.log(BasicLevel.DEBUG, "Proforma not found for id = " + 
		       proformaId + ". ", e);
	    throw new FinderException(ENTITY_PROFORMA);
	}
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





    /* Reporting methods */

    /**
     * Builds a <code>ResponseBean</code> containing the data for the order report. It will contain a 
     * single record describing the order (roughly the fields of <code>OrderEJB</code>). The record
     * will have the field <code>lines</code> that will be a <code>ResponseBean</code> with a record for
     * each item. Each of the records of the <code>order_items</code> fields will contain a <code>product</code>
     * of type <code>ResponseBean</code> describing the product. Products may contain as well other 
     * <code>ResponseBean</code> objects and so on.
     *
     * An order should be selected before calling this method.
     *
     * The xml structure of the response would be something like:
     * <code>
     * response
     * +- record
     *    +- field(name=...) = value
     *    +- field(name=...) = value
     *    ...
     *    +- field(name=...) = value
     *    +- field(name=lines)
     *       +- record
     *          +- field(name=...) = value // valorile liniei
     *          ....
     *          +- field(name=...) = value
     *          +- field(name=product)
     *             +- record
     *                +- field(name=...) = value // valorile atributelor produsului
     *                ...
     *                +- field(name=...)
     *             +- value-list(name=...)
     *               +- vl-item
     *                  +- value = value
     *                  +- label = label
     *             ...
     *
     * </code>
     *
     * @return a rather complex <code>ResponseBean</code> describing the offer, the items of the offer and the products.
     * If an error occurs, the returned <code>ResponseBean</code> will contain the error code and description.
     */
    public ResponseBean orderReport() {

	ResponseBean r;

	if (id == null) {
	    logger.log(BasicLevel.DEBUG, "orderReport: No current order");
	    r = ResponseBean.getErrNotCurrent("Order");
	}
	else {
	    r = new ResponseBean();
	    r.addRecord();

	    DateFormat dfmt = DateFormat.getDateInstance();

	    r.addField("number", form.getNumber());
	    r.addField("date", dfmt.format(form.getDate()));
	    r.addField("clientId", form.getClientId());
	    r.addField("clientName", form.getClientName());
	    r.addField("montaj", form.getMontaj());
	    if(form.getLocalitate() == 0) {
		r.addField("localitate", form.getLocalitateAlta());
		r.addField("distanta", form.getDistanta());
	    } else 
		try {
		    ProductLocal localitateProduct = ValueLists.getValueByCode(12005, form.getLocalitate().toString());
		    r.addField("localitate", localitateProduct.getName());
		    Map amap = localitateProduct.getAttributesMap();
		    AttributeLocal dist = (AttributeLocal)amap.get("distanta");
		    if(dist != null) 
			r.addField("distanta", dist.getDecimalValue());
		    else
			r.addField("distanta", form.getDistanta());
		} catch (Exception e) {
		    r.addField("localitate", "-");
		    logger.log(BasicLevel.WARN, "Can not read the value of orders.localitate for code " 
			       + form.getLocalitate());
		    logger.log(BasicLevel.DEBUG, e);
		}

	    r.addField("localitateAlta", form.getLocalitateAlta());
	    r.addField("observatii", form.getObservatii());
	    r.addField("valoareMontaj", form.getValoareMontaj().setScale(2, RoundingMode.HALF_UP));
	    r.addField("valoareTransport", form.getValoareTransport());
	    r.addField("valoareProduse", form.getValoareProduse().setScale(2, RoundingMode.HALF_UP));
	    r.addField("total", form.getTotal().setScale(2, RoundingMode.HALF_UP)); // valoareMontaj + valoareTransport + valoareProduse
	    r.addField("tvaPercent", form.getTvaPercent());
	    r.addField("totalTva", form.getTotalTva().setScale(2, RoundingMode.HALF_UP));
	    r.addField("discount", form.getDiscount().setScale(2, RoundingMode.HALF_UP));
	    r.addField("totalFinal", form.getTotalFinal().setScale(2, RoundingMode.HALF_UP));
	    r.addField("totalFinalTva", form.getTotalFinalTva().setScale(2, RoundingMode.HALF_UP));
	    r.addField("avans", form.getAvans().setScale(2, RoundingMode.HALF_UP));
	    r.addField("achitatCu", form.getAchitatCu());
	    r.addField("valoareAvans", form.getValoareAvans().setScale(2, RoundingMode.HALF_UP));
	    r.addField("payedAmount", form.getPayedAmount().setScale(2, RoundingMode.HALF_UP));
	    r.addField("invoicedAmount", form.getInvoicedAmount().setScale(2, RoundingMode.HALF_UP));
	    r.addField("currencyInvoicedAmount", form.getCurrencyInvoicedAmount());
	    r.addField("currencyPayedAmount", form.getCurrencyPayedAmount());
	    r.addField("diferenta", form.getDiferenta().setScale(2, RoundingMode.HALF_UP));
	    r.addField("currencyDiferenta", form.getCurrencyDiferenta().setScale(2, RoundingMode.HALF_UP));
	    r.addField("termenLivarare", dfmt.format(form.getTermenLivrare()));
	    r.addField("termenLivrare1", dfmt.format(form.getTermenLivrare1()));
	    r.addField("adresaMontaj", form.getAdresaMontaj());
	    r.addField("adresaReper", form.getAdresaReper());
	    r.addField("telefon", form.getTelefon());
	    r.addField("contact", form.getContact());
	    r.addField("deliveryHour", form.getDeliveryHour());
	    r.addField("tipDemontare", form.getTipDemontare());
	    r.addField("attribute1", form.getAttribute1());
	    r.addField("attribute2", form.getAttribute2());
	    r.addField("attribute3", form.getAttribute3());
	    r.addField("attribute4", form.getAttribute4());
	    r.addField("attribute5", form.getAttribute5());

	    ClientUtils clientUtils;
	    try {
		clientUtils = new ClientUtils(getFormClient());
	    } catch (Exception e) {
		logger.log(BasicLevel.WARN, "Client is not available for report!");
		clientUtils = new ClientUtils(null);
	    }
	    clientUtils.populateResponse(r);

	    r.addField("lines", linesReport());
	    r.addField("invoices", invoicesReport());

	    r.addField("agrementTehnic", Utils.getAgrementTehnic(factory));
	    r.addValueList("tipDemontare", ValueLists.makeStdValueList(11205));

	    
	}

	return r;
    }


    /**
     * Builds a <code>ResponseBean</code> that has all the
     * line items of the current order and for each line item
     * the description of the product as another <code>ResponseBean</code>
     * field (with attribute name product).
     */
    private ResponseBean linesReport() {

	ResponseBean r;
	try {
	    OrderLocal order = getCurrentOrder();
	    double pretKm = getPretKm();
	    if(order != null) {
		
		Collection orderItems = order.getLines();
		r = new ResponseBean();
		for(Iterator i = orderItems.iterator(); i.hasNext();) {
		    OrderLineLocal item = (OrderLineLocal)i.next();
		    try {
			loadOrderLineData(item.getId());
			r.addRecord();

			r.addField("productName", form.getProductName());
			r.addField("productCode", form.getProductCode());
			r.addField("price", form.getPrice());
			r.addField("productPrice", form.getProductPrice());
			r.addField("priceRatio", form.getPriceRatio());
			r.addField("quantity", form.getQuantity());
			r.addField("value", form.getValue().setScale(2, RoundingMode.HALF_UP));
			r.addField("tax", form.getTax().setScale(2, RoundingMode.HALF_UP));
			/*
			r.addField("codMontaj", form.getCodMontaj());
			r.addField("montajProcent", form.getMontajProcent());
			r.addField("montajSeparat", form.getMontajSeparat());
			*/
			try {
			    OfferItemLocal offerItem = getOfferItem();
			    r.addField("obiectiv", offerItem.getOffer().getDescription());
			    r.addField("montajId", offerItem.getMontajId());
			    if(offerItem.getMontajId() == null || offerItem.getMontajId().intValue() == 0)
				r.addField("montajCode", 0);
			    else
				r.addField("montajCode", Products.getProductHome().findByPrimaryKey(offerItem.getMontajId()).getCode());
			    r.addField("montajProcent", offerItem.getMontajProcent());
			    r.addField("montajSeparat", offerItem.getMontajSeparat());
			    r.addField("locationId", offerItem.getLocationId());
			    r.addField("otherLocation", offerItem.getOtherLocation());
			    r.addField("distance", offerItem.getDistance());
			    r.addField("deliveries", offerItem.getDeliveries());
			    double transport = pretKm *
				offerItem.getDistance().doubleValue() *
				offerItem.getDeliveries().doubleValue();
			    double montaj = computePretMontaj(offerItem.getMontajId()) +
				offerItem.getMontajProcent().doubleValue() *
				form.getPrice().doubleValue()/100;
			    r.addField("montajUnitar", new BigDecimal(montaj).setScale(2, RoundingMode.HALF_UP));
			    r.addField("valoareMontaj", new BigDecimal(montaj * form.getQuantity().doubleValue()).setScale(2, RoundingMode.HALF_UP));
			    r.addField("valoareTransport", new BigDecimal(transport).setScale(2, RoundingMode.HALF_UP));
			    double lineValue = form.getValue().doubleValue() + montaj * form.getQuantity().doubleValue() + transport;
			    r.addField("lineValue", new BigDecimal(lineValue).setScale(2, RoundingMode.HALF_UP));
			    double lineTax = lineValue * 0.19;
			    r.addField("lineTax", new BigDecimal(lineTax).setScale(2, RoundingMode.HALF_UP));
			    r.addField("lineTotal", new BigDecimal(lineValue).setScale(2, RoundingMode.HALF_UP).doubleValue() + new BigDecimal(lineTax).setScale(2, RoundingMode.HALF_UP).doubleValue());
			    
			    OfferLocal offer = offerItem.getOffer();
			    r.addField("contract", offer.getContract());
			} catch(NamingException e) {
			    logger.log(BasicLevel.ERROR, "Naming excetpion for offerItem: " + e.getMessage());
			    logger.log(BasicLevel.DEBUG, e);
			} catch(FinderException e) {
			    logger.log(BasicLevel.ERROR, "Finder excetpion for offerItem: " + e.getMessage());
			    logger.log(BasicLevel.DEBUG, e);
			}
			
			r.addField("product", productReport());
		    } catch (FinderException e) {
			logger.log(BasicLevel.WARN, "Order line not found for OrderLineId " + item.getId());
			logger.log(BasicLevel.DEBUG, e);
		    }
		}
	    } else {
		r = ResponseBean.getErrNotCurrent("Order");
		logger.log(BasicLevel.ERROR, "There is no current order (no order selected).");
	    }
	} catch (FinderException e){
	    logger.log(BasicLevel.ERROR, "Call to getCurrentOrder did not find data in the database corresponding to current record!");
	    logger.log(BasicLevel.DEBUG, e);
	    r = ResponseBean.getErrNotFound("Order/" + id);
	} catch (NamingException e) {
	    logger.log(BasicLevel.ERROR, "Naming exception: " + e.getMessage());
	    logger.log(BasicLevel.DEBUG, e);
	    r = ResponseBean.getErrConfigNaming(e.getMessage());
	}

	r.addValueList("montajId", ValueLists.makeVLForCategoryId(11200));
	r.addValueList("locationId", ValueLists.makeVLForCategoryId(12005));

	return r;


    }

    private ResponseBean invoicesReport() {
	ResponseBean r;
	try {
	    Collection invoices = getCurrentOrder().getInvoices();
	    
	    r = new ResponseBean();
	    for(Iterator i = invoices.iterator(); i.hasNext();) {
		InvoiceLocal inv = (InvoiceLocal)i.next();
		
		r.addRecord();
		r.addField("id", inv.getId());
		r.addField("number", inv.getDocument().getNumber());
		r.addField("date", inv.getDocument().getDate());
		r.addField("role", inv.getRole());
		r.addField("amount", inv.getAmount());
		r.addField("tax", inv.getTax());
		r.addField("totalAmount", inv.getAmount().doubleValue() + inv.getTax().doubleValue());
		r.addField("payedAmount", inv.getSumOfPayments());

		r.addField("payments", paymentsReport(inv));
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

	r.addValueList("role", ValueLists.makeStdValueList(11250));

	return r;
    }

    private ResponseBean paymentsReport(InvoiceLocal invoice) {
	ResponseBean r;
	Collection payments = invoice.getPayments();
	
	r = new ResponseBean();
	for(Iterator i = payments.iterator(); i.hasNext();) {
	    PaymentLocal payment = (PaymentLocal)i.next();
	    
	    r.addRecord();
	    r.addField("id", payment.getId());
	    r.addField("number", payment.getDocument().getNumber());
	    r.addField("date", payment.getDocument().getDate());
	    r.addField("amount", payment.getAmount());
	    
	}

	return r;
    }

    /**
     * Build a <code>ResponseBean</code> with the fields of the product in the current offer line. 
     * The response
     * may contain other <code>ResponseBean</code> objects for other products that are
     * contained by the main product.
     *
     * <code>
     * ...
     *   +- field(name=product)
     *      +- record
     *         +- field(name=...) = value
     *         ...
     *         +- field
     *      +- value-list
     *      ....
     *      +- value-list
     * </code>
     */
    private ResponseBean productReport() {
	ResponseBean r;

	try {
	    OfferItemLocal oi = getOfferItem();


	    ProductLocal p = oi.getProduct();
	    CategoryLocal c = p.getCategory();
	    

	    InitialContext ic = new InitialContext();
	    Context env = (Context) ic.lookup("java:comp/env");
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

	    r = cm.getProductReport(p.getId());
	} catch (NamingException e) {
	    r = ResponseBean.getErrConfigNaming(e.getMessage());
	    logger.log(BasicLevel.ERROR, "Name configuration error: " + e.getMessage());
	    logger.log(BasicLevel.WARN, "Check the category manager for jndi name ejb/CategoryManagerHome/default");
	    logger.log(BasicLevel.DEBUG, e);
	} catch (CreateException e) {
	    r = ResponseBean.getErrCreate(e.getMessage());
	    logger.log(BasicLevel.ERROR,
		       "CreateException: CategoryManager could not be instantiated: " + e.getMessage());
	    logger.log(BasicLevel.DEBUG, e);
	}
	catch (FinderException e) {
	    r = ResponseBean.getErrNotFound(e.getMessage());
	    logger.log(BasicLevel.ERROR, "FinderException: The offer for current order line could not be found: " + e.getMessage());
	    logger.log(BasicLevel.DEBUG, e);
	}
	return r;
    }


    protected double computePretMontaj (Integer montajId) {
	double pretMontaj;
	if (montajId != null) {
	    try {
		CommonServicesLocal srv = (CommonServicesLocal)factory
		    .local("ejb/CommonServices", CommonServicesLocalHome.class);
		
		ProductLocal montaj = (ProductLocal)srv.findProductById(montajId);
		if(montaj.getPrice1() != null) {
		    logger.log(BasicLevel.DEBUG, "montaj.getPrice1 is not null");
		    pretMontaj = montaj.getPrice1().doubleValue();
		} else {
		    logger.log(BasicLevel.DEBUG, "montaj.getPrice1 is null");
		    pretMontaj = 0;
		}
	    } 
	    catch (ProductNotAvailable e) {
		logger.log(BasicLevel.WARN, "Tipul de montaj nu este definit in baza de date: montajId = " + montajId);
		logger.log(BasicLevel.DEBUG, e);
		pretMontaj = 0;
	    }
	    catch (ServiceNotAvailable e) {
		logger.log(BasicLevel.ERROR, "CommonServicesLocal not available.");
		logger.log(BasicLevel.ERROR, e);
		pretMontaj = 0;
	    }
	} 
	else { // montajId == null
	    pretMontaj = 0;
	}
	return pretMontaj;
    }
    
    protected double getPretKm() {
	double pretKm;
	try {
	    CommonServicesLocal srv = (CommonServicesLocal)factory
		.local("ejb/CommonServices", CommonServicesLocalHome.class);
	    
	    try {
		ProductLocal km = srv.findProductByCode(CATEGORY_ID_SHIPPING, PRODUCT_CODE_KM);
		pretKm = km.getPrice1().doubleValue();
	    } catch(ProductNotAvailable e) {
		logger.log(BasicLevel.ERROR, "Eroare de configurare: Nu este definit pretul pe km: "
			   + "Category Id=" + CATEGORY_ID_SHIPPING
			   + "/Product Code=" + PRODUCT_CODE_KM);
		logger.log(BasicLevel.DEBUG, e);
		pretKm = 0;
	    }
	} catch (ServiceNotAvailable e) {
	    logger.log(BasicLevel.ERROR, "Service ejb/CommonServices not available. Application setup error. Please check if the ejb/CommonServices local bean has been configured with ServiceFactory bean");
	    logger.log(BasicLevel.DEBUG, e);
	    
	    
	    pretKm = 0;
	}
	return pretKm;
    }


    /**
     * Initializarea raportului de livrari.
     *
     * @param cuMontaj specifica daca raportul va contine
     * usile cu montaj sau fara. Poate avea una din trei valori:
     * <dl>
     * <dt>yes <dd>Raportul arata usile cu montaj.
     * <dt>no  <dd>Raportul arata usile fara montaj.
     * <dt>all <dd>Apar si usi cu montaj si usi fara montaj.
     * </dl>
     *
     * @return valorile initiale ale parametrilor raportului.
     */
    public ResponseBean initLivrariReport(String cuMontaj) {
	super.createNewFormBean();

	Date today = DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
	form.setLivrariRStart(today);

	form.setLivrariREnd(DateUtils.addDays(today, 7));

	form.setLivrariCuMontaj(cuMontaj);
	ResponseBean r = new ResponseBean();
	r.addRecord();
	r.addField("livrariRStart", form.getLivrariRStart());
	r.addField("livrariREnd", form.getLivrariREnd());
	return r;
    }

    protected ResponseBean doLivrariReport(Collection orders) {
    	ResponseBean r;
	
	logger.log(BasicLevel.DEBUG, "livrariRStart = " + form.getLivrariRStart());
	logger.log(BasicLevel.DEBUG, "livrariREnd = " + form.getLivrariREnd());
	logger.log(BasicLevel.DEBUG, "Nr comenzi in raportul de livrari: " + orders.size());
	
	try {
	    
	    InitialContext ic = new InitialContext();
	    Context env =  (Context) ic.lookup("java:comp/env");

	    OrdersHome osH = (OrdersHome)PortableRemoteObject.
		narrow(env.lookup("ejb/OrderSessionHome"), OrdersHome.class);
	    Orders ordersBean = osH.create();

	    r = new ResponseBean();

	    for(Iterator i = orders.iterator(); i.hasNext();) {
		try {
		    OrderLocal o = (OrderLocal)i.next();
		    ordersBean.loadFormData(o.getId());
		    ResponseBean orderR = ordersBean.orderReport();	
		    r.addRecord();
		    r.addField("order", orderR);
		} catch (RemoteException e) {
		    logger.log(BasicLevel.ERROR, "Eroare de sistem la apelul OrderEJB" + e);
		    logger.log(BasicLevel.DEBUG, "Detalii erorare apel OrderEJB", e);
		}
	    }
	} catch (NamingException e) {
	    r = ResponseBean.getErrConfigNaming(e.getMessage());
	    logger.log(BasicLevel.ERROR, "Eroare la rulare raport livrari " + e);
	    logger.log(BasicLevel.DEBUG, "Detalii exceptie la rulare raport livrari", e);
	} catch (FinderException e) {
	    r = ResponseBean.getErrNotFound(e.getMessage());
	    logger.log(BasicLevel.ERROR, "Eroare la extragerea listei de livrari din baza de date: " + e);
	    logger.log(BasicLevel.DEBUG, "Detalii exceptie extragere lista de valori", e);
	} catch (CreateException e) {
	    r = ResponseBean.getErrCreate(e.getMessage());
	    logger.log(BasicLevel.ERROR, "Bean-ul de sesiune nu a putut fi creat (OrdersEJB): " + e);
	    logger.log(BasicLevel.DEBUG, "Detalii eroare creare OrdersEJB bean", e);
	} catch (RemoteException e) {
	    r = ResponseBean.getErrRemote(e.getMessage());
	    logger.log(BasicLevel.ERROR, "Eroare sistem la instantierea unui OrderEJB: " + e);
	    logger.log(BasicLevel.DEBUG, "Detalii eroare instatiere OrderEJB", e);
	}
	
	DateFormat dfmt = DateFormat.getDateInstance();

	ResponseBean report = new ResponseBean();
	report.addRecord();
	report.addField("livrariRStart", dfmt.format(form.getLivrariRStart()));
	report.addField("livrariREnd", dfmt.format(form.getLivrariREnd()));
	report.addField("livrari", r);
	return report;
    }

    public ResponseBean livrariCuMontajReport() {
	ResponseBean r;
	try {
	    OrderLocalHome oh = getOrderHome();
	    Collection orders = oh.findLivrariCuMontaj(form.getLivrariRStart(), form.getLivrariREnd());
	    r = doLivrariReport(orders);
	} catch (NamingException e) {
	    logger.log(BasicLevel.ERROR, "NamingException in legatura cu OrderHome la rulare raport livrari: " + e);
	    logger.log(BasicLevel.DEBUG, e);
	    r = ResponseBean.getErrConfigNaming(e.getMessage());
	} catch (FinderException e) {
	    logger.log(BasicLevel.WARN, "Livrarile nu au fost gasite (FinderException): " + e);
	    logger.log(BasicLevel.DEBUG, e);
	    r = ResponseBean.getErrNotFound(e.getMessage());
	}
	return r;
    }

    public ResponseBean livrariFaraMontajReport() {
	ResponseBean r;
	try {
	    OrderLocalHome oh = getOrderHome();
	    Collection orders = oh.findLivrariFaraMontaj(form.getLivrariRStart(), form.getLivrariREnd());
	    r = doLivrariReport(orders);
	} catch (NamingException e) {
	    logger.log(BasicLevel.ERROR, "NamingException in legatura cu OrderHome la rulare raport livrari: " + e);
	    logger.log(BasicLevel.DEBUG, e);
	    r = ResponseBean.getErrConfigNaming(e.getMessage());
	} catch (FinderException e) {
	    logger.log(BasicLevel.WARN, "Livrarile nu au fost gasite (FinderException): " + e);
	    logger.log(BasicLevel.DEBUG, e);
	    r = ResponseBean.getErrNotFound(e.getMessage());
	}
	return r;
    }

    public ResponseBean livrariReport() {
	ResponseBean r;
	try {
	    OrderLocalHome oh = getOrderHome();
	    Collection orders = oh.findLivrari(form.getLivrariRStart(), form.getLivrariREnd());
	    r = doLivrariReport(orders);
	} catch (NamingException e) {
	    logger.log(BasicLevel.ERROR, "NamingException in legatura cu OrderHome la rulare raport livrari: " + e);
	    logger.log(BasicLevel.DEBUG, e);
	    r = ResponseBean.getErrConfigNaming(e.getMessage());
	} catch (FinderException e) {
	    logger.log(BasicLevel.WARN, "Livrarile nu au fost gasite (FinderException): " + e);
	    logger.log(BasicLevel.DEBUG, e);
	    r = ResponseBean.getErrNotFound(e.getMessage());
	}
	return r;
    }
    
    /**
     * Initialization of the proforma form fields to the default values.
     *
     */
    public void initProformaFields() {
	form.setProformaNumber("");
	form.setProformaDate(new Date());
	form.setProformaRole("");
	form.setProformaAmount(new BigDecimal(0));
	form.setProformaTax(new BigDecimal(0));
	form.setProformaTotal(new BigDecimal(0));
	form.setProformaExchangeRate(new Double(0)); // las script-ul tcl (calculatedFields) sa calculeze default
	form.setProformaPercent(new Double(0)); //se va ocupa script-ul de setarea reala
	form.setProformaUsePercent(false);
	form.setProformaComment(defaultProformaComment);
	form.setProformaContract("");
	form.setProformaObiectiv("");
	form.setProformaCurrency(""); //camp calculat (este aceeasi cu moneda comenzii)

	form.setProformaAttribute1("");
	form.setProformaAttribute2("");
	form.setProformaAttribute3("");
	form.setProformaAttribute4("");
	form.setProformaAttribute5("");
	form.setProformaAttribute6("");
	form.setProformaAttribute7("");
	form.setProformaAttribute8("");
	form.setProformaAttribute9("");
	form.setProformaAttribute10("");
	form.setProformaAttribute11("");
	form.setProformaAttribute12("");
	form.setProformaAttribute13("");
	form.setProformaAttribute14("");
	form.setProformaAttribute15("");

	if(isSelectedOrder()) {
	    form.setProformaAmount(new BigDecimal(form.getTotalFinal().doubleValue() - 
						  form.getInvoicedAmount().doubleValue())
				   .setScale(2, BigDecimal.ROUND_HALF_UP));
	    form.setProformaTax(new BigDecimal(form.getInvoiceAmount().doubleValue() *
					       form.getTvaPercent().doubleValue() / 100)
				.setScale(2, BigDecimal.ROUND_HALF_UP));
	}
    }

    /**
     * Copy the values of currently selected proforma entity into
     * the form fields.
     *
     * @return a <code>ResponseBean</code> with the result code and,
     * if an error occurs, the error info.
     * @exception FinderException if no entity could be found for the
     * id indicating the currently selected proforma.
     */
    public ResponseBean loadProformaFields() throws FinderException {
	ResponseBean r;

	try {
	    ProformaLocal prf = getCurrentProforma();

	    form.setProformaNumber(prf.getDocument().getNumber());
	    form.setProformaDate(prf.getDocument().getDate());
	    form.setProformaRole(prf.getRole());
	    form.setProformaAmount(prf.getAmount());
	    form.setProformaTax(prf.getTax());
	    form.setProformaExchangeRate(prf.getExchangeRate());
	    form.setProformaUsePercent(prf.getUsePercent());
	    form.setProformaComment(prf.getComment());
	    form.setProformaContract(prf.getContract());
	    form.setProformaObiectiv(prf.getObiectiv());

	    form.setProformaAttribute1(prf.getAttribute1());
	    form.setProformaAttribute2(prf.getAttribute2());
	    form.setProformaAttribute3(prf.getAttribute3());
	    form.setProformaAttribute4(prf.getAttribute4());
	    form.setProformaAttribute5(prf.getAttribute5());
	    form.setProformaAttribute6(prf.getAttribute6());
	    form.setProformaAttribute7(prf.getAttribute7());
	    form.setProformaAttribute8(prf.getAttribute8());
	    form.setProformaAttribute9(prf.getAttribute9());
	    form.setProformaAttribute10(prf.getAttribute10());
	    form.setProformaAttribute11(prf.getAttribute11());
	    form.setProformaAttribute12(prf.getAttribute12());
	    form.setProformaAttribute13(prf.getAttribute13());
	    form.setProformaAttribute14(prf.getAttribute14());
	    form.setProformaAttribute15(prf.getAttribute15());
	    
	    r = new ResponseBean();
	} catch (NamingException e) {
	    logger.log(BasicLevel.INFO, "The proforma could not be loaded " +
		       "due to a configuration error of the naming service: " +
		       e.getMessage());
	    logger.log(BasicLevel.DEBUG, e);
	    r = ResponseBean.getErrConfigNaming(e.getMessage());
	}


	return r;
    }

    public ResponseBean saveProformaData() {
	ResponseBean r;

	// Current record should be saved, because it might be a new one
	r = saveFormData();
	if (r.getCode() != ResponseBean.CODE_SUCCESS)
	    return r;

	if(! isSelectedOrder()) {
	    logger.log(BasicLevel.WARN, "saveProformaData called but no order is selected");
	    return ResponseBean.getErrNotCurrent(ENTITY_ORDER);
	}

	try {
	    ProformaLocal prf;

	    if(isSelectedProforma()) {
		prf = getCurrentProforma();
	    } else {
		ProformaLocalHome prfh = getProformaHome();
		prf = prfh.create();
		getCurrentOrder().getProformas().add(prf);

		Integer proformaNo;
		try {
		    InitialContext ic = new InitialContext();
		    Context env = (Context) ic.lookup("java:comp/env");
		    
		    SequenceHome sh = (SequenceHome)PortableRemoteObject.narrow
			(env.lookup("ejb/SequenceHome"), SequenceHome.class);
		    Sequence s = sh.create();
		    proformaNo = s.getNext("ro.setumsa.sequnces.proformas");
		} catch (Exception e) {
		    proformaNo = new Integer(0);
		    logger.log(BasicLevel.WARN, "Can not get a number for proforma", e);
		}

		form.setProformaNumber(proformaNo.toString());
	    }

	    prf.getDocument().setNumber(form.getProformaNumber());
	    prf.getDocument().setDate(form.getProformaDate());
	    prf.setRole(form.getProformaRole());
	    prf.setAmount(form.getProformaAmount().setScale(2, BigDecimal.ROUND_HALF_UP));
	    prf.setTax(form.getProformaTax().setScale(2, BigDecimal.ROUND_HALF_UP));
	    prf.setExchangeRate(form.getProformaExchangeRate());
	    prf.setUsePercent(form.getProformaUsePercent());
	    prf.setComment(form.getProformaComment());
	    prf.setContract(form.getProformaContract());
	    prf.setObiectiv(form.getProformaObiectiv());

	    prf.setAttribute1(form.getProformaAttribute1());
	    prf.setAttribute2(form.getProformaAttribute2());
	    prf.setAttribute3(form.getProformaAttribute3());
	    prf.setAttribute4(form.getProformaAttribute4());
	    prf.setAttribute5(form.getProformaAttribute5());
	    prf.setAttribute6(form.getProformaAttribute6());
	    prf.setAttribute7(form.getProformaAttribute7());
	    prf.setAttribute8(form.getProformaAttribute8());
	    prf.setAttribute9(form.getProformaAttribute9());
	    prf.setAttribute10(form.getProformaAttribute10());
	    prf.setAttribute11(form.getProformaAttribute11());
	    prf.setAttribute12(form.getProformaAttribute12());
	    prf.setAttribute13(form.getProformaAttribute13());
	    prf.setAttribute14(form.getProformaAttribute14());
	    prf.setAttribute15(form.getProformaAttribute15());

	    r = new ResponseBean();
	} catch (CreateException e) {
	    logger.log(BasicLevel.ERROR, "Proforma entity could not be created: " + e.getMessage());
	    logger.log(BasicLevel.INFO, "The order id at proforma create exception is: " + id);
	    logger.log(BasicLevel.DEBUG, e);
	    
	    r = ResponseBean.getErrCreate(ENTITY_PROFORMA);
	} catch (DataLayerException e) {
	    logger.log(BasicLevel.ERROR, "Proforma entity could not be created: " + e.getMessage());
	    logger.log(BasicLevel.INFO, "The order id at proforma create exception is: " + id);
	    logger.log(BasicLevel.DEBUG, e);
	    
	    r = ResponseBean.getErrCreate(ENTITY_INVOICE);
	} catch (FinderException e) {
	    logger.log(BasicLevel.ERROR, "Finder exception: " + e.getMessage());
	    logger.log(BasicLevel.INFO, "The order id at finder exception is: " + id);
	    logger.log(BasicLevel.INFO, "The proforma id at finder exception is: " + proformaId);
	    
	    r = ResponseBean.getErrNotFound(e.getMessage());
	} catch (NamingException e) {
	    logger.log(BasicLevel.ERROR, "Naming service error: " + e.getMessage());
	    logger.log(BasicLevel.DEBUG, e);
	    
	    r = ResponseBean.getErrConfigNaming(e.getMessage());
	} catch (Exception e) {
	    logger.log(BasicLevel.ERROR, "Unexpected exception" , e);
	    r = ResponseBean.getErrUnexpected(e);
	}


	return r;
    }

    public ResponseBean loadProformas() throws RemoteException {
	logger.log(BasicLevel.DEBUG, ">");
	if(! isSelectedOrder()) {
	    logger.log(BasicLevel.WARN, "loadInvoices called but no order is selected.");
	    return ResponseBean.getErrNotCurrent(ENTITY_ORDER);
	}

	ResponseBean r;
	
	try {
	    Collection proformas = getCurrentOrder().getProformas();

	    r = new ResponseBean();
	    for(Iterator i = proformas.iterator(); i.hasNext();) {
		ProformaLocal prf = (ProformaLocal)i.next();
		
		r.addRecord();
		r.addField("proforma.id", prf.getId());
		r.addField("proforma.number", prf.getDocument().getNumber());
		r.addField("proforma.date", prf.getDocument().getDate());
		r.addField("proforma.role", prf.getRole());
		r.addField("proforma.amount", prf.getAmount());
		r.addField("proforma.exchangeRate", prf.getExchangeRate());
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
     * Creaza un raspuns cu toate campurile din proforma. Pe langa campurile din
     * proforma include si toate campurile comenzii. Din cauza ca foloseste 
     * un apel catre <code>copyFieldsToReport</code> vor fi toate campurile gestionate
     * de acest bean, dar nu vor fi relevante decat cele ale proformei si cele ale 
     * ale comenzii.
     *
     * In cazul in care proforma nu este selectata, va intoarce un raspuns de eroare.
     */
    public ResponseBean proformaReport() throws RemoteException {
	ResponseBean r;

	if (isSelectedProforma()) {
	    r = new ResponseBean();
	    r.addRecord();
	    copyFieldsToResponse(r); 

	    DateFormat dfmt = DateFormat.getDateInstance();

	    // formatez data proformei
	    r.addField("proformaDate", dfmt.format(form.getProformaDate()));


	    ClientLocal client;
	    try {
		client = getFormClient();
	    } catch (Exception e) {
		logger.log(BasicLevel.ERROR, "Client's data can not be read: ", e);
		client = null;
	    }
	    (new ClientUtils(client)).populateResponse(r);

	} else {
	    logger.log(BasicLevel.DEBUG, "Raportul proforma nu poate fi construit pentru ca nu s-a selectat nici o proforma.");
	    r = ResponseBean.getErrNotCurrent(ENTITY_PROFORMA);
	}

	return r;
    }

    public ResponseBean removeProforma() throws RemoteException {
	ResponseBean r;

	r = new ResponseBean();
	return r;
    }

     /**
     * Utility method to get a reference to the PaymentLocalHome.
     */
    protected DailySummaryLocalHome getDailySummaryHome() throws NamingException {
	if(cache_dsh == null) {
	    InitialContext ic = new InitialContext();
	    Context env = (Context)ic.lookup("java:comp/env");
	    cache_dsh = (DailySummaryLocalHome)PortableRemoteObject.narrow
		(env.lookup("ejb/DailySummaryHome"), DailySummaryLocalHome.class);
	}

	return cache_dsh;
    }

    /**
     * Incarca datele din DailySummary pentru data raportului de incasari.
     */
    void loadDailySummary() throws NamingException, FinderException {
	DailySummaryLocalHome dsh = getDailySummaryHome();

	if (form != null) {
	    form.setIncasariValoare(new BigDecimal(0));
	    form.setIncasariBucIncasate(0);
	    form.setIncasariBucNeincasate(0);
	    form.setIncasariBucRate(0);

	    Date incasariToDate = form.getIncasariToDate();

	    if(incasariToDate != null) {
		Collection summaries = dsh.findByDate(RAPORT_INCASARI_SUMMARY, incasariToDate);
		Iterator i = summaries.iterator(); 
		if(i.hasNext()) {
		    DailySummaryLocal dailySummary = (DailySummaryLocal)i.next();

		    form.setIncasariValoare(dailySummary.getValue1());
		    form.setIncasariBucIncasate(dailySummary.getValue2().intValue());
		    form.setIncasariBucNeincasate(dailySummary.getValue3().intValue());
		    form.setIncasariBucRate(dailySummary.getValue4().intValue());
		}
	    }
	}
    }


    /**
     * Initializare date raport incasari.
     */
    public void initIncasariFields() {
	ResponseBean r;

	if (form == null) newFormData();

 	Date today = DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
	form.setIncasariToDate(today);

	Date firstOfMonth = DateUtils.truncate(today, Calendar.MONTH);
	form.setIncasariFromDate(firstOfMonth);

	try {
	    loadDailySummary();
	} catch (Exception e) {
	    logger.log(BasicLevel.ERROR, "Eroare la incarcarea raportului de incasari la data " + today, e);
	    form.setIncasariValoare(new BigDecimal(0));
	    form.setIncasariBucIncasate(new Integer(0));
	    form.setIncasariBucNeincasate(new Integer(0));
	    form.setIncasariBucRate(new Integer(0));
	}

    }

    /**
     * Salveaza datele despre incasari.
     */
    public ResponseBean saveIncasariData() {
	ResponseBean r = new ResponseBean();

	try {
	    DailySummaryLocalHome dsh = getDailySummaryHome();

	    if (form != null) {
		Date incasariToDate = form.getIncasariToDate();
		
		if(incasariToDate != null) {
		    Collection summaries = dsh.findByDate(RAPORT_INCASARI_SUMMARY, incasariToDate);
		    Iterator i = summaries.iterator();
		    DailySummaryLocal dailySummary; 
		    if(i.hasNext()) {
			dailySummary = (DailySummaryLocal)i.next();
		    } else {
			dailySummary = dsh.create();
			dailySummary.setTypeURI(RAPORT_INCASARI_SUMMARY);
			dailySummary.setDate(form.getIncasariToDate());
		    }
		    
		    dailySummary.setValue1(form.getIncasariValoare());
		    dailySummary.setValue2(new BigDecimal(form.getIncasariBucIncasate().intValue()));
		    dailySummary.setValue3(new BigDecimal(form.getIncasariBucNeincasate().intValue()));
		    dailySummary.setValue4(new BigDecimal(form.getIncasariBucRate().intValue()));
		}
	    }
	} catch (NamingException e) {
	    r = ResponseBean.getErrConfigNaming(e.getMessage());
	    logger.log(BasicLevel.ERROR, e);
	} catch (FinderException e) {
	    r = ResponseBean.getErrNotFound(e.getMessage());
	    logger.log(BasicLevel.ERROR, e);
	} catch (CreateException e) {
	    r = ResponseBean.getErrCreate(e.getMessage());
	    logger.log(BasicLevel.ERROR, e);
	}
	return r;
    }

    /**
     * Incarca datele despre incasari
     */
    public ResponseBean loadIncasariFields() throws FinderException {
	ResponseBean r = new ResponseBean();
	return r;
    }

    /**
     * Este apelata cand se modifica data la care se face raportul de incasari.
     *
     * Cand se modifica data raportului, trebuie sa caut daca mai exista date
     * de raport pentru ziua respectiva si sa le incarc.
     *
     * @param toDate data raportului de incasari zilnice
     * @return un <code>ResponseBean</code> cu valorile parametrilor raportului
     * din ziua respectiva (daca a mai fost rulat in ziua aceea).
     */
    public ResponseBean updateIncasariToDate(Date toDate) {
	super.updateIncasariToDate(toDate);


	ResponseBean r = new ResponseBean();
	try {
	    loadDailySummary();
	    r.addRecord();
	    r.addField("incasariValoare", form.getIncasariValoare());
	    r.addField("incasariBucIncasate", form.getIncasariBucIncasate());
	    r.addField("incasariBucNeincasate", form.getIncasariBucNeincasate());
	    r.addField("incasariBucRate", form.getIncasariBucRate());
	} catch (Exception e) {
	    logger.log(BasicLevel.ERROR, "Eroare la incarcarea raportului de incasari la data " + toDate, e);
	}

	return r;
    }
  

    /**
     * Raport incasari.
     */
    public ResponseBean incasariReport() throws RemoteException {

	ResponseBean r;
	DateFormat dfmt = DateFormat.getDateInstance();

	try {
	    PaymentLocalHome ph = getPaymentHome();
	    Collection payments = ph.findByDate(form.getIncasariToDate(), form.getIncasariToDate());
	    
	    
	    ResponseBean paymentsList = new ResponseBean();
	    double total = 0;
	    for(Iterator i = payments.iterator(); i.hasNext(); ) {
		PaymentLocal payment = (PaymentLocal) i.next();

		paymentsList.addRecord();
		paymentsList.addField("document",	payment.getDocument().getNumber());
		paymentsList.addField("data",		dfmt.format(payment.getDocument().getDate()));
		paymentsList.addField("amount",		payment.getAmount());
		paymentsList.addField("client",		payment.getInvoice().getOrder().getClient().getName());
		paymentsList.addField("tipFactura",	payment.getInvoice().getRole());
		paymentsList.addField("exchangeRate",	payment.getExchangeRate());
		paymentsList.addField("currencyAmount", 
				      new BigDecimal(payment.getAmount().doubleValue() 
						     / payment.getExchangeRate().doubleValue())
				      .setScale(2, BigDecimal.ROUND_HALF_UP));
		paymentsList.addField("currency",	payment.getInvoice().getOrder().getAttribute5());

		total += payment.getAmount().doubleValue();
		
	    }
	    BigDecimal totalValoare = new BigDecimal(0);
	    BigDecimal totalBucIncasate = new BigDecimal(0);
	    BigDecimal totalBucNeincasate = new BigDecimal(0);
	    BigDecimal totalBucRate = new BigDecimal(0);

	    DailySummaryLocalHome dsh = getDailySummaryHome();
	    Collection summaries = dsh.findByDate(RAPORT_INCASARI_SUMMARY, 
						  form.getIncasariFromDate(), form.getIncasariToDate());

	    for(Iterator i = summaries.iterator(); i.hasNext();) {
		DailySummaryLocal s = (DailySummaryLocal)i.next();
		totalValoare = totalValoare.add(s.getValue1());
		totalBucIncasate = totalBucIncasate.add(s.getValue2());
		totalBucNeincasate = totalBucNeincasate.add(s.getValue3());
		totalBucRate = totalBucRate.add(s.getValue4());
	    }
	    r = new ResponseBean();
	    r.addRecord();
	    r.addField("fromDate", dfmt.format(form.getIncasariFromDate()));
	    r.addField("toDate", dfmt.format(form.getIncasariToDate()));
	    r.addField("valoare", form.getIncasariValoare());
	    r.addField("bucIncasate", form.getIncasariBucIncasate());
	    r.addField("bucNeincasate", form.getIncasariBucNeincasate());
	    r.addField("bucRate", form.getIncasariBucRate());
	    r.addField("totalZi", form.getIncasariValoare().add(new BigDecimal(total)));
	    r.addField("totalBucIncasate", totalBucIncasate.intValue());
	    r.addField("totalBucNeincasate", totalBucNeincasate.intValue());
	    r.addField("totalBucRate", totalBucRate.intValue());
	    r.addField("totalBuc", 
		       totalBucIncasate.intValue() + totalBucNeincasate.intValue() 
		       + totalBucRate.intValue());
	    r.addField("payments", paymentsList);
	    r.addField("total", total);

	} catch (NamingException e) {
	    logger.log(BasicLevel.ERROR, "Eroare de configurare/accesare JNDI: " + e);
	    logger.log(BasicLevel.INFO, "Detalii eroare covfigurare/accesare JNDI: ", e);
	    r = ResponseBean.getErrConfigNaming(e.getMessage());
	} catch (FinderException e) {
	    logger.log(BasicLevel.ERROR, "Eroare la citirea listei de plati din baza de date: " + e);
	    logger.log(BasicLevel.INFO, "Detalii eroare la citirea liste de plati din baza de date: ", e);
	    r = ResponseBean.getErrNotFound(e.getMessage());
	}
	return r;
    }

}
