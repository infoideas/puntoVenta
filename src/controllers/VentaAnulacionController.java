/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controllers;
import entidades.AreaNegocio;
import entidades.Empresa;
import entidades.FormaPago;
import entidades.Venta;
import entidades.VentaDet;
import entidades.Persona;
import entidades.Producto;
import entidades.Rubro;
import entidades.Venta;
import entidades.VentaDet;
import epson.ImpresionEpson;
import general.BeanBase;
import general.Conector;
import general.HibernateUtil;
import general.Impresora;
import general.ItemTipo;
import general.ItemTipoString;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Observable;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import pdv.PuntoVenta;

/**
 *
 * @author rafael
 */

public class VentaAnulacionController extends VBox implements Initializable {
    @FXML
    VBox box;
    @FXML
    Label nroVenta;
    @FXML
    Label usuario;
    @FXML
    Label valorTotal;
    @FXML
    Button buGrabarVenta;
    @FXML
    Button buSalir;
    @FXML
    TitledPane paneRubros;
    @FXML
    Label labelRubroSel;    
    @FXML
    Button buBuscarCliente;  
    @FXML
    Button buImprimir;  
    @FXML
    Label nombreCliente;  
    @FXML
    TextField direccionCliente;    
    @FXML
    Label condIvaCliente;   
    @FXML
    Label numCompro;   
    @FXML
    ComboBox cbTipoCompro;   
    @FXML
    TableView<VentaDet> dataVenta;
    @FXML
    private TableColumn<VentaDet,String> columnaCantidad;
    @FXML
    private TableColumn<VentaDet,String> columnaProducto;
    @FXML
    private TableColumn<VentaDet,BigDecimal> columnaPrecioUnitario;
    @FXML
    private TableColumn<VentaDet,BigDecimal> columnaPrecioTotal;
    
    private boolean wb_mod=false;
    private Venta registroSel= new Venta();
    private Venta ventaAnulada;
    private int idRubroSel=0;
    private int idAreaSel=0;
    private boolean wb_nuevo;
    private Persona personaSel;
    private boolean wbVentaRapida;
    private final char ESTADO_FACTURADO='F';
    private final char ESTADO_ANULADO='A';
    
    private ObservableList<VentaDet> detalleVenta = FXCollections.observableArrayList();
    private ObservableList<Producto> listaProductosBuscados = FXCollections.observableArrayList();
    
    public Venta getRegistroSel() {
        return registroSel;
    }

    public void setRegistroSel(Venta registroSel) {
        this.registroSel = registroSel;
    }

    public ObservableList<VentaDet> getDetalleVenta() {
        return detalleVenta;
    }

    public void setDetalleVenta(ObservableList<VentaDet> detalleVenta) {
        this.detalleVenta = detalleVenta;
    }

    public int getIdRubroSel() {
        return idRubroSel;
    }

    public void setIdRubroSel(int idRubroSel) {
        this.idRubroSel = idRubroSel;
    }

    public Venta getVentaAnulada() {
        return ventaAnulada;
    }

    public void setVentaAnulada(Venta ventaAnulada) {
        this.ventaAnulada = ventaAnulada;
    }

    public Persona getPersonaSel() {
        return personaSel;
    }

    public void setPersonaSel(Persona personaSel) {
        this.personaSel = personaSel;
    }

    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<ItemTipo> listaTiposCompro = FXCollections.observableArrayList();
        ItemTipo item= new ItemTipo('V',"Venta");
        listaTiposCompro.add(item);
        item= new ItemTipo('F',"Factura");
        listaTiposCompro.add(item);
        cbTipoCompro.setItems(listaTiposCompro);
        
        columnaProducto.setCellValueFactory( new PropertyValueFactory<VentaDet,String>("producto"));
        dataVenta.setEditable(true);
    
