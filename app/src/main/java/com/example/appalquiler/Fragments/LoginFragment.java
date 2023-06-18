package com.example.appalquiler.Fragments;

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
import com.example.appalquiler.Models.LoginResponse;
import com.example.appalquiler.Models.Usuario;
import com.example.appalquiler.R;
import com.example.appalquiler.Utils.SharedPreferencesManager;
import com.example.appalquiler.databinding.FragmentLoginBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        // Ocultar barra de navegación botón de retroceso actividad actual
        // ((AppCompatActivity) requireActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

        if ( userName.isEmpty() ) {
            etUsername.setError("Usuario requerido");
            etUsername.requestFocus();
            return;
        } else if ( password.isEmpty() ) {
            etPassword.setError("Contraseña requerida");
            etPassword.requestFocus();
            return;
        }

        RetrofitClient retrofitClient = RetrofitClient.getInstance();
        APIServiceUsuario apiService = retrofitClient.getRetrofit().create( APIServiceUsuario.class );

        Call<LoginResponse> call = apiService.checkUser( new Usuario(userName, password) );
        call.enqueue( new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                if ( response.isSuccessful() && response.body() != null ) {

                    LoginResponse loginResponse = response.body();
                    if ( loginResponse != null && loginResponse.getUser() != null ) {

                        Usuario user = loginResponse.getUser();
                        Toast.makeText(getContext(), "¡Usuario conectado!", Toast.LENGTH_LONG).show();

                        Log.d("LOGIN", "usuario : " + user.toString() );

                        // Realizar operaciones necesarias con usuario y empresa asociada...

                        // Login + Guardar usuario en SharedPreferences
                        SharedPreferencesManager sessionManager = new SharedPreferencesManager(requireContext());
                        sessionManager.saveSpBoolean("spIsLogin", true);
                        sessionManager.saveSpUser("spUser", user);

                        // Navegar a la siguiente pantalla
                        Navigation.findNavController(requireView()).navigate(R.id.calendarHorizontalFragment);
                    }else {
                        // Credenciales inválidas
                        Toast.makeText(getContext(), "¡Credenciales incorrectas! ¡Intentar otra vez!", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Log.d("Error", "Código: " + response.code() + " - Mensaje: " + response.message());
                    Toast.makeText(getContext(), "Error onResponse.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText( getContext(), "Fallo conexión API", Toast.LENGTH_LONG).show();
               // Toast.makeText( getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

}