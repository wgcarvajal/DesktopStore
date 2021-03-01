/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package desktopstore;

import desktopstore.util.AppConstants;
import desktopstore.util.CashInfo;
import desktopstore.util.Encrypt;
import desktopstore.util.GeneratePdf;
import desktopstore.util.PrintPdf;
import desktopstore.util.ResultAddProduct;
import desktopstore.util.Scale;
import desktopstore.util.ScaleIP;
import desktopstore.util.Util;
import desktopstore.util.WordWrapCellRenderer;
import entities.Brand;
import entities.Cancelpurchaseauditorie;
import entities.Category;
import entities.Client;
import entities.Owner;
import entities.Price;
import entities.Pricepurchase;
import entities.Product;
import entities.Purchase;
import entities.Purchaseitem;
import entities.Purchasetotal;
import entities.Unity;
import entities.User;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingWorker;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import model.CancelpurchaseauditorieModel;
import model.ClientModel;
import model.PriceModel;
import model.PricePurchaseModel;
import model.ProductModel;
import model.PurchaseModel;
import model.PurchaseitemModel;
import model.PurchasetotalModel;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author aranda
 */
public class CashForm extends javax.swing.JFrame {
    
    private final String TAG = "CashForm";
    
    private PurchaseModel purchaseModel;
    private ProductModel productModel;
    private PriceModel priceModel;
    private PricePurchaseModel pricePurchaseModel;
    private PurchaseitemModel purchaseitemModel;
    private CancelpurchaseauditorieModel cancelpurchaseauditorieModel;
    private ClientModel clientModel;
    private PurchasetotalModel purchasetotalModel;
    private ImageIcon loadingIcon;
    private ImageIcon searchIcon;
    
    private static List<ScaleIP>listScaleIp;
    private Scale scale;
    private boolean isReturnProductWeight;
    private String weight;
    private String rAmount;
    private String refund;
    private String receivedAmount;
    private String total;
    private long totalProductIva0;
    private long totalProductIva5;
    private long totalProductIva19;
    private GeneratePdf generatePdf;
    
    private List<Purchaseitem> purchaseitems;
    private List<Purchase> purchasesResume;
    private List<Purchasetotal> purchasetotals;
    private List productList;
    private Purchase purchase;
    private User cashier;
    private Product producWaitForWeight;
    private CashInfo cashInfo;

    /**
     * Creates new form CashForm
     */
    public CashForm() {
        initComponents();
        productModel = new ProductModel();
        priceModel = new PriceModel();
        pricePurchaseModel = new PricePurchaseModel();
        purchaseModel = new PurchaseModel();
        purchaseitemModel = new PurchaseitemModel();
        cancelpurchaseauditorieModel = new CancelpurchaseauditorieModel();
        clientModel = new ClientModel();
        purchasetotalModel = new PurchasetotalModel();
    }
    
