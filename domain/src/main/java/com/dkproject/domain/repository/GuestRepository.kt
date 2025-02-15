package com.dkproject.domain.repository

import androidx.paging.PagingData
import com.dkproject.domain.model.GuestFilter
import com.dkproject.domain.model.GuestPost
import com.dkproject.domain.model.UserStatus
import kotlinx.coroutines.flow.Flow

interface GuestRepository {
    suspend fun uploadGuestPost(guestPost: GuestPost): Result<Unit>
    fun getGuestPostList(guestFilter: GuestFilter): Flow<PagingData<GuestPost>>
    suspend fun getPostUserStatus(postUid: String, userUid: String): UserStatus
}