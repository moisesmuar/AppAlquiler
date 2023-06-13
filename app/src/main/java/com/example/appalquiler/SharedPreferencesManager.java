package com.example.appalquiler;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;

import com.example.appalquiler.Clases.Usuario;
import com.google.gson.Gson;

public class SharedPreferencesManager {

    private final String SP_NAMA = "spName";
    private final String SP_EMAIL = "spEmail";
    private final String SP_ISLOGIN = "spIsLogin";
    private final String SP_THEME = "spTheme";
    private static final String KEY_THEME = "theme";
    private static final String KEY_USER = "spUser";

    private SharedPreferences sharedPreference;
    private SharedPreferences.Editor spEditor;

    public SharedPreferencesManager( Context context ) {
        sharedPreference = context.getSharedPreferences("LoggedIn", Context.MODE_PRIVATE);
        spEditor = sharedPreference.edit();
    }
    public void saveSpString(String spKey, String value) {
        spEditor.putString(spKey, value);
        spEditor.apply();
    }
    public void saveSpInt(String spKey, int value) {
        spEditor.putInt(spKey, value);
        spEditor.apply();
    }
    public void saveSpEmail(String spKey, String value) {
        spEditor.putString(spKey, value);
        spEditor.apply();
    }
    public void saveSpBoolean(String spKey, boolean value) {
        spEditor.putBoolean(spKey, value);
        spEditor.apply();
    }
    public String getName() {
        return sharedPreference.getString(SP_NAMA, "");
    }
    public String getEmail() {
        return sharedPreference.getString(SP_EMAIL, "");
    }

    public Boolean isLogin() {
        return sharedPreference.getBoolean(SP_ISLOGIN, false);
    }

    public void saveTheme(int themeMode) {
        spEditor.putInt(KEY_THEME, themeMode);
        spEditor.apply();
    }

    public int getSavedTheme() {
        return sharedPreference.getInt(KEY_THEME, AppCompatDelegate.MODE_NIGHT_UNSPECIFIED);
    }

    public String getString(String key) {
        return sharedPreference.getString(key, "");
    }

    /**
     * Guardar el Usuario
     */
    public void saveSpUser(String spKey, Usuario usuario) {
        Gson gson = new Gson();
        String userJson = gson.toJson(usuario);
        spEditor.putString( spKey, userJson );
        spEditor.apply();
    }

    /**
     * Retorno datos del Usuario logeado
     */
    public Usuario getSpUser() {
        String userJson = sharedPreference.getString(KEY_USER, null);
        if (userJson != null) {
            Gson gson = new Gson();
            return gson.fromJson(userJson, Usuario.class);
        }
        return null;
    }
}