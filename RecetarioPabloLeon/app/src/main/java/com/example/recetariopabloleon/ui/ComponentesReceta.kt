package com.example.recetariopabloleon.ui

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.recetariopabloleon.R
import com.example.recetariopabloleon.data.Receta
import com.example.recetariopabloleon.data.recetasIniciales
import com.example.recetariopabloleon.ui.theme.RecetarioPabloLeonTheme

// Bloque reutilizable: no sabe en qué layout se va a colocar (lista o ficha).
// Quien lo usa decide su tamaño con el modifier.
@Composable
fun BloqueFoto(
    receta: Receta,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp), // Surface ya recorta su contenido a esta forma
        color = MaterialTheme.colorScheme.surfaceContainerHighest
    ) {
        Image(
            painter = painterResource(id = receta.imagenId),
            contentDescription = stringResource(R.string.cd_foto_receta, receta.nombre),
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun BloqueResumen(receta: Receta, modifier: Modifier = Modifier) {
    Text(
        text = stringResource(
            R.string.resumen_receta,
            receta.tiempoMinutos,
            stringResource(receta.dificultad.etiquetaRes),
            receta.comarca
        ),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
fun BloqueIngredientes(
    ingredientes: List<String>,
    modifier: Modifier = Modifier
) {
// (1) hacer de forma autónoma como la imagen
}


@Composable
fun BloquePasos(
    pasos: List<String>,
    modifier: Modifier = Modifier
) {
// (1) hacer de forma autónoma como la imagen
}

@Composable
fun TarjetaReceta(
    receta: Receta,
    expandida: Boolean,
    onAlternar: () -> Unit,
    onVerFicha: () -> Unit,
    modifier: Modifier = Modifier
) {
// Animación 1: el color de la tarjeta reacciona al estado "expandida",
// que viene del ViewModel. label obligatorio en todas las animate*AsState.
    val colorTarjeta by animateColorAsState(
        targetValue = if (expandida) {
            MaterialTheme.colorScheme.secondaryContainer
        } else {
            MaterialTheme.colorScheme.surfaceContainerHighest
        },
        label = "colorTarjetaReceta"
    )
// Animación 2: animateContentSize anima el cambio de altura del contenedor.
    Column(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize()
    ) {
// La tarjeta se pulsa para expandir. No lleva botones dentro.
        Card(
            onClick = onAlternar,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = colorTarjeta,
                contentColor = MaterialTheme.colorScheme.onSurface
            )
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                BloqueFoto(receta = receta, modifier = Modifier.size(72.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = receta.nombre, style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = stringResource(
                            R.string.resumen_corto,
                            receta.tiempoMinutos,
                            stringResource(receta.dificultad.etiquetaRes)
                        ),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
// Animación 3: el panel entra y sale con expandVertically / shrinkVertically.
// El botón va debajo de la Card (no dentro) para evitar zonas clicables anidadas.
        AnimatedVisibility(
            visible = expandida,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            Column(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
                Text(
                    text = stringResource(R.string.comarca_formato, receta.comarca),
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = onVerFicha, modifier = Modifier.fillMaxWidth()) {
                    Text(stringResource(R.string.ver_receta_completa))
                }
            }
        }
// (3) Añadir animateColorAsState y animateContentSize (con label) a la tarjeta
    }
}

@Preview(showBackground = true, name = "Claro")
// (4) Definir de forma autonoma un preview en modo oscuro y otro en idioma Inglés
@Composable
private fun TarjetaRecetaPreview() {
    RecetarioPabloLeonTheme {
        TarjetaReceta(
            receta = recetasIniciales().first(),
            expandida = true,
            onAlternar = {},
            onVerFicha = {}
        )
    }
}