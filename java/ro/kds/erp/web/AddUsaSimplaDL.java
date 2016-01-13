package ro.kds.erp.web;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import javax.naming.InitialContext;
import ro.kds.erp.data.CategoryLocalHome;
import javax.rmi.PortableRemoteObject;
import ro.kds.erp.data.ProductLocalHome;
import ro.kds.erp.data.ProductLocal;
import javax.naming.Context;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;


/**
 * Adds a product to the databse. The data about the product
 * are received in the POST parameters. The product is going
 * to be addes in the "Usa simpla" category.
 *
 * This servlet uses datalayer.jar.
 *
 * Created: Thu Aug 11 03:13:44 2005
 *
 * @author <a href="mailto:Mihai Giurgeanu@CRIMIRA"></a>
 * @version 1.0
 */
public class AddUsaSimplaDL extends HttpServlet {

    /**
     * The servlet's action execution.
     *
     * @param httpServletRequest a <code>HttpServletRequest</code> value
     * @param httpServletResponse a <code>HttpServletResponse</code> value
     * @exception ServletException if an error occurs
     * @exception IOException if an error occurs
     */
    public final void doPost(final HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse) throws ServletException, IOException {



	int code = 0;
	String message="Action completed";
	DecimalFormat numFormat =  new DecimalFormat("0.00", new DecimalFormatSymbols(new Locale("en", "us")));

	httpServletResponse.setContentType("text/xml");
	PrintWriter out = httpServletResponse.getWriter();

	out.println("<?xml version=\"1.0\"?>");
	out.print("<response>");
	try {
	    String codeField = httpServletRequest.getParameter("code");
	    String name = httpServletRequest.getParameter("name");
	    String description = httpServletRequest
		.getParameter("description");
	    BigDecimal sellPrice = new BigDecimal(numFormat.parse(httpServletRequest.getParameter("sellPrice")).doubleValue());

	    try {
		
		InitialContext it = new InitialContext();
		Context env = (Context)it.lookup("java:comp/env");
		
		Integer categId = (Integer)env
		    .lookup("setum/category/usaSimplaId");
		CategoryLocalHome ch = (CategoryLocalHome)PortableRemoteObject.
		    narrow(env.lookup("ejb/CategoryLocalHome"),
			   CategoryLocalHome.class);
		ProductLocalHome ph = (ProductLocalHome)PortableRemoteObject.
		    narrow(env.lookup("ejb/ProductLocalHome"),
			   ProductLocalHome.class);
		
		ProductLocal p = ph.create();
		p.setCode(codeField);
		p.setName(name);
		p.setDescription(description);
		p.setSellPrice(sellPrice);
		p.setCategory(ch.findByPrimaryKey(categId));

	    } catch (Exception e) {
		code = 1;
		message = "Exception: " + e;
		e.printStackTrace();
	    }
	} catch (Exception e) {
	    code = 2;
	    message = "Exception (maybe wrong param format): " + e;
	    e.printStackTrace();
	}
	out.print("<return code=\"");
	out.print(code);
	out.print("\"><![CDATA[\n" + message + "\n]]></return>");
	out.println("</response>");
    }
}
