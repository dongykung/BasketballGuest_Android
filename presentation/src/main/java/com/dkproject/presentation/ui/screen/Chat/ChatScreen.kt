package com.dkproject.presentation.ui.screen.Chat

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.dkproject.domain.model.Chat.Chat
import com.dkproject.presentation.extension.toFormattedHourAndMinute
import com.dkproject.presentation.extension.toFormattedfullString
import com.dkproject.presentation.ui.component.Image.CustomImage
import com.dkproject.presentation.ui.component.Image.DefaultProfileImage
import com.dkproject.presentation.ui.theme.AppTheme
import java.util.Calendar
import java.util.Date

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
    chatList: LazyPagingItems<Chat>,
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

        Column(modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp)
            .imePadding()) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 6.dp),
                verticalAlignment = Alignment.CenterVertically) {
                BasicTextField(
                    value = chatMessage,
                    onValueChange = updateChatMessage,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp),
                    maxLines = 2,
                ) { innerTextField ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(22.dp))
                            .background(Color.LightGray)
                            .height(48.dp) // 여기서 고정 높이를 지정 (원하는 높이에 맞게 조절)
                            .padding(horizontal = 8.dp),
                        contentAlignment = Alignment.CenterStart // 텍스트를 왼쪽 정렬
                    ) {
                        innerTextField()
                    }
                }
                Button(onClick = onSendClick,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                    enabled = chatMessage.isNotEmpty()
                ) {
                    Text("전송")
                }
            }
        }
    }
}

@Composable
fun ChatList(
    chatList: LazyPagingItems<Chat>,
    otherUserUid: String,
    modifier: Modifier = Modifier
) {
//    val groupedChats = chatList.groupBy { chat ->
//        // 날짜별로 그룹화: 하루의 시작 시각으로 계산
//        Calendar.getInstance().apply {
//            time = chat.createAt
//            set(Calendar.HOUR_OF_DAY, 0)
//            set(Calendar.MINUTE, 0)
//            set(Calendar.SECOND, 0)
//            set(Calendar.MILLISECOND, 0)
//        }.time
//    }
    val listState = rememberLazyListState()
    LaunchedEffect(chatList.itemCount) {
        Log.d("ChatList", "ChatList: ${chatList.itemCount}")
        listState.animateScrollToItem(0)
    }
    LazyColumn(modifier = modifier, reverseLayout = true, state = listState) {
            items(chatList.itemCount, key = { chatList.peek(it)?.id ?: it }) {
                val chat = chatList[it] ?: return@items
                if(chat.sender == otherUserUid) {
                    OtherUserChat(chat = chat, modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 6.dp, vertical = 8.dp))
                } else {
                    MyChat(chat = chat, otherUserUid = otherUserUid, modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 6.dp, vertical = 8.dp))
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 6.dp, vertical = 8.dp)
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 6.dp, vertical = 8.dp)
                )
            }
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 6.dp),
                        verticalAlignment = Alignment.CenterVertically) {
                        BasicTextField(
                            value = "",
                            onValueChange = {},
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 8.dp),
                            maxLines = 2,
                            ) { innerTextField ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(22.dp))
                                    .background(Color.LightGray)
                                    .height(48.dp) // 여기서 고정 높이를 지정 (원하는 높이에 맞게 조절)
                                    .padding(horizontal = 8.dp),
                                contentAlignment = Alignment.CenterStart // 텍스트를 왼쪽 정렬
                            ) {
                                innerTextField()
                            }
                        }

                        Button(onClick = {  },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                            enabled = "f".isNotEmpty()
                        ) {
                            Text("전송")
                        }
                    }
                }
            }
        }

    }
}

