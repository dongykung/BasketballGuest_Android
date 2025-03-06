package com.dkproject.domain.model.User

import com.dkproject.domain.model.User.UserStatus.APPLY
import com.dkproject.domain.model.User.UserStatus.GUEST
import com.dkproject.domain.model.User.UserStatus.NONE
import com.dkproject.domain.model.User.UserStatus.OWNER
import com.dkproject.domain.model.User.UserStatus.DENIED

enum class UserStatus {
    OWNER,
    GUEST,
    APPLY,
    DENIED,
    NONE;
}

fun fromFirestoreValue(value: String): UserStatus {
    return when (value) {
        "OWNER" -> OWNER
        "GUEST" -> GUEST
        "APPLY" -> APPLY
        "DENIED" -> DENIED
        else -> NONE
    }
}

data class UserPostStatus(
    val status: String = ""
)