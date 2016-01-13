package ro.kds.erp.biz;

import javax.ejb.SessionContext;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import java.rmi.RemoteException;
import javax.ejb.CreateException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.Context;
import javax.rmi.PortableRemoteObject;
import java.util.Collection;
import java.util.ArrayList;
import java.util.prefs.BackingStoreException;

/**
 * Provides access to system preferences.
 *
 *
 * Created: Tue Oct 11 12:24:54 2005
 *
 * @author <a href="mailto:Mihai Giurgeanu@CRIMIRA"></a>
 * @version 1.0
 */
public class PreferencesBean implements SessionBean {

    private java.util.prefs.Preferences prefsNode;

    // Implementation of javax.ejb.SessionBean

    /**
     * Describe <code>setSessionContext</code> method here.
     *
     * @param sessionContext a <code>SessionContext</code> value
     * @exception EJBException if an error occurs
     * @exception RemoteException if an error occurs
     */
    public  void setSessionContext( SessionContext sessionContext) throws EJBException, RemoteException {

    }

    /**
     * Describe <code>ejbRemove</code> method here.
     *
     * @exception EJBException if an error occurs
     * @exception RemoteException if an error occurs
     */
    public  void ejbRemove() throws EJBException, RemoteException {

    }

    /**
     * Describe <code>ejbActivate</code> method here.
     *
     * @exception EJBException if an error occurs
     * @exception RemoteException if an error occurs
     */
    public  void ejbActivate() throws EJBException, RemoteException {

    }

    /**
     * Describe <code>ejbPassivate</code> method here.
     *
     * @exception EJBException if an error occurs
     * @exception RemoteException if an error occurs
     */
    public  void ejbPassivate() throws EJBException, RemoteException {

    }

    /**
     * Creation of a new bean
     */
    public void ejbCreate() throws CreateException {
	try {
	    InitialContext ic = new InitialContext();
	    Context env = (Context)ic.lookup("java:comp/env");
	    String nodePath = (String)env.lookup("preferencesPath");
	    prefsNode = java.util.prefs.Preferences.systemRoot().
		node(nodePath);
	} catch (NamingException e) {
	    e.printStackTrace();
	    throw new CreateException("Preferences bean could not be created because " + e.getMessage());
	}
    }

    /**
     * Gets the value of a named preference property.
     * @param key is the name of the property.
     * @param def is the default value of the property (if the property
     * could not be accessed)
     * @return the value of the property or the default value, if the
     * property can not be accessed.
     */
    public String get(String key, String def) throws RemoteException {
	synchronized(PreferencesBean.class) {
	    return prefsNode.get(key, def);
	}
    }

    /**
     * Gets the value of a named preference property.
     * @param key is the name of the property.
     * @param def is the default value of the property (if the property
     * could not be accessed)
     * @return the value of the property or the default value, if the
     * property can not be accessed.
     */
    public Double getDouble(String key, Double def) throws RemoteException {
	synchronized(PreferencesBean.class) {
	    return new Double(prefsNode.getDouble(key, def.doubleValue()));
	}
    }

    /**
     * Gets the value of a named preference property.
     * @param key is the name of the property.
     * @param def is the default value of the property (if the property
     * could not be accessed)
     * @return the value of the property or the default value, if the
     * property can not be accessed.
     */
    public Integer getInteger(String key, Integer def) throws RemoteException {
	synchronized(PreferencesBean.class) {
	    return new Integer(prefsNode.getInt(key, def.intValue()));
	}
    }


    /**
     * Associates the specified key with the specified value in the
     * preferences node represented by this bean.
     * @param key - the key with wich the value is to be associated.
     * @param value - the value to associate with the specified key.
     * @throws NullPointerException if key or value is null.
     * @throws IllegalArgumentException if <code>key.length</code> exceedes
     * <code>MAX_KEY_LENGTH</code> or if <code>value.length</code>
     * exceedes <code>MAX_VALUE_LENGTH</code> defined in the preferenses
     * API from Java SDK.
     * @throws IllegalStateException if the preferences node was removed.
     */
    public void put(String key, String value) throws NullPointerException,
						     IllegalArgumentException,
						     IllegalStateException {
	synchronized(PreferencesBean.class) {
	    prefsNode.put(key, value);
	}
    }

    /**
     * Associates the specified key with the specified value in the
     * preferences node represented by this bean.
     * @param key - the key with wich the value is to be associated.
     * @param value - the value to associate with the specified key.
     * @throws NullPointerException if key or value is null.
     * @throws IllegalArgumentException if <code>key.length</code> exceedes
     * <code>MAX_KEY_LENGTH</code> or if <code>value.length</code>
     * exceedes <code>MAX_VALUE_LENGTH</code> defined in the preferenses
     * API from Java SDK.
     * @throws IllegalStateException if the preferences node was removed.
     */
    public void put(String key, Double value) throws NullPointerException,
						     IllegalArgumentException,
						     IllegalStateException
    {
	synchronized(PreferencesBean.class) {
	    prefsNode.putDouble(key, value.doubleValue());
	}
    }

    /**
     * Associates the specified key with the specified value in the
     * preferences node represented by this bean.
     * @param key - the key with wich the value is to be associated.
     * @param value - the value to associate with the specified key.
     * @throws NullPointerException if key or value is null.
     * @throws IllegalArgumentException if <code>key.length</code> exceedes
     * <code>MAX_KEY_LENGTH</code> or if <code>value.length</code>
     * exceedes <code>MAX_VALUE_LENGTH</code> defined in the preferenses
     * API from Java SDK.
     * @throws IllegalStateException if the preferences node was removed.
     */
    public void put(String key, Integer value) throws NullPointerException,
						      IllegalArgumentException,
						      IllegalStateException
    {
	synchronized(PreferencesBean.class) {
	    prefsNode.putInt(key, value.intValue());
	}
    }


    /**
     * Preferences list for the application.
     *
     * @returns an <code>Collection</code> of strings  with
     * the names of the preferences keys contained in the top preference.
     */
    public Collection list() throws BackingStoreException, IllegalStateException {
	String[] _prefsNames = prefsNode.keys();
	ArrayList _prefsNamesList = new ArrayList(_prefsNames.length);
	for (int i = 0; i < _prefsNames.length; i++) {
	    _prefsNamesList.add(_prefsNames[i]);
	}
	return _prefsNamesList;
    }


    /**
     * Convenience method for getting a Preferences instance. It will
     * return an instance reffered in the jndi space with the uri 
     * <code>java:comp/env/ejb/PreferencesEJB</code>.
     */
    public static Preferences getPreferences() throws
	NamingException, CreateException, RemoteException {
	
	InitialContext it = new InitialContext();
	Context env = (Context) it.lookup("java:comp/env");
	PreferencesHome prefHome = (PreferencesHome)PortableRemoteObject.
	    narrow(env.lookup("ejb/PreferencesEJB"),
		   ro.kds.erp.biz.PreferencesHome.class);
	return prefHome.create();
    }
}
