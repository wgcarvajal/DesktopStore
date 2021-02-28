/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package desktopstore.util;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import entities.Purchase;
import entities.Purchaseitem;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 * @author aranda
 */
public class GeneratePdf {
    
    private float sizeHeightPage;
    private float lineLenght;
    private String html;
    private String printReceipt;
    private ByteArrayOutputStream ficheroPdf;
    private float sizeWidthPage;
    private float right;
    private float left;
    private float top;
    private float bottom;
    private float maxPaperHeight;
    private String fontTitleSize;
    private String fontSize;

    
    
    public GeneratePdf(int paperSize){
        if(paperSize==80)
        {
            sizeWidthPage = 226;
            right=10;
            left = 10;
            top = 0;
            bottom = 20;
            maxPaperHeight = 850;
            fontTitleSize = "10pt";
            fontSize = "8pt";
            lineLenght = 11.6f;
            sizeHeightPage = 0 + 20 + 13 +(lineLenght*21)+20;
        }
    }
    
    public void generatePDF(Purchase purchase,List <Purchaseitem>purchaseitems) {
        firstStringHtml(purchase);
        long amount = 0;
        long totalProductIva0 = 0;
        long totalProductIva5 = 0;
        long totalProductIva19 = 0;
        for (Purchaseitem p : purchaseitems) {
            secondStringHtml(p);
            double iva = 0.0;
            double vIva = 0.0;
            long gain = 0;
            long v = 0;
            if (p.getIva() == 5) {
                iva = 1.05;
            } else if (p.getIva() == 19) {
                iva = 1.19;
            }

            if (p.getProduct().getProducttype().getProdtypeValue().equals("Sin empaquetar")) {
                String unity = p.getProduct().getUnity().getUniAbbreviation();
                switch (unity) {
                    case "gr":
                        double fv = p.getPurItemQuantity() / 1000.0;
                        double pfv = fv * p.getPricePurValue();
                        fv = fv * p.getPriceValue();
                        v = Math.round(fv);
                        long pv = Math.round(pfv);
                        gain = v - pv;

                        if (p.getIva() > 0) {
                            double vWIva = gain / iva;
                            vIva = gain - vWIva;
                        } else {
                            vIva = 0;
                        }
                        amount = amount + v;
                }
            } else {
                v = p.getPurItemQuantity() * p.getPriceValue();
                int pv = p.getPurItemQuantity() * p.getPricePurValue();
                gain = v - pv;
                if (p.getIva() > 0) {
                    double vWIva = gain / iva;
                    vIva = gain - vWIva;
                } else {
                    vIva = 0;
                }
                amount = amount + v;
            }

            if (p.getIva() == 0) {
                totalProductIva0 = totalProductIva0 + v;
            } else if (p.getIva() == 5) {
                totalProductIva5 = totalProductIva5 + v;
            } else if (p.getIva() == 19) {
                totalProductIva19 = totalProductIva19 + v;
            }
        }
        String idString = purchase.getPurId() + "";
        int length = idString.length();
        String barCode = "100000000000000000000000000000";
        length = 30 - length;
        barCode = barCode.substring(0, length);
        barCode = barCode + idString;
        thirdStringHtml(purchase.getPurFinalAmount(), purchase.getPurReceivedAmount(), (purchase.getPurReceivedAmount() - purchase.getPurFinalAmount()), barCode, purchase, totalProductIva0, totalProductIva5, totalProductIva19);
        generatePdf(purchase, barCode);
    }
    
