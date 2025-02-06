package com.dkproject.data.Repository

import com.dkproject.data.model.toDTO
import com.dkproject.domain.model.GuestPost
import com.dkproject.domain.repository.GuestRepository
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class GuestRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
): GuestRepository {
    override suspend fun uploadGuestPost(guestPost: GuestPost):Result<Unit> = kotlin.runCatching {
        firestore.collection("Guest").add(guestPost.toDTO())
    }
}