package com.dkproject.data.Repository

import android.content.Context
import com.dkproject.data.R
import com.dkproject.data.model.UserDTO
import com.dkproject.data.model.UserPostStatusDTO
import com.dkproject.data.model.toDTO
import com.dkproject.domain.Error.ErrorType
import com.dkproject.domain.model.UnitResult
import com.dkproject.domain.model.User.User
import com.dkproject.domain.repository.AuthRepository
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    @ApplicationContext private val context: Context
) : AuthRepository {
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
            val document =
                firestore.collection("User").whereEqualTo("nickName", nickName).get().await()
            return document.isEmpty
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun uploadUserData(user: User): Result<Unit> = kotlin.runCatching {
        firestore.collection("User").document(user.id).set(user.toDTO())
    }

    override suspend fun getUserData(userUid: String): User {
        try {
            val snapshot = firestore.collection("User").document(userUid).get().await()

            if (!snapshot.exists()) {
                throw NoSuchElementException(context.getString(R.string.nouser))
            }
            val userDTO = snapshot.toObject(UserDTO::class.java) ?: throw Exception()
            return userDTO.toDomain()
        }  catch (e: Exception) {
            throw e
        }
    }

    override suspend fun setFcmToken(userUid: String) {
        try {
            val token = Firebase.messaging.token.await()
            val tokenData = mapOf(
                "token" to token,
                "updatedAt" to FieldValue.serverTimestamp()
            )
            firestore.collection("User")
                .document(userUid)
                .collection("FcmToken")
                .document(userUid)
                .set(tokenData)
                .await()
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun setApplyGuest(myUid: String, postUid: String) {
        try {
            val data = UserPostStatusDTO("APPLY")
            firestore.collection("User").document(myUid).collection("Participants").document(postUid).set(data).await()
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun cancelApplyGuest(myUid: String, postUid: String) {
        try {
            firestore.collection("User").document(myUid).collection("Participants").document(postUid).delete().await()
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun setPermissionGuest(myUid: String, postUid: String): UnitResult {
        return try {
            firestore.collection("User").document(myUid).collection("Participants").document(postUid).update("status", "GUEST").await()
            UnitResult.Success
        } catch (e: Exception) {
            UnitResult.Error(ErrorType.UNKNOWN_ERROR)
        }
    }

    override suspend fun updatePosition(userUid: String, position: List<String>): UnitResult {
        return try {
            firestore.collection("User").document(userUid).update("position", position).await()
            UnitResult.Success
        } catch (e: Exception) {
            UnitResult.Error(ErrorType.UNKNOWN_ERROR)
        }
    }

    override suspend fun updateHeight(userUid: String, height: Int?): UnitResult {
        return try {
            firestore.collection("User").document(userUid).update("height", height).await()
            UnitResult.Success
        } catch (e: Exception) {
            UnitResult.Error(ErrorType.UNKNOWN_ERROR)
        }
    }

    override suspend fun updateWeight(userUid: String, weight: Int?): UnitResult {
        return try {
            firestore.collection("User").document(userUid).update("weight", weight).await()
            UnitResult.Success
        } catch (e: Exception) {
            UnitResult.Error(ErrorType.UNKNOWN_ERROR)
        }
    }

    override suspend fun updateNickname(userUid: String, nickname: String): UnitResult {
        return try {
            firestore.collection("User").document(userUid).update("nickName", nickname).await()
            UnitResult.Success
        } catch (e: Exception) {
            UnitResult.Error(ErrorType.UNKNOWN_ERROR)
        }
    }

    override suspend fun updateProfileImage(userUid: String, photoUri: String): UnitResult {
        return try {
            firestore.collection("User").document(userUid).update("profileImageUrl", photoUri).await()
            UnitResult.Success
        } catch (e: Exception) {
            UnitResult.Error(ErrorType.UNKNOWN_ERROR)
        }
    }
}