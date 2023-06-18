package com.example.appalquiler.APIInterfaces;

import com.example.appalquiler.Models.Portal;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface APIServicePortal {

    // Obtener todos los registros
    @GET("portales")
    Call<List<Portal>> getPortales();

}
