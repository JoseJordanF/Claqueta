package domain

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.Date

class UserFilmReviewManagerTest {

    private lateinit var users: List<String>
    private lateinit var reviews: List<Review>
    private lateinit var films: MutableMap<Long, Film>
    private lateinit var getManager: UserFilmReviewManager

    @BeforeEach
    fun onBefore() {
        users = listOf()
        reviews = listOf()
        films = mutableMapOf()
        getManager = UserFilmReviewManager(users, reviews, films)
    }

    @Test
    fun `when id is generated`() {
        val newF = Film(
            0, "Toy Story", listOf("John Lasseter"), listOf(
                "Andrew Stanton", "Joss Whedon", "Joel Cohen",
                "Alec Sokolow"
            ), Date(96, 3, 14), listOf(
                "Pixar Animation Studios",
                "Walt Disney Pictures"
            ), listOf("Diney+", "YouTube", "Apple TV", "Google Play Peliculas", "Amazon Prime Video", "Moviestar Plus+")
        )

        //When
        val res = getManager.generateUniqueId(newF)
        println("id: $res")
        //Then
        assert(res != 0.toLong())
    }

    @Test
    fun `When we create a new movie`() {

        //When
        getManager.newFilm(
            "Toy Story", listOf("John Lasseter"), listOf(
                "Andrew Stanton", "Joss Whedon", "Joel Cohen",
                "Alec Sokolow"
            ), Date(96, 3, 14), listOf(
                "Pixar Animation Studios",
                "Walt Disney Pictures"
            ), listOf("Diney+", "YouTube", "Apple TV", "Google Play Peliculas", "Amazon Prime Video", "Moviestar Plus+")
        )
        var sizeFilms = getManager.films.size
        println("sizeFilms: $sizeFilms")
        //Then
        assert(getManager.films.isNotEmpty())

    }

    @Test
    fun `When we create two movies with the same data`() {

        //When
        var id_1 = getManager.newFilm(
            "Toy Story", listOf("John Lasseter"), listOf(
                "Andrew Stanton", "Joss Whedon", "Joel Cohen",
                "Alec Sokolow"
            ), Date(96, 3, 14), listOf(
                "Pixar Animation Studios",
                "Walt Disney Pictures"
            ), listOf("Diney+", "YouTube", "Apple TV", "Google Play Peliculas", "Amazon Prime Video", "Moviestar Plus+")
        )
        var id_2 = getManager.newFilm(
            "Toy Story", listOf("John Lasseter"), listOf(
                "Andrew Stanton", "Joss Whedon", "Joel Cohen",
                "Alec Sokolow"
            ), Date(96, 3, 14), listOf(
                "Pixar Animation Studios",
                "Walt Disney Pictures"
            ), listOf("Diney+", "YouTube", "Apple TV", "Google Play Peliculas", "Amazon Prime Video", "Moviestar Plus+")
        )
        var sizeFilms = getManager.films.size
        println("sizeFilms: $sizeFilms")
        println("id_1: $id_1")
        println("id_2: $id_2")
        //Then
        assert(id_1!=id_2)
    }
}