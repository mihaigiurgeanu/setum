// usa_std_neechipata.js

// Global variable theForm, for managing the form data fields
var theForm = new FormObject();
theForm.text_fields = new Array("code", "name", "description", "price");
theForm.combo_fields = new Array();
theForm.radio_fields = new Array();

theForm.do_link="/usistd/neechipate.do";

// The main tree (main listing)
var usi;
function load_usi() {
  usi = theForm.load_listing();
  document.getElementById('usiListing').view = make_treeview
    (usi,
     function(row, column) {
       return usi[row][column];
     });
}


theForm.setupEventListeners();
load_usi();