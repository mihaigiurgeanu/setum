/* httpdata.js - manages the http requests for data */

const SERVER_URL = "http://setumserver:9000/setum";

//Create a HTTPDataRequest Object
function HTTPDataRequest(target, prefix) {
    //Set some default variables
    this.parms = new Array();
    this.parmsIndex = 0;
    this.prefix = prefix;

    //Set the server url
    this.server = SERVER_URL + target;

    //Add two methods
    this.execute = httpRequestExecute;
    this.add = httpRequestAdd;
}

function httpRequestAdd(name,value) {
    //Add a new pair object to the params
    this.parms[this.parmsIndex] = new Pair(name,value);
    this.parmsIndex++;
}

function httpRequestExecute() {
    //Set the server to a local variable
    var targetURL = this.server;
  
    //Try to create our XMLHttpRequest Object
    try {
	var httpRequest = new XMLHttpRequest();
    }catch (e){
	alert('Error creating the connection!');
	return false;
    }
  
    //Make the connection and send our data
    try {
	var txt = "";

	for(var i in this.parms) {
	    if(i > 0)
		txt += '&';

	    txt += this.parms[i].name+'='+encodeURIComponent(this.parms[i].value);
	}
	//     alert("Request body: " + txt);

	//Two options here, only uncomment one of these
	//GET REQUEST
	//httpRequest.open("GET", targetURL+'?'+txt, false, null, null);  

	//POST REQUEST EXAMPLE
	log("POST request to " + targetURL);
	log("POST request " + txt);
	httpRequest.open("POST", targetURL, false, null, null);  
	httpRequest.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	httpRequest.send(txt);


    }catch (e){
	alert('An error has occured calling the external site: '+e);
	return false;
    }

    //Make sure we received a valid response
    switch(httpRequest.readyState) {
    case 1:
    case 2:
    case 3:
	alert('Bad Ready State: '+httpRequest.status);
	return false;
	break;
    case 4:
	if(httpRequest.status !=200) {
	    alert('The server respond with a bad status code: '+httpRequest.status);
	    return false;
	} else {
	    return new HTTPDataResponse(httpRequest.responseXML);
	}
	break;
    default:
	return false;
    }

    return false;
}

function HTTPDataResponse(xmldoc, prefix) {
    var i;
    var j;

    var responseElem = xmldoc.documentElement;
    var responseData = responseElem.childNodes;
    var responseAttributes = responseElem.attributes;

    var field_prefix = prefix?prefix + ".":"";
  
    this.records = new Array();
    this.length = 0;
    this.vlCount = 0;
    this.valueLists = new Array();
    this.viCount = 0;
    this.vi = new Array();
    this.message = "";

    log("Parsing xml response");

    for(i=0; i<responseData.length; i++) {
	var node = responseData.item(i);
	switch (node.nodeType) {
	case node.ELEMENT_NODE:
	    log("Found element " + node.tagName);

	    if(node.tagName == 'return') {
		if(node.firstChild)
		    this.message = node.firstChild.textContent;
		this.code = node.getAttribute("code");

	    } else if (node.tagName == 'record') {
		this.records[this.length] = new Array();
		var fields = node.getElementsByTagName("field");
		for(j=0; j<fields.length; j++) {
		    var field = fields.item(j);
		    if(field.firstChild)
			this.records[this.length][field_prefix + field.getAttribute("name")] = field.firstChild.textContent;
		    else
			this.records[this.length][field_prefix + field.getAttribute("name")] = "";
		}
		this.length++;
	    } else if(node.tagName == 'value-list') {
		log("Found value list with name <<" + node.getAttribute('name') +
		    ">>.");
		var vlc = new ValueListContainer(field_prefix + node.getAttribute('name'));
		var vlItems = node.childNodes;
		for(j=0; j < vlItems.length; j++) {
		    var vlItem = vlItems[j];
		    if(vlItem.nodeType != vlItem.ELEMENT_NODE || 
		       vlItem.tagName != "vl-item") {
			continue;
		    }
		    var valueTags = vlItem.getElementsByTagName("value");
		    var labelTags = vlItem.getElementsByTagName("label");
		    if(valueTags.length>0 && labelTags.length>0) {
			vlc.valuelist[vlc.length] = new ValueListItem
			    (valueTags[0].textContent, labelTags[0].textContent);

			vlc.length++;
		    }
		}
		this.valueLists[this.vlCount] = vlc;
		log("Value list no " + this.vlCount +
		    " named <<" + vlc.name + ">> added to the parsed response.");
		this.vlCount ++;
	    } else if(node.tagName == 'validation-info') {
		var vi  = new ValidationInfo();
		vi.subject = node.getAttribute("subject");
		vi.message = node.getAttribute("message");
		if(node.firstChild)
		    vi.data = node.firstChild.textContent;

		this.vi[this.viCount ++] = vi;
	    }
	    break;

	default:
	    break;
	}
    }
    log("Response parsing ended.");
}

function ValueListContainer(name) {
    this.name = name;
    this.length = 0;
    this.valuelist = new Array();
}

function ValueListItem(value, label) {
    this.value = value;
    this.label = label;
}

function ValidationInfo() {
    this.subject = "";
    this.message = "";
    this.data = "";
}

function post_data(request) {
    var result;
    var response = request.execute();
    if(response) {
	if(response.code == 0)
	    result = true;
	else {
	    alert("Error posting data to the database. " +
		  "Response code is: " + response.code +
		  "\nMessage:\n" + response.message);
	    result = false;
	}
    } else {
	alert("Error posting data to the database. " +
	      "No response received from server.");
	result = false;
    }

    return result;
}

//Utility Pair class
function Pair(name,value) {
    this.name = name;
    this.value = value;
}

