# AppRecetarioPDMD — Recetario Pablo León

Aplicación Android hecha con **Kotlin + Jetpack Compose (Material 3)** que muestra un recetario de platos tradicionales manchegos. Tiene una lista de recetas con tarjetas animadas, una ficha de detalle que se adapta a la orientación del dispositivo y un botón para cambiar el idioma de la interfaz entre español e inglés.

El proyecto Android está en la carpeta [`RecetarioPabloLeon/`](RecetarioPabloLeon).

## Cómo ejecutarla

1. Abrir la carpeta `RecetarioPabloLeon` en Android Studio (*File → Open*).
2. Esperar a que termine el *Gradle Sync*.
3. Ejecutar la configuración `app` en un emulador o dispositivo (Android 7.0 / API 24 o superior).

## Funcionalidades

- **Lista de recetas**: cada receta aparece en una tarjeta con su foto, tiempo y dificultad. Al pulsar una tarjeta se expande (con animación) y muestra la comarca y el botón *Ver receta completa*. Solo puede haber una tarjeta expandida a la vez.
- **Ficha de la receta**: foto, resumen (tiempo | dificultad | comarca), ingredientes con un check y pasos de elaboración numerados.
- **Diseño adaptativo**: en vertical la foto va arriba y el contenido debajo; en horizontal la foto ocupa el 40 % izquierdo y el contenido, con scroll propio, el 60 % derecho.
- **Cambio de idioma**: el botón **ES / EN** de la barra superior alterna los textos de la interfaz entre español (idioma por defecto, sea cual sea el idioma del dispositivo) e inglés. Solo cambian los textos de los `strings.xml`; los datos de las recetas (nombres, ingredientes, pasos) se quedan en español.

## Estructura del código

```
RecetarioPabloLeon/app/src/main/
├── java/com/example/recetariopabloleon/
│   ├── MainActivity.kt          → Activity, navegación y control del idioma
│   ├── data/
│   │   ├── Receta.kt            → modelo de datos (Receta y Dificultad)
│   │   └── RecetasData.kt       → lista de recetas iniciales
│   └── ui/
│       ├── RecetasViewModel.kt  → estado de la app (recetas, tarjeta expandida, idioma)
│       ├── PantallaReceta.kt    → pantallas: lista y ficha (vertical / horizontal)
│       ├── ComponentesReceta.kt → bloques reutilizables y tarjeta animada
│       └── theme/               → colores, tipografía y tema Material 3
└── res/
    ├── values/strings.xml       → textos en español (idioma base)
    ├── values-en/strings.xml    → textos en inglés
    └── drawable/                → fotos de las recetas (.webp)
```

### Datos (`data/`)

- `Receta` es una `data class` con `id`, `nombre`, `tiempoMinutos`, `dificultad`, `comarca`, `ingredientes`, `pasos` e `imagenId`.
- `Dificultad` es un `enum` (`FACIL`, `MEDIA`, `ALTA`). Cada valor guarda el id del string que lo describe (`etiquetaRes`), así la dificultad se traduce sola con el idioma.
- `recetasIniciales()` devuelve la lista de recetas con la que arranca la app.

### Estado (`RecetasViewModel`)

Sigue el patrón *estado privado mutable / estado público inmutable* con `StateFlow`:

| Estado        | Tipo                   | Para qué sirve                                   |
|---------------|------------------------|--------------------------------------------------|
| `recetas`     | `StateFlow<List<Receta>>` | Lista de recetas que se pinta en pantalla.    |
| `expandidaId` | `StateFlow<Int?>`      | Id de la tarjeta expandida (`null` = ninguna).   |
| `idioma`      | `StateFlow<String>`    | `"es"` o `"en"`; empieza en `"es"`.              |

Funciones: `alternarExpandida(id)`, `recetaConId(id)` y `alternarIdioma()`.

Como el estado está en el ViewModel, sobrevive a la rotación de pantalla.

### Navegación (`MainActivity.kt`)

- `Screen` es una `sealed class` con dos rutas: `lista` y `ficha/{id}`.
- `AppNavigation()` crea el ViewModel **fuera** del `NavHost`. Así su propietario es la Activity y la lista y la ficha comparten la misma instancia.
- La ruta de la ficha recibe el `id` como argumento `Int`. Si no existe la receta se muestra el texto `receta_no_encontrada`.

### Cambio de idioma (`ConIdioma`)

`ConIdioma(idioma) { … }` crea una copia de la `Configuration` actual con el `Locale` elegido y un `Context` a partir de ella, y los inyecta con `CompositionLocalProvider` (`LocalContext` y `LocalConfiguration`). Todo lo que hay dentro, al llamar a `stringResource()`, lee `values/` o `values-en/` según el idioma del ViewModel, sin cambiar el idioma del sistema ni reiniciar la Activity.

### Pantallas (`PantallaReceta.kt`)

- `ListaRecetasScreen`: `Scaffold` con `TopAppBar` (título + botón de idioma) y un `LazyColumn` de `TarjetaReceta`.
- `FichaRecetaScreen`: `Scaffold` con `TopAppBar` (nombre, flecha de volver y botón de idioma). Mira `LocalConfiguration.current.orientation` y elige:
  - `DisposicionVertical`: `Column` con scroll, foto de 220 dp y el contenido debajo.
  - `DisposicionHorizontal`: `Row` con la foto (`weight(0.4f)`) y una `Column` con scroll (`weight(0.6f)`).
- `ContenidoReceta`: resumen + ingredientes + pasos. Lo usan las dos disposiciones, así se escribe una sola vez.
- `BotonIdioma`: `TextButton` que muestra `ES` o `EN` (sacado de los XML) y llama a `alternarIdioma()`.

### Componentes (`ComponentesReceta.kt`)

- `BloqueFoto`: imagen recortada con esquinas redondeadas. El tamaño lo decide quien lo usa con el `modifier`.
- `BloqueResumen`: línea *tiempo | dificultad | comarca*.
- `BloqueIngredientes`: título y un icono de check por ingrediente.
- `BloquePasos`: título y cada paso con su número dentro de un círculo.
- `TarjetaReceta`: tarjeta de la lista con tres animaciones:
  1. `animateColorAsState`: cambia el color de fondo al expandirse.
  2. `animateContentSize`: anima el cambio de altura.
  3. `AnimatedVisibility` (`expandVertically` / `shrinkVertically`): muestra u oculta la comarca y el botón *Ver receta completa*.

## Tecnologías

- Kotlin, Jetpack Compose y Material 3
- Navigation Compose
- Lifecycle ViewModel + `StateFlow` / `collectAsStateWithLifecycle`
- `minSdk` 24 · `targetSdk` 36
