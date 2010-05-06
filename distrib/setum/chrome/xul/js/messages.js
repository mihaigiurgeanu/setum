// messages.js
// Deals with messages localization
//
// The messages are defined in a RDF datasource.


// rdf.js provides definitions for:
//	rdf_service
//	rdf_getLiteral()
//	rdf_getResource()
// Predicates:
//	message_p
// Datasources:
//	messages_ds



// Gets the RDF uri of the subject and returns the associated message
function message(subject) {

    try {
	// Load the RDF datasource

	var subject_r = rdf_service.GetResource(subject);
	return rdf_getLiteral(messages_ds, subject_r, message_p)
    } catch (error) {
	log(error);
    }
    return "";
}

