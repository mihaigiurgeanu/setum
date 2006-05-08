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
     * Describe code here.
     */
    private int code;

    /**
     * Describe message here.
     */
    private String message;
    LinkedList records;
    LinkedHashMap fields;
    LinkedHashMap valueLists;

    /**
     * Field intialization
     */
    private void initialize() {
        if (logger == null) {
            logger = Log.getLogger("ro.kds.erp.biz.basic.GenericProducts");
        }
        logger.log(BasicLevel.DEBUG, "");

	fields = null;
	records = new LinkedList();
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
	fields.put(name,value);
    }

    /**
     * Add a new field to the response. The field is a pair name - value
     * usually used by the session bean to communicate to the UI form different
     * values that should be displayed.
     * @param name - is the name of the field, some kind of control id known
     * by the business logic and by the UI.
     * @param value - the value associated with the field (that, usually,
     * the UI engine should display).
     */
    public void addField(String name, int value) {
	logger.log(BasicLevel.DEBUG, "add field to response: <<" + 
		   name + ">> -> <<" + value + ">>"); 
	fields.put(name, String.valueOf(value));
    }

    /**
     * Add a new field to the response. The field is a pair name - value
     * usually used by the session bean to communicate to the UI form different
     * values that should be displayed.
     * @param name - is the name of the field, some kind of control id known
     * by the business logic and by the UI.
     * @param value - the value associated with the field (that, usually,
     * the UI engine should display).
     */
    public void addField(String name, double value) {
	logger.log(BasicLevel.DEBUG, "add field to response: <<" + 
		   name + ">> -> <<" + value + ">>"); 
	fields.put(name, String.valueOf(value));
    }

    /**
     * Add a new field to the response. The field is a pair name - value
     * usually used by the session bean to communicate to the UI form different
     * values that should be displayed.
     * @param name - is the name of the field, some kind of control id known
     * by the business logic and by the UI.
     * @param value - the value associated with the field (that, usually,
     * the UI engine should display).
     */
    public void addField(String name, Double value) {
	logger.log(BasicLevel.DEBUG, "add field to response: <<" + 
		   name + ">> -> <<" + value + ">>"); 
	if(value != null)
	    fields.put(name, NumberFormat.getInstance().format(value));
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
     */
    public void addField(String name, Integer value) {
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
     */
    public void addField(String name, BigDecimal value) {
	logger.log(BasicLevel.DEBUG, "add field to response: <<" + 
		   name + ">> -> <<" + value + ">>"); 
	if(value != null) {
	    fields.put(name, NumberFormat.getInstance().format(value));
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
     */
    public void addField(String name, Date value) {
	logger.log(BasicLevel.DEBUG, "add field to response: <<" + 
		   name + ">> -> <<" + value + ">>"); 
	if(value != null) {
	    fields.put(name, DateFormat.getDateInstance().format(value));
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
     * Add a new value list to the response. Value lists will be usualy used
     * by the UI in order to give the user options for differeng combo-box
     * types.
     * @param name - is the name of the valuelist, some kind of id known
     * by the bussines logic and by the UI.
     * @param vl - is the actual value list, a <code>Map</code> containing
     * name - value pairs.
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
	xml += "<return code=\"";
	xml += code;
	xml += "\">";
	if(getMessage() != null)
	    xml += "<![CDATA[" + getMessage() + "]]>";
	xml += "</return>";
	xml += "</response>";
	
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


    public static final ResponseBean SUCCESS = new ResponseBean();
    public static final ResponseBean ERR_CONFIG_NAMING = 
	new ResponseBean(1, "Eroare de sistem");
    public static final ResponseBean ERR_REMOTE = 
	new ResponseBean(2, "Eroare comunicare cu serverul de aplicatie");
    public static final ResponseBean ERR_CREATE = 
	new ResponseBean(3, "Eroare la insert in baza de date");
    public static final ResponseBean ERR_NOTCURRENT = 
	new ResponseBean(4, "Nu este nici o selectie curenta!" +
			 " Alegeti ceva din lista!");
    public static final ResponseBean ERR_UNEXPECTED = 
	new ResponseBean(5, "O eroare neasteptata a aparut la executia pe server");
    public static final ResponseBean ERR_CONFIG_NOTFOUND = 
	new ResponseBean(6, "Eroare de sistem");
    public static final ResponseBean ERR_NOTFOUND = 
	new ResponseBean(7, "Informatia nu exista in baza de date");
    public static final ResponseBean ERR_NOTIMPLEMENTED =
	new ResponseBean(8, "Operatia nu este implementata");
    public static final ResponseBean ERR_REMOVE = 
	new ResponseBean(9, "Eroare la executarea operatiei de stergere");
    public static final ResponseBean ERR_OUT_OF_ORDER_OPERATION = 
	new ResponseBean(10, "Metoda apelata gresit ...");
}
