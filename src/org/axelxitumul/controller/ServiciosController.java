
package org.axelxitumul.controller;


import com.jfoenix.controls.JFXTimePicker;
import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import org.axelxitumul.bean.Empresa;
import org.axelxitumul.bean.Servicios;
import org.axelxitumul.db.Conexion;
import org.axelxitumul.main.Principal;


public class ServiciosController implements Initializable{

    
    private enum operaciones{GUARDAR, ELIMINAR, ACTUALIZAR, NINGUNO};
    private operaciones tipoOperacion = operaciones.NINGUNO;
    private Principal escenarioPrincipal;
    private ObservableList<Servicios>listaServicios;
    private ObservableList<Empresa>listaEmpresa;
    private DatePicker fecha;
    
    @FXML private TextField txtCodigoServicio;
    @FXML private GridPane grpFecha;
    @FXML private TextField txtTipoDeServicio;
    @FXML private JFXTimePicker jfxHora;
    @FXML private TextField txtLugarDeServicio;
    @FXML private TextField txtTelefonoContacto;
    @FXML private ComboBox cmbCodigoEmpresa;
    @FXML private TableView tblServicios;
    @FXML private TableColumn colCodigoServicio;
    @FXML private TableColumn colTipoServicio;
    @FXML private TableColumn colFecha;
    @FXML private TableColumn colHora;
    @FXML private TableColumn colLugar;
    @FXML private TableColumn colTelefonoContacto;
    @FXML private TableColumn colCodEmpresa;
    @FXML private Button btnNuevoServicio;
    @FXML private Button btnEliminarServicio;
    @FXML private Button btnEditarServicio;
    @FXML private Button btnReporteServicio;
    @FXML private ImageView imgNuevoServicio;
    @FXML private ImageView imgEliminarServicio;
    @FXML private ImageView imgEditarServicio;
    @FXML private ImageView imgReporteServicio;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        fecha = new DatePicker(Locale.ENGLISH);
        fecha.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        fecha.getCalendarView().todayButtonTextProperty().set("Today");
        fecha.getCalendarView().setShowWeeks(false);
        fecha.getStylesheets().add("/org/axelxitumul/resource/TonysKinal.css");
        fecha.setDisable(true);
        grpFecha.add(fecha, 3, 0);
        cmbCodigoEmpresa.setItems(getEmpresas());
    
    }
    
    public void cargarDatos(){
        tblServicios.setItems(getServicios());
        colCodigoServicio.setCellValueFactory(new PropertyValueFactory<Servicios, Integer>("codigoServicio"));
        colFecha.setCellValueFactory(new PropertyValueFactory<Servicios, Date>("fechaServicio"));
        colTipoServicio.setCellValueFactory(new PropertyValueFactory<Servicios, String>("tipoDeServicio"));
        colHora.setCellValueFactory(new PropertyValueFactory<Servicios, String>("horaDeServicio"));
        colLugar.setCellValueFactory(new PropertyValueFactory<Servicios, String>("lugarServicio"));
        colTelefonoContacto.setCellValueFactory(new PropertyValueFactory<Servicios, String>("telefonoContacto"));
        colCodEmpresa.setCellValueFactory(new PropertyValueFactory<Servicios, Integer>("codigoEmpresa"));
    }
    
    public void seleccionarElemento(){
        if(tblServicios.getSelectionModel().getSelectedItem() != null){
            txtCodigoServicio.setText(String.valueOf(((Servicios)tblServicios.getSelectionModel().getSelectedItem()).getCodigoServicio()));
            fecha.selectedDateProperty().set(((Servicios)tblServicios.getSelectionModel().getSelectedItem()).getFechaServicio());
            txtTipoDeServicio.setText(((Servicios)tblServicios.getSelectionModel().getSelectedItem()).getTipoDeServicio());
            jfxHora.setValue(LocalTime.parse(((Servicios)tblServicios.getSelectionModel().getSelectedItem()).getHoraDeServicio()));
            txtLugarDeServicio.setText(((Servicios)tblServicios.getSelectionModel().getSelectedItem()).getLugarServicio());
            txtTelefonoContacto.setText(((Servicios)tblServicios.getSelectionModel().getSelectedItem()).getTelefonoContacto());
            cmbCodigoEmpresa.getSelectionModel().select(buscarEmpresa(((Servicios)tblServicios.getSelectionModel().getSelectedItem()).getCodigoEmpresa()));
        }else{
            JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
        }
    }
    
    public Empresa buscarEmpresa(int codigoEmpresa){
        Empresa resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_Buscar_Empresa(?)");
            procedimiento.setInt(1, codigoEmpresa);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Empresa(registro.getInt("codigoEmpresa"),
                                        registro.getString("nombreEmpresa"),
                                        registro.getString("direccion"),
                                        registro.getString("telefono"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }
    
    public ObservableList<Servicios> getServicios(){
        ArrayList<Servicios> lista = new ArrayList<Servicios>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_Listar_Servicios()");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Servicios(resultado.getInt("codigoServicio"),
                                        resultado.getDate("fechaServicio"),
                                        resultado.getString("tipoDeServicio"),
                                        resultado.getString("horaDeServicio"),
                                        resultado.getString("lugarServicio"),
                                        resultado.getString("telefonoContacto"),
                                        resultado.getInt("codigoEmpresa")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaServicios = FXCollections.observableArrayList(lista);
    }
    
    public ObservableList<Empresa> getEmpresas(){
        ArrayList<Empresa> lista = new ArrayList<Empresa>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_Listar_Empresas()");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Empresa(resultado.getInt("codigoEmpresa"),
                                        resultado.getString("nombreEmpresa"),
                                        resultado.getString("direccion"),
                                        resultado.getString("telefono")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  listaEmpresa = FXCollections.observableArrayList(lista);
    }
    
    public void nuevo(){
        switch(tipoOperacion){
            case NINGUNO:
                limpiarControles();
                activarControles();
                btnNuevoServicio.setText("Guardar");
                btnEliminarServicio.setText("Cancelar");
                btnEditarServicio.setDisable(true);
                btnReporteServicio.setDisable(true);
                imgNuevoServicio.setImage(new Image("/org/axelxitumul/image/guardar.png"));
                imgEliminarServicio.setImage(new Image("/org/axelxitumul/image/cancelar.png"));
                tipoOperacion = operaciones.GUARDAR;
                break;
            case GUARDAR:
                guardar();
                limpiarControles();
                desactivarControles();
                btnNuevoServicio.setText("Nuevo");
                btnEliminarServicio.setText("Eliminar");
                btnEditarServicio.setDisable(false);
                btnReporteServicio.setDisable(false);
                imgNuevoServicio.setImage(new Image("/org/axelxitumul/image/nuevo.png"));
                imgEliminarServicio.setImage(new Image("/org/axelxitumul/image/eliminar.png"));
                tipoOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }
    
    public void guardar(){
        Servicios registro = new Servicios();
        if(fecha.getSelectedDate()== null || txtTipoDeServicio.getText().isEmpty() || jfxHora.getValue()== null || txtLugarDeServicio.getText().isEmpty() || txtTelefonoContacto.getText().isEmpty() || cmbCodigoEmpresa.getSelectionModel()== null){
            JOptionPane.showMessageDialog(null, "LLene todos los espacios en blanco");
        }else{
            registro.setFechaServicio(fecha.getSelectedDate());
            registro.setTipoDeServicio(txtTipoDeServicio.getText());
            registro.setHoraDeServicio(String.valueOf(jfxHora.getValue()));
            registro.setLugarServicio(txtLugarDeServicio.getText());
            registro.setTelefonoContacto(txtTelefonoContacto.getText());
            registro.setCodigoEmpresa(((Empresa)cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
            try {
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_Agregar_Servicio(?,?,?,?,?,?)");
                procedimiento.setDate(1, new java.sql.Date(registro.getFechaServicio().getTime()));
                procedimiento.setString(2, registro.getTipoDeServicio());
                procedimiento.setString(3, registro.getHoraDeServicio());
                procedimiento.setString(4, registro.getLugarServicio());
                procedimiento.setString(5, registro.getTelefonoContacto());
                procedimiento.setInt(6, registro.getCodigoEmpresa());
                procedimiento.execute();
                listaServicios.add(registro);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
            
    }
    
    public void eliminar(){
        switch(tipoOperacion){
            case GUARDAR:
                limpiarControles();
                desactivarControles();
                btnNuevoServicio.setText("Nuevo");
                btnEliminarServicio.setText("Eliminar");
                btnEditarServicio.setDisable(false);
                btnReporteServicio.setDisable(false);
                imgNuevoServicio.setImage(new Image("/org/axelxitumul/image/nuevo.png"));
                imgEliminarServicio.setImage(new Image("/org/axelxitumul/image/eliminar.png"));
                tipoOperacion = operaciones.NINGUNO;
                break;
            default:
                if(tblServicios.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "Estas seguro de eliminar este registro", "Eliminar Servicio", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_Eliminar_Servicio(?)");
                            procedimiento.setInt(1, ((Servicios)tblServicios.getSelectionModel().getSelectedItem()).getCodigoServicio());
                            listaServicios.remove(tblServicios.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                            procedimiento.execute();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else if(respuesta == JOptionPane.NO_OPTION){
                        limpiarControles();
                        desactivarControles();
                        tblServicios.getSelectionModel().clearSelection();
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                }
        }
       
    }
    
    public void editar(){
        switch(tipoOperacion){
            case NINGUNO:
                if(tblServicios.getSelectionModel().getSelectedItem() != null){
                    btnNuevoServicio.setDisable(true);
                    btnEliminarServicio.setDisable(true);
                    btnEditarServicio.setText("Actualizar");
                    btnReporteServicio.setText("Cancelar");
                    imgEditarServicio.setImage(new Image("/org/axelxitumul/image/actualizar.png"));
                    imgReporteServicio.setImage(new Image("/org/axelxitumul/image/cancelar.png"));
                    activarControles();
                    cmbCodigoEmpresa.setDisable(true);
                    tipoOperacion = operaciones.ACTUALIZAR;
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar Un elemento");
                }
                break;
            case ACTUALIZAR:
                actualizar();
                limpiarControles();
                desactivarControles();
                btnNuevoServicio.setDisable(false);
                btnEliminarServicio.setDisable(false);
                btnEditarServicio.setText("Editar");
                btnReporteServicio.setText("Reporte");
                imgEditarServicio.setImage(new Image("/org/axelxitumul/image/editar.png"));
                imgReporteServicio.setImage(new Image("/org/axelxitumul/image/reporte.png"));
                tipoOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }
    
    public void actualizar(){
       try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_Editar_Servicio(?,?,?,?,?,?)");
            Servicios registro = (Servicios)tblServicios.getSelectionModel().getSelectedItem();
            registro.setFechaServicio(fecha.getSelectedDate());
            registro.setTipoDeServicio(txtTipoDeServicio.getText());
            registro.setHoraDeServicio(String.valueOf(jfxHora.getValue()));
            registro.setLugarServicio(txtLugarDeServicio.getText());
            registro.setTelefonoContacto(txtTelefonoContacto.getText());
            procedimiento.setInt(1, registro.getCodigoServicio());
            procedimiento.setDate(2, new java.sql.Date(registro.getFechaServicio().getTime()));
            procedimiento.setString(3,registro.getTipoDeServicio());
            procedimiento.setString(4, registro.getHoraDeServicio());
            procedimiento.setString(5, registro.getLugarServicio());
            procedimiento.setString(6, registro.getTelefonoContacto());
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        } 
    }
    
    public void reporte(){
        switch (tipoOperacion){
            case ACTUALIZAR:
                limpiarControles();
                desactivarControles();
                btnNuevoServicio.setDisable(false);
                btnEliminarServicio.setDisable(false);
                btnEditarServicio.setText("Editar");
                btnReporteServicio.setText("Reporte");
                imgEditarServicio.setImage(new Image("/org/axelxitumul/image/editar.png"));
                imgReporteServicio.setImage(new Image ("/org/axelxitumul/image/reporte.png"));
                tipoOperacion = operaciones.NINGUNO;
                tblServicios.getSelectionModel().clearSelection();
                break;
    }
    }
    
    public void desactivarControles(){
        txtCodigoServicio.setEditable(false);
        fecha.setDisable(true);
        txtTipoDeServicio.setEditable(false);
        jfxHora.setDisable(true);
        txtLugarDeServicio.setEditable(false);
        txtTelefonoContacto.setEditable(false);
        cmbCodigoEmpresa.setDisable(true);
    }
    
    public void activarControles(){
        txtCodigoServicio.setEditable(false);
        fecha.setDisable(false);
        txtTipoDeServicio.setEditable(true);
        jfxHora.setDisable(false);
        txtLugarDeServicio.setEditable(true);
        txtTelefonoContacto.setEditable(true);
        cmbCodigoEmpresa.setDisable(false);
    }
    
    public void limpiarControles(){
        txtCodigoServicio.clear();
        fecha.selectedDateProperty().set(null);
        txtTipoDeServicio.clear();
        jfxHora.setValue(null);
        txtLugarDeServicio.clear();
        txtTelefonoContacto.clear();
        cmbCodigoEmpresa.setValue(null);
    }
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
    
    public void ventanaEmpresa(){
        escenarioPrincipal.ventanaEmpresa();
    }
}
