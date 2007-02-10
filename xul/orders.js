// orders.js

// cache reference of the maintab object
var maintab = document.getElementById("maintab");


// UI messages to the user
const REMOVE_ORDER_MSG = "Comanda selectata va fi stearsa impreuna cu toate platile si facturile aferente!";
const REMOVE_INVOICE_MSG = "Factura selectata va fi stearsa impreuna cu toate platile aferente!";
const REMOVE_PAYMENT_MSG = "Plata selectata va fi stearsa!";



// the main tree (orders list)
var orders;
var ordersListing = document.getElementById('ordersListing');
function load_orders() {
    orders = new RemoteDataView(theForm.do_link, "loadListing", "getOrdersCount", true);
    ordersListing.view = make_treeview
	(orders,
	 function(row, column) {
	     var col;
	     if(column.id) col = column.id; else col = column;
	     return orders.get_cell_text(row, col);
	 });
}

var lineItems;
var lineItemsListing = document.getElementById('lineItemsListing');
function load_lineItems() {
    var req = theForm.get_request();
    req.add("command", "loadLines");
    lineItems = load_records(req);
    lineItemsListing.view = make_treeview
	(lineItems,
	 function(row, column) {
	     var col;
	     if(column.id) col = column.id; else col = column;
	     return lineItems[row][col];
	 });
}


var invoices;
var invoicesListing = document.getElementById('invoicesListing');
function load_invoices() {
    var req = theForm.get_request();
    req.add("command", "loadInvoices");
    invoices = load_records(req);
    invoicesListing.view = make_treeview
	(invoices,
	 function(row, column) {
	     var col;
	     if(column.id) col = column.id; else col = column;
	     return invoices[row][col];
	 });
}

var payments;
var paymentsListing = document.getElementById("paymentsListing");
function load_payments() {
    var req = theForm.get_request();
    req.add("command", "loadPayments");
    payments = load_records(req);
    paymentsListing.view = make_treeview
	(payments,
	 function(row, column) {
	     var col;
	     if(column.id) col = column.id; else col = column;
	     return payments[row][col];
	 });
}


function load_selected_order() {
    var selid = orders.get_cell_text(ordersListing.currentIndex, "orders.id");
    var req = theForm.get_request();
    req.add("operation", "new-context");
    req.add("command", "loadFormData");
    req.add("param0", selid);
    theForm.post_request(req);
}

function load_selected_invoice() {
    var selid = invoices[invoicesListing.currentIndex]["invoices.id"];
    var req = theForm.get_request();
    req.add("command", "loadInvoiceData");
    req.add("param0", selid);
    theForm.post_request(req);
}

function on_select_order() {
    load_selected_order();
    load_lineItems();
    load_invoices();
}

function on_select_lineItem() {
    var selid = lineItems[lineItemsListing.currentIndex]["orderItems.id"];
    var req = theForm.get_request();
    req.add("command", "loadOrderLineData");
    req.add("param0", selid);
    theForm.post_request(req);
}

function on_select_invoice() {
    load_selected_invoice();
    load_payments();
}

function on_select_payment() {
    var selid = payments[paymentsListing.currentIndex]["payments.id"];
    var req = theForm.get_request();
    req.add("command", "loadPaymentData");
    req.add("param0", selid);
    theForm.post_request(req);
}

function on_new_order() {
    var req = theForm.get_request();
    req.add("operation", "new-context");
    req.add("command", "newFormData");
    theForm.post_request(req);
    maintab.selectedIndex = 1;
}

function on_delete_order() {
    var req = theForm.get_request();
    req.add("operation", "close-context");
    req.add("command", "removeOrder");
    theForm.post_request(req);
    load_orders();


    return true;
}

function on_save() {
    var req = theForm.get_request();
    req.add("operation", "close-context");
    req.add("command", "saveFormData");
    theForm.post_save_request(req);
    load_orders();
    maintab.selectedIndex = 0;
}

function on_select_offerItems() {
    var select_handler = {
	theForm: theForm,
	select: function add_new_item(offerItemId) {
	    var req = this.theForm.get_request();
	    req.add("command", "addItem");
	    req.add("param0", offerItemId);
	    this.theForm.post_request(req);
	    load_lineItems();
	}
    }

    window.openDialog("select-offer-item.xul", 
		      "select-offer-item", "chrome,resizable",
		      select_handler, 
		      theForm.values["clientId"]);
}

function on_delete_orderItem() {
    var req = theForm.get_request();
    req.add("command", "removeItem");
    req.add("operation", "close-context");
    theForm.post_request(req);

    return true;
}

function on_save_orderItem() {
    var req = theForm.get_request();
    req.add("command", "saveOrderLineData");
    theForm.post_request(req);
    load_lineItems();
    maintab.selectedIndex = 1;
}


function on_select_client() {
    var select_handler = {
	theForm: theForm,
	select: function update_client(clientId) {
	    var req = this.theForm.get_request();
	    req.add("command", "change");
	    req.add("field", "clientId");
	    req.add("value", clientId);
	    this.theForm.post_request(req);
	}
    };
    window.openDialog("select-client.xul", "select-client", "chrome,resizable", select_handler);
}

function on_new_invoice() {
    var req = theForm.get_request();
    req.add("command", "newInvoiceData");
    theForm.post_request(req);
    maintab.selectedIndex = 4;
}

function on_new_payment() {
    var req = theForm.get_request();
    req.add("command", "newPaymentData");
    theForm.post_request(req);
    maintab.selectedIndex = 5;
}

function on_save_invoice() {
    var req = theForm.get_request();
    req.add("command", "saveInvoiceData");
    theForm.post_request(req);
    load_selected_order();
    load_invoices();
    maintab.selectedIndex = 3;
    on_select_order();
}

function on_save_payment() {
    var req = theForm.get_request();
    req.add("command", "savePaymentData");
    theForm.post_request(req);
    load_payments();
    load_selected_order();
    load_selected_invoice();
    load_payments();
    maintab.selectedIndex = 4;
}

function on_remove_invoice() {
    var req = theForm.get_request();
    req.add("command", "removeInvoice");
    theForm.post_request(req);
    load_selected_order();
    load_invoices();
    maintab.selectedIndex = 3;

    return true;
}


function on_remove_payment() {
    var req = theForm.get_request();
    req.add("command", "removePayment");
    theForm.post_request(req);
    load_selected_order();
    load_selected_invoice();
    load_payments();
    maintab.selectedIndex = 4;

    return true;
}

var theForm = new FormObject();
theForm.do_link = "/orders.do";

theForm.text_fields = new Array("number", "date", "clientName", "localitateAlta",
				"distanta", "observatii", "total", "totalTva",
				"discount", "totalFinal", "totalFinalTva",
				"avans", "invoicedAmount", "payedAmount",
				//"achitatCu", 
				"valoareAvans", "diferenta",
				"termenLivrare", "termenLivrare1", "adresaMontaj", 
				"adresaReper", "telefon", "contact",
				
				"productName", "productCode", "price",
				"productPrice", "priceRatio", "quantity",
				"value", "tax",

				"invoiceNumber", "invoiceDate","invoiceAmount",
				"invoiceTax", "invoiceTotal", 
				"invoicePayed", "invoiceUnpayed",

				"paymentNumber", "paymentDate",
				"paymentAmount"
				);

theForm.combo_fields = new Array("montaj", "localitate", "invoiceRole");

theForm.radio_fields = new Array();

theForm.hidden_fields = new Array("clientId", "offerItemId");


theForm.setupEventListeners();
load_orders();

