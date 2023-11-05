package domain

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.RuntimeException
import java.util.Date
import java.util.Calendar
import java.util.Locale

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
        assert(id_1 != id_2)
    }

    @Test
    fun `When we create a new user`() {

        //When
        getManager.newUser("JoseJordan")
        var sizeUser = getManager.users.size
        println("sizeUser: $sizeUser")
        //logger.debug { "id: $res" }
        //Then
        assert(getManager.users.isNotEmpty())

    }

    @Test
    fun `When we create a new user but one already exists with that name`() {

        //When
        getManager.newUser("josejordan")
        var sizeUser = getManager.users.size
        println("sizeUser: $sizeUser")

        //Then
        assertThrows<RuntimeException> {
            getManager.newUser("JoseJordan")
        }
    }

    @Test
    fun `When a review is created and the film does not exist`() {
        //When
        getManager.newUser("JoseJordan")
        var fech = Calendar.getInstance()

        //Then
        assertThrows<RuntimeException> {
            getManager.newReview(
                "En general una pelicula muy entretenida y con una buena trama",
                "Las actuaciones han sido buenas pero sobre todo el prota ha sobresalido",
                "La direccion podria ser mejor pero no esta mal",
                "JoseJordan",
                0,
                fech.time
            )
        }
    }

    @Test
    fun `When a review is created and the user does not exist`() {
        //When
        getManager.newUser("JoseJordan")
        var fech = Calendar.getInstance()

        //Then
        assertThrows<RuntimeException> {
            getManager.newReview(
                "En general una pelicula muy entretenida y con una buena trama",
                "Las actuaciones han sido buenas pero sobre todo el prota ha sobresalido",
                "La direccion podria ser mejor pero no esta mal",
                "JoseJ".lowercase(Locale.getDefault()),
                0,
                fech.time
            )
        }
    }

    @Test
    fun `When a review is created`() {

        var idFilm = getManager.newFilm(
            "Toy Story", listOf("John Lasseter"), listOf(
                "Andrew Stanton", "Joss Whedon", "Joel Cohen",
                "Alec Sokolow"
            ), Date(96, 3, 14), listOf(
                "Pixar Animation Studios",
                "Walt Disney Pictures"
            ), listOf("Diney+", "YouTube", "Apple TV", "Google Play Peliculas", "Amazon Prime Video", "Moviestar Plus+")
        )
        var fech = Calendar.getInstance()
        getManager.newUser("JoseJordan")
        //When
        getManager.newReview(
            "En general una pelicula muy entretenida y con una buena trama",
            "Las actuaciones han sido buenas pero sobre todo el prota ha sobresalido",
            "La direccion podria ser mejor pero no esta mal",
            "JoseJordan".lowercase(Locale.getDefault()),
            getManager.films[idFilm]!!.id,
            fech.time
        )
        var sizeReviews = getManager.reviews.size
        println("sizeReviews: $sizeReviews")
        //Then
        assert(getManager.reviews.isNotEmpty())
    }
}