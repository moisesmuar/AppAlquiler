package com.example.appalquiler.Miscelanea;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appalquiler.Clases.Alquiler;
import com.example.appalquiler.R;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CalendarHorizontalAdapter extends RecyclerView.Adapter<CalendarHorizontalViewHolder> {

    private Context context;
    private ArrayList<Date> data;
    private Calendar currentDate;
    private Calendar changeMonth;
    private OnItemClickListener mListener;
    private int index = -1;
    private boolean selectCurrentDate = true;
    private int currentMonth;
    private int currentYear;
    private int currentDay;
    private int selectedDay;
    private int selectedMonth;
    private int selectedYear;
    SimpleDateFormat sdf = new SimpleDateFormat("EEE", new Locale("es", "ES"));
    private List<Alquiler> alquileresList = null;

    public CalendarHorizontalAdapter(Context context, ArrayList<Date> data,
                                     Calendar currentDate, Calendar changeMonth,
                                     List<Alquiler> alquilerList
                                     ) {
        this.context = context;
        this.data = data;
        this.currentDate = currentDate;
        this.changeMonth = changeMonth;
        this.alquileresList = alquilerList;

        currentMonth = currentDate.get( Calendar.MONTH );
        currentYear = currentDate.get( Calendar.YEAR );
        currentDay = currentDate.get( Calendar.DAY_OF_MONTH );

        if ( changeMonth != null ) {
            selectedDay = changeMonth.getActualMinimum(Calendar.DAY_OF_MONTH);
            selectedMonth = changeMonth.get(Calendar.MONTH);
            selectedYear = changeMonth.get(Calendar.YEAR);
        } else {
            selectedDay = currentDay;
            selectedMonth = currentMonth;
            selectedYear = currentYear;
        }

    }

    @NonNull
    @Override
    public CalendarHorizontalViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.custom_calendar_cell, parent, false);
        return new CalendarHorizontalViewHolder( view, mListener );
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    private void makeItemDisabled(CalendarHorizontalViewHolder holder) {  // gris desactivado
        holder.txtDay.setTextColor( Color.GRAY );
        holder.txtDayInWeek.setTextColor( Color.GRAY );
        holder.linearLayout.setBackgroundColor( Color.WHITE );
        holder.linearLayout.setEnabled(false);
    }

    private void makeItemSelected(CalendarHorizontalViewHolder holder) {   // dia selecionado
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(com.google.android.material.R.attr.colorPrimary, typedValue, true);
        int colorPrimary = typedValue.data;

        holder.txtDay.setTextColor(Color.parseColor("#FFFFFF"));
        holder.txtDayInWeek.setTextColor(Color.parseColor("#FFFFFF"));
        holder.linearLayout.setBackgroundColor( colorPrimary );
        holder.linearLayout.setEnabled(false);
    }

    private void makeItemDefault(CalendarHorizontalViewHolder holder) {
        holder.txtDay.setTextColor(Color.BLACK);
        holder.txtDayInWeek.setTextColor(Color.BLACK);
        holder.linearLayout.setBackgroundColor(Color.WHITE);
        holder.linearLayout.setEnabled(true);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarHorizontalViewHolder holder, int position) {

        Calendar cal = Calendar.getInstance();
        cal.setTime( data.get(position) );

        int displayMonth = cal.get( Calendar.MONTH );
        int displayYear = cal.get( Calendar.YEAR );

        // Establecer el número del día ( 1 2 3 )
        int displayDay = cal.get( Calendar.DAY_OF_MONTH );
        holder.txtDay.setText( String.valueOf( displayDay ) );

        // Establecer el día de la semana abreviado (LUN, MAR, etc.)
        Date dayInWeek = cal.getTime();
        String dayOfWeekAbbr = sdf.format(dayInWeek);
        holder.txtDayInWeek.setText(dayOfWeekAbbr);

        // Establecer celda color blanco, sin tema
        holder.itemView.setBackgroundColor(
                ContextCompat.getColor(context, R.color.md_theme_light_onError )
        );

        /**
         * Creo que puedes usar "cal.after(currentDate)" y "cal == currentDate",
         * pero no funcionó correctamente para mí, así que utilicé esta versión más larga. Aquí simplemente pregunto
         * si la fecha mostrada es posterior a la fecha actual o si es la fecha actual, en ese caso
         * se habilita el elemento y es posible hacer clic en él, de lo contrario se desactiva.
         * El valor de selectCurrentDate solo es válido al principio, será el día actual o el primer día,
         * por ejemplo, al iniciar la aplicación o al cambiar de mes.
         */

        if ( alquileresList != null ) {
            for( Alquiler alquiler : alquileresList ) {

                //Log.d("RECORRO alquileresList en onBindViewHolder del calendario", "....: " );
                //Log.d("ALQUILERRRR: ", "obj: " + alquiler.toString() );

                LocalDate fechaInicio = LocalDate.parse(alquiler.getFhinicio());
                LocalDate fechaFin = LocalDate.parse(alquiler.getFhfin());

                int diaEntrada = fechaInicio.getDayOfMonth();
                int diaSalida = fechaFin.getDayOfMonth();

                // Dia alquilado
                if( displayDay >= diaEntrada && displayDay <= diaSalida ){
                    Log.d("   DiaHolderActual", String.valueOf(displayDay) +
                            " Entrada "+String.valueOf(diaEntrada)+
                            " Salida "+String.valueOf(diaSalida) );

                    // establecer alquiler que pertenece al holder
                    holder.setAlquiler( alquiler );

                    // tagearlo con idAlquiler
                    int idAlquiler = alquiler.getIdAlquiler();
                    holder.itemView.setTag( idAlquiler );

                    // Establecer color
                    String colorHex  = alquiler.getPortal().getColorHex();  // ejemplo #003B95
                    int color = Color.parseColor(colorHex);
                    holder.itemView.setBackgroundColor( color );

                }
            }
        }

    }
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    // Para establecer el listener
    public  void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


}
