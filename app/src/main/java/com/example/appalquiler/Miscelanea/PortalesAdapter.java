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
import com.example.appalquiler.Models.Portal;
import com.example.appalquiler.R;

import java.io.Serializable;
import java.util.List;

public class PortalesAdapter extends RecyclerView.Adapter<PortalAdapterViewHolder>  {

    private List<Portal> portales;
    private int modoSeleccion;
    private Alquiler alquilerEdicion;

    public PortalesAdapter(List<Portal> portales,
                           int modoSeleccion,
                           Alquiler alquiler) {
        this.portales = portales;
        this.modoSeleccion = modoSeleccion;
        this.alquilerEdicion = alquiler;
    }

    public void setFilteredList(List<Portal> listaFiltrada){
        this.portales = listaFiltrada;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PortalAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate( R.layout.item_portal, parent, false );
        return new PortalAdapterViewHolder( itemView );
    }

    @Override
    public void onBindViewHolder(@NonNull PortalAdapterViewHolder holder, int position) {
        Portal itemPortal = portales.get( position );
        holder.render( itemPortal );
        Log.d("onBindViewHolder", "modoSeleccion: " + modoSeleccion );

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                switch (modoSeleccion) {
                    case 0:   // Editar Portal - Aún no hace nada.
                        break;
                    case 1:  // accion creaccion - key alquilerNuevo
                        alquilerEdicion.setPortal( itemPortal );  // Establecer Portal de Alquiler
                        bundle.putSerializable("alquilerNuevo" , (Serializable) alquilerEdicion );
                        Navigation.findNavController(v).navigate( R.id.alquileresFormFragment, bundle );
                        break;
                    case 2:  // acción edición - key alquilerEdicion
                        alquilerEdicion.setPortal( itemPortal );  // Editar Portal del Alquiler
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
    public int getItemCount() {
        return portales.size();
    }

}
