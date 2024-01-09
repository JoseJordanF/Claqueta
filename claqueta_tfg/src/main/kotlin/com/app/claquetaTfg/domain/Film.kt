package com.app.claqueta_tfg.domain

import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class Film(
    val id: Long,
    val title: String,
    val movieDirectors: List<String> = listOf(),
    val screenwriters: List<String> = listOf(),
    val releaseDate: Int,
    val producers: List<String> = listOf(),
    val consPlatforms: List<String> = listOf()
)