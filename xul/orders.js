// orders.js

// cache reference of the maintab object
var maintab = document.getElementById("maintab");


// the main tree (orders list)
var orders;
var ordersListing = document.getElementById('ordersListing');
function load_orders() {
    orders = new RemoteDataView(theForm.do_link, "loadListing", "getOrdersCount");
    ordersListing.view = make_treeview
	(orders,
	 function(row, column) {
	     var col;
	     if(column.id) col = column.id; else col = column;
	     return usi.get_cell_text(row, col);
	 });
}

var lineItems;
var lineItemsListing = document.getElementById('lineItemsListing');
function load_lineItems() {
    var req = theForm.get_request();
    req.add("operation", "new-context");
    req.add("command", "loadLines");
    lineItems = load_records(req);
    lineItemsListing.view = make_treeview
	(line_items,
	 function(row, column) {
	     var col;
	     if(column.id) col = column.id; else col = column;
	     return lineItems[row][col];
	 });
}




function on_select_order() {
    var selid = orders.get_cell_text(ordersListing.currentIndex, "orders.id");
    var req = theForm.get_request();
    req.add("command", "new-context");
    req.add("command", "loadFormData");
    req.add("param0", selid);
    theForm.post_request(req);
    load_lineItems();
}

function on_select_lineItem() {
    var selid = lineItems[lineItemsListing.currentIndex]["lines.id"];
    var req = theForm.get_request();
    req.add("command", "loadLineItemData");
    req.add("param0", selid);
    theForm.post_request(req);
}

function on_new_order() {
    var req = theForm.get_request();
    req.add("command", "newFormData");
    theForm.post_request(req);
    maintab.selectedIndex = 1;
}

function on_delete_order() {
    // not implemented
    log("delete_order operation is not implemented");
}

function on_save() {
    var req = theForm.get_request();
    req.add("operation", "close-context");
    req.add("command", "saveFormData");
    theForm.post_save_request();
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
    theForm.post_request(req);
}

function on_save_orderItem() {
    var req = theForm.get_request();
    req.add("command", "saveLinetItemData");
    theForm.post_request(req);
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



var theForm = new FormObject();
theForm.do_link = "/orders.do";

theForm.text_fields = new Array("number", "date", "clientName", "localitateAlta",
				"distanta", "observatii", "total", "totalTva",
				"discount", "totalFinal", "totalFinalTva",
				"avans", "achitatCu", "valoareAvans", "diferenta",
				"termenLivrare", "termenLivrare1", "adresaMontaj", 
				"adresaReper", "telefon", "contact");

theForm.combo_fields = new Array("montaj", "localitate");

theForm.radio_fields = new Array();

theForm.hidden_fields = new Array("clientId");


theForm.setupEventListeners();
load_orders();

