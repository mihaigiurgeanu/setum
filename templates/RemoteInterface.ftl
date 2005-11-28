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
    public ResponseBean getCurrentFormData() throws RemoteException;
    public ResponseBean getLoadedPrimaryKey() throws RemoteException;

    [#list .node.class.field as field]
    public ResponseBean update${field.name?cap_first}(${field.type} new${field.name?cap_first}) throws RemoteException;
    [/#list]

    [#list .node.class.remote.method as method]
    public ${method};
    [/#list]
}
