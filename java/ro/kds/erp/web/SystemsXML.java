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
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.sql.SQLException;



/**
 * SystemsXML.java
 *
 *
 * Created: Wed Jul 27 03:36:58 2005
 *
 * @author <a href="mailto:mgiurg@xnet.ro"></a>
 * @version
 */

public class SystemsXML extends HttpServlet {

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
		ResultSet rs = stmt.executeQuery("SELECT id, code, name, categoryId, entryPrice, sellPrice, price1 FROM Product WHERE categoryId in (SELECT id from Category WHERE parentId = " + sistemeId + ") ORDER BY name");
		while(rs.next()) {
		    String id = rs.getString("id");
		    String name = rs.getString("name");
		    String codeField = rs.getString("code");
		    int typeid = rs.getInt("categoryId");
		    BigDecimal entryPrice = rs.getBigDecimal("entryPrice");
		    BigDecimal sellPrice = rs.getBigDecimal("sellPrice");
		    BigDecimal sellPrice1 = rs.getBigDecimal("price1");
		    DecimalFormat dec = new DecimalFormat("0.00");
		    out.print("<record><field name=\"id\">");
		    out.print(id);
		    out.print("</field><field name=\"code\">");
		    out.print(codeField);
		    out.print("</field><field name=\"name\">");
		    out.print(name);
		    out.print("</field><field name=\"type_id\">");
		    out.print(typeid);
		    out.print("</field><field name=\"price_entry\">");
		    out.print(dec.format(entryPrice));
		    out.print("</field><field name=\"price_sell\">");
		    out.print(dec.format(sellPrice));
		    out.print("</field><field name=\"price_sell1\">");

		    out.print(dec.format(sellPrice1));
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
    
}// SystemsXML
