package com.dkproject.domain.repository

import com.dkproject.domain.model.User

interface AuthRepository {
    suspend fun checkUserUid(uid: String): Boolean
    /// true 반환 시 닉네임 사용 가능
    suspend fun checkUserNickName(nickName: String): Boolean
    suspend fun uploadUserData(user: User): Result<Unit>
}