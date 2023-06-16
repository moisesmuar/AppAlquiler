package com.example.appalquiler.Fragmentos;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.appalquiler.API.RetrofitClient;
import com.example.appalquiler.APIInterfaces.APIServiceUsuario;
import com.example.appalquiler.Clases.Usuario;
import com.example.appalquiler.R;
import com.example.appalquiler.databinding.FragmentLoginBinding;
import com.example.appalquiler.databinding.FragmentRegistroBinding;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RegistroFragment extends Fragment {

    private FragmentRegistroBinding binding;
    private EditText etUsername, etPassword, etNifEmpresa;

    public RegistroFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegistroBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        etUsername = binding.etRUserName;
        etPassword = binding.etRPassword;
        etNifEmpresa = binding.etRnif;

        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

        binding.tvLoginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController( requireView() ).navigate( R.id.loginFragment );
            }
        });
    }

    private void registerUser() {
        String userName = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String nif = etNifEmpresa.getText().toString().trim();

        if ( userName.isEmpty() ) {
            etUsername.setError("Usuario requerido");
            etUsername.requestFocus();
            return;
        } else if ( password.isEmpty() ) {
            etPassword.setError("Contraseña requerida");
            etPassword.requestFocus();
            return;
        }else if ( nif.isEmpty() ) {
            etNifEmpresa.setError("Empresa requerida");
            etNifEmpresa.requestFocus();
            return;
        }

        RetrofitClient retrofitClient = RetrofitClient.getInstance();
        APIServiceUsuario apiService = retrofitClient.getRetrofit().create( APIServiceUsuario.class );

        Call<ResponseBody> call = apiService.createUser( new Usuario( userName, password, nif ));
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
                    if ( s.equals("SUCCESS") ) {
                        Toast.makeText( getContext(), "Registrado exitosamente./n Por favor Iniciar sesión", Toast.LENGTH_LONG).show();
                        Navigation.findNavController( requireView() ).navigate( R.id.loginFragment );
                    } else if( s.equals("USER_ALREADY_EXISTS") ){
                        Toast.makeText( getContext(), "Usuario ya existe!", Toast.LENGTH_LONG).show();
                    } else if( s.equals("UNAUTHORIZED_COMPANY") ){
                        Toast.makeText( getContext(), "Empresa no autorizada!", Toast.LENGTH_LONG).show();
                    }

                }
                else {    // Manejar el caso de respuesta nula o no exitosa
                    Log.d("onResponse", "Código: " + response.code() + "Error respuesta nula o no exitosa RegistroFragment- Mensaje: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText( getContext(), "Fallo conexión API", Toast.LENGTH_LONG).show();
            }
        });

    }

}