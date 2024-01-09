package com.app.claqueta_tfg.domain

import com.app.claqueta_tfg.util.DateSerializer
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class Review(
    val contentPlot: String,
    val contentPerformance: String,
    val contentDirection: String,
    val userAuthor: String,
    val filmId: Long,
    @Serializable(with = DateSerializer::class)
    val creationDate: Date
)
