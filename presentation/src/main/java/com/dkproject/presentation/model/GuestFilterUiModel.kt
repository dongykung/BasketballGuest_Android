package com.dkproject.presentation.model

import com.dkproject.domain.model.Guest.Coordinate
import com.dkproject.domain.model.Guest.GuestFilter
import java.util.Date

data class GuestFilterUiModel(
    val isNearBy: Boolean = false,
    val myLocation: Coordinate? = null,
    val radiusInMeters: Double = 10000.0, // 10km
    val selectedDate: Date? = null,
    val selectedPosition: List<Position> = emptyList(),
    val limit: Int = 10
) {
    fun toDomain(): GuestFilter {
        return GuestFilter(
            isNearBy = isNearBy,
            myLocation = myLocation,
            radiusInMeters = radiusInMeters,
            selectedDate = selectedDate,
            selectedPosition = selectedPosition.map { it.toFirestoreValue() },
            limit = limit
        )
    }
}