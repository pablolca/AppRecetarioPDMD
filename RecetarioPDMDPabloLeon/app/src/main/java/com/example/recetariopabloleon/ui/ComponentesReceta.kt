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

// Este archivo contiene las "piezas" visuales con las que se montan las pantallas.
// Todos los colores salen de MaterialTheme.colorScheme (nunca colores fijos),
// por eso la app se adapta sola al modo oscuro.

// ---------------------------------------------------------------------
// FOTO DE LA RECETA
// ---------------------------------------------------------------------
// Bloque reutilizable: no sabe en qué layout se va a colocar (lista o ficha).
// Quien lo usa decide su tamaño con el modifier:
//   - tarjeta de la lista: 72 x 72 dp
//   - ficha vertical: todo el ancho x 220 dp
//   - ficha horizontal: 40% del ancho x todo el alto
@Composable
fun BloqueFoto(
    receta: Receta,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp), // Surface ya recorta su contenido a esta forma
        // Color de fondo que se ve mientras carga o si la foto no llena el hueco.
        color = MaterialTheme.colorScheme.surfaceContainerHighest
    ) {
        Image(
            painter = painterResource(id = receta.imagenId), // la foto .webp de res/drawable
            // Texto para lectores de pantalla, p. ej. "Fotografía de Tiznao".
            contentDescription = stringResource(R.string.cd_foto_receta, receta.nombre),
            // Crop: la foto rellena todo el hueco sin deformarse; lo que sobra se recorta.
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}

// ---------------------------------------------------------------------
// LÍNEA DE RESUMEN: "45 min | Media | La Mancha"
// ---------------------------------------------------------------------
@Composable
fun BloqueResumen(receta: Receta, modifier: Modifier = Modifier) {
    Text(
        // R.string.resumen_receta = "%1$d min | %2$s | %3$s"
        // %1$d se rellena con el tiempo, %2$s con la dificultad traducida y %3$s con la comarca.
        text = stringResource(
            R.string.resumen_receta,
            receta.tiempoMinutos,
            stringResource(receta.dificultad.etiquetaRes),
            receta.comarca
        ),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant, // gris suave: información secundaria
        modifier = modifier.fillMaxWidth()
    )
}

// ---------------------------------------------------------------------
// INGREDIENTES: título + una fila "✓ ingrediente" por cada uno
// ---------------------------------------------------------------------
@Composable
fun BloqueIngredientes(
    ingredientes: List<String>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp) // 4 dp entre filas
    ) {
        Text(
            text = stringResource(R.string.titulo_ingredientes),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(4.dp))
        // Una Row por ingrediente: [icono check] [espacio] [texto]
        ingredientes.forEach { ingrediente ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null, // decorativo: el texto ya describe el ingrediente
                    tint = MaterialTheme.colorScheme.primary, // color principal del tema
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = ingrediente,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

// ---------------------------------------------------------------------
// PASOS DE ELABORACIÓN: título + "(1) texto del paso", "(2) ..."
// ---------------------------------------------------------------------
@Composable
fun BloquePasos(
    pasos: List<String>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(R.string.titulo_elaboracion),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        // forEachIndexed nos da también la posición (empieza en 0) para numerar los pasos.
        pasos.forEachIndexed { indice, paso ->
            // Alignment.Top: si el texto ocupa varias líneas, el círculo queda arriba.
            Row(verticalAlignment = Alignment.Top) {
                // Número del paso dentro de un círculo.
                // Box apila sus hijos uno encima de otro: primero el círculo, encima el número.
                Box(
                    modifier = Modifier.size(24.dp),
                    contentAlignment = Alignment.Center // el número queda centrado en el círculo
                ) {
                    // Círculo relleno del color principal (Surface vacía con forma de círculo).
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.fillMaxSize()
                    ) {}
                    Text(
                        text = (indice + 1).toString(), // +1 para que empiece en 1, no en 0
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onPrimary // color que contrasta con primary
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = paso,
                    style = MaterialTheme.typography.bodyMedium,
                    // weight(1f): el texto ocupa todo el ancho que queda y baja de línea si no cabe.
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

// ---------------------------------------------------------------------
// TARJETA DE LA LISTA (con 3 animaciones)
// ---------------------------------------------------------------------
// Cerrada:  [foto 72dp] Nombre
//                       45 min | Media
// Abierta:  lo mismo + debajo "Comarca: ..." y el botón "Ver receta completa".
// La tarjeta NO guarda si está abierta: se lo dice el ViewModel con "expandida"
// (así solo puede haber una abierta y el estado sobrevive al girar el móvil).
@Composable
fun TarjetaReceta(
    receta: Receta,
    expandida: Boolean,      // ¿está abierta? (viene del ViewModel)
    onAlternar: () -> Unit,  // pulsar la tarjeta -> abrir/cerrar
    onVerFicha: () -> Unit,  // pulsar el botón -> ir a la ficha
    modifier: Modifier = Modifier
) {
// Animación 1: el color de la tarjeta reacciona al estado "expandida",
// que viene del ViewModel. label obligatorio en todas las animate*AsState.
// Cuando "expandida" cambia, el color no salta: pasa poco a poco de uno a otro.
    val colorTarjeta by animateColorAsState(
        targetValue = if (expandida) {
            MaterialTheme.colorScheme.secondaryContainer       // abierta: color de acento
        } else {
            MaterialTheme.colorScheme.surfaceContainerHighest  // cerrada: gris neutro
        },
        label = "colorTarjetaReceta"
    )
// Animación 2: animateContentSize anima el cambio de altura del contenedor.
// Al aparecer o desaparecer el panel de abajo, la Column crece/encoge suavemente.
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
                containerColor = colorTarjeta, // el color animado de arriba
                contentColor = MaterialTheme.colorScheme.onSurface
            )
        ) {
            // Fila: [foto] [nombre + resumen corto]
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                BloqueFoto(receta = receta, modifier = Modifier.size(72.dp))
                // weight(1f): la columna de texto ocupa todo el ancho que deja la foto.
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = receta.nombre, style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        // R.string.resumen_corto = "%1$d min | %2$s" (sin comarca)
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
            visible = expandida,       // solo se muestra si esta tarjeta es la abierta
            enter = expandVertically(), // al aparecer: se despliega hacia abajo
            exit = shrinkVertically()   // al desaparecer: se pliega hacia arriba
        ) {
            Column(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
                Text(
                    text = stringResource(R.string.comarca_formato, receta.comarca), // "Comarca: La Mancha"
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = onVerFicha, modifier = Modifier.fillMaxWidth()) {
                    Text(stringResource(R.string.ver_receta_completa))
                }
            }
        }
    }
}

// ---------------------------------------------------------------------
// VISTAS PREVIAS (solo para Android Studio, no salen en la app)
// ---------------------------------------------------------------------
// Las tres anotaciones @Preview generan tres vistas de la misma tarjeta (abierta)
// en el panel de diseño, para revisar el aspecto sin ejecutar la app.
@Preview(showBackground = true, name = "Claro")
// Modo oscuro: comprueba que los colores del tema se ven bien sobre fondo oscuro.
@Preview(
    showBackground = true,
    name = "Oscuro",
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
// Inglés: locale = "en" hace que la preview lea values-en/strings.xml.
@Preview(showBackground = true, name = "Inglés", locale = "en")
@Composable
private fun TarjetaRecetaPreview() {
    RecetarioPabloLeonTheme {
        TarjetaReceta(
            receta = recetasIniciales().first(), // usa la primera receta (Migas ruleras)
            expandida = true,
            onAlternar = {},
            onVerFicha = {}
        )
    }
}
