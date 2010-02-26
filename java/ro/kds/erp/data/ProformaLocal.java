package ro.kds.erp.data;

import javax.ejb.EJBLocalObject;
import java.math.BigDecimal;


public interface ProformaLocal extends javax.ejb.EJBLocalObject {

    /**
     * Primary key field.
     */
    public Integer getId();

    /**
     * Primary key field.
     */
    public void setId(Integer id);

    /**
     * Associated document entity.
     */
    public DocumentLocal getDocument();
    /**
     * Sets the associated document entity.
     */
    public void setDocument(DocumentLocal doc);

    /**
     * The order for which this proforma is issued.
     */
    public OrderLocal getOrder();

    /**
     * Sets the order for which this proforma is issued.
     */
    public void setOrder(OrderLocal o);

    /**
     * The role of the proforma. Can be:
     * <dl>
     * <dt>ADVANCE</dt><dd>For advance payments</dd>.
     * <dt>PAYMENT</dt><dd>For normal/final payments</dd>
     *</dl>
     */
    public String getRole();

    /**
     * Sets the role of the proforma. Can be:
     * <dl>
     * <dt>ADVANCE</dt><dd>For advance payments</dd>.
     * <dt>PAYMENT</dt><dd>For normal/final payments</dd>
     *</dl>
     */
    public void setRole(String role);

    /**
     * Proforma amount without tax. The currency is the system's
     * main currency.
     */
    public BigDecimal getAmount();

    /**
     * Sets the proforma amount without tax. The currency is the
     * system's main currency.
     */
    public void setAmount(BigDecimal amount);
    
    /**
     * The tax of the proforma (absolute value). The currency is
     * the system's main currency.
     */
    public BigDecimal getTax();
    
    /**
     * Sets the tax of the proforma in absolute value. The currency
     * is the system's main currency.
     */
    public void setTax(BigDecimal tax);

    /**
     * The exchange rate between the system's main currency
     * and the associated order's specific currency.
     */
    public Double getExchangeRate();

    /**
     * Sets the exchange rate between the system's main currency
     * and the associated order's specific currency.
     */
    public void setExchangeRate(Double rate);

    /**
     * Se afiseaza procentul in raport? Procentul inseamna cat reprezinta
     * procentual valoarea proformei din valoarea comenzii.
     */
    public Boolean getUsePercent();
    
    /**
     * Seteaza daca se afiseaza sau nu procentul in raport. Procentul inseamna
     * cat reprezinta procentual valoarea proformei din valoarea comenzii.
     */
    public void setUsePercent(Boolean usePercent);

    /**
     * Observatiile pentru proforma. Comentariul care se afiseaza 
     * la sfarsitul proformei.
     */
    public String getComment();
    
    /**
     * Seteaza observatiile pentru proforma.
     */
    public void setComment(String comment);

    /**
     * Contractul pentru care se face proforma.
     */
    public String getContract();

    /**
     * Seteaza numele contractului pentru care se face proforma.
     */
    public void setContract(String contract);

    /**
     * Numele obiectivului din contractul pentru care se face proforma.
     */
    public String getObiectiv();
    
    /**
     * Seteaza numele obiectivului din contractul pentru care se face
     * proforma
     */
    public void setObiectiv(String obiectiv);

    /**
     * Field to be used in future custom developments.
     */
    public String getAttribute1();

    /**
     * Field to be used in future custom developments.
     */
    public void setAttribute1(String attr);

    /**
     * Field to be used in future custom developments.
     */
    public String getAttribute2();
    
    /**
     * Field to be used in future custom developments.
     */
    public void setAttribute2(String attr);
    
    /**
     * Field to be used in future custom developments.
     */
    public String getAttribute3();
    
    /**
     * Field to be used in future custom developments.
     */
    public void setAttribute3(String attr);

    /**
     * Field to be used in future custom developments.
     */
    public String getAttribute4();

    /**
     * Field to be used in future custom developments.
     */
    public void setAttribute4(String attr);

    /**
     * Field to be used in future custom developments.
     */
    public String getAttribute5();

    /**
     * Field to be used in future custom developments.
     */
    public void setAttribute5(String attr);

    /**
     * Field to be used in future custom developments.
     */
    public String getAttribute6();
    
    /**
     * Field to be used in future custom developments.
     */
    public void setAttribute6(String attr);

    /**
     * Field to be used in future custom developments.
     */
    public String getAttribute7();

    /**
     * Field to be used in future custom developments.
     */
    public void setAttribute7(String attr);

    /**
     * Field to be used in future custom developments.
     */
    public String getAttribute8();

    /**
     * Field to be used in future custom developments.
     */
    public void setAttribute8(String attr);

    /**
     * Field to be used in future custom developments.
     */
    public String getAttribute9();

    /**
     * Field to be used in future custom developments.
     */
    public void setAttribute9(String attr);

    /**
     * Field to be used in future custom developments.
     */
    public String getAttribute10();

    /**
     * Field to be used in future custom developments.
     */
    public void setAttribute10(String attr);

    /**
     * Field to be used in future custom developments.
     */
    public String getAttribute11();

    /**
     * Field to be used in future custom developments.
     */
    public void setAttribute11(String attr);

    /**
     * Field to be used in future custom developments.
     */
    public String getAttribute12();

    /**
     * Field to be used in future custom developments.
     */
    public void setAttribute12(String attr);

    /**
     * Field to be used in future custom developments.
     */
    public String getAttribute13();

    /**
     * Field to be used in future custom developments.
     */
    public void setAttribute13(String attr);

    /**
     * Field to be used in future custom developments.
     */
    public String getAttribute14();

    /**
     * Field to be used in future custom developments.
     */
    public void setAttribute14(String attr);

    /**
     * Field to be used in future custom developments.
     */
    public String getAttribute15();

    /**
     * Field to be used in future custom developments.
     */
    public void setAttribute15(String attr);

} // ProformaLocal
