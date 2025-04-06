package com.uomaep.db.controller

import com.uomaep.db.configure.BlockSettings
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/admin")
class AdminController {
    @PostMapping("/block/login")
    @ResponseBody
    fun toggleLoginBlock(@RequestParam value: Boolean): Map<String, Any> {
        BlockSettings.blockLogin = value
        return mapOf("result" to true, "msg" to "로그인 차단 설정: $value")
    }

    @PostMapping("/block/register")
    @ResponseBody
    fun toggleRegisterBlock(@RequestParam value: Boolean): Map<String, Any> {
        BlockSettings.blockRegister = value
        return mapOf("result" to true, "msg" to "회원가입 차단 설정: $value")
    }

    @PostMapping("/block/createdatabase")
    @ResponseBody
    fun toggleDatabaseCreateBlock(@RequestParam value: Boolean): Map<String, Any> {
        BlockSettings.blockCreateDatabase = value
        return mapOf("result" to true, "msg" to "DB 생성 차단 설정: $value")
    }

    @GetMapping("/status")
    @ResponseBody
    fun getBlockStatus(): Map<String, Boolean> = mapOf(
        "loginBlocked" to BlockSettings.blockLogin,
        "registerBlocked" to BlockSettings.blockRegister,
        "databaseBlocked" to BlockSettings.blockCreateDatabase
    )

    fun isLoginBlocked() = BlockSettings.blockLogin
    fun isRegisterBlocked() = BlockSettings.blockRegister
    fun isDatabaseCreateBlocked() = BlockSettings.blockCreateDatabase
}

