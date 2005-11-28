package ro.kds.erp.web;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import javax.servlet.ServletException;
import ro.kds.erp.biz.setum.basic.StandardOffer;
import org.objectweb.util.monolog.api.BasicLevel;
import ro.kds.erp.biz.ResponseBean;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.rmi.RemoteException;
import ro.kds.erp.biz.setum.basic.StandardOfferHome;
import javax.rmi.PortableRemoteObject;
import javax.naming.NamingException;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.naming.InitialContext;
import javax.naming.Context;
import org.objectweb.util.monolog.api.Logger;
import org.objectweb.jonas.common.Log;
import java.text.ParseException;
import javax.ejb.RemoveException;
import java.text.NumberFormat;
import java.math.BigDecimal;
import javax.servlet.http.HttpSession;

/**
 * Implements the intermediate layer between the UI and the business
 * logic bean. Manages the session of editing StandardOffer form data
 * maintaing a session bean and translates the load, save and field
 * update commands into calls to the session bean.
 *
 *
 * Created: Tue Oct 25 10:57:07 2005
 *
 * @author <a href="mailto:Mihai Giurgeanu@CRIMIRA"></a>
 * @version 1.0
 */
public class StandardOfferServlet extends HttpServlet {


    private Logger logger = null;

    /**
     * The HttpRequest session attribute containing the session bean.
     */
    public final static String SESSION_ATTR = "ro.kds.erp.web.StandardOfferServlet.session";

    /**
     * Servlet initialization.
     *
     * @exception ServletException if an error occurs
     */
    public final void init() throws ServletException {
	logger = Log.getLogger("ro.kds.erp.web.StandardOfferServlet");
        logger.log(BasicLevel.DEBUG, "");
    }

