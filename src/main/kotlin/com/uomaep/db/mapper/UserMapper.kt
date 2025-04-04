package com.uomaep.db.mapper

import com.uomaep.db.dto.UserDTO
import org.apache.ibatis.annotations.Mapper
import org.springframework.stereotype.Repository

@Mapper
@Repository
interface UserMapper {
    fun insertUser(user: UserDTO): Int
    fun selectUserByAccount(account: String): UserDTO?
}