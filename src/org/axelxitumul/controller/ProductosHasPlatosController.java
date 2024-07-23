
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.axelxitumul.bean.Platos;
import org.axelxitumul.bean.Producto;
import org.axelxitumul.bean.ProductosHasPlatos;
import org.axelxitumul.db.Conexion;
import org.axelxitumul.main.Principal;


public class ProductosHasPlatosController implements Initializable{
    private enum operaciones {GUARDAR,NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private Principal escenarioPrincipal;
    private ObservableList<ProductosHasPlatos>listaProductosHasPlatos;
    private ObservableList<Platos>listaPlato;
    private ObservableList<Producto>listaProducto;
    
    @FXML private TextField txtProductosCodigoProducto;
    @FXML private ComboBox cmbCodigoPlatos;
    @FXML private ComboBox cmbCodigoProducto;
    @FXML private TableView tblProductosHasPlatos;
    @FXML private TableColumn colProductosCodigoProducto;
    @FXML private TableColumn colCodigoPlato;
    @FXML private TableColumn colCodigoProducto;
    @FXML private Button btnNuevo;
    @FXML private Button btnReporte;
    @FXML private ImageView imgNuevo;
    @FXML private ImageView imgReporte;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCodigoPlatos.setItems(getPlato());
        cmbCodigoProducto.setItems(getProducto());
    }
    
    public void cargarDatos(){
        tblProductosHasPlatos.setItems(getProductosHasPlatos());
        colProductosCodigoProducto.setCellValueFactory(new PropertyValueFactory <ProductosHasPlatos, Integer>("productos_codigoProducto"));
        colCodigoPlato.setCellValueFactory(new PropertyValueFactory <ProductosHasPlatos, Integer>("codigoPlato"));
        colCodigoProducto.setCellValueFactory(new PropertyValueFactory<ProductosHasPlatos, Integer>("codigoProducto"));
    }
    
    public void seleccionarElemento(){
        if(tblProductosHasPlatos.getSelectionModel().getSelectedItem() != null){
            txtProductosCodigoProducto.setText(String.valueOf(((ProductosHasPlatos)tblProductosHasPlatos.getSelectionModel().getSelectedItem()).getProductos_codigoProducto()));
            cmbCodigoPlatos.getSelectionModel().select(buscarPlato(((ProductosHasPlatos)tblProductosHasPlatos.getSelectionModel().getSelectedItem()).getCodigoPlato()));
            cmbCodigoProducto.getSelectionModel().select(buscarProducto(((ProductosHasPlatos)tblProductosHasPlatos.getSelectionModel().getSelectedItem()).getCodigoProducto()));
        }else{
            JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
        }
    }
    
    public Platos buscarPlato(int codigoPlato){
        Platos resultado =  null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_Buscar_Platos(?)");
            procedimiento.setInt(1, codigoPlato);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()){
                resultado = new Platos (registro.getInt("codigoPlato"),
                        registro.getInt("cantidadPlatos"),
                        registro.getString("nombrePlato"),
                        registro.getString("descripcionPlato"),
                        registro.getDouble("precioPlato"),
                        registro.getInt("codigoTipoPlato"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    
    public Producto buscarProducto(int codigoProducto){
        Producto resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_Buscar_Productos(?)");
            procedimiento.setInt(1, codigoProducto);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Producto (registro.getInt("codigoProducto"),
                        registro.getString("nombreProducto"),
                        registro.getInt("cantidad"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }

    public ObservableList<ProductosHasPlatos> getProductosHasPlatos(){
        ArrayList<ProductosHasPlatos> lista =  new ArrayList<ProductosHasPlatos>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_Listar_Productos_has_Platos()");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new ProductosHasPlatos(resultado.getInt("Productos_codigoProducto"),
                        resultado.getInt("codigoPlato"),
                        resultado.getInt("codigoProducto")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaProductosHasPlatos = FXCollections.observableArrayList(lista);
    }
    
    public ObservableList<Platos> getPlato(){
        ArrayList<Platos> lista = new ArrayList<Platos>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_Listar_Platos()");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Platos(resultado.getInt("codigoPlato"),
                        resultado.getInt("cantidadPlatos"),
                        resultado.getString("nombrePlato"),
                        resultado.getString("descripcionPlato"),
                        resultado.getDouble("precioPlato"),
                        resultado.getInt("codigoTipoPlato")));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return listaPlato = FXCollections.observableArrayList(lista);
    }
    
    public ObservableList <Producto> getProducto(){
        ArrayList<Producto> lista = new ArrayList<Producto>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareStatement("call sp_Listar_Productos()");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()){
                lista.add(new Producto(resultado.getInt("codigoProducto"),
                       resultado.getString("nombreProducto"),
                       resultado.getInt("cantidad")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }      
        return listaProducto = FXCollections.observableArrayList(lista);
    }
    
    public void nuevo(){
        switch(tipoDeOperacion){
            case NINGUNO:
                activarControles();
                btnNuevo.setText("Guardar");
                btnReporte.setText("Cancelar");
                imgNuevo.setImage(new Image ("/org/axelxitumul/image/guardar.png"));
                imgReporte.setImage(new Image ("/org/axelxitumul/image/cancelar.png"));
                tipoDeOperacion = operaciones.GUARDAR;
                break;
            case GUARDAR:
                guardar();
                limpiarControles();
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnReporte.setText("Reporte");
                imgNuevo.setImage(new Image ("/org/axelxitumul/image/nuevo.png"));
                imgReporte.setImage(new Image ("/org/axelxitumul/image/reporte.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }
    
    public void reporte(){
        switch(tipoDeOperacion){
            case GUARDAR:
                limpiarControles();
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnReporte.setText("Reporte");
                imgNuevo.setImage(new Image ("/org/axelxitumul/image/nuevo.png"));
                imgReporte.setImage(new Image("/org/axelxitumul/image/reporte.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                break;
        }
    }
    
    public void guardar(){
        ProductosHasPlatos registro = new ProductosHasPlatos();
        if(txtProductosCodigoProducto.getText().isEmpty()|| cmbCodigoPlatos.getSelectionModel() == null||cmbCodigoProducto.getSelectionModel() == null){
            JOptionPane.showMessageDialog(null, "Faltan datos");
        }else{
            registro.setProductos_codigoProducto(Integer.parseInt(txtProductosCodigoProducto.getText()));
            registro.setCodigoPlato(((Platos)cmbCodigoPlatos.getSelectionModel().getSelectedItem()).getCodigoPlato());
            registro.setCodigoProducto(((Producto)cmbCodigoProducto.getSelectionModel().getSelectedItem()).getCodigoProducto());
            try{
               PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_Agregar_Productos_has_Platos(?,?,?)");
               procedimiento.setInt(1, registro.getProductos_codigoProducto());
               procedimiento.setInt(2, registro.getCodigoPlato());
               procedimiento.setInt(3, registro.getCodigoProducto());
               procedimiento.execute();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    
    public void desactivarControles(){
        txtProductosCodigoProducto.setEditable(false);
        cmbCodigoPlatos.setDisable(true);
        cmbCodigoProducto.setDisable(true);
    }
    
    public void activarControles(){
        txtProductosCodigoProducto.setEditable(true);
        cmbCodigoPlatos.setDisable(false);
        cmbCodigoProducto.setDisable(false);
    }
    
    public void limpiarControles(){
        txtProductosCodigoProducto.clear();
        cmbCodigoPlatos.setValue(null);
        cmbCodigoProducto.setValue(null);
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
