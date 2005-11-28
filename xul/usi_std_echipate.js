// usi_std_echipate.js

// Global variable theForm for managing form data fields
var theForm = new FormObject();
theForm.text_fields = new Array("name", "code");
theForm.combo_fields = new Array("usaId", "broascaId", "cilindruId",
				 "sildId", "yallaId", "vizorId");
theForm.radio_fields = new Array();

theForm.do_link = "/usistd/echipate.do";


// The main tree (main listing)
var usi;
function load_usi() {
  usi = theForm.load_listing();
  document.getElementById('listingUsi').view = make_treeview
    (usi,
     function(row, column) {
       return usi[row][column];
     });
}

theForm.setupEventListeners();
load_usi();
