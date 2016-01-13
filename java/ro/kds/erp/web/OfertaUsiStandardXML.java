package ro.kds.erp.web;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import javax.naming.InitialContext;

import javax.naming.Context;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;

import java.sql.Statement;
import java.math.BigDecimal;
import java.sql.ResultSet;
import javax.naming.NamingException;

/**
 * Describe class OfertaUsiStandardXML here.
 *
 *
 * Created: Wed Aug 10 14:55:52 2005
 *
 * @author <a href="mailto:Mihai Giurgeanu@CRIMIRA"></a>
 * @version 1.0
 */
public class OfertaUsiStandardXML extends HttpServlet {

    /**
     * Calls doPost(). It is used for debug purposes.
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
     * Builds the XML data set.
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
	    PreparedStatement pstmt = null;

	    String sqlUsiStd = 
		"SELECT id, name " +
		" FROM Product" +
		" WHERE categoryId = " + USA_STD_ID;
	    String sqlEchipareUsi = 
		"SELECT p.id, p.name, p.code, p.description, p.sellPrice " +
		" FROM Product p, CompositeProductLink cpl" +
		" WHERE p.id = cpl.childProductId" +
		" AND cpl.productId = ?" +
		" AND p.categoryId = ?";

	    try {
		stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sqlUsiStd);
		pstmt = conn.prepareStatement(sqlEchipareUsi);

		while(rs.next()) {
		    BigDecimal compositePrice = new BigDecimal(0);
		    int id = rs.getInt("id");
		    String name = rs.getString("name");
		    
		    out.print("<record><field name=\"id\">");
		    out.print(id);
		    out.print("</field>");


		    pstmt.setInt(1, id);
		    pstmt.setInt(2, USA_SIMPLA_ID.intValue());
		    ResultSet rsDetail = pstmt.executeQuery();
		    if(rsDetail.next()) {
			out.print("<field name=\"usa.id\">");
			out.print(rsDetail.getInt("p.id"));
			out.print("</field>");
			out.print("<field name=\"usa.name\">");
			out.print(rsDetail.getString("p.name"));
			out.print("</field>");
			out.print("<field name=\"usa.code\">");
			out.print(rsDetail.getString("p.code"));
			out.print("</field>");
			out.print("<field name=\"usa.description\">");
			out.print(rsDetail.getString("p.description"));
			out.print("</field>");
			out.print("<field name=\"usa.sellPrice\">");
			out.print(rsDetail.getBigDecimal("p.sellPrice"));
			out.print("</field>");
			compositePrice = compositePrice.add(rsDetail.getBigDecimal("p.sellPrice"));
		    } else {
			out.print("<field name=\"usa.id\">");
			out.print("null");
			out.print("</field>");
			out.print("<field name=\"usa.name\">");
			out.print("-");
			out.print("</field>");
			out.print("<field name=\"usa.code\">");
			out.print("null");
			out.print("</field>");
			out.print("<field name=\"usa.description\">");
			out.print("");
			out.print("</field>");
			out.print("<field name=\"usa.sellPrice\">");
			out.print("null");
			out.print("</field>");
		    }



		    pstmt.setInt(1, id);
		    pstmt.setInt(2, BROASCA_ID.intValue());
		    rsDetail = pstmt.executeQuery();
		    if(rsDetail.next()) {
			out.print("<field name=\"broasca.id\">");
			out.print(rsDetail.getInt("p.id"));
			out.print("</field>");
			out.print("<field name=\"broasca.name\">");
			out.print(rsDetail.getString("p.name"));
			out.print("</field>");
			out.print("<field name=\"broasca.code\">");
			out.print(rsDetail.getString("p.code"));
			out.print("</field>");
			out.print("<field name=\"broasca.description\">");
			out.print(rsDetail.getString("p.description"));
			out.print("</field>");
			out.print("<field name=\"broasca.sellPrice\">");
			out.print(rsDetail.getBigDecimal("p.sellPrice"));
			out.print("</field>");
			compositePrice = compositePrice.add(rsDetail.getBigDecimal("p.sellPrice"));
		    } else {
			out.print("<field name=\"broasca.id\">");
			out.print("null");
			out.print("</field>");
			out.print("<field name=\"broasca.name\">");
			out.print("-");
			out.print("</field>");
			out.print("<field name=\"usa.code\">");
			out.print("null");
			out.print("</field>");
			out.print("<field name=\"usa.description\">");
			out.print("");
			out.print("</field>");
			out.print("<field name=\"usa.sellPrice\">");
			out.print("null");
			out.print("</field>");
		    }


		    pstmt.setInt(1, id);
		    pstmt.setInt(2, CILINDRU_ID.intValue());
		    rsDetail = pstmt.executeQuery();
		    if(rsDetail.next()) {
			out.print("<field name=\"cilindru.id\">");
			out.print(rsDetail.getInt("p.id"));
			out.print("</field>");
			out.print("<field name=\"cilindru.name\">");
			out.print(rsDetail.getString("p.name"));
			out.print("</field>");
			compositePrice = compositePrice.add(rsDetail.getBigDecimal("p.sellPrice"));
		    } else {
			out.print("<field name=\"cilindru.id\">");
			out.print("null");
			out.print("</field>");
			out.print("<field name=\"cilindru.name\">");
			out.print("-");
			out.print("</field>");
		    }


		    pstmt.setInt(1, id);
		    pstmt.setInt(2, SILD_ID.intValue());
		    rsDetail = pstmt.executeQuery();
		    if(rsDetail.next()) {
			out.print("<field name=\"sild.id\">");
			out.print(rsDetail.getInt("p.id"));
			out.print("</field>");
			out.print("<field name=\"sild.name\">");
			out.print(rsDetail.getString("p.name"));
			out.print("</field>");
			compositePrice = compositePrice.add(rsDetail.getBigDecimal("p.sellPrice"));
		    } else {
			out.print("<field name=\"sild.id\">");
			out.print("null");
			out.print("</field>");
			out.print("<field name=\"sild.name\">");
			out.print("-");
			out.print("</field>");
		    }


		    pstmt.setInt(1, id);
		    pstmt.setInt(2, YALLA_ID.intValue());
		    rsDetail = pstmt.executeQuery();
		    if(rsDetail.next()) {
			out.print("<field name=\"yalla.id\">");
			out.print(rsDetail.getInt("p.id"));
			out.print("</field>");
			out.print("<field name=\"yalla.name\">");
			out.print(rsDetail.getString("p.name"));
			out.print("</field>");
			compositePrice = compositePrice.add(rsDetail.getBigDecimal("p.sellPrice"));
		    } else {
			out.print("<field name=\"yalla.id\">");
			out.print("null");
			out.print("</field>");
			out.print("<field name=\"yalla.name\">");
			out.print("-");
			out.print("</field>");
		    }



		    pstmt.setInt(1, id);
		    pstmt.setInt(2, VIZOR_ID.intValue());
		    rsDetail = pstmt.executeQuery();
		    if(rsDetail.next()) {
			out.print("<field name=\"vizor.id\">");
			out.print(rsDetail.getInt("p.id"));
			out.print("</field>");
			out.print("<field name=\"vizor.name\">");
			out.print(rsDetail.getString("p.name"));
			out.print("</field>");
			compositePrice = compositePrice.add(rsDetail.getBigDecimal("p.sellPrice"));
		    } else {
			out.print("<field name=\"vizor.id\">");
			out.print("null");
			out.print("</field>");
			out.print("<field name=\"vizor.name\">");
			out.print("-");
			out.print("</field>");
		    }



		    out.print("<field name=\"sellPrice\">");
		    out.print(compositePrice);
		    out.print("</field>");
		    out.println("</record>");

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
		    if(pstmt!=null) pstmt.close();
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

    private Integer USA_STD_ID;
    private Integer USA_SIMPLA_ID;
    private Integer BROASCA_ID;
    private Integer CILINDRU_ID;
    private Integer SILD_ID;
    private Integer YALLA_ID;
    private Integer VIZOR_ID;

    public OfertaUsiStandardXML() throws NamingException {
	Context it = new InitialContext();
	
	USA_STD_ID = (Integer)it
	    .lookup("java:comp/env/setum/category/usaStdId");
	USA_SIMPLA_ID = (Integer)it
	    .lookup("java:comp/env/setum/category/usaSimplaId");
	BROASCA_ID = (Integer)it
	    .lookup("java:comp/env/setum/category/broascaId");
	CILINDRU_ID = (Integer)it
	    .lookup("java:comp/env/setum/category/cilindruId");
	SILD_ID = (Integer)it
	    .lookup("java:comp/env/setum/category/sildId");
	YALLA_ID = (Integer)it
	    .lookup("java:comp/env/setum/category/yallaId");
	VIZOR_ID = (Integer)it
	    .lookup("java:comp/env/setum/category/vizorId");

    }
}
