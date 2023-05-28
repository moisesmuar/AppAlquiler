package com.example.appalquiler.Fragmentos;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.appalquiler.R;
import com.example.appalquiler.SharedPreferencesManager;
import com.example.appalquiler.databinding.FragmentLogOutBinding;
import com.example.appalquiler.databinding.FragmentLoginBinding;

public class LogOutFragment extends Fragment {

    private FragmentLogOutBinding binding;

    public LogOutFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_log_out, container, false);

        binding = FragmentLogOutBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
/*
        // Llamar al método clearSession() para cerrar la sesión ->logout
        SharedPreferencesManager sessionManager = new SharedPreferencesManager(requireContext());
        sessionManager.clearSession();

        // Navegarfragmento de inicio de sesión
        Navigation.findNavController(requireView()).navigate( R.id.loginFragment );
*/
        return root;
    }


}