package com.example.appalquiler;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {

    private final String SP_NAMA = "spName";
    private final String SP_EMAIL = "spEmail";
    private final String SP_ISLOGIN = "spIsLogin";
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

    public String getString(String key) {
        return sharedPreference.getString(key, "");
    }

    /**
     * eliminará todos los datos guardados en las preferencias compartidas,
     * Cierra la sesión. estado ini sesion = false
     */
    public void clearSession() {
        spEditor.clear();
        spEditor.apply();
    }

}