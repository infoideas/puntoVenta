package entidades;
// Generated 8 oct. 2022 10:23:07 by Hibernate Tools 4.3.1



/**
 * VentaEstanciaRemito generated by hbm2java
 */
public class VentaEstanciaRemito  implements java.io.Serializable {


     private Integer id;
     private RemitoVenta remitoVenta;
     private VentaEstancia ventaEstancia;

    public VentaEstanciaRemito() {
    }

    public VentaEstanciaRemito(RemitoVenta remitoVenta, VentaEstancia ventaEstancia) {
       this.remitoVenta = remitoVenta;
       this.ventaEstancia = ventaEstancia;
    }
   
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    public RemitoVenta getRemitoVenta() {
        return this.remitoVenta;
    }
    
    public void setRemitoVenta(RemitoVenta remitoVenta) {
        this.remitoVenta = remitoVenta;
    }
    public VentaEstancia getVentaEstancia() {
        return this.ventaEstancia;
    }
    
    public void setVentaEstancia(VentaEstancia ventaEstancia) {
        this.ventaEstancia = ventaEstancia;
    }




}

