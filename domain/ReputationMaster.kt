package com.app.claquetatfg.model.domain

import com.app.claquetatfg.model.domain.valueObjects.User

data class ReputationMaster(
    val repu_users : MutableMap<String, Int> = mutableMapOf()
){}
