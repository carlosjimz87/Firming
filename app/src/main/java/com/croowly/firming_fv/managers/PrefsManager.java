package com.croowly.firming_fv.managers;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by CHARS on 4/18/2017.
 *
 */

public class PrefsManager {
    // Preferences
    private static SharedPreferences prefs;
    // Editors
    private static SharedPreferences.Editor editor;

    private static final String PREFS = "PREFS";
    private static final String SEPARATOR = ":";

    public static final String KEYEMAIL= "EMAIL";


    public static void configPrefs(Context context){
        int PRIVATE_MODE = 0;
        prefs = context.getSharedPreferences(PREFS, PRIVATE_MODE);
        editor = prefs.edit();
        editor.apply();
    }

    /**
     * Save CAN
     * */
    public static void saveEmail(String email){
        // Storing name in pref
        editor.putString(KEYEMAIL,email);
        editor.commit();
    }
    /**
     *
     * Get PIN
     * */
    public static String getEmail()
    {
        return prefs.getString(KEYEMAIL,"carlosjimz87@gmail.com");
    }

}
