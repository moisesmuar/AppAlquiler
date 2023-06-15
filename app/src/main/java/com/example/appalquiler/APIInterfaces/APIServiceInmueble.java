package com.example.appalquiler.APIInterfaces;

import com.example.appalquiler.Clases.Cliente;
import com.example.appalquiler.Clases.Inmueble;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

// INTERFAZ Para hacer llamadas a API REST y manejar CRUD en tabla "inmuebles"
// DEFINIR clase "Inmueble" con campos del JSON que recibo de API
public interface APIServiceInmueble {

    // Crear registro
    @POST("inmuebles")
    Call<Inmueble> createInmueble(@Body Inmueble inmueble);

    // Obtener todos los registros
    @GET("inmuebles")
    Call<List<Inmueble>> getInmuebles();

    // Obtener registro por su ID
    @GET("inmuebles/{id}")
    Call<Inmueble> getInmueble(@Path("id") int id);

    // Actualizar registro
    @PUT("inmuebles/{id}")
    Call<Inmueble> updateInmueble(@Path("id") int id, @Body Inmueble inmueble);

    // Eliminar registro
    @DELETE("inmuebles/{id}")
    Call<Void> deleteInmueble(@Path("id") int id);


    // obtener todos los Inmuebles de la empresa, para mostrar al user logeado
    @GET("inmuebles/empresa/{nombreEmpresa}")
    Call<List<Inmueble>> getInmueblesEmpresa(@Path("nombreEmpresa") String nombreEmpresa);

}
