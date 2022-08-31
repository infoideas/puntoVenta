/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import entidades.CondIva;
import entidades.Localidad;
import entidades.Persona;
import entidades.PersonaDireccion;
import entidades.Provincia;
import entidades.Tipoid;
import general.HibernateUtil;
import general.ItemTipo;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import pdv.PuntoVenta;

/**
 *
 * @author rafaelg
 */
public class ClienteDetalleController extends VBox implements Initializable {
    @FXML
    TextField tfId;
    @FXML
    ComboBox cbTipoCliente;
    @FXML
    ComboBox cbTipoId;
    @FXML
    ComboBox cbCondIva;
    @FXML
    TextField tfNumId;
    @FXML
    TextField tfApellido;
    @FXML
    TextField tfNombre;
    @FXML
    TextField tfNombreFantasia;
    @FXML
    TextField tfRazonSocial;
    
    @FXML
    TableView<PersonaDireccion> dataDir;
    @FXML
    private TableColumn<PersonaDireccion,String> columnaDireccion;
    @FXML
    private TableColumn<PersonaDireccion,String> columnaTelefono;   
    @FXML
    private TableColumn<PersonaDireccion,Localidad> columnaLocalidad = new TableColumn<>("Localidad");
    
    @FXML
    Button buAceptar,buSalir;
    @FXML
    Button buNuevaDir,buEliminarDir;
    
    private ObservableList<PersonaDireccion> listaDir = FXCollections.observableArrayList();
    
    private Persona registroSel;
    private boolean wb_nuevo;
    private boolean wb_mod;

    public Persona getRegistroSel() {
        return registroSel;
    }

    public void setRegistroSel(Persona registroSel) {
        this.registroSel = registroSel;
    }

    public boolean isWb_mod() {
        return wb_mod;
    }

    public void setWb_mod(boolean wb_mod) {
        this.wb_mod = wb_mod;
    }

    public TableView<PersonaDireccion> getDataDir() {
        return dataDir;
    }

    public void setDataDir(TableView<PersonaDireccion> dataDir) {
        this.dataDir = dataDir;
    }

    public ObservableList<PersonaDireccion> getListaDir() {
        return listaDir;
    }

    public void setListaDir(ObservableList<PersonaDireccion> listaDir) {
        this.listaDir = listaDir;
    }
       
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dataDir.setEditable(true);
        columnaDireccion.setCellValueFactory(new PropertyValueFactory<PersonaDireccion,String>("direccion"));
        columnaDireccion.setCellFactory(TextFieldTableCell.forTableColumn());
        columnaDireccion.setOnEditCommit(data -> {
            PersonaDireccion p = data.getRowValue();
            p.setDireccion(data.getNewValue());
            System.out.println(p);
        });
        
        columnaTelefono.setCellValueFactory(new PropertyValueFactory<PersonaDireccion,String>("telefono"));
        columnaTelefono.setCellFactory(TextFieldTableCell.forTableColumn());
        columnaTelefono.setOnEditCommit(data -> {
            PersonaDireccion p = data.getRowValue();
            p.setTelefono(data.getNewValue());
            System.out.println(p);
        });
        
        //Cargo lista de localidades
        ObservableList<Localidad> listaItemsLoc = FXCollections.observableArrayList();
        List<Localidad> listaLoc=buscarListaLoc(PuntoVenta.getProvinciaEmpresa());
        for (Localidad loc: listaLoc) {
            listaItemsLoc.add(loc);
        }
        columnaLocalidad.setCellValueFactory(new PropertyValueFactory<PersonaDireccion,Localidad>("localidad"));
        columnaLocalidad.setCellFactory(ChoiceBoxTableCell.forTableColumn(listaItemsLoc));
        columnaLocalidad.setOnEditCommit((TableColumn.CellEditEvent<PersonaDireccion, Localidad> t) -> {
            ((PersonaDireccion) t.getTableView().getItems().get(
                    t.getTablePosition().getRow())
                    ).setLocalidad(t.getNewValue());
        });
        
        ObservableList<ItemTipo> listaTiposCliente = FXCollections.observableArrayList();
        ItemTipo item= new ItemTipo('P',"Persona");
        listaTiposCliente.add(item);
        item= new ItemTipo('E',"Empresa");
        listaTiposCliente.add(item);
        cbTipoCliente.setItems(listaTiposCliente);
        
