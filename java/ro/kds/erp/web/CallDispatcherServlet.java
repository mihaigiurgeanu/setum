package ro.kds.erp.web;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.objectweb.util.monolog.api.BasicLevel;
import java.util.HashMap;
import javax.ejb.EJBObject;
import ro.kds.erp.biz.ResponseBean;
import org.objectweb.util.monolog.api.Logger;
import java.lang.reflect.Method;
import org.objectweb.jonas.common.Log;
import java.util.Map;
import org.apache.commons.beanutils.ConvertUtils;
import javax.naming.NamingException;
import javax.ejb.CreateException;
import java.rmi.RemoteException;
import javax.ejb.EJBHome;
import javax.rmi.PortableRemoteObject;
import javax.naming.InitialContext;
import javax.naming.Context;
import java.lang.reflect.InvocationTargetException;
import java.io.PrintWriter;
import java.util.Map.Entry;
import java.util.Iterator;
import org.apache.commons.beanutils.locale.converters.DateLocaleConverter;

/**
 * Gets the calls from the UI interface and dispatch them to the
 * business logic layer. The business layer is representing by a session
 * bean stored in the current HTTP session of the request. The session bean
 * is localized through the jndi. It's jndi name is retrieved from the
 * servlet's init parameter named "sessionBean".
 *
 * The dispatching is made uppon the <code>command</code>
 * http parameter:
 *
 * <dl>
 * <dt>change
 * <dd>The <code>field</code> http parameter of the request contains the
 * name of the field that would be updated and the <code>value</code>
 * parameter contains the <code>String</code> representation of the new value
 * to be put on that field. The servlet will look in the current
 * session bean for a method called 
 * <code>updateField</code>, and invokes the method by converting the
 * <code>value</code> parameter to the type of the method's parameter.
 *
 * <dt>load
 * <dd>The <code>id</code> parameter will contain the value of the primary
 * key that should be loaded. The servlet gets the current session bean or
 * creates a new one and invokes the <code>loadFormData(Integer id)</code>
 * on it.
 *
 * <dt>save
 * <dd>The current session bean's method <code>saveFormData()</code> is
 * invoked.
 *
 * <dt>new
 * <dd>The current session bean is retrieved from the HTTP session and if it 
 * does not exists a new one is created. The <code>newFormData()</code>
 * method is invoked on this bean.
 *
 * <dt>listing
 * <dd>The current session bean's <code>loadListing()</code> is invoked.
 * 
 * <dt>other commands
 * <dd>The current session bean is retrieved. It is searched for a method
 * called as the <code>command</code> parameter. For each of its parameters
 * the values are looked in the current request and converted to the 
 * parameter types found in the method. The method is then invoked with these
 * parameters.
 *
 * </dl>
 *
 * Created: Sun Oct 30 16:35:02 2005
 *
 * @author <a href="mailto:Mihai Giurgeanu@CRIMIRA"></a>
 * @version 1.0
 */
public class CallDispatcherServlet extends HttpServlet {


    private Logger logger = null;

    /**
     * The HttpRequest session attribute containing the session bean. It is
     * read from servlet's init parameter with the name "sessionName".
     */
    private String SESSION_ATTR;

    /**
     * The JNDI location of the session bean's home. It is read from the
     * servlet's init parameter "sessionBean".
     */
    private String SESSION_JNDI;

    /**
     * The map of the methods in the session bean's class.
     */
    private Map methods;

    /**
     * The home class for instantiating the session bean.
     */
    private Class homeInterface;

