package com.dkproject.presentation.util

import com.dkproject.presentation.model.GuestPostUiModel
import java.util.Date

val guestPostDummy =
    GuestPostUiModel(
        id = "",
        date = Date(),
        description = "안양 짱농구에서 어린이 농구 코치 구합니다~우대사항: 생활체육 지도사 자격증오전 10시 ~ 12시 월 수 금 클래스 입니다~",
        endDate = Date(),
        lat = 0.0,
        lng = 0.0,
        memberCount = 3,
        parkFlag = 1,
        placeAddress = "경기 안양시 동안구 평촌대로 389",
        placeName = "안양정관장아레나",
        positions = listOf("스몰 포워드", "슈팅 가드"),
        startDate = Date(),
        title = "안양 짱농구에서 코치 구합니다~",
        writerUid = ""
)