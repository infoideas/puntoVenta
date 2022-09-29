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

public class VentaDetalleController extends VBox implements Initializable {
    @FXML
    VBox box;
    @FXML
    Label nroVenta;
    @FXML
    Label usuario;
    @FXML
    TextField nombreProducto;
    @FXML
    Label valorTotal;
    @FXML
    Button buGrabarVenta;
    @FXML
    Button buBuscarProductos;
    @FXML
    Button buEliminar;
    @FXML
    Button buSalir;
    @FXML
    GridPane gridAreas;
    @FXML
    GridPane gridRubros;
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
    
    @FXML
    TableView<Producto> dataProductosBuscados;
    @FXML
    private TableColumn<Producto,String> columnaProductoBuscado;
    @FXML
    private TableColumn<Producto,String> columnaRubroBuscado;
    @FXML
    private TableColumn<Producto,BigDecimal> columnaPrecioBuscado;

    private boolean wb_mod=false;
    private Venta registroSel= new Venta();
    private int idRubroSel=0;
    private int idAreaSel=0;
    private boolean wb_nuevo;
    private Persona personaSel;
    private boolean wbVentaRapida;
    
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Cargo las áreas de negocio
        cargarAreas();
        
        ObservableList<ItemTipo> listaTiposCompro = FXCollections.observableArrayList();
        ItemTipo item= new ItemTipo('V',"Venta");
        listaTiposCompro.add(item);
        item= new ItemTipo('F',"Factura");
        listaTiposCompro.add(item);
        cbTipoCompro.setItems(listaTiposCompro);
        
