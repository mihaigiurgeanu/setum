// businessCategory.js
//
// Methods that deal with objects having business categories.
// These objects are objects containing other objects. The business
// category defines the service that is handling the business rules
// fot the contained object and the UI module that deals with editing or
// selecting the contained object.


// the file that holds the definitions for categoryBusiness resources
var categories_uri = "chrome://setum/content/categories.xml";
var modules_uri = "chrome://setum/content/modules.rdf";

var rdfService = Components.classes["@mozilla.org/rdf/rdf-service;1"].getService(Components.interfaces.nsIRDFService);

// open the rdf datasources
var categories_ds = rdfService.GetDataSource(categories_uri);
var modules_ds = rdfService.GetDataSource(modules_uri);



// Creates a new BusinessCategory object.
// Params:
// category	- is the rdf category id
// theForm	- is the object that handles the form that wants
//		- to call on methods on this business category
function BusinessCategory(category, theForm) {

  this.category = rdfService.GetResource(category);

  // Predicates:
  this.title_predicate = rdfService.GetResource("http://www.kds.ro/erp/businessCategory#title");

  // Points to the service url to which requests will be submited for loading or adding a new object
  this.service_predicate = rdfService.GetResource("http://www.kds.ro/erp/businessCategory#service");

  // Points to the module resource found in the modules.rdf; this is the dialog window to be
  // opened for editing (and adding) a new object
  this.module_predicate = rdfService.GetResource("http://www.kds.ro/erp/businessCategory#module");

  // Points to the command name to be sent to the service for adding a new object
  this.onnew_predicate = rdfService.GetResource("http://www.kds.ro/erp/businessCategory#onnew");

  // Points to the command name to be sent to the service to load a specific object in
  // this category
  this.onload_predicate = rdfService.GetResource("http://www.kds.ro/erp/businessCategory#load");

  // Points to the command name to be sent to the service to duplicate the currently loaded object
  this.duplicate_predicate = rdfService.GetResource("http://www.kds.ro/erp/businessCategory#duplicate");

  // The predicate used in modules.rdf to point to the xulfile containing the definition
  // of the object to be opened and edited
  this.xulfile_predicate = rdfService.GetResource("http://www.kds.ro/erp/modules/definition#xulfile");


  this.theForm = theForm;


  // Sends a load request to the service containing the business
  // logic of the object in this business category
  this.load = function load_related_object(id) {
    var req = new HTTPDataRequest(get_literal(categories_ds, 
					      this.category, 
					      this.service_predicate));
    var load_command = get_literal(categories_ds,
				   this.category,
				   this.onload_predicate)
    req.add("command", get_literal(categories_ds, 
				   this.category, 
				   this.service_predicate));
    req.add("id", id);		// old style for id param (deprecated)
    req.add("param0", id);	// new style for calling load method

    this.theForm.post_request(req);

  };


  // Opens a new dialog window for creating a new object in this business category.
  // It first sends a "new" command to the service for this category without any
  // params. Then opens the dialog associated with this business category.
  //
  // After closing the dialog with OK button, the dialog calls the 
  // select_handler.select(id) method with the id of the created object.
  //
  // The name of the "new" command is taken from the description of the
  // business category (in the rdf file) with the predicate onnew_predicate.
  //
  // A sample select handler:
  // select_handler = {
  //	theForm: theForm,
  //	category: "http://bla/bla/bla",
  //	select: function add_new(id) {
  //		var req = this.theForm.get_request();
  //		req.add("command", "addItem");
  //		req.add("param0", id);
  //		req.add("param1", this.category.Value);
  //		this.theForm.post_request(req);
  //		load_items(); // the method that refreshes the tree listing
  //	}
  // } 
  this.addnew_dlg = function open_addnew_dialog(select_handler) {
    if(categories_ds.hasArcOut(this.category, this.onnew_predicate)) {
      var req = new HTTPDataRequest(get_literal(categories_ds,
						this.category,
						this.service_predicate));
      req.add("command", get_literal(categories_ds,
				     this.category,
				     this.onnew_predicate));
      req.execute(); // just send the addnew command; ignore the response
      
    }
    var module_res = get_resource(categories_ds, 
				  this.category,
				  this.module_predicate);

    window.openDialog(get_literal(modules_ds, 
				  module_res, this.xulfile_predicate),
		      get_literal(modules_ds, 
				  module_res, this.xulfile_predicate),
		      "chrome, resizable, scrollbars, modal", select_handler);
  };



  // Opens a new dialog window for editing the data of the current object in 
  // this business category.
  // It first sends a "load" command to the service for this category with one
  // param indicating the id of the object to be edited. Then opens the dialog 
  // associated with this business category.
  // After closing the dialog with OK button, the dialog calls the
  // select_handler.select(id) method. You should provide the select_handler object.
  //
  // The select_handler can be something like this:
  // select_handler = {
  //	theForm: theForm,
  //	select: function update_command(id) {
  //		var req = this.theForm.get_request();
  //		req.add("command", "change");
  //		req.add("field", <field name>);
  //		req.add("value", id);
  //		this.theForm.post_request(req);
  //	}
  // }
  //
  // Calling change command will refresh the calling form data.
  this.edit_dlg = function open_edit_dialog(objectId, select_handler) {

    if(categories_ds.hasArcOut(this.category, this.onload_predicate)) {
      var req = new HTTPDataRequest(get_literal(categories_ds,
						this.category,
						this.service_predicate));
      req.add("command", get_literal(categories_ds,
				     this.category,
				     this.onload_predicate));
      req.add("id", objectId);
      req.add("param0", objectId); /* send parameter for both old style load
				    * and general style call procedures.
				    */
      req.execute(); // just send the addnew command; ignore the response
      
    }

    var module_res =get_resource(categories_ds, 
				 this.category, 
				 this.module_predicate);
    
    window.openDialog(get_literal(modules_ds, 
				  module_res, this.xulfile_predicate),
		      get_literal(modules_ds, 
				  module_res, this.xulfile_predicate),
		      "chrome, resizable, scrollbars, modal", select_handler);
  };



  // Duplicates the current object and opens a new dialog window for editing 
  // its data.
  // It first sends a "load" command to the service for this category with one
  // param indicating the id of the object to be edited. After this, calls
  // "duplicate" method. Then opens the dialog associated with this business category.
  // After closing the dialog with OK button, the dialog calls the
  // select_handler.select(id) method. You should provide the select_handler object.
  //
  // The select_handler can be something like this:
  // select_handler = {
  //	theForm: theForm,
  //	select: function update_command(id) {
  //		var req = this.theForm.get_request();
  //		req.add("command", "change");
  //		req.add("field", <field name>);
  //		req.add("value", id);
  //		this.theForm.post_request(req);
  //	}
  // }
  //
  // Calling change command will refresh the calling form data.
  this.duplicate_dlg = function open_duplicate_dialog(objectId, select_handler) {

    if(categories_ds.hasArcOut(this.category, this.onload_predicate)) {
      var req = new HTTPDataRequest(get_literal(categories_ds,
						this.category,
						this.service_predicate));
      req.add("command", get_literal(categories_ds,
				     this.category,
				     this.onload_predicate));
      req.add("id", objectId);
      req.add("param0", objectId); /* send parameter for both old style load
				    * and general style call procedures.
				    */
      req.execute(); // just send the addnew command; ignore the response
      
    }


    if(categories_ds.hasArcOut(this.category, this.duplicate_predicate)) {
      var req = new HTTPDataRequest(get_literal(categories_ds,
						this.category,
						this.service_predicate));
      req.add("command", get_literal(categories_ds,
				     this.category,
				     this.duplicate_predicate));
      req.execute(); // just send the addnew command; ignore the response
      
    }

    var module_res =get_resource(categories_ds, 
				 this.category, 
				 this.module_predicate);
    
    window.openDialog(get_literal(modules_ds, 
				  module_res, this.xulfile_predicate),
		      get_literal(modules_ds, 
				  module_res, this.xulfile_predicate),
		      "chrome, resizable, scrollbars", select_handler);
  };
}

function get_literal(datasource, res, predicate) {
  var target = datasource.GetTarget(res, predicate, true);
  if(target instanceof Components.interfaces.nsIRDFLiteral) {
    return target.Value;
  }
  alert(target + " is not instance of Componets.interfaces.nsIRDFLiteral");
  return "";
}

function get_resource(datasource, res, predicate) {
  var target = datasource.GetTarget(res, predicate, true);
  if(target instanceof Components.interfaces.nsIRDFResource) {
    return target;
  }
  alert(target + " is not instance of Componets.interfaces.nsIRDFResource");
  return "";
}
