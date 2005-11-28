[#ftl]
package ${.node.class.package};

import java.io.Serializable;

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

    [/#list]
}
