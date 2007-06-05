package ro.kds.erp.biz;

import java.rmi.RemoteException;
import javax.naming.NamingException;
import javax.ejb.CreateException;
import javax.naming.InitialContext;
import javax.naming.Context;
import org.apache.commons.cli.*;
import javax.rmi.PortableRemoteObject;
import java.util.prefs.BackingStoreException;
import java.util.Iterator;
import java.util.Collection;

/**
 * Console client for administrating preferences settings.
 *
 *
 * Created: Tue Oct 11 14:32:36 2005
 *
 * @author <a href="mailto:Mihai Giurgeanu@CRIMIRA"></a>
 * @version 1.0
 */
public final class PreferencesConsole {

    /**
     * The entry point.
     *
     * @param args a <code>String[]</code> value
     */
    public static void main(final String[] args) {
	Option property = OptionBuilder.withArgName("property")
	    .hasArg()
	    .withDescription("The name of the setting you want to display or modify.")
	    .withType(java.lang.String.class)
	    .create('p');

	Option putString = OptionBuilder.withArgName("value")
	    .hasArg()
	    .withDescription("Update the setting with the string value.")
	    .withType(java.lang.String.class)
	    .create('v');

	Option putDouble = OptionBuilder.withArgName("value")
	    .hasArg()
	    .withDescription("Update the setting with the double value.")
	    .withType(java.lang.Double.class)
	    .create('d');

	Option putInt = OptionBuilder.withArgName("value")
	    .hasArg()
	    .withDescription("Update the setting with the int value.")
	    .withType(java.lang.Integer.class)
	    .create('i');

	Option show = OptionBuilder.withDescription("Display the setting's value")
	    .create('s');

	Option list = OptionBuilder.withDescription("List the set preferences")
            .create('l');

	OptionGroup commandOptions = new OptionGroup()
	    .addOption(putString)
	    .addOption(putDouble)
	    .addOption(putInt)
	    .addOption(show)
            .addOption(list);

	options = new Options()
	    .addOption("u", "usage", false, "Display usage instructions")
	    .addOption(property)
	    .addOptionGroup(commandOptions);

	try {
	    CommandLine parsedArguments = new PosixParser().
		parse(options, args);
	    if(parsedArguments.hasOption('h') 
	       || parsedArguments.hasOption('?')
	       || parsedArguments.hasOption('u')) {
		
		printHelp();
	    } else {
		try {
		    PreferencesConsole application = new PreferencesConsole();

		    if(parsedArguments.hasOption('l')) {
			application.list();
		    } 
		    else if(!parsedArguments.hasOption('p')) {
			throw new MissingOptionException("-p");
		    }
		    String propName = parsedArguments
			.getOptionValue('p', null);

		    if(parsedArguments.hasOption('s')) {
			application.show(propName);
		    } else if(parsedArguments.hasOption('i')) {
			application.put(propName,
					new Integer(Integer.parseInt(parsedArguments.getOptionValue('i', null))));
		    } else if(parsedArguments.hasOption('d')) {
			application.put(propName,
					new Double(Double.parseDouble(parsedArguments.getOptionValue('d', null))));
		    } else if(parsedArguments.hasOption('v')) {
			application.put(propName,
					parsedArguments.getOptionValue('v', null));
		    }
		} catch (NamingException e) {
		    System.err.println("System error occured:");
		    e.printStackTrace();
		    // return ERR_SYSTEM_ERROR;
		} catch (CreateException e) {
		    System.err.println("System error occured:");
		    e.printStackTrace();

		    // return ERR_SYSTEM_ERROR;
		} catch (RemoteException e) {
		    System.err.println("System error occured:");
		    e.printStackTrace();
		    // return ERR_SYSTEM_ERROR;
		} catch (BackingStoreException e) {
		    System.err.println("Can not access the preferences backingstore (database):");
		    e.printStackTrace();
		}
	    }
	} catch (AlreadySelectedException e) {
	    System.err.print("You must use only one of these options: ");
	    System.err.println(e.getMessage());
	    System.err.println("Please use -u to see usage help.");
	    System.exit(ERR_USAGE_ERROR);
	} catch (MissingArgumentException e) {
	    System.err.print("Argument missing - ");
	    System.err.println(e.getMessage());
	    System.err.println("Please use -u to see usage help.");
	    System.exit(ERR_USAGE_ERROR);
	} catch (MissingOptionException e) {
	    System.err.print("Mandatory option not used: ");
	    System.err.println(e.getMessage());
	    System.err.println("Please use -u to see usage help.");
	    System.exit(ERR_USAGE_ERROR);
	} catch (UnrecognizedOptionException e) {
	    System.err.print("Unrecognized option: ");
	    System.err.println(e.getMessage());
	    System.err.println("Please use -u to see usage help.");
	    System.exit(ERR_USAGE_ERROR);
	} catch (ParseException e) {
	    System.err.print("Error while parsing the command line.");
	    System.err.print("The error is related to the use of option ");
	    System.err.println(e.getMessage());
	    System.err.println("Please use -u to see usage help.");
	}
	System.exit(0);
    }

