package com.example.appalquiler.Fragmentos;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.appalquiler.API.RetrofitClient;
import com.example.appalquiler.APIInterfaces.APIServiceAlquiler;
import com.example.appalquiler.Clases.Cliente;
import com.example.appalquiler.APIInterfaces.APIServiceCliente;
import com.example.appalquiler.Clases.Empresa;
import com.example.appalquiler.Clases.Usuario;
import com.example.appalquiler.R;
import com.example.appalquiler.SharedPreferencesManager;
import com.example.appalquiler.databinding.FragmentClientesFormBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ClientesFormFragment extends Fragment {

    private FragmentClientesFormBinding binding;
    private Cliente cliente;
    SharedPreferencesManager sessionManager;

    public ClientesFormFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentClientesFormBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        sessionManager = new SharedPreferencesManager( requireContext() );
        if ( !sessionManager.isLogin() ) { // Usuario logeado? no. redirigir a fragmento login
            Navigation.findNavController(view).navigate( R.id.loginFragment );
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Obtener bundle de argumentos del fragment
        Bundle bundle = getArguments();
        if( bundle != null ) {
            this.cliente = (Cliente) bundle.getSerializable("cliente");

            APIobtenerCliente( cliente.getIdCliente() );
            binding.btnGuardar.setText("Modificar");
            binding.btnEliminar.setText("Eliminar");
        }
        else {
            binding.btnGuardar.setText("Añadir");
            binding.btnEliminar.setText("Cancelar");
        }

        binding.btnGuardar.setOnClickListener(new View.OnClickListener() {  // POST CLIENTE API
            @Override
            public void onClick(View view) {
                Usuario user = sessionManager.getSpUser();

                if( binding.btnGuardar.getText().equals("Modificar") ){
                    Cliente cliModificado = new Cliente(
                        binding.editTextNombre.getText().toString(),
                        binding.editTextTelefono.getText().toString(),
                        binding.editTextEmail.getText().toString(),
                        binding.editTextCalle.getText().toString(),
                        binding.editTextCiudad.getText().toString(),
                        binding.editTextPais.getText().toString(),
                        binding.editTextCP.getText().toString(),
                        user.getEmpresa()
                    );
                    editar( cliente.getIdCliente() , cliModificado );

                    Navigation.findNavController(view).navigate( R.id.clientesFragment );
                }
                else{  // Crear NUEVO
                    String nombre = binding.editTextNombre.getText().toString();
                    String telefono = binding.editTextTelefono.getText().toString();
                    String email = binding.editTextEmail.getText().toString();
                    String calle = binding.editTextCalle.getText().toString();
                    String ciudad = binding.editTextCiudad.getText().toString();
                    String pais = binding.editTextPais.getText().toString();
                    String cp = binding.editTextCP.getText().toString();

                    Cliente nuevoCliente = new Cliente(
                            nombre, telefono,
                            email, calle,
                            ciudad, pais,
                            cp,  user.getEmpresa() );
                    guardar( nuevoCliente );

                    Navigation.findNavController(view).navigate( R.id.clientesFragment );
                }
            }
        });

        binding.btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( binding.btnEliminar.getText().equals("Eliminar") ){
                    eliminar( cliente.getIdCliente() );

                    Navigation.findNavController(v).navigate( R.id.clientesFragment );
                }
                else{  // Cancelar  ir atrás
                    Navigation.findNavController(v).navigate( R.id.clientesFragment );
                }
            }
        });

    }

    public void guardar( Cliente cli ) {
        RetrofitClient retrofitClient = RetrofitClient.getInstance();
        APIServiceCliente apiService = retrofitClient.getRetrofit().create( APIServiceCliente.class );

       // APIServiceCliente clienteApi = getRetrofit().create( APIServiceCliente.class );
        Call<Cliente> call = apiService.createCliente( cli );  //  POST con Retrofit
        call.enqueue( new Callback<Cliente>() {
            @Override
            public void onResponse(Call<Cliente> call, Response<Cliente> response) {
                if ( response.isSuccessful() ) {
                    Toast.makeText(getContext(), "¡Guardado!", Toast.LENGTH_LONG).show();
                    limpiarCamposFragment();
                    Log.e("onResponse", " Registro Guardado ");
                }else {
                    Log.e("onResponse", "Error en la respuesta: " + response.message());
                }
            }
            @Override
            public void onFailure(Call<Cliente> call, Throwable t) {
                Log.e("onFailure", "Error en la petición: " + t.getMessage());
            }
        });
    }

    public void editar( Integer idCliente , Cliente cliente ) {
        RetrofitClient retrofitClient = RetrofitClient.getInstance();
        APIServiceCliente apiService = retrofitClient.getRetrofit().create( APIServiceCliente.class );

        Call<Cliente> call = apiService.updateCliente( idCliente, cliente );
        call.enqueue( new Callback<Cliente>() {
            @Override
            public void onResponse(Call<Cliente> call, Response<Cliente> response) {
                if ( response.isSuccessful() ) {
                    Toast.makeText(getContext(), "¡Editado!", Toast.LENGTH_LONG).show();
                    limpiarCamposFragment();
                }
            }
            @Override
            public void onFailure(Call<Cliente> call, Throwable t) {
                Log.e("Error con Log.e", "¡¡onFailure Error Editar", t);
            }
        });
    }

    public void eliminar( Integer idCliente ) {
        RetrofitClient retrofitClient = RetrofitClient.getInstance();
        APIServiceCliente apiService = retrofitClient.getRetrofit().create( APIServiceCliente.class );

        Call<Void> call = apiService.deleteCliente( idCliente );
        call.enqueue( new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if ( response.isSuccessful() ) {
                    Toast.makeText(getContext(), "¡Eliminado!", Toast.LENGTH_LONG).show();
                    limpiarCamposFragment();
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("Error con Log.e", "¡¡onFailure Error Eliminar", t);
            }
        });
    }

    public void limpiarCamposFragment( ) {
        binding.editTextNombre.setText("");
        binding.editTextTelefono.setText("");
        binding.editTextEmail.setText("");
        binding.editTextCalle.setText("");
        binding.editTextCiudad.setText("");
        binding.editTextPais.setText("");
        binding.editTextCP.setText("");
    }

    public void APIobtenerCliente( Integer idCliente ) {
        RetrofitClient retrofitClient = RetrofitClient.getInstance();
        APIServiceCliente apiService = retrofitClient.getRetrofit().create( APIServiceCliente.class );

        Call<Cliente> apiCall = apiService.getCliente( idCliente );
        apiCall.enqueue( new Callback<Cliente>() {      // LAMADA ASINC call.enqueue
            @Override
            public void onResponse(Call<Cliente> call, Response<Cliente> response) {
                if ( response.isSuccessful() && response.body() != null ) {

                    Cliente cli = response.body();

                    binding.editTextNombre.setText( cli.getNombre() );
                    binding.editTextTelefono.setText( cli.getTelefono() );
                    binding.editTextEmail.setText( cli.getEmail() );
                    binding.editTextCalle.setText( cli.getCalle() );
                    binding.editTextCiudad.setText( cli.getCiudad() );
                    binding.editTextPais.setText( cli.getPais() );
                    binding.editTextCP.setText( cli.getCp() );

                    Log.d("RESPONSE", "Código: " + response.code() + " Respuesta: " + cli.toString() );

                } else {
                    Log.d("ERROR", "Código: " + response.code() + " Mensaje: " + response.message());
                }
            }
            @Override
            public void onFailure(Call<Cliente> call, Throwable t) {
                Log.e("Error con Log.e", "¡¡onFailure Error al obtener", t);
            }
        });
    }

}