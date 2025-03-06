package com.dkproject.domain.repository

import com.dkproject.domain.model.UnitResult
import com.dkproject.domain.model.User.User

interface AuthRepository {
    suspend fun checkUserUid(uid: String): Boolean
    /// true 반환 시 닉네임 사용 가능
    suspend fun checkUserNickName(nickName: String): Boolean
    suspend fun uploadUserData(user: User): Result<Unit>
    suspend fun getUserData(userUid: String): User
    suspend fun setFcmToken(userUid: String)
    suspend fun setApplyGuest(myUid: String, postUid: String)
    suspend fun cancelApplyGuest(myUid: String, postUid: String)
    suspend fun setPermissionGuest(myUid: String, postUid: String): UnitResult
    suspend fun updatePosition(userUid: String, position: List<String>): UnitResult
    suspend fun updateHeight(userUid: String, height: Int?): UnitResult
    suspend fun updateWeight(userUid: String, weight: Int?): UnitResult
    suspend fun updateNickname(userUid: String, nickname: String): UnitResult
    suspend fun updateProfileImage(userUid: String, photoUri: String): UnitResult
}