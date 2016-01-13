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
import ro.kds.erp.data.ProductLocalHome;
import javax.rmi.PortableRemoteObject;
import ro.kds.erp.data.ProductLocal;
import ro.kds.erp.data.CategoryLocalHome;
import ro.kds.erp.data.CategoryLocal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;



/**
 * SaveSystemDL.java - Like SaveSystem but the access to the database
 * is done through ejb layer datalayer.jar.
 *
 *
 * Created: Wed Jul 27 05:05:46 2005
 *
 * @author <a href="mailto:mgiurg@xnet.ro"></a>
 * @version
 */

public class SaveSystemDL extends HttpServlet {

    public void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {

	int code = 0;
	String message="Action completed";


	httpServletResponse.setContentType("text/xml");
	PrintWriter out = httpServletResponse.getWriter();
	DecimalFormat numFormat =  new DecimalFormat("0.00", new DecimalFormatSymbols(new Locale("en", "us")));

	out.println("<?xml version=\"1.0\"?>");
	out.print("<response>");
	try {
	    int id = Integer.parseInt(httpServletRequest
				      .getParameter("id"));
	    String codeField = httpServletRequest.getParameter("code");
	    String name = httpServletRequest.getParameter("name");
	    int typeid = Integer.parseInt(httpServletRequest
					  .getParameter("type_id"));
	    BigDecimal entryPrice = new BigDecimal(numFormat.parse(httpServletRequest.getParameter("price_entry")).doubleValue());
	    BigDecimal sellPrice = new BigDecimal(numFormat.parse(httpServletRequest.getParameter("price_sell")).doubleValue());
	    BigDecimal sellPrice1 = new BigDecimal(numFormat.parse(httpServletRequest.getParameter("price_sell1")).doubleValue());
	    try {
		InitialContext it = new InitialContext();
		Context env = (Context)it.lookup("java:comp/env");
		ProductLocalHome ph = (ProductLocalHome)PortableRemoteObject.
		    narrow(env.lookup("ejb/ProductLocalHome"),
				      ProductLocalHome.class);
		ProductLocal p = ph.findByPrimaryKey(new Integer(id));
		CategoryLocalHome ch = (CategoryLocalHome)PortableRemoteObject.
		    narrow(env.lookup("ejb/CategoryLocalHome"),
				      CategoryLocalHome.class);
		CategoryLocal c = ch.findByPrimaryKey(new Integer(typeid));
	    
		p.setCode(codeField);
		p.setName(name);
		p.setCategory(c);
		p.setEntryPrice(entryPrice);
		p.setSellPrice(sellPrice);
		p.setPrice1(sellPrice1);

	    } catch (Exception e) {
		code = 1;
		message = "Exception: " + e;
		e.printStackTrace();
	    }
	} catch (Exception e) {
	    code = 2;
	    message = "Exception (maybe bad HTTP params): " + e;
	    e.printStackTrace();
	}
	out.print("<return code=\"");
	out.print(code);
	out.print("\"><![CDATA[\n" + message + "\n]]></return>");
	out.println("</response>");
    }

    
}// SaveSystem
