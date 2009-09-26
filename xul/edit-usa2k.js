// edit-usa2k.js


// Global variable theForm that will be used by event handlers
var theForm = new FormObject();

theForm.text_fields = new Array("name", 
				"lg", "hg", "lcorrection", "hcorrection",
				"le", "he", "se", "lCurrent",
				"lUtil", "hUtil", "lFoaie", "hFoaie", "lFoaieSec",
				"lFrame", "bFrame", "cFrame", "lTreshold",
				"cTreshold", "hTreshold", "h1Treshold",
				"h2Treshold",
				"broascaBuc", "cilindruBuc", "copiatCheieBuc",
				"vizorBuc",
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
				"intFinisajPanouLateral",
				"extFinisajBlat",
				"extFinisajToc",
				"extFinisajGrilaj",
				"extFinisajFereastra",
				"extFinisajSupralumina",
				"extFinisajPanouLateral");
theForm.combo_fields = new Array("subclass", "version", "material", 
				 "intFoil", "extFoil", "isolation",
				 "intFoilSec", "extFoilSec", 
				 "openingDir", "openingSide", "foilPosition",
				 "masca", "lacrimar", "bolturi", "platbanda", 
				 "decupareSistemId", "broascaId", "cilindruId",
				 "copiatCheieId", "vizorId", 
				 "sildId", "rozetaId",
				 "manerId", "yalla1Id", "yalla2Id",
				 "baraAntipanicaId", "manerSemicilindruId",
				 "selectorOrdineId", "amortizorId", 
				 "alteSisteme1Id", "alteSisteme2Id",
				 "benefBroascaTip",
				 "benefCilindruTip",
				 "benefSildTip",
				 "benefYallaTip",
				 "benefBaraAntipanicaTip");
theForm.radio_fields = new Array("k", "kType", "ieFoil", "ieFoilSec", 
				 "frameType", "tresholdType",
				 "tresholdSpace", "montareSistem", 
				 "sistemSetumSauBeneficiar");


theForm.hidden_fields = new Array(
				  "id",
				  "intFinisajBlatId",
				  "intFinisajTocId",
				  "intFinisajGrilajId",
				  "intFinisajFereastraId",
				  "intFinisajSupraluminaId",
				  "intFinisajPanouLateralId",
				  "extFinisajBlatId",
				  "extFinisajTocId",
				  "extFinisajGrilajId",
				  "extFinisajFereastraId",
				  "extFinisajSupraluminaId",
				  "extFinisajPanouLateralId");

theForm.cb_fields = new Array(
			      "finisajTocBlat",
			      "finisajGrilajBlat",
			      "finisajFereastraBlat",
			      "finisajSupraluminaBlat",
			      "finisajPanouLateralBlat",
			      "finisajBlatExtInt",
			      "finisajTocExtInt",
			      "finisajGrilajExtInt",
			      "finisajFereastraExtInt",
			      "finisajSupraluminaExtInt",
			      "finisajPanouLateralExtInt");

theForm.do_link = "/usametalica2k.do";

theForm.afterpost = function afterpost() {
    log("edit-usa2k: afterpost called")
    var k = document.getElementById("k");
    var usa2Kstatus = document.getElementById("usa2Kstatus");

    if(k.selectedItem) {
	switch(k.selectedItem.value) {
	case "1":
	    log("edit-usa2k: usa cu 1 canat");
	    //usa2Kstatus.setAttribute("readonly", "true");
	    usa2Kstatus.setAttribute("disabled", "true");
	    break;
	case "2":
	    log("edit-usa2k: usa cu 2 canate");
	    //usa2Kstatus.setAttribute("readonly", "false");
	    usa2Kstatus.removeAttribute("disabled");
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
	(options, 
	 function(row,column) { 
	     var col;
	     if(column.id) col = column.id; else col = column;
	     return options[row][col]; 
	 });
}


// add a new option
function popup_new_item(categoryURI) {
    var select_handler = {
	theForm: theForm,
	category: categoryURI,
	select: function add_item(productId) {
	    var req = this.theForm.get_request();
	    req.add("command", "addOption");
	    req.add("param0", productId);
	    req.add("param1", this.category);
	    var result = this.theForm.post_request(req);
	    load_options();
	    return result;
	}
    };

    new BusinessCategory(categoryURI).addnew_dlg(select_handler);
}

// edit the selected option
function edit_option() {

    var select_handler = {
	theForm: theForm,
	category: options[optionsListing.currentIndex]['options.businessCategory'],
	select: function update_option(productId) {
	    // inform the server about editing the option
	    // addOption service will not actually add the option again
	    var req = this.theForm.get_request();
	    req.add("command", "addOption");
	    req.add("param0", productId);
	    req.add("param1", this.category);
	    var result = this.theForm.post_request(req);
	    load_options();
	    return result;
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

// dialog editare sau duplicare, in functie de valoarea fieldDuplicateName
function edit_or_dup_finisaj(fieldName, fieldDuplicateName) {
    log("edit_or_dup_finisaj: " + fieldDuplicateName + " = " + theForm.values[fieldDuplicateName]);
    edit_finisaj(fieldName, theForm.values[fieldDuplicateName] == "true");
}


// open dialog for editing finisaje
function edit_finisaj(fieldName, duplicate) {
    var select_handler = {
	theForm: theForm,
	fieldName: fieldName,
	select: function change_finisaje_id(productId) {
	    var req = this.theForm.get_request();
	    req.add("command", "change");
	    req.add("field", this.fieldName);
	    req.add("value", productId);
	    this.theForm.post_request(req);
	}
    };

    var current_id = theForm.values[fieldName];
    var category = new BusinessCategory("http://www.kds.ro/erp/businessCategory/setum/usaMetalica/finisaje", theForm);
    if(current_id != 0) {
	if( duplicate ) {
	    category.duplicate_dlg(current_id, select_handler);
	} else {
	    // edit current object
	    category.edit_dlg(current_id, select_handler);
	}
    } else {
	// add new object
	category.addnew_dlg(select_handler);
    }

}



// Primeste ca parametrii id-ul unui camp. Apeleaza change cu valoarea 0.
function clear_sistem(fieldname) {
	
    var req = theForm.get_request();
    req.add("command", "change");
    req.add("field", fieldname);
    req.add("value", "0");
    theForm.post_request(req);
}


function doOk() {
    var req = theForm.get_request();
    req.add("command", "saveFormData");
    
    if(theForm.post_request(req)) {
	window.arguments[0].select(theForm.values["id"]);
	//window.arguments[0].select(theForm.get_loaded_id());
	return true;
    }
    return false;
}


theForm.setupEventListeners();
theForm.load_current();
load_options();



