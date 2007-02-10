package ro.kds.erp.reports;

import javax.ejb.EJBLocalObject;
import ro.kds.erp.biz.ResponseBean;

/**
 * A <code>Transformer</code> object is used to transform a <code>ResponseBean</code>
 * object into a <code>XSLFO</code> representation.
 *
 *
 * Created: Wed Jan 24 13:38:12 2007
 *
 * @author <a href="mailto:Mihai Giurgeanu@MIHAIG">U-MIHAIG\Mihai Giurgeanu</a>
 * @version 1.0
 */
public interface TransformerLocal extends EJBLocalObject {
    /**
     * Performs the transformation of the <code>ResponseBean</code> into XSLFO.
     *
     * @param r is the ResponseBean to be transformed.
     * @param templateName is the name of the template to use for transformation.
     *
     * @return a string containing the XSLFO representation of the <code>ResponseBean</code>.
     * @throws TransformerException in case of problems related with transformation or
     * instantiation of the template.
     */
    public String transform(ResponseBean r, String templateName) throws TransformerException;
}

