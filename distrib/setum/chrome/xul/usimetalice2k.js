var productsList; /* the global array for the list of products
		   * that will be displayed in the first tree
		   */

function initform() {
  // initializing event listeners for text fields here, because at onblur,
  // this object has no id (a mozilla bug?).
  document.getElementById("lg").addEventListener("blur", text_event_listener, true);
  document.getElementById("hg").addEventListener("blur", text_event_listener, true);
  document.getElementById("lcorrection").addEventListener("blur", text_event_listener, true);
  document.getElementById("hcorrection").addEventListener("blur", text_event_listener, true);
  document.getElementById("lCurrent").addEventListener("blur", text_event_listener, true);
  document.getElementById("lFrame").addEventListener("blur", text_event_listener, true);
  document.getElementById("bFrame").addEventListener("blur", text_event_listener, true);
  document.getElementById("cFrame").addEventListener("blur", text_event_listener, true);
  document.getElementById("lTreshold").addEventListener("blur", text_event_listener, true);
  document.getElementById("hTreshold").addEventListener("blur", text_event_listener, true);
  document.getElementById("cTreshold").addEventListener("blur", text_event_listener, true);
  document.getElementById("h1Treshold").addEventListener("blur", text_event_listener, true);
  document.getElementById("h2Treshold").addEventListener("blur", text_event_listener, true);

  refreshProductsList();
}

function post_request(req) {
  var response = req.execute();
  if(response) {
    records = response.records;
    if(records[0])
      update_form(records[0]);
    
    if(response.code == 0) {
    } else {
      alert("ERROR: " + response.message + "\nCode: " + response.code);
    }
  } else {
    alert("ERROR receiving the response");
  }
}

function addnew() {
  var req = new HTTPDataRequest("/usametalica2k.do");
  req.add("command", "new");
  post_request(req);

  tabbox=document.getElementById('maintab');
  tabbox.selectedIndex=1;
}

function editUsa() {
  var req = new HTTPDataRequest("/usametalica2k.do");
  req.add("command", "load");
  req.add("id", productsList[document.getElementById('listausi').currentIndex]["id"]);
  post_request(req);
  

  tabbox=document.getElementById('maintab');
  tabbox.selectedIndex=1;
}

function save() {
  var req = new HTTPDataRequest("/usametalica2k.do");
  req.add("command", "save");
  post_request(req);  
  refreshProductsList();
}


function update_form(record) {
  update_combo("subclass", record);
  update_combo("version", record);
  update_combo("material", record);
  update_text("lg", record);
  update_text("hg", record);
  update_text("le", record);
  update_text("he", record);
  update_text("lcorrection", record);
  update_text("hcorrection", record);
  update_text("lCurrent", record);
  update_radio("kType", record);
  update_combo("intFoil", record);
  update_combo("ieFoil", record);
  update_combo("extFoil", record);
  update_combo("intFoilSec", record);
  update_combo("ieFoilSec", record);
  update_combo("extFoilSec", record);
  update_combo("isolation", record);
  update_combo("openingDir", record);
  update_combo("openingSide", record);
  update_radio("frameType", record);
  update_text("lFrame", record);
  update_text("bFrame", record);
  update_text("cFrame", record);
  update_combo("foilPosition", record);
  update_radio("tresholdType", record);
  update_text("lTreshold", record);
  update_text("hTreshold", record);
  update_text("cTreshold", record);
  update_radio("tresholdSpace", record);
  update_text("h1Treshold", record);
  update_text("h2Treshold", record);
}

function update_combo(field, record) {
  if(record[field] != undefined) {
    document.getElementById(field).selectedItem = 
      document.getElementById(field + "-" + record[field]);    
  }
}

function update_radio(field, record) {
  if(record[field] != undefined) {
    document.getElementById(field).selectedItem = 
      document.getElementById(field + "-" + record[field]);    
  }
}

function update_text(field, record) {
  if(record[field] != undefined) {
    document.getElementById(field).value = record[field]; 
  }
}

function combo_changed(control) {
  var req = new HTTPDataRequest("/usametalica2k.do");
  req.add("command", "change");
  req.add("field", control.id);
  req.add("value", control.selectedItem.value);

  post_request(req);
}

function radio_changed(control) {
  var req = new HTTPDataRequest("/usametalica2k.do");
  req.add("command", "change");
  req.add("field", control.id);
  req.add("value", control.selectedItem.value);

  post_request(req);
}

function text_changed(event) {
  control = event.target;
  var req = new HTTPDataRequest("/usametalica2k.do");
  req.add("command", "change");
  req.add("field", control.id);
  req.add("value", control.value);

  post_request(req);
}

var text_event_listener = { handleEvent: text_changed };


// refreshing the list of products
// Defining the tree view and the global data structures
function refreshProductsList()
{
  productsList = getListOfProducts();
  treeView = {
    rowCount : productsList.length,
    getCellText : function(row,column)
    {
      if (column=="col-id")
	{
	  return productsList[row]["id"];
	}
      if (column=="col-usa")
	{
	  return productsList[row]["name"];
	}
      if (column=="col-material")
	{
	  switch(productsList[row]["material"]) {
	  case "1":
	    return "otel";
	  case "2":
	    return "otel zincat termic";
	  case "3":
	    return "inox";
	  case "4":
	    return "aluminiu";
	  case "5":
	    return "alama";
	  case "6":
	    return "cupru";
	  default:
	    return "altceva";
	  }
	}
    },
    setTree: function(treebox){ this.treebox = treebox; },
    isContainer: function(row){ return false; },
    isSeparator: function(row){ return false; },
    isSorted: function(row){ return false; },
    getLevel: function(row){ return 0; },
    getImageSrc: function(row,col){ return null; },
    getRowProperties: function(row,props){},
    getCellProperties: function(row,col,props){},
    getColumnProperties: function(colid,col,props){}
  };
  document.getElementById('listausi').view=treeView;

}

// Connects to the application server and retrieves the list of products
function getListOfProducts()
{

  var req = new HTTPDataRequest("/usimetalice2k.xml");
  var response = req.execute();
  if(response) {
    if(response.code == 0)
      return response.records;
    else {
      alert("Error while retrieving data. Response code is: " + response.code +
	    "\nMessage:\n" + response.message);
      return new Array();
    }
  }
  alert("Error while retrieving data. No response received from server.");
  return new Array();


}
