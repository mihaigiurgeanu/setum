package ro.kds.erp.biz.setum;

import ro.kds.erp.biz.setum.basic.ClientsBean;
import ro.kds.erp.biz.ResponseBean;
import javax.naming.InitialContext;
import javax.naming.Context;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collection;
import org.objectweb.util.monolog.api.BasicLevel;
import ro.kds.erp.data.ContactLocal;
import ro.kds.erp.data.ContactLocalHome;
import javax.ejb.FinderException;
import ro.kds.erp.data.ClientLocalHome;
import ro.kds.erp.data.ClientLocal;
import javax.rmi.PortableRemoteObject;
import javax.ejb.RemoveException;
import java.util.Collections;
import java.util.Comparator;



/**
 * ClientsBeanImplementation.java
 * Implements the business logic for managing the clients records by
 * extending the functionality of auto generated <code>ClientsBean</code>
 * class.
 *
 * Created: Tue Jan 17 10:40:18 2006
 *
 * @author <a href="mailto:mihai@cris.kds.ro">Mihai Giurgeanu</a>
 * @version $Id: ClientsBeanImplementation.java,v 1.5 2009/09/18 13:41:36 mihai Exp $
 */
public class ClientsBeanImplementation extends ClientsBean {

    /**
     * Holds the contact that is currently selected.
     */
    private ContactLocal selectedContact = null;


    /**
     * The name of the Client entity.
     */
    public static final String ENTITY_CLIENT = "Client";



    /**
     * Saves the data from the current main form (client form) to the
     * persistent storage.
     */
    public ResponseBean saveFormData() {
	ResponseBean r;

	try {
	    InitialContext ic = new InitialContext();
	    Context env = (Context)ic.lookup("java:comp/env");
	    ClientLocalHome clh = (ClientLocalHome)PortableRemoteObject.narrow
		(env.lookup("ejb/ClientHome"), ClientLocalHome.class);
	    ClientLocal client;
	    if(id == null) {
		client = clh.create();
		id = client.getId();
	    } else {
		client = clh.findByPrimaryKey(id);
	    }

	    client.setIsCompany(form.getIsCompany().intValue()==0?false:true);
	    client.setFirstName(form.getFirstName());
	    client.setLastName(form.getLastName());
	    client.setCompanyName(form.getCompanyName());
	    client.setAddress(form.getAddress());
	    client.setPostalCode(form.getPostalCode());
	    client.setCity(form.getCity());
	    client.setCountryCode(form.getCountryCode());
	    client.setCompanyCode(form.getCompanyCode());
	    client.setPhone(form.getPhone());
	    client.setIban(form.getIban());
	    client.setBank(form.getBank());
	    client.setComment(form.getComment());
	    client.setAttribute1(form.getRegCom());
	    client.setAttribute2(form.getCnp());
	    
	    r = new ResponseBean();
	} catch (Exception e) {
	    r = new ResponseBean();
	    r.setCode(1);
	    r.setMessage("Eroare sistem! Datele despre client nu au putut fi salvate!");
	    logger.log(BasicLevel.ERROR, "Error saving the current record for id = " + id, e);
	    ejbContext.setRollbackOnly();
	}

	return r;
    }

    /**
     * Loads the value of the main form's fields from the persistent layer.
     * The <code>id</code> instance variable contains the primary key
     * for the client record that should be loaded. 
     */
    public ResponseBean loadFields() throws FinderException {
	ResponseBean r;

	try {
	    InitialContext ic = new InitialContext();
	    Context env = (Context)ic.lookup("java:comp/env");
	    ClientLocalHome clh = (ClientLocalHome)PortableRemoteObject.narrow
		(env.lookup("ejb/ClientHome"), ClientLocalHome.class);
	    ClientLocal client;
	    client = clh.findByPrimaryKey(id);

	    form.setIsCompany(new Integer(client.getIsCompany()?1:0));
	    form.setFirstName(client.getFirstName());
	    form.setLastName(client.getLastName());
	    form.setCompanyName(client.getCompanyName());
	    form.setAddress(client.getAddress());
	    form.setPostalCode(client.getPostalCode());
	    form.setCity(client.getCity());
	    form.setCountryCode(client.getCountryCode());
	    form.setCompanyCode(client.getCompanyCode());
	    form.setPhone(client.getPhone());
	    form.setIban(client.getIban());
	    form.setBank(client.getBank());
	    form.setComment(client.getComment());
	    form.setRegCom(client.getAttribute1());
	    form.setCnp(client.getAttribute2());

	    r = new ResponseBean();
	} catch (FinderException e) {
	    logger.log(BasicLevel.DEBUG,
		       "The client with id: " + id + " can not be loaded", e);
	    throw e;
	} catch (Exception e) {
	    r = new ResponseBean();
	    r.setCode(1);
	    r.setMessage("Eroare! Datele formului nu pot fi incarcate!");
	    logger.log(BasicLevel.ERROR,
		       "Error loading client data for id " + id);
	}
	return r;
    }

