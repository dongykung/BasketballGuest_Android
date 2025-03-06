package com.dkproject.domain.model.Chat

import java.util.Date

data class ChatRoom(
   val id: String,
   val lastMessage: String,
   val lastMessageAt: Date,
   val participant: List<String>,
   val readStatus: Map<String, Date>,
   val participantsInfo: Map<String, ChatUserInfo>,
   val unReadCount: Map<String, Int>
)