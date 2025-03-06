package com.dkproject.data.data.local

import androidx.room.TypeConverter
import com.dkproject.data.model.ChatUserInfoEntity
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.Date

class Converter {
    private val json = Json { ignoreUnknownKeys = true }

    // List<String> 변환
    @TypeConverter
    fun fromStringList(list: List<String>?): String? {
        return list?.let { json.encodeToString(it) }
    }

    @TypeConverter
    fun toStringList(value: String?): List<String>? {
        return value?.let { json.decodeFromString(it) }
    }

    // Map<String, Int> 변환
    @TypeConverter
    fun fromIntMap(map: Map<String, Int>?): String? {
        return map?.let { json.encodeToString(it) }
    }

    @TypeConverter
    fun toIntMap(value: String?): Map<String, Int>? {
        return value?.let { json.decodeFromString(it) }
    }

    // Map<String, ChatUserInfoEntity> 변환
    @TypeConverter
    fun fromChatUserInfoMap(map: Map<String, ChatUserInfoEntity>?): String? {
        return map?.let { json.encodeToString(it) }
    }

    @TypeConverter
    fun toChatUserInfoMap(value: String?): Map<String, ChatUserInfoEntity>? {
        return value?.let { json.decodeFromString(it) }
    }

    // Map<String, Date> 변환: Date를 Long으로 변환
    @TypeConverter
    fun fromDateMap(map: Map<String, Date>?): String? {
        return map?.mapValues { it.value.time }?.let { json.encodeToString(it) }
    }

    @TypeConverter
    fun toDateMap(value: String?): Map<String, Date>? {
        return value?.let { str ->
            val longMap: Map<String, Long> = json.decodeFromString(str)
            longMap.mapValues { Date(it.value) }
        }
    }

    // Date 변환 (개별 필드)
    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun toDate(value: Long?): Date? {
        return value?.let { Date(it) }
    }
}