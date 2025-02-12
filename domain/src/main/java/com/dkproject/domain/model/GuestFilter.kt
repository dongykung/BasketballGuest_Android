package com.dkproject.domain.model

import java.util.Date

data class GuestFilter(
    var isNearBy: Boolean = false,
    var myLocation: Coordinate? = null,
    var radiusInMeters: Double = 10000.0, // 10km
    var selectedDate: Date?,
    var selectedPosition: List<String> = emptyList(),
    var limit: Int = 10
)

data class Coordinate(val latitude: Double, val longitude: Double)
