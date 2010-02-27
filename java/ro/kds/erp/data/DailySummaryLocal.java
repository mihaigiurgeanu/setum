package ro.kds.erp.data;

import javax.ejb.EJBLocalObject;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Describe interface DailySummaryLocal here.
 *
 *
 * Created: Sat Feb 27 00:55:06 2010
 *
 * @author <a href="mailto:mihai@mihaigiurgeanu.local">Mihai Giurgeanu</a>
 * @version 1.0
 */
public interface DailySummaryLocal extends javax.ejb.EJBLocalObject {
    /**
     * The primary key.
     */
    public Integer getId();

    /**
     * Get the summary date.
     */
    public Date getDate();

    /**
     * Set the summary date.
     */
    public void setDate(Date date);
    
    /**
     * Get the type of the summary. The type of the summary is a summary
     * category identifier. It is in the form of an URI <code>String</code>
     * to uniquely identify this summary.
     */
    public String getTypeURI();

    /**
     * Set the type of the summary.
     */
    public void setTypeURI(String typeURI);
    
    /**
     * Get a summary generic value.
     */
    public BigDecimal getValue1();

    /**
     * Set a summary generic value.
     */
    public void setValue1(BigDecimal value);

    /**
     * Get a summary generic value.
     */
    public BigDecimal getValue2();

    /**
     * Set a summary generic value.
     */
    public void setValue2(BigDecimal value);

    /**
     * Get a summary generic value.
     */
    public BigDecimal getValue3();

    /**
     * Set a summary generic value.
     */
    public void setValue3(BigDecimal value);

    /**
     * Get a summary generic value.
     */
    public BigDecimal getValue4();

    /**
     * Set a summary generic value.
     */
    public void setValue4(BigDecimal value);

    /**
     * Get a summary generic value.
     */
    public BigDecimal getValue5();

    /**
     * Set a summary generic value.
     */
    public void setValue5(BigDecimal value);

    /**
     * Get a summary generic value.
     */
    public BigDecimal getValue6();

    /**
     * Set a summary generic value.
     */
    public void setValue6(BigDecimal value);

    /**
     * Get a summary generic value.
     */
    public BigDecimal getValue7();

    /**
     * Set a summary generic value.
     */
    public void setValue7(BigDecimal value);

    /**
     * Get a summary generic value.
     */
    public BigDecimal getValue8();

    /**
     * Set a summary generic value.
     */
    public void setValue8(BigDecimal value);

    /**
     * Get a summary generic value.
     */
    public BigDecimal getValue9();

    /**
     * Set a summary generic value.
     */
    public void setValue9(BigDecimal value);

    /**
     * Get a summary generic value.
     */
    public BigDecimal getValue10();

    /**
     * Set a summary generic value.
     */
    public void setValue10(BigDecimal value);

}
