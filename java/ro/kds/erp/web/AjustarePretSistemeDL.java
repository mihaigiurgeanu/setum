package ro.kds.erp.web;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import javax.naming.InitialContext;
import ro.kds.erp.data.ProductLocalHome;
import javax.rmi.PortableRemoteObject;
import ro.kds.erp.data.CategoryLocalHome;
import ro.kds.erp.data.CategoryLocal;
import ro.kds.erp.data.ProductLocal;
import javax.naming.Context;
import java.util.Collection;
import java.util.Iterator;


/**
 * Ajustare valorica sau procentuala a preturilor sistemelor.
 *
 *
 * Created: Thu Aug 11 08:40:13 2005
 *
 * @author <a href="mailto:Mihai Giurgeanu@CRIMIRA"></a>
 * @version 1.0
 */
public class AjustarePretSistemeDL extends HttpServlet {

    /**
     * Actualizare baza de date. It uses datalayer.jar ejbs.
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
	    char type = httpServletRequest.getParameter("type").charAt(0);
	    BigDecimal value = new BigDecimal(httpServletRequest.getParameter("value"));
	    try {
		InitialContext it = new InitialContext();
		Context env = (Context)it.lookup("java:comp/env");
		Integer sistemeId = (Integer)env.lookup("setum/category/sistemeId");
		ProductLocalHome ph = (ProductLocalHome)PortableRemoteObject.
		    narrow(env.lookup("ejb/ProductLocalHome"),
				      ProductLocalHome.class);
		CategoryLocalHome ch = (CategoryLocalHome)PortableRemoteObject.
		    narrow(env.lookup("ejb/CategoryLocalHome"),
				      CategoryLocalHome.class);
		Collection categs = ch.findByPrimaryKey(sistemeId)
		    .getSubCategories();
		for(Iterator i = categs.iterator(); i.hasNext(); ) {
		    Collection products = ((CategoryLocal)i.next())
			.getProducts();
		    for(Iterator j = products.iterator(); j.hasNext();) {
			ProductLocal p = (ProductLocal)j.next();
			
			switch (type) {
			case 'p':
			    p.setSellPrice(p.getSellPrice().multiply(value.add(new BigDecimal(100)).divide(new BigDecimal(100), BigDecimal.ROUND_HALF_UP)));
			    break;
			case 'v':
			    p.setSellPrice(p.getSellPrice().add(value));
			    break;
			}
		    }
		}
	    } catch (Exception e) {
		code = 1;
		message = "Exception: " + e;
		e.printStackTrace();
	    }
	} catch (Exception e) {
	    code = 2;
	    message = "Exception (maybe the params format is wrong): " + e;
	    e.printStackTrace();
	}
	out.print("<return code=\"");
	out.print(code);
	out.print("\"><![CDATA[\n" + message + "\n]]></return>");
	out.println("</response>");



    }

    
}
