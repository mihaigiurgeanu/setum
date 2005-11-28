package ro.kds.erp.reports;

import javax.servlet.http.HttpServlet;
import javax.servlet.ServletException;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperExportManager;
import javax.sql.DataSource;

import javax.naming.Context;
import java.sql.Connection;
import javax.naming.InitialContext;

import java.io.OutputStream;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import java.io.File;
import java.io.InputStream;

/**
 * Describe class PDFServlet here.
 *
 *
 * Created: Tue Aug 09 23:40:57 2005
 *
 * @author <a href="mailto:Mihai Giurgeanu@CRIMIRA"></a>
 * @version 1.0
 */
public class PDFServlet extends HttpServlet {

    /**
     * Describe <code>doGet</code> method here.
     *
     * @param httpServletRequest a <code>HttpServletRequest</code> value
     * @param httpServletResponse a <code>HttpServletResponse</code> value
     * @exception ServletException if an error occurs
     * @exception IOException if an error occurs
     */
    public final void doGet(final HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse) throws ServletException, IOException {
	OutputStream outputStream = httpServletResponse.getOutputStream();
	httpServletResponse.setContentType("application/pdf");

	try {
	    Context it = new InitialContext();
	    DataSource ds = (DataSource)it
		.lookup("java:comp/env/jdbc/Products");
	    Connection conn = ds.getConnection();
	    
	    String jasperFileName = (new File(httpServletRequest.getServletPath())).getName();
	    jasperFileName = jasperFileName.substring(0, jasperFileName.length()-4);
	    InputStream jasperFile = getServletContext().
		getResourceAsStream("/WEB-INF/reports/" + 
				    jasperFileName + ".jasper");

	    try {

		JasperPrint report = JasperFillManager.fillReport(jasperFile, new HashMap(), conn);
		JasperExportManager.exportReportToPdfStream(report, outputStream);


	    } finally {
		try {
		    conn.close();
		} catch (Exception ignore) {}
	    }

	} catch (Exception e) {
	    e.printStackTrace();
	}

    }


}
