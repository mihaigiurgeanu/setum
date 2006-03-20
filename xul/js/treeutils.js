
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
