package ro.kds.erp.biz.basic;

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
public class GenericProductsForm implements Serializable {
        
    Integer categoryId;
    String categoryName;
    Integer productId;
    String productName;
    String productDescription;
    String productCode;
    java.math.BigDecimal productEntryPrice;
    java.math.BigDecimal productSellPrice;
    java.math.BigDecimal productPrice1;
    java.math.BigDecimal productPrice2;
    java.math.BigDecimal productPrice3;
    java.math.BigDecimal productPrice4;
    java.math.BigDecimal productPrice5;
    Integer attrId;
    String attrName;
    String attrString;
    Integer attrInt;
    java.math.BigDecimal attrDecimal;
    Double attrDouble;

    public GenericProductsForm() {


       this.categoryId = new Integer(0);



       this.categoryName = "";



       this.productId = new Integer(0);



       this.productName = "";



       this.productDescription = "";



       this.productCode = "";



       this.productEntryPrice = new java.math.BigDecimal(0);



       this.productSellPrice = new java.math.BigDecimal(0);



       this.productPrice1 = new java.math.BigDecimal(0);



       this.productPrice2 = new java.math.BigDecimal(0);



       this.productPrice3 = new java.math.BigDecimal(0);



       this.productPrice4 = new java.math.BigDecimal(0);



       this.productPrice5 = new java.math.BigDecimal(0);



       this.attrId = new Integer(0);



       this.attrName = "";



       this.attrString = "";



       this.attrInt = new Integer(0);



       this.attrDecimal = new java.math.BigDecimal(0);



       this.attrDouble = new Double(0);
   


    }

    public void setCategoryId(Integer newCategoryId) {
        this.categoryId = newCategoryId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void readCategoryId(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("categoryId");
	if(a != null) {
	    this.setCategoryId(a.getIntValue());
	}
    }

    public void setCategoryName(String newCategoryName) {
        this.categoryName = newCategoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void readCategoryName(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("categoryName");
	if(a != null) {
	    this.setCategoryName(a.getStringValue());
	}
    }

    public void setProductId(Integer newProductId) {
        this.productId = newProductId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void readProductId(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("productId");
	if(a != null) {
	    this.setProductId(a.getIntValue());
	}
    }

    public void setProductName(String newProductName) {
        this.productName = newProductName;
    }

    public String getProductName() {
        return productName;
    }

    public void readProductName(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("productName");
	if(a != null) {
	    this.setProductName(a.getStringValue());
	}
    }

    public void setProductDescription(String newProductDescription) {
        this.productDescription = newProductDescription;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void readProductDescription(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("productDescription");
	if(a != null) {
	    this.setProductDescription(a.getStringValue());
	}
    }

    public void setProductCode(String newProductCode) {
        this.productCode = newProductCode;
    }

    public String getProductCode() {
        return productCode;
    }

    public void readProductCode(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("productCode");
	if(a != null) {
	    this.setProductCode(a.getStringValue());
	}
    }

    public void setProductEntryPrice(java.math.BigDecimal newProductEntryPrice) {
        this.productEntryPrice = newProductEntryPrice;
    }

    public java.math.BigDecimal getProductEntryPrice() {
        return productEntryPrice;
    }

    public void readProductEntryPrice(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("productEntryPrice");
	if(a != null) {
	    this.setProductEntryPrice(a.getDecimalValue());
	}
    }

    public void setProductSellPrice(java.math.BigDecimal newProductSellPrice) {
        this.productSellPrice = newProductSellPrice;
    }

    public java.math.BigDecimal getProductSellPrice() {
        return productSellPrice;
    }

    public void readProductSellPrice(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("productSellPrice");
	if(a != null) {
	    this.setProductSellPrice(a.getDecimalValue());
	}
    }

    public void setProductPrice1(java.math.BigDecimal newProductPrice1) {
        this.productPrice1 = newProductPrice1;
    }

    public java.math.BigDecimal getProductPrice1() {
        return productPrice1;
    }

    public void readProductPrice1(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("productPrice1");
	if(a != null) {
	    this.setProductPrice1(a.getDecimalValue());
	}
    }

    public void setProductPrice2(java.math.BigDecimal newProductPrice2) {
        this.productPrice2 = newProductPrice2;
    }

    public java.math.BigDecimal getProductPrice2() {
        return productPrice2;
    }

    public void readProductPrice2(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("productPrice2");
	if(a != null) {
	    this.setProductPrice2(a.getDecimalValue());
	}
    }

    public void setProductPrice3(java.math.BigDecimal newProductPrice3) {
        this.productPrice3 = newProductPrice3;
    }

    public java.math.BigDecimal getProductPrice3() {
        return productPrice3;
    }

    public void readProductPrice3(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("productPrice3");
	if(a != null) {
	    this.setProductPrice3(a.getDecimalValue());
	}
    }

    public void setProductPrice4(java.math.BigDecimal newProductPrice4) {
        this.productPrice4 = newProductPrice4;
    }

    public java.math.BigDecimal getProductPrice4() {
        return productPrice4;
    }

    public void readProductPrice4(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("productPrice4");
	if(a != null) {
	    this.setProductPrice4(a.getDecimalValue());
	}
    }

    public void setProductPrice5(java.math.BigDecimal newProductPrice5) {
        this.productPrice5 = newProductPrice5;
    }

    public java.math.BigDecimal getProductPrice5() {
        return productPrice5;
    }

    public void readProductPrice5(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("productPrice5");
	if(a != null) {
	    this.setProductPrice5(a.getDecimalValue());
	}
    }

    public void setAttrId(Integer newAttrId) {
        this.attrId = newAttrId;
    }

    public Integer getAttrId() {
        return attrId;
    }

    public void readAttrId(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("attrId");
	if(a != null) {
	    this.setAttrId(a.getIntValue());
	}
    }

    public void setAttrName(String newAttrName) {
        this.attrName = newAttrName;
    }

    public String getAttrName() {
        return attrName;
    }

    public void readAttrName(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("attrName");
	if(a != null) {
	    this.setAttrName(a.getStringValue());
	}
    }

    public void setAttrString(String newAttrString) {
        this.attrString = newAttrString;
    }

    public String getAttrString() {
        return attrString;
    }

    public void readAttrString(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("attrString");
	if(a != null) {
	    this.setAttrString(a.getStringValue());
	}
    }

    public void setAttrInt(Integer newAttrInt) {
        this.attrInt = newAttrInt;
    }

    public Integer getAttrInt() {
        return attrInt;
    }

    public void readAttrInt(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("attrInt");
	if(a != null) {
	    this.setAttrInt(a.getIntValue());
	}
    }

    public void setAttrDecimal(java.math.BigDecimal newAttrDecimal) {
        this.attrDecimal = newAttrDecimal;
    }

    public java.math.BigDecimal getAttrDecimal() {
        return attrDecimal;
    }

    public void readAttrDecimal(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("attrDecimal");
	if(a != null) {
	    this.setAttrDecimal(a.getDecimalValue());
	}
    }

    public void setAttrDouble(Double newAttrDouble) {
        this.attrDouble = newAttrDouble;
    }

    public Double getAttrDouble() {
        return attrDouble;
    }

    public void readAttrDouble(Map attributes) {
	AttributeLocal a = (AttributeLocal)attributes.get("attrDouble");
	if(a != null) {
	    this.setAttrDouble(a.getDoubleValue());
	}
    }

}
