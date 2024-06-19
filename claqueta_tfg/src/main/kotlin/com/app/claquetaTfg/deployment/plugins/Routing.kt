package com.app.claquetaTfg.deployment.plugins

import com.app.claquetaTfg.deployment.ClaquetaService
import com.app.claquetaTfg.domain.Film
import com.app.claquetaTfg.domain.Review
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.install
import io.ktor.server.application.call
import io.ktor.server.application.Application
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.request.receive
import io.ktor.server.response.respondText
import io.ktor.server.response.respond
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import io.ktor.server.routing.get
import io.ktor.server.routing.post

fun Application.configureRouting() {
    val service = ClaquetaService()
	install(ContentNegotiation) {
        json()
    }
    routing {
        route("/films") {
            get {
                service.findAllFilms().run { call.respond(this) }
            }
            post {
                val film = call.receive<Film>()
                try {
                    service.aFilm(film).run { call.respond(HttpStatusCode.Created, this) }
                } catch (e: Exception) {
                    call.respondText(e.message!!, status = HttpStatusCode.BadRequest)
                }
            }
            get("{idFilm}") {
                try {
                    service.findFilmById(call.parameters["idFilm"]!!).run { call.respond(this) }
                } catch (e: Exception) {
                    call.respondText(e.message!!, status = HttpStatusCode.NotFound)
                }

            }
            route("/{idFilm}/reviews") {

                get {
                    try {
                        service.findReviewsById(call.parameters["idFilm"]!!).run { call.respond(this) }
                    } catch (e: Exception) {

                        call.respondText(e.message!!, status = HttpStatusCode.NotFound)
                    }
                }
                post {
                    val review = call.receive<Review>()
                    try {
                        service.aReview(review).run { call.respond(HttpStatusCode.Created, this) }
                    } catch (e: Exception) {
                        call.respondText(e.message!!, status = HttpStatusCode.BadRequest)
                    }
                }
            }
        }

        route("/users") {
            post {
                val user = call.receive<String>()
                try {
                    service.aUser(user).run { call.respond(HttpStatusCode.Created, this) }
                } catch (e: Exception) {
                    call.respondText(e.message!!, status = HttpStatusCode.BadRequest)
                }
            }
            get("/{idUser}/recommendations") {
                try {
                    service.findRecomendationsById(call.parameters["idUser"]!!).run { call.respond(this) }
                } catch (e: Exception) {
                    call.respondText(e.message!!, status = HttpStatusCode.NotFound)
                }
            }
        }
    }
}
