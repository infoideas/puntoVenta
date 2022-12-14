// Decompiled by DJ v3.12.12.96 Copyright 2011 Atanas Neshkov  Date: 23/05/2019 10:39:46 a.m.
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   ImpresionEpson.java

package epson;

import com.sun.jna.Native;
import entidades.VentaDet;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

// Referenced classes of package impresionepson:
//            EpsonFiscalDriverJNA, ConvertidorNumeroLetra


public class ImpresionEpson
{

    protected static final char hexArray[] = "0123456789ABCDEF".toCharArray();
    private static final int SIZE_BUFF_IN = 8192;
    private static final int SIZE_BUFF_OUT = 51200;
    private static String lsComando;
    private static String lsExt;
    private static String lsComandoCompleto;
    private static String lsCampos;
    private static EpsonFiscalDriverJNA EpsonFiscalDriver;
    private int puerto;
    
    int CounterField = 0;
    byte BufferIn[] = new byte[8192];
    byte BufferOut[] = new byte[51200];
    int SizeBufferIn[] = new int[8];
    int SizeBufferOut[] = new int[8];
    
    private String numComprobante;
    private String tipoComprobante;

    public int getPuerto()
    {
        return puerto;
    }

    public void setPuerto(int puerto)
    {
        this.puerto = puerto;
    }

    public String getNumComprobante() {
        return numComprobante;
    }

    public void setNumComprobante(String numComprobante) {
        this.numComprobante = numComprobante;
    }

    public String getTipoComprobante() {
        return tipoComprobante;
    }

    public void setTipoComprobante(String tipoComprobante) {
        this.tipoComprobante = tipoComprobante;
    }

    
    public ImpresionEpson()
    {
    }
    
    

    public void inicializaControlador()
    {
        System.setProperty("jna.library.path", "EpsonFiscalDriver.dll");
        byte Version[] = new byte[16];
        EpsonFiscalDriver = (EpsonFiscalDriverJNA) Native.loadLibrary("EpsonFiscalDriver",EpsonFiscalDriverJNA.class);
        System.out.println("Cargo lib Epson");
        EpsonFiscalDriver.GetAPIVersion(Version);
        String strVersion = new String(Version);    
        System.out.println((new StringBuilder()).append("GetApiVersion: ").append(strVersion).toString());
        //EpsonFiscalDriver.SetLog("./", (byte)1);
        EpsonFiscalDriver.setProtocolType(1);
        EpsonFiscalDriver.setComPort(getPuerto());
        EpsonFiscalDriver.setBaudRate(115200);
        System.out.println((new StringBuilder()).append("Controlador inicializado"));        
    }

    public boolean abrirPuerto()
    {
        int LastComError = 0;
        EpsonFiscalDriver.OpenPort();
        LastComError = EpsonFiscalDriver.getLastError();
        if(LastComError > 0)
        {
            System.out.println((new StringBuilder()).append("Error al abrir puerto: ").append(LastComError).toString());
            return false;
        } else
        {
            System.out.println((new StringBuilder()).append("Puerto abierto: ").append(getPuerto()).toString());        
            return true;
        }
    }