    /**
     * Form data initialization.
     */
    protected void createNewFormBean() {
	super.createNewFormBean();
	
	form.setIsCompany(new Integer(0));
	form.setFirstName("");
	form.setLastName("");
	form.setCompanyName("");
	form.setAddress("");
	form.setPostalCode("");
	form.setCity("Bucuresti");
	form.setCountryCode("ro");
	form.setCompanyCode("");
	form.setPhone("");
	form.setIban("");
	form.setBank("");
	form.setComment("");

	// subform fields
	form.setContactFirstName("");
	form.setContactLastName("");
	form.setContactDepartment("");
	form.setContactPhone("");
	form.setContactMobile("");
	form.setContactFax("");
	form.setContactEmail("");
	form.setContactTitle("");
	form.setContactComment("");

	selectedContact = null;
    }

    /**
     * Loads the list of clients
     */
    public ResponseBean loadListing(Integer isCompany) {
	ResponseBean r;
	try {
	    InitialContext ic = new InitialContext();
	    Context env = (Context)ic.lookup("java:comp/env");
	    
	    ClientLocalHome clh = (ClientLocalHome)PortableRemoteObject.narrow
		(env.lookup("ejb/ClientHome"), ClientLocalHome.class);

	    Collection clients;
	    switch(isCompany.intValue()) {
	    case 0:
		// select only persons
		clients = clh.findByCompanyFlag(false);
		break;
	    case 1:
		// selct only companies
		clients = clh.findByCompanyFlag(true);
		break;
	    case -1:
		// select all
		clients = clh.findAll();
		break;
	    default:
		logger.log(BasicLevel.WARN, "Unknown value for isCompany flag: " + isCompany);
		clients = clh.findAll();
		break;
	    }

	    

	    ArrayList cllist = new ArrayList(clients);
	    Collections.sort(cllist, new Comparator() {
		    public int compare(Object o1, Object o2) {
			ClientLocal c1 = (ClientLocal)o1;
			ClientLocal c2 = (ClientLocal)o2;

			return c1.getName().compareTo(c2.getName());
		    }
		});


	    r = new ResponseBean();
	    for(Iterator i = cllist.iterator(); i.hasNext(); ) {
		ClientLocal c = (ClientLocal)i.next();
		r.addRecord();
		r.addField("listing.id", c.getId());
		r.addField("listing.name", c.getName());
		r.addField("listing.companyName", c.getCompanyName());
		r.addField("listing.firstName", c.getFirstName());
		r.addField("listing.lastName", c.getLastName());
		r.addField("listing.city", c.getCity());
		r.addField("listing.countryCode", c.getCountryCode());
		r.addField("listing.phone", c.getPhone());
	    }
	    
	} catch (Exception e) {
	    r = new ResponseBean();
	    r.setCode(1);
	    r.setMessage("Eroare la incarcarea listei de clienti!");
	    logger.log(BasicLevel.ERROR,
		       "Error loading the list of clients", e);
	}

	return r;
    }



    /**
     * Gets the list of contacts for the current client.
     */
    public ResponseBean contactsListing() {
	ResponseBean r;
	try {
	    InitialContext ic = new InitialContext();
	    Context env = (Context)ic.lookup("java:comp/env");
	    
	    ClientLocalHome clh = (ClientLocalHome)PortableRemoteObject.narrow
		(env.lookup("ejb/ClientHome"), ClientLocalHome.class);
	    ClientLocal client = clh.findByPrimaryKey(id);
	    Collection contacts = client.getContacts();
	    
	    r = new ResponseBean();
	    for(Iterator i = contacts.iterator(); i.hasNext();) {
		ContactLocal c = (ContactLocal)i.next();
		r.addRecord();
		r.addField("contacts.id", c.getId());
		r.addField("contacts.firstName", c.getFirstName());
		r.addField("contacts.lastName", c.getLastName());
		r.addField("contacts.department", c.getDepartment());
		r.addField("contacts.phone", c.getPhone());
		r.addField("contacts.mobile", c.getMobile());
		r.addField("contacts.fax", c.getFax());
		r.addField("contacts.email", c.getEmail());
	    }
	} catch (FinderException e) {
	    r = new ResponseBean();
	    r.setCode(4);
	    r.setMessage("Lista de contacte pentru clientul curent nu poate fi incarcata.");
	    logger.log(BasicLevel.ERROR, "Error loading the contacts for client id " + id, e);
	} catch (Exception e) {
	    r = new ResponseBean();
	    r.setCode(1);
	    r.setMessage("Lista de contacte pentru clientul curent nu poate fi incarcata.");
	    logger.log(BasicLevel.ERROR, "Error loading the contacts for client id " + id, e);
	}
	return r;
    }

