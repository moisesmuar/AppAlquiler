package com.example.appalquiler.Clases;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;

public class Alquiler implements Serializable {

    @SerializedName("idAlquiler")
    private int idAlquiler;

    @SerializedName("fhinicio")
    private String fhinicio;

    @SerializedName("fhfin")
    private String   fhfin;

    @SerializedName("inmueble")
    private Inmueble inmueble;
    @SerializedName("cliente")
    private Cliente cliente;
    @SerializedName("portal")
    private Portal portal;
    @SerializedName("empresa")
    private Empresa empresa;

    public Alquiler(){}

    public Alquiler( String fhinicio, String fhfin,
                     Inmueble inmueble, Cliente cliente,
                     Portal portal, Empresa empresa) {

        this.fhinicio = fhinicio;
        this.fhfin = fhfin;
        this.inmueble = inmueble;
        this.cliente = cliente;
        this.portal = portal;
        this.empresa = empresa;
    }

    public int getIdAlquiler() {
        return idAlquiler;
    }

    public void setIdAlquiler(int idAlquiler) {
        this.idAlquiler = idAlquiler;
    }

    public String getFhinicio() {
        return fhinicio;
    }

    public void setFhinicio(String fhinicio) {
        this.fhinicio = fhinicio;
    }

    public String getFhfin() {
        return fhfin;
    }

    public void setFhfin(String fhfin) {
        this.fhfin = fhfin;
    }

    public Inmueble getInmueble() {
        return inmueble;
    }

    public void setInmueble(Inmueble inmueble) {
        this.inmueble = inmueble;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Portal getPortal() {
        return portal;
    }

    public void setPortal(Portal portal) {
        this.portal = portal;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }
}
