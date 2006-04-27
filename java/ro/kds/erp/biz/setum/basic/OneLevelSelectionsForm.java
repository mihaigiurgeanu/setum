package ro.kds.erp.biz.setum.basic;

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
public class OneLevelSelectionsForm implements Serializable {
        
    Integer id;
    String code;
    String name;
    String description;

    public OneLevelSelectionsForm() {


       this.id = new Integer(0);



       this.code = "";



       this.name = "";



       this.description = "";



    }

    public void setId(Integer newId) {
        this.id = newId;
    }

    public Integer getId() {
        return id;
    }

    public void readId(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("id");
	if(a != null) {
	    this.setId(a.getIntValue());
	}
    }

    public void setCode(String newCode) {
        this.code = newCode;
    }

    public String getCode() {
        return code;
    }

    public void readCode(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("code");
	if(a != null) {
	    this.setCode(a.getStringValue());
	}
    }

    public void setName(String newName) {
        this.name = newName;
    }

    public String getName() {
        return name;
    }

    public void readName(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("name");
	if(a != null) {
	    this.setName(a.getStringValue());
	}
    }

    public void setDescription(String newDescription) {
        this.description = newDescription;
    }

    public String getDescription() {
        return description;
    }

    public void readDescription(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("description");
	if(a != null) {
	    this.setDescription(a.getStringValue());
	}
    }

}
