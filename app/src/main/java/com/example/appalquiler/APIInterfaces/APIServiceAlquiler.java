package com.example.appalquiler.APIInterfaces;

import com.example.appalquiler.Models.Alquiler;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

// INTERFAZ Para hacer llamadas a API REST y manejar CRUD en tabla "alquileres"
// DEFINIR clase "Alquiler" con campos del JSON que recibo de API

public interface APIServiceAlquiler {

    // endpoints  de la interfaz

    // Crear registro
    @POST("alquileres")
    Call<Alquiler> createAlquiler(@Body Alquiler alquiler);

    // obtener todos los registros
    @GET("alquileres")
    Call<List<Alquiler>> getAlquileres();

    // obtener registro por su ID
    @GET("alquileres/{id}")
    Call<Alquiler> getAlquiler(@Path("id") int id);

    // actualizar registro
    @PUT("alquileres/{id}")
    Call<Alquiler> updateAlquiler(@Path("id") int id, @Body Alquiler alquiler);

    // eliminar registro
    @DELETE("alquileres/{id}")
    Call<Void> deleteAlquiler(@Path("id") int id);



    // obtener todos los Alquileres de la empresa, para mostrar al user logeado
    @GET("alquileres/empresa/{nombreEmpresa}")
    Call<List<Alquiler>> getAlquileresEmpresa(@Path("nombreEmpresa") String nombreEmpresa);

    // obtener alquileres por su mes y año de la Empresa
    @GET("alquileres/{mes}/{ano}/{idInmueble}/{nombreEmpresa}")
    Call<List<Alquiler>> getAlquileresMesAnoInmueble_deEmpresa(
            @Path("mes") int mes,
            @Path("ano") int ano,
            @Path("idInmueble") int idInmueble,
            @Path("nombreEmpresa") String nombreEmpresa);

}
