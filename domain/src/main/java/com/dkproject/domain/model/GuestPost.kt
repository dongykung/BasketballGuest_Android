package com.dkproject.domain.model

import java.util.Date

data class GuestPost(
    val id: String?,
    val date: Date,
    val description: String,
    val endDate: Date,
    val lat: Double,
    val lng: Double,
    val memberCount: Int,
    val parkFlag: String,
    val placeAddress: String,
    val placeName: String,
    val positions: List<String>,
    val startDate: Date,
    val title: String,
    val writerUid: String
)