    public void generatePdf(Purchase purchase,String barCode)
    {  
        Document document = new Document(new Rectangle(0,0,sizeWidthPage,sizeHeightPage));
        if(sizeHeightPage>maxPaperHeight)
            document = new Document(new Rectangle(0,0,sizeWidthPage,maxPaperHeight));
        
        document.setMargins(left, right, top, bottom);
        try
        {
            Date date = purchase.getPurDate();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            String css = "@font-face{font-family: 'fakereceipt'; src: url('fakereceipt.ttf');}body{font-family:'fakereceipt';}div{padding:0px 0px 0px 0px;margin: 0px 0px 0px 0px;}";
            XMLWorkerFontProvider fontProvider = new XMLWorkerFontProvider(Util.FONTDIR);
            fontProvider.register(Util.FONTDIR + "fakereceipt.ttf", "fakereceipt");
            ficheroPdf = new ByteArrayOutputStream();
            PdfWriter pdfWriter = PdfWriter.getInstance(document, ficheroPdf);
            document.open();
            InputStream is = new ByteArrayInputStream(html.getBytes("UTF-8"));
            InputStream cis = new ByteArrayInputStream(css.getBytes("UTF-8"));
            XMLWorkerHelper xMLWorkerHelper = XMLWorkerHelper.getInstance();
            Barcode128 barcode128 = new Barcode128();
            barcode128.setCode(barCode);
            barcode128.setChecksumText(true);
            barcode128.setCodeType(Barcode128.CODE128);
            barcode128.setFont(null);
            PdfContentByte pdfContentByte = pdfWriter.getDirectContent();
            BaseColor baseColor = new BaseColor(21, 21, 21);
            Image code128Image = barcode128.createImageWithBarcode(pdfContentByte, baseColor, null);
            code128Image.scaleAbsolute(sizeWidthPage -(right+left) ,20);
            xMLWorkerHelper.parseXHtml(pdfWriter, document,is,cis,Charset.forName("UTF-8"),fontProvider);
            document.add(code128Image);
        }
        catch(DocumentException | IOException ex)
        {
            System.out.println("error:"+ex.getMessage());
            
        }
        document.close();
        
    }
    
    public void firstStringHtml(Purchase purchase)
    {  
        html = "<html>" +
                      "<head>"                      
                    + "</head>"
                    + "<body style='color: #151515;'>"
                        +"<div style='font-size: "+fontTitleSize+";text-align:center;font-weight:bold;'>"+Util.nameStore+"</div>"
                        +"<div style='font-size: "+fontSize+";text-align:center;'>"+Util.addressStore+"</div>"
                        +"<div style='font-size: "+fontSize+";text-align:center;'>NIT "+Util.nitStore+"</div>"
                        +"<div style='font-size: "+fontSize+";text-align:center;'>GERENTE: "+Util.bossStore+"</div>"
                        +"<div style='font-size: "+fontSize+";text-align:center;'>TELEFONO: "+Util.phoneStore+"</div>"
			+"<div style='font-size: "+fontSize+";text-align:center;'>FECHA DE EXPEDICION: "+Util.getFormatDate(purchase.getPurDate())+"</div>"
                        +"<div style='width:100%;'>"
                            +"<div style='font-size: "+fontSize+";width:50%;float:left;text-align:center;'>Descripción</div>"
                            +"<div style='font-size: "+fontSize+";width:20%;float:left;text-align:center;'>UM</div>"
                            +"<div style='font-size: "+fontSize+";width:36%;float:left;text-align:center;'>CNT</div>"
                            +"<div style='font-size: "+fontSize+";width:100%;float:left;text-align:center;'>Valor</div>"
                            +"<div style='clear:left;'></div>"
                            +"<div style='font-size: "+fontSize+";width:50%;float:left;padding-right:1px;padding-left:1px;'>------------------</div>"
                            +"<div style='font-size: "+fontSize+";width:20%;float:left;padding-right:1px;padding-left:1px;'>---</div>"
                            +"<div style='font-size: "+fontSize+";width:36%;float:left;padding-right:1px;padding-left:1px;'>-----</div>"
                            +"<div style='font-size: "+fontSize+";width:90%;float:left;padding-right:1px;padding-left:1px;'>--------</div>"
                            +"<div style='font-size: "+fontSize+";width:100%;float:left;padding-right:1px;padding-left:1px;'></div>"
                            +"<div style='clear:left;'></div>";
    }
    
