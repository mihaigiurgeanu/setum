// messages.js
// Deals with messages localization
//
// The messages are defined in a RDF datasource.


//var rdfService = Components.classes["@mozilla:org/rdf/rdf-service;1"].getService(Components.interfaces.nsIRDFService);

// Load the RDF datasource
var m_ds = rdfService.GetDataSource("chrome://setum/xul/messages.xml");




// Vocabulary
var message_p = rdfService.GetResource("http://www.kds.ro/readybeans/rdf/vocabulary#Message");


// Gets the RDF uri of the subject and returns the associated message
function message(subject) {

    try {
	// Load the RDF datasource

	var subject_r = rdfService.GetResource(subject);
	log("Searching for " + subject_r.Value);
	log("having predicate " + message_p.Value);
	var target = m_ds.GetTarget(subject_r, message_p, true);
	log("Target: " + target);
	if(target instanceof Components.interfaces.nsIRDFLiteral) {
	    return target.Value;
	}
    } catch (error) {
	log(error);
    }
    return "";
}

