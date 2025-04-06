package com.uomaep.db.controller

import com.uomaep.db.configure.BlockSettings
import com.uomaep.db.dto.UserDTO
import com.uomaep.db.dto.UserDatabaseDTO
import com.uomaep.db.service.DatabaseService
import com.uomaep.db.service.UserService
import jakarta.servlet.http.HttpSession
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/database")
class DatabaseController(
    val userService: UserService,
    val databaseService: DatabaseService,
) {
    @Value("\${admin.account}") private lateinit var adminAccount: String

    fun String.hasSpecialCharacter(): Boolean {
        val regex = Regex("[^a-zA-Z0-9]")
        return regex.containsMatchIn(this)
    }

    @PostMapping("/create")
    @ResponseBody
    fun createDatabase(@RequestParam databaseName: String, session: HttpSession): Map<String, Any> {
        val user = session.getAttribute("user") as UserDTO

        if(BlockSettings.blockCreateDatabase && user.account != adminAccount) {
            return mapOf(Pair("result", false), Pair("msg", "관리자에 의해 데이터베이스 생성이 차단되었습니다."))
        }

        if(databaseName.hasSpecialCharacter())
            return mapOf(Pair("result", false), Pair("msg", "잘못된 데이터베이스 이름입니다."))

        if(userService.isExistDatabase(databaseName) || databaseService.isExistDatabase(databaseName))
            return mapOf(Pair("result", false), Pair("msg", "이미 존재하는 데이터베이스 이름입니다."))

        if(databaseService.createDatabase(databaseName, user).isFailure)
            return mapOf(Pair("result", false), Pair("msg", "데이터베이스 생성 중 오류가 발생했습니다."))

        return mapOf(Pair("result", true), Pair("msg", "데이터베이스 생성 완료"))
    }

    @PostMapping("/drop")
    @ResponseBody
    fun dropDatabase(@RequestBody database: UserDatabaseDTO, session: HttpSession): Boolean {
        var user = session.getAttribute("user") as UserDTO
        return !databaseService.drop(database).isFailure
    }
}