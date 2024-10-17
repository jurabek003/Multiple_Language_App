@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package uz.turgunboyevjurabek.multiplelanguageapp

import android.app.Activity
import android.app.LocaleManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.LocaleList
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat.recreate
import androidx.core.app.ActivityCompat.startActivity
import androidx.core.os.LocaleListCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import uz.turgunboyevjurabek.multiplelanguageapp.ui.theme.MultipleLanguageAppTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val languageCode = getSavedLanguage(this) ?: "en"
            setLanguage(this, languageCode)

            MultipleLanguageAppTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(title = { Text(text = stringResource(id = R.string.top_bar_text)) })
                    }
                ) { innerPadding ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .padding(innerPadding)
                    ) {
                        Greeting(stringResource(id = R.string.center_name_text))
                    }
                }
            }
        }
    }

    private fun getSavedLanguage(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences("language_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("language", null)
    }
}
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    LazyColumn(Modifier.padding(16.dp)) {
        item {
            Text(
                text = stringResource(id = R.string.title),
                modifier = modifier,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            HorizontalDivider(Modifier.fillMaxWidth())
        }
        items(10){
            Text(
                text = name,
                modifier = modifier,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }
        item{
            HorizontalDivider(Modifier.fillMaxWidth())
            LanguageSwitcher()
        }
    }
}

@Composable
fun LanguageSwitcher(){
    val context = LocalContext.current
    Column {
        Button(onClick = {
            setLanguage(context, "en")
            (context as? Activity)?.recreate()
        }) {
            Text("Switch to English")
        }
        Button(onClick = {
            setLanguage(context, "uz")
            (context as? Activity)?.recreate()
        }) {
            Text("Switch to Uzbek")
        }
    }
}

fun setLanguage(context: Context, languageCode: String) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        context.getSystemService(LocaleManager::class.java)
            .applicationLocales = LocaleList.forLanguageTags(languageCode)
    }else{
        Log.d("TTT", "setLanguage: ${AppCompatDelegate.getApplicationLocales()}")
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(languageCode))
    }
    saveLanguagePreference(context, languageCode)
}

fun saveLanguagePreference(context: Context, languageCode: String) {
    val pref = context.getSharedPreferences("language_prefs", Context.MODE_PRIVATE)
    pref.edit().putString("language", languageCode).apply()
}