package com.dkproject.domain.model

import com.dkproject.domain.model.UserStatus.APPLY
import com.dkproject.domain.model.UserStatus.GUEST
import com.dkproject.domain.model.UserStatus.NONE
import com.dkproject.domain.model.UserStatus.OWNER

enum class UserStatus {
    OWNER,
    GUEST,
    APPLY,
    NONE;
}

fun fromFirestoreValue(value: String): UserStatus {
    return when (value) {
        "OWNER" -> OWNER
        "GUEST" -> GUEST
        "APPLY" -> APPLY
        else -> NONE
    }
}

data class UserPostStatus(
    val status: String
)