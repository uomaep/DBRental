package com.uomaep.db.controller

import com.uomaep.db.configure.BlockSettings
import com.uomaep.db.dto.DBCreateUserDTO
import com.uomaep.db.dto.UserLoginDTO
import com.uomaep.db.dto.UserRegisterDTO
import com.uomaep.db.service.DatabaseService
import com.uomaep.db.service.UserService
import com.uomaep.db.utils.encodeURL
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpSession
import org.springframework.beans.factory.annotation.Value
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
    @Value("\${admin.account}") private lateinit var adminAccount: String

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
        if(BlockSettings.blockLogin && reqBody.account != adminAccount) {
            return "redirect:/user/login?msg=${"관리자에 의해 로그인이 차단되었습니다.".encodeURL()}"
        }

        if(!isValidAccount(reqBody.account)) {
            return "redirect:/user/login?msg=${"아이디 또는 비밀번호 형식이 올바르지 않습니다.".encodeURL()}"
        }

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
        if(BlockSettings.blockRegister) {
            return "redirect:/user/login?msg=${"관리자에 의해 회원가입이 비활성화되었습니다.".encodeURL()}"
        }
        return "user/register"
    }

    @PostMapping("/register")
    fun handleRegister(
        reqBody: UserRegisterDTO,
        req: HttpServletRequest,
        session: HttpSession
    ): String {
        if(BlockSettings.blockRegister) {
            return "redirect:/user/login?msg=${"관리자에 의해 회원가입이 비활성화되었습니다.".encodeURL()}"
        }

        if(isValidAccount(reqBody.account)) {
            return "redirect:/user/login?msg=${"아이디 또는 비밀번호 형식이 올바르지 않습니다.".encodeURL()}"
        }

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

    fun isValidAccount(account: String): Boolean {
        return account.matches(Regex("^[^\\\\s]{4,20}\$"))
    }
}