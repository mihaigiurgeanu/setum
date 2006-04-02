package ro.kds.erp.reports;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import javax.naming.InitialContext;
import ro.kds.erp.data.CategoryLocalHome;
import javax.rmi.PortableRemoteObject;
import ro.kds.erp.data.ProductLocal;
import java.util.HashMap;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperExportManager;
import javax.naming.Context;
import java.util.Collection;
import java.util.Iterator;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import ro.kds.erp.data.CategoryLocal;
import java.util.Collections;
import java.util.Comparator;


/**
 * Instantiates an JasperReports report.
 * DEPRECATED: use StandardOffer instead.
 *
 * Created: Mon Oct 10 06:12:57 2005
 *
 * @author <a href="mailto:Mihai Giurgeanu@CRIMIRA"></a>
 * @version 1.0
 */
public class OfertaSistemePDF extends HttpServlet {

    /**
     * Instantiates the Jasper report and sends out the PDF result.
     *
     * @param httpServletRequest a <code>HttpServletRequest</code> value
     * @param httpServletResponse a <code>HttpServletResponse</code> value
     * @exception ServletException if an error occurs
     * @exception IOException if an error occurs
     */
    public final void doGet(final HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse) throws ServletException, IOException {
	try {
	    Context ic = new InitialContext();
	    Context env = (Context)ic.lookup("java:comp/env");
	    CategoryLocalHome ch = (CategoryLocalHome)PortableRemoteObject.
		narrow(env.lookup("ejb/CategoryLocalHome"),
		       CategoryLocalHome.class);
	    Integer sistemeId = (Integer)env.
		lookup("setum/category/sistemeId");
	    CategoryLocal c = ch.findByPrimaryKey(sistemeId);
	    ArrayList reportData = new ArrayList();
	    ArrayList categs = new ArrayList(c.getSubCategories());
	    Collections.sort(categs,
			     new Comparator() {
				 public int compare(Object o1, Object o2) {
				     return ((CategoryLocal)o1).getName().
					 compareTo(((CategoryLocal)o2).
						 getName());
				 }
				 public boolean equals(Object obj) {
				     return super.equals(obj);
				 }
			     });

	    for(Iterator j = categs.iterator(); j.hasNext(); ) {
		CategoryLocal cat = (CategoryLocal)j.next();
		ArrayList products = new ArrayList(cat.getProducts());
		Collections.sort(products,
				 new Comparator() {
				     public int compare(Object o1, Object o2) {
					 return ((ProductLocal)o1).getName().
					     compareTo(((ProductLocal)o2).
						       getName());
				     }
				     public boolean equals(Object obj) {
					 return super.equals(obj);
				     }
				 });
				 
		for(Iterator i = products.iterator(); i.hasNext(); ) {
		    ProductLocal p = (ProductLocal)i.next();
		    HashMap dataRow = new HashMap();
		    dataRow.put("name", cat.getName() + " - " + p.getName());
		    dataRow.put("code", p.getCode());
		    dataRow.put("sellPrice", p.getSellPrice());
		    reportData.add(dataRow);
		}
	    }
	    
	    JRDataSource ds = new JRMapCollectionDataSource(reportData);
	    InputStream jasperFile = getServletContext().
		getResourceAsStream("/WEB-INF/reports/OfertaSisteme.jasper");
	    JasperPrint report = JasperFillManager.fillReport(jasperFile, new HashMap(), ds);

	    OutputStream outputStream = httpServletResponse.getOutputStream();
	    httpServletResponse.setContentType("application/pdf");
	    JasperExportManager.exportReportToPdfStream(report, outputStream);
	} catch (Exception e) {
	    throw new ServletException(e);
	}
    }


}
