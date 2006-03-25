// standardoffer.js

function FormObject() {
    this.text_fields = new Array();
    this.combo_fields = new Array();
    this.radio_fields = new Array();
    this.hidden_fields = new Array();

    this.values = new Array();
    this.do_link = "";

    // main methods
    this.setupEventListeners = setupEventListeners; 
    this.update_form = update_form;
    this.post_request = post_request;
    this.post_save_request = post_save_request;

    // afterpost method will be called after a post request
    // finishes; the default method does nothing
    // put your own method to do some action (like enabling or
    // disableing controls) after new data is received from server
    this.afterpost = function () {log ("default afterpost() called"); };

    // convenience methods
    this.set_value = set_value;
    this.load_current = load_current;
    this.get_request = get_request;
    this.get_loaded_id = get_loaded_id;

    // internal methods
    this.update_fields = update_fields;


    // DEPRECATED
    // use instead:
    //  var req = theForm.getRequest();
    //  req.add("command", "removeObject");
    //  theForm.post_request(req);
    this.remove_item = remove_item;

    // DEPRECATED
    // use instead:
    //  var req = theForm.getRequest();
    //  req.add("command", "saveFormData");
    //  theForm.post_save_request(req);
    this.save = save;
    this.save_sub = save_sub;

    // DEPRECATED
    // use instead:
    //  var req = theForm.getRequest();
    //  req.add("command", "newFormData");
    //  theForm.post_request(req);
    this.addnew = addnew;
    this.addnew_sub = addnew_sub;

    // DEPRECATED
    // use instead:
    //  var req = theForm.getRequest();
    //  req.add("command", "loadFormData");
    //  req.add("param0", paramValue);
    //  theForm.post_request(req);
    this.load_form = load_form;
    this.load_subform = load_subform;

    // DEPRECATED
    // use instead:
    //  var req = theForm.get_request();
    //  req.add("command", "loadObjects");
    //  req.add("param0", paramValue);
    //  var records = load_records(req);
    this.load_listing = load_listing;
    this.load_param_listing = load_param_listing;
    this.load_sub_listing = load_sub_listing;



    this.text_event_listener = { handleEvent: text_changed };
    this.combo_event_listener = { handleEvent: combo_changed };
    this.radio_event_listener = { handleEvent: radio_changed };


}

function setupEventListeners() {
    var i;

    for(i=0; i<this.text_fields.length; i++) {
	//log("Set up event listener for: " + this.text_fields[i]);
	if(document.getElementById(this.text_fields[i])) {
	    document.getElementById(this.text_fields[i])
		.addEventListener("change", this.text_event_listener, true);
	} else {
	    log("setupEventListeners Warning: Text Field " 
		+ this.text_fields[i] 
		+ " does not exists in this document");
	}
    }
  
    for(i=0; i<this.combo_fields.length; i++) {
	//log("Set up event listener for: " + this.combo_fields[i]);
	if(document.getElementById(this.combo_fields[i])) {
	    document.getElementById(this.combo_fields[i])
		.addEventListener("command", this.combo_event_listener, true);
	} else {
	    log("setupEventListeners Warning: Combo Field " 
		+ this.combo_fields[i] 
		+ " does not exists in this document");
	}
    }

    for(i=0; i<this.radio_fields.length; i++) {
	//log("Set up event listener for: " + this.radio_fields[i]);
	if(document.getElementById(this.radio_fields[i])) {
	    document.getElementById(this.radio_fields[i])
		.addEventListener("RadioStateChange", this.radio_event_listener, true);
	} else {
	    log("setupEventListeners Warning: Radio Field " 
		+ this.radio_fields[i] 
		+ " does not exists in this document");
	}
    }
}

function combo_changed(event) {

    var control = event.currentTarget;
    var req = new HTTPDataRequest(theForm.do_link);
    req.add("command", "change");
    req.add("field", control.id);
    if(control.selectedItem) {
	log("Field changed: " + control.id + ". New value: " + control.selectedItem.value);
	req.add("value", control.selectedItem.value);
    } else {
	// nothing selected
	log("Update field: " + control.id + ", no value selected.");
	req.add("value", "");
    }

    theForm.post_request(req);
}

function radio_changed(event) {
    var control = event.currentTarget;

    log("Field changed: " + control.id + ". New value: " + control.selectedItem.value);

    var req = new HTTPDataRequest(theForm.do_link);
    req.add("command", "change");
    req.add("field", control.id);
    if(control.selectedItem) {
	if(log) log("Update field: " + control.id + " with value: " + control.selectedItem.value);
	req.add("value", control.selectedItem.value);
    } else {
	// nothing selected
	log("Update field: " + control.id + ", no value selected.");
	req.add("value", "");
    }


    theForm.post_request(req);
}

function text_changed(event) {
    var control = event.target;

    log("Field changed: " + control.id + ". New value: " + control.value);

    var req = new HTTPDataRequest(theForm.do_link);
    req.add("command", "change");
    req.add("field", control.id);
    req.add("value", control.value);

    theForm.post_request(req);
}

