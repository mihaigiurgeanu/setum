// standardoffer.js

// cache a reference to the maintab
var maintab = document.getElementById("maintab");

// The main tree
var offers;
var offersListing = document.getElementById('offersListing');
function load_offers() {
    var req = theForm.get_request();
    req.add("command", "loadListing");
    offers = load_records(req);

    offersListing.view = make_treeview
	(offers,
	 function(row,column) {
	     return offers[row][column];
	 });
}

// The list of offer items
var line_items;
var lineItemsListing = document.getElementById('offerLines');
function load_items() {
    var req = theForm.get_request();
    req.add("command", "lineItemsListing");
    line_items = load_records(req);
    lineItemsListing.view = make_treeview
	(line_items,
	 function(row,column) {
	     return line_items[row][column];
	 });
}


function make_category(categoryURI) {
    log("Making business category for uri: " + categoryURI);
    var category = new BusinessCategory(categoryURI);
    category.theForm = theForm;
    category.fieldsPrefix = "product";
    category.idFieldName = "productId"; // the name of the primary key field
    
    return category;
}

function crtItem() {
    return line_items[lineItemsListing.currentIndex];
}

function on_select_offer() {
    var selid = offers[offersListing.currentIndex]["id"];
    var req = theForm.get_request();
    req.add("command", "loadFormData");
    req.add("param0", selid);
    theForm.post_request(req);
    load_items();
}

function on_select_item() {
    var selid = line_items[lineItemsListing.currentIndex]["id"];
    var req = theForm.get_request();
    req.add("command", "loadSubForm");
    req.add("param0", selid);
    theForm.post_request(req);
}


function add_offer() {
    var req = theForm.get_request();
    req.add("command", "newFormData");
    theForm.post_request(req);
    load_items();
    maintab.selectedIndex = 1;
}

function remove_item() {
    var req = theForm.get_request();
    req.add("command", "removeItem");
    theForm.post_request(req);
    load_items();
}

function save_offer() {
    var req = theForm.get_request();
    req.add("command", "saveFormData");
    theForm.post_save_request(req);
    load_offers();
    maintab.selectedIndex = 0;
}

function save_item() {
    var req = theForm.get_request();
    req.add("command", "saveSubForm");
    theForm.post_save_request(req);
    load_items();
    maintab.selectedIndex = 1;
}


function edit_product() {
    log("edit_product: " + crtItem()['productId']);
    log("edit_product: " + crtItem()['businessCategory']);
    make_category(crtItem()['businessCategory']).edit_dlg(crtItem()['productId']);
}

function popup_new_item(id) {
    theForm.save(); 
    make_category(id).addnew_dlg(load_items);
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

