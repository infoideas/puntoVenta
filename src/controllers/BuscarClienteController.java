/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import entidades.Empresa;
import entidades.Persona;
import entidades.Producto;
import entidades.Rubro;
import general.BeanBase;
import general.HibernateUtil;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import pdv.PuntoVenta;

/**
 *
 * @author rafael
 */
public class BuscarClienteController extends VBox implements Initializable {
    @FXML 
    TextField tfNombre;
    @FXML
    Button buBuscar;
    @FXML
    Button buSeleccionar;
    @FXML
    Button buNuevo,buEditar;
    @FXML
    Button buSalir;
    @FXML
    TableView<Persona> dataClientes;
    @FXML
    private TableColumn<Persona,String> columnaNombre;
    @FXML
    private TableColumn<Persona,String> columnaDireccion;
        
    private ObservableList<Persona> listaData = FXCollections.observableArrayList();

    public TableView<Persona> getDataClientes() {
        return dataClientes;
    }

    public void setDataClientes(TableView<Persona> dataClientes) {
        this.dataClientes = dataClientes;
    }

    public TableColumn<Persona, String> getColumnaNombre() {
        return columnaNombre;
    }

    public void setColumnaNombre(TableColumn<Persona, String> columnaNombre) {
        this.columnaNombre = columnaNombre;
    }

    public TableColumn<Persona, String> getColumnaDireccion() {
        return columnaDireccion;
    }

    public void setColumnaDireccion(TableColumn<Persona, String> columnaDireccion) {
        this.columnaDireccion = columnaDireccion;
    }

    public ObservableList<Persona> getListaData() {
        return listaData;
    }

    public void setListaData(ObservableList<Persona> listaData) {
        this.listaData = listaData;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        columnaNombre.setCellValueFactory( new PropertyValueFactory<Persona,String>("nombreCompleto"));
        columnaDireccion.setCellValueFactory( new PropertyValueFactory<Persona,String>("direccion"));
                
        //Buscar clientes
        buBuscar.setOnAction(event -> {
            String ls_cliente;
            listaData.clear();
            ls_cliente=tfNombre.getText();
            Session session=HibernateUtil.getSessionFactory().openSession();            
            List<Persona> listaPersonas=buscarPersonas(ls_cliente);
            for (Persona persona: listaPersonas) {
                session.beginTransaction();
                Persona p =(Persona) session.get(Persona.class,persona.getId());
                Hibernate.initialize(p.getPersonaDireccions());
                session.getTransaction().commit();
                listaData.add(p);
            }
            dataClientes.setItems(listaData);
        });   
        
        //Seleccionar al cliente
        buSeleccionar.setOnAction(event -> {
            Stage stageActual = null;
            stageActual= (Stage) buSeleccionar.getScene().getWindow();        
            stageActual.close();
        });        
        
        //Ingresar cliente nuevo
        buNuevo.setOnAction(event -> {
            ingresarCliente("/vistas/EditaCliente.fxml");
        });  
        
        //Editar cliente 
        buEditar.setOnAction(event -> {
            Persona personaSel= (Persona) dataClientes.getSelectionModel().getSelectedItem();
           if (personaSel !=null){
              editarCliente("/vistas/EditaCliente.fxml",personaSel);
           }
            
        }); 
        
        //Botón salir de la ventana
        buSalir.setOnAction(event -> {
            Stage stageActual = null;
            stageActual= (Stage) buSalir.getScene().getWindow();        
            stageActual.close();
        });
        
        //Doble click para seleccionar un cliente    
        dataClientes.setRowFactory( tv -> {
        TableRow<Persona> row = new TableRow<>();
        row.setOnMouseClicked(event -> {
        if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
            Persona persona = row.getItem();
            System.out.println(persona);
            
            Stage stageActual = null;
            stageActual= (Stage) buSalir.getScene().getWindow();        
            stageActual.close();
        }
        });
        return row ;
        });
        
        
    }
    
    //Obtiene lista de personas de la base de datos
    public List<Persona> buscarPersonas(String cliente){
        if (cliente==null)
            cliente="";
        cliente="%".concat(cliente).concat("%");
        List<Persona> resultList=null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            Query q=session.createQuery("from Persona a where a.apellido like :apellidoSel order by apellido");
            q.setParameter("apellidoSel",cliente);
            resultList = q.list();
            session.getTransaction().commit();
        } catch (HibernateException he) {
            session.getTransaction().rollback();
            he.printStackTrace();
            return null;
        }
        finally {
            session.close();
        }
        return resultList;
    }
    
    public void ingresarCliente(String fxml) {
        //Cargo nuevo stage
        FXMLLoader firstPaneLoader = new FXMLLoader(getClass().getResource(fxml));
        
        Parent newPane = null;
        try {
            newPane = firstPaneLoader.load();
            //Controlador de la nueva ventana
            ClienteDetalleController controller = (ClienteDetalleController) firstPaneLoader.getController();
            controller.nuevoRegistro();
            
            Scene newScene = new Scene(newPane,650,500);
            Stage nuevoStage = new Stage();
            nuevoStage.setScene(newScene);
            nuevoStage.setTitle("Ingresar cliente");
            nuevoStage.initModality(Modality.APPLICATION_MODAL);
            nuevoStage.setResizable(false);
            nuevoStage.showAndWait();
            
            if(controller.isWb_mod()){
                //Muestro la persona cargada
                Persona personaSel= (Persona) controller.getRegistroSel();
                listaData.clear();
                listaData.add(personaSel);
                dataClientes.setItems(listaData);
                dataClientes.getSelectionModel().select(personaSel);
            }
               
            
        } catch (IOException ex) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setTitle(PuntoVenta.getTituloApp());
            alert.setContentText("No se pudo cargar la ventana: " + ex.getMessage());
            alert.show();
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            return;
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setTitle(PuntoVenta.getTituloApp());
            alert.setContentText("No se pudo cargar la ventana: " + ex.getMessage());
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            alert.show();
            return;
        }
       
    }
    
    public void editarCliente(String fxml,Persona clientSel) {
        //Cargo nuevo stage
        FXMLLoader firstPaneLoader = new FXMLLoader(getClass().getResource(fxml));
        
        Parent newPane = null;
        try {
            newPane = firstPaneLoader.load();
            //Controlador de la nueva ventana
            ClienteDetalleController controller = (ClienteDetalleController) firstPaneLoader.getController();
            controller.setRegistroSel(clientSel);
            controller.editaRegistro();
            
            Scene newScene = new Scene(newPane,650,500);
            Stage nuevoStage = new Stage();
            nuevoStage.setScene(newScene);
            nuevoStage.setTitle("Editar cliente");
            nuevoStage.initModality(Modality.APPLICATION_MODAL);
            nuevoStage.setResizable(false);
            nuevoStage.showAndWait();
            
            if(controller.isWb_mod()){
                //Muestro la persona cargada
                Persona personaSel= (Persona) controller.getRegistroSel();
                listaData.clear();
                listaData.add(personaSel);
                dataClientes.setItems(listaData);
                dataClientes.getSelectionModel().select(personaSel);
            }
               
            
        } catch (IOException ex) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setTitle(PuntoVenta.getTituloApp());
            alert.setContentText("No se pudo cargar la ventana: " + ex.getMessage());
            alert.show();
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            return;
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setTitle(PuntoVenta.getTituloApp());
            alert.setContentText("No se pudo cargar la ventana: " + ex.getMessage());
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            alert.show();
            return;
        }
       
    }
}
