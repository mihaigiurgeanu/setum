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
    line_items = new RemoteDataView(theForm.do_link, "lineItemsListing", "lineItemsCount");
    document.getElementById('offerLines').view = make_treeview
	(line_items,
	 function(row,column) {
	     return line_items.get_cell_text(row, column);
	 });
}

// Global variable theForm that will be used by event handlers
var theForm = new FormObject();
theForm.text_fields = new Array("no", "docDate", "dateFrom", "dateTo", 
				"period", "name", "description", "comment",
				"vatPrice", "relativeGain", "absoluteGain",
				"usa", "broasca", "cilindru", "sild",
				"yalla", "vizor", "sellPrice", "entryPrice",
				"filterUsa", "filterBroasca", "filterSild", 
				"filterCilindru", "filterYalla", "filterVizor");

theForm.combo_fields = new Array();
theForm.radio_fields = new Array();
theForm.do_link = "/oferta_usi_std.do";

theForm.setupEventListeners();
load_offers();


// addProducts() opens a dialog box for selecting the products to
// be added to the offer; the products are added with the standard
// reference price
function addProducts() {
    window.openDialog("select_usistd.xul", "usistdselction", "chrome,modal");
}

// add the specified product to the offer
function addProduct(id) {
    log("addProduct id " + id);
    var req = new HTTPDataRequest(theForm.do_link);

    req.add("command", "addProduct");
    req.add("param0", id);
    theForm.post_request(req);

}

function crtItem() {
    return line_items.get_row(document.getElementById('offerLines').currentIndex);
}
