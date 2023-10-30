package domain

import java.util.Date


data class Review(
    val contentPlot: String,
    val contentPerformance: String,
    val contentDirection: String,
    val userAuthor: String,
    val filmId: Long,
    val creationDate: Date
)
