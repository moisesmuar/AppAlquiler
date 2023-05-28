package com.example.appalquiler.Clases;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Inmueble implements Serializable  {

    @SerializedName("idInmueble")
    private int idInmueble;
    @SerializedName("nombre")
    private String nombre;
    @SerializedName("calle")
    private String calle;
    @SerializedName("ciudad")
    private String ciudad;
    @SerializedName("numPersonas")
    private int numPersonas;
    @SerializedName("numHabitaciones")
    private int numHabitaciones;
    @SerializedName("numBanos")
    private int numBanos;
    @SerializedName("numAseos")
    private int numAseos;

    public Inmueble( String nombre, String calle, String ciudad, int numPersonas, int numHabitaciones, int numBanos, int numAseos) {

        this.nombre = nombre;
        this.calle = calle;
        this.ciudad = ciudad;
        this.numPersonas = numPersonas;
        this.numHabitaciones = numHabitaciones;
        this.numBanos = numBanos;
        this.numAseos = numAseos;
    }

    @Override
    public String toString() {
        return "Inmueble{" +
                "idInmueble=" + idInmueble +
                ", nombre='" + nombre + '\'' +
                ", calle='" + calle + '\'' +
                ", ciudad='" + ciudad + '\'' +
                ", numPersonas=" + numPersonas +
                ", numHabitaciones=" + numHabitaciones +
                ", numBanos=" + numBanos +
                ", numAseos=" + numAseos +
                '}';
    }

    public int getIdInmueble() {
        return idInmueble;
    }

    public void setIdInmueble(int idInmueble) {
        this.idInmueble = idInmueble;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public int getNumPersonas() {
        return numPersonas;
    }

    public void setNumPersonas(int numPersonas) {
        this.numPersonas = numPersonas;
    }

    public int getNumHabitaciones() {
        return numHabitaciones;
    }

    public void setNumHabitaciones(int numHabitaciones) {
        this.numHabitaciones = numHabitaciones;
    }

    public int getNumBanos() {
        return numBanos;
    }

    public void setNumBanos(int numBanos) {
        this.numBanos = numBanos;
    }

    public int getNumAseos() {
        return numAseos;
    }

    public void setNumAseos(int numAseos) {
        this.numAseos = numAseos;
    }

}
