package org.axelxitumul.bean;

public class TipoEmpleado {
    private int codigoTipoEmpleado;
    private String descripcionTipo;

    public TipoEmpleado() {
    }

    public TipoEmpleado(int codigoTipoEmpleado, String descripcionTipo) {
        this.codigoTipoEmpleado = codigoTipoEmpleado;
        this.descripcionTipo = descripcionTipo;
    }

    public int getCodigoTipoEmpleado() {
        return codigoTipoEmpleado;
    }

    public void setCodigoTipoEmpleado(int codigoTipoEmpleado) {
        this.codigoTipoEmpleado = codigoTipoEmpleado;
    }

    public String getDescripcionTipo() {
        return descripcionTipo;
    }

    public void setDescripcionTipo(String descripcionTipo) {
        this.descripcionTipo = descripcionTipo;
    }
    
    @Override
    public String toString(){
        return codigoTipoEmpleado + "|" + descripcionTipo;
    }
    
}
