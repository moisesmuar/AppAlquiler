package com.example.appalquiler.APIInterfaces;

import com.example.appalquiler.Models.Cliente;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

// INTERFAZ Para hacer llamadas a API REST y manejar CRUD en tabla "clientes"
// DEFINIR clase "Cliente" con campos del JSON que recibo de API

public interface APIServiceCliente {

    // Crear registro
    @POST("clientes")
    Call<Cliente> createCliente(@Body Cliente cliente);

    // obtener todos los registros
    @GET("clientes")
    Call<List<Cliente>> getClientes();

    // obtener registro por su ID
    @GET("clientes/{id}")
    Call<Cliente> getCliente(@Path("id") int id);

    // actualizar registro
    @PUT("clientes/{id}")
    Call<Cliente> updateCliente(@Path("id") int id, @Body Cliente cliente);

    // eliminar registro
    @DELETE("clientes/{id}")
    Call<Void> deleteCliente(@Path("id") int id);


    // obtener todos los Clientes de la empresa, para mostrar al user logeado
    @GET("clientes/empresa/{nombreEmpresa}")
    Call<List<Cliente>> getClientesEmpresa(@Path("nombreEmpresa") String nombreEmpresa);

}
