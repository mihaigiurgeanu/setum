package ro.kds.erp.biz.setum;

import javax.ejb.EJBLocalObject;

/**
 * Colectie de servicii specifice SETUM.
 *
 *
 * Created: Tue Feb  6 21:16:30 2007
 *
 * @author <a href="mailto:Mihai Giurgeanu@MIHAIG">U-MIHAIG\Mihai Giurgeanu</a>
 * @version 1.0
 */
public interface SetumLocal extends EJBLocalObject {
    /**
     * Listele de valori sunt modelate in aplicatia SETUM ca produse.
     * Modelul este:
     * Categorie liste valori -> contine Categorii fiecare reprezentand o lista de valori -> 
     * contine Produse, fiecare fiind o valoare a listei. Fiecare valoare poate avea diverse
     * atribute; interpretarea atributelor este data de business-logic.
     */

}
