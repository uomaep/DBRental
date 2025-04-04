package com.uomaep.db.mapper

import com.uomaep.db.dto.UserDTO
import com.uomaep.db.dto.UserDatabaseDTO
import org.apache.ibatis.annotations.Mapper
import org.springframework.stereotype.Repository

@Mapper
@Repository
interface UserMapper {
    fun insertUser(user: UserDTO): Int
    fun selectUserByAccount(account: String): UserDTO?
    fun selectDatabaseByUserId(userId: Int): List<UserDatabaseDTO>
    fun selectDatabaseByDatabaseName(databaseName: String): UserDatabaseDTO?
    fun insertDatabase(databaseName: String, userId: Int): Int
}