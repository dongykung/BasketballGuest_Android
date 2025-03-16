package com.dkproject.data.Repository

import android.content.Context
import com.dkproject.data.R
import com.dkproject.data.model.UserDTO
import com.dkproject.data.model.UserPostStatusDTO
import com.dkproject.data.model.toDTO
import com.dkproject.data.util.mapDomainError
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

    override suspend fun checkUserUid(uid: String): Result<Boolean> = kotlin.runCatching {
        val document = firestore.collection("User").document(uid).get().await()
        document.exists()
    }.mapDomainError()

    override suspend fun checkUserNickName(nickName: String): Result<Boolean> = kotlin.runCatching {
        val document =
            firestore.collection("User").whereEqualTo("nickName", nickName).get().await()
        document.isEmpty
    }.mapDomainError()

    override suspend fun uploadUserData(user: User): Result<Unit> = kotlin.runCatching {
        firestore.collection("User").document(user.id).set(user.toDTO())
        Unit
    }.mapDomainError()

    override suspend fun getUserData(userUid: String): Result<User> = kotlin.runCatching {
        val snapshot = firestore.collection("User").document(userUid).get().await()

        if (!snapshot.exists()) {
            throw NoSuchElementException(context.getString(R.string.nouser))
        }
        val userDTO = snapshot.toObject(UserDTO::class.java) ?: throw Exception()
        userDTO.toDomain()
    }.mapDomainError()

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

    override suspend fun setApplyGuest(myUid: String, postUid: String): Result<Unit> =
        kotlin.runCatching {
            val data = UserPostStatusDTO("APPLY")
            firestore.collection("User").document(myUid).collection("Participants")
                .document(postUid).set(data).await()
            Unit
        }.mapDomainError()

    override suspend fun cancelApplyGuest(myUid: String, postUid: String): Result<Unit> =
        kotlin.runCatching {
            firestore.collection("User").document(myUid).collection("Participants")
                .document(postUid).delete().await()
            Unit
        }.mapDomainError()

    override suspend fun setPermissionGuest(myUid: String, postUid: String): Result<Unit> =
        kotlin.runCatching {
            firestore.collection("User").document(myUid).collection("Participants")
                .document(postUid).update("status", "GUEST").await()
            Unit
        }.mapDomainError()

    override suspend fun updatePosition(userUid: String, position: List<String>): Result<Unit> =
        kotlin.runCatching {
            firestore.collection("User").document(userUid).update("position", position).await()
            Unit
        }.mapDomainError()

    override suspend fun updateHeight(userUid: String, height: Int?): Result<Unit> =
        kotlin.runCatching {
            firestore.collection("User").document(userUid).update("height", height).await()
            Unit
        }.mapDomainError()

    override suspend fun updateWeight(userUid: String, weight: Int?): Result<Unit> =
        kotlin.runCatching {
            firestore.collection("User").document(userUid).update("weight", weight).await()
            Unit
        }.mapDomainError()

    override suspend fun updateNickname(userUid: String, nickname: String): Result<Unit> =
        kotlin.runCatching {
            firestore.collection("User").document(userUid).update("nickName", nickname).await()
            Unit
        }.mapDomainError()

    override suspend fun updateProfileImage(userUid: String, photoUri: String): Result<Unit> =
        kotlin.runCatching {
            firestore.collection("User").document(userUid).update("profileImageUrl", photoUri)
                .await()
            Unit
        }.mapDomainError()
}