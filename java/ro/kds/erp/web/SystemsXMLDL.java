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
import ro.kds.erp.data.ProductLocal;
import javax.naming.Context;
import java.util.Collection;
import java.util.Iterator;
import java.text.DecimalFormat;
import java.util.Locale;
import java.text.DecimalFormatSymbols;




/**
 * SystemsXMLDL.java - outputs the list of systems in the XML forma.
 * The access to the database is done through the datalayer.jar EJBs.
 *
 *
 * Created: Wed Jul 27 03:36:58 2005
 *
 * @author <a href="mailto:mgiurg@xnet.ro"></a>
 * @version
 */

public class SystemsXMLDL extends HttpServlet {

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
	DecimalFormat dec =  new DecimalFormat("0.00", new DecimalFormatSymbols(new Locale("en", "us")));

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
	    for(Iterator j = categs.iterator(); j.hasNext(); ) {
		Collection products = ((CategoryLocal)j.next()).getProducts();
		for(Iterator i = products.iterator(); i.hasNext(); ) {
		    ProductLocal p = (ProductLocal)i.next();
		    
		    out.print("<record><field name=\"id\">");
		    out.print(p.getId());
		    out.print("</field><field name=\"code\">");
		    out.print(p.getCode());
		    out.print("</field><field name=\"name\">");
		    out.print(p.getName());
		    out.print("</field><field name=\"type_id\">");
		    out.print(p.getCategory().getId());
		    out.print("</field><field name=\"price_entry\">");
		    out.print(dec.format(p.getEntryPrice()));
		    out.print("</field><field name=\"price_sell\">");
		    out.print(dec.format(p.getSellPrice()));
		    out.print("</field><field name=\"price_sell1\">");
		    out.print(dec.format(p.getPrice1()));
		    out.print("</field></record>");
		}
	    }
	} catch (Exception e) {
	    code = 1;
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
    
}// SystemsXML
