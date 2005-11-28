function init() {
  parent.document.getElementById('modules-list')
    .setAttribute('title',
		  document.getElementById('lista-preturi-usi-standard-win')
		  .getAttribute('title'));

  updateOferta();
  setView();

  updateUsaSimpla();
  updateBroasca();
  updateCilindru();
  updateSild();
  updateYalla();
  updateVizor();
}


// Setting the data view for the main tree
var rsOferta;

// retrieves oferta from the database
function updateOferta() {
  var req = new HTTPDataRequest("/ofertausistd.xml");
  var response = req.execute();
  if(response) {
    if(response.code == 0)
      rsOferta = response.records;
    else {
      alert("Error while retrieving data. Response code is: " + response.code +
	    "\nMessage:\n" + response.message);
      rsOferta = new Array();
    }
  } else {
    alert("Error while retrieving data. No response received from server.");
    rsOferta = new Array();
  }
}

function setView() {
  var treeView = {
    rowCount : rsOferta.length,
    getCellText : function(row,column)
    {
      if (column=="col-usa")
	{
	  return rsOferta[row]["usa.code"];
	}
      else if (column=="col-broasca")
	{
	  return rsOferta[row]["broasca.name"];
	}
      else if (column=="col-cilindru")
	{
	  return rsOferta[row]["cilindru.name"];
	}
      else if (column=="col-sild")
	{
	  return rsOferta[row]["sild.name"]
	}
      if (column=="col-yalla")
	{
	  return rsOferta[row]["yalla.name"];
	}
      if (column=="col-vizor")
	{
	  return rsOferta[row]["vizor.name"];
	}
      if (column=="col-pret")
	{
	  return rsOferta[row]["sellPrice"];
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
  document.getElementById('command-saveusmpl').setAttribute('disabled', true);
  document.getElementById('command-saveustd').setAttribute('disabled', true);  
}

// The current index in the rsOferta array
function currentIndex() {
  var tree=document.getElementById('ofertatree');
  return tree.currentIndex;
}

function updateValueList(req, popupid) {
  var response = req.execute();
  if(response) {
    if(response.code == 0) {
      buildPopup(popupid, response.records);
    } else {
      alert("Error while retrieving data. Response code is: " + response.code +
	    "\nMessage:\n" + response.message);
    }
  } else {
    alert("Error while retrieving data. No response received from server.");
  }  
}

function buildPopup(popupid, values) {
  var popup = document.getElementById(popupid);

  while (popup.hasChildNodes()) {
    popup.removeChild(popup.firstChild);
  }

  for (i=0; i<values.length; i++)
    {
      menit=document.createElement("menuitem");
      menit.setAttribute('id',popupid + '.' + values[i]["id"]);
      menit.setAttribute('label',values[i]["name"]);
      menit.setAttribute('value',values[i]["id"]);
      popup.appendChild(menit);
    }
}

function updateUsaSimpla() {
  var req = new HTTPDataRequest("/usisimple.xml");
  updateValueList(req, "usa-simpla-popup");
}

function updateBroasca() {
  var req = new HTTPDataRequest("/broaste.xml");
  updateValueList(req, "broasca-popup");
}

function updateCilindru() {
  var req = new HTTPDataRequest("/cilindrii.xml");
  updateValueList(req, "cilindru-popup");
}

function updateSild() {
  var req = new HTTPDataRequest("/silduri.xml");
  updateValueList(req, "sild-popup");
}

function updateYalla() {
  var req = new HTTPDataRequest("/yalla.xml");
  updateValueList(req, "yalla-popup");
}

function updateVizor() {
  var req = new HTTPDataRequest("/vizoare.xml");
  updateValueList(req, "vizor-popup");
}

function save_usmpl() {
  var req = new HTTPDataRequest("/saveusasimpla.do");
  if(! prepare_usmpl_form(req))
    return false;

  if(post_data(req)) {
    updateUsaSimpla();
    updateOferta();
    setView();
    alert("Salvare OK");
  }
}

function add_usmpl() {
  var req = new HTTPDataRequest("/addusasimpla.do");
  if(! prepare_usmpl_form(req))
    return false;

  if(post_data(req)) {
    updateUsaSimpla();
    alert("Adaugare OK");
  }
}

function save_ustd() {
  var req = new HTTPDataRequest("/saveusastd.do");
  if(! prepare_ustd_form(req))
    return false;

  if(post_data(req)) {
    updateOferta();
    setView();
    alert("Salvare OK");
  }
}

function add_ustd() {
  var req = new HTTPDataRequest("/addusastd.do");
  if(! prepare_ustd_form(req))
    return false;

  if(post_data(req)) {
    updateOferta();
    setView();
    alert("Adaugare OK");
  }
}

function prepare_usmpl_form(request) {
  var rowNo = currentIndex();

  var name = document.getElementById("denumire-produs").value;
  var code = document.getElementById("cod-produs").value;
  var description = document.getElementById("varianta-constructiva").value;
  var sellPrice = document.getElementById("pret-std-fara-tva").value;
  var id = rowNo>-1? rsOferta[rowNo]["usa.id"]: "null";
  
  request.add("id", id);
  request.add("code", code);
  request.add("name", name);
  request.add("description", description);
  request.add("sellPrice", sellPrice);

  return true;
}

function prepare_ustd_form(request) {
  var rowNo = currentIndex();

  if(!document.getElementById("usa-simpla").selectedItem) {
    alert("Eroare: Trebuie sa alegeti o usa");
    return false;
  }
  if(!document.getElementById("broasca").selectedItem) {
    alert("Eroare: Trebuie sa alegeti o broasca");
    return false;
  }
  if(!document.getElementById("cilindru").selectedItem) {
    alert("Eroare: Trebuie sa alegeti un cilindru");
    return false;
  }
  if(!document.getElementById("sild").selectedItem) {
    alert("Eroare: Trebuie sa alegeti un sild");
    return false;
  }
  if(!document.getElementById("yalla").selectedItem) {
    alert("Eroare: Trebuie sa alegeti o yalla");
    return false;
  }
  if(!document.getElementById("vizor").selectedItem) {
    alert("Eroare: Trebuie sa alegeti un vizor");
    return false;
  }


  var usaid = document.getElementById("usa-simpla").selectedItem.value;
  var broascaid = document.getElementById("broasca").selectedItem.value;
  var cilindruid = document.getElementById("cilindru").selectedItem.value;
  var sildid = document.getElementById("sild").selectedItem.value;
  var yallaid = document.getElementById("yalla").selectedItem.value;
  var vizorid = document.getElementById("vizor").selectedItem.value;
  var id = rowNo>-1? rsOferta[rowNo]["id"]: "null";

  

  request.add("usaid", usaid);
  request.add("broascaid", broascaid);
  request.add("cilindruid", cilindruid);
  request.add("sildid", sildid);
  request.add("yallaid", yallaid);
  request.add("vizorid", vizorid);
  request.add("id", id);

  return true;
}

function syncForms() {
  if(currentIndex() < 0)
    return;

  var row = rsOferta[currentIndex()];
  
  // usa simpla
  document.getElementById("denumire-produs").value = row["usa.name"];
  document.getElementById("cod-produs").value = row["usa.code"];
  document.getElementById("varianta-constructiva").value = row["usa.description"];
  document.getElementById("pret-std-fara-tva").value = row["usa.sellPrice"];
  
  // echipare usa
  document.getElementById("usa-simpla").selectedItem = 
    document.getElementById("usa-simpla-popup." + row["usa.id"]);
  document.getElementById("broasca").selectedItem = 
    document.getElementById("broasca-popup." + row["broasca.id"]);
  document.getElementById("cilindru").selectedItem = 
    document.getElementById("cilindru-popup." + row["cilindru.id"]);
  document.getElementById("sild").selectedItem = 
    document.getElementById("sild-popup." + row["sild.id"]);
  document.getElementById("yalla").selectedItem = 
    document.getElementById("yalla-popup." + row["yalla.id"]);
  document.getElementById("vizor").selectedItem = 
    document.getElementById("vizor-popup." + row["vizor.id"]);

  document.getElementById('command-saveusmpl').setAttribute('disabled', false);
  document.getElementById('command-saveustd').setAttribute('disabled', false);
}

function editUsaStd() {
  syncForms();
  tabbox=document.getElementById('maintab');
  tabbox.selectedIndex=1;
}

