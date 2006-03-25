// businessCategory.js
//
// Methods that deal with objects having business categories.
// These objects are objects containing other objects. The business
// category defines the service that is handling the business rules
// fot the contained object and the UI module that deals with editing or
// selecting the contained object.


// the file that holds the definitions for categoryBusiness resources
var categories_uri = "chrome://setum/xul/categories.xml";
var modules_uri = "chrome://setum/xul/modules.rdf";

var rdfService = Components.classes["@mozilla.org/rdf/rdf-service;1"].getService(Components.interfaces.nsIRDFService);

// open the rdf datasources
var categories_ds = rdfService.GetDataSource(categories_uri);
var modules_ds = rdfService.GetDataSource(modules_uri);

function BusinessCategory(category, theForm, fieldsPrefix, idFieldName) {

  this.category = rdfService.GetResource(category);
  this.title_predicate = rdfService.GetResource("http://www.kds.ro/erp/businessCategory#title");
  this.service_predicate = rdfService.GetResource("http://www.kds.ro/erp/businessCategory#service");
  this.module_predicate = rdfService.GetResource("http://www.kds.ro/erp/businessCategory#module");
  this.onnew_predicate = rdfService.GetResource("http://www.kds.ro/erp/businessCategory#onnew");
  this.onload_predicate = rdfService.GetResource("http://www.kds.ro/erp/businessCategory#load");
  this.xulfile_predicate = rdfService.GetResource("http://www.kds.ro/erp/modules/definition#xulfile");


  this.theForm = theForm;
  this.prefix = fieldsPrefix;
  this.idFieldName = idFieldName;

  this.load = function(id) {
    var req = new HTTPDataRequest(get_literal(categories_ds, 
					      this.category, 
					      this.service_predicate));
    req.add("command", "load");
    req.add("id", id);
    this.theForm.post_request(req, this.prefix);

  };

  this.addnew_dlg = function open_addnew_dialog(update_procedure) {
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

    var select_handler = {
      theForm: this.theForm,
      idFieldName: this.idFieldName,
      update_procedure: update_procedure,
      category: this.category,
      select: function(id) {
	var req = new HTTPDataRequest(this.theForm.do_link);	
	req.add("command", "addItem");
	req.add("param0", id);
	req.add("param1", this.category.Value);
	req.execute();
	if(this.update_procedure) { this.update_procedure(); }
      }
    }
    window.openDialog(get_literal(modules_ds, 
				  module_res, this.xulfile_predicate),
		      get_literal(modules_ds, 
				  module_res, this.xulfile_predicate),
		      "chrome, resizable, scrollbars", select_handler);
  };

  this.edit_dlg = function open_edit_dialog(objectId, update_procedure) {

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
    
    var select_handler = {
      theForm: this.theForm,
      idFieldName: this.idFieldName,
      update_procedure: update_procedure,
      category: this.category,
      select: function(id) {
	var req = new HTTPDataRequest(this.theForm.do_link);
	req.add("command", "change");
	req.add("field", this.idFieldName);
	req.add("value", id);
	this.theForm.post_request(req);
	if(this.update_procedure) { this.update_procedure(); }
      }
    };
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
