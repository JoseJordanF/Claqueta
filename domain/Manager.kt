package domain

data class Manager(
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

        return TODO("Provide the return value")
    }
}