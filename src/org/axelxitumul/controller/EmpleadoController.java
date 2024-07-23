package org.axelxitumul.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
import javax.swing.JOptionPane;
import org.axelxitumul.bean.Empleados;
import org.axelxitumul.bean.Presupuesto;
import org.axelxitumul.bean.TipoEmpleado;
import org.axelxitumul.db.Conexion;
import org.axelxitumul.main.Principal;


public class EmpleadoController implements Initializable {
    private Principal escenarioPrincipal;
    private enum operaciones{GUARDAR,ELIMINAR,ACTUALIZAR,NINGUNO};
    private operaciones tipoOperacion = operaciones.NINGUNO;
    private ObservableList<Empleados> listaEmpleados;
    private ObservableList<TipoEmpleado> listaTipoEmpleados;
    
    @FXML private TextField txtCodigoEmpleado;
    @FXML private TextField txtNumeroEmpleado;
    @FXML private TextField txtApellidos;
    @FXML private TextField txtNombres;
    @FXML private TextField txtDireccion;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtGradoCocinero;
    @FXML private ComboBox cmbCodigoTipoEmpleado;
    @FXML private TableView tblEmpleados;
    @FXML private TableColumn colCodigoEmpleado;
    @FXML private TableColumn colNumeroEmpleado;
    @FXML private TableColumn colApellidos;
    @FXML private TableColumn colNombres;
    @FXML private TableColumn colDireccion;
    @FXML private TableColumn colTelefono;
    @FXML private TableColumn colGradoCocinero;
    @FXML private TableColumn colCodTipoEmpleado;
    @FXML private Button btnNuevoEmpleado;
    @FXML private Button btnEliminarEmpleado;
    @FXML private Button btnEditarEmpleado;
    @FXML private Button btnReporteEmpleado;
    @FXML private ImageView imgNuevoEmpleado;
    @FXML private ImageView imgEliminarEmpleado;
    @FXML private ImageView imgEditarEmpleado;
    @FXML private ImageView imgReporteEmpleado;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCodigoTipoEmpleado.setItems(getTipoEmpleado());
        cmbCodigoTipoEmpleado.setDisable(true);
    }
    
    public void cargarDatos(){
        tblEmpleados.setItems(getEmpleados());
        colCodigoEmpleado.setCellValueFactory(new PropertyValueFactory<Empleados, Integer>("codigoEmpleado"));
        colNumeroEmpleado.setCellValueFactory(new PropertyValueFactory<Empleados, Integer>("numeroEmpleado"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<Empleados, String>("apellidosEmpleado"));
        colNombres.setCellValueFactory(new PropertyValueFactory<Empleados, String>("nombresEmpleado"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<Empleados, String>("direccionEmpleado"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<Empleados,String>("telefonoEmpleado"));
        colGradoCocinero.setCellValueFactory(new PropertyValueFactory<Empleados, String>("gradoCocinero"));
        colCodTipoEmpleado.setCellValueFactory((new PropertyValueFactory<Empleados, Integer>("codigoTipoEmpleado")));
    }
    
    public void seleccionarElemento(){
        txtCodigoEmpleado.setText(String.valueOf(((Empleados)tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoEmpleado()));
        txtNumeroEmpleado.setText(String.valueOf(((Empleados)tblEmpleados.getSelectionModel().getSelectedItem()).getNumeroEmpleado()));
        txtApellidos.setText(((Empleados)tblEmpleados.getSelectionModel().getSelectedItem()).getApellidosEmpleado());
        txtNombres.setText(((Empleados)tblEmpleados.getSelectionModel().getSelectedItem()).getNombresEmpleado());
        txtDireccion.setText(((Empleados)tblEmpleados.getSelectionModel().getSelectedItem()).getDireccionEmpleado());
        txtTelefono.setText(((Empleados)tblEmpleados.getSelectionModel().getSelectedItem()).getTelefonoEmpleado());
        txtGradoCocinero.setText(((Empleados)tblEmpleados.getSelectionModel().getSelectedItem()).getGradoCocinero());
        cmbCodigoTipoEmpleado.getSelectionModel().select(buscarTipoEmpleado(((Empleados)tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado()));
    }
    
    public TipoEmpleado buscarTipoEmpleado(int codigoTipoEmpleado){
        TipoEmpleado resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_Buscar_TipoEmpleado(?)");
            procedimiento.setInt(1, codigoTipoEmpleado);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new TipoEmpleado(registro.getInt("codigoTipoEmpleado"),
                                             registro.getString("descripcionTipo"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return resultado;
    }
    
    public ObservableList<TipoEmpleado> getTipoEmpleado(){
        ArrayList<TipoEmpleado> lista7 = new ArrayList<TipoEmpleado>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_Listar_TipoEmpleado()");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()){
                lista7.add(new TipoEmpleado(resultado.getInt("codigoTipoEmpleado"),
                                            resultado.getString("descripcionTipo")));
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaTipoEmpleados = FXCollections.observableArrayList(lista7);
    }
    
    public ObservableList<Empleados> getEmpleados(){
        ArrayList<Empleados> lista = new ArrayList<Empleados>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_Listar_Empleado()");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Empleados(resultado.getInt("codigoEmpleado"),
                                         resultado.getInt("numeroEmpleado"),
                                        resultado.getString("apellidosEmpleado"),
                                        resultado.getString("nombresEmpleado"),
                                        resultado.getString("direccionEmpleado"),
                                        resultado.getString("telefonoEmpleado"),
                                        resultado.getString("gradoCocinero"),
                                        resultado.getInt("codigoTipoEmpelado")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaEmpleados = FXCollections.observableArrayList(lista);
    }
    
    public void nuevo(){
        switch(tipoOperacion){
            case NINGUNO:
                activarControles();
                btnNuevoEmpleado.setText("Guardar");
                btnEliminarEmpleado.setText("Cancelar");
                btnEditarEmpleado.setDisable(true);
                btnReporteEmpleado.setDisable(true);
                imgNuevoEmpleado.setImage(new Image("/org/axelxitumul/image/guardar.png"));
                imgEliminarEmpleado.setImage(new Image("/org/axelxitumul/image/cancelar.png"));
                tipoOperacion = operaciones.GUARDAR;
                break;
            case GUARDAR:
                guardar();
                limpiarControles();
                desactivarControles();
                btnNuevoEmpleado.setText("Nuevo");
                btnEliminarEmpleado.setText("Eliminar");
                btnEditarEmpleado.setDisable(false);
                btnReporteEmpleado.setDisable(false);
                imgNuevoEmpleado.setImage(new Image("/org/axelxitumul/image/nuevo.png"));
                imgEliminarEmpleado.setImage(new Image("/org/axelxitumul/image/eliminar.png"));
                tipoOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }
    
    public void guardar(){
        Empleados registro = new Empleados();
        registro.setNumeroEmpleado(Integer.parseInt(txtNumeroEmpleado.getText()));
        registro.setApellidosEmpleado(txtApellidos.getText());
        registro.setNombresEmpleado(txtNombres.getText());
        registro.setDireccionEmpleado(txtDireccion.getText());
        registro.setTelefonoEmpleado(txtTelefono.getText());
        registro.setGradoCocinero(txtGradoCocinero.getText());
        registro.setCodigoTipoEmpleado(((TipoEmpleado)cmbCodigoTipoEmpleado.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado());
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_Agregar_Empleado(?,?,?,?,?,?,?)");
            procedimiento.setInt(1, registro.getNumeroEmpleado());
            procedimiento.setString(2, registro.getApellidosEmpleado());
            procedimiento.setString(3, registro.getNombresEmpleado());
            procedimiento.setString(4, registro.getDireccionEmpleado());
            procedimiento.setString(5, registro.getTelefonoEmpleado());
            procedimiento.setString(6, registro.getGradoCocinero());
            procedimiento.setInt(7, registro.getCodigoTipoEmpleado());
            procedimiento.execute();
            listaEmpleados.add(registro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void eliminar(){
        switch(tipoOperacion){
            case GUARDAR:
                limpiarControles();
                desactivarControles();
                btnNuevoEmpleado.setText("Nuevo");
                btnEliminarEmpleado.setText("Eliminar");
                btnEditarEmpleado.setDisable(false);
                btnReporteEmpleado.setDisable(false);
                imgNuevoEmpleado.setImage(new Image("/org/axelxitumul/image/nuevo.png"));
                imgEliminarEmpleado.setImage(new Image("/org/axelxitumul/image/eliminar.png"));
                tipoOperacion = operaciones.NINGUNO;
            break;
            default:
                if(tblEmpleados.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Estas Seguro de eliminar el registro?","Eliminar Empleado",JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_Eliminar_Empleado(?)");
                            procedimiento.setInt(1, ((Empleados)tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoEmpleado());
                            listaEmpleados.remove(tblEmpleados.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                            procedimiento.execute(); 
                        } catch(SQLException e){
                            JOptionPane.showMessageDialog(null, "No se puede borrar porque esta conectado a otros datos");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                            
                }
        }
    }
    
    public void editar(){
        switch(tipoOperacion){
            case NINGUNO:
                if(tblEmpleados.getSelectionModel().getSelectedItem() != null){
                btnNuevoEmpleado.setDisable(true);
                btnEliminarEmpleado.setDisable(true);
                btnEditarEmpleado.setText("Actulizar");
                btnReporteEmpleado.setText("Cancelar");
                imgEditarEmpleado.setImage(new Image("/org/axelxitumul/image/actualizar.png"));
                imgReporteEmpleado.setImage(new Image("/org/axelxitumul/image/cancelar.png"));
                activarControles();
                tipoOperacion = operaciones.ACTUALIZAR;
            }else{
                    JOptionPane.showMessageDialog(null, "Seleccione un elemento de la tabla");
                }
                break;
            case ACTUALIZAR:
                actualizar();
                limpiarControles();
                desactivarControles();
                btnNuevoEmpleado.setDisable(false);
                btnEliminarEmpleado.setDisable(false);
                btnEditarEmpleado.setText("Editar");
                btnReporteEmpleado.setText("Reporte");
                imgEditarEmpleado.setImage(new Image("/org/axelxitumul/image/editar.png"));
                imgReporteEmpleado.setImage(new Image("/org/axelxitumul/image/reporte.png"));
                tipoOperacion = operaciones.NINGUNO;
                cargarDatos();
                tblEmpleados.getSelectionModel().clearSelection();
                break;
        }
    }
    
    public void actualizar(){
        if(txtNumeroEmpleado.getText().isEmpty() || txtApellidos.getText().isEmpty() || txtNombres.getText().isEmpty() || txtDireccion.getText().isEmpty() || txtTelefono.getText().isEmpty() || txtGradoCocinero.getText().isEmpty()){
            JOptionPane.showMessageDialog(null, "Llene todos los espacios en blanco");
            
        }else{
            try {
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_Editar_Empleado(?,?,?,?,?,?,?)");
                Empleados registro = (Empleados)tblEmpleados.getSelectionModel().getSelectedItem();
                registro.setNumeroEmpleado(Integer.parseInt(txtNumeroEmpleado.getText()));
                registro.setApellidosEmpleado(txtApellidos.getText());
                registro.setNombresEmpleado(txtNombres.getText());
                registro.setDireccionEmpleado(txtDireccion.getText());
                registro.setTelefonoEmpleado(txtTelefono.getText());
                registro.setGradoCocinero(txtGradoCocinero.getText());
                procedimiento.setInt(1, registro.getCodigoEmpleado());
                procedimiento.setInt(2, registro.getNumeroEmpleado());
                procedimiento.setString(3, registro.getApellidosEmpleado());
                procedimiento.setString(4, registro.getNombresEmpleado());
                procedimiento.setString(5, registro.getDireccionEmpleado());
                procedimiento.setString(6, registro.getTelefonoEmpleado());
                procedimiento.setString(7, registro.getGradoCocinero());
                procedimiento.execute();
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public void reporte(){
        switch(tipoOperacion){
            case ACTUALIZAR:
                limpiarControles();
                desactivarControles();
                btnNuevoEmpleado.setDisable(false);
                btnEliminarEmpleado.setDisable(false);
                btnEditarEmpleado.setText("Editar");
                btnReporteEmpleado.setText("Reporte");
                imgEditarEmpleado.setImage(new Image("/org/axelxitumul/image/editar.png"));
                imgReporteEmpleado.setImage(new Image("/org/axelxitumul/image/reporte.png"));
                tipoOperacion = operaciones.NINGUNO;
                tblEmpleados.getSelectionModel().clearSelection();
        }
    }
    
     public void desactivarControles(){
        txtCodigoEmpleado.setEditable(false);
        txtNumeroEmpleado.setEditable(false);
        txtApellidos.setEditable(false);
        txtNombres.setEditable(false);
        txtDireccion.setEditable(false);
        txtTelefono.setEditable(false);
        txtGradoCocinero.setEditable(false);
        cmbCodigoTipoEmpleado.setDisable(true);
    }
    
    public void activarControles(){
        txtCodigoEmpleado.setEditable(true);
        txtNumeroEmpleado.setEditable(true);
        txtApellidos.setEditable(true);
        txtNombres.setEditable(true);
        txtDireccion.setEditable(true);
        txtTelefono.setEditable(true);
        txtGradoCocinero.setEditable(true);
        cmbCodigoTipoEmpleado.setDisable(false);
    }
    
    public void limpiarControles(){
        txtCodigoEmpleado.clear();
        txtNumeroEmpleado.clear();
        txtApellidos.clear();
        txtNombres.clear();
        txtDireccion.clear();
        txtTelefono.clear();
        txtGradoCocinero.clear();
        cmbCodigoTipoEmpleado.setValue(null);
    }
    
    public Principal getEscenarioPrincipal(){
        return escenarioPrincipal;
    }
    
    public void setEscenarioPrincipal(Principal escenarioPrincipal){
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
    
    public void ventanaTipoEmpleado(){
        escenarioPrincipal.ventanaTipoEmpleado();
    }
}
