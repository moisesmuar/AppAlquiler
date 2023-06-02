package com.example.appalquiler.API;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Realiza todas las operaciones de cliente API REST.
 * Es una clase singleton.
 */
public class RetrofitClient {

    private static  final String BASE_URL = "http://10.0.2.2:8082/calendario/";
    private static RetrofitClient mInstance;
    private Retrofit retrofit;

    private RetrofitClient () {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized RetrofitClient getInstance() {
        if (mInstance == null) {
            mInstance = new RetrofitClient();
        }
        return mInstance;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }
    // Llamo cada servicio API según necesito, despues de traer RetrofitClient

    /* public API getAPI () {
        return retrofit.create(API.class);
    } */


}