package com.dkproject.domain.model

import com.dkproject.domain.Error.ErrorType

sealed class DataState<out T> {
    data object Loading : DataState<Nothing>()
    data class Success<T>(val data: T) : DataState<T>()
    data class Error(val message: String) : DataState<Nothing>()
}

sealed class UnitResult {
    data object Success : UnitResult()
    data class Error(val errorType: ErrorType) : UnitResult()
}