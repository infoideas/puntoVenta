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
            String URL="jdbc:mysql://" + ls_servidor + ":" + ls_puerto + "/gestion?zeroDateTimeBehavior=convertToNull&serverTimezone=America/Buenos_Aires";
            
            sessionFactory=new Configuration( )
             .addResource ("entidades/PorcentajeIva.hbm.xml")                                               
             .addResource ("entidades/Rubro.hbm.xml")   
             .addResource ("entidades/CausaDevolucion.hbm.xml")  
             .addResource ("entidades/Empleado.hbm.xml")   
             .addResource ("entidades/Marca.hbm.xml")          
             .addResource ("entidades/LocalEmpleado.hbm.xml")  
             .addResource ("entidades/Producto.hbm.xml")  
             .addResource ("entidades/CondIva.hbm.xml")         
             .addResource ("entidades/LocalCarniceria.hbm.xml")    
             .addResource ("entidades/LocalRubro.hbm.xml")                        
             .addResource ("entidades/VentaCc.hbm.xml")     
             .addResource ("entidades/Empresa.hbm.xml")       
             .addResource ("entidades/Moneda.hbm.xml")                          
             .addResource ("entidades/FormaPago.hbm.xml")      
             .addResource ("entidades/UnidadMedida.hbm.xml")         
             .addResource ("entidades/AreaNegocio.hbm.xml")      
             .addResource ("entidades/Tarjeta.hbm.xml")           
             .addResource ("entidades/SubRubro.hbm.xml")      
             .addResource ("entidades/Pais.hbm.xml")        
             .addResource ("entidades/Venta.hbm.xml")   
             .addResource ("entidades/VentaDet.hbm.xml")   
             .addResource ("entidades/Provincia.hbm.xml")      
             .addResource ("entidades/Cargo.hbm.xml")         
             .addResource ("entidades/ListaPrecios.hbm.xml")   
             .addResource ("entidades/ListaPreciosDet.hbm.xml")                      
             .addResource ("entidades/ListaPreciosLocal.hbm.xml")   
             .addResource ("entidades/ListaPreciosLocalDet.hbm.xml")                      
             .addResource ("entidades/Persona.hbm.xml")            
             .addResource ("entidades/PersonaDireccion.hbm.xml")       
             .addResource ("entidades/Localidad.hbm.xml")            
             .addResource ("entidades/Tipoid.hbm.xml")                                
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
