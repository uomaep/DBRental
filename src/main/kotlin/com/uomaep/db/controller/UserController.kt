package com.uomaep.db.controller

import com.uomaep.db.dto.DBCreateUserDTO
import com.uomaep.db.dto.UserLoginDTO
import com.uomaep.db.dto.UserRegisterDTO
import com.uomaep.db.service.DatabaseService
import com.uomaep.db.service.UserService
import com.uomaep.db.utils.encodeURL
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpSession
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/user")
class UserController(
    val userService: UserService,
    val databaseService: DatabaseService
) {
    @GetMapping("/login")
    fun login(): String {
        return "user/login"
    }

    @PostMapping("/login")
    fun handleLogin(
        reqBody: UserLoginDTO,
        req: HttpServletRequest,
        session: HttpSession
    ): String {
        val result = userService.login(reqBody.account, reqBody.password)
        if(result.isFailure)
            return "redirect:/user/login?msg=${"로그인 실패".encodeURL()}"

        req.session.setAttribute("user", result.getOrThrow())
        return "redirect:/"
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

    @PostMapping("/register")
    fun handleRegister(
        reqBody: UserRegisterDTO,
        req: HttpServletRequest,
        session: HttpSession
    ): String {
        if(reqBody.password != reqBody.checkPassword) {
            return "redirect:/user/register?msg=${"비밀번호가 일치하지 않습니다.".encodeURL()}"
        }

        val newUser = userService.register(reqBody.account, reqBody.password)
        if(newUser.isFailure) {
            return "redirect:/user/register?msg=${"회원가입 실패".encodeURL()}"
        }

        if(Result.runCatching { databaseService.createUser(DBCreateUserDTO(reqBody.account, reqBody.password)) }.isFailure)
            return "redirect:/user/register?msg?=${"회원가입 실패".encodeURL()}"

        return "redirect:/user/login?msg=${"회원가입 성공".encodeURL()}"
    }
}