    public void cerrarPuerto()
    {
        EpsonFiscalDriver.ClosePort();
        System.out.println((new StringBuilder()).append("Puerto cerrado: ").append(getPuerto()).toString()); 
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
    
    public void ticketTecnico()
    {
        int LastComError = 0;
        lsComando = "0210";
        lsExt = "0000";
        lsComandoCompleto = (new StringBuilder()).append(lsComando).append("|").append(lsExt).toString();
        EpsonFiscalDriver.SendCommandComplete(lsComandoCompleto);
        LastComError = EpsonFiscalDriver.getLastError();
        if(LastComError > 0)
        {
            System.out.println((new StringBuilder()).append("Error ticket tecnico: ").append(LastComError).toString());
            return;
        } 
        
    }
    
    //Imprimir Ticket Factura
    public boolean imprimirTicketFactura(char tipoComprobante, String nombreComprador,String dirComp,
            int tipIdComprador,String numIdComprador,int ivaComprador,
            String docAsocL1, String docAsocL2, String docAsocL3,String lineaCompOrigen,List<VentaDet> listaItems)
    {
        //Separo el nombre del comprador
        int li_len = nombreComprador.length();
        String lsNombreCompradorL1=" ";
        String lsNombreCompradorL2=" ";
        if(li_len > 29)
        {
            lsNombreCompradorL1 = nombreComprador.substring(0, 29);
            lsNombreCompradorL2 = nombreComprador.substring(30);
        } else
        {
            lsNombreCompradorL1 = nombreComprador;
        }
        
        //Separo la dirección del comprador
        li_len = dirComp.length();
        String lsDirCompL1=" ";
        String lsDirCompL2=" ";
        String lsDirCompL3=" ";
        
        if(li_len > 29)
        {
            lsDirCompL1 = dirComp.substring(0, 29);
            dirComp = dirComp.substring(30);
            System.out.println(dirComp);
            if(dirComp.length() > 29)
            {
                lsDirCompL2 = dirComp.substring(0, 29);
                lsDirCompL3 = dirComp.substring(30);
            } else
            {
                lsDirCompL2 = dirComp;
            }
            lsDirCompL3 = " ";
        } else
        {
            lsDirCompL1 = dirComp;
            if (lsDirCompL1.isEmpty())  lsDirCompL1=" ";
            lsDirCompL2 = " ";
            lsDirCompL3 = " ";
        }
        
        //Obtiene los valores usados por epson
        String lsTipIdComprador=obtieneTipoId(tipIdComprador);
        String lsIvaComprador=obtieneCondIva(ivaComprador);
        
        //Abro el comprobante
        if (abrirTicketFactura(lsNombreCompradorL1,lsNombreCompradorL2,lsDirCompL1,lsDirCompL2,lsDirCompL3, 
            lsTipIdComprador,numIdComprador,lsIvaComprador,
            docAsocL1,docAsocL2,docAsocL3,lineaCompOrigen)==false) return false;
        
        //Agrego los items del comprobante
        for(VentaDet item : listaItems){
            String lsNombreItem="";
            String lsDescripcionItem="";
            double ld_cantidad,ld_precioUnitario;
            double ld_tasaIva = 10.50;  //OJO 
            String ls_codInt = "CodigoInterno123";
            String ls_codIva = "7";  //Gravado
            
            lsNombreItem=item.getProducto().getNombre();
            lsDescripcionItem="";//item.getProducto().getDetalle();
            if (lsDescripcionItem==null) lsDescripcionItem="";
            ld_cantidad=item.getCantidad().doubleValue();
            ld_precioUnitario=item.getPrecioUnitario().doubleValue();
            
            if (!agregaItemTicketFactura(tipoComprobante,lsNombreItem,lsDescripcionItem,ld_cantidad, ld_precioUnitario,ld_tasaIva, 
            ls_codInt,ls_codIva)) {
                cerrarTicketFactura();
                return false;
            }
            
        }
        
        //Imprimo sub total
        subTotalTicketFactura();
        
        //Cierro el ticket factura
        if (cerrarTicketFactura())
            return true;
        else
            return false;
    }
    
    //Imprimir Ticket de Nota Crédito
    public boolean imprimirTicketNotaCredito(char tipoComprobante, String nombreComprador,String dirComp,
            int tipIdComprador,String numIdComprador,int ivaComprador,
            String docAsocL1, String docAsocL2, String docAsocL3,String lineaCompOrigen,List<VentaDet> listaItems)
    {
        //Separo el nombre del comprador
        int li_len = nombreComprador.length();
        String lsNombreCompradorL1=" ";
        String lsNombreCompradorL2=" ";
        if(li_len > 29)
        {
            lsNombreCompradorL1 = nombreComprador.substring(0, 29);
            lsNombreCompradorL2 = nombreComprador.substring(30);
        } else
        {
            lsNombreCompradorL1 = nombreComprador;
        }
        
        //Separo la dirección del comprador
        li_len = dirComp.length();
        String lsDirCompL1=" ";
        String lsDirCompL2=" ";
        String lsDirCompL3=" ";
        
        if(li_len > 29)
        {
            lsDirCompL1 = dirComp.substring(0, 29);
            dirComp = dirComp.substring(30);
            System.out.println(dirComp);
            if(dirComp.length() > 29)
            {
                lsDirCompL2 = dirComp.substring(0, 29);
                lsDirCompL3 = dirComp.substring(30);
            } else
            {
                lsDirCompL2 = dirComp;
            }
            lsDirCompL3 = " ";
        } else
        {
            lsDirCompL1 = dirComp;
            if (lsDirCompL1.isEmpty())  lsDirCompL1=" ";
            lsDirCompL2 = " ";
            lsDirCompL3 = " ";
        }
        
        //Obtiene los valores usados por epson
        String lsTipIdComprador=obtieneTipoId(tipIdComprador);
        String lsIvaComprador=obtieneCondIva(ivaComprador);
        
        //Abro el comprobante
        if (abrirTicketNotaCrédito(lsNombreCompradorL1,lsNombreCompradorL2,lsDirCompL1,lsDirCompL2,lsDirCompL3, 
            lsTipIdComprador,numIdComprador,lsIvaComprador,
            docAsocL1,docAsocL2,docAsocL3,lineaCompOrigen)==false) return false;
        
        //Agrego los items del comprobante
        for(VentaDet item : listaItems){
            String lsNombreItem="";
            String lsDescripcionItem="";
            double ld_cantidad,ld_precioUnitario;
            double ld_tasaIva = 10.50;  //OJO 
            String ls_codInt = "CodigoInterno123";
            String ls_codIva = "7";  //Gravado
            
            lsNombreItem=item.getProducto().getNombre();
            lsDescripcionItem="";//item.getProducto().getDetalle();
            if (lsDescripcionItem==null) lsDescripcionItem="";
            ld_cantidad=item.getCantidad().doubleValue();
            ld_precioUnitario=item.getPrecioUnitario().doubleValue();
            
            if (!agregaItemTicketFactura(tipoComprobante,lsNombreItem,lsDescripcionItem,ld_cantidad, ld_precioUnitario,ld_tasaIva, 
            ls_codInt,ls_codIva)) {
                cerrarTicketNotaCredito();
                return false;
            }
            
        }
        
        //Imprimo sub total
        subTotalTicketNotaCredito();
        
        //Cierro el ticket factura
        if (cerrarTicketNotaCredito())
            return true;
        else
            return false;
    }
    
    //Abre comprobante de Ticket-Factura
    public boolean abrirTicketFactura(String lsNombreCompradorL1, String lsNombreCompradorL2, 
            String lsDirCompL1, String lsDirCompL2, String lsDirCompL3, 
            String lsTipIdComprador,String lsNumIdComprador,String lsIvaComprador,
            String lsDocAsocL1, String lsDocAsocL2, String lsDocAsocL3,String lsLineaCompOrigen)
    {
        int LastComError = 0;
        lsComando = "0B01";
        lsExt = "0000";
        lsCampos = (new StringBuilder()).append(lsNombreCompradorL1).append("|").append(lsNombreCompradorL2).append("|").append(lsDirCompL1).append("|").append(lsDirCompL2).append("|").append(lsDirCompL3).append("|").append(lsTipIdComprador).append("|").append(lsNumIdComprador).append("|").append(lsIvaComprador).append("|").append(lsDocAsocL1).append("|").append(lsDocAsocL2).append("|").append(lsDocAsocL3).append("|").toString();
        lsComandoCompleto = (new StringBuilder()).append(lsComando).append("|").append(lsExt).append("|").append(lsCampos).toString();
        EpsonFiscalDriver.SendCommandComplete(lsComandoCompleto);
        System.out.println("Comando abrir ticket: " + lsComandoCompleto );
        LastComError = EpsonFiscalDriver.getLastError();
        if(LastComError > 0)
        {
            System.out.println((new StringBuilder()).append("Error: ").append(LastComError).toString());
            return false;
        } 
        else
            return true;
    }
    
    //Abre comprobante de Nota de Crédito
    public boolean abrirTicketNotaCrédito(String lsNombreCompradorL1, String lsNombreCompradorL2, 
            String lsDirCompL1, String lsDirCompL2, String lsDirCompL3, 
            String lsTipIdComprador,String lsNumIdComprador,String lsIvaComprador,
            String lsDocAsocL1, String lsDocAsocL2, String lsDocAsocL3,String lsLineaCompOrigen)
    {
        int LastComError = 0;
        lsComando = "0D01";
        lsExt = "0000";
        //lsLineaCompOrigen="081-0005-0007777";
        System.out.println(lsLineaCompOrigen);
        lsCampos = (new StringBuilder()).append(lsNombreCompradorL1).append("|").append(lsNombreCompradorL2).
                                         append("|").append(lsDirCompL1).append("|").append(lsDirCompL2).
                                         append("|").append(lsDirCompL3).append("|").
                                         append(lsTipIdComprador).append("|").append(lsNumIdComprador).append("|").
                                         append(lsIvaComprador).
                                         append("|").append(lsDocAsocL1).append("|").append(lsDocAsocL2).append("|").append(lsDocAsocL3).
                                         append("|").append(lsLineaCompOrigen).toString();
        lsComandoCompleto = (new StringBuilder()).append(lsComando).append("|").append(lsExt).append("|").append(lsCampos).toString();
        EpsonFiscalDriver.SendCommandComplete(lsComandoCompleto);
        System.out.println("Comando abrir ticket Nota de Crédito: " + lsComandoCompleto );
        LastComError = EpsonFiscalDriver.getLastError();
        if(LastComError > 0)
        {
            System.out.println((new StringBuilder()).append("Error: ").append(LastComError).toString());
            return false;
        } 
        else
            return true;
        
    }

    //Agrega Item para Factura o Nota de Crédito
    public boolean agregaItemTicketFactura(char tipoComprobante,String lsNombreItem,String lsDescripcionItem, double Cantidad, double PrecioUnitario, double TasaIva, 
            String lsCodInt, String lsCodIva)
    {
        String lsDescExtraL1 = "";
        String lsDescExtraL2 = "";
        String lsDescExtraL3 = "";
        String lsDescExtraL4 = "";
        int liCantidad = 0;
        int ldPrecioUnitario = 0;
        int ldTasaIva = 0;
        String lsDescripcionItemCompleta = lsDescripcionItem;
        int li_len = lsDescripcionItemCompleta.length();
        System.out.println(lsDescripcionItemCompleta);
        if(li_len > 29)
        {
            lsDescExtraL1 = lsDescripcionItemCompleta.substring(0, 29);
            lsDescripcionItemCompleta = lsDescripcionItemCompleta.substring(30);
            System.out.println(lsDescripcionItemCompleta);
            if(lsDescripcionItemCompleta.length() > 29)
            {
                lsDescExtraL2 = lsDescripcionItemCompleta.substring(0, 29);
                lsDescripcionItemCompleta = lsDescripcionItemCompleta.substring(30);
                System.out.println(lsDescripcionItemCompleta);
                if(lsDescripcionItemCompleta.length() > 29)
                {
                    lsDescExtraL3 = lsDescripcionItemCompleta.substring(0, 29);
                    lsDescripcionItemCompleta = lsDescripcionItemCompleta.substring(30);
                    System.out.println(lsDescripcionItemCompleta);
                    lsDescExtraL4 = lsDescripcionItemCompleta;
                } else
                {
                    lsDescExtraL3 = lsDescripcionItemCompleta;
                }
            } else
            {
                lsDescExtraL2 = lsDescripcionItemCompleta;
            }
        } else
        {
            lsDescExtraL1 = lsDescripcionItemCompleta;
        }
        liCantidad = (int)(Cantidad * 10000D);
        String lsCantidad = String.valueOf(liCantidad);
        
        if (TasaIva > 0)
            PrecioUnitario=PrecioUnitario/(1 + TasaIva/100);

        ldTasaIva=(int) (TasaIva * 100);
        
        ldPrecioUnitario = (int)(PrecioUnitario * 10000D);
        String lsPrecioUnitario = String.valueOf(ldPrecioUnitario);

        String lsTasaIva = String.valueOf(ldTasaIva);
        String lsImpInt = "";
        String lsCoefII = "";
        String lsURMTX = "";
        String lsCodItemMTX = "";
        String lsCodUM = "07";
        
        //Seteo el comando de acuerdo al comprobante
        if (tipoComprobante=='F'){
            //Ticket Factura
            lsComando = "0B02";
            lsExt = "0000";
        }
        else{
            //Nota de crédito
            lsComando = "0D02";
            lsExt = "0000";
        }
        
        lsCampos = (new StringBuilder()).append(lsDescExtraL1).append("|").
                append(lsDescExtraL2).append("|").append(lsDescExtraL3).append("|").append(lsDescExtraL4).
                append("|").append(lsNombreItem).append("|").append(lsCantidad).append("|").
                append(lsPrecioUnitario).append("|").append(lsTasaIva).append("|").append(lsImpInt).
                append("|").append(lsCoefII).append("|").append(lsURMTX).append("|").append(lsCodItemMTX).
                append("|").append(lsCodInt).append("|").append(lsCodUM).append("|").append(lsCodIva).toString();
        lsComandoCompleto = (new StringBuilder()).append(lsComando).append("|").append(lsExt).append("|").append(lsCampos).toString();
        System.out.println("Comando imprimir item: " + lsComandoCompleto );
        EpsonFiscalDriver.SendCommandComplete(lsComandoCompleto);
        int LastComError = 0;
        LastComError = EpsonFiscalDriver.getLastError();
        if(LastComError > 0)
        {
            System.out.println((new StringBuilder()).append("Error: ").append(LastComError).toString());
            return false;
        } 
        else
            return true;
    }

    //SubTotal Ticket Factura
    public boolean subTotalTicketFactura()
    {
        lsComando = "0B03";
        lsExt = "0000";
        lsComandoCompleto = (new StringBuilder()).append(lsComando).append("|").append(lsExt).toString();
        EpsonFiscalDriver.SendCommandComplete(lsComandoCompleto);
        int LastComError = 0;
        LastComError = EpsonFiscalDriver.getLastError();
        if(LastComError > 0)
        {
            System.out.println((new StringBuilder()).append("Error: ").append(LastComError).toString());
            return false;
        } 
        else
            return true;
    }
    
    //SubTotal Ticket Nota de Crédito
    public boolean subTotalTicketNotaCredito()
    {
        lsComando = "0D03";
        lsExt = "0000";
        lsComandoCompleto = (new StringBuilder()).append(lsComando).append("|").append(lsExt).toString();
        EpsonFiscalDriver.SendCommandComplete(lsComandoCompleto);
        int LastComError = 0;
        LastComError = EpsonFiscalDriver.getLastError();
        if(LastComError > 0)
        {
            System.out.println((new StringBuilder()).append("Error: ").append(LastComError).toString());
            return false;
        } 
        else
            return true;
    }
    
    //Cerrar Ticket Factura
    public boolean cerrarTicketFactura()
    {
        lsComando = "0B06";
        lsExt = "0013";
        lsCampos = "|||||";
        lsComandoCompleto = (new StringBuilder()).append(lsComando).append("|").append(lsExt).append("|").append(lsCampos).toString();
        EpsonFiscalDriver.SendCommandComplete(lsComandoCompleto);
        System.out.println("Comando cierre: " + lsComandoCompleto);
        int LastComError = 0;
        LastComError = EpsonFiscalDriver.getLastError();
        if(LastComError > 0)
        {   //Error
            System.out.println((new StringBuilder()).append("Error: ").append(LastComError).toString());
            return false;
        } 
        else
            {
            //Proceso Ok
            CounterField = EpsonFiscalDriver.getExtraFieldCount();
            System.out.println((new StringBuilder()).append("getExtraFieldCount: ").append(CounterField).toString());
            
            //Obtengo el número de comprobante del controlador fiscal
            EpsonFiscalDriver.GetExtraField(1, BufferOut, 51200, SizeBufferOut);
            byte bExtraField1[] = Arrays.copyOfRange(BufferOut, 0, SizeBufferOut[0]);
            String ls_numComprobante = new String(bExtraField1);
            System.out.println("Comprobante: " + ls_numComprobante);
            setNumComprobante(ls_numComprobante);
            
            //Obtengo el tipo de comprobante del controlador fiscal
            EpsonFiscalDriver.GetExtraField(2, BufferOut, 51200, SizeBufferOut);
            byte bExtraField2[] = Arrays.copyOfRange(BufferOut, 0, SizeBufferOut[0]);
            String ls_tipoComprobante = new String(bExtraField2);
            System.out.println("Tipo de comprobante: " + ls_tipoComprobante);
            setTipoComprobante(ls_tipoComprobante);
            return true;
        }
        
    }

    //Cerrar Ticket Nota de Crédito
    public boolean cerrarTicketNotaCredito()
    {
        lsComando = "0D06";
        lsExt = "0003";
        lsCampos = "||||||";
        lsComandoCompleto = (new StringBuilder()).append(lsComando).append("|").append(lsExt).append("|").append(lsCampos).toString();
        EpsonFiscalDriver.SendCommandComplete(lsComandoCompleto);
        System.out.println("Comando cierre: " + lsComandoCompleto);
        int LastComError = 0;
        LastComError = EpsonFiscalDriver.getLastError();
        if(LastComError > 0)
        {   //Error
            System.out.println((new StringBuilder()).append("Error: ").append(LastComError).toString());
            return false;
        } 
        else
            {
            //Proceso Ok
            CounterField = EpsonFiscalDriver.getExtraFieldCount();
            System.out.println((new StringBuilder()).append("getExtraFieldCount: ").append(CounterField).toString());
            
            //Obtengo el número de comprobante del controlador fiscal
            EpsonFiscalDriver.GetExtraField(1, BufferOut, 51200, SizeBufferOut);
            byte bExtraField1[] = Arrays.copyOfRange(BufferOut, 0, SizeBufferOut[0]);
            String ls_numComprobante = new String(bExtraField1);
            System.out.println("Comprobante: " + ls_numComprobante);
            setNumComprobante(ls_numComprobante);
            
            //Obtengo el tipo de comprobante del controlador fiscal
            EpsonFiscalDriver.GetExtraField(2, BufferOut, 51200, SizeBufferOut);
            byte bExtraField2[] = Arrays.copyOfRange(BufferOut, 0, SizeBufferOut[0]);
            String ls_tipoComprobante = new String(bExtraField2);
            System.out.println("Tipo de comprobante: " + ls_tipoComprobante);
            setTipoComprobante(ls_tipoComprobante);
            return true;
        }
        
    }
    
    //Obtiene el tipo de id usado por Epson
    public String obtieneTipoId(int tipoIdComprador){
        String lsTipIdComprador=null;
        //Tipo de Id del comprador D: DNI, T : CUIT
        switch (tipoIdComprador){
            case 1 :
                //DNI
                lsTipIdComprador="D";
                break;
            case 2 :
                //CUIT
                lsTipIdComprador="T";
                break;  
            case 3 :
                //CUIL
                lsTipIdComprador="T";
                break;                  
            default: 
                lsTipIdComprador=null;
        }
        return lsTipIdComprador;    
    }
    
    //Obtiene la condición ante IVA usada por Epson
    public String obtieneCondIva(int condIvaComprador){
        String lsCondIvaComprador=null;
        //Tipo de Id del comprador D: DNI, T : CUIT
        switch (condIvaComprador){
            case 1 :
                //Consumidor final
                lsCondIvaComprador="F";
                break;
            case 2 :
                //Responsable inscripto
                lsCondIvaComprador="I";
                break;  
            case 3 :
                //Monotributista
                lsCondIvaComprador="M";
                break;   
            case 4 :
                //Exento
                lsCondIvaComprador="E";
                break;    
            case 5 :
                //MonoTributista Social
                lsCondIvaComprador="T";
                break;    
            case 6 :
                //MonoTributista Trabajador Independiente Promovido
                lsCondIvaComprador="P";
                break;    
                
            default: 
                lsCondIvaComprador=null;
        }
        return lsCondIvaComprador;    
    }
    
    public void abrirDFNH(String lsTipDoc, String lsNombreCompradorL1, String lsNombreCompradorL2, String lsDirCompL1, String lsDirCompL2, String lsDirCompL3, String lsTipIdComprador, 
            String lsNumIdComprador, String lsIvaComprador, String lsDocAsocL1, String lsDocAsocL2, String lsDocAsocL3, String lsChequeRT, String lsRazonTransBenL1, 
            String lsRazonTransBenL2, String lsDirTransL1, String lsDirTransL2, String lsDirTransL3, String lsTipIdTrans, String lsNumIdTrans, String lsIvaTrans, 
            String lsNombreChoferL1, String lsNombreChoferL2, String lsTipIdChofer, String lsNumIdChofer, String lsDomChoferL1, String lsDomChoferL2)
    {
        lsComando = "1001";
        lsExt = "0182";
        lsCampos = (new StringBuilder()).append(lsTipDoc).append("|").append(lsNombreCompradorL1).append("|").append(lsNombreCompradorL2).append("|").append(lsDirCompL1).append("|").append(lsDirCompL2).append("|").append(lsDirCompL3).append("|").append(lsTipIdComprador).append("|").append(lsNumIdComprador).append("|").append(lsIvaComprador).append("|").append(lsDocAsocL1).append("|").append(lsDocAsocL2).append("|").append(lsDocAsocL3).append("|").append(lsChequeRT).append("|").append(lsRazonTransBenL1).append("|").append(lsRazonTransBenL2).append("|").append(lsDirTransL1).append("|").append(lsDirTransL2).append("|").append(lsDirTransL3).append("|").append(lsTipIdTrans).append("|").append(lsNumIdTrans).append("|").append(lsIvaTrans).append("|").append(lsNombreChoferL1).append("|").append(lsNombreChoferL2).append("|").append(lsTipIdChofer).append("|").append(lsNumIdChofer).append("|").append(lsDomChoferL1).append("|").append(lsDomChoferL2).toString();
        lsComandoCompleto = (new StringBuilder()).append(lsComando).append("|").append(lsExt).append("|").append(lsCampos).toString();
        EpsonFiscalDriver.SendCommandComplete(lsComandoCompleto);
        System.out.println(lsComandoCompleto);
        System.out.println((new StringBuilder()).append("Tip Id Trans: ").append(lsTipIdTrans).toString());
        System.out.println((new StringBuilder()).append("Num Id Trans: ").append(lsNumIdTrans).toString());
        System.out.println((new StringBuilder()).append("Iva Trans: ").append(lsIvaTrans).toString());
    }

    public void agregaItemDFNH(String lsNombreItem, String lsDescripcionItem, double Cantidad, double PrecioUnitario, int TasaIva, 
            String lsCodInt, String lsCodIva, String lsMontoItem)
    {
        String lsDescExtraL1 = "";
        String lsDescExtraL2 = "";
        String lsDescExtraL3 = "";
        String lsDescExtraL4 = "";
        int liCantidad = 0;
        int liPrecioUnitario = 0;
        int liTasaIva = 0;
        String lsDescripcionItemCompleta = lsDescripcionItem;
        int li_len = lsDescripcionItemCompleta.length();
        System.out.println(lsDescripcionItemCompleta);
        if(li_len > 29)
        {
            lsDescExtraL1 = lsDescripcionItemCompleta.substring(0, 29);
            lsDescripcionItemCompleta = lsDescripcionItemCompleta.substring(30);
            System.out.println(lsDescripcionItemCompleta);
            if(lsDescripcionItemCompleta.length() > 29)
            {
                lsDescExtraL2 = lsDescripcionItemCompleta.substring(0, 29);
                lsDescripcionItemCompleta = lsDescripcionItemCompleta.substring(30);
                System.out.println(lsDescripcionItemCompleta);
                if(lsDescripcionItemCompleta.length() > 29)
                {
                    lsDescExtraL3 = lsDescripcionItemCompleta.substring(0, 29);
                    lsDescripcionItemCompleta = lsDescripcionItemCompleta.substring(30);
                    System.out.println(lsDescripcionItemCompleta);
                    lsDescExtraL4 = lsDescripcionItemCompleta;
                } else
                {
                    lsDescExtraL3 = lsDescripcionItemCompleta;
                }
            } else
            {
                lsDescExtraL2 = lsDescripcionItemCompleta;
            }
        } else
        {
            lsDescExtraL1 = lsDescripcionItemCompleta;
        }
        liCantidad = (int)(Cantidad * 10000D);
        String lsCantidad = String.valueOf(liCantidad);
        liPrecioUnitario = (int)(PrecioUnitario * 10000D);
        String lsPrecioUnitario = String.valueOf(liPrecioUnitario);
        liTasaIva = TasaIva * 100;
        String lsTasaIva = String.valueOf(liTasaIva);
        String lsImpInt = "";
        String lsCoefII = "";
        String lsURMTX = "";
        String lsCodItemMTX = "";
        String lsCodUM = "07";
        lsComando = "1002";
        lsExt = "0000";
        lsCampos = (new StringBuilder()).append(lsDescExtraL1).append("|").append(lsDescExtraL2).append("|").append(lsDescExtraL3).append("|").append(lsDescExtraL4).append("|").append(lsNombreItem).append("|").append(lsCantidad).append("|").append(lsPrecioUnitario).append("|").append(lsTasaIva).append("|").append(lsImpInt).append("|").append(lsCoefII).append("|").append(lsURMTX).append("|").append(lsCodItemMTX).append("|").append(lsCodInt).append("|").append(lsCodUM).append("|").append(lsCodIva).append("|").append(lsMontoItem).toString();
        lsComandoCompleto = (new StringBuilder()).append(lsComando).append("|").append(lsExt).append("|").append(lsCampos).toString();
        EpsonFiscalDriver.SendCommandComplete(lsComandoCompleto);
    }

    public void cierreZ()
    {
        EpsonFiscalDriver.SendCommandComplete("0801|0C00");
    }

    public void cierreX()
    {
        EpsonFiscalDriver.SendCommandComplete("0802|0C21");
    }
    
    public void subTotal()
    {
        lsComando = "1003";
        lsExt = "0000";
        lsComandoCompleto = (new StringBuilder()).append(lsComando).append("|").append(lsExt).toString();
        EpsonFiscalDriver.SendCommandComplete(lsComandoCompleto);
    }

    public void cerrarComprobante()
    {
        lsComando = "1006";
        lsExt = "0001";
        lsCampos = "||||||";
        lsComandoCompleto = (new StringBuilder()).append(lsComando).append("|").append(lsExt).append("|").append(lsCampos).toString();
        EpsonFiscalDriver.SendCommandComplete(lsComandoCompleto);
    }

    public void generarReciboX(String lsNombreComprador, String lsDirComp, String lsTipIdComprador, String lsNumIdComprador, String lsIvaComprador, String lsNombreItem, String lsDescripcionItem, 
            double ldCantidad, double ldPrecioUnitario, int liTasaIva, String lsCodInt, String lsCodIva)
    {
        int li_len = lsNombreComprador.length();
        String lsNombreCompradorL1;
        String lsNombreCompradorL2;
        if(li_len > 29)
        {
            lsNombreCompradorL1 = lsNombreComprador.substring(0, 29);
            lsNombreCompradorL2 = lsNombreComprador.substring(30);
        } else
        {
            lsNombreCompradorL1 = lsNombreComprador;
        }
        lsNombreCompradorL2 = "";
        li_len = lsDirComp.length();
        System.out.println(lsDirComp);
        String lsDirCompL1;
        String lsDirCompL2;
        String lsDirCompL3;
        if(li_len > 29)
        {
            lsDirCompL1 = lsDirComp.substring(0, 29);
            lsDirComp = lsDirComp.substring(30);
            System.out.println(lsDirComp);
            if(lsDirComp.length() > 29)
            {
                lsDirCompL2 = lsDirComp.substring(0, 29);
                lsDirCompL3 = lsDirComp.substring(30);
            } else
            {
                lsDirCompL2 = lsDirComp;
            }
            lsDirCompL3 = "";
        } else
        {
            lsDirCompL1 = lsDirComp;
            lsDirCompL2 = "";
            lsDirCompL3 = "";
        }
        String lsTipDoc = "902";
        String lsDocAsocL1 = "";
        String lsDocAsocL2 = "";
        String lsDocAsocL3 = "";
        String lsChequeRT = "";
        String lsRazonTransBenL1 = "";
        String lsRazonTransBenL2 = "";
        String lsDirTransL1 = "";
        String lsDirTransL2 = "";
        String lsDirTransL3 = "";
        String lsTipIdTrans;
        String lsNumIdTrans;
        String lsIvaTrans;
        if(lsIvaComprador.equals("F"))
        {
            lsTipIdTrans = "D";
            lsNumIdTrans = "";
            lsIvaTrans = "F";
        } else
        {
            lsTipIdTrans = lsTipIdComprador;
            lsNumIdTrans = lsNumIdComprador;
            lsIvaTrans = lsIvaComprador;
        }
        String lsNombreChoferL1 = "";
        String lsNombreChoferL2 = "";
        String lsTipIdChofer = "D";
        String lsNumIdChofer = "";
        String lsDomChoferL1 = "";
        String lsDomChoferL2 = "";
        double ldMontoItem = ldCantidad * (1.0D + (double)liTasaIva / 100D) * ldPrecioUnitario;
        ConvertidorNumeroLetra convertidor = new ConvertidorNumeroLetra();
        String lsMontoItem = convertidor.convertNumberToLetter(ldMontoItem);
        System.out.println((new StringBuilder()).append("Monto en palabras:").append(lsMontoItem).toString());
        abrirPuerto();
        abrirDFNH(lsTipDoc, lsNombreCompradorL1, lsNombreCompradorL2, lsDirCompL1, lsDirCompL2, lsDirCompL3, lsTipIdComprador, lsNumIdComprador, lsIvaComprador, lsDocAsocL1, lsDocAsocL2, lsDocAsocL3, lsChequeRT, lsRazonTransBenL1, lsRazonTransBenL2, lsDirTransL1, lsDirTransL2, lsDirTransL3, lsTipIdTrans, lsNumIdTrans, lsIvaTrans, lsNombreChoferL1, lsNombreChoferL2, lsTipIdChofer, lsNumIdChofer, lsDomChoferL1, lsDomChoferL2);
        agregaItemDFNH(lsNombreItem, lsDescripcionItem, ldCantidad, ldPrecioUnitario, liTasaIva, lsCodInt, lsCodIva, lsMontoItem);
        subTotal();
        cerrarComprobante();
        cerrarPuerto();
    }

    
}
