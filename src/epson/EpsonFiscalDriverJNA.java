// Decompiled by DJ v3.12.12.96 Copyright 2011 Atanas Neshkov  Date: 23/05/2019 10:38:52 a.m.
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   EpsonFiscalDriverJNA.java

package epson;

import com.sun.jna.Library;

public interface EpsonFiscalDriverJNA
    extends Library
{

    public abstract int SetLog(String s, byte byte0);

    public abstract void setComPort(int i);

    public abstract int getComPort();

    public abstract void setBaudRate(int i);

    public abstract int getBaudRate();

    public abstract int getState();

    public abstract int getLastError();

    public abstract int getFiscalStatus();

    public abstract int getPrinterStatus();

    public abstract int getReturnCode();

    public abstract int getExtraFieldCount();

    public abstract void GetAPIVersion(byte abyte0[]);

    public abstract void OpenPort();
    
    public abstract void OpenPortByName(String name);

    public abstract void ClosePort();

    public abstract void Purge();

    public abstract void AddDataField(byte abyte0[], int i);

    public abstract void SendCommand();

    public abstract void GetExtraField(int i, byte abyte0[], int j, int ai[]);

    public abstract void GetSentFrame(byte abyte0[], int i, int ai[]);

    public abstract void GetReceivedFrame(byte abyte0[], int i, int ai[]);

    public abstract void setProtocolType(int i);

    public abstract void SendCommandComplete(String s);

    public abstract int getProtocolType();
}
