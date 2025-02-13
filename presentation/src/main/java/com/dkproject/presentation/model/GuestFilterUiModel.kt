package com.dkproject.presentation.model

import com.dkproject.domain.model.Coordinate
import com.dkproject.domain.model.GuestFilter
import java.util.Date

data class GuestFilterUiModel(
    var isNearBy: Boolean = false,
    var myLocation: Coordinate? = null,
    var radiusInMeters: Double = 10000.0, // 10km
    var selectedDate: Date?,
    var selectedPosition: List<Position> = emptyList(),
    var limit: Int = 10
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