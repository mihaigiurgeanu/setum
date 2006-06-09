[#ftl]
package ${.node.class.package};

import java.rmi.RemoteException;
import javax.ejb.EJBObject;
import javax.ejb.FinderException;
import ro.kds.erp.biz.ResponseBean;

/**
 * ${.node.class.name} remote interface.
 *
 * Class was automaticaly generated from a template.
 *
 */
public interface ${.node.class.name} extends EJBObject {
        
    public ResponseBean newFormData() throws RemoteException;
    public ResponseBean saveFormData() throws RemoteException;
    public ResponseBean loadFormData(Integer id) throws RemoteException, FinderException;

    [#list .node.class.subforms.subform as subform]
    public ResponseBean new${subform.@name?cap_first}Data() throws RemoteException;
    public ResponseBean save${subform.@name?cap_first}Data() throws RemoteException;
    public ResponseBean load${subform.@name?cap_first}Data(Integer id) throws RemoteException, FinderException;
    [/#list]

    public ResponseBean getCurrentFormData() throws RemoteException;
    public ResponseBean getLoadedPrimaryKey() throws RemoteException;

    [#list .node.class.field as field]
    public ResponseBean update${field.name?cap_first}(${field.type} new${field.name?cap_first}) throws RemoteException;
    [/#list]

    [#list .node.class.remote.method as method]
    public ${method};
    [/#list]

    [#list .node.class.services.method as method]
    public ResponseBean ${method.name} (
        [#list method.params.param as param]
        ${param.type} ${param.name}[#if param_has_next],[/#if]
        [/#list]
    ) throws RemoteException[#list method.throws.throw as throw], ${throw}[/#list];
    [/#list]
}
