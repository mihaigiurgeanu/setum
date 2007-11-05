package ro.kds.erp.rules;

import javax.ejb.EJBLocalObject;

/**
 * Describe interface RuleLocal here.
 *
 *
 * Created: Tue Oct 30 18:54:27 2007
 *
 * @author <a href="mailto:mihai@mihai-giurgeanus-computer.local">Mihai Giurgeanu</a>
 * @version 1.0
 */
public interface RuleLocal extends EJBLocalObject {
    public Integer getId();
    
    public String getName();
    public void setName(String name);

    public String getCondition();
    public void setCondition(String name);

    public String getMessage();
    public void setMessage(String msg);

    public Boolean getErrorFlag();
    public void setErrorFlag(Boolean error);
}
