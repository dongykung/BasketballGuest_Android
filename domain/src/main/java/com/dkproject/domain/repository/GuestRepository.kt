package com.dkproject.domain.repository

import com.dkproject.domain.model.GuestPost

interface GuestRepository {
    suspend fun uploadGuestPost(guestPost: GuestPost): Result<Unit>
}