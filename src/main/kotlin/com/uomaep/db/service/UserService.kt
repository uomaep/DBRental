package com.uomaep.db.service

import com.uomaep.db.dto.UserDTO
import com.uomaep.db.dto.UserDatabaseDTO
import com.uomaep.db.mapper.UserMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.security.MessageDigest
import java.time.LocalDateTime

@Service
class UserService(val userMapper: UserMapper) {
    companion object {
        @Value("security.password.salt")
        val salt: String = ""

        fun hashPassword(password: String): String {
            val md = MessageDigest.getInstance("SHA-512")
            md.update("$salt$password".toByteArray())
            return md.digest().joinToString("") {eachByte -> "%02x".format(eachByte)}
        }
    }

    fun register(account: String, password: String): Result<UserDTO> {
        val hashedPassword = hashPassword(password)
        val createdId = userMapper.insertUser(UserDTO(null, account,  hashedPassword, LocalDateTime.now()))
        return Result.success(UserDTO(createdId, account, hashedPassword, null))
    }

    fun login(account: String, password: String): Result<UserDTO> {
        val hashedPassword = hashPassword(password)
        val target = userMapper.selectUserByAccount(account)
        if(target == null || target.password != hashedPassword)
            return Result.failure(Exception("로그인 실패"))

        return Result.success(target)
    }

    fun getDatabaseByUserId(userId: Int): List<UserDatabaseDTO> {
        return userMapper.selectDatabaseByUserId(userId)
    }

    fun isExistDatabase(databaseName: String): Boolean {
        return userMapper.selectDatabaseByDatabaseName(databaseName) != null
    }
}