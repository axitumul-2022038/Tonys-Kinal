
package org.axelxitumul.controller;

import java.awt.Toolkit;
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
import org.axelxitumul.bean.Servicios;
import org.axelxitumul.bean.ServiciosHasPlatos;
import org.axelxitumul.db.Conexion;
import org.axelxitumul.main.Principal;


public class ServiciosHasPlatosController implements Initializable{
    private Principal escenarioPrincipal;
    private enum operaciones {NUEVO, GUARDAR,CANCELAR, NINGUNO};
    private operaciones tipoOperacion = operaciones.NINGUNO;
    private ObservableList<ServiciosHasPlatos> listaServiciohasPlato;
    private ObservableList<Platos> listaPlato;
    private ObservableList<Servicios> listaServicio;
    
    private @FXML TextField txtServiciosCodigoServicio;
    private @FXML ComboBox cmbCodigoPlato;
    private @FXML  ComboBox cmbCodigoServicio;
    private @FXML Button btnNuevo;
    private @FXML Button btnReporte;
    private @FXML TableView tblServicioshasPlatos;
    private @FXML TableColumn colServiciosCodigoServicio;
    private @FXML TableColumn colCodigoPlato;
    private @FXML TableColumn colCodigoServicio;
    private @FXML ImageView imgNuevo;
    private @FXML ImageView imgReporte;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCodigoPlato.setItems(getPlato());
        cmbCodigoServicio.setItems(getServicio());
        btnNuevo.setDisable(false);
        btnReporte.setDisable(false);
    }
    
    public void cargarDatos() {
        try {
            tblServicioshasPlatos.setItems(getServicioHasPlatos());
            colServiciosCodigoServicio.setCellValueFactory(new PropertyValueFactory<ServiciosHasPlatos, Integer>("servicios_CodigoServicio"));
            colCodigoPlato.setCellValueFactory(new PropertyValueFactory<ServiciosHasPlatos, Integer>("codigoPlato"));
            colCodigoServicio.setCellValueFactory(new PropertyValueFactory<ServiciosHasPlatos, Integer>("codigoServicio"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void seleccionarElemento() {
        if (tblServicioshasPlatos.getSelectionModel().getSelectedItem() != null) {
            txtServiciosCodigoServicio.setText(String.valueOf(((ServiciosHasPlatos) tblServicioshasPlatos.getSelectionModel().getSelectedItem()).getServicios_CodigoServicio()));
            cmbCodigoPlato.getSelectionModel().select(buscarPlato(((ServiciosHasPlatos) tblServicioshasPlatos.getSelectionModel().getSelectedItem()).getCodigoPlato()));
            cmbCodigoServicio.getSelectionModel().select(buscarServicio(((ServiciosHasPlatos) tblServicioshasPlatos.getSelectionModel().getSelectedItem()).getCodigoServicio()));
        } else {
            JOptionPane.showMessageDialog(null, "Selecciona un campo que tenga datos.");
        }
    }

    
    public ObservableList<ServiciosHasPlatos> getServicioHasPlatos() {
        ArrayList<ServiciosHasPlatos> lista = new ArrayList<ServiciosHasPlatos>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_Listar_Servicios_has_Platos()");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new ServiciosHasPlatos(resultado.getInt("servicios_CodigoServicio"),
                        resultado.getInt("codigoPlato"),
                        resultado.getInt("codigoServicio")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaServiciohasPlato = FXCollections.observableArrayList(lista);
    }
    
    public ObservableList<Platos> getPlato() {
        ArrayList<Platos> lista = new ArrayList<Platos>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_Listar_Platos()");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Platos(resultado.getInt("codigoPlato"),
                        resultado.getInt("cantidadPlatos"),
                        resultado.getString("nombrePlato"),
                        resultado.getString("descripcionPlato"),
                        resultado.getDouble("precioPlato"),
                        resultado.getInt("codigoTipoPlato")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaPlato = FXCollections.observableArrayList(lista);
    }
    
    public ObservableList<Servicios> getServicio() {
        ArrayList<Servicios> lista = new ArrayList<Servicios>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_Listar_Servicios()");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
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
        return listaServicio = FXCollections.observableArrayList(lista);
    }
    
    public Platos buscarPlato(int codigoPlato) {
        Platos resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_Buscar_Platos(?)");
            procedimiento.setInt(1, codigoPlato);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado = new Platos(registro.getInt("codigoPlato"),
                        registro.getInt("cantidadPlatos"),
                        registro.getString("nombrePlato"),
                        registro.getString("descripcionPlato"),
                        registro.getDouble("precioPlato"),
                        registro.getInt("codigoTipoPlato"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }
    
    public Servicios buscarServicio(int codigoServicio) {
        Servicios resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_Buscar_Servicio(?)");
            procedimiento.setInt(1, codigoServicio);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado = new Servicios(registro.getInt("codigoServicio"),
                        registro.getDate("fechaServicio"),
                        registro.getString("tipoDeServicio"),
                        registro.getString("horaDeServicio"),
                        registro.getString("lugarServicio"),
                        registro.getString("telefonoContacto"),
                        registro.getInt("codigoEmpresa"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }
    
    public void nuevo() {
        switch (tipoOperacion) {
            case NINGUNO:
                activarControles();
                btnNuevo.setText("Guardar");
                btnReporte.setText("Cancelar");
                imgNuevo.setImage(new Image("/org/axelxitumul/image/guardar.png"));
                imgReporte.setImage(new Image("/org/axelxitumul/image/cancelar.png"));
                tipoOperacion = operaciones.GUARDAR;
                break;
            case GUARDAR:
                guardar();
                limpiarControles();
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnReporte.setText("Reporte");
                imgNuevo.setImage(new Image("/org/axelxitumul/image/nuevo.png"));
                imgReporte.setImage(new Image("/org/axelxitumul/image/reporte.png"));
                tipoOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }

 public void guardar() {
        String codSerHasServi = txtServiciosCodigoServicio.getText();
        codSerHasServi = codSerHasServi.replaceAll(" ", "");
        if (codSerHasServi.length() == 0) {
            JOptionPane.showMessageDialog(null, "Debe colocar el código Servicio Codigo Servicio");
        } else {
            if (cmbCodigoPlato.getSelectionModel().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Debe seleccionar el código del plato");
            } else {
                if (cmbCodigoServicio.getSelectionModel().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar el código del servicio");
                } else {
                    try {
                        ServiciosHasPlatos registro = new ServiciosHasPlatos();
                        registro.setServicios_CodigoServicio(Integer.parseInt(txtServiciosCodigoServicio.getText()));
                        registro.setCodigoPlato(((Platos) cmbCodigoPlato.getSelectionModel().getSelectedItem()).getCodigoPlato());
                        registro.setCodigoServicio(((Servicios) cmbCodigoServicio.getSelectionModel().getSelectedItem()).getCodigoServicio());
                        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_Agregar_Servicios_has_Platos(?,?,?)");
                        procedimiento.setInt(1, registro.getServicios_CodigoServicio());
                        procedimiento.setInt(2, registro.getCodigoPlato());
                        procedimiento.setInt(3, registro.getCodigoServicio());
                        procedimiento.execute();
                        listaServiciohasPlato.add(registro);
                    } catch (SQLException e) {
                       JOptionPane.showMessageDialog(null, "No se puede duplicar");
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    
    public void reporte(){
        switch(tipoOperacion){
            case GUARDAR:
                limpiarControles();
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnReporte.setText("Reporte");
                imgNuevo.setImage(new Image("/org/axelxitumul/image/nuevo.png"));
                imgReporte.setImage(new Image("/org/axelxitumul/image/reporte.png"));
                tipoOperacion = operaciones.NINGUNO;
                break;
        }
    }
    
    public void deseleccionar() {
        tblServicioshasPlatos.getSelectionModel().clearSelection();
        limpiarControles();
    }
    
    public void desactivarControles() {
        txtServiciosCodigoServicio.setEditable(false);
        cmbCodigoPlato.setDisable(true);
        cmbCodigoServicio.setDisable(true);
    }

    public void activarControles() {
        txtServiciosCodigoServicio.setEditable(true);
        cmbCodigoPlato.setDisable(false);
        cmbCodigoServicio.setDisable(false);
    }

    public void limpiarControles() {
        txtServiciosCodigoServicio.clear();
        cmbCodigoPlato.setValue(null);
        cmbCodigoServicio.setValue(null);
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
