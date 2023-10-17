package com.app.claquetatfg.model.domain

import com.app.claquetatfg.model.domain.valueObjects.Rating
import java.util.*


data class Film(
    val title : String,
    val description: String,
    val releaseDate: Date,
    val movieDirector : String,
    val duration : Int = 0,
    val rating : Rating
){}
