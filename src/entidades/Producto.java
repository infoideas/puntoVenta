package entidades;
// Generated 8 oct. 2022 10:23:07 by Hibernate Tools 4.3.1


import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * Producto generated by hbm2java
 */
public class Producto  implements java.io.Serializable {


     private Integer id;
     private Marca marca;
     private PorcentajeIva porcentajeIva;
     private Rubro rubro= new Rubro();
     private Unidad unidad= new Unidad();
     private String nombre;
     private String detalle;
     private char tipo;
     private BigDecimal stockMinimo;
     private BigDecimal precioContado;
     private char estado;
     private char elaborado;
     private Set<RemitoVentaDet> remitoVentaDets = new HashSet<RemitoVentaDet>(0);
     private Set<BalanceIngreso> balanceIngresos = new HashSet<BalanceIngreso>(0);
     private Set<CategoriaDet> categoriaDets = new HashSet<CategoriaDet>(0);
     private Set<VentaEstanciaDet> ventaEstanciaDets = new HashSet<VentaEstanciaDet>(0);
     private Set<CompraDet> compraDets = new HashSet<CompraDet>(0);
     private Set<ListaPreciosLocalDet> listaPreciosLocalDets = new HashSet<ListaPreciosLocalDet>(0);
     private Set<MovStockDet> movStockDets = new HashSet<MovStockDet>(0);
     private Set<Categoria> categorias = new HashSet<Categoria>(0);
     private Set<BalanceInvIni> balanceInvInis = new HashSet<BalanceInvIni>(0);
     private Set<BalanceRemanente> balanceRemanentes = new HashSet<BalanceRemanente>(0);
     private Set<ListaPreciosDet> listaPreciosDets = new HashSet<ListaPreciosDet>(0);
     private Set<VentaDet> ventaDets = new HashSet<VentaDet>(0);

    public Producto() {
    }

	
    public Producto(PorcentajeIva porcentajeIva, Rubro rubro, Unidad unidad, String nombre, char tipo, char estado, char elaborado) {
        this.porcentajeIva = porcentajeIva;
        this.rubro = rubro;
        this.unidad = unidad;
        this.nombre = nombre;
        this.tipo = tipo;
        this.estado = estado;
        this.elaborado = elaborado;
    }
    public Producto(Marca marca, PorcentajeIva porcentajeIva, Rubro rubro, Unidad unidad, String nombre, String detalle, char tipo, BigDecimal stockMinimo, char estado, char elaborado, Set<RemitoVentaDet> remitoVentaDets, Set<BalanceIngreso> balanceIngresos, Set<CategoriaDet> categoriaDets, Set<VentaEstanciaDet> ventaEstanciaDets, Set<CompraDet> compraDets, Set<ListaPreciosLocalDet> listaPreciosLocalDets, Set<MovStockDet> movStockDets, Set<Categoria> categorias, Set<BalanceInvIni> balanceInvInis, Set<BalanceRemanente> balanceRemanentes, Set<ListaPreciosDet> listaPreciosDets, Set<VentaDet> ventaDets) {
       this.marca = marca;
       this.porcentajeIva = porcentajeIva;
       this.rubro = rubro;
       this.unidad = unidad;
       this.nombre = nombre;
       this.detalle = detalle;
       this.tipo = tipo;
       this.stockMinimo = stockMinimo;
       this.estado = estado;
       this.elaborado = elaborado;
       this.remitoVentaDets = remitoVentaDets;
       this.balanceIngresos = balanceIngresos;
       this.categoriaDets = categoriaDets;
       this.ventaEstanciaDets = ventaEstanciaDets;
       this.compraDets = compraDets;
       this.listaPreciosLocalDets = listaPreciosLocalDets;
       this.movStockDets = movStockDets;
       this.categorias = categorias;
       this.balanceInvInis = balanceInvInis;
       this.balanceRemanentes = balanceRemanentes;
       this.listaPreciosDets = listaPreciosDets;
       this.ventaDets = ventaDets;
    }
   
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    public Marca getMarca() {
        return this.marca;
    }
    
    public void setMarca(Marca marca) {
        this.marca = marca;
    }
    public PorcentajeIva getPorcentajeIva() {
        return this.porcentajeIva;
    }
    
