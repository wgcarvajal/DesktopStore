/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package desktopstore.util;

/**
 *
 * @author Wilson Carvajal
 */
public class ResultAddProduct {
    
    private String message;
    private boolean productType;

    public ResultAddProduct() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isProductType() {
        return productType;
    }

    public void setProductType(boolean productType) {
        this.productType = productType;
    }
    
    
    
}
