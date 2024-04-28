package com.app.claquetaTfg.domain

import com.app.claquetaTfg.domain.generatorId.generateUniqueId
import com.app.claquetaTfg.logs.Logger
import com.app.claquetaTfg.util.Constants.logConfig
import java.lang.RuntimeException
import java.util.Calendar
import java.util.Date
import java.util.Locale

data class UserFilmReviewManager(
    var users: List<String> = listOf(),
    var reviews: List<Review> = listOf(),
    val films: MutableMap<Long, Film> = mutableMapOf(),
    var recommendations: MutableMap<String, List<Long>> = mutableMapOf(),
){

     var logger = Logger.instance(this::class.java, logConfig))

     fun generateUniqueId(obj: Any): Long {
        return when (obj) {
            is Film -> generateFilmUniqueId(obj)
            else -> throw IllegalArgumentException("Objeto no admitido para generar un identificador único")
        }
    }

    private fun generateFilmUniqueId(film: Film): Long {
        return generateUniqueId(film.title, film.movieDirectors[0])
    }

    fun newFilm(
        title: String,
        movieDirectors: List<String>,
        screenwriters: List<String>,
        releaseDate: Int,
        producers: List<String>,
        consPlataforms: List<String>
    ): Long {
        val newF = Film(0, title, movieDirectors, screenwriters, releaseDate, producers, consPlataforms)
        val newID = generateFilmUniqueId(newF)

        val trueFilm = Film(newID, title, movieDirectors, screenwriters, releaseDate, producers, consPlataforms)
        films[newID] = trueFilm
        logger.debug("Film creation event:\n" +
                "-id_film= $newID\n" +
                "-date_of_creation= ${Calendar.getInstance().time}"
                )
        return newID
    }

    fun newUser(username: String) {
        if (!users.contains(username.lowercase(Locale.getDefault()))) {
            users += username.lowercase(Locale.getDefault())
        } else {
            throw UserAlreadyExistsException("This user already exists")
        }
    }

    fun newReview(
        contentPlot: String,
        contentPerformance: String,
        contentDirection: String,
        userAuthor: String,
        filmId: Long,
        creationDate: Date
    ) {
        val oldReview = reviews.find { (it.filmId == filmId) and (it.userAuthor == userAuthor) }
        if (oldReview == null) {
                    val newR = Review(
                        contentPlot,
                        contentPerformance,
                        contentDirection,
                        userAuthor,
                        filmId,
                        creationDate
                    )
                    reviews += newR
                    recomendFilmToUser(userAuthor)
                    logger.info(
                	"Review creation event:\n" +
                        	"-user= $userAuthor\n" +
                        	"-id_film= $filmId\n" +
                        	"-title_film= ${films[filmId]?.title}\n" +
                        	"-content_plot= $contentPlot"
            	    )
        } else {
	    logger.error(
                "Review duplication event:\n" +
                        "-user= $userAuthor\n" +
                        "-id_film= $filmId\n" +
                        "-title_film= ${films[filmId]?.title}\n" +
                        "-date_of_old_review= ${oldReview.creationDate}\n" +
                        "-date_of_attempted_duplication= ${Calendar.getInstance().time}"
            )
            throw ReviewDuplicateException("You have already written a review of this movie")
        }
    }

    fun recomendFilmToUser(username: String) {
        var filtro: List<Review> = reviews.filter { it.userAuthor == username }
        var filtroIds: MutableList<Long> = mutableListOf()

        for (i in filtro){
            filtroIds.add(i.filmId)
        }

        var directorsUser: Set<String> = filtro.flatMap { films[it.filmId]?.movieDirectors.orEmpty() }.toSet()
        var productorsUser: Set<String> = filtro.flatMap { films[it.filmId]?.producers.orEmpty() }.toSet()
        var platformsUser: Set<String> = filtro.flatMap { films[it.filmId]?.consPlatforms.orEmpty() }.toSet()
        var screenwritersUser: Set<String> = filtro.flatMap { films[it.filmId]?.screenwriters.orEmpty() }.toSet()
        val filmsToUser: MutableSet<Long> = mutableSetOf()

        var recomends: List<Set<Any>> = listOf(directorsUser, productorsUser, platformsUser, screenwritersUser)

        for ((key, film) in films) {
            for (i in recomends.indices) {
                if (recomends[i].any {
                        film.movieDirectors.contains(it) || film.producers.contains(it)
                                || film.consPlatforms.contains(it)
                                || film.screenwriters.contains(it)
                    }) {
                    filmsToUser.add(key)
                }
            }
        }
        var recommendRes : List<Long> = filmsToUser.filterNot {  it in filtroIds }
        recommendations[username] = recommendRes
    }

    class ReviewDuplicateException(message: String) : RuntimeException(message)
    class UserAlreadyExistsException(message: String) : RuntimeException(message)
}
