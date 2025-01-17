package org.axelxitumul.bean;

import java.util.Date;

public class Servicios {
   private int codigoServicio;
   private Date fechaServicio;
   private String tipoDeServicio;
   private String horaDeServicio;
   private String lugarServicio;
   private String telefonoContacto;
   private int codigoEmpresa;

    public Servicios() {
    }

    public Servicios(int codigoServicio, Date fechaServicio, String tipoDeServicio, String horaDeServicio, String lugarServicio, String telefonoContacto, int codigoEmpresa) {
        this.codigoServicio = codigoServicio;
        this.fechaServicio = fechaServicio;
        this.tipoDeServicio = tipoDeServicio;
        this.horaDeServicio = horaDeServicio;
        this.lugarServicio = lugarServicio;
        this.telefonoContacto = telefonoContacto;
        this.codigoEmpresa = codigoEmpresa;
    }

    public int getCodigoServicio() {
        return codigoServicio;
    }

    public void setCodigoServicio(int codigoServicio) {
        this.codigoServicio = codigoServicio;
    }

    public Date getFechaServicio() {
        return fechaServicio;
    }

    public void setFechaServicio(Date fechaServicio) {
        this.fechaServicio = fechaServicio;
    }

    public String getTipoDeServicio() {
        return tipoDeServicio;
    }

    public void setTipoDeServicio(String tipoDeServicio) {
        this.tipoDeServicio = tipoDeServicio;
    }

    public String getHoraDeServicio() {
        return horaDeServicio;
    }

    public void setHoraDeServicio(String horaDeServicio) {
        this.horaDeServicio = horaDeServicio;
    }

    public String getLugarServicio() {
        return lugarServicio;
    }

    public void setLugarServicio(String lugarServicio) {
        this.lugarServicio = lugarServicio;
    }

    public String getTelefonoContacto() {
        return telefonoContacto;
    }

    public void setTelefonoContacto(String telefonoContacto) {
        this.telefonoContacto = telefonoContacto;
    }

    public int getCodigoEmpresa() {
        return codigoEmpresa;
    }

    public void setCodigoEmpresa(int codigoEmpresa) {
        this.codigoEmpresa = codigoEmpresa;
    }
   
   
   @Override
    public String toString() {
        return codigoServicio + "|" + tipoDeServicio + "|" + codigoEmpresa;
    }
   
   
}
