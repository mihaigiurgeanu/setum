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
import javax.naming.NamingException;
import java.math.BigDecimal;
import javax.naming.Context;
import java.util.Collection;
import java.util.Iterator;
import ro.kds.erp.data.CategoryLocal;
import javax.servlet.ServletException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;


/**
 * Servlet that returns a list of "UsiStandard" products. It 
 * usses datalayer.jar ejbs.
 *
 *
 * Created: Wed Aug 10 14:55:52 2005
 *
 * @author <a href="mailto:Mihai Giurgeanu@CRIMIRA"></a>
 * @version 1.0
 */
public class OfertaUsiStandardXMLDL extends HttpServlet {

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
	DecimalFormat dec =  new DecimalFormat("0.00", new DecimalFormatSymbols(new Locale("en", "us")));

	httpServletResponse.setContentType("text/xml");
	PrintWriter out = httpServletResponse.getWriter();

	out.println("<?xml version=\"1.0\"?>");
	out.print("<response>");
	try {
	    InitialContext it = new InitialContext();
	    Context env = (Context)it.lookup("java:comp/env");

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

	    CategoryLocal usiCat = ch.findByPrimaryKey(USA_STD_ID);
	    Collection usi = usiCat.getProducts();

	    for(Iterator i = usi.iterator(); i.hasNext(); ) {
		ProductLocal usa = (ProductLocal)i.next();

		out.print("<record><field name=\"id\">");
		out.print(usa.getId());
		out.print("</field>");
		
		ProductBean usaSimpla = new ProductBean();
		ProductBean broasca = new ProductBean();
		ProductBean cilindru = new ProductBean();
		ProductBean sild = new ProductBean();
		ProductBean yalla = new ProductBean();
		ProductBean vizor = new ProductBean();
		BigDecimal compositePrice;
		compositePrice = new BigDecimal(0);

		for(Iterator j = 
			usa.getCompositeProduct().getComponents().iterator();
		    j.hasNext(); ) {
		    
		    ProductLocal p = (ProductLocal)j.next();
		    if(p.getCategory().getId().intValue() == 
		       USA_SIMPLA_ID.intValue()) {
			
			copyProduct(p, usaSimpla);
		    }
		    if(p.getCategory().getId().intValue() == 
		       BROASCA_ID.intValue()) {
			
			copyProduct(p, broasca);
		    }
		    if(p.getCategory().getId().intValue() == 
		       CILINDRU_ID.intValue()) {
			
			copyProduct(p, cilindru);
		    }
		    if(p.getCategory().getId().intValue() == 
		       SILD_ID.intValue()) {
			
			copyProduct(p, sild);
		    }
		    if(p.getCategory().getId().intValue() == 
		       YALLA_ID.intValue()) {
			
			copyProduct(p, yalla);
		    }
		    if(p.getCategory().getId().intValue() == 
		       VIZOR_ID.intValue()) {
			
			copyProduct(p, vizor);
		    }
		}

		out.print("<field name=\"usa.id\">");
		out.print(usaSimpla.getId());
		out.print("</field>");
		out.print("<field name=\"usa.name\">");
		out.print(usaSimpla.getName());
		out.print("</field>");
		out.print("<field name=\"usa.code\">");
		out.print(usaSimpla.getCode());
		out.print("</field>");
		out.print("<field name=\"usa.description\">");
		out.print(usaSimpla.getDescription());
		out.print("</field>");
		out.print("<field name=\"usa.sellPrice\">");
		out.print(dec.format(usaSimpla.getSellPrice()));
		out.print("</field>");
		if(usaSimpla.getSellPrice() != null)
		    compositePrice = compositePrice
			.add(usaSimpla.getSellPrice());

		out.print("<field name=\"broasca.id\">");
		out.print(broasca.getId());
		out.print("</field>");
		out.print("<field name=\"broasca.name\">");
		out.print(broasca.getName());
		out.print("</field>");
		out.print("<field name=\"broasca.code\">");
		out.print(broasca.getCode());
		out.print("</field>");
		out.print("<field name=\"broasca.description\">");
		out.print(broasca.getDescription());
		out.print("</field>");
		out.print("<field name=\"broasca.sellPrice\">");
		out.print(broasca.getSellPrice());
		out.print("</field>");
		if(broasca.getSellPrice() != null)
		    compositePrice = compositePrice.add(broasca.getSellPrice());
		out.print("<field name=\"cilindru.id\">");
		out.print(cilindru.getId());
		out.print("</field>");
		out.print("<field name=\"cilindru.name\">");
		out.print(cilindru.getName());
		out.print("</field>");
		if(cilindru.getSellPrice() != null)
		    compositePrice = compositePrice.add(cilindru.getSellPrice());

		out.print("<field name=\"sild.id\">");
		out.print(sild.getId());
		out.print("</field>");
		out.print("<field name=\"sild.name\">");
		out.print(sild.getName());
		out.print("</field>");
		if(sild.getSellPrice() != null)
		    compositePrice = compositePrice.add(sild.getSellPrice());

		out.print("<field name=\"yalla.id\">");
		out.print(yalla.getId());
		out.print("</field>");
		out.print("<field name=\"yalla.name\">");
		out.print(yalla.getName());
		out.print("</field>");
		if(yalla.getSellPrice() != null)
		    compositePrice = compositePrice.add(yalla.getSellPrice());

		out.print("<field name=\"vizor.id\">");
		out.print(vizor.getId());
		out.print("</field>");
		out.print("<field name=\"vizor.name\">");
		out.print(vizor.getName());
		out.print("</field>");
		if(vizor.getSellPrice() != null)
		    compositePrice = compositePrice.add(vizor.getSellPrice());


		out.print("<field name=\"sellPrice\">");
		out.print(dec.format(compositePrice));
		out.print("</field>");
		out.println("</record>");
		
	    }

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

    private Integer USA_STD_ID;
    private Integer USA_SIMPLA_ID;
    private Integer BROASCA_ID;
    private Integer CILINDRU_ID;
    private Integer SILD_ID;
    private Integer YALLA_ID;
    private Integer VIZOR_ID;

    /**
     * Servlet's initialization. It gets the well known ids values from
     * the servlet's environment.
     *
     * @exception ServletException if an error occurs
     */
    public final void init() throws ServletException {
	try {
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
	} catch (NamingException e) {
	    throw new ServletException(e);
	}
    }

    void copyProduct(ProductLocal p, ProductBean b) {
	b.setId(p.getId());
	b.setName(p.getName());
	b.setCode(p.getCode());
	b.setDescription(p.getDescription());
	b.setSellPrice(p.getSellPrice());
    }
}

class ProductBean {
    private Integer id;
    private String name;
    private String code;
    private String description;
    private BigDecimal sellPrice;

    
    /**
     * Gets the value of id
     *
     * @return the value of id
     */
    public final Integer getId() {
	return this.id;
    }