    public void setPorcentajeIva(PorcentajeIva porcentajeIva) {
        this.porcentajeIva = porcentajeIva;
    }
    public Rubro getRubro() {
        return this.rubro;
    }
    
    public void setRubro(Rubro rubro) {
        this.rubro = rubro;
    }
    public Unidad getUnidad() {
        return this.unidad;
    }
    
    public void setUnidad(Unidad unidad) {
        this.unidad = unidad;
    }
    public String getNombre() {
        return this.nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getDetalle() {
        return this.detalle;
    }
    
    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }
    public char getTipo() {
        return this.tipo;
    }
    
    public void setTipo(char tipo) {
        this.tipo = tipo;
    }
    public BigDecimal getStockMinimo() {
        return this.stockMinimo;
    }
    
    public void setStockMinimo(BigDecimal stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public BigDecimal getPrecioContado() {
        return precioContado;
    }

    public void setPrecioContado(BigDecimal precioContado) {
        this.precioContado = precioContado;
    }
    
    public char getEstado() {
        return this.estado;
    }
    
    public void setEstado(char estado) {
        this.estado = estado;
    }
    public char getElaborado() {
        return this.elaborado;
    }
    
    public void setElaborado(char elaborado) {
        this.elaborado = elaborado;
    }
    
    public Set<RemitoVentaDet> getRemitoVentaDets() {
        return this.remitoVentaDets;
    }
    
    public void setRemitoVentaDets(Set<RemitoVentaDet> remitoVentaDets) {
        this.remitoVentaDets = remitoVentaDets;
    }
    public Set<BalanceIngreso> getBalanceIngresos() {
        return this.balanceIngresos;
    }
    
    public void setBalanceIngresos(Set<BalanceIngreso> balanceIngresos) {
        this.balanceIngresos = balanceIngresos;
    }
    public Set<CategoriaDet> getCategoriaDets() {
        return this.categoriaDets;
    }
    
    public void setCategoriaDets(Set<CategoriaDet> categoriaDets) {
        this.categoriaDets = categoriaDets;
    }
    public Set<VentaEstanciaDet> getVentaEstanciaDets() {
        return this.ventaEstanciaDets;
    }
    
    public void setVentaEstanciaDets(Set<VentaEstanciaDet> ventaEstanciaDets) {
        this.ventaEstanciaDets = ventaEstanciaDets;
    }
    public Set<CompraDet> getCompraDets() {
        return this.compraDets;
    }
    
    public void setCompraDets(Set<CompraDet> compraDets) {
        this.compraDets = compraDets;
    }
    public Set<ListaPreciosLocalDet> getListaPreciosLocalDets() {
        return this.listaPreciosLocalDets;
    }
    
    public void setListaPreciosLocalDets(Set<ListaPreciosLocalDet> listaPreciosLocalDets) {
        this.listaPreciosLocalDets = listaPreciosLocalDets;
    }
    public Set<MovStockDet> getMovStockDets() {
        return this.movStockDets;
    }
    
    public void setMovStockDets(Set<MovStockDet> movStockDets) {
        this.movStockDets = movStockDets;
    }
    public Set<Categoria> getCategorias() {
        return this.categorias;
    }
    
    public void setCategorias(Set<Categoria> categorias) {
        this.categorias = categorias;
    }
    public Set<BalanceInvIni> getBalanceInvInis() {
        return this.balanceInvInis;
    }
    
    public void setBalanceInvInis(Set<BalanceInvIni> balanceInvInis) {
        this.balanceInvInis = balanceInvInis;
    }
    public Set<BalanceRemanente> getBalanceRemanentes() {
        return this.balanceRemanentes;
    }
    
    public void setBalanceRemanentes(Set<BalanceRemanente> balanceRemanentes) {
        this.balanceRemanentes = balanceRemanentes;
    }
    public Set<ListaPreciosDet> getListaPreciosDets() {
        return this.listaPreciosDets;
    }
    
    public void setListaPreciosDets(Set<ListaPreciosDet> listaPreciosDets) {
        this.listaPreciosDets = listaPreciosDets;
    }
    public Set<VentaDet> getVentaDets() {
        return this.ventaDets;
    }
    
    public void setVentaDets(Set<VentaDet> ventaDets) {
        this.ventaDets = ventaDets;
    }

    @Override   
    public String toString(){
       return nombre;   
    }  




}


