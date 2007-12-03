package ro.kds.erp.biz.basic;

import java.rmi.RemoteException;
import javax.ejb.EJBObject;
import javax.ejb.FinderException;
import ro.kds.erp.biz.ResponseBean;

/**
 * RulesServer remote interface.
 *
 * Class was automaticaly generated from a template.
 *
 */
public interface RulesServer extends EJBObject {
        
    public ResponseBean newFormData() throws RemoteException;
    public ResponseBean saveFormData() throws RemoteException;
    public ResponseBean loadFormData(Integer id) throws RemoteException, FinderException;

    /**
     * Access to the form data.
     */
     public RulesServerForm getForm() throws RemoteException;

    public ResponseBean newRuleData() throws RemoteException;
    public ResponseBean saveRuleData() throws RemoteException;
    public ResponseBean loadRuleData(Integer id) throws RemoteException, FinderException;

    public ResponseBean getCurrentFormData() throws RemoteException;
    public ResponseBean getLoadedPrimaryKey() throws RemoteException;

    public ResponseBean updateSetName(String newSetName) throws RemoteException;
    public ResponseBean updateRuleName(String newRuleName) throws RemoteException;
    public ResponseBean updateCondition(String newCondition) throws RemoteException;
    public ResponseBean updateMessage(String newMessage) throws RemoteException;
    public ResponseBean updateMessageParam(String newMessageParam) throws RemoteException;
    public ResponseBean updateErrorFlag(Boolean newErrorFlag) throws RemoteException;


    public ResponseBean loadSets (
    ) throws RemoteException;
    public ResponseBean loadRules (
    ) throws RemoteException;
}
