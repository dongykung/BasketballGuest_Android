package com.dkproject.presentation.ui.screen.Chat

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dkproject.domain.model.Chat.Chat
import com.dkproject.presentation.extension.toFormattedHourAndMinute
import com.dkproject.presentation.extension.toFormattedfullString
import com.dkproject.presentation.ui.component.Image.CustomImage
import com.dkproject.presentation.ui.component.Image.DefaultProfileImage
import com.dkproject.presentation.ui.theme.AppTheme
import java.util.Calendar
import java.util.Date
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    otherUserUid: String,
    otherUserNickname: String,
    otherUserProfile: String,
    chatMessage: String,
    onBackClick: () -> Unit,
    onSendClick: () -> Unit,
    updateChatMessage: (String) -> Unit,
    chatList: List<Chat>,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        CenterAlignedTopAppBar(title = {
            ChatTopAppBar(
                otherUserProfile = otherUserProfile,
                otherUserNickname = otherUserNickname
            )
        }, navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(Icons.AutoMirrored.Default.ArrowBack, null)
            }
        })
        ChatList(
            chatList = chatList,
            otherUserUid = otherUserUid,
            modifier = Modifier.weight(1f)
        )

        Row(modifier = Modifier.fillMaxWidth()) {
            TextField(
                value = chatMessage,
                onValueChange = updateChatMessage,
                modifier = Modifier.weight(1f),
                maxLines = 2
            )
            Button(onClick = onSendClick) {
                Text("전송")
            }
        }
    }
}

@Composable
fun ChatList(
    chatList: List<Chat>,
    otherUserUid: String,
    modifier: Modifier = Modifier
) {
    val groupedChats = chatList.groupBy { chat ->
        // 날짜별로 그룹화: 하루의 시작 시각으로 계산
        Calendar.getInstance().apply {
            time = chat.createAt
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.time
    }
    LazyColumn(modifier = modifier, reverseLayout = true) {
        groupedChats.forEach { (date, chats) ->
            items(chats.sortedByDescending { it.createAt }) { chat ->
                if(chat.sender == otherUserUid) {
                    OtherUserChat(chat = chat, modifier = Modifier.fillMaxWidth().padding(horizontal = 6.dp, vertical = 8.dp))
                } else {
                    MyChat(chat = chat, otherUserUid = otherUserUid, modifier = Modifier.fillMaxWidth().padding(horizontal = 6.dp, vertical = 8.dp))
                }
            }
            stickyHeader {
                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.Center) {
                   Surface(shape = RoundedCornerShape(16.dp), color = Color.LightGray) {
                       Text(text = date.toFormattedfullString(),
                           style = MaterialTheme.typography.bodyMedium,
                           modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                           )
                   }
                }
            }
        }
    }
}

@Composable
fun ChatTopAppBar(
    otherUserProfile: String,
    otherUserNickname: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        if (otherUserProfile.isEmpty()) {
            DefaultProfileImage(modifier = Modifier.size(42.dp))
        } else {
            CustomImage(
                url = otherUserProfile,
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
            )
        }
        Spacer(modifier = Modifier.padding(horizontal = 12.dp))
        Text(text = otherUserNickname, style = MaterialTheme.typography.titleSmall)
    }
}

@Composable
fun MyChat(
    chat: Chat,
    otherUserUid: String,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier,
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.Bottom) {
        Column(verticalArrangement = Arrangement.Center, modifier = Modifier.fillMaxHeight()) {
            if(!chat.readBy.contains(otherUserUid)) {
                Text(text = "1", style = MaterialTheme.typography.bodySmall, modifier = Modifier.align(Alignment.End))
            }
            Text(chat.createAt.toFormattedHourAndMinute(), style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        }
        Spacer(modifier = Modifier.width(3.dp))
        Surface(shape = RoundedCornerShape(16.dp), color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)) {
            Text(text = chat.message, modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
fun OtherUserChat(
    chat: Chat,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier,
        horizontalArrangement = Arrangement.Start) {
        Surface(shape = RoundedCornerShape(16.dp), color = Color.LightGray) {
            Text(text = chat.message, modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                style = MaterialTheme.typography.bodyLarge
                )
        }
        Spacer(modifier = Modifier.width(3.dp))
        Text(chat.createAt.toFormattedHourAndMinute(),
            modifier = Modifier.align(Alignment.Bottom),
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun PreviewMyChat() {
    AppTheme {
        LazyColumn {
            item {
                MyChat(
                    chat = Chat(
                        id = "",
                        message = "안녕하세요\n adsㅇㅎㅁㄴㅇ;리ㅓ;ㅣㅁㅇ널",
                        createAt = Date(),
                        sender = "d",
                        readBy = listOf("d")
                    ), otherUserUid = "d",
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 6.dp, vertical = 8.dp)
                )
            }
            item {
                OtherUserChat(
                    chat = Chat(
                        id = "",
                        message = "안녕하세요ㅇㄹㅁㄴㅇㄹㄴㅁㅇㄹ\n adf",
                        createAt = Date(),
                        sender = "",
                        readBy = emptyList()
                    ),
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 6.dp, vertical = 8.dp)
                )
            }
        }

    }
}