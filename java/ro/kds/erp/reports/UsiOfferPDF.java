package ro.kds.erp.reports;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import ro.kds.erp.biz.setum.basic.OfertaUsiStandard;
import ro.kds.erp.web.StandardOfferServlet;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperExportManager;
import java.util.Collection;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

/**
 * Displays a jasper report related to a specifiv OfferEJB object.
 * The current offer is taker from the servlet's context session attribute.
 *
 *
 * Created: Sat Nov 19 08:54:03 2005
 *
 * @author <a href="mailto:Mihai Giurgeanu@CRIMIRA"></a>
 * @version 1.0
 */
public class UsiOfferPDF extends HttpServlet {

    protected static final String SESSION_ATTR = "forms.setum.OfetaUsiStandard";

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
	    OfertaUsiStandard offer = (OfertaUsiStandard)httpServletRequest
		.getSession().getAttribute(SESSION_ATTR);
	    
	    Collection reportData = offer.lineItemsCollectionMap();
	    Map reportParams = offer.fieldsMap();

	    JRDataSource ds = new JRMapCollectionDataSource(reportData);
	    InputStream jasperFile = getServletContext().
		getResourceAsStream("/WEB-INF/reports/OfertaUsiStandard.jasper");
	    JasperPrint report = JasperFillManager.fillReport(jasperFile, reportParams, ds);

	    OutputStream outputStream = httpServletResponse.getOutputStream();
	    httpServletResponse.setContentType("application/pdf");
	    JasperExportManager.exportReportToPdfStream(report, outputStream);
	    
	    
	} catch (Exception e) {
	    throw new ServletException(e);
	}
    }

}