function set_value(fieldName, fieldValue) {
    log("Send set field value: " + fieldName + ". New value: " + fieldValue);
    log(" ... to " + this.do_link);
    var req = new HTTPDataRequest(this.do_link);
    req.add("command", "change");
    req.add("field", fieldName);
    req.add("value", fieldValue);

    this.post_request(req);
}

function post_request(req) {

    var response = req.execute();
    var result;

    if(response) {
	this.update_fields(response);
	this.afterpost();
	result = (response.code == 0);
    } else {
	log("No response received");
	result = false;
    }

    return result;
}

function update_fields(response) {
    if(response) {
	log("Response code: " + response.code);
	log("Response message: " + response.message);
	if(response.message) {
	    alert(response.message + "\n\n" + "Cod: " + response.code);
	}

	log("There are " + response.vlCount + " value lists");
	for(var i=0; i<response.vlCount; i++) {
	    log("Updating value list no " + i);
	    update_valuelists(response.valueLists[i]);
	}
	records = response.records;
	if(records[0]) {
	    this.update_form(records[0]);
	}	
    } else {
	// alert("ERROR receiving the response");
    }
}

function update_form(record) {
    var i;
    for(i=0; i<this.hidden_fields.length; i++) {
	if(record[this.hidden_fields[i]] != undefined) {
	    this.values[this.hidden_fields[i]] = record[this.hidden_fields[i]];
	}
    }

    for(i=0; i<this.text_fields.length; i++) {
	update_text(this.text_fields[i], record);
	if(record[this.text_fields[i]] != undefined) {
	    this.values[this.text_fields[i]] = record[this.text_fields[i]];
	}
    }
  
    for(i=0; i<this.combo_fields.length; i++) {
	update_combo(this.combo_fields[i], record);
	if(record[this.combo_fields[i]] != undefined) {
	    this.values[this.combo_fields[i]] = record[this.combo_fields[i]];
	}
    }

    for(i=0; i<this.radio_fields.length; i++) {
	update_radio(this.radio_fields[i], record);
	if(record[this.radio_fields[i]] != undefined) {
	    this.values[this.radio_fields[i]] = record[this.radio_fields[i]];
	}
    }
}

function update_combo(field, record) {
    log(" Checking new value for field " + field + ": " + record[field]); 
    if(record[field] != undefined) {
	document.getElementById(field).selectedItem = 
	    document.getElementById(field + "." + record[field]);    
    }
}

function update_radio(field, record) {
    log(" Checking new value for field " + field + ": " + record[field]); 
    if(record[field] != undefined) {
	document.getElementById(field).selectedItem = 
	    document.getElementById(field + "." + record[field]);    
    }
}

function update_text(field, record) {
    log(" Checking new value for field " + field + ": " + record[field]); 
    if(record[field] != undefined) {
	document.getElementById(field).value = record[field]; 
    }
}

function update_valuelists(vlc) {
    // vlc is a value list container.
    // a value list container has the following components:
    // vlc.name
    // vlc.length
    // vlc.valuelist[i].value
    // vlc.valuelist[i].label

    log("Update value list <<" + vlc.name + ">>, " +
	"length: " + vlc.length + ".");
    var popup = document.getElementById(vlc.name + "-popup");
    if(popup) {
	remove_children(popup);
	for(i=0; i<vlc.length; i++) {
	    menuitem = document.createElement("menuitem");
	    menuitem.setAttribute("id", vlc.name + "." + vlc.valuelist[i].value);
	    menuitem.setAttribute("label", vlc.valuelist[i].label);
	    menuitem.setAttribute("value", vlc.valuelist[i].value);

	    popup.appendChild(menuitem);
	}
    }
  
}

function remove_children(node) {
    while(node.hasChildNodes())
	node.removeChild(node.firstChild);
}

function get_request() {
    return new HTTPDataRequest(this.do_link);
}

function load_current() {
    var req = new HTTPDataRequest(this.do_link);
    req.add("command", "getCurrentFormData");
    this.post_request(req);
}



function post_save_request(req) {
    // executes the request.
    // The response should have code 0 on success and number of 
    // validation errors otherwise.
    // In case of validation errors, there should be records for each error
    // containing the message field and the fieldId field.

    log("Send save request");
    response = req.execute();
    if(response) {
	if(response.code != 0) {
	    err = response.message  + "\n\n";
	    if(response.records) {
		if(response.records[0]) {
		    var field = document.
			getElementById(response.records[0]['fieldId']);
		    if(field) field.focus();
		}
		for(var i=0; i<response.code; i++) {
		    if(response.records[i]) {
			err += response.records[i]["message"] + "\n\n";
		    }
		}
		if(response.records[response.code]) {
		    this.update_form(response.records[response.code]);
		}
	    }
	    alert(err);
	    this.afterpost();
	    return false;
	} else {
	    if(response.records[0]) {
		this.update_form(response.records[0]);
	    }
	    this.afterpost();
	    return true;
	}
    }
    log("No response for save operation");
    return false;
}

