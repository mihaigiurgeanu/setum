package ro.kds.erp.web;

import javax.servlet.http.HttpServlet;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import javax.naming.InitialContext;
import ro.kds.erp.biz.ListMakerBeanHome;
import javax.rmi.PortableRemoteObject;
import ro.kds.erp.biz.ListMakerBean;
import ro.kds.erp.biz.ResponseBean;
import javax.naming.NamingException;
import java.rmi.RemoteException;
import javax.ejb.CreateException;
import java.io.PrintWriter;
import javax.naming.Context;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 * Constructs an XML response containing a list of records. The
 * list is composed by a session bean. The name of the session bean
 * is read from the init parameter <code>bean</code>. 
 *
 * Created: Tue Oct 04 18:34:04 2005
 *
 * @author <a href="mailto:Mihai Giurgeanu@CRIMIRA"></a>
 * @version 1.0
 */
public class ObjectsListXML extends HttpServlet {
    String beanName;

    /**
     * Describe <code>init</code> method here.
     *
     * @exception ServletException if an error occurs
     */
    public final void init() throws ServletException {
	beanName = getInitParameter("bean");
    }

    /**
     * Returns an xml file with the result.
     *
     * @param httpServletRequest a <code>HttpServletRequest</code> value
     * @param httpServletResponse a <code>HttpServletResponse</code> value
     * @exception ServletException if an error occurs
     * @exception IOException if an error occurs
     */
    public final void doPost(final HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse) throws ServletException, IOException {
	ResponseBean r;
	try {
	    InitialContext it = new InitialContext();
	    Context env = (Context)it.lookup("java:comp/env");

	    ListMakerBeanHome bh = (ListMakerBeanHome)PortableRemoteObject.narrow(env.lookup(beanName),ListMakerBeanHome.class);

	    // creates and registers a new session bean
	    ListMakerBean b = bh.create();
	    r = b.makeList();
	    
	} catch (NamingException e) {
	    r = new ResponseBean();
	    r.setCode(2);
	    r.setMessage("Configuration error: " + e);
	    e.printStackTrace();
	} catch (RemoteException e) {
	    r = new ResponseBean();
	    r.setCode(3);
	    r.setMessage("Error while communicating with business logic server: " + e);
	    e.printStackTrace();
	} catch (CreateException e) {
	    r = new ResponseBean();
	    r.setCode(4);
	    r.setMessage("Error creating session bean: " + e);
	    e.printStackTrace();
	}

	httpServletResponse.setContentType("text/xml");
	PrintWriter out = httpServletResponse.getWriter();
	out.print(r.toXML());
    }

    /**
     * Describe <code>doGet</code> method here.
     *
     * @param httpServletRequest a <code>HttpServletRequest</code> value
     * @param httpServletResponse a <code>HttpServletResponse</code> value
     * @exception ServletException if an error occurs
     * @exception IOException if an error occurs
     */
    public final void doGet(final HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse) throws ServletException, IOException {
	doPost(httpServletRequest, httpServletResponse);
    }

}
