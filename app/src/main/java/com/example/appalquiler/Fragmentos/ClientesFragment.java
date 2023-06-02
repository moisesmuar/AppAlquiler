package com.example.appalquiler.Fragmentos;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.appalquiler.API.RetrofitClient;
import com.example.appalquiler.Clases.Alquiler;
import com.example.appalquiler.Clases.Cliente;
import com.example.appalquiler.APIInterfaces.APIServiceCliente;
import com.example.appalquiler.Miscelanea.ClienteAdapter;
import com.example.appalquiler.R;
import com.example.appalquiler.SharedPreferencesManager;
import com.example.appalquiler.databinding.FragmentClientesBinding;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ClientesFragment extends Fragment {

    private FragmentClientesBinding binding;
    private ClienteAdapter clienteAdapter;
    private List<Cliente> listaClientes = new ArrayList<>();

    private boolean modoSeleccionActivado = false;
    private Alquiler alquilerEdicion;

    public ClientesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentClientesBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        SharedPreferencesManager sessionManager = new SharedPreferencesManager( requireContext() );
        if ( !sessionManager.isLogin() ) { // Usuario logeado? no. redirigir a fragmento login
            Navigation.findNavController(view).navigate( R.id.loginFragment );
        }

        // genero token
        String token = UUID.randomUUID().toString();
        Log.d("    token    ", token );

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initRecyclerView();
        obtenerClientes();
        binding.btMasClientes.setOnClickListener(new View.OnClickListener() {  // IR A FORM CLIENTES
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController( view );
                navController.navigate( R.id.clientesFormFragment );
            }
        });

    }

    private void initRecyclerView() {

        Bundle bundle = getArguments();
        if ( bundle != null && bundle.containsKey("modoSeleccionActivado") ) {  // key "seleccionCliente" no es nula
            this.modoSeleccionActivado = bundle.getBoolean("modoSeleccionActivado");
            this.alquilerEdicion = (Alquiler) bundle.getSerializable("alquiler");
            Log.d("ClientesFragment", "modoSeleccionActivado: " + modoSeleccionActivado );
        }

        binding.rvClientes.setLayoutManager( new LinearLayoutManager( getContext() ) );
        clienteAdapter = new ClienteAdapter( listaClientes , modoSeleccionActivado, alquilerEdicion );
        //clienteAdapter = new ClienteAdapter( listaClientes );
        binding.rvClientes.setAdapter( clienteAdapter );
    }

    public void obtenerClientes( ) {
        RetrofitClient retrofitClient = RetrofitClient.getInstance();
        APIServiceCliente apiService = retrofitClient.getRetrofit().create( APIServiceCliente.class );

        Call<List<Cliente>> apiCall = apiService.getClientes();
        apiCall.enqueue( new Callback<List<Cliente>>() {
            @Override
            public void onResponse(Call<List<Cliente>> call, Response<List<Cliente>> response) {
                if ( response.isSuccessful() && response.body() != null ) {

                    List<Cliente> listaRespuesta = response.body();;
                    Log.d("RESPONSE", "Código: " + response.code() + " Respuesta: " + listaRespuesta.toString());

                    listaClientes.clear();
                    listaClientes.addAll( listaRespuesta );
                    clienteAdapter.notifyDataSetChanged();
                } else {
                    Log.d("ERROR", "Código: " + response.code() + " Mensaje: " + response.message());
                }
            }
            @Override
            public void onFailure(Call<List<Cliente>> call, Throwable t) {
                Log.e("Error con Log.e", "Petición Clientes Fallida", t);
                Toast.makeText( getContext() , "Petición Clientes Fallida ", Toast.LENGTH_SHORT ).show();
            }
        });
    }

}