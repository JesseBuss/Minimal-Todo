package com.example.avjindersinghsekhon.minimaltodo.Main.model

import android.content.Context
import android.content.Context.MODE_PRIVATE

class PrefsHelper(private val context: Context) {
    companion object {
        const val THEME_PREFERENCES = "com.avjindersekhon.themepref"
        const val RECREATE_ACTIVITY = "com.avjindersekhon.recreateactivity"
        const val THEME_SAVED = "com.avjindersekhon.savedtheme"
        const val DARKTHEME = "com.avjindersekon.darktheme"
        const val LIGHTTHEME = "com.avjindersekon.lighttheme"
        const val SHARED_PREF_DATA_SET_CHANGED = "com.avjindersekhon.datasetchanged";
        const val CHANGE_OCCURED = "com.avjinder.changeoccured";
    }

    fun getTheme() = getPrefs().getString(THEME_SAVED, LIGHTTHEME)

    private fun getPrefs() = context.getSharedPreferences(THEME_PREFERENCES, MODE_PRIVATE)
}