    /**
     * Sends UI commands to the business logic and returns the results.
     * The result is returned as an XML representation of the ResponseBean.
     *
     * The commands are sent by the UI as values of the command parameter.
     * Depending of the value of this parameter, it is possible tha other
     * parameters would be requested.
     *
     * The commands known to this servlet are:
     *
     * <dl>
     * <dt>change
     * <dd>To update the value of a field. It requires the parameters
     * field and value.
     * <dt>load
     * <dd>Loads a new object from the database. It requires one parameter, id,
     * with the value of objects primary key.
     * <dt>save
     * <dd>Saves the current form values into the persistent layer.
     * </dl>
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
	
	    if(command.equals("change")) {
		
		// retrieve the session bean from the session
		StandardOffer offerManager = (StandardOffer)httpServletRequest.getSession().getAttribute(SESSION_ATTR);
		if(offerManager == null) {
		    r = new ResponseBean();
		    r.setCode(4);
		    r.setMessage("Nu este selectat nici un obiect.");
		    logger.log(BasicLevel.INFO, "change operation was selected, but there is no current session bean");
		} else {
		    String field = httpServletRequest.getParameter("field");
		    String value = httpServletRequest.getParameter("value");
		    logger.log(BasicLevel.DEBUG, "change operation selected for field " + field + " with value " + value);
		    DateFormat df = DateFormat.getDateInstance();
		    df.setLenient(true);
		    NumberFormat nf = NumberFormat.getInstance();
		    try {
			if(field.equals("no")) {
			    r = offerManager.updateNo(value);
			} else if(field.equals("docDate")) {
			    r = offerManager.updateDocDate(df.parse(value));
			} else if(field.equals("dateFrom")) {
			    r = offerManager.updateDateFrom(df.parse(value));
			} else if(field.equals("dateTo")) {
			    r = offerManager.updateDateTo(df.parse(value));
			} else if(field.equals("period")) {
			    r = offerManager.updatePeriod(new Integer(value));
			} else if(field.equals("name")) {
			    r = offerManager.updateName(value);
			} else if(field.equals("description")) {
			    r = offerManager.updateDescription(value);
			} else if(field.equals("comment")) {
			    r = offerManager.updateComment(value);
			} else if(field.equals("productId")) {
			    r = offerManager.updateProductId(new Integer(value));
			} else if(field.equals("price")) {
			    r = offerManager.updatePrice(new BigDecimal(nf.parse(value).doubleValue()));
			} else if(field.equals("relativeGain")) {
			    r = offerManager.updateRelativeGain(new Double(nf.parse(value).doubleValue()));
			} else if(field.equals("absoluteGain")) {
			    r = offerManager.updateAbsoluteGain(new BigDecimal(nf.parse(value).doubleValue()));
			} else  if(field.equals("productCategory") ||
				   field.equals("productCode") ||
				   field.equals("productName") || 
				   field.equals("entryPrice") ||
				   field.equals("sellPrice")) {
			    // readonly fields; just ignore them
			    logger.log(BasicLevel.DEBUG, "Ignoring change request for read only field " + field);
			    r = new ResponseBean();
			} else {
			    r = new ResponseBean();
			    r.setCode(1);
			    r.setMessage("Camp necunoscut! Modificarea nu a fost salvata.");
			    logger.log(BasicLevel.WARN, "Field " + field +
				       " unknown. Its value is <<" + value
				       + ">>.");
			}
		    } catch (RemoteException e) {
			r = new ResponseBean();
			r.setCode(2);
			r.setMessage("Eroare in comunicarea cu server-ul de business logic. Modificarea nu va fi luata in considerare.");
			logger.log(BasicLevel.ERROR, "RemoteException while updating the value of field " + field + " with value <<" + value + ">>.");
		    } catch (NumberFormatException e) {
			r =  new ResponseBean();
			r.setCode(4);
			r.setMessage("Nu pot interpreta valoarea <<" + value
				     + ">> ca un numar. Trebuie sa scrieti un numar in acest camp.");
			logger.log(BasicLevel.INFO, "NumberFormatException in field " + field + " value <<" + value + ">>.");
			logger.log(BasicLevel.DEBUG, e);
		    } catch (ParseException e) {
			r = new ResponseBean();
			r.setCode(4);
			r.setMessage("Valoarea <<" + value +">> nu este potrivita pentru acest camp");
			logger.log(BasicLevel.INFO, "ParseError in field " + field + " value <<" + value + ">>.");
			logger.log(BasicLevel.DEBUG, e);
		    }
		    
		}
		
	    } else if(command.equals("load")) {
		Integer id = null;
		try {
		    id = new Integer(Integer.parseInt(httpServletRequest
						      .getParameter("id")));
		    logger.log(BasicLevel.DEBUG, "load operation selected for id " + id);

		    r = getTheSessionBean(httpServletRequest.getSession()).loadFormData(id);

		} catch (NumberFormatException e) {
		    r =  new ResponseBean();
		    r.setCode(5);
		    r.setMessage("Eroare: valoarea id nepermisa: <<" +
				 httpServletRequest.getParameter("id")
				 + ">>.");
		    logger.log(BasicLevel.ERROR, "NumberFormatException for id value <<" + httpServletRequest.getParameter("id") + ">>.");
		    logger.log(BasicLevel.DEBUG, e);
		} catch (NamingException e) {
		    r = new ResponseBean();
		    r.setCode(1);
		    r.setMessage("Eroare de configurare a server-ului. Datele nu pot fi incarcate!");
		    logger.log(BasicLevel.ERROR, "NamingException in load operation. Is ejb/StandardOfferHome defined?", e);
		} catch (CreateException e) {
		    r = new ResponseBean();
		    r.setCode(2);
		    r.setMessage("Nu pot porni sesiunea de editare pentru acest form");
		    logger.log(BasicLevel.ERROR, "CreateException trying to instantiate a StandardOffer bean for id " + id, e);
		    try {
			((StandardOffer)httpServletRequest.getSession().getAttribute(SESSION_ATTR)).remove();
		    } catch (Exception ignore) {}
		    httpServletRequest.getSession().removeAttribute(SESSION_ATTR);
		} catch (RemoteException e) {
		    r = new ResponseBean();
		    r.setCode(2);
		    r.setMessage("Eroare la comunicarea cu server-ul de business logic");
		    logger.log(BasicLevel.ERROR, "RemoteException trying to instantiate a StandardOffer bean for id " + id, e);
		    try {
			((StandardOffer)httpServletRequest.getSession().getAttribute(SESSION_ATTR)).remove();
		    } catch (Exception ignore) {}
		    httpServletRequest.getSession().removeAttribute(SESSION_ATTR);
		} catch (FinderException e) {
		    r = new ResponseBean();
		    r.setCode(3);
		    r.setMessage("Nu am gasit obiectul in baza de date");
		    logger.log(BasicLevel.WARN, "FinderException for offer id " + id);
		}
		
	    } else if(command.equals("save")) {

		// retrieve the session bean from the session
		StandardOffer offerManager = (StandardOffer)httpServletRequest.getSession().getAttribute(SESSION_ATTR);
		if(offerManager == null) {
		    r = new ResponseBean();
		    r.setCode(4);
		    r.setMessage("Nu este selectat nici un obiect.");
		    logger.log(BasicLevel.INFO, "change operation was selected, but there is no current session bean");
		} else {
		    try {
			r = offerManager.saveFormData();
		    } catch (RemoteException e) {
			r = new ResponseBean();
			r.setCode(2);
			r.setMessage("Eroare in comunicarea cu server-ul de business logic");
			logger.log(BasicLevel.ERROR, "RemoteException for save operation", e);
		    }
		}
	    } else if(command.equals("new")) {
		try {
		    InitialContext it = new InitialContext();
		    Context env = (Context)it.lookup("java:comp/env");

		    
		    r = getTheSessionBean(httpServletRequest.getSession()).newFormData();
		} catch (NamingException e) {
		    r = new ResponseBean();
		    r.setCode(1);
		    r.setMessage("Eroare de configurare a server-ului. Datele nu pot fi incarcate!");
		    logger.log(BasicLevel.ERROR, "NamingException in new operation. Is ejb/StandardOfferHome defined?", e);
		} catch (RemoteException e) {
		    r = new ResponseBean();
		    r.setCode(2);
		    r.setMessage("Eroare la comunicarea cu server-ul de business logic");
		    logger.log(BasicLevel.ERROR, "RemoteException trying to instantiate a StandardOffer bean and create a new offer object.", e);
		} catch (CreateException e) {
		    r = new ResponseBean();
		    r.setCode(2);
		    r.setMessage("Nu pot porni sesiunea de editare pentru acest form");
		    logger.log(BasicLevel.ERROR, "CreateException trying to instantiate a StandardOffer bean", e);
		}
	    } else if (command.equals("listing")) {
		// send the listing of offers
		
		try {
		    r = getTheSessionBean(httpServletRequest.getSession()).offersListing();
		} catch (Exception e) {
		    r = new ResponseBean();
		    r.setCode(1);
		    r.setMessage("Eroare la crearea sesiunii. Lista de oferte nu poate fi incarcata.");
		    logger.log(BasicLevel.ERROR, e.getMessage(), e);
		    try {
			((StandardOffer)httpServletRequest.getSession().getAttribute(SESSION_ATTR)).remove();
		    } catch (Exception ignore) {}
		    httpServletRequest.getSession().removeAttribute(SESSION_ATTR);
		}
	    } else if (command.equals("lineItemsListing")) {

		try {
		    StandardOffer o = (StandardOffer)
			httpServletRequest.getSession()
			.getAttribute(SESSION_ATTR);
		    if(o == null) {
			r = new ResponseBean();
			r.setCode(4);
			r.setMessage("Nu ati ales nici o oferta!");
		    } else {
			r = o.lineItemsListing();
		    }

		} catch (Exception e) {
		    r = new ResponseBean();
		    r.setCode(1);
		    r.setMessage("Eroare la crearea sesiunii. Linille ofertei nu pot fi incarcate.");
		    logger.log(BasicLevel.ERROR, e.getMessage(), e);
		}
	    } else if (command.equals("loadSubForm")) {

		try {
		    StandardOffer o = (StandardOffer)
			httpServletRequest.getSession()
			.getAttribute(SESSION_ATTR);
		    if(o == null) {
			r = new ResponseBean();
			r.setCode(4);
			r.setMessage("Nu ati ales nici o oferta!");
		    } else {
			Integer subFormId =
			    new Integer(httpServletRequest
					.getParameter("id"));
			r = o.loadSubForm(subFormId);
		    }
		} catch (Exception e) {
		    r = new ResponseBean();
		    r.setCode(1);
		    r.setMessage("Datele nu pot fi incarcate din cauza unei erori interne.");
		    logger.log(BasicLevel.ERROR, 
			       "Error in loadSubForm command. id = "
			       + httpServletRequest.getParameter("id"), e);
		}
	    } else if(command.equals("addNewItem")) {
		try {
		    StandardOffer o = (StandardOffer)
			httpServletRequest.getSession()
			.getAttribute(SESSION_ATTR);
		    if(o == null) {
			r = new ResponseBean();
			r.setCode(4);
			r.setMessage("Nu ati ales nici o oferta!");
		    } else {
			r = o.addNewItem();
		    }
		} catch (Exception e) {
		    r = new ResponseBean();
		    r.setCode(2);
		    r.setMessage("Eroare la adaugarea unei noi linii!");
		    logger.log(BasicLevel.ERROR, 
			       "Error in addNewItem command", e);
		}
	    } else if(command.equals("removeItem")) {
		try {
		    StandardOffer o = (StandardOffer)
			httpServletRequest.getSession()
			.getAttribute(SESSION_ATTR);
		    if(o == null) {
			r = new ResponseBean();
			r.setCode(4);
			r.setMessage("Nu ati ales nici o oferta!");
		    } else {
			r = o.removeItem();
		    }
		} catch (Exception e) {
		    r = new ResponseBean();
		    r.setCode(2);
		    r.setMessage("Eroare la stergerea liniei!");
		    logger.log(BasicLevel.ERROR, 
			       "Error in removeItem command", e);
		}		
	    } else if(command.equals("saveSubForm")) {
		try {
		    StandardOffer o = (StandardOffer)
			httpServletRequest.getSession()
			.getAttribute(SESSION_ATTR);
		    if(o == null) {
			r = new ResponseBean();
			r.setCode(4);
			r.setMessage("Nu ati ales nici o oferta!");
		    } else {
			r = o.saveSubForm();
		    }
		} catch (Exception e) {
		    r = new ResponseBean();
		    r.setCode(2);
		    r.setMessage("Eroare la stergerea liniei!");
		    logger.log(BasicLevel.ERROR, 
			       "Error in removeItem command", e);
		}		
	    } else {
		r = new ResponseBean();
		r.setCode(5);
		r.setMessage("Eroare: comanda primita de server este necunoscuta!");
		logger.log(BasicLevel.ERROR, "Unknown command " + command);
	    }
	}

	httpServletResponse.setContentType("text/xml");
	PrintWriter out = httpServletResponse.getWriter();
	String xml = r.toXML();
	logger.log(BasicLevel.DEBUG, "Sending the response: \n" + xml);
	out.print(xml);
    }

    /**
     * Retrieves the current session bean or creates another session bean.
     * The session bean is responsible for the business logic. It is 
     * registered within the HttpSession object.
     */
    protected StandardOffer getTheSessionBean(HttpSession session) 
	throws NamingException, CreateException, RemoteException {

	StandardOffer offerManager = (StandardOffer)session
	    .getAttribute(SESSION_ATTR);

	if(offerManager==null) {
	    InitialContext ic = new InitialContext();
	    Context env = (Context)ic.lookup("java:comp/env");
	    
	    StandardOfferHome offerHome = (StandardOfferHome)
		PortableRemoteObject.narrow
		(env.lookup("ejb/StandardOfferHome"), 
		 StandardOfferHome.class);
	    offerManager = offerHome.create();
	    session.setAttribute(SESSION_ATTR, offerManager);
	}
	return offerManager;
    }
}
