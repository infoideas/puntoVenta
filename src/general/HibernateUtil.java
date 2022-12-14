/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package general;

import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

/**
 * Hibernate Utility class with a convenient method to get Session Factory
 * object.
 *
 * @author Propietario
 */

import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.SessionFactory;

/**
 * Hibernate Utility class with a convenient method to get Session Factory
 * object.
 *
 * @author rafael
 */
public class HibernateUtil {

    private static final SessionFactory sessionFactory;
    
    static {
        try {
            // Create the SessionFactory from standard (hibernate.cfg.xml) 
            // config file.
            
            BeanBase beanBase= new BeanBase();
            String ls_servidor=(String) beanBase.getConfiguracion().getProperty("servidor").trim();
            String  ls_puerto=(String) beanBase.getConfiguracion().getProperty("puerto").trim();
            String URL="jdbc:mysql://" + ls_servidor + ":" + ls_puerto + "/estancia?zeroDateTimeBehavior=convertToNull&serverTimezone=America/Buenos_Aires";
            
            sessionFactory=new Configuration( )
             .addResource ("entidades/AreaNegocio.hbm.xml")                          
             .addResource ("entidades/Balance.hbm.xml")                          
             .addResource ("entidades/BalanceCobranza.hbm.xml")        
             .addResource ("entidades/BalanceDifPesada.hbm.xml")      
             .addResource ("entidades/BalanceEfectivo.hbm.xml")                          
             .addResource ("entidades/BalanceGastoExtra.hbm.xml")      
             .addResource ("entidades/BalancePorCobrar.hbm.xml")     
             .addResource ("entidades/BalanceRemanente.hbm.xml")    
             .addResource ("entidades/BalanceGastoPersonal.hbm.xml")    
             .addResource ("entidades/BalanceGastoVario.hbm.xml")                        
             .addResource ("entidades/BalanceIngreso.hbm.xml")       
             .addResource ("entidades/BalanceInvIni.hbm.xml")              
             .addResource ("entidades/Banco.hbm.xml") 
             .addResource ("entidades/Cargo.hbm.xml")                        
             .addResource ("entidades/Categoria.hbm.xml")                        
             .addResource ("entidades/CategoriaDet.hbm.xml")                        
             .addResource ("entidades/CausaDevolucion.hbm.xml")      
             .addResource ("entidades/Chofer.hbm.xml")     
             .addResource ("entidades/Cliente.hbm.xml")                          
             .addResource ("entidades/Comisionista.hbm.xml")     
             .addResource ("entidades/Compra.hbm.xml")                          
             .addResource ("entidades/CompraDet.hbm.xml")    
             .addResource ("entidades/CondIva.hbm.xml")                        
             .addResource ("entidades/DireccionUsuarioCliente.hbm.xml")                          
             .addResource ("entidades/CostoFleteAnimales.hbm.xml")   
             .addResource ("entidades/CostoFleteGranos.hbm.xml")   
             .addResource ("entidades/CuentaBanco.hbm.xml")          
             .addResource ("entidades/Deposito.hbm.xml")          
             .addResource ("entidades/EfectivoFlete.hbm.xml")                              
             .addResource ("entidades/Empleado.hbm.xml")                              
             .addResource ("entidades/Empresa.hbm.xml")    
             .addResource ("entidades/Entrega.hbm.xml")                           
             .addResource ("entidades/EntregaDet.hbm.xml")                           
             .addResource ("entidades/Gasto.hbm.xml")      
             .addResource ("entidades/InventarioTropa.hbm.xml")                          
             .addResource ("entidades/LiquidacionFlete.hbm.xml")      
             .addResource ("entidades/LiquidacionGasto.hbm.xml")      
             .addResource ("entidades/LiquidacionViaje.hbm.xml") 
             .addResource ("entidades/LiquidacionEfectivo.hbm.xml")                     
             .addResource ("entidades/MovStock.hbm.xml") 
             .addResource ("entidades/MovStockDet.hbm.xml") 
             .addResource ("entidades/Productor.hbm.xml")         
             .addResource ("entidades/Proveedor.hbm.xml")   
             .addResource ("entidades/RemitoVenta.hbm.xml")          
             .addResource ("entidades/RemitoVentaDet.hbm.xml")                       
             .addResource ("entidades/Repartidor.hbm.xml") 
             .addResource ("entidades/TipoGasto.hbm.xml")                     
             .addResource ("entidades/TipoGrano.hbm.xml")  
             .addResource ("entidades/Tipoid.hbm.xml")       
             .addResource ("entidades/Tropa.hbm.xml")                                                    
             .addResource ("entidades/TropaDet.hbm.xml")                                                                        
             .addResource ("entidades/TropaDetGarron.hbm.xml")                                                                        
             .addResource ("entidades/TropaPagoCiva.hbm.xml")   
             .addResource ("entidades/TropaViaje.hbm.xml")   
             .addResource ("entidades/UnidadNegocio.hbm.xml")                       
             .addResource ("entidades/UnidadNegocioGasto.hbm.xml")                       
             .addResource ("entidades/VehiculoFlete.hbm.xml")                       
             .addResource ("entidades/ListaPrecios.hbm.xml")                        
             .addResource ("entidades/ListaPreciosDet.hbm.xml")                                            
             .addResource ("entidades/ListaPreciosLocal.hbm.xml")                        
             .addResource ("entidades/ListaPreciosLocalDet.hbm.xml")                                            
             .addResource ("entidades/LocalCarniceria.hbm.xml")     
             .addResource ("entidades/LocalEmpleado.hbm.xml")                         
             .addResource ("entidades/LocalRubro.hbm.xml")                         
             .addResource ("entidades/Localidad.hbm.xml")                         
             .addResource ("entidades/Marca.hbm.xml")                                             
             .addResource ("entidades/Moneda.hbm.xml")                          
             .addResource ("entidades/Pais.hbm.xml")                          
             .addResource ("entidades/Persona.hbm.xml")                          
             .addResource ("entidades/PersonaDireccion.hbm.xml")                                              
             .addResource ("entidades/PorcentajeIva.hbm.xml")                                                                   
             .addResource ("entidades/FormaPago.hbm.xml")   
             .addResource ("entidades/Producto.hbm.xml")   
             .addResource ("entidades/Provincia.hbm.xml")                          
             .addResource ("entidades/Rubro.hbm.xml")  
             .addResource ("entidades/SubRubro.hbm.xml")                       
             .addResource ("entidades/Tarjeta.hbm.xml")           
             .addResource ("entidades/Unidad.hbm.xml")                             
             .addResource ("entidades/UsuarioCliente.hbm.xml")                                          
             .addResource ("entidades/Venta.hbm.xml")   
             .addResource ("entidades/VentaCc.hbm.xml")                         
             .addResource ("entidades/VentaDet.hbm.xml")   
             .addResource ("entidades/VentaEstancia.hbm.xml")   
             .addResource ("entidades/VentaEstanciaDet.hbm.xml")  
             .addResource ("entidades/VentaEstanciaRemito.hbm.xml")                       
             .addResource ("entidades/Viaje.hbm.xml")                       
             .setProperty("hibernate.connection.driver_class","com.mysql.cj.jdbc.Driver")                                        
	     .setProperty("hibernate.connection.url",URL)
             .setProperty("hibernate.connection.username","dba")                    
             .setProperty("hibernate.connection.password","Admin*Duende*2022")                                        
             .setProperty("hibernate.dialect","org.hibernate.dialect.MySQLDialect")     
             .setProperty("hibernate.show_sql","true")         
             .setProperty("hibernate.connection.characterEncoding","utf8")        
	     .buildSessionFactory();
            
            //sessionFactory = new AnnotationConfiguration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            // Log the exception. 
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }
    
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
