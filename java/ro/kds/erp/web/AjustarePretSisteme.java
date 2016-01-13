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
 * Ajustare valorica sau procentuala a preturilor sistemelor.
 *
 *
 * Created: Thu Aug 11 08:40:13 2005
 *
 * @author <a href="mailto:Mihai Giurgeanu@CRIMIRA"></a>
 * @version 1.0
 */
public class AjustarePretSisteme extends HttpServlet {

    /**
     * Actualizare baza de date.
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
	    Integer sistemeId = (Integer)it
		.lookup("java:comp/env/setum/category/sistemeId");

	    DataSource ds = (DataSource)it
		.lookup("java:comp/env/jdbc/Products");
	    Connection conn = ds.getConnection();
	    PreparedStatement stmt = null;
	    try {
		char type = httpServletRequest.getParameter("type").charAt(0);
		BigDecimal value = new BigDecimal(httpServletRequest.getParameter("value"));
		String sql = null;
		switch (type) {
		case 'p':
		    sql = "UPDATE Product SET sellPrice = (sellPrice * (100 +?))/100 WHERE categoryId in (SELECT id FROM category WHERE parentId = ?)";
		    break;
		case 'v':
		    sql = "UPDATE Product SET sellPrice = sellPrice +? WHERE categoryId in (SELECT id FROM category WHERE parentId = ?)";
		    break;
		}
		stmt = conn.prepareStatement(sql);
		stmt.setBigDecimal(1, value);
		stmt.setInt(2, sistemeId.intValue());
		
		stmt.executeUpdate();
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
