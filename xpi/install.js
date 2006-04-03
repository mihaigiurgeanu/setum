// install.js


initInstall("Setumapp", "/kds/setum", "1.0");

var appFolder = getFolder("file:///", "c|/setumapp");
var serverFolder = getFolder("file:///", "c|/JOnAS-4.5.3");

var err;


// should have setum/xul and setum/scripting
err = addDirectory("AppFolder",
		   "1.0",
		   "setumapp",
		   appFolder,
		   "",
		   0);

logComment("addDirectory() returned: " + err);

if (err == SUCCESS) {
    err = addFile("DeployFolder",
		  "1.0",
		  "setum.ear",
		  serverFolder,
		  "apps/autoload/setum.ear",
		  0);
    
    logComment("addDirectory() returned: " + err);

    if(err == SUCCESS) {
	err = performInstall();
	logComment("performInstall() returned :" + err);
    } else {
	cancelInstall(err);
    }
} else {
    cancelInstall(err);
}
