package com.uomaep.db.dto

class UserRegisterDTO (
    val account: String,
    val password: String,
    val checkPassword: String,
    val csrf: String
)