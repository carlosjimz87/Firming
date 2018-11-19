package com.croowly.firming_fv.managers;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Belal on 10/30/2015.
 */
public class Config {
    private static final String USER ="firming@croowly.com";
    private static final String PASSWORD ="Techuser01";
    private SharedPreferences prefs;

    public Config(Context context){
        prefs = new CipherSharedPreferences(context, context.getSharedPreferences("Preferences", Context.MODE_PRIVATE) );
        prefs.edit().putString("USER",USER).apply();
        prefs.edit().putString("PASSWORD",PASSWORD).apply();

    }

    public String getUser(){
       return prefs.getString("USER", null);
    }
    public String getPass(){
        return prefs.getString("PASSWORD", null);
    }
}
