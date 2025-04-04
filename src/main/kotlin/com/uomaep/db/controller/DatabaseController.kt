package com.uomaep.db.controller

import com.uomaep.db.dto.UserDTO
import com.uomaep.db.dto.UserDatabaseDTO
import com.uomaep.db.mapper.MySQLMapper
import com.uomaep.db.service.DatabaseService
import com.uomaep.db.service.UserService
import jakarta.servlet.http.HttpSession
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody

@Controller
@RequestMapping("/database")
class DatabaseController(
    val userService: UserService,
    val databaseService: DatabaseService,
) {

    fun String.hasSpecialCharacter(): Boolean {
        val regex = Regex("[^a-zA-Z0-9]")
        return regex.containsMatchIn(this)
    }

    @PostMapping("/create")
    @ResponseBody
    fun createDatabase(@RequestParam databaseName: String, session: HttpSession): Map<String, Any> {
        val user = session.getAttribute("user") as UserDTO

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