    /**
     * Load a specific contact data.
     */
    public ResponseBean loadSubForm(Integer loadId) {
	ResponseBean r;
	try {
	    InitialContext ic = new InitialContext();
	    Context env = (Context)ic.lookup("java:comp/env");
	    ContactLocalHome cth = (ContactLocalHome)PortableRemoteObject.
		narrow(env.lookup("ejb/ContactHome"), ContactLocalHome.class);
	    ContactLocal c = cth.findByPrimaryKey(loadId);

	    form.setContactFirstName(c.getFirstName());
	    form.setContactLastName(c.getLastName());
	    form.setContactDepartment(c.getDepartment());
	    form.setContactPhone(c.getPhone());
	    form.setContactMobile(c.getMobile());
	    form.setContactFax(c.getFax());
	    form.setContactEmail(c.getEmail());
	    form.setContactTitle(c.getTitle());
	    form.setContactComment(c.getComment());

	    selectedContact = c;

	    computeCalculatedFields(null);
	    
	    r = new ResponseBean();
	    r.addRecord();
	    copyFieldsToResponse(r);
	} catch (Exception e) {
	    r = new ResponseBean();
	    r.setCode(1);
	    r.setMessage("Datele contactului nu au putut fi incarcate");
	    logger.log(BasicLevel.ERROR,
		       "Data can not be loaded for contact id " + loadId);
	}
	return r;
    }
    
    /**
     * Clear the form data for creating a new contact. The addition
     * to the database will be done by saveSubForm method.
     */
    public ResponseBean addNewContact() {
	form.setContactFirstName("");
	form.setContactLastName("");
	form.setContactDepartment("");
	form.setContactPhone("");
	form.setContactMobile("");
	form.setContactFax("");
	form.setContactEmail("");
	form.setContactTitle("");
	form.setContactComment("");

	selectedContact = null;

	computeCalculatedFields(null);
	ResponseBean r = new ResponseBean();
	r.addRecord();
	copyFieldsToResponse(r);

	return r;
    }

    /**
     * Deletes the current contact from the database.
     */
    public ResponseBean removeContact() {
	ResponseBean r;
	if(selectedContact == null) {
	    r = new ResponseBean();
	    r.setCode(4);
	    r.setMessage("Nu este selectat nici un contact");
	} else {
	    try {
		selectedContact.remove();
		selectedContact = null;
		r = new ResponseBean();
	    } catch (RemoveException e) {
		r = new ResponseBean();
		r.setCode(3);
		r.setMessage("Eroare la stergerea contactului: " + e.getMessage());
		logger.log(BasicLevel.ERROR, "Exception when removing contact", e);
	    }
	}
	return r;
    }

    /**
     * Save the data for the current contact to the database.
     */
    public ResponseBean saveSubForm() {
	ResponseBean r;
	ContactLocal contact = selectedContact;
	try {
	    if(contact == null) {
		InitialContext ic = new InitialContext();
		Context env = (Context)ic.lookup("java:comp/env");
		ClientLocalHome clh = (ClientLocalHome)PortableRemoteObject.
		    narrow(env.lookup("ejb/ClientHome"), 
			   ClientLocalHome.class);
		ClientLocal client = clh.findByPrimaryKey(id);
		ContactLocalHome cth = (ContactLocalHome)PortableRemoteObject.
		    narrow(env.lookup("ejb/ContactHome"),
			   ContactLocalHome.class);
		contact = cth.create();
		client.getContacts().add(contact);
	    }
	    contact.setFirstName(form.getContactFirstName());
	    contact.setLastName(form.getContactLastName());
	    contact.setDepartment(form.getContactDepartment());
	    contact.setPhone(form.getContactPhone());
	    contact.setMobile(form.getContactMobile());
	    contact.setFax(form.getContactFax());
	    contact.setEmail(form.getContactEmail());
	    contact.setTitle(form.getContactTitle());
	    contact.setComment(form.getContactComment());

	    r = new ResponseBean();
	    selectedContact = contact;
	} catch (FinderException e) {
	    r = new ResponseBean();
	    r.setCode(4);
	    r.setMessage("Eroare! Clientul selectat curent nu poate fi incarcat!");
	    logger.log(BasicLevel.ERROR, "Can not load client id " + id, e);
	    ejbContext.setRollbackOnly();
	} catch (Exception e) {
	    r = new ResponseBean();
	    r.setCode(1);
	    r.setMessage("Eroare la salvarea datelor de contact.");
	    logger.log(BasicLevel.ERROR, "Error saving contact for client id " + id, e);
	    ejbContext.setRollbackOnly();
	}
	return r;
    }

    /**
     * Prepares a Collection of Maps containing the records of
     * client and its contacts. It will be used for displayng the
     * report of clients and contacts.
     */
    public Collection clientsCollectionMap() {
	return new ArrayList();
    }


    /**
     * Adds default value lists to the response bean. This method is 
     * called by the <code>copyFieldsToResponse(...)</code> method. Default
     * implementation does nothing. You have to overwrite this method
     * to add your value lists to the response bean, when a form data is
     * loading or when a new object is to be created.
     */
     protected void loadValueLists(ResponseBean r) {
	 r.addValueList("countryCode", ValueLists.makeCountriesVL());
     }

} // ClientsBeanImplementation
