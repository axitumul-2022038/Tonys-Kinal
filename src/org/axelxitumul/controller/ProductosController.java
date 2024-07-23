
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
import org.axelxitumul.bean.Producto;
import org.axelxitumul.db.Conexion;
import org.axelxitumul.main.Principal;

public class ProductosController implements Initializable {
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private operaciones tipoOperacion = operaciones.NINGUNO;
    private Principal escenarioPrincipal;
    private ObservableList<Producto> listaProducto;
    
    @FXML private TextField txtCodigoProducto;
    @FXML private TextField txtNombreProducto;
    @FXML private TextField txtCantidadProducto;
    @FXML private TableView tblProductos ;
    @FXML private TableColumn colCodigoProducto;
    @FXML private TableColumn colNombreProducto;
    @FXML private TableColumn colCantidadProducto;
    @FXML private Button btnNuevoProducto;
    @FXML private Button btnEliminarProducto;
    @FXML private Button btnEditarProducto;
    @FXML private Button btnReporteProducto;
    @FXML private ImageView imgNuevoProducto;
    @FXML private ImageView imgEliminarProducto;
    @FXML private ImageView imgEditarProducto;
    @FXML private ImageView imgReporteProducto;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }
    
    public void cargarDatos(){
        tblProductos.setItems(getProducto());
        colCodigoProducto.setCellValueFactory(new PropertyValueFactory<Producto, Integer>("codigoProducto"));
        colNombreProducto.setCellValueFactory(new PropertyValueFactory<Producto, String>("nombreProducto"));
        colCantidadProducto.setCellValueFactory(new PropertyValueFactory<Producto, Integer>("cantidad"));
    }
    
    public ObservableList<Producto> getProducto(){
        ArrayList<Producto> lista2 = new ArrayList<Producto>();
        try{
            PreparedStatement procedimiento2 = Conexion.getInstance().getConexion().prepareCall("call sp_Listar_Productos");
            ResultSet resultado2 = procedimiento2.executeQuery();
            while(resultado2.next()){
                lista2.add(new Producto(resultado2.getInt("codigoProducto"),
                                resultado2.getString("nombreProducto"),
                                resultado2.getInt("cantidad")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaProducto = FXCollections.observableArrayList(lista2);
    }
    
    public void nuevo(){
        switch(tipoOperacion){
            case NINGUNO:
                activarControles();
                btnNuevoProducto.setText("Guardar");
                btnEliminarProducto.setText("Cancelar");
                btnEditarProducto.setDisable(true);
                btnReporteProducto.setDisable(true);
                imgNuevoProducto.setImage(new Image("/org/axelxitumul/image/guardar.png"));
                imgEliminarProducto.setImage(new Image("/org/axelxitumul/image/cancelar.png"));
                tipoOperacion = operaciones.GUARDAR;
                break;
            case GUARDAR:
                guardarPro();
                limpiarControles();
                desactivarControles();
                btnNuevoProducto.setText("Nuevo");
                btnEliminarProducto.setText("Eliminar");
                btnEditarProducto.setDisable(false);
                btnReporteProducto.setDisable(false);
                imgNuevoProducto.setImage(new Image("/org/axelxitumul/image/nuevo.png"));
                imgEliminarProducto.setImage(new Image("/org/axelxitumul/image/eliminar.png"));
                tipoOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }
    
    public void seleccionarElemento(){
        if(tblProductos.getSelectionModel().getSelectedItem() != null){
            txtCodigoProducto.setText(String.valueOf(((Producto)tblProductos.getSelectionModel().getSelectedItem()).getCodigoProducto()));
            txtNombreProducto.setText(((Producto)tblProductos.getSelectionModel().getSelectedItem()).getNombreProducto());
            txtCantidadProducto.setText(String.valueOf(((Producto)tblProductos.getSelectionModel().getSelectedItem()).getCantidad()));
        }else{
            JOptionPane.showMessageDialog(null, "SELECCIONE UN DATO CORRECTO (NO VACIO)");
        }
    }
    public void eliminar(){
        switch(tipoOperacion){
            case GUARDAR:
                limpiarControles();
                desactivarControles();
                btnNuevoProducto.setText("Nuevo");
                btnEliminarProducto.setText("Eliminar");
                btnEditarProducto.setDisable(false);
                btnReporteProducto.setDisable(false);
                imgNuevoProducto.setImage(new Image("/org/axelxitumul/image/nuevo.png"));
                imgEliminarProducto.setImage(new Image("/org/axelxitumul/image/eliminar.png"));
                tipoOperacion = operaciones.NINGUNO;
                break;
            default:
                if(tblProductos.getSelectionModel().getSelectedItem() != null){
                   int respuesta1 = JOptionPane.showConfirmDialog(null, "¿Estas seguro de Eliminar el registro?","Eliminar Empresa",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                        if(respuesta1 == JOptionPane.YES_OPTION){
                            try{
                                PreparedStatement procedimiento1 = Conexion.getInstance().getConexion().prepareCall("call sp_Eliminar_Productos(?)");
                                procedimiento1.setInt(1,((Producto)tblProductos.getSelectionModel().getSelectedItem()).getCodigoProducto());
                                procedimiento1.execute();
                                listaProducto.remove(tblProductos.getSelectionModel().getSelectedIndex());
                                limpiarControles();
                                tblProductos.getSelectionModel().clearSelection();
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                        }if(respuesta1 == JOptionPane.NO_OPTION){
                                tblProductos.getSelectionModel().clearSelection();
                                limpiarControles();
                            }
                        
                }else{
                    JOptionPane.showMessageDialog(null, "Seleccione un elemento para borrar");
                }
        }
    }
    public void guardarPro(){
        if(txtNombreProducto.getText().isEmpty()|| txtCantidadProducto.getText().isEmpty()){
            JOptionPane.showMessageDialog(null, "Este formulario contiene espacios obligatorios");
        }else{
        Producto registro1 = new Producto();
        registro1.setNombreProducto(txtNombreProducto.getText());
        registro1.setCantidad(Integer.parseInt(txtCantidadProducto.getText()));
        try{
            PreparedStatement procedimiento1 = Conexion.getInstance().getConexion().prepareCall("call sp_Agregar_Productos(?,?)");
            procedimiento1.setString(1, registro1.getNombreProducto());
            procedimiento1.setInt(2, registro1.getCantidad());
            procedimiento1.execute();
            listaProducto.add(registro1);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    }
    
    public void editar(){
        switch(tipoOperacion){
            case NINGUNO:
                if(tblProductos.getSelectionModel().getSelectedItem() != null){
                    btnNuevoProducto.setDisable(true);
                    btnEliminarProducto.setDisable(true);
                    btnEditarProducto.setText("Actualizar");
                    btnReporteProducto.setText("Cancelar");
                    imgEditarProducto.setImage(new Image("/org/axelxitumul/image/actualizar.png"));
                    imgReporteProducto.setImage(new Image("/org/axelxitumul/image/cancelar.png"));
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
                btnNuevoProducto.setDisable(false);
                btnEliminarProducto.setDisable(false);
                btnEditarProducto.setText("Editar");
                btnReporteProducto.setText("Reporte");
                imgEditarProducto.setImage(new Image("/org/axelxitumul/image/editar.png"));
                imgReporteProducto.setImage(new Image("/org/axelxitumul/image/reporte.png"));
                tipoOperacion = operaciones.NINGUNO;
                cargarDatos();
                tblProductos.getSelectionModel().clearSelection();
                break;
        }
    }
    
    public void actualizar(){
        if(txtNombreProducto.getText().isEmpty()|| txtCantidadProducto.getText().isEmpty()){
            JOptionPane.showMessageDialog(null, "Este formulario contiene espacios obligatorios");
        }else{
        try{
            PreparedStatement procedimiento1 = Conexion.getInstance().getConexion().prepareCall("call sp_Editar_Productos(?,?,?)");
            Producto registro1 = (Producto)tblProductos.getSelectionModel().getSelectedItem();
            registro1.setNombreProducto(txtNombreProducto.getText());
            registro1.setCantidad(Integer.parseInt(txtCantidadProducto.getText()));
            procedimiento1.setInt(1, registro1.getCodigoProducto());
            procedimiento1.setString(2, registro1.getNombreProducto());
            procedimiento1.setInt(3, registro1.getCantidad());
            procedimiento1.execute();
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
                btnEditarProducto.setText("Editar");
                btnReporteProducto.setText("Reporte");
                btnNuevoProducto.setDisable(false);
                btnEliminarProducto.setDisable(false);
                imgEditarProducto.setImage(new Image("/org/axelxitumul/image/editar.png"));
                imgReporteProducto.setImage(new Image("/org/axelxitumul/image/reporte.png"));
                tipoOperacion = operaciones.NINGUNO;
                tblProductos.getSelectionModel().clearSelection();
                break;
        }
    }
    
    public void desactivarControles(){
        txtCodigoProducto.setEditable(false);
        txtNombreProducto.setEditable(false);
        txtCantidadProducto.setEditable(false);
    }
    
    public void activarControles(){
        txtCodigoProducto.setEditable(false);
        txtNombreProducto.setEditable(true);
        txtCantidadProducto.setEditable(true);
    }
    
    public void limpiarControles(){
        txtCodigoProducto.clear();
        txtNombreProducto.clear();
        txtCantidadProducto.clear();
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
}
