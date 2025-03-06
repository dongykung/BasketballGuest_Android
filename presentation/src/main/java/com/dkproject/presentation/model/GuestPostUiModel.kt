package com.dkproject.presentation.model

import com.dkproject.domain.model.Guest.GuestPost
import com.dkproject.presentation.util.DateSerializer
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class GuestPostUiModel(
    val id: String? = null,
    @Serializable(with = DateSerializer::class)
    val date: Date = Date(),
    val description: String = "",
    @Serializable(with = DateSerializer::class)
    val endDate: Date = Date(),
    val lat: Double = 0.0,
    val lng: Double = 0.0,
    val currentMember: Int = 0,
    val memberCount: Int = 1,
    val parkFlag: Int = 0,
    val placeAddress: String = "",
    val placeName: String = "",
    val positions: List<String> = emptyList(),
    @Serializable(with = DateSerializer::class)
    val startDate: Date = Date(),
    val title: String = "",
    val writerUid: String = ""
)

fun GuestPostUiModel.toDomainModel(): GuestPost {
    return GuestPost(
        id = id,
        date = date,
        description = description,
        endDate = endDate,
        lat = lat,
        lng = lng,
        currentMember = currentMember,
        memberCount = memberCount,
        parkFlag = parkFlag,
        placeAddress = placeAddress,
        placeName = placeName,
        positions = positions,
        startDate = startDate,
        title = title,
        writerUid = writerUid
    )
}

fun GuestPost.toUiModel(): GuestPostUiModel {
    return GuestPostUiModel(
        id = id,
        date = date,
        description = description,
        endDate = endDate,
        lat = lat,
        lng = lng,
        currentMember = currentMember,
        memberCount = memberCount,
        parkFlag = parkFlag,
        placeAddress = placeAddress,
        placeName = placeName,
        positions = positions,
        startDate = startDate,
        title = title,
        writerUid = writerUid
    )
}