    /**
     * Prints usage instructions
     */
    private static void printHelp() {
	System.out.println("");
	new HelpFormatter().printHelp(prgName + " { -u | --usage | -l} | -p <property> {-d <value> | -i <value> | -v <value> | -s}", options);
    }
    
    /**
     * Set a property.
     */
    void put(String key, String value) throws 
	RemoteException, NullPointerException, IllegalArgumentException,
	IllegalStateException {

	prefs.put(key, value);
    }

    /**
     * Set a property.
     */
    void put(String key, Double value) throws 
	RemoteException, NullPointerException, IllegalArgumentException,
	IllegalStateException {

	prefs.put(key, value);
    }

    /**
     * Set a property.
     */
    void put(String key, Integer value) throws 
	RemoteException, NullPointerException, IllegalArgumentException,
	IllegalStateException {

	prefs.put(key, value);
    }

    /**
     * List the current preferences.
     */
    void list() throws RemoteException, BackingStoreException, IllegalStateException {
	Collection _keys = prefs.list();
	for(Iterator i = _keys.iterator(); i.hasNext(); ) {
	    String _key = (String)i.next();
	    System.out.println(_key);
	}
    }

    /**
     * Get a property's value and prints it at standard output.
     */
    void show(String key) throws RemoteException {
	System.out.println(prefs.get(key, DEFAULT_STRING));
    }

    /**
     * Get a property's value and prints it on standard output.
     */
    void showDouble(String key) throws RemoteException {
	System.out.println(prefs.getDouble(key, new Double(DEFAULT_DOUBLE)));
    }

    /**
     * Get a property's value and prints it on standard output.
     */
    void showInt(String key) throws RemoteException {
	System.out.println(prefs.getInteger(key, new Integer(DEFAULT_INT)));
    }

    /**
     * PreferencesConsole constructor. It creates the preferences bean.
     */
    protected PreferencesConsole() throws 
	NamingException, CreateException, RemoteException {
	
	InitialContext it = new InitialContext();
	Context env = (Context) it.lookup("java:comp/env");
	ro.kds.erp.biz.PreferencesHome prefHome = (ro.kds.erp.biz.PreferencesHome)PortableRemoteObject.
	    narrow(env.lookup("ejb/PreferencesEJB"),
		   ro.kds.erp.biz.PreferencesHome.class);
	prefs = prefHome.create();
    }

    private ro.kds.erp.biz.Preferences prefs = null;
    private static Options options = null;

    private static String HELP_MESSAGE = "Use ....";
    private static String DEFAULT_STRING = "Property not set";
    private static double DEFAULT_DOUBLE = 0;
    private static int DEFAULT_INT = 0;

    private static int ERR_SYSTEM_ERROR = -1;
    private static int ERR_USAGE_ERROR = -2;

    private static String prgName = "prefadmin";
}
