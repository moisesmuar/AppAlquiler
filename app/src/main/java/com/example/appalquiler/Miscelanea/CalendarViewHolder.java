package com.example.appalquiler.Miscelanea;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appalquiler.Clases.Alquiler;
import com.example.appalquiler.R;

public class CalendarViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public final TextView dayOfMonth;
    private final CalendarAdapter.OnItemListener onItemListener;
    private Alquiler alquiler = null;

    public CalendarViewHolder(@NonNull View itemView, CalendarAdapter.OnItemListener onItemListener) {
        super(itemView);
        dayOfMonth = itemView.findViewById( R.id.cellDayText );  // coger TextView y asignar Dia
        this.onItemListener = onItemListener;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick( View view ) {
        onItemListener.onItemClick( getAdapterPosition(), (String) dayOfMonth.getText(), this.alquiler );
    }

    public void setAlquiler( Alquiler alquiler ) {
        this.alquiler = alquiler;
    }

}