    public void secondStringHtml(Purchaseitem purchaseitem)
    {
        sizeHeightPage = sizeHeightPage +lineLenght;
        html =html
        +"<div style='text-align:left;font-size: "+fontSize+";width:50%;float:left;padding-right:1px;padding-left:1px;'>"+purchaseitem.getProduct().getProdNameBill()+"</div>";
        String um = "";
        double q = 0;
        if(purchaseitem.getProduct().getProducttype().getProdtypeValue().equals("Sin empaquetar"))
        {
            if(purchaseitem.getProduct().getUnity().getUniName().equals("Gramos"))
            {
                um = "KG";
                q = purchaseitem.getPurItemQuantity() / 1000.0;
            }
        }
        else
        {
            um = "UND";
            q = purchaseitem.getPurItemQuantity();
        }
        DecimalFormat df = new DecimalFormat("0.00");
        html= html +"<div style='text-align:left;font-size: "+fontSize+";width:20%;float:left;padding-right:1px;padding-left:1px;'>"+um+"</div>"
        +"<div style='text-align:right;font-size: "+fontSize+";width:36%;float:left;'>"+df.format(q)+"</div>";
        long precio=purchaseitem.getPriceValue();
        boolean productType = purchaseitem.getProduct().getProducttype().getProdtypeValue().equals("Empaquetado");
        if(productType)
        {
            precio = precio * purchaseitem.getPurItemQuantity();
        }
        else{
            double fv = purchaseitem.getPurItemQuantity()/1000.0;
            fv = fv * precio;
            precio= Math.round(fv);
        }
        html = html+"<div style='text-align:right;font-size: "+fontSize+";width:90%;float:left;padding-right:1px;padding-left:1px;'>"+precio+"</div>"
        +"<div style='text-align:center;font-size: "+fontSize+";width:100%;float:left;padding-right:1px;padding-left:1px;'>"+Util.evaluateIva(purchaseitem.getIva())+"</div>"
        +"<div style='clear:left;'></div>";
    }
    
