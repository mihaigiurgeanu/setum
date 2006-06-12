// value_lists.js

// cache a reference to the maintab
var maintab = document.getElementById("maintab");

// the categories listing
var categories;
var categoriesListing = document.getElementById("categoriesListing");
function load_categories() {
    var req = theForm.get_request();
    req.add("command", "loadCategories");
    categories = load_records(req);
    categoriesListing.view = make_treeview
	(categories, function(row,col) { return categories[row][col]; });
}

// products listing
var products;
var productsListing = document.getElementById("productsListing")
function load_products() {
    var req = theForm.get_request();
    req.add("command", "loadProducts");
    products = load_records(req);
    productsListing.view = make_treeview
    (products, function(row,col) { return products[row][col]; });
}

// attributes listing
var attributes;
var attributesListing = document.getElementById("attributesListing");
function load_attributes() {
    var req = theForm.get_request();
    req.add("command", "loadAttributes");
    attributes = load_records(req);
    attributesListing.view = make_treeview
	(attributes, function(row,col) { return attributes[row][col]; });
}

// a selection was made in the category listing
function on_select_category() {
    var selid = categories[categoriesListing.currentIndex]["categories.id"];
    var req = theForm.get_request();
    req.add("command", "loadFormData");
    req.add("param0", selid);
    theForm.post_request(req);
    load_products();
}

// a selection was made in the products listing
function on_select_product() {
    var selid = products[productsListing.currentIndex]["products.id"];
    var req = theForm.get_request();
    req.add("command", "loadProductData");
    req.add("param0", selid);
    theForm.post_request(req);
    load_attributes();
}

// a selection was made in the attributes listing
function on_select_attribute() {
    var selid = attributes[attributesListing.currentIndex]["attr.id"];
    var req = theForm.get_request();
    req.add("command", "loadAttributeData");
    req.add("param0", selid);
    theForm.post_request(req);
}

function add_category() {
    var req = theForm.get_request();
    req.add("command", "newFormData");
    theForm.post_request(req);
    load_products();
    maintab.selectedIndex = 1;
}

function add_product() {
    var req = theForm.get_request();
    req.add("command", "newProductData");
    theForm.post_request(req);
    load_attributes();
    maintab.selectedIndex = 2;
}

function add_attribute() {
    var req = theForm.get_request();
    req.add("command", "newAttributeData");
    theForm.post_request(req);
    maintab.selectedIndex = 3;
}

function remove_category() {
    var req = theForm.get_request();
    req.add("command", "removeCategory");
    theForm.post_request(req);
    load_categories();
}

function remove_product() {
    var req = theForm.get_request();
    req.add("command", "removeProduct");
    theForm.post_request(req);
    load_products();
}

function remove_attribute() {
    var req = theForm.get_request();
    req.add("command", "removeAttribute");
    theForm.post_request(req);
    load_attrbutes();
}

function save_category() {
    var req = theForm.get_request();
    req.add("command", "saveFormData");
    theForm.post_save_request(req);
    load_categories();
    maintab.selectedIndex = 0;
}

function save_product() {
    var req = theForm.get_request();
    req.add("command", "saveProductData");
    theForm.post_save_request(req);
    load_products();
    maintab.selectedIndex = 1;
}

function save_attribute() {
    var req = theForm.get_request();
    req.add("command", "saveAttributeData");
    theForm.post_save_request(req);
    load_attributes();
    maintab.selectedIndex = 2;
}

var theForm = new FormObject();
theForm.do_link = "/value_lists.do";

theForm.text_fields = new Array(
				"categoryId", 
				"categoryName", 

				"productId",
				"productName",
				"productCode",
				"productDescription",
				"productEntryPrice",
				"productSellPrice",
				"productPrice1",
				"productPrice2",
				"productPrice3",
				"productPrice4",
				"productPrice5",
				
				"attrId",
				"attrName",
				"attrString",
				"attrInt",
				"attrDecimal"
				);
theForm.combo_fields = new Array();
theForm.radio_fields = new Array();

theForm.setupEventListeners();
load_categories();
