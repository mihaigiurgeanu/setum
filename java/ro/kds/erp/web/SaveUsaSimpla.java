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
import java.math.BigDecimal;
import java.sql.SQLException;
import javax.naming.Context;

/**
 * Saves a product record in the category "Usa simpla".
 *
 *
 * Created: Thu Aug 11 03:27:04 2005
 *
 * @author <a href="mailto:Mihai Giurgeanu@CRIMIRA"></a>
 * @version 1.0
 */
public class SaveUsaSimpla extends HttpServlet {

    /**
     * Interprets the POST parameters and updates the database.
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
		.lookup("java:comp/env/setum/category/usaSimplaId");
	    try {
		int id = Integer.parseInt(httpServletRequest
					  .getParameter("id"));
		String codeField = httpServletRequest.getParameter("code");
		String name = httpServletRequest.getParameter("name");
		String description = httpServletRequest
		    .getParameter("description");
		BigDecimal sellPrice = new 
		    BigDecimal(httpServletRequest.getParameter("sellPrice"));
		
		stmt = conn.prepareStatement("UPDATE Product SET code=?,name=?,description=?,categoryId=?,sellPrice=? WHERE id=? ");

		stmt.setString(1, codeField);
		stmt.setString(2, name);
		stmt.setString(3, description);
		stmt.setInt(4, categId.intValue());
		stmt.setBigDecimal(5, sellPrice);
		stmt.setInt(6, id);

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
