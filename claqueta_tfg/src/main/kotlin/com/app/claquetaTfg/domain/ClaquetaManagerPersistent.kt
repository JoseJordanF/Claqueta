package com.app.claquetaTfg.domain

import com.app.claquetaTfg.database.PersistenceLayer
import com.app.claquetaTfg.logs.LoggerManager
import com.app.claquetaTfg.logs.SimpleLogger
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.core.type.TypeReference

class ClaquetaManagerPersistent(private var persistenceLayerImplementation: PersistenceLayer) : ClaquetaManager {
    override var users: HashSet<String> = loadUsers()
    override val films: HashMap<String, Film> = loadFilms()
    override var reviews: HashMap<String, HashSet<Review>> = loadReviews()
    override var recommendations: HashMap<String, List<String>> = loadRecomendations()
    override var logger: LoggerManager = LoggerManager(SimpleLogger.instance())


    override fun newFilm(
        nfilm: Film,
    ): String {
        val filmId = super.newFilm(nfilm)
        saveFilms()
        return filmId
    }

    override fun newUser(name: String) {
        super.newUser(name)
        saveUser()
    }

    override fun newReview(
        nreview: Review,
    ) {
        super.newReview(nreview)
        saveReviews()
    }

    override fun recomendFilmToUser(userName: String) {
        super.recomendFilmToUser(userName)
        saveRecomendations()
    }

    private fun saveFilms() {
        val filmPair = Pair("films", films.values)
        println(films.values)
        persistenceLayerImplementation.saveData(filmPair)
    }

    private fun loadFilms(): HashMap<String, Film> {
        val data = persistenceLayerImplementation.loadData() as HashMap<String, Any>
        val mapFilm = hashMapOf<String, Film>()
        if (data["films"] == null) {
            return mapFilm
        }
        val filmList: List<Film> = ObjectMapper().convertValue(data["films"], object : TypeReference<List<Film>>() {})
        for (film in filmList) {
            mapFilm[film.id] = film
        }
        return mapFilm
    }

    private fun saveReviews() {
        val reviewsSet = hashSetOf<Review>()
        for (set in reviews.values) {
            reviewsSet.addAll(set)
        }
        val reviewPair = Pair("reviews", reviewsSet)
        persistenceLayerImplementation.saveData(reviewPair)
    }

    private fun loadReviews(): HashMap<String, HashSet<Review>> {
        val data = persistenceLayerImplementation.loadData() as HashMap<String, List<Review>>
        val mapReview = hashMapOf<String, HashSet<Review>>()
        if (data["reviews"] == null) {
            return mapReview
        }

        val reviewList: List<Review> =
            ObjectMapper().convertValue(data["reviews"], object : TypeReference<List<Review>>() {})
        for (review in reviewList) {
            mapReview.getOrPut(review.filmId) { HashSet() }.add(review)
            mapReview.getOrPut(review.userName) { HashSet() }.add(review)
        }
        return mapReview
    }

    private fun saveUser() {
        val userPair = Pair("users", users)
        persistenceLayerImplementation.saveData(userPair)
    }

    private fun loadUsers(): HashSet<String> {
        val data = persistenceLayerImplementation.loadData() as HashMap<String, Any>
        if (data["users"] == null) {
            return hashSetOf()
        }
        return ObjectMapper().convertValue(data["users"], object : TypeReference<HashSet<String>>() {})
    }

    private fun saveRecomendations() {
        val recomendationPair = Pair("recommendations", recommendations)
        persistenceLayerImplementation.saveData(recomendationPair)
    }

    private fun loadRecomendations(): HashMap<String, List<String>> {
        val data = persistenceLayerImplementation.loadData() as HashMap<String, Any>
        var mapRecomendation = hashMapOf<String, List<String>>()
        if (data["recommendations"] == null) {
            return mapRecomendation
        }
        mapRecomendation = ObjectMapper().convertValue(data["recommendations"],
            object : TypeReference<HashMap<String, List<String>>>() {})
        return mapRecomendation
    }
}