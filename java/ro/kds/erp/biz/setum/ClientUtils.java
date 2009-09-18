package ro.kds.erp.biz.setum;

import ro.kds.erp.data.ClientLocal;
import ro.kds.erp.biz.ResponseBean;
import ro.kds.erp.data.ContactLocal;
import java.util.Iterator;
import java.util.Collection;

/**
 * Few common operations on client. The purpose of this class is
 * to be used by offer and order beans for reporting purposes.
 *
 *
 * Created: Tue Dec  9 01:01:21 2008
 *
 * @author <a href="mailto:mihai@mihai-giurgeanus-computer.local">Mihai Giurgeanu</a>
 * @version 1.0
 */
public class ClientUtils {

    ClientLocal client;

    /**
     * Creates a new <code>ClientUtils</code> instance.
     *
     */
    public ClientUtils(ClientLocal client) {
	this.client = client;
    }

    /**
     * Adds to the response bean fields representing the client.
     * The following fields are added to the response:
     *
     * @param r is the response to which the client fields will be
     * added. The <code>addRecord</code> should of been called. The
     * fields are added to the current record.
     */
    public void populateResponse(ResponseBean r) {
	if(client == null) {
	    r.addField("clientIsCompany", false);
	    r.addField("clientAddress", "");
	    r.addField("clientPostalCode", "");
	    r.addField("clientCity", "");
	    r.addField("clientCountryCode", "ro");
	    r.addField("clientCompanyCode", "");
	    r.addField("clientFiscalCode", "");
	    r.addField("clientPhone", "");
	    r.addField("clientIban", "");
	    r.addField("clientBank", "");
	    r.addField("clientComment", "");
	    r.addField("clientRegCom", "");
	} else {
	    r.addField("clientIsCompany", client.getIsCompany());
	    r.addField("clientAddress", client.getAddress());
	    r.addField("clientPostalCode", client.getPostalCode());
	    r.addField("clientCity", client.getCity());
	    r.addField("clientCountryCode", client.getCountryCode());
	    r.addField("clientCompanyCode", client.getCompanyCode());
	    r.addField("clientFiscalCode", client.getCompanyCode());
	    r.addField("clientPhone", client.getPhone());
	    r.addField("clientIban", client.getIban());
	    r.addField("clientBank", client.getBank());
	    r.addField("clientComment", client.getComment());
	    r.addField("clientRegCom", client.getAttribute1());
	}
    }

    public ResponseBean getContacts() {
	ResponseBean r = new ResponseBean();
	if(client != null) {
	    Collection contacts = client.getContacts();
	    for(Iterator i = contacts.iterator(); i.hasNext();) {
		ContactLocal contact = (ContactLocal)i.next();

		r.addRecord();
		r.addField("id", contact.getId());
		r.addField("firstName", contact.getFirstName());
		r.addField("lastName", contact.getLastName());
		r.addField("phone", contact.getPhone());
		r.addField("mobile", contact.getMobile());
		r.addField("fax", contact.getFax());
		r.addField("email", contact.getEmail());
		r.addField("department", contact.getDepartment());
		r.addField("title", contact.getTitle());
		r.addField("comment", contact.getComment());
	    }
	}
	return r;
    }
}
