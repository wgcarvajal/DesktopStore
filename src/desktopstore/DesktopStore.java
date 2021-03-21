/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package desktopstore;

import java.util.List;
import model.ProductModel;

/**
 *
 * @author aranda
 */
public class DesktopStore {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        java.awt.EventQueue.invokeLater(() -> {
            LoginForm loginForm = new  LoginForm();
            loginForm.setVisible(true);
            loginForm.setValue();
        });
    }
    
}
