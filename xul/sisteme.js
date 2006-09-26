//  sisteme.js


// The main tree
var sisteme;
function load_sisteme() {
    sisteme = theForm.load_listing();
    document.getElementById('sistemeListing').view = make_treeview
	(sisteme,
	 function(row,column) {
	     var col;
	     if(column.id) col = column.id; else col = column;
	     return sisteme[row][col];
	 });
}

// Global variable theForm that will be used by event handlers
var theForm = new FormObject();
theForm.text_fields = new Array("code", "name", "entryPrice", "sellPrice",
				"partPrice", "laborPrice", "relativeGainSP", 
				"absoluteGainSP", "relativeGainPP",
				"absoluteGainPP");

theForm.combo_fields = new Array("categoryId");
theForm.radio_fields = new Array();
theForm.do_link = "/sisteme.do";

theForm.setupEventListeners();
load_sisteme();




function adjustPricesDlg() {
    openDialog("adjustPriceDialog.xul", "AdjustPricesDlg", "chrome", adjustPrices);
}

function adjustPrices(val, type) {
    //alert("adjustPrices with " + val + " " + type);
    var req = new HTTPDataRequest("/sisteme/ajustarepret.do");
    req.add("value", val);
    req.add("type", type);
    if(post_data(req)) {
	alert("Preturi ajustate");
    }
}
