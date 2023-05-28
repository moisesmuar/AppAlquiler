package com.example.appalquiler.Miscelanea;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.example.appalquiler.Clases.Alquiler;
import com.example.appalquiler.databinding.ItemAlquilerBinding;

public class AlquilerAdapterViewHolder extends RecyclerView.ViewHolder {

    private final ItemAlquilerBinding binding;

    public AlquilerAdapterViewHolder(View view) {
        super(view);
        binding = ItemAlquilerBinding.bind(view);
    }

    public void render( Alquiler alquiler ) {
        binding.tvFhInicio.setText( alquiler.getFhinicio() );
        binding.tvFhFin.setText(  alquiler.getFhfin() );

        // binding.tvDias.setText( String.valueOf( alquiler.getDias() ) );
        // binding.tvPrecioDia.setText( String.valueOf(alquiler.getPrecioDia()) );

        binding.tvnombreInmueble.setText( alquiler.getInmueble().getNombre() );
        binding.tvnombreCliente.setText( alquiler.getCliente().getNombre() );
    }

}