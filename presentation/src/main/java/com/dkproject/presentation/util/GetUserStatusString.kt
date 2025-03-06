package com.dkproject.presentation.util

import androidx.compose.ui.graphics.Color
import com.dkproject.domain.model.User.UserStatus
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

fun getGeustStatusColorSet(userStatus: UserStatus): Pair<Color, Color> {
    return when(userStatus) {
        UserStatus.GUEST -> { Pair(Color(0XFFDCFCE7), Color(0XFF64AD7A))}
        UserStatus.APPLY ->  { Pair(Color(0xFFDBE9FE), Color(0XFF1E50D8))}
        else -> { Pair(Color(0XFFFEE2E1), Color(0XFFBD2927))}
    }
}