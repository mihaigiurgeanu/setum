package ro.kds.erp.web;

import javax.servlet.http.HttpServlet;
import javax.servlet.ServletException;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ro.kds.erp.biz.ResponseBean;
import java.io.PrintWriter;
import ro.kds.erp.biz.setum.basic.UsaMetalica1KHome;
import javax.rmi.PortableRemoteObject;
import ro.kds.erp.biz.setum.basic.UsaMetalica1K;
import javax.naming.NamingException;
import java.rmi.RemoteException;
import javax.naming.InitialContext;
import javax.naming.Context;
import javax.ejb.CreateException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.ServletException;
import org.objectweb.util.monolog.api.BasicLevel;
import org.objectweb.util.monolog.api.Logger;
import org.objectweb.jonas.common.Log;
import javax.ejb.FinderException;

/**
 * Servlet interfacing the UI layer and business logic.
 *
 *
 * Created: Wed Sep 28 06:23:05 2005
 *
 * @author <a href="mailto:Mihai Giurgeanu@CRIMIRA"></a>
 * @version 1.0
 */
public class UsaMetalica1KServlet extends HttpServlet {

    private Logger logger = null;

    /**
     * Describe <code>init</code> method here.
     *
     * @exception ServletException if an error occurs
     */
    public final void init() throws ServletException {
	logger = Log.getLogger("ro.kds.erp.web.UsaMetalica1KServlet");
        logger.log(BasicLevel.DEBUG, "");
    }

