package com.example.appalquiler.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.example.appalquiler.R;
import com.example.appalquiler.Utils.SharedPreferencesManager;


public class ConfiguracionFragment extends Fragment {

    private FragmentConfiguracionBinding binding;
    SharedPreferencesManager sessionManager;

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

        sessionManager = new SharedPreferencesManager( requireContext() );
        if ( !sessionManager.isLogin() ) {
            Log.e("LOGIN", " false ");
            Navigation.findNavController(view).navigate( R.id.loginFragment );
        }
        else {
            Log.e("LOGIN", " true ");
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Obtener el tema guardado o utilizar el valor predeterminado
        int savedTheme = sessionManager.getSavedTheme();
        if ( savedTheme == AppCompatDelegate.MODE_NIGHT_UNSPECIFIED ) {
            savedTheme = AppCompatDelegate.MODE_NIGHT_NO; // Valor predeterminado si no hay tema guardado
        }

        // Establecer el tema guardado o el valor predeterminado
        AppCompatDelegate.setDefaultNightMode(savedTheme);

        // Configurar el estado del RadioGroup según el tema guardado
        switch (savedTheme) {
            case AppCompatDelegate.MODE_NIGHT_NO:
                binding.themeLight.setChecked(true);
                break;
            case AppCompatDelegate.MODE_NIGHT_YES:
                binding.themeDark.setChecked(true);
                break;
        }

        binding.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int itemSelected) {
                switch (itemSelected) {
                    case R.id.themeLight:
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                        sessionManager.saveTheme(AppCompatDelegate.MODE_NIGHT_NO);
                        break;
                    case R.id.themeDark:
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        sessionManager.saveTheme(AppCompatDelegate.MODE_NIGHT_YES);
                        break;
                }
            }
        });
    }

}