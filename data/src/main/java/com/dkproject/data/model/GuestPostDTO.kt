package com.dkproject.data.model

import com.dkproject.domain.model.GuestPost
import com.google.firebase.firestore.DocumentId
import java.util.Date

data class GuestPostDTO(
    @DocumentId val id: String?,
    val date: Date,
    val description: String,
    val endDate: Date,
    val lat: Double,
    val lng: Double,
    val memberCount: Int,
    val parkFlag: Int,
    val placeAddress: String,
    val placeName: String,
    val positions: List<String>,
    val startDate: Date,
    val title: String,
    val writerUid: String
)

fun GuestPostDTO.toDomain(): GuestPost {
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

fun GuestPost.toDTO(): GuestPostDTO {
    return GuestPostDTO(
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