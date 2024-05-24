package com.app.claquetaTfg.domain

import com.app.claquetaTfg.domain.generatorId.generateUniqueId
import com.app.claquetaTfg.logs.LoggerManager
import com.app.claquetaTfg.logs.SimpleLogger
import java.lang.RuntimeException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

data class ClaquetaManager(
    var users: HashSet<String> = hashSetOf(),
    var reviews: HashMap<String, HashSet<Review>> = hashMapOf(),
    val films: HashMap<String, Film> = hashMapOf(),
    var recommendations: HashMap<String, List<String>> = hashMapOf(),
) {

    private val logger = LoggerManager(SimpleLogger.instance())

    fun generateUniqueId(obj: Any): String {
        return when (obj) {
            is Film -> generateFilmUniqueId(obj)
            else -> throw UnsupportedObjectToGenerateIdException(
                "Object of type ${obj::class.java} not" +
                        " supported to generate unique identifier"
            )
        }
    }

    private fun generateFilmUniqueId(film: Film): String {
        return generateUniqueId(film.title, film.movieDirectors[0])
    }

    fun newFilm(
        title: String,
        movieDirectors: List<String>,
        screenwriters: List<String>,
        releaseDate: Int,
        producers: List<String>,
        consPlataforms: List<String>,
    ): String {
        val newF = Film("0", title, movieDirectors, screenwriters, releaseDate, producers, consPlataforms)
        val newID = generateUniqueId(newF)

        val trueFilm = Film(newID, title, movieDirectors, screenwriters, releaseDate, producers, consPlataforms)
        films[newID] = trueFilm
        logger.log(
            "Film creation event", arrayOf(trueFilm)
        )
        return newID
    }

    fun newUser(name: String) {
        if (users.add(name.lowercase())) {
            logger.log(
                "User creation event", arrayOf(name)
            )
        } else {
            val anotherUser = users.find { it == name }
            logger.error(
                "Error username already in use", arrayOf(anotherUser)
            )
            throw UserAlreadyExistsException(
                "The username $name is in use by another user. Which is:" + "$anotherUser"
            )
        }
    }

    fun newReview(
        contentPlot: String,
        contentPerformance: String,
        contentDirection: String,
        userName: String,
        filmId: String,
        creationDate: Date,
    ) {
        val newR = Review(
            contentPlot, contentPerformance, contentDirection, userName, filmId, creationDate
        )
        val setFilmR = reviews.getOrPut(filmId) { HashSet() }
        if (setFilmR.add(newR)) {
            val setUserR = reviews.getOrPut(userName) { HashSet() }
            setUserR.add(newR)
            logger.log(
                "Review creation event", arrayOf(newR)
            )
            recomendFilmToUser(userName)
        } else {
            val anotherReview = setFilmR.filter { it.userName == userName }.last()
            logger.error(
                "Review duplication error", arrayOf(
                    anotherReview,
                    SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().time)
                )
            )
            throw ReviewDuplicateException(
                "The user $userName had already written a review about the movie $filmId. This was his review: $anotherReview"
            )
        }
    }

    fun recomendFilmToUser(userName: String) {
        val filtro = reviews[userName]
        val filtroIds: List<String>? = filtro?.map { it.filmId }

        val dataToRecommeds = hashSetOf<String>()
        for (id in filtroIds!!) {
            dataToRecommeds.addAll(films[id]!!.movieDirectors)
            dataToRecommeds.addAll(films[id]!!.producers)
            dataToRecommeds.addAll(films[id]!!.consPlatforms)
            dataToRecommeds.addAll(films[id]!!.screenwriters)
        }

        val filmsToFilter: Map<String, Film> = films.filterNot { it.key in filtroIds }
        val recommendRes = mutableListOf<String>()
        for ((key, film) in filmsToFilter) {
            val auxHash = (film.movieDirectors + film.producers + film.consPlatforms + film.screenwriters).toHashSet()
            if (dataToRecommeds.any {
                    auxHash.contains(it)
                }) {
                recommendRes.add(key)
            }
        }
        recommendations[userName] = recommendRes
    }

    class ReviewDuplicateException(message: String) : RuntimeException(message)
    class UserAlreadyExistsException(message: String) : RuntimeException(message)
    class UnsupportedObjectToGenerateIdException(message: String) : IllegalArgumentException(message)

}
