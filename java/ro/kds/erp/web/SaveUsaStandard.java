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
 * Saves the composite product of 'Usa standard' category.
 *
 *
 * Created: Thu Aug 11 07:25:34 2005
 *
 * @author <a href="mailto:Mihai Giurgeanu@CRIMIRA"></a>
 * @version 1.0
 */
public class SaveUsaStandard extends HttpServlet {

    /**
     * Interprets POST parameters and udates the database.
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
	    PreparedStatement stmt = null;
	    Integer categId = (Integer)it
		.lookup("java:comp/env/setum/category/usaStdId");
	    try {
		int id = Integer.parseInt(httpServletRequest
					  .getParameter("id"));

		stmt = conn.prepareStatement("DELETE FROM CompositeProductLink WHERE productId = ?");
		stmt.setInt(1, id);
		stmt.executeUpdate();
		stmt.close();

		stmt = conn.prepareStatement("INSERT INTO CompositeProductLink(productId, childProductId) VALUES(?,?)");
		stmt.setInt(1, id);
		try {
		    stmt.setInt(2, Integer.parseInt(httpServletRequest
						    .getParameter("usaid")));
		    stmt.executeUpdate();
		} catch (NumberFormatException ignore) {}
		try {
		    stmt.setInt(2, Integer.parseInt(httpServletRequest
						    .getParameter("broascaid")));
		    stmt.executeUpdate();
		} catch (NumberFormatException ignore) {}
		try {
		    stmt.setInt(2, Integer.parseInt(httpServletRequest
						    .getParameter("cilindruid")));
		    stmt.executeUpdate();
		} catch (NumberFormatException ignore) {}
		try {
		    stmt.setInt(2, Integer.parseInt(httpServletRequest
						    .getParameter("sildid")));
		    stmt.executeUpdate();
		} catch (NumberFormatException ignore) {}
		try {
		    stmt.setInt(2, Integer.parseInt(httpServletRequest
						    .getParameter("yallaid")));
		    stmt.executeUpdate();
		} catch (NumberFormatException ignore) {}
		try {
		    stmt.setInt(2, Integer.parseInt(httpServletRequest
						    .getParameter("vizorid")));
		    stmt.executeUpdate();
		} catch (NumberFormatException ignore) {}

		stmt.close();

	    } catch (java.sql.SQLException e) {
		code = 1;
		message = "SqlException: " + e;
		e.printStackTrace();
	    } finally {
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
