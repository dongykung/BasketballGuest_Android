package com.dkproject.presentation.extension

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun Date.toFormattedfullString(): String {
    val formatter = SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault())
    return formatter.format(this)
}

fun Date.toFormattedHomeGuestListString(): String {
    val formatter = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
    return formatter.format(this)
}

fun Date.toFormattedFilterDate(): String {
    val formatter = SimpleDateFormat("MM.dd", Locale.getDefault())
    return formatter.format(this)
}

fun Date.toFormattedHourAndMinute(): String {
    val formatter = SimpleDateFormat("a h시 mm분", Locale.getDefault())
    return formatter.format(this)
}

fun Date.withTime(hour: Int, minute: Int, second: Int = 0, millisecond: Int = 0): Date {
    val calendar = Calendar.getInstance().apply {
        time = this@withTime
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, minute)
        set(Calendar.SECOND, second)
        set(Calendar.MILLISECOND, millisecond)
    }
    return calendar.time
}

/// 날짜 변경 시 startTime, endTime 해당 날짜로 변경
fun Date.combineWithTimeFrom(timeSource: Date): Date {
    val baseCal = Calendar.getInstance().apply { time = this@combineWithTimeFrom }
    val timeCal = Calendar.getInstance().apply { time = timeSource }
    baseCal.set(Calendar.YEAR, timeCal.get(Calendar.YEAR))
    baseCal.set(Calendar.MONTH, timeCal.get(Calendar.MONTH))
    baseCal.set(Calendar.DAY_OF_MONTH, timeCal.get(Calendar.DAY_OF_MONTH))
    return baseCal.time
}

fun startTimeWithEndTime(startTime: Date, endTime: Date): String {
    val formatter = SimpleDateFormat("a h:mm", Locale.getDefault())
    val formatter2 = SimpleDateFormat("h:mm", Locale.getDefault())
    return formatter.format(startTime) + " ~ " + formatter2.format(endTime)
}


fun Date.toFormattedChatRoomDate(): String {
    val now = Calendar.getInstance()
    val cal = Calendar.getInstance().apply { time = this@toFormattedChatRoomDate }

    if (now.get(Calendar.YEAR) == cal.get(Calendar.YEAR) &&
        now.get(Calendar.DAY_OF_YEAR) == cal.get(Calendar.DAY_OF_YEAR)) {
        val timeFormat = SimpleDateFormat("a h시 m분", Locale("ko", "KR"))
        return timeFormat.format(this)
    }

    val yesterday = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }
    if (cal.get(Calendar.YEAR) == yesterday.get(Calendar.YEAR) &&
        cal.get(Calendar.DAY_OF_YEAR) == yesterday.get(Calendar.DAY_OF_YEAR)) {
        return "어제"
    }
    return if (cal.get(Calendar.YEAR) == now.get(Calendar.YEAR)) {
        val formatter = SimpleDateFormat("M월 d일", Locale("ko", "KR"))
        formatter.format(this)
    } else {
        val formatter = SimpleDateFormat("yyyy.MM.dd", Locale("ko", "KR"))
        formatter.format(this)
    }
}
