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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.dkproject.domain.model.Chat.Chat
import com.dkproject.presentation.extension.toFormattedHourAndMinute
import com.dkproject.presentation.extension.toFormattedfullString
import com.dkproject.presentation.ui.component.Image.CustomImage
import com.dkproject.presentation.ui.component.Image.DefaultProfileImage
import com.dkproject.presentation.ui.component.util.FooterErrorScreen
import com.dkproject.presentation.ui.component.util.LoadingScreen
import com.dkproject.presentation.ui.theme.AppTheme
import java.util.Calendar
import java.util.Date
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    viewModel: ChatViewModel,
    otherUserUid: String,
    otherUserNickname: String,
    otherUserProfile: String,
    onBackClick: () -> Unit,
    onSendClick: () -> Unit,
    updateChatMessage: (String) -> Unit,
    fetchedLastMessages: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    var hasUpdatedLastFetched by rememberSaveable { mutableStateOf(false) }
    val uiState by viewModel.uiState.collectAsState()
    val chatList = uiState.chatList.collectAsLazyPagingItems()
    LaunchedEffect(chatList.loadState.refresh) {
        if (chatList.loadState.refresh is LoadState.NotLoading&&!hasUpdatedLastFetched) {
            hasUpdatedLastFetched = true
            val lastFetched = chatList.peek(0)?.createAt?.time ?: 0L
            fetchedLastMessages(lastFetched)
            Log.d("dk", "$lastFetched")
        }
    }
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
            newChatList = uiState.newChatList,
            chatList = chatList,
            otherUserUid = otherUserUid,
            modifier = Modifier.weight(1f)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 6.dp)
                .imePadding()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicTextField(
                    value = uiState.chatMessage,
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
                Button(
                    onClick = onSendClick,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                    enabled = uiState.chatMessage.isNotEmpty()
                ) {
                    Text("전송")
                }
            }
        }
    }
}

@Composable
fun ChatList(
    newChatList: List<Chat>,
    chatList: LazyPagingItems<Chat>,
    otherUserUid: String,
    modifier: Modifier = Modifier
) {

    val listState = rememberLazyListState()
    LaunchedEffect(newChatList.size) {
        listState.animateScrollToItem(0)
    }
    LazyColumn(modifier = modifier, reverseLayout = true, state = listState) {
        items(newChatList, key = { it.id!! }) { chat ->
            if (chat.sender == otherUserUid) {
                OtherUserChat(
                    chat = chat, modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 6.dp, vertical = 8.dp)
                )
            } else {
                MyChat(
                    chat = chat, otherUserUid = otherUserUid, modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 6.dp, vertical = 8.dp)
                )
            }
        }
        items(chatList.itemCount, key = { chatList[it]?.id ?: it }) {
            val chat = chatList[it] ?: return@items
            if (chat.sender == otherUserUid) {
                OtherUserChat(
                    chat = chat, modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 6.dp, vertical = 8.dp)
                )
            } else {
                MyChat(
                    chat = chat, otherUserUid = otherUserUid, modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 6.dp, vertical = 8.dp)
                )
            }
        }
//            item {
//                Row(modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(vertical = 8.dp), horizontalArrangement = Arrangement.Center) {
//                    Surface(shape = RoundedCornerShape(16.dp), color = Color.LightGray) {
//                        Text(text = date.toFormattedfullString(),
//                            style = MaterialTheme.typography.bodyMedium,
//                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
//                        )
//                    }
//                }
//            }
        item {
            when (chatList.loadState.append) {
                is LoadState.Error -> FooterErrorScreen(
                    modifier = Modifier.fillMaxWidth(),
                    retryAction = { chatList.retry() }
                )

                LoadState.Loading -> {
                    LoadingScreen(modifier = Modifier.fillMaxWidth())
                }

                is LoadState.NotLoading -> {}
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
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.Bottom
    ) {
        Column(verticalArrangement = Arrangement.Center, modifier = Modifier.fillMaxHeight()) {
            if (!chat.readBy.contains(otherUserUid)) {
                Text(
                    text = "1",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.align(Alignment.End)
                )
            }
            Text(
                chat.createAt.toFormattedHourAndMinute(),
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
        Spacer(modifier = Modifier.width(3.dp))
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
        ) {
            Text(
                text = chat.message,
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
fun OtherUserChat(
    chat: Chat,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Start
    ) {
        Surface(shape = RoundedCornerShape(16.dp), color = Color.LightGray) {
            Text(
                text = chat.message,
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                style = MaterialTheme.typography.bodyLarge
            )
        }
        Spacer(modifier = Modifier.width(3.dp))
        Text(
            chat.createAt.toFormattedHourAndMinute(),
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
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp, horizontal = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
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

                        Button(
                            onClick = { },
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

