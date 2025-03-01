package com.dkproject.domain.repository

import androidx.paging.PagingData
import com.dkproject.domain.model.GuestFilter
import com.dkproject.domain.model.GuestManage
import com.dkproject.domain.model.GuestPost
import com.dkproject.domain.model.UnitResult
import com.dkproject.domain.model.UserStatus
import kotlinx.coroutines.flow.Flow

interface GuestRepository {
    suspend fun uploadGuestPost(guestPost: GuestPost): Result<Unit>
    fun getGuestPostList(guestFilter: GuestFilter): Flow<PagingData<GuestPost>>
    suspend fun getPostUserStatus(postUid: String, userUid: String): UserStatus
    suspend fun applyGuestPost(postUid: String, userUid: String)
    suspend fun cancelGuestPost(postUid: String, userUid: String)
    suspend fun getPostData(postUid: String): GuestPost
    fun getGuestManageList(postUid: String): Flow<PagingData<GuestManage>>
    suspend fun acceptGuestUser(postUid: String, userUid: String): UnitResult
    suspend fun rejectGuestUser(postUid: String, userUid: String): UnitResult
    suspend fun updateGuestPost(postUid: String, guestPost: GuestPost): Result<Unit>
    suspend fun deleteGuestPost(postUid: String): Result<Unit>
}