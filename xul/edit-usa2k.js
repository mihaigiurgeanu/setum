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
				"manerSemicilindruBuc", "amortizorBuc",
				"alteSisteme1Buc", "alteSisteme2Buc",
				"sistemeComment");
theForm.combo_fields = new Array("subclass", "version", "material", 
				 "intFoil", "extFoil", "isolation",
				 "intFoilSec", "extFoilSec", 
				 "openingDir", "openingSide", "foilPosition",
				 "decupareSistemId", "broascaId", "cilindruId",
				 "copiatCheieId", "sildId", "rozetaId",
				 "manerId", "yalla1Id", "yalla2Id",
				 "baraAntipanicaId", "manerSemicilindruId",
				 "selectorOrdineId", "amortizorId", 
				 "alteSisteme1Id", "alteSisteme2Id");
theForm.radio_fields = new Array("k", "ieFoil", "ieFoilSec", 
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
	    usa2Kstatus.setAttribute("disabled", "true");
	    break;
	case "2":
	    log("edit-usa2k: usa cu 2 canate");
	    usa2Kstatus.setAttribute("disabled", "false");
	    break;
	default:
	    log("edit-usa2k: valoare nepermisa pentru k: " + k.selectedItem.value);
	    break;
	}
    } else {
	log("edit-usa2k: k.slectedItem este undefined");
    }
}


theForm.setupEventListeners();
theForm.load_current();

function doOk() {
    if(theForm.save()) {
	window.arguments[0].select(theForm.get_loaded_id());
	return true;
    }
    return false;
}
