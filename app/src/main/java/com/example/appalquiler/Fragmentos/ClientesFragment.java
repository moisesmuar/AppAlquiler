package com.example.appalquiler.Fragmentos;

import android.content.ClipData;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.appalquiler.API.RetrofitClient;
import com.example.appalquiler.Clases.Alquiler;
import com.example.appalquiler.Clases.Cliente;
import com.example.appalquiler.APIInterfaces.APIServiceCliente;
import com.example.appalquiler.Clases.Usuario;
import com.example.appalquiler.Miscelanea.ClienteAdapter;
import com.example.appalquiler.R;
import com.example.appalquiler.SharedPreferencesManager;
import com.example.appalquiler.databinding.FragmentClientesBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.GsonBuilder;

import java.io.Serializable;
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
    SharedPreferencesManager sessionManager;
    private Usuario user;

    private ClienteAdapter clienteAdapter;
    private List<Cliente> listaClientes = new ArrayList<>();
    private Alquiler alquilerEdicion;
    private SearchView searchView;

    private int modoSeleccion = 0;  // 0 listar Clientes

    public ClientesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentClientesBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        sessionManager = new SharedPreferencesManager( requireContext() );
        if ( !sessionManager.isLogin() ) { // Usuario logeado? no. redirigir a fragmento login
            Navigation.findNavController(view).navigate( R.id.loginFragment );
        }
        user = sessionManager.getSpUser();


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initRecyclerView();
        obtenerClientes();

        searchView = view.findViewById(R.id.idSearchViewClientes);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            private String previousQuery = "";
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if ( newText.equals(previousQuery) ) {
                    // El texto de búsqueda no ha cambiado, no ejecutar la lógica búsqueda
                    return false;
                }
                filtrarLista(newText);
                previousQuery = newText;
                return false;
            }
        });

        Log.e("INFO USER", "Fragment Clientes" + user.toString() +" "+ user.getRol());

        // Si el usuario es admin (rol 0) permitir crear registros boton +
        if ( user.getRol() == 0 ) {

            ConstraintLayout constraintLayout = view.findViewById(R.id.idLayoutClientes);

            FloatingActionButton fab = new FloatingActionButton(getContext());
            fab.setLayoutParams(new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
            ));

            fab.setId(View.generateViewId()); // Genera un ID único para el botón
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) fab.getLayoutParams();
            layoutParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID; // Alinea borde inferior con borde inferior del ConstraintLayout
            layoutParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID; // Alinea borde derecho con borde derecho del ConstraintLayout
            layoutParams.setMargins(0, 0, 40, 40); // Establece los márgenes
            fab.setLayoutParams(layoutParams);

            fab.setContentDescription("Nuevo Cliente");
            fab.setImageResource(R.drawable.ic_mas);

            fab.setOnClickListener( new View.OnClickListener() {  // IR A FORM CLIENTES
                @Override
                public void onClick(View view) {
                    NavController navController = Navigation.findNavController( view );
                    navController.navigate( R.id.clientesFormFragment );
                }
            });

            constraintLayout.addView( fab );
        }

    }

    /**
     * Fragmento listado queda en segundo plano al ir FormFragment
     * Actualizo datos de lista por si hay un CREATE UPDATE DELETE
     */
    @Override
    public void onResume() {
        super.onResume();
        obtenerClientes();
    }

    /**
     * Filtro por nombre  Cliente
     * @param texto
     */
    private void filtrarLista( String texto ) {
        List<Cliente> listFiltrada = new ArrayList<>();
        for( Cliente obj : listaClientes ){
            if( obj.getNombre().toLowerCase().contains( texto.toLowerCase() )){
                listFiltrada.add( obj );
            }
        }

        if( listFiltrada.isEmpty() ){
            Toast.makeText( getContext() , "No existen registros con ese Nombre", Toast.LENGTH_SHORT ).show();
        }else{
            clienteAdapter.setFilteredList( listFiltrada );
        }
    }

    private void initRecyclerView() {
        Bundle bundle = getArguments();
        if ( bundle != null && bundle.containsKey("modoSeleccion") ) {
            this.modoSeleccion = bundle.getInt("modoSeleccion");
            switch (modoSeleccion) {
                case 1:     // Creacción
                    this.alquilerEdicion = (Alquiler) bundle.getSerializable("alquilerNuevo");
                    break;
                case 2:     // Edición
                    this.alquilerEdicion = (Alquiler) bundle.getSerializable("alquilerEdicion");
                    break;
                default:
                    break;
            }
        }
        Log.d("ClientesFragment", "modoSeleccion: " + modoSeleccion );

        binding.rvClientes.setLayoutManager( new LinearLayoutManager( getContext() ) );
        clienteAdapter = new ClienteAdapter( listaClientes , modoSeleccion, alquilerEdicion );
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