// $Id: rdf.js,v 1.1 2007/02/12 21:07:55 mihai Exp $
// Global vars and utilities to work with RDF data sources


var rdf_service = Components.classes["@mozilla.org/rdf/rdf-service;1"].getService(Components.interfaces.nsIRDFService);


// Data sources
var messages_ds = rdf_service.GetDataSource("chrome://setum/xul/messages.xml");


// Vocabulary
var message_p = rdf_service.GetResource("http://www.kds.ro/readybeans/rdf/vocabulary#Message");




function rdf_getLiteral(ds, res, predicate) {
    log("Searching for " + res.Value);
    log("having predicate " + predicate.Value);
    var target = ds.GetTarget(res, predicate, true);
    if(target instanceof Components.interfaces.nsIRDFLiteral) {
	return target.Value;
    }
    log(target + " is not instance of Componets.interfaces.nsIRDFLiteral");
    return "";
}

function rdf_getResource(ds, res, predicate) {
    log("Searching for " + res.Value);
    log("having predicate " + predicate.Value);
    var target = ds.GetTarget(res, predicate, true);
    if(target instanceof Components.interfaces.nsIRDFResource) {
	return target;
    }
    log(target + " is not instance of Componets.interfaces.nsIRDFResource");
    return "";
}

