package ro.kds.erp.biz.setum.basic;

import java.rmi.RemoteException;
import javax.ejb.EJBObject;
import javax.ejb.FinderException;
import ro.kds.erp.biz.ResponseBean;

/**
 * Fereastra remote interface.
 *
 * Class was automaticaly generated from a template.
 *
 */
public interface Fereastra extends EJBObject {
        
    public ResponseBean newFormData() throws RemoteException;
    public ResponseBean saveFormData() throws RemoteException;
    public ResponseBean loadFormData(Integer id) throws RemoteException, FinderException;


    public ResponseBean getCurrentFormData() throws RemoteException;
    public ResponseBean getLoadedPrimaryKey() throws RemoteException;

    public ResponseBean updateCanat(Integer newCanat) throws RemoteException;
    public ResponseBean updateLf(Double newLf) throws RemoteException;
    public ResponseBean updateHf(Double newHf) throws RemoteException;
    public ResponseBean updatePozitionare1(String newPozitionare1) throws RemoteException;
    public ResponseBean updatePozitionare2(String newPozitionare2) throws RemoteException;
    public ResponseBean updatePozitionare3(String newPozitionare3) throws RemoteException;
    public ResponseBean updateDeschidere(Integer newDeschidere) throws RemoteException;
    public ResponseBean updateSensDeschidere(Integer newSensDeschidere) throws RemoteException;
    public ResponseBean updatePozitionareBalamale(Integer newPozitionareBalamale) throws RemoteException;
    public ResponseBean updateComponenta(Integer newComponenta) throws RemoteException;
    public ResponseBean updateTipComponenta(Integer newTipComponenta) throws RemoteException;
    public ResponseBean updateTipGeam(Integer newTipGeam) throws RemoteException;
    public ResponseBean updateGeamSimpluId(Integer newGeamSimpluId) throws RemoteException;
    public ResponseBean updateGeamTermopanId(Integer newGeamTermopanId) throws RemoteException;
    public ResponseBean updateTipGrilaj(Integer newTipGrilaj) throws RemoteException;
    public ResponseBean updateGrilajStasId(Integer newGrilajStasId) throws RemoteException;
    public ResponseBean updateValoareGrilajAtipic(java.math.BigDecimal newValoareGrilajAtipic) throws RemoteException;
    public ResponseBean updateSellPrice(java.math.BigDecimal newSellPrice) throws RemoteException;
    public ResponseBean updateEntryPrice(java.math.BigDecimal newEntryPrice) throws RemoteException;
    public ResponseBean updatePrice1(java.math.BigDecimal newPrice1) throws RemoteException;

}
