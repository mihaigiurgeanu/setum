package ro.kds.erp.biz;

import java.io.Serializable;
import java.util.Map;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Iterator;
import java.util.LinkedList;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.text.DateFormat;
import java.text.NumberFormat;
import org.objectweb.util.monolog.api.BasicLevel;
import org.objectweb.util.monolog.api.Logger;
import org.objectweb.jonas.common.Log;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * The <code>ResponseBean</code> is a data transporter between the
 * business logic and the web interface to the business logic. A
 * <code>ResponseBean</code> object is constructed by a business logic
 * session bean as a response to a method call. The calling servlet
 * can call <code>toXML()</code> method of the <code>ResponseBean</code>
 * to return the processing result to the UI layer.
 *
 *
 * Created: Fri Sep 09 20:08:00 2005
 *
 * @author <a href="mailto:Mihai Giurgeanu@CRIMIRA"></a>
 * @version 1.0
 */
public class ResponseBean implements Serializable {

    static protected Logger logger = null;

    /**
     * The empty validation subject, when only the message is enough for the user.
     */
    public static final String EMPTY_VALIDATION_SUBJECT = "http://www.kds.ro/readybeans/rdf/validation/subject/empty";

    /**
     * Validation message for not complying to a minimum rule.
     */
    public static final String MINRULE_VALIDATION_MESSAGE = "http://www.kds.ro/readybeans/rdf/validation/message#min";

    /**
     * Validation message for not complying to a maximum rule.
     */
    public static final String MAXRULE_VALIDATION_MESSAGE = "http://www.kds.ro/readybeans/rdf/validation/message#max";


    /**
     * The error code of the operation that returned this response.
     */
    private int code;

    /**
     * A message attached to the response.
     */
    private String message;



    LinkedList records;
    LinkedHashMap fields;
    LinkedHashMap valueLists;
    LinkedList validationInfo;

    /**
     * Field intialization
     */
    private void initialize() {
        if (logger == null) {
            logger = Log.getLogger("ro.kds.erp.biz.basic.GenericProducts");
        }
        logger.log(BasicLevel.DEBUG, "Creating new response");

	fields = null;
	records = new LinkedList();
	validationInfo = new LinkedList();
	setCode(0);
	setMessage(null);
	valueLists = new LinkedHashMap();
    }

    /**
     * Creates a new <code>ResponseBean</code> instance.
     *
     */
    public ResponseBean() {
	initialize();
    }

    public ResponseBean(int code, String msg) {
	initialize();
	setCode(code);
	setMessage(msg);
    }

    /** 
     * Add a new record to the response. The new record will be
     * the currently selected one. <code>addField</code> methods
     * will add fields to this new record.
     */
    public void addRecord() {
	fields = new LinkedHashMap();
	records.add(fields);
    }

    /**
     * Add a new field to the current record. The field is a pair name - value
     * usually used by the session bean to communicate to the UI form different
     * values that should be displayed.
     *
     * You must call <code>addRecord()</code> first. The <code>addRecord</code>
     * method sets the current record, the <code>addField</code> method will
     * operate on.
     * @param name - is the name of the field, some kind of control id known
     * by the business logic and by the UI.
     * @param value - the value associated with the field (that, usually,
     * the UI engine should display).
     * @throws NullPointerException if <code>addRecord</code> was never called.
     */
    public void addField(String name, String value) {
	logger.log(BasicLevel.DEBUG, "add field to response: <<" + 
		   name + ">> -> <<" + value + ">>"); 
	fields.put(name,"<![CDATA[" + value + "]]>");
    }

    /**
     * Add a new field to the response. The field is a pair name - value
     * usually used by the session bean to communicate to the UI form different
     * values that should be displayed.
     * @param name - is the name of the field, some kind of control id known
     * by the business logic and by the UI.
     * @param value - the value associated with the field (that, usually,
     * the UI engine should display).
     * @throws NullPointerException if <code>addRecord</code> was never called.
     */
    public void addField(String name, int value) {
	logger.log(BasicLevel.DEBUG, "add field to response: <<" + 
		   name + ">> -> <<" + value + ">>"); 

	NumberFormat nf = NumberFormat.getIntegerInstance(Locale.US);
	nf.setGroupingUsed(false);
	fields.put(name, nf.format(value));
    }