        //Cargo lista de Tipos de Id
        ObservableList<Tipoid> listaItemsTipoId = FXCollections.observableArrayList();
        List<Tipoid> listaTiposId=buscarListaTiposId();
        for (Tipoid tipo: listaTiposId) {
            listaItemsTipoId.add(tipo);
        }
        cbTipoId.setItems(listaItemsTipoId);

        //Cargo lista de cond. iva
        ObservableList<CondIva> listaItemsCondIva = FXCollections.observableArrayList();
        List<CondIva> listaCondIva=buscarListaCondIva();
        for (CondIva condIva: listaCondIva) {
            listaItemsCondIva.add(condIva);
        }
        cbCondIva.setItems(listaItemsCondIva);
        
        buAceptar.setOnAction(event -> {
           if (grabaRegistro()){
               Stage stageActual;
               stageActual= (Stage) buAceptar.getScene().getWindow();        
               stageActual.close();    
           }
        });
        
        //Botón salir de la ventana
        buSalir.setOnAction(event -> {
            Stage stageActual = null;
            stageActual= (Stage) buSalir.getScene().getWindow();        
            stageActual.close();
        });
        
        //Agregar comprobante
        buNuevaDir.setOnAction(event -> {
            PersonaDireccion d= new PersonaDireccion();
            d.setPersona(registroSel);
            d.setProvincia(PuntoVenta.getProvinciaEmpresa());
            d.setLocalidad(PuntoVenta.getLocalidadEmpresa());
            listaDir.add(d);
            dataDir.setItems(listaDir);
            dataDir.getSelectionModel().select(listaDir.size() - 1);
        });  
        
