package com.example.appalquiler.Miscelanea;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appalquiler.Models.Alquiler;
import com.example.appalquiler.Models.Cliente;
import com.example.appalquiler.R;

import java.io.Serializable;
import java.util.List;

public class ClienteAdapter extends RecyclerView.Adapter<ClienteAdapterViewHolder>{

    private List<Cliente> clientes;
    private int modoSeleccion;
    private Alquiler alquilerEdicion;

    public ClienteAdapter(List<Cliente> itemList,
                          int modoSeleccion,
                          Alquiler alquiler) {
        this.clientes = itemList;
        this.modoSeleccion = modoSeleccion;
        this.alquilerEdicion = alquiler;
    }

    public void setFilteredList(List<Cliente> listaFiltrada){
        this.clientes = listaFiltrada;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ClienteAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate( R.layout.item_cliente, parent, false );
        return new ClienteAdapterViewHolder( itemView );
    }

    @Override
    public void onBindViewHolder(@NonNull ClienteAdapterViewHolder holder, int position) {
        Cliente itemCliente = clientes.get( position );  // objeto Cliente
        holder.render( itemCliente );

        Log.d("onBindViewHolder", "modoSeleccion: " + modoSeleccion );

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                switch (modoSeleccion) {
                    case 0:   // Editar Cliente
                        bundle.putSerializable("cliente" , (Serializable) itemCliente );
                        Navigation.findNavController(v).navigate( R.id.clientesFormFragment, bundle );                       break;
                    case 1:  // accion creaccion - key alquilerNuevo
                        alquilerEdicion.setCliente( itemCliente );  // Establecer CLIENTE de Alquiler
                        bundle.putSerializable("alquilerNuevo" , (Serializable) alquilerEdicion );
                        Navigation.findNavController(v).navigate( R.id.alquileresFormFragment, bundle );                        break;
                    case 2:  // acción edición - key alquilerEdicion
                        alquilerEdicion.setCliente( itemCliente );  // EDITAR CLIENTE del Alquiler
                        bundle.putSerializable("alquilerEdicion" , (Serializable) alquilerEdicion );
                        Navigation.findNavController(v).navigate( R.id.alquileresFormFragment, bundle );                        break;
                    default: //  casos no cubiertos

                        break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return clientes.size();
    }
}
