/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package desktopstore.util;

import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;
import com.sun.pdfview.PDFRenderer;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;

/**
 *
 * @author aranda
 */
public class PrintPdf {
    
    private PrinterJob pjob = null;
    private int paperWidth;
    private int paperImageableAreaWidth;
    
    public PrintPdf(){};    
    public PrintPdf(int paperSize)
    {
       if(paperSize == 80)
       {
           paperWidth = 226;
           paperImageableAreaWidth = 205;
       }
    }
        
    public void imprimirTicket(ByteArrayOutputStream ficheroPdf,String printName) {
        try{
            PrintService printService = this.findPrintService(printName);
            if(printService!=null)
            {
                ByteBuffer buffer = ByteBuffer.wrap(ficheroPdf.toByteArray());
                final PDFFile pdfFile = new PDFFile(buffer);
                System.out.println(pdfFile.getNumPages());
                PDFPrintPage pages = new PDFPrintPage(pdfFile);
                PrinterJob printJob = PrinterJob.getPrinterJob();
                Book book = new Book();
                PageFormat pageFormat = new PageFormat();
                Paper paper = new Paper();
                paper.setSize(paperWidth, pdfFile.getPage(0).getHeight());
                paper.setImageableArea(0, 0, paperImageableAreaWidth, pdfFile.getPage(0).getHeight());
                pageFormat.setPaper(paper);
                book.append(pages, pageFormat,pdfFile.getNumPages());
                printJob.setPageable(book);
                printJob.setPrintService(printService);
                printJob.setJobName("Receipt");
                printJob.print();
            }
            else
            {
                System.out.println("printservice null");
            }
            
        }catch(IOException | NullPointerException | PrinterException e)
        {
           System.out.println(e.getLocalizedMessage());
        }
    }
    
    public void openCashDrawer(String printName,String commandOpenCashDrawer){

        try {
            PrintService pservice = this.findPrintService(printName);
            if (pservice != null) {
                commandOpenCashDrawer = commandOpenCashDrawer.replace("{", "");
                commandOpenCashDrawer = commandOpenCashDrawer.replace("}", "");
                String[] split = commandOpenCashDrawer.split(",");
                if (split != null) {
                    if (split.length > 0) {
                        byte[] open = new byte[split.length];
                        for (int i = 0; i < split.length; i++) {
                            open[i] = getByte(split[i]);
                        }
                        //      byte[] cutter = {29, 86,49};
                        DocPrintJob job = pservice.createPrintJob();
                        DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
                        Doc doc = new SimpleDoc(open, flavor, null);
                        PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
                        job.print(doc, aset);
                    }
                }
            }
            else
            {
                System.out.println("printservice null");
            }
        } catch (Exception e) {
           System.out.println(e.getLocalizedMessage());
        }
    }
    
    private PrintService findPrintService(String printerName) {
        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
        for (PrintService printService : printServices) {
            System.out.println(printService.getName());

            if (printService.getName().trim().equals(printerName)) {
                return printService;
            }
        }
        return null;
    }
    
    private byte getByte(String number)
    {
        return (byte)Integer.parseInt(number);
    }
}




/**
 * Class that actually converts the PDF file into Printable format
 */
class PDFPrintPage implements Printable {

	private PDFFile file;

	PDFPrintPage(PDFFile file) {
		this.file = file;
	}

	public int print(Graphics g, PageFormat format, int index) throws PrinterException {
		int pagenum = index + 1;
		if ((pagenum >= 1) && (pagenum <= file.getNumPages())) {
			Graphics2D g2 = (Graphics2D) g;
			PDFPage page = file.getPage(pagenum);

			// fit the PDFPage into the printing area
			Rectangle imageArea = new Rectangle((int) format.getImageableX(), (int) format.getImageableY(),
					(int) format.getImageableWidth(), (int) format.getImageableHeight());
			g2.translate(0, 0);
			PDFRenderer pgs = new PDFRenderer(page, g2, imageArea, null, null);
			try {
				page.waitForFinish();
				pgs.run();
			} catch (InterruptedException ie) {
				// nothing to do
			}
			return PAGE_EXISTS;
		} else {
			return NO_SUCH_PAGE;
		}
	}
    
}
