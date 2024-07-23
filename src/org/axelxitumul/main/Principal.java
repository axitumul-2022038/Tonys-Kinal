    /*
    Axel Antonio Xitumul Chen
    IN5AM
    2022038
 */
package org.axelxitumul.main;


import java.io.IOException;
import java.io.InputStream;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.axelxitumul.bean.Platos;
import org.axelxitumul.controller.EmpleadoController;
import org.axelxitumul.controller.EmpresaController;
import org.axelxitumul.controller.LoginController;
import org.axelxitumul.controller.MenuPrincipalController;
import org.axelxitumul.controller.PlatosController;
import org.axelxitumul.controller.PresupuestoController;
import org.axelxitumul.controller.ProductosController;
import org.axelxitumul.controller.ProductosHasPlatosController;
import org.axelxitumul.controller.ProgramadorController;
import org.axelxitumul.controller.ServiciosController;
import org.axelxitumul.controller.ServiciosHasPlatosController;
import org.axelxitumul.controller.TipoEmpleadoController;
import org.axelxitumul.controller.TipoPlatoController;
import org.axelxitumul.controller.UsuarioController;

public class Principal extends Application {
    private final String PAQUETE_VISTA = "/org/axelxitumul/view/";
    private Stage escenarioPrincipal;
    private Scene escena;
    
    @Override
    public void start(Stage escenarioPrincipal) throws IOException {
        this.escenarioPrincipal = escenarioPrincipal;
        this.escenarioPrincipal.setTitle("Tony's Kinal 2023");
        escenarioPrincipal.getIcons().add(new Image("/org/axelxitumul/image/vegetariano.png"));
        //Parent root = FXMLLoader.load(getClass().getResource("/org/axelxitumul/view/MenuPrincipalView.fxml"));
        //Scene escena = new Scene(root);
        //escenarioPrincipal.setScene(escena);
        Login();
        escenarioPrincipal.show();
    }
    public void menuPrincipal(){
        try{
            MenuPrincipalController menu = (MenuPrincipalController)cambiarEscena("MenuPrincipalView.fxml",481,471);
            menu.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaProgramador(){
        try{
            ProgramadorController programador = (ProgramadorController)cambiarEscena("ProgramadorView.fxml",600,339);
            programador.setEscenarioPrincipal(this);
        }catch(Exception e){
           e.printStackTrace();
        }
    }
    
    public void ventanaEmpresa(){
        try{
            EmpresaController empresa = (EmpresaController) cambiarEscena("EmpresaView.fxml",803,348);
            empresa.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaPresupuesto(){
        try{
            PresupuestoController presupuesto = (PresupuestoController) cambiarEscena("PresupuestoView.fxml",803,348);
            presupuesto.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaProductos(){
        try{
            ProductosController productos = (ProductosController) cambiarEscena("ProductosView.fxml",803,348);
            productos.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaTipoEmpleado(){
        try{
            TipoEmpleadoController tipoEmpleado = (TipoEmpleadoController) cambiarEscena("TipoEmpleadoView.fxml",803,348);
            tipoEmpleado.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaTipoPlato(){
        try{
            TipoPlatoController tipoPlato = (TipoPlatoController) cambiarEscena("TipoPlatoView.fxml",803,348);
            tipoPlato.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaEmpleados(){
        try {
            EmpleadoController empleado = (EmpleadoController) cambiarEscena("EmpleadosView.fxml", 1002, 348);
            empleado.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaServicios(){
        try {
            ServiciosController servicio = (ServiciosController) cambiarEscena("ServiciosView.fxml", 803, 348);
            servicio.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void Login(){
        try{
            LoginController login = (LoginController) cambiarEscena("LoginView.fxml", 700, 500);
            login.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
        public void ventanaUsuario(){
        try{
            UsuarioController usuario = (UsuarioController) cambiarEscena("UsuarioView.fxml", 700, 500);
            usuario.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
        
        public void ventanaPlato(){
            try {
                PlatosController platos = (PlatosController) cambiarEscena("PlatosView.fxml", 826, 348);
                platos.setEscenarioPrincipal(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        public void ventanaServicioHasPlatos(){
            try {
                ServiciosHasPlatosController serHasPla = (ServiciosHasPlatosController) cambiarEscena("ServiciosHasPlatosView.fxml", 803, 348);
                serHasPla.setEscenarioPrincipal(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        public void ventanaProductoHasPlatos(){
            try {
                ProductosHasPlatosController proHasPla = (ProductosHasPlatosController) cambiarEscena("ProductosHasPlatosView", 803, 348);
                proHasPla.setEscenarioPrincipal(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    
    public static void main(String[] args) {
        launch(args);
    }
    
    public Initializable cambiarEscena(String fxml, int ancho, int alto) throws IOException{
        Initializable resultado = null;
        FXMLLoader cargadorFXML = new FXMLLoader();
        InputStream archivo = Principal.class.getResourceAsStream(PAQUETE_VISTA+fxml);
        cargadorFXML.setBuilderFactory(new JavaFXBuilderFactory());
        cargadorFXML.setLocation(Principal.class.getResource(PAQUETE_VISTA+fxml));
        escena = new Scene((AnchorPane)cargadorFXML.load(archivo),ancho,alto);
        escenarioPrincipal.setScene(escena);
        escenarioPrincipal.sizeToScene();
        resultado = (Initializable)cargadorFXML.getController();
        return resultado;
    }
    
}
