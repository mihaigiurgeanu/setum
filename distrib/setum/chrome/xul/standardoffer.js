// standardoffer.js


// The main tree
var offers;
function load_offers() {
  offers = theForm.load_listing();
  document.getElementById('offersListing').view = make_treeview
    (offers,
     function(row,column) {
	 var col;
	 if(column.id) col = column.id; else col = column;
	 return offers[row][col];
     });
}

// The list of offer items
var line_items;
function load_items() {
  line_items = theForm.load_sub_listing("lineItemsListing");
  document.getElementById('offerLines').view = make_treeview
    (line_items,
     function(row,column) {
	 var col;
	 if(column.id) col = column.id; else col = column;
	 return line_items[row][col];
     });
}

// Global variable theForm that will be used by event handlers
var theForm = new FormObject();
theForm.text_fields = new Array("no", "docDate", "dateFrom", "dateTo", 
				"period", "name", "description", "comment",
				"price", "relativeGain", "absoluteGain",
				"price1", "price2",
				"productCategory", "productCode",
				"productName", "entryPrice", "sellPrice", "lineComments");

theForm.combo_fields = new Array();
theForm.radio_fields = new Array();
theForm.do_link = "/standardoffer.do";

theForm.setupEventListeners();
load_offers();


// launch offer
function launch_offer() {
    var req = new HTTPDataRequest(theForm.do_link);
    req.add("command", "makeCurrent");
    theForm.post_request(req);
    load_offers();
}

// discontinue offer
function discontinue_offer() {
    var req = new HTTPDataRequest(theForm.do_link);
    req.add("command", "discontinue");
    theForm.post_request(req);
    load_offers();
}

function crtItem() {
    return line_items[document.getElementById('offerLines').currentIndex];
}

// Open a dialog to select the items to add to the offer
function addItems() {
    //theForm.addnew_sub('addNewItem'); 
    //document.getElementById('maintab').selectedIndex=2;

    var select_handler = {
	theForm: theForm,
	select: function(id) {
	    var req = this.theForm.get_request();
	    req.add("command", "addProduct");
	    req.add("param0", id);
	    this.theForm.post_request(req);
	    load_items();
	}
    };

    window.openDialog("select-sistem.xul", "selectsistem", "chrome,resizable", select_handler);

}



function adjust_prices_dialog() {

    var adjust_prices_handler = {
	theForm: theForm,
	adjust_prices: function(percent) {
	    var req = new HTTPDataRequest(theForm.do_link);
	    req.add("command", "adjustPrices");
	    req.add("param0", percent);

	    return this.theForm.post_request(req);
	}
    }
    window.openDialog("adjust-std-offer.xul", "adjust-std-offer", "chrome,resizable", adjust_prices_handler);
}
