package domain

import com.app.claquetatfg_2.domain.SnowFlakeForFilm
import java.util.Date

data class UserFilmReviewManager(
    val users: List<String> = listOf(),
    val reviews: List<Review> = listOf(),
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
}
