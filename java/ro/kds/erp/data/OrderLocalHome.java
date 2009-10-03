package ro.kds.erp.data;

import javax.ejb.EJBLocalHome;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import java.util.Collection;
import java.util.Date;



/**
 * Describe interface OrderLocalHome here.
 *
 *
 * Created: Sun Sep 17 11:40:41 2006
 *
 * @author <a href="mailto:Mihai Giurgeanu@MIHAIG">U-MIHAIG\Mihai Giurgeanu</a>
 * @version 1.0
 */
public interface OrderLocalHome extends EJBLocalHome {
    public OrderLocal create() throws CreateException, DataLayerException;
    public OrderLocal findByPrimaryKey(Integer id) throws FinderException;
    public Collection findAll() throws FinderException;
    public Collection findLivrari(Date start, Date end) throws FinderException;
    public Collection findLivrariCuMontaj(Date start, Date end) throws FinderException;
    public Collection findLivrariFaraMontaj(Date start, Date end) throws FinderException;
}
