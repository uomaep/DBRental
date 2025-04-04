package com.uomaep.db.controller

import com.uomaep.db.dto.UserDTO
import com.uomaep.db.service.UserService
import jakarta.servlet.http.HttpSession
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/")
class MainController(val userService: UserService) {
    @Value("\${admin.account}")
    lateinit var admin: String

    @GetMapping
    fun index(model: Model, session: HttpSession): String {
        val user = session.getAttribute("user") as UserDTO
        val databases = userService.getDatabaseByUserId(user.id!!)
        model.addAttribute("databases", databases)
        model.addAttribute("userId", user.id)
        model.addAttribute("isAdmin", user.account == admin)

        return "index"
    }

}