package ro.kds.erp.web;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.objectweb.util.monolog.api.BasicLevel;
import ro.kds.erp.biz.ResponseBean;
import org.objectweb.util.monolog.api.Logger;
import org.objectweb.jonas.common.Log;

/**
 * HTTP interface to the business logic of the "Sistem" form.
 *
 *
 * Created: Sun Oct 30 14:19:12 2005
 *
 * @author <a href="mailto:Mihai Giurgeanu@CRIMIRA"></a>
 * @version 1.0
 */
public class SistemServlet extends HttpServlet {


    private Logger logger = null;

    /**
     * The HttpRequest session attribute containing the session bean.
     */
    public final static String SESSION_ATTR = "ro.kds.erp.web.SistemServlet.session";

    /**
     * Servlet initialization.
     *
     * @exception ServletException if an error occurs
     */
    public final void init() throws ServletException {
	logger = Log.getLogger("ro.kds.erp.web.SistemServlet");
        logger.log(BasicLevel.DEBUG, "");
    }

    /**
     * Servlet's entry point.
     *
     * @param httpServletRequest a <code>HttpServletRequest</code> value
     * @param httpServletResponse a <code>HttpServletResponse</code> value
     * @exception ServletException if an error occurs
     * @exception IOException if an error occurs
     */
    public void doPost(HttpServletRequest httpServletRequest, 
		       HttpServletResponse httpServletResponse) 
	throws ServletException, IOException {

	

	logger.log(BasicLevel.DEBUG, "doPost entered");

	String command = httpServletRequest.getParameter("command");
	ResponseBean r;

	if(command == null) {
	    r = new ResponseBean();
	    r.setCode(1);
	    r.setMessage("'command' parameter was not received");
	} else {
	}
    }


}
