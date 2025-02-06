package com.dkproject.presentation.model

import androidx.annotation.StringRes
import com.dkproject.presentation.R

enum class Position(@StringRes val labelRes: Int) {
    POINT_GUARD(R.string.point_guard),
    SHOOTING_GUARD(R.string.shooting_guard),
    SMALL_FORWARD(R.string.small_forward),
    POWER_FORWARD(R.string.power_forward),
    CENTER(R.string.center),
    NONE(R.string.positionnone);

    fun toFirestoreValue(): String {
        return when (this) {
            POINT_GUARD -> "포인트 가드"
            SHOOTING_GUARD -> "슈팅 가드"
            SMALL_FORWARD -> "스몰 포워드"
            POWER_FORWARD -> "파워 포워드"
            CENTER -> "센터"
            NONE -> "무관"
        }
    }

    companion object {
        /**
         * Firestore에 저장된 문자열 값을 기반으로 Position enum을 반환합니다.
         * 해당 문자열과 매칭되는 enum 값이 없으면 null을 반환합니다.
         */
        fun fromFirestoreValue(value: String): Position {
            return when (value) {
                "포인트 가드" -> POINT_GUARD
                "슈팅 가드" -> SHOOTING_GUARD
                "스몰 포워드" -> SMALL_FORWARD
                "파워 포워드" -> POWER_FORWARD
                "센터" -> CENTER
                else-> NONE
            }
        }
    }
}