    /**
     * Sends UI commands to the business logic and returns the results.
     *
     * @param httpServletRequest a <code>HttpServletRequest</code> value
     * @param httpServletResponse a <code>HttpServletResponse</code> value
     * @exception ServletException if an error occurs
     * @exception IOException if an error occurs
     */
    public final void doPost(final HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse) throws ServletException, IOException {
	logger.log(BasicLevel.DEBUG, "doPost entered");

	String command = httpServletRequest.getParameter("command");
	ResponseBean r;

	if(command == null) {
	    r = new ResponseBean();
	    r.setCode(1);
	    r.setMessage("'command' parameter was not received");
	} else {
	
	    httpServletResponse.setContentType("text/xml");
	    PrintWriter out = httpServletResponse.getWriter();
	    
	    if(command.compareTo("change") == 0) {
		UsaMetalica1K b = (UsaMetalica1K)httpServletRequest.getSession().getAttribute("session");
		String field = httpServletRequest.getParameter("field");
		String value = httpServletRequest.getParameter("value");
		logger.log(BasicLevel.DEBUG, "change operation selected for field " + field + " with value " + value);
		
		try {
		    if(field.compareTo("subclass") == 0) {
			r = b.updateSubclass(value);
		    } else if(field.compareTo("version") == 0) {
			r = b.updateVersion(value);
		    } else if(field.compareTo("material") == 0) {
			r = b.updateMaterial(new Integer(Integer.parseInt(value)));
		    } else if(field.compareTo("lg") == 0) {
			r = b.updateLg(new Double(Double.parseDouble(value)));
		    } else if(field.compareTo("hg") == 0) {
			r = b.updateHg(new Double(Double.parseDouble(value)));
		    } else if(field.compareTo("lcorrection") == 0) {
			r = b.updateLcorrection(new Double(Double.parseDouble(value)));
		    } else if(field.compareTo("hcorrection") == 0) {
			r = b.updateHcorrection(new Double(Double.parseDouble(value)));
		    } else if(field.compareTo("intFoil") == 0) {
			r = b.updateIntFoil(new Integer(Integer.parseInt(value)));
		    } else if(field.compareTo("ieFoil") == 0) {
			r = b.updateIeFoil(new Integer(Integer.parseInt(value)));
		    } else if(field.compareTo("extFoil") == 0) {
			r = b.updateExtFoil(new Integer(Integer.parseInt(value)));
		    } else if(field.compareTo("isolation") == 0) {
			r = b.updateIsolation(new Integer(Integer.parseInt(value)));
		    } else if(field.compareTo("openingDir") == 0) {
			r = b.updateOpeningDir(new Integer(Integer.parseInt(value)));
		    } else if(field.compareTo("openingSide") == 0) {
			r = b.updateOpeningSide(new Integer(Integer.parseInt(value)));
		    } else if(field.compareTo("frameType") == 0) {
			r = b.updateFrameType(new Integer(Integer.parseInt(value)));
		    } else if(field.compareTo("lFrame") == 0) {
			r = b.updateLFrame(new Double(Double.parseDouble(value)));
		    } else if(field.compareTo("bFrame") == 0) {
			r = b.updateBFrame(new Double(Double.parseDouble(value)));
		    } else if(field.compareTo("cFrame") == 0) {
			r = b.updateCFrame(new Double(Double.parseDouble(value)));
		    } else if(field.compareTo("foilPosition") == 0) {
			r = b.updateFoilPosition(new Integer(Integer.parseInt(value)));
		    } else if(field.compareTo("tresholdType") == 0) {
			r = b.updateTresholdType(new Integer(Integer.parseInt(value)));
		    } else if(field.compareTo("lTreshold") == 0) {
			r = b.updateLTreshold(new Double(Double.parseDouble(value)));
		    } else if(field.compareTo("hTreshold") == 0) {
			r = b.updateHTreshold(new Double(Double.parseDouble(value)));
		    } else if(field.compareTo("cTreshold") == 0) {
			r = b.updateCTreshold(new Double(Double.parseDouble(value)));
		    } else if(field.compareTo("tresholdSpace") == 0) {
			r = b.updateTresholdSpace(new Integer(Integer.parseInt(value)));
		    } else if(field.compareTo("h1Treshold") == 0) {
			r = b.updateH1Treshold(new Double(Double.parseDouble(value)));
		    } else if(field.compareTo("h2Treshold") == 0) {
			r = b.updateH2Treshold(new Double(Double.parseDouble(value)));
		    } else {
			r = new ResponseBean();
			r.setMessage("Unknown field");
			r.setCode(1);
		    }
		} catch (RemoteException e) {
		    r = new ResponseBean();
		    r.setCode(1);
		    r.setMessage("Error while communicating with business logic server: " + e);
		    e.printStackTrace();
		}
	    } else if(command.compareTo("load") == 0) {
		int id = Integer.parseInt(httpServletRequest
					  .getParameter("id"));
		logger.log(BasicLevel.DEBUG, "load operation selected for id " + id);

		try {
		    InitialContext it = new InitialContext();
		    Context env = (Context)it.lookup("java:comp/env");

		    UsaMetalica1KHome bh = (UsaMetalica1KHome)PortableRemoteObject.narrow(env.lookup("ejb/UsaMetalica1K"),UsaMetalica1KHome.class);

		    // creates and registers a new session bean
		    UsaMetalica1K b = bh.create();
		    httpServletRequest.getSession().setAttribute("session", b);
		    
		    r = b.loadFormData(new Integer(id));
		} catch (NamingException e) {
		    r = new ResponseBean();
		    r.setCode(1);
		    r.setMessage("Configuration error: " + e);
		    e.printStackTrace();
		} catch (RemoteException e) {
		    r = new ResponseBean();
		    r.setCode(1);
		    r.setMessage("Error while communicating with business logic server: " + e);
		    e.printStackTrace();
		} catch (CreateException e) {
		    r = new ResponseBean();
		    r.setCode(1);
		    r.setMessage("Error creating session bean: " + e);
		    e.printStackTrace();
		} catch (FinderException e) {
		    r = new ResponseBean();
		    r.setCode(1);
		    r.setMessage("Eroare: Produsul cu id-ul " + id + " nu exista");
		    logger.log(BasicLevel.WARN, e);
		}


	    } else if(command.compareTo("save") == 0) {
		logger.log(BasicLevel.DEBUG, "save operation selected");

		UsaMetalica1K b = (UsaMetalica1K)httpServletRequest.getSession().getAttribute("session");
		try {
		    r = b.saveFormData();
		} catch (RemoteException e) {
		    r = new ResponseBean();
		    r.setCode(1);
		    r.setMessage("Error while communicating with business logic server: " + e);
		    e.printStackTrace();
		}
	    } else if(command.compareTo("new") == 0) {
		try {
		    InitialContext it = new InitialContext();
		    Context env = (Context)it.lookup("java:comp/env");

		    UsaMetalica1KHome bh = (UsaMetalica1KHome)PortableRemoteObject.narrow(env.lookup("ejb/UsaMetalica1K"),UsaMetalica1KHome.class);

		    // creates and registers a new session bean
		    UsaMetalica1K b = bh.create();
		    httpServletRequest.getSession().setAttribute("session", b);
		    
		    r = b.newFormData();
		} catch (NamingException e) {
		    r = new ResponseBean();
		    r.setCode(1);
		    r.setMessage("Configuration error: " + e);
		    e.printStackTrace();
		} catch (RemoteException e) {
		    r = new ResponseBean();
		    r.setCode(1);
		    r.setMessage("Error while communicating with business logic server: " + e);
		    e.printStackTrace();
		} catch (CreateException e) {
		    r = new ResponseBean();
		    r.setCode(1);
		    r.setMessage("Error creating session bean: " + e);
		    e.printStackTrace();
		}
	    } else {
		r = new ResponseBean();
		r.setCode(1);
		r.setMessage("'command' parameter has an unrecognized value: " + command);
	    }

	    
	}

	httpServletResponse.setContentType("text/xml");
	PrintWriter out = httpServletResponse.getWriter();
	String xml = r.toXML();
	logger.log(BasicLevel.DEBUG, "Send xml response:\n" + xml);
	out.print(xml);
    }

    /**
     * Calls <code>doPost</code>. For debug purposes.
     *
     * @param httpServletRequest a <code>HttpServletRequest</code> value
     * @param httpServletResponse a <code>HttpServletResponse</code> value
     * @exception ServletException if an error occurs
     * @exception IOException if an error occurs
     */
    public final void doGet(final HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse) throws ServletException, IOException {
	doPost(httpServletRequest, httpServletResponse);
    }

}
