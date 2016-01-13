// usi_std_echipate.js

// Global variable theForm for managing form data fields
var theForm = new FormObject();
theForm.text_fields = new Array("name", "code");
theForm.combo_fields = new Array("usaId", "broascaId", "cilindruId",
				 "sildId", "yallaId", "vizorId");
theForm.radio_fields = new Array("discontinued");

theForm.do_link = "/usistd/echipate.do";


// The main tree (main listing)
var usi;
function load_usi() {
    usi = new RemoteDataView(theForm.do_link, "loadListing", "getListingLength");
    document.getElementById('listingUsi').view = make_treeview
	(usi,
	 function(row, column) {
	     var col;
	     if(column.id) col = column.id; else col = column;
	     return usi.get_cell_text(row, col);
	 });
}


function save() {
    var req = theForm.get_request();
    req.add("command", "saveFormData");
    theForm.post_save_request(req);
    load_usi();
    document.getElementById('maintab').selectedIndex=0;
}

function send_new_command() {
  var req = theForm.get_request();
  req.add("command", "newFormData");
  theForm.post_request(req);
}

function addnew() {
  send_new_command();
  document.getElementById('maintab').selectedIndex=1;
}

function remove_selected_product() {
    var req = theForm.get_request();
    req.add("command", "removeProductDefinition");
    theForm.post_request(req);
    load_usi();
}

theForm.setupEventListeners();

// addnew pentru select filtru de selectie
send_new_command();
load_usi();
