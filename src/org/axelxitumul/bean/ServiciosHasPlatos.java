
package org.axelxitumul.bean;


public class ServiciosHasPlatos {
    private int servicios_CodigoServicio;
    private int codigoPlato;
    private int codigoServicio;

    public ServiciosHasPlatos() {
    }

    public ServiciosHasPlatos(int servicios_CodigoServicio, int codigoPlato, int codigoServicio) {
        this.servicios_CodigoServicio = servicios_CodigoServicio;
        this.codigoPlato = codigoPlato;
        this.codigoServicio = codigoServicio;
    }

    public int getServicios_CodigoServicio() {
        return servicios_CodigoServicio;
    }

    public void setServicios_CodigoServicio(int servicios_CodigoServicio) {
        this.servicios_CodigoServicio = servicios_CodigoServicio;
    }

    public int getCodigoPlato() {
        return codigoPlato;
    }

    public void setCodigoPlato(int codigoPlato) {
        this.codigoPlato = codigoPlato;
    }

    public int getCodigoServicio() {
        return codigoServicio;
    }

    public void setCodigoServicio(int codigoServicio) {
        this.codigoServicio = codigoServicio;
    }
    
    
}
