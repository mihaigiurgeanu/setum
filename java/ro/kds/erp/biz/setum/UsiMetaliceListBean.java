package ro.kds.erp.biz.setum;

import java.rmi.RemoteException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import ro.kds.erp.biz.ResponseBean;
import ro.kds.erp.data.CategoryLocalHome;
import javax.rmi.PortableRemoteObject;
import ro.kds.erp.data.CategoryLocal;
import ro.kds.erp.data.ProductLocal;
import ro.kds.erp.data.AttributeLocal;
import java.util.Collection;
import java.util.Iterator;
import javax.naming.InitialContext;
import javax.naming.Context;

/**
 * Describe class UsiMetaliceListBean here.
 *
 *
 * Created: Wed Oct 05 10:49:03 2005
 *
 * @author <a href="mailto:Mihai Giurgeanu@CRIMIRA"></a>
 * @version 1.0
 */
public class UsiMetaliceListBean implements SessionBean {


    // Implementation of javax.ejb.SessionBean

    /**
     * Describe <code>setSessionContext</code> method here.
     *
     * @param sessionContext a <code>SessionContext</code> value
     * @exception EJBException if an error occurs
     * @exception RemoteException if an error occurs
     */
    public  void setSessionContext(final SessionContext sessionContext) throws EJBException, RemoteException {

    }

    /**
     * Describe <code>ejbRemove</code> method here.
     *
     * @exception EJBException if an error occurs
     * @exception RemoteException if an error occurs
     */
    public  void ejbRemove() throws EJBException, RemoteException {

    }

    /**
     * Describe <code>ejbActivate</code> method here.
     *
     * @exception EJBException if an error occurs
     * @exception RemoteException if an error occurs
     */
    public  void ejbActivate() throws EJBException, RemoteException {

    }

    /**
     * Describe <code>ejbPassivate</code> method here.
     *
     * @exception EJBException if an error occurs
     * @exception RemoteException if an error occurs
     */
    public  void ejbPassivate() throws EJBException, RemoteException {

    }

    /**
     * Create a new bean
     */
    public void ejbCreate() {
    }

    /**
     * Returns the list of products.
     */
    public ResponseBean makeList() {
	ResponseBean r = new ResponseBean();
	try {
	    InitialContext it = new InitialContext();
	    Context env = (Context)it.lookup("java:comp/env");
	    CategoryLocalHome ch = (CategoryLocalHome)
		PortableRemoteObject
		.narrow(env.lookup("ejb/CategoryLocalHome"),
			CategoryLocalHome.class);
	    Integer catId = (Integer)env.lookup("categoryId");
	    CategoryLocal c = ch.findByPrimaryKey(catId);
	    Collection products = c.getProducts();
	    for(Iterator i = products.iterator(); i.hasNext(); ) {
		ProductLocal p = (ProductLocal) i.next();
		r.addRecord();
		r.addField("id", p.getId().intValue());
		r.addField("name", c.getName());
		
		Collection attribs = p.getAttributes();
		for(Iterator j = attribs.iterator(); j.hasNext(); ) {
		    AttributeLocal a = (AttributeLocal)j.next();
		    if(a.getName().compareTo("material") == 0) {
			r.addField("material", a.getIntValue().intValue());
			break;
		    }
		}
		    
	    }
	} catch (Exception e) {
	    r.setCode(1);
	    r.setMessage(e.getMessage());
	    e.printStackTrace();
	}
	return r;
    }
}
