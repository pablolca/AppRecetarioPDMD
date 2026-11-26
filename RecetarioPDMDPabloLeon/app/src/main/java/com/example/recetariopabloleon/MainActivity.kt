package com.example.recetariopabloleon

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
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

// Rutas de navegación de la app. Una sealed class obliga a que solo existan
// estas dos pantallas, y evita escribir las rutas a mano (y equivocarse) en varios sitios.
sealed class Screen(val route: String) {
    // Pantalla de inicio: la lista de recetas.
    data object Lista : Screen("lista")

    // Pantalla de detalle. "{id}" es un hueco que se rellena con el id de la receta.
    data object Ficha : Screen("ficha/{id}") {
        // Construye la ruta real, p. ej. createRoute(3) -> "ficha/3"
        fun createRoute(id: Int): String = "ficha/$id"
    }
}

// Raíz de la interfaz: conecta el ViewModel, el idioma y la navegación.
@Composable
fun AppNavigation(
// PUNTO CRÍTICO: el ViewModel se crea AQUÍ, fuera del NavHost.
// Su propietario es la Activity, así que lista y ficha reciben la MISMA instancia.
// Si se creara dentro de cada composable() del NavHost, el propietario sería
// el NavBackStackEntry y habría dos ViewModels distintos.
    viewModel: RecetasViewModel = viewModel()
) {
    // Leemos el idioma del ViewModel. "by" + collectAsStateWithLifecycle() hace que,
    // cuando el idioma cambia, esta función se vuelva a ejecutar con el valor nuevo.
    val idioma by viewModel.idioma.collectAsStateWithLifecycle()
    // Todo lo que va dentro lee los strings.xml en el idioma elegido,
    // sin importar el idioma del dispositivo.
    ConIdioma(idioma = idioma) {
        NavegacionRecetas(viewModel = viewModel)
    }
}

// Sustituye el Context y la Configuration de Compose por unos con el idioma indicado.
// Así stringResource() toma values/ (español) o values-en/ (inglés).
// No cambia el idioma del sistema ni reinicia la Activity: solo afecta a lo que va dentro.
@Composable
fun ConIdioma(idioma: String, contenido: @Composable () -> Unit) {
    // Context y Configuration "reales" que nos da Compose.
    val contextoBase = LocalContext.current
    val configuracionBase = LocalConfiguration.current

    // 1) Copia de la configuración actual con el idioma cambiado.
    //    Se copia (en vez de crear una nueva) para conservar el resto: orientación,
    //    modo oscuro, tamaño de pantalla...
    //    remember(...) la guarda y solo la recalcula si cambia el idioma o la configuración base
    //    (por ejemplo al girar el móvil).
    val configuracion = remember(idioma, configuracionBase) {
        Configuration(configuracionBase).apply { setLocale(Locale.forLanguageTag(idioma)) }
    }

    // 2) Context nuevo creado a partir de esa configuración.
    //    Es el que sabe buscar los textos en values-en/ cuando el idioma es "en".
    val contexto = remember(configuracion) {
        contextoBase.createConfigurationContext(configuracion)
    }

    // 3) "Inyectamos" el Context y la Configuration nuevos: todos los composables de dentro,
    //    al pedir LocalContext.current o llamar a stringResource(), reciben estos en vez de los reales.
    CompositionLocalProvider(
        LocalContext provides contexto,
        LocalConfiguration provides configuracion
    ) {
        contenido()
    }
}

// Define qué pantalla se muestra para cada ruta y cómo se pasa de una a otra.
@Composable
private fun NavegacionRecetas(viewModel: RecetasViewModel) {
    // El navController lleva la pila de pantallas (para poder ir hacia delante y volver).
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.Lista.route // la app empieza en la lista
    ) {
        // Ruta "lista"
        composable(Screen.Lista.route) {
            ListaRecetasScreen(
                viewModel = viewModel,
                // Al pulsar "Ver receta completa" navegamos a "ficha/<id>"
                onVerFicha = { id -> navController.navigate(Screen.Ficha.createRoute(id)) }
            )
        }
        // Ruta "ficha/{id}": declaramos que {id} es un número entero.
        composable(
            route = Screen.Ficha.route,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
// prevenimos errores en el argumento: si no llega el id usamos -1 (que no existe)
            val id = backStackEntry.arguments?.getInt("id") ?: -1
            val receta = viewModel.recetaConId(id)
            if (receta != null) {
                FichaRecetaScreen(
                    receta = receta,
                    onVolver = { navController.popBackStack() },   // quita la ficha de la pila -> vuelve a la lista
                    onCambiarIdioma = viewModel::alternarIdioma     // referencia a la función del ViewModel
                )
            } else {
                // Si el id no corresponde a ninguna receta, mostramos un aviso en vez de fallar.
                Text(
                    text = stringResource(R.string.receta_no_encontrada),
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

// Punto de entrada de la app: Android crea esta Activity al abrirla.
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContent sustituye los layouts XML clásicos: a partir de aquí todo es Compose.
        setContent {
            // Tema Material 3 (colores, tipografía, modo claro/oscuro) para toda la app.
            RecetarioPabloLeonTheme {
                AppNavigation()
            }
        }
    }
}
