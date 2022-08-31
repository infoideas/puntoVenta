/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;
import entidades.Empresa;
import general.BeanBase;
import general.Conector;
import general.HibernateUtil;
import general.LogInRespuesta;
import general.StringMD;
import general.UsuarioAdmin;
import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import pdv.PuntoVenta;

/**
 *
 * @author rafaelg
 */
public class LoginController extends VBox implements Initializable {
    private String usuario;
    private Scene newScene;  
    private Empresa empresaSel;
    
    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Scene getNewScene() {
        return newScene;
    }

    public void setNewScene(Scene newScene) {
        this.newScene = newScene;
    }

    public Empresa getEmpresaSel() {
        return empresaSel;
    }

    public void setEmpresaSel(Empresa empresaSel) {
        this.empresaSel = empresaSel;
    }

    
    @FXML
    TextField userId;
    @FXML
    PasswordField password;
    @FXML
    Button login;
    @FXML
    Label errorMessage;
    @FXML
    Label bienvenido;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        BeanBase beanBase= new BeanBase();
        Empresa emp=beanBase.obtenerEmpresa(Integer.valueOf(beanBase.getConfiguracion().getProperty("idEmpresa").trim()));         
        userId.setPromptText("Ingrese su usuario");
        userId.setText(beanBase.getConfiguracion().getProperty("usuario").trim());
        password.setPromptText("Ingrese su password");

        empresaSel=emp;
        bienvenido.setText("Bienvenido a " +  empresaSel.getNombreFantasia());
    }
    
    public void processLogin(ActionEvent event) throws Exception {
            UsuarioAdmin usuarioAdmin=validarLogin(userId.getText(),password.getText());
            if (usuarioAdmin.getEstado().equals("HABILITADO"))
            {
                PuntoVenta.setUsuarioConectado(usuarioAdmin);
                irPantallaPrincipal("/vistas/Principal.fxml",empresaSel);
            }
                
            else
                errorMessage.setText(usuarioAdmin.getObservaciones());
    }
    
    public UsuarioAdmin validarLogin(String nombreUsuario,String claveUsuario){
        CallableStatement s=null;
        ResultSet r=null;
        int li_id;
        String ls_nombreUsuario,ls_clave,ls_apellido,ls_nombre,ls_tipo,ls_estado;
        StringMD md5=new StringMD();
        String ls_clave_MD5;
        
        //Encripto la clave en MD5    
        ls_clave_MD5= md5.getStringMessageDigest(claveUsuario,"MD5");
        
        UsuarioAdmin usuario= new UsuarioAdmin();
        //Conectamos a la base
        Conector conector = new Conector();  
        Connection conexion = conector.connect("administracion");
        
        try {
               s=conexion.prepareCall("{call sp_get_def_usuario_admin ( ? )}",ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
               s.setString(1,nombreUsuario);
               
               r=s.executeQuery();
               if (r.next()){
                   li_id=r.getInt("id");
                   ls_nombreUsuario=r.getString("nombreUsuario");
                   ls_clave=r.getString("clave");
                   
                   ls_apellido=r.getString("apellido");
                   if (ls_apellido==null)
                       ls_apellido="";
                   
                   ls_nombre=r.getString("nombre");
                   if (ls_nombre==null)
                       ls_nombre="";
                   
                   ls_estado=r.getString("estado");
                   ls_tipo=r.getString("tipo");
                   
                   if (ls_clave.equals(ls_clave_MD5)){
                       usuario.setId(li_id);
                       usuario.setNombreUsuario(ls_nombreUsuario);
                       usuario.setNombreCompletoUsuario(ls_apellido + " " + ls_nombre);
                       
                       //setUsuarioAdmin(usuario);
                       if (ls_estado.equals("1"))
                           usuario.setEstado("HABILITADO");

                   }
                   else{
                       usuario.setId(0);
                       usuario.setEstado("NO HABILITADO");
                       usuario.setObservaciones("Clave incorrecta");
                       errorMessage.setText("Clave incorrecta");
                   }
               }
               else{
                       usuario.setId(0);
                       usuario.setEstado("NO HABILITADO");
                       usuario.setObservaciones("Usuario no existe");
                       errorMessage.setText("Usuario no existe");

               }
               return usuario;
        } catch (SQLException e){
            e.getMessage();
            return null;            
        } finally {
             try{
                if (conexion != null) conexion.close();   
                if (r != null) r.close();   
             }catch(Exception e)  {
                e.getMessage();
             }
        }

    }
    
    public void irPantallaPrincipal(String fxml,Empresa empresaSel) throws IOException{
        //Guardo la empresa seleccionada
        PuntoVenta.setEmpresaSel(empresaSel);
        
        //Stage actual
        Stage stageActual=(Stage) login.getScene().getWindow();
        
        //Cargo nuevo stage
        FXMLLoader firstPaneLoader = new FXMLLoader(getClass().getResource(fxml));
        
        Parent newPane = firstPaneLoader.load();
        Scene newScene = new Scene(newPane, 1366,700);
        Stage nuevoStage = new Stage();
        nuevoStage.setScene(newScene);
        nuevoStage.setTitle("Módulo de Gestión - Empresa: " + empresaSel.getNombreFantasia() );
        nuevoStage.show();
        
        //Controlador de la nueva ventana
        PrincipalController principalController = (PrincipalController) firstPaneLoader.getController();
        principalController.setUsuario(PuntoVenta.getUsuarioConectado());
        BeanBase beanBase= new BeanBase();
        principalController.servidor.setText("Servidor: " + beanBase.getConfiguracion().getProperty("servidor"));         
        
        
        //Volver a la ventana actual al cerrar ventana principal
        nuevoStage.setOnCloseRequest(e->{
            System.exit(0);
            //stageActual.show();
        });
        
        //Volver a la ventana actual al salir de la ventana principal
        MenuItem menuItem=principalController.menuitemSalir;
        VBox box=principalController.box;
        menuItem.setOnAction(event -> {
            System.exit(0);
            //stageActual.show();
            //nuevoStage.close();
        });
        
        //Cierra stage actual
        stageActual.close();
        
    }
    
    
}