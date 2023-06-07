package com.example.appalquiler.Miscelanea;

import android.graphics.Color;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.example.appalquiler.Clases.Portal;
import com.example.appalquiler.databinding.ItemPortalBinding;

public class PortalAdapterViewHolder extends RecyclerView.ViewHolder {

    private final ItemPortalBinding binding;

    public PortalAdapterViewHolder(View view) {
        super(view);
        binding = ItemPortalBinding.bind(view);
    }

    public void render( Portal p ) {
        binding.tvNombrePortal.setText( p.getNombre() );

        // Convierte color hex en objeto Color y asignar
        int color = Color.parseColor( p.getColorHex() );
        binding.idCuadroColor.setBackgroundColor(color);
    }

}
