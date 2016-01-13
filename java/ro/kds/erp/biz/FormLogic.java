package ro.kds.erp.biz;

/**
 * The methods that a business logic bean for form data entry are exposed
 * to the scripting engine.
 *
 *
 * Created: Wed Oct 19 20:22:20 2005
 *
 * @author <a href="mailto:Mihai Giurgeanu@CRIMIRA"></a>
 * @version 1.0
 */
public interface FormLogic {

    /**
     * Runs the rules that computes the values of "calculated fields"
     * and evaluates the validation rules.
     * The return is a ResponseBean that has records representing the
     * validation errors. A record in the response contains the fields:
     * - errorMessage
     * - fieldId - is the name of the field that caused the error
     * The code of the response will represent the number of rules that
     * were broken.
     */
    public ResponseBean applyRules();

}
