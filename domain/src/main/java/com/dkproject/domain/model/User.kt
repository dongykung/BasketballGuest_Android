package com.dkproject.domain.model

data class User(
    val id: String = "",
    val nickName: String = "",
    val position: List<String> = emptyList(),
    val weight: Double? = null,
    val height: Double? = null,
    val profileImageUrl: String = ""
)