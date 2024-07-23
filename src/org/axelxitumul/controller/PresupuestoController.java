package org.axelxitumul.controller;

import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
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
import org.axelxitumul.bean.Presupuesto;
import org.axelxitumul.db.Conexion;
import org.axelxitumul.main.Principal;
import org.axelxitumul.report.GenerarReporte;

public class PresupuestoController implements Initializable {
    private Principal escenarioPrincipal;
    private enum operaciones{GUARDAR,ELIMINAR,ACTUALIZAR,NINGUNO};
    private operaciones tipoOperacion = operaciones.NINGUNO;
    private ObservableList<Presupuesto> listaPresupuesto;
    private ObservableList<Empresa> listaEmpresa;
    private DatePicker fecha;
    
    
    @FXML private TextField txtCodigoPresupuesto;
    @FXML private TextField txtCantidadDePresupuesto;
    @FXML private GridPane grpFecha;
    @FXML private ComboBox cmbCodigoEmpresa;
    @FXML private TableView tblPresupuestos;
    @FXML private TableColumn colCodigoPresupuesto;
    @FXML private TableColumn colFechaDeSolicitud;
    @FXML private TableColumn colCantidadDePresupuesto;
    @FXML private TableColumn colCodigoEmpresa;
    @FXML private Button btnNuevoPresupuesto;
    @FXML private Button btnEliminarPresupuesto;
    @FXML private Button btnEditarPresupuesto;
    @FXML private Button btnReportePresupuesto;
    @FXML private ImageView imgNuevoPresupuesto;
    @FXML private ImageView imgEliminarPresupuesto;
    @FXML private ImageView imgEditarPresupuesto;
    @FXML private ImageView imgReportePresupuesto;
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        fecha = new DatePicker(Locale.ENGLISH);
        fecha.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        fecha.getCalendarView().todayButtonTextProperty().set("Today");
        fecha.getCalendarView().setShowWeeks(false);
        fecha.getStylesheets().add("/org/axelxitumul/resource/TonysKinal.css");
        grpFecha.add(fecha, 3, 0);
        cmbCodigoEmpresa.setItems(getEmpresa());
        fecha.setDisable(true);
    }
    
    public void cargarDatos(){
        tblPresupuestos.setItems(getPrespuesto());
        colCodigoPresupuesto.setCellValueFactory(new PropertyValueFactory<Presupuesto, Integer>("codigoPresupuesto"));
        colFechaDeSolicitud.setCellValueFactory(new PropertyValueFactory<Presupuesto, Date>("fechaDeSolicitud"));
        colCantidadDePresupuesto.setCellValueFactory(new PropertyValueFactory<Presupuesto, Double>("cantidadPresupuesto"));
        colCodigoEmpresa.setCellValueFactory(new PropertyValueFactory<Presupuesto, Integer>("codigoEmpresa"));
    }
    
    public void seleccionarElemento(){
        txtCodigoPresupuesto.setText(String.valueOf(((Presupuesto)tblPresupuestos.getSelectionModel().getSelectedItem()).getCodigoPresupuesto()));
        fecha.selectedDateProperty().set(((Presupuesto)tblPresupuestos.getSelectionModel().getSelectedItem()).getFechaDeSolicitud());
        txtCantidadDePresupuesto.setText(String.valueOf(((Presupuesto)tblPresupuestos.getSelectionModel().getSelectedItem()).getCantidadPresupuesto()));
        cmbCodigoEmpresa.getSelectionModel().select(buscarEmpresa(((Presupuesto)tblPresupuestos.getSelectionModel().getSelectedItem()).getCodigoEmpresa()));
    }
    
    public Empresa buscarEmpresa(int codigoEmpresa){
        Empresa resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_Buscar_Empresa(?)");
            procedimiento.setInt(1, codigoEmpresa);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Empresa(registro.getInt("codigoEmpresa"),
                                        registro.getString("nombreEmpresa"),
                                        registro.getString("direccion"),
                                        registro.getString("telefono"));
                
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return resultado;
    }
    
    public ObservableList<Empresa> getEmpresa(){
        ArrayList<Empresa> lista6 = new ArrayList<Empresa>();
        try{
            PreparedStatement procedimiento6 = Conexion.getInstance().getConexion().prepareCall("call sp_Listar_Empresas()");
            ResultSet resultado6 = procedimiento6.executeQuery();
            while(resultado6.next()){
                lista6.add(new Empresa(resultado6.getInt("codigoEmpresa"),
                                        resultado6.getString("nombreEmpresa"),
                                        resultado6.getString("direccion"),
                                        resultado6.getString("telefono")));
                        
            }
        }catch(Exception e){
            e.printStackTrace();
        }
       return listaEmpresa = FXCollections.observableArrayList(lista6);
    }
    
    public ObservableList<Presupuesto> getPrespuesto(){
        ArrayList<Presupuesto> lista5 = new ArrayList<Presupuesto>();
        try{
            PreparedStatement procedimiento5 = Conexion.getInstance().getConexion().prepareCall("call sp_Listar_Presupuestos()");
            ResultSet resultado5 = procedimiento5.executeQuery();
            while(resultado5.next()){
                lista5.add(new Presupuesto(resultado5.getInt("codigoPresupuesto"),
                            resultado5.getDate("fechaDeSolicitud"),
                             resultado5.getDouble("cantidadPresupuesto"),
                            resultado5.getInt("codigoEmpresa")));
                        
            }
        }catch(Exception e){
            e.printStackTrace();
        }
       return listaPresupuesto = FXCollections.observableArrayList(lista5);
    }
    
    public void nuevo(){
        switch(tipoOperacion){
            case NINGUNO:
                activarControles();
                btnNuevoPresupuesto.setText("Guardar");
                btnEliminarPresupuesto.setText("Cancelar");
                btnEditarPresupuesto.setDisable(true);
                btnReportePresupuesto.setDisable(true);
                imgNuevoPresupuesto.setImage(new Image("/org/axelxitumul/image/guardar.png"));
                imgEliminarPresupuesto.setImage(new Image("/org/axelxitumul/image/cancelar.png"));
                tipoOperacion = operaciones.GUARDAR;
                break;
            case GUARDAR:
                guardar();
                limpiarControles();
                desactivarControles();
                btnNuevoPresupuesto.setText("Nuevo");
                btnEliminarPresupuesto.setText("Eliminar");
                btnEditarPresupuesto.setDisable(false);
                btnReportePresupuesto.setDisable(false);
                imgNuevoPresupuesto.setImage(new Image("/org/axelxitumul/image/nuevo.png"));
                imgEliminarPresupuesto.setImage(new Image("/org/axelxitumul/image/eliminar.png"));
                tipoOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }
    
    public void eliminar(){
        switch(tipoOperacion){
            case GUARDAR:
                limpiarControles();
                desactivarControles();
                btnNuevoPresupuesto.setText("Nuevo");
                btnEliminarPresupuesto.setText("Eliminar");
                btnEditarPresupuesto.setDisable(false);
                btnReportePresupuesto.setDisable(false);
                imgNuevoPresupuesto.setImage(new Image("/org/axelxitumul/image/nuevo.png"));
                imgEliminarPresupuesto.setImage(new Image("/org/axelxitumul/image/eliminar.png"));
                tipoOperacion = operaciones.NINGUNO;
              break;
            default:
                if(tblPresupuestos.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Estas seguro de Eliminar el registro?","Eliminar Empresa",JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_Eliminar_Presupuesto(?)");
                            procedimiento.setInt(1, ((Presupuesto)tblPresupuestos.getSelectionModel().getSelectedItem()).getCodigoPresupuesto());
                            listaPresupuesto.remove(tblPresupuestos.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                            procedimiento.execute();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }
        }
    }
    
    public void editar (){
        switch(tipoOperacion){
            case NINGUNO:
                if(tblPresupuestos.getSelectionModel().getSelectedItem() != null){
                    btnNuevoPresupuesto.setDisable(true);
                    btnEliminarPresupuesto.setDisable(true);
                    btnEditarPresupuesto.setText("Actualizar");
                    btnReportePresupuesto.setText("Cancelar");
                    imgEditarPresupuesto.setImage(new Image("/org/axelxitumul/image/actualizar.png"));
                    imgReportePresupuesto.setImage(new Image("/org/axelxitumul/image/cancelar.png"));
                    activarControles();
                    tipoOperacion = operaciones.ACTUALIZAR;
                }else{
                    JOptionPane.showMessageDialog(null, "Seleccione un elemento de la Tabla");
                }
                break;
            case ACTUALIZAR:
                actualizar();
                limpiarControles();
                desactivarControles();
                btnNuevoPresupuesto.setDisable(false);
                btnEliminarPresupuesto.setDisable(false);
                btnEditarPresupuesto.setText("Editar");
                btnReportePresupuesto.setText("Reporte");
                imgEditarPresupuesto.setImage(new Image("/org/axelxitumul/image/editar.png"));
                imgReportePresupuesto.setImage(new Image("/org/axelxitumul/image/reporte.png"));
                tipoOperacion = operaciones.NINGUNO;
                cargarDatos();
                tblPresupuestos.getSelectionModel().clearSelection();
                break;
        }
    }
    
    public void guardar(){
        Presupuesto registro = new Presupuesto();
        registro.setFechaDeSolicitud(fecha.getSelectedDate());
        registro.setCantidadPresupuesto(Double.parseDouble(txtCantidadDePresupuesto.getText()));
        registro.setCodigoEmpresa(((Empresa)cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_Agregar_Presupuesto(?,?,?)");
            procedimiento.setDate(1, new java.sql.Date(registro.getFechaDeSolicitud().getTime()));
            procedimiento.setDouble(2, registro.getCantidadPresupuesto());
            procedimiento.setInt(3, registro.getCodigoEmpresa());
            procedimiento.execute();
            listaPresupuesto.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void actualizar(){
        if(txtCantidadDePresupuesto.getText().isEmpty()){
            JOptionPane.showMessageDialog(null, "XD");
        }else{
            try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_Editar_Presupuesto(?,?,?)");
            Presupuesto registro = (Presupuesto)tblPresupuestos.getSelectionModel().getSelectedItem();
            registro.setCantidadPresupuesto(Double.parseDouble(txtCantidadDePresupuesto.getText()));
            registro.setFechaDeSolicitud(fecha.getSelectedDate());
            procedimiento.setInt(1, registro.getCodigoPresupuesto());
            procedimiento.setDate(2, new java.sql.Date(registro.getFechaDeSolicitud().getTime()));
            procedimiento.setDouble(3, registro.getCantidadPresupuesto());
            procedimiento.execute();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    
    public void reporte(){
        switch(tipoOperacion){
            case ACTUALIZAR:
                limpiarControles();
                desactivarControles();
                btnNuevoPresupuesto.setDisable(false);
                btnEliminarPresupuesto.setDisable(false);
                btnEditarPresupuesto.setText("Editar");
                btnReportePresupuesto.setText("Reporte");
                imgEditarPresupuesto.setImage(new Image("/org/axelxitumul/image/editar.png"));
                imgReportePresupuesto.setImage(new Image("/org/axelxitumul/image/reporte.png"));
                tipoOperacion = operaciones.NINGUNO;
                tblPresupuestos.getSelectionModel().clearSelection();
                break;
                
            case NINGUNO:
               imprimirReporte();
               break;
        }
    }
    
    public void imprimirReporte(){
        Map parametros = new HashMap();
        int codEmpresa = Integer.valueOf((((Empresa)cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa()));
        parametros.put("codEmpresa", codEmpresa);
        GenerarReporte.mostrarReporte("ReportePresupuesto.jasper", "Reporte Presupuesto", parametros);
    }
    
    public void desactivarControles(){
        txtCantidadDePresupuesto.setEditable(false);
        txtCodigoPresupuesto.setEditable(false);
        cmbCodigoEmpresa.setDisable(true);
        fecha.setDisable(true);
    }
    
    public void activarControles(){
        txtCodigoPresupuesto.setEditable(false);
        txtCantidadDePresupuesto.setEditable(true);
        cmbCodigoEmpresa.setDisable(false);
        fecha.setDisable(false);
    }
    
    public void limpiarControles(){
        txtCantidadDePresupuesto.clear();
        txtCodigoPresupuesto.clear();
        cmbCodigoEmpresa.setValue(null);
        fecha.selectedDateProperty().set(null);
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
