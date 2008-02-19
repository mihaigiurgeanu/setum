package ro.kds.erp.biz.setum;

/**
 * Helper methods for building a grouping code. The grouping code
 * is a string used in reporting tools to group together similar
 * products. It is build by concatenating the codes of the product
 * components and features.
 *
 *
 * Created: Sat Feb 16 18:44:03 2008
 *
 * @author <a href="mailto:mihai@cris.kds.ro">Mihai Giurgeanu</a>
 * @version 1.0
 */
public class GroupingCode {

    /**
     * The string used to separate two components of
     * the grouping code.
     */
    private static String SEPARATOR="$";
    private StringBuffer gCode = new StringBuffer();
    /**
     * Creates a new <code>GroupingCode</code> instance.
     *
     */
    public GroupingCode() {
    }

    /**
     * Adds a new component to the grouping code. The component
     * is a string that will be concatenated at the end of the
     * current string with the grouping code separator.
     *
     * @param component is a string to be added to the end of the 
     * grouping code.
     * @return a reference to <code>this GroupinCode</code>.
     */
    public GroupingCode add(Object component) {
	gCode.append(SEPARATOR).append(component);
	return this;
    }

    /**
     * Get the grouping code.
     */
    public String toString() {
	return gCode.toString();
    }
}
