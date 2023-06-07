package com.example.appalquiler.Miscelanea;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
//import androidx.navigation.findNavController;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appalquiler.Clases.Alquiler;
import com.example.appalquiler.R;

import java.io.Serializable;
import java.util.List;

public class AlquilerAdapter extends RecyclerView.Adapter<AlquilerAdapterViewHolder> {

    private List<Alquiler> alquileres;

    public AlquilerAdapter(  List<Alquiler> itemList ) {
        this.alquileres = itemList;
    }

    @NonNull
    @Override
    public AlquilerAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate( R.layout.item_alquiler, parent, false );
        return new AlquilerAdapterViewHolder( itemView );
    }

    @Override
    public void onBindViewHolder( AlquilerAdapterViewHolder holder, int position ) {

        Alquiler itemAlquiler = alquileres.get( position );  // objeto Alquiler
        holder.render( itemAlquiler );

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("alquilerEdicion" , (Serializable) itemAlquiler );

                Navigation.findNavController(v).navigate( R.id.alquileresFormFragment, bundle );
            }
        });
    }

    @Override
    public int getItemCount() {
        return alquileres.size();
    }

}