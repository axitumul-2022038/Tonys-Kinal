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
import org.axelxitumul.bean.Platos;
import org.axelxitumul.bean.TipoPlato;
import org.axelxitumul.db.Conexion;
import org.axelxitumul.main.Principal;

public class PlatosController implements Initializable{
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private operaciones tipoOperacion = operaciones.NINGUNO;
    private Principal escenarioPrincipal;
    private ObservableList<Platos> listaPlatos;
    private ObservableList<TipoPlato> listaTipoPlatos;
    
    @FXML private TextField txtCodigoPlato;
    @FXML private TextField txtCantidadPlatos;
    @FXML private TextField txtNombrePlato;
    @FXML private TextField txtDescripcionPlato;
    @FXML private TextField txtPrecioPlato;
    @FXML private ComboBox cmbCodigoTipoPlato;
    @FXML private TableView tblPlatos;
    @FXML private TableColumn colCodigoPlato;
    @FXML private TableColumn colCantidadPlatos;
    @FXML private TableColumn colNombrePlato;
    @FXML private TableColumn colDescripcionPlato;  
    @FXML private TableColumn colPrecioPlato;
    @FXML private TableColumn colCodigoTipoPlato;
    @FXML private Button btnNuevoPlato;
    @FXML private Button btnEliminarPlato;
    @FXML private Button btnEditarPlato;
    @FXML private Button btnReportePlato;
    @FXML private ImageView imgNuevoPlato;
    @FXML private ImageView imgEliminarPlato;
    @FXML private ImageView imgEditarPlato;
    @FXML private ImageView imgReportePlato;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
       cmbCodigoTipoPlato.setItems(getTipoPlatos());
        cmbCodigoTipoPlato.setDisable(true);
    }
    
    public void cargarDatos(){
        tblPlatos.setItems(getPlatos());
        colCodigoPlato.setCellValueFactory(new PropertyValueFactory<Platos, Integer>("codigoPlato"));
        colCantidadPlatos.setCellValueFactory(new PropertyValueFactory<Platos, Integer>("cantidadPlatos"));
        colNombrePlato.setCellValueFactory(new PropertyValueFactory<Platos, String>("nombrePlato"));
        colDescripcionPlato.setCellValueFactory(new PropertyValueFactory<Platos,String>("descripcionPlato"));
        colPrecioPlato.setCellValueFactory(new PropertyValueFactory<Platos,Double>("precioPlato"));
        colCodigoTipoPlato.setCellValueFactory(new PropertyValueFactory<Platos, Integer>("codigoTipoPlato"));
    }
    
    public void seleccionarElemento(){
        txtCodigoPlato.setText(String.valueOf(((Platos)tblPlatos.getSelectionModel().getSelectedItem()).getCodigoPlato()));
        txtCantidadPlatos.setText(String.valueOf(((Platos)tblPlatos.getSelectionModel().getSelectedItem()).getCantidadPlatos()));
        txtNombrePlato.setText(((Platos)tblPlatos.getSelectionModel().getSelectedItem()).getNombrePlato());
        txtDescripcionPlato.setText(((Platos)tblPlatos.getSelectionModel().getSelectedItem()).getDescripcionPlato());
        txtPrecioPlato.setText(String.valueOf(((Platos)tblPlatos.getSelectionModel().getSelectedItem()).getPrecioPlato()));
        cmbCodigoTipoPlato.getSelectionModel().select(buscarTipoPlato(((Platos)tblPlatos.getSelectionModel().getSelectedItem()).getCodigoTipoPlato()));
    }
    
    public TipoPlato buscarTipoPlato(int codigoTipoPlato){
        TipoPlato resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_Buscar_Platos(?)");
                    procedimiento.setInt(1, codigoTipoPlato);
                    ResultSet registro = procedimiento.executeQuery();
                    while(registro.next()){
                        resultado = new TipoPlato(registro.getInt("codigoTipoPlato"),
                                                    registro.getString("descripcionPlato"));
                    }
        } catch (Exception e) {
        }
        return resultado;
   }
    
    public ObservableList<Platos>getPlatos(){
        ArrayList<Platos> lista8 = new ArrayList<Platos>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_Listar_Platos()");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista8.add(new Platos(resultado.getInt("codigoPlato"),
                                        resultado.getInt("cantidadPlatos"),
                                        resultado.getString("nombrePlato"),
                                        resultado.getString("descripcionPlato"),
                                        resultado.getDouble("precioPlato"),
                                        resultado.getInt("codigoTipoPlato")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaPlatos = FXCollections.observableArrayList(lista8);
    }
    
    public ObservableList<TipoPlato> getTipoPlatos(){
        ArrayList<TipoPlato> lista9 = new ArrayList<TipoPlato>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_Listar_TipoPlato()");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista9.add(new TipoPlato(resultado.getInt("codigoTipoPlato"),
                                            resultado.getString("descripcionPlato")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaTipoPlatos = FXCollections.observableArrayList(lista9);
    }
    
    public void  nuevo(){
        switch(tipoOperacion){
            case NINGUNO:
                activarControles();
                btnNuevoPlato.setText("Guardar");
                btnEliminarPlato.setText("Cancelar");
                btnEditarPlato.setDisable(true);
                btnReportePlato.setDisable(true);
                imgNuevoPlato.setImage(new Image("/org/axelxitumul/image/guardar.png"));
                imgEliminarPlato.setImage(new Image("/org/axelxitumul/image/cancelar.png"));
                tipoOperacion = operaciones.GUARDAR;
                break;
            case GUARDAR:
                guardar();
                limpiarControles();
                desactivarControles();
                btnNuevoPlato.setText("Nuevo");
                btnEliminarPlato.setText("Eliminar");
                btnEditarPlato.setDisable(false);
                btnReportePlato.setDisable(false);
                imgNuevoPlato.setImage(new Image("/org/axelxitumul/image/nuevo.png"));
                imgEliminarPlato.setImage(new Image("/org/axelxitumul/image/eliminar.png"));
                tipoOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }
    
    public void guardar(){
        Platos registro = new Platos();
        registro.setCantidadPlatos(Integer.parseInt(txtCantidadPlatos.getText()));
        registro.setNombrePlato(txtNombrePlato.getText());
        registro.setDescripcionPlato(txtDescripcionPlato.getText());
        registro.setPrecioPlato(Double.parseDouble(txtPrecioPlato.getText()));
        registro.setCodigoTipoPlato(((TipoPlato)cmbCodigoTipoPlato.getSelectionModel().getSelectedItem()).getCodigoTipoPlato());
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_Agregar_Plato(?,?,?,?,?)");
            procedimiento.setInt(1, registro.getCantidadPlatos());
            procedimiento.setString(2, registro.getNombrePlato());
            procedimiento.setString(3, registro.getDescripcionPlato());
            procedimiento.setDouble(4, registro.getPrecioPlato());
            procedimiento.setInt(5, registro.getCodigoTipoPlato());
            procedimiento.execute();
            listaPlatos.add(registro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void eliminar(){
        switch(tipoOperacion){
            case GUARDAR:
                limpiarControles();
                desactivarControles();
                btnNuevoPlato.setText("Nuevo");
                btnEliminarPlato.setText("Eliminar");
                btnEditarPlato.setDisable(false);
                btnReportePlato.setDisable(false);
                imgNuevoPlato.setImage(new Image("/org/axelxitumul/image/nuevo.png"));
                imgEliminarPlato.setImage(new Image("/org/axelxitumul/image/eliminar.png"));
                tipoOperacion = operaciones.NINGUNO;
                break;
            default:
                if(tblPlatos.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Estas seguro de eliminar el registro?", "Eliminar Plato", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try {
                          PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_Eliminar_Plato(?)");
                          procedimiento.setInt(1, ((Platos)tblPlatos.getSelectionModel().getSelectedItem()).getCodigoPlato());
                          listaPlatos.remove(tblPlatos.getSelectionModel().getSelectedIndex());
                          limpiarControles();
                          procedimiento.execute();
                        } catch(SQLException e){
                            JOptionPane.showMessageDialog(null, "No se puede Borrar porque esta relacionado con otros datos");
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
                if(tblPlatos.getSelectionModel().getSelectedItem() != null){
                    btnNuevoPlato.setDisable(true);
                    btnEliminarPlato.setDisable(true);
                    btnEditarPlato.setText("Actualizar");
                    btnReportePlato.setText("Cancelar");
                    imgEditarPlato.setImage(new Image("/org/axelxitumul/image/actualizar.png"));
                    imgReportePlato.setImage(new Image("/org/axelxitumul/image/cancelar.png"));
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
                btnNuevoPlato.setDisable(false);
                btnEliminarPlato.setDisable(false);
                btnEditarPlato.setText("Editar");
                btnReportePlato.setText("Reporte");
                imgEditarPlato.setImage(new Image("/org/axelxitumul/image/editar.png"));
                imgReportePlato.setImage(new Image("/org/axelxitumul/image/reporte.png"));
                tipoOperacion = operaciones.NINGUNO;
                cargarDatos();
                tblPlatos.getSelectionModel().clearSelection();
                break;
        }
    }
    
    public void actualizar(){
        if(txtCantidadPlatos.getText().isEmpty() || txtNombrePlato.getText().isEmpty() || txtDescripcionPlato.getText().isEmpty() || txtPrecioPlato.getText().isEmpty()){
            JOptionPane.showMessageDialog(null, "LLene todos los espacios en blanco");
        }else{
            try {
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_Editar_Plato(?,?,?,?,?)");
                Platos registro = (Platos)tblPlatos.getSelectionModel().getSelectedItem();
                registro.setCantidadPlatos(Integer.parseInt(txtCantidadPlatos.getText()));
                registro.setNombrePlato(txtNombrePlato.getText());
                registro.setDescripcionPlato(txtDescripcionPlato.getText());
                registro.setPrecioPlato(Double.parseDouble(txtPrecioPlato.getText()));
                procedimiento.setInt(1, registro.getCodigoPlato());
                procedimiento.setInt(2, registro.getCantidadPlatos());
                procedimiento.setString(3, registro.getNombrePlato());
                procedimiento.setString(4, registro.getDescripcionPlato());
                procedimiento.setDouble(5, registro.getPrecioPlato());
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
                btnNuevoPlato.setDisable(false);
                btnEliminarPlato.setDisable(false);
                btnEditarPlato.setText("Editar");
                btnReportePlato.setText("Reporte");
                imgEditarPlato.setImage(new Image("/org/axelxitumul/image/editar.png"));
                imgReportePlato.setImage(new Image("/org/axelxitumul/image/reporte.png"));
                tipoOperacion = operaciones.NINGUNO;
                tblPlatos.getSelectionModel().clearSelection();
        }
    }
    
    
    public void activarControles(){
        txtCodigoPlato.setEditable(true);
        txtCantidadPlatos.setEditable(true);
        txtNombrePlato.setEditable(true);
        txtDescripcionPlato.setEditable(true);
        txtPrecioPlato.setEditable(true);
        cmbCodigoTipoPlato.setDisable(false);
    }
    public void desactivarControles(){
        txtCodigoPlato.setEditable(false);
        txtCantidadPlatos.setEditable(false);
        txtNombrePlato.setEditable(false);
        txtDescripcionPlato.setEditable(false);
        txtPrecioPlato.setEditable(false);
        cmbCodigoTipoPlato.setDisable(true);
    }
    
    public void limpiarControles(){
        txtCodigoPlato.clear();
        txtCantidadPlatos.clear();
        txtNombrePlato.clear();
        txtDescripcionPlato.clear();
        txtPrecioPlato.clear();
        cmbCodigoTipoPlato.setValue(null);
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
    
    public void ventanaTipoPlato(){
        escenarioPrincipal.ventanaTipoPlato();
    }
}
