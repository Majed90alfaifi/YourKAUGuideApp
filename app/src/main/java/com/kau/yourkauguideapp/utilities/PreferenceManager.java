package com.kau.yourkauguideapp.utilities;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {

    private final SharedPreferences sharedPreferences;

    public PreferenceManager(Context context) {
        sharedPreferences = context.getSharedPreferences("chatAppPreference" , context.MODE_PRIVATE);    }

}



