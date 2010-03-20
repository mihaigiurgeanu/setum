
// retrieves data from the database
function getRecordset(request) {
  var req = new HTTPDataRequest("/ofertausistd.xml");
  var response = req.execute();
  if(response) {
    if(response.code == 0)
      rsOferta = response.records;
    else {
      alert("Error while retrieving data. Response code is: " + response.code +
	    "\nMessage:\n" + response.message);
      rsOferta = new Array();
    }
  } else {
    alert("Error while retrieving data. No response received from server.");
    rsOferta = new Array();
  }
}


function load_view() {
  
  var treeView = {
    rowCount : recordset.length,
    getCellText : function(row,column)
    {
      return this.rows[row][column];
    },
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
