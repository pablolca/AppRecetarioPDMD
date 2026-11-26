package com.example.recetariopabloleon.data

import com.example.recetariopabloleon.R

// Datos con los que arranca la app (no hay base de datos ni internet: todo está aquí).
// El ViewModel llama a esta función una sola vez al crearse y guarda la lista en su estado.
// Cada Receta lleva:
//  - un id único (1..10), que es lo que viaja en la navegación a la ficha,
//  - textos en español (nombre, ingredientes, pasos, comarca): NO cambian con el botón ES/EN,
//  - una Dificultad (esa sí se traduce, porque apunta a un string de los XML),
//  - imagenId = la foto correspondiente en res/drawable (archivos .webp).
// Para añadir una receta nueva basta con añadir otro Receta(...) a esta lista
// con un id que no se repita y su foto en res/drawable.
fun recetasIniciales(): List<Receta> = listOf(
    Receta(
        id = 1, nombre = "Migas ruleras", tiempoMinutos = 45,
        dificultad = Dificultad.MEDIA, comarca = "La Mancha",
        ingredientes = listOf("pan de migas", "ajo", "chorizo", "pimentón", "aceite de oliva", "uvas"),
        pasos = listOf(
            "Hidrata las migas de pan con un poco de agua y déjalas reposar tapadas.",
            "Dora los ajos y el chorizo en aceite; retira del fuego y añade el pimentón.",
            "Incorpora las migas y remueve a fuego medio hasta que queden sueltas y doradas.",
            "Sirve con uvas o con sardinas, según la tradición de cada pueblo."
        ),
        imagenId = R.drawable.receta_migas_ruleras
    ),
    Receta(
        id = 2, nombre = "Gazpacho manchego", tiempoMinutos = 90,
        dificultad = Dificultad.ALTA, comarca = "La Mancha",
        ingredientes = listOf("conejo", "perdiz", "pimiento choricero", "tomate", "ajos", "tortas cenceñas"),
        pasos = listOf(
            "Trocea la carne, salpimienta y dórala en una cazuela con aceite.",
            "Añade los ajos laminados y el pimiento choricero; cubre con agua.",
            "Cocina a fuego lento hasta que la carne esté tierna.",
            "Incorpora las tortas desmigadas y deja que absorban el caldo antes de servir."
        ),
        imagenId = R.drawable.receta_gazpacho_manchego
    ),
    Receta(
        id = 3, nombre = "Atascaburras", tiempoMinutos = 60,
        dificultad = Dificultad.FACIL, comarca = "Serranía de Cuenca",
        ingredientes = listOf("bacalao", "patata", "ajo", "aceite de oliva", "huevo duro", "nueces"),
        pasos = listOf(
            "Cuece las patatas con piel y el bacalao desalado por separado.",
            "Pela las patatas y machácalas con el bacalao desmigado.",
            "Incorpora el ajo picado y el aceite en hilo hasta obtener una pasta homogénea.",
            "Sirve con huevo duro picado y nueces."
        ),
        imagenId = R.drawable.receta_atascaburras
    ),
    Receta(
        id = 4, nombre = "Pisto manchego", tiempoMinutos = 50,
        dificultad = Dificultad.FACIL, comarca = "La Mancha",
        ingredientes = listOf("calabacín", "berenjena", "pimiento rojo", "tomate", "ajo", "huevo"),
        pasos = listOf(
            "Corta todas las verduras en trozos irregulares del mismo tamaño.",
            "Fríe por separado la berenjena, el calabacín y el pimiento.",
            "Añade el tomate y el ajo; cocina hasta que reduzca.",
            "Sirve en cazuela de barro con un huevo poché encima."
        ),
        imagenId = R.drawable.receta_pisto_manchego
    ),
    Receta(
        id = 5, nombre = "Tiznao", tiempoMinutos = 75,
        dificultad = Dificultad.MEDIA, comarca = "La Mancha",
        ingredientes = listOf("bacalao", "patata", "pimiento rojo", "ajo", "aceite de oliva"),
        pasos = listOf(
            "Coloca en una fuente de horno capas de patata, bacalao y pimiento.",
            "Añade los ajos laminados y un buen chorro de aceite.",
            "Hornea a 180 grados durante una hora, regando de vez en cuando."
        ),
        imagenId = R.drawable.receta_tiznao
    ),
    Receta(
        id = 6, nombre = "Caldereta de cordero", tiempoMinutos = 120,
        dificultad = Dificultad.ALTA, comarca = "La Mancha",
        ingredientes = listOf("cordero", "patata", "pimiento", "tomate", "ajo", "vino blanco"),
        pasos = listOf(
            "Sella los trozos de cordero en una cazuela con aceite.",
            "Añade el pimiento, el ajo y el tomate; riega con el vino blanco.",
            "Cubre con agua y cocina a fuego lento durante una hora y media.",
            "Incorpora la patata en trozos y termina la cocción."
        ),
        imagenId = R.drawable.receta_caldereta_cordero
    ),
    Receta(
        id = 7, nombre = "Sopa de ajo", tiempoMinutos = 30,
        dificultad = Dificultad.FACIL, comarca = "La Mancha",
        ingredientes = listOf("pan seco", "ajo", "pimentón", "huevo", "caldo", "aceite de oliva"),
        pasos = listOf(
            "Dora los ajos y el pan en una cazuela con aceite.",
            "Retira del fuego, añade el pimentón y cubre con caldo.",
            "Cuece diez minutos, casca los huevos dentro y deja cuajar."
        ),
        imagenId = R.drawable.receta_sopa_ajo
    ),
    Receta(
        id = 8, nombre = "Gachas manchegas", tiempoMinutos = 35,
        dificultad = Dificultad.MEDIA, comarca = "La Mancha",
        ingredientes = listOf("harina de almortas", "ajo", "pimentón", "chorizo", "agua", "sal"),
        pasos = listOf(
            "Dora el ajo y el chorizo en aceite; retira del fuego.",
            "Añade la harina de almortas y el pimentón, removiendo rápido.",
            "Incorpora el agua poco a poco sin dejar de remover hasta que espese."
        ),
        imagenId = R.drawable.receta_gachas_manchegas
    ),
    Receta(
        id = 9, nombre = "Morteruelo", tiempoMinutos = 90,
        dificultad = Dificultad.ALTA, comarca = "Serranía de Cuenca",
        ingredientes = listOf("hígado de cerdo", "carne de caza", "ajo", "pan", "pimienta", "manteca"),
        pasos = listOf(
            "Cocina las carnes con el ajo y las especias hasta que estén muy tiernas.",
            "Desmenuza y mezcla con el pan remojado.",
            "Machaca hasta obtener una pasta fina y uniforme.",
            "Pásala a una terrina, cubre con manteca y deja reposar."
        ),
        imagenId = R.drawable.receta_morteruelo
    ),
    Receta(
        id = 10, nombre = "Perdiz toledana", tiempoMinutos = 100,
        dificultad = Dificultad.ALTA, comarca = "Toledo",
        ingredientes = listOf("perdiz", "tocino", "ajo", "laurel", "vino blanco", "pan"),
        pasos = listOf(
            "Fríe las perdices limpias en una cazuela con el tocino.",
            "Añade el ajo, el laurel y el vino blanco; cocina hasta reducir.",
            "Cubre con agua y estofa a fuego lento hasta que la carne esté tierna.",
            "Sirve sobre rebanadas de pan frito con la salsa por encima."
        ),
        imagenId = R.drawable.receta_perdiz_toledana
    )
)