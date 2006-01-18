package ro.kds.erp.biz.setum.basic;

import java.io.Serializable;
import java.util.Map;
import ro.kds.erp.data.AttributeLocal;

/**
 * A represantation of form data. While user is editing the data associated
 * with this object, the data is maintained temporary into this bean. At
 * the end, when <code>save</code> operation is called, the data is
 * saved in the persistance layer.
 *
 * Class was automaticaly generated from a template.
 *
 */
public class ClientsForm implements Serializable {
        
    Integer isCompany;
    String firstName;
    String lastName;
    String companyName;
    String address;
    String postalCode;
    String city;
    String countryCode;
    String companyCode;
    String phone;
    String iban;
    String bank;
    String comment;
    String contactFirstName;
    String contactLastName;
    String contactDepartment;
    String contactPhone;
    String contactMobile;
    String contactFax;
    String contactEmail;
    String contactTitle;
    String contactComment;

    public void setIsCompany(Integer newIsCompany) {
        this.isCompany = newIsCompany;
    }

    public Integer getIsCompany() {
        return isCompany;
    }

    public void readIsCompany(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("isCompany");
	if(a != null) {
	    this.setIsCompany(a.getIntValue());
	}
    }

    public void setFirstName(String newFirstName) {
        this.firstName = newFirstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void readFirstName(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("firstName");
	if(a != null) {
	    this.setFirstName(a.getStringValue());
	}
    }

    public void setLastName(String newLastName) {
        this.lastName = newLastName;
    }

    public String getLastName() {
        return lastName;
    }

    public void readLastName(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("lastName");
	if(a != null) {
	    this.setLastName(a.getStringValue());
	}
    }

    public void setCompanyName(String newCompanyName) {
        this.companyName = newCompanyName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void readCompanyName(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("companyName");
	if(a != null) {
	    this.setCompanyName(a.getStringValue());
	}
    }

    public void setAddress(String newAddress) {
        this.address = newAddress;
    }

    public String getAddress() {
        return address;
    }

    public void readAddress(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("address");
	if(a != null) {
	    this.setAddress(a.getStringValue());
	}
    }

    public void setPostalCode(String newPostalCode) {
        this.postalCode = newPostalCode;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void readPostalCode(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("postalCode");
	if(a != null) {
	    this.setPostalCode(a.getStringValue());
	}
    }

    public void setCity(String newCity) {
        this.city = newCity;
    }

    public String getCity() {
        return city;
    }

    public void readCity(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("city");
	if(a != null) {
	    this.setCity(a.getStringValue());
	}
    }

    public void setCountryCode(String newCountryCode) {
        this.countryCode = newCountryCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void readCountryCode(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("countryCode");
	if(a != null) {
	    this.setCountryCode(a.getStringValue());
	}
    }

    public void setCompanyCode(String newCompanyCode) {
        this.companyCode = newCompanyCode;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void readCompanyCode(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("companyCode");
	if(a != null) {
	    this.setCompanyCode(a.getStringValue());
	}
    }

    public void setPhone(String newPhone) {
        this.phone = newPhone;
    }

    public String getPhone() {
        return phone;
    }

    public void readPhone(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("phone");
	if(a != null) {
	    this.setPhone(a.getStringValue());
	}
    }

    public void setIban(String newIban) {
        this.iban = newIban;
    }

    public String getIban() {
        return iban;
    }

    public void readIban(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("iban");
	if(a != null) {
	    this.setIban(a.getStringValue());
	}
    }

    public void setBank(String newBank) {
        this.bank = newBank;
    }

    public String getBank() {
        return bank;
    }

    public void readBank(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("bank");
	if(a != null) {
	    this.setBank(a.getStringValue());
	}
    }

    public void setComment(String newComment) {
        this.comment = newComment;
    }

    public String getComment() {
        return comment;
    }

    public void readComment(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("comment");
	if(a != null) {
	    this.setComment(a.getStringValue());
	}
    }

    public void setContactFirstName(String newContactFirstName) {
        this.contactFirstName = newContactFirstName;
    }

    public String getContactFirstName() {
        return contactFirstName;
    }

    public void readContactFirstName(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("contactFirstName");
	if(a != null) {
	    this.setContactFirstName(a.getStringValue());
	}
    }

    public void setContactLastName(String newContactLastName) {
        this.contactLastName = newContactLastName;
    }

    public String getContactLastName() {
        return contactLastName;
    }

    public void readContactLastName(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("contactLastName");
	if(a != null) {
	    this.setContactLastName(a.getStringValue());
	}
    }

    public void setContactDepartment(String newContactDepartment) {
        this.contactDepartment = newContactDepartment;
    }

    public String getContactDepartment() {
        return contactDepartment;
    }

    public void readContactDepartment(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("contactDepartment");
	if(a != null) {
	    this.setContactDepartment(a.getStringValue());
	}
    }

    public void setContactPhone(String newContactPhone) {
        this.contactPhone = newContactPhone;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void readContactPhone(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("contactPhone");
	if(a != null) {
	    this.setContactPhone(a.getStringValue());
	}
    }

    public void setContactMobile(String newContactMobile) {
        this.contactMobile = newContactMobile;
    }

    public String getContactMobile() {
        return contactMobile;
    }

    public void readContactMobile(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("contactMobile");
	if(a != null) {
	    this.setContactMobile(a.getStringValue());
	}
    }

    public void setContactFax(String newContactFax) {
        this.contactFax = newContactFax;
    }

    public String getContactFax() {
        return contactFax;
    }

    public void readContactFax(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("contactFax");
	if(a != null) {
	    this.setContactFax(a.getStringValue());
	}
    }

    public void setContactEmail(String newContactEmail) {
        this.contactEmail = newContactEmail;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void readContactEmail(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("contactEmail");
	if(a != null) {
	    this.setContactEmail(a.getStringValue());
	}
    }

    public void setContactTitle(String newContactTitle) {
        this.contactTitle = newContactTitle;
    }

    public String getContactTitle() {
        return contactTitle;
    }

    public void readContactTitle(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("contactTitle");
	if(a != null) {
	    this.setContactTitle(a.getStringValue());
	}
    }

    public void setContactComment(String newContactComment) {
        this.contactComment = newContactComment;
    }

    public String getContactComment() {
        return contactComment;
    }

    public void readContactComment(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("contactComment");
	if(a != null) {
	    this.setContactComment(a.getStringValue());
	}
    }

}
