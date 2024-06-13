package com.app.claquetaTfg.deployment.plugins

import com.app.claquetaTfg.deployment.module
import com.app.claquetaTfg.domain.Film
import com.app.claquetaTfg.domain.Review
import com.app.claquetaTfg.util.Constants
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.get
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.HttpResponse
import io.ktor.serialization.kotlinx.json.json
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.contentOrNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import java.io.File
import java.util.Calendar
import kotlin.test.Test


class ClaquetaRoutingTest {

    private lateinit var jsonContentFilms: String
    private lateinit var exampleFilms: List<Film>
    private lateinit var jsonContentReviews: String
    private lateinit var exampleReviews: List<Review>
    private lateinit var filmKeys: Array<String>
    private lateinit var reviewKeys: Array<String>

    @BeforeEach
    fun onBefore() {
        jsonContentFilms =
            File(Constants.resourcesExamplePath + "examples/filmsExamples.json").readText()
        exampleFilms = Json.decodeFromString(jsonContentFilms)
        jsonContentReviews =
            File(Constants.resourcesExamplePath + "examples/reviewsExamples.json").readText()
        exampleReviews = Json.decodeFromString(jsonContentReviews)
        filmKeys =
            arrayOf("id", "title", "movieDirectors", "screenwriters", "releaseDate", "producers", "consPlatforms")
        reviewKeys =
            arrayOf("contentPlot", "contentPerformance", "contentDirection", "userName", "filmId", "creationDate")
    }


    @Test
    fun `It is requested, to create a film`() = testApplication {
        application {
            module()
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val response = client.post("/films") {
            contentType(ContentType.Application.Json)
            setBody(
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
        }
        assertEquals(response.status, HttpStatusCode.Created)
        val data = response.bodyAsText()
        val dataJson = Json.parseToJsonElement(data).jsonObject
        for (i in filmKeys) {
            assertTrue(dataJson[i] != null && !isNullOrEmpty(dataJson[i]!!))
        }
    }

    @Test
    fun `All films are requested`() = testApplication {
        application {
            module()
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        for (i in 0..1) {
            client.post("/films") {
                contentType(ContentType.Application.Json)
                setBody(
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
            }
        }
        val response = client.get("/films")
        assertEquals(response.status, HttpStatusCode.OK)
        val data = response.bodyAsText()
        val dataJson = Json.parseToJsonElement(data).jsonArray
        for (i in filmKeys) {
            assertTrue(dataJson[0].jsonObject[i] != null && !isNullOrEmpty(dataJson[0].jsonObject[i]!!))
            assertTrue(dataJson[1].jsonObject[i] != null && !isNullOrEmpty(dataJson[1].jsonObject[i]!!))
        }
    }

    @Test
    fun `A specific film is requested`() = testApplication {
        application {
            module()
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val film = client.post("/films") {
            contentType(ContentType.Application.Json)
            setBody(
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
        }
        val f = film.body<Film>()
        val response = client.get("/films/${f.id}")
        assertEquals(response.status, HttpStatusCode.OK)
        val data = response.bodyAsText()
        val dataJson = Json.parseToJsonElement(data).jsonObject
        for (i in filmKeys) {
            assertTrue(dataJson[i] != null && !isNullOrEmpty(dataJson[i]!!))
        }
    }

    @Test
    fun `Request to create a review of a film`() = testApplication {
        application {
            module()
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val film = client.post("/films") {
            contentType(ContentType.Application.Json)
            setBody(
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
        }
        val f = film.body<Film>()
        val fech = Calendar.getInstance()
        val user = "josejordan"
        val response = client.post("/films/${f.id}/reviews") {
            contentType(ContentType.Application.Json)
            setBody(
                Review(
                    exampleReviews.first().contentPlot,
                    exampleReviews.first().contentPerformance,
                    exampleReviews.first().contentDirection,
                    user,
                    f.id,
                    fech.time
                )
            )
        }
        assertEquals(response.status, HttpStatusCode.Created)
        val data = response.bodyAsText()
        val dataJson = Json.parseToJsonElement(data).jsonObject
        for (i in reviewKeys) {
            assertTrue(dataJson[i] != null && !isNullOrEmpty(dataJson[i]!!))
        }
    }

    @Test
    fun `Reviews of a film are requested`() = testApplication {
        application {
            module()
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val film = client.post("/films") {
            contentType(ContentType.Application.Json)
            setBody(
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
        }
        val f = film.body<Film>()
        val fech = Calendar.getInstance()
        val users = arrayOf("josejordan", "alexjordan")
        for (i in 0..1) {
            client.post("/films/${f.id}/reviews") {
                contentType(ContentType.Application.Json)
                setBody(
                    Review(
                        exampleReviews.first().contentPlot,
                        exampleReviews.first().contentPerformance,
                        exampleReviews.first().contentDirection,
                        users[i],
                        f.id,
                        fech.time
                    )
                )
            }
        }
        val response = client.get("/films/${f.id}/reviews")
        assertEquals(response.status, HttpStatusCode.OK)
        val data = response.bodyAsText()
        val dataJson = Json.parseToJsonElement(data).jsonArray
        for (i in reviewKeys) {
            assertTrue(dataJson[0].jsonObject[i] != null && !isNullOrEmpty(dataJson[0].jsonObject[i]!!))
            assertTrue(dataJson[1].jsonObject[i] != null && !isNullOrEmpty(dataJson[1].jsonObject[i]!!))
        }
    }

    @Test
    fun `The creation of a user is requested`() = testApplication {
        application {
            module()
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val response = client.post("/users") {
            contentType(ContentType.Application.Json)
            setBody(
                "josejordan"
            )
        }
        assertEquals(response.status, HttpStatusCode.Created)
        assertEquals(response.bodyAsText(), "josejordan")
    }

    @Test
    fun `Recommendations from a user are requested`() = testApplication {
        application {
            module()
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val user = client.post("/users") {
            contentType(ContentType.Application.Json)
            setBody(
                "josejordan"
            )
        }
        var film: HttpResponse? = null
        for (i in 0..2) {
            film = client.post("/films") {
                contentType(ContentType.Application.Json)
                setBody(
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
            }
        }
        val f = film!!.body<Film>()
        val fech = Calendar.getInstance()
        client.post("/films/${f.id}/reviews") {
            contentType(ContentType.Application.Json)
            setBody(
                Review(
                    exampleReviews.first().contentPlot,
                    exampleReviews.first().contentPerformance,
                    exampleReviews.first().contentDirection,
                    user.bodyAsText(),
                    f.id,
                    fech.time
                )
            )
        }
        val response = client.get("/users/${user.bodyAsText()}/recommendations")
        assertEquals(response.status, HttpStatusCode.OK)
        val data = response.bodyAsText()
        val dataJson = Json.parseToJsonElement(data).jsonArray
        for (i in filmKeys) {
            assertTrue(dataJson[0].jsonObject[i] != null && !isNullOrEmpty(dataJson[0].jsonObject[i]!!))
            assertTrue(dataJson[1].jsonObject[i] != null && !isNullOrEmpty(dataJson[1].jsonObject[i]!!))
        }
    }


    fun isNullOrEmpty(jsonElement: JsonElement): Boolean {
        return when (jsonElement) {
            is JsonPrimitive -> jsonElement.contentOrNull.isNullOrEmpty()
            is JsonArray -> jsonElement.isEmpty()
            is JsonObject -> jsonElement.isEmpty()
            else -> true
        }
    }
}