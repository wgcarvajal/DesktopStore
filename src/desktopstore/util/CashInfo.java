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
public class CashInfo {
    
    private String cashName;
     private String cashIp;
     private int cashPaperSize;
     private String cashPrintName;
     private String cashScalePortSerialName;
     private String cashPrintCommandOpenCashDrawer;
     
     
     public CashInfo() {
    }

    public CashInfo(String cashName, String cashIp, int cashPaperSize, String cashPrintName, String cashScalePortSerialName, String cashPrintCommandOpenCashDrawer) {
       this.cashName = cashName;
       this.cashIp = cashIp;
       this.cashPaperSize = cashPaperSize;
       this.cashPrintName = cashPrintName;
       this.cashScalePortSerialName = cashScalePortSerialName;
       this.cashPrintCommandOpenCashDrawer = cashPrintCommandOpenCashDrawer;
    }

    public String getCashName() {
        return cashName;
    }

    public void setCashName(String cashName) {
        this.cashName = cashName;
    }

    public String getCashIp() {
        return cashIp;
    }

    public void setCashIp(String cashIp) {
        this.cashIp = cashIp;
    }

    public int getCashPaperSize() {
        return cashPaperSize;
    }

    public void setCashPaperSize(int cashPaperSize) {
        this.cashPaperSize = cashPaperSize;
    }

    public String getCashPrintName() {
        return cashPrintName;
    }

    public void setCashPrintName(String cashPrintName) {
        this.cashPrintName = cashPrintName;
    }

    public String getCashScalePortSerialName() {
        return cashScalePortSerialName;
    }

    public void setCashScalePortSerialName(String cashScalePortSerialName) {
        this.cashScalePortSerialName = cashScalePortSerialName;
    }

    public String getCashPrintCommandOpenCashDrawer() {
        return cashPrintCommandOpenCashDrawer;
    }

    public void setCashPrintCommandOpenCashDrawer(String cashPrintCommandOpenCashDrawer) {
        this.cashPrintCommandOpenCashDrawer = cashPrintCommandOpenCashDrawer;
    }
    
    
    
    
}
