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
 * @version $Id: ClientLocal.java,v 1.3 2009/09/18 13:41:36 mihai Exp $
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

    /**
     * Convenience method to build the name of the client
     */
    public String getName();

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

    public String getAttribute1();
    public void setAttribute1(String attr);
    
    public String getAttribute2();
    public void setAttribute2(String attr);

    public String getAttribute3();
    public void setAttribute3(String attr);

    public String getAttribute4();
    public void setAttribute4(String attr);

    public String getAttribute5();
    public void setAttribute5(String attr);

    public String getAttribute6();
    public void setAttribute6(String attr);

    public String getAttribute7();
    public void setAttribute7(String attr);

    public String getAttribute8();
    public void setAttribute8(String attr);

    public String getAttribute9();
    public void setAttribute9(String attr);

    public String getAttribute10();
    public void setAttribute10(String attr);

    public String getAttribute11();
    public void setAttribute11(String attr);

    public String getAttribute12();
    public void setAttribute12(String attr);

    public String getAttribute13();
    public void setAttribute13(String attr);

    public String getAttribute14();
    public void setAttribute14(String attr);

    public String getAttribute15();
    public void setAttribute15(String attr);

}// ClientLocal
