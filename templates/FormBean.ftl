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

    /**
     * Builds an RDF URI for the field. This RDF is used by the UI to display
     * a localized and friendly name for the field in validation messages.
     *
     * @param fieldName - the name of the field
     * @return an RDF URI by concatenating the string <code>http://www.kds.ro/readybeans/rdf/forms/${.node.class.name}#</code>
     * with the name of the field.
     */
     public static String uri(String fieldName) {
	return "http://www.kds.ro/readybeans/rdf/forms/${.node.class.name}#" + fieldName;
     }

    [#list .node.class.field as field]
    ${field.type} ${field.name};
    [/#list]

    public ${.node.class.name}Form() {

       [#list .node.class.field as field]

       [#if field.type = 'Integer']
       this.${field.name} = new Integer(0);

       [#elseif field.type = 'String']
       this.${field.name} = "";

       [#elseif field.type = 'Double']
       this.${field.name} = new Double(0);
   
       [#elseif field.type = 'java.math.BigDecimal']
       this.${field.name} = new java.math.BigDecimal(0);

       [#elseif field.type = 'Boolean']
       this.${field.name} = new Boolean(false);

       [#else]

       // No rule to initialize this.${field.name}

       [/#if]

       [/#list]

    }

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
    [#elseif field.type = 'Boolean']
    public void read${field.name?cap_first}(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("${field.name}");
	if(a != null) {
	    this.set${field.name?cap_first}(a.getBoolValue());
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
