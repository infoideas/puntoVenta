// Decompiled by DJ v3.12.12.96 Copyright 2011 Atanas Neshkov  Date: 23/05/2019 10:40:50 a.m.
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   TestJNA.java

package epson;

import com.sun.jna.Native;
import java.io.PrintStream;
import java.util.Arrays;

// Referenced classes of package impresionepson:
//            EpsonFiscalDriverJNA

public class TestJNA
{

    public TestJNA()
    {
    }

    public static void main(String args[])
    {
        EpsonFiscalDriverJNA EpsonFiscalDriver = (EpsonFiscalDriverJNA)Native.loadLibrary("EpsonFiscalDriver.dll",EpsonFiscalDriverJNA.class);
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
        EpsonFiscalDriver.GetAPIVersion(Version);
        String strVersion = new String(Version);
        System.out.println((new StringBuilder()).append("GetApiVersion: ").append(strVersion).toString());
        //EpsonFiscalDriver.SetLog("./", (byte)1);
        EpsonFiscalDriver.setBaudRate(115200);
        EpsonFiscalDriver.setComPort(5);
        EpsonFiscalDriver.setProtocolType(1);        
        EpsonFiscalDriver.OpenPort();
        LastComError = EpsonFiscalDriver.getLastError();
        System.out.println((new StringBuilder()).append("OpenPort: ").append(LastComError).toString());
        if(LastComError == 0)
        {
            byte szCommand[] = new byte[8];
            byte szExtension[] = new byte[8];
            int iSize = 0;
            EpsonFiscalDriver.Purge();
            if(EpsonFiscalDriver.getProtocolType() == 1)
            {
                szCommand[0] = 8;
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
                PrinterStatus = EpsonFiscalDriver.getPrinterStatus();
                ReturnCode = EpsonFiscalDriver.getReturnCode();
                EpsonFiscalDriver.GetExtraField(CounterField, BufferOut, 51200, SizeBufferOut);
                byte bExtraField[] = Arrays.copyOfRange(BufferOut, 0, SizeBufferOut[0]);
                String strExtraField = new String(bExtraField);
                System.out.println((new StringBuilder()).append("GetExtraField: ").append(strExtraField).toString());
                System.out.println((new StringBuilder()).append("PrinterStatus: ").append(PrinterStatus).toString());
                System.out.println((new StringBuilder()).append("FiscalStatus: ").append(FiscalStatus).toString());
                System.out.println((new StringBuilder()).append("ReturnCode: ").append(ReturnCode).toString());
                EpsonFiscalDriver.GetSentFrame(BufferIn, 8192, SizeBufferIn);
                System.out.println(bytesToHex(BufferIn, SizeBufferIn[0]));
                EpsonFiscalDriver.GetReceivedFrame(BufferOut, 51200, SizeBufferOut);
                System.out.println(bytesToHex(BufferOut, SizeBufferOut[0]));
            }
            String lsTipDoc = "902";
            String lsNombreCompradorL1 = "Rafael G\363mez Moreno";
            String lsNombreCompradorL2 = "x";
            String lsDirCompL1 = "Pablo Splinder 637";
            String lsDirCompL2 = "";
            String lsDirCompL3 = "";
            String lsTipIdComprador = "D";
            String lsNumIdComprador = "93997077";
            String lsIvaComprador = "F";
            String lsDocAsocL1 = "";
            String lsDocAsocL2 = "";
            String lsDocAsocL3 = "";
            String lsChequeRT = "";
            String lsRazonTransBenL1 = "";
            String lsRazonTransBenL2 = "";
            String lsDirTransL1 = "";
            String lsDirTransL2 = "";
            String lsDirTransL3 = "";
            String lsTipIdTrans = "D";
            String lsNumIdTrans = "";
            String lsIvaTrans = "F";
            String lsNombreChoferL1 = "";
            String lsNombreChoferL2 = "";
            String lsTipIdChofer = "D";
            String lsNumIdChofer = "";
            String lsDomChoferL1 = "";
            String lsDomChoferL2 = "";
            String lsComando = "1001";
            String lsExt = "0180";
            String lsCampos = (new StringBuilder()).append(lsTipDoc).append("|").append(lsNombreCompradorL1).append("|").append(lsNombreCompradorL2).append("|").append(lsDirCompL1).append("|").append(lsDirCompL2).append("|").append(lsDirCompL3).append("|").append(lsTipIdComprador).append("|").append(lsNumIdComprador).append("|").append(lsIvaComprador).append("|").append(lsDocAsocL1).append("|").append(lsDocAsocL2).append("|").append(lsDocAsocL3).append("|").append(lsChequeRT).append("|").append(lsRazonTransBenL1).append("|").append(lsRazonTransBenL2).append("|").append(lsDirTransL1).append("|").append(lsDirTransL2).append("|").append(lsDirTransL3).append("|").append(lsTipIdTrans).append("|").append(lsNumIdTrans).append("|").append(lsIvaTrans).append("|").append(lsNombreChoferL1).append("|").append(lsNombreChoferL2).append("|").append(lsTipIdChofer).append("|").append(lsNumIdChofer).append("|").append(lsDomChoferL1).append("|").append(lsDomChoferL2).toString();
            String lsComandoCompleto = (new StringBuilder()).append(lsComando).append("|").append(lsExt).append("|").append(lsCampos).toString();
            EpsonFiscalDriver.SendCommandComplete(lsComandoCompleto);
            System.out.println(lsComandoCompleto);
            lsComando = "1002";
            lsExt = "0000";
            String lsDescExtraL1 = "";
            String lsDescExtraL2 = "";
            String lsDescExtraL3 = "";
            String lsDescExtraL4 = "";
            String lsDescItem = "Seguro de Vida";
            String lsCantidad = "10000";
            String lsPrecioUnitario = "3500000";
            String lsTasaIva = "2100";
            String lsImpInt = "";
            String lsCoefII = "";
            String lsURMTX = "";
            String lsCodItemMTX = "";
            String lsCodInt = "CodigoInterno123";
            String lsCodUM = "01";
            String lsCodIva = "7";
            String lsMontoItem = "QUINIENTOS VEINTISEIS con 00/100";
            lsCampos = (new StringBuilder()).append(lsDescExtraL1).append("|").append(lsDescExtraL2).append("|").append(lsDescExtraL3).append("|").append(lsDescExtraL4).append("|").append(lsDescItem).append("|").append(lsCantidad).append("|").append(lsPrecioUnitario).append("|").append(lsTasaIva).append("|").append(lsImpInt).append("|").append(lsCoefII).append("|").append(lsURMTX).append("|").append(lsCodItemMTX).append("|").append(lsCodInt).append("|").append(lsCodUM).append("|").append(lsCodIva).append("|").append(lsMontoItem).toString();
            lsComandoCompleto = (new StringBuilder()).append(lsComando).append("|").append(lsExt).append("|").append(lsCampos).toString();
            System.out.println(lsComandoCompleto);
            EpsonFiscalDriver.SendCommandComplete(lsComandoCompleto);
            lsComando = "1003";
            lsExt = "0000";
            lsComandoCompleto = (new StringBuilder()).append(lsComando).append("|").append(lsExt).toString();
            System.out.println(lsComandoCompleto);
            EpsonFiscalDriver.SendCommandComplete(lsComandoCompleto);
            lsComando = "1006";
            lsExt = "0001";
            lsCampos = "||||||";
            lsComandoCompleto = (new StringBuilder()).append(lsComando).append("|").append(lsExt).append("|").append(lsCampos).toString();
            System.out.println(lsComandoCompleto);
            EpsonFiscalDriver.SendCommandComplete(lsComandoCompleto);
            CounterField = EpsonFiscalDriver.getExtraFieldCount();
            System.out.println((new StringBuilder()).append("getExtraFieldCount: ").append(CounterField).toString());
            LastComError = EpsonFiscalDriver.getLastError();
            System.out.println((new StringBuilder()).append("ClosePort: ").append(LastComError).toString());
            EpsonFiscalDriver.ClosePort();
        }
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

    public static String convertStringToHex(String str)
    {
        char chars[] = str.toCharArray();
        StringBuffer hex = new StringBuffer();
        for(int i = 0; i < chars.length; i++)
            hex.append(Integer.toHexString(chars[i]));

        return hex.toString();
    }

    public static String convertHexToString(String hex)
    {
        StringBuilder sb = new StringBuilder();
        StringBuilder temp = new StringBuilder();
        for(int i = 0; i < hex.length() - 1; i += 2)
        {
            String output = hex.substring(i, i + 2);
            int decimal = Integer.parseInt(output, 16);
            sb.append((char)decimal);
            temp.append(decimal);
        }

        System.out.println((new StringBuilder()).append("Decimal : ").append(temp.toString()).toString());
        return sb.toString();
    }

    protected static final char hexArray[] = "0123456789ABCDEF".toCharArray();
    private static final int SIZE_BUFF_IN = 8192;
    private static final int SIZE_BUFF_OUT = 51200;

}
