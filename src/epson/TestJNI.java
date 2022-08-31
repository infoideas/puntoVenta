// Decompiled by DJ v3.12.12.96 Copyright 2011 Atanas Neshkov  Date: 23/05/2019 10:41:03 a.m.
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   TestJNI.java

package epson;

import java.io.PrintStream;
import java.util.Arrays;

// Referenced classes of package impresionepson:
//            EpsonFiscalDriverJNI

public class TestJNI
{

    public TestJNI()
    {
    }

    public static void main(String args[])
    {
        int LastComError = 0;
        int CounterField = 0;
        byte Version[] = new byte[16];
        byte BufferIn[] = new byte[8192];
        byte BufferOut[] = new byte[51200];
        int SizeBufferIn[] = new int[8];
        int SizeBufferOut[] = new int[8];
        int FiscalStatus = 0;
        int PrinterStatus = 0;
        int ReturnCode = 0;
        EpsonFiscalDriverJNI EpsonFiscalDriver = new EpsonFiscalDriverJNI();
        EpsonFiscalDriver.GetAPIVersion(Version);
        String strVersion = new String(Version);
        System.out.println((new StringBuilder()).append("GetApiVersion: ").append(strVersion).toString());
        EpsonFiscalDriver.SetLog("./", (byte)1);
        EpsonFiscalDriver.setProtocolType(1);
        EpsonFiscalDriver.setComPort(3);
        EpsonFiscalDriver.setBaudRate(9600);
        EpsonFiscalDriver.OpenPort();
        LastComError = EpsonFiscalDriver.getLastError();
        System.out.println((new StringBuilder()).append("getLastError: ").append(LastComError).toString());
        System.out.println((new StringBuilder()).append("getComPort: ").append(EpsonFiscalDriver.getComPort()).toString());
        System.out.println((new StringBuilder()).append("getBaudRate: ").append(EpsonFiscalDriver.getBaudRate()).toString());
        if(LastComError == 0)
        {
            byte szCommand[] = new byte[8];
            byte szExtension[] = new byte[8];
            int iSize = 0;
            EpsonFiscalDriver.Purge();
            if(EpsonFiscalDriver.getProtocolType() == 1)
            {
                szCommand[0] = 5;
                szCommand[1] = 2;
                szExtension[0] = 0;
                szExtension[1] = 0;
                iSize = 2;
            } else
            {
                szCommand[0] = 42;
                szExtension[0] = 78;
                iSize = 1;
            }
            EpsonFiscalDriver.AddDataField(szCommand, iSize);
            EpsonFiscalDriver.AddDataField(szExtension, iSize);
            EpsonFiscalDriver.SendCommand();
            for(; EpsonFiscalDriver.getState() == 2; System.out.println("Busy"));
            LastComError = EpsonFiscalDriver.getLastError();
            System.out.println((new StringBuilder()).append("getLastError: ").append(LastComError).toString());
            if(LastComError == 0)
            {
                CounterField = EpsonFiscalDriver.getExtraFieldCount();
                System.out.println((new StringBuilder()).append("getExtraFieldCount: ").append(CounterField).toString());
                FiscalStatus = EpsonFiscalDriver.getFiscalStatus();
                System.out.println((new StringBuilder()).append("FiscalStatus: ").append(FiscalStatus).toString());
                PrinterStatus = EpsonFiscalDriver.getPrinterStatus();
                System.out.println((new StringBuilder()).append("PrinterStatus: ").append(PrinterStatus).toString());
                ReturnCode = EpsonFiscalDriver.getReturnCode();
                System.out.println((new StringBuilder()).append("ReturnCode: ").append(ReturnCode).toString());
                EpsonFiscalDriver.GetExtraField(1, BufferOut, 51200, SizeBufferOut);
                byte bField1[] = Arrays.copyOfRange(BufferOut, 0, SizeBufferOut[0]);
                String strField1 = new String(bField1);
                System.out.println((new StringBuilder()).append("Field1: ").append(strField1).toString());
                EpsonFiscalDriver.GetExtraField(2, BufferOut, 51200, SizeBufferOut);
                byte bField2[] = Arrays.copyOfRange(BufferOut, 1, SizeBufferOut[1]);
                String strField2 = new String(bField2);
                System.out.println((new StringBuilder()).append("Field2: ").append(strField2).toString());
                EpsonFiscalDriver.GetSentFrame(BufferIn, 8192, SizeBufferIn);
                System.out.println(bytesToHex(BufferIn, SizeBufferOut[0]));
                EpsonFiscalDriver.GetReceivedFrame(BufferOut, 51200, SizeBufferOut);
                System.out.println(bytesToHex(BufferOut, SizeBufferOut[1]));
            }
        }
        EpsonFiscalDriver.SendCommandComplete("0502|0000");
        EpsonFiscalDriver.ClosePort();
        LastComError = EpsonFiscalDriver.getLastError();
        System.out.println((new StringBuilder()).append("ClosePort: ").append(LastComError).toString());
    }

    public static String bytesToHex(byte bytes[], int Size)
    {
        char hexChars[] = new char[Size * 2];
        for(int j = 0; j < Size; j++)
        {
            int v = bytes[j] & 0xff;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0xf];
        }

        return new String(hexChars);
    }

    protected static final char hexArray[] = "0123456789ABCDEF".toCharArray();
    private static final int SIZE_BUFF_IN = 8192;
    private static final int SIZE_BUFF_OUT = 51200;

}