    /**
     * Add a new field to the response. The field is a pair name - value
     * usually used by the session bean to communicate to the UI form different
     * values that should be displayed.
     * @param name - is the name of the field, some kind of control id known
     * by the business logic and by the UI.
     * @param value - the value associated with the field (that, usually,
     * the UI engine should display).
     * @throws NullPointerException if <code>addRecord</code> was never called.
     */
    public void addField(String name, double value) {
	logger.log(BasicLevel.DEBUG, "add field to response: <<" + 
		   name + ">> -> <<" + value + ">>"); 
	NumberFormat nf = NumberFormat.getInstance(Locale.US);
	nf.setGroupingUsed(false);
	fields.put(name, nf.format(value));
    }

    /**
     * Add a new field to the response. The field is a pair name - value
     * usually used by the session bean to communicate to the UI form different
     * values that should be displayed.
     * @param name - is the name of the field, some kind of control id known
     * by the business logic and by the UI.
     * @param value - the value associated with the field (that, usually,
     * the UI engine should display).
     * @throws NullPointerException if <code>addRecord</code> was never called.
     */
    public void addField(String name, Double value) {
	logger.log(BasicLevel.DEBUG, "add field to response: <<" + 
		   name + ">> -> <<" + value + ">>"); 
	if(value != null) {
	    NumberFormat nf = NumberFormat.getInstance(Locale.US);
	    nf.setGroupingUsed(false);
	    fields.put(name, nf.format(value));
	}
	else 
	    fields.put(name, "");
    }

    /**
     * Add a new field to the response. The field is a pair name - value
     * usually used by the session bean to communicate to the UI form different
     * values that should be displayed.
     * @param name - is the name of the field, some kind of control id known
     * by the business logic and by the UI.
     * @param value - the value associated with the field (that, usually,
     * the UI engine should display).
     * @throws NullPointerException if <code>addRecord</code> was never called.
     */
    public void addField(String name, Integer value) {
	logger.log(BasicLevel.DEBUG, "add field to response: <<" + 
		   name + ">> -> <<" + value + ">>"); 
	if(value != null) {
	    NumberFormat nf = NumberFormat.getInstance(Locale.US);
	    nf.setGroupingUsed(false);
	    fields.put(name, nf.format(value));
	}
	else 
	    fields.put(name, "");
    }

    /**
     * Add a new field to the response. The field is a pair name - value
     * usually used by the session bean to communicate to the UI form different
     * values that should be displayed.
     * @param name - is the name of the field, some kind of control id known
     * by the business logic and by the UI.
     * @param value - the value associated with the field (that, usually,
     * the UI engine should display).
     * @throws NullPointerException if <code>addRecord</code> was never called.
     */
    public void addField(String name, BigDecimal value) {
	logger.log(BasicLevel.DEBUG, "add field to response: <<" + 
		   name + ">> -> <<" + value + ">>"); 
	if(value != null) {
	    NumberFormat nf = NumberFormat.getInstance(Locale.US);
	    nf.setGroupingUsed(false);
	    fields.put(name, nf.format(value));
	}
	else 
	    fields.put(name, "");
    }

    /**
     * Add a new field to the response. The field is a pair name - value
     * usually used by the session bean to communicate to the UI form different
     * values that should be displayed.
     * @param name - is the name of the field, some kind of control id known
     * by the business logic and by the UI.
     * @param value - the value associated with the field (that, usually,
     * the UI engine should display).
     * @throws NullPointerException if <code>addRecord</code> was never called.
     */
    public void addField(String name, Date value) {
	logger.log(BasicLevel.DEBUG, "add field to response: <<" + 
		   name + ">> -> <<" + value + ">>"); 
	if(value != null) {
	    //fields.put(name, DateFormat.getDateInstance(DateFormat.FULL).format(value));
	    fields.put(name, (new SimpleDateFormat("yyyy-MM-dd")).format(value));
	    //fields.put(name, value);
	}
	else 
	    fields.put(name, "");
    }

    /**
     * Add a new field to the response. The field is a pair name - value
     * usually used by the session bean to communicate to the UI form different
     * values that should be displayed.
     * @param name - is the name of the field, some kind of control id known
     * by the business logic and by the UI.
     * @param value - the value associated with the field (that, usually,
     * the UI engine should display).
     * @throws NullPointerException if <code>addRecord</code> was never called.
     */
    public void addField(String name, Boolean value) {
	logger.log(BasicLevel.DEBUG, "add field to response: <<" + 
		   name + ">> -> <<" + value + ">>"); 
	if(value != null)
	    fields.put(name, value.toString());
	else 
	    fields.put(name, "");
    }


