
package org.axelxitumul.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.axelxitumul.bean.TipoEmpleado;
import org.axelxitumul.db.Conexion;
import org.axelxitumul.main.Principal;


public class TipoEmpleadoController implements Initializable{
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private operaciones tipoOperacion = operaciones.NINGUNO;
    private Principal escenarioPrincipal;
    private ObservableList<TipoEmpleado> listaTipoEmpleado;
    
    @FXML private TextField txtCodigoTipoEmpleado;
    @FXML private TextField txtDescripcionTipo;
    @FXML private TableView tblTipoEmpleados;
    @FXML private TableColumn colCodigoTipoEmpleado;
    @FXML private TableColumn colDescripcionTipo;
    @FXML private Button btnNuevoTipoEmpleado;
    @FXML private Button btnEliminarTipoEmpleado;
    @FXML private Button btnEditarTipoEmpleado;
    @FXML private Button btnReporteTipoEmpleado;
    @FXML private ImageView imgNuevoTipoEmpleado;
    @FXML private ImageView imgEliminarTipoEmpleado;
    @FXML private ImageView imgEditarTipoEmpleado;
    @FXML private ImageView imgReporteTipoEmpleado;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }
    
    public void cargarDatos(){
        tblTipoEmpleados.setItems(getTipoEmpleado());
        colCodigoTipoEmpleado.setCellValueFactory(new PropertyValueFactory<TipoEmpleado, Integer>("codigoTipoEmpleado"));
        colDescripcionTipo.setCellValueFactory(new PropertyValueFactory<TipoEmpleado, String>("descripcionTipo"));
    }
    
    
    public ObservableList<TipoEmpleado> getTipoEmpleado(){
        ArrayList<TipoEmpleado> lista4 = new ArrayList<TipoEmpleado>();
        try{
            PreparedStatement procedimiento4 = Conexion.getInstance().getConexion().prepareCall("call sp_Listar_TipoEmpleado");
            ResultSet resultado4 = procedimiento4.executeQuery();
            while(resultado4.next()){
                lista4.add(new TipoEmpleado(resultado4.getInt("codigoTipoEmpleado"),
                                                resultado4.getString("descripcionTipo")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaTipoEmpleado = FXCollections.observableArrayList(lista4);
    }
    
    public void nuevo(){
        switch(tipoOperacion){
            case NINGUNO:
                activarControles();
                btnNuevoTipoEmpleado.setText("Guardar");
                btnEliminarTipoEmpleado.setText("Cancelar");
                btnEditarTipoEmpleado.setDisable(true);
                btnReporteTipoEmpleado.setDisable(true);
                imgNuevoTipoEmpleado.setImage(new Image("/org/axelxitumul/image/guardar.png"));
                imgEliminarTipoEmpleado.setImage(new Image("/org/axelxitumul/image/cancelar.png"));
                tipoOperacion = operaciones.GUARDAR;
                break;
            case GUARDAR:
                guardar();
                limpiarControles();
                desactivarControles();
                btnNuevoTipoEmpleado.setText("Nuevo");
                btnEliminarTipoEmpleado.setText("Eliminar");
                btnEditarTipoEmpleado.setDisable(false);
                btnReporteTipoEmpleado.setDisable(false);
                imgNuevoTipoEmpleado.setImage(new Image("/org/axelxitumul/image/nuevo.png"));
                imgEliminarTipoEmpleado.setImage(new Image("/org/axelxitumul/image/eliminar.png"));
                tipoOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }
        
    public void seleccionarElemento(){
        if(tblTipoEmpleados.getSelectionModel().getSelectedItem() != null){
            txtCodigoTipoEmpleado.setText(String.valueOf(((TipoEmpleado)tblTipoEmpleados.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado()));
            txtDescripcionTipo.setText(((TipoEmpleado)tblTipoEmpleados.getSelectionModel().getSelectedItem()).getDescripcionTipo());
        }else{
            JOptionPane.showMessageDialog(null, "SELECCIONE UN DATO CORRECTO (NO VACIO)");
        }
    }
    
    public void eliminar(){
        switch(tipoOperacion){
            case GUARDAR:
                limpiarControles();
                desactivarControles();
                btnNuevoTipoEmpleado.setText("Nuevo");
                btnEliminarTipoEmpleado.setText("Eliminar");
                btnEditarTipoEmpleado.setDisable(false);
                btnReporteTipoEmpleado.setDisable(false);
                imgNuevoTipoEmpleado.setImage(new Image("/org/axelxitumul/image/nuevo.png"));
                imgEliminarTipoEmpleado.setImage(new Image("/org/axelxitumul/image/eliminar.png"));
                tipoOperacion = operaciones.NINGUNO;
                break;
            default:
                if(tblTipoEmpleados.getSelectionModel().getSelectedItem() != null){
                   int respuesta3 = JOptionPane.showConfirmDialog(null, "¿Estas seguro de Eliminar el registro?","Eliminar Empresa",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                        if(respuesta3 == JOptionPane.YES_OPTION){
                            try{
                                PreparedStatement procedimiento1 = Conexion.getInstance().getConexion().prepareCall("call sp_Eliminar_TipoEmpleado(?)");
                                procedimiento1.setInt(1,((TipoEmpleado)tblTipoEmpleados.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado());
                                procedimiento1.execute();
                                listaTipoEmpleado.remove(tblTipoEmpleados.getSelectionModel().getSelectedIndex());
                                limpiarControles();
                                tblTipoEmpleados.getSelectionModel().clearSelection();
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                            
                        }
                        if(respuesta3 == JOptionPane.NO_OPTION){
                                tblTipoEmpleados.getSelectionModel().clearSelection();
                                limpiarControles();
                        }
                }else{
                    JOptionPane.showMessageDialog(null, "Seleccione un elemento para borrar");
                }
        }
    }
    
    public void guardar(){
        if(txtDescripcionTipo.getText().isEmpty()){
            JOptionPane.showMessageDialog(null, "Este formulario contiene espacios obligatorios");
        }else{
        TipoEmpleado registro1 = new TipoEmpleado();
        registro1.setDescripcionTipo(txtDescripcionTipo.getText());
        try{
            PreparedStatement procedimiento1 = Conexion.getInstance().getConexion().prepareCall("call sp_Agregar_TipoEmpleado(?)");
            procedimiento1.setString(1, registro1.getDescripcionTipo());
            procedimiento1.execute();
            listaTipoEmpleado.add(registro1);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    }
    
    public void editar(){
        switch(tipoOperacion){
            case NINGUNO:
                if(tblTipoEmpleados.getSelectionModel().getSelectedItem() != null){
                    btnNuevoTipoEmpleado.setDisable(true);
                    btnEliminarTipoEmpleado.setDisable(true);
                    btnEditarTipoEmpleado.setText("Actualizar");
                    btnReporteTipoEmpleado.setText("Cancelar");
                    imgEditarTipoEmpleado.setImage(new Image("/org/axelxitumul/image/actualizar.png"));
                    imgReporteTipoEmpleado.setImage(new Image("/org/axelxitumul/image/cancelar.png"));
                    activarControles();
                    tipoOperacion = operaciones.ACTUALIZAR;
                }else{
                    JOptionPane.showMessageDialog(null, "Seleccione un elemento para Editar");
                }
                    break;
            case ACTUALIZAR : 
                actualizar();
                limpiarControles();
                desactivarControles();
                btnNuevoTipoEmpleado.setDisable(false);
                btnEliminarTipoEmpleado.setDisable(false);
                btnEditarTipoEmpleado.setText("Editar");
                btnReporteTipoEmpleado.setText("Reporte");
                imgEditarTipoEmpleado.setImage(new Image("/org/axelxitumul/image/editar.png"));
                imgReporteTipoEmpleado.setImage(new Image("/org/axelxitumul/image/reporte.png"));
                tipoOperacion = operaciones.NINGUNO;
                cargarDatos();
                tblTipoEmpleados.getSelectionModel().clearSelection();
                break;
        }
    }
    
    public void actualizar(){
        if(txtDescripcionTipo.getText().isEmpty()){
            JOptionPane.showMessageDialog(null, "Este formulario contiene espacios obligatorios");
        }else{
        try{
            PreparedStatement procedimiento3 = Conexion.getInstance().getConexion().prepareCall("call sp_Editar_TipoEmpleado(?,?)");
            TipoEmpleado registro3 = (TipoEmpleado)tblTipoEmpleados.getSelectionModel().getSelectedItem();
            registro3.setDescripcionTipo(txtDescripcionTipo.getText());
            procedimiento3.setInt(1, registro3.getCodigoTipoEmpleado());
            procedimiento3.setString(2, registro3.getDescripcionTipo());
            procedimiento3.execute();
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
                btnEditarTipoEmpleado.setText("Editar");
                btnReporteTipoEmpleado.setText("Reporte");
                btnNuevoTipoEmpleado.setDisable(false);
                btnEliminarTipoEmpleado.setDisable(false);
                imgEditarTipoEmpleado.setImage(new Image("/org/axelxitumul/image/editar.png"));
                imgReporteTipoEmpleado.setImage(new Image("/org/axelxitumul/image/reporte.png"));
                tipoOperacion = operaciones.NINGUNO;
                tblTipoEmpleados.getSelectionModel().clearSelection();
                break;
        }
    }
    
    public void desactivarControles(){
        txtCodigoTipoEmpleado.setEditable(false);
        txtDescripcionTipo.setEditable(false);
    }
    
    public void activarControles(){
        txtCodigoTipoEmpleado.setEditable(false);
        txtDescripcionTipo.setEditable(true);
    }
    
    public void limpiarControles(){
        txtCodigoTipoEmpleado.clear();
        txtDescripcionTipo.clear();
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
    
    public void ventanaEmpleados(){
        escenarioPrincipal.ventanaEmpleados();
    }
    
}