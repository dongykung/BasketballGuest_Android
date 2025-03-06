package com.dkproject.domain.model.MyData

import com.dkproject.domain.model.Guest.GuestPost
import com.dkproject.domain.model.User.UserStatus

data class MyParticipant(
    val post: GuestPost,
    val myStatus: UserStatus
)