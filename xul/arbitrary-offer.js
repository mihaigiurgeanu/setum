// standardoffer.js


// The main tree
var offers;
function load_offers() {
    offers = theForm.load_listing();
    document.getElementById('offersListing').view = make_treeview
	(offers,
	 function(row,column) {
	     return offers[row][column];
	 });
}

// The list of offer items
var line_items;
function load_items() {
    line_items = theForm.load_sub_listing("lineItemsListing");
    document.getElementById('offerLines').view = make_treeview
	(line_items,
	 function(row,column) {
	     return line_items[row][column];
	 });
}

// Global variable theForm that will be used by event handlers
var theForm = new FormObject();
theForm.text_fields = new Array("no", "docDate", "dateFrom", "dateTo", 
				"period", "name", "description", "comment",
				"price", "relativeGain", "absoluteGain",
				"productCategory", "productCode",
				"productName", "sellPrice", "entryPrice");

theForm.combo_fields = new Array();
theForm.radio_fields = new Array();
theForm.do_link = "/arbitrary-offer.do";

theForm.setupEventListeners();
load_offers();

function make_category(categoryURI) {
    log("Making business category for uri: " + categoryURI);
    category = new BusinessCategory(categoryURI);
    category.theForm = theForm;
    category.fieldsPrefx = "product";
    category.idFieldName = "productId"; // the name of the primary key field
    
    return category;
}

function crtItem() {
    return line_items[document.getElementById('offerLines').currentIndex];
}
