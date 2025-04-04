package com.uomaep.db.dto

import java.time.LocalDateTime

data class UserDTO(
    val id: Int? = null,
    val account: String,
    val password: String,
    val createdAt: LocalDateTime? = null
)