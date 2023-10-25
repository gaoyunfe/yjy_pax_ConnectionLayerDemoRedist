package com.paxitalia.connectionlayerdemoredist;


/**
 * Created by andrea.urio on 29/03/2017.
 */

import com.paxitalia.mpos.common.IpUtility;
import com.paxitalia.mpos.connectionlayer.Logger;
import com.paxitalia.mpos.connectionlayer.Wlan;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

public class Wlan_Custom extends Wlan {

    private String ipAddress;
    private int tcpPort;
    /* AG - 2015/09/25 - use connection timeout from connection request - start */
    private int timeout;
    /* AG - 2015/09/25 - use connection timeout from connection request -  end  */
    private Socket socket;
    private Logger logger = new Logger("ConnectionLayer: Wlan_Custom");

    public Wlan_Custom() {
        super();
    }


    /* AU - 2017/03/29: setIpAddress method of Wlan superclass is Overridable by any of
    *                   its subclass.*/
    @Override
    public void setIpAddress(String ipAddress) {
        this.ipAddress = IpUtility.decimalizeIpAddress(ipAddress);

        logger.logInfo("setIpAddress Wlan_Custom: decimalized IP address: " + this.ipAddress);
    }

    /* AU - 2017/03/29: getIpAddress method of Wlan superclass is Overridable by any of
    *                   its subclass.*/
    @Override
    public String getIpAddress() {
        return ipAddress;
    }

    /* AU - 2017/03/29: setTcpPort method of Wlan superclass is Overridable by any of
    *                   its subclass.*/
    @Override
    public void setTcpPort(int tcpPort) {
        this.tcpPort = tcpPort;
    }

    /* AU - 2017/03/29: setConnectionTimeout method of Wlan superclass is Overridable by any of
    *                   its subclass.*/
    @Override
/* AG - 2015/09/25 - use connection timeout from connection request - start */
    public void setConnectionTimeout ( int tmo ) {
        this.timeout = tmo;
    }
/* AG - 2015/09/25 - use connection timeout from connection request -  end  */

    /* AU - 2017/03/29: getTcpPort method of Wlan superclass is Overridable by any of
    *                   its subclass.*/
    @Override
    public int getTcpPort() {
        return tcpPort;
    }
	

    /* AU - 2017/03/29: The open method shall be overridded to implement the opening process of the
                        customized connection protocol.*/
    @Override
    public synchronized boolean open(Object recipientObject) {

        boolean result;

        try {
/* AG - 2015/09/25 - use connection timeout from connection request - start */
            if (timeout > 0)
            {
                SocketAddress myAddr = new InetSocketAddress(ipAddress, tcpPort);

                socket = new Socket();
                socket.connect(myAddr, timeout);
            }
            else
            {
                socket = new Socket(ipAddress, tcpPort);
            }
/* AG - 2015/09/25 - use connection timeout from connection request -  end  */
            logger.logInfo("Wlan_Custom connection established");

            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();

        } catch (UnknownHostException e) {
            e.printStackTrace();
            logger.logError("Wlan_Custom open connection error: caught exception: " + e.getMessage());
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            logger.logError("Wlan_Custom open connection error: caught exception: " + e.getMessage());
            return false;
        }

        return true;
    }

    /* AU - 2017/03/29: The close method shall be overridded to implement the closing process of the
                        customized connection protocol.*/
    @Override
    public synchronized void close() {
        try {
            socket.shutdownInput();
        } catch (IOException e) {
            e.printStackTrace();
            logger.logError("Wlan_Custom close error: shutdownInput: caught exception: " + e.getMessage());
        }

        try {
            socket.shutdownOutput();
        } catch (IOException e) {
            e.printStackTrace();
            logger.logError("Wlan_Custom close error: shutdownOutput: caught exception: " + e.getMessage());
        }

        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
            logger.logError("Wlan_Custom close error: close: caught exception: " + e.getMessage());
        }
    }

    long gSendTime = 0;

    /* AU - 2017/03/29: The send method shall be overridded to implement the send process of the
                        customized connection protocol.*/
    @Override
    public void send(byte [] byteArray) {

        try {
            outputStream.write(byteArray);
        } catch (IOException e) {
            e.printStackTrace();
            logger.logError("send error: outputStream: caught exception: " + e.getMessage());
        }

        gSendTime = System.currentTimeMillis();
    }

    /* AU - 2017/03/29: The read method shall be overridded to implement the read process of the
                        customized connection protocol.*/
    @Override
    public byte [] read() {

        int avalData = 0;

        try {
            avalData = inputStream.available();
        } catch (IOException e) {
            e.printStackTrace();
            logger.logError("read error: inputStream: caught exception: " + e.getMessage());
            avalData = -1;
        }

        if (avalData <= 0)
            return new byte[0];

        int data = -1;

        try {

            data = inputStream.read();

        } catch (IOException e) {
            e.printStackTrace();
            logger.logError("read error: inputStream: caught exception: " + e.getMessage());
        }

        if ((data >= 0) && (data <= 255)) {

            byte[] ret = new byte[1];

            ret[0] = (byte) data;

            return ret;
        }

        return new byte[0];
    }

    /* AU - 2017/03/29: The read method shall be overridded to implement the read process of the
                        customized connection protocol.*/
    @Override
    public byte [] read(int maxBytes) {

        int avalData = 0;

        try {
            avalData = inputStream.available();
        } catch (IOException e) {
            e.printStackTrace();
            logger.logError("read error: inputStream: caught exception: " + e.getMessage());
            avalData = -1;
        }

        if (avalData <= 0)
            return new byte[0];

/* AG - 2017/06/13 - respect mPOS max byte request */
        if (avalData > maxBytes)
            avalData = maxBytes;
/*
        long now = System.currentTimeMillis();

        if( (now - gSendTime ) < 4000 ){

            System.out.printf( String.format("WAIT.... MsElapsed: %04d ", (now - gSendTime)) );
            avalData = 0;

        }
*/
        byte[] ret = new byte[avalData];

        try {

            inputStream.read(ret, 0, avalData);

        } catch (IOException e) {
            e.printStackTrace();
            logger.logError("read error: inputStream: caught exception: " + e.getMessage());
        }

        return ret;
    }

}
