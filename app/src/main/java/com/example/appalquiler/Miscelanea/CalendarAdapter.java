package com.example.appalquiler.Miscelanea;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appalquiler.Clases.Alquiler;
import com.example.appalquiler.R;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder>
{
    private Context context;
    private final ArrayList<String> daysOfMonth;
    private final OnItemListener onItemListener;

    private List<Alquiler> alquileresList = null;  // recibido asic de API
    private LocalDate selectedDate = null;

    // Constructor sin petición a API
    public CalendarAdapter(Context context, ArrayList<String> daysOfMonth, OnItemListener onItemListener ) {
        this.context = context;
        this.daysOfMonth = daysOfMonth;
        this.onItemListener = onItemListener;
    }

    // Constructor cuando recibe datos de API
    public CalendarAdapter(Context context, ArrayList<String> daysOfMonth, OnItemListener onItemListener,
                           List<Alquiler> alquileresList, LocalDate selectedDate ) {
        this.context = context;
        this.daysOfMonth = daysOfMonth;
        this.onItemListener = onItemListener;
        this.selectedDate = selectedDate;  // fh actual sistema ej 2023-05-019
        this.alquileresList = alquileresList;
    }

    // Se llama una vez por elemento en la lista
    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate( R.layout.calendar_cell, parent, false );
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int) (parent.getHeight() * 0.166666666);
        return new CalendarViewHolder(view, onItemListener);
    }

    // Se llama cada vez que se actualiza un elemento en la pantalla.
    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position)
    {
        String dia = daysOfMonth.get( position ); // Obtener el día correspondiente a esta posición en la lista
        holder.dayOfMonth.setText( dia );


        if ( alquileresList != null  &&  dia != null  &&  !dia.isEmpty() ) {

            int monthvisionado = selectedDate.getMonthValue(); // mes actual entero (1-12)
            int yearvisionado = selectedDate.getYear();        // año actual entero (ej: 2023)
            int diaHolderActual = Integer.parseInt( dia );

            for( Alquiler alquiler : this.alquileresList ) {  // alquileres de mes actual y año actual
                LocalDate  fhhoraInicio = LocalDate.parse( alquiler.getFhinicio() );
                int diaEntrada = fhhoraInicio.getDayOfMonth();
                //int mesEntrada = fhhoraInicio.getMonthValue();
                //int anoEntrada = fhhoraInicio.getYear();
                LocalDate fhhorafin = LocalDate.parse( alquiler.getFhfin() );
                int diaSalida = fhhorafin.getDayOfMonth();
                //int mesSalida = fhhorafin.getMonthValue();
                //int anoSalida = fhhorafin.getYear();
                /* if( diaHolderActual >= diaEntrada && diaHolderActual <= diaSalida &&
                        monthvisionado == mesEntrada && yearvisionado == anoEntrada ){ */
                if( diaHolderActual >= diaEntrada && diaHolderActual <= diaSalida ){
                    // establecer alquiler que pertenece al holder
                    holder.setAlquiler( alquiler );

                    // tagearlo con idAlquiler
                    int idAlquiler = alquiler.getIdAlquiler();
                    holder.itemView.setTag( idAlquiler );

                    holder.itemView.setBackgroundColor(
                            ContextCompat.getColor(context, R.color.color_dia_alquilado)
                    );
                }
            }
        }
    }

    public interface  OnItemListener {
        void onItemClick(int position, String dayText, Alquiler alquiler);
    }

    @Override
    public int getItemCount() {
        return daysOfMonth.size();
    }

}
