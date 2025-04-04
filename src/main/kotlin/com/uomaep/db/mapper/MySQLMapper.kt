package com.uomaep.db.mapper

import com.uomaep.db.dto.DBCreateUserDTO
import org.apache.ibatis.annotations.Mapper
import org.springframework.stereotype.Repository

@Mapper
@Repository
interface MySQLMapper {
    fun createUser(user: DBCreateUserDTO): Int
    fun isExistsUser(username: String): Boolean
    fun isExistsDatabase(databaseName: String): Boolean
    fun createDatabase(databaseName: String): Int
    fun grantPermission(databaseName: String, account: String)
}