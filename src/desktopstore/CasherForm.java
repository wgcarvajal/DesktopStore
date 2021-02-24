/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package desktopstore;

import desktopstore.util.AppConstants;
import desktopstore.util.CashInfo;
import desktopstore.util.Encrypt;
import desktopstore.util.ResultAddProduct;
import desktopstore.util.Scale;
import desktopstore.util.ScaleIP;
import desktopstore.util.Util;
import desktopstore.util.WordWrapCellRenderer;
import entities.Cancelpurchaseauditorie;
import entities.Price;
import entities.Pricepurchase;
import entities.Product;
import entities.Purchase;
import entities.Purchaseitem;
import entities.User;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingWorker;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import model.CancelpurchaseauditorieModel;
import model.PriceModel;
import model.PricePurchaseModel;
import model.ProductModel;
import model.PurchaseModel;
import model.PurchaseitemModel;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author aranda
 */
public class CasherForm extends javax.swing.JFrame {
    
    private final String TAG = "CasherForm";
    
    private PurchaseModel purchaseModel;
    private ProductModel productModel;
    private PriceModel priceModel;
    private PricePurchaseModel pricePurchaseModel;
    private PurchaseitemModel purchaseitemModel;
    private CancelpurchaseauditorieModel cancelpurchaseauditorieModel;
    
    private static List<ScaleIP>listScaleIp;
    private Scale scale;
    private boolean isReturnProductWeight;
    private String weight;
    
    private List<Purchaseitem> purchaseitems;
    private Purchase purchase;
    private User cashier;
    private Product producWaitForWeight;
    private CashInfo cashInfo;

    /**
     * Creates new form CasherForm
     */
    public CasherForm() {
        initComponents();
        productModel = new ProductModel();
        priceModel = new PriceModel();
        pricePurchaseModel = new PricePurchaseModel();
        purchaseModel = new PurchaseModel();
        purchaseitemModel = new PurchaseitemModel();
        cancelpurchaseauditorieModel = new CancelpurchaseauditorieModel();
    }
    
    
    public void closeView()
    {
        openBtn.setVisible(true);
        exitBtn.setVisible(false);
        addClientBtn.setVisible(false);
        addToBacketBtn.setVisible(false);
        changeQuantitybtn.setVisible(false);
        cancelBtn.setVisible(false);
        paymentBtn.setVisible(false);
        removeProductBtn.setVisible(false);
        resumeBtn.setVisible(false);
        returnProductBtn.setVisible(false);
        saleJPanel.setVisible(false);
        searchClientBtn.setVisible(false);
        searchProductBtn.setVisible(false);
        selectBtn.setVisible(false);
        totalBtn.setVisible(false);
    }
    
    public void openCashView()
    {
        exitBtn.setVisible(true);
        totalBtn.setVisible(true);
        resumeBtn.setVisible(true);
        searchClientBtn.setVisible(true);
        searchProductBtn.setVisible(true);
        openBtn.setVisible(false);
        addClientBtn.setVisible(false);
        addToBacketBtn.setVisible(true);
        changeQuantitybtn.setVisible(false);
        cancelBtn.setVisible(false);
        paymentBtn.setVisible(false);
        removeProductBtn.setVisible(false);
        returnProductBtn.setVisible(false);
        saleJPanel.setVisible(false);
        selectBtn.setVisible(false);
        
    }
    
    public void addToBacketView()
    {
        openBtn.setVisible(false);
        exitBtn.setVisible(false);
        addClientBtn.setVisible(false);
        addToBacketBtn.setVisible(false);
        changeQuantitybtn.setVisible(false);
        cancelBtn.setVisible(true);
        paymentBtn.setVisible(false);
        removeProductBtn.setVisible(false);
        resumeBtn.setVisible(false);
        returnProductBtn.setVisible(true);
        saleJPanel.setVisible(true);
        searchClientBtn.setVisible(true);
        searchProductBtn.setVisible(true);
        selectBtn.setVisible(true);
        totalBtn.setVisible(false);
    }
    
    public void addtoBacketInitValues()
    {
        lblNameClient.setText("");
        lblTotal.setText("$0");
        tableScrollPanel.setVisible(false);
    }
    
    
    public void openCash()
    {
        new SwingWorker<Boolean, String>(){
            @Override
            protected Boolean doInBackground()  {
               String pass = txtOpenCashPassword.getText();
               if(!pass.isEmpty())
               {
                    String passEncrypt = Encrypt.sha256(pass);
                    return cashier.getUsPassword().equals(passEncrypt); 
               }
               return false;
            }
            @Override
            protected void done() {
                try {
                    boolean result = get();
                    loadingDialog.setVisible(false);
                    if(result)
                    {
                        openCashView();
                    }
                    else
                    {
                        openCashPasswordDialog.setVisible(true);
                    }
                } catch (InterruptedException | ExecutionException ex) {
                   ex.printStackTrace();
                }
            }
        }.execute();
        openCashPasswordDialog.setVisible(false);
        loadingDialog.setVisible(true);
    }
    
