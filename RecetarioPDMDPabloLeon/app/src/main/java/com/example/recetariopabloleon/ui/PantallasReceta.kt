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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaRecetasScreen(
    viewModel: RecetasViewModel,
    onVerFicha: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val recetas by viewModel.recetas.collectAsStateWithLifecycle()
    val expandidaId by viewModel.expandidaId.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.titulo_lista)) },
                actions = { BotonIdioma(onClick = viewModel::alternarIdioma) }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(items = recetas, key = { it.id }) { receta ->
                TarjetaReceta(
                    receta = receta,
                    expandida = expandidaId == receta.id,
                    onAlternar = { viewModel.alternarExpandida(receta.id) },
                    onVerFicha = { onVerFicha(receta.id) }
                )
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FichaRecetaScreen(
    receta: Receta,
    onVolver: () -> Unit,
    modifier: Modifier = Modifier,
    onCambiarIdioma: () -> Unit = {}
) {
// Punto crítico de adaptabilidad: se decide la disposición según la orientación.
    val configuracion = LocalConfiguration.current
    val esHorizontal = configuracion.orientation == Configuration.ORIENTATION_LANDSCAPE
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(receta.nombre) },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.volver)
                        )
                    }
                },
                actions = { BotonIdioma(onClick = onCambiarIdioma) }
            )
        }
    ) { padding ->
        if (esHorizontal) {
            DisposicionHorizontal(receta = receta, modifier = modifier.padding(padding))
        } else {
            DisposicionVertical(receta = receta, modifier = modifier.padding(padding))
        }
    }
}
// Botón de la barra superior: muestra el idioma actual (ES / EN, sacado de los XML)
// y cada pulsación rota al otro.
@Composable
private fun BotonIdioma(onClick: () -> Unit) {
    val descripcion = stringResource(R.string.cd_cambiar_idioma)
    TextButton(
        onClick = onClick,
        modifier = Modifier.semantics { contentDescription = descripcion }
    ) {
        Text(stringResource(R.string.boton_idioma))
    }
}

@Composable
private fun DisposicionVertical(receta: Receta, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        BloqueFoto(receta = receta, modifier = Modifier.fillMaxWidth().height(220.dp))
        Spacer(modifier = Modifier.height(16.dp))
        ContenidoReceta(receta = receta)
    }
}
@Composable
private fun DisposicionHorizontal(receta: Receta, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxSize().padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Foto a la izquierda: 40% del ancho, ocupa todo el alto disponible
        BloqueFoto(
            receta = receta,
            modifier = Modifier
                .weight(0.4f)
                .fillMaxHeight()
        )
        // Contenido a la derecha: 60% del ancho, con scroll propio
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

// Contenido compartido por ambas disposiciones: se escribe una sola vez .
// Se invoca siempre dentro de un Column, que es quien lo apil a.
@Composable
private fun ContenidoReceta(receta: Receta) {
    BloqueResumen(receta = receta)
    Spacer(modifier = Modifier.height(16.dp))
    BloqueIngredientes(ingredientes = receta.ingredientes)
    Spacer(modifier = Modifier.height(16.dp))
    BloquePasos(pasos = receta.pasos)
}