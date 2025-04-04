package com.uomaep.db.service

import com.uomaep.db.dto.DBCreateUserDTO
import com.uomaep.db.mapper.MySQLMapper
import org.springframework.stereotype.Service

@Service
class DatabaseService(val mysqlMapper: MySQLMapper) {
    fun createUser(user: DBCreateUserDTO): Result<Unit> {
        if(mysqlMapper.isExistsUser(user.account) != 0) {
            return Result.failure(Exception("이미 존재합니다."))
        }

        if(Result.runCatching { mysqlMapper.createUser(user) }.isFailure) {
            return Result.failure(Exception("유저 생성 중 오류가 발생했습니다."))
        }

        return Result.success(Unit)
    }
}