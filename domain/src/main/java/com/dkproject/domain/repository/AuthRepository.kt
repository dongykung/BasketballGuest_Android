package com.dkproject.domain.repository

import com.dkproject.domain.model.User.User

interface AuthRepository {
    suspend fun checkUserUid(uid: String): Result<Boolean>

    /// true 반환 시 닉네임 사용 가능
    suspend fun checkUserNickName(nickName: String): Result<Boolean>
    suspend fun uploadUserData(user: User): Result<Unit>
    suspend fun getUserData(userUid: String): Result<User>
    suspend fun setFcmToken(userUid: String)
    suspend fun setApplyGuest(myUid: String, postUid: String): Result<Unit>
    suspend fun cancelApplyGuest(myUid: String, postUid: String): Result<Unit>
    suspend fun setPermissionGuest(myUid: String, postUid: String): Result<Unit>
    suspend fun updatePosition(userUid: String, position: List<String>): Result<Unit>
    suspend fun updateHeight(userUid: String, height: Int?): Result<Unit>
    suspend fun updateWeight(userUid: String, weight: Int?): Result<Unit>
    suspend fun updateNickname(userUid: String, nickname: String): Result<Unit>
    suspend fun updateProfileImage(userUid: String, photoUri: String): Result<Unit>
}