package com.example.appalquiler.Fragmentos;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.appalquiler.API.RetrofitClient;
import com.example.appalquiler.Clases.Alquiler;
import com.example.appalquiler.APIInterfaces.APIServiceAlquiler;
import com.example.appalquiler.Clases.Usuario;
import com.example.appalquiler.Dialog.DatePickerFragment;
import com.example.appalquiler.R;
import com.example.appalquiler.SharedPreferencesManager;
import com.example.appalquiler.databinding.FragmentAlquileresFormBinding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AlquileresFormFragment extends Fragment {

    private FragmentAlquileresFormBinding binding;
    private Alquiler alquiler;
    SharedPreferencesManager sessionManager;
    Bundle bundle;
    private int modoSeleccion;
    private final SimpleDateFormat sdf_bd = new SimpleDateFormat("yyyy-MM-dd");
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

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

        sessionManager = new SharedPreferencesManager( requireContext() );
        if ( !sessionManager.isLogin() ) { // Usuario logeado? no. redirigir a fragmento login
            Navigation.findNavController(view).navigate( R.id.loginFragment );
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bundle = getArguments();

        // Edición
        // key : "alquilerEdicion" proviene de click sobre tarjeta Alquiler
        // o sobre la sucesion de fragments en una "accion" de edición
        if( bundle.getSerializable("alquilerEdicion") != null ) {

            this.alquiler = (Alquiler) bundle.getSerializable("alquilerEdicion");
            modoSeleccion = 2;

            binding.editTextFhInicio.setText( alquiler.getFhinicio() );
            binding.editTextFhFin.setText(  alquiler.getFhfin() );
            binding.editTextNombreInmueble.setText( alquiler.getInmueble().getNombre() );
            binding.editTextNombreCliente.setText( alquiler.getCliente().getNombre() );
            binding.editTextNombrePortal.setText( alquiler.getPortal().getNombre() );

            binding.btnGuardar.setText("Modificar");
            binding.btnEliminar.setText("Eliminar");
        }

        // Creacción
        // key : "alquilerNuevo" proviene de click sobre btMasAlquileres
        // o sobre la sucesion de fragments en una "accion" de crear nuevo objeto Alquiler
        if( bundle.getSerializable("alquilerNuevo") != null ) {

            this.alquiler = (Alquiler) bundle.getSerializable("alquilerNuevo");
            modoSeleccion = 1;

            if( null !=  alquiler.getFhinicio() ){
                binding.editTextFhInicio.setText( alquiler.getFhinicio() );
            }
            if( null !=  alquiler.getFhfin() ){
                binding.editTextFhFin.setText(  alquiler.getFhfin() );
            }
            if( null !=  alquiler.getInmueble() ){
                binding.editTextNombreInmueble.setText( alquiler.getInmueble().getNombre() );
            }
            if( null !=  alquiler.getCliente() ){
                binding.editTextNombreCliente.setText( alquiler.getCliente().getNombre() );
            }
            if( null !=  alquiler.getPortal() ){
                binding.editTextNombrePortal.setText( alquiler.getPortal().getNombre() );
            }

            binding.btnGuardar.setText("Añadir");
            binding.btnEliminar.setText("Cancelar");
        }

        binding.btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                Usuario user = sessionManager.getSpUser();

                if (alquiler != null   // Validar datos
                        && alquiler.getFhinicio() != null
                        && alquiler.getFhfin() != null
                        && alquiler.getInmueble() != null
                        && alquiler.getCliente() != null
                        && alquiler.getPortal() != null
                        && user != null
                        && user.getEmpresa() != null) {

                    if( binding.btnGuardar.getText().equals("Modificar") ){

                        Alquiler alquilerMod = new Alquiler(
                            alquiler.getFhinicio(),
                            alquiler.getFhfin(),
                            alquiler.getInmueble(),
                            alquiler.getCliente(),
                            alquiler.getPortal(),
                            user.getEmpresa()
                        );
                        editar( alquiler.getIdAlquiler() , alquilerMod );
                        Navigation.findNavController(view).navigate( R.id.alquileresFragment );

                    } else{   //  Nuevo Alquiler

                            Alquiler alquilerNuevo = new Alquiler(
                                    alquiler.getFhinicio(),
                                    alquiler.getFhfin(),
                                    alquiler.getInmueble(),
                                    alquiler.getCliente(),
                                    alquiler.getPortal(),
                                    user.getEmpresa()
                            );

                            // ver json para envio
                            Gson gson = new GsonBuilder().create();
                            String json = gson.toJson(alquilerNuevo);
                            System.out.println(json);

                            guardar( alquilerNuevo );
                            Navigation.findNavController(view).navigate( R.id.alquileresFragment );
                    }

                } else {
                    // Al menos uno de los valores es nulo
                    Toast.makeText(getContext(), "¡Rellena todos los campos!", Toast.LENGTH_LONG).show();
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





        // Guardar en objeto Alquiler la fecha que DatePickerDialog, escribe en EditText
        binding.editTextFhInicio.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                alquiler.setFhinicio( s.toString() );
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        binding.editTextFhFin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                alquiler.setFhfin( s.toString() );
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // EditText Clicables, aparece DatePickerDialog
        binding.editTextFhInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(binding.editTextFhInicio);
            }
        });
        binding.editTextFhFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(binding.editTextFhFin);
            }
        });

        // Botones para lanzar DatePickerDialog
        binding.ibtnFhini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(binding.editTextFhInicio);
            }
        });
        binding.ibtnFhfin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(binding.editTextFhFin);
            }
        });

        // Ir a listado para selección de INMUEBLE o CLIENTE o PORTAL   accion crear || accion editar
        binding.ibtnInmuebleAlquilerBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle =  EstablecerBundleKeys_SegunAccion();
                Navigation.findNavController(v).navigate( R.id.inmueblesFragment, bundle );
            }
        });
        binding.ibtnClienteAlquilerBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle =  EstablecerBundleKeys_SegunAccion();
                Navigation.findNavController(v).navigate( R.id.clientesFragment, bundle );
            }
        });
        binding.ibtnPortalBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle =  EstablecerBundleKeys_SegunAccion();
                Navigation.findNavController(v).navigate( R.id.portalesFragment, bundle );
            }
        });

    }

    /**
     * Configura un Bundle, para usarlo en el flujo de las acciones que defino.
     * editar un objeto Alquiler y crear nuevo Alquiler
     */
    private Bundle EstablecerBundleKeys_SegunAccion(){
        Bundle bundle = new Bundle();
        if( modoSeleccion == 2 ){
            // Entrada clic tarjeta alquiler
            // Si lo es por la edición de uno existente.
            bundle.putInt("modoSeleccion", 2 );
            bundle.putSerializable("alquilerEdicion" , (Serializable) alquiler );
        }
        if( modoSeleccion == 1 ){
            // Entrada a este fragment por boton +
            // Si es un recorrido de fragment: inmuebles, clientes, portales..por un alquiler nuevo
            bundle.putInt("modoSeleccion", 1 );
            bundle.putSerializable("alquilerNuevo" , (Serializable) alquiler );
        }
        return bundle;
    }

    /**
     *  Reestablecer instancia de Alquiler usado en una edición o creacción
     *  borra keys y su contenido.
     */
    private void vaciarAlquilerYkey() {
        alquiler = new Alquiler();
        if ( bundle.containsKey("alquilerNuevo" )) {
            bundle.remove("alquilerNuevo");
        }
        if ( bundle.containsKey("alquilerEdicion" )) {
            bundle.remove("alquilerEdicion");
        }
    }

    private void showDatePickerDialog(final EditText editText) {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // final String selectedDate = twoDigits(day) + "/" + twoDigits(month+1) + "/" + year;
                final String selectedDate = year +"-"+ dosDigitos(month+1) +"-"+ dosDigitos(day);

                editText.setText( selectedDate );   // String ejemplo 08/06/2023
            }
        });

        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }
    private String dosDigitos(int n) {
        return (n<=9) ? ("0"+n) : String.valueOf(n);
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
                    vaciarAlquilerYkey();

                    Log.d("onResponse", "Respuesta exitosa: ");
                }else {
                    Log.e("onResponse", "Error en la respuesta: " + response.message());
                }
            }
            @Override
            public void onFailure(Call<Alquiler> call, Throwable t) {
                Log.e("onFailure", "Error en la petición: " + t.getMessage());
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
                    vaciarAlquilerYkey();

                    Log.d("onResponse", "Respuesta exitosa: " );
                }
                else {
                    Log.e("onResponse", "Error en la respuesta: " + response.message());
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

                    Log.d("onResponse", "Respuesta exitosa: ");
                }else {
                    Log.e("onResponse", "Error en la respuesta: " + response.message());
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("Error con Log.e", "¡¡onFailure Error Eliminar ", t);
            }
        });
    }

    public void limpiarCamposFragment( ) {
        binding.editTextFhInicio.setText("");
        binding.editTextFhFin.setText("");
        binding.editTextNombreInmueble.setText("");
        binding.editTextNombreCliente.setText("");
        binding.editTextNombrePortal.setText("");
    }


}