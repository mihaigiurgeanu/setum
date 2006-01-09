package ro.kds.erp.biz.setum;

import javax.ejb.MessageDrivenContext;
import javax.ejb.EJBException;
import javax.ejb.MessageDrivenBean;
import javax.jms.Message;
import javax.jms.MessageListener;
import org.objectweb.util.monolog.api.BasicLevel;
import org.objectweb.util.monolog.api.Logger;
import javax.jms.Connection;
import javax.naming.InitialContext;
import javax.jms.Session;
import javax.jms.JMSException;
import javax.jms.Destination;
import javax.jms.ConnectionFactory;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.jms.MessageProducer;
import org.objectweb.jonas.common.Log;
import javax.jms.DeliveryMode;
import ro.kds.erp.biz.setum.basic.UsaStandardHome;
import ro.kds.erp.biz.setum.basic.UsaStandard;
import javax.rmi.PortableRemoteObject;



/**
 * PricesUpdater.java
 *
 *
 * Created: Sun Dec 11 17:46:17 2005
 *
 * @author <a href="mailto:mihai@cris.kds.ro">Mihai Giurgeanu</a>
 * @version 1.0
 */
public class PricesUpdater implements MessageListener, MessageDrivenBean {

    private transient MessageDrivenContext ejbContext;

    // Implementation of javax.jms.MessageListener

    /**
     * On receiving a message it updates the prices of composite products.
     * The message body is not interpreted in any way.
     *
     * @param message a <code>Message</code> value
     */
    public void onMessage(Message message) {
	logger.log(BasicLevel.DEBUG, "New message arrived");
	
	try {
	    InitialContext ic = new InitialContext();
	    Context env = (Context)ic.lookup("java:comp/env");
	    
	    {
		UsaStandardHome usaStdHome = 
		    (UsaStandardHome)PortableRemoteObject.narrow
		    (env.lookup("ejb/UsaStandardHome"), UsaStandardHome.class);
		UsaStandard usaStd = usaStdHome.create();
		usaStd.recalculatePrices();
	    }
	    // TODO add price recalculation code for other products
	} catch (Exception e) {
	    logger.log(BasicLevel.ERROR, "Can not run the price updating code",
		       e);
	    ejbContext.setRollbackOnly();
	}
    }





    static protected Logger logger = null;
    // Implementation of javax.ejb.MessageDrivenBean

    /**
     * <code>MessageDrivenBean</code> interface implementation
     *
     * @param context a <code>MessageDrivenContext</code> value
     * @exception EJBException if an error occurs
     */
    public void setMessageDrivenContext(MessageDrivenContext context) 
	throws EJBException {
	
        if (logger == null) {
            logger = Log.getLogger("ro.kds.erp.biz.setum.PricesUpdater");
        }
        logger.log(BasicLevel.DEBUG, "");
    }

    /**
     * <code>MessageDrivenBean</code> interface implementation.
     *
     * @exception EJBException if an error occurs
     */
    public void ejbRemove() throws EJBException {
	
        logger.log(BasicLevel.DEBUG, "");
    }

    
    /**
     * Method called when a new instance is created.
     * 
     */
    public void ejbCreate() {
   
	logger.log(BasicLevel.DEBUG, "");
    }

    /**
     * A convenience method to send messages to this bean.
     * When use it in your bean configure the following references:
     * <ul>
     * <li>resource-ref for jms/CF
     * <li>message-destination-ref for jms/priceUpdate
     * <ul>
     *
     * @throws JMSException if something goes wrong and the message could
     * not be sent. You probably need to rollback the current update
     * transaction.
     * @throws NamingException if the jndi names are not defined, or other
     * error occurs in the naming operations. The message could not be sent.
     * Also, you might consider to roll back the entire transaction.
     */
    static public void updatePrices() throws JMSException, NamingException {

	Connection conn = null;
	Session sess = null;
	try {
	    InitialContext ic = new InitialContext();
	    Context env = (Context)ic.lookup("java:comp/env");
	    ConnectionFactory jmsCF = (ConnectionFactory)env.lookup("jms/CF");
	    Destination topic = (Destination)env.lookup("jms/priceUpdate");
	    
	    conn = jmsCF.createConnection();
	    sess = conn.createSession(true, Session.AUTO_ACKNOWLEDGE);
	    MessageProducer mp = sess.createProducer(topic);
	    mp.setDeliveryMode(DeliveryMode.PERSISTENT);

	    Message msg = sess.createMessage();
	    mp.send(msg);

	} catch (NamingException e) {
	    logger.log(BasicLevel.ERROR, 
		       "Can not update the product's prices: ", e);
	    throw e;
	} catch (JMSException e) {
	    logger.log(BasicLevel.ERROR, 
		       "Can not update the product's prices: ", e);
	    throw e;
	} finally {
	    try { sess.close(); } catch (Exception ignore) {}
	    try { conn.close(); } catch (Exception ignore) {}
	}


    }
    
} // PricesUpdater
