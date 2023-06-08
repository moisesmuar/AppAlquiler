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
import android.widget.SearchView;
import android.widget.Toast;

import com.example.appalquiler.API.RetrofitClient;
import com.example.appalquiler.Clases.Alquiler;
import com.example.appalquiler.Clases.Cliente;
import com.example.appalquiler.Clases.Inmueble;
import com.example.appalquiler.APIInterfaces.APIServiceInmueble;
import com.example.appalquiler.Miscelanea.InmuebleAdapter;
import com.example.appalquiler.R;
import com.example.appalquiler.SharedPreferencesManager;
import com.example.appalquiler.databinding.FragmentInmueblesBinding;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InmueblesFragment extends Fragment {

    private FragmentInmueblesBinding binding;
    private InmuebleAdapter inmuebleAdapter;
    private List<Inmueble> listaInmuebles = new ArrayList<>();
    private Alquiler alquilerEdicion;
    private SearchView searchView;

    private int modoSeleccion = 0; // 0 listar Inmuebles

    public InmueblesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentInmueblesBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        SharedPreferencesManager sessionManager = new SharedPreferencesManager( requireContext() );
        if ( !sessionManager.isLogin() ) { // Usuario logeado? no. redirigir a fragmento login
            Navigation.findNavController(view).navigate( R.id.loginFragment );
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initRecyclerView();
        obtenerInmuebles();

        searchView = view.findViewById(R.id.idSearchViewInmuebles);
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

        binding.btMasInmuebles.setOnClickListener(new View.OnClickListener() {  // IR A FORM Inmueble
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController( view );
                navController.navigate( R.id.inmueblesFormFragment );
            }
        });

    }

    /**
     * Filtro por nombre Inmueble
     * @param texto
     */
    private void filtrarLista( String texto ) {
        List<Inmueble> listFiltrada = new ArrayList<>();
        for( Inmueble obj : listaInmuebles ){
            if( obj.getNombre().toLowerCase().contains( texto.toLowerCase() )){
                listFiltrada.add( obj );
            }
        }

        if( listFiltrada.isEmpty() ){
            Toast.makeText( getContext() , "No existen registros con ese Nombre", Toast.LENGTH_SHORT ).show();
        }else{
            inmuebleAdapter.setFilteredList( listFiltrada );
        }
    }

    private void initRecyclerView() {
        // Modo selección
        // 0 default listado
        // 1 selecionar obj Inmueble - accion creacción de un Alquiler
        // 2 seleccionar obj Inmueble - acción edición de un Alquiler
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
        Log.d("InmueblesFragment", "modoSeleccion: " + modoSeleccion );

        binding.rvInmuebles.setLayoutManager( new LinearLayoutManager( getContext() ) );
        inmuebleAdapter = new InmuebleAdapter( listaInmuebles, modoSeleccion, alquilerEdicion );
        binding.rvInmuebles.setAdapter( inmuebleAdapter );
    }

    public void obtenerInmuebles( ) {
        RetrofitClient retrofitClient = RetrofitClient.getInstance();
        APIServiceInmueble apiService = retrofitClient.getRetrofit().create( APIServiceInmueble.class );

        Call<List<Inmueble>> apiCall = apiService.getInmuebles();
        apiCall.enqueue( new Callback<List<Inmueble>>() {
            @Override
            public void onResponse(Call<List<Inmueble>> call, Response<List<Inmueble>> response) {
                if ( response.isSuccessful() && response.body() != null ) {

                    List<Inmueble> listaRespuesta = response.body();;
                    Log.d("RESPONSE", "Código: " + response.code() + " Respuesta: " + listaRespuesta.toString());

                    listaInmuebles.clear();
                    listaInmuebles.addAll( listaRespuesta );
                    inmuebleAdapter.notifyDataSetChanged();
                } else {
                    Log.d("ERROR", "Código: " + response.code() + " Mensaje: " + response.message());
                }
            }
            @Override
            public void onFailure(Call<List<Inmueble>> call, Throwable t) {
                Log.e("Error con Log.e", "Petición Inmuebles Fallida", t);
                Toast.makeText( getContext() , "Petición Inmuebles Fallida", Toast.LENGTH_SHORT ).show();
            }
        });
    }


}