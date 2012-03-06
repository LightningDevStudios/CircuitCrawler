package com.ltdev.cc.network;

import java.io.IOException;
import java.net.SocketException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.net.Socket;
import android.os.Bundle;
import java.net.ServerSocket;
import android.app.Activity;

public class Server extends Activity 
{
    public static String SERVERIP = "127.0.0.1";
    public static final int SERVERPORT = 5555;

    private ServerSocket serverSocket;
    private boolean threadsActive = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        SERVERIP = getLocalIpAddress();

        Thread srvthr = new Thread(new ServerThread());
        srvthr.start();
    }

    public class ServerThread implements Runnable 
    {
        public void run() 
        {
            try 
            {
                if (SERVERIP != null) 
                {
                    serverSocket = new ServerSocket(SERVERPORT);
                    while (threadsActive) 
                    {
                        Socket client = serverSocket.accept();
                        try 
                        {
                            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                            String line = null;
                            while ((line = in.readLine()) != null) 
                            {

                            }
                            break;
                        } 
                        catch (Exception e) 
                        {

                        }
                    }
                } 
                else 
                {

                }
            } 
            catch (Exception e) 
            {

            }
        }
    }

    private String getLocalIpAddress() 
    {
        try 
        {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) 
            {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) 
                {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) 
                    {
                        return inetAddress.getHostAddress().toString(); 
                    }
                }
            }
        } 
        catch (SocketException ex) 
        {

        }
        return null;
    }

    @Override
    protected void onStop() 
    {
        super.onStop();
        try 
        {
             serverSocket.close();
        } 
        catch (IOException e) 
        {
             
        }
    }
}
