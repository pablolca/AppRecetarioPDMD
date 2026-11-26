package com.example.recetariopabloleon.ui

import androidx.lifecycle.ViewModel
import com.example.recetariopabloleon.data.Receta
import com.example.recetariopabloleon.data.recetasIniciales
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// ViewModel: guarda el ESTADO de la app (los datos que cambian mientras se usa).
// Vive más que las pantallas: Android no lo destruye al girar el móvil,
// por eso la tarjeta abierta y el idioma elegido no se pierden al rotar.
//
// Patrón usado en los tres estados (visto en P5):
//   private val _algo = MutableStateFlow(...)   -> solo el ViewModel puede cambiarlo
//   val algo: StateFlow<...> = _algo.asStateFlow() -> las pantallas solo pueden leerlo
// Las pantallas lo leen con collectAsStateWithLifecycle(); cada vez que el valor cambia,
// Compose vuelve a dibujar (recompone) lo que depende de él.
class RecetasViewModel : ViewModel() {

    // ---------- 1. Lista de recetas ----------
    // Se carga una vez con los datos de RecetasData.kt.
    private val _recetas = MutableStateFlow(recetasIniciales())
    val recetas: StateFlow<List<Receta>> = _recetas.asStateFlow()

    // ---------- 2. Tarjeta expandida en la lista ----------
    // Guarda el id de la tarjeta abierta. null = ninguna abierta.
    // Como solo se guarda UN id, solo puede haber una tarjeta abierta a la vez.
    private val _expandidaId = MutableStateFlow<Int?>(null)
    val expandidaId: StateFlow<Int?> = _expandidaId.asStateFlow()

    // Se llama al pulsar una tarjeta:
    //  - si pulsas la que ya estaba abierta -> se cierra (null)
    //  - si pulsas otra -> esa pasa a ser la abierta (y la anterior se cierra sola)
    fun alternarExpandida(id: Int) {
        _expandidaId.value = if (_expandidaId.value == id) null else id
    }

    // Busca una receta por su id (lo usa la pantalla de ficha).
    // Devuelve null si no existe, y la navegación muestra "receta no encontrada".
    fun recetaConId(id: Int): Receta? = _recetas.value.firstOrNull { it.id == id }

    // ---------- 3. Idioma de la interfaz ----------
    // "es" o "en". Empieza siempre en español, aunque el móvil esté en otro idioma.
    // MainActivity lo lee y, con ConIdioma(), hace que stringResource() tome
    // los textos de values/ (es) o values-en/ (en).
    // Ojo: está en memoria, así que al cerrar la app del todo vuelve a "es".
    private val _idioma = MutableStateFlow("es")
    val idioma: StateFlow<String> = _idioma.asStateFlow()

    // Lo llama el botón ES/EN de la barra superior: cada pulsación cambia al otro idioma.
    fun alternarIdioma() {
        _idioma.value = if (_idioma.value == "es") "en" else "es"
    }
}
