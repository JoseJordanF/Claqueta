package com.app.claquetaTfg.deployment

import com.app.claquetaTfg.domain.ClaquetaManagerInMemory
import com.app.claquetaTfg.domain.Film
import com.app.claquetaTfg.domain.Review
import com.app.claquetaTfg.logs.LoggerManager
import com.app.claquetaTfg.logs.SimpleLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.withContext


class ClaquetaService {

    private val getManager = ClaquetaManagerInMemory()
    private var logger: LoggerManager = LoggerManager(SimpleLogger.instance())


    suspend fun findAllFilms(): Flow<Film> {
        logger.log(::findAllFilms.name, arrayOf(getManager.films.size))
        return withContext(Dispatchers.IO) {
            if (getManager.films.isNotEmpty()) {
                return@withContext getManager.films.values.toList().asFlow()
            } else {
                return@withContext emptyFlow()
            }
        }
    }

    suspend fun findFilmById(id: String): Film {
        logger.log(::findFilmById.name, arrayOf(id as String))
        return withContext(Dispatchers.IO) {
            try {
                return@withContext getManager.films[id] as Film
            } catch (e: Exception) {
                throw FilmNotFoundException("The film with id $id was not found")
            }
        }
    }

    suspend fun aFilm(entity: Any): Film {
        return withContext(Dispatchers.IO) {
            try {
                val id = getManager.newFilm(entity as Film)
                return@withContext getManager.films[id]!!
            } catch (e: Exception) {
                throw FilmSaveFailedException("Failed to save the film $entity")
            }
        }
    }

    class FilmSaveFailedException(message: String) : RuntimeException(message)
    class FilmNotFoundException(message: String) : RuntimeException(message)

    suspend fun findReviewsById(id: Any): Flow<Review> {
        logger.log(::findReviewsById.name, arrayOf(id as String))
        return withContext(Dispatchers.IO) {
            try {
                return@withContext getManager.reviews[id]!!.toList().asFlow()
            } catch (e: Exception) {
                throw ReviewsNotFoundException("Reviews for id $id not found")
            }
        }
    }

    suspend fun aReview(entity: Any): Review {
        return withContext(Dispatchers.IO) {
            try {
                getManager.newReview(entity as Review)
                return@withContext entity as Review

            } catch (e: Exception) {
                throw ReviewSaveFailedException(e.message!!)
            }
        }
    }

    class ReviewSaveFailedException(message: String) : RuntimeException(message)
    class ReviewsNotFoundException(message: String) : RuntimeException(message)

    suspend fun findRecomendationsById(id: Any): Flow<Film> {
        logger.log(::findRecomendationsById.name, arrayOf(id as String))
        return withContext(Dispatchers.IO) {
            try {
                getManager.recomendFilmToUser(id)
                val recomendList = getManager.recommendations[id]
                val filmList = getManager.films.filter { recomendList!!.contains(it.key) }
                return@withContext filmList.values.asFlow()
            } catch (e: Exception) {
                throw UserHasNotWrittenReviewException("The user $id has not written any reviews yet. ")
            }
        }
    }

    class UserHasNotWrittenReviewException(message: String) : RuntimeException(message)

    suspend fun findAllUsers(): Flow<String> {
        logger.log(::findAllUsers.name, arrayOf(getManager.users.size))
        return withContext(Dispatchers.IO) {
            if (getManager.users.isNotEmpty()) {
                return@withContext getManager.users.toList().asFlow()
            } else {
                return@withContext emptyFlow()
            }
        }
    }

    suspend fun aUser(entity: Any): String {
        return withContext(Dispatchers.IO) {
            try {
                getManager.newUser((entity as String).trim('"'))
                return@withContext entity as String
            } catch (e: Exception) {
                throw UserSaveFailedException(e.message!!)
            }
        }
    }

    class UserSaveFailedException(message: String) : RuntimeException(message)

}