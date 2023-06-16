package com.example.appalquiler.Miscelanea;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appalquiler.Clases.Cliente;
import com.example.appalquiler.databinding.ItemClienteBinding;

public class ClienteAdapterViewHolder extends RecyclerView.ViewHolder{

    private final ItemClienteBinding binding;

    public ClienteAdapterViewHolder(@NonNull View itemView) {
        super(itemView);
        binding = ItemClienteBinding.bind( itemView );
    }

    public void render( Cliente cli ) {
        binding.tvNombre.setText( cli.getNombre() );
        binding.tvTlf.setText( cli.getTelefono() );
        binding.tvEmail.setText( cli.getEmail() );
        binding.tvCiudad.setText( cli.getCiudad() );
        binding.tvCp.setText( cli.getCp() );
    }

}
