package com.app.claquetaTfg.domain

import kotlin.collections.HashMap
import kotlin.collections.HashSet

interface ClaquetaManager {

    var users: HashSet<String>
    var reviews: HashMap<String, HashSet<Review>>
    val films: HashMap<String, Film>
    var recommendations: HashMap<String, List<String>>

    fun generateUniqueId(obj: Any): String

    fun generateFilmUniqueId(film: Film): String

    fun newFilm(nfilm: Film): String

    fun newUser(name: String)

    fun newReview(nreview: Review)

    fun recomendFilmToUser(userName: String)

}