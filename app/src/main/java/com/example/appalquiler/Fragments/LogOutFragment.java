package com.example.appalquiler.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.appalquiler.R;
import com.example.appalquiler.Utils.SharedPreferencesManager;
import com.example.appalquiler.databinding.FragmentLogOutBinding;

public class LogOutFragment extends Fragment {

    private FragmentLogOutBinding binding;

    public LogOutFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLogOutBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferencesManager sessionManager = new SharedPreferencesManager(requireContext());
        sessionManager.saveSpBoolean("spIsLogin", false);

        // Cerrar la aplicación
        // System.exit(0);

        Navigation.findNavController(requireView()).navigate(R.id.loginFragment);
    }
}