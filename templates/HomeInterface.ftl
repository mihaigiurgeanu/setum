[#ftl]
package ${.node.class.package};

import java.rmi.RemoteException;
import java.util.Collection;
import javax.ejb.CreateException;
import javax.ejb.EJBHome;

/**
 * ${.node.class.name} home interface.
 *
 * Class was automaticaly generated from a template.
 *
 */
public interface ${.node.class.name}Home extends EJBHome {        
    ${.node.class.name} create() throws CreateException, RemoteException;
}
