package com.example.appalquiler.Clases;

import com.google.gson.annotations.SerializedName;

public class Portal {

    @SerializedName("idPortal")
    private int idPortal;
    @SerializedName("nombre")
    private String nombre;
    @SerializedName("colorHex")
    private String colorHex;

    public Portal(int idPortal, String nombre, String colorHex) {
        this.idPortal = idPortal;
        this.nombre = nombre;
        this.colorHex = colorHex;
    }

    public int getIdPortal() {
        return idPortal;
    }

    public void setIdPortal(int idPortal) {
        this.idPortal = idPortal;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getColorHex() {
        return colorHex;
    }

    public void setColorHex(String colorHex) {
        this.colorHex = colorHex;
    }
}
