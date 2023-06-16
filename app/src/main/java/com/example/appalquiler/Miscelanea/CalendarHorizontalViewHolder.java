package com.example.appalquiler.Miscelanea;

import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appalquiler.Clases.Alquiler;
import com.example.appalquiler.R;

public class CalendarHorizontalViewHolder extends RecyclerView.ViewHolder   {

    public CalendarHorizontalAdapter.OnItemClickListener onItemListener;
    TextView txtDay;
    TextView txtDayInWeek;
    LinearLayout linearLayout;
    private Alquiler alquiler = null;
    
    public CalendarHorizontalViewHolder(@NonNull View itemView, CalendarHorizontalAdapter.OnItemClickListener onItemListener) {
        super(itemView);
        txtDay = itemView.findViewById( R.id.txt_date );
        txtDayInWeek = itemView.findViewById( R.id.txt_day );
        linearLayout = itemView.findViewById(R.id.calendar_linear_layout);


    }

    public void setAlquiler( Alquiler alquiler ) {
        this.alquiler = alquiler;
    }

}