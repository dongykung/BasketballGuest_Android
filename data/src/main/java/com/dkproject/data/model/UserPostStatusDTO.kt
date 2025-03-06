package com.dkproject.data.model

import com.dkproject.domain.model.User.UserPostStatus
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class UserPostStatusDTO(
    val status: String = "",
    @ServerTimestamp val createdAt: Date? = null
) {
    fun toDomain(): UserPostStatus {
        return UserPostStatus(status = status)
    }
}