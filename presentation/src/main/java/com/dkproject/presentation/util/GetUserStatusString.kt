package com.dkproject.presentation.util

import com.dkproject.domain.model.UserStatus
import com.dkproject.presentation.R

fun GetUserStatusString(userStatus: UserStatus): Int {
    return when(userStatus) {
        UserStatus.OWNER -> R.string.manage
        UserStatus.GUEST -> R.string.cancelguest
        UserStatus.APPLY -> R.string.applycancel
        UserStatus.DENIED -> R.string.rejectguest
        UserStatus.NONE -> R.string.apply
    }
}

fun getGuestManageStatusString(userStatus: UserStatus): Int {
    return when(userStatus) {
        UserStatus.APPLY -> R.string.applying
        UserStatus.GUEST -> R.string.guest
        UserStatus.DENIED -> R.string.denied
        else -> R.string.etc
    }
}