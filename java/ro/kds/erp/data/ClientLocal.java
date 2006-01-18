package ro.kds.erp.data;

import javax.ejb.EJBLocalObject;
import java.util.Collection;



/**
 * ClientLocal.java
 *
 *
 * Created: Tue Jan 17 04:20:56 2006
 *
 * @author <a href="mailto:mihai@cris.kds.ro">Mihai Giurgeanu</a>
 * @version $Id: ClientLocal.java,v 1.1 2006/01/18 04:26:10 mihai Exp $
 */

public interface ClientLocal extends EJBLocalObject {
    public Integer getId();
    public void setId(Integer newId);

    public boolean getIsCompany();
    public void setIsCompany(boolean isCompany);
    
    public String getFirstName();
    public void setFirstName(String firstName);
    
    public String getLastName();
    public void setLastName(String lastName);

    public String getCompanyName();
    public void setCompanyName(String compayName);

    public String getAddress();
    public void setAddress(String address);

    public String getPostalCode();
    public void setPostalCode(String postalCode);

    public String getCity();
    public void setCity(String city);

    public String getCountryCode();
    public void setCountryCode(String countryCode);

    public String getCompanyCode();
    public void setCompanyCode(String companyCode);

    public String getPhone();
    public void setPhone(String phone);

    public String getIban();
    public void setIban(String iban);
    
    public String getBank();
    public void setBank(String bank);

    public String getComment();
    public void setComment(String comment);

    public Collection getContacts();
    public void setContacts(Collection contacts);
}// ClientLocal
