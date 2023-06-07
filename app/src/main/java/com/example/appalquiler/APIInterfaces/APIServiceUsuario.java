package com.example.appalquiler.APIInterfaces;

import com.example.appalquiler.Clases.LoginResponse;
import com.example.appalquiler.Clases.Usuario;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface APIServiceUsuario {

    @POST("users/register")
    Call<ResponseBody> createUser (
            @Body Usuario user
    );

    @POST("users/login")
    Call<LoginResponse> checkUser (
            @Body Usuario user
    );

}
