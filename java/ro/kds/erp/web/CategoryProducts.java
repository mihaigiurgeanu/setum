package ro.kds.erp.web;

import javax.servlet.http.HttpServlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.naming.InitialContext;
import javax.naming.Context;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.naming.NamingException;
import java.io.PrintWriter;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Returns a recordset containing products in a specific category. The
 * category for which products will be returned is configured in the
 * web.xml file through the <code>category</code> init parameter. The
 * value of this parameter will be a reference to the environment values
 * representing an integer value (the category id).
 *
 *
 * Created: Wed Aug 10 17:39:54 2005
 *
 * @author <a href="mailto:Mihai Giurgeanu@CRIMIRA"></a>
 * @version 1.0
 */
public class CategoryProducts extends HttpServlet {

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
	    Context it = new InitialContext();
	    DataSource ds = (DataSource)it
		.lookup("java:comp/env/jdbc/Products");
	    Connection conn = ds.getConnection();
	    Statement stmt = null;

	    Integer sistemeId = (Integer)it.lookup("java:comp/env/setum/category/sistemeId");

	    try {
		stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT id, code, name, categoryId, entryPrice, sellPrice, price1 FROM Product WHERE categoryId = " + categoryId);

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

		while(rs.next()) {
		    String id = rs.getString("id");
		    String codeField = rs.getString("code");
		    String name = rs.getString("name");
		    int categoryIdField = rs.getInt("categoryId");
		    BigDecimal entryPrice = rs.getBigDecimal("entryPrice");
		    BigDecimal sellPrice = rs.getBigDecimal("sellPrice");
		    BigDecimal price1 = rs.getBigDecimal("price1");
		    
		    out.print("<record><field name=\"id\">");
		    out.print(id);
		    out.print("</field><field name=\"code\">");
		    out.print(codeField);
		    out.print("</field><field name=\"name\">");
		    out.print(name);
		    out.print("</field><field name=\"categoryId\">");
		    out.print(categoryIdField);
		    out.print("</field><field name=\"entryPrice\">");
		    out.print(entryPrice);
		    out.print("</field><field name=\"sellPrice\">");
		    out.print(sellPrice);
		    out.print("</field><field name=\"price1\">");
		    out.print(price1);
		    out.print("</field></record>");
		}
	    } catch (java.sql.SQLException e) {
		code = 1;
		message = "SqlException: " + e;
		e.printStackTrace();
	    } finally {
		try {
		    if(stmt!=null) stmt.close();
		} catch (Exception ignore) {}
		try {
		    conn.close();
		} catch (Exception ignore) {}
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

}
