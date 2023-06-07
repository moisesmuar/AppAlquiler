package com.example.appalquiler.Miscelanea;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.example.appalquiler.Clases.Alquiler;
import com.example.appalquiler.databinding.ItemAlquilerBinding;

import java.text.SimpleDateFormat;

public class AlquilerAdapterViewHolder extends RecyclerView.ViewHolder {

    private final ItemAlquilerBinding binding;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public AlquilerAdapterViewHolder(View view) {
        super(view);
        binding = ItemAlquilerBinding.bind(view);
    }

    public void render( Alquiler alquiler ) {

        binding.tvFhInicio.setText( alquiler.getFhinicio());
        binding.tvFhFin.setText( alquiler.getFhfin());
        binding.tvnombreInmueble.setText( alquiler.getInmueble().getNombre() );
        binding.tvnombreCliente.setText( alquiler.getCliente().getNombre() );
        binding.tvNombrePortal.setText( alquiler.getPortal().getNombre() );
    }

}