package ro.kds.erp.data;

import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import javax.ejb.CreateException;

/**
 * Operations for creating and locating DocumentLocal  entities.
 *
 *
 * Created: Sun Oct 23 17:08:26 2005
 *
 * @author <a href="mailto:Mihai Giurgeanu@CRIMIRA"></a>
 * @version 1.0
 */
public interface DocumentLocalHome extends EJBLocalHome {
    public DocumentLocal create() throws CreateException;
    public DocumentLocal findByPrimaryKey(Integer id) throws FinderException;
}
