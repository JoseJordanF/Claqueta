package com.app.claquetatfg.model.domain

data class LinkFilmReviews(
    val links : MutableMap<Long, List<Review>> = mutableMapOf()
){}
