package entidades;
// Generated 8 oct. 2022 10:23:07 by Hibernate Tools 4.3.1


import java.math.BigDecimal;
import java.util.Date;

/**
 * VentaCc generated by hbm2java
 */
public class VentaCc  implements java.io.Serializable {


     private Integer id;
     private FormaPago formaPago;
     private Tarjeta tarjeta;
     private Venta venta;
     private Date fecMov;
     private Date fecCarga;
     private int idUsuario;
     private String numBt;
     private BigDecimal valor;
     private char estado;

    public VentaCc() {
    }

	
    public VentaCc(FormaPago formaPago, Venta venta, Date fecMov, Date fecCarga, int idUsuario, BigDecimal valor, char estado) {
        this.formaPago = formaPago;
        this.venta = venta;
        this.fecMov = fecMov;
        this.fecCarga = fecCarga;
        this.idUsuario = idUsuario;
        this.valor = valor;
        this.estado = estado;
    }
    public VentaCc(FormaPago formaPago, Tarjeta tarjeta, Venta venta, Date fecMov, Date fecCarga, int idUsuario, String numBt, BigDecimal valor, char estado) {
       this.formaPago = formaPago;
       this.tarjeta = tarjeta;
       this.venta = venta;
       this.fecMov = fecMov;
       this.fecCarga = fecCarga;
       this.idUsuario = idUsuario;
       this.numBt = numBt;
       this.valor = valor;
       this.estado = estado;
    }
   
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    public FormaPago getFormaPago() {
        return this.formaPago;
    }
    
    public void setFormaPago(FormaPago formaPago) {
        this.formaPago = formaPago;
    }
    public Tarjeta getTarjeta() {
        return this.tarjeta;
    }
    
    public void setTarjeta(Tarjeta tarjeta) {
        this.tarjeta = tarjeta;
    }
    public Venta getVenta() {
        return this.venta;
    }
    
    public void setVenta(Venta venta) {
        this.venta = venta;
    }
    public Date getFecMov() {
        return this.fecMov;
    }
    
    public void setFecMov(Date fecMov) {
        this.fecMov = fecMov;
    }
    public Date getFecCarga() {
        return this.fecCarga;
    }
    
    public void setFecCarga(Date fecCarga) {
        this.fecCarga = fecCarga;
    }
    public int getIdUsuario() {
        return this.idUsuario;
    }
    
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
    public String getNumBt() {
        return this.numBt;
    }
    
    public void setNumBt(String numBt) {
        this.numBt = numBt;
    }
    public BigDecimal getValor() {
        return this.valor;
    }
    
    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
    public char getEstado() {
        return this.estado;
    }
    
    public void setEstado(char estado) {
        this.estado = estado;
    }




}


