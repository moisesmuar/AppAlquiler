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
import com.example.appalquiler.APIInterfaces.APIServiceAlquiler;
import com.example.appalquiler.Clases.Cliente;
import com.example.appalquiler.Miscelanea.AlquilerAdapter;
import com.example.appalquiler.R;
import com.example.appalquiler.SharedPreferencesManager;
import com.example.appalquiler.databinding.FragmentAlquileresBinding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AlquileresFragment extends Fragment {

    private FragmentAlquileresBinding binding;
    SharedPreferencesManager sessionManager;
    private AlquilerAdapter alquilerAdapter;
    private List<Alquiler> listaAlquileres = new ArrayList<>();
    private SearchView searchView;

    public AlquileresFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAlquileresBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        sessionManager = new SharedPreferencesManager( requireContext() );
        if ( !sessionManager.isLogin() ) {
            Navigation.findNavController(view).navigate( R.id.loginFragment );
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initRecyclerView();
        obtenerAlquileres();

        searchView = view.findViewById(R.id.idSearchViewAlquileres);
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

        binding.btMasAlquileres.setOnClickListener( new View.OnClickListener() {  // IR A FORM Alquileres
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("alquilerNuevo" , (Serializable) new Alquiler() );

                NavController navController = Navigation.findNavController( view );
                navController.navigate( R.id.alquileresFormFragment, bundle  );

            }
        });
    }

    /**
     * Filtro por nombre Portal
     * @param texto
     */
    private void filtrarLista( String texto ) {
        List<Alquiler> listFiltrada = new ArrayList<>();
        for( Alquiler obj : listaAlquileres ){
            /*if( obj.getCliente().getNombre().toLowerCase().contains( texto.toLowerCase() )){
                listFiltrada.add( obj );
            }
            if( obj.getInmueble().getNombre().toLowerCase().contains( texto.toLowerCase() )){
                listFiltrada.add( obj );
            }*/
            if( obj.getPortal().getNombre().toLowerCase().contains( texto.toLowerCase() )){
                listFiltrada.add( obj );
            }
        }

        if( listFiltrada.isEmpty() ){
            Toast.makeText( getContext() , "No hay registros con ese Nombre Portal", Toast.LENGTH_SHORT ).show();
        }else{
            alquilerAdapter.setFilteredList( listFiltrada );
        }
    }

    private void initRecyclerView() {
        binding.rvAlquileres.setLayoutManager( new LinearLayoutManager( getContext() ) );

        alquilerAdapter = new AlquilerAdapter( listaAlquileres );
        binding.rvAlquileres.setAdapter( alquilerAdapter );
    }

    public void obtenerAlquileres() {

        RetrofitClient retrofitClient = RetrofitClient.getInstance();
        APIServiceAlquiler apiService = retrofitClient.getRetrofit().create( APIServiceAlquiler.class );

        Call<List<Alquiler>> apiCall = apiService.getAlquileres();
        apiCall.enqueue( new Callback<List<Alquiler>>() {      // LAMADA ASINC call.enqueue
            @Override
            public void onResponse(Call<List<Alquiler>> call, Response<List<Alquiler>> response) {

                // Convertir la lista de Alquiler a JSON
                Gson gson = new Gson();
                String jsonResponse = gson.toJson( response.body() );

                // Imprimir el JSON de respuesta en la consola
                Log.d("Respuesta JSON Listado alquileres", jsonResponse);

                if ( response.isSuccessful() && response.body() != null ) {

                    List<Alquiler> listaRespuesta = response.body();
                    /*Log.d("RESPONSE", "Código: " + response.code() + " Respuesta: " + listaRespuesta.toString());
                    for (Alquiler alquiler : listaRespuesta) {
                        Log.d("RESPONSE", alquiler.toString());
                    }*/
                    listaAlquileres.clear();
                    listaAlquileres.addAll( listaRespuesta );
                    alquilerAdapter.notifyDataSetChanged();
                } else {
                    Log.d("ERROR", "Código: " + response.code() + " Mensaje: " + response.message());
                }
            }
            @Override
            public void onFailure(Call<List<Alquiler>> call, Throwable t) {
                Log.e("Error con Log.e", "Petición Alquileres Fallida", t);
                Toast.makeText( getContext() , "Petición Alquileres Fallida", Toast.LENGTH_SHORT ).show();
            }
        });
    }

}