package ro.kds.erp.biz.setum.basic;

import java.io.Serializable;
import java.util.Map;
import ro.kds.erp.data.AttributeLocal;

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

    /**
     * Builds an RDF URI for the field. This RDF is used by the UI to display
     * a localized and friendly name for the field in validation messages.
     *
     * @param fieldName - the name of the field
     * @return an RDF URI by concatenating the string <code>http://www.kds.ro/readybeans/rdf/forms/UsaStandard#</code>
     * with the name of the field.
     */
     public static String uri(String fieldName) {
	return "http://www.kds.ro/readybeans/rdf/forms/UsaStandard#" + fieldName;
     }

    String name;
    String code;
    Integer discontinued;
    Integer usaId;
    Integer broascaId;
    Integer cilindruId;
    Integer sildId;
    Integer yallaId;
    Integer vizorId;
    String filterUsa;
    String filterBroasca;
    String filterCilindru;
    String filterSild;
    String filterYalla;
    String filterVizor;

    public UsaStandardForm() {


       this.name = "";



       this.code = "";



       this.discontinued = new Integer(0);



       this.usaId = new Integer(0);



       this.broascaId = new Integer(0);



       this.cilindruId = new Integer(0);



       this.sildId = new Integer(0);



       this.yallaId = new Integer(0);



       this.vizorId = new Integer(0);



       this.filterUsa = "";



       this.filterBroasca = "";



       this.filterCilindru = "";



       this.filterSild = "";



       this.filterYalla = "";



       this.filterVizor = "";



    }

    public void setName(String newName) {
        this.name = newName;
    }

    public String getName() {
        return name;
    }

    public void readName(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("name");
	if(a != null) {
	    this.setName(a.getStringValue());
	}
    }

    public void setCode(String newCode) {
        this.code = newCode;
    }

    public String getCode() {
        return code;
    }

    public void readCode(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("code");
	if(a != null) {
	    this.setCode(a.getStringValue());
	}
    }

    public void setDiscontinued(Integer newDiscontinued) {
        this.discontinued = newDiscontinued;
    }

    public Integer getDiscontinued() {
        return discontinued;
    }

    public void readDiscontinued(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("discontinued");
	if(a != null) {
	    this.setDiscontinued(a.getIntValue());
	}
    }

    public void setUsaId(Integer newUsaId) {
        this.usaId = newUsaId;
    }

    public Integer getUsaId() {
        return usaId;
    }

    public void readUsaId(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("usaId");
	if(a != null) {
	    this.setUsaId(a.getIntValue());
	}
    }

    public void setBroascaId(Integer newBroascaId) {
        this.broascaId = newBroascaId;
    }

    public Integer getBroascaId() {
        return broascaId;
    }

    public void readBroascaId(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("broascaId");
	if(a != null) {
	    this.setBroascaId(a.getIntValue());
	}
    }

    public void setCilindruId(Integer newCilindruId) {
        this.cilindruId = newCilindruId;
    }

    public Integer getCilindruId() {
        return cilindruId;
    }

    public void readCilindruId(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("cilindruId");
	if(a != null) {
	    this.setCilindruId(a.getIntValue());
	}
    }

    public void setSildId(Integer newSildId) {
        this.sildId = newSildId;
    }

    public Integer getSildId() {
        return sildId;
    }

    public void readSildId(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("sildId");
	if(a != null) {
	    this.setSildId(a.getIntValue());
	}
    }

    public void setYallaId(Integer newYallaId) {
        this.yallaId = newYallaId;
    }

    public Integer getYallaId() {
        return yallaId;
    }

    public void readYallaId(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("yallaId");
	if(a != null) {
	    this.setYallaId(a.getIntValue());
	}
    }

    public void setVizorId(Integer newVizorId) {
        this.vizorId = newVizorId;
    }

    public Integer getVizorId() {
        return vizorId;
    }

    public void readVizorId(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("vizorId");
	if(a != null) {
	    this.setVizorId(a.getIntValue());
	}
    }

    public void setFilterUsa(String newFilterUsa) {
        this.filterUsa = newFilterUsa;
    }

    public String getFilterUsa() {
        return filterUsa;
    }

    public void readFilterUsa(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("filterUsa");
	if(a != null) {
	    this.setFilterUsa(a.getStringValue());
	}
    }

    public void setFilterBroasca(String newFilterBroasca) {
        this.filterBroasca = newFilterBroasca;
    }

    public String getFilterBroasca() {
        return filterBroasca;
    }

    public void readFilterBroasca(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("filterBroasca");
	if(a != null) {
	    this.setFilterBroasca(a.getStringValue());
	}
    }

    public void setFilterCilindru(String newFilterCilindru) {
        this.filterCilindru = newFilterCilindru;
    }

    public String getFilterCilindru() {
        return filterCilindru;
    }

    public void readFilterCilindru(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("filterCilindru");
	if(a != null) {
	    this.setFilterCilindru(a.getStringValue());
	}
    }

    public void setFilterSild(String newFilterSild) {
        this.filterSild = newFilterSild;
    }

    public String getFilterSild() {
        return filterSild;
    }

    public void readFilterSild(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("filterSild");
	if(a != null) {
	    this.setFilterSild(a.getStringValue());
	}
    }

    public void setFilterYalla(String newFilterYalla) {
        this.filterYalla = newFilterYalla;
    }

    public String getFilterYalla() {
        return filterYalla;
    }

    public void readFilterYalla(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("filterYalla");
	if(a != null) {
	    this.setFilterYalla(a.getStringValue());
	}
    }

    public void setFilterVizor(String newFilterVizor) {
        this.filterVizor = newFilterVizor;
    }

    public String getFilterVizor() {
        return filterVizor;
    }

    public void readFilterVizor(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("filterVizor");
	if(a != null) {
	    this.setFilterVizor(a.getStringValue());
	}
    }

}