     /**
     * Add a new field to the response. The field is a pair name - value
     * usually used by the session bean to communicate to the UI form different
     * values that should be displayed.
     * @param name - is the name of the field, some kind of control id known
     * by the business logic and by the UI.
     * @param value - the value associated with the field (that, usually,
     * the UI engine should display).
     * @throws NullPointerException if <code>addRecord</code> was never called.
     */
    public void addField(String name, ResponseBean value) {

	String xmlField;
	if(value != null)
	    xmlField = value.xmlContent();
	else 
	    xmlField = "";

	logger.log(BasicLevel.DEBUG, "add field to response: <<" + 
		   name + ">> -> <<" + xmlField + ">>"); 
	fields.put(name, xmlField);
    }



    /**
     * Add a new value list to the response. Value lists will be usualy used
     * by the UI in order to give the user options for differeng combo-box
     * types.
     * @param name - is the name of the valuelist, some kind of id known
     * by the bussines logic and by the UI.
     * @param vl - is the actual value list, a <code>Map</code> containing
     * name - value pairs.
     * @throws NullPointerException if <code>addRecord</code> was never called.
     */
    public void addValueList(String name, Map vl) {
	valueLists.put(name, vl);
    }

    /**
     * Create the XML response. This is the representation of the response
     * object in an XML format.
     * @return an <code>String</code> containing the XML representation of
     * the response.
     */
    public String toXML() {
	String xml = "";
	xml += "<?xml version=\"1.0\"?>";
	xml += "<response>";
	xml += xmlContent();
	xml += "</response>";
	
	return xml;
    }


    /**
     * Build the content of the xml document (that is without response tag).
     */
    protected String xmlContent() {
	String xml = "";

	for(Iterator j=records.iterator(); j.hasNext(); ) {
	    Map myFields = (Map)j.next();
	    xml += "<record>";
	    for(Iterator i = myFields.entrySet().iterator(); i.hasNext();) {
		Map.Entry field = (Map.Entry)i.next();
		xml += "<field name=\"" + field.getKey() + "\">";
		xml += field.getValue();
		xml += "</field>";
	    }
	    xml += "</record>";	    
	}
	for(Iterator i = valueLists.entrySet().iterator(); i.hasNext(); ) {
	    Map.Entry vlDefinition = (Map.Entry)i.next();
	    String vlName = (String)vlDefinition.getKey();
	    Map vl = (Map)vlDefinition.getValue();
	    xml += "<value-list name=\"" + vlName + "\">";
	    for(Iterator j = vl.entrySet().iterator(); j.hasNext(); ) {
		Map.Entry val = (Map.Entry)j.next();
		xml += "<vl-item>";
		xml += "<value>" + val.getValue() + "</value>";
		xml += "<label>" + val.getKey() + "</label>";
		xml += "</vl-item>";
	    }
	    xml += "</value-list>";
	}
	for(Iterator i = validationInfo.iterator(); i.hasNext(); ) {
	    ValidationInfo vi = (ValidationInfo)i.next();
	    xml += "<validation-info subject=\"" + vi.getSubject() + 
		"\" message=\"" + vi.getMessage() + 
		"\"><![CDATA[" + vi.getData() + "]]></validation-info>";
	}

	xml += "<return code=\"";
	xml += code;
	xml += "\">";
	if(getMessage() != null)
	    xml += "<![CDATA[" + getMessage() + "]]>";
	xml += "</return>";

	return xml;
    }

    /**
     * Get the <code>Code</code> value.
     *
     * @return an <code>int</code> value
     */
    public final int getCode() {
	return code;
    }

    /**
     * Set the <code>Code</code> value.
     *
     * @param newCode The new Code value.
     */
    public final void setCode(final int newCode) {
	logger.log(BasicLevel.DEBUG, "New return code: " + newCode);
	this.code = newCode;
    }

    /**
     * Get the <code>Message</code> value.
     *
     * @return a <code>String</code> value
     */
    public final String getMessage() {
	return message;
    }

    /**
     * Set the <code>Message</code> value.
     *
     * @param newMessage The new Message value.
     */
    public final void setMessage(final String newMessage) {
	this.message = newMessage;
    }





    /**
     * Add a validation info.
     */
    public void addValidationInfo(final String subject, final String message, final String data) {
	logger.log(BasicLevel.DEBUG, "Adding ValidationInfo: " + subject
		   + ": " + message + ": " + data);
	validationInfo.add(new ValidationInfo(subject, message, data));
    }

    /**
     * Add a validation info with no subject.
     */
    public void addValidationInfo(final String message, final String data) {
	addValidationInfo(EMPTY_VALIDATION_SUBJECT, message, data);
    }

    /**
     * Add a validation info with no subject and no data (message only).
     */
    public void addValidationInfo(final String message) {
	addValidationInfo(EMPTY_VALIDATION_SUBJECT, message, "");
    }


    // Response codes and messages