        //Buscar productos conforme va escribiendo
        nombreProducto.textProperty().addListener(new ChangeListener<String>() {
        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue, 
        String newValue) {
            cargaListaProductos();
        }
        });
        
        columnaProducto.setCellValueFactory( new PropertyValueFactory<VentaDet,String>("producto"));
        dataVenta.setEditable(true);
    
        columnaCantidad.setCellValueFactory( new PropertyValueFactory<VentaDet,String>("cantidad"));
        columnaPrecioUnitario.setCellValueFactory( new PropertyValueFactory<VentaDet,BigDecimal>("precioUnitario"));
        columnaPrecioUnitario.setCellFactory(column -> {
        TableCell<VentaDet,BigDecimal> cell = new TableCell<VentaDet, BigDecimal>() {
            DecimalFormat df = new DecimalFormat( "#,##0.#0", new DecimalFormatSymbols(new Locale("es", "AR")));    
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
            DecimalFormat df = new DecimalFormat( "#,##0.#0", new DecimalFormatSymbols(new Locale("es", "AR")));    
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
        
        //Doble click para seleccionar un producto    
        dataProductosBuscados.setRowFactory( tv -> {
        TableRow<Producto> row = new TableRow<>();
        row.setOnMouseClicked(event -> {
        if (event.getClickCount() == 2 && (! row.isEmpty()) && wb_nuevo==true) {
            Producto producto = row.getItem();
            
            if (producto.getPrecioContado()==null){
              Alert alert = new Alert(Alert.AlertType.WARNING);
              alert.setTitle(PuntoVenta.getTituloApp());
              alert.setContentText("Producto seleccionado no tiene precio cargado");
              alert.show();            
              return;
            }
            
            System.out.println(producto);
            
            //Fecha y hora de carga actual
            TimeZone gmtZone = TimeZone.getTimeZone("America/Buenos_Aires");
            DateFormat destDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            destDateFormat.setTimeZone(gmtZone);
            Date lda_fec_carga = new Date();
            
            double ld_cantidad;
            //Si es un producto final o no elaborado puede agregar mas de 1
            if (producto.getElaborado()=='0')
                ld_cantidad=pedirCantidad(1,producto.getNombre());
            else
                ld_cantidad=1;
            
            if (ld_cantidad > 0){
                VentaDet d= new VentaDet();
                d.setProducto(producto);
                d.setVenta(registroSel);
                BigDecimal cantidadOrig = new BigDecimal(ld_cantidad);
                BigDecimal cantidadRedondeada = cantidadOrig.setScale(2,RoundingMode.HALF_UP);
                d.setCantidad(cantidadRedondeada);
                d.setPrecioUnitario(producto.getPrecioContado().setScale(2,RoundingMode.HALF_UP));
                d.setValorAdicional(BigDecimal.ZERO);
                BigDecimal precioTotalOrig = new BigDecimal(ld_cantidad*producto.getPrecioContado().doubleValue());
                BigDecimal precioTotalRedondeada = precioTotalOrig.setScale(2,RoundingMode.HALF_UP);
                d.setPrecioTotal(precioTotalRedondeada);
                d.setEstado('P'); //Pendiente
                d.setUnidadMedida(producto.getUnidadMedida());
                detalleVenta.add(d);
                dataVenta.setItems(detalleVenta);
                dataVenta.getSelectionModel().select(detalleVenta.size() - 1);
            
                double ld_total=0.00;
                ld_total=calculaTotal();
                DecimalFormat df = new DecimalFormat( "#,##0.#0", new DecimalFormatSymbols(new Locale("es", "AR")));    
                valorTotal.setText(df.format(ld_total));
                       
                if (buGrabarVenta.isDisabled())
                    buGrabarVenta.setDisable(false);
                
            }
        }
        });
        
        return row ;
        });
        
        //Eliminar producto de la venta
        buEliminar.setOnAction(event -> {
           VentaDet pedidoDetSel= (VentaDet) dataVenta.getSelectionModel().getSelectedItem();
           if (pedidoDetSel !=null){
                Alert alert;
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setHeaderText(null);
                alert.setTitle(PuntoVenta.getTituloApp());
                alert.setContentText("¿Está seguro de eliminar el producto seleccionado?");
                Optional<ButtonType> action = alert.showAndWait();
                if (action.get() == ButtonType.OK) {
                        int li_fila=dataVenta.getSelectionModel().getSelectedIndex();
                        detalleVenta.remove(li_fila);
                        dataVenta.setItems(detalleVenta);     
                        double ld_total=0.00;
                        ld_total=calculaTotal();
                        DecimalFormat df = new DecimalFormat( "#,##0.##", new DecimalFormatSymbols(new Locale("es", "AR")));    
                        valorTotal.setText(df.format(ld_total));                        
                        wb_mod=true;                    
                }
           }
        });
        
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
        Platform.runLater(() -> nombreProducto.requestFocus());
    }
    
    public double calculaTotal(){
        double ld_total=0.00;
        for (VentaDet m: detalleVenta) {
            ld_total=ld_total + m.getPrecioTotal().doubleValue();
        }
        return ld_total;
    }

    public void nuevaVenta(boolean isVentaRapida){
        wb_nuevo=true;
        wbVentaRapida=isVentaRapida;
        usuario.setText(PuntoVenta.getUsuarioConectado().getNombreCompletoUsuario());
        personaSel=null;
        buGrabarVenta.setDisable(false); 
        buBuscarProductos.setDisable(false);
        buBuscarCliente.setDisable(false);
        cbTipoCompro.setDisable(false);
        buEliminar.setDisable(false);
        buImprimir.setDisable(true);
        wb_mod=false;  
        
        //Venta nuevo 
        condIvaCliente.setText("Consumidor Final");
        
        ObservableList<ItemTipo> listaTiposCompro = FXCollections.observableArrayList();
        ItemTipo item= new ItemTipo('V',"Venta");
        listaTiposCompro.add(item);
        item= new ItemTipo('F',"Factura");
        listaTiposCompro.add(item);
        cbTipoCompro.setItems(listaTiposCompro);
        
        //Tipo de comprobante por default Factura
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
        detalleVenta.clear();
        dataVenta.setItems(detalleVenta);     
        buGrabarVenta.setDisable(true);
        numCompro.setText("");
        
        if (wbVentaRapida){
            Producto producto=PuntoVenta.getProductoGenerico();
            CantidadProducto cantidadPrecio=pedirPrecioTotal(1,producto.getNombre());
            if (cantidadPrecio!=null){
                double ld_cantidad=0,ld_precioTotal=0;
                ld_cantidad=cantidadPrecio.getCantidad();
                ld_precioTotal=cantidadPrecio.getPrecioTotal();
                if (ld_cantidad >= 1 && ld_precioTotal > 0){
                    VentaDet d= new VentaDet();
                    d.setProducto(producto);
                    d.setVenta(registroSel);
                    BigDecimal cantidadOrig = new BigDecimal(ld_cantidad);
                    BigDecimal cantidadRedondeada = cantidadOrig.setScale(2,RoundingMode.HALF_UP);
                    d.setCantidad(cantidadRedondeada);
                    d.setPrecioUnitario(new BigDecimal(ld_precioTotal));
                    d.setValorAdicional(BigDecimal.ZERO);
                    BigDecimal precioTotalOrig = new BigDecimal(ld_precioTotal);
                    BigDecimal precioTotalRedondeada = precioTotalOrig.setScale(2,RoundingMode.HALF_UP);
                    d.setPrecioTotal(precioTotalRedondeada);
                    d.setEstado('P'); //Pendiente
                    d.setUnidadMedida(producto.getUnidadMedida());
                    detalleVenta.add(d);
                    dataVenta.setItems(detalleVenta);
                    dataVenta.getSelectionModel().select(detalleVenta.size() - 1);
                    
                    double ld_total=0.00;
                    ld_total=calculaTotal();
                    DecimalFormat df = new DecimalFormat( "#,##0.##", new DecimalFormatSymbols(new Locale("es", "AR")));    
                    valorTotal.setText(df.format(ld_total));
                       
                    if (buGrabarVenta.isDisabled())
                        buGrabarVenta.setDisable(false);
                }
            }
        }
        // Poner focus en el nombre del producto
        Platform.runLater(() -> buGrabarVenta.requestFocus());
    }
    
    public void editaVenta(){
        
        wb_nuevo=false;
        buEliminar.setDisable(true);
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

            detalleVenta.add(p);
            
        }
        
        //Ordeno la lista por hora de venta
        //Collections.sort(detalleVenta,Comparator.comparing((VentaDet p)->p.getHoraVenta()).thenComparing(p->p.getProducto().getNombre()));
        
        dataVenta.setItems(detalleVenta);
        
        DecimalFormat df = new DecimalFormat( "#,##0.##", new DecimalFormatSymbols(new Locale("es", "AR")));
        valorTotal.setText(df.format(registroSel.getValorFinal().doubleValue()));
        
        buGrabarVenta.setDisable(true);
        buBuscarProductos.setDisable(true);
        
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
    
    //Carga lista de productos
    public void cargaListaProductos(){
        listaProductosBuscados.clear();
        String nombre;
        nombre=nombreProducto.getText();
        if (nombre==null) 
            nombre="";
        else
            nombre=nombre.trim();
        
        columnaProductoBuscado.setCellValueFactory( new PropertyValueFactory<Producto,String>("nombre"));
        columnaRubroBuscado.setCellValueFactory( new PropertyValueFactory<Producto,String>("rubro"));
        columnaPrecioBuscado.setCellValueFactory( new PropertyValueFactory<Producto,BigDecimal>("precioContado"));
        columnaPrecioBuscado.setCellFactory(column -> {
        TableCell<Producto,BigDecimal> cell = new TableCell<Producto, BigDecimal>() {
            DecimalFormat df = new DecimalFormat( "#,##0.#0", new DecimalFormatSymbols(new Locale("es", "AR")));    
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
        columnaPrecioBuscado.setStyle("-fx-alignment: CENTER-RIGHT;");

        Date lda_fec_carga = new Date();
        List<Producto> listaProductos=buscarProductos(idRubroSel,nombre);
        for (Producto producto: listaProductos) {
            double ld_precio=0;
            try {
                //Obtengo el precio del producto
                ld_precio=obtienePrecio(PuntoVenta.getLocalSel().getId(),producto.getId(),lda_fec_carga);
            } catch (SQLException ex) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setTitle(PuntoVenta.getTituloApp());
                alert.setContentText("Error al consultar precio: " + ex.getMessage());
                alert.show();
            }
            producto.setPrecioContado((new BigDecimal(ld_precio)).setScale(2,RoundingMode.HALF_UP));
            listaProductosBuscados.add(producto);
            
        }
        dataProductosBuscados.setItems(listaProductosBuscados);
          
    }
    
    //Busca lista de productos en base al nombre
    public List<Producto> buscarProductos(int idRubroSel,String nombreProducto){
        if (nombreProducto==null)
            nombreProducto="";
        nombreProducto="%".concat(nombreProducto).concat("%");
        List<Producto> resultList=null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        //Busco todas las mesas
        try {
            session.beginTransaction();
            Query q;
            if (idAreaSel==0 && idRubroSel==0){
                //Todos los rubros y todas las áreas
                q=session.createQuery("from Producto a where upper(nombre) like :nombreProducto "
                        + "and estado='1' order by nombre");
                q.setParameter("nombreProducto",nombreProducto.toUpperCase());
            }
            else {
                if (idRubroSel==0){
                    //Todos los rubros
                    q=session.createQuery("from Producto a where rubro.areaNegocio.id = :idAreaSel "
                                        + "and upper(nombre) like :nombreProducto and estado='1' order by nombre");
                    q.setParameter("idAreaSel",idAreaSel);
                    q.setParameter("nombreProducto",nombreProducto.toUpperCase());
                }
                else{
                    //Un rubro específico
                    q=session.createQuery("from Producto a where rubro.id = :idRubroSel "
                            + "and upper(nombre) like :nombreProducto and estado='1' order by nombre");
                    q.setParameter("idRubroSel",idRubroSel);
                    q.setParameter("nombreProducto",nombreProducto.toUpperCase());
                }
            }
            
            resultList = q.list();
            session.getTransaction().commit();
        } catch (HibernateException he) {
            session.getTransaction().rollback();
            he.printStackTrace();
        }
        finally {
            session.close();
        }
        return resultList;
        
    }
    

    
    //Carga el grid de áreas de negocio
    public void cargarAreas(){
        gridAreas.getChildren().clear();
        Button buArea=null;
        int liCantiAreas=0;
        int li_area=0;
        //Obtengo la lista de áreas de negocios
        List<AreaNegocio>listaAreas=new ArrayList();
        listaAreas=buscarAreas();
        
        liCantiAreas=listaAreas.size();
        int li_filas=liCantiAreas/3;
        double ld_residuo;
        ld_residuo=(liCantiAreas % 3);
        if (ld_residuo > 0) li_filas++;
        
        for (int i = 0; i < li_filas; i++) {
            for (int j = 0; j < 3; j++) {
                li_area++;
                if (li_area <= liCantiAreas){
                        AreaNegocio area=(AreaNegocio) listaAreas.get(li_area - 1);
                        buArea= new Button(area.getNombre() + "\n");
                        buArea.setId(String.valueOf(area.getId()));
                        buArea.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                        
                        if(idAreaSel==area.getId().intValue())
                            buArea.setStyle("-fx-border-color: white; -fx-background-color :  #EB3501; -fx-text-fill:  #FFFFFF;-fx-font-weight: bold;");
                        else
                            buArea.setStyle("-fx-border-color: white; -fx-background-color :  #1C5599; -fx-text-fill:  #FFFFFF;-fx-font-weight: bold;");
                        buArea.setPrefHeight(75);
                        
                        buArea.addEventHandler(EventType.ROOT, (event) -> {
                            if (event.getEventType() == ActionEvent.ACTION) {
                                //Cuando se hace click en el área seleccionada
                                //se carga la lista de rubros de esa área
                                Button areaButton = (Button) event.getTarget();
                                String ls_texto=areaButton.getText();
                                idAreaSel=Integer.valueOf(areaButton.getId()).intValue();
                                AreaNegocio areaSel=obtenerAreaNegocio(idAreaSel);
                                idRubroSel=0;
                                labelRubroSel.setText("Todos");
                                nombreProducto.setText("");
                                cargarRubros(areaSel);
                                cargarAreas();
                                cargaListaProductos();
                                // Poner focus en campo para nombre de producto
                                Platform.runLater(() -> nombreProducto.requestFocus());
                                
//                                Button rubroButton = (Button) event.getTarget();
//                                String ls_texto=rubroButton.getText();
//                                //Marco el rubro seleccionado
//                                labelRubroSel.setText(ls_texto);
//                                idRubroSel=Integer.valueOf(rubroButton.getId()).intValue();
//                                cargaListaProductos();
                            }
                        });
                        
                        gridAreas.add(buArea,j,i + 1); //  (child, columnIndex, rowIndex)
                        gridAreas.setFillHeight(buArea, Boolean.TRUE);
                        gridAreas.setFillWidth(buArea, Boolean.TRUE);
                          
                        
                }
                else
                    continue;
                
            }
        }
    }
    
    //Carga el grid de rubros
    public void cargarRubros(AreaNegocio areaNegocio){
        gridRubros.getChildren().clear();
        Button buRubro=null;
        int liCantiRubros=0;
        int li_rubro=0;
        //Obtengo la lista de rubros
        List<Rubro>listaRubros=new ArrayList();
        listaRubros=buscarRubros(areaNegocio);
        
        liCantiRubros=listaRubros.size();
        int li_filas=liCantiRubros/3;
        double ld_residuo;
        ld_residuo=(liCantiRubros % 3);
        if (ld_residuo > 0) li_filas++;
        
        for (int i = 0; i < li_filas; i++) {
            for (int j = 0; j < 3; j++) {
                li_rubro++;
                if (li_rubro <= liCantiRubros){
                    Rubro rubro=(Rubro) listaRubros.get(li_rubro - 1);
                    buRubro= new Button(rubro.getNombre() + "\n");
                    buRubro.setId(String.valueOf(rubro.getId()));
                    buRubro.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                    if(idRubroSel==rubro.getId().intValue())
                            buRubro.setStyle("-fx-border-color: white; -fx-background-color :  #ff6f00; -fx-text-fill:  #FFFFFF;-fx-font-weight: bold;");
                        else
                            buRubro.setStyle("-fx-border-color: white; -fx-background-color :  #2196f3; -fx-text-fill:  #FFFFFF;-fx-font-weight: bold;");
                    buRubro.setPrefHeight(75);
                        
                        
                        buRubro.addEventHandler(EventType.ROOT, (event) -> {
                            if (event.getEventType() == ActionEvent.ACTION) {
                                //Cuando se hace click en la mesa se muestra el detalle de la misma
                                Button rubroButton = (Button) event.getTarget();
                                String ls_texto=rubroButton.getText();
                                //Marco el rubro seleccionado
                                labelRubroSel.setText(ls_texto);
                                idRubroSel=Integer.valueOf(rubroButton.getId()).intValue();
                                nombreProducto.setText("");
                                cargaListaProductos();
                                AreaNegocio areaNegocio1=obtenerAreaNegocio(idAreaSel);
                                cargarRubros(areaNegocio);
                                // Poner focus en campo para nombre de producto
                                Platform.runLater(() -> nombreProducto.requestFocus());
                            }
                        });
                        
                        gridRubros.add(buRubro,j,i + 1); //  (child, columnIndex, rowIndex)
                        gridRubros.setFillHeight(buRubro, Boolean.TRUE);
                        gridRubros.setFillWidth(buRubro, Boolean.TRUE);
                        
                        
                }
                else
                    continue;
                
            }
        }
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
            lsDirComprador="";  //Agregar dirección
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
        
        //Venta venta= new Venta();
        
        
        List<VentaDet> listaItems= new ArrayList<VentaDet>();
        for (VentaDet d: detalleVenta){
            VentaDet ventaDet= new VentaDet();
            ventaDet.setVenta(registroSel);
            ventaDet.setProducto(d.getProducto());
            ventaDet.setCantidad(d.getCantidad());
            ventaDet.setUnidadMedida(d.getUnidadMedida());
            ventaDet.setPrecioUnitario(new BigDecimal(d.getPrecioUnitario().doubleValue() + (d.getValorAdicional()==null ? 0 : d.getValorAdicional().doubleValue())));
            ventaDet.setPrecioTotal(d.getPrecioTotal());
            listaItems.add(ventaDet);
        }
                
        if (lc_tipo_compro=='F'){
            //Imprimo el ticket fiscal
            ImpresionEpson impresion = new ImpresionEpson();
            //Configuro el puerto COM del controlador fiscal
            impresion.setPuerto(PuntoVenta.getPuertoCF());
            impresion.inicializaControlador();
            impresion.abrirPuerto();
            boolean lb_ticket=impresion.imprimirTicketFactura(lsTipDoc,lsNombreComprador,lsDirComprador,
                             liTipIdComprador, lsNumIdComprador, liIvaComprador, lsDocAsocL1, lsDocAsocL2, lsDocAsocL3,lsLineaCompOrigen,listaItems);
            if (lb_ticket && !impresion.getNumComprobante().isEmpty()){
                //Pongo los datos del ticket en la factura
                registroSel.setPuntoVenta(PuntoVenta.getPuntoVentaCF());
                registroSel.setNumFactura(impresion.getNumComprobante());
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
        buBuscarProductos.setDisable(true);
        buBuscarCliente.setDisable(true);
        cbTipoCompro.setDisable(true);
        buEliminar.setDisable(true);
        buImprimir.setDisable(false);
        wb_mod=false;  
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle("Información");
        alert.setContentText("Venta grabado exitosamente!");
        alert.showAndWait();     
        
        if (wbVentaRapida)
            nuevaVenta(wbVentaRapida);
 
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
        Connection conexion = conector.connect("gestion");
        
        try {      
             s=conexion.prepareCall("{call sp_get_precio_producto_local ( ? , ? , ? , ? )}"); 
             s.setInt(1,idLocal); 
             s.setInt(2,idProducto); 
             s.setTimestamp(3,new java.sql.Timestamp(fecha.getTime()));
             s.registerOutParameter(4,java.sql.Types.DOUBLE);
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
