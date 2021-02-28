/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package desktopstore.util;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.TooManyListenersException;
import org.apache.commons.lang3.ArrayUtils;

/**
 *
 * @author Wilson Carvajal
 */
public class Scale implements SerialPortEventListener {
    
    private final String TAG = "Scale";
    
    private CommPortIdentifier portIdentifier; 
    private String portName;
    private SerialPort serialPort;
    private int  baud,databits,parity,stopbits;
    private String weight = "0";
    
    public Scale(String portName)
    {
        baud = 9600;                    //default baud setting
        databits = 8;                   //default databits setting
        parity = 0;                     //default parity setting
        stopbits = 1; 
        this.portName = portName; 
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
            setParameterSerialPort();
            initListener();
            return true;
        }
        return false;
    }
    
    private boolean initSerialPort()
    {
        try{
            Enumeration<CommPortIdentifier> portEnum
                    = CommPortIdentifier.getPortIdentifiers();

            while (portEnum.hasMoreElements()) {
                portIdentifier = portEnum.nextElement();

                if (portIdentifier.getName().equals(portName)
                        && portIdentifier.getPortType() == CommPortIdentifier.PORT_SERIAL) {

                    try {    //try to open the port
                        serialPort = (SerialPort) portIdentifier.open("SerialPort", 2000);
                        return true;
                    } catch (PortInUseException e) {
                        Util.logError(TAG, "initSerialPort", "Port " + portIdentifier.getName() + " is in use.");
                        return false;
                    } catch (Exception e) {
                        Util.logError(TAG, "initSerialPort", e.getMessage());
                        return false;
                    }
                }
            }
        }catch(UnsatisfiedLinkError | NoClassDefFoundError  e)
        {
            
        }
        return false;
    }
    
    private void setParameterSerialPort()
    {
        try {
            serialPort.setSerialPortParams(baud, databits, stopbits, parity);
            serialPort.setFlowControlMode(0);
        }
        catch (UnsupportedCommOperationException e) {
            Util.logError(TAG, "setParameterSerialPort",e.getMessage());
        }
    }
    
    private void initListener()
    {
        try {
            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);
        }
        catch (TooManyListenersException e) {
          Util.logError(TAG, "initListener",e.getMessage());
        }
    }
    
    public void stop()
    {
        try{
            serialPort.removeEventListener();
            serialPort.close();
        }catch(Exception e)
        {
            Util.logError(TAG, "stop",e.getMessage());
        }
    }

    @Override
    public void serialEvent(SerialPortEvent serialPortEvent) {
        try {
            int value;
            String[] values = new String[0];
            while ((value = serialPort.getInputStream().read()) != -1) {
                //System.out.println("Values: "+Integer.toHexString(value));
                values = ArrayUtils.add(values, Integer.toHexString(value));
            }
            String gramos = "";
            if (!values[4].equals("0")) {
                gramos = values[4];
                if (values[4].length() == 1) {
                    gramos = gramos + "0";
                }
            }

            if (gramos.isEmpty()) {
                if (!values[3].equals("0")) {
                    gramos = values[3];
                }
            } else {
                gramos = gramos + values[3];
            }

            if (gramos.isEmpty()) {
                gramos = values[2];
            } else if (values[2].length() == 1) {
                gramos = gramos + "0" + values[2];
            } else {
                gramos = gramos + values[2];
            }

            System.out.println(gramos);
            weight = gramos;

        } catch (IOException e) {
            Util.logError(TAG, "serialEvent",e.getMessage());
        }
    }
    
}
