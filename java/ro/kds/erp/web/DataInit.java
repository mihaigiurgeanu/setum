package ro.kds.erp.web;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import org.objectweb.util.monolog.api.BasicLevel;

import org.objectweb.jonas.common.Log;

import org.objectweb.util.monolog.api.Logger;
import javax.naming.InitialContext;
import ro.kds.erp.data.CategoryLocalHome;
import javax.rmi.PortableRemoteObject;
import ro.kds.erp.data.CategoryLocal;
import java.io.PrintWriter;
import javax.naming.Context;
import java.util.Collection;

/**
 * Servlet for data initialization. It creates the objects needed
 * for using the application.
 *
 *
 * Created: Mon Sep 26 01:16:49 2005
 *
 * @author <a href="mailto:Mihai Giurgeanu@CRIMIRA"></a>
 * @version 1.0
 */
public class DataInit extends HttpServlet {

    /**
     * Performs action associated with <code>GET</code> request. It does
     * not require any HTTP parameter.
     *
     * @param httpServletRequest a <code>HttpServletRequest</code> value
     * @param httpServletResponse a <code>HttpServletResponse</code> value
     * @exception ServletException if an error occurs
     * @exception IOException if an error occurs
     */
    public final void doGet(final HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse) throws ServletException, IOException {
	
	Logger logger = Log.getLogger("ro.kds.erp.data.ProductEC2L");
        logger.log(BasicLevel.DEBUG, "");

	try {
	    InitialContext ic = new InitialContext();
	    Context env = (Context)ic.lookup("java:comp/env");
	    CategoryLocalHome ch = (CategoryLocalHome)PortableRemoteObject.
		narrow(env.lookup("ejb/CategoryLocalHome"),
		       CategoryLocalHome.class);

	    // the objects with ids lower then 10000 are considered system
	    // objects, and they have "known values" through the application
	    CategoryLocal sistemeCat = ch.create(new Integer(9999),"sisteme");
	    Collection sisteme = sistemeCat.getSubCategories();
	    sisteme.add(ch.create(new Integer(9998), "Broasca"));
	    sisteme.add(ch.create(new Integer(9997), "Cilindru"));
	    sisteme.add(ch.create(new Integer(9996), "Sild"));
	    sisteme.add(ch.create(new Integer(9995), "Yalla"));
	    ch.create(new Integer(9994), "Vizor");
	    ch.create(new Integer(9993), "Usa standard");
	    ch.create(new Integer(9992), "Usa simpla");
	    ch.create(new Integer(9991), "Usa metalica cu 1 canat");
	    ch.create(new Integer(9990), "Usa metalica cu 2 canate");
	    ch.create(new Integer(9989), "Fereastra");
	    
	    CategoryLocal othersCat = ch.create(new Integer(9988), 
						"Diverse");
	    Collection others = othersCat.getSubCategories();
	    others.add(ch.create(new Integer(9987), "Geam simmplu"));
	    others.add(ch.create(new Integer(9986), "Geam termopan"));
	    others.add(ch.create(new Integer(9985), "Grilaj"));
	    others.add(ch.create(new Integer(9984), "Tabla"));


	    ch.create(new Integer(9983), "Grila ventilatie");
	    ch.create(new Integer(9982), "Gauri aerisire");
	    ch.create(new Integer(9981), "Supralumina");
	    ch.create(new Integer(9980), "Panou lateral");
	    ch.create(new Integer(9979), "Ghiseu");

	    // the objects with ids over 10000 are user objects;
	    // create here some for convenience
	    sisteme.add(ch.create(new Integer(10000), "Cheie"));
	    sisteme.add(ch.create(new Integer(10001), "Amortizor"));
	    sisteme.add(ch.create(new Integer(10002), "Bara de protectie"));
	    sisteme.add(ch.create(new Integer(10003), "Lant de siguranta"));
	    sisteme.add(ch.create(new Integer(10004), "Rozeta"));
	    sisteme.add(ch.create(new Integer(10005), "Protectie"));
	    sisteme.add(ch.create(new Integer(10006), "Copiat cheie"));

	} catch (Exception e) {
	    logger.log(BasicLevel.ERROR, "Exception initializing beans");
	    throw new ServletException(e);
	}
	
	httpServletResponse.setContentType("text/plain");
	PrintWriter out = httpServletResponse.getWriter();
	out.println("Initialization completed");
    }

}
