package com.example.appalquiler.Miscelanea;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appalquiler.Clases.Alquiler;
import com.example.appalquiler.R;

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


    private void makeItemDisabled(CalendarHorizontalViewHolder holder) {
        holder.txtDay.setTextColor(ContextCompat.getColor( context, R.color.themeColor2) );
        holder.txtDayInWeek.setTextColor(ContextCompat.getColor( context, R.color.themeColor2 ));
        holder.linearLayout.setBackgroundColor(Color.WHITE);
        holder.linearLayout.setEnabled(false);
    }

    private void makeItemSelected(CalendarHorizontalViewHolder holder) {
        holder.txtDay.setTextColor(Color.parseColor("#FFFFFF"));
        holder.txtDayInWeek.setTextColor(Color.parseColor("#FFFFFF"));
        holder.linearLayout.setBackgroundColor(ContextCompat.getColor( context, R.color.themeColor1) );
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
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss", Locale.ENGLISH);
        Calendar cal = Calendar.getInstance();
        cal.setTime( data.get(position) );

        // Establezca el año, mes y día que se mostrará
        int displayMonth = cal.get( Calendar.MONTH );
        int displayYear = cal.get( Calendar.YEAR );
        int displayDay = cal.get( Calendar.DAY_OF_MONTH );

        // Establecer texto en txtDayInWeek y txtDay
        try {
            Date dayInWeek = sdf.parse( cal.getTime().toString() );
            sdf.applyPattern("EEE");
            holder.txtDayInWeek.setText( sdf.format(dayInWeek).toString() );
        } catch (ParseException ex) {
            Log.v("Exception", ex.getLocalizedMessage());
        }

        holder.itemView.setBackgroundColor(
                ContextCompat.getColor(context, R.color.white)
        );
        holder.txtDay.setText( String.valueOf( displayDay ) );

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

                LocalDate fhhoraInicio = LocalDate.parse( alquiler.getFhinicio() );
                int diaEntrada = fhhoraInicio.getDayOfMonth();
                LocalDate fhhorafin = LocalDate.parse( alquiler.getFhfin() );
                int diaSalida = fhhorafin.getDayOfMonth();

                if( displayDay >= diaEntrada && displayDay <= diaSalida ){
                    Log.d("   DiaHolderActual", String.valueOf(displayDay) +
                            " Entrada "+String.valueOf(diaEntrada)+
                            " Salida "+String.valueOf(diaSalida) );

                    // establecer alquiler que pertenece al holder
                    holder.setAlquiler( alquiler );

                    // tagearlo con idAlquiler
                    int idAlquiler = alquiler.getIdAlquiler();
                    holder.itemView.setTag( idAlquiler );
                    Log.d("  Alquilado ", "Coloresar Azul Dia: "+ displayDay );

                    holder.itemView.setBackgroundColor(
                            ContextCompat.getColor(context, R.color.color_dia_alquilado)
                    );
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
