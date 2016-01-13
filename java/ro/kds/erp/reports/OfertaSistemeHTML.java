package ro.kds.erp.reports;

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
import java.text.DecimalFormat;



/**
 * OfertaSistemeHTML.java
 *
 *
 * Created: Wed Jul 27 08:20:20 2005
 *
 * @author <a href="mailto:mgiurg@xnet.ro"></a>
 * @version
 */

public class OfertaSistemeHTML extends HttpServlet {

    /**
     * Describe <code>doGet</code> method here.
     *
     * @param httpServletRequest a <code>HttpServletRequest</code> value
     * @param httpServletResponse a <code>HttpServletResponse</code> value
     * @exception ServletException if an error occurs
     * @exception IOException if an error occurs
     */
    public void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {

	DecimalFormat priceFormat = new DecimalFormat("#,##0.00");

	int crt = 0;
	
	httpServletResponse.setContentType("text/html");
	PrintWriter out = httpServletResponse.getWriter();
	out.println("<head>");
	out.println("<title>Lista de preturei sisteme</title>");
	out.println("</head>");
	
	out.println("<body>");

	out.println("<table width=\"100%\">");
	out.println("<tr>");
	out.println("<td width=\"50%\">&nbsp;</td>");
	out.println("<td width=\"50%\"><strong>");
	out.println("BUCURESTI B-dul PRECIZIEI nr. 32, Sector 6<br />");
	out.println("Contractari: Tel/Fax: 316.05.90 Tel: 316.18.56; 316.05.88<br />");
	out.println("Secretariat: Tel/Fax: 316.05.88; Tel: 316.39.57<br />");
	out.println("Centrala: Tel: 316.05.78 Dep. Economic: Tel: 317.25.40<br />");
	out.println("Magazin OBOR: sos. Mihai Bravu 6, Tel/Fax: 252.49.67<br />");
	out.println("Web: http://www.setum.xnet.ro; e-mail: setum@xnet.ro<br />");
        out.println("Capital social: 14,10 miliarde lei<br />");
	out.println("</strong></td>");
	out.println("<tr>");
	out.println("</table>");

	out.println("<hr />");

	out.println("<h1>LISTA DE PRETURI FENORERIE ABUS</h1>");

	out.println("<center>");
	out.println("<table border=1 cellspacing=0 width=80%>");
	out.println("<tr width=4em><th>Nr.<br>Crt.</th><th>Denumire produs</th><th width=15em>Pret cu TVA</th><tr>");

	try {
	    Context it = new InitialContext();
	    DataSource ds = (DataSource)it
		.lookup("java:comp/env/jdbc/Products");
	    Connection conn = ds.getConnection();
	    Statement stmt = null;
	    try {
		stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT id, name, type_id, price_entry, price_sell, price_sell1 FROM system ORDER BY name ASC");
		while(rs.next()) {
		    String id = rs.getString("id");
		    String name = rs.getString("name");
		    int typeid = rs.getInt("type_id");
		    BigDecimal entryPrice = rs.getBigDecimal("price_entry");
		    BigDecimal sellPrice = rs.getBigDecimal("price_sell");
		    BigDecimal sellPrice1 = rs.getBigDecimal("price_sell1");
		    out.print("<tr>");
		    out.print("<td align=center>");
		    out.print(crt++);
		    out.print(".</td><td>");
		    out.print(name);
		    out.print("</td><td align=right>");
		    out.print(priceFormat.format(sellPrice));
		    out.println("</td></tr>");
		}
	    } catch (java.sql.SQLException e) {
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
	    e.printStackTrace();
	}
	out.println("</table>");
	out.println("</center>");
	out.println("<hr />");
	out.println("<center><strong>Nr. RC J40/314/1991 CONT IBAN RO32RNCB5060000000240001 BCR SUC. SECTOR 6 CF:R458556</strong></center>");
	out.println("</body>");

    }
    
}// OfertaSistemeHTML
