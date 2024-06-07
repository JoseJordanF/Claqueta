package com.app.claquetaTfg.domain

import ch.qos.logback.classic.Level
import com.app.claquetaTfg.logs.LoggerManager
import com.app.claquetaTfg.logs.SimpleLogger
import com.app.claquetaTfg.util.Constants.loggerLevel
import com.app.claquetaTfg.util.Constants.resourcesExamplePath
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import kotlin.collections.HashSet
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class ClaquetaManagerTest {

     private lateinit var getManager: ClaquetaManager
    private lateinit var jsonContentFilms: String
    private lateinit var exampleFilms: List<Film>
    private lateinit var jsonContentReviews: String
    private lateinit var exampleReviews: List<Review>
    private lateinit var logger: LoggerManager

    @BeforeEach
    fun onBefore() {
        getManager = ClaquetaManagerInMemory()
        jsonContentFilms =
            File(resourcesExamplePath + "examples/filmsExamples.json").readText()
        exampleFilms = Json.decodeFromString(jsonContentFilms)
        jsonContentReviews =
            File(resourcesExamplePath + "examples/reviewsExamples.json").readText()
        exampleReviews = Json.decodeFromString(jsonContentReviews)

        //LOGGER
        logger = LoggerManager(SimpleLogger.instance())
    }

     @Test
    fun `when id is generated`() {

        //When
        val res = getManager.generateUniqueId(exampleFilms.first())
        //Then
        assertNotEquals(res, "0")
    }

    @Test
    fun `When we create a new movie`() {

        //When
        getManager.newFilm(
            Film(
                exampleFilms.first().id,
                exampleFilms.first().title,
                exampleFilms.first().movieDirectors,
                exampleFilms.first().screenwriters,
                exampleFilms.first().releaseDate,
                exampleFilms.first().producers,
                exampleFilms.first().consPlatforms
            )
        )
        //Then
        assertTrue(getManager.films.isNotEmpty())
    }

    @Test
    fun `When we create two movies with the same data`() {
        val ids: MutableList<String> = mutableListOf()

        //When
        for (i in 0..1) {
            ids.add(
                getManager.newFilm(
                    Film(
                        exampleFilms.first().id,
                        exampleFilms[i].title,
                        exampleFilms[i].movieDirectors,
                        exampleFilms[i].screenwriters,
                        exampleFilms[i].releaseDate,
                        exampleFilms[i].producers,
                        exampleFilms[i].consPlatforms
                    )
                )
            )
        }

        //Then
        assertNotEquals(ids[0], ids[1])
    }

    @Test
    fun `When we create a new user`() {

        //When
        getManager.newUser("JoseJordan")

        //Then
        assertTrue(getManager.users.isNotEmpty())
    }

    @Test
    fun `When we create a new user but one already exists with that name`() {

        //When
        val user = "JoseJordan3"
        getManager.newUser(user)

        //Then
        assertThrows<RuntimeException> {
            getManager.newUser(user)
        }
    }

    @Test
    fun `When a review is created`() {

        val idFilm = getManager.newFilm(
            Film(
                exampleFilms.first().id,
                exampleFilms.first().title,
                exampleFilms.first().movieDirectors,
                exampleFilms.first().screenwriters,
                exampleFilms.first().releaseDate,
                exampleFilms.first().producers,
                exampleFilms.first().consPlatforms
            )
        )
        val fech = Calendar.getInstance()
        val user = "josejordan4"
        getManager.newUser(user)
        //When
        getManager.newReview(
            Review(
                exampleReviews.first().contentPlot,
                exampleReviews.first().contentPerformance,
                exampleReviews.first().contentDirection,
                user,
                getManager.films[idFilm]!!.id,
                fech.time
            )
        )
        //Then
        assertTrue(getManager.reviews.isNotEmpty())
    }

    @Test
    fun `When we try to create a review of a film that we have already reviewed`() {

        val idFilm = getManager.newFilm(
            Film(
                exampleFilms.first().id,
                exampleFilms.first().title,
                exampleFilms.first().movieDirectors,
                exampleFilms.first().screenwriters,
                exampleFilms.first().releaseDate,
                exampleFilms.first().producers,
                exampleFilms.first().consPlatforms
            )
        )
        val fech = Calendar.getInstance()
        val user = "josejordan5"
        getManager.newUser(user)
        //When
        getManager.newReview(
            Review(
                exampleReviews.first().contentPlot,
                exampleReviews.first().contentPerformance,
                exampleReviews.first().contentDirection,
                user,
                getManager.films[idFilm]!!.id,
                fech.time
            )
        )
        //Then
        assertThrows<RuntimeException> {
            getManager.newReview(
                Review(
                    exampleReviews.first().contentPlot,
                    exampleReviews.first().contentPerformance,
                    exampleReviews.first().contentDirection,
                    user,
                    getManager.films[idFilm]!!.id,
                    fech.time
                )
            )
        }
    }

    @Test
    fun `When we create a review of a movie, that movie is not added to recommendations`() {

        val idFilm = getManager.newFilm(
            Film(
                exampleFilms.first().id,
                exampleFilms.first().title,
                exampleFilms.first().movieDirectors,
                exampleFilms.first().screenwriters,
                exampleFilms.first().releaseDate,
                exampleFilms.first().producers,
                exampleFilms.first().consPlatforms
            )
        )
        val fech = Calendar.getInstance()
        val user = "josejordan7"
        getManager.newUser(user)
        //When
        getManager.newReview(
            Review(
                exampleReviews.first().contentPlot,
                exampleReviews.first().contentPerformance,
                exampleReviews.first().contentDirection,
                user,
                getManager.films[idFilm]!!.id,
                fech.time
            )
        )
        val recommends =
            getManager.recommendations[user]
        //Then
        assertFalse(recommends!!.contains(idFilm))
    }

    @Test
    fun `When we create a review and there are movies with related data, for the recommendations`() {

        val ids: MutableList<String> = mutableListOf()

        //When
        for (i in 0..3) {
            ids.add(
                getManager.newFilm(
                    Film(
                        exampleFilms[i].id,
                        exampleFilms[i].title,
                        exampleFilms[i].movieDirectors,
                        exampleFilms[i].screenwriters,
                        exampleFilms[i].releaseDate,
                        exampleFilms[i].producers,
                        exampleFilms[i].consPlatforms
                    )
                )
            )
        }
        val fech = Calendar.getInstance()
        val user = "joseAjordan"
        getManager.newUser(user)
        //When
        getManager.newReview(
            Review(
                exampleReviews.first().contentPlot,
                exampleReviews.first().contentPerformance,
                exampleReviews.first().contentDirection,
                user,
                getManager.films[ids[0]]!!.id,
                fech.time
            )
        )
        val sizeRecommends =
            getManager.recommendations[user]!!.size


        //Then
        assertTrue(sizeRecommends >= 2)
    }

    @Test
    fun `When a film is created and the log that indicates this is produced`() {

        //When
        getManager.newFilm(
            Film(
                exampleFilms.first().id,
                exampleFilms.first().title,
                exampleFilms.first().movieDirectors,
                exampleFilms.first().screenwriters,
                exampleFilms.first().releaseDate,
                exampleFilms.first().producers,
                exampleFilms.first().consPlatforms
            )
        )
        val logs: List<HashMap<String, Any>> = logger.historyLogs() as List<HashMap<String, Any>>

        //Then
        assertEquals(loggerLevel.toString(), logs.last()["level"].toString())
        assertEquals("Film creation event", logs.last()["message"].toString())
        assertTrue(logs.isNotEmpty())
    }

    @Test
    fun `When a user creates a review of a film, the log reports`() {
        //When doing all these actions, 2 logs must be created,
        //one for the creation of the film and one for the creation
        //of the review
        val logs: List<HashMap<String, Any>>

        val idFilm = getManager.newFilm(
            Film(
                exampleFilms.first().id,
                exampleFilms.first().title,
                exampleFilms.first().movieDirectors,
                exampleFilms.first().screenwriters,
                exampleFilms.first().releaseDate,
                exampleFilms.first().producers,
                exampleFilms.first().consPlatforms
            )
        )
        val fech = Calendar.getInstance()
        getManager.newUser("josejordan8")
        //When
        getManager.newReview(
            Review(
                exampleReviews.first().contentPlot,
                exampleReviews.first().contentPerformance,
                exampleReviews.first().contentDirection,
                getManager.users.last(),
                getManager.films[idFilm]!!.id,
                fech.time
            )
        )
        logs = logger.historyLogs() as List<HashMap<String, Any>>

        //Then
        assertEquals(loggerLevel.toString(), logs.last()["level"].toString())
        assertEquals("Film creation event", logs[logs.size - 3]["message"].toString())
        assertEquals("User creation event", logs[logs.size - 2]["message"].toString())
        assertEquals("Review creation event", logs.last()["message"].toString())
        assertTrue(logs.size >= 3)
    }

    @Test
    fun `When a user creates a review of a film, in which he or she has already reviewed and should give error log`() {
        //When doing all these actions, 3 logs should be created,
        //one for the creation of the film, one for the creation of
        //the first review and one for the error when trying to create
        //another review of the same film.
        val logs: List<HashMap<String, Any>>

        val idFilm = getManager.newFilm(
            Film(
                exampleFilms.first().id,
                exampleFilms.first().title,
                exampleFilms.first().movieDirectors,
                exampleFilms.first().screenwriters,
                exampleFilms.first().releaseDate,
                exampleFilms.first().producers,
                exampleFilms.first().consPlatforms
            )
        )
        val fech = Calendar.getInstance()
        getManager.newUser("josejordan9")

        //When
        getManager.newReview(
            Review(
                exampleReviews.first().contentPlot,
                exampleReviews.first().contentPerformance,
                exampleReviews.first().contentDirection,
                getManager.users.last(),
                getManager.films[idFilm]!!.id,
                fech.time
            )
        )
        //Then
        assertThrows<RuntimeException> {
            getManager.newReview(
                Review(
                    exampleReviews.first().contentPlot,
                    exampleReviews.first().contentPerformance,
                    exampleReviews.first().contentDirection,
                    getManager.users.last(),
                    getManager.films[idFilm]!!.id,
                    fech.time
                )
            )
        }
        logs = logger.historyLogs() as List<HashMap<String, Any>>

        assertEquals(Level.ERROR.toString(), logs.last()["level"].toString())
        assertEquals("Film creation event", logs[logs.size - 4]["message"].toString())
        assertEquals("User creation event", logs[logs.size - 3]["message"].toString())
        assertEquals("Review creation event", logs[logs.size - 2]["message"].toString())
        assertEquals("Review duplication error", logs.last()["message"].toString())
        assertEquals(
            SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().time),
            logs.last()["item_involved_2"].toString()
        )
        assertTrue(logs.size >= 4)
    }
}