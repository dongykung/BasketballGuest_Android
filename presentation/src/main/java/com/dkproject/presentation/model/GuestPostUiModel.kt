package com.dkproject.presentation.model

import com.dkproject.domain.model.GuestPost
import java.util.Date

data class GuestPostUiModel(
    val id: String? = null,
    val date: Date = Date(),
    val description: String = "",
    val endDate: Date = Date(),
    val lat: Double = 0.0,
    val lng: Double = 0.0,
    val memberCount: Int = 0,
    val parkFlag: Int = 0,
    val placeAddress: String = "",
    val placeName: String = "",
    val positions: List<String> = emptyList(),
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