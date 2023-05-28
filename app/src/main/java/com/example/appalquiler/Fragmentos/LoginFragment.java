package com.example.appalquiler.Fragmentos;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.appalquiler.API.RetrofitClient;
import com.example.appalquiler.APIInterfaces.APIServiceInmueble;
import com.example.appalquiler.APIInterfaces.APIServiceUsuario;
import com.example.appalquiler.Clases.Usuario;
import com.example.appalquiler.R;
import com.example.appalquiler.SharedPreferencesManager;
import com.example.appalquiler.databinding.FragmentClientesBinding;
import com.example.appalquiler.databinding.FragmentLoginBinding;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private EditText etUsername, etPassword;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate( R.layout.fragment_login, container, false);
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_login);

        etUsername = binding.etUserName;
        etPassword = binding.etPassword;

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });
        binding.tvRegisterLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController( requireView() ).navigate( R.id.registroFragment );
                // startActivity( new Intent( LoginFragment.this, RegistroFragment.class) );
            }
        });
    }

    private void loginUser() {
        final String userName = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (userName.isEmpty()) {
            etUsername.setError("Username is required");
            etUsername.requestFocus();
            return;
        } else if (password.isEmpty()) {
            etPassword.setError("Password is required");
            etPassword.requestFocus();
            return;
        }

        RetrofitClient retrofitClient = RetrofitClient.getInstance();
        APIServiceUsuario apiService = retrofitClient.getRetrofit().create( APIServiceUsuario.class );

        Call<ResponseBody> call = apiService.checkUser( new Usuario(userName, password) );
        call.enqueue( new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if ( response.isSuccessful() && response.body() != null ) {

                    String s = "";
                    try {
                        s = response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.d("String response.body", "Retorno de API : " + s );
                    if ( s.equals(userName) ) {
                        Toast.makeText( getContext(), "User logged in!", Toast.LENGTH_LONG).show();

                        // LOGIN APLICACION
                        SharedPreferencesManager sessionManager = new SharedPreferencesManager( requireContext() );
                        sessionManager.saveSpBoolean("spIsLogin", true);
                        Navigation.findNavController( requireView() ).navigate( R.id.calendarHorizontalFragment );

                    } else {
                        Toast.makeText( getContext() , "Incorrect Credentials! Try again!", Toast.LENGTH_LONG).show();
                        Log.d("Incorrect Credentials!", "Retorno de API : " + s );
                    }

                }
                else {    // Manejar el caso de respuesta nula o no exitosa
                    Log.d("ERROR", "Código: " + response.code() + "respuesta nula o no exitosa LoginFragment - Mensaje: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText( getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

}