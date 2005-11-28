package ro.kds.erp.web;

import javax.servlet.http.HttpServlet;
import java.io.PrintWriter;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.naming.Context;
import java.sql.Statement;
import javax.servlet.ServletException;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Describe class ContactsXML here.
 *
 *
 * Created: Thu Sep 08 07:17:54 2005
 *
 * @author <a href="mailto:Mihai Giurgeanu@CRIMIRA"></a>
 * @version 1.0
 */
public class ContactsXML extends HttpServlet {

    /**
     * Describe <code>doPost</code> method here.
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
		.lookup("java:comp/env/jdbc/Clients");
	    Connection conn = ds.getConnection();
	    Statement stmt = null;

	    try {
		stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT id, clientId, firstName, lastName, department, phone, mobile, fax, email, title, comment FROM Contact ORDER BY lastName, firstName");
		while(rs.next()) {		    
		    out.print("<record><field name=\"id\">");
		    out.print(rs.getInt("id"));
		    out.print("</field><field name=\"clientId\">");
		    out.print(rs.getInt("clientId"));
		    out.print("</field><field name=\"firstName\">");
		    out.print(rs.getString("firstName"));
		    out.print("</field><field name=\"lastName\">");
		    out.print(rs.getString("lastName"));
		    out.print("</field><field name=\"department\">");
		    out.print(rs.getString("department"));
		    out.print("</field><field name=\"phone\">");
		    out.print(rs.getString("phone"));
		    out.print("</field><field name=\"mobile\">");
		    out.print(rs.getString("mobile"));
		    out.print("</field><field name=\"fax\">");
		    out.print(rs.getString("fax"));
		    out.print("</field><field name=\"email\">");
		    out.print(rs.getString("email"));
		    out.print("</field><field name=\"title\">");
		    out.print(rs.getString("title"));
		    out.print("</field><field name=\"comment\">");
		    out.print(rs.getString("comment"));
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

}
