package com.app.claquetaTfg.domain

import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.File
import java.lang.RuntimeException
import java.util.Calendar
import java.util.Locale

//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserFilmReviewManagerTest {

    private lateinit var users: List<String>
    private lateinit var reviews: List<Review>
    private lateinit var films: MutableMap<Long, Film>
    private lateinit var recommendations: MutableMap<String, List<Long>>
    private lateinit var getManager: UserFilmReviewManager
    private lateinit var jsonContentFilms: String
    private lateinit var exampleFilms: List<Film>
    private lateinit var jsonContentReviews: String
    private lateinit var exampleReviews: List<Review>

    @BeforeEach
    fun onBefore() {
        users = listOf()
        reviews = listOf()
        films = mutableMapOf()
        recommendations = mutableMapOf()
        getManager = UserFilmReviewManager(users, reviews, films, recommendations)
        jsonContentFilms =
            File("C:\\Users\\34625\\Desktop\\JOB\\TFG\\DEFINITIVO\\claqueta_tfg\\src\\test\\resources\\filmsExamples.json").readText()
        exampleFilms = Json.decodeFromString(jsonContentFilms)
        jsonContentReviews =
            File("C:\\Users\\34625\\Desktop\\JOB\\TFG\\DEFINITIVO\\claqueta_tfg\\src\\test\\resources\\reviewsExamples.json").readText()
        exampleReviews = Json.decodeFromString(jsonContentReviews)
    }

    @Test
    fun `when id is generated`() {

        //When
        val res = getManager.generateUniqueId(exampleFilms.first())
        println("id: $res")
        //Then
        assert(res != 0.toLong())
    }

    @Test
    fun `When we create a new movie`() {

        //When
        getManager.newFilm(
            exampleFilms.first().title,
            exampleFilms.first().movieDirectors,
            exampleFilms.first().screenwriters,
            exampleFilms.first().releaseDate,
            exampleFilms.first().producers,
            exampleFilms.first().consPlatforms
        )

        var sizeFilms = getManager.films.size
        println("sizeFilms: $sizeFilms")
        //Then
        assert(getManager.films.isNotEmpty())

    }


    @Test
    fun `When we create two movies with the same data`() {
        var ids: MutableList<Long> = mutableListOf()

        //When
        for (i in 0..1) {
            ids.add(
                getManager.newFilm(
                    exampleFilms[i].title,
                    exampleFilms[i].movieDirectors,
                    exampleFilms[i].screenwriters,
                    exampleFilms[i].releaseDate,
                    exampleFilms[i].producers,
                    exampleFilms[i].consPlatforms
                )
            )
        }
        println("ids: $ids")
        var sizeFilms = getManager.films.size
        println("sizeFilms: $sizeFilms")
        //Then
        assert(ids[0] != ids[1])
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
    fun `When a review is created`() {

        var idFilm = getManager.newFilm(
            exampleFilms.first().title,
            exampleFilms.first().movieDirectors,
            exampleFilms.first().screenwriters,
            exampleFilms.first().releaseDate,
            exampleFilms.first().producers,
            exampleFilms.first().consPlatforms
        )
        var fech = Calendar.getInstance()
        getManager.newUser("JoseJordan")
        //When
        getManager.newReview(
            exampleReviews.first().contentPlot,
            exampleReviews.first().contentPerformance,
            exampleReviews.first().contentDirection,
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
            exampleFilms.first().title,
            exampleFilms.first().movieDirectors,
            exampleFilms.first().screenwriters,
            exampleFilms.first().releaseDate,
            exampleFilms.first().producers,
            exampleFilms.first().consPlatforms
        )
        var fech = Calendar.getInstance()
        getManager.newUser("JoseJordan")
        //When
        getManager.newReview(
            exampleReviews.first().contentPlot,
            exampleReviews.first().contentPerformance,
            exampleReviews.first().contentDirection,
            "JoseJordan".lowercase(Locale.getDefault()),
            getManager.films[idFilm]!!.id,
            fech.time
        )
        var sizeReviews = getManager.reviews.size
        println("sizeReviews: $sizeReviews")
        //Then
        assertThrows<RuntimeException> {
            getManager.newReview(
                exampleReviews.first().contentPlot,
                exampleReviews.first().contentPerformance,
                exampleReviews.first().contentDirection,
                "JoseJordan".lowercase(Locale.getDefault()),
                getManager.films[idFilm]!!.id,
                fech.time
            )
        }
    }

    @Test
    fun `When we create a review of a movie, that movie is not added to recommendations`() {

        var idFilm = getManager.newFilm(
            exampleFilms.first().title,
            exampleFilms.first().movieDirectors,
            exampleFilms.first().screenwriters,
            exampleFilms.first().releaseDate,
            exampleFilms.first().producers,
            exampleFilms.first().consPlatforms
        )
        var fech = Calendar.getInstance()
        getManager.newUser("JoseJordan")
        //When
        getManager.newReview(
            exampleReviews.first().contentPlot,
            exampleReviews.first().contentPerformance,
            exampleReviews.first().contentDirection,
            "JoseJordan".lowercase(Locale.getDefault()),
            getManager.films[idFilm]!!.id,
            fech.time
        )
        var sizeReviews = getManager.reviews.size
        println("sizeReviews: $sizeReviews")
        var sizeRecommends =
            getManager.recommendations["JoseJordan".lowercase(Locale.getDefault())]!!.size
        println("sizeRecommends: $sizeRecommends")
        //Then
        assert(sizeRecommends == 0)
    }

    @Test
    fun `When we create a review and there are movies with related data, for the recommendations`() {

        var ids: MutableList<Long> = mutableListOf()

        //When
        for (i in 0..3) {
            ids.add(
                getManager.newFilm(
                    exampleFilms[i].title,
                    exampleFilms[i].movieDirectors,
                    exampleFilms[i].screenwriters,
                    exampleFilms[i].releaseDate,
                    exampleFilms[i].producers,
                    exampleFilms[i].consPlatforms
                )
            )
        }
        println(ids[1])
        var fech = Calendar.getInstance()
        getManager.newUser("JoseJordan")
        //When
        getManager.newReview(
            exampleReviews.first().contentPlot,
            exampleReviews.first().contentPerformance,
            exampleReviews.first().contentDirection,
            "JoseJordan".lowercase(Locale.getDefault()),
            getManager.films[ids[0]]!!.id,
            fech.time
        )
        var sizeReviews = getManager.reviews.size
        println("sizeReviews: $sizeReviews")
        var sizeFilms = getManager.films.size
        println("sizeFilms: $sizeFilms")
        var sizeRecommends =
            getManager.recommendations["JoseJordan".lowercase(Locale.getDefault())]!!.size
        println(
            "sizeRecommends: $sizeRecommends id:" + getManager.recommendations["JoseJordan".lowercase(
                Locale.getDefault()
            )]!![0]
        )
        //Then
        assert(sizeRecommends == 2)
    }


}