    public static final int CODE_SUCCESS		= 0;
    public static final int CODE_ERR_CONFIG_NAMING	= 1;
    public static final int CODE_ERR_REMOTE		= 2;
    public static final int CODE_ERR_CREATE		= 3;
    public static final int CODE_ERR_NOTCURRENT		= 4;
    public static final int CODE_ERR_UNEXPECTED		= 5;
    public static final int CODE_ERR_CONFIG_NOTFOUND	= 6;
    public static final int CODE_ERR_NOTFOUND		= 7;
    public static final int CODE_ERR_NOTIMPLEMENTED	= 8;
    public static final int CODE_ERR_REMOVE		= 9;
    public static final int CODE_ERR_OUT_OF_ORDERE_OPERATION = 10;
    public static final int CODE_ERR_DATAMISSING	= 11;
    public static final int CODE_ERR_VALIDATION		= 12;


    public static final ResponseBean SUCCESS = new ResponseBean();
    /**
     * Deprecated. Use <code>getErrConfigNaming(...)</code> instead
     */
    public static final ResponseBean ERR_CONFIG_NAMING = 
	new ResponseBean(1, "Eroare de sistem");
    /**
     * Deprecated. Use <code>getErrRemote(...)</code> instead
     */
    public static final ResponseBean ERR_REMOTE = 
	new ResponseBean(2, "Eroare comunicare cu serverul de aplicatie");
    /**
     * Deprecated. Use <code>getErrCreate(...)</code> instead
     */
    public static final ResponseBean ERR_CREATE = 
	new ResponseBean(3, "Eroare la insert in baza de date");
    /**
     * Deprecated. Use <code>getErrNotCurrent(...)</code> instead
     */
    public static final ResponseBean ERR_NOTCURRENT = 
	new ResponseBean(4, "Nu este nici o selectie curenta!" +
			 " Alegeti ceva din lista!");
    /**
     * Deprecated. Use <code>getErrUnexpected(...)</code> instead
     */
    public static final ResponseBean ERR_UNEXPECTED = 
	new ResponseBean(5, "O eroare neasteptata a aparut la executia pe server");
    /**
     * Deprecated. Use <code>getErrConfigNotFound(...)</code> instead
     */
    public static final ResponseBean ERR_CONFIG_NOTFOUND = 
	new ResponseBean(6, "Eroare de sistem");
    /**
     * Deprecated. Use <code>getErrNotFound(...)</code> instead
     */
    public static final ResponseBean ERR_NOTFOUND = 
	new ResponseBean(7, "Informatia nu exista in baza de date");
    /**
     * Deprecated. Use <code>getErrNotImplemented(...)</code> instead
     */
    public static final ResponseBean ERR_NOTIMPLEMENTED =
	new ResponseBean(8, "Operatia nu este implementata");
    /**
     * Deprecated. Use <code>getErrRemove(...)</code> instead
     */
    public static final ResponseBean ERR_REMOVE = 
	new ResponseBean(9, "Eroare la executarea operatiei de stergere");
    /**
     * Deprecated. Use <code>getErrOutOfOrderOperation(...)</code> instead
     */
    public static final ResponseBean ERR_OUT_OF_ORDER_OPERATION = 
	new ResponseBean(10, "Metoda apelata gresit ...");


    /**
     * Builds a configuration error response regarding a naming service error.
     *
     * @param nameEntry is the naming entry that have not been found or that
     * generated the error some how.
     */
    public static final ResponseBean getErrConfigNaming(String nameEntry) {
	return new ResponseBean(CODE_ERR_CONFIG_NAMING, nameEntry);
    }
    
    /**
     * Builds an error response regarding a remote exception, meaning that something
     * is wrong in comunicating with a server side business component.
     * 
     * @param componentName is a hint about the component which generated the error.
     */
    public static final ResponseBean getErrRemote(String componentName) {
	return new ResponseBean(CODE_ERR_REMOTE, componentName);
    }
    
    /**
     * Builds an error response regarding the creation of an entity. The entity
     * name is a name known by both ui and business engines.
     *
     * @param entityName is the name of the entity that could not be created.
     */
    public static final ResponseBean getErrCreate(String entityName) {
	return new ResponseBean(CODE_ERR_CREATE, entityName);
    }
    
    /**
     * Builds an error response saying that a record has not been selected.
     *
     * @param formName is a hint to the user interface about the form/subform
     * record that should of been selected prior to calling the operation that
     * caused this error.
     */
    public static final ResponseBean getErrNotCurrent(String formName) {
	return new ResponseBean(CODE_ERR_NOTCURRENT, formName);
    }
    
