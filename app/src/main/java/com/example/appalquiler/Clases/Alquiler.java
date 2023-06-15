package com.example.appalquiler.Clases;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

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

    public int totaldiasAlquiler (){
        int totalDias = 0;
        try {
            // Parsear las cadenas de texto a objetos Date
            Date fechaInicio = dateFormat.parse( this.fhinicio );
            Date fechaFin = dateFormat.parse( this.fhfin );

            // Calcular la duración en días sumando 1 al resultado
            long duracionMilisegundos = fechaFin.getTime() - fechaInicio.getTime();
            totalDias = (int) (duracionMilisegundos / (1000 * 60 * 60 * 24))+ 1;

        } catch (ParseException e) { // caso de error al parsear fechas
            e.printStackTrace();
        }

        return totalDias;
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
