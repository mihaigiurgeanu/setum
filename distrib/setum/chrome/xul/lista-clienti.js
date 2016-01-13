function init()
{
  generateTariMenuList();
  setView();
}

function getArrayOfData()
{
  var req = new HTTPDataRequest("/clients.xml");
  var response = req.execute();
  if(response) {
    if(response.code == 0)
      return response.records;
    else {
      alert("Error while retrieving clients list. Response code is: " + 
	    response.code +
	    "\nMessage:\n" + response.message);
      return new Array();
    }
  }
  alert("Error while retrieving clients. No response received from server.");
  return new Array();
}

var arrayOfData;
var arrayTari = getArrayTari();
var treeView;
var arrayPersContact;


function setView()
{
  arrayOfData = getArrayOfData();
  
  treeView = {
    rowCount : arrayOfData.length,
    getCellText : function(row,column)
    {
      if (column=="tc-tip-pers")
	{
	  if (arrayOfData[row]["isCompany"]==0)
	    return "Persoana fizica";
	  else
	    return "Persoana juridica";
	}
      if (column=="tc-nume-companie")
	{
	  return arrayOfData[row]["companyName"];
	}
      if (column=="tc-nume")
	{
	  return arrayOfData[row]["lastName"];
	}
      if (column=="tc-prenume")
	{
	  return arrayOfData[row]["firstName"];
	}
      if (column=="tc-adresa")
	{
	  return arrayOfData[row]["address"];
	}
      if (column=="tc-cod-postal")
	{
	  return arrayOfData[row]["postalCode"];
	}
      if (column=="tc-oras")
	{
	  return arrayOfData[row]["city"];
	}
      if (column=="tc-tara")
	{
	  for (i=0; i < arrayTari.length; i++)
	    {
	      if (arrayTari[i][0]==arrayOfData[row]["countryCode"]) {
		return arrayTari[i][1];
	      }
	    }
	  return "Unknown";
	}
      if (column=="tc-cod-fiscal")
	{
	  return arrayOfData[row]["companyCode"];
	}
      if (column=="tc-telefon")
	{
	  return arrayOfData[row]["phone"];
	}
      if (column=="tc-cont-iban")
	{
	  return arrayOfData[row]["iban"];
	}
      if (column=="tc-banca")
	{
	  return arrayOfData[row]["bank"];
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

  document.getElementById('command-saveclient')
    .setAttribute('disabled', 'true');
  document.getElementById('lista-clienti-tree').view=treeView;
}

function initformdata()
{
  listatabbox=document.getElementById('clienti-tabbox');
  listatabbox.selectedIndex=1;
  copyvalues();
  setTreeContentForPersoaneContact();
}



function initformdataPC()
{
  listatabbox=document.getElementById('perscont-tabbox');
  listatabbox.selectedIndex=1;
  copyvaluesPC();
}

function copyvalues()
{
  var clienti_tree=document.getElementById('lista-clienti-tree');
  var v_tip_pers=arrayOfData[clienti_tree.currentIndex]["isCompany"];
  var v_nume_companie=arrayOfData[clienti_tree.currentIndex]["companyName"];
  var v_nume=arrayOfData[clienti_tree.currentIndex]["lastName"];
  var v_prenume=arrayOfData[clienti_tree.currentIndex]["firstName"];
  var v_adresa=arrayOfData[clienti_tree.currentIndex]["address"];
  var v_cod_postal=arrayOfData[clienti_tree.currentIndex]["postalCode"];
  var v_oras=arrayOfData[clienti_tree.currentIndex]["city"];
  var v_tara=arrayOfData[clienti_tree.currentIndex]["countryCode"];
  var v_cod_fiscal=arrayOfData[clienti_tree.currentIndex]["companyCode"];
  var v_telefon=arrayOfData[clienti_tree.currentIndex]["phone"];
  var v_cont_iban=arrayOfData[clienti_tree.currentIndex]["iban"];
  var v_banca=arrayOfData[clienti_tree.currentIndex]["bank"];

  if (v_tip_pers==0)
    document.getElementById('tip-persoana').selectedItem=document.getElementById('pers-fiz');
  else
    document.getElementById('tip-persoana').selectedItem=document.getElementById('pers-jur');
  document.getElementById('nume-companie').value=v_nume_companie;
  document.getElementById('nume').value=v_nume;
  document.getElementById('prenume').value=v_prenume;
  document.getElementById('adresa').value=v_adresa;
  document.getElementById('cod-postal').value=v_cod_postal;
  document.getElementById('oras').value=v_oras;
  document.getElementById('tara').selectedItem=document.getElementById('tara.'+v_tara);
  document.getElementById('cod-fiscal').value=v_cod_fiscal;
  document.getElementById('telefon').value=v_telefon;
  document.getElementById('cont-iban').value=v_cont_iban;
  document.getElementById('banca').value=v_banca;
  document.getElementById('observatii').value=arrayOfData[clienti_tree.currentIndex]["coment"];

  document.getElementById('command-saveclient')
    .setAttribute('disabled', 'false');

}

function copyvaluesPC()
{
  var  clienti_tree=document.getElementById('lista-clienti-tree');
  var idclient=arrayOfData[clienti_tree.currentIndex]["id"];
  //var arrayPersContact = getPersoaneContactForClient(idclient);

  var pc_tree=document.getElementById('persoane-contact-tree');
  var v_pcnume=arrayPersContact[pc_tree.currentIndex]["lastName"];
  var v_pcprenume=arrayPersContact[pc_tree.currentIndex]["firstName"];
  var v_pcdepartament=arrayPersContact[pc_tree.currentIndex]["department"];
  var v_pctelefon=arrayPersContact[pc_tree.currentIndex]["phone"];
  var v_pcmobil=arrayPersContact[pc_tree.currentIndex]["mobile"];
  var v_pcfax=arrayPersContact[pc_tree.currentIndex]["fax"];
  var v_pcemail=arrayPersContact[pc_tree.currentIndex]["email"];
  var v_pcfunctie=arrayPersContact[pc_tree.currentIndex]["title"];
  var v_pcobservatii=arrayPersContact[pc_tree.currentIndex]["comment"];


  document.getElementById('pc-nume').value=v_pcnume;
  document.getElementById('pc-prenume').value=v_pcprenume;
  document.getElementById('pc-departament').value=v_pcdepartament;
  document.getElementById('pc-telefon').value=v_pctelefon;
  document.getElementById('pc-mobil').value=v_pcmobil;
  document.getElementById('pc-fax').value=v_pcfax;
  document.getElementById('pc-email').value=v_pcemail;
  document.getElementById('pc-functie').value=v_pcfunctie;
  document.getElementById('pc-observatii').value=v_pcobservatii;

  document.getElementById('command-savecontact')
    .setAttribute('disabled', 'false');

}

function getArrayTari()
{
  var arrayTari = new Array();
  arrayTari[0] = new Array("ROM", "Romania");
  return arrayTari;
}

function generateTariMenuList()
{
  var arrayTari=getArrayTari();
  for (i=0; i<arrayTari.length; i++)
    {
      menit=document.createElement("menuitem");
      menit.setAttribute('id','tara.'+arrayTari[i][0]);
      menit.setAttribute('label',arrayTari[i][1]);
      menit.setAttribute('value',arrayTari[i][0]);
      document.getElementById('tara-popup').appendChild(menit);
    }
  document.getElementById('tara').selectedIndex=0;
}

function getAllPersoaneContact()
{
  var req = new HTTPDataRequest("/contacts.xml");
  var response = req.execute();
  if(response) {
    if(response.code == 0)
      return response.records;
    else {
      alert("Error while retrieving contacts list. Response code is: " + 
	    response.code +
	    "\nMessage:\n" + response.message);
      return new Array();
    }
  }
  alert("Error while retrieving contacts. No response received from server.");
  return new Array();
}

function setTreeContentForPersoaneContact()
{
  var  clienti_tree=document.getElementById('lista-clienti-tree');
  var idclient=arrayOfData[clienti_tree.currentIndex]["id"];
  arrayPersContact = getPersoaneContactForClient(idclient);
  var treeViewPC = 
    {
      rowCount : arrayPersContact.length,
      getCellText : function(row,column)
      {
	if (column=="tc-pc-nume")
	  {
	    return arrayPersContact[row]["lastName"];
	  }
	if (column=="tc-pc-prenume")
	  {
	    return arrayPersContact[row]["firstName"];
	  }
	if (column=="tc-pc-departament")
	  {
	    return arrayPersContact[row]["department"];
	  }
	if (column=="tc-pc-telefon")
	  {
	    return arrayPersContact[row]["phone"];
	  }
	if (column=="tc-pc-mobil")
	  {
	    return arrayPersContact[row]["mobile"];
	  }
	if (column=="tc-pc-fax")
	  {
	    return arrayPersContact[row]["fax"];
	  }
	if (column=="tc-pc-email")
	  {
	    return arrayPersContact[row]["email"];
	  }
	if (column=="tc-pc-functie")
	  {
	    return arrayPersContact[row]["title"];
	  }
	if (column=="tc-pc-observatii")
	  {
	    return arrayPersContact[row]["comment"];
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

  document.getElementById('command-savecontact')
    .setAttribute('disabled', 'true');
  document.getElementById('persoane-contact-tree').view=treeViewPC;

}


function getPersoaneContactForClient(idclient)
{
  var arrayAllPersContact = getAllPersoaneContact();
  var arrayPC = new Array();
  j=0;
  for (i=0; i<arrayAllPersContact.length; i++)
    {
      if (arrayAllPersContact[i]["clientId"]==idclient)
	{
	  arrayPC[j]=arrayAllPersContact[i];
	  j++;
	}
    }
  return arrayPC;
}


// post data to the server (for save)
function saveClient() {
  var req = new HTTPDataRequest("/saveclient.do");
  if(! prepare_client_form(req))
    return false;

  if(post_data(req)) {
    setView();
    alert("Datele au fost salvate");
  }

}

// post data to the server (for new)
function addClient() {
  var req = new HTTPDataRequest("/addclient.do");
  if(! prepare_client_form(req))
    return false;

  if(post_data(req)) {
    setView();
    alert("Datele au fost salvate");
  }
}

// post data to the server (for save)
function saveContact() {
  var req = new HTTPDataRequest("/savecontact.do");
  if(! prepare_contact_form(req))
    return false;

  if(post_data(req)) {
    setTreeContentForPersoaneContact();
    alert("Datele au fost salvate");
  }

}

// post data to the server (for new)
function addContact() {
  var req = new HTTPDataRequest("/addcontact.do");
  if(! prepare_contact_form(req))
    return false;

  if(post_data(req)) {
    setTreeContentForPersoaneContact();
    alert("Datele au fost salvate");
  }
}

// build the client form request
function prepare_client_form(req) {
  var tree = document.getElementById('lista-clienti-tree');

  if(tree.currentIndex > -1) {
    req.add("id", arrayOfData[tree.currentIndex]["id"]);
  }

  var isCompany = document.getElementById('tip-persoana').selectedItem.value;
  var companyName = document.getElementById('nume-companie').value;
  var lastName = document.getElementById('nume').value;
  var firstName = document.getElementById('prenume').value;
  var address = document.getElementById('adresa').value;
  var postalCode = document.getElementById('cod-postal').value;
  var city = document.getElementById('oras').value;
  var countryCode = document.getElementById('tara').selectedItem.value;
  var companyCode = document.getElementById('cod-fiscal').value;
  var phone = document.getElementById('telefon').value;
  var iban = document.getElementById('cont-iban').value;
  var bank = document.getElementById('banca').value;
  var comment = document.getElementById('observatii').value;

  req.add("isCompany", isCompany);
  req.add("companyName", companyName);
  req.add("lastName", lastName);
  req.add("firstName", firstName);
  req.add("address", address);
  req.add("postalCode", postalCode);
  req.add("city", city);
  req.add("countryCode", countryCode);
  req.add("companyCode", companyCode);
  req.add("phone", phone);
  req.add("iban", iban);
  req.add("bank", bank);
  req.add("comment", comment);

  return true;
}

// build the contact form request
function prepare_contact_form(req) {
  var tree = document.getElementById('persoane-contact-tree');
  if(tree.currentIndex > -1) {
    req.add("id", arrayOfData[tree.currentIndex]["id"]);
    req.add("clientId", arrayOfData[tree.currentIndex]["clientId"]);
  }

  var lastName = document.getElementById('pc-nume').value;
  var firstName = document.getElementById('pc-prenume').value;
  var department = document.getElementById('pc-departament').value;
  var phone = document.getElementById('pc-telefon').value;
  var mobile = document.getElementById('pc-mobil').value;
  var fax = document.getElementById('pc-fax').value;
  var email = document.getElementById('pc-email').value;
  var title = document.getElementById('pc-functie').value;
  var comment = document.getElementById('pc-observatii').value;

  req.add("lastName", lastName);
  req.add("firstName", firstName);
  req.add("department", department);
  req.add("phone", phone);
  req.add("mobile", mobile);
  req.add("fax", fax);
  req.add("email", email);
  req.add("title", title);
  req.add("comment", comment);

  return true;
}

