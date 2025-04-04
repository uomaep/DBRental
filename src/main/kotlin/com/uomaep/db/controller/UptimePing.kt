package com.uomaep.db.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/ping")
class UptimePing {
    @GetMapping
    fun ping(): String = "pong"
}