/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package desktopstore.util;

import com.fazecast.jSerialComm.SerialPort;
import javax.swing.SwingUtilities;

/**
 *
 * @author Wilson Carvajal
 */
public class Scale{
    
    private final String TAG = "Scale";
    private String portName;
    private SerialPort serialPort;
    private int  baud,databits,parity,stopbits;
    private String weight = "0";
    private boolean run = false;
    private Mlistener mlistener;
    
    public Scale(String portName)
    {
        baud = 9600;                    //default baud setting
        databits = 8;                   //default databits setting
        parity = 0;                     //default parity setting
        stopbits = 1; 
        this.portName = portName; 
    }

    
    public interface Mlistener
    {
        void paintWeight(String weight);
    }

    public void setListener(Mlistener mlistener)
    {
        this.mlistener = mlistener;
    }
    
    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
    
    public boolean start()
    {
        if(initSerialPort())
        {
            return setParameterSerialPort();
            
        }
        return false;
    }
    
    private boolean initSerialPort()
    {
            SerialPort []portEnum
                    = SerialPort.getCommPorts();
            
            if(portEnum!=null && portEnum.length>0)
            {
                for(int i=0;i<portEnum.length;i++)
                {
                    SerialPort portIdentifier = portEnum[i];
                    String format = portIdentifier.getSystemPortName();
                    if(format.equals(portName))
                    {
                        serialPort = portIdentifier;
                        return serialPort.openPort();
                    }
                }
            }

        return false;    
    }
    
    private boolean setParameterSerialPort()
    {
        serialPort.setComPortParameters(baud, databits, stopbits, parity);
        return  serialPort.setFlowControl(0);
    }
    
    public void runScale() {
        try {
            run = true;
            int first = 1;
            String current = "0";
            while (run) {
                while (serialPort.bytesAvailable() == 0) {
                    Thread.sleep(20);
                }
                byte[] readBuffer = new byte[serialPort.bytesAvailable()];
                int numRead = serialPort.readBytes(readBuffer, readBuffer.length);
                System.out.println("Read " + numRead + " bytes :");
                if(numRead>=6)
                {
                    String gramos="";
                    String value0 = Integer.toHexString(readBuffer[0]);
                    String value1 = Integer.toHexString(readBuffer[1]);
                    String value2 = Integer.toHexString(readBuffer[2]);
                    String value3 = Integer.toHexString(readBuffer[3]);
                    String value4 = Integer.toHexString(readBuffer[4]);
                    String value5 = Integer.toHexString(readBuffer[5]);
                    value2= value2.replaceAll("f", "");
                    /*System.out.println("Values: " + value0);
                    System.out.println("Values: " + value1);
                    System.out.println("Values: " + value2);
                    System.out.println("Values: " + value3);
                    System.out.println("Values: " + value4);
                    System.out.println("Values: " + value5);*/

                    if (!value4.equals("0")) {
                        gramos = value4;
                        if (value4.length() == 1) {
                            gramos = gramos + "0";
                        }
                    }

                    if (gramos.isEmpty()) {
                        if (!value3.equals("0")) {
                            gramos = value3;
                        }
                    } else {
                        gramos = gramos + value3;
                    }

                    if (gramos.isEmpty()) {
                        gramos = value2;
                    } else if (value2.length() == 1) {
                        gramos = gramos + "0" + value2;
                    } else {
                        gramos = gramos + value2;
                    }

                    if (gramos.equals(current)) {
                        if (first == 4) {
                            weight = gramos;
                        }
                        first += 1;
                    } else {
                        current = gramos;
                        first = 1;
                    }
                    System.out.println(gramos);
                    
                    final String g = gramos;
                    if (mlistener != null) {
                        SwingUtilities.invokeLater(() -> {
                            mlistener.paintWeight(g);
                        });
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        serialPort.closePort();
    }
    
    public void stopScale()
    {
        run = false;
    }

}