    /**
     * Servlet initialization.
     *
     * @exception ServletException if an error occurs
     */
    public final void init() throws ServletException {
	logger = Log.getLogger("ro.kds.erp.web.CallDispatcherServlet");
        logger.log(BasicLevel.DEBUG, "");

	try {
	    SESSION_ATTR = getServletConfig().getInitParameter("sessionName");
	    SESSION_JNDI = getServletConfig().getInitParameter("sessionBean");

	    Class beanClass = Class.forName(getServletConfig()
					    .getInitParameter("sessionClass"));
	    methods = new HashMap();
	    
	    Method[] businessMethods = beanClass.getMethods();
	    for(int i=0; i<businessMethods.length; i++) {
		methods.put(businessMethods[i].getName(), 
			    businessMethods[i]);
	    }

	    homeInterface = Class.forName(getServletConfig().
					  getInitParameter("sessionHome"));

	    // Converter initialization
	    ConvertUtils.register(new ro.kds.erp.utils.DateConverter(),
				  java.util.Date.class);
	    
	} catch (Exception e) {
	    logger.log(BasicLevel.ERROR, "Servlet can not be initialized", e);
	    throw new ServletException(e);
	}
    }



    /**
     * GET HTTP method redirects to POST processing method.
     *
     * @param request a <code>HttpServletRequest</code> value
     * @param response a <code>HttpServletResponse</code> value
     * @exception ServletException if an error occurs
     * @exception IOException if an error occurs
     */
    public void doGet(HttpServletRequest request, 
		      HttpServletResponse response) 
	throws ServletException, IOException {
	
	doPost(request, response);
    }



