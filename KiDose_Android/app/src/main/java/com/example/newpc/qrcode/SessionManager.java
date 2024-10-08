package com.example.newpc.qrcode;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by Harsh on 8/24/2017.
 */

public class SessionManager {
    private static String TAG = SessionManager.class.getSimpleName();
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Context context;

    // Shared Pref Mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "Kidose";

    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";
    //private static final String KEY_IS_LANG_SET = "isLangSet";

    public SessionManager(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = preferences.edit();
    }

    public void setLogin(boolean isLoggedIn) {
        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);
        editor.commit();
        Log.d(TAG, "User Login Session Modified!!");
    }

/*
    public void setLang(String isLangSet){
        editor.putString(KEY_IS_LANG_SET, isLangSet);
        editor.commit();
        Log.d(TAG, "User Language Modified!!");
    }
*/

/*
    public String checkLang(){
        return preferences.getString(KEY_IS_LANG_SET, "english");
    }
*/

    public boolean isLoggedIn() {
        return preferences.getBoolean(KEY_IS_LOGGEDIN, false);
    }
}
