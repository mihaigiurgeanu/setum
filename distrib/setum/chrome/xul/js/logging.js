// logging.js
// Access to logging messages through the java script logging service

function log(message) {
    var logger;
    logger = Components.classes['@mozilla.org/consoleservice;1'].getService(Components.interfaces.nsIConsoleService);
    logger.logStringMessage(message);
}
