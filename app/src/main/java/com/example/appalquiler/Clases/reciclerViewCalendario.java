package com.example.appalquiler.Clases;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appalquiler.API.RetrofitClient;
import com.example.appalquiler.APIInterfaces.APIServiceAlquiler;
import com.example.appalquiler.Miscelanea.CalendarHorizontalAdapter;
import com.example.appalquiler.R;
import com.google.android.material.button.MaterialButton;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Obtiene alquileres del mes de un inmueble, lo
 */
public class reciclerViewCalendario {

    private Calendar lastDayInCalendar = Calendar.getInstance(new Locale("es", "ES"));
    private SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy", new Locale("es", "ES"));
    private Calendar cal = Calendar.getInstance(new Locale("es", "ES"));  // fecha actual

    private Calendar currentDate = Calendar.getInstance(new Locale("es", "ES")); // día, mes y año actual
    private int currentDay = currentDate.get(Calendar.DAY_OF_MONTH);
    private int currentMonth = currentDate.get(Calendar.MONTH);
    private int currentYear = currentDate.get(Calendar.YEAR);

    private int selectedDay = currentDay;   // guardar selección
    private int selectedMonth = currentMonth;
    private int selectedYear = currentYear;
    private ArrayList<Date> dates = new ArrayList<>(); // ej [2023-04-01, 2023-04-02, 2023-04-03, ...] mes leido

    private ConstraintLayout constraintLayout_btn_tv;
    private MaterialButton materialButton;
    private TextView tvPorcentajeOcupacion;
    LinearLayout linearParaAnadirRecicler;
    RecyclerView recyclerView;

    private Inmueble inmueble;
    private List<Alquiler> alquilerList;

    private Usuario user;
    private Context context;


    public reciclerViewCalendario( @NonNull Inmueble inmueble,
                                   LinearLayout linearParaAnadirRecicler,
                                   Usuario user, Context context
                                   ) {
        this.inmueble = inmueble;
        //this.alquilerList = alquilerList;  // lo consigo en peticion aqui dentro
        this.linearParaAnadirRecicler = linearParaAnadirRecicler;
        this.user = user;
        this.context = context;

        // Maximo mes que muestra calendario
        lastDayInCalendar.add(Calendar.MONTH, 12);

        // Consultas por mes y año - Peticion
        obtenerAlquileresMesDelInmueble( );
    }
    /**
     * Ir al mes anterior. Primero, asegúrese de que el mes actual (cal)
     * es posterior a la fecha actual, por lo que no puede ir antes del mes actual.
     * Luego reste un mes del lodo. Finalmente, pregunte si cal es igual a la fecha actual.
     * Si es así, entonces no desea dar @param changeMonth, de lo contrario changeMonth como cal.
     */
    public void mesAnterior(){
        cal.add( Calendar.MONTH, -1 );
        if ( cal.equals(currentDate) ){
            linearParaAnadirRecicler.removeView( recyclerView );  // borrado de anterior creado
            linearParaAnadirRecicler.removeView( constraintLayout_btn_tv );

            obtenerAlquileresMesDelInmueble( );
        }
        else{
            linearParaAnadirRecicler.removeView( recyclerView );  // borrado de anterior creado
            linearParaAnadirRecicler.removeView( constraintLayout_btn_tv );

            obtenerAlquileresMesDelInmueble( );
        }
    }
    /**
     * Ir al mes siguiente. Primero verifique si el mes actual (cal) es anterior a lastDayInCalendar,
     * para que no puedas ir después del último mes posible. Luego agregue un mes a cal.
     * Luego pon @param changeMonth.
     */
    public void mesPosterior(){
        cal.add( Calendar.MONTH, 1 );

        linearParaAnadirRecicler.removeView( recyclerView );  // borrado de anterior creado
        linearParaAnadirRecicler.removeView( constraintLayout_btn_tv );

        obtenerAlquileresMesDelInmueble( );
    }

