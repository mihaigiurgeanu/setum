package ro.kds.erp.web;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;
import javax.naming.InitialContext;
import ro.kds.erp.data.CategoryLocalHome;
import javax.rmi.PortableRemoteObject;
import ro.kds.erp.data.CategoryLocal;
import java.sql.SQLException;
import javax.naming.Context;
import java.util.Collection;
import java.util.Iterator;



/**
 * SystemsTypesXMLDL.java - makes the work of SystemesTypesXML accessing
 * data through datalayer.jar EJBs.
 *
 *
 * Created: Tue Jul 26 12:20:12 2005
 *
 * @author <a href="mailto:mgiurg@xnet.ro"></a>
 * @version
 */

public class SystemsTypesXMLDL extends HttpServlet {

    /**
     * Creates an XML file with data response.
     *
     * @param httpServletRequest a <code>HttpServletRequest</code> value
     * @param httpServletResponse a <code>HttpServletResponse</code> value
     * @exception ServletException if an error occurs
     * @exception IOException if an error occurs
     */
    public void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {

	int code = 0;
	String message="Action completed";


	httpServletResponse.setContentType("text/xml");
	PrintWriter out = httpServletResponse.getWriter();

	out.println("<?xml version=\"1.0\"?>");
	out.print("<response>");
	try {
	    InitialContext it = new InitialContext();
	    Context env = (Context)it.lookup("java:comp/env");
	    Integer sistemeId = (Integer)env
		.lookup("setum/category/sistemeId");
	    CategoryLocalHome ch = (CategoryLocalHome)PortableRemoteObject.
		narrow(env.lookup("ejb/CategoryLocalHome"),
		       CategoryLocalHome.class);
	    CategoryLocal c = ch.findByPrimaryKey(sistemeId);
	    Collection categs = c.getSubCategories();
	    for(Iterator i = categs.iterator(); i.hasNext(); ) {
		CategoryLocal subcat = (CategoryLocal) i.next();
		try {
		    out.print("<record><field name=\"id\">");
		    out.print(subcat.getId());
		    out.print("</field><field name=\"name\">");
		    out.print(subcat.getName());
		    out.print("</field></record>");
		} catch (Exception e) {
		    code = 1;
		    message = "Exception: " + e;
		    e.printStackTrace();
		}
	    }
	} catch (Exception e) {
	    code = 2;
	    message = "Exception: " + e;
	    e.printStackTrace();
	}
	out.print("<return code=\"");
	out.print(code);
	out.print("\"><![CDATA[\n" + message + "\n]]></return>");
	out.println("</response>");
    }

    /**
     * Just calls doPost. It is used for debugging purposes.
     *
     * @param httpServletRequest a <code>HttpServletRequest</code> value
     * @param httpServletResponse a <code>HttpServletResponse</code> value
     * @exception ServletException if an error occurs
     * @exception IOException if an error occurs
     */
    public void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
	doPost(httpServletRequest, httpServletResponse);
    }
    
}// SystemsTypesXML
