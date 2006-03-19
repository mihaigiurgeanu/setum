
// load the view from the objects array
// objects array must be a reference to a global variable
function refresh_listing(objects, tree) {
    tree.view = make_treeview
	(
	 objects,
	 function(row,column) {
	     return offers[row][column];
	 }
	 );
}

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
