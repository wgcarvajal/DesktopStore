/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package desktopstore;

import desktopstore.util.Encrypt;
import entities.User;
import entities.Usergroup;
import java.awt.Color;
import java.util.concurrent.ExecutionException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.border.LineBorder;
import model.UserModel;

/**
 *
 * @author aranda
 */
public class LoginForm extends JFrame {
    
    private UserModel userModel;
    private User user;

    /**
     * Creates new form LoginForm
     */
    public LoginForm() {
        initComponents();
        userModel  = new UserModel();
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panel = new javax.swing.JPanel();
        tittle = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        username = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        password = new javax.swing.JPasswordField();
        btnLogin = new javax.swing.JButton();
        lblLoading = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setExtendedState(6);

        tittle.setFont(new java.awt.Font("Lucida Grande", 0, 24)); // NOI18N
        tittle.setText("Inicio de sesión");
        tittle.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/ico-usuario.png"))); // NOI18N

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/ico-pass.png"))); // NOI18N

        password.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                passwordActionPerformed(evt);
            }
        });

        btnLogin.setText("Iniciar sesión");
        btnLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoginActionPerformed(evt);
            }
        });

        lblLoading.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lblLoading.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/carga.gif"))); // NOI18N

        javax.swing.GroupLayout panelLayout = new javax.swing.GroupLayout(panel);
        panel.setLayout(panelLayout);
        panelLayout.setHorizontalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(tittle)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(panelLayout.createSequentialGroup()
                .addGap(106, 106, 106)
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panelLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(username, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(password)
                            .addGroup(panelLayout.createSequentialGroup()
                                .addComponent(btnLogin)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap(99, Short.MAX_VALUE))
            .addGroup(panelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblLoading)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelLayout.setVerticalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(tittle)
                .addGap(37, 37, 37)
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(username))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(password))
                .addGap(18, 18, 18)
                .addComponent(btnLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblLoading)
                .addContainerGap(41, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(30, Short.MAX_VALUE)
                .addComponent(panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(25, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(40, Short.MAX_VALUE)
                .addComponent(panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoginActionPerformed
        // TODO add your handling code here:
        login();
       
       
    }//GEN-LAST:event_btnLoginActionPerformed

    private void passwordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_passwordActionPerformed
        // TODO add your handling code here:
        login();
    }//GEN-LAST:event_passwordActionPerformed

    
   
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLogin;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel lblLoading;
    private javax.swing.JPanel panel;
    private javax.swing.JPasswordField password;
    private javax.swing.JLabel tittle;
    private javax.swing.JTextField username;
    // End of variables declaration//GEN-END:variables

    private void login() {
        username.setEnabled(false);
        password.setEnabled(false);
        btnLogin.setEnabled(false);
        lblLoading.setVisible(true);
        new SwingWorker<String,String>(){
            @Override
            protected String doInBackground()  {
                String result = "";
                String un = username.getText();
                String pass = password.getText();
                if (!un.isEmpty() && !pass.isEmpty()) {
                    String passEncrypt = Encrypt.sha256(pass);
                    Usergroup usergroup = userModel.findbyUserNameAndPassword(un, passEncrypt);
                    if(usergroup!=null)
                    {
                        user = usergroup.getUser();
                        String typeUser = usergroup.getGroupp().getGrouId();
                        if (typeUser.equals("cashier")) {
                            result = "cashier";
                        }
                        else if(typeUser.equals("admin"))
                        {
                            result = "admin";
                        }
                    }
                    else{
                        result = "Usuario o contraseña incorrectos";
                    }
                }else
                {
                    result = "Ingrese usuario y contraseña";
                }
              return result;   
            }

            @Override
            protected void done() {
                username.setEnabled(true);
                password.setEnabled(true);
                btnLogin.setEnabled(true);
                lblLoading.setVisible(false);
                try {
                    String result = get();
                    if(result.equals("cashier")){
                        CashForm cashForm = new CashForm();
                        cashForm.setCashier(user);
                        cashForm.init();
                        cashForm.setVisible(true);
                        LoginForm.this.dispose();
                    }
                    else if(result.equals("admin")){
                        
                    }
                    else{
                        JOptionPane.showMessageDialog(null, result);
                    }
                } catch (InterruptedException | ExecutionException ex) {
                    ex.printStackTrace();
                }
            }
        }.execute();
    }
    
    public void setValue()
    {
        getContentPane().setBackground(Color.WHITE);
        panel.setBackground(Color.WHITE);
        LineBorder line = new LineBorder(Color.blue, 1, true);
        panel.setBorder(line);
        lblLoading.setVisible(false);
    }
    
}