    private void montarCalendario( ) {
        LinearSnapHelper snapHelper = new LinearSnapHelper();

        recyclerView = new RecyclerView( context );
        recyclerView.setId( View.generateViewId() ); // Asignar un ID único al RecyclerView
        snapHelper.attachToRecyclerView( recyclerView );

        constraintLayout_btn_tv = new ConstraintLayout(context);
        materialButton = new MaterialButton(context);
        materialButton.setText( inmueble.getNombre() );
        materialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Crear el Bundle con los datos a pasar al fragmento de destino
                Bundle bundle = new Bundle();
                bundle.putSerializable("inmueble", (Serializable) inmueble);
                Navigation.findNavController(view).navigate(R.id.calendarDetalleFagment, bundle);
            }
        });
        tvPorcentajeOcupacion = new TextView(context);
        tvPorcentajeOcupacion.setText("0 %");  // Insertaré porcentaje ocupación.

        // Asignación Parámetros ConstraintLayout
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(0, 45, 0, 20);
        constraintLayout_btn_tv.setLayoutParams(layoutParams);

        // Asignación Parámetros materialButton - margen izquierdo para el Button
        ConstraintLayout.LayoutParams buttonParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        buttonParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        buttonParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        buttonParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
        buttonParams.setMargins(30, 0, 0, 0);
        materialButton.setLayoutParams(buttonParams);

        // Asignación Parámetros tvPorcentajeOcupacion -  margen derecho para el TextView
        ConstraintLayout.LayoutParams tvParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        tvParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
        tvParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        tvParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
        tvParams.setMargins(0, 0, 40, 0);
        tvPorcentajeOcupacion.setLayoutParams(tvParams);

        constraintLayout_btn_tv.addView( materialButton );
        constraintLayout_btn_tv.addView( tvPorcentajeOcupacion );

        linearParaAnadirRecicler.addView( constraintLayout_btn_tv );
        linearParaAnadirRecicler.addView( recyclerView );
    }

    /**
     * changeMonth no se lo paso nulo, si mes siguiente o anterior no es actual
     * @param changeMonth
     */
    private void setUpCalendar( Calendar changeMonth ) {

        // Copia del obj calendario
        Calendar monthCalendar = (Calendar) cal.clone();
        // num maximos dias mes actual objeto cal
        int maxDaysInMonth = cal.getActualMaximum( Calendar.DAY_OF_MONTH );

        // Verificar si cal no está en el currentMonth
        if ( cal.get(Calendar.MONTH) != currentDate.get(Calendar.MONTH) ) {
            // El mes de cal es diferente al currentMonth
            selectedDay = changeMonth.getActualMinimum( Calendar.DAY_OF_MONTH );
            selectedMonth = changeMonth.get( Calendar.MONTH );
            selectedYear = changeMonth.get( Calendar.YEAR );
        } else {
            selectedDay = currentDay;
            selectedMonth = currentMonth;
            selectedYear = currentYear;
        }

        selectedDay = changeMonth.getActualMinimum( Calendar.DAY_OF_MONTH );
        selectedMonth = changeMonth.get( Calendar.MONTH );
        selectedYear = changeMonth.get( Calendar.YEAR );

        dates.clear(); // borrar ArrayList fechas, monthCalendar primer dia de mes
        monthCalendar.set( Calendar.DAY_OF_MONTH, 1 ); // Establecer en 1 dia mes actual en copiaCalendario

        // Agregar al array de fechas -hasta- max dias del mes.
        int currentPosition = 0;  // posición primer dia seleccionado
        while ( dates.size() < maxDaysInMonth ) {
            if ( monthCalendar.get( Calendar.DAY_OF_MONTH ) == selectedDay ) {
                currentPosition = dates.size();
            }
            dates.add( monthCalendar.getTime() );  // ej [2023-04-01, 2023-04-02, 2023-04-03, ...] mes leido
            monthCalendar.add( Calendar.DAY_OF_MONTH, 1 );
        }

        // Asignar % ocupacion mensual
        int totalDiasAlquilado = 0;
        if ( alquilerList.size() > 0 ) {
            for (Alquiler alquiler : alquilerList) {
                totalDiasAlquilado += alquiler.totaldiasAlquiler();
            }
            //Log.d("Total días de alquiler", String.valueOf(totalDiasAlquilado));
        }
        double porcentaje = 0;
        if (dates.size() > 0 ) {
            porcentaje = (totalDiasAlquilado * 100) / dates.size();
        }
        DecimalFormat decimalFormat = new DecimalFormat("#");
        tvPorcentajeOcupacion.setText( decimalFormat.format(porcentaje) + " %" );

        //Log.d("totalDiasAlquilado ", ": " + totalDiasAlquilado);
        //Log.d("total dates", ": " + dates.size());

        // Asignar vista de calendario
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                context, LinearLayoutManager.HORIZONTAL, false
        );
        recyclerView.setLayoutManager(layoutManager);
        CalendarHorizontalAdapter calendarHorizontalAdapter = new CalendarHorizontalAdapter(
                context, dates, currentDate, changeMonth , alquilerList
        );
        recyclerView.setAdapter( calendarHorizontalAdapter );

        // Centrar dia actual si no es 3 primeros 1 2 3 o 3 últimos 29 30 31
        if ( currentPosition > 2 ) {
            recyclerView.scrollToPosition( currentPosition - 3 );
        } else if ( maxDaysInMonth - currentPosition < 2 ) {
            recyclerView.scrollToPosition(currentPosition);
        } else {
            recyclerView.scrollToPosition(currentPosition);
        }

        calendarHorizontalAdapter.setOnItemClickListener(new CalendarHorizontalAdapter.OnItemClickListener() {
            @Override
            public void onItemClick( int position ) {  // Cuando se hace click en elemento
            }
        });
    }

    public void obtenerAlquileresMesDelInmueble(  ){
        RetrofitClient retrofitClient = RetrofitClient.getInstance();
        APIServiceAlquiler apiService = retrofitClient.getRetrofit().create( APIServiceAlquiler.class );

        Log.d("calReferenciaMes.get(Calendar.MONTH),", ": " + cal.get(Calendar.MONTH )+ 1 );
        Log.d("calReferenciaMes.get(Calendar.YEAR),", ": " + cal.get(Calendar.YEAR) );

        Call<List<Alquiler>> call = apiService.getAlquileresMesAnoInmueble_deEmpresa(
                // convención de indexación de meses en Calendar: enero 0 febrero 1
                cal.get(Calendar.MONTH )+ 1,  // mes actual
                cal.get(Calendar.YEAR),
                inmueble.getIdInmueble(),
                user.getEmpresa().getNombre()
        );
        call.enqueue( new Callback<List<Alquiler>>() {
            @Override
            public void onResponse( Call<List<Alquiler>> call, Response<List<Alquiler>> response) {
                if ( response.isSuccessful() ) {

                    List<Alquiler> list = response.body();
                    Log.d("RESPONSE", "Código: " + response.code() + " obtenerAlquileresMesDelInmueble() " );
                    for ( Alquiler alq : list ) {
                        Log.d("Lectura ", " "+ inmueble.getNombre()+ " "+ alq.toString() );
                    }

                    alquilerList = list;

                    montarCalendario();
                    setUpCalendar( cal );

                } else {
                    Log.e("onResponse", "Not succesful");
                }
            }
            @Override
            public void onFailure( Call<List<Alquiler>> call, Throwable t ) {
                Log.e("onFailure", "Error petición alquileres", t );
            }

        });

    }

}
