// rules.js

// cache a reference to the maintab
var maintab = document.getElementById("maintab");

// The main tree
var sets;
var setsListing = document.getElementById('setsListing');
function load_sets() {
  var req = theForm.get_request();
  req.add("command", "loadSets");
  sets = load_records(req);

  setsListing.view = make_treeview
    (sets,
     function(row, column) {
      var col;
      if(column.id) col = column.id; else col = column;
      return sets[row][col];
    });
}

var rules;
var rulesListing = document.getElementById("rulesListing");
function load_rules() {
  var req = theForm.get_request();
  req.add("command", "loadRules");
  rules = load_records(req);
  rulesListing.view = make_treeview
    (rules,
     function(row, column) {
      var col;
      if(column.id) col = column.id; else col = column;
      return rules[row][col];
    });
}


function load_selected_set() {
  var selid = sets.get_cell_text(setsListing.currentIndex, "sets.id");
  var req = theForm.get_request();
  req.add("operation", "new-context");
  req.add("command", "loadFormData");
  req.add("param0", selid);
  theForm.post_request(req);
}

function load_selected_rule() {
  var selid = sets.get_cell_text(rulesListing.currentIndex, "rules.id");
  var req = theForm.get_request();
  req.add("command", "loadRuleData");
  req.add("param0", selid);
  theForm.post_request(req);
}

function on_select_set() {
  load_selected_set();
  load_rules();
}

function on_select_rule() {
  load_selected_rule();
}

function on_add_set() {
  var req = theForm.get_request();
  req.add("operation", "new-context");
  req.add("command", "newFormData");
  theForm.post_request(req);
  maintab.selectedIndex  = 1;
}


function on_add_rule() {
  var req = theForm.get_request();
  req.add("command", "newRuleData");
  theForm.post_request(req);
  maintab.seletedIndex = 2;
}


function on_remove_set() {
  var req = theForm.get_request();
  req.add("operation", "close-context");
  req.add("command", "removeSet");
  theForm.post_request(req);
  load_sets();

  return true;
}

function on_remove_rule() {
  var req = theForm.get_request();
  req.add("command", "removeRule");
  theForm.post_request(req);
  load_rules();

  return true;
}

function on_save_set() {
  var req = theForm.get_request();
  req.add("command", "saveFormData");
  theForm.post_request(req);
  load_sets();
  maintab.selectedIndex = 0;
}

function on_save_rule() {
  var req = theForm.get_request();
  req.add("command", "saveRuleData");
  theForm.post_request(req);
  load_rules();
  maintab.selectedIndex = 1;
}





var theForm = new FormObject();
theForm.do_link = "/rules.do";

theForm.text_fields = new Array("setName", "name", "condition");
theForm.combo_fields = new Array();
theForm.radio_fields = new Array("errorFlag");
theForm.hidden_fields = new Array();

theForm.setupEventListeners();
load_sets();
