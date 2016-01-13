package ro.kds.erp.reports;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import ro.kds.erp.biz.setum.basic.StandardOffer;
import ro.kds.erp.web.StandardOfferServlet;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperFillManager;
import java.util.HashMap;
import net.sf.jasperreports.engine.JasperExportManager;
import java.util.Collection;
import java.io.InputStream;
import java.io.OutputStream;
import javax.servlet.ServletException;
import java.util.Map;


/**
 * Outputs the PDF report with company's standard offer.
 *
 *
 * Created: Fri Oct 28 05:55:43 2005
 *
 * @author <a href="mailto:Mihai Giurgeanu@CRIMIRA"></a>
 * @version 1.0
 */
public class StandardOfferPDF extends HttpServlet {

    /**
     * Contains the name of the http session attribute that holds the 
     * current StandardOfferEJB session bean.
     * It is read from the init parameter "sessionName".
     */
    protected String SESSION_ATTR;

    /**
     * Read init parameters.
     *
     * @exception ServletException if an error occurs
     */
    public final void init() throws ServletException {
	SESSION_ATTR = getServletConfig().getInitParameter("sessionName");
    }

    /**
     * Outputs the PDF report. It first builds a <code>JRDataSource</code>
     * object and then pass this object to the JasperReports engine to
     * obtain the PDF content.
     *
     * @param httpServletRequest a <code>HttpServletRequest</code> value
     * @param httpServletResponse a <code>HttpServletResponse</code> value
     * @exception ServletException if an error occurs
     * @exception IOException if an error occurs
     */
    public final void doGet(final HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse) throws ServletException, IOException {
	try {
	    StandardOffer offer = (StandardOffer)httpServletRequest
		.getSession().getAttribute(SESSION_ATTR);
	    log("SESSION_ATTR: " + SESSION_ATTR);
	    log("StandardOffer: " + offer);
	    Collection reportData = offer.lineItemsCollectionMap();
	    JRDataSource ds = new JRMapCollectionDataSource(reportData);
	    Map reportParams = offer.getOfferFieldsMap();

	    InputStream jasperFile = getServletContext().
		getResourceAsStream("/WEB-INF/reports/OfertaSisteme.jasper");
	    JasperPrint report = JasperFillManager.fillReport(jasperFile, reportParams, ds);

	    OutputStream outputStream = httpServletResponse.getOutputStream();
	    httpServletResponse.setContentType("application/pdf");
	    JasperExportManager.exportReportToPdfStream(report, outputStream);
	    
	    
	} catch (Exception e) {
	    throw new ServletException(e);
	}
    }

}