    public void thirdStringHtml(int amount,int ra,int change,String barCode,Purchase purchase,long totalProductIva0,long totalProductIva5,long totalProductIva19)
    {
        html = html + "<div style='font-weight:bold;font-size: "+fontSize+";width:65%;float:left;padding-right:1px;padding-left:1px;'>+ +SUBTOTAL/TOTAL - - ></div>"
                            +"<div style='font-weight:bold;font-size: "+fontSize+";width:100%;float:left;padding-right:1px;padding-left:1px;'>$ "+Util.getFormatPrice(amount)+"</div>"
                            +"<div style='clear:left;'></div>"
                            +"<div style='text-align:center;font-size: "+fontSize+";width:65%;float:left;padding-right:1px;padding-left:1px;'>PAGO/EFECTIVO</div>"
                            +"<div style='font-size: "+fontSize+";width:100%;float:left;padding-right:1px;padding-left:1px;'>$ "+Util.getFormatPrice(ra)+"</div>"
                            +"<div style='clear:left;'></div>"
                            +"<div style='text-align:center;font-size: "+fontSize+";width:65%;float:left;padding-right:1px;padding-left:1px;'>CAMBIO</div>"
                            +"<div style='font-size: "+fontSize+";width:100%;float:left;padding-right:1px;padding-left:1px;'>$ "+Util.getFormatPrice(change)+"</div>"
                            +"<div style='clear:left;'></div>"
                            +"<div style='font-size: "+fontSize+";width:100%;padding-right:1px;padding-left:1px;'>= = = = = = = = = = = = = = = = = = =</div>";
                            if(purchase.getClient()!=null)
                            {
                                sizeHeightPage = sizeHeightPage +lineLenght;
                                html=html+"<div style='font-size: "+fontSize+";'>Nombre Cte: "+Util.upperCase(processReceiptName(purchase.getClient().getCliName())+" "+processReceiptLastName(purchase.getClient().getCliLastName()))+"</div>";
                            }
                            
                            html=html+"<div style='font-size: "+fontSize+";width:100%;padding-right:1px;padding-left:1px;'>= = = = = = = = = = = = = = = = = = =</div>"
                            +"<div style='text-align:center;font-size: "+fontSize+";width:100%;padding-right:1px;padding-left:1px;'>+ + IMPUESTOS + +</div>"
                            +"<div style='font-size: "+fontSize+";width:25%;float:left;text-align:center;'>Tipo</div>"
                            +"<div style='font-size: "+fontSize+";width:33%;float:left;text-align:center;'>Compra</div>"
                            +"<div style='font-size: "+fontSize+";width:50%;float:left;text-align:center;'>Base/Imp</div>"
                            +"<div style='font-size: "+fontSize+";width:100%;float:left;text-align:center;'>Imp</div>"
                            +"<div style='clear:left;'></div>"
                            +"<div style='font-size: "+fontSize+";width:25%;float:left;padding-left:1px;'>---------</div>"
                            +"<div style='font-size: "+fontSize+";width:33%;float:left;padding-left:1px;'>---------</div>"
                            +"<div style='font-size: "+fontSize+";width:50%;float:left;padding-left:1px;'>---------</div>"
                            +"<div style='font-size: "+fontSize+";width:100%;float:left;padding-left:1px;'>---------</div>"
                            +"<div style='clear:left;'></div>";
                            long totalBaseImp =0;
                            long totalImp =0;
                            if(totalProductIva5>0)
                            {
                               sizeHeightPage = sizeHeightPage +lineLenght;
                               html=html+"<div style='text-align:center;font-size: "+fontSize+";width:25%;float:left;padding-right:1px;padding-left:1px;'>A= 5%</div>"+
                                        "<div style='text-align:center;font-size: "+fontSize+";width:33%;float:left;padding-right:1px;padding-left:1px;'>"+Util.getFormatPrice(totalProductIva5)+"</div>";
                                        double iva = totalProductIva5 / 1.05;
                                        long iva5 = totalProductIva5 - Math.round(iva);
                                        totalBaseImp = (totalProductIva5 - iva5);
                                        totalImp = iva5;
                                        html=html+"<div style='text-align:center;font-size: "+fontSize+";width:50%;float:left;padding-right:1px;padding-left:1px;'>"+Util.getFormatPrice(totalProductIva5 - iva5)+"</div>"+
                                        "<div style='text-align:center;font-size: "+fontSize+";width:100%;float:left;padding-right:1px;padding-left:1px;'>"+Util.getFormatPrice(iva5)+"</div>"+
                                        "<div style='clear:left;'></div>";
                            }
                            
                            if(totalProductIva19>0)
                            {
                               sizeHeightPage = sizeHeightPage +lineLenght;
                               html=html+"<div style='text-align:center;font-size: "+fontSize+";width:25%;float:left;padding-right:1px;padding-left:1px;'>B= 19%</div>"+
                                        "<div style='text-align:center;font-size: "+fontSize+";width:33%;float:left;padding-right:1px;padding-left:1px;'>"+Util.getFormatPrice(totalProductIva19)+"</div>";
                                        double iva = totalProductIva19 / 1.19f;
                                        long iva19 = totalProductIva19 - Math.round(iva);
                                        totalBaseImp = totalBaseImp +(totalProductIva19 - iva19);
                                        totalImp = totalImp +iva19;
                                        html=html+"<div style='text-align:center;font-size: "+fontSize+";width:50%;float:left;padding-right:1px;padding-left:1px;'>"+Util.getFormatPrice(totalProductIva19 - iva19)+"</div>"+
                                        "<div style='text-align:center;font-size: "+fontSize+";width:100%;float:left;padding-right:1px;padding-left:1px;'>"+Util.getFormatPrice(iva19)+"</div>"+
                                        "<div style='clear:left;'></div>";
                            }
                            
                            if(totalProductIva0>0)
                            {
                               sizeHeightPage = sizeHeightPage +lineLenght;
                               html=html+"<div style='text-align:center;font-size: "+fontSize+";width:25%;float:left;padding-right:1px;padding-left:1px;'>+SIN IVA</div>"+
                                        "<div style='text-align:center;font-size: "+fontSize+";width:33%;float:left;padding-right:1px;padding-left:1px;'>"+Util.getFormatPrice(totalProductIva0)+"</div>"+
                                        "<div style='text-align:center;font-size: "+fontSize+";width:50%;float:left;padding-right:1px;padding-left:1px;'></div>"+
                                        "<div style='text-align:center;font-size: "+fontSize+";width:100%;float:left;padding-right:1px;padding-left:1px;'></div>"+
                                        "<div style='clear:left;'></div>";
                            }
                            
                             html=html+"<div style='font-size: "+fontSize+";width:25%;float:left;padding-left:1px;'>---------</div>"
                            +"<div style='font-size: "+fontSize+";width:33%;float:left;padding-left:1px;'>---------</div>"
                            +"<div style='font-size: "+fontSize+";width:50%;float:left;padding-left:1px;'>---------</div>"
                            +"<div style='font-size: "+fontSize+";width:100%;float:left;padding-left:1px;'>---------</div>"
                            +"<div style='clear:left;'></div>"
                            +"<div style='text-align:center;font-size: "+fontSize+";width:25%;float:left;padding-right:1px;padding-left:1px;'>+TOTAL+</div>"
                            +"<div style='text-align:center;font-size: "+fontSize+";width:33%;float:left;padding-right:1px;padding-left:1px;'>"+Util.getFormatPrice(amount)+"</div>";
                            
                            String totalBaseImpString = "";
                            String totalImpString = "";
                            if(totalBaseImp>0)
                            {
                                totalBaseImpString = Util.getFormatPrice(totalBaseImp);
                            }
                            
                            if(totalImp>0)
                            {
                                totalImpString = Util.getFormatPrice(totalImp);
                            }
                            html=html+"<div style='text-align:center;font-size: "+fontSize+";width:50%;float:left;padding-right:1px;padding-left:1px;'>"+totalBaseImpString+"</div>"
                            +"<div style='text-align:center;font-size: "+fontSize+";width:100%;float:left;padding-right:1px;padding-left:1px;'>"+totalImpString+"</div>"
                            +"<div style='clear:left;'></div>"
                            +"<div style='font-size: "+fontSize+";width:25%;float:left;padding-left:1px;'>---------</div>"
                            +"<div style='font-size: "+fontSize+";width:33%;float:left;padding-left:1px;'>---------</div>"
                            +"<div style='font-size: "+fontSize+";width:50%;float:left;padding-left:1px;'>---------</div>"
                            +"<div style='font-size: "+fontSize+";width:100%;float:left;padding-left:1px;'>---------</div>"
                            +"<div style='clear:left;'></div>"
                            +"<div style='font-size: "+fontSize+";'>Lo Atentidió: "+Util.upperCase(processReceiptName(purchase.getUser().getUsName())+" "+processReceiptLastName(purchase.getUser().getUsLastName()))+"</div>"
                            +"<div style='width:100%;text-align:center;font-size: "+fontSize+";'>No."+barCode+"</div>"
                            +"<div style='text-align:center;font-size: "+fontSize+";'>"+Util.getFormatCurrentDate(new Date())+"</div>"
                        + "</div>"
                    + "</body>"
                + "</html>";
    }
    
    private String processReceiptName(String name)
    {
        String []splitName = name.split(" ");
        if(splitName.length > 1)
        {
            String secondName = splitName[1].charAt(0)+"";
            String newName = splitName[0] +" "+secondName.toUpperCase()+".";
            return newName;
        }
        return splitName[0];
    }
    
    private String processReceiptLastName(String lastName)
    {
        String []splitLastName = lastName.split(" ");
        return splitLastName[0];
    }
    
    
    public String getHtml()
    {
        return html;
    }
    
    public String getPrintReceipt() {
        return printReceipt;
    }

    public ByteArrayOutputStream getFicheroPdf() {
        return ficheroPdf;
    }
    
    
    
}
