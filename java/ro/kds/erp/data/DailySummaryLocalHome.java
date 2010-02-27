package ro.kds.erp.data;

import javax.ejb.EJBLocalHome;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import java.util.Date;
import java.util.Collection;

/**
 * Describe interface DailySummaryLocalHome here.
 *
 *
 * Created: Sat Feb 27 01:15:54 2010
 *
 * @author <a href="mailto:mihai@mihaigiurgeanu.local">Mihai Giurgeanu</a>
 * @version 1.0
 */
public interface DailySummaryLocalHome extends javax.ejb.EJBLocalHome {
    public DailySummaryLocal create() throws CreateException;
    public DailySummaryLocal findByPrimaryKey(Integer id) throws FinderException;
    public Collection findByDate(String typeURI, Date day) throws FinderException;
    public Collection findByDate(String typeURI, Date from, Date to) throws FinderException;
}
