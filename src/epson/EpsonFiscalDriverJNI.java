// Decompiled by DJ v3.12.12.96 Copyright 2011 Atanas Neshkov  Date: 23/05/2019 10:39:19 a.m.
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   EpsonFiscalDriverJNI.java

package epson;


public class EpsonFiscalDriverJNI
{

    public EpsonFiscalDriverJNI()
    {
    }

    public native int SetLog(String s, byte byte0);

    public native void setComPort(int i);

    public native int getComPort();

    public native void setBaudRate(int i);

    public native int getBaudRate();

    public native int getState();

    public native int getLastError();

    public native int getFiscalStatus();

    public native int getPrinterStatus();

    public native int getReturnCode();

    public native int getExtraFieldCount();

    public native void GetAPIVersion(byte abyte0[]);

    public native void OpenPort();

    public native void ClosePort();

    public native void Purge();

    public native void AddDataField(byte abyte0[], int i);

    public native void SendCommand();

    public native void GetExtraField(int i, byte abyte0[], int j, int ai[]);

    public native void GetSentFrame(byte abyte0[], int i, int ai[]);

    public native void GetReceivedFrame(byte abyte0[], int i, int ai[]);

    public native void setProtocolType(int i);

    public native int getProtocolType();

    public native void SendCommandComplete(String s);

    static 
    {
        System.loadLibrary("EpsonFiscalDriver");
    }
}
