package com.example.appalquiler.Miscelanea;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appalquiler.Models.Inmueble;
import com.example.appalquiler.databinding.ItemInmuebleBinding;

public class InmuebleAdapterViewHolder extends RecyclerView.ViewHolder{

    private final ItemInmuebleBinding binding;

    public InmuebleAdapterViewHolder(@NonNull View itemView) {
        super(itemView);
        binding = ItemInmuebleBinding.bind(itemView);
    }

    public void render( Inmueble inmueble ) {

        binding.tvNombre.setText( inmueble.getNombre() );
        binding.tvCalleInmueble.setText( inmueble.getCalle() );

        binding.tvCiudadInmueble.setText( inmueble.getCiudad() );
        binding.tvNumPersonas.setText( String.valueOf( inmueble.getNumPersonas() ) );

        binding.tvNumHabitaciones.setText( String.valueOf( inmueble.getNumHabitaciones() ) );
        binding.tvNumBanos.setText( String.valueOf( inmueble.getNumBanos() ) );
        binding.tvNumAseos.setText( String.valueOf( inmueble.getNumAseos() ) );

    }

}
