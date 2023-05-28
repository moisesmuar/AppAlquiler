package com.example.appalquiler.Fragmentos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.appalquiler.R;
import com.example.appalquiler.SharedPreferencesManager;
import com.example.appalquiler.databinding.FragmentClientesBinding;
import com.example.appalquiler.databinding.FragmentConfiguracionBinding;


public class ConfiguracionFragment extends Fragment {

    private FragmentConfiguracionBinding binding;

    public ConfiguracionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentConfiguracionBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        SharedPreferencesManager sessionManager = new SharedPreferencesManager( requireContext() );
        if ( !sessionManager.isLogin() ) {
            Navigation.findNavController(view).navigate( R.id.loginFragment );
        }

        return view;
    }


}