package com.app.claquetatfg.model.domain


data class User(
    val username : String,
    val email: String,
    val password: String,
    val reputation : Int = 0
){}