    /**
     * Sets the value of id
     *
     * @param argId Value to assign to this.id
     */
    public final void setId(final Integer argId) {
	this.id = argId;
    }

    /**
     * Gets the value of name
     *
     * @return the value of name
     */
    public final String getName() {
	return this.name;
    }

    /**
     * Sets the value of name
     *
     * @param argName Value to assign to this.name
     */
    public final void setName(final String argName) {
	this.name = argName;
    }

    /**
     * Gets the value of code
     *
     * @return the value of code
     */
    public final String getCode() {
	return this.code;
    }

    /**
     * Sets the value of code
     *
     * @param argCode Value to assign to this.code
     */
    public final void setCode(final String argCode) {
	this.code = argCode;
    }

    /**
     * Gets the value of description
     *
     * @return the value of description
     */
    public final String getDescription() {
	return this.description;
    }

    /**
     * Sets the value of description
     *
     * @param argDescription Value to assign to this.description
     */
    public final void setDescription(final String argDescription) {
	this.description = argDescription;
    }

    /**
     * Gets the value of sellPrice
     *
     * @return the value of sellPrice
     */
    public final BigDecimal getSellPrice() {
	return this.sellPrice;
    }

    /**
     * Sets the value of sellPrice
     *
     * @param argSellPrice Value to assign to this.sellPrice
     */
    public final void setSellPrice(final BigDecimal argSellPrice) {
	this.sellPrice = argSellPrice;
    }

    public ProductBean() {
	setId(null);
	setName("-");
	setCode("-");
	setDescription("-");
	setSellPrice(null);
    }
}
