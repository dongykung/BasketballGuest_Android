package com.dkproject.domain.repository

import androidx.paging.PagingData
import com.dkproject.domain.model.Guest.GuestFilter
import com.dkproject.domain.model.GuestManage.GuestManage
import com.dkproject.domain.model.Guest.GuestPost
import com.dkproject.domain.model.User.UserStatus
import kotlinx.coroutines.flow.Flow

interface GuestRepository {
    suspend fun uploadGuestPost(guestPost: GuestPost): Result<Unit>
    fun getGuestPostList(guestFilter: GuestFilter): Flow<PagingData<GuestPost>>
    suspend fun getPostUserStatus(postUid: String, userUid: String): Result<UserStatus>
    suspend fun applyGuestPost(postUid: String, userUid: String): Result<Unit>
    suspend fun cancelGuestPost(postUid: String, userUid: String): Result<Unit>
    suspend fun getPostData(postUid: String): Result<GuestPost>
    fun getGuestManageList(postUid: String): Flow<PagingData<GuestManage>>
    suspend fun acceptGuestUser(postUid: String, userUid: String): Result<Unit>
    suspend fun rejectGuestUser(postUid: String, userUid: String): Result<Unit>
    suspend fun updateGuestPost(postUid: String, guestPost: GuestPost): Result<Unit>
    suspend fun deleteGuestPost(postUid: String): Result<Unit>
}