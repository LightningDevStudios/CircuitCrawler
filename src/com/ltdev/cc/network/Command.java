package com.ltdev.cc.network;

public enum Command
{
    STX (0x02),
    ETX (0x03),
    EOT (0x04),
    ACK (0x06),
    NAK (0x15);
    
    Byte value;
    
    Command(int b) { value = (Byte)((byte)b); }
    public Byte getValue() { return value; }
};
