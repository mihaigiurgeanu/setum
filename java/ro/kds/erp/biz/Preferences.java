package ro.kds.erp.biz;

import javax.ejb.EJBObject;
import java.rmi.RemoteException;
import java.util.prefs.BackingStoreException;
import java.util.Collection;

/**
 * Remote stateless session bean interface used to access application
 * preferences.
 *
 *
 * Created: Tue Oct 11 12:13:14 2005
 *
 * @author <a href="mailto:Mihai Giurgeanu@CRIMIRA"></a>
 * @version 1.0
 */
public interface Preferences extends EJBObject {


    /**
     * Gets the value of a named preference property.
     * @param key is the name of the property.
     * @param def is the default value of the property (if the property
     * could not be accessed)
     * @return the value of the property or the default value, if the
     * property can not be accessed.
     */
    public String get(String key, String def) throws RemoteException;

    /**
     * Gets the value of a named preference property.
     * @param key is the name of the property.
     * @param def is the default value of the property (if the property
     * could not be accessed)
     * @return the value of the property or the default value, if the
     * property can not be accessed.
     */
    public Double getDouble(String key, Double def) throws RemoteException;

    /**
     * Gets the value of a named preference property.
     * @param key is the name of the property.
     * @param def is the default value of the property (if the property
     * could not be accessed)
     * @return the value of the property or the default value, if the
     * property can not be accessed.
     */
    public Integer getInteger(String key, Integer def) throws RemoteException;

    /**
     * Associates the specified key with the specified value in the
     * preferences node represented by this bean.
     * @param key - the key with wich the value is to be associated.
     * @param value - the value to associate with the specified key.
     * @throws RemoteException - if an error occurs in the communication
     * with the application server.
     * @throws NullPointerException if key or value is null.
     * @throws IllegalArgumentException if <code>key.length</code> exceedes
     * <code>MAX_KEY_LENGTH</code> or if <code>value.length</code>
     * exceedes <code>MAX_VALUE_LENGTH</code> defined in the preferenses
     * API from Java SDK.
     * @throws IllegalStateException if the preferences node was removed.
     */
    public void put(String key, String value) throws RemoteException, 
						     NullPointerException,
						     IllegalArgumentException,
						     IllegalStateException;

    /**
     * Associates the specified key with the specified value in the
     * preferences node represented by this bean.
     * @param key - the key with wich the value is to be associated.
     * @param value - the value to associate with the specified key.
     * @throws RemoteException - if an error occurs in the communication
     * with the application server.
     * @throws NullPointerException if key or value is null.
     * @throws IllegalArgumentException if <code>key.length</code> exceedes
     * <code>MAX_KEY_LENGTH</code> or if <code>value.length</code>
     * exceedes <code>MAX_VALUE_LENGTH</code> defined in the preferenses
     * API from Java SDK.
     * @throws IllegalStateException if the preferences node was removed.
     */
    public void put(String key, Double value) throws RemoteException,
						     NullPointerException,
						     IllegalArgumentException,
						     IllegalStateException;


    /**
     * Associates the specified key with the specified value in the
     * preferences node represented by this bean.
     * @param key - the key with wich the value is to be associated.
     * @param value - the value to associate with the specified key.
     * @throws RemoteException - if an error occurs in the communication
     * with the application server.
     * @throws NullPointerException if key or value is null.
     * @throws IllegalArgumentException if <code>key.length</code> exceedes
     * <code>MAX_KEY_LENGTH</code> or if <code>value.length</code>
     * exceedes <code>MAX_VALUE_LENGTH</code> defined in the preferenses
     * API from Java SDK.
     * @throws IllegalStateException if the preferences node was removed.
     */
    public void put(String key, Integer value) throws RemoteException,
						      NullPointerException,
						      IllegalArgumentException,
						      IllegalStateException;


    /**
     * List the names of the preferences keys.
     *
     * @returns a <code>Collection</code> of strings.
     */
    public Collection list() throws RemoteException, IllegalStateException, BackingStoreException;
}
