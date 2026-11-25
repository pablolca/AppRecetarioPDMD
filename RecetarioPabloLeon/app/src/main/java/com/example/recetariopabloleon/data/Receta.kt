package com.example.recetariopabloleon.data
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.recetariopabloleon.R
enum class Dificultad(@StringRes val etiquetaRes: Int) {
    FACIL(R.string.dificultad_facil),
    MEDIA(R.string.dificultad_media),
    ALTA(R.string.dificultad_alta)
}
data class Receta(
    val id: Int,
    val nombre: String,
    val tiempoMinutos: Int,
    val dificultad: Dificultad,
    val comarca: String,
    val ingredientes: List<String>,
    val pasos: List<String>,
    @param:DrawableRes val imagenId: Int,
)