package uz.turgunboyevjurabek.multiplelanguageapp

import android.app.LocaleManager
import android.content.Context
import android.os.Build
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class LanguageViewModel(private val context: Context) : ViewModel() {
    private val LANGUAGE_KEY = stringPreferencesKey("language")

    val currentLanguage: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[LANGUAGE_KEY]
        }

    fun setLanguage(languageCode: String) {
        viewModelScope.launch {
            context.dataStore.edit { preferences ->
                preferences[LANGUAGE_KEY] = languageCode
            }
        }
        updateLocale(languageCode)
    }

    private fun updateLocale(languageCode: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.getSystemService(LocaleManager::class.java)
                .applicationLocales = LocaleList.forLanguageTags(languageCode)
        } else {
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(languageCode))
        }
    }
}
