package com.example.appalquiler.Miscelanea;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appalquiler.Clases.Alquiler;
import com.example.appalquiler.Clases.Cliente;
import com.example.appalquiler.R;

import java.io.Serializable;
import java.util.List;

public class ClienteAdapter extends RecyclerView.Adapter<ClienteAdapterViewHolder>{

    private List<Cliente> clientes;
    private boolean modoSeleccionActivado;
    private Alquiler alquilerEdicion;

    public ClienteAdapter(List<Cliente> itemList, boolean modoSeleccionActivado, Alquiler alquiler) {
        this.clientes = itemList;
        this.modoSeleccionActivado = modoSeleccionActivado;
        this.alquilerEdicion = alquiler;
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

        Log.d("ViewHolder", "modoSeleccionActivado: " + modoSeleccionActivado );

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();

                if( modoSeleccionActivado ){  //
                    alquilerEdicion.setCliente( itemCliente );  // EDITAR Alquiler NEW Cliente
                    bundle.putSerializable("alquiler" , (Serializable) alquilerEdicion );
                    Navigation.findNavController(v).navigate( R.id.alquileresFormFragment, bundle );
                }
                else{
                    bundle.putSerializable("cliente" , (Serializable) itemCliente );
                    Navigation.findNavController(v).navigate( R.id.clientesFormFragment, bundle );
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return clientes.size();
    }
}
