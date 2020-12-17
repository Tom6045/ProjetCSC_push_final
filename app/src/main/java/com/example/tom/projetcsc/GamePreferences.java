package com.example.tom.projetcsc;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class GamePreferences extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.game_prefs);
    }

}
