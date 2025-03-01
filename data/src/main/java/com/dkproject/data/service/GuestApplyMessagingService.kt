package com.dkproject.data.service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.util.Log
import androidx.paging.LOG_TAG
import com.dkproject.data.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import javax.inject.Inject

class GuestApplyMessagingService: FirebaseMessagingService() {
    companion object {
        const val GUEST_CHANNEL_ID = "GuestApplyChannelId"
        const val CHAT_CHANNEL_ID = "ChatChannelId"
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val title = message.notification?.title ?: "기본 제목"
        val body = message.notification?.body ?: "기본 본문"
        Log.d("message", message.data.toString())
        Log.d("message", body)

        createChannel(title, body, GUEST_CHANNEL_ID)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    private fun createChannel(title: String, messageBody: String, channelId: String) {
        val notificationId = System.currentTimeMillis().toInt() // 고유한 알림 ID 생성
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val notificationBuilder = Notification.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.baseline_sports_basketball_24)
            .setContentTitle(title)
            .setContentTitle(messageBody)
            .setAutoCancel(true)

        val channelName = when(channelId) {
            GUEST_CHANNEL_ID -> "게스트 신청 알림"
            CHAT_CHANNEL_ID -> "채팅 알림"
            else -> "기본 알림"
        }
        val channelDescription = when(channelId) {
            GUEST_CHANNEL_ID -> "게스트 신청 관련 알림"
            CHAT_CHANNEL_ID -> "채팅 메시지 관련 알림"
            else -> "기본 알림 채널"
        }
        val channel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = channelDescription
        }
        notificationManager.createNotificationChannel(channel)
        notificationManager.notify(notificationId, notificationBuilder.build())
    }

}