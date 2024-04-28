package com.app.claquetaTfg.domain

import com.app.claquetaTfg.logs.Logger
import com.app.claquetaTfg.util.Constants.logConfig
import com.app.claquetaTfg.logs.ManagerLogger
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.File
import java.lang.RuntimeException
import java.util.Calendar
import java.util.Locale
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

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
    private lateinit var logger: Logger

    @BeforeEach
    fun onBefore() {
	System.setProperty("CLAQUETA_LOGBACK_CONFIG","src/main/resources/configLogB.xml")
        users = listOf()
        reviews = listOf()
        films = mutableMapOf()
        recommendations = mutableMapOf()
        getManager = UserFilmReviewManager(users, reviews, films, recommendations)
        jsonContentFilms =
            File("src/test/resources/filmsExamples.json").readText()
        exampleFilms = Json.decodeFromString(jsonContentFilms)
        jsonContentReviews =
            File("src/test/resources/reviewsExamples.json").readText()
        exampleReviews = Json.decodeFromString(jsonContentReviews)
	logger = Logger.instance(this::class.java,"CLAQUETA_LOGBACK_CONFIG")
    }

    @Test
    fun `when id is generated`() {

        //When
        val res = getManager.generateUniqueId(exampleFilms.first())
        println("id: $res")
        //Then
        assertNotEquals(res, 0.toLong())
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
        assertTrue(getManager.films.isNotEmpty())

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
        assertNotEquals(ids[0], ids[1])
    }

    @Test
    fun `When we create a new user`() {

        //When
        getManager.newUser("JoseJordan")
        var sizeUser = getManager.users.size
        println("sizeUser: $sizeUser")
        //logger.debug { "id: $res" }
        //Then
        assertTrue(getManager.users.isNotEmpty())

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
        assertTrue(getManager.reviews.isNotEmpty())
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
        assertEquals(sizeRecommends, 0)
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
        assertEquals(sizeRecommends, 2)
    }

    @Test
    fun `When a film is created and the log that indicates this is produced`() {

        val sizeLogInMemoryBefore = ManagerLogger.getLogsInMemory().size

        //When
        var idFilm =getManager.newFilm(
            exampleFilms.first().title,
            exampleFilms.first().movieDirectors,
            exampleFilms.first().screenwriters,
            exampleFilms.first().releaseDate,
            exampleFilms.first().producers,
            exampleFilms.first().consPlatforms
        )

        var dataLog = ManagerLogger.dataLogs(
            ManagerLogger.getLogsInMemory().last().second,
            "-",
            "="
        )
        val sizeLogInMemoryAfter = ManagerLogger.getLogsInMemory().size

        //Then
        assertEquals(idFilm.toString(), dataLog["id_film"])
        assertTrue { sizeLogInMemoryAfter - sizeLogInMemoryBefore == 1 }
    }

    @Test
    fun `When a user creates a review of a film, the log reports`() {

        //When doing all these actions, 2 logs must be created,
        //one for the creation of the film and one for the creation
        //of the review

        val sizeLogInMemoryBefore = ManagerLogger.getLogsInMemory().size

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

        val dataLog = ManagerLogger.dataLogs(
            ManagerLogger.getLogsInMemory().last().second,
            "-",
            "="
        )

        val sizeLogInMemoryAfter = ManagerLogger.getLogsInMemory().size
        //Then
        assertEquals(idFilm.toString(), dataLog["id_film"])
        assertEquals("JoseJordan".lowercase(Locale.getDefault()), dataLog["user"])
        assertTrue { sizeLogInMemoryAfter - sizeLogInMemoryBefore == 2 }
    }

    @Test
    fun `When a user creates a review of a film, in which he or she has already reviewed and should give error log`() {

        //When doing all these actions, 3 logs should be created,
        //one for the creation of the film, one for the creation of
        //the first review and one for the error when trying to create
        //another review of the same film.

        val sizeLogInMemoryBefore = ManagerLogger.getLogsInMemory().size

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

        Thread.sleep(1000)
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

        val dataLog = ManagerLogger.dataLogs(
            ManagerLogger.getLogsInMemory().last().second,
            "-",
            "="
        )
        val sizeLogInMemoryAfter = ManagerLogger.getLogsInMemory().size

        assertEquals(idFilm.toString(), dataLog["id_film"])
        assertEquals("JoseJordan".lowercase(Locale.getDefault()), dataLog["user"])
        assertNotEquals(dataLog["date_of_old_review"],dataLog["date_of_attempted_duplication"])
        assertTrue(sizeLogInMemoryAfter - sizeLogInMemoryBefore == 3)
    }
}
