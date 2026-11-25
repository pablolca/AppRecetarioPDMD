package com.example.recetariopabloleon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
                    onVolver = { navController.popBackStack() }
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
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
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