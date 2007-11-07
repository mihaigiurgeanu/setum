package ro.kds.erp.biz.basic;

import java.io.Serializable;
import java.util.Map;
import ro.kds.erp.data.AttributeLocal;

/**
 * A represantation of form data. While user is editing the data associated
 * with this object, the data is maintained temporary into this bean. At
 * the end, when <code>save</code> operation is called, the data is
 * saved in the persistance layer.
 *
 * Class was automaticaly generated from a template.
 *
 */
public class RulesServerForm implements Serializable {

    /**
     * Builds an RDF URI for the field. This RDF is used by the UI to display
     * a localized and friendly name for the field in validation messages.
     *
     * @param fieldName - the name of the field
     * @return an RDF URI by concatenating the string <code>http://www.kds.ro/readybeans/rdf/forms/RulesServer#</code>
     * with the name of the field.
     */
     public static String uri(String fieldName) {
	return "http://www.kds.ro/readybeans/rdf/forms/RulesServer#" + fieldName;
     }

    String setName;
    String ruleName;
    String condition;
    String message;
    String messageParam;
    Boolean isError;

    public RulesServerForm() {


       this.setName = "";



       this.ruleName = "";



       this.condition = "";



       this.message = "";



       this.messageParam = "";



       this.isError = new Boolean(false);



    }

    public void setSetName(String newSetName) {
        this.setName = newSetName;
    }

    public String getSetName() {
        return setName;
    }

    public void readSetName(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("setName");
	if(a != null) {
	    this.setSetName(a.getStringValue());
	}
    }

    public void setRuleName(String newRuleName) {
        this.ruleName = newRuleName;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void readRuleName(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("ruleName");
	if(a != null) {
	    this.setRuleName(a.getStringValue());
	}
    }

    public void setCondition(String newCondition) {
        this.condition = newCondition;
    }

    public String getCondition() {
        return condition;
    }

    public void readCondition(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("condition");
	if(a != null) {
	    this.setCondition(a.getStringValue());
	}
    }

    public void setMessage(String newMessage) {
        this.message = newMessage;
    }

    public String getMessage() {
        return message;
    }

    public void readMessage(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("message");
	if(a != null) {
	    this.setMessage(a.getStringValue());
	}
    }

    public void setMessageParam(String newMessageParam) {
        this.messageParam = newMessageParam;
    }

    public String getMessageParam() {
        return messageParam;
    }

    public void readMessageParam(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("messageParam");
	if(a != null) {
	    this.setMessageParam(a.getStringValue());
	}
    }

    public void setIsError(Boolean newIsError) {
        this.isError = newIsError;
    }

    public Boolean getIsError() {
        return isError;
    }

    public void readIsError(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("isError");
	if(a != null) {
	    this.setIsError(a.getBoolValue());
	}
    }

}
