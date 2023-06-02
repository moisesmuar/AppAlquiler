package com.example.appalquiler.Fragmentos;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appalquiler.API.RetrofitClient;
import com.example.appalquiler.APIInterfaces.APIServiceAlquiler;
import com.example.appalquiler.APIInterfaces.APIServiceInmueble;
import com.example.appalquiler.Clases.Alquiler;
import com.example.appalquiler.Clases.Inmueble;
import com.example.appalquiler.Clases.reciclerViewCalendario;
import com.example.appalquiler.Miscelanea.CalendarAdapter;
import com.example.appalquiler.Miscelanea.CalendarHorizontalAdapter;
import com.example.appalquiler.R;
import com.example.appalquiler.SharedPreferencesManager;
import com.example.appalquiler.databinding.FragmentCalendarHorizontalBinding;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CalendarHorizontalFragment extends Fragment {

    private FragmentCalendarHorizontalBinding binding;

    private List<reciclerViewCalendario> listaRvCalendarios = new ArrayList<>();
    private LinearLayout linearParaAnadirRecicler;

    private Calendar calReferenciaMes = Calendar.getInstance(new Locale("es", "ES"));  // fecha actual
    private SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy", new Locale("es", "ES"));

    private List<TextView> tvInmuebles = new ArrayList<>();
    private List<RecyclerView> rvCalendarios = new ArrayList<>();

    private List<Inmueble> listaInmuebles = new ArrayList<>(); // lista inmuebles API
    Map<String, List<Alquiler>> alquileresDeInmueblesMap = new HashMap<>();  // almacenar listado alquiler cada inmueble

    public CalendarHorizontalFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCalendarHorizontalBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Ocultar el teclado virtual
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        linearParaAnadirRecicler = binding.idLinearCalenHorizont;

        obtenerInmuebles();

        // Establecer mes, dia mes año seleccionado
        binding.txtCurrentMonth.setText( sdf.format( calReferenciaMes.getTime() ) );

        binding.calendarPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calReferenciaMes.add( Calendar.MONTH, -1 );
                binding.txtCurrentMonth.setText( sdf.format( calReferenciaMes.getTime() ) );

                // recorrer listado obj con rv calendarios, llamar metodo mesAnterior()
                for( reciclerViewCalendario rvc : listaRvCalendarios ){
                    rvc.mesAnterior();
                }
            }
        });
        binding.calendarNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calReferenciaMes.add( Calendar.MONTH, 1 );
                binding.txtCurrentMonth.setText( sdf.format( calReferenciaMes.getTime() ) );

                for( reciclerViewCalendario rvc : listaRvCalendarios ){
                    rvc.mesPosterior();
                }
            }
        });

        return view;
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

                    Log.d("RESPONSE", "Código: " + response.code() + " obtenerInmuebles(inmueble)" );
                    for ( Inmueble inmueble : listaRespuesta ) {
                        Log.d("Lectura ", " " + inmueble.toString() );
                    }

                    listaInmuebles.clear();
                    listaInmuebles.addAll( listaRespuesta );

                    for ( Inmueble inmueble : listaInmuebles ) {

                        listaRvCalendarios.add( new reciclerViewCalendario(
                                inmueble, linearParaAnadirRecicler, getContext() )
                        );
                    }

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