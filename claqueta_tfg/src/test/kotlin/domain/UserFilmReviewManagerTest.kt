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
    private lateinit var recommendations: MutableMap<String, List<Long>>
    private lateinit var getManager: UserFilmReviewManager

    @BeforeEach
    fun onBefore() {
        users = listOf()
        reviews = listOf()
        films = mutableMapOf()
        recommendations = mutableMapOf()
        getManager = UserFilmReviewManager(users, reviews, films, recommendations)
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
        println("id_1: $id_1")
        println("id_2: $id_2")
        var sizeFilms = getManager.films.size
        println("sizeFilms: $sizeFilms")
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

    @Test
    fun `When we try to create a review of a film that we have already reviewed`() {

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
        assertThrows<RuntimeException> {
            getManager.newReview(
                "En general una pelicula muy entretenida y con una buena trama",
                "Las actuaciones han sido buenas pero sobre todo el prota ha sobresalido",
                "La direccion podria ser mejor pero no esta mal",
                "JoseJordan".lowercase(Locale.getDefault()),
                getManager.films[idFilm]!!.id,
                fech.time
            )
        }
    }

    @Test
    fun `When we create a review of a movie, that movie is not added to recommendations`() {

        var idFilm = getManager.newFilm(
            "Toy Story",
            listOf("John Lasseter"),
            listOf(
                "Andrew Stanton", "Joss Whedon", "Joel Cohen",
                "Alec Sokolow"
            ),
            Date(96, 3, 14),
            listOf(
                "Pixar Animation Studios",
                "Walt Disney Pictures"
            ),
            listOf(
                "Diney+",
                "YouTube",
                "Apple TV",
                "Google Play Peliculas",
                "Amazon Prime Video",
                "Moviestar Plus+"
            )
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
        var sizeRecommends = getManager.recommendations["JoseJordan".lowercase(Locale.getDefault())]!!.size
        println("sizeRecommends: $sizeRecommends")
        //Then
        assert(sizeRecommends == 0)
    }

    @Test
    fun `When we create a review and there are movies with related data, for the recommendations`() {

        var idFilm_1 = getManager.newFilm(
            "Toy Story",
            listOf("John Lasseter"),
            listOf(
                "Andrew Stanton", "Joss Whedon", "Joel Cohen",
                "Alec Sokolow"
            ),
            Date(96, 3, 14),
            listOf(
                "Pixar Animation Studios",
                "Walt Disney Pictures"
            ),
            listOf(
                "Diney+",
                "YouTube",
                "Apple TV",
                "Google Play Peliculas",
                "Amazon Prime Video",
                "Moviestar Plus+"
            )
        )

        var idFilm_2 = getManager.newFilm(
            "Toy Story 2",
            listOf("John Lasseter"),
            listOf(
                "Andrew Stanton", "Joss Whedon", "Joel Cohen",
                "Alec Sokolow"
            ),
            Date(98, 3, 14),
            listOf(
                "Pixar Animation Studios",
                "Walt Disney Pictures"
            ),
            listOf(
                "Diney+",
                "YouTube",
                "Apple TV",
                "Google Play Peliculas",
                "Amazon Prime Video",
                "Moviestar Plus+"
            )
        )
        getManager.newFilm(
            "Toy Story 3",
            listOf("John Lasse"),
            listOf(
                "Andrew Stanton"
            ),
            Date(98, 3, 14),
            listOf(
                "Pixar",
                "Walt"
            ),
            listOf(
                "Diney+",
            )
        )
        getManager.newFilm(
            "Toy Story 3",
            listOf("Tarantino"),
            listOf(
                "Tarantino"
            ),
            Date(99, 3, 14),
            listOf(
                "Fox",
            ),
            listOf(
                "HBO",
            )
        )
        println(idFilm_2)
        var fech = Calendar.getInstance()
        getManager.newUser("JoseJordan")
        //When
        getManager.newReview(
            "En general una pelicula muy entretenida y con una buena trama",
            "Las actuaciones han sido buenas pero sobre todo el prota ha sobresalido",
            "La direccion podria ser mejor pero no esta mal",
            "JoseJordan".lowercase(Locale.getDefault()),
            getManager.films[idFilm_1]!!.id,
            fech.time
        )
        var sizeReviews = getManager.reviews.size
        println("sizeReviews: $sizeReviews")
        var sizeFilms = getManager.films.size
        println("sizeFilms: $sizeFilms")
        var sizeRecommends = getManager.recommendations["JoseJordan".lowercase(Locale.getDefault())]!!.size
        println("sizeRecommends: $sizeRecommends id:" + getManager.recommendations["JoseJordan".lowercase(Locale.getDefault())]!![0])
        //Then
        assert(sizeRecommends == 2)
    }
}