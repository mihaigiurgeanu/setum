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
import javax.ejb.FinderException;

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
	
	Logger logger = Log.getLogger("ro.kds.erp.data.DataInit");

	try {

	    // the objects with ids lower then 10000 are considered system
	    // objects, and they have "known values" through the application
	    CategoryLocal sistemeCat = createCategory(9999, "sisteme");
// 	    Collection sisteme = sistemeCat.getSubCategories();
	    addSub(sistemeCat, 9998, "Broasca");
	    addSub(sistemeCat, 9997, "Cilindru");
// 	    sisteme.add(ch.create(new Integer(9996), "Sild"));
	    addSub(sistemeCat, 9996, "Sild");
// 	    sisteme.add(ch.create(new Integer(9995), "Yalla"));
	    addSub(sistemeCat, 9995, "Yalla");
// 	    ch.create(new Integer(9994), "Vizor");
	    createCategory(9994, "Vizor");
// 	    ch.create(new Integer(9993), "Usa standard");
	    createCategory(9993, "Usa standard");
// 	    ch.create(new Integer(9992), "Usa simpla");
	    createCategory(9992, "Usa simpla");
// 	    ch.create(new Integer(9991), "Usa metalica cu 1 canat");
//	    createCategory(9991, "Usa metalica cu 1 canat");
// 	    ch.create(new Integer(9990), "Usa metalica cu 2 canate");
	    createCategory(9990, "Usa metalica");
// 	    ch.create(new Integer(9989), "Fereastra");
	    createCategory(9989, "Fereastra");

// 	    CategoryLocal othersCat = ch.create(new Integer(9988), 
// 						"Diverse");
	    CategoryLocal othersCat = createCategory(9988, "Diverse");
// 	    Collection others = othersCat.getSubCategories();
// 	    others.add(ch.create(new Integer(9987), "Geam simmplu"));
	    addSub(othersCat, 9987, "Gean simplu");
// 	    others.add(ch.create(new Integer(9986), "Geam termopan"));
	    addSub(othersCat, 9986, "Gean termopan");
// 	    others.add(ch.create(new Integer(9985), "Grilaj"));
	    addSub(othersCat, 9985, "Grilaj");
// 	    others.add(ch.create(new Integer(9984), "Tabla"));
	    addSub(othersCat, 9984, "Tabla");

// 	    ch.create(new Integer(9983), "Grila ventilatie");
	    createCategory(9983, "Grila ventilatie");
// 	    ch.create(new Integer(9982), "Gauri aerisire");
	    createCategory(9982, "Gauri aerisire");
// 	    ch.create(new Integer(9981), "Supralumina");
	    createCategory(9981, "Supralumina");
// 	    ch.create(new Integer(9980), "Panou lateral");
	    createCategory(9980, "Panou lateral");
// 	    ch.create(new Integer(9979), "Ghiseu");
	    createCategory(9979, "Ghiseu");

// 	    others.add(ch.create(new Integer(9978), "Pret grila ventilatie"));
	    addSub(othersCat, 9978, "Pret grila ventilatie");

// 	    sisteme.add(ch.create(new Integer(9977), "Maner"));
	    addSub(sistemeCat, 9977, "Maner");
// 	    sisteme.add(ch.create(new Integer(9976), "Bara antipanica"));
	    addSub(sistemeCat, 9976, "Bara antipanica");
// 	    sisteme.add(ch.create(new Integer(9975), "Selector ordine"));
	    addSub(sistemeCat, 9975, "Selector ordine");
// 	    sisteme.add(ch.create(new Integer(9974), "Decupare sistem"));
	    addSub(sistemeCat, 9974, "Decupare sistem");



// 	    CategoryLocal valueListsCategory = ch.create(new Integer(9973), "Liste de valori");
	    CategoryLocal valueListsCategory = createCategory(9973, "Liste de valori");


// 	    // the objects with ids over 10000 are user objects;
// 	    // create here some for convenience
// 	    sisteme.add(ch.create(new Integer(10000), "Cheie"));
	    addSub(sistemeCat, 10000, "Cheie");
// 	    sisteme.add(ch.create(new Integer(10001), "Amortizor"));
	    addSub(sistemeCat, 10001, "Amortizor");
// 	    sisteme.add(ch.create(new Integer(10002), "Bara de protectie"));
	    addSub(sistemeCat, 10002, "BaraProtectie");
// 	    sisteme.add(ch.create(new Integer(10003), "Lant de siguranta"));
	    addSub(sistemeCat, 10003, "Lant de siguranta");
// 	    sisteme.add(ch.create(new Integer(10004), "Rozeta"));
	    addSub(sistemeCat, 10004, "Rozeta");
// 	    sisteme.add(ch.create(new Integer(10005), "Protectie"));
	    addSub(sistemeCat, 10005, "Protectie");
// 	    sisteme.add(ch.create(new Integer(10006), "Copiat cheie"));
	    addSub(sistemeCat, 10006, "Copiat cheie");


	} catch (Exception e) {
	    logger.log(BasicLevel.ERROR, "Exception initializing beans");
	    throw new ServletException(e);
	}
	
	httpServletResponse.setContentType("text/plain");
	PrintWriter out = httpServletResponse.getWriter();
	out.println("Initialization completed");
    }


    /**
     * Helper method to create a category
     */
    private CategoryLocal createCategory(int id, String name) {
	Logger logger = Log.getLogger("ro.kds.erp.data.DataInit");

	try {
	    logger.log(BasicLevel.DEBUG, "Creating category " + id + " " + name);

	    InitialContext ic = new InitialContext();
	    Context env = (Context)ic.lookup("java:comp/env");
	    CategoryLocalHome ch = (CategoryLocalHome)PortableRemoteObject.
		narrow(env.lookup("ejb/CategoryHome"),
		       CategoryLocalHome.class);

	    CategoryLocal c;
	    try {
		c = ch.findByPrimaryKey(new Integer(id));
		logger.log(BasicLevel.DEBUG, "Category exists " + id + " " + name);
	    } catch (FinderException e) {

		logger.log(BasicLevel.DEBUG, "Category does not exist " + id + " " + name);
		c =  ch.create(new Integer(id), name);
		logger.log(BasicLevel.INFO, "Category created: " + id + " " + name);
	    }
	    return c;
	    
	} catch (Exception e) {
	    logger.log(BasicLevel.DEBUG, e);
	}
	return null;
    }

    /**
     * Helper method to create and add a subcategory
     */
    private CategoryLocal addSub(CategoryLocal cat, int id, String name) {
	Logger logger = Log.getLogger("ro.kds.erp.data.DataInit");

	try {
	    logger.log(BasicLevel.DEBUG, "Adding new sub cat " + id +
		       " " + name + " to category " + cat.getId() +
		       " " + cat.getName());
	    CategoryLocal c;
	    c = createCategory(id, name);
	    cat.getSubCategories().add(c);
	    logger.log(BasicLevel.INFO, "Category added " + c.getId() +
		       " " + c.getName() + " to the parrent " +
		       cat.getId() + " " + cat.getName());
	    return c;
	} catch (Exception e) {
	    logger.log(BasicLevel.DEBUG, e);
	}
	return null;
    }
}
