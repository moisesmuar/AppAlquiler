package com.example.appalquiler.Fragmentos;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appalquiler.API.RetrofitClient;
import com.example.appalquiler.Clases.Alquiler;
import com.example.appalquiler.APIInterfaces.APIServiceAlquiler;
import com.example.appalquiler.Clases.Inmueble;
import com.example.appalquiler.Clases.Usuario;
import com.example.appalquiler.Miscelanea.CalendarAdapter;
import com.example.appalquiler.R;
import com.example.appalquiler.SharedPreferencesManager;
import com.example.appalquiler.databinding.FragmentCalendarDetalleBinding;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CalendarDetalleFragment extends Fragment implements CalendarAdapter.OnItemListener {

    private FragmentCalendarDetalleBinding binding;
    SharedPreferencesManager sessionManager;
    private Usuario user;

    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private TextView tvTituloNombreInmueble;
    private LocalDate selectedDate;

    private Inmueble inmueble;

    private List<Alquiler> alquileresList; // lista objetos Alquiler recibida de API

    public CalendarDetalleFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar diseño del fragmento
        binding = FragmentCalendarDetalleBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // mostrar barra de navegación botón de retroceso en barra de acción de la actividad actual
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

        // Obtener bundle de argumentos del fragment
        Bundle bundle = getArguments();
        if( bundle != null ) {
            // inmueble del cual obtendremos los alquileres.
            this.inmueble = (Inmueble) bundle.getSerializable("inmueble");
        }

        // Fecha actual basada reloj del sistema ej 2023-04-03
        selectedDate = LocalDate.now();
        this.calendarRecyclerView =  binding.calendarRecyclerView;
        this.monthYearText =  binding.monthYearTV;

        // QUE Inmueble
        binding.tvTituloNombreInmueble.setText(  inmueble.getNombre() );

        establecerVistaMes();
        obtenerAlquileresMesDelInmueble_deEmpresa_establecerVistaMes_datosAPI();

        binding.btnIrMesAnterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedDate = selectedDate.minusMonths(1);
                establecerVistaMes();
                obtenerAlquileresMesDelInmueble_deEmpresa_establecerVistaMes_datosAPI();
            }
        });
        binding.btnIrMesPosterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedDate = selectedDate.plusMonths(1);
                establecerVistaMes();
                obtenerAlquileresMesDelInmueble_deEmpresa_establecerVistaMes_datosAPI();
            }
        });
    }

    private void establecerVistaMes() {
        monthYearText.setText( mesyAnoDelaFecha(selectedDate) );
        ArrayList<String> daysInMonth = daysInMonthArray(selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(getContext(), daysInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager( getContext(), 7);
        calendarRecyclerView.setLayoutManager( layoutManager );
        calendarRecyclerView.setAdapter( calendarAdapter );
    }

    private void establecerVistaMes_datosAPI() {
        monthYearText.setText( mesyAnoDelaFecha(selectedDate) );
        ArrayList<String> daysInMonth = daysInMonthArray(selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(getContext(),daysInMonth, this, alquileresList, selectedDate);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager( getContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    private ArrayList<String> daysInMonthArray(LocalDate date)
    {
        ArrayList<String> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);

        int daysInMonth = yearMonth.lengthOfMonth();

        LocalDate firstOfMonth = selectedDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();

        for(int i = 1; i <= 42; i++)
        {
            if(i <= dayOfWeek || i > daysInMonth + dayOfWeek)
            {
                daysInMonthArray.add("");
            }
            else
            {
                daysInMonthArray.add(String.valueOf(i - dayOfWeek));
            }
        }
        return  daysInMonthArray;
    }

    /**
     * Representación legible de mes y año de una fecha en formato "MMMM yyyy"
     * in 2023-05-19 out "Mayo 2023"
     * @param date
     * @return String
     */
    private String mesyAnoDelaFecha(LocalDate date) {
        /*// pasar LocalDate a Date
        Date fecha = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
        SimpleDateFormat formatter = new SimpleDateFormat("MMMM yyyy", new Locale("es", "ES"));
        return formatter.format(fecha);*/

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy", new Locale("es", "ES"));
        return date.format( formatter );
    }

    @Override
    public void onItemClick(int position, String dayText, Alquiler alquiler) {
        /*if( !dayText.equals("") ) {
            String message = "Dia " + dayText + " " + mesyAnoDelaFecha( selectedDate );
            Toast.makeText( getContext(), message, Toast.LENGTH_LONG).show();
        }*/

        if ( alquiler != null ) {

            LocalDate fechaInicio = LocalDate.parse(alquiler.getFhinicio());
            LocalDate fechaFin = LocalDate.parse(alquiler.getFhfin());

            String message = "Entrada: " + fechaInicio.getDayOfMonth()
                    + " Salida: " + fechaFin.getDayOfMonth() + "\n"
                    + "Cliente: " + alquiler.getCliente().getNombre();

          /*  String message = "Entrada: " + alquiler.getFhinicio().getDayOfMonth()
                    + " Salida: " + alquiler.getFhfin().getDayOfMonth() + "\n"
                    + "Cliente: " + alquiler.getCliente().getNombre();*/

            Toast.makeText( getContext(), message, Toast.LENGTH_LONG).show();
        }

    }

    public void obtenerAlquileresMesDelInmueble_deEmpresa_establecerVistaMes_datosAPI() {
        RetrofitClient retrofitClient = RetrofitClient.getInstance();
        APIServiceAlquiler apiService = retrofitClient.getRetrofit().create( APIServiceAlquiler.class );

        Call<List<Alquiler>> call = apiService.getAlquileresMesAnoInmueble_deEmpresa(
                selectedDate.getMonthValue(),
                selectedDate.getYear(),
                inmueble.getIdInmueble(),
                user.getEmpresa().getNombre()
        );

        call.enqueue(new Callback<List<Alquiler>>() {     // LAMADA ASINC call.enqueue
            @Override
            public void onResponse(Call<List<Alquiler>> call, Response<List<Alquiler>> response) {
                if ( response.isSuccessful() ) {
                    alquileresList = response.body();

                    for ( Alquiler alquiler : alquileresList ) {
                        int idAlquiler = alquiler.getIdAlquiler();

                        System.out.println("Fecha inicio: " + alquiler.getFhinicio());
                        System.out.println("Fecha fin: " + alquiler.getFhfin());
                        Log.d("RESPONSE", "Código: " + response.code() + " Respuesta: " + alquiler.toString() );

                    }
                    establecerVistaMes_datosAPI();

                } else {
                    Log.e("onResponse", "Response not succesful");
                }
            }
            @Override
            public void onFailure(Call<List<Alquiler>> call, Throwable t) {
                Log.e("onFailure", "Error al obtener los alquileres", t);
            }
        });
    }

}