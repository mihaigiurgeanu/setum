package ro.kds.erp.biz;

import javax.ejb.EJBLocalObject;

/**
 * A category manager implements business logic common to 
 * all products in a category.
 *
 *
 * Created: Sun Jan 21 21:15:15 2007
 *
 * @author <a href="mailto:Mihai Giurgeanu@MIHAIG">U-MIHAIG\Mihai Giurgeanu</a>
 * @version 1.0
 */
public interface CategoryManagerLocal extends EJBLocalObject {

    /**
     * Builds a <code>ResponseBean</code> object with one record holding
     * the fields describing the products in the current category. Note that
     * this <code>ResponseBean</code> might be quite a complex structure. It
     * can contain inside other <code>ResponseBean</code> objects if it is a
     * complex product containing other products or lists of features.
     *
     * Its intended use is for building reports.
     */
    public ResponseBean getProductReport(Integer productId);

}
