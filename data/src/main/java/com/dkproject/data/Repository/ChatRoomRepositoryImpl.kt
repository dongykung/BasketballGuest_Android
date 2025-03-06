package com.dkproject.data.Repository

import android.util.Log
import com.dkproject.data.data.local.ChatRoomDao
import com.dkproject.data.model.ChatDTO
import com.dkproject.data.model.ChatRoomDTO
import com.dkproject.data.model.toDTO
import com.dkproject.data.model.toEntity
import com.dkproject.domain.Error.ErrorType
import com.dkproject.domain.model.Chat.ChatRoom
import com.dkproject.domain.model.UnitResult
import com.dkproject.domain.repository.ChatRoomRepository
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
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

class ChatRoomRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val chatRoomDao: ChatRoomDao
): ChatRoomRepository {
    override fun getChatRooms(): Flow<List<ChatRoom>> {
        return chatRoomDao.getChatRooms()
            .map { list -> list.map { it.toDomain() } }
            .flowOn(context = Dispatchers.IO)
    }

    override suspend fun getChatRoomById(chatRoomId: String): ChatRoom? {
        return chatRoomDao.getChatRoomById(chatRoomId = chatRoomId)?.toDomain()
    }

    override fun listenToChatRooms(myUid: String): Flow<Unit>  = callbackFlow {
        val query = firestore.collection("Chat").whereArrayContains("participant", myUid)
        val listenerRegistration = query.addSnapshotListener { snapshot, error ->
            if (error != null || snapshot == null) {
                trySend(Unit)
                return@addSnapshotListener
            }
            for(diff in snapshot.documentChanges) {
                val chatRoomDTO = diff.document.toObject(ChatRoomDTO::class.java).copy(id = diff.document.id) ?: continue
                when(diff.type) {
                    DocumentChange.Type.ADDED -> {
                        CoroutineScope(context = Dispatchers.IO).launch {
                            val myUnreadCount = getCountUnReadMessage(uid = myUid, chatRoomId = diff.document.id, date = chatRoomDTO.readStatus[myUid] ?: Date())
                            val otherUserUid = chatRoomDTO.participant.first { it != myUid }
                            val otherUserUnreadCount = getCountUnReadMessage(uid = otherUserUid, chatRoomId = diff.document.id, date = chatRoomDTO.readStatus[otherUserUid] ?: Date())
                            val chatEntity = chatRoomDTO.toChatRoomEntitiy()
                            chatEntity.unReadCount = mapOf(myUid to myUnreadCount, otherUserUid to otherUserUnreadCount)
                            chatRoomDao.insertChatRoom(chatEntity)
                        }
                    }
                    DocumentChange.Type.MODIFIED -> {
                        CoroutineScope(context = Dispatchers.IO).launch {
                            val myUnreadCount = getCountUnReadMessage(uid = myUid, chatRoomId = diff.document.id, date = chatRoomDTO.readStatus[myUid] ?: Date())
                            val otherUserUid = chatRoomDTO.participant.first { it != myUid }
                            val otherUserUnreadCount = getCountUnReadMessage(uid = otherUserUid, chatRoomId = diff.document.id, date = chatRoomDTO.readStatus[otherUserUid] ?: Date())
                            val chatEntity = chatRoomDTO.toChatRoomEntitiy()
                            chatEntity.unReadCount = mapOf(myUid to myUnreadCount, otherUserUid to otherUserUnreadCount)
                            chatRoomDao.updateChatRoom(chatEntity)
                        }
                    }
                    DocumentChange.Type.REMOVED -> {
                        CoroutineScope(context = Dispatchers.IO).launch {
                            chatRoomDao.deleteChatRoom(chatRoomDTO.toChatRoomEntitiy())
                        }
                    }
                }
            }
        }
        awaitClose {
            Log.d("ChatROomRepository", "removeListener")
            listenerRegistration.remove()
        }
    }

    override suspend fun createChatRoom(chatRoom: ChatRoom): UnitResult {
        try {
            firestore.collection("Chat").document(chatRoom.id).set(chatRoom.toDTO()).await()
            return UnitResult.Success
        } catch (e: Exception) {
            return UnitResult.Error(ErrorType.UNKNOWN_ERROR)
        }
    }

    override suspend fun updateChatRoomData(
        myUid: String,
        chatRoomId: String,
        lastMessage: String,
        date: Date
    ): UnitResult {
        try {
            val batch = firestore.batch()
            val chatRoomRef = firestore.collection("Chat").document(chatRoomId)
            batch.update(chatRoomRef, "readStatus.$myUid", date)
            batch.update(chatRoomRef, "lastMessage", lastMessage)
            batch.update(chatRoomRef, "lastMessageAt", date)
            batch.commit().await()
            return UnitResult.Success
        } catch (e:Exception) {
            return UnitResult.Error(ErrorType.UNKNOWN_ERROR)
        }
    }

    override suspend fun getCountUnReadMessage(uid: String, chatRoomId: String, date: Date): Int {
        try {
            val snapshots = firestore.collection("Chat").document(chatRoomId).collection("Message")
                .whereGreaterThan("createAt", date).get().await().documents
            val count = snapshots.mapNotNull { it.toObject(ChatDTO::class.java) }
                .count { it.sender != uid && !it.readBy.contains(uid) }
            return count
        } catch (e: Exception) {
            throw e
        }
    }
}

