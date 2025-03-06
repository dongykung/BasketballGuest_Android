package com.dkproject.domain.model.User

data class User(
    val id: String = "",
    val nickName: String = "",
    val position: List<String> = emptyList(),
    val weight: Int? = 175,
    val height: Int? = 85,
    val profileImageUrl: String = ""
)