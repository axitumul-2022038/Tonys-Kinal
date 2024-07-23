package org.axelxitumul.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.axelxitumul.bean.Usuario;
import org.axelxitumul.db.Conexion;
import org.axelxitumul.main.Principal;

public class UsuarioController implements Initializable{
    private Principal escenarioPrincipal;
    private enum operaciones{GUARDAR,NINGUNO};
    private operaciones tipoOperacion = operaciones.NINGUNO;
    @FXML private TextField txtCodigoUsuario;
    @FXML private TextField txtNombreUsuario;
    @FXML private TextField txtApellidoUsuario;
    @FXML private TextField txtUsuario;
    @FXML private TextField txtContrasenia;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private ImageView imgNuevoUsuario;
    @FXML private ImageView imgEliminarUsuario;
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
       
    }
    
    public void nuevo(){
        switch(tipoOperacion){
            case NINGUNO:
                limpiarControles();
                activarControles();
                btnNuevo.setText("GUARDAR");
                btnEliminar.setText("CANCELAR");
                imgNuevoUsuario.setImage(new Image("/org/axelxitumul/image/guardar.png"));
                imgEliminarUsuario.setImage(new Image("/org/axelxitumul/image/cancelar.png"));
                tipoOperacion = operaciones.GUARDAR;
                break;
            case GUARDAR:
                guardar();
                limpiarControles();
                desactivarControles();
                btnNuevo.setText("NUEVO");
                btnEliminar.setText("CANCELAR");
                imgNuevoUsuario.setImage(new Image("/org/axelxitumul/image/nuevo.png"));
                imgEliminarUsuario.setImage(new Image("/org/axelxitumul/image/cancelar.png"));
                tipoOperacion = operaciones.NINGUNO;
                login();
                break;
        }
    }
    
    public void eliminar(){
        switch(tipoOperacion){
            case GUARDAR : 
                limpiarControles();
                desactivarControles();
                btnNuevo.setText("NUEVO");
                btnEliminar.setText("CANCELAR");
                imgNuevoUsuario.setImage(new Image("/org/axelxitumul/image/nuevo.png"));
                imgEliminarUsuario.setImage(new Image("/org/axelxitumul/image/cancelar.png"));
                tipoOperacion = operaciones.NINGUNO;
        }
    }
    
    public void guardar(){
        Usuario registro =  new Usuario();
        registro.setNombreUsuario(txtNombreUsuario.getText());
        registro.setApellidoUsuario(txtApellidoUsuario.getText());
        registro.setUsuarioLogin(txtUsuario.getText());
        registro.setContrasena(txtContrasenia.getText());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarUsuario(?,?,?,?)}");
            procedimiento.setString(1, registro.getApellidoUsuario());
            procedimiento.setString(2, registro.getNombreUsuario());
            procedimiento.setString(3, registro.getUsuarioLogin());
            procedimiento.setString(4, registro.getContrasena());
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void desactivarControles(){
        txtCodigoUsuario.setEditable(false);
        txtNombreUsuario.setEditable(false);
        txtApellidoUsuario.setEditable(false);
        txtUsuario.setEditable(false);
        txtContrasenia.setEditable(false);
    }
    
    public void activarControles(){
        txtCodigoUsuario.setEditable(true);
        txtNombreUsuario.setEditable(true);
        txtApellidoUsuario.setEditable(true);
        txtUsuario.setEditable(true);
        txtContrasenia.setEditable(true);
    }
     public void limpiarControles(){
         txtCodigoUsuario.clear();
        txtNombreUsuario.clear();
        txtApellidoUsuario.clear();
        txtUsuario.clear();
        txtContrasenia.clear();
     }
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    public void login(){
        escenarioPrincipal.Login();
    }
    
}
