package com.dkproject.domain.model.Chat

data class ChatUserInfo(
    val nickname: String,
    val profileUrl: String,
    val status: String = "active"
)