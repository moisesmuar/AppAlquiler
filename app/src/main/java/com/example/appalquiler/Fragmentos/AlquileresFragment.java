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
import com.example.appalquiler.APIInterfaces.APIServiceAlquiler;
import com.example.appalquiler.Miscelanea.AlquilerAdapter;
import com.example.appalquiler.R;
import com.example.appalquiler.SharedPreferencesManager;
import com.example.appalquiler.databinding.FragmentAlquileresBinding;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AlquileresFragment extends Fragment {

    private FragmentAlquileresBinding binding;
    private AlquilerAdapter alquilerAdapter;
    private List<Alquiler> listaAlquileres = new ArrayList<>();

    public AlquileresFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAlquileresBinding.inflate(inflater, container, false);
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
        obtenerAlquileres();
        binding.btMasAlquileres.setOnClickListener( new View.OnClickListener() {  // IR A FORM Alquileres
            @Override
            public void onClick(View view) {

                NavController navController = Navigation.findNavController( view );
                navController.navigate( R.id.alquileresFormFragment );

            }
        });
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
                if ( response.isSuccessful() && response.body() != null ) {

                    List<Alquiler> listaRespuesta = response.body();
                    Log.d("RESPONSE", "Código: " + response.code() + " Respuesta: " + listaRespuesta.toString());
                    for (Alquiler alquiler : listaRespuesta) {
                        Log.d("RESPONSE", alquiler.toString());
                    }
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