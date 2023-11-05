package domain

import com.app.claquetatfg_2.domain.SnowFlakeForFilm

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

}
