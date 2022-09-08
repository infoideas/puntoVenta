/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import entidades.Venta;
import general.Conector;
import general.HibernateUtil;
import java.io.IOException;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import java.time.Instant;
import java.util.Calendar;
import pdv.PuntoVenta;

/**
 *
 * @author rafaelg
 */
public class VentaController extends VBox implements Initializable {
    @FXML
    TableView<Venta> dataGrid;
    @FXML
    private TableColumn<Venta,String> columnaId;
    @FXML
    private TableColumn<Venta,Date> columnaFecha;
    @FXML
    private TableColumn<Venta,String> columnaCliente;
    @FXML
    private TableColumn<Venta,String> columnaValor;
    @FXML
    DatePicker dpDesde;
    @FXML
    DatePicker dpHasta;
    
    private ObservableList<Venta> listaData = FXCollections.observableArrayList();
    
    @FXML
    Button buBuscar,buNuevo,buConsultar,buEliminar,buSalir;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        columnaId.setCellValueFactory( new PropertyValueFactory<Venta,String>("id"));
        columnaCliente.setCellValueFactory( new PropertyValueFactory<Venta,String>("persona"));
        columnaFecha.setCellValueFactory( new PropertyValueFactory<Venta,Date>("fecMov"));
        columnaFecha.setCellFactory(column -> {
        TableCell<Venta,Date> cell = new TableCell<Venta, Date>() {
            private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            @Override
            protected void updateItem(Date item, boolean empty) {
                super.updateItem(item, empty);
                if(empty) {
                    setText(null);
                }
                else {
                    if (item!=null)
                        this.setText(format.format(item));
                }
            }
        };
        return cell;
        });
        columnaValor.setCellValueFactory( new PropertyValueFactory<Venta,String>("valorTotal"));
        columnaValor.setStyle( "-fx-alignment: CENTER-RIGHT;");
               
        //Buscar lista
        buBuscar.setOnAction(event -> {
           cargarLista();
        });
        
        //Nuevo registro
        buNuevo.setOnAction(event -> {
           irNuevoRegistro("/vistas/VentaDetalle.fxml");
        });
        
        //Consular o editar registro
        buConsultar.setOnAction(event -> {
           Venta registroSel= (Venta) dataGrid.getSelectionModel().getSelectedItem();
           if (registroSel !=null)
                editarRegistro("/vistas/VentaDetalle.fxml",registroSel);
        });
        
        //Consular o editar registro
        buEliminar.setOnAction(event -> {
           Venta registroSel= (Venta) dataGrid.getSelectionModel().getSelectedItem();
           if (registroSel !=null){
               Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
               alert.setTitle("Confirmación");
               alert.setContentText("¿Está seguro de eliminar el registro seleccionado?");
               Optional<ButtonType> action = alert.showAndWait();
               if (action.get() == ButtonType.OK) {
                   if ( eliminaRegistro(registroSel)){
                       int li_fila_sel=dataGrid.getSelectionModel().getSelectedIndex();
                       listaData.remove(li_fila_sel);
                       dataGrid.setItems(listaData);
                   }    
               }
           }
        });
        
        //Botón salir de la ventana
        buSalir.setOnAction(event -> {
            Stage stage= (Stage) buSalir.getScene().getWindow();
            stage.close();
        });
        
