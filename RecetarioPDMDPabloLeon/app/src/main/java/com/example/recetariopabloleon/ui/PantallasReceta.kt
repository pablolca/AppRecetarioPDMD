package com.example.recetariopabloleon.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.recetariopabloleon.R
import com.example.recetariopabloleon.data.Receta

// =====================================================================
// PANTALLA 1: LISTA DE RECETAS
// =====================================================================
// TopAppBar es experimental en Material 3, de ahí el @OptIn.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaRecetasScreen(
    viewModel: RecetasViewModel,
    onVerFicha: (Int) -> Unit,          // qué hacer al pedir ver una receta (lo decide la navegación)
    modifier: Modifier = Modifier
) {
    // Leemos el estado del ViewModel. Si cualquiera de los dos cambia,
    // esta pantalla se vuelve a dibujar con los valores nuevos.
    val recetas by viewModel.recetas.collectAsStateWithLifecycle()
    val expandidaId by viewModel.expandidaId.collectAsStateWithLifecycle()

    // Scaffold = esqueleto de pantalla Material: barra superior + contenido.
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.titulo_lista)) },
                // "actions" = botones a la derecha de la barra: aquí el de idioma
                actions = { BotonIdioma(onClick = viewModel::alternarIdioma) }
            )
        }
    ) { padding ->
        // "padding" es el hueco que ocupa la barra superior: lo aplicamos para que
        // la lista no quede tapada debajo de ella.
        // LazyColumn = lista que solo dibuja los elementos visibles (eficiente con listas largas).
        LazyColumn(
            modifier = modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp),              // margen alrededor de toda la lista
            verticalArrangement = Arrangement.spacedBy(12.dp)   // separación entre tarjetas
        ) {
            // key = id: Compose identifica cada tarjeta por su id y no por su posición,
            // así las animaciones no se mezclan entre tarjetas.
            items(items = recetas, key = { it.id }) { receta ->
                TarjetaReceta(
                    receta = receta,
                    expandida = expandidaId == receta.id,                   // ¿es esta la abierta?
                    onAlternar = { viewModel.alternarExpandida(receta.id) }, // abrir/cerrar al pulsar
                    onVerFicha = { onVerFicha(receta.id) }                   // botón "Ver receta completa"
                )
            }
        }
    }
}

// =====================================================================
// PANTALLA 2: FICHA DE UNA RECETA
// =====================================================================
// No recibe el ViewModel, solo los datos y las acciones que necesita:
// así es más fácil de reutilizar y de previsualizar.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FichaRecetaScreen(
    receta: Receta,
    onVolver: () -> Unit,
    modifier: Modifier = Modifier,
    onCambiarIdioma: () -> Unit = {}
) {
// Punto crítico de adaptabilidad: se decide la disposición según la orientación.
// LocalConfiguration se actualiza al girar el móvil, y eso vuelve a dibujar la pantalla.
    val configuracion = LocalConfiguration.current
    val esHorizontal = configuracion.orientation == Configuration.ORIENTATION_LANDSCAPE
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(receta.nombre) },
                // Flecha de volver a la izquierda de la barra.
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(
                            // AutoMirrored: la flecha se invierte sola en idiomas de derecha a izquierda.
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.volver) // para lectores de pantalla
                        )
                    }
                },
                actions = { BotonIdioma(onClick = onCambiarIdioma) }
            )
        }
    ) { padding ->
        // Mismos datos, dos colocaciones distintas según la orientación.
        if (esHorizontal) {
            DisposicionHorizontal(receta = receta, modifier = modifier.padding(padding))
        } else {
            DisposicionVertical(receta = receta, modifier = modifier.padding(padding))
        }
    }
}

// Botón de la barra superior: muestra el idioma actual (ES / EN, sacado de los XML)
// y cada pulsación rota al otro.
// El texto sale de R.string.boton_idioma, que vale "ES" en values/ y "EN" en values-en/:
// por eso el botón cambia solo al cambiar el idioma, sin ningún if.
@Composable
private fun BotonIdioma(onClick: () -> Unit) {
    val descripcion = stringResource(R.string.cd_cambiar_idioma)
    TextButton(
        onClick = onClick,
        // Descripción para accesibilidad: el lector de pantalla dirá "Cambiar idioma".
        modifier = Modifier.semantics { contentDescription = descripcion }
    ) {
        Text(stringResource(R.string.boton_idioma))
    }
}

// Móvil en VERTICAL: todo en una columna con scroll.
//   [ FOTO 220 dp ]
//   [ resumen / ingredientes / pasos ]
@Composable
private fun DisposicionVertical(receta: Receta, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()) // permite desplazar si el contenido no cabe
            .padding(16.dp)
    ) {
        // La foto ocupa todo el ancho y 220 dp de alto.
        BloqueFoto(receta = receta, modifier = Modifier.fillMaxWidth().height(220.dp))
        Spacer(modifier = Modifier.height(16.dp))
        ContenidoReceta(receta = receta)
    }
}

// Móvil en HORIZONTAL: dos columnas lado a lado.
//   [ FOTO 40% ] [ resumen / ingredientes / pasos 60% (con scroll) ]
@Composable
private fun DisposicionHorizontal(receta: Receta, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxSize().padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Foto a la izquierda: 40% del ancho, ocupa todo el alto disponible.
        // weight() reparte el ancho sobrante de la Row en proporción (0.4 + 0.6 = 100%).
        BloqueFoto(
            receta = receta,
            modifier = Modifier
                .weight(0.4f)
                .fillMaxHeight()
        )
        // Contenido a la derecha: 60% del ancho, con scroll propio
        // (la foto se queda fija mientras se desplaza el texto).
        Column(
            modifier = Modifier
                .weight(0.6f)
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
        ) {
            ContenidoReceta(receta = receta)
        }
    }
}

// Contenido compartido por ambas disposiciones: se escribe una sola vez.
// Se invoca siempre dentro de un Column, que es quien lo apila.
@Composable
private fun ContenidoReceta(receta: Receta) {
    BloqueResumen(receta = receta)                         // "45 min | Media | La Mancha"
    Spacer(modifier = Modifier.height(16.dp))
    BloqueIngredientes(ingredientes = receta.ingredientes) // lista con checks
    Spacer(modifier = Modifier.height(16.dp))
    BloquePasos(pasos = receta.pasos)                      // pasos numerados en círculos
}
