package com.example.appalquiler.Clases;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appalquiler.API.RetrofitClient;
import com.example.appalquiler.APIInterfaces.APIServiceAlquiler;
import com.example.appalquiler.Miscelanea.CalendarHorizontalAdapter;
import com.example.appalquiler.R;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    private TextView tvNombreInmueble;
    private Inmueble inmueble;
    private List<Alquiler> alquilerList;

    LinearLayout linearParaAnadirRecicler;
    RecyclerView recyclerView;
    private Context context;

    public reciclerViewCalendario( @NonNull Inmueble inmueble,

                                   LinearLayout linearParaAnadirRecicler,
                                   Context context
                                   ) {
        this.inmueble = inmueble;
        //this.alquilerList = alquilerList;  // lo consigo en peticion aqui dentro
        this.linearParaAnadirRecicler = linearParaAnadirRecicler;
        this.context = context;

        // Maximo mes que muestra calendario
        lastDayInCalendar.add(Calendar.MONTH, 12);


        // Consultas por mes y año

        obtenerAlquileresMesDelInmueble( );  // Peticion
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
            linearParaAnadirRecicler.removeView( tvNombreInmueble );

            obtenerAlquileresMesDelInmueble( );

            //montarCalendario();
                        // setUpCalendar( null );
            //setUpCalendar( cal );
        }
        else{
            linearParaAnadirRecicler.removeView( recyclerView );  // borrado de anterior creado
            linearParaAnadirRecicler.removeView( tvNombreInmueble );

            obtenerAlquileresMesDelInmueble( );

            //montarCalendario();
            //setUpCalendar( cal );
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
        linearParaAnadirRecicler.removeView( tvNombreInmueble );

        obtenerAlquileresMesDelInmueble( );

       // montarCalendario();
        // setUpCalendar( cal );
    }

    private void montarCalendario( ) {

        LinearSnapHelper snapHelper = new LinearSnapHelper();

        recyclerView = new RecyclerView( context );
        snapHelper.attachToRecyclerView( recyclerView );

        tvNombreInmueble = new TextView( context ); // Crear textView dinámicamente
        tvNombreInmueble.setText( inmueble.getNombre() );

        tvNombreInmueble.setOnClickListener(new View.OnClickListener() {  // Ir a CalendarDetalle
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("inmueble" , (Serializable) inmueble );
                Navigation.findNavController(view).navigate( R.id.calendarDetalleFagment, bundle );
                // Navigation.findNavController(view).navigate(R.id.action_calendarHorizontalFragment_to_calendarDetalleFagment,bundle);
            }
        });

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(  // Crear parámetros de diseño
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(30, 40, 0, 20);
        tvNombreInmueble.setLayoutParams(params);

        recyclerView.setId( View.generateViewId() ); // Asignar un ID único al RecyclerView

        // Agregar tv y rv al LinearLayout
        linearParaAnadirRecicler.addView( tvNombreInmueble );
        linearParaAnadirRecicler.addView( recyclerView );

        // ¿necesito añadirlo lo hago, esta por hacer?
        // rvCalendarios.add( recyclerView ); // Agregar referencia del rv a ArrayList
        // tvInmuebles.add( textView );

    }

    /**
     * changeMonth no se lo paso nulo, si mes siguiente o anterior no es actual
     * @param changeMonth
     */
    private void setUpCalendar( Calendar changeMonth ) {

        // Establecer mes, dia mes año seleccionado
        // binding.txtCurrentMonth.setText( sdf.format( cal.getTime() ) );


        // Copia del obj calendario
        Calendar monthCalendar = (Calendar) cal.clone();
        // num maximos dias mes actual objeto cal
        int maxDaysInMonth = cal.getActualMaximum( Calendar.DAY_OF_MONTH );

        // Si CambioDeMes no Nulo coger dia,mes,año de él
        /*
        if ( changeMonth != null ) {
            selectedDay = changeMonth.getActualMinimum( Calendar.DAY_OF_MONTH );
            selectedMonth = changeMonth.get( Calendar.MONTH );
            selectedYear = changeMonth.get( Calendar.YEAR );
        } else {
            selectedDay = currentDay;
            selectedMonth = currentMonth;
            selectedYear = currentYear;
        }*/

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

        // borrar ArrayList fechas, monthCalendar primer dia de mes
        dates.clear();
        // Establecer en 1 dia mes actual en copiaCalendario
        monthCalendar.set( Calendar.DAY_OF_MONTH, 1 );

        // Agregar al array de fechas -hasta- max dias del mes.
        // currentPosition posición primer dia seleccionado
        int currentPosition = 0;
        while ( dates.size() < maxDaysInMonth ) {
            if ( monthCalendar.get( Calendar.DAY_OF_MONTH ) == selectedDay ) {
                currentPosition = dates.size();
            }
            dates.add( monthCalendar.getTime() );  // ej [2023-04-01, 2023-04-02, 2023-04-03, ...] mes leido
            monthCalendar.add( Calendar.DAY_OF_MONTH, 1 );
        }

        // Asignar vista de calendario
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                context, LinearLayoutManager.HORIZONTAL, false
        );
        recyclerView.setLayoutManager(layoutManager);
        CalendarHorizontalAdapter calendarHorizontalAdapter = new CalendarHorizontalAdapter(
                context, dates, currentDate, changeMonth , alquilerList
        );
        recyclerView.setAdapter( calendarHorizontalAdapter );

        // Centrar dia actual si no es 3 primeros 1 2 3 o 3 ultimos 29 30 31
        if ( currentPosition > 2 ) {
            recyclerView.scrollToPosition( currentPosition - 3 );
        } else if ( maxDaysInMonth - currentPosition < 2 ) {
            recyclerView.scrollToPosition(currentPosition);
        } else {
            recyclerView.scrollToPosition(currentPosition);
        }

        // Editar listener y sobreescribir onItemClick
        calendarHorizontalAdapter.setOnItemClickListener(new CalendarHorizontalAdapter.OnItemClickListener() {
            @Override
            public void onItemClick( int position ) {  // Cuando se hace click en elemento
                //Calendar clickCalendar = Calendar.getInstance();  // nuevo calendario
                //clickCalendar.setTime( dates.get(position) );  // establecer fecha, que corresponde
                //selectedDay = clickCalendar.get( Calendar.DAY_OF_MONTH ); // actualizar variable dia seleccionaddo
            }
        });

    }

    /**
     * obtiene List<Alquiler> de 1 Inmueble y lo guarda en
     * Map<String, List<Alquiler>> alquileresDeInmueblesMap
     */
    public void obtenerAlquileresMesDelInmueble(  ){
        RetrofitClient retrofitClient = RetrofitClient.getInstance();
        APIServiceAlquiler apiService = retrofitClient.getRetrofit().create( APIServiceAlquiler.class );

        //Log.d("PETICION Alq Mes Inmueble", "selectedMonth: "  + currentDate.get( Calendar.MONTH )+ " selectedYear: " + selectedYear+" idInmueble: " + inmueble.getIdInmueble() );

        Log.d("calReferenciaMes.get(Calendar.MONTH),", ": " + cal.get(Calendar.MONTH )+ 1 );
        Log.d("calReferenciaMes.get(Calendar.YEAR),", ": " + cal.get(Calendar.YEAR) );

        Call<List<Alquiler>> call = apiService.getAlquileresMesAnoInmueble(
                // convención de indexación de meses en Calendar: enero 0 febrero 1
                cal.get(Calendar.MONTH )+ 1,  // mes actual
                cal.get(Calendar.YEAR),
                inmueble.getIdInmueble()
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

                    // Guardar respuesta en Map con clave
                    // alquileresDeInmueblesMap.put( inmueble.getNombre() , alquileresList );


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
