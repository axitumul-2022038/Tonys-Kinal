
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
import org.axelxitumul.bean.TipoPlato;
import org.axelxitumul.db.Conexion;
import org.axelxitumul.main.Principal;

public class TipoPlatoController implements Initializable{
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private operaciones tipoOperacion = operaciones.NINGUNO;
    private Principal escenarioPrincipal;
    private ObservableList<TipoPlato> listaTipoPlato;
    
    @FXML private TextField txtCodigoTipoPlato;
    @FXML private TextField txtDescripcionPlato;
    @FXML private TableView tblTipoPlato;
    @FXML private TableColumn colCodigoTipoPlato;
    @FXML private TableColumn colDescripcionPlato;
    @FXML private Button btnNuevoTipoPlato;
    @FXML private Button btnEliminarTipoPlato;
    @FXML private Button btnEditarTipoPlato;
    @FXML private Button btnReporteTipoPlato;
    @FXML private ImageView imgNuevoTipoPlato;
    @FXML private ImageView imgEliminarTipoPlato;
    @FXML private ImageView imgEditarTipoPlato;
    @FXML private ImageView imgReporteTipoPlato;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }
    
    public void cargarDatos(){
            tblTipoPlato.setItems(getTipoPlato());
            colCodigoTipoPlato.setCellValueFactory(new PropertyValueFactory<TipoPlato, Integer>("codigoTipoPlato"));
            colDescripcionPlato.setCellValueFactory(new PropertyValueFactory<TipoPlato, String>("descripcionPlato"));
    }
    
    public ObservableList<TipoPlato> getTipoPlato(){
        ArrayList<TipoPlato> lista3 = new ArrayList<TipoPlato>();
        try{
            PreparedStatement procedimiento3 = Conexion.getInstance().getConexion().prepareCall("call sp_Listar_TipoPlato");
            ResultSet resultado3 = procedimiento3.executeQuery();
            while(resultado3.next()){
                lista3.add(new TipoPlato(resultado3.getInt("codigoTipoPlato"),
                                            resultado3.getString("descripcionPlato")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaTipoPlato = FXCollections.observableArrayList(lista3);
    }
    
    public void nuevo(){
        switch(tipoOperacion){
            case NINGUNO:
                activarControles();
                btnNuevoTipoPlato.setText("Guardar");
                btnEliminarTipoPlato.setText("Cancelar");
                btnEditarTipoPlato.setDisable(true);
                btnReporteTipoPlato.setDisable(true);
                imgNuevoTipoPlato.setImage(new Image("/org/axelxitumul/image/guardar.png"));
                imgEliminarTipoPlato.setImage(new Image("/org/axelxitumul/image/cancelar.png"));
                tipoOperacion = operaciones.GUARDAR;
                break;
            case GUARDAR:
                guardar();
                limpiarControles();
                desactivarControles();
                btnNuevoTipoPlato.setText("Nuevo");
                btnEliminarTipoPlato.setText("Eliminar");
                btnEditarTipoPlato.setDisable(false);
                btnReporteTipoPlato.setDisable(false);
                imgNuevoTipoPlato.setImage(new Image("/org/axelxitumul/image/nuevo.png"));
                imgEliminarTipoPlato.setImage(new Image("/org/axelxitumul/image/eliminar.png"));
                tipoOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }
    
    public void seleccionarElemento(){
        if(tblTipoPlato.getSelectionModel().getSelectedItem() != null){
            txtCodigoTipoPlato.setText(String.valueOf(((TipoPlato)tblTipoPlato.getSelectionModel().getSelectedItem()).getCodigoTipoPlato()));
            txtDescripcionPlato.setText(((TipoPlato)tblTipoPlato.getSelectionModel().getSelectedItem()).getDescripcionPlato());
        }else{
            JOptionPane.showMessageDialog(null, "Seleccione un dato correcto (no vacio)");
         }
    }
    
    public void eliminar(){
        switch(tipoOperacion){
            case GUARDAR:
                limpiarControles();
                desactivarControles();
                btnNuevoTipoPlato.setText("Nuevo");
                btnEliminarTipoPlato.setText("Eliminar");
                btnEditarTipoPlato.setDisable(false);
                btnReporteTipoPlato.setDisable(false);
                imgNuevoTipoPlato.setImage(new Image("/org/axelxitumul/image/nuevo.png"));
                imgEliminarTipoPlato.setImage(new Image("/org/axelxitumul/image/eliminar.png"));
                tipoOperacion = operaciones.NINGUNO;
                break;
            default:
                if(tblTipoPlato.getSelectionModel().getSelectedItem() != null){
                    int respuesta2 = JOptionPane.showConfirmDialog(null, "Estas seguro de Eliminar el registro","Eliminar Tipo Plato",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta2 == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento2 = Conexion.getInstance().getConexion().prepareCall("call sp_Eliminar_TipoPlato(?)");
                            procedimiento2.setInt(1, ((TipoPlato)tblTipoPlato.getSelectionModel().getSelectedItem()).getCodigoTipoPlato());
                            procedimiento2.execute();
                            listaTipoPlato.remove(tblTipoPlato.getSelectionModel().getSelectedItem());
                            limpiarControles();
                            tblTipoPlato.getSelectionModel().clearSelection();
                        }catch(Exception e){
                            e.printStackTrace();
                        } 
                    }
                    if(respuesta2 == JOptionPane.NO_OPTION){
                                tblTipoPlato.getSelectionModel().clearSelection();
                                limpiarControles();
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "Seleccione un elemento para borrar");
                }
        }
    }
    
    public void guardar(){
        if(txtDescripcionPlato.getText().isEmpty()){
            JOptionPane.showMessageDialog(null, "Este formulario contiene espacios obligatorios");
        }else{
        TipoPlato registro2 = new TipoPlato();
        registro2.setDescripcionPlato(txtDescripcionPlato.getText());
        try{
            PreparedStatement procedimiento2 = Conexion.getInstance().getConexion().prepareCall("call sp_Agrega_TipoPlato(?)");
            procedimiento2.setString(1, registro2.getDescripcionPlato());
            procedimiento2.execute();
            listaTipoPlato.add(registro2);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    }
    public void editar(){
        switch(tipoOperacion){
            case NINGUNO:
                if(tblTipoPlato.getSelectionModel().getSelectedItem() != null){
                    btnNuevoTipoPlato.setDisable(true);
                    btnEliminarTipoPlato.setDisable(true);
                    btnEditarTipoPlato.setText("Actualizar");
                    btnReporteTipoPlato.setText("Cancelar");
                    imgEditarTipoPlato.setImage(new Image("/org/axelxitumul/image/actualizar.png"));
                    imgReporteTipoPlato.setImage(new Image("/org/axelxitumul/image/cancelar.png"));
                    activarControles();
                    tipoOperacion = operaciones.ACTUALIZAR;
        }else {
                    JOptionPane.showConfirmDialog(null, "seleccione un elemento para Editar");
                }
                break;
                
            case ACTUALIZAR : 
                actualizar();
                limpiarControles();
                desactivarControles();
                btnNuevoTipoPlato.setDisable(false);
                btnEliminarTipoPlato.setDisable(false);
                btnEditarTipoPlato.setText("Editar");
                btnReporteTipoPlato.setText("Reporte");
                imgEditarTipoPlato.setImage(new Image("/org/axelxitumul/image/editar.png"));
                imgReporteTipoPlato.setImage(new Image("/org/axelxitumul/image/reporte.png"));
                tipoOperacion = operaciones.NINGUNO;
                cargarDatos();
                tblTipoPlato.getSelectionModel().clearSelection();
                break;
    }
    }
    
    public void actualizar(){
        if(txtDescripcionPlato.getText().isEmpty()){
            JOptionPane.showMessageDialog(null, "Este formulario contiene espacios obligatorios");
        }else{
        try{
            PreparedStatement procedimiento2 = Conexion.getInstance().getConexion().prepareCall("call sp_Editar_TipoPlato(?,?)");
            TipoPlato registro2 = (TipoPlato)tblTipoPlato.getSelectionModel().getSelectedItem();
            registro2.setDescripcionPlato(txtDescripcionPlato.getText());
            procedimiento2.setInt(1, registro2.getCodigoTipoPlato());
            procedimiento2.setString(2, registro2.getDescripcionPlato());
            procedimiento2.execute();
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
                btnEditarTipoPlato.setText("Editar");
                btnReporteTipoPlato.setText("Reporte");
                btnNuevoTipoPlato.setDisable(false);
                btnEliminarTipoPlato.setDisable(false);
                imgEditarTipoPlato.setImage(new Image("/org/axelxitumul/image/editar.png"));
                imgReporteTipoPlato.setImage(new Image("/org/axelxitumul/image/reporte.png"));
                tipoOperacion = operaciones.NINGUNO;
                tblTipoPlato.getSelectionModel().clearSelection();
                break;
        }
    }
    
    public void desactivarControles(){
        txtCodigoTipoPlato.setEditable(false);
        txtDescripcionPlato.setEditable(false);
    }
    
    public void activarControles(){
        txtCodigoTipoPlato.setEditable(false);
        txtDescripcionPlato.setEditable(true);
    }
    
    public void limpiarControles(){
        txtCodigoTipoPlato.clear();
        txtDescripcionPlato.clear();
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
    
     public void ventanaPlato(){
        escenarioPrincipal.ventanaPlato();
    }
}
