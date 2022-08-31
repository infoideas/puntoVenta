/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pdv;

import controllers.LoginController;
import entidades.Empresa;
import entidades.Localidad;
import entidades.Producto;
import entidades.Provincia;
import entidades.UnidadMedida;
import general.BeanBase;
import general.HibernateUtil;
import general.UsuarioAdmin;
import java.io.InputStream;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.hibernate.HibernateException;
import org.hibernate.Session;

/**
 *
 * @author Propietario
 */
public class PuntoVenta extends Application {
    private Stage stage;
    private static UsuarioAdmin usuarioConectado;
    private static Empresa empresaSel;
    private static Producto productoGenerico;
    private static UnidadMedida unidadGenerica;
    private static Provincia provinciaEmpresa;
    private static Localidad localidadEmpresa;
    private static String puntoVentaCF;
    private static int puertoCF;
    private final static String tituloApp="Punto de Venta";

    public static UsuarioAdmin getUsuarioConectado() {
        return usuarioConectado;
    }

    public static void setUsuarioConectado(UsuarioAdmin usuarioConectado) {
        PuntoVenta.usuarioConectado = usuarioConectado;
    }

    public static Empresa getEmpresaSel() {
        return empresaSel;
    }

    public static void setEmpresaSel(Empresa empresaSel) {
        PuntoVenta.empresaSel = empresaSel;
    }

    public static Producto getProductoGenerico() {
        return productoGenerico;
    }

    public static void setProductoGenerico(Producto productoGenerico) {
        PuntoVenta.productoGenerico = productoGenerico;
    }

    public static UnidadMedida getUnidadGenerica() {
        return unidadGenerica;
    }

    public static void setUnidadGenerica(UnidadMedida unidadGenerica) {
        PuntoVenta.unidadGenerica = unidadGenerica;
    }

    public static Provincia getProvinciaEmpresa() {
        return provinciaEmpresa;
    }

    public static void setProvinciaEmpresa(Provincia provinciaEmpresa) {
        PuntoVenta.provinciaEmpresa = provinciaEmpresa;
    }

    public static Localidad getLocalidadEmpresa() {
        return localidadEmpresa;
    }

    public static void setLocalidadEmpresa(Localidad localidadEmpresa) {
        PuntoVenta.localidadEmpresa = localidadEmpresa;
    }

    public static String getPuntoVentaCF() {
        return puntoVentaCF;
    }

    public static void setPuntoVentaCF(String puntoVentaCF) {
        PuntoVenta.puntoVentaCF = puntoVentaCF;
    }

    public static int getPuertoCF() {
        return puertoCF;
    }

    public static void setPuertoCF(int puertoCF) {
        PuntoVenta.puertoCF = puertoCF;
    }

    public static String getTituloApp() {
        return tituloApp;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        BeanBase beanBase= new BeanBase();
        Empresa empresaSel=beanBase.obtenerEmpresa(Integer.valueOf(beanBase.getConfiguracion().getProperty("idEmpresa").trim())); 
        setEmpresaSel(empresaSel);
        productoGenerico=obtieneProducto(1);
        unidadGenerica=obtieneUnidad(1);
        provinciaEmpresa=obtieneProvincia(Integer.valueOf(beanBase.getConfiguracion().getProperty("idProvincia").trim()));
        localidadEmpresa=obtieneLocalidad(Integer.valueOf(beanBase.getConfiguracion().getProperty("idLocalidad").trim()));
       
        //Punto de venta del controlador fiscal
        puntoVentaCF=beanBase.getConfiguracion().getProperty("puntoVentaCF").trim();
        if (puntoVentaCF==null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setTitle("Error");
            alert.setContentText("No existe el punto de venta asociado");
            alert.show();
            return;
        }    
        
        //Puerto COM del controlador fiscal
        puertoCF=Integer.valueOf(beanBase.getConfiguracion().getProperty("puertoCF").trim());
        gotoLogin();
    }

    private void gotoLogin() {
        try {
            LoginController login = (LoginController) replaceSceneContent("/vistas/LogIn.fxml");
            //login.setApp(this);
            stage.show();
            stage.setOnCloseRequest(e->{
                System.exit(0);            
            });
    
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage()); 
        }
    }
    
    public Initializable replaceSceneContent(String fxml) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        InputStream in = PuntoVenta.class.getResourceAsStream(fxml);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(PuntoVenta.class.getResource(fxml));
        GridPane page;
        try {
            page = (GridPane) loader.load(in);
        } finally {
            in.close();
        } 
        Scene scene = new Scene(page, 800,600);
        stage.setScene(scene);
        return (Initializable) loader.getController();
    }
    
    //Obtiene provincia de la empresa
    public Provincia obtieneProvincia(int codProvincia){
            Provincia provincia = null;
            Session session = null;
            try{
                session=HibernateUtil.getSessionFactory().openSession();
                session.beginTransaction();
                provincia=(Provincia) session.get(Provincia.class,codProvincia);
                session.getTransaction().commit();
            }
            catch (HibernateException e){
                session.getTransaction().rollback();
                return null;
            }
            finally {
                session.close();
            }
            return provincia;
    }
    
    //Obtiene localidad de la empresa
    public Localidad obtieneLocalidad(int codLocalidad){
            Localidad localidad = null;
            Session session = null;
            try{
                session=HibernateUtil.getSessionFactory().openSession();
                session.beginTransaction();
                localidad=(Localidad) session.get(Localidad.class,codLocalidad);
                session.getTransaction().commit();
            }
            catch (HibernateException e){
                session.getTransaction().rollback();
                return null;
            }
            finally {
                session.close();
            }
            return localidad;
    }
    
    //Obtiene producto
    public Producto obtieneProducto(int codProducto){
            Producto producto = null;
            Session session = null;
            try{
                session=HibernateUtil.getSessionFactory().openSession();
                session.beginTransaction();
                producto=(Producto) session.get(Producto.class,codProducto);
                session.getTransaction().commit();
            }
            catch (HibernateException e){
                session.getTransaction().rollback();
                return null;
            }
            finally {
                session.close();
            }
            return producto;
    }
    
    //Obtiene unidad de medida
    public UnidadMedida obtieneUnidad(int codUnidad){
            UnidadMedida unidad = null;
            Session session = null;
            try{
                session=HibernateUtil.getSessionFactory().openSession();
                session.beginTransaction();
                unidad=(UnidadMedida) session.get(UnidadMedida.class,codUnidad);
                session.getTransaction().commit();
            }
            catch (HibernateException e){
                session.getTransaction().rollback();
                return null;
            }
            finally {
                session.close();
            }
            return unidad;
    }
    
}
