/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import com.sun.javafx.logging.PlatformLogger.Level;
import entidades.Venta;
import epson.ImpresionEpson;
import general.ItemMenu;
import general.UsuarioAdmin;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.jboss.logging.Logger;
import pdv.PuntoVenta;

/**
 *
 * @author rafaelg
 */
public class PrincipalController extends VBox implements Initializable {
    @FXML
    VBox box;
    @FXML
    MenuItem menuitemSalir;
    @FXML
    MenuItem menuitemBancos;
    @FXML
    TreeView tvMenu;
    @FXML
    Label nombreUsuario;
    @FXML
    Label servidor;
    
      
    private UsuarioAdmin usuario;

    public UsuarioAdmin getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioAdmin usuario) {
        this.usuario = usuario;
    }

    public PrincipalController() {
     
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("Inicializando");
      //Creating tree items
      
      ItemMenu itemMenu= new ItemMenu("Configuracion","Configuración");
      TreeItem root1 = new TreeItem(itemMenu);
      itemMenu= new ItemMenu("Cuentas","Cuentas");
      TreeItem item11 = new TreeItem(itemMenu);
      itemMenu= new ItemMenu("Bancos","Bancos");
      TreeItem item12 = new TreeItem(itemMenu);
      itemMenu= new ItemMenu("Conceptos","Conceptos");
      TreeItem item13 = new TreeItem(itemMenu);
      //Adding elements to root1
      root1.getChildren().addAll(item11,item12,item13);
      
      ItemMenu itemMenu2= new ItemMenu("Ventas","Ventas");
      TreeItem root2 = new TreeItem(itemMenu2);
      itemMenu= new ItemMenu("IngresarVenta","Ingresar venta");
      TreeItem item21 = new TreeItem(itemMenu);
      itemMenu= new ItemMenu("IngresarVentaRapida","Ingresar venta rápida");
      TreeItem item22 = new TreeItem(itemMenu);
      itemMenu= new ItemMenu("ConsultaVentas","Consulta de ventas");
      TreeItem item23 = new TreeItem(itemMenu);
      //Adding elements to root2
      root2.getChildren().addAll(item21,item22,item23);
      
      ItemMenu itemMenu3= new ItemMenu("Cierre de jornada","Jornada");
      TreeItem root3 = new TreeItem(itemMenu3);
      itemMenu= new ItemMenu("CierreZ","Cierre Z");
      TreeItem item31 = new TreeItem(itemMenu);
      itemMenu= new ItemMenu("CierreX","Cierre X");
      TreeItem item32 = new TreeItem(itemMenu);
      //Adding elements to root2
      root3.getChildren().addAll(item31,item32);
      
      TreeItem<String> base = new TreeItem<String>("Gestión");
      base.setExpanded(true);
      base.getChildren().addAll(root1, root2,root3);
      //Creating a TreeView item
      
      tvMenu.setRoot(base);
      
        //Doble click en menuitem del treeView
        tvMenu.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent mouseEvent)
            {            
                if(mouseEvent.getClickCount() == 2)
                {   
                    TreeItem<ItemMenu> item = (TreeItem<ItemMenu>) tvMenu.getSelectionModel().getSelectedItem();
                    if (item!=null){
                        switch (item.getValue().getVentana()){
                            case "ConsultaVentas":
                             irPantallaVentas("/vistas/ConsultaVentas.fxml");
                             break;
                            case "Cuentas":
                             //irPantallaCuentas("CuentasFondos.fxml");
                             break;
                            case "PuntoVentaPendientes":
                             //irPuntoVentaPendientes("ConsultaPuntoVentaPendientes.fxml");
                             break;                                
                            case "ConsultaPuntoVenta":
                             //irPantallaPuntoVenta("ConsultaPuntoVenta.fxml");
                             break;                             
                            case "IngresarVenta":
                             irNuevaVenta("/vistas/VentaDetalle.fxml",false);
                             break;                             
                            case "IngresarVentaRapida":
                             irNuevaVenta("/vistas/VentaDetalle.fxml",true);
                             break;                             
                            case "CierreZ":
                             Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                             alert.setTitle("Confirmación");
                             alert.setContentText("¿Está seguro de realizar el cierre Z del controlador fiscal?");
                             Optional<ButtonType> action = alert.showAndWait();
                             if (action.get() == ButtonType.OK) {
                                //Imprimo el ticket fiscal
                                ImpresionEpson impresion = new ImpresionEpson();
                                //Configuro el puerto COM del controlador fiscal
                                impresion.setPuerto(PuntoVenta.getPuertoCF());
                                impresion.inicializaControlador();
                                impresion.abrirPuerto();
                                impresion.cierreZ();
                                impresion.cerrarPuerto();
                             }
                             break;   
                             
                             case "CierreX":
                             alert = new Alert(Alert.AlertType.CONFIRMATION);
                             alert.setTitle("Confirmación");
                             alert.setContentText("¿Está seguro de realizar el cierre X del controlador fiscal?");
                             action = alert.showAndWait();
                             if (action.get() == ButtonType.OK) {
                                //Imprimo el ticket fiscal
                                ImpresionEpson impresion = new ImpresionEpson();
                                //Configuro el puerto COM del controlador fiscal
                                impresion.setPuerto(PuntoVenta.getPuertoCF());
                                impresion.inicializaControlador();
                                impresion.abrirPuerto();
                                impresion.cierreX();
                                impresion.cerrarPuerto();
                             }
                             break;  
                             
                        default:
                            return;
                        }   
                    }   
                }
            }
        });      
			      
      nombreUsuario.setText("Usuario: " + PuntoVenta.getUsuarioConectado().getNombreCompletoUsuario());
    }
    
    
    public void irPantallaVentas(String fxml) {
        //Cargo nuevo stage
        FXMLLoader firstPaneLoader = new FXMLLoader(getClass().getResource(fxml));
        //Controlador de la nueva ventana
        VentaController ventasController = (VentaController) firstPaneLoader.getController();
        
        Parent newPane = null;
        try {
            newPane = firstPaneLoader.load();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());                        
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setTitle("Error");
            alert.setContentText("No se pudo cargar la ventana");
            alert.show();
            return;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());            
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setTitle("Error");
            alert.setContentText("No se pudo cargar la ventana");
            alert.show();
            return;
        }
        
        Scene newScene = new Scene(newPane, 1024,700);
        Stage nuevoStage = new Stage();
        nuevoStage.setScene(newScene);
        nuevoStage.setTitle("Consulta de ventas");
        nuevoStage.show();
    }
    
    
    
