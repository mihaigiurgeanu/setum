// edit-usa2k.js


// Global variable theForm that will be used by event handlers
var theForm = new FormObject();

theForm.text_fields = new Array("lg", "hg", "lcorrection", "hcorrection",
				"le", "he", "lCurrent",
				"lFrame", "bFrame", "cFrame", "lTreshold",
				"cTreshold", "hTreshold", "h1Treshold",
				"h2Treshold",
				"broascaBuc", "cilindruBuc", "copiatCheieBuc",
				"sildTip", "sildCuloare", "sildBuc",
				"rozetaTip", "rozetaCuloare", "rozetaBuc",
				"manerTip", "manerCuloare", "manerBuc",
				"yalla1Buc", "yalla2Buc",
				"baraAntipanicaBuc",
				"manerSemicilindruBuc", 
				"selectorOrdineBuc", "amortizorBuc",
				"alteSisteme1Buc", "alteSisteme2Buc",
				"sistemeComment",
				"benefBroasca", "benefBroascaBuc",
				"benefCilindru", "benefCilindruBuc",
				"benefSild", "benefSildBuc",
				"benefYalla", "benefYallaBuc",
				"benefBaraAntipanica", "benefBaraAntipanicaBuc",
				"benefManer", "benefManerBuc",
				"benefSelectorOrdine", "benefSelectorOrdineBuc",
				"benefAmortizor", "benefAmortizorBuc",
				"benefAlteSisteme1", "benefAlteSisteme1Buc",
				"benefAlteSisteme2", "benefAlteSisteme2Buc",
				"intFinisajBlat",
				"intFinisajToc",
				"intFinisajGrilaj",
				"intFinisajFereastra",
				"intFinisajSupralumina",
				"intFinisajPanouLateral");
theForm.combo_fields = new Array("subclass", "version", "material", 
				 "intFoil", "extFoil", "isolation",
				 "intFoilSec", "extFoilSec", 
				 "openingDir", "openingSide", "foilPosition",
				 "decupareSistemId", "broascaId", "cilindruId",
				 "copiatCheieId", "sildId", "rozetaId",
				 "manerId", "yalla1Id", "yalla2Id",
				 "baraAntipanicaId", "manerSemicilindruId",
				 "selectorOrdineId", "amortizorId", 
				 "alteSisteme1Id", "alteSisteme2Id",
				 "benefBroascaTip",
				 "benefCilindruTip",
				 "benefSildTip",
				 "benefYallaTip",
				 "benefBaraAntipanicaTip",
				 "intFinisajBlatId",
				 "intFinisajTocId",
				 "intFinisajGrilajId",
				 "intFinisajFereastraId",
				 "intFinisajSupraluminaId",
				 "intFinisajPanouLateralId");
theForm.radio_fields = new Array("k", "kType", "ieFoil", "ieFoilSec", 
				 "frameType", "tresholdType",
				 "tresholdSpace", "montareSistem", 
				 "sistemSetumSauBeneficiar");


theForm.hidden_fields = new Array();
theForm.do_link = "/usametalica2k.do";

theForm.afterpost = function afterpost() {
    log("edit-usa2k: afterpost called")
    var k = document.getElementById("k");
    var usa2Kstatus = document.getElementById("usa2Kstatus");

    if(k.selectedItem) {
	switch(k.selectedItem.value) {
	case "1":
	    log("edit-usa2k: usa cu 1 canat");
	    usa2Kstatus.setAttribute("readonly", "true");
	    break;
	case "2":
	    log("edit-usa2k: usa cu 2 canate");
	    usa2Kstatus.setAttribute("readonly", "false");
	    break;
	default:
	    log("edit-usa2k: valoare nepermisa pentru k: " + k.selectedItem.value);
	    break;
	}
    } else {
	log("edit-usa2k: k.slectedItem este undefined");
    }
}


// Options



// the categories listing
var options;
var optionsListing = document.getElementById("optionsListing");
function load_options() {
    var req = theForm.get_request();
    req.add("command", "getOptionsListing");
    options = load_records(req);
    optionsListing.view = make_treeview
	(options, function(row,col) { return options[row][col]; });
}


// add a new option
function popup_new_item(categoryURI) {
    theForm.save();
    var select_handler = {
	theForm: theForm,
	category: categoryURI,
	select: function add_item(productId) {
	    var req = this.theForm.get_request();
	    req.add("command", "addOption");
	    req.add("param0", productId);
	    req.add("param1", this.category);
	    this.theForm.post_request(req);
	    load_options();
	}
    };

    new BusinessCategory(categoryURI).addnew_dlg(select_handler);
}

// edit the selected option
function edit_option() {

    var select_handler = {
	theForm: theForm,
	select: function update_option(productId) {
	    load_options();
	}
    };

    new BusinessCategory(options[optionsListing.currentIndex]['options.businessCategory'])
	.edit_dlg(options[optionsListing.currentIndex]['options.id'], select_handler);
}

// delete the selected option
function delete_option() {
    var req = theForm.get_request();
    req.add("command", "removeOption");
    req.add("param0", options[optionsListing.currentIndex]['options.id']);
    theForm.post_request(req);
    load_options();
}


function doOk() {
    if(theForm.save()) {
	window.arguments[0].select(theForm.get_loaded_id());
	return true;
    }
    return false;
}


theForm.setupEventListeners();
theForm.load_current();
load_options();



