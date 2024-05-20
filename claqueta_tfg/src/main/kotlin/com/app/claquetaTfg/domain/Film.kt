package com.app.claquetaTfg.domain

import kotlinx.serialization.Serializable

@Serializable
data class Film(
    val id: String,
    val title: String,
    val movieDirectors: List<String> = listOf(),
    val screenwriters: List<String> = listOf(),
    val releaseDate: Int,
    val producers: List<String> = listOf(),
    val consPlatforms: List<String> = listOf()
)