package connection;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * This version of the TwoWaySerialComm example makes use of the 
 * SerialPortEventListener to avoid polling.
 *
 */
public class TwoWaySerialUSBCommunication
{
    public TwoWaySerialUSBCommunication()
    {
        super();
    }
    
    void connect ( String portName ) throws Exception
    {
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
        if ( portIdentifier.isCurrentlyOwned() )
        {
            System.out.println("Error: Port is currently in use");
        }
        else
        {
            CommPort commPort = portIdentifier.open(this.getClass().getName(),2000);
            
            if ( commPort instanceof SerialPort )
            {
                SerialPort serialPort = (SerialPort) commPort;
                serialPort.setSerialPortParams(57600,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
                
                InputStream in = serialPort.getInputStream();
                OutputStream out = serialPort.getOutputStream();
                               
                (new Thread(new SerialWriter(out))).start();
                
                serialPort.addEventListener(new SerialReader(in));
                serialPort.notifyOnDataAvailable(true);

            }
            else
            {
                System.out.println("Error: Only serial ports are handled by this example.");
            }
        }     
    }
    
    /**
     * Handles the input coming from the serial port. A new line character
     * is treated as the end of a block in this example. 
     */
    public static class SerialReader implements SerialPortEventListener 
    {
        private InputStream in;
        private byte[] buffer = new byte[1024];
        //private ArrayList<Integer> buffer = new ArrayList<Integer>();
        
        public SerialReader ( InputStream in )
        {
            this.in = in;
        }
        
        public void serialEvent(SerialPortEvent arg0) {
            int data;
          
            try
            {
                int len = 0;
                while ( ( data = in.read()) > -1 )
                {
                    if ( data == '\n' ) {
                        break;
                    }
                    buffer[len++] = (byte) data;
                	//buffer.add(data);
                }
                //for(Integer number : buffer) {
                //	System.out.println(number);
                //}
                System.out.println(new String(buffer,0,len));
            }
            catch ( IOException e )
            {
                e.printStackTrace();
                System.exit(-1);
            }             
        }

    }

    /** */
    public static class SerialWriter implements Runnable 
    {
        OutputStream out;
        
        public SerialWriter ( OutputStream out )
        {
            this.out = out;
        }
        
        public void run ()
        {
            try
            {                
                int c = 0;
                //while ( ( c = System.in.read()) > -1 )
                //{
                //    this.out.write(c);
                //}
                this.out.write(c);
                Thread.sleep(1000);
                c = 0;
                this.out.write(c);
                Thread.sleep(1000);
                c = 1;
                this.out.write(c);
                Thread.sleep(1000);
                c = 4;
                this.out.write(c);
                Thread.sleep(1000);
                c = 128;
                this.out.write(c);
                Thread.sleep(1000);
                c = 64;
                this.out.write(c);
                Thread.sleep(1000);
                c = 1;
                this.out.write(c);
                Thread.sleep(1000);
            }
            catch ( IOException | InterruptedException e )
            {
                e.printStackTrace();
                System.exit(-1);
            }            
        }
    }
    
    public static void main ( String[] args ) {
        try
        {
            //(new TwoWaySerialUSBCommunication()).connect("COM4");
        }
        catch ( Exception e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public double[][] getReceivedDataChannelOne() {
    	double[][] list = new double[2][1000];
    	double c = 0.01d;
    	double x;
    	for(int i=0; i<1000; i++) {
    		x = c*i;
    		list[0][i] = x;
    		list[1][i] = Math.sin(2d*2d*Math.PI*x); //frequency = 2Hz
    		System.out.println(list[1][i]);
    	}
    	return list;
    }
    
    public double[][] getReceivedDataChannelTwo() {
    	double[][] list = new double[2][1000];
    	double c = 0.01d;
    	double x;
    	for(int i=0; i<1000; i++) {
    		x = c*i;
    		list[0][i] = x;
    		list[1][i] = Math.cos(2d*Math.PI*x)+Math.cos(6d*Math.PI*x); //frequency = 1+3Hz
    	}
    	return list;
    }

}
