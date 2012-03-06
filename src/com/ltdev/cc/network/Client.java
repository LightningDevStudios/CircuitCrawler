package com.ltdev.cc.network;

import java.io.OutputStreamWriter;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.InetAddress;
import android.os.Bundle;
import android.app.Activity;

public class Client extends Activity 
{
    private boolean connected = false;
    
    public static String SERVERIP = "";
    public static final int SERVERPORT = 5555;

    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        
        //TODO - Get Server IP Here
        
        if (!SERVERIP.equals("")) 
        {
            Thread clthr = new Thread(new ClientThread());
            clthr.start();
        }
    }

    public class ClientThread implements Runnable 
    {
        public void run() 
        {
            try 
            {
                InetAddress serverAddr = InetAddress.getByName(SERVERIP);
                Socket socket = new Socket(serverAddr, SERVERPORT);
                connected = true;
                while (connected) 
                {
                    try 
                    {
                        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                        //out.println("Hello Server!"); // Testing
                    } 
                    catch (Exception e) 
                    {
                        
                    }
                }
                socket.close();
            } 
            catch (Exception e) 
            {
                connected = false;
            }
        }
    }
}