// post the request and return an array of records.
// used for loading listings.
function load_records(req) {
    var response = req.execute();
    if(response) {
	if(response.message) {
	    alert(response.message + "\n\n" + "Cod: " + response.code);
	}
	if(response.code == 0)
	    return response.records;
    }
    return new Array();
}

function get_loaded_id() {
    var req = new HTTPDataRequest(this.do_link);
    req.add("command", "getLoadedPrimaryKey");
    var response = req.execute();
    if(response) {
	if(response.message)
	    alert(response.message);
	if(response.records)
	    return response.records[0]['id'];
    }
    return undefined;
}


/**
 * Opens a dialog to edit one of the objects refered by the current form.
 * It assumes that the current form is in the variable called 'theForm'.
 */
function open_service_dlg(idField, service, xulfile) {
    var req = new HTTPDataRequest(service);
    if(theForm.values[idField] && theForm.values[idField] != 0) {
	req.add("command", "load");
	req.add("id", theForm.values[idField]);
    } else {
	req.add("command", "new");
    }
    var response = req.execute();
    if(response.message) {
	alert(response.message);
    }

    if(response.code == 0) {
	var select_handler = {
	    theForm: theForm,
	    idField: idField,
	    select: function(id) {
		var req = new HTTPDataRequest(this.theForm.do_link);
		req.add("command", "change");
		req.add("field", this.idField);
		req.add("value", id);
		this.theForm.post_request(req);
	    }
	};
	window.openDialog(xulfile, xulfile, "chrome,resizable", 
			  select_handler);
    } else {
	alert("Cod eroare: " + response.code);
    }
}

// DEPRECATED
// use instead:
//  var req = theForm.getRequest();
//  req.add("command", "newFormData");
//  theForm.post_request(req);
function addnew() {
    var req = new HTTPDataRequest(this.do_link);
    req.add("command", "new");
    this.post_request(req);
}

// DEPRECATED
// use instead:
//  var req = theForm.getRequest();
//  req.add("command", "newFormData");
//  theForm.post_request(req);
function addnew_sub(newcmd) {
    var req = new HTTPDataRequest(this.do_link);
    req.add("command", newcmd);
    this.post_request(req);
}


// DEPRECATED
// use instead:
//  var req = theForm.getRequest();
//  req.add("command", "removeObject");
//  theForm.post_request(req);
function remove_item(id) {
    var req = new HTTPDataRequest(this.do_link);
    req.add("command", "removeItem");
    req.add("param0", id);
    this.post_request(req);
}

// DEPRECATED
// use instead:
//  var req = theForm.getRequest();
//  req.add("command", "loadFormData");
//  req.add("param0", paramValue);
//  theForm.post_request(req);
function load_form(id) {
    var req = new HTTPDataRequest(this.do_link);
    req.add("command", "load");
    req.add("id", id);
    this.post_request(req);
}

// DEPRECATED
// use instead:
//  var req = theForm.getRequest();
//  req.add("command", "loadObjectData");
//  req.add("param0", paramValue);
//  theForm.post_request(req);
function load_subform(loadcommand, id) {
    var req = new HTTPDataRequest(this.do_link);
    req.add("command", loadcommand);
    req.add("id", id);
    req.add("param0", id);
    this.post_request(req);
}

// DEPRECATED
// use instead:
//  var req = theForm.getRequest();
//  req.add("command", "saveFormData");
//  theForm.post_save_request(req);
function save() {
    var req = new HTTPDataRequest(this.do_link);
    req.add("command", "save");
    return this.post_save_request(req);  
}

// DEPRECATED
// use instead:
//  var req = theForm.getRequest();
//  req.add("command", "saveFormData");
//  theForm.post_save_request(req);
function save_sub(save_cmd) {
    var req = new HTTPDataRequest(this.do_link);
    req.add("command", save_cmd);
    return this.post_save_request(req);  
}

// DEPRECATED
// use instead:
//  var req = theForm.get_request();
//  req.add("command", "loadObjects");
//  req.add("param0", paramValue);
//  var records = load_records(req);
function load_listing() {
    var req = new HTTPDataRequest(this.do_link);
    req.add("command", "listing");
    return load_records(req);
}


// DEPRECATED
// use instead:
//  var req = theForm.get_request();
//  req.add("command", "loadObjects");
//  req.add("param0", paramValue);
//  var records = load_records(req);
function load_param_listing(param) {
    var req = new HTTPDataRequest(this.do_link);
    req.add("command", "listing");
    req.add("param0", param);
    return load_records(req);
}

// DEPRECATED
// use instead:
//  var req = theForm.get_request();
//  req.add("command", "loadObjects");
//  req.add("param0", paramValue);
//  var records = load_records(req);
function load_sub_listing(listing_cmd) {
    var req = new HTTPDataRequest(this.do_link);
    req.add("command", listing_cmd);
    return load_records(req);
}

