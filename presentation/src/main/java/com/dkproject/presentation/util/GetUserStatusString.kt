package com.dkproject.presentation.util

import com.dkproject.domain.model.UserStatus
import com.dkproject.presentation.R

fun GetUserStatusString(userStatus: UserStatus): Int {
    return when(userStatus) {
        UserStatus.OWNER -> R.string.manage
        UserStatus.GUEST -> R.string.cancelguest
        UserStatus.APPLY -> R.string.applycancel
        UserStatus.NONE -> R.string.apply
    }
}