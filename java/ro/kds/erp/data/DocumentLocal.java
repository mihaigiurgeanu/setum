package ro.kds.erp.data;

import javax.ejb.EJBLocalObject;
import java.util.Date;

/**
 * The interface to the DocumentEJB bean. It encapsulates the most general
 * persistant information about a document. Examples of documents: offers,
 * orders, invoices, etc.
 *
 *
 * Created: Sun Oct 23 16:56:22 2005
 *
 * @author <a href="mailto:Mihai Giurgeanu@CRIMIRA"></a>
 * @version 1.0
 */
public interface DocumentLocal extends EJBLocalObject {

    /**
     * The primary key of the document.
     */
    public Integer getId();

    /**
     * Set the primary key of this document.
     */
    public void setId(Integer id);
    
    public String getNumber();
    public void setNumber(String number);
    
    public Date getDate();
    public void setDate(Date d);

    /**
     * A document that is "draft" can be edited and changed.
     * Once the document's property <code>draft</code> is marked false,
     * the document should not be modified any more. It means that the 
     * document has already been released to a third party. 
     *
     * These rules
     * are not enforced by the bean, but they should be enforced by the
     * business logic.
     */
    public Boolean getIsDraft();

    /**
     * Changes the "draft" status of this document. The business logic
     * should only change this status to true.
     *
     */
    public void setIsDraft(Boolean isDraft);


}
