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
import com.example.appalquiler.APIInterfaces.APIServiceInmueble;
import com.example.appalquiler.APIInterfaces.APIServicePortal;
import com.example.appalquiler.Clases.Alquiler;
import com.example.appalquiler.Clases.Cliente;
import com.example.appalquiler.Clases.Inmueble;
import com.example.appalquiler.Clases.Portal;
import com.example.appalquiler.Clases.reciclerViewCalendario;
import com.example.appalquiler.Miscelanea.AlquilerAdapter;
import com.example.appalquiler.Miscelanea.PortalesAdapter;
import com.example.appalquiler.R;
import com.example.appalquiler.SharedPreferencesManager;
import com.example.appalquiler.databinding.FragmentPortalesBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PortalesFragment extends Fragment {

    private FragmentPortalesBinding binding;
    SharedPreferencesManager sessionManager;
    private PortalesAdapter portalAdapter;
    private List<Portal> listaPortales = new ArrayList<>();
    private Alquiler alquilerEdicion;
    private SearchView searchView;


    private int modoSeleccion = 0;

    public PortalesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       binding = FragmentPortalesBinding.inflate(inflater, container, false);
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
        obtenerPortales();

        searchView = view.findViewById(R.id.idSearchViewPortales);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filtrarLista( newText );
                return false;
            }
        });

        binding.btMasPortales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText( getContext() , "No se puede registrar un portal.", Toast.LENGTH_SHORT ).show();

            }
        });
    }

    /**
     * Filtro por nombre Portal
     * @param texto
     */
    private void filtrarLista( String texto ) {
        List<Portal> listFiltrada = new ArrayList<>();
        for( Portal obj : listaPortales ){
            if( obj.getNombre().toLowerCase().contains( texto.toLowerCase() )){
                listFiltrada.add( obj );
            }
        }

        if( listFiltrada.isEmpty() ){
            Toast.makeText( getContext() , "No existen registros con ese Nombre", Toast.LENGTH_SHORT ).show();
        }else{
            portalAdapter.setFilteredList( listFiltrada );
        }
    }

    private void initRecyclerView() {
        Bundle bundle = getArguments();
        if ( bundle != null && bundle.containsKey("modoSeleccion") ) {  // key "seleccionCliente" no es nula
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

        Log.d("PortalesFragment", "modoSeleccion: " + modoSeleccion );

        binding.rvPortales.setLayoutManager( new LinearLayoutManager( getContext() ) );
        portalAdapter = new PortalesAdapter( listaPortales, modoSeleccion, alquilerEdicion );
        binding.rvPortales.setAdapter( portalAdapter );
    }

    public void obtenerPortales( ) {
        RetrofitClient retrofitClient = RetrofitClient.getInstance();
        APIServicePortal apiService = retrofitClient.getRetrofit().create( APIServicePortal.class );

        Call<List<Portal>> apiCall = apiService.getPortales();
        apiCall.enqueue( new Callback<List<Portal>>() {
            @Override
            public void onResponse(Call<List<Portal>> call, Response<List<Portal>> response) {
                if ( response.isSuccessful() && response.body() != null ) {

                    List<Portal> listaRespuesta = response.body();;
                    Log.d("RESPONSE", "Código: " + response.code() + " obtenerPortales()" );
                    for ( Portal port : listaRespuesta ) {
                        Log.d("Lectura Portal", "color " + port.getColorHex() + "nombre" + port.getNombre() );
                    }
                    listaPortales.clear();
                    listaPortales.addAll( listaRespuesta );
                    portalAdapter.notifyDataSetChanged();
                } else {
                    Log.d("ERROR", "Código: " + response.code() + " Mensaje: " + response.message());
                }
            }
            @Override
            public void onFailure(Call<List<Portal>> call, Throwable t) {
                Log.e("Error con Log.e", "Petición Portales Fallida", t);
                Toast.makeText( getContext() , "Petición Portales Fallida", Toast.LENGTH_SHORT ).show();
            }
        });
    }

}