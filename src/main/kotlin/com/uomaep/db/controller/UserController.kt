package com.uomaep.db.controller

import jakarta.servlet.http.HttpServletRequest
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/user")
class UserController {
    @GetMapping("/login")
    fun login(): String {
        return "user/login"
    }

    @GetMapping("/logout")
    fun logout(req: HttpServletRequest): String {
        req.session.removeAttribute("user")

        return "redirect:/user/login"
    }

    @GetMapping("/register")
    fun register(): String {
        return "user/register"
    }
}