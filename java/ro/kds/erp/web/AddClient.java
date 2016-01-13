package ro.kds.erp.web;

import javax.servlet.http.HttpServlet;
import javax.servlet.ServletException;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.naming.Context;

/**
 * Describe class AddClient here.
 *
 *
 * Created: Thu Sep 08 06:15:19 2005
 *
 * @author <a href="mailto:Mihai Giurgeanu@CRIMIRA"></a>
 * @version 1.0
 */
public class AddClient extends HttpServlet {

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
	    PreparedStatement stmt = null;

	    try {
		
		stmt = conn.prepareStatement("INSERT INTO Client(isCompany, firstName, lastName, companyName, address, postalCode, city, countryCode, companyCode, phone, iban, bank, comment) " +
					     "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)");
		stmt.setInt(1, Integer.parseInt(httpServletRequest.getParameter("isCompany")));
		stmt.setString(2, httpServletRequest.getParameter("firstName"));
		stmt.setString(3, httpServletRequest.getParameter("lasttName"));
		stmt.setString(4, httpServletRequest.getParameter("companyName"));
		stmt.setString(5, httpServletRequest.getParameter("address"));
		stmt.setString(6, httpServletRequest.getParameter("postalCode"));
		stmt.setString(7, httpServletRequest.getParameter("city"));
		stmt.setString(8, httpServletRequest.getParameter("countryCode"));
		stmt.setString(9, httpServletRequest.getParameter("companyCode"));
		stmt.setString(10, httpServletRequest.getParameter("phone"));
		stmt.setString(11, httpServletRequest.getParameter("iban"));
		stmt.setString(12, httpServletRequest.getParameter("bank"));
		stmt.setString(13, httpServletRequest.getParameter("comment"));

		stmt.execute();
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
