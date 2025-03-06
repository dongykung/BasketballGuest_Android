package com.dkproject.presentation.ui.screen.ChatRoom

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dkproject.domain.model.Chat.ChatRoom
import com.dkproject.domain.model.Chat.ChatUserInfo
import com.dkproject.presentation.R
import com.dkproject.presentation.extension.toFormattedChatRoomDate
import com.dkproject.presentation.navigation.Screen
import com.dkproject.presentation.ui.component.Image.CustomImage
import com.dkproject.presentation.ui.component.Image.DefaultProfileImage
import com.dkproject.presentation.ui.theme.AppTheme
import com.google.firebase.auth.FirebaseAuth
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatRoomScreen(
    chatRoomList: List<ChatRoom>,
    navigateToChat: (Screen.Chat) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    Column(modifier = modifier) {
        TopAppBar(title = { Text(text = stringResource(R.string.chat)) })
        LazyColumn(modifier = Modifier.padding(vertical = 12.dp)) {
            items(chatRoomList) { chatRoom ->
                ChatRoomItem(
                    chatRoom = chatRoom,
                    myUid = auth.currentUser?.uid ?: "",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable {
                            val otherUserUid =
                                chatRoom.participant.first { it != auth.currentUser?.uid }
                            val otherUserData = chatRoom.participantsInfo[otherUserUid]
                            otherUserData?.run {
                                navigateToChat(
                                    Screen.Chat(
                                        chatRoomId = chatRoom.id,
                                        otherUserUid = otherUserUid,
                                        otherUserName = otherUserData.nickname,
                                        otherProfileUrl = otherUserData.profileUrl
                                    )
                                )
                            }
                        }
                )
            }
        }
    }
}

@Composable
fun ChatRoomItem(
    chatRoom: ChatRoom,
    myUid: String,
    modifier: Modifier = Modifier
) {
    val otherUserUid = chatRoom.participant.first { it != myUid }
    val otherUserInfo = chatRoom.participantsInfo[otherUserUid]
    otherUserInfo?.run {
        Box(modifier = modifier) {
            Row(
                modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp),
                verticalAlignment = Alignment.Top
            ) {
                if (otherUserInfo.profileUrl.isEmpty()) {
                    DefaultProfileImage(modifier = Modifier.size(64.dp))
                } else {
                    CustomImage(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape),
                        url = otherUserInfo.profileUrl
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(verticalArrangement = Arrangement.Center) {
                    Text(text = otherUserInfo.nickname, style = MaterialTheme.typography.titleSmall)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = chatRoom.lastMessage,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Column(verticalArrangement = Arrangement.Top, modifier = Modifier.fillMaxHeight()) {
                    Text(
                        text = chatRoom.lastMessageAt.toFormattedChatRoomDate(),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    val unReadCount = chatRoom.unReadCount[myUid] ?: 0
                    if (unReadCount > 0)
                        Surface(
                            shape = CircleShape,
                            color = Color.Red,
                            modifier = Modifier
                                .size(18.dp)
                                .align(Alignment.End)
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(
                                    text = "$unReadCount",
                                    color = Color.White,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun PreviewChatRoomItem() {
    AppTheme {
        LazyColumn {
            item {
                ChatRoomItem(
                    chatRoom = ChatRoom(
                        id = "",
                        lastMessage = "안녕하세요",
                        lastMessageAt = Date(),
                        participant = listOf("a", "b"),
                        readStatus = mapOf("a" to Date(), "b" to Date()),
                        participantsInfo = mapOf(
                            "a" to ChatUserInfo(
                                nickname = "동경a",
                                profileUrl = ""
                            ), "b" to ChatUserInfo(nickname = "동경b", profileUrl = "")
                        ),
                        unReadCount = mapOf("a" to 10, "b" to 0)
                    ),
                    myUid = "a",
                )
            }
        }
    }
}