    public void init()
    {
       loadingIcon = new ImageIcon(getClass().getResource("/resources/carga.gif"));
       openCashPasswordDialog.pack();
       cancelSalePasswordDialog.pack();
       noActionDialog.pack();
       changeQuatityDialog.pack();
       removeProductDialog.pack();
       returnProductDialog.pack();
       resumeDialog.pack();
       totalDialog.pack();
       addClientDialog.pack();
       receivedAmountDialog.pack();
       endPaymentDialog.pack();
       addWeightDialog.pack();
       openCashPasswordDialog.setLocationRelativeTo(null);
       cancelSalePasswordDialog.setLocationRelativeTo(null);
       noActionDialog.setLocationRelativeTo(null);
       changeQuatityDialog.setLocationRelativeTo(null);
       removeProductDialog.setLocationRelativeTo(null);
       returnProductDialog.setLocationRelativeTo(null);
       selectProductDialog.setLocationRelativeTo(null);
       resumeDialog.setLocationRelativeTo(null);
       endPaymentDialog.setLocationRelativeTo(null);
       addWeightDialog.setLocationRelativeTo(null);
       
       Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
       int width = (int)(screenSize.getWidth()/2);
       buttonsAndSearchPanel.setPreferredSize(new Dimension(width-6, 0));
       infoSalePanel.setPreferredSize(new Dimension(width-12, 0));
       
       selectProductDialog.setPreferredSize(new Dimension((int)screenSize.getWidth(),selectProductDialog.getPreferredSize().height));
       successfulPaymentDialog.setUndecorated(true);
       selectProductDialog.pack();
       successfulPaymentDialog.pack();
       
       clientAndTotalPanel.setVisible(false);
       scrollProductTable.setVisible(false);
       
       openBtn = new JButton();
       openBtn.setBackground(new java.awt.Color(255, 255, 255));
       openBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/sale/openDoor.png"))); // NOI18N
       openBtn.setBorderPainted(false);
       openBtn.setFocusable(false);
       openBtn.setOpaque(true);
       
       exitBtn = new JButton();
       exitBtn.setBackground(new java.awt.Color(255, 255, 255));
       exitBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/sale/exitDoor.png"))); // NOI18N
       exitBtn.setBorderPainted(false);
       exitBtn.setFocusable(false);
       exitBtn.setOpaque(true);
       exitBtn.setVisible(false);
       
       cancelBtn = new JButton();
       cancelBtn.setBackground(new java.awt.Color(255, 255, 255));
       cancelBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/sale/cancelar.png"))); // NOI18N
       cancelBtn.setBorderPainted(false);
       cancelBtn.setFocusable(false);
       cancelBtn.setOpaque(true);
       cancelBtn.setVisible(false);
       
       paymentBtn = new JButton();
       paymentBtn.setBackground(new java.awt.Color(255, 255, 255));
       paymentBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/sale/payment.png"))); // NOI18N
       paymentBtn.setBorderPainted(false);
       paymentBtn.setFocusable(false);
       paymentBtn.setOpaque(true);
       paymentBtn.setVisible(false);
       
       selectBtn = new JButton();
       selectBtn.setBackground(new java.awt.Color(255, 255, 255));
       selectBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/product/selectProduct.png"))); // NOI18N
       selectBtn.setBorderPainted(false);
       selectBtn.setFocusable(false);
       selectBtn.setOpaque(true);
       selectBtn.setVisible(false);
       
       changeQuantitybtn = new JButton();
       changeQuantitybtn.setBackground(new java.awt.Color(255, 255, 255));
       changeQuantitybtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/sale/changeQuantity.png"))); // NOI18N
       changeQuantitybtn.setBorderPainted(false);
       changeQuantitybtn.setFocusable(false);
       changeQuantitybtn.setOpaque(true);
       changeQuantitybtn.setVisible(false);
       
       removeProductBtn = new JButton();
       removeProductBtn.setBackground(new java.awt.Color(255, 255, 255));
       removeProductBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/product/removeProduct.png"))); // NOI18N
       removeProductBtn.setBorderPainted(false);
       removeProductBtn.setFocusable(false);
       removeProductBtn.setOpaque(true);
       removeProductBtn.setVisible(false);
       
       addToBacketBtn = new JButton();
       addToBacketBtn.setBackground(new java.awt.Color(255, 255, 255));
       addToBacketBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/sale/addToBasket.png"))); // NOI18N
       addToBacketBtn.setBorderPainted(false);
       addToBacketBtn.setFocusable(false);
       addToBacketBtn.setOpaque(true);
       addToBacketBtn.setVisible(false);
       
       resumeBtn = new JButton();
       resumeBtn.setBackground(new java.awt.Color(255, 255, 255));
       resumeBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/sale/resumeToBasket.png"))); // NOI18N
       resumeBtn.setBorderPainted(false);
       resumeBtn.setFocusable(false);
       resumeBtn.setOpaque(true);
       resumeBtn.setVisible(false);
       
       searchProductBtn = new JButton();
       searchProductBtn.setBackground(new java.awt.Color(255, 255, 255));
       searchProductBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/product/searchProduct.png"))); // NOI18N
       searchProductBtn.setBorderPainted(false);
       searchProductBtn.setFocusable(false);
       searchProductBtn.setOpaque(true);
       searchProductBtn.setVisible(false);
       
       returnProductBtn = new JButton();
       returnProductBtn.setBackground(new java.awt.Color(255, 255, 255));
       returnProductBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/product/returnProduct.png"))); // NOI18N
       returnProductBtn.setBorderPainted(false);
       returnProductBtn.setFocusable(false);
       returnProductBtn.setOpaque(true);
       returnProductBtn.setVisible(false);
       
       addClientBtn = new JButton();
       addClientBtn.setBackground(new java.awt.Color(255, 255, 255));
       addClientBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/client/addClient.png"))); // NOI18N
       addClientBtn.setBorderPainted(false);
       addClientBtn.setFocusable(false);
       addClientBtn.setOpaque(true);
       addClientBtn.setVisible(false);
       
       searchClientBtn = new JButton();
       searchClientBtn.setBackground(new java.awt.Color(255, 255, 255));
       searchClientBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/client/searchClient.png"))); // NOI18N
       searchClientBtn.setBorderPainted(false);
       searchClientBtn.setFocusable(false);
       searchClientBtn.setOpaque(true);
       searchClientBtn.setVisible(false);
       
       totalBtn = new JButton();
       totalBtn.setBackground(new java.awt.Color(255, 255, 255));
       totalBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/sale/total.png"))); // NOI18N
       totalBtn.setBorderPainted(false);
       totalBtn.setFocusable(false);
       totalBtn.setOpaque(true);
       totalBtn.setVisible(false);
       
       openBtn.addActionListener((ActionEvent e) -> {
           openBtnActionPerformed(e);
       });
       
       exitBtn.addActionListener((ActionEvent e) -> {
           exitBtnActionPerformed(e);
       });
       
       cancelBtn.addActionListener((ActionEvent e) -> {
           cancelBtnActionPerformed(e);
       });
       
       paymentBtn.addActionListener((ActionEvent e) -> {
           paymentBtnActionPerformed(e);
       });
       
       selectBtn.addActionListener((ActionEvent e) -> {
           selectBtnActionPerformed(e);
       });
       
       changeQuantitybtn.addActionListener((ActionEvent e) -> {
           changeQuantitybtnActionPerformed(e);
       });
       
       removeProductBtn.addActionListener((ActionEvent e) -> {
           removeProductBtnActionPerformed(e);
       });
       
       addToBacketBtn.addActionListener((ActionEvent e) -> {
           addToBacketBtnActionPerformed(e);
       });
       
       resumeBtn.addActionListener((ActionEvent e) -> {
           resumeBtnActionPerformed(e);
       });
       
       searchProductBtn.addActionListener((ActionEvent e) -> {
           searchProductBtnActionPerformed(e);
       });
       
       returnProductBtn.addActionListener((ActionEvent e) -> {
           returnProductBtnActionPerformed(e);
       });
       
       addClientBtn.addActionListener((ActionEvent e) -> {
           addClientBtnActionPerformed(e);
       });
       
       searchClientBtn.addActionListener((ActionEvent e) -> {
           searchClientBtnActionPerformed(e);
       });
       
       totalBtn.addActionListener((ActionEvent e) -> {
           totalBtnActionPerformed(e);
       });
       
       GroupLayout jPanel5Layout = new javax.swing.GroupLayout(butonsPanel);
        butonsPanel.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addComponent(openBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                )
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(openBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                ))
        );
   }
    
    public void setCashier(User cashier)
    {
        this.cashier = cashier;
        userName.setText(cashier.getUsUserName());
    }
    
    public void openCash()
    {
        startLoading();
        openCashPasswordDialog.setVisible(false);
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
                stopLoading();
                try {
                    boolean result = get();
                    if(result)
                    {
                        openCashView();
                        openCashPasswordDialog.dispose();
                    }
                    else{
                        openCashPasswordDialog.setVisible(true);
                    }
                } catch (InterruptedException | ExecutionException ex) {
                   ex.printStackTrace();
                }
            }
        }.execute();
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
        jPanel9 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        txtOpenCashPassword = new javax.swing.JPasswordField();
        jPanel10 = new javax.swing.JPanel();
        btnOkOpenCash = new javax.swing.JButton();
        cancelSalePasswordDialog = new javax.swing.JDialog();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtCancelSalepassword = new javax.swing.JPasswordField();
        jPanel11 = new javax.swing.JPanel();
        btnOkCancelSale = new javax.swing.JButton();
        noActionDialog = new javax.swing.JDialog();
        jLabel6 = new javax.swing.JLabel();
        btnOkNoAction = new javax.swing.JButton();
        changeQuatityDialog = new javax.swing.JDialog();
        jLabel10 = new javax.swing.JLabel();
        txtChangeQuantityDialog = new javax.swing.JTextField();
        btnOkChangeQuantityDialog = new javax.swing.JButton();
        removeProductDialog = new javax.swing.JDialog();
        jLabel11 = new javax.swing.JLabel();
        txtBarCodeRemoveProductDialog = new javax.swing.JTextField();
        btnOkRemoveProductDialog = new javax.swing.JButton();
        returnProductDialog = new javax.swing.JDialog();
        jLabel12 = new javax.swing.JLabel();
        txtReturnProductDialog = new javax.swing.JTextField();
        btnOkReturnProductDialog = new javax.swing.JButton();
        selectProductDialog = new javax.swing.JDialog();
        scrollSelectProductDialogTable = new javax.swing.JScrollPane();
        selectProductDialogTable = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        txtSelectProductDialog = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        resumeDialog = new javax.swing.JDialog();
        jScrollPane1 = new javax.swing.JScrollPane();
        resumeDialogTable = new javax.swing.JTable();
        btnResumeDialog = new javax.swing.JButton();
        totalDialog = new javax.swing.JDialog();
        lblTotalDialog = new javax.swing.JLabel();
        btnTotalDialog = new javax.swing.JButton();
        addClientDialog = new javax.swing.JDialog();
        jLabel14 = new javax.swing.JLabel();
        txtAddClientDialog = new javax.swing.JTextField();
        btnAddClientDialog = new javax.swing.JButton();
        receivedAmountDialog = new javax.swing.JDialog();
        jLabel15 = new javax.swing.JLabel();
        txtReceivedAmountDialog = new javax.swing.JTextField();
        btnReceivedAmountDialog = new javax.swing.JButton();
        successfulPaymentDialog = new javax.swing.JDialog();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        txtSuccessFullPaymentDialog = new javax.swing.JTextField();
        btnSuccessfullPaymentDialog = new javax.swing.JButton();
        endPaymentDialog = new javax.swing.JDialog();
        jLabel19 = new javax.swing.JLabel();
        lblTotalEndPaymentDialog = new javax.swing.JLabel();
        lblQuantityEndPaymentDialog = new javax.swing.JLabel();
        lblReturnEndPaymentDialog = new javax.swing.JLabel();
        btnOkEndPaymentDialog = new javax.swing.JButton();
        addWeightDialog = new javax.swing.JDialog();
        jLabel8 = new javax.swing.JLabel();
        txtAddWeightDialog = new javax.swing.JTextField();
        btnOkAddWeightDialog = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        rSLabelFecha2 = new rojeru_san.RSLabelFecha();
        jPanel2 = new javax.swing.JPanel();
        rSLabelHora1 = new rojeru_san.RSLabelHora();
        jPanel4 = new javax.swing.JPanel();
        userName = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        buttonsAndSearchPanel = new javax.swing.JPanel();
        butonsPanel = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        codeTxt = new javax.swing.JTextField();
        btnReadCode = new javax.swing.JButton();
        infoSalePanel = new javax.swing.JPanel();
        clientAndTotalPanel = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        lblNameClient = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        lblTotal = new javax.swing.JLabel();
        scrollProductTable = new javax.swing.JScrollPane();
        productTable = new javax.swing.JTable();
        jPanel7 = new javax.swing.JPanel();

        openCashPasswordDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        openCashPasswordDialog.setModal(true);

        jLabel9.setText("Contraseña:");

        txtOpenCashPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtOpenCashPasswordActionPerformed(evt);
            }
        });
        txtOpenCashPassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtOpenCashPasswordKeyPressed(evt);
            }
        });

        btnOkOpenCash.setText("Aceptar");
        btnOkOpenCash.setFocusable(false);
        btnOkOpenCash.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkOpenCashActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(btnOkOpenCash)
                .addContainerGap(26, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnOkOpenCash, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
        );

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtOpenCashPassword, javax.swing.GroupLayout.DEFAULT_SIZE, 291, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtOpenCashPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout openCashPasswordDialogLayout = new javax.swing.GroupLayout(openCashPasswordDialog.getContentPane());
        openCashPasswordDialog.getContentPane().setLayout(openCashPasswordDialogLayout);
        openCashPasswordDialogLayout.setHorizontalGroup(
            openCashPasswordDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        openCashPasswordDialogLayout.setVerticalGroup(
            openCashPasswordDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        cancelSalePasswordDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
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
        txtCancelSalepassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCancelSalepasswordKeyPressed(evt);
            }
        });

        btnOkCancelSale.setText("Aceptar");
        btnOkCancelSale.setFocusable(false);
        btnOkCancelSale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkCancelSaleActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnOkCancelSale, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(7, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnOkCancelSale, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

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
                        .addComponent(txtCancelSalepassword, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(22, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cancelSalePasswordDialogLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(137, 137, 137))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        noActionDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        noActionDialog.setModal(true);

        jLabel6.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("No puedes realizar esta acción");

        btnOkNoAction.setText("Aceptar");
        btnOkNoAction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkNoActionActionPerformed(evt);
            }
        });
        btnOkNoAction.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnOkNoActionKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout noActionDialogLayout = new javax.swing.GroupLayout(noActionDialog.getContentPane());
        noActionDialog.getContentPane().setLayout(noActionDialogLayout);
        noActionDialogLayout.setHorizontalGroup(
            noActionDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
            .addGroup(noActionDialogLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnOkNoAction)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        noActionDialogLayout.setVerticalGroup(
            noActionDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(noActionDialogLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnOkNoAction, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
                .addContainerGap())
        );

        changeQuatityDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        changeQuatityDialog.setTitle("Cambiar la cantidad");
        changeQuatityDialog.setModal(true);

        jLabel10.setFont(new java.awt.Font("Lucida Grande", 1, 16)); // NOI18N
        jLabel10.setText("Cantidad:");

        txtChangeQuantityDialog.setFont(new java.awt.Font("Lucida Grande", 0, 24)); // NOI18N
        txtChangeQuantityDialog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtChangeQuantityDialogActionPerformed(evt);
            }
        });
        txtChangeQuantityDialog.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtChangeQuantityDialogKeyPressed(evt);
            }
        });

        btnOkChangeQuantityDialog.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        btnOkChangeQuantityDialog.setText("Aceptar");
        btnOkChangeQuantityDialog.setFocusable(false);
        btnOkChangeQuantityDialog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkChangeQuantityDialogActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout changeQuatityDialogLayout = new javax.swing.GroupLayout(changeQuatityDialog.getContentPane());
        changeQuatityDialog.getContentPane().setLayout(changeQuatityDialogLayout);
        changeQuatityDialogLayout.setHorizontalGroup(
            changeQuatityDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(changeQuatityDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtChangeQuantityDialog, javax.swing.GroupLayout.PREFERRED_SIZE, 377, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(17, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, changeQuatityDialogLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnOkChangeQuantityDialog, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        changeQuatityDialogLayout.setVerticalGroup(
            changeQuatityDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(changeQuatityDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(changeQuatityDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtChangeQuantityDialog, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnOkChangeQuantityDialog, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        removeProductDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        removeProductDialog.setTitle("Quitar producto");
        removeProductDialog.setModal(true);

        jLabel11.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        jLabel11.setText("Código de barras:");

        txtBarCodeRemoveProductDialog.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        txtBarCodeRemoveProductDialog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBarCodeRemoveProductDialogActionPerformed(evt);
            }
        });
        txtBarCodeRemoveProductDialog.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBarCodeRemoveProductDialogKeyPressed(evt);
            }
        });

        btnOkRemoveProductDialog.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        btnOkRemoveProductDialog.setText("Aceptar");
        btnOkRemoveProductDialog.setFocusable(false);
        btnOkRemoveProductDialog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkRemoveProductDialogActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout removeProductDialogLayout = new javax.swing.GroupLayout(removeProductDialog.getContentPane());
        removeProductDialog.getContentPane().setLayout(removeProductDialogLayout);
        removeProductDialogLayout.setHorizontalGroup(
            removeProductDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(removeProductDialogLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtBarCodeRemoveProductDialog, javax.swing.GroupLayout.DEFAULT_SIZE, 466, Short.MAX_VALUE)
                .addGap(12, 12, 12))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, removeProductDialogLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnOkRemoveProductDialog, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        removeProductDialogLayout.setVerticalGroup(
            removeProductDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(removeProductDialogLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(removeProductDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtBarCodeRemoveProductDialog, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnOkRemoveProductDialog, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        returnProductDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        returnProductDialog.setTitle("Devolución de prodcuto");
        returnProductDialog.setModal(true);

        jLabel12.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        jLabel12.setText("Código de barras:");

        txtReturnProductDialog.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        txtReturnProductDialog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtReturnProductDialogActionPerformed(evt);
            }
        });
        txtReturnProductDialog.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtReturnProductDialogKeyPressed(evt);
            }
        });

        btnOkReturnProductDialog.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        btnOkReturnProductDialog.setText("Aceptar");
        btnOkReturnProductDialog.setFocusable(false);
        btnOkReturnProductDialog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkReturnProductDialogActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout returnProductDialogLayout = new javax.swing.GroupLayout(returnProductDialog.getContentPane());
        returnProductDialog.getContentPane().setLayout(returnProductDialogLayout);
        returnProductDialogLayout.setHorizontalGroup(
            returnProductDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(returnProductDialogLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtReturnProductDialog, javax.swing.GroupLayout.DEFAULT_SIZE, 466, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(returnProductDialogLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnOkReturnProductDialog, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        returnProductDialogLayout.setVerticalGroup(
            returnProductDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(returnProductDialogLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(returnProductDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtReturnProductDialog, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnOkReturnProductDialog, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        selectProductDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        selectProductDialog.setTitle("Seleccione producto");
        selectProductDialog.setModal(true);
        selectProductDialog.setResizable(false);

        selectProductDialogTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código de barras", "Nombre", "Presentación", "Categoria", "Marca", "Precio"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            @Override
            public boolean isCellEditable(int row, int column) {

                DefaultTableModel model = (DefaultTableModel) selectProductDialogTable.getModel();
                String barcode = (String)model.getValueAt(row, 0);
                searchAndAddProduct(barcode, false);
                selectProductDialog.dispose();
                return false;
            }

        });
        selectProductDialogTable.setFocusable(false);
        selectProductDialogTable.setRowHeight(35);
        selectProductDialogTable.setSelectionBackground(new java.awt.Color(223, 239, 252));
        selectProductDialogTable.setSelectionForeground(new java.awt.Color(0, 0, 0));
        JTableHeader header2 = selectProductDialogTable.getTableHeader();
        header2.setBackground(new Color(223, 239, 252));
        header2.setForeground(new Color(46, 110, 158));
        header2.setFont(new Font("SansSerif", Font.BOLD, 14));
        Border border2 = BorderFactory.createLineBorder(new Color(116,201,226), 1);
        header2.setBorder(border2);
        header2.setReorderingAllowed(false);

        TableCellRenderer rendererFromHeader2 = selectProductDialogTable.getTableHeader().getDefaultRenderer();
        JLabel headerLabel2= (JLabel) rendererFromHeader2;
        headerLabel2.setHorizontalAlignment(JLabel.CENTER);
        headerLabel2.setPreferredSize(new Dimension(100,30));
        selectProductDialogTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
        scrollSelectProductDialogTable.setViewportView(selectProductDialogTable);

        txtSelectProductDialog.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        txtSelectProductDialog.setToolTipText("");
        txtSelectProductDialog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSelectProductDialogActionPerformed(evt);
            }
        });
        txtSelectProductDialog.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSelectProductDialogKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSelectProductDialogKeyReleased(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        jLabel13.setText("Buscar nombre:");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtSelectProductDialog, javax.swing.GroupLayout.DEFAULT_SIZE, 740, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(30, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSelectProductDialog, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22))
        );

        javax.swing.GroupLayout selectProductDialogLayout = new javax.swing.GroupLayout(selectProductDialog.getContentPane());
        selectProductDialog.getContentPane().setLayout(selectProductDialogLayout);
        selectProductDialogLayout.setHorizontalGroup(
            selectProductDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(selectProductDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(selectProductDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrollSelectProductDialogTable)
                    .addGroup(selectProductDialogLayout.createSequentialGroup()
                        .addGap(0, 287, Short.MAX_VALUE)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 457, Short.MAX_VALUE)))
                .addContainerGap())
        );
        selectProductDialogLayout.setVerticalGroup(
            selectProductDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, selectProductDialogLayout.createSequentialGroup()
                .addContainerGap(15, Short.MAX_VALUE)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, Short.MAX_VALUE)
                .addComponent(scrollSelectProductDialogTable, javax.swing.GroupLayout.PREFERRED_SIZE, 351, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(108, 108, 108))
        );

        resumeDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        resumeDialog.setTitle("Reanudar venta");
        resumeDialog.setModal(true);

        resumeDialogTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Fecha"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            @Override
            public boolean isCellEditable(int row, int column) {

                resumePurchase(row);
                return false;
            }
        });
        resumeDialogTable.setFocusable(false);
        resumeDialogTable.setRowHeight(35);
        JTableHeader header3 = resumeDialogTable.getTableHeader();
        header3.setBackground(new Color(223, 239, 252));
        header3.setForeground(new Color(46, 110, 158));
        header3.setFont(new Font("SansSerif", Font.BOLD, 14));
        Border border3 = BorderFactory.createLineBorder(new Color(116,201,226), 1);
        header3.setBorder(border3);
        header3.setReorderingAllowed(false);

        TableCellRenderer rendererFromHeader3 = resumeDialogTable.getTableHeader().getDefaultRenderer();
        JLabel headerLabel3= (JLabel) rendererFromHeader3;
        headerLabel3.setHorizontalAlignment(JLabel.CENTER);
        headerLabel3.setPreferredSize(new Dimension(100,30));
        resumeDialogTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
        jScrollPane1.setViewportView(resumeDialogTable);

        btnResumeDialog.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        btnResumeDialog.setText("Cerrar");
        btnResumeDialog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResumeDialogActionPerformed(evt);
            }
        });
        btnResumeDialog.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnResumeDialogKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout resumeDialogLayout = new javax.swing.GroupLayout(resumeDialog.getContentPane());
        resumeDialog.getContentPane().setLayout(resumeDialogLayout);
        resumeDialogLayout.setHorizontalGroup(
            resumeDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(resumeDialogLayout.createSequentialGroup()
                .addContainerGap(69, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 528, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(70, Short.MAX_VALUE))
            .addGroup(resumeDialogLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnResumeDialog, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        resumeDialogLayout.setVerticalGroup(
            resumeDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(resumeDialogLayout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnResumeDialog, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        totalDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        totalDialog.setTitle("Total del dia");
        totalDialog.setModal(true);

        lblTotalDialog.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        lblTotalDialog.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        btnTotalDialog.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        btnTotalDialog.setText("Aceptar");
        btnTotalDialog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTotalDialogActionPerformed(evt);
            }
        });
        btnTotalDialog.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnTotalDialogKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout totalDialogLayout = new javax.swing.GroupLayout(totalDialog.getContentPane());
        totalDialog.getContentPane().setLayout(totalDialogLayout);
        totalDialogLayout.setHorizontalGroup(
            totalDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(totalDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTotalDialog, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(totalDialogLayout.createSequentialGroup()
                .addContainerGap(160, Short.MAX_VALUE)
                .addComponent(btnTotalDialog, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(172, Short.MAX_VALUE))
        );
        totalDialogLayout.setVerticalGroup(
            totalDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(totalDialogLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(lblTotalDialog, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnTotalDialog, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
                .addContainerGap())
        );

        addClientDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addClientDialog.setTitle("Agregar cliente");
        addClientDialog.setModal(true);

        jLabel14.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        jLabel14.setText("Número de identificación:");

        txtAddClientDialog.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        txtAddClientDialog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAddClientDialogActionPerformed(evt);
            }
        });
        txtAddClientDialog.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtAddClientDialogKeyPressed(evt);
            }
        });

        btnAddClientDialog.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        btnAddClientDialog.setText("Aceptar");
        btnAddClientDialog.setFocusable(false);
        btnAddClientDialog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddClientDialogActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout addClientDialogLayout = new javax.swing.GroupLayout(addClientDialog.getContentPane());
        addClientDialog.getContentPane().setLayout(addClientDialogLayout);
        addClientDialogLayout.setHorizontalGroup(
            addClientDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addClientDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(addClientDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(addClientDialogLayout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtAddClientDialog, javax.swing.GroupLayout.DEFAULT_SIZE, 411, Short.MAX_VALUE))
                    .addGroup(addClientDialogLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnAddClientDialog, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        addClientDialogLayout.setVerticalGroup(
            addClientDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addClientDialogLayout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(addClientDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtAddClientDialog, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnAddClientDialog, javax.swing.GroupLayout.DEFAULT_SIZE, 53, Short.MAX_VALUE)
                .addContainerGap())
        );

        receivedAmountDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        receivedAmountDialog.setTitle("Pago en efectivo");
        receivedAmountDialog.setModal(true);

        jLabel15.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        jLabel15.setText("Cantidad recibida:");

        txtReceivedAmountDialog.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        txtReceivedAmountDialog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtReceivedAmountDialogActionPerformed(evt);
            }
        });
        txtReceivedAmountDialog.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtReceivedAmountDialogKeyPressed(evt);
            }
        });

        btnReceivedAmountDialog.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        btnReceivedAmountDialog.setText("Aceptar");
        btnReceivedAmountDialog.setFocusable(false);
        btnReceivedAmountDialog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReceivedAmountDialogActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout receivedAmountDialogLayout = new javax.swing.GroupLayout(receivedAmountDialog.getContentPane());
        receivedAmountDialog.getContentPane().setLayout(receivedAmountDialogLayout);
        receivedAmountDialogLayout.setHorizontalGroup(
            receivedAmountDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(receivedAmountDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(receivedAmountDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(receivedAmountDialogLayout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtReceivedAmountDialog, javax.swing.GroupLayout.DEFAULT_SIZE, 481, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, receivedAmountDialogLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnReceivedAmountDialog, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        receivedAmountDialogLayout.setVerticalGroup(
            receivedAmountDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(receivedAmountDialogLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(receivedAmountDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtReceivedAmountDialog, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnReceivedAmountDialog, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        successfulPaymentDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        successfulPaymentDialog.setTitle("Pago completado");
        successfulPaymentDialog.setBackground(new java.awt.Color(255, 255, 255));
        successfulPaymentDialog.setModal(true);

        jLabel16.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        jLabel16.setText("1:Imprimir 2:Finalizar");

        jLabel17.setBackground(new java.awt.Color(217, 237, 247));
        jLabel17.setFont(new java.awt.Font("Lucida Grande", 1, 24)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(58, 135, 173));
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/ic_info.png"))); // NOI18N
        jLabel17.setText("Que deseas hacer?");
        jLabel17.setOpaque(true);

        jLabel18.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        jLabel18.setLabelFor(txtSuccessFullPaymentDialog);
        jLabel18.setText("Acción:");

        txtSuccessFullPaymentDialog.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        txtSuccessFullPaymentDialog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSuccessFullPaymentDialogActionPerformed(evt);
            }
        });

        btnSuccessfullPaymentDialog.setFont(new java.awt.Font("Lucida Grande", 1, 24)); // NOI18N
        btnSuccessfullPaymentDialog.setText("Aceptar");
        btnSuccessfullPaymentDialog.setFocusable(false);
        btnSuccessfullPaymentDialog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuccessfullPaymentDialogActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout successfulPaymentDialogLayout = new javax.swing.GroupLayout(successfulPaymentDialog.getContentPane());
        successfulPaymentDialog.getContentPane().setLayout(successfulPaymentDialogLayout);
        successfulPaymentDialogLayout.setHorizontalGroup(
            successfulPaymentDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(successfulPaymentDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(successfulPaymentDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, 518, Short.MAX_VALUE)
                    .addGroup(successfulPaymentDialogLayout.createSequentialGroup()
                        .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtSuccessFullPaymentDialog))
                    .addGroup(successfulPaymentDialogLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnSuccessfullPaymentDialog, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        successfulPaymentDialogLayout.setVerticalGroup(
            successfulPaymentDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(successfulPaymentDialogLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(successfulPaymentDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtSuccessFullPaymentDialog, javax.swing.GroupLayout.DEFAULT_SIZE, 43, Short.MAX_VALUE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSuccessfullPaymentDialog, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(17, Short.MAX_VALUE))
        );

        endPaymentDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        endPaymentDialog.setModal(true);

        jLabel19.setBackground(new java.awt.Color(217, 237, 247));
        jLabel19.setFont(new java.awt.Font("Lucida Grande", 1, 24)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(58, 135, 173));
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/ic_info.png"))); // NOI18N
        jLabel19.setText("Pago exitoso!!");
        jLabel19.setOpaque(true);

        lblTotalEndPaymentDialog.setFont(new java.awt.Font("Lucida Grande", 0, 24)); // NOI18N
        lblTotalEndPaymentDialog.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotalEndPaymentDialog.setText("Total: 1000 ");
        lblTotalEndPaymentDialog.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(166, 201, 226)));

        lblQuantityEndPaymentDialog.setFont(new java.awt.Font("Lucida Grande", 0, 24)); // NOI18N
        lblQuantityEndPaymentDialog.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblQuantityEndPaymentDialog.setText("Cantidad recibida : 2000 ");
        lblQuantityEndPaymentDialog.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(166, 201, 226)));

        lblReturnEndPaymentDialog.setFont(new java.awt.Font("Lucida Grande", 0, 24)); // NOI18N
        lblReturnEndPaymentDialog.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblReturnEndPaymentDialog.setText("Devolución: 0 ");
        lblReturnEndPaymentDialog.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(166, 201, 226)));

        btnOkEndPaymentDialog.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        btnOkEndPaymentDialog.setText("Aceptar");
        btnOkEndPaymentDialog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkEndPaymentDialogActionPerformed(evt);
            }
        });
        btnOkEndPaymentDialog.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnOkEndPaymentDialogKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout endPaymentDialogLayout = new javax.swing.GroupLayout(endPaymentDialog.getContentPane());
        endPaymentDialog.getContentPane().setLayout(endPaymentDialogLayout);
        endPaymentDialogLayout.setHorizontalGroup(
            endPaymentDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(endPaymentDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(endPaymentDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblReturnEndPaymentDialog, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblTotalEndPaymentDialog, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblQuantityEndPaymentDialog, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 478, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(endPaymentDialogLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnOkEndPaymentDialog, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        endPaymentDialogLayout.setVerticalGroup(
            endPaymentDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(endPaymentDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblTotalEndPaymentDialog, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(lblQuantityEndPaymentDialog, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(lblReturnEndPaymentDialog, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnOkEndPaymentDialog, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        addWeightDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWeightDialog.setModal(true);

        jLabel8.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        jLabel8.setText("Peso (gramos):");

        txtAddWeightDialog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAddWeightDialogActionPerformed(evt);
            }
        });
        txtAddWeightDialog.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtAddWeightDialogKeyPressed(evt);
            }
        });

        btnOkAddWeightDialog.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        btnOkAddWeightDialog.setText("Aceptar");
        btnOkAddWeightDialog.setFocusable(false);
        btnOkAddWeightDialog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkAddWeightDialogActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout addWeightDialogLayout = new javax.swing.GroupLayout(addWeightDialog.getContentPane());
        addWeightDialog.getContentPane().setLayout(addWeightDialogLayout);
        addWeightDialogLayout.setHorizontalGroup(
            addWeightDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addWeightDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtAddWeightDialog, javax.swing.GroupLayout.DEFAULT_SIZE, 342, Short.MAX_VALUE))
            .addGroup(addWeightDialogLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnOkAddWeightDialog)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        addWeightDialogLayout.setVerticalGroup(
            addWeightDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addWeightDialogLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(addWeightDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtAddWeightDialog, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnOkAddWeightDialog, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setExtendedState(6);

        jPanel1.setBackground(new java.awt.Color(3, 111, 171));
        jPanel1.setPreferredSize(new java.awt.Dimension(400, 44));

        jPanel3.setBackground(new java.awt.Color(92, 156, 204));
        jPanel3.setPreferredSize(new java.awt.Dimension(100, 24));

        rSLabelFecha2.setForeground(new java.awt.Color(255, 255, 255));
        rSLabelFecha2.setPreferredSize(new java.awt.Dimension(100, 23));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(rSLabelFecha2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(0, 1, Short.MAX_VALUE)
                .addComponent(rSLabelFecha2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel2.setBackground(new java.awt.Color(92, 156, 204));
        jPanel2.setPreferredSize(new java.awt.Dimension(100, 24));

        rSLabelHora1.setForeground(new java.awt.Color(255, 255, 255));
        rSLabelHora1.setPreferredSize(new java.awt.Dimension(100, 24));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(rSLabelHora1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(rSLabelHora1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel4.setBackground(new java.awt.Color(3, 111, 171));
        jPanel4.setPreferredSize(new java.awt.Dimension(150, 24));

        userName.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        userName.setForeground(new java.awt.Color(255, 255, 255));
        userName.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/ico-usuario.png"))); // NOI18N
        userName.setText("fgfgf");
        userName.setPreferredSize(new java.awt.Dimension(20, 30));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 150, Short.MAX_VALUE)
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(userName, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 24, Short.MAX_VALUE)
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(userName, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 24, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10))
        );

        jPanel5.setBackground(new java.awt.Color(3, 111, 171));

        jLabel1.setFont(new java.awt.Font("Lucida Grande", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Developed by wgcarvajal");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        buttonsAndSearchPanel.setBackground(new java.awt.Color(255, 255, 255));
        buttonsAndSearchPanel.setForeground(new java.awt.Color(255, 255, 255));

        butonsPanel.setBackground(new java.awt.Color(204, 204, 204));

        javax.swing.GroupLayout butonsPanelLayout = new javax.swing.GroupLayout(butonsPanel);
        butonsPanel.setLayout(butonsPanelLayout);
        butonsPanelLayout.setHorizontalGroup(
            butonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 610, Short.MAX_VALUE)
        );
        butonsPanelLayout.setVerticalGroup(
            butonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));

        jLabel4.setText("Código:");

        codeTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                codeTxtActionPerformed(evt);
            }
        });

        searchIcon = new ImageIcon(getClass().getResource("/resources/searchIcon.png"));
        btnReadCode.setIcon(searchIcon);
        btnReadCode.setFocusable(false);
        btnReadCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReadCodeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(codeTxt)
                .addGap(0, 0, 0)
                .addComponent(btnReadCode, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(codeTxt)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnReadCode, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout buttonsAndSearchPanelLayout = new javax.swing.GroupLayout(buttonsAndSearchPanel);
        buttonsAndSearchPanel.setLayout(buttonsAndSearchPanelLayout);
        buttonsAndSearchPanelLayout.setHorizontalGroup(
            buttonsAndSearchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(butonsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        buttonsAndSearchPanelLayout.setVerticalGroup(
            buttonsAndSearchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(buttonsAndSearchPanelLayout.createSequentialGroup()
                .addComponent(butonsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        infoSalePanel.setBackground(new java.awt.Color(255, 255, 255));

        clientAndTotalPanel.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Lucida Grande", 1, 15)); // NOI18N
        jLabel2.setText("Cliente:");
        jLabel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(166, 201, 226)));

        lblNameClient.setFont(new java.awt.Font("Lucida Grande", 1, 15)); // NOI18N
        lblNameClient.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblNameClient.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(166, 201, 226)));

        jLabel3.setFont(new java.awt.Font("Lucida Grande", 1, 30)); // NOI18N
        jLabel3.setText("Total:");
        jLabel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(166, 201, 226)));

        lblTotal.setFont(new java.awt.Font("Lucida Grande", 1, 60)); // NOI18N
        lblTotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotal.setText("$0 ");
        lblTotal.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(166, 201, 226)));

        javax.swing.GroupLayout clientAndTotalPanelLayout = new javax.swing.GroupLayout(clientAndTotalPanel);
        clientAndTotalPanel.setLayout(clientAndTotalPanelLayout);
        clientAndTotalPanelLayout.setHorizontalGroup(
            clientAndTotalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(clientAndTotalPanelLayout.createSequentialGroup()
                .addGroup(clientAndTotalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(clientAndTotalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblNameClient, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblTotal, javax.swing.GroupLayout.DEFAULT_SIZE, 519, Short.MAX_VALUE)))
        );
        clientAndTotalPanelLayout.setVerticalGroup(
            clientAndTotalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(clientAndTotalPanelLayout.createSequentialGroup()
                .addGroup(clientAndTotalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblNameClient, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(clientAndTotalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(clientAndTotalPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(lblTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

        productTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Nombre", "Precio", "Cantidad", "Total"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            @Override
            public boolean isCellEditable(int row, int column) {

                String myString =purchaseitems.get(row).getProduct().getProdBarCode();
                StringSelection stringSelection = new StringSelection(myString);
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(stringSelection, null);
                return false;
            }
        });
        productTable.setFocusable(false);
        productTable.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        productTable.setRowHeight(35);
        productTable.setSelectionBackground(new java.awt.Color(223, 239, 252));
        productTable.setSelectionForeground(new java.awt.Color(0, 0, 0));
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
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );
        productTable.setDefaultRenderer(String.class, centerRenderer);
        productTable.setShowVerticalLines(true);
        productTable.setShowHorizontalLines(true);
        productTable.getColumnModel().getColumn(1).setCellRenderer(new WordWrapCellRenderer());
        scrollProductTable.setViewportView(productTable);

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setForeground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout infoSalePanelLayout = new javax.swing.GroupLayout(infoSalePanel);
        infoSalePanel.setLayout(infoSalePanelLayout);
        infoSalePanelLayout.setHorizontalGroup(
            infoSalePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(clientAndTotalPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(scrollProductTable)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        infoSalePanelLayout.setVerticalGroup(
            infoSalePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(infoSalePanelLayout.createSequentialGroup()
                .addComponent(clientAndTotalPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(scrollProductTable, javax.swing.GroupLayout.DEFAULT_SIZE, 279, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 1268, Short.MAX_VALUE)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(buttonsAndSearchPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(infoSalePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 6, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(buttonsAndSearchPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(infoSalePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnOkOpenCashActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkOpenCashActionPerformed
        openCash();
    }//GEN-LAST:event_btnOkOpenCashActionPerformed

    private void txtOpenCashPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtOpenCashPasswordActionPerformed
        openCash();
    }//GEN-LAST:event_txtOpenCashPasswordActionPerformed

    private void txtCancelSalepasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCancelSalepasswordActionPerformed
        cancelSale();
    }//GEN-LAST:event_txtCancelSalepasswordActionPerformed

    private void btnOkCancelSaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkCancelSaleActionPerformed
        cancelSale();
    }//GEN-LAST:event_btnOkCancelSaleActionPerformed

    private void codeTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_codeTxtActionPerformed
        readCode();
    }//GEN-LAST:event_codeTxtActionPerformed

    private void btnReadCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReadCodeActionPerformed
        readCode();
    }//GEN-LAST:event_btnReadCodeActionPerformed

    private void btnOkNoActionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkNoActionActionPerformed
        noActionDialog.dispose();
    }//GEN-LAST:event_btnOkNoActionActionPerformed

    private void btnOkNoActionKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnOkNoActionKeyPressed
        if (evt.getKeyCode() == 10 || evt.getKeyCode() == 27) {
            noActionDialog.dispose();
        }
       
    }//GEN-LAST:event_btnOkNoActionKeyPressed

    private void txtCancelSalepasswordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCancelSalepasswordKeyPressed
        if (evt.getKeyCode() == 27) {
            cancelSalePasswordDialog.dispose();
        }
    }//GEN-LAST:event_txtCancelSalepasswordKeyPressed

    private void txtOpenCashPasswordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtOpenCashPasswordKeyPressed
        if (evt.getKeyCode() == 27) {
            openCashPasswordDialog.dispose();
        }
    }//GEN-LAST:event_txtOpenCashPasswordKeyPressed

    private void txtChangeQuantityDialogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtChangeQuantityDialogActionPerformed
        changeQuantityProduct();
    }//GEN-LAST:event_txtChangeQuantityDialogActionPerformed

    private void btnOkChangeQuantityDialogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkChangeQuantityDialogActionPerformed
        changeQuantityProduct();
    }//GEN-LAST:event_btnOkChangeQuantityDialogActionPerformed

    private void txtChangeQuantityDialogKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtChangeQuantityDialogKeyPressed
        if (evt.getKeyCode() == 27) {
            changeQuatityDialog.dispose();
        }
    }//GEN-LAST:event_txtChangeQuantityDialogKeyPressed

    private void txtBarCodeRemoveProductDialogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBarCodeRemoveProductDialogActionPerformed
        removeProduct();
    }//GEN-LAST:event_txtBarCodeRemoveProductDialogActionPerformed

    private void txtBarCodeRemoveProductDialogKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBarCodeRemoveProductDialogKeyPressed
        if (evt.getKeyCode() == 27) {
            removeProductDialog.dispose();
        }
    }//GEN-LAST:event_txtBarCodeRemoveProductDialogKeyPressed

    private void btnOkRemoveProductDialogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkRemoveProductDialogActionPerformed
        removeProduct();
    }//GEN-LAST:event_btnOkRemoveProductDialogActionPerformed

    private void txtReturnProductDialogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtReturnProductDialogActionPerformed
        String codeForReturn = txtReturnProductDialog.getText();
        if(!codeForReturn.isEmpty())
        {
            searchAndAddProduct(codeForReturn, true);
            returnProductDialog.dispose();
        }
    }//GEN-LAST:event_txtReturnProductDialogActionPerformed

    private void btnOkReturnProductDialogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkReturnProductDialogActionPerformed
        String codeForReturn = txtReturnProductDialog.getText();
        if(!codeForReturn.isEmpty())
        {
            searchAndAddProduct(codeForReturn, true);
            returnProductDialog.dispose();
        }
    }//GEN-LAST:event_btnOkReturnProductDialogActionPerformed

    private void txtReturnProductDialogKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtReturnProductDialogKeyPressed
        if (evt.getKeyCode() == 27) {
            returnProductDialog.dispose();
        }
    }//GEN-LAST:event_txtReturnProductDialogKeyPressed

    private void txtSelectProductDialogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSelectProductDialogActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSelectProductDialogActionPerformed

    private void txtSelectProductDialogKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSelectProductDialogKeyPressed
        if (evt.getKeyCode() == 27) {
            selectProductDialog.dispose();
        }
    }//GEN-LAST:event_txtSelectProductDialogKeyPressed

    private void txtSelectProductDialogKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSelectProductDialogKeyReleased
        String search = txtSelectProductDialog.getText();
            DefaultTableModel model = (DefaultTableModel) selectProductDialogTable.getModel();
            model.setRowCount(0);
            for (int i = 0; i < productList.size(); i++) {
                Object[] objects = (Object[]) productList.get(i);
                Product p = (Product) objects[0];
                Price pr = (Price) objects[1];
                Brand b = (Brand) objects[2];
                Category c = (Category) objects[3];
                Unity u = (Unity) objects[4];

                String name = p.getProdName().toLowerCase();
                if(search.isEmpty() || name.contains(search.toLowerCase())){
                    Object[] row = {p.getProdBarCode(), p.getProdName(),
                        p.getProdUnitValue() + " " + u.getUniAbbreviation(),
                        c.getCatName(), b.getBrandName(), Util.getFormatPrice(pr.getPriceValue())};

                    model.addRow(row);
                }
            }
    }//GEN-LAST:event_txtSelectProductDialogKeyReleased

    private void btnResumeDialogKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnResumeDialogKeyPressed
        if (evt.getKeyCode() == 10 || evt.getKeyCode() == 27) {
            resumeDialog.dispose();
        }
    }//GEN-LAST:event_btnResumeDialogKeyPressed

    private void btnResumeDialogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResumeDialogActionPerformed
        resumeDialog.dispose();
    }//GEN-LAST:event_btnResumeDialogActionPerformed

    private void btnTotalDialogKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnTotalDialogKeyPressed
        if (evt.getKeyCode() == 10 || evt.getKeyCode() == 27) {
            totalDialog.dispose();
        }
    }//GEN-LAST:event_btnTotalDialogKeyPressed

    private void btnTotalDialogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTotalDialogActionPerformed
        totalDialog.dispose();
    }//GEN-LAST:event_btnTotalDialogActionPerformed

    private void txtAddClientDialogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAddClientDialogActionPerformed
       addClient();
    }//GEN-LAST:event_txtAddClientDialogActionPerformed

    private void txtAddClientDialogKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAddClientDialogKeyPressed
        if (evt.getKeyCode() == 27)
        {
            addClientDialog.dispose();
        }
    }//GEN-LAST:event_txtAddClientDialogKeyPressed

    private void btnAddClientDialogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddClientDialogActionPerformed
        addClient();
    }//GEN-LAST:event_btnAddClientDialogActionPerformed

    private void txtReceivedAmountDialogKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtReceivedAmountDialogKeyPressed
        if (evt.getKeyCode() == 27)
        {
            receivedAmountDialog.dispose();
        }
        
    }//GEN-LAST:event_txtReceivedAmountDialogKeyPressed

    private void btnReceivedAmountDialogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReceivedAmountDialogActionPerformed
        receivedAmount();
    }//GEN-LAST:event_btnReceivedAmountDialogActionPerformed

    private void txtSuccessFullPaymentDialogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSuccessFullPaymentDialogActionPerformed
        okAction();
    }//GEN-LAST:event_txtSuccessFullPaymentDialogActionPerformed

    private void btnSuccessfullPaymentDialogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuccessfullPaymentDialogActionPerformed
        okAction();
    }//GEN-LAST:event_btnSuccessfullPaymentDialogActionPerformed

    private void txtReceivedAmountDialogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtReceivedAmountDialogActionPerformed
        receivedAmount();
    }//GEN-LAST:event_txtReceivedAmountDialogActionPerformed

    private void btnOkEndPaymentDialogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkEndPaymentDialogActionPerformed
        endPaymentDialog.dispose();
    }//GEN-LAST:event_btnOkEndPaymentDialogActionPerformed

    private void btnOkEndPaymentDialogKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnOkEndPaymentDialogKeyPressed
        if (evt.getKeyCode() == 10 || evt.getKeyCode() == 27) {
            endPaymentDialog.dispose();
        }
    }//GEN-LAST:event_btnOkEndPaymentDialogKeyPressed

    private void txtAddWeightDialogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAddWeightDialogActionPerformed
        String w = txtAddWeightDialog.getText();
        if(!w.isEmpty())
        {
            try{
                int d = Integer.parseInt(w);
                weight = w;
                addWeightDialog.dispose();
                proccessManualWeight();
            }
            catch(Exception e)
            {
                        
            }
        }
    }//GEN-LAST:event_txtAddWeightDialogActionPerformed

    private void btnOkAddWeightDialogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkAddWeightDialogActionPerformed
        String w = txtAddWeightDialog.getText();
        if(!w.isEmpty())
        {
            try{
                int d = Integer.parseInt(w);
                weight = w;
                addWeightDialog.dispose();
                proccessManualWeight();
            }
            catch(Exception e)
            {
                        
            }
        }
    }//GEN-LAST:event_btnOkAddWeightDialogActionPerformed

    private void txtAddWeightDialogKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAddWeightDialogKeyPressed
        if (evt.getKeyCode() == 27)
        {
            addWeightDialog.dispose();
        }
    }//GEN-LAST:event_txtAddWeightDialogKeyPressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JDialog addClientDialog;
    private javax.swing.JDialog addWeightDialog;
    private javax.swing.JButton btnAddClientDialog;
    private javax.swing.JButton btnOkAddWeightDialog;
    private javax.swing.JButton btnOkCancelSale;
    private javax.swing.JButton btnOkChangeQuantityDialog;
    private javax.swing.JButton btnOkEndPaymentDialog;
    private javax.swing.JButton btnOkNoAction;
    private javax.swing.JButton btnOkOpenCash;
    private javax.swing.JButton btnOkRemoveProductDialog;
    private javax.swing.JButton btnOkReturnProductDialog;
    private javax.swing.JButton btnReadCode;
    private javax.swing.JButton btnReceivedAmountDialog;
    private javax.swing.JButton btnResumeDialog;
    private javax.swing.JButton btnSuccessfullPaymentDialog;
    private javax.swing.JButton btnTotalDialog;
    private javax.swing.JPanel butonsPanel;
    private javax.swing.JPanel buttonsAndSearchPanel;
    private javax.swing.JDialog cancelSalePasswordDialog;
    private javax.swing.JDialog changeQuatityDialog;
    private javax.swing.JPanel clientAndTotalPanel;
    private javax.swing.JTextField codeTxt;
    private javax.swing.JDialog endPaymentDialog;
    private javax.swing.JPanel infoSalePanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblNameClient;
    private javax.swing.JLabel lblQuantityEndPaymentDialog;
    private javax.swing.JLabel lblReturnEndPaymentDialog;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JLabel lblTotalDialog;
    private javax.swing.JLabel lblTotalEndPaymentDialog;
    private javax.swing.JDialog noActionDialog;
    private javax.swing.JDialog openCashPasswordDialog;
    private javax.swing.JTable productTable;
    private rojeru_san.RSLabelFecha rSLabelFecha2;
    private rojeru_san.RSLabelHora rSLabelHora1;
    private javax.swing.JDialog receivedAmountDialog;
    private javax.swing.JDialog removeProductDialog;
    private javax.swing.JDialog resumeDialog;
    private javax.swing.JTable resumeDialogTable;
    private javax.swing.JDialog returnProductDialog;
    private javax.swing.JScrollPane scrollProductTable;
    private javax.swing.JScrollPane scrollSelectProductDialogTable;
    private javax.swing.JDialog selectProductDialog;
    private javax.swing.JTable selectProductDialogTable;
    private javax.swing.JDialog successfulPaymentDialog;
    private javax.swing.JDialog totalDialog;
    private javax.swing.JTextField txtAddClientDialog;
    private javax.swing.JTextField txtAddWeightDialog;
    private javax.swing.JTextField txtBarCodeRemoveProductDialog;
    private javax.swing.JPasswordField txtCancelSalepassword;
    private javax.swing.JTextField txtChangeQuantityDialog;
    private javax.swing.JPasswordField txtOpenCashPassword;
    private javax.swing.JTextField txtReceivedAmountDialog;
    private javax.swing.JTextField txtReturnProductDialog;
    private javax.swing.JTextField txtSelectProductDialog;
    private javax.swing.JTextField txtSuccessFullPaymentDialog;
    private javax.swing.JLabel userName;
    // End of variables declaration//GEN-END:variables
    private JButton openBtn; 
    private JButton exitBtn; 
    private JButton cancelBtn;
    private JButton paymentBtn;
    private JButton selectBtn;
    private JButton changeQuantitybtn;
    private JButton removeProductBtn;
    private JButton addToBacketBtn;
    private JButton resumeBtn;
    private JButton searchProductBtn;
    private JButton returnProductBtn;
    private JButton addClientBtn;
    private JButton searchClientBtn;
    private JButton totalBtn;

    
    
    private void openBtnActionPerformed(java.awt.event.ActionEvent evt) {                                        
        txtOpenCashPassword.setText("");
        openCashPasswordDialog.setVisible(true);
    }
    
    private void exitBtnActionPerformed(java.awt.event.ActionEvent evt) {                                        
        closeView();
    } 
    
    private void cancelBtnActionPerformed(java.awt.event.ActionEvent evt) {                                          
        txtCancelSalepassword.setText("");
        cancelSalePasswordDialog.setVisible(true);
    } 
    
    private void paymentBtnActionPerformed(java.awt.event.ActionEvent evt) {                                           
        txtReceivedAmountDialog.setText("");
        receivedAmountDialog.setLocationRelativeTo(null);
        receivedAmountDialog.setVisible(true);
    } 
    
    private void selectBtnActionPerformed(java.awt.event.ActionEvent evt) {  
        openSelectProduct();
    }    
    
    private void changeQuantitybtnActionPerformed(java.awt.event.ActionEvent evt) {                                                  
        txtChangeQuantityDialog.setText("");
        changeQuatityDialog.setVisible(true);
    } 
    
    private void removeProductBtnActionPerformed(java.awt.event.ActionEvent evt) {                                                 
        txtBarCodeRemoveProductDialog.setText("");
        removeProductDialog.setVisible(true);
    } 
    
    private void addToBacketBtnActionPerformed(java.awt.event.ActionEvent evt) {                                               
        addToBacketView();
    }  
    
    private void resumeBtnActionPerformed(java.awt.event.ActionEvent evt) {                                          
        resumeSale();
    } 
    
    private void searchProductBtnActionPerformed(java.awt.event.ActionEvent evt) {                                                 
        // TODO add your handling code here:
    } 
    
    private void returnProductBtnActionPerformed(java.awt.event.ActionEvent evt) {                                                 
        txtReturnProductDialog.setText("");
        returnProductDialog.setVisible(true);
    } 
    
    private void addClientBtnActionPerformed(java.awt.event.ActionEvent evt) {                                             
        addClientDialog.setLocationRelativeTo(null);
        txtAddClientDialog.setText("");
        addClientDialog.setVisible(true);
    }
    
    private void searchClientBtnActionPerformed(java.awt.event.ActionEvent evt) {                                                
        // TODO add your handling code here:
    } 
    
    private void totalBtnActionPerformed(java.awt.event.ActionEvent evt) {                                         
        currentDayTotal();
    } 
    
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
                    noActionDialog.setVisible(true);
                }
                break;
            case "010":
                if(exitBtn.isVisible())
                {
                    
                    closeView();
                }
                else
                {
                   noActionDialog.setVisible(true);
                }
                break;
            case "002":
                if (addToBacketBtn.isVisible()) {
                    addToBacketView();
                } else {
                   noActionDialog.setVisible(true);
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
                    noActionDialog.setVisible(true);
                }
                break;
            case "003":
                if(changeQuantitybtn.isVisible())
                {
                   txtChangeQuantityDialog.setText("");
                   changeQuatityDialog.setVisible(true);
                }
                else
                {
                    noActionDialog.setVisible(true);
                }
                break;
            case "004":
                if(addClientBtn.isVisible())
                {
                    addClientDialog.setLocationRelativeTo(null);
                    txtAddClientDialog.setText("");
                    addClientDialog.setVisible(true);
                }
                else
                {
                    noActionDialog.setVisible(true);
                }
                break;
            case "030":
                if(removeProductBtn.isVisible())
                {
                    txtBarCodeRemoveProductDialog.setText("");
                    removeProductDialog.setVisible(true);
                }
                else
                {
                    noActionDialog.setVisible(true);
                }
                break;
            case "005":
                if(paymentBtn.isVisible())
                {
                    txtReceivedAmountDialog.setText("");
                    receivedAmountDialog.setLocationRelativeTo(null);
                    receivedAmountDialog.setVisible(true);
                }
                else
                {
                    noActionDialog.setVisible(true);
                }
                break;
            case "040":
                if(searchProductBtn.isVisible())
                {
                    //openSearchProduct();
                }
                else
                {
                    noActionDialog.setVisible(true);
                }
                break;
            case "050":
                if(searchClientBtn.isVisible())
                {
                    //openSearchClient();
                }
                else
                {
                    noActionDialog.setVisible(true);
                }
                break;
            case "006":
                if(selectBtn.isVisible())
                {
                    openSelectProduct();
                }
                else
                {
                    noActionDialog.setVisible(true);
                }
                break;
            case "007":
                if(resumeBtn.isVisible())
                {
                    resumeSale();
                }
                else
                {
                   noActionDialog.setVisible(true);
                }
                break;
            case "008":
                if(totalBtn.isVisible())
                {
                   currentDayTotal();
                }
                else{
                    noActionDialog.setVisible(true);
                }
                    break;
            case "009":
                if(returnProductBtn.isVisible())
                {
                    txtReturnProductDialog.setText("");
                    returnProductDialog.setVisible(true);
                }
                else
                {
                    noActionDialog.setVisible(true);
                }
                break;
            default:
                if(selectBtn.isVisible())
                {
                    if(code.length()>12)
                    {
                        code = code.substring(0, 12);
                    }
                    searchAndAddProduct(code,false);
                }
                break;
        }
    }
    
    
    private void removeButtons()
    {
        butonsPanel.remove(openBtn);
        butonsPanel.remove(exitBtn); 
        butonsPanel.remove(cancelBtn);
        butonsPanel.remove(paymentBtn);
        butonsPanel.remove(selectBtn);
        butonsPanel.remove(changeQuantitybtn);
        butonsPanel.remove(removeProductBtn);
        butonsPanel.remove(addToBacketBtn);
        butonsPanel.remove(resumeBtn);
        butonsPanel.remove(searchProductBtn);
        butonsPanel.remove(returnProductBtn);
        butonsPanel.remove(addClientBtn);
        butonsPanel.remove(searchClientBtn);
        butonsPanel.remove(totalBtn);
        butonsPanel.revalidate();
        butonsPanel.repaint();
        
        openBtn.setVisible(false); 
        exitBtn.setVisible(false);
        cancelBtn.setVisible(false);
        paymentBtn.setVisible(false);
        selectBtn.setVisible(false);
        changeQuantitybtn.setVisible(false);
        removeProductBtn.setVisible(false);
        addToBacketBtn.setVisible(false);
        resumeBtn.setVisible(false);
        searchProductBtn.setVisible(false);
        returnProductBtn.setVisible(false);
        addClientBtn.setVisible(false);
        searchClientBtn.setVisible(false);
        totalBtn.setVisible(false);
    }
    
    private void emptyVariables() {
        listScaleIp = null;
        scale = null;
        weight = null;
        rAmount = null;
        refund = null;
        receivedAmount = null;
        total = null;
        generatePdf = null;
        purchaseitems = null;
        purchasesResume = null;
        purchasetotals = null;
        productList = null;
        purchase = null;
        producWaitForWeight = null;
    }
    
    private void openCashView()
    {
        removeButtons();
        exitBtn.setVisible(true);
        addToBacketBtn.setVisible(true);
        resumeBtn.setVisible(true);
        searchProductBtn.setVisible(true);
        searchClientBtn.setVisible(true);
        totalBtn.setVisible(true);
        clientAndTotalPanel.setVisible(false);
        scrollProductTable.setVisible(false);
        
        GroupLayout jPanel5Layout = new javax.swing.GroupLayout(butonsPanel);
        butonsPanel.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(exitBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(addToBacketBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(resumeBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(searchProductBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                    .addComponent(exitBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addToBacketBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(resumeBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchProductBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchClientBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(totalBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                ))
        );
    }
    
    public void closeView()
    {
        removeButtons();
        openBtn.setVisible(true);
        
        GroupLayout jPanel5Layout = new javax.swing.GroupLayout(butonsPanel);
        butonsPanel.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addComponent(openBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                )
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(openBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                ))
        );
    }
    
    
    public void addToBacketView()
    {
        stopScale();
        initScale();
        removeButtons();
        lblNameClient.setText("");
        lblTotal.setText("$0 ");
        scrollProductTable.setVisible(false);
        clientAndTotalPanel.setVisible(true);
        
        cancelBtn.setVisible(true);
        selectBtn.setVisible(true);
        searchProductBtn.setVisible(true);
        returnProductBtn.setVisible(true);
        searchClientBtn.setVisible(true);
        
        emptyVariables();
        
        GroupLayout jPanel5Layout = new javax.swing.GroupLayout(butonsPanel);
        butonsPanel.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(cancelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(selectBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(searchProductBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(returnProductBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(searchClientBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(cancelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(selectBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchProductBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(returnProductBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchClientBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    ))
        );
    }
    
    private void createPurchaseItemView(boolean isProductType) {
        removeButtons();
        cancelBtn.setVisible(true);
        paymentBtn.setVisible(true);
        selectBtn.setVisible(true);
        changeQuantitybtn.setVisible(!isProductType);
        removeProductBtn.setVisible(true);
        searchProductBtn.setVisible(true);
        returnProductBtn.setVisible(true);
        addClientBtn.setVisible(true);
        searchClientBtn.setVisible(true);
        scrollProductTable.setVisible(true);
        
        GroupLayout jPanel5Layout = new javax.swing.GroupLayout(butonsPanel);
        butonsPanel.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
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
                .addComponent(searchProductBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(returnProductBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(addClientBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(searchClientBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(cancelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(paymentBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(selectBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(changeQuantitybtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(removeProductBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchProductBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(returnProductBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addClientBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchClientBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    ))
        );

        Purchaseitem purchaseitem = purchaseitems.get(purchaseitems.size() - 1);

        Object[] row = {purchaseitem.getProduct().getProdBarCode(),
            purchaseitem.getProduct().getProdName() + " " + purchaseitem.getProduct().getProdUnitValue() + " " + purchaseitem.getProduct().getUnity().getUniAbbreviation(),
            Util.getFormatPrice(purchaseitem.getPriceValue()),
            getQuantity(purchaseitem),
            getTotal(purchaseitem)};

        DefaultTableModel model = (DefaultTableModel) productTable.getModel();
        if (purchaseitems.size() == 1) {
            model.setRowCount(0);
        }
        
        model.addRow(row);
        productTable.changeSelection(productTable.getRowCount() - 1, 0, false, false);
        calculateAmountTotal();
    }
    
    private void updatePurchaseItemView(boolean isProductType) {
        removeButtons();
        cancelBtn.setVisible(true);
        paymentBtn.setVisible(true);
        selectBtn.setVisible(true);
        changeQuantitybtn.setVisible(!isProductType);
        removeProductBtn.setVisible(true);
        searchProductBtn.setVisible(true);
        returnProductBtn.setVisible(true);
        addClientBtn.setVisible(true);
        searchClientBtn.setVisible(true);
        scrollProductTable.setVisible(true);
        
        if(!clientAndTotalPanel.isVisible())
        {
            clientAndTotalPanel.setVisible(true);
        }
        
        GroupLayout jPanel5Layout = new javax.swing.GroupLayout(butonsPanel);
        butonsPanel.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
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
                .addComponent(searchProductBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(returnProductBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(addClientBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(searchClientBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(cancelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(paymentBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(selectBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(changeQuantitybtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(removeProductBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchProductBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(returnProductBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addClientBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchClientBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    ))
        );

        DefaultTableModel model = (DefaultTableModel) productTable.getModel();
        
        model.setRowCount(0);
        
        for(Purchaseitem purchaseitem: purchaseitems){
            Object[] row = {purchaseitem.getProduct().getProdBarCode(),
            purchaseitem.getProduct().getProdName() + " " + purchaseitem.getProduct().getProdUnitValue() + " " + purchaseitem.getProduct().getUnity().getUniAbbreviation(),
            Util.getFormatPrice(purchaseitem.getPriceValue()),
            getQuantity(purchaseitem),
            getTotal(purchaseitem)};
            model.addRow(row);
        }
        productTable.changeSelection(productTable.getRowCount() - 1, 0, false, false);
        calculateAmountTotal();
    }
    
    public void cancelSale()
    {
        startLoading();
        cancelSalePasswordDialog.setVisible(false);
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
                stopLoading();
                try {
                    boolean result = get();
                    if(result)
                    {
                        openCashView();
                        cancelSalePasswordDialog.dispose();
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
    }
    
    private void searchAndAddProduct(String code,boolean isReturnProduct)
    {
        startLoading();
        new SwingWorker<ResultAddProduct, ResultAddProduct>() {
            @Override
            protected ResultAddProduct doInBackground() {
                    String c = code;
                    Product product = productModel.findByBarCode(c);
                    if (product != null) {
                        boolean productType = product.getProducttype().getProdtypeValue().equals("Sin empaquetar");
                        if (!productType) {
                            return addProduct(product, productType, isReturnProduct);
                        } else {
                            ResultAddProduct result = new ResultAddProduct();
                            isReturnProductWeight = isReturnProduct;
                            producWaitForWeight = product;
                            result.setMessage(AppConstants.Cashier.AUTOMATIC_READ_WEIGHT);
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
                stopLoading();
                try {
                    ResultAddProduct result = get();
                    processResultAddProduct(result);
                    
                } catch (InterruptedException | ExecutionException ex) {
                    ex.printStackTrace();
                }
            }

        }.execute();
    }
    
    private void processResultAddProduct(ResultAddProduct result)
    {
        String message = result.getMessage();
        if(message.equals(AppConstants.Cashier.NO_FOUND_PRODUCT))
        {
            //show message toast
        }
        else if(message.equals(AppConstants.Cashier.CREATE_PURCHASE_ITEM))
        {
            createPurchaseItemView(result.isProductType());
        }
        else if(message.equals(AppConstants.Cashier.UPDATE_PURCHASE_ITEM) || message.equals(AppConstants.Cashier.NO_DELETE_PURCHASE))
        {
            updatePurchaseItemView(result.isProductType());
        }
        else if(message.equals(AppConstants.Cashier.DELETE_PURCHASE))
        {
            addToBacketView();
        }
        else if(message.equals(AppConstants.Cashier.AUTOMATIC_READ_WEIGHT))
        {
            automaticReadWeight();
        }
        else if(message.equals(AppConstants.Cashier.OPEN_ADD_WEIGHT))
        {
            txtAddWeightDialog.setText("");
            addWeightDialog.setVisible(true);
        }
    }
    
    private void automaticReadWeight()
    {
        startLoading();
         new SwingWorker<ResultAddProduct, Void>() {
            @Override
            protected ResultAddProduct doInBackground() {
                ResultAddProduct resultAddProduct = readWeight(isReturnProductWeight);
                return resultAddProduct;
            }

            @Override
            protected void done() {
                stopLoading();
                try {
                    ResultAddProduct result = get();
                    processResultAddProduct(result);
                } catch (InterruptedException | ExecutionException ex) {
                    ex.printStackTrace();
                }
            }
         }.execute();
    }
    
    private void proccessManualWeight()
    {
        startLoading();
         new SwingWorker<ResultAddProduct, Void>() {
            @Override
            protected ResultAddProduct doInBackground() throws Exception {
                return addProduct(producWaitForWeight,true,isReturnProductWeight);
            }

            @Override
            protected void done() {
               stopLoading();
               try{
                  ResultAddProduct result = get(); 
                  processResultAddProduct(result);
               }
               catch(InterruptedException | ExecutionException ex)
               {
                   ex.printStackTrace();
               }
            }
            
         }.execute();
            
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
        scaleStar = scale.start();
        
        if(scaleStar)
        {
            Util.logInformation(TAG, "initScale", "start true");
            ScaleIP scaleIP = new ScaleIP(cashInfo.getCashIp(), scale);
            listScaleIp.add(scaleIP);
        }
        else
        {
            Util.logInformation(TAG, "initScale", "start false");
            scale =null;
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
    
    
    private void calculateAmountTotal()
    {
        long amount = 0;
        if (purchaseitems != null) {
            for (Purchaseitem purchaseitem : purchaseitems) {
                if (purchaseitem.getProduct().getProducttype().getProdtypeValue().equals("Sin empaquetar")) {
                    String unity = purchaseitem.getProduct().getUnity().getUniAbbreviation();
                    switch (unity) {
                        case "gr":
                            double fv = purchaseitem.getPurItemQuantity() / 1000.0;
                            fv = fv * purchaseitem.getPriceValue();
                            amount = amount + Math.round(fv);
                    }
                }
                else
                    amount = amount + (purchaseitem.getPurItemQuantity() * purchaseitem.getPriceValue());
            }
        }
        lblTotal.setText("$"+Util.getFormatPrice(amount)+" ");
    }
    
    
    private void changeQuantityProduct()
    {
        startLoading();
        changeQuatityDialog.setVisible(false);
        new SwingWorker<Boolean, String>(){
            @Override
            protected Boolean doInBackground(){
                String quantity = txtChangeQuantityDialog.getText();
                if (!quantity.isEmpty()){
                    try {
                        int q = Integer.parseInt(quantity);
                        if (q > 0) {
                            int index = purchaseitems.size() - 1;
                            if (purchaseitems.get(index).getPurItemQuantity() < 0) {
                                q = q * -1;
                            }
                            purchaseitems.get(index).setPurItemQuantity(q);
                            purchaseitemModel.update(purchaseitems.get(index));
                            return true;
                        }

                    } catch (NumberFormatException e) {
                         
                    }
                    return false;
                }
                return false;
            }
            @Override
            protected void done() {
                stopLoading();
                try {
                    boolean result = get();
                    
                    if(result)
                    {
                        updatePurchaseItemView(false);
                        changeQuatityDialog.dispose();
                    }
                    else
                    {
                        changeQuatityDialog.setVisible(true);
                    }
                } catch (InterruptedException | ExecutionException ex) {
                   ex.printStackTrace();
                }
            }
        }.execute();
    }
    
    
    public void removeProduct()
    {
        startLoading();
        removeProductDialog.setVisible(false);
        new SwingWorker<Boolean, String>(){
            @Override
            protected Boolean doInBackground(){
                String codeForRemove = txtBarCodeRemoveProductDialog.getText();
                if(!codeForRemove.isEmpty())
                {
                    Product product = productModel.findByBarCode(codeForRemove);
                    if (product != null) {
                        for (int i = purchaseitems.size() - 1; i >= 0; i--) {
                            Purchaseitem p = purchaseitems.get(i);
                            if (p.getProduct().getProdBarCode().equals(product.getProdBarCode())) {
                                purchaseitems.remove(i);
                            }
                        }
                        purchaseitemModel.deleteByPurIdAndProdId(purchase.getPurId(), product.getProdId());
                        if (purchaseitems.isEmpty()) {
                            purchaseModel.delete(purchase);
                            purchaseitems = null;
                            purchase = null;
                        }
                        return true;
                    }
                }
                return false;
            }
            @Override
            protected void done() {
                stopLoading();
                try {
                    boolean result = get();
                    if(result)
                    {
                        if(purchase== null)
                        {
                            addToBacketView();
                            removeProductDialog.dispose();
                        }
                        else{
                           boolean isProductType = purchaseitems.get(purchaseitems.size() -1).getProduct().getProducttype().getProdtypeValue().equals("Sin empaquetar");
                           updatePurchaseItemView(isProductType); 
                        }
                        
                    }
                    else
                    {
                        removeProductDialog.setVisible(true);
                    }
                } catch (InterruptedException | ExecutionException ex) {
                   ex.printStackTrace();
                }
            }
        }.execute();
    }
    
    private void openSelectProduct()
    {
        startLoading();
        new SwingWorker<List, Void>(){
            @Override
            protected List doInBackground(){
                List Products= productModel.findAllProducts();
                return Products;
            }

            @Override
            protected void done() {
                try {
                    productList = get();
                    DefaultTableModel model = (DefaultTableModel) selectProductDialogTable.getModel();
                    model.setRowCount(0);
                    for(int i=0;i<productList.size();i++)
                    {
                        Object[] objects = (Object[])productList.get(i);
                        Product p = (Product)objects[0];
                        Price pr = (Price)objects[1];
                        Brand b = (Brand)objects[2];
                        Category c = (Category)objects[3];
                        Unity u = (Unity)objects[4];
                        
                        Object[] row = {p.getProdBarCode(),p.getProdName(),
                            p.getProdUnitValue()+" "+u.getUniAbbreviation(),
                            c.getCatName(),b.getBrandName(),Util.getFormatPrice(pr.getPriceValue())};
                        
                        model.addRow(row);
                    }
                    stopLoading();
                    selectProductDialog.setLocationRelativeTo(null);
                    txtSelectProductDialog.setText("");
                    selectProductDialog.setVisible(true);
                    productList = null;
                } catch (InterruptedException | ExecutionException ex) {
                   ex.printStackTrace();
                }
            }
            
        }.execute();
    }
    
    
    private void resumeSale()
    {
        startLoading();
        new SwingWorker<List<Purchase>, Void>(){
            @Override
            protected List<Purchase> doInBackground(){
                List<Purchase> purchases= purchaseModel.findSaleForResume(cashier.getUsId());
                return purchases;
            }

            @Override
            protected void done() {
                try {
                    purchasesResume = get();
                    DefaultTableModel model = (DefaultTableModel) resumeDialogTable.getModel();
                    model.setRowCount(0);
                    for(Purchase purchase: purchasesResume)
                    {
                        
                        Object[] row = {purchase.getPurDate()};
                        
                        model.addRow(row);
                    }
                    stopLoading();
                    resumeDialog.setLocationRelativeTo(null);
                    resumeDialog.setVisible(true);
                } catch (InterruptedException | ExecutionException ex) {
                   ex.printStackTrace();
                }
            }
            
        }.execute();
    }
    
    private void resumePurchase(int index)
    {
        purchase = purchasesResume.get(0);
        purchasesResume = null;
        startLoading();
        resumeDialog.dispose();
        new SwingWorker<List<Purchaseitem>, Void>(){
            @Override
            protected List<Purchaseitem> doInBackground(){
                return purchaseitemModel.findByPurId(purchase.getPurId());
            }

            @Override
            protected void done() {
                stopLoading();
                try {
                    purchaseitems = get();
                    boolean isProductType = purchaseitems.get(purchaseitems.size()-1).getProduct().getProducttype().getProdtypeValue().equals("Sin empaquetar");
                    updatePurchaseItemView(isProductType);
                    if(purchase.getClient()!=null)
                    {
                        lblNameClient.setText(purchase.getClient().getCliName()+" "+purchase.getClient().getCliLastName());
                    }
                } catch (InterruptedException | ExecutionException ex) {
                   ex.printStackTrace();
                }
            }
            
        }.execute();
    }
    
    
    private void currentDayTotal()
    {
        startLoading();
        new SwingWorker<Integer, Void>(){
            @Override
            protected Integer doInBackground(){
                Calendar now = Calendar.getInstance();
                now.set(Calendar.HOUR_OF_DAY, 0);
                now.set(Calendar.MINUTE, 0);
                now.set(Calendar.SECOND, 0);
                Date initial = now.getTime();
                now.set(Calendar.HOUR_OF_DAY, 23);
                now.set(Calendar.MINUTE, 59);
                now.set(Calendar.SECOND, 59);
                Date end = now.getTime();
                List<Purchase> plist = purchaseModel.findPurshaseUsIdAndDay(cashier.getUsId(), initial, end);
                int amount = 0;
                for (Purchase p : plist) {
                    amount = amount + p.getPurFinalAmount();
                }
                return amount;
            }

            @Override
            protected void done() {
                stopLoading();
                try {
                    int amount = get();
                    lblTotalDialog.setText("Total: "+ Util.getFormatPrice(amount));
                    totalDialog.setLocationRelativeTo(null);
                    totalDialog.setVisible(true);
                } catch (InterruptedException | ExecutionException ex) {
                   ex.printStackTrace();
                }
            }
            
        }.execute();
    }
    
    
    private void addClient()
    {
        startLoading();
        addClientDialog.setVisible(false);
        new SwingWorker<Boolean, Void>(){
            @Override
            protected Boolean doInBackground(){
                String identification = txtAddClientDialog.getText();
                if (!identification.isEmpty()) {
                    Client client = clientModel.findByCliIdentity(identification);
                    if (client != null) {
                        purchase.setClient(client);
                        purchaseModel.update(purchase);
                        return true;
                    }
                }
                return false;
            }

            @Override
            protected void done() {
                stopLoading();
                try {
                    boolean resp = get();
                    if(resp)
                    {
                        lblNameClient.setText(purchase.getClient().getCliName()+" "+purchase.getClient().getCliLastName());
                        addClientDialog.dispose();
                    }
                    else
                    {
                        addClientDialog.setLocationRelativeTo(null);
                        addClientDialog.setVisible(true);
                    }
                } catch (InterruptedException | ExecutionException ex) {
                   ex.printStackTrace();
                }
            }
            
        }.execute();
    }
    
    private void receivedAmount()
    {
        startLoading();
        receivedAmountDialog.setVisible(false);
        new SwingWorker<Boolean, Void>() {
                @Override
                protected Boolean doInBackground() {
                    rAmount = txtReceivedAmountDialog.getText();
                    if (!rAmount.isEmpty()) {
                        try {
                            int rm = Integer.parseInt(rAmount);
                            if (rm >= 0) {
                                generatePdf = null;
                                if (cashInfo == null) {
                                    JSONParser parser = new JSONParser();
                                    try {
                                        JSONObject jSONObject = (JSONObject) parser.parse(new FileReader(Util.CASHINFO));
                                        cashInfo = new CashInfo();
                                        cashInfo.setCashName((String) jSONObject.get("Name"));
                                        cashInfo.setCashIp((String) jSONObject.get("cashIP"));
                                        cashInfo.setCashPaperSize(Integer.parseInt(jSONObject.get("cashPaperSize") + ""));
                                        cashInfo.setCashPrintName((String) jSONObject.get("cashPrintName"));
                                        cashInfo.setCashScalePortSerialName((String) jSONObject.get("cashScalePortSerialName"));
                                        cashInfo.setCashPrintCommandOpenCashDrawer((String) jSONObject.get("cashPrintCommandOpenCashDrawer"));
                                    } catch (IOException | ParseException ex) {
                                        ex.printStackTrace();
                                    }
                                }
                                if (cashInfo != null) {
                                    generatePdf = new GeneratePdf(cashInfo.getCashPaperSize());
                                    generatePdf.firstStringHtml(purchase);
                                }
                                int amount = getAmountTotalIntegerFormat();
                                if (rm >= amount) {
                                    int r = rm - amount;
                                    String id = purchase.getPurId() + "";
                                    int length = id.length();
                                    String barCode = "100000000000000000000000000000";
                                    length = 30 - length;
                                    barCode = barCode.substring(0, length);
                                    barCode = barCode + id;
                                    if (generatePdf != null) {
                                        generatePdf.thirdStringHtml(amount, rm, r, barCode, purchase, totalProductIva0, totalProductIva5, totalProductIva19);
                                        generatePdf.generatePdf(purchase, barCode);
                                    }
                                    refund = Util.getFormatPrice(r);
                                    receivedAmount = Util.getFormatPrice(rm);
                                    total = Util.getFormatPrice(amount);
                                    purchase.setPurFinalAmount(amount);
                                    purchase.setPurReceivedAmount(rm);
                                    purchase.setPurState(1);
                                    purchaseModel.update(purchase);
                                    for (Purchasetotal pt : purchasetotals) {
                                        purchasetotalModel.create(pt);
                                    }
                                    purchaseitems = null;
                                    purchase = null;
                                    purchasetotals = null;
                                    return true;
                                }
                            }
                        } catch (NumberFormatException e) {
                        }
                    }
                    return false;
                }

                @Override
                protected void done() {
                    stopLoading();
                    try {
                        boolean resp = get();
                        if (resp) {
                            openCashView();
                            receivedAmountDialog.dispose();
                            txtSuccessFullPaymentDialog.setText("");
                            successfulPaymentDialog.setLocationRelativeTo(null);
                            successfulPaymentDialog.setVisible(true);
                        } else {
                            receivedAmountDialog.setLocationRelativeTo(null);
                            receivedAmountDialog.setVisible(true);
                        }
                    } catch (InterruptedException | ExecutionException ex) {
                        ex.printStackTrace();
                    }
                }

            }.execute();
    }
    
    private int getAmountTotalIntegerFormat()
    {
        purchasetotals = new ArrayList();
        long amount = 0;
        totalProductIva0=0;
        totalProductIva5=0;
        totalProductIva19=0;
        if (purchaseitems != null) {
            for (Purchaseitem purchaseitem : purchaseitems) {
                if(generatePdf!=null)
                {
                    generatePdf.secondStringHtml(purchaseitem);
                }
                Purchasetotal purchasetotal = getPurchasetotal(purchaseitem.getProduct().getOwner());
                double iva = 0.0;
                double vIva = 0.0;
                long gain = 0;
                long v = 0;
                if(purchaseitem.getIva()== 5)
                {
                    iva = 1.05;
                }
                else if(purchaseitem.getIva()== 19)
                {
                    iva = 1.19;
                }
                
                if(purchasetotal == null)
                {
                    purchasetotal = new Purchasetotal();
                    purchasetotal.setOwner(purchaseitem.getProduct().getOwner());
                    purchasetotal.setPurchase(purchase);
                    purchasetotal.setPurToTotal(0);
                    purchasetotal.setPurToGain(0);
                    purchasetotal.setPurToIva(0);
                    purchasetotals.add(purchasetotal);
                }
                int q;
                if(purchaseitem.getPurItemQuantity()>0)
                {
                    q = purchaseitem.getPurItemQuantity();
                }
                else{
                    q = purchaseitem.getPurItemQuantity()*-1;
                }
                if (purchaseitem.getProduct().getProducttype().getProdtypeValue().equals("Sin empaquetar")) {
                    String unity = purchaseitem.getProduct().getUnity().getUniAbbreviation();
                    switch (unity) {
                        case "gr":
                            double fv = q / 1000.0;
                            double pfv= fv * purchaseitem.getPricePurValue();
                            fv = fv * purchaseitem.getPriceValue();
                            v = Math.round(fv);
                            long pv = Math.round(pfv);
                            gain = v - pv;
                            
                            if(purchaseitem.getIva() > 0)
                            {
                                double vWIva = gain / iva;
                                vIva = gain - vWIva;
                            }
                            else
                            {
                                vIva = 0;
                            }
                            
                            if(purchaseitem.getPurItemQuantity()<0)
                            {
                                vIva = vIva *-1;
                                v = v*-1;
                                gain = gain * -1;
                            }
                            amount = amount + v;
                           
                    }
                }
                else{
                    v = q * purchaseitem.getPriceValue();
                    int pv = q * purchaseitem.getPricePurValue();
                    gain = v - pv;
                    if(purchaseitem.getIva() > 0)
                    {
                        double vWIva = gain/iva;
                        vIva = gain - vWIva;
                    }
                    else
                    {
                        vIva = 0;
                    }
                    if(purchaseitem.getPurItemQuantity()<0)
                    {
                        vIva = vIva *-1;
                        v = v*-1;
                        gain = gain * -1;
                    }
                    
                    amount = amount + v;
                }
                
                if(purchaseitem.getIva()==0)
                {
                    totalProductIva0=totalProductIva0 + v;
                }
                else if(purchaseitem.getIva()==5)
                {
                    totalProductIva5=totalProductIva5+v;
                }
                else if(purchaseitem.getIva()==19)
                {
                    totalProductIva19=totalProductIva19+v;
                }
                purchasetotal.setPurToTotal((int)(purchasetotal.getPurToTotal() + v));
                purchasetotal.setPurToGain((int)(purchasetotal.getPurToGain()+ gain));
                purchasetotal.setPurToIva((float)(purchasetotal.getPurToIva()+ vIva));
            }
        }
        return (int)amount;
    }
    
    private Purchasetotal getPurchasetotal(Owner owner)
    {
        if(purchasetotals.size()>0)
        {
            for(Purchasetotal pt: purchasetotals)
            {
                if(pt.getOwner().getOwnId() == owner.getOwnId())
                {
                    return pt;
                }
            }
        }
        return null;
    }
    
    private void okAction()
    {
        startLoading();
        successfulPaymentDialog.setVisible(false);
        new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() {
                String action = txtSuccessFullPaymentDialog.getText();
                if (!action.isEmpty()) {
                    try {
                        int a = Integer.parseInt(action);
                        if (a == 1)//print
                        {
                            if (cashInfo != null) {
                                //print
                                PrintPdf printPdf = new PrintPdf(cashInfo.getCashPaperSize());
                                printPdf.imprimirTicket(generatePdf.getFicheroPdf(), cashInfo.getCashPrintName());
                                printPdf.openCashDrawer(cashInfo.getCashPrintName(), cashInfo.getCashPrintCommandOpenCashDrawer());
                            }
                            return true;
                        } else if (a == 2)//end
                        {
                            if (cashInfo != null) {
                                PrintPdf printPdf = new PrintPdf();
                                printPdf.openCashDrawer(cashInfo.getCashPrintName(), cashInfo.getCashPrintCommandOpenCashDrawer());
                            }
                            return true;
                        }
                        return false;

                    } catch (NumberFormatException e) {

                    }
                }
                return false;
            }
            @Override
            protected void done() {
                stopLoading();
                try {
                    boolean resp = get();
                    if(resp)
                    {
                        lblTotalEndPaymentDialog.setText("Total: "+total+" ");
                        lblQuantityEndPaymentDialog.setText("Cantidad recibida: "+receivedAmount+" ");
                        lblReturnEndPaymentDialog.setText("Devolución: "+refund+" ");
                        endPaymentDialog.setVisible(true);
                        successfulPaymentDialog.dispose();
                    }
                    else
                    {
                        txtSuccessFullPaymentDialog.setText("");
                        successfulPaymentDialog.setLocationRelativeTo(null);
                        successfulPaymentDialog.setVisible(true);
                    }
                } catch (InterruptedException | ExecutionException ex) {
                   ex.printStackTrace();
                }
            }
            
        }.execute();
    }
    
    private void startLoading() {
        openBtn.setEnabled(false);
        exitBtn.setEnabled(false);
        cancelBtn.setEnabled(false);
        paymentBtn.setEnabled(false);
        selectBtn.setEnabled(false);
        changeQuantitybtn.setEnabled(false);
        removeProductBtn.setEnabled(false);
        addToBacketBtn.setEnabled(false);
        resumeBtn.setEnabled(false);
        searchProductBtn.setEnabled(false);
        returnProductBtn.setEnabled(false);
        addClientBtn.setEnabled(false);
        searchClientBtn.setEnabled(false);
        totalBtn.setEnabled(false);
        codeTxt.setEnabled(false);
        btnReadCode.setEnabled(false);
        btnReadCode.setIcon(loadingIcon);
    }
    
    private void stopLoading()
    {
        openBtn.setEnabled(true);
        exitBtn.setEnabled(true);
        cancelBtn.setEnabled(true);
        paymentBtn.setEnabled(true);
        selectBtn.setEnabled(true);
        changeQuantitybtn.setEnabled(true);
        removeProductBtn.setEnabled(true);
        addToBacketBtn.setEnabled(true);
        resumeBtn.setEnabled(true);
        searchProductBtn.setEnabled(true);
        returnProductBtn.setEnabled(true);
        addClientBtn.setEnabled(true);
        searchClientBtn.setEnabled(true);
        totalBtn.setEnabled(true);
        codeTxt.setEnabled(true);
        btnReadCode.setEnabled(true);
        btnReadCode.setIcon(searchIcon);
        codeTxt.requestFocus();
    }
    
}

