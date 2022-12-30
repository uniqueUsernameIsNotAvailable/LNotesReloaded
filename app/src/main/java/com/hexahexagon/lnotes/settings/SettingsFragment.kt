package com.hexahexagon.lnotes.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.hexahexagon.lnotes.R

class SettingsFragment:PreferenceFragmentCompat(){
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)
    }
}