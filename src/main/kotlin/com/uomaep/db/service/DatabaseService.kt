package com.uomaep.db.service

import com.uomaep.db.dto.DBCreateUserDTO
import com.uomaep.db.dto.UserDTO
import com.uomaep.db.dto.UserDatabaseDTO
import com.uomaep.db.mapper.MySQLMapper
import com.uomaep.db.mapper.UserMapper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DatabaseService(
    val mysqlMapper: MySQLMapper,
    val userMapper: UserMapper
) {
    fun createUser(user: DBCreateUserDTO): Result<Unit> {
        if(mysqlMapper.isExistsUser(user.account)) {
            return Result.failure(Exception("이미 존재합니다."))
        }

        if(Result.runCatching { mysqlMapper.createUser(user) }.isFailure) {
            return Result.failure(Exception("유저 생성 중 오류가 발생했습니다."))
        }

        return Result.success(Unit)
    }

    fun isExistDatabase(databaseName: String): Boolean {
        return mysqlMapper.isExistsDatabase(databaseName)
    }

    fun createDatabase(databaseName: String, user: UserDTO): Result<Unit> {
        return Result.runCatching {
            userMapper.insertDatabase(databaseName, user.id!!)
            mysqlMapper.createDatabase(databaseName)
            mysqlMapper.grantPermission(databaseName, user.account)
        }
    }

    fun drop(database: UserDatabaseDTO): Result<Unit> {
        return Result.runCatching {
            userMapper.deleteDatabase(database.id!!, database.userId!!)
            mysqlMapper.dropDatabase(database.databaseName!!)
        }
    }

}