        columnaCantidad.setCellValueFactory( new PropertyValueFactory<VentaDet,String>("cantidad"));
        columnaPrecioUnitario.setCellValueFactory( new PropertyValueFactory<VentaDet,BigDecimal>("precioUnitario"));
        columnaPrecioUnitario.setCellFactory(column -> {
        TableCell<VentaDet,BigDecimal> cell = new TableCell<VentaDet, BigDecimal>() {
            DecimalFormat df = new DecimalFormat( "#,##0.##", new DecimalFormatSymbols(new Locale("es", "AR")));    
            @Override
            protected void updateItem(BigDecimal item, boolean empty) {
                super.updateItem(item, empty);
                if(empty) {
                    setText(null);
                }
                else {
                    if (item!=null)
                        this.setText(df.format(item));
                }
            }
        };
        return cell;
        });
        columnaPrecioTotal.setCellValueFactory( new PropertyValueFactory<VentaDet,BigDecimal>("precioTotal"));
        columnaPrecioTotal.setCellFactory(column -> {
        TableCell<VentaDet,BigDecimal> cell = new TableCell<VentaDet, BigDecimal>() {
            DecimalFormat df = new DecimalFormat( "#,##0.##", new DecimalFormatSymbols(new Locale("es", "AR")));    
            @Override
            protected void updateItem(BigDecimal item, boolean empty) {
                super.updateItem(item, empty);
                if(empty) {
                    setText(null);
                }
                else {
                    if (item!=null)
                        this.setText(df.format(item));
                }
            }
        };
        return cell;
        });
        
        columnaCantidad.setStyle( "-fx-alignment: CENTER-RIGHT;");
        columnaPrecioUnitario.setStyle("-fx-alignment: CENTER-RIGHT;");
        columnaPrecioTotal.setStyle( "-fx-alignment: CENTER-RIGHT;");
        
        //Buscar cliente
        buBuscarCliente.setOnAction(event -> {
           buscarCliente("/vistas/BuscarCliente.fxml");
        });
        
        //Grabar venta
        buGrabarVenta.setOnAction(event -> {
           grabarVenta();
        });
        
        //Botón salir de la ventana
        buSalir.setOnAction(event -> {
            Stage stage= (Stage) buSalir.getScene().getWindow();
            stage.close();
        });
        
