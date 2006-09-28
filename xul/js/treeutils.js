
// objects is the array of objects to be displayed in the tree
// getCellText is a function of 2 paramams -- row and col -- 
// returning the data for the given row and column
function make_treeview(objects, getCellText) {

    var treeView = {
	rowCount : objects.length,
	getCellText : getCellText,
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

    return treeView;
}


// For views with alot of records, we do not need to load
// all the records at once. The data should be loaded on
// request, per blocks of records that are cached during
// one data load.
// The tree will call getCellText(row, column) for each cell
// that it wants to display. The implementation of getCellText
// will check first to see if the wanted row is in cache, and,
// if not, should send a load data request to the server with
// a param indicating the row number. The server will return
// a list of records starting with the given row number and 
// a default number of records following that row number. The 
// implementation of getCellText will cache the records received
// and will return the value of the requested cell.
//
// For this purpose, the implementation of getCellText will use
// a RemoteDataView object that will know how to request data from
// the server and to cache the received data.
//
// The RemoteDataView object should know:
// - the link of the request (the same with theForm.do_link)
// - the name of the load data request method
// - the name of the method that returns the total number of the
// records for this listing.
function RemoteDataView(do_link, load_data_request, get_length_request, initOnClear) {

    this.do_link = do_link;
    this.load_data_request = load_data_request;
    this.get_length_request = get_length_request;

    this.initOnClear = initOnClear;

    this.clear(); // initialization of the records
}


RemoteDataView.prototype.get_cell_text = function (row, col) {
    return this.get_row(row)[col];
}

RemoteDataView.prototype.get_row = function (row) {
    if(row < 0) {
	log("RemoteDataView.get_row: row is less then 0: " + row);
	return undefined;
    }
    if(this.rows[row] == undefined) {
	var req = new HTTPDataRequest(this.do_link);
	req.add("command", this.load_data_request);
	req.add("param0", row);
	var response = req.execute();
	if(response) {
	    if(response.message) {
		alert(response.message + "\n\nCod: " + response.code);
	    }
	    if(response.code == 0) {
		for(var i = 0; i < response.records.length; i++) {
		    this.rows[row + i] = response.records[i];
		}
	    }
	}
    }
    return this.rows[row];
}


RemoteDataView.prototype.clear = function () {
    var req = new HTTPDataRequest(this.do_link);
    req.add("command", this.get_length_request);

    if(this.initOnClear) {
	req.add("operation", "new-context");
    }

    var response = req.execute();

    this.length = 0;
    this.rows = new Array();

    if(response) {
	if(response.message) {
	    alert(response.message + "\n\n" + "Cod: " + response.code);
	}
	if (response.code == 0 && response.records.length > 0) {
	    this.length = response.records[0]['records-count'];
	}
    }
}