        //Consular o editar registro
        buEliminarDir.setOnAction(event -> {
           PersonaDireccion registroSel= (PersonaDireccion) dataDir.getSelectionModel().getSelectedItem();
           if (registroSel !=null){
              int li_fila_sel=dataDir.getSelectionModel().getSelectedIndex();
              listaDir.remove(li_fila_sel);
              dataDir.setItems(listaDir);
           }
        });
        
        
    }
    
    public void nuevoRegistro(){
        registroSel= new Persona();
        wb_nuevo=true;
        tfNombre.requestFocus();
    }
            
    public void editaRegistro(){
        Session session=HibernateUtil.getSessionFactory().openSession();
        try{
            session.beginTransaction();
            this.registroSel =(Persona) session.get(Persona.class,registroSel.getId());
            Hibernate.initialize(registroSel.getPersonaDireccions());
            session.getTransaction().commit();
        }
        catch (HibernateException e){
            session.getTransaction().rollback();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle(PuntoVenta.getTituloApp());
            alert.setContentText(e.getMessage());
            alert.show();
            return;
        }
        finally {
            session.close();
        }
        
        String ls_tipo = null;
        tfId.setText(String.valueOf(registroSel.getId().intValue()));
        switch(registroSel.getTipo())
        {
            case 'P' :
                ls_tipo="Persona";
                break;  
            case 'E' :
                ls_tipo="Empresa";
                break;    
        }
        ItemTipo item= new ItemTipo(registroSel.getTipo(),ls_tipo);
        cbTipoCliente.setValue(item);
        cbTipoId.setValue(registroSel.getTipoid());
        cbCondIva.setValue(registroSel.getCondIva());
        tfNumId.setText(registroSel.getNumIdentificacion());
        tfApellido.setText(registroSel.getApellido());
        tfNombre.setText(registroSel.getNombre());
        tfNombreFantasia.setText(registroSel.getNombreFantasia());
        tfRazonSocial.setText(registroSel.getRazonSocial());
        wb_nuevo=false;
        
        Iterator i= registroSel.getPersonaDireccions().iterator();
        while (i.hasNext()){
            PersonaDireccion p= (PersonaDireccion) i.next();
            listaDir.add(p);
        }
        dataDir.setItems(listaDir);
        tfNombre.requestFocus();
    }
    
    public boolean grabaRegistro()
    {
        ItemTipo tipoCliente=(ItemTipo) cbTipoCliente.getValue();
        if (tipoCliente==null){
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle(PuntoVenta.getTituloApp());
            alert.setContentText("Debe seleccionar el tipo de cliente");
            alert.show();          
            cbTipoCliente.requestFocus();
            return false;
        }
        char lc_tipo=tipoCliente.getTipo();
        Tipoid tipoId=(Tipoid) cbTipoId.getValue();

        CondIva condIVA=(CondIva) cbCondIva.getValue();
        if (condIVA==null){
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle(PuntoVenta.getTituloApp());
            alert.setContentText("Debe seleccionar la condición ante IVA");
            alert.show();          
            cbCondIva.requestFocus();
            return false;
        }
        
        
        String ls_num_identificacion = null; 
        String ls_apellido = null,ls_nombre = null;
        if (tfNumId.getText() != null)
            ls_num_identificacion=tfNumId.getText().toString().trim();
        
        if (lc_tipo=='P'){
            //Persona
            if (tfApellido.getText() != null)
                ls_apellido=tfApellido.getText().toString().trim();

            if (ls_apellido==null || ls_apellido.isEmpty()){
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle(PuntoVenta.getTituloApp());
                alert.setContentText("Debe ingresar el apellido");
                alert.show();          
                return false;
            }
            
            if (tfNombre.getText() != null)
                ls_nombre=tfNombre.getText().toString().trim();

            if (ls_nombre==null || ls_nombre.isEmpty()){
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle(PuntoVenta.getTituloApp());
                alert.setContentText("Debe ingresar el nombre");
                alert.show();          
                return false;
            }
        }
        
        String ls_nombre_fantasia=null,ls_razon_social=null;
        if (lc_tipo=='E'){
            //Empresa
            if (tfNombreFantasia.getText() != null)
                ls_nombre_fantasia=tfNombreFantasia.getText().toString().trim();

            if (ls_nombre_fantasia==null || ls_nombre_fantasia.isEmpty()){
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle(PuntoVenta.getTituloApp());
                alert.setContentText("Debe ingresar el nombre de fantasía");
                alert.show();          
                return false;
            }
            
            if (tfRazonSocial.getText() != null)
                ls_razon_social=tfRazonSocial.getText().toString().trim();

            if (ls_razon_social==null || ls_razon_social.isEmpty()){
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle(PuntoVenta.getTituloApp());
                alert.setContentText("Debe ingresar la razón social");
                alert.show();          
                return false;
            }
        }

        Persona p=getRegistroSel();
        p.setTipo(lc_tipo);
        p.setTipoid(tipoId);
        p.setNumIdentificacion(ls_num_identificacion);
        p.setApellido(ls_apellido);
        p.setNombre(ls_nombre);
        p.setNombreFantasia(ls_nombre_fantasia);
        p.setRazonSocial(ls_razon_social);
        p.setCondIva(condIVA);
        
        //Pongo las direcciones
        listaDir=dataDir.getItems();
        registroSel.getPersonaDireccions().clear();
        int i=0;
        for (PersonaDireccion d: listaDir) {
           if (d.getDireccion()==null || d.getDireccion().isEmpty()) {
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle(PuntoVenta.getTituloApp());
                alert.setContentText("Debe ingresar la dirección");
                alert.show();          
                return false;   
           }
           if (d.getTelefono()==null || d.getTelefono().isEmpty()) {
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle(PuntoVenta.getTituloApp());
                alert.setContentText("Debe ingresar el teléfono");
                alert.show();          
                return false;   
           }
           registroSel.getPersonaDireccions().add(d);
        }
        
        if (actualizaBaseDatos(p)){
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle(PuntoVenta.getTituloApp());
            alert.setContentText("Actualización exitosa!");
            alert.show();
            
            if (wb_nuevo)
               tfId.setText(Integer.toString(p.getId()));
            wb_mod=true;
            return true;    
        }
        else
            return false;
        
    }
    
    private boolean actualizaBaseDatos(Object o){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Persona u= (Persona) o;
        try{
            session.beginTransaction();
            session.saveOrUpdate(u);
            session.getTransaction().commit();
            return true;
            
        }
        catch (HibernateException e){
            session.getTransaction().rollback();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle(PuntoVenta.getTituloApp());
            alert.setContentText("Error al actualizar: " + e.getMessage());
            alert.showAndWait();
            return false;
        }
        finally {
            session.close();
        }
    }
    
    
    //Obtiene lista de tipos de id de la base de datos
    public List<Tipoid> buscarListaTiposId(){
        List<Tipoid> resultList=null;

        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            Query q=session.createQuery("from Tipoid a order by nombre");
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
    
    //Obtiene lista de localidades
    public List<Localidad> buscarListaLoc(Provincia provincia){
        List<Localidad> resultList=null;

        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            Query q=session.createQuery("from Localidad a where a.provincia = :provinciaSel order by nombre");
            q.setParameter("provinciaSel", provincia);
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
    
    //Obtiene lista de Cond. Iva de id de la base de datos
    public List<CondIva> buscarListaCondIva(){
        List<CondIva> resultList=null;

        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            Query q=session.createQuery("from CondIva a order by nombre");
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

}

