package com.dkproject.domain.model.Chat

import java.util.Date

data class Chat(
    val id: String ?= null,
    val message: String,
    val sender: String,
    val readBy: List<String>,
    val createAt: Date = Date()
)