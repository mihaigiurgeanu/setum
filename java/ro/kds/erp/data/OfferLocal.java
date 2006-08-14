package ro.kds.erp.data;

import javax.ejb.EJBLocalObject;
import java.util.Collection;
import java.util.Date;

/**
 * Local interface to the OfferEJB bean.
 *
 *
 * Created: Sun Oct 23 12:31:39 2005
 *
 * @author <a href="mailto:Mihai Giurgeanu@CRIMIRA"></a>
 * @version 1.0
 */
public interface OfferLocal extends EJBLocalObject {

    /**
     * Get the primary key.
     */
    public Integer getId();
    
    /**
     * Set the primary key.
     */
    public void setId(Integer id);

    /**
     * The name of the offer is an internal identification. It can
     * be used for a short description, like "The offer for december",
     * or "Christmas sales", or it can be used to enter the name
     * of the person or company to which the offer is made to.
     * Since offers are either generic offers or made to a certain
     * customer, the offer does not have the client information. It
     * is not necessary to add a client to the database in order to
     * make an offer to a certain potential customer.
     */
    public String getName();

    /**
     * See <code>getName()</code>
     */
    public void setName(String offerName);

    /**
     * A small description of the offer.
     */
    public String getDescription();

    /**
     * A small description of the offer.
     */
    public void setDescription(String offerDescription);

    /**
     * The date from which the offer is available
     */
    public Date getDateFrom();

    /**
     * The date from which the offer is available
     */
    public void setDateFrom(Date dateFrom);

    /**
     * The date until this offer is available.
     */
    public Date getDateTo();
    
    /**
     * The date until this offer is available.
     */
    public void setDateTo(Date dateTo);

    /**
     * An offer can be discontinued, regardless its valability dates.
     *
     * @returns true if the offer is discontinued
     */
    public Boolean getDiscontinued();
    
    /**
     * An offer can be discontinued regardless its valability daty.
     *
     * @param discontinue should be true to discontinue this offer or false
     * to allow this offer to be checked against valability dates.
     */
    public void setDiscontinued(Boolean discontinue);

    /**
     * A comment to be attached with this offer.
     */
    public String getComment();
    
    /**
     * A comment to be attached with this offer.
     */
    public void setComment(String comment);

    /**
     * The line items contained in this offer.
     *
     * Returns a <code>Collection</code> of <code>OfferItemLocal</code> ojects
     */
    public Collection getItems();

    /**
     * The line items on this offer.
     *
     * @param items is a <code>Collection</code> of <code>OfferItemLocal</code>
     */
    public void setItems(Collection items);

    /**
     * The generic document object specifies data common to all documents
     * like document's number and document's date.
     */
    public DocumentLocal getDocument();

    /**
     * The generic document object specifies data common to all documents
     * like document's number and document's date.
     */
    public void setDocument(DocumentLocal d);
    
    /**
     * The category of the offer is used to be able to group
     * categories into different kinds of categories.
     */
    public Integer getCategory();

    /**
     * The category of the offer is used to be able to group
     * categories into different kinds of categories.
     */
    public void setCategory(Integer cat);

    /**
     * The client associated with this offer.
     */
    public ClientLocal getClient();

    /**
     * The client associated with this offer.
     */
    public void setClient(ClientLocal client);

}
