package com.dkproject.data.Repository

import android.util.Log
import com.dkproject.data.data.local.ChatDao
import com.dkproject.data.model.ChatDTO
import com.dkproject.data.model.toDTO
import com.dkproject.data.util.mapDomainError
import com.dkproject.domain.model.Chat.Chat
import com.dkproject.domain.repository.ChatRepository
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.Date
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val chatDao: ChatDao,
    private val firestore: FirebaseFirestore,
) : ChatRepository {
    override fun getChatsByChatRoomId(chatRoomId: String): Flow<List<Chat>> {
        return chatDao.getChatsByChatRoomId(chatRoomId = chatRoomId)
            .map { list -> list.map { it.toDomain() } }
            .flowOn(context = Dispatchers.IO)
    }

    override fun listenToChats(myUid: String, chatRoomId: String): Flow<Unit> = callbackFlow {
        val query = firestore.collection("Chat").document(chatRoomId).collection("Message")
        val listenerRegistration = query.addSnapshotListener { snapshot, error ->
            if (error != null || snapshot == null) {
                trySend(Unit)
                return@addSnapshotListener
            }
            for (diff in snapshot.documentChanges) {
                val chat = diff.document.toObject(ChatDTO::class.java).copy(id = diff.document.id)
                    ?: continue
                when (diff.type) {
                    DocumentChange.Type.ADDED -> {
                        CoroutineScope(context = Dispatchers.IO).launch {
                            if (chat.sender != myUid && !chat.readBy.contains(myUid)) {
                                firestore.collection("Chat").document(chatRoomId)
                                    .collection("Message").document(diff.document.id)
                                    .update("readBy", FieldValue.arrayUnion(myUid)).await()
                                firestore.collection("Chat").document(chatRoomId)
                                    .update("readStatus.${myUid}", Date()).await()
                            }
                            chatDao.insertChat(chat.toChatEntity(chatRoomId = chatRoomId))
                        }
                    }

                    DocumentChange.Type.MODIFIED -> {
                        CoroutineScope(context = Dispatchers.IO).launch {
                            chatDao.updateChat(chat.toChatEntity(chatRoomId = chatRoomId))
                        }
                    }

                    DocumentChange.Type.REMOVED -> {
                        CoroutineScope(context = Dispatchers.IO).launch {
                            chatDao.deleteChat(chat.toChatEntity(chatRoomId = chatRoomId))
                        }
                    }
                }
            }
        }
        awaitClose {
            Log.d("ChatRepository", "removeListener")
            listenerRegistration.remove()
        }
    }

    override suspend fun sendMessage(chatRoomId: String, chat: Chat): Result<Unit> =
        kotlin.runCatching {
            val chatDTO = chat.toDTO()
            firestore.collection("Chat").document(chatRoomId).collection("Message").add(chatDTO)
                .await()
            Unit
        }.mapDomainError()
}