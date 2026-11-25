package com.example.recetariopabloleon.ui

import androidx.lifecycle.ViewModel
import com.example.recetariopabloleon.data.Receta
import com.example.recetariopabloleon.data.recetasIniciales
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
class RecetasViewModel : ViewModel() {
    // Estado privado mutable / estado público inmutable (patrón visto en P5)
    private val _recetas = MutableStateFlow(recetasIniciales())
    val recetas: StateFlow<List<Receta>> = _recetas.asStateFlow()
    // Id de la tarjeta expandida. null = ninguna.
    private val _expandidaId = MutableStateFlow<Int?>(null)
    val expandidaId: StateFlow<Int?> = _expandidaId.asStateFlow()
    // Pulsar la tarjeta expandida la contrae; pulsar otra, cambia la expandida.
    fun alternarExpandida(id: Int) {
        _expandidaId.value = if (_expandidaId.value == id) null else id
    }
    fun recetaConId(id: Int): Receta? = _recetas.value.firstOrNull { it.id == id }

    // Idioma de los textos de los XML. Español de base; se guarda aquí para
    // que sobreviva a la rotación de pantalla.
    private val _idioma = MutableStateFlow("es")
    val idioma: StateFlow<String> = _idioma.asStateFlow()
    // Cada pulsación del botón rota entre español e inglés.
    fun alternarIdioma() {
        _idioma.value = if (_idioma.value == "es") "en" else "es"
    }
}