package com.app.claquetaTfg.domain

import com.app.claquetaTfg.util.DateSerializer
import kotlinx.serialization.Serializable
import java.util.Date
import java.util.Calendar

@Serializable
data class Review(
    val contentPlot: String="",
    val contentPerformance: String="",
    val contentDirection: String="",
    val userName: String="",
    val filmId: String="",
    @Serializable(with = DateSerializer::class)
    val creationDate: Date = Calendar.getInstance().time
){

	override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Review) return false
        return this.filmId == other.filmId && this.userName == other.userName
    }

    override fun hashCode(): Int {
        var res = filmId.hashCode()
        res = 31 * res + userName.hashCode()
        return res
    }

}

