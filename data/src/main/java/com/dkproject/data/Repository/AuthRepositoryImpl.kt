package com.dkproject.data.Repository

import com.dkproject.domain.repository.AuthRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
): AuthRepository {
    override suspend fun checkUserUid(uid: String): Boolean {
        try {
            val document = firestore.collection("User").document(uid).get().await()
            return document.exists()
        } catch (e: Exception) {
            return false
        }
    }

    override suspend fun checkUserNickName(nickName: String): Boolean {
        try {
            val document = firestore.collection("User").whereEqualTo("nickName", nickName).get().await()
            return document.isEmpty
        } catch (e: Exception) {
            throw e
        }
    }
}