//    public void irPuntoVentaPendientes(String fxml) {
//        //Cargo nuevo stage
//        FXMLLoader firstPaneLoader = new FXMLLoader(getClass().getResource(fxml));
//        //Controlador de la nueva ventana
//        PedidoPendienteController pedidosController = (PedidoPendienteController) firstPaneLoader.getController();
//        
//        Parent newPane = null;
//        try {
//            newPane = firstPaneLoader.load();
//        } catch (IOException ex) {
//            System.out.println(ex.getMessage());                        
//            Alert alert = new Alert(Alert.AlertType.INFORMATION);
//            alert.setHeaderText(null);
//            alert.setTitle("Error");
//            alert.setContentText("No se pudo cargar la ventana");
//            alert.show();
//            return;
//        } catch (Exception ex) {
//            System.out.println(ex.getMessage());            
//            Alert alert = new Alert(Alert.AlertType.INFORMATION);
//            alert.setHeaderText(null);
//            alert.setTitle("Error");
//            alert.setContentText("No se pudo cargar la ventana");
//            alert.show();
//            return;
//        }
//        
//        Scene newScene = new Scene(newPane, 1024,700);
//        Stage nuevoStage = new Stage();
//        nuevoStage.setScene(newScene);
//        nuevoStage.setTitle("PuntoVenta pendientes");
//        nuevoStage.show();
//    }
//    
//    public void irPantallaPuntoVenta(String fxml) {
//        //Cargo nuevo stage
//        FXMLLoader firstPaneLoader = new FXMLLoader(getClass().getResource(fxml));
//        //Controlador de la nueva ventana
//        PedidoController pedidosController = (PedidoController) firstPaneLoader.getController();
//        
//        Parent newPane = null;
//        try {
//            newPane = firstPaneLoader.load();
//        } catch (IOException ex) {
//            System.out.println(ex.getMessage());                        
//            Alert alert = new Alert(Alert.AlertType.INFORMATION);
//            alert.setHeaderText(null);
//            alert.setTitle("Error");
//            alert.setContentText("No se pudo cargar la ventana");
//            alert.show();
//            return;
//        } catch (Exception ex) {
//            System.out.println(ex.getMessage());            
//            Alert alert = new Alert(Alert.AlertType.INFORMATION);
//            alert.setHeaderText(null);
//            alert.setTitle("Error");
//            alert.setContentText("No se pudo cargar la ventana");
//            alert.show();
//            return;
//        }
//        
//        Scene newScene = new Scene(newPane, 1024,700);
//        Stage nuevoStage = new Stage();
//        nuevoStage.setScene(newScene);
//        nuevoStage.setTitle("Consulta de pedidos");
//        nuevoStage.show();
//    }
//    
    public void irNuevaVenta(String fxml,boolean isVentaRapida) {
        //Cargo nuevo stage
        FXMLLoader firstPaneLoader = new FXMLLoader(getClass().getResource(fxml));
        Parent newPane = null;
        try {
            newPane = firstPaneLoader.load();
            //Controlador de la nueva ventana
            VentaDetalleController controller = (VentaDetalleController) firstPaneLoader.getController();
            controller.setRegistroSel(new Venta());
            controller.nuevaVenta(isVentaRapida);
            
            Scene newScene = new Scene(newPane, 1024,700);
            Stage nuevoStage = new Stage();
            nuevoStage.setScene(newScene);
            nuevoStage.setTitle("Nueva venta");
            //nuevoStage.initModality(Modality.APPLICATION_MODAL);
            nuevoStage.setResizable(true);
            nuevoStage.show();
            
        
        } catch (IOException ex) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setTitle("Error");
            alert.setContentText("No se pudo cargar la ventana: " + ex.getMessage());
            alert.show();
            return;
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setTitle("Error");
            alert.setContentText("No se pudo cargar la ventana: " + ex.getMessage());
            alert.show();
            return;
        }
       
    }
    
}
