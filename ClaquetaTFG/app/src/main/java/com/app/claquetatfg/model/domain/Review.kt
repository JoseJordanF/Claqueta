package com.app.claquetatfg.model.domain

import java.util.*


data class Review(
    val contentPlot : String,
    val contentPerformance: String,
    val contentDirection: String,
    val userAuthor : String,
    val creationDate : Date,
    val interactions : Double,
    ){}