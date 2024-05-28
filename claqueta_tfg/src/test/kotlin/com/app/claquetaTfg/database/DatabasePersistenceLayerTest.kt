package com.app.claquetaTfg.database


import com.app.claquetaTfg.domain.Film
import com.app.claquetaTfg.domain.Review
import com.app.claquetaTfg.util.Constants.resourcesExamplePath
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DatabasePersistenceLayerTest {

    private lateinit var dataManagerFilms: DataManager
    private lateinit var dataManagerReviews: DataManager
    private lateinit var jsonContentFilms: String
    private lateinit var exampleFilms: List<Film>
    private lateinit var jsonContentReviews: String
    private lateinit var exampleReviews: List<Review>


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
        jsonContentFilms =
            File("$resourcesExamplePath/examples/filmsExamples.json").readText()
        exampleFilms = Json.decodeFromString(jsonContentFilms)
        jsonContentReviews =
            File("$resourcesExamplePath/examples/reviewsExamples.json").readText()
        exampleReviews = Json.decodeFromString(jsonContentReviews)      
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
        val loadListBefore = dataManagerFilms.loadData() as List<Film>
        dataManagerFilms.saveData(uniqFilm)
        val loadListAfter = dataManagerFilms.loadData() as List<Film>
        val amountAdd = loadListAfter.size-loadListBefore.size
        //Then
        assertTrue(loadListAfter.isNotEmpty())
        assertFalse(loadListAfter.contains<Film?>(null))
        assertEquals(loadListAfter.size,loadListBefore.size+amountAdd)
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
        val loadListBefore = dataManagerFilms.loadData() as List<Film>
        dataManagerFilms.saveData(filmList)
        val loadListAfter = dataManagerFilms.loadData() as List<Film>
        val amountAdd = loadListAfter.size-loadListBefore.size
        //Then
        assertTrue(loadListAfter.isNotEmpty())
        assertFalse(loadListAfter.contains<Film?>(null))
        assertEquals(loadListAfter.size , loadListBefore.size+amountAdd)
    }

    @Test
    fun `When I want to save only one review`() {
        val uniqReview = Review(
            exampleReviews[1].contentPlot,
            exampleReviews[1].contentPerformance,
            exampleReviews[1].contentDirection,
            exampleReviews[1].userName,
            exampleReviews[1].filmId,
            exampleReviews[1].creationDate
        )

        //When
        val loadListBefore = dataManagerReviews.loadData() as List<Review>
        dataManagerReviews.saveData(uniqReview)
        val loadListAfter = dataManagerReviews.loadData() as List<Review>
        val amountAdd = loadListAfter.size-loadListBefore.size
        //Then
        assertTrue(loadListAfter.isNotEmpty())
        assertFalse(loadListAfter.contains<Review?>(null))
        assertEquals(loadListAfter.size , loadListBefore.size+amountAdd)
    }

    @Test
    fun `When I want to save a list of reviews`() {
        val reviewList: MutableCollection<Review> = mutableListOf()
        for (i in 0..1) {
            val auxReview = Review(
                exampleReviews[i].contentPlot,
                exampleReviews[i].contentPerformance,
                exampleReviews[i].contentDirection,
                exampleReviews[i].userName,
                exampleReviews[i].filmId,
                exampleReviews[i].creationDate
            )
            reviewList.add(auxReview)
        }

        //When
        val loadListBefore = dataManagerReviews.loadData() as List<Review>
        dataManagerReviews.saveData(reviewList)
        val loadListAfter = dataManagerReviews.loadData() as List<Review>
        val amountAdd = loadListAfter.size-loadListBefore.size
        //Then
        assertTrue(loadListAfter.isNotEmpty())
        assertFalse(loadListAfter.contains<Review?>(null))
        assertEquals(loadListAfter.size , loadListBefore.size+amountAdd)
    }
}


