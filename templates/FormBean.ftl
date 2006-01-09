[#ftl]
package ${.node.class.package};

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
public class ${.node.class.name}Form implements Serializable {
        
    [#list .node.class.field as field]
    ${field.type} ${field.name};
    [/#list]

    [#list .node.class.field as field]
    public void set${field.name?cap_first}(${field.type} new${field.name?cap_first}) {
        this.${field.name} = new${field.name?cap_first};
    }

    public ${field.type} get${field.name?cap_first}() {
        return ${field.name};
    }

    [#if field.type = 'Integer']
    public void read${field.name?cap_first}(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("${field.name}");
	if(a != null) {
	    this.set${field.name?cap_first}(a.getIntValue());
	}
    }
    [#elseif field.type = 'String']
    public void read${field.name?cap_first}(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("${field.name}");
	if(a != null) {
	    this.set${field.name?cap_first}(a.getStringValue());
	}
    }
    [#elseif field.type = 'Double']
    public void read${field.name?cap_first}(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("${field.name}");
	if(a != null) {
	    this.set${field.name?cap_first}(a.getDoubleValue());
	}
    }
    [#elseif field.type = 'java.math.BigDecimal']
    public void read${field.name?cap_first}(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("${field.name}");
	if(a != null) {
	    this.set${field.name?cap_first}(a.getDecimalValue());
	}
    }
    [/#if]

    [/#list]
}
