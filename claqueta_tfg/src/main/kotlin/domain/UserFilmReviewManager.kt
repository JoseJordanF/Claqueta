package domain

import com.app.claquetatfg_2.domain.SnowFlakeForFilm
import java.lang.RuntimeException
import java.util.*

data class UserFilmReviewManager(
    var users: List<String> = listOf(),
    var reviews: List<Review> = listOf(),
    val films: MutableMap<Long, Film> = mutableMapOf()
) : UniqueIdGenerator {

    override fun generateUniqueId(obj: Any): Long {
        return when (obj) {
            is Film -> generateFilmUniqueId(obj)
            else -> throw IllegalArgumentException("Objeto no admitido para generar un identificador único")
        }
    }

    private fun generateFilmUniqueId(film: Film): Long {
        val snowFlake = SnowFlakeForFilm()
        return snowFlake.generateUniqueId(film.title, film.movieDirectors[0])
    }

    fun newFilm(
        title: String,
        movieDirectors: List<String>,
        screenwriters: List<String>,
        releaseDate: Date,
        producers: List<String>,
        consPlataforms: List<String>
    ): Long {
        val newF = Film(0, title, movieDirectors, screenwriters, releaseDate, producers, consPlataforms)
        val newID = generateFilmUniqueId(newF)

        val trueFilm = Film(newID, title, movieDirectors, screenwriters, releaseDate, producers, consPlataforms)
        films[newID] = trueFilm
        return newID
    }

    fun newUser(username: String) {
        if (!users.contains(username.lowercase(Locale.getDefault()))) {
            users += username.lowercase(Locale.getDefault())
        } else {
            throw RuntimeException("Ese usuario ya existe")
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
        if (users.contains(userAuthor)) {
            if (films.containsKey(filmId)) {
                val newR = Review(
                    contentPlot,
                    contentPerformance,
                    contentDirection,
                    userAuthor,
                    filmId,
                    creationDate
                )
                reviews += newR
            } else {
                throw RuntimeException("Esa pelicula no existe")
            }
        } else {
            throw RuntimeException("Ese usuario no existe")
        }
    }
}