    /**
     * Servlet's entry point.
     *
     * @param request a <code>HttpServletRequest</code> value
     * @param response a <code>HttpServletResponse</code> value
     * @exception ServletException if an error occurs
     * @exception IOException if an error occurs
     */
    public void doPost(HttpServletRequest request, 
		       HttpServletResponse response) 
	throws ServletException, IOException {

	logger.log(BasicLevel.DEBUG, "Component's path: " + 
		   request.getRequestURI());
	logger.log(BasicLevel.DEBUG, "Query string: " + 
		   request.getQueryString());
	
	EJBObject bean = (EJBObject)request.getSession()
	    .getAttribute(SESSION_ATTR);
	String command = request.getParameter("command");

	logger.log(BasicLevel.DEBUG, "Command: " + command);
	{
	    String params = "";
	    for(Iterator i = request.getParameterMap().entrySet().iterator(); 
		i.hasNext();) {
		
		Map.Entry entry = (Map.Entry)i.next();
		params += "<<" + entry.getKey() + ">> = ";
		Object[] value = (Object []) entry.getValue();
		for(int j=0; j<value.length; j++)
		    params += "<<" + value[j] + ">> ";
		params += "\n";
	    }

	    logger.log(BasicLevel.DEBUG, "Parameters: \n" + params);
	}
	ResponseBean r;

	if(command.equals("change")) {
	    String field = request.getParameter("field");
	    String value = request.getParameter("value");
	    logger.log(BasicLevel.DEBUG, "Operation change for field <<" +
		       field + ">> with value <<" + value + ">>");
	    if(bean == null) {
		r = new ResponseBean();
		r.setCode(4);
		r.setMessage("Trebuie sa selectati mai intai o inregistrare. Modificarea nu a fost salvata!");
		logger.log(BasicLevel.INFO, "Change operation called but no itemwas selected");
	    } else {
		try {
		    String methodName = "update" + 
			new Character(Character.toUpperCase(field.charAt(0)))
			.toString() + field.substring(1);
		    logger.log(BasicLevel.DEBUG, "Searching for method name "
			       + methodName);
		    
		    Method m = (Method)methods.get(methodName);
		    if(m == null) {
			r = new ResponseBean(); // don't do anything
			// just log an warning
			logger.log(BasicLevel.WARN, "Method " + methodName
				   + " not found when trying to update field "
				   + field);
		    } else {
			Class[] paramsTypes = m.getParameterTypes();
			logger.log(BasicLevel.DEBUG,
				   "Method "  + methodName + " has " +
				   paramsTypes.length + " parameters");

			logger.log(BasicLevel.DEBUG, "Converting " + value +
				   " to type " + paramsTypes[0].getName());

			Object params[] = new Object[1];
			params[0] = ConvertUtils.convert(value, 
							 paramsTypes[0]);

			logger.log(BasicLevel.DEBUG,
				   "Invoking " + methodName + 
				   " with one parameter: " + value
				   + "(" + params[0] + " - "
				   + params[0].getClass().getName()
				   + ")");
			r = (ResponseBean)m.invoke(bean, params);
		    }
		} catch (Exception e) {
		    r = new ResponseBean();
		    r.setCode(1);
		    r.setMessage("Valoarea <<" + value +
				 ">> nu a putut fi salvata.");
		    logger.log(BasicLevel.ERROR, "Value <<" + value + 
			       ">> can not be saved for field: " + field, e);
		}
	    }
	    
	} else if (command.equals("load")) {
	    String idParam = request.getParameter("id");
	    logger.log(BasicLevel.DEBUG, "Operation load for id " + idParam);
	    try {
		Object params[] = new Object[1];
		params[0] = new Integer(Integer.parseInt(idParam));
		Method loadFormData = (Method)methods.get("loadFormData");

		if(bean == null) {
		    bean = newSessionBean(request);
		}
		r = (ResponseBean)loadFormData.invoke(bean, params);
	    } catch (RemoteException e) {
		removeSessionBean(request);
		r = new ResponseBean();
		r.setCode(2);
		r.setMessage("Eroare la comunicarea cu server-ul de business logic");
		logger.log(BasicLevel.ERROR,
			   "Error executing load operation", e);
	    } catch (Exception e) {
		r = new ResponseBean();
		r.setCode(1);
		r.setMessage("Eroare la incarcarea formului");
		logger.log(BasicLevel.ERROR, "Can not load form for id " +
			   idParam, e);
	    }
	} else if (command.equals("new")) {
	    logger.log(BasicLevel.DEBUG, "Executing operation new");
	    try {
		if(bean == null) {
		    bean = newSessionBean(request);
		}
		Method newFormData = (Method)methods.get("newFormData");
		r = (ResponseBean)newFormData.invoke(bean, null);
	    } catch (RemoteException e) {
		removeSessionBean(request);
		r = new ResponseBean();
		r.setCode(2);
		r.setMessage("Eroare la comunicarea cu server-ul de business logic");
		logger.log(BasicLevel.ERROR,
			   "Error executing new operation", e);
	    } catch (Exception e) {
		r = new ResponseBean();
		r.setCode(1);
		r.setMessage("Eroare la crearea unui nou obiect");
		logger.log(BasicLevel.ERROR, "Error executing new", e);
	    }
	} else if (command.equals("listing")) {
	    try {
		if(bean == null) {
		    bean = newSessionBean(request);
		}
		Method loadListing = (Method)methods.get("loadListing");


		Class paramTypes[] = loadListing.getParameterTypes();
		logger.log(BasicLevel.DEBUG, "Method loadListing" + 
			   " requires " + paramTypes.length + " parameters.");
		Object params[] = new Object[paramTypes.length];
		for(int i=0; i<paramTypes.length; i++) {
		    // i expect the parameters with the names
		    // param1, param2, ... .
		    String param = request.getParameter("param" + i);
		    if(param == null) {
			params[i] = null;
			logger.log(BasicLevel.WARN, "param" + i + " does not exist. Setting it to null when calling method " + command);
		    } else {
			params[i] = ConvertUtils.convert(param, paramTypes[i]);
			logger.log(BasicLevel.DEBUG, "param" + i + 
				   " has value <<" + param +
				   ">>  when calling method " + command);
		    }
		}


		r = (ResponseBean)loadListing.invoke(bean, params);

	    } catch (RemoteException e) {
		removeSessionBean(request);
		r = new ResponseBean();
		r.setCode(2);
		r.setMessage("Nu pot incarca lista");
		removeSessionBean(request);
		logger.log(BasicLevel.ERROR,
			   "Error executing listing operation", e);
	    } catch (Exception e) {
		r = new ResponseBean();
		r.setCode(1);
		r.setMessage("Nu pot incarca lista");
		removeSessionBean(request);
		logger.log(BasicLevel.ERROR,
			   "Error executing listing operation", e);
	    }
	} else if (command.equals("save")) {
	    if(bean == null) {
		r = new ResponseBean();
		r.setCode(4);
		r.setMessage("Trebuie sa selectati mai intai o inregistrare!");
		logger.log(BasicLevel.INFO, "Save operation called but no itemwas selected");
	    } else {
		try {
		    Method saveFormData = (Method)methods.get("saveFormData");
		    r = (ResponseBean)saveFormData.invoke(bean, null);
		} catch (Exception e) {
		    r = new ResponseBean();
		    r.setCode(1);
		    r.setMessage("Eroare la salvare!");
		    logger.log(BasicLevel.ERROR, "Error executing save", e);
		}
	    }	    
	} else {
	    // other commands
	    Method m = (Method)methods.get(command);
	    if(m == null) {
		r = new ResponseBean();
		logger.log(BasicLevel.WARN, "Command " + command + 
			   " unknown.");
	    } else {
		logger.log(BasicLevel.DEBUG, "Method " + command + " found.");
		Class paramTypes[] = m.getParameterTypes();
		logger.log(BasicLevel.DEBUG, "Method " + command + 
			   " requires " + paramTypes.length + " parameters.");
		Object params[] = new Object[paramTypes.length];
		for(int i=0; i<paramTypes.length; i++) {
		    // i expect the parameters with the names
		    // param1, param2, ... .
		    String param = request.getParameter("param" + i);
		    if(param == null) {
			params[i] = null;
			logger.log(BasicLevel.WARN, "param" + i + " does not exist. Setting it to null when calling method " + command);
		    } else {
			params[i] = ConvertUtils.convert(param, paramTypes[i]);
			logger.log(BasicLevel.DEBUG, "param" + i + 
				   " has value <<" + param +
				   ">>  when calling method " + command);
		    }
		}
		try {
		    if(bean == null) {
			bean = newSessionBean(request);
		    }
		    r = (ResponseBean)m.invoke(bean, params);
		} catch (Exception e) {
		    r = new ResponseBean();
		    r.setCode(1);
		    r.setMessage("Eroare, operatia nu a putut fi efectuata");
		    logger.log(BasicLevel.ERROR, "Error executing command " 
			       + command, e);
		}
	    }
	}

	response.setContentType("text/xml");
	PrintWriter out = response.getWriter();
	String xml = r.toXML();
	logger.log(BasicLevel.DEBUG, "Sending the response: \n" + xml);
	out.print(xml);
    }


