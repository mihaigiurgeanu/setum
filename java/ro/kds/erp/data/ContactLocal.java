package ro.kds.erp.data;

import javax.ejb.EJBLocalObject;



/**
 * ContactLocal.java
 *
 *
 * Created: Tue Jan 17 04:31:01 2006
 *
 * @author <a href="mailto:mihai@cris.kds.ro">Mihai Giurgeanu</a>
 * @version $Id: ContactLocal.java,v 1.1 2006/01/18 04:26:10 mihai Exp $
 */

public interface ContactLocal extends EJBLocalObject {

    public Integer getId();
    public void setId(Integer newId);

    public String getFirstName();
    public void setFirstName(String firstName);
    
    public String getLastName();
    public void setLastName(String lastName);

    public String getPhone();
    public void setPhone(String phone);

    public String getMobile();
    public void setMobile(String mobile);

    public String getFax();
    public void setFax(String fax);

    public String getEmail();
    public void setEmail(String email);

    public String getDepartment();
    public void setDepartment(String department);

    public String getTitle();
    public void setTitle(String title);

    public String getComment();
    public void setComment(String comment);
}// ContactLocal
