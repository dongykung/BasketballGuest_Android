package com.dkproject.domain.model

import com.dkproject.domain.model.UserStatus.APPLY
import com.dkproject.domain.model.UserStatus.GUEST
import com.dkproject.domain.model.UserStatus.NONE
import com.dkproject.domain.model.UserStatus.OWNER
import com.dkproject.domain.model.UserStatus.DENIED

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