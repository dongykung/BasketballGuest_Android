package com.dkproject.data.model

import com.dkproject.domain.model.User
import com.google.firebase.firestore.DocumentId

data class UserDTO(
    @DocumentId val id: String,
    val nickName: String,
    val position: List<String>,
    val weight: Double?,
    val height: Double?,
    val profileImageUrl: String
)

fun UserDTO.toDomain(): User {
    return User(
        id = id,
        nickName = nickName,
        position = position,
        weight = weight,
        height = height,
        profileImageUrl = profileImageUrl
    )
}

fun User.toDTO(): UserDTO {
    return UserDTO(
        id = id,
        nickName = nickName,
        position = position,
        weight = weight,
        height = height,
        profileImageUrl = profileImageUrl
    )
}