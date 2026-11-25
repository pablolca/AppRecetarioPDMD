package com.example.recetariopabloleon

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.recetariopabloleon.ui.FichaRecetaScreen
import com.example.recetariopabloleon.ui.ListaRecetasScreen
import com.example.recetariopabloleon.ui.RecetasViewModel
import com.example.recetariopabloleon.ui.theme.RecetarioPabloLeonTheme
import java.util.Locale


sealed class Screen(val route: String) {
    data object Lista : Screen("lista")
    data object Ficha : Screen("ficha/{id}") {
        fun createRoute(id: Int): String = "ficha/$id"
    }
}

@Composable
fun AppNavigation(
// PUNTO CRÍTICO: el ViewModel se crea AQUÍ, fuera del NavHost.
// Su propietario es la Activity, así que lista y ficha reciben la MISMA instancia.
// Si se creara dentro de cada composable() del NavHost, el propietario sería
// el NavBackStackEntry y habría dos ViewModels distintos.
    viewModel: RecetasViewModel = viewModel()
) {
    val idioma by viewModel.idioma.collectAsStateWithLifecycle()
    // Todo lo que va dentro lee los strings.xml en el idioma elegido,
    // sin importar el idioma del dispositivo.
    ConIdioma(idioma = idioma) {
        NavegacionRecetas(viewModel = viewModel)
    }
}

// Sustituye el Context y la Configuration de Compose por unos con el idioma indicado.
// Así stringResource() toma values/ (español) o values-en/ (inglés).
@Composable
fun ConIdioma(idioma: String, contenido: @Composable () -> Unit) {
    val contextoBase = LocalContext.current
    val configuracionBase = LocalConfiguration.current
    val configuracion = remember(idioma, configuracionBase) {
        Configuration(configuracionBase).apply { setLocale(Locale.forLanguageTag(idioma)) }
    }
    val contexto = remember(configuracion) {
        contextoBase.createConfigurationContext(configuracion)
    }
    CompositionLocalProvider(
        LocalContext provides contexto,
        LocalConfiguration provides configuracion
    ) {
        contenido()
    }
}

@Composable
private fun NavegacionRecetas(viewModel: RecetasViewModel) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.Lista.route
    ) {
        composable(Screen.Lista.route) {
            ListaRecetasScreen(
                viewModel = viewModel,
                onVerFicha = { id -> navController.navigate(Screen.Ficha.createRoute(id)) }
            )
        }
        composable(
            route = Screen.Ficha.route,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
// prevenimos errores en el argumento
            val id = backStackEntry.arguments?.getInt("id") ?: -1
            val receta = viewModel.recetaConId(id)
            if (receta != null) {
                FichaRecetaScreen(
                    receta = receta,
                    onVolver = { navController.popBackStack() },
                    onCambiarIdioma = viewModel::alternarIdioma
                )
            } else {
                Text(
                    text = stringResource(R.string.receta_no_encontrada),
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RecetarioPabloLeonTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RecetarioPabloLeonTheme {
        Greeting("Android")
    }
}