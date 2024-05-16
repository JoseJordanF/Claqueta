package com.app.claquetaTfg.domain

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val userId: String,
    val userName: String,
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is User) return false
        return this.userName.lowercase() == other.userName.lowercase()
    }

    override fun hashCode(): Int {
        return userName.lowercase().hashCode()
    }

}
