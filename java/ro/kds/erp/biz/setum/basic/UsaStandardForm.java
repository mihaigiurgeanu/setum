package ro.kds.erp.biz.setum.basic;

import java.io.Serializable;

/**
 * A represantation of form data. While user is editing the data associated
 * with this object, the data is maintained temporary into this bean. At
 * the end, when <code>save</code> operation is called, the data is
 * saved in the persistance layer.
 *
 * Class was automaticaly generated from a template.
 *
 */
public class UsaStandardForm implements Serializable {
        
    String name;
    String code;
    Integer usaId;
    String broascaId;
    String cilindruId;
    String sildId;
    String yallaId;
    String vizorId;

    public void setName(String newName) {
        this.name = newName;
    }

    public String getName() {
        return name;
    }

    public void setCode(String newCode) {
        this.code = newCode;
    }

    public String getCode() {
        return code;
    }

    public void setUsaId(Integer newUsaId) {
        this.usaId = newUsaId;
    }

    public Integer getUsaId() {
        return usaId;
    }

    public void setBroascaId(String newBroascaId) {
        this.broascaId = newBroascaId;
    }

    public String getBroascaId() {
        return broascaId;
    }

    public void setCilindruId(String newCilindruId) {
        this.cilindruId = newCilindruId;
    }

    public String getCilindruId() {
        return cilindruId;
    }

    public void setSildId(String newSildId) {
        this.sildId = newSildId;
    }

    public String getSildId() {
        return sildId;
    }

    public void setYallaId(String newYallaId) {
        this.yallaId = newYallaId;
    }

    public String getYallaId() {
        return yallaId;
    }

    public void setVizorId(String newVizorId) {
        this.vizorId = newVizorId;
    }

    public String getVizorId() {
        return vizorId;
    }

}
