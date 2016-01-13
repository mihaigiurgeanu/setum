package ro.kds.erp.web;

import javax.servlet.http.HttpServlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import javax.naming.Context;
import javax.sql.DataSource;
import javax.naming.InitialContext;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;



/**
 * SystemsTypesXML.java
 *
 *
 * Created: Tue Jul 26 12:20:12 2005
 *
 * @author <a href="mailto:mgiurg@xnet.ro"></a>
 * @version
 */

public class SystemsTypesXML extends HttpServlet {

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
	    Context it = new InitialContext();
	    DataSource ds = (DataSource)it
		.lookup("java:comp/env/jdbc/Products");
	    Connection conn = ds.getConnection();
	    Statement stmt = null;
	    Integer sistemeId = (Integer)it.lookup("java:comp/env/setum/category/sistemeId");

	    try {
		stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT id, name FROM category where parentId = " + sistemeId + " ORDER BY name");
		while(rs.next()) {
		    int id = rs.getInt("id");
		    String name = rs.getString("name");
		    
		    out.print("<record><field name=\"id\">");
		    out.print(id);
		    out.print("</field><field name=\"name\">");
		    out.print(name);
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
