package com.example.appalquiler.Clases;

import com.google.gson.annotations.SerializedName;

public class Usuario {

    private String userName;
    private String password;
    @SerializedName("empresa")
    private Empresa empresa;

    // para el login no solicito empresa
    public Usuario(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public Usuario( String userName,
                    String password,
                    Empresa empresa) {
        this.userName = userName;
        this.password = password;
        this.empresa = empresa;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", empresa=" + empresa +
                '}';
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

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }
}
