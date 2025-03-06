package com.dkproject.domain.model.GuestManage

import com.dkproject.domain.model.User.User
import com.dkproject.domain.model.User.UserStatus

data class GuestManage(
    val user: User,
    var userStatus: UserStatus
)