        // Poner focus en el nombre del producto
        //Platform.runLater(() -> nombreProducto.requestFocus());
    }
    
    public double calculaTotal(){
        double ld_total=0.00;
        for (VentaDet m: detalleVenta) {
            ld_total=ld_total + m.getPrecioTotal().doubleValue();
        }
        return ld_total;
    }

    //Nueva anulación
    public void nuevaAnulacion(){
        wb_nuevo=true;
        usuario.setText(PuntoVenta.getUsuarioConectado().getNombreCompletoUsuario());
        buGrabarVenta.setDisable(false); 
        buBuscarCliente.setDisable(false);
        cbTipoCompro.setDisable(false);
        buImprimir.setDisable(true);
        wb_mod=false;  
        
      
        //Pongo los datos del cliente
        registroSel.setPersona(personaSel);
        nombreCliente.setText((registroSel.getPersona() != null ? registroSel.getPersona().getNombreCompleto() : ""));
        condIvaCliente.setText( (registroSel.getPersona() != null ? ( registroSel.getPersona().getCondIva() != null ? registroSel.getPersona().getCondIva().getNombre() : "Consumidor final" )   : "Consumidor final"));
        direccionCliente.setText((registroSel.getPersona() != null ? registroSel.getPersona().getDireccion() : ""));
        
        ObservableList<ItemTipo> listaTiposCompro = FXCollections.observableArrayList();
        ItemTipo item= new ItemTipo('C',"Nota de Crédito");
        listaTiposCompro.add(item);
        cbTipoCompro.setItems(listaTiposCompro);
        
        //Tipo de comprobante por default Nota de Crédito
        cbTipoCompro.setValue(item);
        registroSel.setLocalCarniceria(PuntoVenta.getLocalSel());
        registroSel.setIdUsuario(PuntoVenta.getUsuarioConectado().getId());
        registroSel.setValorTotal(BigDecimal.ZERO);
        registroSel.setPorcIva(new BigDecimal(21));
        registroSel.setValorIva(BigDecimal.ZERO);
        registroSel.setValorSiva(BigDecimal.ZERO);
        registroSel.setPorcDesc(BigDecimal.ZERO);
        registroSel.setValorDesc(BigDecimal.ZERO);
        registroSel.setValorFinal(BigDecimal.ZERO);  
        registroSel.setEstado(ESTADO_ANULADO);
        numCompro.setText("");
        
        Iterator i= ventaAnulada.getVentaDets().iterator();
        while (i.hasNext()){
            VentaDet p= (VentaDet) i.next();

            VentaDet q= new VentaDet();
            q.setVenta(registroSel);
            q.setProducto(p.getProducto());
            q.setUnidad(p.getUnidad());
            q.setCantidad(new BigDecimal(p.getCantidad().intValue()*(-1)));
            q.setPrecioUnitario(new BigDecimal(p.getPrecioUnitario().intValue()*(-1)));
            q.setPrecioTotal(new BigDecimal(p.getPrecioTotal().intValue()*(-1)));
            q.setValorAdicional(new BigDecimal(p.getValorAdicional().intValue()*(-1)));
            q.setEstado(ESTADO_ANULADO);
            registroSel.getVentaDets().add(q);
            detalleVenta.add(q);
            
        }
        dataVenta.setItems(detalleVenta);     
        
        //Actualizo los totales
        double ld_total=0.00;
        ld_total=calculaTotal();
        DecimalFormat df = new DecimalFormat( "#,##0.##", new DecimalFormatSymbols(new Locale("es", "AR")));    
        valorTotal.setText(df.format(ld_total));
        
        registroSel.setValorTotal(new BigDecimal(ld_total));
        registroSel.setValorDesc(BigDecimal.ZERO);
        registroSel.setValorFinal(new BigDecimal(ld_total));
        
        // Poner focus en el nombre del producto
        Platform.runLater(() -> buGrabarVenta.requestFocus());
    }
    
    public void editaVenta(){
        
        wb_nuevo=false;
        buBuscarCliente.setDisable(true);
        cbTipoCompro.setDisable(true);
        personaSel=registroSel.getPersona();
        buImprimir.setDisable(false);
        nroVenta.setText(String.valueOf(registroSel.getId().intValue()));
        
        char lc_tipo_compro;
        lc_tipo_compro=registroSel.getTipoComprobante();
        String ls_tipo_compro = null;
        switch(lc_tipo_compro)
        {
            case 'V' :
                ls_tipo_compro="Venta";
                break;  
            case 'F' :
                ls_tipo_compro="Factura";
                break;    
        }
        ItemTipo item= new ItemTipo(registroSel.getTipoComprobante(),ls_tipo_compro);
        cbTipoCompro.setValue(item);
        
        if (lc_tipo_compro=='F'){
            numCompro.setText(registroSel.getPuntoVenta() + "-"  + registroSel.getNumFactura() + "-" + registroSel.getTipoFactura());
        }
        
        nroVenta.setText(String.valueOf(registroSel.getId().intValue()));
        BeanBase bean= new BeanBase();
        usuario.setText(bean.obtenerUsuario(registroSel.getIdUsuario()).getNombreCompletoUsuario());
        nombreCliente.setText((registroSel.getPersona() != null ? registroSel.getPersona().getNombreCompleto() : ""));
        condIvaCliente.setText( (registroSel.getPersona() != null ? ( registroSel.getPersona().getCondIva() != null ? registroSel.getPersona().getCondIva().getNombre() : "Consumidor final" )   : "Consumidor final"));
        direccionCliente.setText((registroSel.getPersona() != null ? registroSel.getPersona().getDireccion() : ""));
        
        Iterator i= registroSel.getVentaDets().iterator();
        while (i.hasNext()){
            VentaDet p= (VentaDet) i.next();
                
            //Inicializo cada detalle de mesa
            Session session=HibernateUtil.getSessionFactory().openSession();
            try{
                session.beginTransaction();
                p =(VentaDet) session.get(VentaDet.class,p.getId());
                session.getTransaction().commit();
            }
            catch (HibernateException e){
                session.getTransaction().rollback();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setTitle("Error");
                alert.setContentText(e.getMessage());   
                alert.show();
                return;
            }
            finally {
                session.close();
            }

            p.setCantidad(new BigDecimal(p.getCantidad().intValue()*(-1)));
            p.setPrecioUnitario(new BigDecimal(p.getPrecioUnitario().intValue()*(-1)));
            p.setPrecioTotal(new BigDecimal(p.getPrecioTotal().intValue()*(-1)));
            p.setValorAdicional(new BigDecimal(p.getValorAdicional().intValue()*(-1)));
            detalleVenta.add(p);
            
        }
        
        //Ordeno la lista por hora de venta
        //Collections.sort(detalleVenta,Comparator.comparing((VentaDet p)->p.getHoraVenta()).thenComparing(p->p.getProducto().getNombre()));
        
        dataVenta.setItems(detalleVenta);
        
        DecimalFormat df = new DecimalFormat( "#,##0.##", new DecimalFormatSymbols(new Locale("es", "AR")));
        valorTotal.setText(df.format(registroSel.getValorFinal().doubleValue()));
        
        buGrabarVenta.setDisable(true);
        
    }
    
    public String getEstadodelaVenta(char estado){
        String ls_estado=null;
        switch (estado){
            case 'A':
                ls_estado="Abierta";
                break;
            case 'C':
                ls_estado="Cerrada";
                break;
            case 'F':
                ls_estado="Facturada";
                break;              
        }
        return ls_estado;
    }
    
    //Obtiene lista de rubros de la base de datos
    public List<Rubro> buscarRubros(AreaNegocio areaNegocio){
        BeanBase beanBase= new BeanBase();
        Empresa empresaSel=beanBase.obtenerEmpresa(Integer.valueOf(beanBase.getConfiguracion().getProperty("idEmpresa").trim())); 
        List<Rubro> resultList=null;
        //Busco todas las mesas
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            Query q=session.createQuery("from Rubro a "
                    + "where areaNegocio = :areaNegocio order by nombre");
            q.setParameter("areaNegocio",areaNegocio);
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
    
    //Obtiene lista de áreas de negocio de la base de datos
    public List<AreaNegocio> buscarAreas(){
        BeanBase beanBase= new BeanBase();
        Empresa empresaSel=beanBase.obtenerEmpresa(Integer.valueOf(beanBase.getConfiguracion().getProperty("idEmpresa").trim())); 
        List<AreaNegocio> resultList=null;
        //Busco todas las mesas
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            Query q=session.createQuery("from AreaNegocio a order by nombre");
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
    
    //Imprimo el detalle de la mesa
    public void imprimirDetalle(){
        DecimalFormat df = new DecimalFormat( "#,##0.##", new DecimalFormatSymbols(new Locale("es", "AR")));
        Impresora impresora;
        impresora = new Impresora();
              
        //Imprimo encabezado
        impresora.imprimirLinea(PuntoVenta.getEmpresaSel().getNombreFantasia(),10);
        impresora.imprimirLinea(PuntoVenta.getEmpresaSel().getCuit(),20);
        impresora.imprimirLinea(PuntoVenta.getEmpresaSel().getDireccion(),30);
        impresora.imprimirLinea("Venta: " + registroSel.getId().intValue(),40);
        int li_fila=70;
        detalleVenta=dataVenta.getItems();
        for (VentaDet d: detalleVenta) {
            impresora.imprimirLinea(d.getProducto().getNombre(),li_fila);
            li_fila=li_fila + 10;
            String ls_detalle_precio;
            ls_detalle_precio=df.format(d.getCantidad().doubleValue()) + "x" + df.format(d.getPrecioUnitario().doubleValue()) ;
            impresora.imprimirLinea(ls_detalle_precio,li_fila);
            li_fila=li_fila + 10;
        } 
        double ld_total=0.00;
        ld_total=calculaTotal();
        impresora.imprimirLinea("Total:" + df.format(ld_total),li_fila);
        li_fila=li_fila + 10;
        impresora.imprimirLinea("----------------------------",li_fila);
        impresora.cerrarImpresion();
    }
    
    
    public void buscarCliente(String fxml) {
        //Cargo nuevo stage
        FXMLLoader firstPaneLoader = new FXMLLoader(getClass().getResource(fxml));
        
        Parent newPane = null;
        try {
            newPane = firstPaneLoader.load();
            //Controlador de la nueva ventana
            BuscarClienteController controller = (BuscarClienteController) firstPaneLoader.getController();
            
            Scene newScene = new Scene(newPane,800,600);
            Stage nuevoStage = new Stage();
            nuevoStage.setScene(newScene);
            nuevoStage.setTitle("Buscar cliente");
            nuevoStage.initModality(Modality.APPLICATION_MODAL);
            nuevoStage.setResizable(false);
            nuevoStage.showAndWait();
            
            //Agrego las preferencias seleccionadas
            personaSel= (Persona) controller.dataClientes.getSelectionModel().getSelectedItem();            
            
            if(personaSel !=null){
                registroSel.setPersona(personaSel);
                nombreCliente.setText(personaSel.getNombreCompleto());
                direccionCliente.setText(personaSel.getDireccion());
                condIvaCliente.setText((personaSel.getCondIva() != null ? personaSel.getCondIva().getNombre() : "Consumidor Final"));
                wb_mod=true;
            }
            
            
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
    
    public AreaNegocio obtenerAreaNegocio(int id){
        AreaNegocio p= null;
        Session session=HibernateUtil.getSessionFactory().openSession();
        try{
            session.beginTransaction();
            p=(AreaNegocio) session.get(AreaNegocio.class,id);
            session.getTransaction().commit();
        }
        catch (HibernateException e){
            session.getTransaction().rollback();
            return null;
       }
        finally {
            session.close();
            return p;
        }
       
    }
    
    
    //Pide la cantidad de productos para agregar a la mesa
    public double pedirCantidad(int cantidadInicial,String nombreProducto){
        double li_cantidad = 0;
        Dialog<CantidadProducto> dialog = new Dialog<>();
        dialog.setTitle("Cantidad");
        dialog.setHeaderText(nombreProducto);
         
        Label label1 = new Label("Cantidad: ");
        TextField txtCantidad = new TextField(String.valueOf(cantidadInicial));
         
        GridPane grid = new GridPane();
        grid.add(label1, 1, 1);
        grid.add(txtCantidad, 2, 1);
        dialog.getDialogPane().setContent(grid);
    
        // Poner focus en cantidad al inicio
        Platform.runLater(() -> txtCantidad.requestFocus());

        txtCantidad.textProperty().addListener(new ChangeListener<String>() {
        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue, 
        String newValue) {
            //if (!newValue.matches("\\d{5}[.]\\d{2}")) {    
            if (!newValue.matches("[0-9]{1,8}[.][0-9]{1,2}")) {        
                txtCantidad.setText(newValue.replaceAll("[0-9]{1,8}[.][0-9]{1,2}",""));
            }
        }
        });
         
        ButtonType buttonTypeOk = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonTypeCancel = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeCancel);
        
        dialog.setResultConverter(new Callback<ButtonType, CantidadProducto>() {
            @Override
            public CantidadProducto call(ButtonType b) {
                if (b == buttonTypeOk) {
                    double li_cantidad=0;
                    try{
                        li_cantidad=(txtCantidad.getText().isEmpty() || txtCantidad.getText()==null ?  0 : Double.parseDouble(txtCantidad.getText()));
                        }
                    catch( NumberFormatException e){
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle(PuntoVenta.getTituloApp());
                        alert.setContentText("Cantidad incorrecta!");
                        alert.show();   
                        return null;
                    }
                    return new CantidadProducto(li_cantidad);
                }
               return null;
            }
        });
         
        Optional<CantidadProducto> result = dialog.showAndWait();
        if (result.isPresent()) {
            CantidadProducto cantidad = result.get();
            li_cantidad=cantidad.getCantidad();
        }
        else
            li_cantidad=0;
        
        return li_cantidad;
    }
    
    //Pide la cantidad de productos para agregar a la mesa
    public CantidadProducto pedirPrecioTotal(int cantidadInicial,String nombreProducto){
        double li_cantidad = 0;
        Dialog<CantidadProducto> dialog = new Dialog<>();
        dialog.setTitle("Cantidad");
        dialog.setHeaderText(nombreProducto);
         
        Label label1 = new Label("Cantidad: ");
        TextField txtCantidad = new TextField(String.valueOf(cantidadInicial));

        Label label2 = new Label("Precio: ");
        TextField txtPrecio = new TextField("0");
        
        GridPane grid = new GridPane();
        grid.add(label1, 1, 1);
        grid.add(txtCantidad, 2, 1);
        grid.add(label2, 1, 2);
        grid.add(txtPrecio, 2, 2);
        dialog.getDialogPane().setContent(grid);
    
        // Poner focus en cantidad al inicio
        Platform.runLater(() -> txtPrecio.requestFocus());

        txtCantidad.textProperty().addListener(new ChangeListener<String>() {
        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue, 
        String newValue) {
            //if (!newValue.matches("\\d{5}[.]\\d{2}")) {    
            if (!newValue.matches("[0-9]{1,8}[.][0-9]{1,2}")) {        
                txtCantidad.setText(newValue.replaceAll("[0-9]{1,8}[.][0-9]{1,2}",""));
            }
        }
        });
         
        txtPrecio.textProperty().addListener(new ChangeListener<String>() {
        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue, 
        String newValue) {
            //if (!newValue.matches("\\d{5}[.]\\d{2}")) {    
            if (!newValue.matches("[0-9]{1,8}[.][0-9]{1,2}")) {        
                txtPrecio.setText(newValue.replaceAll("[0-9]{1,8}[.][0-9]{1,2}",""));
            }
        }
        });        
        
        ButtonType buttonTypeOk = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonTypeCancel = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeCancel);
        
        dialog.setResultConverter(new Callback<ButtonType, CantidadProducto>() {
            @Override
            public CantidadProducto call(ButtonType b) {
                if (b == buttonTypeOk) {
                    double li_cantidad=0;
                    double ld_precio=0;
                    try{
                        li_cantidad=(txtCantidad.getText().isEmpty() || txtCantidad.getText()==null ?  0 : Double.parseDouble(txtCantidad.getText()));
                        ld_precio=(txtPrecio.getText().isEmpty() || txtPrecio.getText()==null ?  0 : Double.parseDouble(txtPrecio.getText()));                        
                        }
                    catch( NumberFormatException e){
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle(PuntoVenta.getTituloApp());
                        alert.setContentText("Cantidad incorrecta!");
                        alert.show();   
                        return null;
                    }
                    return new CantidadProducto(li_cantidad,ld_precio);
                }
               return null;
            }
        });
         
        CantidadProducto cantidad=null; 
        Optional<CantidadProducto> result = dialog.showAndWait();
        if (result.isPresent()) {
            cantidad = result.get();
            li_cantidad=cantidad.getCantidad();
        }
        else
            li_cantidad=0;
        
        return cantidad;
    }
    
    //Factura el pedido
    public void grabarVenta(){
        
        //Fecha y hora de carga actual
        TimeZone gmtZone = TimeZone.getTimeZone("America/Buenos_Aires");
        DateFormat destDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        destDateFormat.setTimeZone(gmtZone);
        Date lda_fec_carga = new Date();
        
        ItemTipo tipoComprobante=(ItemTipo) cbTipoCompro.getValue();
        if (tipoComprobante==null){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Módulo de Terminal");
            alert.setContentText("Debe seleccionar el tipo de comprobante");
            alert.show();          
            cbTipoCompro.requestFocus();
            return;
        }
        char lc_tipo_compro=tipoComprobante.getTipo();   
        
        //Ordeno la lista por hora de pedido
        //Collections.sort(detalleVenta,Comparator.comparing((VentaDet p)->p.getHoraVenta()).thenComparing(p->p.getProducto().getNombre()));
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmación");
        alert.setContentText("¿Está seguro de grabar la venta?");
        Optional<ButtonType> action = alert.showAndWait();
        if (action.get() == ButtonType.CANCEL) return;
        
        String ls_mensaje="Actualización exitosa!";
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();        
        if (wb_nuevo)
        {
            //Venta nueva
            registroSel.setFecCarga(lda_fec_carga);
            registroSel.setFecMov(lda_fec_carga);
            registroSel.setTipoComprobante(lc_tipo_compro);
            registroSel.setCondIva("CF");
            registroSel.setPagado('0');
        }
        registroSel.setPersona(personaSel);
        
        //Agrego los productos a la comanda
        detalleVenta=dataVenta.getItems();
        registroSel.getVentaDets().clear();
        
        //Guardo el detalle de la mesa
        detalleVenta=dataVenta.getItems();
        registroSel.getVentaDets().clear();
        for (VentaDet d: detalleVenta) {
            registroSel.getVentaDets().add(d);
        }
                
        //Actualizo los totales
        double ld_total=calculaTotal();
        registroSel.setValorTotal(new BigDecimal(ld_total));
        registroSel.setValorDesc(BigDecimal.ZERO);
        registroSel.setValorFinal(new BigDecimal(ld_total));
        
        //////////////////////
        //Facturo la venta
        //////////////////////
        String lsNombreComprador="";
        String lsDirComprador="";
        int liIvaComprador=1;
        int liTipIdComprador=1;
        String lsNumIdComprador="";
        String lsLineaCompOrigen=" ";
        String lsTipDoc = "902";
        String lsDocAsocL1 = "";
        String lsDocAsocL2 = "";
        String lsDocAsocL3 = "";
        
       
        //Si existe un cliente seleccionado
        if (registroSel.getPersona() != null){
            Persona p=registroSel.getPersona();
            //Inicializo la persona
            try{
                p =(Persona) session.get(Persona.class,p.getId());
                Hibernate.initialize(p.getPersonaDireccions());
            }
            catch (HibernateException e){
                session.getTransaction().rollback();
                session.close();
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setTitle("Error");
                alert.setContentText(e.getMessage());   
                alert.show();
                return;
            }
                        
            lsNombreComprador=p.getNombreCompleto();
            lsDirComprador=p.getDireccion();  //Agregar dirección
            if (lsDirComprador==null) lsDirComprador="";
            //Tipo de Id del comprador D: DNI, T : CUIT
            liTipIdComprador = p.getTipoid().getId().intValue();
            lsNumIdComprador = p.getNumIdentificacion();
            //Cond. Iva Comprador F: Consumidor final, I: Resp. Inscripto
            if (p.getCondIva()==null)
                liIvaComprador=1; //Consumidor final
            else
                liIvaComprador = p.getCondIva().getId().intValue();
        }
        else{
            //Consumidor final
            liIvaComprador=1;
            liTipIdComprador=1;
            lsNombreComprador=" ";
            lsDirComprador=" ";
        }
        
        lsDocAsocL1 = "";
        lsDocAsocL2 = "";
        lsDocAsocL3 = "";
        
        //Tipo de factura que se va a anular
        String ls_cod_compro = null;
        switch (ventaAnulada.getTipoFactura()) {
            case "A":
                ls_cod_compro="081";
            case "B":
                ls_cod_compro="082";
        }
        //Datos del comprobante a anular
        lsLineaCompOrigen=ls_cod_compro + "-" + ventaAnulada.getPuntoVenta() + "-" + ventaAnulada.getNumFactura();
        
        List<VentaDet> listaItems= new ArrayList<VentaDet>();
        for (VentaDet d: detalleVenta){
            VentaDet ventaDet= new VentaDet();
            ventaDet.setVenta(registroSel);
            ventaDet.setProducto(d.getProducto());
            ventaDet.setCantidad(new BigDecimal(d.getCantidad().intValue()*(-1)));
            ventaDet.setUnidad(d.getUnidad());
            ventaDet.setPrecioUnitario(new BigDecimal( (d.getPrecioUnitario().doubleValue() + (d.getValorAdicional()==null ? 0 : d.getValorAdicional().doubleValue()))*(-1)));
            ventaDet.setPrecioTotal(new BigDecimal(d.getPrecioTotal().doubleValue()*(-1)));
            listaItems.add(ventaDet);
        }
        
        //Nota de crédito
        if (lc_tipo_compro=='C'){
            //Imprimo el ticket fiscal de la Nota de crédito
            ImpresionEpson impresion = new ImpresionEpson();
            //Configuro el puerto COM del controlador fiscal
            impresion.setPuerto(PuntoVenta.getPuertoCF());
            impresion.inicializaControlador();
            impresion.abrirPuerto();
            boolean lb_ticket=impresion.imprimirTicketNotaCredito('C',lsNombreComprador,lsDirComprador,
                             liTipIdComprador, lsNumIdComprador, liIvaComprador, lsDocAsocL1, lsDocAsocL2, lsDocAsocL3,lsLineaCompOrigen,listaItems);
            if (lb_ticket && !impresion.getNumComprobante().isEmpty()){
                //Pongo los datos del ticket en la factura
                registroSel.setPuntoVenta(String.format("%04d",Integer.parseInt(PuntoVenta.getPuntoVentaCF())));
                registroSel.setNumFactura(String.format("%08d",Integer.parseInt(impresion.getNumComprobante())));
                registroSel.setTipoFactura(impresion.getTipoComprobante());  //Tipo de factura
                impresion.cerrarPuerto();
            }
            else
            {
                impresion.cerrarPuerto();
                session.getTransaction().rollback();
                session.close();
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setTitle("Error");
                alert.setContentText("Error al realizar ticket factura");   
                alert.show();
                return ;
            }
            
        }
        
        //Cambio el estado de la venta anulada
        ventaAnulada.setEstado(ESTADO_ANULADO);
        try{
            session.saveOrUpdate(ventaAnulada);
        }
        catch (HibernateException e){
            session.getTransaction().rollback();
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());   
            alert.show();
            return ;
        }
        
        //Grabo la anulación de la venta
        try{
            session.saveOrUpdate(registroSel);
            session.getTransaction().commit();
        }
        catch (HibernateException e){
            session.getTransaction().rollback();
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());   
            alert.show();
            return ;
        }
        finally {
            session.close();
        }
        
        if (wb_nuevo ) wb_nuevo=false;   
        //Nro de venta
        nroVenta.setText(registroSel.getId().toString());
        if (lc_tipo_compro=='F'){
            numCompro.setText(registroSel.getPuntoVenta() + "-"  + registroSel.getNumFactura() + "-" + registroSel.getTipoFactura() );
        }
        buGrabarVenta.setDisable(true); 
        buBuscarCliente.setDisable(true);
        cbTipoCompro.setDisable(true);
        buImprimir.setDisable(false);
        wb_mod=false;  
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle("Información");
        alert.setContentText("Venta grabado exitosamente!");
        alert.showAndWait();     
        
    }
    
    //Obtiene lista de formas de pago
    public List<FormaPago> buscarListaFormas(){
        List<FormaPago> resultList=null;

        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            Query q=session.createQuery("from FormaPago a order by nombre");
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
    
    //Obtiene precio de acuerdo al local y la fecha
    public double obtienePrecio(int idLocal,int idProducto,Date fecha) throws SQLException{
        CallableStatement s=null;
        ResultSet r=null;
        double ld_precio=0;
        
        //Conectamos a la base de datos
        Conector conector = new Conector();  
        Connection conexion = conector.connect("estancia");
        
        try {      
             s=conexion.prepareCall("{call sp_get_precio_producto_local ( ? , ? , ? , ? )}"); 
             s.setInt(1,idLocal); 
             s.setInt(2,idProducto); 
             s.setTimestamp(3,new java.sql.Timestamp(fecha.getTime()));
             s.registerOutParameter(4,java.sql.Types.DECIMAL);
             r=s.executeQuery();

             //Obtengo el id y la fecha de fin del último balance
             ld_precio=s.getDouble(4);             

        }catch (SQLException e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(PuntoVenta.getTituloApp());
            alert.setContentText("Error al obtener precio del producto");
            alert.show();          
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(PuntoVenta.getTituloApp());
            alert.setContentText("Error al obtener precio del producto");
            alert.show();          
        }finally {
            if (r != null) {r.close();}
            if (s != null) {s.close();}
            if (conexion != null) conexion.close(); 
        }
        return ld_precio;
    }
    
    
}
