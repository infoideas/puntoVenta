package entidades;
// Generated 3 ene. 2022 16:12:26 by Hibernate Tools 4.3.1


import java.math.BigDecimal;
import java.util.Date;

/**
 * BalanceGastoVario generated by hbm2java
 */
public class BalanceGastoVario  implements java.io.Serializable {


     private Integer id;
     private Balance balance;
     private Proveedor proveedor= new Proveedor();
     private TipoGasto tipoGasto= new TipoGasto();
     private Date fecha;
     private BigDecimal valor;
     private String observaciones;

    public BalanceGastoVario() {
    }

	
    public BalanceGastoVario(Balance balance, TipoGasto tipoGasto, Date fecha, BigDecimal valor) {
        this.balance = balance;
        this.tipoGasto = tipoGasto;
        this.fecha = fecha;
        this.valor = valor;
    }
    public BalanceGastoVario(Balance balance, Proveedor proveedor, TipoGasto tipoGasto, Date fecha, BigDecimal valor, String observaciones) {
       this.balance = balance;
       this.proveedor = proveedor;
       this.tipoGasto = tipoGasto;
       this.fecha = fecha;
       this.valor = valor;
       this.observaciones = observaciones;
    }
   
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    public Balance getBalance() {
        return this.balance;
    }
    
    public void setBalance(Balance balance) {
        this.balance = balance;
    }
    public Proveedor getProveedor() {
        return this.proveedor;
    }
    
    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }
    public TipoGasto getTipoGasto() {
        return this.tipoGasto;
    }
    
    public void setTipoGasto(TipoGasto tipoGasto) {
        this.tipoGasto = tipoGasto;
    }
    public Date getFecha() {
        return this.fecha;
    }
    
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    public BigDecimal getValor() {
        return this.valor;
    }
    
    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
    public String getObservaciones() {
        return this.observaciones;
    }
    
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }




}


