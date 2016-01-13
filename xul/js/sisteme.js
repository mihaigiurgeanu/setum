/* sisteme.js - data communication for "sisteme" form */
/* it uses HTTPRequest object defined in httpdata.js script */

function makeTable() {
  var div = document.getElementById('tipurisisteme');
  var records = getSystemsTypes();
  if(records) {
    var table = renderSystemsTypes(records);
    div.appendChild(table);
  } else {
    div.appendChild(document.createTextNode("Records could not be read"));
  }
}

function renderSystemsTypes(records) {
  var table = document.createElement("table");
  for(var i=0; i<records.length; i++) {
    var tr = document.createElement("tr");
    var td;

    td = document.createElement("td");
    td.appendChild(document.createTextNode(records[i]["id"]));
    tr.appendChild(td);

    td = document.createElement("td");
    td.appendChild(document.createTextNode(records[i]["name"]));
    tr.appendChild(td);

    table.appendChild(tr);
  }

  return table;
}

function getSystemsTypes() {
  var req = new HTTPDataRequest("/systemstypes.xml");
  var response = req.execute();
  if(response) {
    alert("Response code: " + response.code + "\nMessage: " + response.message);

    return response.records;
  }
  return false;
}
