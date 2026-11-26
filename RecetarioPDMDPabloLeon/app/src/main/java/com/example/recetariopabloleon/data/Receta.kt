package com.example.recetariopabloleon.data
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.recetariopabloleon.R

// Nivel de dificultad de una receta.
// En vez de guardar el texto ("Fácil", "Media"...) guarda el ID del string de los XML.
// Así, al pintarlo con stringResource(etiquetaRes), sale traducido al idioma activo
// (values/ para español, values-en/ para inglés).
// @StringRes avisa al compilador de que el Int debe ser un R.string.xxx.
enum class Dificultad(@StringRes val etiquetaRes: Int) {
    FACIL(R.string.dificultad_facil),
    MEDIA(R.string.dificultad_media),
    ALTA(R.string.dificultad_alta)
}

// Modelo de datos: una receta del recetario.
// Es una data class, así que Kotlin genera equals(), hashCode(), toString() y copy().
// Todas las propiedades son val (inmutables): para "cambiar" una receta se crea otra con copy().
data class Receta(
    val id: Int,                       // identificador único; se usa en la ruta "ficha/{id}" y como key en la lista
    val nombre: String,                // nombre del plato (no se traduce, va escrito en español)
    val tiempoMinutos: Int,            // tiempo total de preparación
    val dificultad: Dificultad,        // FACIL, MEDIA o ALTA (sí se traduce, ver arriba)
    val comarca: String,               // zona de origen del plato
    val ingredientes: List<String>,    // se pintan con un check delante (BloqueIngredientes)
    val pasos: List<String>,           // se pintan numerados dentro de un círculo (BloquePasos)
    // @DrawableRes: el Int debe ser un R.drawable.xxx (la foto .webp de res/drawable)
    @param:DrawableRes val imagenId: Int,
)
