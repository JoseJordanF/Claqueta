package com.app.claquetaTfg.database


import com.app.claquetaTfg.domain.Film
import com.app.claquetaTfg.domain.Review
import com.app.claquetaTfg.domain.User
import com.app.claquetaTfg.util.Constants.resourcesExamplePath
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DatabasePersistenceLayerTest {

    private lateinit var dataManagerFilms: DataManager
    private lateinit var dataManagerReviews: DataManager
    private lateinit var dataManagerUser: DataManager
    private lateinit var jsonContentFilms: String
    private lateinit var exampleFilms: List<Film>
    private lateinit var jsonContentReviews: String
    private lateinit var exampleReviews: List<Review>
    private lateinit var jsonContentUsers: String
    private lateinit var exampleUsers: List<User>


    @BeforeEach
    fun onBefore() {
        dataManagerFilms =
            DataManager(
                DatabasePersistenceLayer<Film>(
                    "$resourcesExamplePath/database/filmsData.json",
                    Film.serializer(), Film::class
                )
            )
        dataManagerReviews =
            DataManager(
                DatabasePersistenceLayer<Review>(
                    "$resourcesExamplePath/database/reviewsData.json",
                    Review.serializer(), Review::class
                )
            )
        dataManagerUser =
            DataManager(
                DatabasePersistenceLayer<User>(
                    "$resourcesExamplePath/database/usersData.json",
                    User.serializer(), User::class
                )
            )
        jsonContentFilms =
            File("$resourcesExamplePath/examples/filmsExamples.json").readText()
        exampleFilms = Json.decodeFromString(jsonContentFilms)
        jsonContentReviews =
            File("$resourcesExamplePath/examples/reviewsExamples.json").readText()
        exampleReviews = Json.decodeFromString(jsonContentReviews)
        jsonContentUsers =
            File("$resourcesExamplePath/examples/usersExamples.json").readText()
        exampleUsers = Json.decodeFromString(jsonContentUsers)
    }

    @Test
    fun `When I want to save only one film`() {
        val uniqFilm = Film(
            exampleFilms[1].id,
            exampleFilms[1].title,
            exampleFilms[1].movieDirectors,
            exampleFilms[1].screenwriters,
            exampleFilms[1].releaseDate,
            exampleFilms[1].producers,
            exampleFilms[1].consPlatforms
        )

        //When
        dataManagerFilms.saveData(uniqFilm)
        val loadList = dataManagerFilms.loadData() as List<Film>

        //Then
        assertTrue(loadList.isNotEmpty())
        assertEquals(uniqFilm.id, loadList.last().id)
    }

    @Test
    fun `When I want to save a list of movies`() {

        val filmList: MutableCollection<Film> = mutableListOf()
        for (i in 1..2) {
            val auxFilm = Film(
                exampleFilms[i].id,
                exampleFilms[i].title,
                exampleFilms[i].movieDirectors,
                exampleFilms[i].screenwriters,
                exampleFilms[i].releaseDate,
                exampleFilms[i].producers,
                exampleFilms[i].consPlatforms
            )
            filmList.add(auxFilm)
        }

        //When
        dataManagerFilms.saveData(filmList)
        val loadList = dataManagerFilms.loadData() as List<Film>

        //Then
        assertTrue(loadList.isNotEmpty())
        assertEquals(filmList.last().id, loadList.last().id)
        assertEquals(filmList.toList()[1].id, loadList[loadList.size - 2].id)
        assertEquals(filmList.toList()[0].id, loadList[loadList.size - 3].id)
    }

    @Test
    fun `When I want to save only one review`() {
        val uniqReview = Review(
            exampleReviews[1].contentPlot,
            exampleReviews[1].contentPerformance,
            exampleReviews[1].contentDirection,
            exampleReviews[1].userId,
            exampleReviews[1].filmId,
            exampleReviews[1].creationDate
        )

        //When
        dataManagerReviews.saveData(uniqReview)
        val loadList = dataManagerReviews.loadData() as List<Review>

        //Then
        assertTrue(loadList.isNotEmpty())
        assertEquals(uniqReview.filmId, loadList.last().filmId)
        assertEquals(uniqReview.userId, loadList.last().userId)
    }

    @Test
    fun `When I want to save a list of reviews`() {
        val reviewList: MutableCollection<Review> = mutableListOf()
        for (i in 0..1) {
            val auxReview = Review(
                exampleReviews[i].contentPlot,
                exampleReviews[i].contentPerformance,
                exampleReviews[i].contentDirection,
                exampleReviews[i].userId,
                exampleReviews[i].filmId,
                exampleReviews[i].creationDate
            )
            reviewList.add(auxReview)
        }

        //When
        dataManagerReviews.saveData(reviewList)
        val loadList = dataManagerReviews.loadData() as List<Review>

        //Then
        assertTrue(loadList.isNotEmpty())
        assertEquals(reviewList.last().filmId, loadList.last().filmId)
        assertEquals(reviewList.last().userId, loadList.last().userId)
        assertEquals(reviewList.toList()[0].filmId, loadList[loadList.size - 2].filmId)
        assertEquals(reviewList.toList()[0].userId, loadList[loadList.size - 2].userId)
    }

    @Test
    fun `When I want to save only one user`() {
        val uniqUser = User(
            exampleUsers[1].id,
            exampleUsers[1].userName
        )

        //When
        dataManagerUser.saveData(uniqUser)
        val loadList = dataManagerUser.loadData() as List<User>

        //Then
        assertTrue(loadList.isNotEmpty())
        assertEquals(uniqUser.id, loadList.last().id)
        assertEquals(uniqUser.userName, loadList.last().userName)
    }

    @Test
    fun `When I want to save a list of users`() {
        val usersList: MutableCollection<User> = mutableListOf()
        for (i in 0..1) {
            val auxUser = User(
                exampleUsers[i].id,
                exampleUsers[i].userName
            )
            usersList.add(auxUser)
        }

        //When
        dataManagerUser.saveData(usersList)
        val loadList = dataManagerUser.loadData() as List<User>

        //Then
        assertTrue(loadList.isNotEmpty())
        assertEquals(usersList.last().id, loadList.last().id)
        assertEquals(usersList.last().userName, loadList.last().userName)
        assertEquals(usersList.toList()[0].id, loadList[loadList.size-2].id)
        assertEquals(usersList.toList()[0].userName, loadList[loadList.size-2].userName)
    }
}


