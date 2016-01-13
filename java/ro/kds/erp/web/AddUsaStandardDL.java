package ro.kds.erp.web;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;
import javax.naming.InitialContext;
import ro.kds.erp.data.CategoryLocalHome;
import javax.rmi.PortableRemoteObject;
import ro.kds.erp.data.ProductLocalHome;
import ro.kds.erp.data.CompositeProductLocalHome;
import ro.kds.erp.data.ProductLocal;
import ro.kds.erp.data.CompositeProductLocal;
import javax.naming.Context;
import java.util.Collection;
import org.objectweb.util.monolog.api.BasicLevel;
import org.objectweb.util.monolog.api.Logger;
import org.objectweb.jonas.common.Log;

/**
 * Add to the database a composite product in category "Usa standard".
 * It uses datalayer.jar.
 *
 * Created: Thu Aug 11 05:35:14 2005
 *
 * @author <a href="mailto:Mihai Giurgeanu@CRIMIRA"></a>
 * @version 1.0
 */
public class AddUsaStandardDL extends HttpServlet {

    private Logger logger = null;

    /**
     * Describe <code>init</code> method here.
     *
     * @exception ServletException if an error occurs
     */
    public final void init() throws ServletException {
	logger = Log.getLogger("ro.kds.erp.web.AdddUsaStandardDL");
        logger.log(BasicLevel.DEBUG, "");
    }

    /**
     * Describe <code>doPost</code> method here.
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
	    InitialContext it = new InitialContext();
	    Context env = (Context)it.lookup("java:comp/env");
	    
	    Integer categId = (Integer)env
		.lookup("setum/category/usaStdId");
	    CategoryLocalHome ch = (CategoryLocalHome)PortableRemoteObject.
		narrow(env.lookup("ejb/CategoryLocalHome"),
		       CategoryLocalHome.class);
	    ProductLocalHome ph = (ProductLocalHome)PortableRemoteObject.
		narrow(env.lookup("ejb/ProductLocalHome"),
		       ProductLocalHome.class);
	    CompositeProductLocalHome cph = (CompositeProductLocalHome)
		PortableRemoteObject.
		narrow(env.lookup("ejb/CompositeProductLocalHome"),
		       CompositeProductLocalHome.class);

	    ProductLocal p = ph.create();
	    p.setCategory(ch.findByPrimaryKey(categId));

	    CompositeProductLocal cp = cph.create(p.getId());
	    p.setCompositeProduct(cp);

	    Collection parts = cp.getComponents();
	    parts.clear();
	    try {
		logger.log(BasicLevel.DEBUG, "Add part usaid: " + 
			   httpServletRequest.getParameter("usaid"));
		parts.add(ph.findByPrimaryKey(new Integer(Integer.parseInt(httpServletRequest.getParameter("usaid")))));
	    } catch (NumberFormatException ignore) {}
	    try {
		logger.log(BasicLevel.DEBUG, "Add part broascaid: " + 
			   httpServletRequest.getParameter("broascaid"));
		parts.add(ph.findByPrimaryKey(new Integer(Integer.parseInt(httpServletRequest.getParameter("broascaid")))));
	    } catch (NumberFormatException ignore) {}
	    try {
		logger.log(BasicLevel.DEBUG, "Add part cilindruid: " + 
			   httpServletRequest.getParameter("cilindruid"));
		parts.add(ph.findByPrimaryKey(new Integer(Integer.parseInt(httpServletRequest.getParameter("cilindruid")))));
	    } catch (NumberFormatException ignore) {}
	    try {
		logger.log(BasicLevel.DEBUG, "Add part sildid: " + 
			   httpServletRequest.getParameter("sildid"));
		parts.add(ph.findByPrimaryKey(new Integer(Integer.parseInt(httpServletRequest.getParameter("sildid")))));
	    } catch (NumberFormatException ignore) {}
	    try {
		logger.log(BasicLevel.DEBUG, "Add part yallaid: " + 
			   httpServletRequest.getParameter("yallaid"));
		parts.add(ph.findByPrimaryKey(new Integer(Integer.parseInt(httpServletRequest.getParameter("yallaid")))));
	    } catch (NumberFormatException ignore) {}
	    try {
		logger.log(BasicLevel.DEBUG, "Add part vizorid: " + 
			   httpServletRequest.getParameter("vizorid"));
		parts.add(ph.findByPrimaryKey(new Integer(Integer.parseInt(httpServletRequest.getParameter("vizorid")))));
	    } catch (NumberFormatException ignore) {}


	} catch (Exception e) {
	    code = 1;
	    message = "Exception: " + e;
	    e.printStackTrace();
	}
	out.print("<return code=\"");
	out.print(code);
	out.print("\"><![CDATA[\n" + message + "\n]]></return>");
	out.println("</response>");
    }   
}
