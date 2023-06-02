package com.example.appalquiler.Clases;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Cliente implements Serializable  {

    @SerializedName("idCliente")
    private int idCliente;
    @SerializedName("nombre")
    private String nombre;
    @SerializedName("telefono")
    private String telefono;
    @SerializedName("email")
    private String email;
    @SerializedName("calle")
    private String calle;
    @SerializedName("ciudad")
    private String ciudad;
    @SerializedName("pais")
    private String pais;
    @SerializedName("cp")
    private String cp;
    @SerializedName("empresa")
    private Empresa empresa;

    public Cliente( String nombre, String telefono,
                    String email, String calle,
                    String ciudad, String pais,
                    String cp, Empresa empresa) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.email = email;
        this.calle = calle;
        this.ciudad = ciudad;
        this.pais = pais;
        this.cp = cp;
        this.empresa = empresa;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getCp() {
        return cp;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }
}
