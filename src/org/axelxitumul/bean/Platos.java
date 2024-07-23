package org.axelxitumul.bean;


public class Platos {
    private int codigoPlato;
    private int cantidadPlatos;
    private String nombrePlato;
    private String descripcionPlato;
    private double precioPlato;
    private int codigoTipoPlato;

    public Platos() {
    }

    public Platos(int codigoPlato, int cantidadPlatos, String nombrePlato, String descripcionPlato, double precioPlato, int codigoTipoPlato) {
        this.codigoPlato = codigoPlato;
        this.cantidadPlatos = cantidadPlatos;
        this.nombrePlato = nombrePlato;
        this.descripcionPlato = descripcionPlato;
        this.precioPlato = precioPlato;
        this.codigoTipoPlato = codigoTipoPlato;
    }

    public int getCodigoPlato() {
        return codigoPlato;
    }

    public void setCodigoPlato(int codigoPlato) {
        this.codigoPlato = codigoPlato;
    }

    public int getCantidadPlatos() {
        return cantidadPlatos;
    }

    public void setCantidadPlatos(int cantidadPlatos) {
        this.cantidadPlatos = cantidadPlatos;
    }

    public String getNombrePlato() {
        return nombrePlato;
    }

    public void setNombrePlato(String nombrePlato) {
        this.nombrePlato = nombrePlato;
    }

    public String getDescripcionPlato() {
        return descripcionPlato;
    }

    public void setDescripcionPlato(String descripcionPlato) {
        this.descripcionPlato = descripcionPlato;
    }

    public double getPrecioPlato() {
        return precioPlato;
    }

    public void setPrecioPlato(double precioPlato) {
        this.precioPlato = precioPlato;
    }

    public int getCodigoTipoPlato() {
        return codigoTipoPlato;
    }

    public void setCodigoTipoPlato(int codigoTipoPlato) {
        this.codigoTipoPlato = codigoTipoPlato;
    }
    
    @Override
    public String toString() {
        return codigoPlato + "|" + nombrePlato + "|" + precioPlato;
    }
    
}
