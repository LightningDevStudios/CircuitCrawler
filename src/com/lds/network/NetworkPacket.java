package com.lds.network;

import java.util.ArrayList;

public class NetworkPacket 
{
    private boolean valid = true;
    private Command cmd;
    private String data;
    private byte[] rawPacketData;
    
    //Packet Format
    //<STX> <CMD> | <DATAn> <DATAn> <DATA...> | <ETX> <CS> <EOT>
    public NetworkPacket(Command cmd, String data)
    {
        this.cmd = cmd;
        this.data = data;
        
        ArrayList<Byte> buffer= new ArrayList<Byte>();
        try
        {
            buffer.add(cmd.getValue());
            for(char c : data.toCharArray())
                buffer.add((Byte)((byte)c));
            byte CS = CRC(buffer);
            buffer.add(0, Command.STX.getValue());
            buffer.add(Command.ETX.getValue());
            buffer.add((Byte)CS);
            buffer.add(Command.EOT.getValue()); 
            
            rawPacketData = new byte[buffer.size()];
            for(int i = 0; i < buffer.size(); i++)
                rawPacketData[i] = (byte)buffer.get(i);
        }
        catch (Exception e)
        {
            valid = false;
        }
    }
    
    public boolean isValid()
    {
        return valid;
    }
    public Command getCommand()
    {
        return cmd;
    }
    public String getData()
    {
        return data;
    }
    
    private byte CRC(ArrayList<Byte> ar)
    {
        int CS = 0;
        for (int i = 0; i < ar.size(); i++)
        {
            CS ^= (byte)ar.get(i);
        }
        return (byte)CS;
    }
}
