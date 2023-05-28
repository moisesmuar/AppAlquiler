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
import com.example.appalquiler.Clases.Inmueble;
import com.example.appalquiler.R;

import java.io.Serializable;
import java.util.List;

public class InmuebleAdapter extends RecyclerView.Adapter<InmuebleAdapterViewHolder> {

    private List<Inmueble> inmuebles;
    private boolean modoSeleccionActivado;
    private Alquiler alquilerEdicion;

    public InmuebleAdapter(  List<Inmueble> itemList, boolean modoSeleccionActivado, Alquiler alquiler ) {
        this.inmuebles = itemList;
        this.modoSeleccionActivado = modoSeleccionActivado;
        this.alquilerEdicion = alquiler;
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

        Log.d("ViewHolder", "modoSeleccionActivado: " + modoSeleccionActivado );

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();

                if( modoSeleccionActivado ){  //
                    alquilerEdicion.setInmueble( itemInmueble );  // EDITAR Alquiler NEW Inmueble
                    bundle.putSerializable("alquiler" , (Serializable) alquilerEdicion );
                    Navigation.findNavController(v).navigate( R.id.alquileresFormFragment, bundle );
                }
                else{
                    bundle.putSerializable("inmueble" , (Serializable) itemInmueble );
                    Navigation.findNavController(v).navigate( R.id.inmueblesFormFragment, bundle );
                }
            }
        });
    }

    @Override
    public int getItemCount() {return inmuebles.size();}
}
