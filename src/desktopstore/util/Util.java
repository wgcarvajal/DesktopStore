/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package desktopstore.util;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author Wilson Carvajal
 */
public class Util {
    
    public static String nameStore = "TIENDA DE LUCHO";
    public static String addressStore = "EL CORTIJO CRA 5B N. 63-07";
    public static String nitStore = "1.075.220.291";
    public static String bossStore = "WILSON G. CARVAJAL";
    public static String phoneStore = "8624840";
    
    //public static String urlServer = "http://192.168.0.71:8080";
    public static String urlServer = "http://localhost:8080";
    public static String projectPath = "/store";
    
    
    public static String PRODUCTIMAGEDIR= "/Users/aranda/filesStore/productImage/";
    public static String BILLDIR= "/Users/aranda/filesStore/bill/";
    public static String FONTDIR= "/Users/aranda/filesStore/font/";
    public static String LOGSDIR= "/Users/aranda/filesStore/logs/";
    public static String CASHINFO = "/Users/aranda/filesStore/cashInfo/info.json";
    
    /*public static String PRODUCTIMAGEDIR= "/home/store/filesStore/productImage/";
    public static String BILLDIR= "/home/store/filesStore/bill/";
    public static String FONTDIR= "/home/store/filesStore/font/";
    public static String LOGSDIR= "/home/store/filesStore/logs/";*/
    
    /*public static String PRODUCTIMAGEDIR= "C:\\filesStore\\productImage\\";
    public static String BILLDIR= "C:\\filesStore\\bill\\";
    public static String FONTDIR= "C:\\filesStore\\font\\";
    public static String LOGSDIR= "C:\\filesStore\\logs\\";
    public static String CASHINFO = "C:\\filesStore\\cashInfo\\info.json";*/
    
    /*ublic static String PRODUCTIMAGEDIR= "D:\\filesStore\\productImage\\";
    public static String BILLDIR= "D:\\filesStore\\bill\\";
    public static String FONTDIR= "D:\\filesStore\\font\\";
    public static String LOGSDIR= "D:\\filesStore\\logs\\";
    public static String CASHINFO = "D:\\filesStore\\cashInfo\\info.json";*/
    
    public static String formatText(String value)
    {
        value = value.trim();
        value = value.substring(0, 1).toUpperCase() + value.substring(1).toLowerCase();
        return value;
        
    }
    
    public static String formatTextWithSapace(String value)
    {
        value = value.trim();
        value = value.substring(0, 1).toUpperCase() + value.substring(1).toLowerCase();
        String [] values = value.split(" ");
        if(values.length > 1)
        {
            value = values[0];
            for(int i=1;i<values.length;i++)
            {
               value = value +" "+values[i].substring(0, 1).toUpperCase() + values[i].substring(1).toLowerCase();; 
            }
        }
        return value;
    }
    
    public static String getFormatPrice(long price)
    {
        NumberFormat format = NumberFormat.getInstance();
        Currency currency = Currency.getInstance("COP");
        format.setCurrency(currency);
        return format.format(price);
    }
    
    public static String getFormatPrice(double price)
    {
        NumberFormat format = NumberFormat.getInstance();
        Currency currency = Currency.getInstance("COP");
        format.setCurrency(currency);
        return format.format(price);
    }
    
    
    
    public static boolean isInteger(String value)
    {
        try
        {
            long l = Integer.parseInt(value);
            return true;
        }catch(NumberFormatException e)
        {
            return false;
        }
    }
    
    public static String getFormatDateReturnDay(Date date)
    {
        SimpleDateFormat formato = new SimpleDateFormat("EEEE",new Locale("es","ES"));
        String fecha = formato.format(date);
        fecha = fecha.substring(0, 1).toUpperCase() + fecha.substring(1).toLowerCase();
        return fecha;
    }
    
    public static String getFormatDate(Date date)
    {        
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yy");
        String fecha = formato.format(date);
        return fecha;
    }
    
    public static String getFormatHour(Date date)
    {        
        SimpleDateFormat formato = new SimpleDateFormat("HH:mm:ss");
        String hora = formato.format(date);
        return hora;
    }
    
    public static String getFormatCurrentDate(Date date)
    {
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
        String hora = formato.format(date);
        return hora;
    }
    
    
    public static String getFormatCurrentDateTranslate(Date date)
    {
        SimpleDateFormat formato = new SimpleDateFormat("EEEE d  MMMM yyyy",new Locale("es","ES"));
        String fecha = formato.format(date);
        fecha = fecha.substring(0, 1).toUpperCase() + fecha.substring(1).toLowerCase();
        return fecha;
    }
    
    public static String evaluateIva(int iva)
    {
        if(iva == 5)
        {
            return "A";
        }
        else if(iva == 19)
        {
            return "B";
        }
        return "+";
    }
    
    public static String upperCase(String string)
    {
        return string.toUpperCase();
    }
    
    public static void logInformation(String className, String method, String message){
        Logger.log(Logger.M_INFORMATION, className, method, message);
        System.out.println(Logger.createLogLine(Logger.M_INFORMATION, className,method, message));
    }
    
    public static void logError(String className, String method, String message){
        Logger.log(Logger.M_ERROR, className, method, message);
        System.out.println(Logger.createLogLine(Logger.M_ERROR, className,method, message));
    }
    
    public static void logWarning(String className, String method, String message){
        Logger.log(Logger.M_WARNING, className, method, message);
        System.out.println(Logger.createLogLine(Logger.M_WARNING, className,method, message));
    }
}