    public void cancelSale()
    {
        new SwingWorker<Boolean, String>(){
            @Override
            protected Boolean doInBackground()  {
               String pass = txtCancelSalepassword.getText();
               if(!pass.isEmpty())
               {
                    String passEncrypt = Encrypt.sha256(pass);
                    if(cashier.getUsPassword().equals(passEncrypt)){
                       if (purchase != null) {
                           Cancelpurchaseauditorie cancelpurchaseauditorie = new Cancelpurchaseauditorie();
                           cancelpurchaseauditorie.setCanpuraudDate(new Date());
                           cancelpurchaseauditorie.setUser(purchase.getUser());
                           JSONObject jsonPurchase = new JSONObject();
                           jsonPurchase.put("purId", purchase.getPurId());
                           jsonPurchase.put("purDate", Util.getFormatCurrentDate(purchase.getPurDate()));
                           JSONArray jsonArrayPurchaseItems = new JSONArray();
                           for (Purchaseitem pi : purchaseitems) {
                               JSONObject jsonPurchaseItem = new JSONObject();
                               jsonPurchaseItem.put("purItemId", pi.getPurItemId());
                               jsonPurchaseItem.put("purItemQuantity", pi.getPurItemQuantity());
                               jsonPurchaseItem.put("prodId", pi.getProduct().getProdId());
                               jsonPurchaseItem.put("priceValue", pi.getPriceValue());
                               jsonPurchaseItem.put("iva", pi.getIva());
                               jsonPurchaseItem.put("pricePurValue", pi.getPricePurValue());
                               jsonPurchaseItem.put("ownId", pi.getOwner().getOwnId());
                               jsonArrayPurchaseItems.add(jsonPurchaseItem);
                           }
                           jsonPurchase.put("purchaseItems", jsonArrayPurchaseItems);
                           cancelpurchaseauditorie.setPurchase(jsonPurchase.toString());
                           cancelpurchaseauditorieModel.create(cancelpurchaseauditorie);
                           purchaseitemModel.deleteByPurId(purchase.getPurId());
                           purchaseModel.delete(purchase);
                           purchase = null;
                       }
                       purchaseitems = null;
                       return true;
                   }
                    
                    return false; 
               }
               return false;
            }
            @Override
            protected void done() {
                try {
                    boolean result = get();
                    loadingDialog.setVisible(false);
                    if(result)
                    {
                        openCashView();
                    }
                    else
                    {
                        cancelSalePasswordDialog.setVisible(true);
                    }
                } catch (InterruptedException | ExecutionException ex) {
                   ex.printStackTrace();
                }
            }
        }.execute();
        cancelSalePasswordDialog.setVisible(false);
        loadingDialog.setVisible(true);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        openCashPasswordDialog = new javax.swing.JDialog();
        jPanel8 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        btnOkOpenCash = new javax.swing.JButton();
        txtOpenCashPassword = new javax.swing.JPasswordField();
        cancelSalePasswordDialog = new javax.swing.JDialog();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtCancelSalepassword = new javax.swing.JPasswordField();
        btnOkCancelSale = new javax.swing.JButton();
        loadingDialog = new javax.swing.JDialog();
        jLabel8 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        rSLabelHora1 = new rojeru_san.RSLabelHora();
        jPanel3 = new javax.swing.JPanel();
        rSLabelFecha2 = new rojeru_san.RSLabelFecha();
        userName = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        openBtn = new javax.swing.JButton();
        exitBtn = new javax.swing.JButton();
        cancelBtn = new javax.swing.JButton();
        paymentBtn = new javax.swing.JButton();
        selectBtn = new javax.swing.JButton();
        changeQuantitybtn = new javax.swing.JButton();
        removeProductBtn = new javax.swing.JButton();
        addToBacketBtn = new javax.swing.JButton();
        resumeBtn = new javax.swing.JButton();
        searchProductBtn = new javax.swing.JButton();
        returnProductBtn = new javax.swing.JButton();
        addClientBtn = new javax.swing.JButton();
        searchClientBtn = new javax.swing.JButton();
        totalBtn = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        codeTxt = new javax.swing.JTextField();
        btnReadCode = new javax.swing.JButton();
        saleJPanel = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        lblNameClient = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        lblTotal = new javax.swing.JLabel();
        tableScrollPanel = new javax.swing.JScrollPane();
        productTable = new javax.swing.JTable();

        openCashPasswordDialog.setModal(true);

        jLabel9.setText("Contraseña:");

        btnOkOpenCash.setText("Aceptar");
        btnOkOpenCash.setFocusable(false);
        btnOkOpenCash.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkOpenCashActionPerformed(evt);
            }
        });

        txtOpenCashPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtOpenCashPasswordActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtOpenCashPassword, javax.swing.GroupLayout.DEFAULT_SIZE, 291, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnOkOpenCash)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtOpenCashPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnOkOpenCash, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout openCashPasswordDialogLayout = new javax.swing.GroupLayout(openCashPasswordDialog.getContentPane());
        openCashPasswordDialog.getContentPane().setLayout(openCashPasswordDialogLayout);
        openCashPasswordDialogLayout.setHorizontalGroup(
            openCashPasswordDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        openCashPasswordDialogLayout.setVerticalGroup(
            openCashPasswordDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        cancelSalePasswordDialog.setModal(true);

        jLabel5.setBackground(new java.awt.Color(252, 248, 227));
        jLabel5.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(192, 152, 83));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/warningIcon.png.png"))); // NOI18N
        jLabel5.setText("Está seguro que desea eliminar la compra?");
        jLabel5.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(251, 238, 213), 1, true));
        jLabel5.setOpaque(true);

        jLabel7.setText("Contraseña:");

        txtCancelSalepassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCancelSalepasswordActionPerformed(evt);
            }
        });

        btnOkCancelSale.setText("Aceptar");
        btnOkCancelSale.setFocusable(false);
        btnOkCancelSale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkCancelSaleActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout cancelSalePasswordDialogLayout = new javax.swing.GroupLayout(cancelSalePasswordDialog.getContentPane());
        cancelSalePasswordDialog.getContentPane().setLayout(cancelSalePasswordDialogLayout);
        cancelSalePasswordDialogLayout.setHorizontalGroup(
            cancelSalePasswordDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cancelSalePasswordDialogLayout.createSequentialGroup()
                .addContainerGap(22, Short.MAX_VALUE)
                .addGroup(cancelSalePasswordDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 356, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(cancelSalePasswordDialogLayout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(cancelSalePasswordDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtCancelSalepassword, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnOkCancelSale, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(22, Short.MAX_VALUE))
        );
        cancelSalePasswordDialogLayout.setVerticalGroup(
            cancelSalePasswordDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cancelSalePasswordDialogLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(cancelSalePasswordDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCancelSalepassword, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnOkCancelSale, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        loadingDialog.setModal(true);
        loadingDialog.setModalExclusionType(java.awt.Dialog.ModalExclusionType.APPLICATION_EXCLUDE);

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/carga.gif"))); // NOI18N

        loadingDialog.setUndecorated(true);

        javax.swing.GroupLayout loadingDialogLayout = new javax.swing.GroupLayout(loadingDialog.getContentPane());
        loadingDialog.getContentPane().setLayout(loadingDialogLayout);
        loadingDialogLayout.setHorizontalGroup(
            loadingDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(loadingDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        loadingDialogLayout.setVerticalGroup(
            loadingDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(loadingDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setExtendedState(6);

        jPanel1.setBackground(new java.awt.Color(3, 111, 171));

        jPanel2.setBackground(new java.awt.Color(92, 156, 204));

        rSLabelHora1.setForeground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(rSLabelHora1, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(rSLabelHora1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel3.setBackground(new java.awt.Color(92, 156, 204));
        jPanel3.setPreferredSize(new java.awt.Dimension(100, 23));

        rSLabelFecha2.setForeground(new java.awt.Color(255, 255, 255));
        rSLabelFecha2.setPreferredSize(new java.awt.Dimension(100, 23));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(rSLabelFecha2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(rSLabelFecha2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        userName.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        userName.setForeground(new java.awt.Color(255, 255, 255));
        userName.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/ico-usuario.png"))); // NOI18N

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/officon.png"))); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(userName, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(userName, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(9, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(3, 111, 171));

        jLabel1.setFont(new java.awt.Font("Lucida Grande", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Developed by wgcarvajal");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap(533, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addContainerGap(597, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(204, 204, 204));

        openBtn.setBackground(new java.awt.Color(255, 255, 255));
        openBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/sale/openDoor.png"))); // NOI18N
        openBtn.setBorderPainted(false);
        openBtn.setFocusable(false);
        openBtn.setOpaque(true);
        openBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openBtnActionPerformed(evt);
            }
        });

        exitBtn.setBackground(new java.awt.Color(255, 255, 255));
        exitBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/sale/exitDoor.png"))); // NOI18N
        exitBtn.setBorderPainted(false);
        exitBtn.setFocusable(false);
        exitBtn.setOpaque(true);
        exitBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitBtnActionPerformed(evt);
            }
        });

        cancelBtn.setBackground(new java.awt.Color(255, 255, 255));
        cancelBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/sale/cancelar.png"))); // NOI18N
        cancelBtn.setBorderPainted(false);
        cancelBtn.setFocusable(false);
        cancelBtn.setOpaque(true);
        cancelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelBtnActionPerformed(evt);
            }
        });

        paymentBtn.setBackground(new java.awt.Color(255, 255, 255));
        paymentBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/sale/payment.png"))); // NOI18N
        paymentBtn.setBorderPainted(false);
        paymentBtn.setFocusable(false);
        paymentBtn.setOpaque(true);
        paymentBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                paymentBtnActionPerformed(evt);
            }
        });

        selectBtn.setBackground(new java.awt.Color(255, 255, 255));
        selectBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/product/selectProduct.png"))); // NOI18N
        selectBtn.setBorderPainted(false);
        selectBtn.setFocusable(false);
        selectBtn.setOpaque(true);
        selectBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectBtnActionPerformed(evt);
            }
        });

        changeQuantitybtn.setBackground(new java.awt.Color(255, 255, 255));
        changeQuantitybtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/sale/changeQuantity.png"))); // NOI18N
        changeQuantitybtn.setBorderPainted(false);
        changeQuantitybtn.setFocusable(false);
        changeQuantitybtn.setOpaque(true);
        changeQuantitybtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeQuantitybtnActionPerformed(evt);
            }
        });

        removeProductBtn.setBackground(new java.awt.Color(255, 255, 255));
        removeProductBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/product/removeProduct.png"))); // NOI18N
        removeProductBtn.setBorderPainted(false);
        removeProductBtn.setFocusable(false);
        removeProductBtn.setOpaque(true);
        removeProductBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeProductBtnActionPerformed(evt);
            }
        });

        addToBacketBtn.setBackground(new java.awt.Color(255, 255, 255));
        addToBacketBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/sale/addToBasket.png"))); // NOI18N
        addToBacketBtn.setBorderPainted(false);
        addToBacketBtn.setFocusable(false);
        addToBacketBtn.setOpaque(true);
        addToBacketBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addToBacketBtnActionPerformed(evt);
            }
        });

        resumeBtn.setBackground(new java.awt.Color(255, 255, 255));
        resumeBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/sale/resumeToBasket.png"))); // NOI18N
        resumeBtn.setBorderPainted(false);
        resumeBtn.setFocusable(false);
        resumeBtn.setOpaque(true);
        resumeBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resumeBtnActionPerformed(evt);
            }
        });

        searchProductBtn.setBackground(new java.awt.Color(255, 255, 255));
        searchProductBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/product/searchProduct.png"))); // NOI18N
        searchProductBtn.setBorderPainted(false);
        searchProductBtn.setFocusable(false);
        searchProductBtn.setOpaque(true);
        searchProductBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchProductBtnActionPerformed(evt);
            }
        });

        returnProductBtn.setBackground(new java.awt.Color(255, 255, 255));
        returnProductBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/product/returnProduct.png"))); // NOI18N
        returnProductBtn.setBorderPainted(false);
        returnProductBtn.setFocusable(false);
        returnProductBtn.setOpaque(true);
        returnProductBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                returnProductBtnActionPerformed(evt);
            }
        });

        addClientBtn.setBackground(new java.awt.Color(255, 255, 255));
        addClientBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/client/addClient.png"))); // NOI18N
        addClientBtn.setBorderPainted(false);
        addClientBtn.setFocusable(false);
        addClientBtn.setOpaque(true);
        addClientBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addClientBtnActionPerformed(evt);
            }
        });

        searchClientBtn.setBackground(new java.awt.Color(255, 255, 255));
        searchClientBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/client/searchClient.png"))); // NOI18N
        searchClientBtn.setBorderPainted(false);
        searchClientBtn.setFocusable(false);
        searchClientBtn.setOpaque(true);
        searchClientBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchClientBtnActionPerformed(evt);
            }
        });

        totalBtn.setBackground(new java.awt.Color(255, 255, 255));
        totalBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/sale/total.png"))); // NOI18N
        totalBtn.setBorderPainted(false);
        totalBtn.setFocusable(false);
        totalBtn.setOpaque(true);
        totalBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                totalBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addComponent(openBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(exitBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(cancelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(paymentBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(selectBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(changeQuantitybtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(removeProductBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(addToBacketBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(resumeBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(searchProductBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(returnProductBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(addClientBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(searchClientBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(totalBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(searchProductBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(searchClientBtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(addClientBtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(totalBtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(resumeBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(exitBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(paymentBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(openBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(addToBacketBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(cancelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(returnProductBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                        .addComponent(removeProductBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                        .addComponent(changeQuantitybtn, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                        .addComponent(selectBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))))))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jLabel3.setText("Código:");

        codeTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                codeTxtActionPerformed(evt);
            }
        });

        btnReadCode.setBackground(new java.awt.Color(255, 255, 255));
        btnReadCode.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/searchIcon.png.png"))); // NOI18N
        btnReadCode.setFocusable(false);
        btnReadCode.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnReadCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReadCodeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(codeTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 395, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(btnReadCode))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnReadCode, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(codeTxt)
        );

        jPanel7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(166, 201, 226)));

        jLabel4.setFont(new java.awt.Font("Lucida Grande", 1, 15)); // NOI18N
        jLabel4.setText(" Cliente:");
        jLabel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(166, 201, 226)));

        lblNameClient.setFont(new java.awt.Font("Lucida Grande", 1, 15)); // NOI18N
        lblNameClient.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblNameClient.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(166, 201, 226)));

        jLabel6.setFont(new java.awt.Font("Lucida Grande", 1, 30)); // NOI18N
        jLabel6.setText(" Total:");
        jLabel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(166, 201, 226)));

        lblTotal.setFont(new java.awt.Font("Lucida Grande", 1, 60)); // NOI18N
        lblTotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotal.setText("$0 ");
        lblTotal.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(166, 201, 226)));

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblNameClient, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblNameClient, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblTotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        productTable.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(166, 201, 226)));
        productTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Nombre", "Precio de venta", "Cantidad", "Total"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        productTable.setFocusable(false);
        productTable.setRowHeight(35);
        productTable.setSelectionBackground(new java.awt.Color(223, 239, 252));
        productTable.setSelectionForeground(new java.awt.Color(0, 0, 0));
        productTable.setShowGrid(true);
        productTable.setSize(new java.awt.Dimension(1, 1));
        JTableHeader header = productTable.getTableHeader();
        header.setBackground(new Color(223, 239, 252));
        header.setForeground(new Color(46, 110, 158));
        header.setFont(new Font("SansSerif", Font.BOLD, 14));
        Border border = BorderFactory.createLineBorder(new Color(116,201,226), 1);
        header.setBorder(border);
        header.setReorderingAllowed(false);

        TableCellRenderer rendererFromHeader = productTable.getTableHeader().getDefaultRenderer();
        JLabel headerLabel = (JLabel) rendererFromHeader;
        headerLabel.setHorizontalAlignment(JLabel.CENTER);
        header.setPreferredSize(new Dimension(100,30));

        productTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
        productTable.setEnabled(false);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );
        productTable.setDefaultRenderer(String.class, centerRenderer);
        productTable.setShowVerticalLines(true);
        productTable.setShowHorizontalLines(true);
        productTable.getColumnModel().getColumn(1).setCellRenderer(new WordWrapCellRenderer());
        tableScrollPanel.setViewportView(productTable);

        javax.swing.GroupLayout saleJPanelLayout = new javax.swing.GroupLayout(saleJPanel);
        saleJPanel.setLayout(saleJPanelLayout);
        saleJPanelLayout.setHorizontalGroup(
            saleJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, saleJPanelLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(saleJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(tableScrollPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 542, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(22, 22, 22))
        );
        saleJPanelLayout.setVerticalGroup(
            saleJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(saleJPanelLayout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(tableScrollPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 398, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 22, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(saleJPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 543, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(saleJPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnReadCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReadCodeActionPerformed
       readCode();
    }//GEN-LAST:event_btnReadCodeActionPerformed

    private void btnOkOpenCashActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkOpenCashActionPerformed
        openCash();
    }//GEN-LAST:event_btnOkOpenCashActionPerformed

    private void txtOpenCashPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtOpenCashPasswordActionPerformed
        openCash();
    }//GEN-LAST:event_txtOpenCashPasswordActionPerformed

    private void btnOkCancelSaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkCancelSaleActionPerformed
        cancelSale();
    }//GEN-LAST:event_btnOkCancelSaleActionPerformed

    private void txtCancelSalepasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCancelSalepasswordActionPerformed
        cancelSale();
    }//GEN-LAST:event_txtCancelSalepasswordActionPerformed

    private void codeTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_codeTxtActionPerformed
        readCode();
    }//GEN-LAST:event_codeTxtActionPerformed

    private void addClientBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addClientBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_addClientBtnActionPerformed

    private void addToBacketBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addToBacketBtnActionPerformed
        addToBacketView();
        addtoBacketInitValues();
    }//GEN-LAST:event_addToBacketBtnActionPerformed

    private void paymentBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_paymentBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_paymentBtnActionPerformed

    private void cancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBtnActionPerformed
        txtCancelSalepassword.setText("");
        cancelSalePasswordDialog.setVisible(true);
    }//GEN-LAST:event_cancelBtnActionPerformed

    private void exitBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitBtnActionPerformed
        closeView();
    }//GEN-LAST:event_exitBtnActionPerformed

    private void openBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openBtnActionPerformed
        txtOpenCashPassword.setText("");
        openCashPasswordDialog.setVisible(true);
    }//GEN-LAST:event_openBtnActionPerformed

    private void selectBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectBtnActionPerformed
        // TODO add your handling code here:
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth();
        double height = screenSize.getHeight();
        
        System.out.println("width: "+width);
        System.out.println("height: "+height);
        
    }//GEN-LAST:event_selectBtnActionPerformed

    private void changeQuantitybtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changeQuantitybtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_changeQuantitybtnActionPerformed

    private void removeProductBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeProductBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_removeProductBtnActionPerformed

    private void resumeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resumeBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_resumeBtnActionPerformed

    private void searchProductBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchProductBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchProductBtnActionPerformed

    private void returnProductBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_returnProductBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_returnProductBtnActionPerformed

    private void searchClientBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchClientBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchClientBtnActionPerformed

    private void totalBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_totalBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_totalBtnActionPerformed

   public void init()
   {
       
       openCashPasswordDialog.pack();
       cancelSalePasswordDialog.pack();
       loadingDialog.pack();
       openCashPasswordDialog.setLocationRelativeTo(null);
       cancelSalePasswordDialog.setLocationRelativeTo(null);
       loadingDialog.setLocationRelativeTo(null);
       
   }
    
    public void setCashier(User cashier)
    {
        this.cashier = cashier;
        userName.setText(cashier.getUsUserName());
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addClientBtn;
    private javax.swing.JButton addToBacketBtn;
    private javax.swing.JButton btnOkCancelSale;
    private javax.swing.JButton btnOkOpenCash;
    private javax.swing.JButton btnReadCode;
    private javax.swing.JButton cancelBtn;
    private javax.swing.JDialog cancelSalePasswordDialog;
    private javax.swing.JButton changeQuantitybtn;
    private javax.swing.JTextField codeTxt;
    private javax.swing.JButton exitBtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JLabel lblNameClient;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JDialog loadingDialog;
    private javax.swing.JButton openBtn;
    private javax.swing.JDialog openCashPasswordDialog;
    private javax.swing.JButton paymentBtn;
    private javax.swing.JTable productTable;
    private rojeru_san.RSLabelFecha rSLabelFecha2;
    private rojeru_san.RSLabelHora rSLabelHora1;
    private javax.swing.JButton removeProductBtn;
    private javax.swing.JButton resumeBtn;
    private javax.swing.JButton returnProductBtn;
    private javax.swing.JPanel saleJPanel;
    private javax.swing.JButton searchClientBtn;
    private javax.swing.JButton searchProductBtn;
    private javax.swing.JButton selectBtn;
    private javax.swing.JScrollPane tableScrollPanel;
    private javax.swing.JButton totalBtn;
    private javax.swing.JPasswordField txtCancelSalepassword;
    private javax.swing.JPasswordField txtOpenCashPassword;
    private javax.swing.JLabel userName;
    // End of variables declaration//GEN-END:variables

    public void readCode()
    {
        String code = codeTxt.getText();
        codeTxt.setText("");
        if(!code.isEmpty())
        {
          evaluateCode(code);
        }
    }
    
    private void evaluateCode(String code)
    {
        switch(code)
        {
            case "001":
                //purchaseEJB.findTotalEachMonthByYear(2015);
                if(openBtn.isVisible())
                {
                    txtOpenCashPassword.setText("");
                    openCashPasswordDialog.setVisible(true);
                }
                else
                {
                    //openNoAction();
                }
                break;
            case "010":
                if(exitBtn.isVisible())
                {
                    closeView();
                }
                else
                {
                   
                }
                break;
            case "002":
                if (addToBacketBtn.isVisible()) {
                    addToBacketView();
                    addtoBacketInitValues();
                } else {
                    //openNoAction();
                }
                break;
            case "020":
                if(cancelBtn.isVisible())
                {
                    txtCancelSalepassword.setText("");
                    cancelSalePasswordDialog.setVisible(true);
                }
                else
                {
                    //openNoAction();
                }
                break;
            case "003":
                
                initScale();
                /*if(changeQuantityLastProdut)
                {
                    openChangeQuantityProduct();
                }
                else
                {
                    openNoAction();
                }*/
                break;
            case "004":
                /*if(addClient)
                {
                    openAddClient();
                }
                else
                {
                    openNoAction();
                }*/
                break;
            case "030":
                /*if(removeProduct)
                {
                    openRemoveProducto();
                }
                else
                {
                    openNoAction();
                }*/
                break;
            case "005":
                /*if(payment)
                {
                    openPaymentCash();
                }
                else
                {
                    openNoAction();
                }*/
                break;
            case "040":
                /*if(searchProduct)
                {
                    openSearchProduct();
                }
                else
                {
                    openNoAction();
                }*/
                break;
            case "050":
                /*if(searchClient)
                {
                    openSearchClient();
                }
                else
                {
                    openNoAction();
                }*/
                break;
            case "006":
                /*if(selectProduct)
                {
                    openSelectProduct();
                }
                else
                {
                    openNoAction();
                }*/
                break;
            case "007":
                /*if(start)
                {
                    resumeSale(onSesionUserController);
                }
                else
                {
                    openNoAction();
                }*/
                break;
            case "008":
                /*if(start)
                {
                    openCurrentDayTotal();
                }
                else{
                    openNoAction();
                }*/
                    break;
            case "009":
                /*if(selectProduct)
                {
                    openReturnProducto();
                }
                else
                {
                    
                }*/
                break;
            default:
                if(cancelBtn.isVisible())
                {
                    searchAndAddProduct(code,false);
                }
                break;
        }
    }
    
    
    private void searchAndAddProduct(String code,boolean isReturnProduct)
    {
        new SwingWorker<ResultAddProduct, ResultAddProduct>() {
            @Override
            protected ResultAddProduct doInBackground() {
                String c = code;
                if (isReturnProduct) {
                    //c = codeForReturn;
                }
                Product product = productModel.findByBarCode(c);
                if (product != null) {
                    boolean productType = product.getProducttype().getProdtypeValue().equals("Sin empaquetar");
                    if (!productType) {
                        return addProduct(product, productType, isReturnProduct);
                    } else {
                        producWaitForWeight = product;
                        initScale();
                        ResultAddProduct result = readWeight(isReturnProduct);
                        stopScale();
                        return result;
                    }
                } else {
                    ResultAddProduct result = new ResultAddProduct();
                    result.setMessage(AppConstants.Cashier.NO_FOUND_PRODUCT);
                    return result;
                }
            }
            @Override
            protected void done() {
                try {
                    ResultAddProduct result = get();
                    String message = result.getMessage();
                    if(message.equals(AppConstants.Cashier.NO_FOUND_PRODUCT))
                    {
                        //show message toast
                    }
                    else if(message.equals(AppConstants.Cashier.CREATE_PURCHASE_ITEM))
                    {
                        addClientBtn.setVisible(true);
                        tableScrollPanel.setVisible(true);
                        removeProductBtn.setVisible(true);
                        paymentBtn.setVisible(true);
                        changeQuantitybtn.setVisible(!result.isProductType());
                        
                        Purchaseitem purchaseitem = purchaseitems.get(purchaseitems.size()-1);
                        
                        Object[] row = { purchaseitem.getProduct().getProdBarCode(), 
                            purchaseitem.getProduct().getProdName() +" "+purchaseitem.getProduct().getProdUnitValue()+" "+purchaseitem.getProduct().getUnity().getUniAbbreviation(), 
                            Util.getFormatPrice(purchaseitem.getPriceValue()),
                            getQuantity(purchaseitem),
                            getTotal(purchaseitem)};

                        DefaultTableModel model = (DefaultTableModel) productTable.getModel();
                        model.addRow(row);
                        productTable.changeSelection(productTable.getRowCount() - 1, 0, false, false);
                    }
                    else if(message.equals(AppConstants.Cashier.UPDATE_PURCHASE_ITEM) || message.equals(AppConstants.Cashier.NO_DELETE_PURCHASE))
                    {
                        changeQuantitybtn.setVisible(!result.isProductType());
                    }
                    else if(message.equals(AppConstants.Cashier.DELETE_PURCHASE))
                    {
                        addToBacketView();
                        addtoBacketInitValues();
                    }
                    else if(message.equals(AppConstants.Cashier.OPEN_ADD_WEIGHT))
                    {
                        System.out.println("open weight");
                    }
                    
                } catch (InterruptedException | ExecutionException ex) {
                    ex.printStackTrace();
                }
                loadingDialog.setVisible(false);
            }

        }.execute();
        loadingDialog.setVisible(true);
    }
    
    private ResultAddProduct addProduct(Product product,boolean productType,boolean isReturnProduct)
    {
        ResultAddProduct result = new ResultAddProduct();
        result.setProductType(productType);
        Price price = priceModel.findCurrentByProdId(product.getProdId());
        Pricepurchase pricepurchase = pricePurchaseModel.findCurrentByProdId(product.getProdId());
        if (purchaseitems == null || purchaseitems.isEmpty()) {
            purchase = new Purchase();
            purchase.setPurDate(new Date());
            purchase.setUser(cashier);
            purchase.setPurState(0);
            purchaseModel.create(purchase);
            purchaseitems = new ArrayList<>();
        }
        if (!purchaseitems.isEmpty() && product.getProdId().equals(purchaseitems.get(purchaseitems.size() - 1).getProduct().getProdId())) {
            int q = 1;
            if(isReturnProduct)
            {
                q = -1;
            }
            if(productType)
            {
                q = getSimulatedBalance()*q;

            }
            purchaseitems.get(purchaseitems.size() - 1).
                    setPurItemQuantity(purchaseitems.get(purchaseitems.size() - 1).
                    getPurItemQuantity() +q);
            if(purchaseitems.get(purchaseitems.size() - 1).getPurItemQuantity()==0)
            {
                purchaseitemModel.delete(purchaseitems.get(purchaseitems.size() - 1));
                purchaseitems.remove(purchaseitems.size() - 1);
                if(purchaseitems.isEmpty())
                {
                    purchaseModel.delete(purchase);
                    purchaseitems = null;
                    purchase = null;
                    result.setMessage(AppConstants.Cashier.DELETE_PURCHASE);
                    return result;
                }
                result.setProductType(purchaseitems.get(purchaseitems.size() - 1).getProduct().getProducttype().getProdtypeValue().equals("Sin empaquetar"));
                result.setMessage(AppConstants.Cashier.NO_DELETE_PURCHASE);
                return result;
            }
            else{
                purchaseitemModel.update(purchaseitems.get(purchaseitems.size() - 1));
                result.setMessage(AppConstants.Cashier.UPDATE_PURCHASE_ITEM);
                return result;
            }
        }
        else
        {
            Purchaseitem purchaseitem = new Purchaseitem();
            purchaseitem.setProduct(product);
            int factor = 1;
            if(isReturnProduct)
            {
               factor = -1;
            }
            if(productType)
            {
                purchaseitem.setPurItemQuantity(getSimulatedBalance()*factor);
            }
            else
            {
                purchaseitem.setPurItemQuantity(factor);
            }
            purchaseitem.setPriceValue(price.getPriceValue());
            if(pricepurchase!=null){
                purchaseitem.setPricePurValue(pricepurchase.getPricePurValue());
            }
            else{
                purchaseitem.setPricePurValue(price.getPriceValue());
            }
            purchaseitem.setIva(product.getProdIva());
            purchaseitems.add(purchaseitem);
            purchaseitem.setPurchase(purchase);
            purchaseitem.setOwner(product.getOwner());
            purchaseitemModel.create(purchaseitem);
            result.setMessage(AppConstants.Cashier.CREATE_PURCHASE_ITEM);
            return result;
        }
    }
    
    
    public void initScale()
    {
        if(cashInfo==null)
        {
            JSONParser parser = new JSONParser();
            
            try {
                JSONObject jSONObject = (JSONObject)parser.parse(new FileReader(Util.CASHINFO));
                cashInfo = new CashInfo();
                cashInfo.setCashName((String)jSONObject.get("Name"));
                cashInfo.setCashIp((String)jSONObject.get("cashIP"));
                cashInfo.setCashPaperSize(Integer.parseInt(jSONObject.get("cashPaperSize")+""));
                cashInfo.setCashPrintName((String)jSONObject.get("cashPrintName"));
                cashInfo.setCashScalePortSerialName((String)jSONObject.get("cashScalePortSerialName"));
                cashInfo.setCashPrintCommandOpenCashDrawer((String)jSONObject.get("cashPrintCommandOpenCashDrawer"));
            } catch (IOException | ParseException ex) {
                ex.printStackTrace();
            }
        }
        
        if(scale!=null){
            scale.stop();
        }
        
        if(listScaleIp==null)
        {
            listScaleIp = new ArrayList<>();
        }
        else if(!listScaleIp.isEmpty()){
            for(int i = listScaleIp.size() - 1 ; i>=0; i--)
            {
                ScaleIP scaleIP = listScaleIp.get(i);
                if(scaleIP.getIp().equals(cashInfo.getCashIp()))
                {
                    scaleIP.getScale().stop();
                    listScaleIp.remove(scaleIP);
                }
            }
        }
        String scalePortSerialName = cashInfo.getCashScalePortSerialName();
        scale = new Scale(scalePortSerialName);
        Util.logInformation(TAG, "initScale", "scalePortSerialName: "+ scalePortSerialName);
        boolean scaleStar = false;
        try{
            scaleStar = scale.start();
        }catch(Exception e)
        {
            e.printStackTrace();
            
        }
        
        if(scaleStar)
        {
            Util.logInformation(TAG, "initScale", "start true");
            ScaleIP scaleIP = new ScaleIP(cashInfo.getCashIp(), scale);
            listScaleIp.add(scaleIP);
        }
        else
        {
            Util.logInformation(TAG, "initScale", "start false");
        }
    }
    
    public void stopScale()
    {
        if(scale!=null){
            scale.stop();
            scale = null;
        }
    }
    
    
    
    public ResultAddProduct readWeight(boolean isReturnProduct)
    {
        Calendar c = Calendar.getInstance();
        long initTime = c.getTimeInMillis();
        if (scale != null) {
            scale.setWeight("0");
            while(scale.getWeight().equals("0")) {
                c = Calendar.getInstance();
                long currentTime = c.getTimeInMillis();
                if(currentTime>(initTime + 4000l))
                {
                    break;
                }
            }
            if(scale.getWeight().equals("0"))
            {
                Util.logInformation(TAG, "readWeight", "scale.getWeight == 0, call openAddWeight()");
                ResultAddProduct resultAddProduct = new ResultAddProduct();
                resultAddProduct.setMessage(AppConstants.Cashier.OPEN_ADD_WEIGHT);
                isReturnProductWeight = isReturnProduct;
                return resultAddProduct;
            }else{
                weight = scale.getWeight();
                Util.logInformation(TAG, "readWeight", "scale.getWeight sucessfull, Weight = weight");
                return addProduct(producWaitForWeight, true,isReturnProduct);
            }
        } else
        {
            Util.logInformation(TAG, "readWeight", "scale is null, call openAddWeight()");
            ResultAddProduct resultAddProduct = new ResultAddProduct();
            resultAddProduct.setMessage(AppConstants.Cashier.OPEN_ADD_WEIGHT);
            isReturnProductWeight = isReturnProduct;
            return resultAddProduct;
        }
    }
    
    public int getSimulatedBalance()
    {
        return Integer.parseInt(weight);
    }
    
    public String getQuantity(Purchaseitem purchaseitem)
    {
        if(purchaseitem.getProduct().getProducttype().getProdtypeValue().equals("Sin empaquetar"))
        {
           String unity = purchaseitem.getProduct().getUnity().getUniAbbreviation();
           switch(unity)
           { 
               case "gr":
                   double fv = purchaseitem.getPurItemQuantity() / 1000.0;
                   return String.format("%.3f", fv);
                   
           }
        }
        return purchaseitem.getPurItemQuantity()+"";
    }
    
    public String getTotal(Purchaseitem purchaseitem)
    {
        if(purchaseitem.getProduct().getProducttype().getProdtypeValue().equals("Sin empaquetar"))
        {
           String unity = purchaseitem.getProduct().getUnity().getUniAbbreviation();
           switch(unity)
           { 
               case "gr":
                   double fv = purchaseitem.getPurItemQuantity()/1000.0;
                   fv = fv * purchaseitem.getPriceValue();
                   long round = Math.round(fv);
                   return Util.getFormatPrice(round);
                   
           }
        }
        return Util.getFormatPrice(purchaseitem.getPurItemQuantity() * purchaseitem.getPriceValue());
    }
}