    /**
     * Builds an response about an unexpecting error. The <code>message</code>
     * field of the response will contain an exception description.
     *
     * @param exception is the unexpected exception.
     */
    public static final ResponseBean getErrUnexpected(Exception e) {
	return new ResponseBean(CODE_ERR_UNEXPECTED, e.toString());
    }
    
    /**
     * Builds a response message for reporting the error that the cofiguration
     * needed was not found. When calling this function you would provide the name
     * of the missing configuration. This name may
     * be used by ui service to give an information to the user about what it is
     * needed to be configured.
     *
     * @param configName is the name or the code of configuration. It is stored
     * in the response's <code>message</code> field.
     */
    public static final ResponseBean getErrConfigNotFound(String configName) {
	return new ResponseBean(CODE_ERR_CONFIG_NOTFOUND, configName);
    }

    /**
     * Builds a response message for reporting when a entity was not found. Usually
     * the entity lookup is done by its primary key. A call to this function will
     * provide the name of the entity type. This name should be known by the ui engine so
     * the ui engine could display an error message about the missing entity.
     *
     * @param entityName is the name of the missing entity. It will stored in the 
     * <code>message</code> field of the response.
     */
    public static final ResponseBean getErrNotFound(String entityName) {
	return new ResponseBean(CODE_ERR_NOTFOUND, entityName);
    }

    /**
     * Buidls an error response message to report that the invoked functionality is
     * not implemented. The call to this fuction should provide the name of the missing
     * functionality. The name is a code of functionality that should be used by the 
     * ui engine to display a localized error message.
     *
     * @param functionalityName is the name of missing fuctionality. This name is stored
     * in the <code>message</code> field of the response.
     */
    public static final ResponseBean getErrNotImplemented(String functionalityName) {
	return new ResponseBean(CODE_ERR_NOTIMPLEMENTED, functionalityName);
    }

    /**
     * Builds an error response message to report that the entity deletion failed. The caller
     * provides the name of the entity type that could not be deleted. This name is used by
     * the ui service to provide a localized error message.
     *
     * @param entityName is the name of the entity type that could not be deleted. It will
     * be stored in the <code>message</code> field of the response.
     */
    public static final ResponseBean getErrRemove(String entityName) {
	return new ResponseBean(CODE_ERR_REMOVE, entityName);
    }

    /**
     * Builds an error message about missing expected data. A missingData code
     * will be provided when calling this function. The code will stay in the
     * message field of the <code>ResponseBean</code> object.
     *
     * @param missingData is the code of missing field or data. This is
     * a business field that must be agreed on between user interface and
     * business level. It is usually the name of the field that is missing.
     */
    public static final ResponseBean getErrDataMissing(String missingData) {
	return new ResponseBean(CODE_ERR_DATAMISSING, missingData);
    }
    
}

/**
 * The information resulted from applying a validation rule.
 * It is a pair of subject and message. The subject is an identifier
 * that must be understood by the UI. It is the id of a field if the
 * validation message referes to the value of a field. In a more general
 * way, the subject may be a compex subject that is represented, for example,
 * as a unique URI for use with a RDF datasource.
 *
 * Message, also, should be an identifier of the message to be displayed. For example
 * it can be the URI of a subject in a RDF datasource.
 *
 * Besides message and subject, a third field is a <code>String</code> that contains
 * some more specific data to explain the error message. This data is intended to 
 * be displayed to the user as it is, and should be language independent.
 */
class ValidationInfo implements Serializable {
    private String subject;
    private String message;
    private String data;


    /**
     * Create a new <code>ValidationInfo</code> object.
     */
    public ValidationInfo(final String subject, final String message, final String data) {
	this.subject = subject;
	this.message = message;
	this.data = data;
    }
    
    /**
     * Gets the value of subject
     *
     * @return the value of subject
     */
    public final String getSubject() {
	return this.subject;
    }

    /**
     * Sets the value of subject
     *
     * @param argSubject Value to assign to this.subject
     */
    public final void setSubject(final String argSubject) {
	this.subject = argSubject;
    }

    /**
     * Gets the value of message
     *
     * @return the value of message
     */
    public final String getMessage() {
	return this.message;
    }

    /**
     * Sets the value of message
     *
     * @param argMessage Value to assign to this.message
     */
    public final void setMessage(final String argMessage) {
	this.message = argMessage;
    }

    /**
     * Gets the value of data
     *
     * @return the value of data
     */
    public final String getData() {
	return this.data;
    }

    /**
     * Sets the value of data
     *
     * @param argData Value to assign to this.data
     */
    public final void setData(final String argData) {
	this.data = argData;
    }

}
