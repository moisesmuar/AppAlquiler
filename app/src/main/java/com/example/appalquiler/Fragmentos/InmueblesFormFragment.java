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
import com.example.appalquiler.Clases.Inmueble;
import com.example.appalquiler.APIInterfaces.APIServiceInmueble;
import com.example.appalquiler.R;
import com.example.appalquiler.SharedPreferencesManager;
import com.example.appalquiler.databinding.FragmentInmueblesFormBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class InmueblesFormFragment extends Fragment {

    private FragmentInmueblesFormBinding binding;
    private Inmueble inmueble;

    public InmueblesFormFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentInmueblesFormBinding.inflate(inflater, container, false);
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

        // Obtener bundle de argumentos del fragment
        Bundle bundle = getArguments();
        if( bundle != null ) {
            this.inmueble = (Inmueble) bundle.getSerializable("inmueble");

            APIobtenerInmueble( inmueble.getIdInmueble() );
            binding.btnGuardar.setText("Modificar");
            binding.btnEliminar.setText("Eliminar");
        }
        else {
            binding.btnGuardar.setText("Añadir");
            binding.btnEliminar.setText("Cancelar");
        }

        binding.btnGuardar.setOnClickListener(new View.OnClickListener() {  // POST Inmueble API
            @Override
            public void onClick(View view) {

                if( binding.btnGuardar.getText().equals("Modificar") ){
                    Inmueble inmModificado = new Inmueble(
                            binding.editTextNombre.getText().toString(),
                            binding.editTextCalle.getText().toString(),
                            binding.editTextCiudad.getText().toString(),
                            Integer.parseInt( binding.editTextNumPersonas.getText().toString() ),
                            Integer.parseInt( binding.editTextNumHabitaciones.getText().toString() ),
                            Integer.parseInt( binding.editTextNumBanos.getText().toString() ),
                            Integer.parseInt( binding.editTextNumAseos.getText().toString() )
                    );
                    editar( inmueble.getIdInmueble() , inmModificado );

                    Navigation.findNavController(view).navigate( R.id.inmueblesFragment );
                }
                else{  // Crear NUEVO
                    String nombre = binding.editTextNombre.getText().toString();
                    String calle = binding.editTextCalle.getText().toString();
                    String ciudad = binding.editTextCiudad.getText().toString();
                    Integer numPersonas =  Integer.parseInt( binding.editTextNumPersonas.getText().toString() );
                    Integer numHabitaciones =  Integer.parseInt( binding.editTextNumHabitaciones.getText().toString() );
                    Integer numBanos =  Integer.parseInt( binding.editTextNumBanos.getText().toString() );
                    Integer numAseos =  Integer.parseInt( binding.editTextNumAseos.getText().toString() );
                    Inmueble nuevoInmueble = new Inmueble( nombre, calle, ciudad, numPersonas, numHabitaciones, numBanos, numAseos );
                    guardar( nuevoInmueble );

                    Navigation.findNavController(view).navigate( R.id.inmueblesFragment );
                }

            }
        });

        binding.btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( binding.btnEliminar.getText().equals("Eliminar") ){
                    eliminar( inmueble.getIdInmueble() );

                    Navigation.findNavController(v).navigate( R.id.inmueblesFragment );
                }
                else{  // Cancelar  ir atrás
                    Navigation.findNavController(v).navigate( R.id.inmueblesFragment );
                }
            }
        });
    }

    public void guardar( Inmueble inmueble ) {
        RetrofitClient retrofitClient = RetrofitClient.getInstance();
        APIServiceInmueble apiService = retrofitClient.getRetrofit().create( APIServiceInmueble.class );

        Call<Inmueble> call = apiService.createInmueble( inmueble );  //  POST con Retrofit
        call.enqueue( new Callback<Inmueble>() {
            @Override
            public void onResponse(Call<Inmueble> call, Response<Inmueble> response) {
                if ( response.isSuccessful() ) {
                    Toast.makeText(getContext(), "¡Guardado!", Toast.LENGTH_LONG).show();
                    limpiarCamposFragment();
                }
            }
            @Override
            public void onFailure(Call<Inmueble> call, Throwable t) {
                Log.e("Error con Log.e", "¡¡onFailure Error al Guardar", t);
            }
        });
    }

    public void editar( Integer idInmueble , Inmueble inmueble ) {
        RetrofitClient retrofitClient = RetrofitClient.getInstance();
        APIServiceInmueble apiService = retrofitClient.getRetrofit().create( APIServiceInmueble.class );

        Call<Inmueble> call = apiService.updateInmueble( idInmueble , inmueble );
        call.enqueue( new Callback<Inmueble>() {
            @Override
            public void onResponse(Call<Inmueble> call, Response<Inmueble> response) {
                if ( response.isSuccessful() ) {
                    Toast.makeText(getContext(), "¡Editado!", Toast.LENGTH_LONG).show();
                    limpiarCamposFragment();
                }
            }
            @Override
            public void onFailure(Call<Inmueble> call, Throwable t) {
                Log.e("Error con Log.e", "¡¡onFailure Error Editar", t);
            }
        });
    }

    public void eliminar( Integer idInmueble ) {
        RetrofitClient retrofitClient = RetrofitClient.getInstance();
        APIServiceInmueble apiService = retrofitClient.getRetrofit().create( APIServiceInmueble.class );

        Call<Void> call = apiService.deleteInmueble( idInmueble );
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
        binding.editTextCalle.setText("");
        binding.editTextCiudad.setText("");
        binding.editTextNumPersonas.setText("");
        binding.editTextNumHabitaciones.setText("");
        binding.editTextNumBanos.setText("");
        binding.editTextNumAseos.setText("");
    }

    public void APIobtenerInmueble( Integer idInmueble ) {
        RetrofitClient retrofitClient = RetrofitClient.getInstance();
        APIServiceInmueble apiService = retrofitClient.getRetrofit().create( APIServiceInmueble.class );

        Call<Inmueble> apiCall = apiService.getInmueble( idInmueble );
        apiCall.enqueue( new Callback<Inmueble>() {      // LAMADA ASINC call.enqueue
            @Override
            public void onResponse(Call<Inmueble> call, Response<Inmueble> response) {
                if ( response.isSuccessful() && response.body() != null ) {

                    Inmueble inmueble = response.body();

                    binding.editTextNombre.setText( inmueble.getNombre() );
                    binding.editTextCalle.setText( inmueble.getCalle() );
                    binding.editTextCiudad.setText( inmueble.getCiudad() );
                    binding.editTextNumPersonas.setText( String.valueOf( inmueble.getNumPersonas() ) );
                    binding.editTextNumHabitaciones.setText( String.valueOf( inmueble.getNumHabitaciones() ) );
                    binding.editTextNumBanos.setText( String.valueOf( inmueble.getNumBanos() ) );
                    binding.editTextNumAseos.setText( String.valueOf( inmueble.getNumAseos() ) );

                    Log.d("RESPONSE", "Código: " + response.code() + " Respuesta: " + inmueble.toString() );

                } else {
                    Log.d("ERROR", "Código: " + response.code() + " Mensaje: " + response.message());
                }
            }
            @Override
            public void onFailure(Call<Inmueble> call, Throwable t) {
                Log.e("Error con Log.e", "¡¡onFailure Error al obtener", t);
            }
        });
    }

}