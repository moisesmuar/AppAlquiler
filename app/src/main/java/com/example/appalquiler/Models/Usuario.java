package com.example.appalquiler.Models;

import com.google.gson.annotations.SerializedName;

public class Usuario {

    private String userName;
    private String password;
    private int rol;
    @SerializedName("empresa")
    private Empresa empresa;
    private String nif;  // Solo uso para crear el user

    // login de usuario no solicito empresa
    public Usuario(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    // registro de usuario, solicito nif de empresa
    public Usuario(String userName, String password, String nif) {
        this.userName = userName;
        this.password = password;
        this.nif = nif;
    }

    // Mapear objetos recibidos API
    public Usuario( String userName,
                    String password,
                    int rol,
                    Empresa empresa) {
        this.userName = userName;
        this.password = password;
        this.rol = rol;
        this.empresa = empresa;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRol() {
        return rol;
    }

    public void setRol(int rol) {
        this.rol = rol;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }


}
