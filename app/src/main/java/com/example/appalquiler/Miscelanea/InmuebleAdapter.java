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
import com.example.appalquiler.Clases.Inmueble;
import com.example.appalquiler.R;

import java.io.Serializable;
import java.util.List;

public class InmuebleAdapter extends RecyclerView.Adapter<InmuebleAdapterViewHolder> {

    private List<Inmueble> inmuebles;
    private int modoSeleccion;
    private Alquiler alquilerEdicion;

    public InmuebleAdapter(  List<Inmueble> itemList, int modoSeleccion, Alquiler alquiler ) {
        this.inmuebles = itemList;
        this.modoSeleccion = modoSeleccion;
        this.alquilerEdicion = alquiler;
    }

    public void setFilteredList(List<Inmueble> listaFiltrada){
        this.inmuebles = listaFiltrada;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public InmuebleAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate( R.layout.item_inmueble, parent, false );
        return new InmuebleAdapterViewHolder( itemView );
    }

    @Override
    public void onBindViewHolder(@NonNull InmuebleAdapterViewHolder holder, int position) {
        Inmueble itemInmueble = inmuebles.get( position );
        holder.render( itemInmueble );
        Log.d("onBindViewHolder", "modoSeleccion: " + modoSeleccion );

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();

                switch (modoSeleccion) {
                    case 0:   // Editar Inmueble
                        bundle.putSerializable("inmueble" , (Serializable) itemInmueble );
                        Navigation.findNavController(v).navigate( R.id.inmueblesFormFragment, bundle );
                        break;
                    case 1:  // accion creaccion - key alquilerNuevo
                        alquilerEdicion.setInmueble( itemInmueble );  // Establecer Inmueble de Alquiler
                        bundle.putSerializable("alquilerNuevo" , (Serializable) alquilerEdicion );
                        Navigation.findNavController(v).navigate( R.id.alquileresFormFragment, bundle );
                        break;
                    case 2:  // acción edición - key alquilerEdicion
                        alquilerEdicion.setInmueble( itemInmueble );  // EDITAR Inmueble del Alquiler
                        bundle.putSerializable("alquilerEdicion" , (Serializable) alquilerEdicion );
                        Navigation.findNavController(v).navigate( R.id.alquileresFormFragment, bundle );
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {return inmuebles.size();}
}
