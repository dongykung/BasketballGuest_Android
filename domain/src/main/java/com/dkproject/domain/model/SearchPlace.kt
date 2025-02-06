package com.dkproject.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class SearchPlace(
    val searchPoiInfo: SearchPoiInfo
)
@Serializable
data class SearchPoiInfo(
    val totalCount: Int,
    val count: Int,
    val page: Int,
    val pois: Pois
)
@Serializable
data class Pois(
    val poi: List<Poi>
)
@Serializable
data class Poi(
    val id: String,
    val pkey: String,
    val name: String,
    val noorLat: Double,
    val noorLon: Double,
    val upperAddrName: String,
    val middleAddrName: String,
    val roadName: String,
    val firstBuildNo: String,
    val parkFlag: Int?,
) {
    val detailAddress: String = "$upperAddrName $middleAddrName $roadName $firstBuildNo"
}
