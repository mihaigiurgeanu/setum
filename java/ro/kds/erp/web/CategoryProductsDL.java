package ro.kds.erp.web;

import javax.servlet.http.HttpServlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import ro.kds.erp.data.CategoryLocalHome;
import javax.rmi.PortableRemoteObject;
import ro.kds.erp.data.ProductLocalHome;
import ro.kds.erp.data.CategoryLocal;
import ro.kds.erp.data.ProductLocal;
import javax.naming.Context;
import java.util.Collection;
import java.util.Iterator;


/**
 * Returns a recordset containing products in a specific category. The
 * category for which products will be returned is configured in the
 * web.xml file through the <code>category</code> init parameter. The
 * value of this parameter will be a reference to the environment values
 * representing an integer value (the category id).
 *
 * This class uses datalayer.jar ejbs to access the database.
 *
 *
 * Created: Wed Aug 10 17:39:54 2005
 *
 * @author <a href="mailto:Mihai Giurgeanu@CRIMIRA"></a>
 * @version 1.0
 */
public class CategoryProductsDL extends HttpServlet {

    /**
     * The category from which products will be selected.
     */
    private Integer categoryId;

    /**
     * Initializes <code>categoryId</code> variable reading the corresponding
     * init parameter.
     *
     * @param servletConfig a <code>ServletConfig</code> value
     * @exception ServletException if an error occurs
     */
    public final void init(final ServletConfig servletConfig) throws ServletException {
	try {
	    Context it = new InitialContext();
	    categoryId = (Integer)it.lookup("java:comp/env/" + 
					    servletConfig.getInitParameter("category"));
	} catch (NamingException e) {
	    e.printStackTrace();
	    throw new ServletException(e);
	}
	    
    }

    /**
     * Calls doPost. Get requests are used for debuging purposes.
     *
     * @param httpServletRequest a <code>HttpServletRequest</code> value
     * @param httpServletResponse a <code>HttpServletResponse</code> value
     * @exception ServletException if an error occurs
     * @exception IOException if an error occurs
     */
    public final void doGet(final HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse) throws ServletException, IOException {
	doPost(httpServletRequest, httpServletResponse);
    }

    /**
     * Selects products in the given category and makes the xml response.
     *
     * @param httpServletRequest a <code>HttpServletRequest</code> value
     * @param httpServletResponse a <code>HttpServletResponse</code> value
     * @exception ServletException if an error occurs
     * @exception IOException if an error occurs
     */
    public final void doPost(final HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse) throws ServletException, IOException {


	int code = 0;
	String message="Action completed";


	httpServletResponse.setContentType("text/xml");
	PrintWriter out = httpServletResponse.getWriter();

	out.println("<?xml version=\"1.0\"?>");
	out.print("<response>");
	try {
	    InitialContext it = new InitialContext();
	    Context env = (Context)it.lookup("java:comp/env");

	    CategoryLocalHome ch = (CategoryLocalHome)PortableRemoteObject.
		narrow(env.lookup("ejb/CategoryLocalHome"),
		       CategoryLocalHome.class);
	    ProductLocalHome ph = (ProductLocalHome)PortableRemoteObject.
		narrow(env.lookup("ejb/ProductLocalHome"),
		       ProductLocalHome.class);
	    Integer sistemeId = (Integer)env.lookup("setum/category/sistemeId");
	    CategoryLocal cat = ch.findByPrimaryKey(categoryId);
	    Collection products = cat.getProducts();

	    // output a row for a null product
	    out.print("<record><field name=\"id\">");
	    out.print("null");
	    out.print("</field><field name=\"code\">");
	    out.print("null");
	    out.print("</field><field name=\"name\">");
	    out.print("-");
	    out.print("</field><field name=\"categoryId\">");
	    out.print("null");
	    out.print("</field><field name=\"entryPrice\">");
	    out.print("null");
	    out.print("</field><field name=\"sellPrice\">");
	    out.print("null");
	    out.print("</field><field name=\"price1\">");
	    out.print("null");
	    out.print("</field></record>");

	    for(Iterator i = products.iterator(); i.hasNext(); ) {
		ProductLocal p = (ProductLocal)i.next();

		out.print("<record><field name=\"id\">");
		out.print(p.getId());
		out.print("</field><field name=\"code\">");
		out.print(p.getCode());
		out.print("</field><field name=\"name\">");
		out.print(p.getName());
		out.print("</field><field name=\"categoryId\">");
		out.print(p.getCategory().getId());
		out.print("</field><field name=\"entryPrice\">");
		out.print(p.getEntryPrice());
		out.print("</field><field name=\"sellPrice\">");
		out.print(p.getSellPrice());
		out.print("</field><field name=\"price1\">");
		out.print(p.getPrice1());
		out.print("</field></record>");
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

}
