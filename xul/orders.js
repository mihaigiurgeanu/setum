




/*
 * UI commands
 */

// Client selection
function select_client() {
    var select_handler = {
	theForm: theForm,
	select: function update_client(clientId) {
	    var req = this.theForm.get_request();
	    req.add("command", "change");
	    req.add("field", "clientId");
	    req.add("value", clientId);
	    this.theForm.post_request(req);
	}
    };
    window.openDialog("select-client.xul", "select-client", "chrome", select_handler);
}
