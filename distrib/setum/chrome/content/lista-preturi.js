// global variables
var origId;

var arrayOfData;
var arrayOfCategorieSistem;
var treeView;


function init()
{
  setView();
  generateCategorieSistemMenuList();
}

// Data retrieval function
function getArrayOfData()
{
  var req = new HTTPDataRequest("/systems.xml");
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

// Data retrieval function
function getArrayForCategorieSistem()
{
  var req = new HTTPDataRequest("/systemstypes.xml");
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


// Defining the tree view and the global data structures
function setView()
{
  arrayOfData = getArrayOfData();
  arrayForCategorieSistem = getArrayForCategorieSistem();
  treeView = {
    rowCount : arrayOfData.length,
    getCellText : function(row,column)
    {
      if (column=="codcol")
	{
	  return arrayOfData[row]["code"];
	}
      if (column=="denumirecol")
	{
	  return arrayOfData[row]["name"];
	}
      if (column=="categoriecol")
	{
	  for (i=0; i < arrayForCategorieSistem.length; i++)
	    {
	      if (arrayForCategorieSistem[i]["id"]==arrayOfData[row]["type_id"]) {
		return arrayForCategorieSistem[i]["name"];
	      }
	    }
	  return "Unknown";
	}
      if (column=="pretintcol")
	{
	  return arrayOfData[row]["price_entry"];
	}
      if (column=="pretmagcol")
	{
	  return arrayOfData[row]["price_sell"];
	}
      if (column=="pretmontcol")
	{
	  return arrayOfData[row]["price_sell1"];
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
  document.getElementById('ofertatree').view=treeView;

  // there is no element selected on the treeview
  document.getElementById('command-save').setAttribute("disabled", true);
}

function initformdata()
{
  listatabbox=document.getElementById('listatabbox');
  listatabbox.selectedIndex=1;
  copyvalues();
}

// initialize the controls in the form with data corresponding to the
// current selection in the tree control
function copyvalues()
{
  var tree=document.getElementById('ofertatree');
  var codsistem=arrayOfData[tree.currentIndex]["code"];
  var denumiresistem=arrayOfData[tree.currentIndex]["name"];
  var categoriesistem=arrayOfData[tree.currentIndex]["type_id"];
  var pretintsistem=arrayOfData[tree.currentIndex]["price_entry"];
  var pretmagsistem=arrayOfData[tree.currentIndex]["price_sell"];
  var pretmontsistem=arrayOfData[tree.currentIndex]["price_sell1"];
  document.getElementById('codsistem').value=codsistem;
  document.getElementById('denumiresistem').value=denumiresistem;
  document.getElementById('categoriesistem').selectedItem=document.getElementById('categoriesistem.'+categoriesistem);
  document.getElementById('pretintsistem').value=pretintsistem;
  document.getElementById('pretmagsistem').value=pretmagsistem;
  document.getElementById('pretmontsistem').value=pretmontsistem;

  // now, the controls on the edit form have values, so I can enable the 
  // save command
  document.getElementById('command-save').setAttribute("disabled", false);
  origId = arrayOfData[tree.currentIndex]["id"];
}

// Builds the selection menu for the locking system types
function generateCategorieSistemMenuList()
{
  var arrayForCategorieSistem=getArrayForCategorieSistem();
  for (i=0; i<arrayForCategorieSistem.length; i++)
    {
      menit=document.createElement("menuitem");
      menit.setAttribute('id','categoriesistem.'+arrayForCategorieSistem[i]["id"]);
      menit.setAttribute('label',arrayForCategorieSistem[i]["name"]);
      menit.setAttribute('value',arrayForCategorieSistem[i]["id"]);
      document.getElementById('categoriesistempopup').appendChild(menit);
    }
  document.getElementById('categoriesistem').selectedIndex=0;
}

// post data to the server (for save)
function save() {
  var req = new HTTPDataRequest("/savesystem.do");
  if(! prepare_form(req))
    return false;

  if(post_data(req)) {
    setView();
    alert("Datele au fost salvate");
  }

}

// post data to the server (for addnew)
function addnew() {
  var req = new HTTPDataRequest("/addsystem.do");
  if(! prepare_form(req))
    return false;

  if(post_data(req)) {
    setView();
    alert("Sistemul a fost adaugat");
  }
}

// add values of the controls to the request
function prepare_form(request) {
  var codsistem = document.getElementById("codsistem").value;
  var categoriesistem = document.getElementById("categoriesistem").selectedItem.value;
  var denumiresistem = document.getElementById("denumiresistem").value;
  var pretintsistem = document.getElementById("pretintsistem").value;
  var pretmagsistem = document.getElementById("pretmagsistem").value;
  var pretmontsistem = document.getElementById("pretmontsistem").value;

  if(!codsistem || codsistem == '') {
    alert("Eroare: Codul sistemului trebuie completat!");
    return false;
  }
  if(!pretintsistem || pretintsistem == '') {
    alert("Eroare: Pretul de intrare al sistemului trebuie completat!");
    return false;
  }
  if(!pretmagsistem || pretmagsistem == '') {
    alert("Eroare: Pretul de magazin al sistemului trebuie completat!");
    return false;
  }
  if(!pretmontsistem || pretmontsistem == '') {
    alert("Eroare: Pretul de montare al sistemului trebuie completat!");
    return false;
  }

  request.add("code", codsistem);
  request.add("id", origId);
  request.add("name", denumiresistem);
  request.add("type_id", categoriesistem);
  request.add("price_entry", pretintsistem);
  request.add("price_sell", pretmagsistem);
  request.add("price_sell1", pretmontsistem);

  return true;
}


function adjustPricesDlg() {
  openDialog("adjustPriceDialog.xul", "AdjustPricesDlg", "chrome", adjustPrices);
}

function adjustPrices(val, type) {
  //alert("adjustPrices with " + val + " " + type);
  var req = new HTTPDataRequest("/sisteme/ajustarepret.do");
  req.add("value", val);
  req.add("type", type);
  if(post_data(req)) {
    setView();
    alert("Preturi ajustate");
  }
}