    /**
     * Creates a new session bean and registers it into the current session.
     *
     * @param request is the current HttpServletRequest.
     */
    EJBObject newSessionBean(HttpServletRequest request) 
	throws NamingException, CreateException, RemoteException,
	       NoSuchMethodException, IllegalAccessException,
	       InvocationTargetException {

	InitialContext ic = new InitialContext();
	Context env = (Context)ic.lookup("java:comp/env");

	EJBHome beanHome = (EJBHome)PortableRemoteObject.narrow
	    (env.lookup(SESSION_JNDI), homeInterface);
	EJBObject bean = (EJBObject)homeInterface.getMethod("create", null)
	    .invoke(beanHome, null);
	request.getSession().setAttribute(SESSION_ATTR, bean);
	
	return bean;
    }

    /**
     * Removes the session bean object.
     *
     * @param request is the current HttpServletRequest.
     */
    private void removeSessionBean(HttpServletRequest request) {
	try {
	    EJBObject bean = (EJBObject)request.getSession()
		.getAttribute(SESSION_ATTR);
	    if(bean != null) {
		bean.remove();
	    }
	} catch(Exception e) {
	    logger.log(BasicLevel.WARN, "Error removing the session bean", e);
	}
	request.getSession().removeAttribute(SESSION_ATTR);
    }

}