        //Doble click para consultar un registro   
        dataGrid.setRowFactory( tv -> {
        TableRow<Venta> row = new TableRow<>();
        row.setOnMouseClicked(event -> {
        if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
            Venta registroSel = row.getItem();
            editarRegistro("/vistas/VentaDetalle.fxml",registroSel);
        }
        });
        return row ;
        });

    }
    
    public void cargarLista(){
        listaData.clear();
        List<Venta> listaVentas=buscarLista();
        System.out.println("Lista: " + listaVentas);
        for (Venta pedido: listaVentas) {
            listaData.add(pedido);
        }
        dataGrid.setItems(listaData);
    }
    
    //Obtiene lista de bancos de la base de datos
    public List<Venta> buscarLista(){
        List<Venta> resultList=null;
        
        //Fecha y hora de carga actual
        TimeZone gmtZone = TimeZone.getTimeZone("America/Buenos_Aires");
        DateFormat destDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        destDateFormat.setTimeZone(gmtZone);
        
        //Fecha de inicio
        LocalDate localDate = dpDesde.getValue();
        if (localDate==null){
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Módulo de Ventas");
            alert.setContentText("Debe seleccionar la fecha de inicio");
            alert.show();          
            return null;
        }
        Instant instant = Instant.from(localDate.atStartOfDay(gmtZone.toZoneId()));
        Date lda_fec_desde = Date.from(instant);
        
        java.util.Calendar fecha_desde= java.util.Calendar.getInstance();
        fecha_desde.setTimeZone(TimeZone.getTimeZone("America/Buenos_Aires"));
        fecha_desde.setTime(lda_fec_desde);
        fecha_desde.set(Calendar.HOUR_OF_DAY, 0);
        fecha_desde.set(Calendar.MINUTE, 0);
        fecha_desde.set(Calendar.SECOND, 0);
        fecha_desde.set(Calendar.MILLISECOND, 0);
        
        localDate = dpHasta.getValue();
        if (localDate==null){
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Módulo de Ventas");
            alert.setContentText("Debe seleccionar la fecha de fin");
            alert.show();          
            return null;
        }
        instant = Instant.from(localDate.atStartOfDay(gmtZone.toZoneId()));
        Date lda_fec_hasta = Date.from(instant);
        
        java.util.Calendar fecha_hasta = java.util.Calendar.getInstance();
        fecha_hasta.setTimeZone(TimeZone.getTimeZone("America/Buenos_Aires"));
        fecha_hasta.setTime(lda_fec_hasta);
        fecha_hasta.set(Calendar.HOUR_OF_DAY, 23);
        fecha_hasta.set(Calendar.MINUTE, 59);
        fecha_hasta.set(Calendar.SECOND, 59);
        fecha_hasta.set(Calendar.MILLISECOND, 0);
        
        //Busco todos los bancos
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            Query q=session.createQuery("from Venta a "
                                       + "where fecMov >= :fec_desde " 
                                       + "and fecMov <= :fec_hasta order by fecMov");
            q.setParameter("fec_desde",fecha_desde.getTime());
            q.setParameter("fec_hasta",fecha_hasta.getTime());
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
    
    public void irNuevoRegistro(String fxml) {
        //Cargo nuevo stage
        FXMLLoader firstPaneLoader = new FXMLLoader(getClass().getResource(fxml));
        Parent newPane = null;
        try {
            newPane = firstPaneLoader.load();
            //Controlador de la nueva ventana
            VentaDetalleController controller = (VentaDetalleController) firstPaneLoader.getController();
            controller.setRegistroSel(new Venta());
            controller.nuevaVenta();
            
            Scene newScene = new Scene(newPane, 1024,700);
            Stage nuevoStage = new Stage();
            nuevoStage.setScene(newScene);
            nuevoStage.setTitle("Nueva venta");
            nuevoStage.setResizable(true);
            nuevoStage.show();
            
        
        } catch (IOException ex) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setTitle("Error");
            alert.setContentText("No se pudo cargar la ventana: " + ex.getMessage());
            alert.show();
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            return;
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setTitle("Error");
            alert.setContentText("No se pudo cargar la ventana: " + ex.getMessage());
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            alert.show();
            return;
        }
       
    }
    
    public void editarRegistro(String fxml,Venta movSel) {
        Venta p= null;
        Session session=HibernateUtil.getSessionFactory().openSession();
        try{
            session.beginTransaction();
            p=(Venta) session.get(Venta.class,movSel.getId());
            Hibernate.initialize(p.getVentaDets());
            Hibernate.initialize(p.getPersona().getDireccion());
            session.getTransaction().commit();
        }
        catch (HibernateException e){
            session.getTransaction().rollback();
            return;
       }
        finally {
            session.close();
        }
        
        //Cargo nuevo stage
        FXMLLoader firstPaneLoader = new FXMLLoader(getClass().getResource(fxml));
        Parent newPane = null;
        try {
            newPane = firstPaneLoader.load();
            //Controlador de la nueva ventana
            VentaDetalleController controller = (VentaDetalleController) firstPaneLoader.getController();
            controller.setRegistroSel(p);
            controller.editaVenta();
            
            Scene newScene = new Scene(newPane, 1024,700);
            Stage nuevoStage = new Stage();
            nuevoStage.setScene(newScene);
            nuevoStage.setTitle("Edita venta");
            nuevoStage.setResizable(true);
            nuevoStage.show();
        
        } catch (IOException ex) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setTitle("Error");
            alert.setContentText("No se pudo cargar la ventana: " + ex.getMessage());
            alert.show();
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            return;
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setTitle("Error");
            alert.setContentText("No se pudo cargar la ventana: " + ex.getMessage());
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            alert.show();
            return;
        }
    }
    
    public boolean eliminaRegistro(Object o) {
       Session session = HibernateUtil.getSessionFactory().openSession();
       Venta p=(Venta) o;
       try{
           session.beginTransaction();
           session.delete(p);
           session.getTransaction().commit();
           Alert alert = new Alert(AlertType.INFORMATION);
           alert.setTitle(PuntoVenta.getTituloApp());
           alert.setContentText("Eliminación exitosa!");
           alert.show();
           return true;
       }
       catch (HibernateException e){
            session.getTransaction().rollback();
            return false;
       }
       finally {
            session.close();
       }
    }
    
    
    
}
