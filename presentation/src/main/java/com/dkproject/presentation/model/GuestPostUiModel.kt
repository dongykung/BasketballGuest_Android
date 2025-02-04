package com.dkproject.presentation.model

import java.util.Date

data class GuestPostUiModel(
    val id: String? = null,
    val date: Date = Date(),
    val description: String = "",
    val endDate: Date = Date(),
    val lat: Double = 0.0,
    val lng: Double = 0.0,
    val memberCount: Int = 0,
    val parkFlag: String = "",
    val placeAddress: String = "",
    val placeName: String = "",
    val positions: List<String> = emptyList(),
    val startDate: Date = Date(),
    val title: String = "",
    val writerUid: String = ""
)