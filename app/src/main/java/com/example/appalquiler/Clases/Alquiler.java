package com.example.appalquiler.Clases;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Alquiler implements Serializable {

    @SerializedName("idAlquiler")
    private int idAlquiler;
    /*@SerializedName("dias")
    private int dias;
    @SerializedName("precioDia")
    private double precioDia;*/
    @SerializedName("fhinicio")
    private String fhinicio;
    @SerializedName("fhfin")
    private String fhfin;
    @SerializedName("inmueble")
    private Inmueble inmueble;
    @SerializedName("cliente")
    private Cliente cliente;

    public Alquiler( /*int dias, double precioDia,*/ String fhinicio, String fhfin, Inmueble inmueble, Cliente cliente) {
       /* this.dias = dias;
        this.precioDia = precioDia;*/
        this.fhinicio = fhinicio;
        this.fhfin = fhfin;
        this.inmueble = inmueble;
        this.cliente = cliente;
    }

    @Override
    public String toString() {
        return "Alquiler{" +
                "idAlquiler=" + idAlquiler +
                ", fhinicio='" + fhinicio + '\'' +
                ", fhfin='" + fhfin + '\'' +
                ", inmueble=" + inmueble +
                ", cliente=" + cliente +
                '}';
    }

    public int getIdAlquiler() {
        return idAlquiler;
    }

    public void setIdAlquiler(int idAlquiler) {
        this.idAlquiler = idAlquiler;
    }
/*
    public int getDias() {
        return dias;
    }

    public void setDias(int dias) {
        this.dias = dias;
    }

    public double getPrecioDia() {
        return precioDia;
    }

    public void setPrecioDia(double precioDia) {
        this.precioDia = precioDia;
    }
*/
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
}
