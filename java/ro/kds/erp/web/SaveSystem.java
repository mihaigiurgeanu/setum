package ro.kds.erp.web;

import javax.servlet.http.HttpServlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import javax.naming.Context;
import javax.sql.DataSource;
import javax.naming.InitialContext;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.math.BigDecimal;
import java.sql.PreparedStatement;



/**
 * SaveSystem.java
 *
 *
 * Created: Wed Jul 27 05:05:46 2005
 *
 * @author <a href="mailto:mgiurg@xnet.ro"></a>
 * @version
 */

public class SaveSystem extends HttpServlet {

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
	    PreparedStatement stmt = null;
	    try {
		int id = Integer.parseInt(httpServletRequest
					  .getParameter("id"));
		String codeField = httpServletRequest.getParameter("code");
		String name = httpServletRequest.getParameter("name");
		int typeid = Integer.parseInt(httpServletRequest
					      .getParameter("type_id"));
		BigDecimal entryPrice = new BigDecimal(httpServletRequest.getParameter("price_entry"));
		BigDecimal sellPrice = new BigDecimal(httpServletRequest.getParameter("price_sell"));
		BigDecimal sellPrice1 = new BigDecimal(httpServletRequest.getParameter("price_sell1"));

		String typename = "";
		Statement categStmt = null;
		try {
		    categStmt = conn.createStatement();
		    ResultSet rs = categStmt.executeQuery("SELECT name FROM Category WHERE id = " + typeid);
		    if(rs.next()) {
			typename = rs.getString("name");
		    }
		} finally {
		    try{
			categStmt.close();
		    } catch (Exception ignore) {}
		}

		stmt = conn.prepareStatement("UPDATE Product SET " +
					     "code = ?, " + 
					     "name = ?, " + 
					     "categoryId = ?, " +
					     "entryPrice = ?, " +
					     "sellPrice = ?, "+
					     "price1 = ? WHERE id = ?");

		stmt.setString(1, codeField);
		stmt.setString(2, typename + " - " + codeField);
		stmt.setInt(3, typeid);
		stmt.setBigDecimal(4, entryPrice);
		stmt.setBigDecimal(5, sellPrice);
		stmt.setBigDecimal(6, sellPrice1);
		stmt.setInt(7, id);

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

    
}// SaveSystem
