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
import com.example.appalquiler.Clases.Alquiler;
import com.example.appalquiler.APIInterfaces.APIServiceAlquiler;
import com.example.appalquiler.R;
import com.example.appalquiler.SharedPreferencesManager;
import com.example.appalquiler.databinding.FragmentAlquileresFormBinding;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class AlquileresFormFragment extends Fragment {

    private FragmentAlquileresFormBinding binding;

    private Alquiler alquiler;

    public AlquileresFormFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentAlquileresFormBinding.inflate(inflater, container, false);
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

        Bundle bundle = getArguments();  // Obtener bundle de argumentos del fragment

        if( bundle != null ) {   // Para saber si es edición o creacción.

            // RECOGER OBJ Alquiler de (AlquileresFragment) listado o de listad despues de Editarlo
            this.alquiler = (Alquiler) bundle.getSerializable("alquiler");

            //binding.editTextDias.setText( String.valueOf( alquiler.getDias() )  );
            //binding.editTextPrecioDia.setText( String.valueOf( alquiler.getPrecioDia() ) );
            binding.editTextFhInicio.setText( alquiler.getFhinicio() );
            binding.editTextFhFin.setText( alquiler.getFhfin() );
            binding.editTextNombreInmueble.setText( alquiler.getInmueble().getNombre() );
            binding.editTextNombreCliente.setText( alquiler.getCliente().getNombre() );

            // APIobtenerAlquiler( alquiler.getIdAlquiler() );
            binding.btnGuardar.setText("Modificar");
            binding.btnEliminar.setText("Eliminar");
        }
        else {
            binding.btnGuardar.setText("Añadir");
            binding.btnEliminar.setText("Cancelar");
        }

        // GET INMUEBLES y CLIENTES
        binding.btnGuardar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick( View view ) {

                if( binding.btnGuardar.getText().equals("Modificar") ){
                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy"); // ???
                    Alquiler alquilerMod = new Alquiler(
                        binding.editTextFhInicio.getText().toString(),
                        binding.editTextFhFin.getText().toString(),
                        alquiler.getInmueble(),
                        alquiler.getCliente()
                    );
                    editar( alquiler.getIdAlquiler() , alquilerMod );

                    Navigation.findNavController(view).navigate( R.id.alquileresFragment );
                }
                else{    // Crear NUEVO
                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");  // ???
                    Alquiler alquilerNuevo = new Alquiler(
                        binding.editTextFhInicio.getText().toString(),
                        binding.editTextFhFin.getText().toString(),
                        alquiler.getInmueble(),
                        alquiler.getCliente()
                    );
                    guardar( alquilerNuevo );

                    Navigation.findNavController(view).navigate( R.id.alquileresFragment );
                }
            }
        });

        binding.btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( binding.btnEliminar.getText().equals("Eliminar") ){
                    eliminar( alquiler.getIdAlquiler() );

                    Navigation.findNavController(view).navigate( R.id.alquileresFragment );
                }
                else{  // Cancelar  ir atrás
                    Navigation.findNavController(v).navigate( R.id.alquileresFragment );
                }
            }
        });

        // IR A LISTADO PARA SELECCION DE CLIENTE y editarlo en obj alquiler
        binding.ibtnClienteAlquilerBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("modoSeleccionActivado", true );
                bundle.putSerializable("alquiler" , (Serializable) alquiler );
                Navigation.findNavController(v).navigate( R.id.clientesFragment, bundle );
            }
        });

        // IR A LISTADO PARA SELECCION DE INMUEBLE y editarlo en obj alquiler
        binding.ibtnInmuebleAlquilerBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("modoSeleccionActivado", true );
                bundle.putSerializable("alquiler" , (Serializable) alquiler );
                Navigation.findNavController(v).navigate( R.id.inmueblesFragment, bundle );
            }
        });

    }

    public void guardar( Alquiler alquiler ) {
        RetrofitClient retrofitClient = RetrofitClient.getInstance();
        APIServiceAlquiler apiService = retrofitClient.getRetrofit().create( APIServiceAlquiler.class );

        Call<Alquiler> call = apiService.createAlquiler( alquiler );  //  POST con Retrofit
        call.enqueue( new Callback<Alquiler>() {
            @Override
            public void onResponse(Call<Alquiler> call, Response<Alquiler> response) {
                if ( response.isSuccessful() ) {
                    Toast.makeText(getContext(), "¡Guardado!", Toast.LENGTH_LONG).show();
                    limpiarCamposFragment();
                }
            }
            @Override
            public void onFailure(Call<Alquiler> call, Throwable t) {
                Log.e("Error con Log.e", "¡¡onFailure Error al Guardar", t);
            }
        });
    }

    public void editar( Integer idAlquiler , Alquiler alquiler ) {
        RetrofitClient retrofitClient = RetrofitClient.getInstance();
        APIServiceAlquiler apiService = retrofitClient.getRetrofit().create( APIServiceAlquiler.class );

        Call<Alquiler> call = apiService.updateAlquiler( idAlquiler, alquiler );
        call.enqueue( new Callback<Alquiler>() {
            @Override
            public void onResponse(Call<Alquiler> call, Response<Alquiler> response) {
                if ( response.isSuccessful() ) {
                    Toast.makeText(getContext(), "¡Editado!", Toast.LENGTH_LONG).show();
                    limpiarCamposFragment();
                }
            }
            @Override
            public void onFailure(Call<Alquiler> call, Throwable t) {
                Log.e("Error con Log.e", "¡¡onFailure Error Editar", t);
            }
        });
    }

    public void eliminar( Integer idAlquiler ) {
        RetrofitClient retrofitClient = RetrofitClient.getInstance();
        APIServiceAlquiler apiService = retrofitClient.getRetrofit().create( APIServiceAlquiler.class );

        Call<Void> call = apiService.deleteAlquiler( idAlquiler );
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
                Log.e("Error con Log.e", "¡¡onFailure Error Eliminar ", t);
            }
        });
    }

    public void limpiarCamposFragment( ) {
        binding.editTextDias.setText("");
        binding.editTextPrecioDia.setText("");
        binding.editTextFhInicio.setText("");
        binding.editTextFhFin.setText("");
        binding.editTextNombreInmueble.setText("");
        binding.editTextNombreCliente.setText("");
    }

    public void APIobtenerAlquiler( Integer idAlquiler ) {
        RetrofitClient retrofitClient = RetrofitClient.getInstance();
        APIServiceAlquiler apiService = retrofitClient.getRetrofit().create( APIServiceAlquiler.class );

        Call<Alquiler> apiCall = apiService.getAlquiler( idAlquiler );
        apiCall.enqueue( new Callback<Alquiler>() {      // LAMADA ASINC call.enqueue
            @Override
            public void onResponse(Call<Alquiler> call, Response<Alquiler> response) {
                if ( response.isSuccessful() && response.body() != null ) {

                    Alquiler a = response.body();

                    // binding.editTextDias.setText( String.valueOf( a.getDias() )  );
                    // binding.editTextPrecioDia.setText( String.valueOf( a.getPrecioDia() ) );
                    binding.editTextFhInicio.setText( a.getFhinicio() );
                    binding.editTextFhFin.setText( a.getFhfin() );

                    binding.editTextNombreInmueble.setText( a.getInmueble().getNombre() );
                    binding.editTextNombreCliente.setText( a.getCliente().getNombre() );

                    Log.d("RESPONSE", "Código: " + response.code() + " Respuesta: " + a.toString() );

                } else {
                    Log.d("ERROR", "Código: " + response.code() + " Mensaje: " + response.message());
                }
            }
            @Override
            public void onFailure(Call<Alquiler> call, Throwable t) {
                Log.e("Error con Log.e", "¡¡onFailure Error al obtener alquiler", t);
            }
        });
    }

}