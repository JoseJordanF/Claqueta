package com.app.claquetatfg.model.domain

import com.app.claquetatfg.model.domain.valueObjects.User

data class LinkUserReviews(
    val links : MutableMap<String, List<Review>> = mutableMapOf()
)
