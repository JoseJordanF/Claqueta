package com.app.claquetaTfg.domain

import com.app.claquetaTfg.domain.generatorId.generateUniqueId
import com.app.claquetaTfg.logs.LoggerManager
import com.app.claquetaTfg.logs.SimpleLogger
import java.lang.RuntimeException
import java.text.SimpleDateFormat
import java.util.Calendar


class ClaquetaManagerInMemory : ClaquetaManager {
    override var users: HashSet<String> = hashSetOf()
    override var reviews: HashMap<String, HashSet<Review>> = hashMapOf()
    override val films: HashMap<String, Film> = hashMapOf()
    override var recommendations: HashMap<String, List<String>> = hashMapOf()
    var logger: LoggerManager = LoggerManager(SimpleLogger.instance())

    override fun generateUniqueId(obj: Any): String {
        return when (obj) {
            is Film -> generateFilmUniqueId(obj)
            else -> throw UnsupportedObjectToGenerateIdException(
                "Object of type ${obj::class.java} not" +
                        " supported to generate unique identifier"
            )
        }
    }

    override fun generateFilmUniqueId(film: Film): String {
        return generateUniqueId(film.title, film.movieDirectors[0])
    }

    override fun newFilm(
        nfilm: Film,
    ): String {
        val newID = generateUniqueId(nfilm)

        val trueFilm = Film(
            newID,
            nfilm.title,
            nfilm.movieDirectors,
            nfilm.screenwriters,
            nfilm.releaseDate,
            nfilm.producers,
            nfilm.consPlatforms
        )
        films[newID] = trueFilm
        logger.log(
            "Film creation event", arrayOf(trueFilm)
        )
        return newID
    }

    override fun newUser(name: String) {
        if (users.add(name.lowercase())) {
            logger.log(
                "User creation event", arrayOf(name)
            )
        } else {
            logger.error(
                "Error username already in use", arrayOf(name)
            )
            throw UserAlreadyExistsException(
                "The username $name is in use by another user."
            )
        }
    }

    override fun newReview(
        nreview: Review,
    ) {
        val setFilmR = reviews.getOrPut(nreview.filmId) { HashSet() }
        if (setFilmR.add(nreview)) {
            val setUserR = reviews.getOrPut(nreview.userName) { HashSet() }
            setUserR.add(nreview)
            logger.log(
                "Review creation event", arrayOf(nreview)
            )
            recomendFilmToUser(nreview.userName)
        } else {
            val anotherReview = setFilmR.filter { it.userName == nreview.userName }.last()
            logger.error(
                "Review duplication error", arrayOf(
                    anotherReview,
                    SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().time)
                )
            )
            throw ReviewDuplicateException(
                "The user ${nreview.userName} had already written a review about the movie ${nreview.filmId}. This was his review: $anotherReview"
            )
        }
    }

    override fun recomendFilmToUser(userName: String) {
        val filtro = reviews[userName]
        val filtroIds: List<String>? = filtro?.map { it.filmId }

        val dataToRecommeds = hashSetOf<String>()
        println(filtroIds)
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
