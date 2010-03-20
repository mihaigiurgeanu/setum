function init()
{
	generate_subclass_MenuList();
	generate_version_MenuList();
	generate_material_MenuList();
	generate_intFoil_MenuList();
	generate_extFoil_MenuList();
	generate_isolation_MenuList();
	generate_openingDir_MenuList();
	generate_openingSide_MenuList();
	generate_foilPozition_MenuList();
}

function getArray_subclass_MenuList()
{
	var myarray = new Array();
	myarray[0] = new Array('A', "A-apartament");
	myarray[1] = new Array('S', "S-siguranta");
	myarray[2] = new Array('M', "M-metalica");
	myarray[3] = new Array('B', "B-batanta");
	return myarray;
}

function generate_subclass_MenuList()
{
	var myarray=getArray_subclass_MenuList();
	for (i=0; i<myarray.length; i++)
	{
		menit=document.createElement("menuitem");
		menit.setAttribute('id','subclass.'+i);
		menit.setAttribute('label',myarray[i][1]);
		menit.setAttribute('value',myarray[i][0]);
		document.getElementById('subclass-popup').appendChild(menit);
	}
	document.getElementById('subclass').selectedIndex=0;
}

function getArray_version_MenuList()
{
	var myarray = new Array();
	myarray[0] = new Array('UF', "UF-cu falt");
	myarray[1] = new Array('UFF', "UFF-fara falt");
	return myarray;
}

function generate_version_MenuList()
{
	var myarray=getArray_version_MenuList();
	for (i=0; i<myarray.length; i++)
	{
		menit=document.createElement("menuitem");
		menit.setAttribute('id','subclass.'+i);
		menit.setAttribute('label',myarray[i][1]);
		menit.setAttribute('value',myarray[i][0]);
		document.getElementById('version-popup').appendChild(menit);
	}
	document.getElementById('version').selectedIndex=0;
}


function getArray_material_MenuList()
{
	var myarray = new Array();
	myarray[0] = new Array('otel', "otel");
	myarray[1] = new Array('otel zimcat termic', "otel zimcat termic");
	myarray[2] = new Array('inox', "inox");
	myarray[3] = new Array('aluminiu', "aluminiu");
	myarray[4] = new Array('alama', "alama");
	myarray[5] = new Array('cupru', "cupru");
	return myarray;
}

function generate_material_MenuList()
{
	var myarray=getArray_material_MenuList();
	for (i=0; i<myarray.length; i++)
	{
		menit=document.createElement("menuitem");
		menit.setAttribute('id','subclass.'+i);
		menit.setAttribute('label',myarray[i][1]);
		menit.setAttribute('value',myarray[i][0]);
		document.getElementById('material-popup').appendChild(menit);
	}
	document.getElementById('material').selectedIndex=0;
}


function getArray_intFoil_MenuList()
{
	var myarray = new Array();
	myarray[0] = new Array('Lisa', "Lisa");
	myarray[1] = new Array('Amprentata', "Amprentata");
	return myarray;
}

function generate_intFoil_MenuList()
{
	var myarray=getArray_intFoil_MenuList();
	for (i=0; i<myarray.length; i++)
	{
		menit=document.createElement("menuitem");
		menit.setAttribute('id','subclass.'+i);
		menit.setAttribute('label',myarray[i][1]);
		menit.setAttribute('value',myarray[i][0]);
		document.getElementById('intFoil-popup').appendChild(menit);
	}
	document.getElementById('intFoil').selectedIndex=0;
}


function getArray_extFoil_MenuList()
{
	var myarray = new Array();
	myarray[0] = new Array('Lisa', "Lisa");
	myarray[1] = new Array('Amprentata', "Amprentata");
	return myarray;
}

function generate_extFoil_MenuList()
{
	var myarray=getArray_extFoil_MenuList();
	for (i=0; i<myarray.length; i++)
	{
		menit=document.createElement("menuitem");
		menit.setAttribute('id','subclass.'+i);
		menit.setAttribute('label',myarray[i][1]);
		menit.setAttribute('value',myarray[i][0]);
		document.getElementById('extFoil-popup').appendChild(menit);
	}
	document.getElementById('extFoil').selectedIndex=0;
}


function getArray_isolation_MenuList()
{
	var myarray = new Array();
	myarray[0] = new Array(1, "Vata minerala");
	myarray[1] = new Array(2, "Spuma poliuretanica");
	return myarray;
}

function generate_isolation_MenuList()
{
	var myarray=getArray_isolation_MenuList();
	for (i=0; i<myarray.length; i++)
	{
		menit=document.createElement("menuitem");
		menit.setAttribute('id','subclass.'+i);
		menit.setAttribute('label',myarray[i][1]);
		menit.setAttribute('value',myarray[i][0]);
		document.getElementById('isolation-popup').appendChild(menit);
	}
	document.getElementById('isolation').selectedIndex=0;
}


function getArray_openingDir_MenuList()
{
	var myarray = new Array();
	myarray[0] = new Array(1, "interior");
	myarray[1] = new Array(2, "exterior");
	return myarray;
}

function generate_openingDir_MenuList()
{
	var myarray=getArray_openingDir_MenuList();
	for (i=0; i<myarray.length; i++)
	{
		menit=document.createElement("menuitem");
		menit.setAttribute('id','subclass.'+i);
		menit.setAttribute('label',myarray[i][1]);
		menit.setAttribute('value',myarray[i][0]);
		document.getElementById('openingDir-popup').appendChild(menit);
	}
	document.getElementById('openingDir').selectedIndex=0;
}


function getArray_openingSide_MenuList()
{
	var myarray = new Array();
	myarray[0] = new Array(1, "stanga");
	myarray[1] = new Array(2, "dreapta");
	return myarray;
}

function generate_openingSide_MenuList()
{
	var myarray=getArray_openingSide_MenuList();
	for (i=0; i<myarray.length; i++)
	{
		menit=document.createElement("menuitem");
		menit.setAttribute('id','subclass.'+i);
		menit.setAttribute('label',myarray[i][1]);
		menit.setAttribute('value',myarray[i][0]);
		document.getElementById('openingSide-popup').appendChild(menit);
	}
	document.getElementById('openingSide').selectedIndex=0;
}



function getArray_foilPozition_MenuList()
{
	var myarray = new Array();
	myarray[0] = new Array(1, "interior");
	myarray[1] = new Array(2, "exterior");
	myarray[2] = new Array(3, "mijloc");
	return myarray;
}

function generate_foilPozition_MenuList()
{
	var myarray=getArray_foilPozition_MenuList();
	for (i=0; i<myarray.length; i++)
	{
		menit=document.createElement("menuitem");
		menit.setAttribute('id','subclass.'+i);
		menit.setAttribute('label',myarray[i][1]);
		menit.setAttribute('value',myarray[i][0]);
		document.getElementById('foilPozition-popup').appendChild(menit);
	}
	document.getElementById('foilPozition').selectedIndex=0;
}
