package com.dkproject.data.util

import com.dkproject.domain.model.Coordinate
import kotlin.math.PI
import kotlin.math.cos

data class CoordinateBounds(
    val minLatitude: Double,
    val maxLatitude: Double,
    val minLongitude: Double,
    val maxLongitude: Double,
)


fun calculateCoordinateBounds(center: Coordinate, radiusInMeters: Double): CoordinateBounds {
    val earthRadius = 6371.0 // Earth's radius in kilometers
    val radiusInKm = radiusInMeters / 1000.0

    val deltaLatitude = (radiusInKm / earthRadius) * (180.0 / PI)
    val deltaLongitude = (radiusInKm / earthRadius) * (180.0 / PI) / cos(center.latitude * PI / 180)

    val minLat = center.latitude - deltaLatitude
    val maxLat = center.latitude + deltaLatitude
    val minLng = center.longitude - deltaLongitude
    val maxLng = center.longitude + deltaLongitude

    return CoordinateBounds(minLatitude = minLat, maxLatitude =  maxLat, minLongitude = minLng, maxLongitude = maxLng)
}